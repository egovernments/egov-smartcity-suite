#UP

INSERT INTO EG_SCRIPT ( ID, NAME, SCRIPT_TYPE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE,
SCRIPT, START_DATE, END_DATE ) VALUES ( 
eg_script_seq.nextval, 'ContraJournalVoucher.workflow.validactions', 'python', NULL, NULL, NULL
, NULL, 'transitions={''OPERATOR'':[''co_approve'',''co_reject''],''SECTION MANAGER'':[''am_approve'',''am_reject''],''ACCOUNTS OFFICER'':[''ao_approve'',''ao_reject'']}
state=persistenceService.getDesginationName()
if state in transitions:result=transitions[state]'
,  TO_Date( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 


Insert into EG_SCRIPT (ID,NAME,SCRIPT_TYPE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,SCRIPT,START_DATE,END_DATE) 
values (eg_script_seq.nextval,'ContraJournalVoucher.workflow','python',null,null,null,null,'from org.egov.pims.dao import EisDAOFactory
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
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
def co_approve():
    update_workflow(''SECTION MANAGER'',wfItem,''CO APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_approve():
    update_workflow(''ACCOUNTS OFFICER'',wfItem,''MANAGER APPROVED'',comments)
    return (persistenceService.persist(wfItem),None)
def ao_approve():
     update_workflow(''ACCOUNTS OFFICER'',wfItem,''END'',comments)
     persistenceService.createVoucherfromPreApprovedVoucher(wfItem)
     return (persistenceService.persist(wfItem),None)
def co_reject():
    update_workflow(''OPERATOR'',wfItem,''END'',comments)
    persistenceService.cancelVoucher(wfItem)
    return (persistenceService.persist(wfItem),None)
def ao_reject():
    update_workflow(''OPERATOR'',wfItem,''AO REJECTED'',comments)
    return (persistenceService.persist(wfItem),None)
def am_reject():
   update_workflow(''OPERATOR'',wfItem,''SECTION MANAGER REJECTED'',comments)
   return (persistenceService.persist(wfItem),None)
def find_desig(designationName):
    designations=persistenceService.findAllBy(''from DesignationMaster dm where designationName=?'',[designationName])
    if not designations: raise ValidationException,[ValidationError(''currentState.owner'',''egf.preapprovedJV.no_next_desig'')]
    return designations[0]
',TO_Date( '01/01/1900 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '01/01/2100 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')); 

update eg_script set script=script||'
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
def update_workflow(designation1, wfItem,designation2,comments):
    pos=find_posForDesignation(designation1,wfItem)
    wfItem.changeState(designation2,pos,comments)
transitions={''co_approve'':co_approve,''co_reject'':co_reject,''am_approve'':am_approve,''ao_approve'':ao_approve,''ao_reject'':ao_reject,''am_reject'':am_reject}
result,validationErrors=transitions[action.getName()]()' where name='ContraJournalVoucher.workflow';


Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'ContraJournalVoucher','co_approve','Forward',null,null,null,null);
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'ContraJournalVoucher','co_reject','Cancel',null,null,null,null);

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'ContraJournalVoucher','am_approve','Forward',null,null,null,null);
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'ContraJournalVoucher','am_reject','Send back',null,null,null,null);

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'ContraJournalVoucher','ao_approve','Approve',null,null,null,null);
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'ContraJournalVoucher','ao_reject','Send back',null,null,null,null);


#DOWN