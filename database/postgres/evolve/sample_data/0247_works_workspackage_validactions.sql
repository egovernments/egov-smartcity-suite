#UP
update EG_SCRIPT
SET SCRIPT='transitions={''DEFAULT'':[''approve''],''APPROVED'':['''']}  
state=''DEFAULT''  
if wfItem.getCurrentState():  
    state=wfItem.getCurrentState().getValue()  
result=[]  
if state in transitions: result = transitions[state]'
where NAME='WorksPackage.workflow.validactions';

#DOWN
update EG_SCRIPT
SET SCRIPT=
'transitions={''DEFAULT'':[''submit_for_approval''],''CREATED'':[''approve'',''reject''],''CANCELLED'':[''''],''REJECTED'':[''submit_for_approval'',''cancel''],''APPROVED'':[''''],''RESUBMITTED'':[''approve'',''reject''],''CHECKED'':[''approve'',''reject'']}  
state=''DEFAULT''  
if wfItem.getCurrentState():  
    state=wfItem.getCurrentState().getValue()  
result=[]  
if state in transitions: result = transitions[state]'
where NAME='WorksPackage.workflow.validactions';
