#UP
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
def approve():          
    save_workspackage()          
    return submit()          
def save_workspackage():          
    if not wfItem.getCurrentState():     
        wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
    return (persistenceService.persist(wfItem),None)  
def submit():          
    try:
        if wfItem.getCurrentState().getValue() == ''DEFAULT'' or wfItem.getCurrentState().getValue() == ''NEW'':
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
' where name = 'WorksPackage.workflow';

update eg_script set script=script||(
'transitions={        
 ''approve'':approve
}          
result,validationErrors=transitions[action.getName()]()')
where name = 'WorksPackage.workflow';
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
    save_tenderResponse()          
    return submit()          
def save_tenderResponse():          
    if not wfItem.getCurrentState():     
        wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
    return (persistenceService.persist(wfItem),None)        
    if wfItem.getCurrentState().getValue() == ''REJECTED'':  
        wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation()))  
        return (persistenceService.persist(wfItem),None)        
    return (None,None)     
def submit():          
    state=''DEFAULT''          
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
    return approvals[state]()   
def reject():   
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation()), comments)   
    return (persistenceService.persist(wfItem),None)
def cancel():  
    wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation()),comments)  
    wfItem.changeState(''END'',persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation()),'''')           
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
' where name = 'WorksPackage.workflow';;

update eg_script set script=script||( 
'def find_position(worksPackage):              
    try:	  
	next_position=persistenceService.getEisManager().getPositionforEmp(worksPackage.getWorkflowApproverUserId())      
    except:  
        pass  
    if not next_position:  
        raise ValidationException,[ValidationError(''currentState.position'',''works.WorksPackageworkflow.no_employee_position'')]   
    return next_position         
approvals={          
    ''DEFAULT'':save_and_submit,  
    ''NEW'':submit_for_approval,          
    ''CREATED'':approve,             
    ''REJECTED'':submit_for_approval          
}          
transitions={          
 ''submit_for_approval'':submit,          
 ''reject'':reject,          
 ''cancel'':cancel,          
 ''approve'':approve
}          
result,validationErrors=transitions[action.getName()]()')
where name = 'WorksPackage.workflow';
