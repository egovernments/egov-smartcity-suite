#UP
update  EG_WF_ACTIONS set NAME='approve', DESCRIPTION='SUBMIT' where name='approval' and type='TenderResponse';

UPDATE EG_SCRIPT set SCRIPT=
'transitions={''DEFAULT'':[''save'',''submit_for_approval''],''NEW'':[''save'',''submit_for_approval'',''cancel'' ],''CREATED'':[''approve'',''reject''],''CANCELLED'':[''''],''REJECTED'':[''submit_for_approval'',''cancel''],''APPROVED'':[''''],''RESUBMITTED'':[''approve'',''reject''],''CHECKED'':[''approve'',''reject'']}  
state=''DEFAULT''  
if wfItem.getCurrentState():  
    state=wfItem.getCurrentState().getValue()  
result=[]  
if state in transitions: result = transitions[state]'
WHERE NAME='TenderResponse.workflow.validactions';
#DOWN
INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,SCRIPT) VALUES(EG_SCRIPT_SEQ.NEXTVAL,'TenderResponse.workflow.validactions','python',
'transitions={''DEFAULT'':[''save'',''submit_for_approval''],''NEW'':[''save'',''submit_for_approval'',''cancel'' ],''REJECTED'':[''save'',''submit_for_approval'',''cancel''],''APPROVAL_PENDING'':[''reject'',''approval''],''APPROVED'':['''']}
state=''DEFAULT''
if wfItem.getCurrentState():
    state=wfItem.getCurrentState().getValue()
result=[]
if state in transitions: result = transitions[state]
'
);
update  EG_WF_ACTIONS set NAME='approval', DESCRIPTION='APPROVED' where name='approve' and type='TenderResponse';
