#UP
update eg_script set script='from org.egov.pims.dao import EisDAOFactory  
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
from org.egov.infstr.client.filter import EGOVThreadLocals  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
egBillRegDao=BillsDaoFactory.getDAOFactory().getEgBillRegisterHibernateDAO()  
eisCommonMgr=EisManagersUtill.getEisCommonsManager()  
expType=egBillRegDao.getBillTypeforVoucher(wfItem)  
def aa_approve():  
    if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):  
        update_workflow(wfItem,''AA APPROVED'',comments)
    else:
        workflowUpdate(''SECTION MANAGER'',wfItem,''CO APPROVED'',comments)  
    return (persistenceService.persist(wfItem),None)  
def am_approve():  
    if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):  
        update_workflow(wfItem,''AM APPROVED'',comments)  
    else:
        workflowUpdate(''ACCOUNTS OFFICER'',wfItem,''SECTION MANAGER APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)  
def ao_approve():  
    if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):  
        update_workflow(wfItem,''END'',comments)  
    else:
        workflowUpdate(''ACCOUNTS OFFICER'',wfItem,''END'',comments)
    persistenceService.createVoucherfromPreApprovedVoucher(wfItem)  
    return (persistenceService.persist(wfItem),None)  
def ao_reject():  
    if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):  
        update_workflow(wfItem,''AO REJECTED'',comments)
    else:
        workflowUpdate(''OPERATOR'',wfItem,''AO REJECTED'',comments)  
    return (persistenceService.persist(wfItem),None)  
def am_reject():  
    if (expType == ''Purchase'' or expType == ''Contingency'' or expType == ''Works'' or expType == ''Salary''):  
        update_workflow(wfItem,''AM REJECTED'',comments)
    else:
        workflowUpdate(''OPERATOR'',wfItem,''SECTION MANAGER REJECTED'',comments)
    return (persistenceService.persist(wfItem),None)  
def aa_reject():
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()))  
    wfItem.changeState(''END'',position,comments)
    persistenceService.cancelVoucher(wfItem)
    return (persistenceService.persist(wfItem),None)
    ' where name='CVoucherHeader.workflow';
    
update eg_script set script=script||'
def update_workflow(wfItem,wfItemStatus,comments):  
    position=eisCommonMgr.getPositionByUserId(Integer.valueOf(action.getName().split(''|'')[1]))  
    wfItem.changeState(wfItemStatus,position,comments)  
def find_desig(designationName):
    designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'',[designationName])
    if not designations: raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_next_desig'')]
    return designations[0]
def find_posForDesignation(designation, wfItem):
    next_desig=find_desig(designation)
    if not next_desig:
        raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_designation'')]
    emp=None
    user=None
    try:
        btypeStr=EGovConfig.getProperty("egf_config.xml", "city", "1", "BoundaryType")
        htype=persistenceService.find("from HeirarchyTypeImpl where lower(name)=lower(?)",["Administration"])
        sqlQry="from BoundaryTypeImpl btype where lower(btype.name)=lower(''"+btypeStr+"'') and btype.heirarchyType=?"
        btype=persistenceService.find(sqlQry,[htype])
        bndry=persistenceService.find("from BoundaryImpl bndry where bndry.boundaryType=?",[btype])
        dept=persistenceService.getDepartmentForWfItem(wfItem)
        if(dept.getDeptCode()==''A''):
            functionary = persistenceService.find("from Functionary where upper(name)=?",["TREASURY"])
            emp=pimsDAO.getEmployeeByFunctionary(dept.getId(),next_desig.getDesignationId(),bndry.getId(),functionary.getId())
        else:    
            emp=pimsDAO.getEmployee(dept.getId(),next_desig.getDesignationId(),bndry.getId())
        pos=persistenceService.getPositionForEmployee(emp)
    except TooManyValuesException:
        raise ValidationException,[ValidationError(''TooManyValuesException'',''TooManyValuesException'')]
    except NoSuchObjectException:
        raise ValidationException,[ValidationError(''NoSuchObjectException'',''NoSuchObjectException'')]
    except EGOVRuntimeException:
        raise ValidationException,[ValidationError(''EGOVRuntimeException'',''TooManyValuesException'')]
    else:
        pass
    if not pos:
        raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_position'')]
    return pos
def workflowUpdate(designation1, wfItem,designation2,comments):
    pos=find_posForDesignation(designation1,wfItem)
    wfItem.changeState(designation2,pos,comments)
transitions={''aa_approve'':aa_approve,''aa_reject'':aa_reject,''am_approve'':am_approve,''ao_approve'':ao_approve,''ao_reject'':ao_reject,''am_reject'':am_reject}  
result,validationErrors=transitions[action.getName().split(''|'')[0]]()' where name='CVoucherHeader.workflow';

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (eg_wf_actions_seq.nextval,'CVoucherHeader','aa_reject','Cancel',null,null,null,null);

update eg_script set script ='
transitions={''ASSISTANT'':[''aa_approve'',''aa_reject''],''SECTION MANAGER'':[''am_approve'',''am_reject''],''ACCOUNTS OFFICER'':[''ao_approve'',''ao_reject''],''INVALID'':[''invalid'']}
employee = eisManagerBean.getEmpForUserId(userId)
assignment = eisManagerBean.getAssignmentByEmpAndDate(date,employee.getIdPersonalInformation())
state=assignment.desigId.designationName
if(state.lower()!=''assistant'' and purpose==''designation''):
    state=''INVALID''
if state in transitions:result=transitions[state]' where name = 'pjv.validbuttons';

#DOWN