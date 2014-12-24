#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException
validationErrors=None
result=None
wfmatrix=None
if wfItem.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks():
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
else:
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())
if validationErrors==None:  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''MBHeader'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''MBHeader'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''MBHeader'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	persistenceService.persist(wfItem)
	result=wfItem'
where NAME='MBHeader.workflow';

#DOWN

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException
validationErrors=None
result=None
try:
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())
	if wfmatrix==None:
		raise ValidationException,[ValidationError(''workflow.not.exist'',''workflow.not.exist'')]
except ValidationException,e:
	validationErrors=e.getErrors()
if validationErrors==None:  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''MBHeader'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''MBHeader'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''MBHeader'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	persistenceService.persist(wfItem)
	result=wfItem'
where NAME='MBHeader.workflow';

