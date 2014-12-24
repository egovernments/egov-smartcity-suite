#UP

update EG_SCRIPT set script='result=['' '','' '' ]
employee = eisManagerBean.getEmpForUserId(userId)
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation()) 
state=''''
if(assignment.functionary != None and assignment.desigId != None):
	state=assignment.functionary.name + "-" + assignment.desigId.designationName
if (state == ''ADMIN-ASSISTANT''):
	result[0]="ADMIN-SECTION MANAGER"
elif (state == ''ADMIN-SECTION MANAGER''):
	result[0]="ANYFUNCTIONARY-ANYDESG"
else:
     result[0]="END"'
where name='cbill.nextUser';



update EG_SCRIPT set script = 'from org.egov.pims.dao import EisDAOFactory
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
from java.util import Date
from org.egov.infstr.client.filter import EGOVThreadLocals 
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
egBillRegDao=BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO()
eisCommonMgr=EisManagersUtill.getEisCommonsManager()
eisManager=EisManagersUtill.getEisManager()  
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
    personalInfo = eisManager.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()))
    assignment = eisManager.getAssignmentByEmpAndDate(Date(),personalInfo.getIdPersonalInformation())
    lastApproval = ''''
    if(assignment.functionary != None):
      lastApproval=  assignment.functionary.name  + '' ''
    if(assignment.desigId != None):
      lastApproval = lastApproval + assignment.desigId.designationName
    lastApproval = lastApproval + '' APPROVED''
    update_workflow(wfItem,lastApproval,comments)
    update_workflow(wfItem,''END'',''Expense Bill approved workflow ends'')
    egwstatus = persistenceService.find("from EgwStatus where moduletype=? and description=?",["EXPENSEBILL","Approved"])
    wfItem.setStatus(egwstatus)
    return (persistenceService.persist(wfItem),None)	
def hod_reject():
    personalInfo = eisManager.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()))
    assignment = eisManager.getAssignmentByEmpAndDate(Date(),personalInfo.getIdPersonalInformation())
    lastApproval = ''''
    if(assignment.functionary != None):
      lastApproval=  assignment.functionary.name  + '' ''
    if(assignment.desigId != None):
      lastApproval = lastApproval + assignment.desigId.designationName
    lastApproval = lastApproval + '' REJECTED''
    update_workflow(wfItem,lastApproval,comments)
    return (persistenceService.persist(wfItem),None)
def update_workflow(wfItem,wfItemStatus,comments):
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
    wfItem.changeState(wfItemStatus,position,comments)
transitions={''aa_approve'':aa_approve,''am_approve'':am_approve,''am_reject'':am_reject,''hod_approve'':hod_approve,''hod_reject'':hod_reject}
result,validationErrors=transitions[action.getName().split(''|'')[0]]()'
WHERE name='Cbill.workflow'; 

#DOWN
