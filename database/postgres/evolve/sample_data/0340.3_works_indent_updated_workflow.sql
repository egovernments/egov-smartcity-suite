#UP

INSERT INTO eg_script(id,name,script_type,script)
VALUES(EG_SCRIPT_SEQ.NEXTVAL,'works.generic.workflow','python','from org.egov.pims.utils import EisManagersUtill
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
result,validationErrors=transitions[action.getName()]()');

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
if(action.getName()==''Approve'' and wfItem.getIndentType()== ''Amount''):	
	try:
		try:
			check=persistenceService.getIndentRateContractService().checkForBudgetaryAppropriation(wfItem)
		except EGOVException,e:
			raise ValidationException,[ValidationError(e.getMessage(),e.getMessage())]
		if check:
			wfItem.setBudgetApprNo(persistenceService.getIndentRateContractService().getBudgetAppropriationNumber(wfItem))
			persistenceService.persist(wfItem)
		else:
			raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
	except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
if(validationErrors==None):
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getDepartment().getDeptName(),None,None,wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''IndentRateContract'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	persistenceService.persist(wfItem)
	result=wfItem
	validationErrors=None'
where NAME='Indent.workflow';

#DOWN

Delete from eg_script where name='works.generic.workflow';

update eg_script set script='from org.egov.pims.dao import EisDAOFactory
from org.egov.pims.commons import DesignationMaster
from org.egov.pims.commons.dao import DesignationMasterDAO
from org.egov.infstr.commons.dao import GenericDaoFactory
from org.egov.infstr import ValidationError
from org.egov.infstr import ValidationException
from org.egov import EGOVException
from  org.egov.exceptions import NoSuchObjectException
pimsDAO=EisDAOFactory.getDAOFactory().getPersonalInformationDAO()
empDeptDAO=EisDAOFactory.getDAOFactory().getEmployeeDepartmentDAO()
appConfigDAO=GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO()
def save_and_submit():
	print ''---------------------------save_and_submit--------------------------------''
	save_indent()
	return submit()
def save_indent():
	if not wfItem.getCurrentState():
		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
		wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',''NEW''))
		return (persistenceService.persist(wfItem),None)
	if wfItem.getCurrentState().getValue() == ''NEW'':
		return (persistenceService.persist(wfItem),None)
	if wfItem.getCurrentState().getValue() == ''REJECTED'':
		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()))
		return (persistenceService.persist(wfItem),None)
	return (None,None)
') where name = 'Indent.workflow';

update eg_script set script=script||('
def submit():
	state=''DEFAULT''
	if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()
	return approvals[state]()
def reject():
	print ''------------------------------------reject-----------------''
	position=persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation())
	wfItem.changeState(''REJECTED'',position, comments)
	wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',''REJECTED''))
	return (persistenceService.persist(wfItem),None)
def cancel():
	print ''------------------------------------cancel-----------------''
	position=persistenceService.getEisManager().getPositionforEmp(wfItem.getPreparedBy().getIdPersonalInformation())
	wfItem.changeState(''CANCELLED'',position,comments)
	wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',''CANCELLED''))
	wfItem.changeState(''END'',position,'''')
	return (persistenceService.persist(wfItem),None)
') where name = 'Indent.workflow';

update eg_script set script=script||('
def submit_for_approval():
	try:
		if wfItem.getCurrentState().getValue() == ''REJECTED'':
			state=''RESUBMITTED''
			if wfItem.getDepartment().getDeptName()==''Electrical'':
				action=''Pending for check''
			elif wfItem.getDepartment().getDeptName()==''Garden'':
				action=''Pending for Approval''
			else:
				action=''Pending for check''
		else:
			state=''CREATED''
			if wfItem.getDepartment().getDeptName()==''Electrical'':
				action=''Pending for check''
			elif wfItem.getDepartment().getDeptName()==''Garden'':
				action=''Pending for Approval''
			else:
				action=''Pending for check''
		position=find_position(wfItem)
		wfItem.changeState(state,action,position,comments)
		wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',state))
		result=persistenceService.persist(wfItem)
		return (result,None)
	except ValidationException,e:
		return (None,e.getErrors())
') where   name = 'Indent.workflow';

update eg_script set script=script||('
def submit_for_budget_appropriation():
	print ''---------------------------submit_for_budget_appropriation--------------------------------''
	try:
		try:
			check=persistenceService.getIndentRateContractService().checkForBudgetaryAppropriation(wfItem)
		except EGOVException,e:
			return (None,e.getMessage())
		if check:
			wfItem.setBudgetApprNo(persistenceService.getIndentRateContractService().getBudgetAppropriationNumber(wfItem))
			position=wfItem.getCurrentState().getOwner()
			wfItem.changeState(''BUDGETARY_APPROPRIATION_DONE'',''Pending Approval'',position,comments)
			persistenceService.persist(wfItem)
		else:
			raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
	except ValidationException,e:
		return (None,e.getErrors())
') where   name = 'Indent.workflow';

update eg_script set script=script||('
def approve():
	print ''---------------------------approve_section--------------------------------''
	try:
		currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
		if wfItem.getCurrentState().getValue()==''RESUBMITTED'':
			if wfItem.getDepartment().getDeptName()==''Electrical'':
				state=''CHECKED''
				action=''Pending for Approval''
				position=find_position(wfItem)
			elif wfItem.getDepartment().getDeptName()==''Garden'':
				if  wfItem.getIndentType()== ''Amount'':
					submit_for_budget_appropriation()
				state=''APPROVED''
				action=''''
				position=wfItem.getCurrentState().getOwner()
				wfItem.changeState(state,action,position,comments)
				wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',state))
				persistenceService.persist(wfItem)
			else:
				state=''CHECKED''
				action=''Pending for Approval''
				position=find_position(wfItem)
		if wfItem.getCurrentState().getValue()==''CREATED'':
			if wfItem.getDepartment().getDeptName()==''Electrical'':
				state=''CHECKED''
				action=''Pending for Approval''
				position=find_position(wfItem)
			elif wfItem.getDepartment().getDeptName()==''Garden'':
				if  wfItem.getIndentType()== ''Amount'':
					submit_for_budget_appropriation()
				state=''APPROVED''
				action=''''
				position=wfItem.getCurrentState().getOwner()
				wfItem.changeState(state,action,position,comments)
				wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',state))
				persistenceService.persist(wfItem)
			else:
				state=''CHECKED''
				action=''Pending for Approval''
				position=find_position(wfItem)
		if wfItem.getCurrentState().getValue()==''CHECKED'':
			if wfItem.getDepartment().getDeptName()==''Electrical'':
				if  wfItem.getIndentType()== ''Amount'':
					submit_for_budget_appropriation()
				state=''APPROVED''
				action=''''
				position=wfItem.getCurrentState().getOwner()
				wfItem.changeState(state,action,position,comments)
				wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',state))
				persistenceService.persist(wfItem)
			elif wfItem.getDepartment().getDeptName()!=''Garden'':
				if  wfItem.getIndentType()== ''Amount'':
					submit_for_budget_appropriation()
				state=''APPROVED''
				action=''''
				position=wfItem.getCurrentState().getOwner()
				wfItem.changeState(state,action,position,comments)
				wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',state))
				persistenceService.persist(wfItem)
		if state==''APPROVED'':
			state=''END''
			position=wfItem.getCurrentState().getOwner()
			action=''''
		wfItem.changeState(state,action,position,comments)
		if state!=''END'':
			wfItem.setEgwStatus(persistenceService.getCommonsMgr().getStatusByModuleAndCode(''IndentRateContract'',state))
		result=persistenceService.persist(wfItem)
		return (result,None)
	except ValidationException,e:
		return (None,e.getErrors())
') where   name = 'Indent.workflow';

update eg_script set script=script||('
def find_position(indent):
	try:
		next_position=persistenceService.getEisManager().getPositionforEmp(indent.getWorkflowApproverUserId())
	except:
		pass
	if not next_position:
		raise ValidationException,[ValidationError(''currentState.position'',''works.rateContract.workflow.no_employee_position'')]
	return next_position
approvals={
	''DEFAULT'':save_and_submit,
	''NEW'':submit_for_approval,
	''CREATED'':submit_for_approval,
	''REJECTED'':submit_for_approval
}
transitions={
	''save'':save_indent,
	''submit_for_approval'':submit,
	''reject'':reject,
	''cancel'':cancel,
	''approval'':approve,
	''admin_sanction'':approve
}
result,validationErrors=transitions[action.getName()]()
') where name = 'Indent.workflow';


