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
        wfItem.changeState(''NEW'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Material Indent Created'')  
        return (persistenceService.persist(wfItem),None)  
    elif wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''REJECTED'' or wfItem.getCurrentState().getValue() == ''APPROVAL_PENDING'': 
        return (persistenceService.persist(wfItem),None)  
    return (None,None)  
def submit_for_approval():  
    try:  
        if wfItem.getCurrentState().getValue() == ''NEW'':  
	    position=None  
	    position=find_employee_position(''Asst. Engineer'',wfItem)
	    if position != 0:  
                wfItem.changeState(''APPROVAL_PENDING'',position,''Material Indent Pending Approval'')  
                result=persistenceService.persist(wfItem)  
        elif wfItem.getCurrentState().getValue() == ''REJECTED'':  
            position=None  
	    position=find_employee_position(''Asst. Engineer'',wfItem)
	    if position != 0:  
                wfItem.changeState(''APPROVAL_PENDING'',position,''Material Indent Pending Approval'')  
                result=persistenceService.persist(wfItem)  
        return (result,None)  
    except ValidationException,e:  
        return (None,e.getErrors())') 
where name = 'EgfMrheader.workflow';

update EG_SCRIPT SET script = 
	script || ('
def approve():  
    try:  
        if wfItem.getCurrentState().getValue() == ''APPROVAL_PENDING'':  
            position=None  
	    wfItem.changeState(''END'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Material Indent  Approved'')
	    status=commonsManager.getStatusByModuleAndDescription(StoresConstants.MATERIALINDENT, ''Approved'')
	    wfItem.setStatusid(BigDecimal.valueOf(status.getId()))
	    result=persistenceService.persist(wfItem)  
	elif wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''REJECTED'':
	    return submit_for_approval()
	return (result, None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def reject():  
    try:  
        print ''in reject'' 
        if wfItem.getCurrentState().getValue() == ''NEW'' or wfItem.getCurrentState().getValue() == ''REJECTED'':  
	    wfItem.changeState(''END'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Material Indent Canceled'')
	    status=commonsManager.getStatusByModuleAndDescription(StoresConstants.MATERIALINDENT, ''Rejected'')
	    wfItem.setStatusid(BigDecimal.valueOf(status.getId()))
	    result=persistenceService.persist(wfItem)  
	elif wfItem.getCurrentState().getValue() == ''APPROVAL_PENDING'':  
	    position=None  
	    position=find_employee_position(''MEDICAL OFFICER'',wfItem) 
	    print position
            wfItem.changeState(''REJECTED'',position,''Material Indent Rejected'')  
	    result=persistenceService.persist(wfItem)
	return (persistenceService.persist(wfItem),None) 
    except ValidationException,e: 
        return (None,e.getErrors())    
def find_designation(designationName):
    designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'',[designationName])
    if len(designations)>0:
        return designations[0]
    else:
        raise ValidationException,[ValidationError(''workflow.no.position'',''workflow.no.position'')]     
def find_employee_position(designation, mrheader):
    try:
        next_designation=find_designation(designation)
    	wardId=1
   	print mrheader.getDepartmentId().getId()
        print next_designation.getDesignationId()
    	next_employee=None
   	next_employee=pimsDAO.getEmployee(mrheader.getDepartmentId().getId(),next_designation.getDesignationId(),wardId)
    	if not next_employee:
    	    raise ValidationException,[ValidationError(''workflow.no.employee'',''workflow.no.employee'')]
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
WHERE name = 'EgfMrheader.workflow';


#DOWN
