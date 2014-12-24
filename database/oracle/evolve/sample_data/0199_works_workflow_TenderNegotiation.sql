#UP
update eg_script set script=
'from org.egov.pims.dao import EisDAOFactory            
from org.egov.pims.commons import DesignationMaster  
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()        
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()          
def save_and_submit():          
    save_tenderResponse()          
    return submit()       
def save_tenderResponse():          
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
    state=''DEFAULT''          
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
    return approvals[state]()          
def reject():              
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()).getIdPersonalInformation()),'''')          
    return (persistenceService.persist(wfItem),None)
def cancel():
    position=persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()) 
    wfItem.changeState(''CANCELLED'',position,'''') 
    wfItem.changeState(''END'',position,'''') 
    return (persistenceService.persist(wfItem),None)  
def submit_for_approval():          
  try:    
        position=find_position(''SUPERINTENDING ENGINEER'',wfItem)        
        wfItem.changeState(''APPROVAL_PENDING'',position,'''')          
        result=persistenceService.persist(wfItem)          
        return (result,None)          
  except ValidationException,e:          
        return (None,e.getErrors())  
def approval():          
    try:    
        position=find_position(''SUPERINTENDING ENGINEER'',wfItem)        
        wfItem.changeState(''APPROVED'',position,'''') 
	wfItem.changeState(''END'',position,'''')         
        result=persistenceService.persist(wfItem)          
        return (result,None)          
    except ValidationException,e:          
        return (None,e.getErrors())
'
where name = 'TenderResponse.workflow';

       
update eg_script set script=script||('def find_desig(designationName):  
    designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'', [designationName])        
    if not designations: raise ValidationException,[ValidationError(''currentState.owner'',''works.tenderresponseworkflow.no_next_desig'')]         
    return designations[0]        
def find_position(designation, tenderResponse):        
    next_desig=find_desig(designation)        
    wardId=0        
    next_emp=None     
    next_position=None   
    if tenderResponse.getTenderEstimate().getAbstractEstimate().getWard(): wardId=tenderResponse.getTenderEstimate().getAbstractEstimate().getWard().getId()        
    try:        
        next_emp=pimsDAO.getEmployee(tenderResponse.getTenderEstimate().getAbstractEstimate().getExecutingDepartment().getId(),next_desig.getDesignationId(),wardId)          
    except:        
        pass        
    if not next_emp:        
        raise ValidationException,[ValidationError(''currentState.owner'',''works.tenderresponseworkflow.no_employee'')]  
    try:
        next_position=persistenceService.getEisManager().getPositionforEmp(next_emp.getIdPersonalInformation())    
    except:
        pass
    if not next_position:
        raise ValidationException,[ValidationError(''currentState.position'',''works.tenderresponseworkflow.no_employee_position'')] 
    return next_position      
approvals={        
    ''DEFAULT'':save_and_submit,        
    ''NEW'':submit_for_approval,        
    ''APPROVAL_PENDING'':approval,
    ''REJECTED'':submit_for_approval,        
}        
transitions={        
 ''save'':save_tenderResponse,        
 ''submit_for_approval'':submit,        
 ''reject'':reject,
 ''cancel'':cancel,
 ''approval'':approval
}        
result,validationErrors=transitions[action.getName()]()')
where name = 'TenderResponse.workflow';

#DOWN
update eg_script set script=
'from org.egov.pims.dao import EisDAOFactory            
from org.egov.pims.commons import DesignationMaster  
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()        
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()          
def save_and_submit():          
    save_tenderResponse()          
    return submit()       
def save_tenderResponse():          
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
    state=''DEFAULT''          
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
    return approvals[state]()          
def reject():              
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')          
    return (persistenceService.persist(wfItem),None)
def cancel():
    wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getNegotiationPreparedBy().getIdPersonalInformation()),'''')
    return (persistenceService.persist(wfItem),None)  
def submit_for_approval():          
  try:    
        position=find_position(''SE'',wfItem)        
        wfItem.changeState(''APPROVAL_PENDING'',position,'''')          
        result=persistenceService.persist(wfItem)          
        return (result,None)          
  except ValidationException,e:          
        return (None,e.getErrors())  
def approval():          
    try:    
        position=find_position(''SE'',wfItem)        
        wfItem.changeState(''APPROVED'',position,'''')          
        result=persistenceService.persist(wfItem)          
        return (result,None)          
    except ValidationException,e:          
        return (None,e.getErrors())
'
where name = 'TenderResponse.workflow';

       
update eg_script set script=script||('def find_desig(designationName):  
    designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'', [designationName])        
    if not designations: raise ValidationException,[ValidationError(''currentState.owner'',''works.tenderresponseworkflow.no_next_desig'')]         
    return designations[0]        
def find_position(designation, tenderResponse):        
    next_desig=find_desig(designation)        
    wardId=0        
    next_emp=None     
    next_position=None   
    if tenderResponse.getTenderEstimate().getAbstractEstimate().getWard(): wardId=tenderResponse.getTenderEstimate().getAbstractEstimate().getWard().getId()        
    try:        
        next_emp=pimsDAO.getEmployee(tenderResponse.getTenderEstimate().getAbstractEstimate().getExecutingDepartment().getId(),next_desig.getDesignationId(),wardId)          
    except:        
        pass        
    if not next_emp:        
        raise ValidationException,[ValidationError(''currentState.owner'',''works.tenderresponseworkflow.no_employee'')]  
    try:
        next_position=persistenceService.getEisManager().getPositionforEmp(next_emp.getIdPersonalInformation())    
    except:
        pass
    if not next_position:
        raise ValidationException,[ValidationError(''currentState.position'',''works.tenderresponseworkflow.no_employee_position'')] 
    return next_position      
approvals={        
    ''DEFAULT'':save_and_submit,        
    ''NEW'':submit_for_approval,        
    ''APPROVAL_PENDING'':approval,
    ''REJECTED'':submit_for_approval,        
}        
transitions={        
 ''save'':save_tenderResponse,        
 ''submit_for_approval'':submit,        
 ''reject'':reject,
 ''cancel'':cancel,
 ''approval'':approval
}        
result,validationErrors=transitions[action.getName()]()')
where name = 'TenderResponse.workflow';




