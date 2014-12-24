#UP

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


#DOWN

update eg_script set script=
'from org.egov.pims.dao import EisDAOFactory            
from org.egov.pims.commons import DesignationMaster            
from org.egov.pims.commons.dao import DesignationMasterDAO          
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException          
from org.egov import EGOVException   
from  org.egov.exceptions import NoSuchObjectException          
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()        
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
workOrder=persistenceService.find("from WorkOrder where workOrderNumber=?",[wfItem.getWorkordernumber()])
def save_and_submit():
    print ''------------------------------------submit_and_submit-----------------''
    save_bill()          
    return submit()          
def save_bill():
    if workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks():
	    if not wfItem.getCurrentState():     
		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
	    return (persistenceService.persist(wfItem),None)        
	    if wfItem.getCurrentState().getValue() == ''REJECTED'':  
		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()))  
		return (persistenceService.persist(wfItem),None)        
	    return (None,None)
    else:
    	print ''-----------------Normal Work-----------------''
    	if not wfItem.getCurrentState():
    		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
   	return (persistenceService.persist(wfItem),None)
    	if wfItem.getCurrentState().getValue() == ''REJECTED'':
   		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()))
   		return (persistenceService.persist(wfItem),None)
    	return (None,None)     
def submit():
    if workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks():          
	    state=''DEFAULT''          
	    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
	    return approvals[state]()
    else:
    	print ''-----------------Normal Work-----------------''
    	state=''DEFAULT''
    	if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()
    	return approvals[state]()   
' where name = 'ContractorBillRegister.workflow';

update eg_script set script=script||('
def reject():
    print ''------------------------------------reject-----------------''
    position=persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation())   
    wfItem.changeState(''REJECTED'',position, comments)   
    return (persistenceService.persist(wfItem),None)
def cancel():
    print ''------------------------------------cancel-----------------''
    position=persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation())  
    wfItem.changeState(''CANCELLED'',position,comments)  
    wfItem.changeState(''END'',position,'''')           
    return (persistenceService.persist(wfItem),None)
') where name = 'ContractorBillRegister.workflow';   

update eg_script set script=script||('
def submit_for_approval():          
    print ''------------------------------------submit_for_approval-----------------''
    if workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks():
	    try:         
		position=find_position(wfItem)    
		return approve()          
	    except ValidationException,e:          
		return (None,e.getErrors())
    else:
    	print ''-----------------Normal Work-----------------''
    	try:
    		position=find_position(wfItem)
    		return approve()
    	except ValidationException,e:
    		return (None,e.getErrors())
') where name = 'ContractorBillRegister.workflow';   

update eg_script set script=script||('
def approve():   
    print ''------------------------------------approve-----------------''
    if workOrder.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks():
	    try:
		state=''APPROVED''
		action=''''
		position=wfItem.getCurrentState().getOwner()
		wfItem.changeState(state,action,position,comments)
		if state==''APPROVED'':
		    wfItem.changeState(''END'',action,wfItem.getCurrentState().getOwner(),comments)        
		result=persistenceService.persist(wfItem)        
		return (result,None)   
	    except ValidationException,e:            
		return (None,e.getErrors())
    else:
    	print ''-----------------Normal Work-----------------''
    	try:
    		state=''APPROVED''
    		action=''''
    		position=wfItem.getCurrentState().getOwner()
    		wfItem.changeState(state,action,position,comments)
    		if state==''APPROVED'':
    			wfItem.changeState(''END'',action,wfItem.getCurrentState().getOwner(),comments)
    		result=persistenceService.persist(wfItem)
    		return (result,None)
    	except ValidationException,e:
    		return (None,e.getErrors())
') where name = 'ContractorBillRegister.workflow';

update eg_script set script=script||( 
'def find_position(bill):
    try:	  
	print bill.getWorkflowApproverUserId()
	next_position=persistenceService.getEisManager().getPositionforEmp(bill.getWorkflowApproverUserId())      
    except:  
	pass  
    if not next_position:  
	raise ValidationException,[ValidationError(''currentState.position'',''works.estimateworkflow.no_employee_position'')]   
    return next_position
approvals={          
    ''DEFAULT'':save_and_submit,  
    ''NEW'':submit_for_approval,          
    ''CREATED'':approve,             
    ''REJECTED'':submit_for_approval          
}          
transitions={
 ''save'':save_bill,            
 ''submit_for_approval'':submit,          
 ''reject'':reject,          
 ''cancel'':cancel,          
 ''approve'':approve
}          
result,validationErrors=transitions[action.getName()]()
') where name = 'ContractorBillRegister.workflow';

