#UP
update EG_SCRIPT set script='transitions={''ASSISTANTADMIN'':[''aa_approve'',''aa_reject''],''SECTION MANAGERADMIN'':[''am_approve'',''am_reject''],''ANYONE'':[''hod_approve'',''hod_reject''],''INVALID'':[''invalid'']} 
employee = eisManagerBean.getEmpForUserId(userId)  
assignment  = eisManagerBean.getAssignmentByEmpAndDate(date,employee.getIdPersonalInformation())    
state=''''
if(assignment.functionary != None and assignment.desigId != None):
	state=assignment.desigId.designationName + assignment.functionary.name
	state=state.upper()  
if(state !=''ASSISTANTADMIN'' and purpose==''authentication''):
    state=''INVALID''
elif(state ==''ASSISTANTADMIN''):
    state=''ASSISTANTADMIN''
elif(state ==''SECTION MANAGERADMIN''):
    state=''SECTION MANAGERADMIN''
else:
    state=''ANYONE''
if state in transitions:result=transitions[state]'
where name='cbill.validation';


Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'EgBillregister','hod_approve','Approve',null,null,null,null);

Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) 
values (EG_WF_ACTIONS_SEQ.NEXTVAL,'EgBillregister','hod_reject','Reject',null,null,null,null);

#DOWN