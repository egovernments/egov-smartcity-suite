#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
if(validationErrors==None):
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	persistenceService.persist(wfItem)
	result=wfItem
	validationErrors=None'
where NAME='WorkOrder.workflow'; 


#DOWN

DELETE FROM EG_SCRIPT WHERE NAME='WorkOrder.workflow';

INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,SCRIPT,START_DATE,END_DATE)
  VALUES(EG_SCRIPT_SEQ.NEXTVAL,'WorkOrder.workflow','python', NULL, NULL, NULL,NULL,('
from org.egov.pims.dao import EisDAOFactory            
from org.egov.pims.commons import DesignationMaster            
from org.egov.pims.commons.dao import DesignationMasterDAO          
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException          
from org.egov import EGOVException   
from  org.egov.exceptions import NoSuchObjectException          
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()        
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()          
def save_and_submit():          
    save_workOrder()          
    return submit()          
def save_workOrder():
    print wfItem.getCurrentState()           
    if not wfItem.getCurrentState():     
	wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
    return (persistenceService.persist(wfItem),None)        
    if wfItem.getCurrentState().getValue() == ''REJECTED'':  
	wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getWorkOrderPreparedBy().getIdPersonalInformation()))  
	return (persistenceService.persist(wfItem),None)        
    return (None,None)     
def submit():          
    state=''DEFAULT''          
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
    return approvals[state]()   
def reject():   
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getWorkOrderPreparedBy().getIdPersonalInformation()), comments)   
    return (persistenceService.persist(wfItem),None)
def cancel():  
    wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getWorkOrderPreparedBy().getIdPersonalInformation()),comments)  
    wfItem.changeState(''END'',persistenceService.getEisManager().getPositionforEmp(wfItem.getWorkOrderPreparedBy().getIdPersonalInformation()),'''')           
    return (persistenceService.persist(wfItem),None)   
def submit_for_approval():          
    try:         
	position=find_position(wfItem)    
	if wfItem.getCurrentState().getValue() == ''REJECTED'':
	    wfItem.changeState(''RESUBMITTED'',''Pending checking for Approval'',position,comments)   
	else:  
	    wfItem.changeState(''CREATED'',''Pending checking for Approval'',position,comments)   
	result=persistenceService.persist(wfItem)          
	return (result,None)          
    except ValidationException,e:          
	return (None,e.getErrors())
def approve():   
    try:
	if wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
	    position=find_position(wfItem)   
	    state=''CHECKED''   
	    action=''Pending for Approval''
	if wfItem.getCurrentState().getValue() == ''CHECKED'':
	    state=''APPROVED''   
	    action='''' 
	    position=wfItem.getCurrentState().getOwner()     
   	wfItem.changeState(state,action,position,comments)
	if wfItem.getCurrentState().getValue() == ''APPROVED'':  
	    wfItem.changeState(''END'',action,position,comments)   
    	result=persistenceService.persist(wfItem)            
    	return (result,None)   
    except ValidationException,e:            
	return (None,e.getErrors())
'),
    TO_DATE('01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),
    TO_DATE('01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')
  );

update eg_script set script=script||( 
'def find_position(workOrder):              
    try:	  
	next_position=persistenceService.getEisManager().getPositionforEmp(workOrder.getWorkflowApproverUserId())      
    except:  
        pass  
    if not next_position:  
        raise ValidationException,[ValidationError(''currentState.position'',''works.workOrderworkflow.no_employee_position'')]   
    return next_position         
approvals={          
    ''DEFAULT'':save_and_submit,  
    ''NEW'':submit_for_approval,          
    ''CREATED'':approve,             
    ''REJECTED'':submit_for_approval          
}          
transitions={
 ''save'':save_workOrder,            
 ''submit_for_approval'':submit,          
 ''reject'':reject,          
 ''cancel'':cancel,          
 ''approve'':approve
}          
result,validationErrors=transitions[action.getName()]()')
where name = 'WorkOrder.workflow';

