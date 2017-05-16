INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Bank Collection Operator', 'Bank Collection Operator', now(), 1, 1, now(), 0);

----------------Search Receeipts -----------------------

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='SearchReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='ViewReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='PrintReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='SearchReceiptReset'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='SearchReceiptSearch'));

-- -- -- -- -- -- -- -- -- -- -- Collection Summary -- -- -- -- -- -- -- --
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='CollectionSummaryReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='CollectionSummaryReportResult'));

---------------------Receipt register report------------------------
INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='ReceiptRegisterReport'));

INSERT into  eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Bank Collection Operator'),(select id from eg_action where name='ReceiptRegisterReportResult'));

------------------------------PT Collection--------------------------------
insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE name = 'Bank Collection Operator'), id 
from eg_action where name in ('Property Tax Collection','Assessment-commonSearch', 'Search Property', 'Search Property By Assessment', 'View Property',
'Search Property By Mobile No', 'Search Property By Door No', 'Search Property By Bndry', 'Search Property By Location', 'Search Property By Demand','Search owner','Update update number','Generate Collection Bill'));

--------------------------cancel receipt---------------------
insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE name = 'Bank Collection Operator'), id 
from eg_action where name in ('CreateReceipt','CancelReceipt','SaveReceipt','SaveOnCancelReceipt','GetAllBankName','AjaxCollectionsBankBranch',
'AjaxCollectionsBankAccount'));

-------- APPCONFIG----------------
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'ROLES_CREATERECEIPT_APPROVEDSTATUS','For these Role Create Receipt with Approved status',(select id from eg_module where name='Collection'));
INSERT into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='ROLES_CREATERECEIPT_APPROVEDSTATUS'),current_date,'Bank Collection Operator');