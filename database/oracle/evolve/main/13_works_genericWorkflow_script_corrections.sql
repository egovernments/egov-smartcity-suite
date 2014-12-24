#UP

update eg_script set script = '
from org.egov.infstr import ValidationError      
from org.egov.infstr import ValidationException      
from org.egov.exceptions import EGOVException    
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
		validationErrors=e.getErrors()'
where name = 'AbstractEstimate.workflow';
    
update eg_script set script = script || ('
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
		persistenceService.setTechSanctionNumber(wfItem)')
where name = 'AbstractEstimate.workflow';
    
update eg_script set script = script || ('
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
where name = 'AbstractEstimate.workflow';
  
update eg_script set script = '
from org.egov.pims.utils import EisManagersUtill  
from org.egov.infstr import ValidationError            
from org.egov.infstr import ValidationException    
eisCommonsService = EisManagersUtill.getEisCommonsService()  
def reject():  
    try:   
    	print ''in reject method''    
    	wfItem.changeState(''REJECTED'',eisCommonsService.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)    
    	result=persistenceService.persist(wfItem)  
	if wfItem.getCurrentState().getPrevious().getValue()==''Rejected'' or wfItem.getCurrentState().getPrevious().getValue()==''NEW'':  
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:    
        return (None,e.getErrors())  
def cancel():  
    try:   
    	print ''in cancel method''    
    	wfItem.changeState(''CANCELLED'',eisCommonsService.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)    
    	result=persistenceService.persist(wfItem)  
	workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:    
        return (None,e.getErrors())          
def approve():  
    try:  
	print ''in approve method''  
	position=None  
	print wfItem.getApproverPositionId()  
	if wfItem.getApproverPositionId()!=None and wfItem.getApproverPositionId()!=-1:  
		position=persistenceService.find(''from Position where id=?'',[wfItem.getApproverPositionId()])  
	else:  
		position=wfItem.getCurrentState().getOwner()  
        if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''Pending for Closure'':  
        	position=eisCommonsService.getPositionByUserId(wfItem.getCreatedBy().getId())  
	print position  
    	wfItem.changeState(wfmatrix.getNextState(),wfmatrix.getNextAction(),position,comments)  
    	result=persistenceService.persist(wfItem)  
	if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''END'':  
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:    
        return (None,e.getErrors())  
def close():  
    try:  
    	print ''in close method''  
    	wfItem.changeState(''Closed'',eisCommonsService.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)  
    	result=persistenceService.persist(wfItem)  
	workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:  
        return (None,e.getErrors())          
transitions={   
 ''approve'':approve,    
 ''forward'':approve,    
 ''reject'':reject,  
 ''Approve'':approve,  
 ''Forward'':approve,  
 ''Cancel'':cancel,  
 ''cancel'':cancel,  
 ''Reject'':reject,  
 ''Close'':close,  
 ''close'':close  
}  
result,validationErrors=transitions[action.getName()]()'
 where name = 'works.generic.workflow';
 
#DOWN

update eg_script set script = '
from org.egov.pims.utils import EisManagersUtill  
from org.egov.infstr import ValidationError            
from org.egov.infstr import ValidationException    
eisCommonsManager = EisManagersUtill.getEisCommonsManager()   
def reject():  
    try:   
    	print ''in reject method''    
    	wfItem.changeState(''REJECTED'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)    
    	result=persistenceService.persist(wfItem)  
	if wfItem.getCurrentState().getPrevious().getValue()==''Rejected'' or wfItem.getCurrentState().getPrevious().getValue()==''NEW'':  
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:    
        return (None,e.getErrors())  
def cancel():  
    try:   
    	print ''in cancel method''    
    	wfItem.changeState(''CANCELLED'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)    
    	result=persistenceService.persist(wfItem)  
	workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:    
        return (None,e.getErrors())          
def approve():  
    try:  
	print ''in approve method''  
	position=None  
	print wfItem.getApproverPositionId()  
	if wfItem.getApproverPositionId()!=None and wfItem.getApproverPositionId()!=-1:  
		position=persistenceService.find(''from Position where id=?'',[wfItem.getApproverPositionId()])  
	else:  
		position=wfItem.getCurrentState().getOwner()  
        if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''Pending for Closure'':  
        	position=eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId())  
	print position  
    	wfItem.changeState(wfmatrix.getNextState(),wfmatrix.getNextAction(),position,comments)  
    	result=persistenceService.persist(wfItem)  
	if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''END'':  
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:    
        return (None,e.getErrors())  
def close():  
    try:  
    	print ''in close method''  
    	wfItem.changeState(''Closed'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)  
    	result=persistenceService.persist(wfItem)  
	workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)  
    	return(result,None)  
    except ValidationException,e:  
        return (None,e.getErrors())          
transitions={   
 ''approve'':approve,    
 ''forward'':approve,    
 ''reject'':reject,  
 ''Approve'':approve,  
 ''Forward'':approve,  
 ''Cancel'':cancel,  
 ''cancel'':cancel,  
 ''Reject'':reject,  
 ''Close'':close,  
 ''close'':close  
}  
result,validationErrors=transitions[action.getName()]()'
 where name = 'works.generic.workflow';
 
update eg_script set script = '
from org.egov.infstr import ValidationError      
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
		validationErrors=e.getErrors()'  
where name = 'AbstractEstimate.workflow';

update eg_script set script = script || ('    
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
		persistenceService.setTechSanctionNumber(wfItem)')
where name = 'AbstractEstimate.workflow';

update eg_script set script = script || ('
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
 where name = 'AbstractEstimate.workflow';