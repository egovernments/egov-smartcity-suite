#UP

Insert into eg_script (ID,NAME,SCRIPT_TYPE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,SCRIPT,START_DATE,END_DATE) 
values (eg_script_seq.nextval,'BudgetDetail.nextDesg','python',null,null,null,null,'result=['' '','' '' ]',to_date('01-04-2000','dd-MM-yyyy'),to_date('01-04-2100','dd-MM-yyyy'));



update eg_script set script='result=['' '','' '' ]      
employee = eisManagerBean.getEmpForUserId(userId)      
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())       
state=assignment.desigId.designationName      
state=state.upper()   
print state
if (state == ''UPPER GRADE CLERK'' ):      
	result[0]="ANYFUNCTIONARY-Deputy Engineer"      
elif (state == ''DEPUTY ENGINEER''):      
	result[0]="ANYFUNCTIONARY-Senior Grade Clerk"  
	result[1]="ANYFUNCTIONARY-Deputy Engineer"   
elif (state == ''SENIOR GRADE CLERK''):      
	result[0]="ANYFUNCTIONARY-Assistant Superintendent"     
elif (state == ''ASSISTANT SUPERINTENDENT''):      
	result[0]="ANYFUNCTIONARY-Chief Accounts and Finance Officer" 
	result[1]="ANYFUNCTIONARY-Senior Grade Clerk"  
elif (state == ''CHIEF ACCOUNTS AND FINANCE OFFICER''):      
	result[0]="ANYFUNCTIONARY-Municipal Commissioner"
	result[1]="ANYFUNCTIONARY-Senior Grade Clerk"   
else: 
	result[1]="ANYFUNCTIONARY-MEDICAL OFFICER"' where name='BudgetDetail.nextDesg';

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
from org.egov.pims.commons.service import EisCommonsManagerBean  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
def save():  
        return (persistenceService.persist(wfItem),None)  
def hod_approve():  
    try:  
        pos=find_posForDesignation("CHIEF ACCOUNTS OFFICER",wfItem,"A-Accounts Central Cell")  
        wfItem.changeState("ForwardedToFMU",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def fmu_approve():  
    try:  
        pos=find_posForDesignation("FINANCIAL ADVISOR",wfItem,"A-Accounts Central Cell")  
        wfItem.changeState("ForwardedToFA",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def fa_approve():  
    try:  
        pos=find_posForDesignation("DEPUTY COMMISSIONER",wfItem,"G-General")  
        wfItem.changeState("ForwardedToDCRF",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def dc_approve():  
    try:  
        pos=find_posForDesignation("Commissioner",wfItem,"G-General")  
        wfItem.changeState("ForwardedToCommissioner",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors()) ' where name='BudgetDetail.workflow';  
update eg_script set script=script||'
def commissioner_approve():  
    try:  
        pos=find_posForDesignation("Commissioner",wfItem,"G-General")  
        wfItem.changeState("END",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def reject():  
    try:  
        pos=find_posForDesignation("CHIEF ACCOUNTS OFFICER",wfItem,"A-Accounts Central Cell")  
        wfItem.changeState("SentBackToFMU",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def fmu_reject():  
    try:  
        dept=persistenceService.getDepartmentForBudget(wfItem)  
        pos=find_posForDesignation("HOD",wfItem,dept.getDeptName())  
        wfItem.changeState("NEW",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def hod_reject():  
    try:  
        dept=persistenceService.getDepartmentForBudget(wfItem)  
        pos=find_posForDesignation("HOD",wfItem,dept.getDeptName())  
        wfItem.changeState("CANCELLED",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def find_desig(designationName):  
    designations=persistenceService.findAllBy("from DesignationMaster dm where upper(designationName)=upper(?)",[designationName])  
    if not designations:  
        raise ValidationException,[ValidationError("budgetworkflow.no_next_designation",designationName)]  
    return designations[0]  
def find_posForDesignation(designation, wfItem,department):  
        next_desig=find_desig(designation)  
        emp=None  
        user=None  
        try:  
            dept=persistenceService.find("FROM DepartmentImpl DI WHERE upper(DI.deptName) =upper(?)",[department])  
            btypeStr=EGovConfig.getProperty("egf_config.xml", "city", "1", "BoundaryType")  
            htype=persistenceService.find("from HeirarchyTypeImpl where lower(name)=lower(?)",["Administration"])  
            sqlQry="from BoundaryTypeImpl btype where lower(btype.name)=lower(''"+btypeStr+"'') and btype.heirarchyType=?"  
            btype=persistenceService.find(sqlQry,[htype])  
            bndry=persistenceService.find("from BoundaryImpl bndry where bndry.boundaryType=?",[btype])  
            if(designation.upper()=="HOD"):  
                dept=persistenceService.getDepartmentForBudget(wfItem)  
            emp=pimsDAO.getEmployee(dept.getId(),next_desig.getDesignationId(),bndry.getId())  
            user=emp.getUserMaster()  
            pos=persistenceService.getPositionForEmployee(emp)  
            if not pos:  
                raise ValidationException,[ValidationError("budgetworkflow.no_position","budgetworkflow.no_position")]  
        except TooManyValuesException:  
            raise ValidationException,[ValidationError("budgetworkflow.tomany.positions","budgetworkflow.tomany.positions")]  
        except NoSuchObjectException:  
            raise ValidationException,[ValidationError("budgetworkflow.tomany.positions","budgetworkflow.tomany.positions")]  
        except EGOVRuntimeException,e:  
            raise ValidationException,[ValidationError("budgetworkflow.no.employee","budgetworkflow.no.employee")]  
        else:  
            pass  
        return pos  
transitions={  
 "save":save,  
 "save_and_submit":hod_approve,  
 "fmu_approval":fmu_approve,  
 "fmu_save":fmu_approve,  
 "fa_approval":fa_approve,  
 "dc_approval":dc_approve,  
 "commissioner_approval":commissioner_approve,  
 "fmu_rejection":fmu_reject,  
 "rejection":reject,  
 "hod_rejection":hod_reject  
 }  
result,validationErrors=transitions[action.getName().split(''|'')[0]]()' where name='BudgetDetail.workflow';  

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
from org.egov.infstr.utils import EGovConfig  
from org.egov.pims.commons.service import EisCommonsManagerBean  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
def save():  
        return (persistenceService.persist(wfItem),None)  
def hod_approve():  
    try:  
        pos=find_posForDesignation("CHIEF ACCOUNTS OFFICER",wfItem,"A-Accounts Central Cell")  
        wfItem.changeState("ForwardedToFMU",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def fmu_approve():  
    try:  
        pos=find_posForDesignation("FINANCIAL ADVISOR",wfItem,"A-Accounts Central Cell")  
        wfItem.changeState("ForwardedToFA",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def fa_approve():  
    try:  
        pos=find_posForDesignation("DEPUTY COMMISSIONER",wfItem,"G-General")  
        wfItem.changeState("ForwardedToDCRF",pos,comments)  
        transformChildsandDetails("ForwardedToDCRF",wfItem,pos)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def dc_approve():  
    try:  
        pos=find_posForDesignation("Commissioner",wfItem,"G-General")  
        wfItem.changeState("ForwardedToCommissioner",pos,comments)  
        transformChildsandDetails("ForwardedToCommissioner",wfItem,pos)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def commissioner_approve():  
    try:  
        pos=find_posForDesignation("Commissioner",wfItem,"G-General")  
        wfItem.changeState("END",pos,comments)  
        transformChildsandDetails("END",wfItem,pos)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def reject():  
    try:  
        pos=find_posForDesignation("CHIEF ACCOUNTS OFFICER",wfItem,"A-Accounts Central Cell")  
        wfItem.changeState("SentBackToFMU",pos,comments)  
        transformChildsandDetails("SentBackToFMU",wfItem,pos)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def fmu_reject():  
    try:  
        dept=persistenceService.getDepartmentForBudget(wfItem)  
        pos=find_posForDesignation("HOD",wfItem,dept.getDeptName())  
        wfItem.changeState("NEW",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors()) ' where name='Budget.workflow'; 
update eg_script set script=script||'
def hod_reject():  
    try:  
        dept=persistenceService.getDepartmentForBudget(wfItem)  
        pos=find_posForDesignation("HOD",wfItem,dept.getDeptName())  
        wfItem.changeState("CANCELLED",pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:  
        return (None,e.getErrors())  
def find_desig(designationName):  
    designations=persistenceService.findAllBy("from DesignationMaster dm where upper(designationName)=upper(?)",[designationName])  
    if not designations:  
        raise ValidationException,[ValidationError("currentState.owner","egf.budget.no_next_desig")]  
    return designations[0]  
def find_posForDesignation(designation, wfItem,department):  
        next_desig=find_desig(designation)  
        if not next_desig:  
              raise ValidationException,[ValidationError("currentState.owner","egf.budgetworkflow.no_designation")]  
        emp=None  
        user=None  
        try:  
            dept=persistenceService.find("FROM DepartmentImpl DI WHERE upper(DI.deptName) =upper(?)",[department])  
            btypeStr=EGovConfig.getProperty("egf_config.xml", "city", "1", "BoundaryType")  
            htype=persistenceService.find("from HeirarchyTypeImpl where lower(name)=lower(?)",["Administration"])  
            sqlQry="from BoundaryTypeImpl btype where lower(btype.name)=lower(''"+btypeStr+"'') and btype.heirarchyType=?"  
            btype=persistenceService.find(sqlQry,[htype])  
            bndry=persistenceService.find("from BoundaryImpl bndry where bndry.boundaryType=?",[btype])  
            if(designation.upper()=="HOD"):  
                dept=persistenceService.getDepartmentForBudget(wfItem)  
            emp=pimsDAO.getEmployee(dept.getId(),next_desig.getDesignationId(),bndry.getId())  
            user=emp.getUserMaster()  
            pos=persistenceService.getPositionForEmployee(emp)  
            if not pos:  
                raise ValidationException,[ValidationError("currentState.owner","egf.budgetworkflow.no_position")]  
        except TooManyValuesException:  
            raise ValidationException,[ValidationError("budgetworkflow.tomany.positions","budgetworkflow.tomany.positions")]  
        except NoSuchObjectException:  
            raise ValidationException,[ValidationError("budgetworkflow.tomany.positions","budgetworkflow.tomany.positions")]  
        except EGOVRuntimeException:  
            raise ValidationException,[ValidationError("budgetworkflow.no.employee","budgetworkflow.no.employee")]  
        else:  
            pass  
        return pos  
def transformChildsandDetails(state,wfItem,pos):  
    budgetList=persistenceService.moveBudgetTree(wfItem,pos)  
    if budgetList:  
        for budget in budgetList:  
            budget.changeState(state,pos,"")  
    persistenceService.moveDetailsTree(budgetList,wfItem)  
    parent=wfItem.getParent()  
    if parent:  
        parentBudget=persistenceService.moveParent(pos,wfItem)  
        if parentBudget:  
            parentBudget.changeState(state,pos,"")  
def transformDetails(state,pos,wfItem):  
    persistenceService.moveDetails(wfItem)  
transitions={  
 "save":save,  
 "save_and_submit":hod_approve,  
 "fmu_approval":fmu_approve,  
 "fmu_save":fmu_approve,  
 "fa_approval":fa_approve,  
 "dc_approval":dc_approve,  
 "commissioner_approval":commissioner_approve,  
 "fmu_rejection":fmu_reject,  
 "rejection":reject,  
 "hod_rejection":hod_reject  
 }  
result,validationErrors=transitions[action.getName().split(''|'')[0]]()' where name='Budget.workflow';
#DOWN
