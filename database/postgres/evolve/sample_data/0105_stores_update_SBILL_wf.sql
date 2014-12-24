#UP
UPDATE EG_SCRIPT SET script = ('from org.egov.pims.dao import EisDAOFactory
from org.egov.pims.commons import DesignationMaster
from org.egov.pims.commons.dao import DesignationMasterDAO
from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from org.egov.exceptions import NoSuchObjectException
from org.egov.pims.utils import EisManagersUtill
from org.egov.pims.commons.dao import PositionMasterDAO
from org.egov.inventory.common import StoresConstants
from org.egov.model.bills import EgBillregistermis
import datetime
from java.text import SimpleDateFormat 
from java.util import Date 
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
eisCommonsManager = EisManagersUtill.getEisCommonsManager()
commonsManager = EisManagersUtill.getCommonsManager()

def save():
    element=None
    element=find_prev_next(wfItem.getCurrentState().getValue())
    wfItem.changeState(element[1],eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''SupplierBill is Created'')
    getCurrentDate(wfItem)
    result=persistenceService.persist(wfItem)
    return(result,None)

def save_and_submit():
    if wfItem.getCurrentState().getValue()==StoresConstants.WF_NEW_STATE:
        save()   
    return submit_for_approval()
def approve():
    if wfItem.getCurrentState().getValue()==StoresConstants.WF_NEW_STATE:
        save()  
    return submit_for_approval()
def reject():
    try:
        print ''in reject'' 
        prevNext,further_prevNext,position=None,None,None
	prevNext=find_prev_next(wfItem.getCurrentState().getValue())	
        if wfItem.getCurrentState().getValue()==StoresConstants.WF_NEW_STATE or prevNext[0]==StoresConstants.WF_NEW_STATE:
            wfItem.changeState(''CANCELLED'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''SupplierBill is Canceled'')
            getCurrentDate(wfItem)
            status=commonsManager.getStatusByModuleAndDescription(StoresConstants.SUPPLIERBILL, ''Cancelled'')	    
	    wfItem.setStatus(status)          
	    result=persistenceService.persist(wfItem)
	else:
	    print ''in rejection of approver else block in reject''
	    print wfItem.getState()
	    print wfItem.getState().getPrevious()
	    print wfItem.getState().getPrevious().getOwner()
	    wfItem.changeState(prevNext[0],wfItem.getState().getPrevious().getOwner(),''SupplierBill is rejected from approver.'')
	    getCurrentDate(wfItem)
	    result=persistenceService.persist(wfItem)
	return (result,None)
    except ValidationException,e:
        return (None,e.getErrors())
        
        
def find_user_and_position(workflowObject):
    print ''find_user_and_position''
    position=None
    approver=None    
    user=None
    print ''Finding Approver''
    approver= workflowObject.getApprover()
    print ''approver''          
    print approver
    if not approver:
        raise ValidationException,[ValidationError(''no.approver'',''no.approver'')]
    else:
        user=approver
        print ''approver User11111''
        print user
        print user.getId()
    
    if not user:
        raise ValidationException,[ValidationError(''User is nulll'',''User is null'')]
    else:
        print ''before getting position for the user''
        print user.getId()
        position=eisCommonsManager.getPositionByUserId(user.getId())
        print ''printing the position value''
        print position.getId()
    return position')
    
    where name ='EgBillregister.workflow';    
    
  
UPDATE EG_SCRIPT
SET SCRIPT=SCRIPT||('
def submit_for_approval():
    try:
        print ''am in approval''
        prevNext,position,further_prevNext,element,result=None,None,None,None,None    
        print wfItem.getCurrentState()
        print wfItem.getCurrentState().getValue()
        prevNext=find_prev_next(wfItem.getCurrentState().getValue())
        further_prevNext=find_prev_next(prevNext[1])  
	if further_prevNext[1]==StoresConstants.WF_END_STATE:
	    wfItem.changeState(prevNext[1],eisCommonsManager.getPositionByUserId(wfItem.getModifiedBy().getId()),''SupplierBill is Approved'')
	    getCurrentDate(wfItem)	    
	    status=commonsManager.getStatusByModuleAndDescription(StoresConstants.SUPPLIERBILL, ''Approved'')
	    wfItem.setStatus(status) 
	    result=persistenceService.persist(wfItem)
	else:
	    position=find_user_and_position(wfItem)
	    if position != 0:
                wfItem.changeState(prevNext[1],position,''SupplierBill is in Pending Approval'')
                getCurrentDate(wfItem)
                result=persistenceService.persist(wfItem)
        print ''result in submit for approval-------->''
        print result
        return (result,None)
    except ValidationException,e:
        return (None,e.getErrors())        

def find_prev_next(elem):
    print ''in find_prev_next the element is''
    print elem
    elements=[StoresConstants.WF_NEW_STATE,StoresConstants.WF_CREATED_STATE,StoresConstants.WF_VERIFIED_STATE,StoresConstants.WF_APPROVE_STATE,StoresConstants.WF_END_STATE]
    previous, next = None, None
    index = elements.index(elem)
    if index > 0:
        previous = elements[index -1]
    if index < (len(elements)-1):
        next = elements[index +1]
        
    print ''previous''
    print previous
    print ''next''
    print next
    return previous, next

def getCurrentDate(wfObject):   
    today=None
    if not wfObject.getApprovedOn():       
        print ''in appoved on is null''
        today = datetime.datetime.now()
        print today        
        print today.strftime(''%d-%m-%Y'')
        wfObject.getCurrentState().setDate1(SimpleDateFormat(''dd/MM/yyyy'').parse(today.strftime(''%d/%m/%Y'')))        
    else:
        print ''approvedon is selected'' 
        wfObject.getCurrentState().setDate1(wfObject.getApprovedOn())   

transitions={
 ''save'':save,
 ''submit'':submit_for_approval,
 ''save_and_submit'':save_and_submit,
 ''reject'':reject,
 ''approve'':approve
}
result,validationErrors=transitions[action.getName()]()')

where name ='EgBillregister.workflow'; 


#DOWN