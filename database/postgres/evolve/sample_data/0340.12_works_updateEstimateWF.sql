#UP

UPDATE EG_SCRIPT 
SET SCRIPT='from org.egov.infstr import ValidationError  
from org.egov.infstr import ValidationException  
from org.egov import EGOVException
validationErrors=None
result=None
if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation Check'':
	try:
		if(wfItem.getTotalAmount().getValue() > persistenceService.getBudgetAvailable(wfItem).doubleValue()):
			raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.budget.amount'',''abstractEstimate.estimate.validate.budget.amount'')]
	except ValidationException,e:
		result=None
		validationErrors=e.getErrors()
if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getNextAction()==''Pending Deposit Code Appropriation Check'':
	try:
		if(wfItem.getTotalAmount().getValue() > persistenceService.getDepositWorksBalance(wfItem).doubleValue()):
			raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.deposit.amount'',''abstractEstimate.estimate.validate.deposit.amount'')]
	except ValidationException,e:
		result=None
		validationErrors=e.getErrors()
if(validationErrors==None):
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getExecutingDepartment().getDeptName(),wfItem.getAmountWfRule(),wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())  
	wfItem=scriptService.executeScript(''works.generic.workflow'',scriptContext)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfmatrix.getNextStatus()!=None:
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',wfmatrix.getNextStatus()])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Reject''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',''REJECTED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Cancel''):
		status=persistenceService.find(''from EgwStatus where moduletype=? and code=?'',[''AbstractEstimate'',''CANCELLED''])
		wfItem.setEgwStatus(status)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''TECH_SANCTIONED'':
		persistenceService.setTechSanctionNumber(wfItem)
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''BUDGETARY_APPR_CHECKED'':
		try:
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))
			check=persistenceService.checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))
			if check:
				persistenceService.persist(wfItem)
			else:
				raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
' where name = 'AbstractEstimate.workflow';

update eg_script set script=script||('
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getValue()==''DEPOSIT_CODE_APPR_CHECKED'':
		try:
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))
			check=persistenceService.checkForBudgetaryAppropriationForDepositWorks(wfItem.getFinancialDetails().get(0))
			if check:
				persistenceService.persist(wfItem)
			else:
				raise ValidationException,[ValidationError(''depositworks.budget.check.fail'',''depositworks.budget.check.fail'')]
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	if(action.getName()==''Approve'' or action.getName()==''Forward'') and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED'':
		persistenceService.setProjectCode(wfItem)
	if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPROPRIATION_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''BUDGETARY_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''budgetApp''):
		try:
			wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
			check=persistenceService.releaseBudgetOnReject(wfItem.getFinancialDetails().get(0))
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	if(action.getName()==''Reject'' and (wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_CHECKED'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_DONE'' or wfItem.getCurrentState().getPrevious().getValue()==''DEPOSIT_CODE_APPR_CHECKED'') and wfmatrix.getAdditionalRule()==''depositCodeApp''):
		try:
			wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
			check=persistenceService.releaseDepositWorksAmountOnReject(wfItem.getFinancialDetails().get(0))
		except ValidationException,e:
			result=None
			validationErrors=e.getErrors()
	persistenceService.persist(wfItem)
if(validationErrors==None):
	result=wfItem
	validationErrors=None')
where NAME='AbstractEstimate.workflow';

#DOWN

update eg_script set script=
'from org.egov.pims.dao import EisDAOFactory          
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
    save_estimate()        
    return submit()        
def save_estimate():
    if wfItem.getIsSpillOverWorks():        
	    if not wfItem.getCurrentState():   
		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
		return (persistenceService.persist(wfItem),None)      
	    if wfItem.getCurrentState().getValue() == ''CREATED'':      
		return (persistenceService.persist(wfItem),None) 
	    if wfItem.getCurrentState().getValue() == ''REJECTED'':
		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()))
		return (persistenceService.persist(wfItem),None)      
	    return (None,None)
    else:
    	print ''-----------------Normal Work---------------''
    	if not wfItem.getCurrentState():
    		wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
    		return (persistenceService.persist(wfItem),None)
    	if wfItem.getCurrentState().getValue() == ''CREATED'':
    		return (persistenceService.persist(wfItem),None)
    	if wfItem.getCurrentState().getValue() == ''REJECTED'':
    		wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()))
    		return (persistenceService.persist(wfItem),None)
    	return (None,None)      
' where   name = 'AbstractEstimate.workflow';

update eg_script set script=script||('
def submit():
    if wfItem.getIsSpillOverWorks():        
	    state=''DEFAULT''        
	    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()        
	    return approvals[state]()
    else:
    	print ''--------------Normal Work----------------''
    	state=''DEFAULT''
    	if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()
    	return approvals[state]()
def reject():
    	print ''---------------------------reject--------------------------------''   
	wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()), comments)   
    	return (persistenceService.persist(wfItem),None)        
def cancel():
    print ''---------------------------cancel--------------------------------''
    try:
        if not persistenceService.validateCancelEstimate(wfItem):
        	raise ValidationException,[ValidationError(''cancel.estimate'',''abstractEstimate.validate.cancel'')]           
        wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()),comments)
	wfItem.changeState(''END'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()),'''')         
        return (persistenceService.persist(wfItem),None) 
    except ValidationException,e:        
        return (None,e.getErrors())
') where   name = 'AbstractEstimate.workflow';

update eg_script set script=script||('
def submit_for_approval():
    print ''---------------------------submit_for_approval--------------------------------''
    if wfItem.getIsSpillOverWorks():  
    	try:      
		state=''DEFAULT''       
		if wfItem.getCurrentState(): 
			state=wfItem.getCurrentState().getValue()  
	    	return tech_sanction()
    	except ValidationException,e:        
		return (None,e.getErrors())
    else:
    	print ''--------------Normal Work---------------''
	# try:      
		# state=''DEFAULT''       
		# if wfItem.getCurrentState(): 
			# state=wfItem.getCurrentState().getValue()    
			# if(isSkipBudget(wfItem)==''true''): 
				# if(wfItem.getTotalAmount().getValue() > persistenceService.getDepositWorksBalance(wfItem).doubleValue()): 
					# raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.deposit.amount'',''abstractEstimate.estimate.validate.deposit.amount'')]
			# else :  
				# if(wfItem.getTotalAmount().getValue() > persistenceService.getBudgetAvailable(wfItem).doubleValue()): 
					# raise ValidationException,[ValidationError(''abstractEstimate.estimate.validate.budget.amount'',''abstractEstimate.estimate.validate.budget.amount'')]
	    	# return 
    	# except ValidationException,e:        
		# return (None,e.getErrors())
    	try:
    		state=''DEFAULT''
    		if wfItem.getCurrentState():
    			state=wfItem.getCurrentState().getValue()
    		return tech_sanction()
    	except ValidationException,e:
    		return (None,e.getErrors()) 
') where   name = 'AbstractEstimate.workflow';     

update eg_script set script=script||('
def tech_sanction():
    print ''---------------------------tech_sanction--------------------------------''
    if wfItem.getIsSpillOverWorks():        
	    try:  
		position=find_position(wfItem)
		currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
		persistenceService.setTechSanctionNumber(wfItem) 
	    	if(isSkipBudget(wfItem)==''true''):  
			wfItem.changeState(''TECH_SANCTIONED'',''Pending Deposit Code Appropriation'',position,comments) 
		else:
			wfItem.changeState(''TECH_SANCTIONED'',''Pending Budgetary Appropriation'',position,comments)
		persistenceService.persist(wfItem)        
		return submit_for_budget_appropriation()         
	    except ValidationException,e:        
		return (None,e.getErrors())
    else:
    	print ''------------Normal Work--------------''
    	try:
    		position=find_position(wfItem)
    		currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
    		persistenceService.setTechSanctionNumber(wfItem)
    		if(isSkipBudget(wfItem)==''true''):
    			wfItem.changeState(''TECH_SANCTIONED'',''Pending Deposit Code Appropriation'',position,comments)
    		else:
    			wfItem.changeState(''TECH_SANCTIONED'',''Pending Budgetary Appropriation'',position,comments)
    		persistenceService.persist(wfItem)
    		return submit_for_budget_appropriation()
    	except ValidationException,e:
    		return (None,e.getErrors())
') where   name = 'AbstractEstimate.workflow';

update eg_script set script=script||(' 
def skip_budget_appropriation():  
    position=find_position(wfItem)   
    wfItem.changeState(''DEPOSIT_CODE_APPR_DONE'',''Pending Admin Sanction'',position,comments)     
    result=persistenceService.persist(wfItem)  
    result=admin_sanction()             
    return result  
def submit_for_budget_appropriation():
   	print ''---------------------------submit_for_budget_appropriation--------------------------------''
	if wfItem.getIsSpillOverWorks():   
		if(isSkipBudget(wfItem)==''true''):     
			try:  
				result = skip_budget_appropriation()  
				return (result,None)           
			except ValidationException,e:              
				return (None,e.getErrors()) 
		else: 
		   try: 		
			    position=find_position(wfItem)    
			    wfItem.changeState(''BUDGETARY_APPROPRIATION_DONE'',''Pending Admin Sanction'',position,comments)      
			    result=persistenceService.persist(wfItem)            
			    return admin_sanction()        
		   except ValidationException,e:            
			return (None,e.getErrors())
	else:
		print ''--------------Normal Works------------''
		if(isSkipBudget(wfItem)==''true''):
			try:
				result = skip_budget_appropriation()
				return (result,None)
			except ValidationException,e:
				return (None,e.getErrors())
		else:
			try:
				position=find_position(wfItem)
				wfItem.changeState(''BUDGETARY_APPROPRIATION_DONE'',''Pending Admin Sanction'',position,comments)
				result=persistenceService.persist(wfItem)
				return admin_sanction()
			except ValidationException,e:
				return (None,e.getErrors()) 

def approve_budget_appropriation(): 
   print ''---------------------------approve_budget_appropriation--------------------------------''
   if wfItem.getIsSpillOverWorks():
	   try: 
	   	position=find_position(wfItem)
	   	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName() 
		if(isSkipBudget(wfItem)==''true''):  	
			wfItem.changeState(''DEPOSIT_CODE_APPR_DONE'',''Pending Admin Sanction'',position,comments)
		else:
			wfItem.changeState(''BUDGETARY_APPROPRIATION_DONE'',''Pending Admin Sanction'',position,comments)
		result=persistenceService.persist(wfItem)          
	    	return (result,None) 
	   except ValidationException,e:          
		return (None,e.getErrors())
   else:
   	print ''-------------Normal Works------------''
   	try:
   		position=find_position(wfItem)
   		currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
   		if(isSkipBudget(wfItem)==''true''):
   			wfItem.changeState(''DEPOSIT_CODE_APPR_DONE'',''Pending Admin Sanction'',position,comments)
   		else:
   			wfItem.changeState(''BUDGETARY_APPROPRIATION_DONE'',''Pending Admin Sanction'',position,comments)
   		result=persistenceService.persist(wfItem)
   		return (result,None)
   	except ValidationException,e:
   		return (None,e.getErrors())  
') where   name = 'AbstractEstimate.workflow';

update eg_script set script=script||(' 
def admin_sanction_stateChange():  
	try: 	
		currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
		print ''----------------Generating Project Code....''
		persistenceService.setProjectCode(wfItem)
		state=''ADMIN_SANCTIONED'' 
		action=''''
		position=wfItem.getCurrentState().getOwner() 
		wfItem.changeState(state,action,position,comments)
		if state==''ADMIN_SANCTIONED'':  
	    		wfItem.changeState(''END'',action,wfItem.getCurrentState().getOwner(),comments) 
		result=persistenceService.persist(wfItem)                
	    	return result  
	except ValidationException,e:          
		return (None,e.getErrors()) 
def admin_sanction():
   print ''---------------------------admin_sanction--------------------------------''
   if wfItem.getIsSpillOverWorks(): 
	if(isSkipBudget(wfItem)==''true''):     
		try:  
			try:     
				wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))         
				check=persistenceService.checkForBudgetaryAppropriationForDepositWorks(wfItem.getFinancialDetails().get(0))     
		    	except EGOVException,e:        
				return (None,e.getMessage())   
			if check:                 
				result = admin_sanction_stateChange()
				return (result,None)  
			raise ValidationException,[ValidationError(''depositworks.budget.check.fail'',''depositworks.budget.check.fail'')]           
		except ValidationException,e:              
		    return (None,e.getErrors()) 
	else: 
	   try: 	  
		try:   
		    wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  	     	      
		    check=1   
		except EGOVException,e:      
		    return (None,e.getMessage())        
		if check:                 
		    	wfItem.setBudgetAvailable(persistenceService.getBudgetAvailable(wfItem))   
			result = admin_sanction_stateChange() 
			return (result,None) 
		raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]         
	   except ValidationException,e:            
		return (None,e.getErrors())	
   else:
   	print ''------------Normal Works--------------''
   	if(isSkipBudget(wfItem)==''true''):
   		try:
   			try:
   				wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))
   				check=persistenceService.checkForBudgetaryAppropriationForDepositWorks(wfItem.getFinancialDetails().get(0))
   			except EGOVException,e:
   				return (None,e.getMessage())
   			if check:
   				result = admin_sanction_stateChange()
   				return (result,None)
   			raise ValidationException,[ValidationError(''depositworks.budget.check.fail'',''depositworks.budget.check.fail'')]
   		except ValidationException,e:
   			return (None,e.getErrors())
   	else:
   		try:
			try:
				wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))
   				check=1
   			except EGOVException,e:
   				return (None,e.getMessage())
   			if check:
   				wfItem.setBudgetAvailable(persistenceService.getBudgetAvailable(wfItem))
   				result = admin_sanction_stateChange()
   				return (result,None)
   			raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]
   		except ValidationException,e:
   			return (None,e.getErrors()) 
') where   name = 'AbstractEstimate.workflow';

update eg_script set script=script||('     
def find_position(estimate):            
    try:	
	print estimate.getApproverUserId()
        next_position=persistenceService.getEisManager().getPositionforEmp(estimate.getApproverUserId())    
    except:
        pass
    if not next_position:
        raise ValidationException,[ValidationError(''currentState.position'',''works.estimateworkflow.no_employee_position'')] 
    return next_position   
def isSkipBudget(wfItem):     
    for appValues in appConfigDAO.getConfigValuesByModuleAndKey(''Works'',''SKIP_BUDGET_CHECK''):      
        if wfItem.getType().getName()==appValues.getValue():  
            return ''true''  
    return ''false''     
approvals={        
    ''DEFAULT'':save_and_submit,
    ''NEW'':submit_for_approval,        
    ''CREATED'':submit_for_approval,           
    ''REJECTED'':submit_for_approval        
}        
transitions={        
 ''save'':save_estimate,        
 ''submit_for_approval'':submit,        
 ''reject'':reject,        
 ''cancel'':cancel,
 ''tech_sanction'':tech_sanction, 
 ''budget_details_save'':submit_for_budget_appropriation,
 ''budget_appropriation'':approve_budget_appropriation,
 ''admin_sanction'':admin_sanction
}        
result,validationErrors=transitions[action.getName()]()
') where name = 'AbstractEstimate.workflow';


