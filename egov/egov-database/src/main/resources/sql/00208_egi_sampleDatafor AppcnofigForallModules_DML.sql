
-- EGI

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION,createdby,lastmodifiedby,version, MODULE  ) 
VALUES (nextval('seq_eg_appconfig'), 'serviceProviderUrl', 'Service Provider Url for sending sms',
2,2,0, (select id from eg_module where name='Administration'));

INSERT INTO EG_APPCONFIG ( ID, KEY_NAME, DESCRIPTION,createddate,lastmodifieddate,createdby,lastmodifiedby,version, MODULE ) 
VALUES (nextval('seq_eg_appconfig'), 'smsSender', 'Sms sender phone number or name',null,null,null,null,0, (select id from eg_module where name='Administration')); 


--rollback DELETE FROM EG_APPCONFIG WHERE KEY_NAME='smsSender';
--rollback DELETE FROM EG_APPCONFIG WHERE KEY_NAME='serviceProviderUrl';


--PGR

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( 
nextval('SEQ_EG_APPCONFIG'), 'SENDEMAILFORESCALATION', 'Send email for for escalation', (select id from eg_module where name='PGR')); 



--rollback DELETE FROM eg_appconfig WHERE key_name='SENDEMAILFORESCALATION';



--egf

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'exclude_status_forbudget_actual','exclude voucher status for budget',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budgetaryCheck_budgettype_cashbased','budgetaryCheck budgettype',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budget_final_approval_status','Final Approval status for budget',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'pjv_saveasworkingcopy_enabled','pjv_saveasworkingcopy_enabled',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Bill_Number_Geneartion_Auto','bill number genration auto or manual ',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'cancelledstatus','Cancelled status for voucher',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'contingencyBillPurposeIds',
'Contingent Bill PurposeIds',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'VOUCHER_STATUS_TO_CHECK_BANK_BALANCE','VOUCHER_STATUS_TO_CHECK_BANK_BALANCE',
(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'salaryBillPurposeIds','Salary Bill PurposeIds',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'salaryBillDefaultPurposeId','Salary Bill Default PurposeIds',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-Revenue','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-110','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-120','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-130','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-140','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-150','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-160','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-170','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-171','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-180','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-Expense','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-210','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-220','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-230','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-240','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-250','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-260','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-270','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-271','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-280','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-290','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-Revenue','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-Expense','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-110','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-120','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-130','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-140','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-150','Department Wise Budget Report',(select id from eg_module where name='EGF'));


Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-160','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-170','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-171','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-180','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-210','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-220','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-230','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-240','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-250','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-260','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-270','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-280','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'departmentWiseBudgetReport-290','Department Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'bank_balance_mandatory','bank_balance_mandatory',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'PurchaseBillApprovalStatus','Purchase bill Status for approval',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'WorksBillApprovalStatus','Works bill Status for approval',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'ExpenseBillApprovalStatus','Contingency bill Status for approval',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'SalaryBillApprovalStatus','Salary bill Status for approval',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Balance Check Based on Fund Flow Report','Check Balance  Based on Fund Flow Report Or Not',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'add_less_codes_for_ie_report','add and less codes for ie report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'planning_budget_multiplication_factor','planning budget multiplication factor',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'CONSIDER_RE_REAPPROPRIATION_AS_SEPARATE','Whether or not to consider re_reappropriation as seperate in budget reports',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'coa_major_capital_exp_fie_report','coa_major_capital_exp_fie_report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'pensionBillPurposeIds','Pension Bill PurposeIds',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'PensionBillApprovalStatus','Pension bill Status for approval',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budget_SMlevel_approver_designation','budget_SMlevel_approver_designation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'USE BILLDATE IN CREATE VOUCHER FROM BILL','USE BILLDATE IN CREATE VOUCHER FROM BILL',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'WORKS VOUCHERS RESTRICTION DATE FROM JV SCREEN','WORKS VOUCHERS RESTRICTION DATE FROM JV SCREEN',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'GJV_FOR_RCPT_CHQ_DISHON','Generate Journal Voucher on dishonoring receipt cheque',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'loangrant.default.fundid','if fund id is present it is defaulted else selection made mandatory',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'BANKBALANCE_CHECK_DATE','Fund Flow BANKBALANCE_CHECK_DATE',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'functionWiseBudgetReport-272','Function Wise Budget Report',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'CONTRACTOR_ADVANCE_CODE','Contractor Advance Payable',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'contingencyBillDefaultPurposeId',' default Purpose ids for Contingency',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Communication Expenses','ContingentBill-Telephone-CheckList',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Electricity Charges','ContingentBill-Electricity-CheckList',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Others','ContingentBill-Others-CheckList',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK','PAYMENT_WF_STATUS_FOR_BANK_BALANCE_CHECK',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'DEFAULT_SEARCH_MISATTRRIBUTES','default search mis attributes',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budget_toplevel_approver_designation','budget_toplevel_approver_designation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budget_secondlevel_approver_designation','Functionary contains colon',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Reason For Cheque Surrendaring',' Reasons for cheque surrendering',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'PREAPPROVEDVOUCHERSTATUS','voucher status before approval',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'APPROVEDVOUCHERSTATUS','next state of approval',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'DEFAULTVOUCHERCREATIONSTATUS','DEFAULTVOUCHERCREATIONSTATUS',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'coa_detailcode_length','detailcode length length',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budgetgroup_range_minor_or_detailed','budgetgroup_range_minor_or_detailed',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'coa_minorcode_length','minorcode length',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'coa_majorcode_length','majorcode length',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budgetaryCheck_groupby_values','budgetarycheck group by values',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'coa_subminorcode_length','subminorcode length',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'purchaseBillPurposeIds','purchaseBillPurposeIds',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'worksBillPurposeIds','worksBillPurposeIds',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'parent_for_detailcode','parent length for detailed code',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Cheque_no_generation_auto','Cheque_nogeneration',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'cheque.assignment.infavourof','cheque.assignment.infavourof',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'boundaryforaccounts','This is the boundary at which books of accounts are maintained',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'DEFAULTTXNMISATTRRIBUTES','default transaction mis attributes',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'JournalVoucher_ConfirmonCreate','journal voucher create isconfirmed value',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'bs_report_half_yearly','balance sheet report half yearly date (dd/mm)',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'REPORT_SEARCH_MISATTRRIBUTES','default search mis attributes',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budgetDetail_mandatory_fields','Budget Detail mandatroy fields',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budgetCheckRequired','budgetCheckRequired',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'EB Voucher Property-Fund','Fund Code Used For EB Bill Voucher Creation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'EB Voucher Property-Function','Function Code Used For EB Bill Voucher Creation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'EB Voucher Property-Department','Department Code Used For EB Bill Voucher Creation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'EB Voucher Property-Bank','Bank Code Used For EB Bill Voucher Creation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'EB Voucher Property-BankBranch','BankBranch Number Used For EB Bill Voucher Creation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'EB Voucher Property-BankAccount','BankAccount Number Used For EB Bill Voucher Creation',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'RTGSNO_GENERATION_AUTO','RTGSNO_GENERATION_AUTO',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'DATE_RESTRICTION_FOR_CJV_PAYMENT_MODE_AS_RTGS','Whether RTGS should be the only mode of payment for CJVs for the bill date',(select id from eg_module where name='EGF'));
Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Remove Entrys With Zero Amount','Value to deside Remove Entrys With Zero Amount in Reports',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'ifRestrictedToOneFunctionCenter','ifRestrictedToOneFunctionCenter',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'VOUCHERDATE_FROM_UI','GET VOUCHERDATE FROM UI',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'CJV_PAYMENT_MODE_AS_RTGS','Whether RTGS should be the only mode of payment for CJVs',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'TNEB Bill Pay to','Name of the bank to which bill will be paid',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'AutoRemittance_Start_Date','Starting date of date to be considered for  RTGS Payment',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'AuoRemittance_Account_Number_For_Receipts',
'Bank Account number Receipt remittance Payment',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'AuoRemittance_Account_Number_For_GJV',
'Bank Account number GJV remittance Payment',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'Remove Entries With Zero Amount in Report','Value to deside Remove Entries With Zero Amount in Reports',(select id from eg_module where name='EGF'));

--rollback delete from eg_appconfig where module=(select id from eg_module where name='EGF');

--Asstes

INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'IS_ASSET_CATEGORYCODE_AUTOGENERATED', 'Auto/User Generated Asset Category Code ', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'ASSET_ACCOUNT_CODE_PURPOSEID', 'Asset Account Code Purpose ID', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'REVALUATION_RESERVE_ACCOUNT_PURPOSEID', 'Revaluation Reserve Account Purpose ID', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'DEPRECIATION_EXPENSE_ACCOUNT_PURPOSEID', 'Depreciation Expense Account Purpose ID', (select id from eg_module where name='Asset Management'));
INSERT INTO eg_appconfig(id, key_name, description, module) VALUES (nextval('seq_eg_appconfig'), 'ACCUMULATED_DEPRECIATION_PURPOSEID', 'Accumulated Depreciation Purpose ID', (select id from eg_module where name='Asset Management'));

--rollback delete from eg_appconfig where module=(select id from eg_module where name='Asset Management');

--BPA

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'BPA_DEPARTMENT_CODE','BPA Department code ForVoucher',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'BPA_FUND_CODE','BPA Fund Code',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'BPA_FUNCTION_CODE','BPA Function code',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'SMS_NOTIFICATION_ALLOWED_BPA','Sending SMS and eMail Register BPA Applicant',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'EMAIL_NOTIFICATION_ALLOWED_BPA','Sending SMS and eMail Register BPA Applicant',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'DueCheckForProperty','Check dues for property before registration',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'OFFICIALS_DESIGLIST_SMS_BPA','Designations of the officials to whom SMS should be sent',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'IS_SATURDAY_HOLIDAY','Saturday is considered as holiday or no',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'IS_SECONDSATURDAY_HOLIDAY','Second Saturday is considered as holiday or no',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'IS_SUNDAY_HOLIDAY','Sunday is considered as holiday or no',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'HOLIDAYLIST_INCLUDED','Other Govt holidays in Holiday List are considered or no',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'AUTO_GENERATION_INSPCTIONDATES','Site Inspection dates are auto Generated',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'ROLELIST_BPA','List of roles for bpa',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'DocUPloadForSurveyor','List Of Servicetype code',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'BPA_FILE_UPLOAD_LOCATION','File upload location for bpa module',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'AUTODCR_FETCH_TYPE','Auto DCR Details Fetching type',(select id from eg_module where name='BPA'));
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'INSPECTION_DWELLUNIT_DATE','Show DwellingUnit in Inspection based on date',(select id from eg_module where name='BPA'));

--rollback delete from eg_appconfig where module=(select id from eg_module where name='BPA');

