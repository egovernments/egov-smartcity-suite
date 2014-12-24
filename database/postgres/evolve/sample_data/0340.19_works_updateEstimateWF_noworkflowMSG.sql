#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
try:
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getExecutingDepartment().getDeptName(),wfItem.getAmountWfRule(),wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())
	if wfmatrix==None:
		raise ValidationException,[ValidationError(''workflow.not.exist'',''workflow.not.exist'')]
except ValidationException,e:
	validationErrors=e.getErrors()
if validationErrors==None:
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation Check'':
		try:
			if(wfItem.getTotalAmount().getValue() > persistenceService.getBudgetAvailable(wfItem).doubleValue()):
				raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.budget.amount'',''abstractEstimate.estimate.validate.budget.amount'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Deposit Code Appropriation Check'':
		try:
			if(wfItem.getTotalAmount().getValue() > persistenceService.getDepositWorksBalance(wfItem).doubleValue()):
				raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.deposit.amount'',''abstractEstimate.estimate.validate.deposit.amount'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
if(validationErrors==None):
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
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''TECH_SANCTIONED'':
		persistenceService.setTechSanctionNumber(wfItem)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''BUDGETARY_APPR_CHECKED'':
		try:
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))
			check=persistenceService.checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))
			if check:
				persistenceService.persist(wfItem)
			else:
				raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
' where name = 'AbstractEstimate.workflow';

update eg_script set script=script||('
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''DEPOSIT_CODE_APPR_CHECKED'':
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
	if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPROPRIATION_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''budgetApp''):
		try:
			wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
			check=persistenceService.releaseBudgetOnReject(wfItem.getFinancialDetails().get(0))
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''depositCodeApp''):
		try:
			wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
			check=persistenceService.releaseDepositWorksAmountOnReject(wfItem.getFinancialDetails().get(0))
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	persistenceService.persist(wfItem)
if(validationErrors==None):
	result=wfItem
	validationErrors=None')
where NAME='AbstractEstimate.workflow';

#DOWN


UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation Check'':
	try:
		if(wfItem.getTotalAmount().getValue() > persistenceService.getBudgetAvailable(wfItem).doubleValue()):
			raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.budget.amount'',''abstractEstimate.estimate.validate.budget.amount'')]
	except ValidationException,e:
		result=None
		validationErrors=e.getErrors()
if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Deposit Code Appropriation Check'':
	try:
		if(wfItem.getTotalAmount().getValue() > persistenceService.getDepositWorksBalance(wfItem).doubleValue()):
			raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.deposit.amount'',''abstractEstimate.estimate.validate.deposit.amount'')]
	except ValidationException,e:
		result=None
		validationErrors=e.getErrors()
if(validationErrors==None):
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
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''TECH_SANCTIONED'':
		persistenceService.setTechSanctionNumber(wfItem)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''BUDGETARY_APPR_CHECKED'':
		try:
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))
			check=persistenceService.checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))
			if check:
				persistenceService.persist(wfItem)
			else:
				raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
' where name = 'AbstractEstimate.workflow';

update eg_script set script=script||('
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''DEPOSIT_CODE_APPR_CHECKED'':
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
	if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPROPRIATION_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''budgetApp''):
		try:
			wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
			check=persistenceService.releaseBudgetOnReject(wfItem.getFinancialDetails().get(0))
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''depositCodeApp''):
		try:
			wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
			check=persistenceService.releaseDepositWorksAmountOnReject(wfItem.getFinancialDetails().get(0))
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	persistenceService.persist(wfItem)
if(validationErrors==None):
	result=wfItem
	validationErrors=None')
where NAME='AbstractEstimate.workflow';


