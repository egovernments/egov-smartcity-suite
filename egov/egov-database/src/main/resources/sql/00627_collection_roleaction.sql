INSERT INTO eg_roleaction (actionid,roleid) values ((SELECT id FROM eg_action where name='Official Home Page'),(SELECT id FROM eg_role where name='Remitter'));

INSERT INTO eg_roleaction  (actionid,roleid) values((SELECT id from eg_action WHERE name='CitizenInboxForm' and contextroot='egi'),
(SELECT id FROM eg_role  WHERE name='Remitter' ));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Remitter'),(select id FROM eg_action  WHERE NAME = 'Inbox' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Remitter'),(select id FROM eg_action  WHERE NAME = 'InboxDraft' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Remitter'),(select id FROM eg_action  WHERE NAME = 'InboxHistory' and CONTEXTROOT='egi'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Remitter'),(select id FROM eg_action  WHERE NAME = 'ListReceiptWorkFlowAction' and CONTEXTROOT='collection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SaveOnCancelReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SearchOnlineReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SearchOnlineReceiptsSearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SearchReceiptSearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'CollectionIndexPage'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SubmitReceiptWorkFlowAction'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'CancelReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ViewReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'PrintReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceCategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceCategoryEdit'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceCategorySave'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceCategoryCreate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceCategoryList'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceDetails'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsList'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsBeforeCreate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'AjaxServiceCodeUniqueCheck'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'CollectionBillingStub'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SaveReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'CreateReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsSchemeList'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsCreate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'GenerateChequeReport'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ApproveReceiptCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'RejectReceiptCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ListDetailAction'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SubmitReceiptCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'SearchReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ajaxValidateReceiptRemit'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ViewReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'GenerateCashReport'));

