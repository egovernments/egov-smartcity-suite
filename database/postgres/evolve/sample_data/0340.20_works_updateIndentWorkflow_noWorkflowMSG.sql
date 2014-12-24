#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
try:
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
	if wfmatrix==None:
		raise ValidationException,[ValidationError(''workflow.not.exist'',''workflow.not.exist'')]
except ValidationException,e:
	validationErrors=e.getErrors()
if validationErrors==None:
	if(action.getName()==''Approve'' and wfItem.getIndentType()==''Amount''):
		try:
			if(wfItem.getIndentAmount().getValue() > persistenceService.getIndentRateContractService().getBudgetAvailable(wfItem).doubleValue()):
				raise ValidationException,[ValidationError(''indent.validate.budget.amount'',''indent.validate.budget.amount'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
if(validationErrors==None):
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Approve'' and wfItem.getIndentType()== ''Amount''):	
		try:
			wfItem.setBudgetApprNo(persistenceService.getIndentRateContractService().getBudgetAppropriationNumber(wfItem))
			check=persistenceService.getIndentRateContractService().checkForBudgetaryAppropriation(wfItem)
			if check:
				persistenceService.persist(wfItem)
			else:
				raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
if(validationErrors==None):
	persistenceService.persist(wfItem)
	result=wfItem
	validationErrors=None'
where NAME='Indent.workflow';

#DOWN

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
if(action.getName()==''Approve'' and wfItem.getIndentType()== ''Amount''):	
	try:
		try:
			check=persistenceService.getIndentRateContractService().checkForBudgetaryAppropriation(wfItem)
		except EGOVException,e:
			raise ValidationException,[ValidationError(e.getMessage(),e.getMessage())]
		if check:
			wfItem.setBudgetApprNo(persistenceService.getIndentRateContractService().getBudgetAppropriationNumber(wfItem))
			persistenceService.persist(wfItem)
		else:
			raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
	except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
if(validationErrors==None):
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	persistenceService.persist(wfItem)
	result=wfItem
	validationErrors=None'
where NAME='Indent.workflow';

