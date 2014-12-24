#UP
update EG_SCRIPT set script = 'from org.egov.pims.dao import EisDAOFactory  
from org.egov.pims.commons import DesignationMaster  
from org.egov.pims.commons.dao import DesignationMasterDAO  
from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov.exceptions import NoSuchObjectException  
from org.egov.pims.utils import EisManagersUtill  
from org.egov.pims.commons.dao import PositionMasterDAO  
from org.egov.inventory.common import StoresConstants  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
eisCommonsManager = EisManagersUtill.getEisCommonsManager()  
commonsManager = EisManagersUtill.getCommonsManager()  
def save_and_submit():  
    save()  
    return submit_for_approval()  
def save():  
    if not wfItem.getCurrentState():          
    	wfItem.changeState(''NEW'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''MMRN Created'')  
        return (persistenceService.persist(wfItem),None)  
    elif wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''REJECTED'':   
        wfItem.getCurrentState().setOwner(eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()))  
        return (persistenceService.persist(wfItem),None)	   
def submit_for_approval():  
    try:  
        if wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''REJECTED'':  	     
	    position=find_employee_position(''Asst. Engineer'',wfItem) 
            wfItem.changeState(''APPROVAL_PENDING'',position,''MMRN Pending Approval'')  
            result=persistenceService.persist(wfItem)          
        return (result,None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def approve():    
    try:    
        if wfItem.getCurrentState().getValue() == ''APPROVAL_PENDING'':    
            position=None    
	    wfItem.changeState(''END'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Approved'')    
	    status=commonsManager.getStatusByModuleAndDescription(StoresConstants.MATERIALRECEIPTNOTE, ''Approved'')    
	    print ''status''    
	    print status.getId()    
            wfItem.setStatus(status)               
	    result=persistenceService.persist(wfItem)  
	elif wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''REJECTED'':  
	    result=submit_for_approval()  
	elif not wfItem.getCurrentState():  
	    result=save_and_submit()  
	return (result, None)    
    except ValidationException,e:    
        return (None,e.getErrors())'
WHERE name = 'EgfMrnheader.workflow' ;



update EG_SCRIPT SET script = 
	script || ('
def reject():      
    try:  
        print ''am in reject''  
        if wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''REJECTED'':  
            print ''am in if reject''  
            wfItem.changeState(''END'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''MMRN Canceled'')  
            status=commonsManager.getStatusByModuleAndDescription(StoresConstants.MATERIALRECEIPTNOTE, ''Cancelled'')  
            print ''status''  
            print status  
            print status.getId()  
            wfItem.setStatus(status)  
            print status.getId()  
            print status.getId()  
	    result=persistenceService.persist(wfItem)  
        elif wfItem.getCurrentState().getValue() == ''APPROVAL_PENDING'':  
            print ''am in else if position reject''  
            position=None   
            position=find_employee_position(''MEDICAL OFFICER'',wfItem)  
            print position  
            wfItem.changeState(''REJECTED'',position,''MMRN Rejected'')  
	    result=persistenceService.persist(wfItem)  
	    print ''before result''  
	    print result  
        return (result,None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def find_designation(designationName): 
    	designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'',[designationName]) 
	print designations
	if not designations:
	    raise ValidationException,[ValidationError(''workflow.no.position'',''workflow.no.position'')] 
    	return designations[0]   
def find_employee_position(designation, mrnheader):  
    next_designation=find_designation(designation)  
    print ''in find_employee_position next_designation''  
    print next_designation  
    wardId=1  
    next_employee=None  
    print ''in find_employee_position next employee''  
    next_employee=pimsDAO.getEmployee(mrnheader.getDept().getId(),next_designation.getDesignationId(),wardId) 
    if not next_employee:
        raise ValidationException,[ValidationError(''workflow.no.employee'',''workflow.no.employee'')]     
    print next_employee  
    print ''in find_employee_position position''  
    position=eisCommonsManager.getPositionByUserId(next_employee.getUserMaster().getId())  
    print position  
    return position      
transitions={  
 ''save'':save,  
 ''submit'':submit_for_approval,  
 ''save_and_submit'':save_and_submit,  
 ''reject'':reject,  
 ''approve'':approve  
}  
result,validationErrors=transitions[action.getName()]()')
WHERE name = 'EgfMrnheader.workflow' ;

#DOWN
