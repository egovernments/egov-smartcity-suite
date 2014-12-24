#UP

UPDATE EG_SCRIPT 
SET SCRIPT=
'from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov import EGOVException  
validationErrors=None  
result=None  
wfmatrix=None  
if wfItem.getIsSpillOverWorks():  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
else:  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getExecutingDepartment().getDeptName(),wfItem.getAmountWfRule(),wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    

if((action.getName()==''Approve'' or action.getName()==''Forward'') and (wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction'' or (wfItem.getCurrentState().getValue()==''TECH_SANCTION_CHECKED'' and wfmatrix.getFromQty().doubleValue()==0))  and wfmatrix.getAdditionalRule()==''budgetApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverBudgetApp''):  
	try:  
		if(wfItem.getIsBudgetCheckRequired()):
			if(wfItem.getTotalAmount().getValue() > persistenceService.getBudgetAvailable(wfItem).doubleValue()):  
				raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.budget.amount'',''abstractEstimate.estimate.validate.budget.amount'')] 
	except ValidationException,e:  
		result=None  
		validationErrors=e.getErrors()  
if((action.getName()==''Approve'' or action.getName()==''Forward'') and (wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction'' or (wfItem.getCurrentState().getValue()==''TECH_SANCTION_CHECKED'' and wfmatrix.getFromQty().doubleValue()==0))  and wfmatrix.getAdditionalRule()==''depositCodeApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverDepositCodeApp''):  
	try:  
		if(wfItem.getTotalAmount().getValue() > persistenceService.getDepositWorksBalance(wfItem).doubleValue()):  
			raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.deposit.amount'',''abstractEstimate.estimate.validate.deposit.amount'')]  
	except ValidationException,e:  
		result=None  
		validationErrors=e.getErrors()  
if(validationErrors==None):
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and (wfItem.getCurrentState().getValue()==''TECH_SANCTION_CHECKED'' and wfmatrix.getFromQty().doubleValue()==0):
		wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
		persistenceService.setTechSanctionNumber(wfItem)
		wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getExecutingDepartment().getDeptName(),wfItem.getAmountWfRule(),wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction()) 
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''TECH_SANCTIONED'') or (action.getName()==''Approve'' and wfItem.getIsSpillOverWorks()):  
		persistenceService.setTechSanctionNumber(wfItem)'
where NAME='AbstractEstimate.workflow';  



update eg_script set script=script||('
	if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''END''  and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED''  and wfmatrix.getAdditionalRule()==''budgetApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverBudgetApp''):  
		try:  
			if(wfItem.getIsBudgetCheckRequired()):
				wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  
				check=persistenceService.checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))  
				if check:  
					persistenceService.persist(wfItem)  
				else:  
					raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')] 
		except ValidationException,e:  
			result=None  
			validationErrors=e.getErrors()
	if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''END''  and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED''  and wfmatrix.getAdditionalRule()==''depositCodeApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverDepositCodeApp''):  
		try:  
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  
			check=persistenceService.checkForBudgetaryAppropriationForDepositWorks(wfItem.getFinancialDetails().get(0))  
			if check:  
				persistenceService.persist(wfItem)  
			else:  
				raise ValidationException,[ValidationError(''depositworks.budget.check.fail'',''depositworks.budget.check.fail'')]  
		except ValidationException,e:  
			result=None  
			validationErrors=e.getErrors()  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED'':  
		persistenceService.setProjectCode(wfItem)  
	# if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPROPRIATION_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''budgetApp''):  
	#	try:  
	#		wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())  
	#		check=persistenceService.releaseBudgetOnReject(wfItem.getFinancialDetails().get(0))  
	#	except ValidationException,e:  
	#		result=None  
	#		validationErrors=e.getErrors()  
	# if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''depositCodeApp''):  
	#	try:  
	#		wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())  
	#		check=persistenceService.releaseDepositWorksAmountOnReject(wfItem.getFinancialDetails().get(0))  
	#	except ValidationException,e:  
	#		result=None  
	#		validationErrors=e.getErrors()  
	persistenceService.persist(wfItem)  
if(validationErrors==None):  
	result=wfItem  
	validationErrors=None')
where NAME='AbstractEstimate.workflow';  


#DOWN


UPDATE EG_SCRIPT 
SET SCRIPT=
'from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov import EGOVException  
validationErrors=None  
result=None  
wfmatrix=None  
if wfItem.getIsSpillOverWorks():  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
else:  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getExecutingDepartment().getDeptName(),wfItem.getAmountWfRule(),wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction''  and wfmatrix.getAdditionalRule()==''budgetApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverBudgetApp''):  
	try:  
		if(wfItem.getIsBudgetCheckRequired()):
			if(wfItem.getTotalAmount().getValue() > persistenceService.getBudgetAvailable(wfItem).doubleValue()):  
				raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.budget.amount'',''abstractEstimate.estimate.validate.budget.amount'')] 
	except ValidationException,e:  
		result=None  
		validationErrors=e.getErrors()  
if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction''  and wfmatrix.getAdditionalRule()==''depositCodeApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverDepositCodeApp''):  
	try:  
		if(wfItem.getTotalAmount().getValue() > persistenceService.getDepositWorksBalance(wfItem).doubleValue()):  
			raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.deposit.amount'',''abstractEstimate.estimate.validate.deposit.amount'')]  
	except ValidationException,e:  
		result=None  
		validationErrors=e.getErrors()  
if(validationErrors==None): 
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''TECH_SANCTIONED'') or (action.getName()==''Approve'' and wfItem.getIsSpillOverWorks()):  
		persistenceService.setTechSanctionNumber(wfItem)'
where NAME='AbstractEstimate.workflow';  



update eg_script set script=script||('
	if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''END''  and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED''  and wfmatrix.getAdditionalRule()==''budgetApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverBudgetApp''):  
		try:  
			if(wfItem.getIsBudgetCheckRequired()):
				wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  
				check=persistenceService.checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))  
				if check:  
					persistenceService.persist(wfItem)  
				else:  
					raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')] 
		except ValidationException,e:  
			result=None  
			validationErrors=e.getErrors()
	if((action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''END''  and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED''  and wfmatrix.getAdditionalRule()==''depositCodeApp'') or (action.getName()==''Approve'' and wfmatrix.getAdditionalRule()==''spillOverDepositCodeApp''):  
		try:  
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  
			check=persistenceService.checkForBudgetaryAppropriationForDepositWorks(wfItem.getFinancialDetails().get(0))  
			if check:  
				persistenceService.persist(wfItem)  
			else:  
				raise ValidationException,[ValidationError(''depositworks.budget.check.fail'',''depositworks.budget.check.fail'')]  
		except ValidationException,e:  
			result=None  
			validationErrors=e.getErrors()  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED'':  
		persistenceService.setProjectCode(wfItem)  
	# if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPROPRIATION_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''budgetApp''):  
	#	try:  
	#		wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())  
	#		check=persistenceService.releaseBudgetOnReject(wfItem.getFinancialDetails().get(0))  
	#	except ValidationException,e:  
	#		result=None  
	#		validationErrors=e.getErrors()  
	# if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''depositCodeApp''):  
	#	try:  
	#		wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())  
	#		check=persistenceService.releaseDepositWorksAmountOnReject(wfItem.getFinancialDetails().get(0))  
	#	except ValidationException,e:  
	#		result=None  
	#		validationErrors=e.getErrors()  
	persistenceService.persist(wfItem)  
if(validationErrors==None):  
	result=wfItem  
	validationErrors=None')
where NAME='AbstractEstimate.workflow';  

