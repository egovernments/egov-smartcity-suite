#UP

update eg_script set script='from org.egov import EGOVRuntimeException  
from org.egov import EGOVException  
from java.lang import Exception  
from org.egov.exceptions import NoSuchObjectException, TooManyValuesException  
from org.egov.infstr import ValidationError,ValidationException  
from org.egov.pims.dao import EisDAOFactory  
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()    
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()  
validationErrors=None  
def is_position_hierarchy_defined():  
    hirPos1=None  
    hirPos2=None  
    hirPos3=None  
    pos=wfItem.getState().getOwner()  
    hirPos1=persistenceService.getSuperiorPosition(pos,''payslip'')  
    if hirPos1!=None:  
        hirPos2=persistenceService.getSuperiorPosition(hirPos1,''payslip'')  
    if hirPos2!=None:  
        hirPos3=persistenceService.getSuperiorPosition(hirPos2,''payslip'')  
    if hirPos1==None or hirPos2==None or hirPos3==None:  
        raise ValidationException,[ValidationError(''Error'',''There is no position hierarchy level defined properly'')]  
    return (None)  
def user_approve():  
    try:  
        pos=wfItem.getState().getOwner()  
        if wfItem.getState().getValue()==''NEW'':
             if persistenceService.typeOfPayslipWF()!=''Manual'':   
                 is_position_hierarchy_defined()
        if persistenceService.typeOfPayslipWF()==''Manual'':
             hirPos=wfItem.getApproverPos()
        else:          
             hirPos=persistenceService.getSuperiorPosition(pos,''payslip'')  
        if hirPos:              
            if wfItem.getState().getValue()==''NEW'' or wfItem.getState().getValue()==''HOD_REJECT'':  
                persistenceService.updatePayslipStatus(wfItem,''Created'')  
                wfItem.changeState(''CLEARK_APPROVED'',''PENDING_APPROVAL'',hirPos,comments)  
            elif wfItem.getState().getValue()==''CLEARK_APPROVED'' or wfItem.getState().getValue()==''ACCOUNTANT_REJECT'':  
                persistenceService.updatePayslipStatus(wfItem,''DeptApproved'')  
                wfItem.changeState(''HOD_APPROVED'',''PENDING_APPROVAL'',hirPos,comments)  
            elif wfItem.getState().getValue()==''HOD_APPROVED''or wfItem.getState().getValue()==''CAO_REJECT'':  
                persistenceService.updatePayslipStatus(wfItem,''AccountsApproved'')  
                wfItem.changeState(''ACCOUNTANT_APPROVED'',''PENDING_FINALAPPROVAL'',hirPos,comments)  
        elif wfItem.getState().getValue()==''ACCOUNTANT_APPROVED'':  
            persistenceService.updatePayslipStatus(wfItem,''AuditApproved'')  
            wfItem.changeState(''END'',pos,comments)  
        else:              
            raise ValidationException,[ValidationError(''currentState.owner'',''There is no superior position for - ''+pos.getName()+'' -position'')]  
        return (persistenceService.persist(wfItem),None)  
    except EGOVException,e:          
        raise ValidationException,[ValidationError(''ExceptionInWorkflow--'',e.getMessage())]  
' where name='EmpPayroll.workflow';

update eg_script set script=script||('
def user_reject():  
    try:  
        pos=wfItem.getState().getOwner()
        lowerPos=wfItem.getState().getPrevious().getOwner()                      
        if wfItem.getState().getValue()==''CLEARK_APPROVED'' or wfItem.getState().getValue()==''ACCOUNTANT_REJECT'':  
            wfItem.changeState(''HOD_REJECT'',''PENDING_APPROVAL'',lowerPos,comments)  
        elif wfItem.getState().getValue()==''HOD_APPROVED''or wfItem.getState().getValue()==''CAO_REJECT'':  
            wfItem.changeState(''ACCOUNTANT_REJECT'',''PENDING_APPROVAL'',lowerPos,comments)  
        elif wfItem.getState().getValue()==''ACCOUNTANT_APPROVED'':  
            wfItem.changeState(''CAO_REJECT'',''PENDING_APPROVAL'',lowerPos,comments)  
        elif wfItem.getState().getValue()==''HOD_REJECT'':  
            persistenceService.updatePayslipStatus(wfItem,''Cancelled'')  
            wfItem.changeState(''END'',pos,comments)
        return (persistenceService.persist(wfItem),None)  
    except Exception,e:          
        raise ValidationException,[ValidationError(''ExceptionInWorkflow--'',e.getMessage())]  
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
result,validationErrors') where name='EmpPayroll.workflow';

#DOWN

update eg_script set script='from org.egov import EGOVRuntimeException
from org.egov import EGOVException
from java.lang import Exception
from org.egov.exceptions import NoSuchObjectException, TooManyValuesException
from org.egov.infstr import ValidationError, ValidationException
from org.egov.pims.dao import EisDAOFactory
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()  
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
validationErrors=None
def user_approve():
    try:
        pos=wfItem.getState().getOwner()
        hirPos=persistenceService.getSuperiorPosition(pos,''payslip'')
        if hirPos:            
            if wfItem.getState().getValue()==''NEW'' or wfItem.getState().getValue()==''HOD_REJECT'':
                persistenceService.updatePayslipStatus(wfItem,''Created'')
                wfItem.changeState(''CLEARK_APPROVED'',hirPos,comments)
            elif wfItem.getState().getValue()==''CLEARK_APPROVED'' or wfItem.getState().getValue()==''ACCOUNTANT_REJECT'':
                persistenceService.updatePayslipStatus(wfItem,''DeptApproved'')
                wfItem.changeState(''HOD_APPROVED'',hirPos,comments)
            elif wfItem.getState().getValue()==''HOD_APPROVED''or wfItem.getState().getValue()==''CAO_REJECT'':
                persistenceService.updatePayslipStatus(wfItem,''AccountsApproved'')
                wfItem.changeState(''ACCOUNTANT_APPROVED'',hirPos,comments)
        elif wfItem.getState().getValue()==''ACCOUNTANT_APPROVED'':
            persistenceService.updatePayslipStatus(wfItem,''AuditApproved'')
            wfItem.changeState(''END'',pos,comments)
        else:            
            raise ValidationException,[ValidationError(''currentState.owner'',''There is no superior position for - ''+pos.getName()+'' -position'')]
        return (persistenceService.persist(wfItem),None)
    except EGOVException,e:        
        raise ValidationException,[ValidationError(''ExceptionInWorkflow--'',e.getMessage())]
def user_reject():
    try:
        pos=wfItem.getState().getOwner()
        lowerPos=persistenceService.getInferiorPosition(pos,''payslip'')
        if lowerPos:            
            if wfItem.getState().getValue()==''CLEARK_APPROVED'' or wfItem.getState().getValue()==''ACCOUNTANT_REJECT'':
                wfItem.changeState(''HOD_REJECT'',lowerPos,comments)
            elif wfItem.getState().getValue()==''HOD_APPROVED''or wfItem.getState().getValue()==''CAO_REJECT'':
                wfItem.changeState(''ACCOUNTANT_REJECT'',lowerPos,comments)
            elif wfItem.getState().getValue()==''ACCOUNTANT_APPROVED'':
                wfItem.changeState(''CAO_REJECT'',lowerPos,comments)
        elif wfItem.getState().getValue()==''HOD_REJECT'':
            persistenceService.updatePayslipStatus(wfItem,''Cancelled'')
            wfItem.changeState(''END'',pos,comments)
        else:            
            raise ValidationException,[ValidationError(''currentState.owner'',''There is no inferior position for - ''+pos.getName()+'' -position'')]
        return (persistenceService.persist(wfItem),None)
    except Exception,e:        
        raise ValidationException,[ValidationError(''ExceptionInWorkflow--'',e.getMessage())]
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
result,validationErrors'  where name='EmpPayroll.workflow' ;
