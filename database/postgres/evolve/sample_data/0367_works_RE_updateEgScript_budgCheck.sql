#UP

UPDATE EG_SCRIPT 
SET SCRIPT=
'from org.egov.infstr import ValidationError      
from org.egov.infstr import ValidationException      
from org.egov import EGOVException    
validationErrors=None    
result=None    
try:    
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getExecutingDepartment().getDeptName(),None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())
except ValidationException,e:    
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
	if(action.getName()==''Approve'' and  wfItem.getCurrentState().getValue()==''END'' and wfItem.getCurrentState().getPrevious().getValue()==''TECH_SANCTIONED'') or (action.getName()==''Forward'' and wfItem.getCurrentState().getValue()==''TECH_SANCTIONED''):    
		persistenceService.getAbstractEstimateService().setTechSanctionNumberForRE(wfItem)    
	if(action.getName()==''Approve'') and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED'':    
		try:    
			print wfItem.getTotalAmount().getValue()
 			if(wfItem.getIsBudgetCheckRequired()):	  
				""" if(wfItem.getTotalAmount().getValue() > persistenceService.getAbstractEstimateService().getBudgetAvailable(wfItem).doubleValue()):    
					raise ValidationException,[ValidationError(''reEstimate.estimate.validate.budget.amount'',''reEstimate.estimate.validate.budget.amount'')] """  
				wfItem.setBudgetApprNo(persistenceService.getAbstractEstimateService().getBudgetAppropriationNumber(wfItem))
			# check=persistenceService.getAbstractEstimateService().checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))   
			check=1    
			if check:    
				persistenceService.persist(wfItem)    
			else:    
				raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]  			  
		except ValidationException,e:    
			result=None    
			validationErrors=e.getErrors()     
	persistenceService.persist(wfItem)    
if(validationErrors==None):    
	result=wfItem    
	validationErrors=None'
where NAME='RevisionAbstractEstimate.workflow';  


#DOWN

UPDATE EG_SCRIPT 
SET SCRIPT=
'from org.egov.infstr import ValidationError      
from org.egov.infstr import ValidationException      
from org.egov import EGOVException    
validationErrors=None    
result=None    
try:    
	wfmatrix=workflowService.getWfMatrix(wfItem.getStateType(),wfItem.getExecutingDepartment().getDeptName(),None,wfItem.getAdditionalWfRule(),wfItem.getCurrentState().getValue(),wfItem.getCurrentState().getNextAction())    
except ValidationException,e:    
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
	if(action.getName()==''Approve'' and  wfItem.getCurrentState().getValue()==''END'' and wfItem.getCurrentState().getPrevious().getValue()==''TECH_SANCTIONED'') or (action.getName()==''Forward'' and wfItem.getCurrentState().getValue()==''TECH_SANCTIONED''):    
		persistenceService.getAbstractEstimateService().setTechSanctionNumberForRE(wfItem)    
	if(action.getName()==''Approve'') and wfItem.getCurrentState().getPrevious().getValue()==''ADMIN_SANCTIONED'':    
		try:    
			print wfItem.getTotalAmount().getValue() 				  
			""" if(wfItem.getTotalAmount().getValue() > persistenceService.getAbstractEstimateService().getBudgetAvailable(wfItem).doubleValue()):    
				raise ValidationException,[ValidationError(''reEstimate.estimate.validate.budget.amount'',''reEstimate.estimate.validate.budget.amount'')] """  
			wfItem.setBudgetApprNo(persistenceService.getAbstractEstimateService().getBudgetAppropriationNumber(wfItem))    
			# check=persistenceService.getAbstractEstimateService().checkForBudgetaryAppropriation(wfItem.getFinancialDetails().get(0))   
			check=1    
			if check:    
				persistenceService.persist(wfItem)    
			else:    
				raise ValidationException,[ValidationError(''budget.check.fail'',''budget.check.fail'')]  			  
		except ValidationException,e:    
			result=None    
			validationErrors=e.getErrors()     
	persistenceService.persist(wfItem)    
if(validationErrors==None):    
	result=wfItem    
	validationErrors=None'
where NAME='RevisionAbstractEstimate.workflow';  
