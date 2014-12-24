#UP

DELETE FROM EG_WF_MATRIX WHERE OBJECTTYPE='WorkOrder';

Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkOrder', 'NEW',null, 'ASSISTANT EXECUTIVE ENGINEER', 'CREATED', 'Pending for Approval', 'EXECUTIVE ENGINEER', 'CREATED', 'Forward,Cancel',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));  
   
Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkOrder', 'CREATED','Pending for Approval', 'EXECUTIVE ENGINEER', 'APPROVED', 'END', null, 'APPROVED', 'Approve,Reject',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkOrder', 'REJECTED',null, 'ASSISTANT EXECUTIVE ENGINEER', 'RESUBMITTED', 'Pending for Approval', 'EXECUTIVE ENGINEER', 'RESUBMITTED', 'Forward,Cancel',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));
   
Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkOrder', 'RESUBMITTED','Pending for Approval', 'EXECUTIVE ENGINEER', 'APPROVED', 'END', null, 'APPROVED', 'Approve,Reject',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

update eg_script set script = '
from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov.exceptions import EGOVException  
validationErrors=None  
result=None  
wfmatrix=None  
if wfItem.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks():  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
else:  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName(),None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
if(validationErrors==None):  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	persistenceService.persist(wfItem)  
	result=wfItem  
	validationErrors=None'
where name = 'WorkOrder.workflow';

update eg_script set script = '
from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov.exceptions import EGOVException  
validationErrors=None  
result=None  
if(validationErrors==None):  
	print wfItem.getCurrentState().getNextAction()  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	persistenceService.persist(wfItem)  
	result=wfItem  
	validationErrors=None'
where name = 'WorkCompletionDetail.workflow';

delete from EG_WF_MATRIX where  OBJECTTYPE='WorkCompletionDetail';

Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkCompletionDetail', 'NEW',null, 'ASSISTANT EXECUTIVE ENGINEER', 'CREATED', 'Pending for Approval', 'EXECUTIVE ENGINEER', 'CREATED', 'Forward,Cancel',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkCompletionDetail', 'CREATED','Pending for Approval', 'EXECUTIVE ENGINEER', 'APPROVED', 'END', null, 'APPROVED', 'Approve,Reject',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkCompletionDetail', 'REJECTED',null, 'ASSISTANT EXECUTIVE ENGINEER', 'RESUBMITTED', 'Pending for Approval', 'EXECUTIVE ENGINEER', 'RESUBMITTED', 'Forward,Cancel',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

Insert into EG_WF_MATRIX
   (ID, DEPARTMENT, OBJECTTYPE, CURRENTSTATE,PENDINGACTIONS,CURRENTDESIGNATION, NEXTSTATE, NEXTACTION, NEXTDESIGNATION, NEXTSTATUS, VALIDACTIONS, FROMDATE)
 Values
   (EG_WF_MATRIX_SEQ.nextVal, 'BR-Bus Route Roads', 'WorkCompletionDetail', 'RESUBMITTED','Pending for Approval', 'EXECUTIVE ENGINEER', 'APPROVED', 'END', null, 'APPROVED', 'Approve,Reject',TO_Date( '08/02/2013 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));

#DOWN

delete from EG_WF_MATRIX where  OBJECTTYPE='WorkCompletionDetail';

update eg_script set script = '
from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov import EGOVException  
validationErrors=None  
result=None  
if(validationErrors==None):  
	print wfItem.getCurrentState().getNextAction()  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	persistenceService.persist(wfItem)  
	result=wfItem  
	validationErrors=None'
where name = 'WorkCompletionDetail.workflow';

update eg_script set script = '
from org.egov.infstr import ValidationError    
from org.egov.infstr import ValidationException    
from org.egov import EGOVException  
validationErrors=None  
result=None  
wfmatrix=None  
if wfItem.getWorkOrderEstimates().get(0).getEstimate().getIsSpillOverWorks():  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),None,None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
else:  
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getWorkOrderEstimates().get(0).getEstimate().getExecutingDepartment().getDeptName(),None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
if(validationErrors==None):  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)  
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',wfmatrix.getNextStatus()])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Reject''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''REJECTED''])  
		wfItem.setEgwStatus(status)  
	if(action.getName()==''Cancel''):  
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''WorkOrder'',''CANCELLED''])  
		wfItem.setEgwStatus(status)  
	persistenceService.persist(wfItem)  
	result=wfItem  
	validationErrors=None'
where name = 'WorkOrder.workflow';

DELETE FROM EG_WF_MATRIX WHERE OBJECTTYPE='WorkOrder' AND CURRENTSTATE IN ('NEW','CREATED','REJECTED','RESUBMITTED');