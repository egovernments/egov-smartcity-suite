#UP

update eg_script set script='from org.egov import EGOVRuntimeException   
from org.egov import EGOVException   
from java.lang import Exception   
from java.lang import System 
from java.lang import Integer   
from org.egov.exceptions import NoSuchObjectException, TooManyValuesException   
from org.egov.infstr import ValidationError, ValidationException   
from org.egov.pims.dao import EisDAOFactory   
from org.egov.payroll.utils import PayrollManagersUtill   
from org.egov.payroll.utils import PayrollConstants  
from org.egov.commons import EgwSatuschange 
from org.egov.commons import Status 
from org.egov.infstr.client.filter import EGOVThreadLocals 
validationErrors=None   
def user_approve():   
    try:   
        pos=wfItem.getState().getOwner()   
        if wfItem.getState().getValue()==''NEW'':   
            advWfType = persistenceService.getTypeOfAdvanceWf() 
            if advWfType==''Manual'': 
                hirPos=wfItem.getApproverPos() 
            else: 
                hirPos=persistenceService.getSuperiorPosition(pos,''advance'') 
            if hirPos:	 
                wfItem.setStatus(PayrollManagersUtill.getPayrollExterInterface().getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_CREATE))  
                wfItem.changeState(''CREATED'',''PENDING_APPROVAL'',hirPos,comments) 
            else: 
                raise ValidationException,[ValidationError(''currentState.owner'',''There is no approver found for - ''+pos.getName()+'' -position'')]	   
        elif wfItem.getState().getValue()==''CREATED'':   
            statusChangeHistory(wfItem,''Approve'') 
            wfItem.changeState(''DISBURSED'',pos,comments)   
            wfItem.changeState(''END'',pos,''Advance Workflow End'')   
        return (persistenceService.persist(wfItem),None)   
    except EGOVException,e:           
        raise ValidationException,[ValidationError(''ExceptionIn Advance Workflow==>'',e.getMessage())]   
def user_reject():   
    try:   
        pos=wfItem.getState().getOwner()   
        statusChangeHistory(wfItem,''Reject'') 
        wfItem.changeState(''REJECTED'',pos,comments)   
        wfItem.changeState(''END'',pos,''Advance Workflow End'')   
        return (persistenceService.persist(wfItem),None)   
    except Exception,e:           
        raise ValidationException,[ValidationError(''Exception In SalaryARF Workflow==>'',e.getMessage())]
' where name='Advance.workflow' ;

update eg_script set script=script||'
def statusChangeHistory(advanceObj,approveOrReject): 
    try: 
        currentStatus = advanceObj.getStatus()
        nextStatus = Status()
        if approveOrReject==''Approve'':
            nextStatus = PayrollManagersUtill.getPayrollExterInterface().getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_DISBURSE) 
        else:
            nextStatus = PayrollManagersUtill.getPayrollExterInterface().getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_REJECT)	 
        statusChanges = EgwSatuschange() 
        statusChanges.setFromstatus(currentStatus.getId()) 
        statusChanges.setCreatedby(Integer.valueOf(EGOVThreadLocals.getUserId())) 
        statusChanges.setTostatus(nextStatus.getId()) 
        statusChanges.setModuleid(2) 
        statusChanges.setModuletype(nextStatus.getModuletype()) 
        PayrollManagersUtill.getPayrollExterInterface().createEgwSatuschange(statusChanges)
        advanceObj.setStatus(nextStatus) 
    except Exception,e: 
        raise ValidationException,[ValidationError(''Exception In statusChangeHistory in Advance Workflow==>'',e.getMessage())] 
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
result,validationErrors' where name='Advance.workflow' ;

#DOWN
