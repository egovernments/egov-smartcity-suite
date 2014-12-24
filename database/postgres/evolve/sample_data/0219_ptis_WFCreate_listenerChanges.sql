#UP

UPDATE EG_SCRIPT SET SCRIPT='' WHERE name = 'WorkflowProperty.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'from org.egov.ptis.property.utils import COCPtUtils 
from org.egov.infstr.client.filter import EGOVThreadLocals 
def save_new():
    if not wfItem.getCurrentStateValue():  
        wfItem.changeState(''NEW'',nextPostion(),''Create Property WorkFlow Started'')  
        save_new()  
    elif wfItem.getCurrentStateValue() == ''NEW'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def save_update():  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
        wfItem.changeState(''ARO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''ZO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        wfItem.changeState(''RO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        wfItem.changeState(''DCR_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None'WHERE name = 'WorkflowProperty.workflow';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def save_submit():  
    wfItem.changeState(''Assistant_Notice_Print_Pending'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
    return persistenceService.persist(wfItem)  
    return None  
def reject():  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
	user = cocPtUtils.getWorkflowInitiator(wfItem)
        if user.getUserName() == ''citizenUser'': 
           wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''citizenUser'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())
        else:
           wfItem.changeState(''Assistant_Submission_Pending'',nextRejectPosition(),wfItem.getRemarks()) 
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        wfItem.changeState(''ARO_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        wfItem.changeState(''ZO_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''DCR_Approval_Pending'':  
        wfItem.changeState(''RO_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None'WHERE name = 'WorkflowProperty.workflow';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT|| '  
def nextPostion():  
    if not wfItem.getCurrentStateValue():  
        if getUser().getUserName() == ''citizenUser'':
           postion = cocPtUtils.getPositionForUser(''Revenue'', ''citizenUser'', wfItem.getBoundary(),wfItem)    
        else:
           postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem)
        return postion   
    if wfItem.getCurrentStateValue() == ''NEW'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ARO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''Zonal Officer'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''RO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''DCR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assistant_Notice_Print_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
        return None'WHERE name = 'WorkflowProperty.workflow';  

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'        
def nextRejectPosition():  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ARO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''Zonal Officer'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''DCR_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''RO'', wfItem.getBoundary(),wfItem)  
        return postion  
    return None'WHERE name = 'WorkflowProperty.workflow';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'       
def generate_notice():  
    if wfItem.getCurrentStateValue() == ''Assistant_Notice_Print_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),''Create Property WorkFlow End'')  
        return persistenceService.persist(wfItem)  
    return None  
def cancel():  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def getUser():    
    return persistenceService.find("from UserImpl where id="+EGOVThreadLocals.getUserId())
transitions={  
 ''SAVE_NEW'':save_new,  
 ''SAVE_UPDATE'':save_update,  
 ''SAVE_SUBMIT'':save_submit,  
 ''REJECT'':reject,  
 ''GENNOTICE'':generate_notice,  
 ''CANCEL'':cancel  
}  
cocPtUtils = COCPtUtils()  
result=transitions[action.getName()]()' WHERE name = 'WorkflowProperty.workflow';


--name transfer
UPDATE EG_SCRIPT SET SCRIPT='' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'from org.egov.ptis.property.utils import COCPtUtils
from org.egov.infstr.client.filter import EGOVThreadLocals 
from org.egov.pims.service import EisManagerBean
from java.lang import Integer
from java.util import Date
def save_new():  
    if not wfItem.getCurrentState():  
        wfItem.changeState(''NEW'',nextPostion(),wfItem.getRemarks())  
        save_new()  
    elif wfItem.getCurrentState().getValue() == ''NEW'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def save_update():  
    if wfItem.getCurrentState().getValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        wfItem.changeState(''ARO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def save_submit():  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''Assistant_Notice_Print_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def reject():  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        wfItem.changeState(''Assistant_Submission_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def nextPostion():  
    if not wfItem.getCurrentState():  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''NEW'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assistant_Submission_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ARO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assistant_Notice_Print_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    return None' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def nextRejectPosition():  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    return None
def getUserDepartment():
    employee = eisMgr.getEmpForUserId(Integer.parseInt(EGOVThreadLocals.getUserId()))
    empAssignment = eisMgr.getAssignmentByEmpAndDate(Date(), employee.getIdPersonalInformation())
    return empAssignment.getDeptId().getDeptName()' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def generate_notice():  
    if wfItem.getCurrentState().getValue() == ''Assistant_Notice_Print_Pending'':  
        wfItem.changeState(''END'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def cancel():  
    if wfItem.getCurrentState().getValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
transitions={  
 ''SAVE_NEW'':save_new,  
 ''SAVE_UPDATE'':save_update,  
 ''SAVE_SUBMIT'':save_submit,  
 ''REJECT'':reject,  
 ''GENNOTICE'':generate_notice,  
 ''CANCEL'':cancel  
}  
cocPtUtils = COCPtUtils()
eisMgr = EisManagerBean()
result=transitions[action.getName()]()' WHERE name = 'PropWorkFlowMutation.workflow';

#DOWN

UPDATE EG_SCRIPT SET SCRIPT='' WHERE name = 'WorkflowProperty.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'from org.egov.ptis.property.utils import COCPtUtils  
def save_new():
    if not wfItem.getCurrentStateValue():  
        wfItem.changeState(''NEW'',nextPostion(),''Create Property WorkFlow Started'')  
        save_new()  
    elif wfItem.getCurrentStateValue() == ''NEW'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def save_update():  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
        wfItem.changeState(''ARO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''ZO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        wfItem.changeState(''RO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        wfItem.changeState(''DCR_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None'WHERE name = 'WorkflowProperty.workflow';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def save_submit():  
    wfItem.changeState(''Assistant_Notice_Print_Pending'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
    return persistenceService.persist(wfItem)  
    return None  
def reject():  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
	user = cocPtUtils.getWorkflowInitiator(wfItem)
        if user.getUserName() == ''citizenUser'': 
           wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''citizenUser'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())
        else:
           wfItem.changeState(''Assistant_Submission_Pending'',nextRejectPosition(),wfItem.getRemarks()) 
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        wfItem.changeState(''ARO_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        wfItem.changeState(''ZO_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentStateValue() == ''DCR_Approval_Pending'':  
        wfItem.changeState(''RO_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None'WHERE name = 'WorkflowProperty.workflow';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT|| '  
def nextPostion():  
    if not wfItem.getCurrentStateValue():  
        if wfItem.getCreatedBy().getUserName() == ''citizenUser'':
           postion = cocPtUtils.getPositionForUser(''Revenue'', ''citizenUser'', wfItem.getBoundary(),wfItem)    
        else:
           postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem)
        return postion   
    if wfItem.getCurrentStateValue() == ''NEW'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ARO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''Zonal Officer'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''RO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''DCR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assistant_Notice_Print_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
        return None'WHERE name = 'WorkflowProperty.workflow';  

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'        
def nextRejectPosition():  
    if wfItem.getCurrentStateValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''ZO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''ARO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''RO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''Zonal Officer'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentStateValue() == ''DCR_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(''Revenue'', ''RO'', wfItem.getBoundary(),wfItem)  
        return postion  
    return None'WHERE name = 'WorkflowProperty.workflow';

UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'       
def generate_notice():  
    if wfItem.getCurrentStateValue() == ''Assistant_Notice_Print_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),''Create Property WorkFlow End'')  
        return persistenceService.persist(wfItem)  
    return None  
def cancel():  
    if wfItem.getCurrentStateValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(''Revenue'', ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
transitions={  
 ''SAVE_NEW'':save_new,  
 ''SAVE_UPDATE'':save_update,  
 ''SAVE_SUBMIT'':save_submit,  
 ''REJECT'':reject,  
 ''GENNOTICE'':generate_notice,  
 ''CANCEL'':cancel  
}  
cocPtUtils = COCPtUtils()  
result=transitions[action.getName()]()'WHERE name = 'WorkflowProperty.workflow';

--name transfer
UPDATE EG_SCRIPT SET SCRIPT='' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'from org.egov.ptis.property.utils import COCPtUtils
from org.egov.pims.service import EisManagerBean
from java.util import Date
def save_new():  
    if not wfItem.getCurrentState():  
        wfItem.changeState(''NEW'',nextPostion(),wfItem.getRemarks())  
        save_new()  
    elif wfItem.getCurrentState().getValue() == ''NEW'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def save_update():  
    if wfItem.getCurrentState().getValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        wfItem.changeState(''ARO_Approval_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def save_submit():  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''Assistant_Notice_Print_Pending'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def reject():  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        wfItem.changeState(''Assistant_Submission_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        wfItem.changeState(''Assessor_Approval_Pending'',nextRejectPosition(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def nextPostion():  
    if not wfItem.getCurrentState():  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''NEW'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assistant_Submission_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ARO'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''Assistant_Notice_Print_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    return None' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def nextRejectPosition():  
    if wfItem.getCurrentState().getValue() == ''Assessor_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem)  
        return postion  
    if wfItem.getCurrentState().getValue() == ''ARO_Approval_Pending'':  
        postion = cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSESSOR'', wfItem.getBoundary(),wfItem)  
        return postion  
    return None
def getUserDepartment():
    employee = eisMgr.getEmpForUserId(wfItem.getCreatedBy().getId())
    empAssignment = eisMgr.getAssignmentByEmpAndDate(Date(), employee.getIdPersonalInformation())
    return empAssignment.getDeptId().getDeptName()' WHERE name = 'PropWorkFlowMutation.workflow';
UPDATE EG_SCRIPT SET SCRIPT=SCRIPT||'
def generate_notice():  
    if wfItem.getCurrentState().getValue() == ''Assistant_Notice_Print_Pending'':  
        wfItem.changeState(''END'',nextPostion(),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
def cancel():  
    if wfItem.getCurrentState().getValue() == ''Assistant_Submission_Pending'':  
        wfItem.changeState(''END'',cocPtUtils.getPositionForUser(getUserDepartment(), ''ASSISTANT'', wfItem.getBoundary(),wfItem),wfItem.getRemarks())  
        return persistenceService.persist(wfItem)  
    return None  
transitions={  
 ''SAVE_NEW'':save_new,  
 ''SAVE_UPDATE'':save_update,  
 ''SAVE_SUBMIT'':save_submit,  
 ''REJECT'':reject,  
 ''GENNOTICE'':generate_notice,  
 ''CANCEL'':cancel  
}  
cocPtUtils = COCPtUtils()
eisMgr = EisManagerBean()
result=transitions[action.getName()]()' WHERE name = 'PropWorkFlowMutation.workflow';
