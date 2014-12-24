#UP

update eg_Script set script=
'from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov import EGOVException  
validationErrors=None  
result=None  
if(validationErrors==None):  
	print wfItem.getCurrentState().getNextAction()   
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName(),wfItem.getAmountWfRule(),wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''ReturnSecurityDeposit'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''ReturnSecurityDeposit'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''ReturnSecurityDeposit'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	persistenceService.persist(wfItem)  
	result=wfItem  
	validationErrors=None'
where name='ReturnSecurityDeposit.workflow';


#DOWN

update eg_Script set script=
'from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov import EGOVException  
validationErrors=None  
result=None  
if(validationErrors==None):  
	print wfItem.getCurrentState().getNextAction()   
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrder().getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName(),None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''ReturnSecurityDeposit'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''ReturnSecurityDeposit'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''ReturnSecurityDeposit'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	persistenceService.persist(wfItem)  
	result=wfItem  
	validationErrors=None'
where name='ReturnSecurityDeposit.workflow';
