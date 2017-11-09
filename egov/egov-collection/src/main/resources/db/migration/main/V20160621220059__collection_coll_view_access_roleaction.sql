INSERT into eg_role values(nextval('seq_eg_role'),'Coll_View Access','This role has view access to all transactions , all reports and all master screens',current_date,1,1,current_date,0); 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --Search Receipts -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='SearchReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ViewReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='PrintReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='SearchReceiptReset'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='SearchReceiptSearch'));
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --Search Challans -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'SearchChallanReset'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'ViewChallan'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'SearchChallanSearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'SearchChallan'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'PrintChallan'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'AjaxServiceByCategory'));
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --Search Online Receipts -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='SearchOnlineReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='SearchOnlineReceiptReset'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='SearchOnlineReceiptsSearch'));
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Service Details -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ServiceDetails'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ServiceDetailsList'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsView'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Service Category -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ServiceCategory'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ServiceCategoryList'));
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Service Type To Bank Account Mapping -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ServiceTypeToBankAccountMappingList'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ServiceTypeToBankAccountMappingSearch'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='bankBranchsByBankForReceiptPayments'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='bankAccountByBankBranch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'AjaxServiceByCategory'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Cash Summary -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CashCollectionSubmissionReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CashCollectionReportResult'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CashCollectionReport'));


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Collection Summary -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CollectionSummaryReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CollectionSummaryReportResult'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Remittance voucher report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Coll_View Access'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReport'));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Coll_View Access'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceVoucherReportResult'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='AjaxAccountListOfService'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ServiceListOfAccount'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Remittance voucher report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ReceiptRegisterReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ReceiptRegisterReportResult'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Cash Collection Report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CashCollectionSubmissionReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CashCollectionReportResult'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Cheque Collection Report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ChequeCollectionSubmissionReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ChequeCollectionReportResult'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='ChequeCollectionReport'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Remitance Statement Report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Coll_View Access'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReport'));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Coll_View Access'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittanceStatementReportResult'));

INSERT INTO EG_ROLEACTION (ROLEID,ACTIONID) VALUES ((SELECT ID FROM eg_role WHERE name='Coll_View Access'),(SELECT ID FROM EG_ACTION WHERE NAME='RemittancePrintBankChallanReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='AjaxCollectionsBankAccount'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='AjaxCollectionsBankBranch'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --



-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- Online Transaction Report -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='OnlineTransactionReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='OnlineTransactionReportResult'));

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- END -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
