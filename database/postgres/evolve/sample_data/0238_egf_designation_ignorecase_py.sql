#UP

update eg_script set script='
transitions={''true'':[''true''],''false'':[''false'']}  
state=''false''     
val=persistenceService.getFunctionaryAndDesignation()
if(val):
    val=val.upper() 
departmentdefaultAndDisabled=''false''  
if((val==''FMU-ASSISTANT'' or val==''FMU-SECTION MANAGER'') and purpose==''balancecheck''):    
    state=''true''   
if(val==''UAC-ASSISTANT'' and purpose==''createpayment''):    
    state=''true''   
if((val==''UAC-ASSISTANT'')  and purpose==''chequeassignment''):    
    state=''true''
if(purpose==''deptcheck''):  
    state= departmentdefaultAndDisabled  
if state in transitions:result=transitions[state]  ' where name='Paymentheader.show.bankbalance';



update eg_script set script='
result=['' '','' '' ]  
employee = eisManagerBean.getEmpForUserId(userId)  
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())    
state=assignment.functionary.name + "-" + assignment.desigId.designationName
if(state):
    state=state.upper()  
mode = type.split(''|'')[0]  
paymentAmount = type.split(''|'')[1]  
if ((state == ''UAC-ASSISTANT'') and  (mode== ''Payment'') ):  
	result[0]="FMU-ASSISTANT"  
if ((state == ''FMU-ASSISTANT'') and  (mode== ''Payment'')):  
	result[0]="FMU-ACCOUNTS OFFICER"  
	result[1]="UAC-ASSISTANT"  
if ((state == ''FMU-ACCOUNTS OFFICER'') and  (mode== ''Payment'')):  
	result[0]="UAC-ASSISTANT"  
	result[1]="END"' where name='paymentHeader.nextDesg';


update EG_SCRIPT set script='transitions={''ASSISTANTADMIN'':[''aa_approve'',''aa_reject''],''SECTION MANAGERADMIN'':[''am_approve'',''am_reject''],''INVALID'':[''invalid'']} 
employee = eisManagerBean.getEmpForUserId(userId)  
assignment  = eisManagerBean.getAssignmentByEmpAndDate(date,employee.getIdPersonalInformation())    
state=assignment.desigId.designationName + assignment.functionary.name
if(state):
    state=state.upper()
if(state !=''ASSISTANTADMIN'' and purpose==''authentication''):
    state=''INVALID''
if state in transitions:result=transitions[state]'
where name='cbill.validation';


update EG_SCRIPT set script='result=['' '','' '' ]
employee = eisManagerBean.getEmpForUserId(userId)
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation()) 
state=''''
if(assignment.functionary != None and assignment.desigId != None):
	state=assignment.functionary.name + "-" + assignment.desigId.designationName
if(state):
    state=state.upper()
if (state == ''ADMIN-ASSISTANT''):
	result[0]="ADMIN-SECTION MANAGER"
elif (state == ''ADMIN-SECTION MANAGER''):
	result[0]="ANYFUNCTIONARY-ANYDESG"
else:
     result[0]="END"'
where name='cbill.nextUser';

#DOWN