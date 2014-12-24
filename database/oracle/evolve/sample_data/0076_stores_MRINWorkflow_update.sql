#UP
UPDATE EG_SCRIPT SET script = ('from org.egov.pims.dao import EisDAOFactory 
from java.math import BigDecimal
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
    	wfItem.changeState(''NEW'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''MRIN Created'')  
        return (persistenceService.persist(wfItem),None)  
def submit_for_approval():  
    try:  
        if wfItem.getCurrentState().getValue() == ''NEW'':  
	    print ''inside submit for approval''
	    position=None	     
	    position=find_employee_position(''Asst. Engineer'',wfItem)  
	    print position
	    if position != 0:  
                wfItem.changeState(''APPROVAL_PENDING'',position,''MRIN Pending Approval'')  
                result=persistenceService.persist(wfItem)          
        print ''before forwarding''
        return (result,None)  
    except ValidationException,e:
        raise ValidationException,[ValidationError(''workflow.no.position'',''workflow.no.position'')]
        print ''***** before throwing error''
        return (None,e.getErrors())  
def approve():    
    try:    
        if wfItem.getCurrentState().getValue() == ''APPROVAL_PENDING'':    
            position=None    
	    wfItem.changeState(''END'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Mrin  Approved'')    
	    status=commonsManager.getStatusByModuleAndDescription(StoresConstants.MATERIALISSUENOTE, ''Approved'')    
	    print ''status''    
	    print status.getId()    
            wfItem.setStatusid(status.getId())               
	    result=persistenceService.persist(wfItem)  
	elif wfItem.getCurrentState().getValue() == ''NEW'':  
	    print ''before sending for approval''
	    result=submit_for_approval()  
	return (result, None)    
    except ValidationException,e:    
        return (None,e.getErrors())')
where name = 'EgfMrinheader.workflow';        
		
UPDATE EG_SCRIPT
SET SCRIPT=SCRIPT||('
def reject():      
    try:  
        print ''am in reject''  
        if wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''APPROVAL_PENDING'':  
            print ''am in if reject''  
            wfItem.changeState(''END'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''MRIN Canceled'')  
            status=commonsManager.getStatusByModuleAndDescription(StoresConstants.MATERIALISSUENOTE, ''Rejected'')  
            print ''status''  
            print status  
            print status.getId()  
            wfItem.setStatusid(status.getId())  
            print status.getId()  
            print status.getId()  
	    result=persistenceService.persist(wfItem)  
        return (result,None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def find_designation(designationName):      
    	designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'',[designationName])     
    	if len(designations)>0:
            print ''inside designation''
            return designations[0]
        else:
            print ''before raising exception in designation''
            raise ValidationException,[ValidationError(''workflow.no.position'',''workflow.no.position'')]  
def find_employee_position(designation, mrinheader):
    try:
        print''before calling designation''
        next_designation=find_designation(designation)  
    	print ''in find_employee_position next_designation''  
    	print next_designation  
    	wardId=1  
    	next_employee=None  
    	print ''in find_employee_position next employee''  
    	next_employee=pimsDAO.getEmployee(mrinheader.getDeptid(),next_designation.getDesignationId(),wardId)  
    	if not next_employee:
    	    raise ValidationException,[ValidationError(''workflow.no.employee'',''workflow.no.employee'')]
    	print next_employee  
    	print ''in find_employee_position position''  
    	position=eisCommonsManager.getPositionByUserId(next_employee.getUserMaster().getId())  
    	print position  
    	return position      
    except ValidationException,e:	
    	raise ValidationException,[ValidationError(''workflow.no.position'',''workflow.no.position'')] 
        return (None,e.getErrors()) 		
transitions={  
 ''save'':save,  
 ''submit'':submit_for_approval,  
 ''save_and_submit'':save_and_submit,  
 ''reject'':reject,  
 ''approve'':approve  
}  
result,validationErrors=transitions[action.getName()]()')
WHERE name = 'EgfMrinheader.workflow' ;
		
#DOWN