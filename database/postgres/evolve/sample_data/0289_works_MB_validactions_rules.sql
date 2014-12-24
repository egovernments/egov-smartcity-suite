#UP

UPDATE EG_SCRIPT set SCRIPT=
'transitions={''DEFAULT'':[''save'',''submit_for_approval''],''NEW'':[''save'',''submit_for_approval'',''cancel'' ],''CANCELLED'':[''''],''REJECTED'':[''save'',''submit_for_approval'',''cancel''],''CREATED-Pending for check'':[''reject'',''approval''],''RESUBMITTED-Pending for check'':[''reject'',''approval''],''CHECKED-Pending checking for correctness'':[''reject'',''approval''],''CHECKED-Pending for Approval'':[''reject'',''approval''],''APPROVED'':['''']}

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
WHERE NAME='MBHeader.workflow.validactions';

#DOWN

UPDATE EG_SCRIPT set SCRIPT=
'transitions={''DEFAULT'':[''save'',''submit_for_approval''],''NEW'':[''save'',''submit_for_approval'',''cancel'' ],''CANCELLED'':[''''],''REJECTED'':[''save'',''submit_for_approval'',''cancel''],''CREATED-Pending for check'':[''reject'',''approval''],''RESUBMITTED-Pending for check'':[''reject'',''approval''],''CREATED-Pending for Approval'':[''reject'',''approval''],''RESUBMITTED-Pending for Approval'':[''reject'',''tech_sanction''],''CHECKED-Pending for Approval'':[''reject'',''approval'',''cancel''],''APPROVED'':[''cancel'']}

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
WHERE NAME='MBHeader.workflow.validactions';

