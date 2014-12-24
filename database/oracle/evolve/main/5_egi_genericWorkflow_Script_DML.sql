#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.pims.utils import EisManagersUtill
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException  
eisCommonsManager = EisManagersUtill.getEisCommonsService() 
def reject():
    try: 
    	print ''in reject method''  
    	wfItem.changeState(''Rejected'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)  
    	result=persistenceService.persist(wfItem)
	if wfItem.getCurrentState().getPrevious().getValue()==''Rejected'' or wfItem.getCurrentState().getPrevious().getValue()==''NEW'':
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)
    	return(result,None)
    except ValidationException,e:  
        return (None,e.getErrors())
def cancel():
    try: 
    	print ''in cancel method''  
    	wfItem.changeState(''Cancelled'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)  
    	result=persistenceService.persist(wfItem)
	workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)
    	return(result,None)
    except ValidationException,e:  
        return (None,e.getErrors())        
def approve():
    try:
	print ''in approve method''
	position=None
	print wfItem.getApproverPositionId()
	if wfItem.getApproverPositionId()!=None and wfItem.getApproverPositionId()!=-1:
		position=persistenceService.find(''from Position where id=?'',[wfItem.getApproverPositionId()])
	else:
		position=wfItem.getCurrentState().getOwner()
	print position
    	wfItem.changeState(wfmatrix.getNextState(),wfmatrix.getNextAction(),position,comments)
    	result=persistenceService.persist(wfItem)
	if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''END'':
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)
    	return(result,None)
    except ValidationException,e:  
        return (None,e.getErrors())
transitions={ 
 ''approve'':approve,  
 ''forward'':approve,  
 ''reject'':reject,
 ''Approve'':approve,
 ''Forward'':approve,
 ''Cancel'':cancel,
 ''cancel'':cancel,
 ''Reject'':reject
}
result,validationErrors=transitions[action.getName()]()'
WHERE NAME='generic.workflow';


#DOWN

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.pims.utils import EisManagersUtill
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException  
eisCommonsManager = EisManagersUtill.getEisCommonsManager() 
def reject():
    try: 
    	print ''in reject method''  
    	wfItem.changeState(''Rejected'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)  
    	result=persistenceService.persist(wfItem)
	if wfItem.getCurrentState().getPrevious().getValue()==''Rejected'' or wfItem.getCurrentState().getPrevious().getValue()==''NEW'':
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)
    	return(result,None)
    except ValidationException,e:  
        return (None,e.getErrors())
def approve():
    try:
	print ''in approve method''
	position=None
	print wfItem.getApproverPositionId()
	if wfItem.getApproverPositionId()!=None and wfItem.getApproverPositionId()!=-1:
		position=persistenceService.find(''from Position where id=?'',[wfItem.getApproverPositionId()])
	else:
		position=wfItem.getCurrentState().getOwner()
	print position
    	wfItem.changeState(wfmatrix.getNextState(),wfmatrix.getNextAction(),position,comments)
    	result=persistenceService.persist(wfItem)
	if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''END'':
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)
    	return(result,None)
    except ValidationException,e:  
        return (None,e.getErrors())
transitions={ 
 ''approve'':approve,  
 ''forward'':approve,  
 ''reject'':reject,
 ''Approve'':approve,
 ''Forward'':approve,
 ''Reject'':reject
}
result,validationErrors=transitions[action.getName()]()'
WHERE NAME='generic.workflow';
