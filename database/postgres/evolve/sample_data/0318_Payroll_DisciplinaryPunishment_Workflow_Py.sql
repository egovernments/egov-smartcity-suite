#UP

insert into eg_script
   (ID, NAME, SCRIPT_TYPE, SCRIPT, START_DATE, END_DATE)
 Values
   (eg_script_seq.nextVal, 'DisciplinaryPunishment.workflow', 'python', 'from org.egov import EGOVRuntimeException    
from org.egov import EGOVException    
from java.lang import Exception    
from java.lang import System  
from java.lang import Integer    
from org.egov.exceptions import NoSuchObjectException, TooManyValuesException    
from org.egov.infstr import ValidationError, ValidationException
from org.egov.pims.utils import EisManagersUtill    
from org.egov.pims.dao import EisDAOFactory    
from org.egov.commons import EgwSatuschange  
from org.egov.commons import Status  
from org.egov.infstr.client.filter import EGOVThreadLocals  
validationErrors=None    
def user_approve():    
    try:    
        pos=wfItem.getState().getOwner() 
        disPunishWfType = persistenceService.typeOfDisciplinaryPunishmentWF() 
        if disPunishWfType==''Manual'': 
            hirPos=wfItem.getApproverPos() 
        if hirPos:	  
            if wfItem.getState().getValue()==''NEW'':   
                wfItem.setStatus(EisManagersUtill.getCommonsManager().getStatusByModuleAndDescription(''DisciplinaryPunishment'',''Created''))   
                wfItem.changeState(''CREATED'',''PENDING_APPROVAL'',hirPos,comments) 
            elif wfItem.getState().getValue()==''CREATED'': 
                wfItem.setStatus(EisManagersUtill.getCommonsManager().getStatusByModuleAndDescription(''DisciplinaryPunishment'',''Approved'')) 
                wfItem.changeState(''FINAL_APPROVED'',''PENDING_EXCEPTION'',hirPos,comments) 
        else:  
            raise ValidationException,[ValidationError(''currentState.owner'',''There is no approver found for - ''+pos.getName()+'' -position'')]	    
        return (persistenceService.persist(wfItem),None)    
    except EGOVException,e:            
        raise ValidationException,[ValidationError(''Exception in Disciplinary Punishment Workflow While approving==>'',e.getMessage())]    
def user_reject():    
    try:    
        pos=wfItem.getState().getOwner()    
        wfItem.setStatus(EisManagersUtill.getCommonsManager().getStatusByModuleAndDescription(''DisciplinaryPunishment'',''Rejected''))  
        wfItem.changeState(''REJECTED'',pos,comments)    
        wfItem.changeState(''END'',pos,''Disciplinary Punishment Workflow End'')    
        return (persistenceService.persist(wfItem),None)    
    except Exception,e:            
        raise ValidationException,[ValidationError(''Exception in Disciplinary Punishment Workflow While Rejecting==>'',e.getMessage())] 
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
