#UP

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
SCRIPT, START_DATE, END_DATE ) VALUES ( 
eg_script_seq.nextval, 'Paymentheader.workflow.validactions', 'python', NULL, NULL, NULL
, NULL, 'from org.egov.pims.dao import EisDAOFactory
from org.egov.pims.utils import EisManagersUtill
eisManagerBean = EisManagersUtill.getEisManager()
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
transitions={''UAC-ASSISTANT'':[''uac_asst_approve''],''FMU-ASSISTANT'':[''fmu_asst_approve'',''fmu_asst_reject''],''FMU-ACCOUNTS OFFICER'':[''fmu_ao_approve'',''fmu_ao_reject''],''INVALID'':[''invalid'']}   
state=''DEFAULT''  
result=[]
if wfItem.getCurrentState():   
    state=persistenceService.getFunctionaryAndDesignation()
if state in transitions:result=transitions[state]'
,  TO_Date( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 


INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
SCRIPT, START_DATE, END_DATE ) VALUES ( 
eg_script_seq.nextval, 'Paymentheader.workflow','python',null,null,null,null,'from org.egov.pims.dao import EisDAOFactory  
from org.egov.pims.commons import DesignationMaster  
from org.egov.pims.commons.dao import DesignationMasterDAO  
from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov.lib.rjbac.dept import DepartmentImpl  
from org.egov.exceptions import NoSuchObjectException  
from org.egov.exceptions import TooManyValuesException  
from org.egov import EGOVRuntimeException  
from org.egov.infstr.utils import EGovConfig  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
def uac_asst_approve():  
    try:  
        pos=find_posForDesignation(''ASSISTANT'',wfItem,wfItem.getVoucherheader().getVouchermis().getDepartmentid().getDeptCode(),''FMU'')  
        pi = persistenceService.getEmpForCurrentUser()  
        wfItem.changeState(''Forwarded by ''+pi.getEmployeeFirstName()+''(UAC - ASSISTANT)'',pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:            
        return (None,e.getErrors())  
def fmu_asst_approve():  
    try:  
        pos=find_posForDesignation(''ACCOUNTS OFFICER'',wfItem,wfItem.getVoucherheader().getVouchermis().getDepartmentid().getDeptCode(),''FMU'')  
        pi = persistenceService.getEmpForCurrentUser()  
        wfItem.changeState(''Forwarded by ''+pi.getEmployeeFirstName()+''(FMU - ASSISTANT)'',pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:              
        return (None,e.getErrors())  
def fmu_ao_approve():  
    try:  
        pos=find_posForDesignation(''ACCOUNTS OFFICER'',wfItem,wfItem.getVoucherheader().getVouchermis().getDepartmentid().getDeptCode(),''FMU'')  
        pi = persistenceService.getEmpForCurrentUser()
        persistenceService.finalApproval(wfItem.getVoucherheader().getId())
        wfItem.changeState(''Approved by ''+pi.getEmployeeFirstName()+''(FMU - ACCOUNTS OFFICER)'',pos,comments)
        wfItem.changeState(''END'',pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:              
        return (None,e.getErrors())
'
,  TO_Date( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 


update EG_SCRIPT set SCRIPT=script||'def reject():  
    try:  
        pos=find_posForDesignation(''ASSISTANT'',wfItem,wfItem.getVoucherheader().getVouchermis().getDepartmentid().getDeptCode(),''UAC'')  
        pi = persistenceService.getEmpForCurrentUser()
        desigAndFunction=  persistenceService.getFunctionaryAndDesignation()
        wfItem.changeState(''Rejected by ''+pi.getEmployeeFirstName()+''(''+desigAndFunction+'')'',pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:              
        return (None,e.getErrors()) 
def find_desig(designationName):  
    try:  
        designations=persistenceService.findAllBy(''from DesignationMaster dm where upper(designationName)=upper(?)'',[designationName])  
        if not designations: raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_next_desig'')]  
        return designations[0]  
    except ValidationException,e:              
        return (None,e.getErrors())  
def find_posForDesignation(designation, wfItem,deptcode,functionaryCode):  
    next_desig=find_desig(designation)  
    if not next_desig:  
        raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_designation'')]  
    emp=None  
    user=None  
    try:
        dept=persistenceService.find(''FROM DepartmentImpl DI WHERE DI.deptCode =?'',[deptcode])  
        btypeStr=EGovConfig.getProperty("egf_config.xml", "city", "1", "BoundaryType")  
        htype=persistenceService.find("from HeirarchyTypeImpl where lower(name)=lower(?)",["Administration"])  
        sqlQry="from BoundaryTypeImpl btype where lower(btype.name)=lower(''"+btypeStr+"'') and btype.heirarchyType=?"  
        btype=persistenceService.find(sqlQry,[htype])  
        bndry=persistenceService.find("from BoundaryImpl bndry where bndry.boundaryType=?",[btype])  
        functionary = persistenceService.find("from Functionary where name=?",[functionaryCode])  
        emp=pimsDAO.getEmployeeByFunctionary(dept.getId(),next_desig.getDesignationId(),bndry.getId(),functionary.getId())  
        pos=persistenceService.getPositionForEmployee(emp)  
    except NoSuchObjectException:  
        raise ValidationException,[ValidationError(''NoSuchObjectException'',''NoSuchObjectException'')]  
    except EGOVRuntimeException:  
        raise ValidationException,[ValidationError(''currentState.owner'',''Personal Information not mapped'')]  
    except Exception,e:  
        raise ValidationException,[ValidationError(''currentState.owner'',''Personal Information not mapped'')]  
    if not pos:  
        raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_position'')]  
    return pos  
transitions={''uac_asst_approve'':uac_asst_approve,''fmu_asst_approve'':fmu_asst_approve,''fmu_asst_reject'':reject,''fmu_ao_approve'':fmu_ao_approve,''fmu_ao_reject'':reject}  
result,validationErrors=transitions[action.getName()]()  
' where name='Paymentheader.workflow' ; 

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
SCRIPT, START_DATE, END_DATE ) VALUES ( 
eg_script_seq.nextval, 'Paymentheader.show.bankbalance', 'python', NULL, NULL, NULL
, NULL, 'transitions={''true'':[''true''],''false'':[''false'']}
state=''false''   
val=persistenceService.getFunctionaryAndDesignation()
if((val==''FMU-ASSISTANT'') and purpose==''balancecheck''):  
    state=''true'' 
if(val==''UAC-ASSISTANT'' and purpose==''createpayment''):  
    state=''true''  
if state in transitions:result=transitions[state]'
,  TO_Date( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 

#DOWN