#UP
update eg_script set script = 'from org.egov.pims.utils import EisManagersUtill
from org.egov.infstr import ValidationError          
from org.egov.infstr import ValidationException  
eisCommonsManager = EisManagersUtill.getEisCommonsManager() 
def reject():
    try: 
    	print ''in reject method''  
    	wfItem.changeState(''REJECTED'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)  
    	result=persistenceService.persist(wfItem)
	if wfItem.getCurrentState().getPrevious().getValue()==''Rejected'' or wfItem.getCurrentState().getPrevious().getValue()==''NEW'':
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)
    	return(result,None)
    except ValidationException,e:  
        return (None,e.getErrors())
def cancel():
    try: 
    	print ''in cancel method''  
    	wfItem.changeState(''CANCELLED'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)  
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
        if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''Pending for Closure'':
        	position=eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId())
	print position
    	wfItem.changeState(wfmatrix.getNextState(),wfmatrix.getNextAction(),position,comments)
    	result=persistenceService.persist(wfItem)
	if wfmatrix.getNextAction()!=None and wfmatrix.getNextAction()==''END'':
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments)
    	return(result,None)
    except ValidationException,e:  
        return (None,e.getErrors())
def close():
    try:
    	print ''in close method''
    	wfItem.changeState(''Closed'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)
    	result=persistenceService.persist(wfItem)
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
 ''Reject'':reject,
 ''Close'':close,
 ''close'':close
}
result,validationErrors=transitions[action.getName()]()' where name ='works.generic.workflow';

#DOWN
update eg_script set script = 'from org.egov.pims.utils import EisManagersUtill 
from org.egov.infstr import ValidationError           
from org.egov.infstr import ValidationException   
eisCommonsManager = EisManagersUtill.getEisCommonsManager()  
def reject(): 
    try:  
    	print ''in reject method''   
    	wfItem.changeState(''REJECTED'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)   
    	result=persistenceService.persist(wfItem) 
	if wfItem.getCurrentState().getPrevious().getValue()==''Rejected'' or wfItem.getCurrentState().getPrevious().getValue()==''NEW'': 
		workflowService.end(wfItem,wfItem.getCurrentState().getOwner(),comments) 
    	return(result,None) 
    except ValidationException,e:   
        return (None,e.getErrors()) 
def cancel(): 
    try:  
    	print ''in cancel method''   
    	wfItem.changeState(''CANCELLED'',eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),comments)   
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
	print wfmatrix 
	print ''in ------------>>>>>'' 
	print wfmatrix 
	print wfmatrix.getNextAction() 
	print ''out--------><-----'' 
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
result,validationErrors=transitions[action.getName()]()'  where name ='works.generic.workflow';
