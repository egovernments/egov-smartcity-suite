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
def save_and_submit():
    save_mbHeader()        
    return submit()     
def save_mbHeader():        
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
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),comments)        
    return (persistenceService.persist(wfItem),None)
def cancel(): 
    position=persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation())          
    wfItem.changeState(''CANCELLED'',position,comments)   
    wfItem.changeState(''END'',position,'''')     
    return (persistenceService.persist(wfItem),None)    
def submit_for_approval():        
  try:  
        position=find_position(wfItem)
        if wfItem.getCurrentState().getValue() == ''REJECTED'':
		wfItem.changeState(''RESUBMITTED'',''Pending for check'',position,comments) 
	else:    
		wfItem.changeState(''CREATED'',''Pending for check'',position,comments)      
        result=persistenceService.persist(wfItem)        
        return (result,None)        
  except ValidationException,e:        
        return (None,e.getErrors())
' where name = 'MBHeader.workflow';
       
update eg_script set script=script||('
def approval():
    print ''------------------------------------approval-----------------''        
    try: 
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName() 
	if (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''ASSISTANT EXECUTIVE ENGINEER'' or currentDesig == ''ASSISTANT DIVISIONAL ENGINEER''):
		if wfItem.getCurrentState().getValue() == ''CREATED'': 		
			state=''CREATED''
		else: 
			state=''RESUBMITTED''
        	action=''Pending for check''
        	position=find_position(wfItem) 
	elif (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER''): 
		state=''CHECKED''
        	action=''Pending checking for correctness''
        	position=find_position(wfItem) 
        elif wfItem.getCurrentState().getValue() == ''CHECKED'' and currentDesig == ''ASSISTANT'':	
		state=''CHECKED''
        	action=''Pending for Approval''
        	position=find_position(wfItem)
	elif wfItem.getCurrentState().getValue() == ''CHECKED'' and currentDesig == ''SECTION MANAGER'':
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
    save_mbHeader()        
    return submit()     
def save_mbHeader():        
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
    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),comments)        
    return (persistenceService.persist(wfItem),None)
def cancel(): 
    position=persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation())          
    wfItem.changeState(''CANCELLED'',position,comments)   
    wfItem.changeState(''END'',position,'''')     
    return (persistenceService.persist(wfItem),None)    
def submit_for_approval():        
  try:  
        position=find_position(wfItem)
        if wfItem.getCurrentState().getValue() == ''REJECTED'':
		wfItem.changeState(''RESUBMITTED'',''Pending for check'',position,comments) 
	else:    
		wfItem.changeState(''CREATED'',''Pending for check'',position,comments)      
        result=persistenceService.persist(wfItem)        
        return (result,None)        
  except ValidationException,e:        
        return (None,e.getErrors())
def approval():
    print ''------------------------------------approval-----------------''        
    try:  
        if wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
        	state=''CHECKED''
        	action=''Pending for Approval''
        	position=find_position(wfItem)
	elif wfItem.getCurrentState().getValue() == ''CHECKED'':
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
' where name = 'MBHeader.workflow';
       
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
