#UP
update eg_script set script='
result=['' '','' '' ]
employee = eisManagerBean.getEmpForUserId(userId)
assignment  = eisManagerBean.getAssignmentByEmpAndDate(DATE,employee.getIdPersonalInformation())  
state=assignment.functionary.name + "-" + assignment.desigId.designationName
if ((state == ''UAC-ASSISTANT'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'')):
	result[0]="UAC-SECTION MANAGER"
if ((state == ''UAC-SECTION MANAGER'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'')):
	result[0]="UAC-ACCOUNTS OFFICER"
if ((state == ''UAC-ACCOUNTS OFFICER'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'')):
	result[1]="END"'
where name='billvoucher.nextDesg';

#DOWN
