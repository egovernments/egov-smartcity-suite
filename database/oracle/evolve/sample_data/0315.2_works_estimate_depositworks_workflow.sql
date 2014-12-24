#UP
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
    save_estimate()        
    return submit()        
def save_estimate():        
    if not wfItem.getCurrentState():
        wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
        return (persistenceService.persist(wfItem),None)      
    if wfItem.getCurrentState().getValue() == ''CREATED'':      
        return (persistenceService.persist(wfItem),None) 
    if wfItem.getCurrentState().getValue() == ''REJECTED'':
        wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()))
        return (persistenceService.persist(wfItem),None)
    return (None,None)      
def submit():        
    state=''DEFAULT''        
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()        
    return approvals[state]() 
' where   name = 'AbstractEstimate.workflow'; 

update eg_script set script=script||(' 
def reject(): 
    currentState=wfItem.getCurrentState().getValue() 
    if wfItem.getFinancialDetails():  
        if wfItem.getFinancialDetails().get(0) and ((currentState==''TECH_SANCTIONED'' and (wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation Check'' or wfItem.getCurrentState().getNextAction()==''Pending Deposit Code Appropriation Check'' or wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction'' or wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction Check'')) or (currentState==''RESUBMITTED'' and (wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation Check'' or wfItem.getCurrentState().getNextAction()==''Pending Deposit Code Appropriation Check'' or wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction'')) or currentState==''TECH_SANCTIONED-Pending Budgetary Appropriation Approval'' or currentState==''BUDGETARY_APPROPRIATION_DONE'' or currentState==''BUDGETARY_APPR_CHECKED'' or currentState==''DEPOSIT_CODE_APPR_DONE'' or currentState==''DEPOSIT_CODE_APPR_CHECKED'' or currentState==''ADMIN_CHECKED''):
            if(isSkipBudget(wfItem)==''true''): 
		wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
		persistenceService.releaseDepositWorksAmountOnReject(wfItem.getFinancialDetails().get(0)) 
                wfItem.changeState(''REJECTED'',''Pending Deposit Code Appropriation'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getFinancialDetails().get(0).getCreatedBy().getId()).getIdPersonalInformation()), comments)
            else: 
	        wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo()) 
                persistenceService.releaseBudgetOnReject(wfItem.getFinancialDetails().get(0))	    
	        wfItem.setBudgetAvailable(persistenceService.getBudgetAvailable(wfItem)) 
	        wfItem.changeState(''REJECTED'',''Pending Budgetary Appropriation'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getFinancialDetails().get(0).getCreatedBy().getId()).getIdPersonalInformation()), comments) 
        elif wfItem.getFinancialDetails() and ((currentState==''REJECTED'' and (wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation'' or wfItem.getCurrentState().getNextAction()==''Pending Deposit Code Appropriation'')) or currentState==''TECH_SANCTIONED'' or currentState==''RESUBMITTED'' or currentState==''TECH_SANCTION_CHECKED''):  
    	    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()), comments)    
    else: 
	wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()), comments) 
    return (persistenceService.persist(wfItem),None) 
') where   name = 'AbstractEstimate.workflow';  
   
update eg_script set script=script||(' 
def cancel():
    try:
        if not persistenceService.validateCancelEstimate(wfItem):
        	raise ValidationException,[ValidationError(''cancel.estimate'',''abstractEstimate.validate.cancel'')]          
        wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()),comments)
	wfItem.changeState(''END'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()),'''')         
        return (persistenceService.persist(wfItem),None) 
    except ValidationException,e:        
        return (None,e.getErrors())       
def submit_for_approval():        
    try:        
        position=find_position(wfItem)  
	if wfItem.getTotalAmount().getValue() <=200000: 
	    if wfItem.getCurrentState().getValue() == ''REJECTED'':
		wfItem.changeState(''RESUBMITTED'',''Pending Approval for Technical Sanction'',position,comments) 
	    else:    
                wfItem.changeState(''CREATED'',''Pending Approval for Technical Sanction'',position,comments) 
	else:
	    if wfItem.getCurrentState().getValue() == ''REJECTED'':
            	wfItem.changeState(''RESUBMITTED'',''Pending checking for Technical Sanction'',position,comments) 
	    else:
		wfItem.changeState(''CREATED'',''Pending checking for Technical Sanction'',position,comments) 
        result=persistenceService.persist(wfItem)        
        return (result,None)        
    except ValidationException,e:        
        return (None,e.getErrors())   
') where   name = 'AbstractEstimate.workflow';   
  
update eg_script set script=script||('
def tech_sanction():        
    try:  
        position=find_position(wfItem)  
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()	
	if wfItem.getTotalAmount().getValue() <=200000 or wfItem.getCurrentState().getValue() == ''TECH_SANCTION_CHECKED'':
            if(isSkipBudget(wfItem)==''true''):
                state=''TECH_SANCTIONED''
                action=''Pending Deposit Code Appropriation''
	    else:
                state=''TECH_SANCTIONED'' 
	        action=''Pending Budgetary Appropriation''		 
	elif (currentDesig == ''ASSISTANT EXECUTIVE ENGINEER'' or currentDesig == ''ASSISTANT DIVISIONAL ENGINEER'') and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and wfItem.getTotalAmount().getValue() >3000000:
	    if wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
	        state=''RESUBMITTED''
	    else:
	        state=''CREATED'' 
	    action=''Pending checking for Technical Sanction'' 
	elif 200000 < wfItem.getTotalAmount().getValue() <=3000000 : 
	    state=''TECH_SANCTION_CHECKED'' 
	    action=''Pending Approval for Technical Sanction'' 
	elif (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'') and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and wfItem.getTotalAmount().getValue() >10000000:	
 	    if wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
	        state=''RESUBMITTED''
	    else:    
	        state=''CREATED'' 
	    action=''Pending checking for Technical Sanction'' 
	elif 3000000 < wfItem.getTotalAmount().getValue() <= 10000000 :  
	    state=''TECH_SANCTION_CHECKED'' 
	    action=''Pending Approval for Technical Sanction'' 
	elif currentDesig == ''SUPERINTENDING ENGINEER'' and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED''):        
	    state=''TECH_SANCTION_CHECKED'' 
	    action=''Pending Approval for Technical Sanction'' 	
        wfItem.changeState(state,action,position,comments)
        result=persistenceService.persist(wfItem)        
        return (result,None)        
    except ValidationException,e:        
        return (None,e.getErrors())
') where   name = 'AbstractEstimate.workflow';
update eg_script set script=script||('
def skip_budget_appropriation():
    position=find_position(wfItem)    
    if wfItem.getCurrentState().getValue() == ''REJECTED'':  
        wfItem.changeState(''RESUBMITTED'',''Pending Deposit Code Appropriation Check'',position,comments)   
    else:   
        wfItem.changeState(''TECH_SANCTIONED'',''Pending Deposit Code Appropriation Check'',position,comments)   
    result=persistenceService.persist(wfItem)            
    return result
def submit_for_budget_appropriation():   
    if(isSkipBudget(wfItem)==''true''):   
	try:
		try:   
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  	     	      
		        check=persistenceService.checkForBudgetaryAppropriationForDepositWorks(wfItem.getFinancialDetails().get(0))   
	    	except EGOVException,e:      
			return (None,e.getMessage()) 
		if check: 
			result = skip_budget_appropriation()
			return (result,None)
		raise ValidationException,[ValidationError(''depositworks.budget.check.fail'',''depositworks.budget.check.fail'')]         
	except ValidationException,e:            
	    return (None,e.getErrors())  
    else:
        try: 	  
            try:   
	        wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  	     	      
                check=persistenceService.checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))   
            except EGOVException,e:      
                return (None,e.getMessage())        
            if check:                 
	        wfItem.setBudgetAvailable(persistenceService.getBudgetAvailable(wfItem))   
	        position=find_position(wfItem)    
	        if wfItem.getCurrentState().getValue() == ''REJECTED'':  
                    wfItem.changeState(''RESUBMITTED'',''Pending Budgetary Appropriation Check'',position,comments)   
	        else:   
                    wfItem.changeState(''TECH_SANCTIONED'',''Pending Budgetary Appropriation Check'',position,comments)      
                result=persistenceService.persist(wfItem)            
                return (result,None)         
            raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]         
        except ValidationException,e:            
            return (None,e.getErrors())
') where   name = 'AbstractEstimate.workflow'; 
update eg_script set script=script||(' 
def approve_budget_appropriation(): 
   try: 
   	position=find_position(wfItem) 
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName() 
	if (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and currentDesig == ''SECTION MANAGER'': 
	    if wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
	        state=''RESUBMITTED''
	    else:       
	        state=''TECH_SANCTIONED'' 
	    if(isSkipBudget(wfItem)==''true''): 
	    	action=''Pending Deposit Code Appropriation Check''
	    else:
		action=''Pending Budgetary Appropriation Check'' 
	if (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER''): 
	    if(isSkipBudget(wfItem)==''true''): 
		state=''DEPOSIT_CODE_APPR_CHECKED'' 
	    	action=''Pending Deposit Code Appropriation Approval''
	    else:         
		state=''BUDGETARY_APPR_CHECKED'' 
	    	action=''Pending Budgetary Appropriation Approval''	
	elif (wfItem.getCurrentState().getValue() == ''BUDGETARY_APPR_CHECKED'' or wfItem.getCurrentState().getValue() == ''DEPOSIT_CODE_APPR_CHECKED'') and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer'') and wfItem.getTotalAmount().getValue() >1000000: 
	    if(isSkipBudget(wfItem)==''true''): 
		state=''DEPOSIT_CODE_APPR_DONE'' 
	    else: 
	    	state=''BUDGETARY_APPROPRIATION_DONE'' 
	    action=''Pending Admin Sanction Check'' 
	elif (wfItem.getCurrentState().getValue() == ''BUDGETARY_APPR_CHECKED'' or wfItem.getCurrentState().getValue() == ''DEPOSIT_CODE_APPR_CHECKED'') and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer'') and wfItem.getTotalAmount().getValue() <=1000000: 
		if(isSkipBudget(wfItem)==''true''): 
		    state=''DEPOSIT_CODE_APPR_DONE'' 
		else: 
		    state=''BUDGETARY_APPROPRIATION_DONE'' 
		action=''Pending Admin Sanction'' 
   	wfItem.changeState(state,action,position,comments)    
    	result=persistenceService.persist(wfItem)          
    	return (result,None) 
   except ValidationException,e:          
        return (None,e.getErrors())  
') where   name = 'AbstractEstimate.workflow';
update eg_script set script=script||('  
def admin_sanction(): 
   try: 
	print wfItem.getCurrentState().getValue() 	
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName() 
	print currentDesig
	if (wfItem.getCurrentState().getValue() == ''BUDGETARY_APPROPRIATION_DONE'' or wfItem.getCurrentState().getValue() == ''DEPOSIT_CODE_APPR_DONE'') and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer''):
	    state=''ADMIN_SANCTIONED'' 
	    action=''''
	    position=wfItem.getCurrentState().getOwner()
	elif currentDesig == ''CHIEF ENGINEER'':
	    if wfItem.getCurrentState().getValue() == ''BUDGETARY_APPROPRIATION_DONE'': 
	    	state=''BUDGETARY_APPROPRIATION_DONE'' 
	    elif wfItem.getCurrentState().getValue() == ''DEPOSIT_CODE_APPR_DONE'': 
	    	state=''DEPOSIT_CODE_APPR_DONE'' 
	    action=''Pending Admin Sanction Check'' 
	    position=find_position(wfItem)  
	elif (wfItem.getCurrentState().getValue() == ''BUDGETARY_APPROPRIATION_DONE'' or wfItem.getCurrentState().getValue() == ''DEPOSIT_CODE_APPR_DONE'') and (currentDesig == ''JOINT COMMISSIONER''):
	    state=''ADMIN_CHECKED'' 
	    action=''Pending Admin Sanction'' 
 	    position=find_position(wfItem) 
        elif (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer''):  
	    state=''ADMIN_SANCTIONED''   
	    action=''''
            position=wfItem.getCurrentState().getOwner()
        elif (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''CHIEF ENGINEER''):  
	    state=''TECH_SANCTIONED''   
	    action=''Pending Admin Sanction Check''
            position=find_position(wfItem)   
	elif wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' and (currentDesig == ''JOINT COMMISSIONER''):  
	    state=''ADMIN_CHECKED''   
	    action=''Pending Admin Sanction'' 
            position=find_position(wfItem)		
	elif wfItem.getCurrentState().getValue() == ''ADMIN_CHECKED'':
	    state=''ADMIN_SANCTIONED'' 
	    action='''' 
	    position=wfItem.getCurrentState().getOwner() 	
   	wfItem.changeState(state,action,position,comments)
	if state==''ADMIN_SANCTIONED'':
	    wfItem.changeState(''END'',action,wfItem.getCurrentState().getOwner(),comments) 
    	result=persistenceService.persist(wfItem)          
    	return (result,None) 
   except ValidationException,e:          
        return (None,e.getErrors())    
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
result,validationErrors=transitions[action.getName()]()')
where name = 'AbstractEstimate.workflow';


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
    save_estimate()        
    return submit()        
def save_estimate():        
    if not wfItem.getCurrentState():
        wfItem.changeState(''NEW'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getCreatedBy().getId()).getIdPersonalInformation()),'''')
        return (persistenceService.persist(wfItem),None)      
    if wfItem.getCurrentState().getValue() == ''CREATED'':      
        return (persistenceService.persist(wfItem),None) 
    if wfItem.getCurrentState().getValue() == ''REJECTED'':
        wfItem.getCurrentState().setOwner(persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()))
        return (persistenceService.persist(wfItem),None)
    return (None,None)      
def submit():        
    state=''DEFAULT''        
    if wfItem.getCurrentState(): state=wfItem.getCurrentState().getValue()        
    return approvals[state]() 
' where   name = 'AbstractEstimate.workflow'; 

update eg_script set script=script||(' 
def reject(): 
    currentState=wfItem.getCurrentState().getValue() 
    if wfItem.getFinancialDetails():  
        if wfItem.getFinancialDetails().get(0) and ((currentState==''TECH_SANCTIONED'' and (wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation Check'' or wfItem.getCurrentState().getNextAction()==''Pending Financial Sanction'' or wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction'' or wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction Check'')) or (currentState==''RESUBMITTED'' and (wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation Check'' or wfItem.getCurrentState().getNextAction()==''Pending Admin Sanction'')) or currentState==''TECH_SANCTIONED-Pending Budgetary Appropriation Approval'' or currentState==''BUDGETARY_APPROPRIATION_DONE'' or currentState==''BUDGETARY_APPR_CHECKED'' or currentState==''ADMIN_CHECKED''):
            if(isSkipBudget(wfItem)==''true''): 
		wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo())
		persistenceService.releaseDepositWorksAmountOnReject(wfItem.getFinancialDetails().get(0)) 
                wfItem.changeState(''REJECTED'',''Pending Financial Sanction'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getFinancialDetails().get(0).getCreatedBy().getId()).getIdPersonalInformation()), comments)
            else: 
	        wfItem.setBudgetRejectionNo(''BC/''+wfItem.getBudgetApprNo()) 
                persistenceService.releaseBudgetOnReject(wfItem.getFinancialDetails().get(0))	    
	        wfItem.setBudgetAvailable(persistenceService.getBudgetAvailable(wfItem)) 
	        wfItem.changeState(''REJECTED'',''Pending Budgetary Appropriation'',persistenceService.getEisManager().getPositionforEmp(persistenceService.getEisManager().getEmpForUserId(wfItem.getFinancialDetails().get(0).getCreatedBy().getId()).getIdPersonalInformation()), comments) 
        elif wfItem.getFinancialDetails() and ((currentState==''REJECTED'' and (wfItem.getCurrentState().getNextAction()==''Pending Budgetary Appropriation'' or wfItem.getCurrentState().getNextAction()==''Pending Financial Sanction'')) or currentState==''TECH_SANCTIONED'' or currentState==''RESUBMITTED'' or currentState==''TECH_SANCTION_CHECKED''):  
    	    wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()), comments)    
    else: 
	wfItem.changeState(''REJECTED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()), comments) 
    return (persistenceService.persist(wfItem),None) 
') where   name = 'AbstractEstimate.workflow';  
   
update eg_script set script=script||(' 
def cancel():
    try:
        if not persistenceService.validateCancelEstimate(wfItem):
        	raise ValidationException,[ValidationError(''cancel.estimate'',''abstractEstimate.validate.cancel'')]          
        wfItem.changeState(''CANCELLED'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()),comments)
	wfItem.changeState(''END'',persistenceService.getEisManager().getPositionforEmp(wfItem.getEstimatePreparedBy().getIdPersonalInformation()),'''')         
        return (persistenceService.persist(wfItem),None) 
    except ValidationException,e:        
        return (None,e.getErrors())       
def submit_for_approval():        
    try:        
        position=find_position(wfItem)  
	if wfItem.getTotalAmount().getValue() <=200000: 
	    if wfItem.getCurrentState().getValue() == ''REJECTED'':
		wfItem.changeState(''RESUBMITTED'',''Pending Approval for Technical Sanction'',position,comments) 
	    else:    
                wfItem.changeState(''CREATED'',''Pending Approval for Technical Sanction'',position,comments) 
	else:
	    if wfItem.getCurrentState().getValue() == ''REJECTED'':
            	wfItem.changeState(''RESUBMITTED'',''Pending checking for Technical Sanction'',position,comments) 
	    else:
		wfItem.changeState(''CREATED'',''Pending checking for Technical Sanction'',position,comments) 
        result=persistenceService.persist(wfItem)        
        return (result,None)        
    except ValidationException,e:        
        return (None,e.getErrors())   
') where   name = 'AbstractEstimate.workflow';     
update eg_script set script=script||('
def tech_sanction():        
    try:  
        position=find_position(wfItem)  
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()	
	if wfItem.getTotalAmount().getValue() <=200000 or wfItem.getCurrentState().getValue() == ''TECH_SANCTION_CHECKED'':
            if(isSkipBudget(wfItem)==''true''):
                state=''TECH_SANCTIONED''
                action=''Pending Financial Sanction''
	    else:
                state=''TECH_SANCTIONED'' 
	        action=''Pending Budgetary Appropriation''		 
	elif (currentDesig == ''ASSISTANT EXECUTIVE ENGINEER'' or currentDesig == ''ASSISTANT DIVISIONAL ENGINEER'') and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and wfItem.getTotalAmount().getValue() >3000000:
	    if wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
	        state=''RESUBMITTED''
	    else:
	        state=''CREATED'' 
	    action=''Pending checking for Technical Sanction'' 
	elif 200000 < wfItem.getTotalAmount().getValue() <=3000000 : 
	    state=''TECH_SANCTION_CHECKED'' 
	    action=''Pending Approval for Technical Sanction'' 
	elif (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'') and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and wfItem.getTotalAmount().getValue() >10000000:	
 	    if wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
	        state=''RESUBMITTED''
	    else:    
	        state=''CREATED'' 
	    action=''Pending checking for Technical Sanction'' 
	elif 3000000 < wfItem.getTotalAmount().getValue() <= 10000000 :  
	    state=''TECH_SANCTION_CHECKED'' 
	    action=''Pending Approval for Technical Sanction'' 
	elif currentDesig == ''SUPERINTENDING ENGINEER'' and (wfItem.getCurrentState().getValue() == ''CREATED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED''):        
	    state=''TECH_SANCTION_CHECKED'' 
	    action=''Pending Approval for Technical Sanction'' 	
        wfItem.changeState(state,action,position,comments)
        result=persistenceService.persist(wfItem)        
        return (result,None)        
    except ValidationException,e:        
        return (None,e.getErrors())
') where   name = 'AbstractEstimate.workflow';
update eg_script set script=script||('
def skip_budget_appropriation():
    if wfItem.getCurrentState().getValue() == ''REJECTED'':  
        state=''RESUBMITTED''  
    else:         
        state=''TECH_SANCTIONED''
    if wfItem.getTotalAmount().getValue() >1000000:
        action=''Pending Admin Sanction Check'' 
    else:
     	action=''Pending Admin Sanction''
    position=find_position(wfItem) 
    wfItem.changeState(state,action,position,comments)      
    result=persistenceService.persist(wfItem)            
    return result
def submit_for_budget_appropriation():   
    if(isSkipBudget(wfItem)==''true''):   
	try:
		try:   
			wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  	     	      
		        check=persistenceService.checkForBudgetaryAppropriationForDepositWorks(wfItem.getFinancialDetails().get(0))   
	    	except EGOVException,e:      
			return (None,e.getMessage()) 
		if check: 
			result = skip_budget_appropriation()
			return (result,None)
		raise ValidationException,[ValidationError(''depositworks.budget.check.fail'',''depositworks.budget.check.fail'')]         
	except ValidationException,e:            
	    return (None,e.getErrors())  
    else:
        try: 	  
            try:   
	        wfItem.setBudgetApprNo(persistenceService.getBudgetAppropriationNumber(wfItem))  	     	      
                check=persistenceService.checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))   
            except EGOVException,e:      
                return (None,e.getMessage())        
            if check:                 
	        wfItem.setBudgetAvailable(persistenceService.getBudgetAvailable(wfItem))   
	        position=find_position(wfItem)    
	        if wfItem.getCurrentState().getValue() == ''REJECTED'':  
                    wfItem.changeState(''RESUBMITTED'',''Pending Budgetary Appropriation Check'',position,comments)   
	        else:   
                    wfItem.changeState(''TECH_SANCTIONED'',''Pending Budgetary Appropriation Check'',position,comments)      
                result=persistenceService.persist(wfItem)            
                return (result,None)         
            raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]         
        except ValidationException,e:            
            return (None,e.getErrors())
') where   name = 'AbstractEstimate.workflow'; 
update eg_script set script=script||(' 
def approve_budget_appropriation(): 
   try: 
   	position=find_position(wfItem) 
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName() 
	if (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and currentDesig == ''SECTION MANAGER'': 
	    if wfItem.getCurrentState().getValue() == ''RESUBMITTED'':
	        state=''RESUBMITTED''
	    else:       
	        state=''TECH_SANCTIONED'' 
	    action=''Pending Budgetary Appropriation Check''
	if (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER''):         
	    state=''BUDGETARY_APPR_CHECKED'' 
	    action=''Pending Budgetary Appropriation Approval''	
	elif wfItem.getCurrentState().getValue() == ''BUDGETARY_APPR_CHECKED'' and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer'') and wfItem.getTotalAmount().getValue() >1000000:
	    state=''BUDGETARY_APPROPRIATION_DONE'' 
	    action=''Pending Admin Sanction Check''
	elif wfItem.getCurrentState().getValue() == ''BUDGETARY_APPR_CHECKED'' and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer'') and wfItem.getTotalAmount().getValue() <=1000000:
	    state=''BUDGETARY_APPROPRIATION_DONE'' 
	    action=''Pending Admin Sanction''
   	wfItem.changeState(state,action,position,comments)    
    	result=persistenceService.persist(wfItem)          
    	return (result,None) 
   except ValidationException,e:          
        return (None,e.getErrors())  
') where   name = 'AbstractEstimate.workflow';
update eg_script set script=script||('  
def admin_sanction(): 
   try: 
	print wfItem.getCurrentState().getValue() 	
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName() 
	print currentDesig
	if wfItem.getCurrentState().getValue() == ''BUDGETARY_APPROPRIATION_DONE'' and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer''):
	    state=''ADMIN_SANCTIONED'' 
	    action=''''
	    position=wfItem.getCurrentState().getOwner()
	elif wfItem.getCurrentState().getValue() == ''BUDGETARY_APPROPRIATION_DONE'' and (currentDesig == ''CHIEF ENGINEER''):
	    state=''BUDGETARY_APPROPRIATION_DONE'' 
	    action=''Pending Admin Sanction Check'' 
	    position=find_position(wfItem) 
	elif wfItem.getCurrentState().getValue() == ''BUDGETARY_APPROPRIATION_DONE'' and (currentDesig == ''JOINT COMMISSIONER''):
	    state=''ADMIN_CHECKED'' 
	    action=''Pending Admin Sanction'' 
 	    position=find_position(wfItem) 
        elif (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer''):  
	    state=''ADMIN_SANCTIONED''   
	    action=''''
            position=wfItem.getCurrentState().getOwner()
        elif (wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' or wfItem.getCurrentState().getValue() == ''RESUBMITTED'') and (currentDesig == ''CHIEF ENGINEER''):  
	    state=''TECH_SANCTIONED''   
	    action=''Pending Admin Sanction Check''
            position=find_position(wfItem)   
	elif wfItem.getCurrentState().getValue() == ''TECH_SANCTIONED'' and (currentDesig == ''JOINT COMMISSIONER''):  
	    state=''ADMIN_CHECKED''   
	    action=''Pending Admin Sanction'' 
            position=find_position(wfItem)		
	elif wfItem.getCurrentState().getValue() == ''ADMIN_CHECKED'':
	    state=''ADMIN_SANCTIONED'' 
	    action='''' 
	    position=wfItem.getCurrentState().getOwner() 	
   	wfItem.changeState(state,action,position,comments)
	if state==''ADMIN_SANCTIONED'':
	    wfItem.changeState(''END'',action,wfItem.getCurrentState().getOwner(),comments) 
    	result=persistenceService.persist(wfItem)          
    	return (result,None) 
   except ValidationException,e:          
        return (None,e.getErrors())    
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
result,validationErrors=transitions[action.getName()]()')
where name = 'AbstractEstimate.workflow';



