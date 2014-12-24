#UP
/*******Script for payslip workflow***************/

update eg_Script set script='from org.egov import EGOVRuntimeException      
from org.egov import EGOVException      
from java.lang import Exception      
from org.egov.exceptions import NoSuchObjectException, TooManyValuesException      
from org.egov.infstr import ValidationError,ValidationException      
from org.egov.pims.dao import EisDAOFactory      
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()        
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()     
validationErrors=None    
def user_approve():    
    try:    
        pos=wfItem.getState().getOwner()                           
        if wfItem.getState().getValue()==''NEW'' or wfItem.getState().getValue()==''REJECTED'':    
            hirPos=persistenceService.getSuperiorPosition(pos,''Gratuity'')    
            if hirPos:    
                persistenceService.updateGratuityStatus(wfItem,''Created'')    
                wfItem.getCurrentState().setNextAction(''Approval Pending'')    
                wfItem.changeState(''CREATED'',hirPos,comments)    
            else:  
                raise ValidationException,[ValidationError(''currentState.owner'',''There is no superior position for - ''+pos.getName()+'' -position'')]  
        elif wfItem.getState().getValue()==''CREATED'':  
                persistenceService.updateGratuityStatus(wfItem,''Approved'')  
                wfItem.changeState(''END'',pos,comments)   
        return (persistenceService.persist(wfItem),None)  
    except EGOVException,e:  
        raise ValidationException,[ValidationError(''ExceptionInWorkflow--'',e.getMessage())]  
def user_reject():    
    try:
        pos=wfItem.getState().getOwner()
        lowerPos=persistenceService.getInferiorPosition(pos,''Gratuity'')
        if lowerPos:            
            if wfItem.getState().getValue()==''CREATED'':
                wfItem.changeState(''REJECTED'',lowerPos,comments)            
        elif wfItem.getState().getValue()==''REJECTED'':
            persistenceService.updateGratuityStatus(wfItem,''Cancelled'')
            wfItem.changeState(''END'',pos,comments)
        else:
            print ''position-----------''+''There is no inferior position for - ''+pos.getName()+'' -position''
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
result,validationErrors' where name like'PensionDetails.workflow';



#DOWN