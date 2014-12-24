#UP

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,SCRIPT, START_DATE, END_DATE ) VALUES ( 
EG_SCRIPT_SEQ.NEXTVAL, 'cbill.validation', 'python', NULL, NULL, NULL, NULL, 'transitions={''ASSISTANTADMIN'':[''aa_approve''],''SECTION MANAGERADMIN'':[''am_approve'',''am_reject''],''INVALID'':[''invalid'']} 
employee = eisManagerBean.getEmpForUserId(userId)  
assignment  = eisManagerBean.getAssignmentByEmpAndDate(date,employee.getIdPersonalInformation())    
state=assignment.desigId.designationName + assignment.functionary.name 
if(state !=''ASSISTANTADMIN'' and purpose==''authentication''):
    state=''INVALID''
if state in transitions:result=transitions[state]'
,  TO_DATE( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_DATE( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'EgBillregister','aa_approve','Save & Forward',null,null,null,null);

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'EgBillregister','am_approve','Approve',null,null,null,null);

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'EgBillregister','am_reject','Reject',null,null,null,null);

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,SCRIPT, START_DATE, END_DATE ) VALUES ( 
EG_SCRIPT_SEQ.NEXTVAL, 'cbill.nextUser', 'python', NULL, NULL, NULL, NULL, 'result=['' '','' '' ]
employee = eisManagerBean.getEmpForUserId(userId)
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation()) 
state=''''
if(assignment.functionary != None and assignment.desigId != None):
	state=assignment.functionary.name + "-" + assignment.desigId.designationName
if (state == ''ADMIN-ASSISTANT''):
	result[0]="ADMIN-SECTION MANAGER"
elif (state == ''ADMIN-SECTION MANAGER''):
	result[0]="HOD"
else:
     result[0]="END"'
,  TO_DATE( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_DATE( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,SCRIPT, START_DATE, END_DATE ) VALUES ( 
EG_SCRIPT_SEQ.NEXTVAL, 'Cbill.workflow', 'python', NULL, NULL, NULL, NULL, '
from org.egov.pims.dao import EisDAOFactory
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
def update_workflow(wfItem,wfItemStatus,comments):
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
    wfItem.changeState(wfItemStatus,position,comments)
transitions={''aa_approve'':aa_approve,''am_approve'':am_approve,''am_reject'':am_reject,''hod_approve'':hod_approve,''hod_reject'':hod_reject}
result,validationErrors=transitions[action.getName().split(''|'')[0]]()'
,  TO_DATE( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_DATE( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 


#DOWN
