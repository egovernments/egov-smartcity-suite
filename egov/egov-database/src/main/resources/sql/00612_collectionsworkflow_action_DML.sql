update eg_wf_types set link='/collection/receipts/collectionsWorkflow-listWorkflow.action?inboxItemDetails=:ID' where type='ReceiptHeader';


Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'ReceiptHeader','Create Receipt','Create Receipt',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'ReceiptHeader','Submit for Approval','Submit for Approval',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'ReceiptHeader','Approve','Approve',1,now(),1,now());
Insert into EG_WF_ACTIONS (ID,TYPE,NAME,DESCRIPTION,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE) values (nextval('EG_WF_ACTIONS_SEQ'),'ReceiptHeader','Reject','Reject',1,now(),1,now());


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SaveOnCancelReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SearchOnlineReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SearchOnlineReceiptsSearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SearchReceiptSearch'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CollectionIndexPage'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SubmitReceiptWorkFlowAction'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CancelReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ViewReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'PrintReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceCategory'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceCategoryEdit'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceCategorySave'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceCategoryCreate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceCategoryList'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceDetails'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsList'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsBeforeCreate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxServiceCodeUniqueCheck'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CollectionBillingStub'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SaveReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CreateReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsSchemeList'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ServiceDetailsCreate'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'GenerateChequeReport'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ApproveReceiptCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'RejectReceiptCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ListDetailAction'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SubmitReceiptCollection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'SearchReceipts'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ajaxValidateReceiptRemit'));



Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'ViewReceipt','/citizen/onlineReceipt-viewReceipt.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'View Receipt','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'ViewReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ViewReceipt'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'ViewReceipt'));




Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'GenerateCashReport','/receipts/collectionsWorkflow-submissionReportCash.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'Generate Cash Report','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'GenerateCashReport'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'GenerateCashReport'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'GenerateCashReport'));







