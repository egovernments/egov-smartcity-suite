#UP

update eg_script set script='result=['' '','' '' ]  
employee = eisManagerBean.getEmpForUserId(userId)  
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())    
state=assignment.functionary.name + "-" + assignment.desigId.designationName
if(state):
    state=state.upper()  
if (state == ''UAC-ASSISTANT''):  
	result[0]="FMU-ASSISTANT"  
if (state == ''FMU-ASSISTANT''):  
	result[0]="FMU-ACCOUNTS OFFICER"  
	result[1]="UAC-ASSISTANT"  
if (state == ''FMU-ACCOUNTS OFFICER''):  
	result[0]="UAC-ASSISTANT"  
	result[1]="END"' where name='paymentHeader.nextDesg';

#DOWN