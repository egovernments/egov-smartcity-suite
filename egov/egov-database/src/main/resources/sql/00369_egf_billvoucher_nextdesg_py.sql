INSERT INTO eg_script(id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate,version) VALUES (nextval('seq_eg_script'), 'billvoucher.nextDesg', 'python', null, null, null, null, 'result=['' '','' '' ]  
employee = eisCommonService.getEmployeeByUserId(userId)  
assignment  = eisCommonService.getLatestAssignmentForEmployeeByToDate(DATE,employee.getIdPersonalInformation())  
tempassignment  = eisCommonService.getEmpTempAssignment(str(employee.getEmployeeCode()),DATE,0)
state=assignment.functionary.name + "-" + assignment.desigId.designationName  
state=state.upper()  
if ((state == ''TREASURY-ASSISTANT'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'' or type==''Pension'' or type==''default'')):  
	result[0]="UAC-ASSISTANT"  
elif ((state == ''UAC-ASSISTANT'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'' or type==''Pension'' or type==''default'')):  
	result[0]="UAC-SECTION MANAGER"  
elif ((state == ''UAC-SECTION MANAGER'' or state == ''ZONE-SECTION MANAGER'' ) and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary''  or type==''Pension'' or type==''default'')):  
	result[0]="UAC-ACCOUNTS OFFICER"  
	result[1]="UAC-ASSISTANT"
elif ((state == ''COMPILATION-SECTION MANAGER'') and  (type==''default'')):  
	result[0]="COMPILATION-ACCOUNTS OFFICER"
elif ((state == ''UAC-ACCOUNTS OFFICER'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'' or type==''Pension''  or type==''default'')):  
	result[0]="UAC-ASSISTANT"  
	result[1]="END"
elif ((state == ''COMPILATION-ACCOUNTS OFFICER'') and  (type==''default'')):  
	result[0]="END"
if result[0] =='' '':
	print "--------------------------------------Inside tempassignment for loop -----"
	for item in tempassignment:
		state=item.getFunctionary().getName().upper() + "-" + item.getDesigId().getDesignationName().upper()
		if ((state == ''TREASURY-ASSISTANT'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'' or type==''Pension'' or type==''default'')): 
  			result[0]="UAC-ASSISTANT" 
  		elif ((state == ''UAC-ASSISTANT'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'' or type==''Pension'' or type==''default'')): 
  			result[0]="UAC-SECTION MANAGER" 
  		elif ((state == ''UAC-SECTION MANAGER'' or state == ''ZONE-SECTION MANAGER'' ) and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary''  or type==''Pension'' or type==''default'')):
  			result[0]="UAC-ACCOUNTS OFFICER"
  			result[1]="UAC-ASSISTANT"
  		elif ((state == ''COMPILATION-SECTION MANAGER'') and  (type==''default'')):
  			result[0]="COMPILATION-ACCOUNTS OFFICER"
  		elif ((state == ''UAC-ACCOUNTS OFFICER'') and  (type == ''Purchase'' or type == ''Expense'' or type == ''Works'' or type == ''Salary'' or type==''Pension''  or type==''default'')):
  			result[0]="UAC-ASSISTANT" 
  			result[1]="END"
  		elif ((state == ''COMPILATION-ACCOUNTS OFFICER'') and  (type==''default'')):
  			result[0]="END"
else:
	print "------------------------------------------no teporary assignment"
print ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+result[0]', '1900-01-01 00:00:00', '2100-01-01 00:00:00',0);

