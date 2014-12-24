#UP
	
update eg_script set script='
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
expType=egBillRegDao.getBillTypeforVoucher(wfItem)
def aa_approve():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
    update_workflow(wfItem,''AA APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_approve():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
    update_workflow(wfItem,''AM APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)
def ao_approve():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
     update_workflow(wfItem,''END'',comments)
     persistenceService.createVoucherfromPreApprovedVoucher(wfItem)
     return (persistenceService.persist(wfItem),None)
def ao_reject():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
    update_workflow(wfItem,''AO REJECTED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_reject():
 if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):
   update_workflow(wfItem,''AM REJECTED'',comments)
   return (persistenceService.persist(wfItem),None)

def update_workflow(wfItem,wfItemStatus,comments):
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
    wfItem.changeState(wfItemStatus,position,comments)
transitions={''aa_approve'':aa_approve,''am_approve'':am_approve,''ao_approve'':ao_approve,''ao_reject'':ao_reject,''am_reject'':am_reject}
result,validationErrors=transitions[action.getName().split(''|'')[0]]()'
where name='CVoucherHeader.workflow';

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,SCRIPT, START_DATE, END_DATE ) VALUES ( 
EG_SCRIPT_SEQ.NEXTVAL, 'billvoucher.nextDesg', 'python', NULL, NULL, NULL, NULL, 'transitions={''ASSISTANT'':[''SECTION MANAGER''],''SECTION MANAGER'':[''ACCOUNTS OFFICER''],''ACCOUNTS OFFICER'':[''END'']} 
employee = eisManagerBean.getEmpForUserId(userId)
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())  
state=assignment.desigId.designationName
if state in transitions:result=transitions[state]'
,  TO_DATE( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_DATE( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 


update eg_script set script='
result=['' '','' '' ]
employee = eisManagerBean.getEmpForUserId(userId)
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())  
state=assignment.functionary.name + "-" + assignment.desigId.designationName
if ((state == ''UAC-ASSISTANT'') and  (type == ''Purchase'' or type == ''Contingency'' or type == ''Works'' or type == ''Salary'')):
	result[0]="UAC-SECTION MANAGER"
if ((state == ''UAC-SECTION MANAGER'') and  (type == ''Purchase'' or type == ''Contingency'' or type == ''Works'' or type == ''Salary'')):
	result[0]="UAC-ACCOUNTS OFFICER"
	result[1]="UAC-ASSISTANT"
if ((state == ''UAC-ACCOUNTS OFFICER'') and  (type == ''Purchase'' or type == ''Contingency'' or type == ''Works'' or type == ''Salary'')):
	result[0]="UAC-ASSISTANT"
	result[1]="END"'
where name='billvoucher.nextDesg';
		
#DOWN