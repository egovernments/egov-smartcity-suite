#UP

update eg_script set script= (
'result=['''','''']
print wfItem 
workValue=0
if wfItem:    
    print wfItem.getTotalAmount().getValue()
    if wfItem.getTotalAmount():	
        workValue=wfItem.getTotalAmount().getValue()
    if wfItem.getCurrentState():
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
	action=wfItem.getCurrentState().getNextAction()
	if action:
    	    state=wfItem.getCurrentState().getValue()+''-''+action
    	else: 
	    state=wfItem.getCurrentState().getValue()
	print state
print workValue
def contains(theString, theQueryValue):
    return theString.find(theQueryValue) > -1	
if ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'')  and department == ''L-Electrical''):
    result[0]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department == ''''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
    result[1]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department != ''L-Electrical''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
elif (state==''TECH_SANCTION_CHECKED-Pending Approval for Technical Sanction''):
    result[0]="ASSISTANT" 
elif ((state == ''CREATED-Pending Approval for Technical Sanction'' or state == ''RESUBMITTED-Pending Approval for Technical Sanction'') and workValue <= 200000):
    result[0]="ASSISTANT" 
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 200000) and currentDesig == ''ASSISTANT EXECUTIVE ENGINEER''):
    result[0]="EXECUTIVE ENGINEER" 
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 200000) and currentDesig == ''ASSISTANT DIVISIONAL ENGINEER''):
    result[0]="DIVISIONAL ELECTRICAL ENGINEER"
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 3000000) and (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'')):
    result[0]="SUPERINTENDING ENGINEER" 
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 10000000) and currentDesig == ''SUPERINTENDING ENGINEER''):
    result[0]="CHIEF ENGINEER"
elif (state == ''TECH_SANCTIONED-Pending Budgetary Appropriation'' or state == ''REJECTED-Pending Budgetary Appropriation'' or state == ''TECH_SANCTIONED-Pending Deposit Code Appropriation'' or state == ''REJECTED-Pending Deposit Code Appropriation''):
    result[0]="SECTION MANAGER"
elif((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'' or state == ''TECH_SANCTIONED-Pending Deposit Code Appropriation Check'' or state == ''RESUBMITTED-Pending Deposit Code Appropriation Check'') and currentDesig == ''SECTION MANAGER'' and department == ''L-Electrical''):
    result[0]="DIVISIONAL ELECTRICAL ENGINEER"
elif ((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'' or state == ''TECH_SANCTIONED-Pending Deposit Code Appropriation Check'' or state == ''RESUBMITTED-Pending Deposit Code Appropriation Check'') and currentDesig == ''SECTION MANAGER'' ):
    result[0]="EXECUTIVE ENGINEER"
elif ((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'' or state == ''TECH_SANCTIONED-Pending Deposit Code Appropriation Check'' or state == ''RESUBMITTED-Pending Deposit Code Appropriation Check'') and (currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'' or currentDesig == ''EXECUTIVE ENGINEER'') and contains(department,''Zone'')):
    result[0]="Zonal Officer"
') where   name = 'AbstractEstimate.nextDesignation';

update eg_script set script=script||('
elif ((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'' or state == ''TECH_SANCTIONED-Pending Deposit Code Appropriation Check'' or state == ''RESUBMITTED-Pending Deposit Code Appropriation Check'') and (currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'' or currentDesig == ''EXECUTIVE ENGINEER'')):
    result[0]="SUPERINTENDING ENGINEER" 
elif ((state == ''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'' or state == ''DEPOSIT_CODE_APPR_CHECKED-Pending Deposit Code Appropriation Approval'') and workValue <=1000000 and currentDesig == ''SUPERINTENDING ENGINEER''):
    result[0]="SUPERINTENDING ENGINEER" 
elif ((state == ''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'' or state == ''DEPOSIT_CODE_APPR_CHECKED-Pending Deposit Code Appropriation Approval'') and workValue <=1000000 and currentDesig == ''Zonal Officer''):
    result[0]="Zonal Officer" 
elif ((state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction'' or state == ''DEPOSIT_CODE_APPR_DONE-Pending Admin Sanction'') and currentDesig == ''SUPERINTENDING ENGINEER''): 
    result[0]="SUPERINTENDING ENGINEER" 
elif ((state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction'' or state == ''DEPOSIT_CODE_APPR_DONE-Pending Admin Sanction'') and currentDesig == ''Zonal Officer''): 
    result[0]="Zonal Officer" 
elif ((state == ''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'' or state == ''DEPOSIT_CODE_APPR_CHECKED-Pending Deposit Code Appropriation Approval'') and workValue >1000000 and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer'')):
    result[0]="CHIEF ENGINEER" 
elif ((state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction Check'' or state == ''DEPOSIT_CODE_APPR_DONE-Pending Admin Sanction Check'') and currentDesig == ''CHIEF ENGINEER''):  
    result[0]="JOINT COMMISSIONER"
elif ((state == ''TECH_SANCTIONED-Pending Admin Sanction Check'' or state == ''RESUBMITTED-Pending Admin Sanction Check'') and currentDesig == ''CHIEF ENGINEER''):  
    result[0]="JOINT COMMISSIONER"     
elif ((state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction Check'' or state == ''DEPOSIT_CODE_APPR_DONE-Pending Admin Sanction Check'') and currentDesig == ''JOINT COMMISSIONER''):  
    result[0]="Commissioner" 
elif (state == ''TECH_SANCTIONED-Pending Admin Sanction Check'' and currentDesig == ''JOINT COMMISSIONER''):  
    result[0]="Commissioner"  
elif (state == ''ADMIN_CHECKED-Pending Admin Sanction'' and currentDesig == ''Commissioner''):  
    result[0]="Commissioner"  
') where   name = 'AbstractEstimate.nextDesignation';


#DOWN

update eg_script set script= (
'result=['''','''']
print wfItem 
workValue=0
if wfItem:    
    print wfItem.getTotalAmount().getValue()
    if wfItem.getTotalAmount():	
        workValue=wfItem.getTotalAmount().getValue()
    if wfItem.getCurrentState():
	currentDesig=wfItem.getCurrentState().getOwner().getDesigId().getDesignationName()
	action=wfItem.getCurrentState().getNextAction()
	if action:
    	    state=wfItem.getCurrentState().getValue()+''-''+action
    	else: 
	    state=wfItem.getCurrentState().getValue()
	print state
print workValue
def contains(theString, theQueryValue):
    return theString.find(theQueryValue) > -1	
if ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'')  and department == ''L-Electrical''):
    result[0]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department == ''''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
    result[1]="ASSISTANT DIVISIONAL ENGINEER"
elif ((state == ''DEFAULT'' or state == ''NEW'' or state == ''REJECTED'') and department != ''L-Electrical''):
    result[0]="ASSISTANT EXECUTIVE ENGINEER" 
elif (state==''TECH_SANCTION_CHECKED-Pending Approval for Technical Sanction''):
    result[0]="ASSISTANT" 
elif ((state == ''CREATED-Pending Approval for Technical Sanction'' or state == ''RESUBMITTED-Pending Approval for Technical Sanction'') and workValue <= 200000):
    result[0]="ASSISTANT" 
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 200000) and currentDesig == ''ASSISTANT EXECUTIVE ENGINEER''):
    result[0]="EXECUTIVE ENGINEER" 
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 200000) and currentDesig == ''ASSISTANT DIVISIONAL ENGINEER''):
    result[0]="DIVISIONAL ELECTRICAL ENGINEER"
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 3000000) and (currentDesig == ''EXECUTIVE ENGINEER'' or currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'')):
    result[0]="SUPERINTENDING ENGINEER" 
elif ((state == ''CREATED-Pending checking for Technical Sanction'' or state == ''RESUBMITTED-Pending checking for Technical Sanction'') and (workValue > 10000000) and currentDesig == ''SUPERINTENDING ENGINEER''):
    result[0]="CHIEF ENGINEER"
elif (state == ''TECH_SANCTIONED-Pending Budgetary Appropriation'' or state == ''REJECTED-Pending Budgetary Appropriation''):
    result[0]="SECTION MANAGER"
elif((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'') and currentDesig == ''SECTION MANAGER'' and department == ''L-Electrical''):
    result[0]="DIVISIONAL ELECTRICAL ENGINEER"
elif ((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'') and currentDesig == ''SECTION MANAGER'' ):
    result[0]="EXECUTIVE ENGINEER"
elif ((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'') and (currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'' or currentDesig == ''EXECUTIVE ENGINEER'') and contains(department,''Zone'')):
    result[0]="Zonal Officer"
') where   name = 'AbstractEstimate.nextDesignation';

update eg_script set script=script||('
elif ((state == ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'' or state == ''RESUBMITTED-Pending Budgetary Appropriation Check'')and (currentDesig == ''DIVISIONAL ELECTRICAL ENGINEER'' or currentDesig == ''EXECUTIVE ENGINEER'')):
    result[0]="SUPERINTENDING ENGINEER" 
elif (state == ''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'' and workValue <=1000000 and currentDesig == ''SUPERINTENDING ENGINEER''):
    result[0]="SUPERINTENDING ENGINEER" 
elif (state == ''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'' and workValue <=1000000 and currentDesig == ''Zonal Officer''):
    result[0]="Zonal Officer" 
elif (state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction'' and currentDesig == ''SUPERINTENDING ENGINEER''): 
    result[0]="SUPERINTENDING ENGINEER" 
elif (state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction'' and currentDesig == ''Zonal Officer''): 
    result[0]="Zonal Officer"
elif ((state == ''TECH_SANCTIONED-Pending Financial Sanction'' or state == ''REJECTED-Pending Financial Sanction'') and workValue <=1000000 and 
contains(department,''Zone'')):  
    result[0]="Zonal Officer"  
elif ((state == ''TECH_SANCTIONED-Pending Financial Sanction'' or state == ''REJECTED-Pending Financial Sanction'') and workValue <=1000000):  
    result[0]="SUPERINTENDING ENGINEER" 
elif (state == ''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'' and workValue >1000000 and (currentDesig == ''SUPERINTENDING ENGINEER'' or currentDesig == ''Zonal Officer'')):
    result[0]="CHIEF ENGINEER" 
elif ((state == ''TECH_SANCTIONED-Pending Financial Sanction'' or state == ''REJECTED-Pending Financial Sanction'') and workValue >1000000):  
    result[0]="CHIEF ENGINEER"  
elif (state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction Check'' and currentDesig == ''CHIEF ENGINEER''):  
    result[0]="JOINT COMMISSIONER"
elif ((state == ''TECH_SANCTIONED-Pending Admin Sanction Check'' or state == ''RESUBMITTED-Pending Admin Sanction Check'') and currentDesig == ''CHIEF ENGINEER''):  
    result[0]="JOINT COMMISSIONER"     
elif (state == ''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction Check'' and currentDesig == ''JOINT COMMISSIONER''):  
    result[0]="Commissioner" 
elif (state == ''TECH_SANCTIONED-Pending Admin Sanction Check'' and currentDesig == ''JOINT COMMISSIONER''):  
    result[0]="Commissioner"  
elif (state == ''ADMIN_CHECKED-Pending Admin Sanction'' and currentDesig == ''Commissioner''):  
    result[0]="Commissioner"  
') where   name = 'AbstractEstimate.nextDesignation';

