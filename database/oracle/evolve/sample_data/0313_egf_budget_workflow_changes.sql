#UP

Insert into eg_script (ID,NAME,SCRIPT_TYPE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,SCRIPT,START_DATE,END_DATE) 
values (eg_script_seq.nextval,'BudgetDetail.enable.amounts','python',null,null,null,null,'result=['' '','' '' ]',to_date('01-04-2000','dd-MM-yyyy'),to_date('01-04-2100','dd-MM-yyyy'));

Insert into eg_script (ID,NAME,SCRIPT_TYPE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,SCRIPT,START_DATE,END_DATE) 
values (eg_script_seq.nextval,'BudgetDetail.get.default.department','python',null,null,null,null,'result=['' '','' '' ]',to_date('01-04-2000','dd-MM-yyyy'),to_date('01-04-2100','dd-MM-yyyy'));

update eg_script set script='
from org.egov.pims.dao import EisDAOFactory
from java.util import Date
result=""
employee = eisManagerBean.getEmpForUserId(userId)        
assignment  = eisManagerBean.getAssignmentByEmpAndDate(Date(),employee.getIdPersonalInformation())   
state=assignment.desigId.designationName.upper()
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
hods=empDeptDAO.getAllHodEmpByDept(assignment.deptId.id)
if(hods.contains(employee)):
	result="A-Accounts Central Cell"
else:
	result=""    
if(state in ["SECTION MANAGER","ACCOUNTS OFFICER","CHIEF ACCOUNTS OFFICER","ASSISTANT","FINANCIAL ADVISOR"]):
	result="A-Accounts Central Cell"
elif(state in ["DEPUTY COMMISSIONER"]):
	result="G-General"
' where name='BudgetDetail.get.default.department';

update eg_script set script='
from org.egov.pims.dao import EisDAOFactory
result=['' '','' '']        
employee = eisManagerBean.getEmpForUserId(userId)        
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())   
if (assignment.functionary!=None):    
	state=assignment.functionary.name+''-''+assignment.desigId.designationName
else:
	state=''-''+assignment.desigId.designationName
state=state.upper()     
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
hods=empDeptDAO.getAllHodEmpByDept(assignment.deptId.id)
if(hods.contains(employee)):
	result[0]="FMU-ASSISTANT"
else:
	result[0]="ANYFUNCTIONARY-ANYDESG"    
if(state=="FMU-ASSISTANT"):
	result[0]="FMU-SECTION MANAGER" 
elif(state=="FMU-SECTION MANAGER"):
	result[0]="FMU-ACCOUNTS OFFICER" 
elif(state=="FMU-ACCOUNTS OFFICER"):
	result[0]="FMU-CHIEF ACCOUNTS OFFICER" 
elif(state=="FMU-CHIEF ACCOUNTS OFFICER"):
	result[0]="ANYFUNCTIONARY-FINANCIAL ADVISOR"
elif(state.split(''-'')[1]=="FINANCIAL ADVISOR"):
	result[0]="ANYFUNCTIONARY-DEPUTY COMMISSIONER"
elif(state.split(''-'')[1]=="DEPUTY COMMISSIONER"):
	result[0]="ANYFUNCTIONARY-COMMISSIONER" 
' where name='BudgetDetail.nextDesg';

update eg_script set script='
from org.egov.pims.dao import PersonalInformationDAO    
from org.egov.pims.dao import EisDAOFactory  
from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException   
from org.egov import EGOVRuntimeException  
from org.egov.pims.utils import EisManagersUtill
from java.util import Date
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
eisManagerBean = EisManagersUtill.getEisManager()
def getAmountToEnable():  
	result=[]   
	pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()     
	if wfItem.getCurrentState():  
		try:  
			user=persistenceService.getUser()    
			emp=pimsDAO.getPersonalInformationByUserId(user.getId())  
			assignment  = eisManagerBean.getAssignmentByEmpAndDate(Date(),emp.getIdPersonalInformation())
			designationName = assignment.desigId.designationName.upper()
			if(designationName in ["DEPUTY COMMISSIONER","COMMISSIONER","FINANCIAL ADVISOR","CHIEF ACCOUNTS OFFICER","ACCOUNTS OFFICER","ASSISTANT","SECTION MANAGER","SECTIONMANAGER"]):
				return "approved"
			else:
				return "original"
			return ""  
		except EGOVRuntimeException,e:  
			return ""    
result=getAmountToEnable()' where name='BudgetDetail.enable.amounts';

update eg_script set script='
from org.egov.pims.dao import PersonalInformationDAO    
from org.egov.pims.dao import EisDAOFactory  
from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException   
from org.egov import EGOVRuntimeException  
from org.egov.pims.utils import EisManagersUtill
from java.util import Date
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
eisManagerBean = EisManagersUtill.getEisManager()
def getButtons():  
	state="DEFAULT"    
	result=[]   
	pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()     
	if wfItem.getCurrentState():  
		try:  
			user=persistenceService.getUser()    
			emp=pimsDAO.getPersonalInformationByUserId(user.getId())  
			assignment  = eisManagerBean.getAssignmentByEmpAndDate(Date(),emp.getIdPersonalInformation())
			designationName = assignment.desigId.designationName.upper()      
			if(designationName in ["DEPUTY COMMISSIONER","COMMISSIONER","FINANCIAL ADVISOR","CHIEF ACCOUNTS OFFICER","ACCOUNTS OFFICER","ASSISTANT","SECTION MANAGER"]):
				state = designationName
			return (transitions[state],None)  
		except EGOVRuntimeException,e:  
			return (None,[ValidationError("Department is not mapped for budget","Department is not mapped for Budget")])    
transitions={"DEFAULT":["save","forward"],"COMMISSIONER":["save","approve"],"DEPUTY COMMISSIONER":["save","forward"],"FINANCIAL ADVISOR":["save","forward"],"CHIEF ACCOUNTS OFFICER":["save","forward"],"ACCOUNTS OFFICER":["save","forward"],"ASSISTANT":["save","forward"],"SECTION MANAGER":["save","forward"]}    
result,validationErrors=getButtons()' where name='Budget.workflow.validactions';

update eg_script set script='
from org.egov.pims.dao import PersonalInformationDAO    
from org.egov.pims.dao import EisDAOFactory  
from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException   
from org.egov import EGOVRuntimeException  
from org.egov.pims.utils import EisManagersUtill
from java.util import Date
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
eisManagerBean = EisManagersUtill.getEisManager()
def getButtons():  
	state="DEFAULT"    
	result=[]   
	pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()     
	if wfItem.getCurrentState():  
		try:  
			user=persistenceService.getUser()    
			emp=pimsDAO.getPersonalInformationByUserId(user.getId())  
			assignment = eisManagerBean.getAssignmentByEmpAndDate(Date(),emp.getIdPersonalInformation())
			designationName = assignment.desigId.designationName.upper()      
			if(designationName in ["DEPUTY COMMISSIONER","COMMISSIONER","FINANCIAL ADVISOR","CHIEF ACCOUNTS OFFICER","ACCOUNTS OFFICER","ASSISTANT","SECTION MANAGER","SECTIONMANAGER"]):
				state = designationName
			return (transitions[state],None)  
		except EGOVRuntimeException,e:  
			return (None,[ValidationError("Department is not mapped for budget","Department is not mapped for Budget")])    
transitions={"DEFAULT":["save","forward"],"COMMISSIONER":["save","approve"],"DEPUTY COMMISSIONER":["save","forward"],"FINANCIAL ADVISOR":["save","forward"],"CHIEF ACCOUNTS OFFICER":["save","forward"],"ACCOUNTS OFFICER":["save","forward"],"ASSISTANT":["save","forward"],"SECTION MANAGER":["save","forward"]}    
result,validationErrors=getButtons()' where name='BudgetDetail.workflow.validactions';

update eg_script set script='
from java.util import List   
from org.egov.pims.dao import EisDAOFactory   
from org.egov.pims.commons import DesignationMaster   
from org.egov.pims.commons.dao import DesignationMasterDAO   
from org.egov.infstr import ValidationError   
from org.egov.infstr import ValidationException   
from org.egov.lib.rjbac.dept import DepartmentImpl   
from  org.egov.exceptions import NoSuchObjectException   
from org.egov.exceptions import TooManyValuesException   
from org.egov import EGOVRuntimeException   
from org.egov.pims.utils import EisManagersUtill 
from org.egov.infstr.utils import EGovConfig 
from java.lang import Integer  
from org.egov.pims.commons.service import EisCommonsManagerBean   
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()   
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO() 
eisCommonMgr=EisManagersUtill.getEisCommonsManager()  
def save():   
	return (persistenceService.persist(wfItem),None)   
def forward():   
    try:   
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
        pi = persistenceService.getEmpForCurrentUser()
        name = ""
        if(pi.getName()!=None):
            name = pi.getName()
        wfItem.changeState("Forwarded by "+name,pos,comments) 
        transformChildsandDetails("Forwarded by "+pi.getName(),wfItem,pos,action.getName())   
        return (persistenceService.persist(wfItem),None)   
    except ValidationException,e:   
        return (None,e.getErrors())   
def approve():   
    try:   
        pos=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))   
        wfItem.changeState("END",pos,comments)   
        transformChildsandDetails("END",wfItem,pos,action.getName())   
        return (persistenceService.persist(wfItem),None)   
    except ValidationException,e:   
        return (None,e.getErrors())   
def transformChildsandDetails(state,wfItem,pos,actionName):  
    budgetList=persistenceService.moveBudgetTree(wfItem,pos)
    if budgetList:  
        for budget in budgetList:  
            budget.changeState(state,pos,"")  
    if wfItem.getParent() and persistenceService.canForwardParent(pos,wfItem):
        wfItem.getParent().changeState(state,pos,"")  
transitions={"save":save,"forward":forward,"approve":approve}  
result,validationErrors=transitions[action.getName().split(''|'')[0]]() ' where name='Budget.workflow';

update eg_script set script='
from org.egov.pims.dao import EisDAOFactory   
from org.egov.pims.commons import DesignationMaster   
from org.egov.pims.commons.dao import DesignationMasterDAO   
from org.egov.infstr import ValidationError   
from org.egov.infstr import ValidationException   
from org.egov.lib.rjbac.dept import DepartmentImpl   
from  org.egov.exceptions import NoSuchObjectException   
from org.egov.exceptions import TooManyValuesException   
from org.egov import EGOVRuntimeException   
from org.egov.infstr.utils import EGovConfig   
from org.egov.pims.utils import EisManagersUtill
from java.lang import Integer 
from org.egov.pims.commons.service import EisCommonsManagerBean   
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()   
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
eisCommonMgr=EisManagersUtill.getEisCommonsManager() 
def save():   
        return (persistenceService.persist(wfItem),None)   
def forward():   
    try:
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))
        pi = persistenceService.getEmpForCurrentUser()
        name = ""
        if(pi.getName()!=None):
            name = pi.getName()
        wfItem.changeState("Forwarded by "+name,pos,comments)
        return (persistenceService.persist(wfItem),None)   
    except ValidationException,e:   
        return (None,e.getErrors()) 
def approve():  
    try:  
        pos=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))  
        wfItem.changeState("END",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors()) 
transitions={"save":save,"forward":forward,"approve":approve}  
result,validationErrors=transitions[action.getName().split(''|'')[0]]() ' where name='BudgetDetail.workflow';

delete from EG_WF_ACTIONS where type='Budget';

#DOWN



