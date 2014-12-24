#UP

Insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'SalaryARF.workflow', 'python', 'from org.egov import EGOVRuntimeException   
from org.egov import EGOVException   
from java.lang import Exception   
from java.lang import System   
from org.egov.exceptions import NoSuchObjectException, TooManyValuesException   
from org.egov.infstr import ValidationError, ValidationException   
from org.egov.pims.dao import EisDAOFactory   
from org.egov.payroll.utils import PayrollManagersUtill   
from org.egov.payroll.utils import PayrollConstants 
from java.lang import Integer 
from org.egov.commons import EgwSatuschange 
from org.egov.commons import Status 
from org.egov.infstr.client.filter import EGOVThreadLocals   
validationErrors=None   
def user_approve():   
    try:   
        pos=wfItem.getState().getOwner()   
        advWfType = persistenceService.getTypeOfAdvanceWf()   
        if advWfType==''Manual'':   
            hirPos=wfItem.getAdvance().getApproverPos()   
        else:   
            hirPos=persistenceService.getSuperiorPosition(pos,''advance'')   
        if hirPos:               
            if wfItem.getState().getValue()==''NEW'':   
                wfItem.changeState(''CREATED'',''PENDING_APPROVAL'',hirPos,comments)   
            elif wfItem.getState().getValue()==''CREATED'':   
                statusChangeHistory(wfItem,''Approve'') 
                wfItem.changeState(''FINAL_APPROVED'',''PENDING_BPV'',hirPos,comments)   
        else:               
            raise ValidationException,[ValidationError(''currentState.owner'',''There is no approver found for - ''+pos.getName()+'' -position'')]   
        return (persistenceService.persist(wfItem),None)   
    except EGOVException,e:           
        raise ValidationException,[ValidationError(''ExceptionIn SalaryARF Workflow Approve==>'',e.getMessage())]   
def user_reject():   
    try:   
        pos=wfItem.getState().getOwner()   
        statusChangeHistory(wfItem,''Reject'')   
        wfItem.changeState(''REJECTED'',pos,comments)   
        wfItem.changeState(''END'',pos,''SalaryARF Workflow End'')   
        return (persistenceService.persist(wfItem),None)   
    except Exception,e:           
        raise ValidationException,[ValidationError(''ExceptionIn SalaryARF Workflow Reject==>'',e.getMessage())]   
def statusChangeHistory(advanceObj,approveOrReject):  
    try: 
        currentStatus = advanceObj.getStatus() 
        nextStatus = Status() 
        if approveOrReject==''Approve'': 
            nextStatus = PayrollManagersUtill.getPayrollExterInterface().getStatusByModuleAndDescription(''ARF'', ''Approved'')  
        else: 
            nextStatus = PayrollManagersUtill.getPayrollExterInterface().getStatusByModuleAndDescription(''ARF'', ''Cancelled'')	  
        statusChanges = EgwSatuschange()  
        statusChanges.setFromstatus(currentStatus.getId())  
        statusChanges.setCreatedby(Integer.valueOf(EGOVThreadLocals.getUserId()))  
        statusChanges.setTostatus(nextStatus.getId())  
        statusChanges.setModuleid(2)  
        statusChanges.setModuletype(nextStatus.getModuletype())  
        PayrollManagersUtill.getPayrollExterInterface().createEgwSatuschange(statusChanges) 
        advanceObj.setStatus(nextStatus)  
    except Exception,e:  
        raise ValidationException,[ValidationError(''Exception In statusChangeHistory in SalaryARF Workflow==>'',e.getMessage())] 
transitions={''user_approve'':user_approve,''user_reject'':user_reject}   
result=None   
try:   
    if action.getName().endswith(''approve''):           
        result=transitions[''user_approve'']()   
    elif action.getName().endswith(''reject''):        
        result=transitions[''user_reject'']()   
except ValidationException, e:       
    validationErrors = e.getErrors()   
except KeyError:   
    validationErrors=''This -''+action.getName() +''- Action is not defined for workflow''      
result,validationErrors', TO_DATE('01/01/1900 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('01/01/2100 00:00:00', 'MM/DD/YYYY HH24:MI:SS'));

#DOWN
delete frome eg_script where name='SalaryARF.workflow';
