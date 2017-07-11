INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'COLLECTION REPORT VIEWER', 'COLLECTION REPORT VIEWER', now(),(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'), now(), 0);

-- -- -- -- -- -- -- -- -- -- -- Bank Remittance Statement-- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='RemittanceStatementReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='RemittanceStatementReportResult'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='COLLECTION REPORT VIEWER'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallanReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='AjaxCollectionsBankAccount'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='AjaxCollectionsBankBranch'));

-- -- -- -- -- -- -- -- -- -- --Receipt register report------------------------
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='ReceiptRegisterReport'));
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='ReceiptRegisterReportResult'));

-- -- -- -- -- -- -- -- -- -- --Collection summary - Head wise report--------------------
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='CollectionSummaryHeadWiseReport'));
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='CollectionSummaryHeadWiseReportResult'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Cash Summary -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='CashCollectionReport'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Collection Summary -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='CollectionSummaryReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='CollectionSummaryReportResult'));


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Remittance voucher report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='COLLECTION REPORT VIEWER'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReport'));
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='COLLECTION REPORT VIEWER'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReportResult'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='AjaxAccountListOfService'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='ServiceListOfAccount'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Cash Collection Report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='CashCollectionSubmissionReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='CashCollectionReportResult'));


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Cheque Collection Report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='ChequeCollectionSubmissionReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='ChequeCollectionReportResult'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='ChequeCollectionReport'));


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Online Transaction Report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='OnlineTransactionReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='OnlineTransactionReportResult'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Dishonored Cheque Report-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='DishonoredChequeReport'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='COLLECTION REPORT VIEWER'),(select id from eg_action where name='DishonoredChequeCreateReport'));

