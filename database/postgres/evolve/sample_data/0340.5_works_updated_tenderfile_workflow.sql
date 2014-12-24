#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException
wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
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


#DOWN

update eg_script set script='from org.egov.pims.dao import EisDAOFactory            
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
    save_tenderFile()          
    return submit()          
def save_tenderFile():        
	if not wfItem.getCurrentState():     
		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
		wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''TenderFile'',''NEW'')) 
		return (persistenceService.persist(wfItem),None)        
	if wfItem.getCurrentState().getValue() == ''REJECTED'':  
		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation()))  
		return (persistenceService.persist(wfItem),None)        
	return (None,None)     
def submit():        
	state=''DEFAULT''          
	if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()          
	return approvals[state]()'
where name = 'TenderFile.workflow'; 

update eg_script set script=script||('
def reject():
    print ''------------------------------------reject-----------------''
    position=persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation())   
    wfItem.changeState(''REJECTED'',position, comments)   
    wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''TenderFile'',''REJECTED'')) 
    return (persistenceService.persist(wfItem),None)
def cancel():
    print ''------------------------------------cancel-----------------''
    position=persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation())  
    wfItem.changeState(''CANCELLED'',position,comments)  
    wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''TenderFile'',''CANCELLED'')) 
    wfItem.changeState(''END'',position,'''')           
    return (persistenceService.persist(wfItem),None)
') where name = 'TenderFile.workflow';   

update eg_script set script=script||('
def submit_for_approval():          
    print ''------------------------------------submit_for_approval-----------------''
    if wfItem.getTenderFileDetails().get(0).getAbstractEstimate() and wfItem.getTenderFileDetails().get(0).getAbstractEstimate().getIsSpillOverWorks():
	print ''------------------------------------Spill Over Works-----------------'' 
	try:   
		return approve()          
	except ValidationException,e:          
		return (None,e.getErrors())
    else:
    	print ''-------------Normal Works--------------''
	try:   
		if wfItem.getCurrentState().getValue() == ''REJECTED'': 
			state=''RESUBMITTED''  
			action='''' 
		else: 
			state=''CREATED''  
			action='''' 
		position=wfItem.getCurrentState().getOwner() 
		wfItem.changeState(state,action,position,comments)
		wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''TenderFile'',state)) 		
		result=persistenceService.persist(wfItem)        
		return (result,None)            
	except ValidationException,e:          
		return (None,e.getErrors())
') where name = 'TenderFile.workflow';   

update eg_script set script=script||('
def approve():   
    print ''------------------------------------approve-----------------''
    if wfItem.getTenderFileDetails().get(0).getAbstractEstimate() and wfItem.getTenderFileDetails().get(0).getAbstractEstimate().getIsSpillOverWorks():
	    print ''------------------------------------Spill Over Works-----------------''
	    try:
		state=''APPROVED''	
		action=''''
		position=wfItem.getCurrentState().getOwner() 
		wfItem.changeState(state,action,position,comments)
		wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''TenderFile'',state)) 
		if state==''APPROVED'':
		    wfItem.changeState(''END'',action,wfItem.getCurrentState().getOwner(),comments)        
		result=persistenceService.persist(wfItem)        
		return (result,None)   
	    except ValidationException,e:            
		return (None,e.getErrors())
    else:
	print ''-------------Normal Works--------------''    	
	try:
		if (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and wfItem.getDepartment().getDeptName()==''Electrical'':   
	    		position=find_position(wfItem)     
	    		state=''CHECKED''    
			action=''''  
		elif (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and wfItem.getDepartment().getDeptName()==''Garden'':  
	    		position=wfItem.getCurrentState().getOwner()      
	    		state=''APPROVED''     
	    		action=''''
		elif wfItem.getCurrentState().getValue() == ''CHECKED'':  
	    		state=''APPROVED''     
	    		action=''''   
	    		position=wfItem.getCurrentState().getOwner() 
		wfItem.changeState(state,action,position,comments)
		wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''TenderFile'',state)) 
		if state==''APPROVED'':
		    wfItem.changeState(''END'',action,wfItem.getCurrentState().getOwner(),comments)        
		result=persistenceService.persist(wfItem)        
		return (result,None)   
	except ValidationException,e:            
		return (None,e.getErrors())
') where name = 'TenderFile.workflow';

update eg_script set script=script||( 
'def find_position(tenderFile):
    try:	
	next_position=persistenceService.getEisManager().getPositionforEmp(tenderFile.getWorkflowApproverUserId())      
    except:  
	pass  
    if not next_position:  
	raise ValidationException,[ValidationError(''currentState.position'',''works.tenderfileworkflow.no_employee_position'')]   
    return next_position
approvals={          
    ''DEFAULT'':save_and_submit,  
    ''NEW'':submit_for_approval,          
    ''CREATED'':approve,             
    ''REJECTED'':submit_for_approval          
}          
transitions={
 ''save'':save_tenderFile,            
 ''submit_for_approval'':submit,          
 ''reject'':reject,          
 ''cancel'':cancel,          
 ''approve'':approve
}          
result,validationErrors=transitions[action.getName()]()
') where name = 'TenderFile.workflow';


