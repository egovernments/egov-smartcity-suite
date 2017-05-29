INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'BANK COLLECTION REMITTER', 'BANK COLLECTION REMITTER', now(),(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'), now(), 0);

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='AjaxAccountListOfService'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='BankRemittanceListData'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='BankRemittance'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='BankRemittanceCreate'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='RemittancePrintBankChallanReport'));

-- -- -- -- -- -- -- -- -- -- -- Bank Remittance Statement-- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='RemittanceStatementReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='RemittanceStatementReportResult'));

-- -- -- -- -- -- -- -- -- -- --Receipt register report------------------------
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='ReceiptRegisterReport'));
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='ReceiptRegisterReportResult'));

-- -- -- -- -- -- -- -- -- -- --Collection summary - Head wise report--------------------
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='CollectionSummaryHeadWiseReport'));
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='CollectionSummaryHeadWiseReportResult'));

-- -- -- -- -- -- -- -- -- -- --Search Receeipts -----------------------
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='SearchReceipts'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='ViewReceipts'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='PrintReceipts'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='SearchReceiptReset'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BANK COLLECTION REMITTER'),(select id from eg_action where name='SearchReceiptSearch'));
