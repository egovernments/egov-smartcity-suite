#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException
wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
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
def save_and_submit():
    print ''------------------------------------submit_and_submit-----------------''
    save_mbHeader()        
    return submit()     
def save_mbHeader():
    if wfItem.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks():        
	    print action
	    if not wfItem.getCurrentState():      
		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')   
		return (persistenceService.persist(wfItem),None)      
	    if wfItem.getCurrentState().getValue() == ''NEW'':      
		return (persistenceService.persist(wfItem),None) 
	    if wfItem.getCurrentState().getValue() == ''REJECTED'':
		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()))
		return (persistenceService.persist(wfItem),None)      
	    return (None,None)
    else:
    	print ''----------------Normal Works--------------''
    	print action
    	if not wfItem.getCurrentState():
    		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
    		return (persistenceService.persist(wfItem),None)
    	if wfItem.getCurrentState().getValue() == ''NEW'':
    		return (persistenceService.persist(wfItem),None)
    	if wfItem.getCurrentState().getValue() == ''REJECTED'':
    		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()))
    		return (persistenceService.persist(wfItem),None)
    	return (None,None)      
def submit():
    if wfItem.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks():
	    state=''DEFAULT''        
	    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()        
	    return approvals[state]()
    else:
    	print ''----------------Normal Works--------------''
    	state=''DEFAULT''
    	if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()
    	return approvals[state]()
' where name = 'MBHeader.workflow';
       
update eg_script set script=script||('        
def reject():
    print ''------------------------------------reject-----------------''
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),comments)        
    return (persistenceService.persist(wfItem),None)
def cancel():
    print ''------------------------------------cancel-----------------''
    position=persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation())          
    wfItem.changeState(''CANCELLED'',position,comments)   
    wfItem.changeState(''END'',position,'''')     
    return (persistenceService.persist(wfItem),None)
') where name = 'MBHeader.workflow';
       
update eg_script set script=script||('    
def submit_for_approval():
  print ''------------------------------------submit_for_approval-----------------''
  if wfItem.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks():        
	  try:  
		position=find_position(wfItem)
		excdept = wfItem.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName()
		return approval()        
	  except ValidationException,e:        
		return (None,e.getErrors())
  else:
  	print ''----------------Normal Works--------------''
  	try:
  		position=find_position(wfItem)
  		excdept = wfItem.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName()
  		return approval()
  	except ValidationException,e:
  		return (None,e.getErrors())
') where name = 'MBHeader.workflow';
       
update eg_script set script=script||('
def approval():
    print ''------------------------------------approval-----------------''
    if wfItem.getWorkOrderEstimate().getEstimate().getIsSpillOverWorks():        
	    try:  
		excdept = wfItem.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName()
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
    	print ''----------------Normal Works--------------''
    	try:
    		excdept = wfItem.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName()
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
') where name = 'MBHeader.workflow';
       
update eg_script set script=script||('
def find_position(mbHeader):
    try:	
	print mbHeader.getApproverUserId()
	next_position=persistenceService.getEisManager().getPositionforEmp(mbHeader.getApproverUserId())    
    except:
	pass
    if not next_position:
	raise ValidationException,[ValidationError(''currentState.position'',''works.estimateworkflow.no_employee_position'')] 
    return next_position
approvals={        
    ''DEFAULT'':save_and_submit,
    ''NEW'':submit_for_approval,        
    ''CREATED'':submit_for_approval,           
    ''REJECTED'':submit_for_approval        
}        
transitions={        
 ''save'':save_mbHeader,        
 ''submit_for_approval'':submit,        
 ''reject'':reject,
 ''cancel'':cancel,
 ''approval'':approval
}        
result,validationErrors=transitions[action.getName()]()')
where name = 'MBHeader.workflow';




