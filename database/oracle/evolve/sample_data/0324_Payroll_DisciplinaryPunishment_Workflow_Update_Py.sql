#UP

update eg_script set script='from org.egov import EGOVRuntimeException     
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
        if wfItem.getState().getValue()==''NEW'': 
            disPunishWfType = persistenceService.typeOfDisciplinaryPunishmentWF()
            if disPunishWfType==''Manual'': 
                hirPos=wfItem.getApproverPos() 
            if hirPos: 
                wfItem.setStatus(EisManagersUtill.getCommonsManager().getStatusByModuleAndDescription(''DisciplinaryPunishment'',''Created'')) 
                wfItem.changeState(''CREATED'',''PENDING FOR APPROVAL/EXCEPTION'',hirPos,comments) 
            else: 
                raise ValidationException,[ValidationError(''currentState.owner'',''There is no approver found for - ''+pos.getName()+'' -position'')]	 
        elif wfItem.getState().getValue()==''CREATED'':  
            wfItem.setStatus(EisManagersUtill.getCommonsManager().getStatusByModuleAndDescription(''DisciplinaryPunishment'',''Approved''))  
            wfItem.changeState(''APPROVED'',pos,comments)
            wfItem.changeState(''END'',pos,''Disciplinary Punishment Workflow End'')  
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
result,validationErrors' 
where name='DisciplinaryPunishment.workflow' ;

#DOWN
