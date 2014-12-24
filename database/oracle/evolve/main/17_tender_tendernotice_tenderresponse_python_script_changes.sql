#UP  

update eg_script set script = '
from org.egov.tender.utils import TenderConstants    
from java.util import Date    
from org.egov.pims.utils import EisManagersUtill     
eisCommonsService = EisManagersUtill.getEisCommonsService()  
commonsService = EisManagersUtill.getCommonsService()    
def save():        
    element=None        
    element=find_prev_next(wfItem.getCurrentState().getValue())        
    print ''inside save ''  
    wfItem.changeState(element[1],eisCommonsService.getPositionByUserId(wfItem.getCreatedBy().getId()),''Tender Notice workflow started'')        
    result=persistenceService.persist(wfItem)        
    return(result,None)        
def save_and_submit():        
    if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE:          
        print ''before calling save in save and submit''  
        save()              
    return submit_for_approval()'
where name = 'TenderNotice.workflow';
    
update eg_script set script = script || ('    
def approve():    
    if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE:    
        print ''before calling save in approve''  
        save()        
    return submit_for_approval()    
def submit_for_approval():    
    try:    
        print ''am in approval''    
        prevNext,position,further_prevNext,element,result=None,None,None,None,None         
        print wfItem.getCurrentState()    
        print wfItem.getCurrentState().getValue()    
        prevNext=find_prev_next(wfItem.getCurrentState().getValue())            
        further_prevNext=find_prev_next(prevNext[1])       
	if further_prevNext[1]==TenderConstants.WF_END_STATE:    
	    wfItem.changeState(prevNext[1],find_user_and_position(wfItem),''Tender Notice Approved'')    
	    status=commonsService.getStatusByModuleAndCode(TenderConstants.TENDERNOTICE, ''Approved'')    
	    wfItem.setStatus(status)       
	    result=persistenceService.persist(wfItem)    
	else:    
	    position=find_user_and_position(wfItem)    
	    if position != 0:    
                wfItem.changeState(prevNext[1],position,''Tender Notice in Pending Approval'')    
                result=persistenceService.persist(wfItem)    
        print ''result in submit for approval''    
        print result    
        return (result,None)    
    except ValidationException,e:    
        return (None,e.getErrors())')  
where name = 'TenderNotice.workflow';

update eg_script set script = script || ('
def reject():    
    try:    
        print ''in reject''     
        prevNext,further_prevNext,position,newstate,tempwfItem,count=None,None,None,None,None,None    
	prevNext=find_prev_next(wfItem.getCurrentState().getValue())	    
        if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE or prevNext[0]==TenderConstants.WF_NEW_STATE:    
            wfItem.changeState(''CANCELLED'',find_user_and_position(wfItem),''Tender Notice Canceled'')    
            status=commonsService.getStatusByModuleAndCode(TenderConstants.TENDERNOTICE, ''Cancelled'')	        
	    print ''status''  
	    print status  
	    wfItem.setStatus(status)                     
	    result=persistenceService.persist(wfItem)    
	else:    
	    print ''in rejection of approver else block in reject''    
	    print wfItem.getState()    
	    print wfItem.getState().getPrevious()    
	    if wfItem.getCurrentState().getValue()==TenderConstants.WF_VERIFIED_STATE:  
	        tempwfItem=wfItem.getState().getPrevious()  
	        while True:  
		    print ''count''  
		    print count  
		    if count==1:  
		        tempwfItem=tempwfItem.getPrevious()  
		    print ''tempwfItem''  
		    print tempwfItem.getValue()  
		    newstate=tempwfItem.getValue()  
		    count=1  
		    if newstate==TenderConstants.WF_NEW_STATE:  
		        break  
	        print tempwfItem.getOwner()  
	        wfItem.changeState(prevNext[0],tempwfItem.getOwner(),''Tender Notice rejected from approver1'')    
	    else:  
	        wfItem.changeState(prevNext[0],wfItem.getState().getPrevious().getOwner(),''Tender Notice rejected from approver.'')    
	    result=persistenceService.persist(wfItem)    
	return (result,None)    
    except ValidationException,e:    
        return (None,e.getErrors())')
where name = 'TenderNotice.workflow';

update eg_script set script = script || ('
def find_user_and_position(workflowObject):    
    print ''find_user_and_position''    
    position,approver,user=None,None,None    
    position=wfItem.getPosition()  
    print ''Finding position''  
    print position  
    if not position:    
    	position=eisCommonsService.getPositionByUserId(workflowObject.getCreatedBy().getId())    
        print ''printing the position value''    
        print position.getId()    
    return position		    
   
def find_prev_next(elem):        
    elements=[TenderConstants.WF_NEW_STATE,TenderConstants.WF_CREATED_STATE,TenderConstants.WF_VERIFIED_STATE,TenderConstants.WF_APPROVE_STATE,TenderConstants.WF_END_STATE]        
    previous, next = None, None        
    index = elements.index(elem)        
    if index > 0:        
        previous = elements[index -1]        
    if index < (len(elements)-1):        
        next = elements[index +1]        
    return previous, next        
transitions={        
 ''save'':save,        
 ''submit'':submit_for_approval,    
 ''save_and_submit'':save_and_submit,    
 ''reject'':reject,    
 ''approve'':approve    
}        
result,validationErrors=transitions[action.getName()]()')
where name = 'TenderNotice.workflow';


update eg_script set script = '
from org.egov.tender.utils import TenderConstants   
from java.util import Date   
from org.egov.pims.utils import EisManagersUtill  
eisCommonsService = EisManagersUtill.getEisCommonsService()     
def save():       
    element=None       
    element=find_prev_next(wfItem.getCurrentState().getValue())       
    wfItem.changeState(element[1],wfItem.getPosition(),''Tender Response workflow started'')       
    result=persistenceService.persist(wfItem)       
    return(result,None)       
def save_and_submit():   
    if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE:     
        save()     
    print ''in save_and_submit''   
    return submit_for_approval()       
def find_prev_next(elem):   
    try:   
        elements=[TenderConstants.WF_NEW_STATE,TenderConstants.WF_CREATED_STATE,TenderConstants.WF_VERIFIED_STATE,TenderConstants.WF_APPROVE_STATE,TenderConstants.WF_END_STATE]         
    	previous, next,index,msg = None,None,None,None   
        index = elements.index(elem)   
        print ''index''   
        print index   
        if index is None:   
            msg=''There is no element in the status List''  
            raise ValidationException,[ValidationError(msg,msg)]   
        if index > 0:   
            previous = elements[index -1]   
        if index < (len(elements)-1):   
            next = elements[index +1]   
        print ''previous''   
        print previous   
        print ''next''  
        print next   
        return previous, next   
    except ValidationException,e:   
        return (None,e.getErrors())'
where name = 'GenericTenderResponse.workflow';

update eg_script set script = script || ('  
def submit_for_approval():     
    try:     
        print ''submit_for_approval''  
        prevNext=find_prev_next(wfItem.getCurrentState().getValue())             
        further_prevNext=find_prev_next(prevNext[1])              
	if further_prevNext[1]==TenderConstants.WF_END_STATE:     
	    wfItem.changeState(prevNext[1],wfItem.getPosition(),''Tender Response Approved'')  
            status=persistenceService.find(TenderConstants.STATUSQUERY,[TenderConstants.TENDERRESPONSEMODULE,''Approved''])  
	    wfItem.setStatus(status)       
	    result=persistenceService.persist(wfItem)     
	else:     
	    wfItem.changeState(prevNext[1],wfItem.getPosition(),''Tender Response is in Pending Approval'')     
            result=persistenceService.persist(wfItem)     
        print ''result in submit for approval-------->''  
        print result     
        return (result,None)     
    except ValidationException,e:     
        return (None,e.getErrors())   
def approve():     
    print ''in approve''  
    return submit_for_approval()   
def reject():     
    try:     
        print ''in reject''      
        prevNext,position=None,None	     
	prevNext=find_prev_next(wfItem.getCurrentState().getValue())		     
        if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE or prevNext[0]==TenderConstants.WF_NEW_STATE:   
            wfItem.changeState(TenderConstants.WF_CANCELED_STATE,eisCommonsService.getPositionByUserId(wfItem.getCreatedBy().getId()),''Tender Response Cancelled'')  
            status=persistenceService.find(TenderConstants.STATUSQUERY,[TenderConstants.TENDERRESPONSEMODULE,''Cancelled''])  
	    wfItem.setStatus(status)               
	    result=persistenceService.persist(wfItem)     
	else:     
	    print ''in rejection of approver else block in reject''  
	    wfItem.changeState(prevNext[0],eisCommonsService.getPositionByUserId(wfItem.getCreatedBy().getId()),''Tender Response is Rejected from approver'')     
	    result=persistenceService.persist(wfItem)     
	return (result,None)     
    except ValidationException,e:     
        return (None,e.getErrors())          
transitions={       
 ''save'':save,       
 ''savesubmit'':save_and_submit,   
 ''reject'':reject,    
 ''approve'':approve    
}       
result,validationErrors=transitions[action.getName()]()')
where name = 'GenericTenderResponse.workflow';

INSERT INTO EG_USERROLE ( ID_ROLE, ID_USER, ID, FROMDATE, TODATE,IS_HISTORY ) VALUES ( 
(select ID_ROLE from EG_ROLES where ROLE_NAME like 'TenderAdmin'), (select ID_USER from eg_user where USER_NAME like 'EETEST'), SEQ_EG_USERROLE.nextval,  TO_Date( '04/01/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  null, 'N'); 
INSERT INTO EG_USERROLE ( ID_ROLE, ID_USER, ID, FROMDATE, TODATE,IS_HISTORY ) VALUES ( 
(select ID_ROLE from EG_ROLES where ROLE_NAME like 'TenderAdmin'), (select ID_USER from eg_user where USER_NAME like 'AEETEST'), SEQ_EG_USERROLE.nextval,  TO_Date( '04/01/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  null, 'N'); 

#DOWN

DELETE FROM EG_USERROLE WHERE ID_ROLE = (SELECT ID_ROLE from EG_ROLES where ROLE_NAME like 'TenderAdmin' )
AND ID_USER = (select ID_USER from eg_user where USER_NAME like 'EETEST');
DELETE FROM EG_USERROLE WHERE ID_ROLE = (SELECT ID_ROLE from EG_ROLES where ROLE_NAME like 'TenderAdmin' )
AND ID_USER = (select ID_USER from eg_user where USER_NAME like 'AEETEST');

update eg_script set script = '
from org.egov.tender.utils import TenderConstants    
from java.util import Date    
from org.egov.pims.utils import EisManagersUtill     
eisCommonsManager = EisManagersUtill.getEisCommonsManager()  
commonsManager = EisManagersUtill.getCommonsManager()    
def save():        
    element=None        
    element=find_prev_next(wfItem.getCurrentState().getValue())        
    print ''inside save ''  
    wfItem.changeState(element[1],eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Tender Notice workflow started'')        
    result=persistenceService.persist(wfItem)        
    return(result,None)        
def save_and_submit():        
    if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE:          
        print ''before calling save in save and submit''  
        save()              
    return submit_for_approval()'
where name = 'TenderNotice.workflow';
    
update eg_script set script = script || ('    
def approve():    
    if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE:    
        print ''before calling save in approve''  
        save()        
    return submit_for_approval()    
def submit_for_approval():    
    try:    
        print ''am in approval''    
        prevNext,position,further_prevNext,element,result=None,None,None,None,None         
        print wfItem.getCurrentState()    
        print wfItem.getCurrentState().getValue()    
        prevNext=find_prev_next(wfItem.getCurrentState().getValue())            
        further_prevNext=find_prev_next(prevNext[1])       
	if further_prevNext[1]==TenderConstants.WF_END_STATE:    
	    wfItem.changeState(prevNext[1],find_user_and_position(wfItem),''Tender Notice Approved'')    
	    status=commonsManager.getStatusByModuleAndDescription(TenderConstants.TENDERNOTICE, ''Approved'')    
	    wfItem.setStatus(status)       
	    result=persistenceService.persist(wfItem)    
	else:    
	    position=find_user_and_position(wfItem)    
	    if position != 0:    
                wfItem.changeState(prevNext[1],position,''Tender Notice in Pending Approval'')    
                result=persistenceService.persist(wfItem)    
        print ''result in submit for approval''    
        print result    
        return (result,None)    
    except ValidationException,e:    
        return (None,e.getErrors())')  
where name = 'TenderNotice.workflow';

update eg_script set script = script || ('
def reject():    
    try:    
        print ''in reject''     
        prevNext,further_prevNext,position,newstate,tempwfItem,count=None,None,None,None,None,None    
	prevNext=find_prev_next(wfItem.getCurrentState().getValue())	    
        if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE or prevNext[0]==TenderConstants.WF_NEW_STATE:    
            wfItem.changeState(''CANCELLED'',find_user_and_position(wfItem),''Tender Notice Canceled'')    
            status=commonsManager.getStatusByModuleAndDescription(TenderConstants.TENDERNOTICE, ''Cancelled'')	        
	    print ''status''  
	    print status  
	    wfItem.setStatus(status)                     
	    result=persistenceService.persist(wfItem)    
	else:    
	    print ''in rejection of approver else block in reject''    
	    print wfItem.getState()    
	    print wfItem.getState().getPrevious()    
	    if wfItem.getCurrentState().getValue()==TenderConstants.WF_VERIFIED_STATE:  
	        tempwfItem=wfItem.getState().getPrevious()  
	        while True:  
		    print ''count''  
		    print count  
		    if count==1:  
		        tempwfItem=tempwfItem.getPrevious()  
		    print ''tempwfItem''  
		    print tempwfItem.getValue()  
		    newstate=tempwfItem.getValue()  
		    count=1  
		    if newstate==TenderConstants.WF_NEW_STATE:  
		        break  
	        print tempwfItem.getOwner()  
	        wfItem.changeState(prevNext[0],tempwfItem.getOwner(),''Tender Notice rejected from approver1'')    
	    else:  
	        wfItem.changeState(prevNext[0],wfItem.getState().getPrevious().getOwner(),''Tender Notice rejected from approver.'')    
	    result=persistenceService.persist(wfItem)    
	return (result,None)    
    except ValidationException,e:    
        return (None,e.getErrors())')
where name = 'TenderNotice.workflow';

update eg_script set script = script || ('
def find_user_and_position(workflowObject):    
    print ''find_user_and_position''    
    position,approver,user=None,None,None    
    position=wfItem.getPosition()  
    print ''Finding position''  
    print position  
    if not position:    
    	position=eisCommonsManager.getPositionByUserId(workflowObject.getCreatedBy().getId())    
        print ''printing the position value''    
        print position.getId()    
    return position		    
   
def find_prev_next(elem):        
    elements=[TenderConstants.WF_NEW_STATE,TenderConstants.WF_CREATED_STATE,TenderConstants.WF_VERIFIED_STATE,TenderConstants.WF_APPROVE_STATE,TenderConstants.WF_END_STATE]        
    previous, next = None, None        
    index = elements.index(elem)        
    if index > 0:        
        previous = elements[index -1]        
    if index < (len(elements)-1):        
        next = elements[index +1]        
    return previous, next        
transitions={        
 ''save'':save,        
 ''submit'':submit_for_approval,    
 ''save_and_submit'':save_and_submit,    
 ''reject'':reject,    
 ''approve'':approve    
}        
result,validationErrors=transitions[action.getName()]()')
where name = 'TenderNotice.workflow';

update eg_script set script = '
from org.egov.tender.utils import TenderConstants   
from java.util import Date   
from org.egov.pims.utils import EisManagersUtill  
eisCommonsManager = EisManagersUtill.getEisCommonsManager()     
def save():       
    element=None       
    element=find_prev_next(wfItem.getCurrentState().getValue())       
    wfItem.changeState(element[1],wfItem.getPosition(),''Tender Response workflow started'')       
    result=persistenceService.persist(wfItem)       
    return(result,None)       
def save_and_submit():   
    if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE:     
        save()     
    print ''in save_and_submit''   
    return submit_for_approval()       
def find_prev_next(elem):   
    try:   
        elements=[TenderConstants.WF_NEW_STATE,TenderConstants.WF_CREATED_STATE,TenderConstants.WF_VERIFIED_STATE,TenderConstants.WF_APPROVE_STATE,TenderConstants.WF_END_STATE]         
    	previous, next,index,msg = None,None,None,None   
        index = elements.index(elem)   
        print ''index''   
        print index   
        if index is None:   
            msg=''There is no element in the status List''  
            raise ValidationException,[ValidationError(msg,msg)]   
        if index > 0:   
            previous = elements[index -1]   
        if index < (len(elements)-1):   
            next = elements[index +1]   
        print ''previous''   
        print previous   
        print ''next''  
        print next   
        return previous, next   
    except ValidationException,e:   
        return (None,e.getErrors())'
where name = 'GenericTenderResponse.workflow';

update eg_script set script = script || ('
def submit_for_approval():     
    try:     
        print ''submit_for_approval''  
        prevNext=find_prev_next(wfItem.getCurrentState().getValue())             
        further_prevNext=find_prev_next(prevNext[1])              
	if further_prevNext[1]==TenderConstants.WF_END_STATE:     
	    wfItem.changeState(prevNext[1],wfItem.getPosition(),''Tender Response Approved'')  
            status=persistenceService.find(TenderConstants.STATUSQUERY,[TenderConstants.TENDERRESPONSEMODULE,''Approved''])  
	    wfItem.setStatus(status)       
	    result=persistenceService.persist(wfItem)     
	else:     
	    wfItem.changeState(prevNext[1],wfItem.getPosition(),''Tender Response is in Pending Approval'')     
            result=persistenceService.persist(wfItem)     
        print ''result in submit for approval-------->''  
        print result     
        return (result,None)     
    except ValidationException,e:     
        return (None,e.getErrors())   
def approve():     
    print ''in approve''  
    return submit_for_approval()   
def reject():     
    try:     
        print ''in reject''      
        prevNext,position=None,None	     
	prevNext=find_prev_next(wfItem.getCurrentState().getValue())		     
        if wfItem.getCurrentState().getValue()==TenderConstants.WF_NEW_STATE or prevNext[0]==TenderConstants.WF_NEW_STATE:   
            wfItem.changeState(TenderConstants.WF_CANCELED_STATE,eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Tender Response Cancelled'')  
            status=persistenceService.find(TenderConstants.STATUSQUERY,[TenderConstants.TENDERRESPONSEMODULE,''Cancelled''])  
	    wfItem.setStatus(status)               
	    result=persistenceService.persist(wfItem)     
	else:     
	    print ''in rejection of approver else block in reject''  
	    wfItem.changeState(prevNext[0],eisCommonsManager.getPositionByUserId(wfItem.getCreatedBy().getId()),''Tender Response is Rejected from approver'')     
	    result=persistenceService.persist(wfItem)     
	return (result,None)     
    except ValidationException,e:     
        return (None,e.getErrors())          
transitions={       
 ''save'':save,       
 ''savesubmit'':save_and_submit,   
 ''reject'':reject,    
 ''approve'':approve    
}       
result,validationErrors=transitions[action.getName()]()')
where name = 'GenericTenderResponse.workflow';