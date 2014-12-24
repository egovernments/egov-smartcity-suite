#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
wfmatrix=None
if wfItem.getTenderFileDetails().get(0).getAbstractEstimate() and wfItem.getTenderFileDetails().get(0).getAbstractEstimate().getIsSpillOverWorks():
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
else:
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getDepartment().getDeptName(),None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
if(validationErrors==None):
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''TenderFile'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''TenderFile'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''TenderFile'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	persistenceService.persist(wfItem)
	result=wfItem
	validationErrors=None'
where NAME='TenderFile.workflow'; 

#DOWN

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException
wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getDepartment().getDeptName(),None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
	status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''TenderFile'',wfmatrix.getNextStatus()])
	wfItem.setEgwStatus(status)
if(action.getName()==''Reject''):
	status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''TenderFile'',''REJECTED''])
	wfItem.setEgwStatus(status)
if(action.getName()==''Cancel''):
	status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''TenderFile'',''CANCELLED''])
	wfItem.setEgwStatus(status)
persistenceService.persist(wfItem)
result=wfItem'
where NAME='TenderFile.workflow';


