#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException
validationErrors=None
result=None
workOrder=persistenceService.find(''from WorkOrder where workOrderNumber=?'',[wfItem.getWorkordernumber()]) 
try:
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())
	if wfmatrix==None:
		raise ValidationException,[ValidationError(''workflow.not.exist'',''workflow.not.exist'')]
except ValidationException,e:
	validationErrors=e.getErrors()
if validationErrors==None:
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''CONTRACTORBILL'',wfmatrix.getNextStatus()])
		wfItem.setStatus(status)
		wfItem.setBillstatus(wfmatrix.getNextStatus())
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''CONTRACTORBILL'',''REJECTED''])
		wfItem.setStatus(status)
		wfItem.setBillstatus(''REJECTED'')
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''CONTRACTORBILL'',''CANCELLED''])
		wfItem.setStatus(status)
		wfItem.setBillstatus(''CANCELLED'')
	persistenceService.persist(wfItem)
	result=wfItem'
where NAME='ContractorBillRegister.workflow';

#DOWN

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException
workOrder=persistenceService.find(''from WorkOrder where workOrderNumber=?'',[wfItem.getWorkordernumber()]) 
wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),workOrder.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
	status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''CONTRACTORBILL'',wfmatrix.getNextStatus()])
	wfItem.setStatus(status)
	wfItem.setBillstatus(wfmatrix.getNextStatus())
if(action.getName()==''Reject''):
	status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''CONTRACTORBILL'',''REJECTED''])
	wfItem.setStatus(status)
	wfItem.setBillstatus(''REJECTED'')
if(action.getName()==''Cancel''):
	status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''CONTRACTORBILL'',''CANCELLED''])
	wfItem.setStatus(status)
	wfItem.setBillstatus(''CANCELLED'')
persistenceService.persist(wfItem)
result=wfItem'
where NAME='ContractorBillRegister.workflow';



