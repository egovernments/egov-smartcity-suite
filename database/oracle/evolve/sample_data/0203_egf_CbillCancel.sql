#UP


update EG_SCRIPT set script='transitions={''ASSISTANTADMIN'':[''aa_approve'',''aa_reject''],''SECTION MANAGERADMIN'':[''am_approve'',''am_reject''],''INVALID'':[''invalid'']} 
employee = eisManagerBean.getEmpForUserId(userId)  
assignment  = eisManagerBean.getAssignmentByEmpAndDate(date,employee.getIdPersonalInformation())    
state=assignment.desigId.designationName + assignment.functionary.name 
if(state !=''ASSISTANTADMIN'' and purpose==''authentication''):
    state=''INVALID''
if state in transitions:result=transitions[state]'
where name='cbill.validation';

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'EgBillregister','aa_reject','Cancel',null,null,null,null);


update EG_SCRIPT set script='from org.egov.pims.dao import EisDAOFactory
from org.egov.pims.commons import DesignationMaster
from org.egov.pims.commons.dao import DesignationMasterDAO
from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from org.egov.lib.rjbac.dept import DepartmentImpl
from org.egov.exceptions import NoSuchObjectException
from org.egov.exceptions import TooManyValuesException
from org.egov import EGOVRuntimeException
from org.egov.infstr.utils import EGovConfig
from org.egov.dao.bills import BillsDaoFactory
from org.egov.pims.utils import EisManagersUtill
from java.lang import Integer
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
egBillRegDao=BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO()
eisCommonMgr=EisManagersUtill.getEisCommonsManager()
def aa_approve():
    update_workflow(wfItem,''ADMIN ASST CREATED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_approve():
    update_workflow(wfItem,''ADMIN SM APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_reject():
    update_workflow(wfItem,''ADMIN SM REJECTED'',comments)
    return (persistenceService.persist(wfItem),None)
def hod_approve():
    update_workflow(wfItem,''HOD APPROVED'',comments)
    update_workflow(wfItem,''END'',''Expense Bill approved workflow ends'')
    egwstatus = persistenceService.find("from EgwStatus where moduletype=? and description=?",["EXPENSEBILL","Approved"])
    wfItem.setStatus(egwstatus)
    return (persistenceService.persist(wfItem),None)	
def hod_reject():
    update_workflow(wfItem,''HOD REJECTED'',comments)
    return (persistenceService.persist(wfItem),None)
def aa_reject():
    update_workflow(wfItem,''END'',''Expense Bill cancelled'')
    egwstatus = persistenceService.find("from EgwStatus where moduletype=? and description=?",["EXPENSEBILL","Cancelled"])
    wfItem.setStatus(egwstatus)
    return (persistenceService.persist(wfItem),None)
def update_workflow(wfItem,wfItemStatus,comments):
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
    wfItem.changeState(wfItemStatus,position,comments)
transitions={''aa_approve'':aa_approve,''aa_reject'':aa_reject,''am_approve'':am_approve,''am_reject'':am_reject,''hod_approve'':hod_approve,''hod_reject'':hod_reject}
result,validationErrors=transitions[action.getName().split(''|'')[0]]()'
where name='Cbill.workflow';

#DOWN