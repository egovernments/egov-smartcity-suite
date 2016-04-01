Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'BankAdviceReport-search','/report/bankAdviceReport-search.action',(select id from eg_module where name='MIS Reports' ),1,'Create FinancialYear',true,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='BankAdviceReport-search'));



Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BankAdviceReportforRTGSBankPayment'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='BankAdviceReport-search'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadBankBranchFromBank'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadBankAccFromBranch'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='ajaxLoadRTGSChequeFromBankAcc'));

Insert into eg_roleaction values((select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='bankAdviceReportExportPDF'));



Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='BankAdviceReportforRTGSBankPayment'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='BankAdviceReport-search'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajaxLoadBankBranchFromBank'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajaxLoadBankAccFromBranch'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ajaxLoadRTGSChequeFromBankAcc'));

Insert into eg_roleaction values((select id from eg_role where name='Payment Creator'),(select id from eg_action where name='bankAdviceReportExportPDF'));
