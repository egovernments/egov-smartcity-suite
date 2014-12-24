#UP

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES ( 
seq_eg_appconfig_values.nextval, (select id from EG_APPCONFIG where key_name='PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK'), sysdate, 'Checked by FMU ASSISTANT'); 

update EG_SCRIPT set SCRIPT = 'from org.egov.pims.dao import EisDAOFactory  
from org.egov.pims.commons import DesignationMaster  
from org.egov.pims.commons.dao import DesignationMasterDAO  
from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov.lib.rjbac.dept import DepartmentImpl  
from org.egov.exceptions import NoSuchObjectException  
from org.egov.exceptions import TooManyValuesException  
from org.egov import EGOVRuntimeException  
from org.egov.infstr.utils import EGovConfig  
from org.egov.pims.utils import EisManagersUtill
from java.lang import Integer
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
eisCommonMgr=EisManagersUtill.getEisCommonsManager()
def uac_asst_approve():  
    try:  
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))  
        pi = persistenceService.getEmpForCurrentUser()  
        wfItem.changeState(''Checked by UAC ASSISTANT'',pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:            
        return (None,e.getErrors())  
def fmu_asst_approve():  
    try:  
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))   
        pi = persistenceService.getEmpForCurrentUser()  
        wfItem.changeState(''Checked by FMU ASSISTANT'',pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:              
        return (None,e.getErrors())  
def fmu_ao_approve():  
    try:  
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))   
        pi = persistenceService.getEmpForCurrentUser()
        persistenceService.finalApproval(wfItem.getVoucherheader().getId())
        wfItem.changeState(''Approved by ''+pi.getEmployeeFirstName()+''(FMU - ACCOUNTS OFFICER)'',pos,comments)
        wfItem.changeState(''END'',pos,comments)  
        return (persistenceService.persist(wfItem),None)  
    except ValidationException,e:              
        return (None,e.getErrors())
' where name='Paymentheader.workflow' ; 


update EG_SCRIPT set SCRIPT=script||'def reject():  
    try:  
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))   
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
result,validationErrors=transitions[action.getName().split(''|'')[0]]()  
' where name='Paymentheader.workflow' ; 

#DOWN