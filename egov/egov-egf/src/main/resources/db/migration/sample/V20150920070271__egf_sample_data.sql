-----------------START--------------------
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (26, 'Rent, Rates And Taxes', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (27, 'Electricity Charges', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (28, 'Water Charges', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (29, 'Security Expenses', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (30, 'Communication Expenses', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (31, 'Books And Periodicals', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (32, 'Printing And Stationery', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (33, 'Postage And Telegrams', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (34, 'Traveling And Conveyance', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (35, 'Insurance', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (36, 'Audit Fees', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (37, 'Legal Expenses', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (38, 'Professional And Other Fees', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (39, 'E-Governance', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (40, 'Advertisement And publicity', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (41, 'Hospitality Expenses', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (42, 'Membership And Subscription', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (43, 'Others', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (44, 'Power And Fuel', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (45, 'Education Expenses', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (46, 'Garden Expenses', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (47, 'Privatization', 'Expense');
------------------END---------------------

----------------------------START------------------------------------
INSERT INTO eg_script (id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (3, 'billvoucher.nextDesg', 'python', NULL, NULL, NULL, NULL, 'result=['' '','' '' ]  
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
print ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+result[0]', '1900-01-01 00:00:00', '2100-01-01 00:00:00', 0);
INSERT INTO eg_script (id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (4, 'voucherheader.vouchernumber', 'python', NULL, NULL, NULL, NULL, 'from org.egov.infstr import ValidationError
from java.lang import Long
from java.lang import Integer
from java.lang import String
from java.lang import Exception
from org.hibernate.exception import SQLGrammarException
transNumber=''''
egnum_voucherType=fundIdentity+''/''+voucherType
def getVoucherNum():  
	if (vNumGenMode == ''Auto''):
		try:
			financialYear = commonsService.getFinancialYearByDate(date)
		except Exception,e:
			return (None,[ValidationError(''Financial Year is not active for posting.'',''Financial Year is not active for posting.'')])
		year=financialYear.getFinYearRange()
		try:
			transNumber = sequenceNumberGenerator.getNextSequence(sequenceName)
        	except SQLGrammarException,e:
			transNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName)
		print transNumber
		print len(str(transNumber))
		for num in range(len(str(transNumber)), 8):
			transNumber="0"+str(transNumber)
		print ''after loop''
		print transNumber
		result=egnum_voucherType+''/''+str(transNumber)+''/''+month+''/''+year
		return (result,None)
	else:
		result=egnum_voucherType+voucherNumber
		return (result,None)
result,validationErrors=getVoucherNum()', '1900-01-01 00:00:00', '2100-01-01 00:00:00', 0);

UPDATE eg_script SET script ='from org.egov.infra.validation.exception import ValidationError
from java.lang import Long
from java.lang import Integer
from java.lang import String
from java.lang import Exception
from org.hibernate.exception import SQLGrammarException
transNumber=''''
egnum_voucherType=fundIdentity+''/''+voucherType
def getVoucherNum():  
	if (vNumGenMode == ''Auto''):
		try:
			financialYear = commonsService.getFinancialYearByDate(date)
		except Exception,e:
			return (None,[ValidationError(''Financial Year is not active for posting.'',''Financial Year is not active for posting.'')])
		year=financialYear.getFinYearRange()
		try:
			transNumber = sequenceNumberGenerator.getNextSequence(sequenceName)
        	except SQLGrammarException,e:
			transNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName)
		print transNumber
		print len(str(transNumber))
		for num in range(len(str(transNumber)), 8):
			transNumber="0"+str(transNumber)
		print ''after loop''
		print transNumber
		result=egnum_voucherType+''/''+str(transNumber)+''/''+month+''/''+year
		return (result,None)
	else:
		result=egnum_voucherType+voucherNumber
		return (result,None)
result,validationErrors=getVoucherNum()' where name = 'voucherheader.vouchernumber';


INSERT INTO eg_script (id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (5, 'autobillnumber', 'python', NULL, NULL, NULL, NULL, 'financialYear = commonsService.getFinancialYearByDate(bill.getBilldate())
year=financialYear.getFinYearRange()
result=bill.getEgBillregistermis().getEgDepartment().getCode()+"/"+"MN"+"/"+sequenceGenerator.getNextNumber("MN",1).getFormattedNumber().zfill(4)+"/"+year', '1900-01-01 00:00:00', '2100-01-01 00:00:00', 0);

INSERT INTO eg_script (id, name, type, createdby, createddate, lastmodifiedby, lastmodifieddate, script, startdate, enddate, version) VALUES (7, 'egf.bill.budgetcheck', 'python', NULL, NULL, NULL, NULL, 'from  org.egov.infstr import ValidationException
from org.egov.infstr import ValidationError
try:
	result=voucherService.budgetaryCheck(bill)
except ValidationException,e:
    validationErrors=e.getErrors()
    result=None', '1900-01-01 00:00:00', '2100-01-01 00:00:00', 0);
    
UPDATE eg_script SET script ='from  org.egov.infra.validation.exception import ValidationException
from org.egov.infra.validation.exception import ValidationError
try:
	result=voucherService.budgetaryCheck(bill)
except ValidationException,e:
    validationErrors=e.getErrors()
    result=None' where name = 'egf.bill.budgetcheck';   
    
-------------------------------END---------------------------    
    
Insert into eg_roleaction  values((select id from eg_role where name='Super User'),(select id from eg_action where name='createScheme'));
Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ExpenseBillCreate'));

--------------------------------------------------START-----------------------

insert into egf_budgetgroup (id,maxcode,mincode,name,description,budgetingtype,accounttype,isactive,updatedtimestamp)values 
(nextval('seq_egf_budgetgroup'),(select id from chartofaccounts where glcode='4120043'),(select id from chartofaccounts where glcode='4120043'),
'4120043-'||(select name from chartofaccounts where glcode='4120043'),'4120043-'||(select name from chartofaccounts where glcode='4120043'),
'DEBIT','REVENUE_EXPENDITURE',true,current_date);

insert into eg_wf_states(id,type,value,createdby,lastmodifiedby,createddate,lastmodifieddate,owner_pos)values(
nextval('seq_eg_wf_states'),'Budget','END',1,1,current_date,current_date,null);

insert into egf_budget(id,name,description,financialyearid,state_id,parent,isactivebudget,updatedtimestamp,isprimarybudget,createdby,lastmodifiedby,isbere,materializedpath)
values(nextval('seq_egf_budgetgroup'),'AP-BUDGET-2015-16','BE Budget 2015-16',(select id from financialyear where financialyear='2015-16'),
(select min(id) from eg_wf_states where type='Budget'),null,1,current_date,1,1,1,'BE','1');

insert into egf_budget(id,name,description,financialyearid,state_id,parent,isactivebudget,updatedtimestamp,isprimarybudget,createdby,lastmodifiedby,isbere,materializedpath)
values(nextval('seq_egf_budgetgroup'),'Works-BUDGET-2015-16','BE Works Budget 2015-16',(select id from financialyear where financialyear='2015-16'),
(select min(id) from eg_wf_states where type='Budget'),(select id from egf_budget where name='AP-BUDGET-2015-16'),1,current_date,1,1,1,'BE','1.1');

insert into eg_wf_states(id,type,value,createdby,lastmodifiedby,createddate,lastmodifieddate,owner_pos)values(
nextval('seq_eg_wf_states'),'BudgetDetail','END',1,1,current_date,current_date,null);

insert into egf_budgetdetail (id,executing_department,function,budget,budgetgroup,originalamount,approvedamount,budgetavailable,
modifieddate,createddate,createdby,state_id,fund,materializedpath,planningpercent) values 
(nextval('seq_egf_budgetdetail'),(select id from eg_department where code='W'),(select id from function where code='202102'),
(select id from egf_budget where name='Works-BUDGET-2015-16'),(select id from egf_budgetgroup where name='4120043-General-Road Widening'),
1000000,1000000,2000000,current_date,current_date,1,(select min(id) from eg_wf_states where type='BudgetDetail'),(select id from fund where code='02'),'1.1.1',200);

------------------------------------END ---------------------------------
