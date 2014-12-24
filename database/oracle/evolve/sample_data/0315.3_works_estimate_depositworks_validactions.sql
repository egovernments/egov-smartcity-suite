#UP

UPDATE EG_SCRIPT set SCRIPT=
'transitions={''DEFAULT'':[''save'',''submit_for_approval''],''NEW'':[''save'',''submit_for_approval'',''cancel'' ],''CREATED-Pending checking for Technical Sanction'':[''reject'',''tech_sanction''],''RESUBMITTED-Pending checking for Technical Sanction'':[''reject'',''tech_sanction''],''CREATED-Pending Approval for Technical Sanction'':[''reject'',''tech_sanction''],''RESUBMITTED-Pending Approval for Technical Sanction'':[''reject'',''tech_sanction''],''TECH_SANCTION_CHECKED-Pending Approval for Technical Sanction'':[''reject'',''tech_sanction''],''CANCELLED'':[''''],''REJECTED'':[''save'',''submit_for_approval'',''cancel''],''TECH_SANCTIONED-Pending Budgetary Appropriation'':[''reject''],''REJECTED-Pending Budgetary Appropriation'':[''reject''], ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'':[''reject'',''budget_appropriation''],''RESUBMITTED-Pending Budgetary Appropriation Check'':[''reject'',''budget_appropriation''],''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'':[''reject'',''budget_appropriation''],''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction Check'':[''reject'',''admin_sanction''],''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction'':[''reject'',''admin_sanction''],''ADMIN_CHECKED-Pending Admin Sanction'':[''reject'',''admin_sanction''],''ADMIN_SANCTIONED'':[''''],''TECH_SANCTIONED-Pending Admin Sanction'':[''reject'',''admin_sanction''],''RESUBMITTED-Pending Admin Sanction'':[''reject'',''admin_sanction''],''RESUBMITTED-Pending Admin Sanction Check'':[''reject'',''admin_sanction''],''TECH_SANCTIONED-Pending Admin Sanction Check'':[''reject'',''admin_sanction''],
''TECH_SANCTIONED-Pending Deposit Code Appropriation'':[''reject''],''REJECTED-Pending Deposit Code Appropriation'':[''reject''], ''TECH_SANCTIONED-Pending Deposit Code Appropriation Check'':[''reject'',''budget_appropriation''],''RESUBMITTED-Pending Deposit Code Appropriation Check'':[''reject'',''budget_appropriation''],''DEPOSIT_CODE_APPR_CHECKED-Pending Deposit Code Appropriation Approval'':[''reject'',''budget_appropriation''],''DEPOSIT_CODE_APPR_DONE-Pending Admin Sanction Check'':[''reject'',''admin_sanction''],''DEPOSIT_CODE_APPR_DONE-Pending Admin Sanction'':[''reject'',''admin_sanction'']}
state=''DEFAULT''
action=''''
if wfItem.getCurrentState(): 
    state=wfItem.getCurrentState().getValue()
    action=wfItem.getCurrentState().getNextAction()
    if action:
    	state=state+''-''+action 
    else: 
	state=state 
result=[]
print state
if state in transitions: result = transitions[state]
'
WHERE NAME='AbstractEstimate.workflow.validactions';

#DOWN

UPDATE EG_SCRIPT set SCRIPT=
'transitions={''DEFAULT'':[''save'',''submit_for_approval''],''NEW'':[''save'',''submit_for_approval'',''cancel'' ],''CREATED-Pending checking for Technical Sanction'':[''reject'',''tech_sanction''],''RESUBMITTED-Pending checking for Technical Sanction'':[''reject'',''tech_sanction''],''CREATED-Pending Approval for Technical Sanction'':[''reject'',''tech_sanction''],''RESUBMITTED-Pending Approval for Technical Sanction'':[''reject'',''tech_sanction''],''TECH_SANCTION_CHECKED-Pending Approval for Technical Sanction'':[''reject'',''tech_sanction''],''CANCELLED'':[''''],''REJECTED'':[''save'',''submit_for_approval'',''cancel''],''TECH_SANCTIONED-Pending Budgetary Appropriation'':[''reject''],''REJECTED-Pending Budgetary Appropriation'':[''reject''], ''TECH_SANCTIONED-Pending Budgetary Appropriation Check'':[''reject'',''budget_appropriation''],''RESUBMITTED-Pending Budgetary Appropriation Check'':[''reject'',''budget_appropriation''],''BUDGETARY_APPR_CHECKED-Pending Budgetary Appropriation Approval'':[''reject'',''budget_appropriation''],''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction Check'':[''reject'',''admin_sanction''],''BUDGETARY_APPROPRIATION_DONE-Pending Admin Sanction'':[''reject'',''admin_sanction''],''ADMIN_CHECKED-Pending Admin Sanction'':[''reject'',''admin_sanction''],''ADMIN_SANCTIONED'':[''''],''TECH_SANCTIONED-Pending Financial Sanction'':[''reject''],''REJECTED-Pending Financial Sanction'':[''reject''],''TECH_SANCTIONED-Pending Admin Sanction'':[''reject'',''admin_sanction''],''RESUBMITTED-Pending Admin Sanction'':[''reject'',''admin_sanction''],''RESUBMITTED-Pending Admin Sanction Check'':[''reject'',''admin_sanction''],''TECH_SANCTIONED-Pending Admin Sanction Check'':[''reject'',''admin_sanction'']}
state=''DEFAULT''
action=''''
if wfItem.getCurrentState(): 
    state=wfItem.getCurrentState().getValue()
    action=wfItem.getCurrentState().getNextAction()
    if action:
    	state=state+''-''+action 
    else: 
	state=state 
result=[]
print state
if state in transitions: result = transitions[state]
'
WHERE NAME='AbstractEstimate.workflow.validactions';



