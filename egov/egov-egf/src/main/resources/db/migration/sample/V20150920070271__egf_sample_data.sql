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
    
---------------------------------START-----------------------
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='PREAPPROVEDVOUCHERSTATUS'),to_date('23-09-09','DD-MM-RR'),'5');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='APPROVEDVOUCHERSTATUS'),to_date('23-09-09','DD-MM-RR'),'0');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULTVOUCHERCREATIONSTATUS'),to_date('23-09-09','DD-MM-RR'),'0');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='exclude_status_forbudget_actual'),to_date('23-09-09','DD-MM-RR'),'4');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetaryCheck_budgettype_cashbased'),to_date('23-09-09','DD-MM-RR'),'N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budget_final_approval_status'),to_date('23-09-09','DD-MM-RR'),'END');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='coa_majorcode_length'),to_date('23-09-09','DD-MM-RR'),'3');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='coa_minorcode_length'),to_date('23-09-09','DD-MM-RR'),'5');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetaryCheck_groupby_values'),to_date('23-09-09','DD-MM-RR'),'department,function,fund');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetaryCheck_groupby_values'),to_date('24-03-10','DD-MM-RR'),'department,function,fund');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='coa_detailcode_length'),to_date('23-09-09','DD-MM-RR'),'9');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetgroup_range_minor_or_detailed'),to_date('23-09-09','DD-MM-RR'),'detailed');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='pjv_saveasworkingcopy_enabled'),to_date('23-09-09','DD-MM-RR'),'N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULTTXNMISATTRRIBUTES'),to_date('28-03-10','DD-MM-RR'),'department|M');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULTTXNMISATTRRIBUTES'),to_date('28-03-10','DD-MM-RR'),'function|N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULTTXNMISATTRRIBUTES'),to_date('28-03-10','DD-MM-RR'),'fundsource|N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULTTXNMISATTRRIBUTES'),to_date('28-03-10','DD-MM-RR'),'fund|M');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULTTXNMISATTRRIBUTES'),to_date('13-08-10','DD-MM-RR'),'scheme|N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULTTXNMISATTRRIBUTES'),to_date('13-08-10','DD-MM-RR'),'subscheme|N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='JournalVoucher_ConfirmonCreate'),to_date('28-03-10','DD-MM-RR'),'0');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='bs_report_half_yearly'),to_date('22-11-09','DD-MM-RR'),'30/09');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='REPORT_SEARCH_MISATTRRIBUTES'),to_date('07-10-09','DD-MM-RR'),'department|N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='REPORT_SEARCH_MISATTRRIBUTES'),to_date('07-10-09','DD-MM-RR'),'fund|M');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='REPORT_SEARCH_MISATTRRIBUTES'),to_date('22-10-09','DD-MM-RR'),'function|N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetDetail_mandatory_fields'),to_date('28-03-10','DD-MM-RR'),'executingDepartment,function,fund');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetCheckRequired'),to_date('11-11-09','DD-MM-RR'),'Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='coa_subminorcode_length'),to_date('28-03-10','DD-MM-RR'),'7');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='purchaseBillPurposeIds'),to_date('28-03-10','DD-MM-RR'),'27');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='worksBillPurposeIds'),to_date('28-03-10','DD-MM-RR'),'26');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='parent_for_detailcode'),to_date('28-03-10','DD-MM-RR'),'3');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Cheque_no_generation_auto'),to_date('17-12-09','DD-MM-RR'),'N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='cheque.assignment.infavourof'),to_date('17-12-09','DD-MM-RR'),'Chairperson');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='boundaryforaccounts'),to_date('17-12-09','DD-MM-RR'),'City');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='boundaryforaccounts'),to_date('01-01-10','DD-MM-RR'),'City');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='cancelledstatus'),to_date('17-12-09','DD-MM-RR'),'4');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='bank_balance_mandatory'),to_date('28-03-10','DD-MM-RR'),'Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='CONTRACTOR_ADVANCE_CODE'),to_date('22-02-10','DD-MM-RR'),'4604002');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='PurchaseBillApprovalStatus'),to_date('01-04-09','DD-MM-RR'),'SBILL|Approved');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='WorksBillApprovalStatus'),to_date('01-04-09','DD-MM-RR'),'CONTRACTORBILL|APPROVED');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='ExpenseBillApprovalStatus'),to_date('01-04-09','DD-MM-RR'),'CBILL|APPROVED');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='ExpenseBillApprovalStatus'),to_date('01-04-09','DD-MM-RR'),'EXPENSEBILL|Approved');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='SalaryBillApprovalStatus'),to_date('01-04-09','DD-MM-RR'),'SALBILL|Approved');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillPurposeIds'),to_date('03-03-10','DD-MM-RR'),'28');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillPurposeIds'),to_date('01-04-10','DD-MM-RR'),'26');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillPurposeIds'),to_date('18-06-10','DD-MM-RR'),'31');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillPurposeIds'),to_date('18-06-10','DD-MM-RR'),'34');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillPurposeIds'),to_date('22-06-10','DD-MM-RR'),'36');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillPurposeIds'),to_date('28-02-11','DD-MM-RR'),'27');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillPurposeIds'),to_date('10-07-12','DD-MM-RR'),'103');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='contingencyBillDefaultPurposeId'),to_date('28-03-10','DD-MM-RR'),'0');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'1. Whether the monthly rental charges claimed is correct.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'10. Whether the original bill is enclosed.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'2. Whether the call charges claimed is correct.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'3. Whether the OB has been compared with CB of the previous bill.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'4. Whether the call charges does not exceed the permitted calls.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'5. Whether the sanction of the competent authority has been obtained if the permitted calls exceeds.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'6. Whether the charges for the excess calls have been remitted in the Treasury and the challan produced in respect of residential phone.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'7. Whether necessary entry has been made in the Standard Register.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'8. Whether the budget provision is available.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Communication Expenses'),to_date('28-03-10','DD-MM-RR'),'9. Whether the bill has been approved by the competent authority.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Electricity Charges'),to_date('28-03-10','DD-MM-RR'),'1. Whether the claim is in order.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Electricity Charges'),to_date('28-03-10','DD-MM-RR'),'2. Whether the OB has been compared with CB of the previous bill. ');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Electricity Charges'),to_date('28-03-10','DD-MM-RR'),'3. Whether necessary entry has been made in the Standard Register.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Electricity Charges'),to_date('28-03-10','DD-MM-RR'),'4. Whether the budget provision is available.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Electricity Charges'),to_date('28-03-10','DD-MM-RR'),'5. Whether the bill has been approved by the competent authority.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Electricity Charges'),to_date('28-03-10','DD-MM-RR'),'6. Whether an extract of the electricity meter reader is enclosed.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Others'),to_date('28-03-10','DD-MM-RR'),'1. Whether the claim is in order.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Others'),to_date('28-03-10','DD-MM-RR'),'2. Whether the budget provision is available.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Others'),to_date('28-03-10','DD-MM-RR'),'3. Whether the bill has been approved by the competent authority. ');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Others'),to_date('28-03-10','DD-MM-RR'),'4. Whether original bill has been enclosed');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK'),to_date('28-03-10','DD-MM-RR'),'Checked by FMU SECTION MANAGER');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK'),to_date('28-03-10','DD-MM-RR'),'Prepared by FMU ASSISTANT');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Bill_Number_Geneartion_Auto'),to_date('28-03-10','DD-MM-RR'),'N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULT_SEARCH_MISATTRRIBUTES'),to_date('07-10-09','DD-MM-RR'),'department|N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DEFAULT_SEARCH_MISATTRRIBUTES'),to_date('07-10-09','DD-MM-RR'),'fund|M');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='VOUCHER_STATUS_TO_CHECK_BANK_BALANCE'),to_date('01-04-09','DD-MM-RR'),'Approved by %(UAC - ACCOUNTS OFFICER');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='VOUCHER_STATUS_TO_CHECK_BANK_BALANCE'),to_date('06-06-12','DD-MM-RR'),'ASSISTANT,FMU');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='salaryBillPurposeIds'),to_date('04-04-10','DD-MM-RR'),'31,34,36,103');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='add_less_codes_for_ie_report'),to_date('18-06-10','DD-MM-RR'),'280-Add,290-Less');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='planning_budget_multiplication_factor'),to_date('03-06-10','DD-MM-RR'),'2');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budget_toplevel_approver_designation'),to_date('01-04-00','DD-MM-RR'),'Commissioner');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budget_secondlevel_approver_designation'),to_date('01-04-00','DD-MM-RR'),'FMU:');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Reason For Cheque Surrendaring'),to_date('16-09-10','DD-MM-RR'),'Cheque to be scrapped.');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Reason For Cheque Surrendaring'),to_date('16-09-10','DD-MM-RR'),'Damaged cheque');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Reason For Cheque Surrendaring'),to_date('16-09-10','DD-MM-RR'),'Surrender: cheque leaf to be re-used.|Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Balance Check Based on Fund Flow Report'),to_date('17-11-10','DD-MM-RR'),'Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='CONSIDER_RE_REAPPROPRIATION_AS_SEPARATE'),to_date('25-04-11','DD-MM-RR'),'N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='GJV_FOR_RCPT_CHQ_DISHON'),to_date('27-07-11','DD-MM-RR'),'Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='loangrant.default.fundid'),to_date('01-04-11','DD-MM-RR'),'23');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='coa_major_capital_exp_fie_report'),to_date('30-03-12','DD-MM-RR'),'410,412');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='BANKBALANCE_CHECK_DATE'),to_date('17-04-12','DD-MM-RR'),'01-Apr-2012');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='pensionBillPurposeIds'),to_date('11-09-12','DD-MM-RR'),'103');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='PensionBillApprovalStatus'),to_date('01-04-09','DD-MM-RR'),'PENSIONBILL|Approved');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budget_SMlevel_approver_designation'),to_date('01-04-00','DD-MM-RR'),'SECTION MANAGER');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='USE BILLDATE IN CREATE VOUCHER FROM BILL'),to_date('02-04-13','DD-MM-RR'),'N');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='WORKS VOUCHERS RESTRICTION DATE FROM JV SCREEN'),to_date('03-05-13','DD-MM-RR'),'Works,01/04/2013');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='ifRestrictedToOneFunctionCenter'),to_date('04-09-13','DD-MM-RR'),'No');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='CJV_PAYMENT_MODE_AS_RTGS'),to_date('01-04-09','DD-MM-RR'),'Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='DATE_RESTRICTION_FOR_CJV_PAYMENT_MODE_AS_RTGS'),to_date('01-04-09','DD-MM-RR'),'01/10/2013');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='RTGSNO_GENERATION_AUTO'),to_date('26-03-14','DD-MM-RR'),'Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='AuoRemittance_Account_Number_For_GJV'),to_date('15-04-14','DD-MM-RR'),'01-171102000000700');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='VOUCHERDATE_FROM_UI'),to_date('29-05-14','DD-MM-RR'),'Y');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Remove Entrys With Zero Amount'),to_date('20-08-14','DD-MM-RR'),'true');
Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='Remove Entries With Zero Amount in Report'),to_date('17-10-14','DD-MM-RR'),'Yes');
update eg_appconfig_values set value = 7 where KEY_ID in (select id from eg_appconfig where key_name = 'coa_detailcode_length');
update eg_appconfig_values set value = 5 where KEY_ID in (select id from eg_appconfig where key_name = 'coa_subminorcode_length');
update eg_appconfig_values set value = 2 where KEY_ID in (select id from eg_appconfig where key_name = 'parent_for_detailcode');

----------------------------------END-------------------------------------------------    
Insert into eg_roleaction  values((select id from eg_role where name='Super User'),(select id from eg_action where name='createScheme'));
Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='ExpenseBillCreate'));

