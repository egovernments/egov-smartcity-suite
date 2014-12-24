#UP
 update eg_wf_actions set description='Save And Forward' where name='uac_asst_approve' and type='Paymentheader' and description='Forward';
 
update eg_script set script='
result=['' '','' '' ]  
employee = eisManagerBean.getEmpForUserId(userId)  
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())    
state=assignment.functionary.name + "-" + assignment.desigId.designationName  
mode = type.split(''|'')[0]  
paymentAmount = type.split(''|'')[1]  
if ((state == ''UAC-ASSISTANT'') and  (mode== ''Payment'') ):  
	result[0]="FMU-ASSISTANT"  
if ((state == ''FMU-ASSISTANT'') and  (mode== ''Payment'')):  
	result[0]="FMU-ACCOUNTS OFFICER"  
	result[1]="UAC-ASSISTANT"  
if ((state == ''FMU-ACCOUNTS OFFICER'') and  (mode== ''Payment'')):  
	result[0]="UAC-ASSISTANT"  
	result[1]="END"' where name='paymentHeader.nextDesg';

update eg_script set script='
from org.egov.pims.dao import EisDAOFactory  
from org.egov.pims.utils import EisManagersUtill  
eisManagerBean = EisManagersUtill.getEisManager()  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
transitions={''UAC-ASSISTANT'':[''uac_asst_approve''],''FMU-ASSISTANT'':[''fmu_asst_approve'',''fmu_asst_reject''],''FMU-ACCOUNTS OFFICER'':[''fmu_ao_approve'',''fmu_ao_reject''],''INVALID'':[''invalid''],''DEFAULT'':[''uac_asst_approve'']}     
state=''DEFAULT''    
result=[]  
if wfItem:
	if wfItem.getCurrentState():     
		state=persistenceService.getFunctionaryAndDesignation()  
if state in transitions:result=transitions[state]' where name='Paymentheader.workflow.validactions';

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
from org.egov.pims.utils import EisManagersUtill  
from java.lang import Integer  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()    
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()    
eisCommonMgr=EisManagersUtill.getEisCommonsManager()  
def uac_asst_approve():    
    try:    
        pos = eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))    
        pi = persistenceService.getEmpForCurrentUser()    
        wfItem.changeState(''Prepared by UAC ASSISTANT'',pos,comments)    
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
def reject():    
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
    user=None' where name='Paymentheader.workflow';
update eg_script set script=script||'    
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
result,validationErrors=transitions[action.getName().split(''|'')[0]]()' where name='Paymentheader.workflow';
 
 #DOWN
 update eg_wf_actions set description='Forward' where name='uac_asst_approve' and type='Paymentheader' and description='Save And Forward';