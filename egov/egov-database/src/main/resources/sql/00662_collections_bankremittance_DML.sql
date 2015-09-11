
Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'BankRemittance','/receipts/bankRemittance-list.action',null,(select ID from eg_module where NAME ='Collection Transaction'),1,'Bank Remittance','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'BankRemittanceReport','/reports/bankRemittanceReport-criteria.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,'Bank Remittance Report','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'BankRemittanceReportResult','/reports/bankRemittanceReport-report.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,'BankRemittanceReportResult','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxReceiptApproverPosition','/receipts/ajaxBankRemittance-positionUserList.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxReceiptApproverPosition','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxCollectionsBankBranch','/receipts/ajaxBankRemittance-bankBranchList.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxCollectionsBankBranch','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxCollectionsBankAccount','/receipts/ajaxBankRemittance-accountList.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxCollectionsBankAccount','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'AjaxCollectionsApproverDesg','/receipts/ajaxBankRemittance-approverDesignationList.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'AjaxCollectionsApproverDesg','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'CashCollectionSubmissionReport','/reports/cashCollectionReport-submissionReport.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'CashCollectionSubmissionReport','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'ChequeCollectionSubmissionReport','/reports/chequeCollectionReport-submissionReport.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'ChequeCollectionSubmissionReport','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'CollectionsWorkflowApprove','/receipts/collectionsWorkflow-approveCollections.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'CollectionsWorkflowApprove','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'CollectionsWorkflowReject','/receipts/collectionsWorkflow-rejectCollections.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'CollectionsWorkflowReject','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'BankRemittanceCreate','/receipts/bankRemittance-create.action',null,(select ID from eg_module where NAME ='Collection Transaction'),1,'BankRemittanceCreate','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'BankRemittance'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittance'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittance'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'BankRemittance'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReportResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReportResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReportResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'BankRemittanceReportResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptApproverPosition'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptApproverPosition'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptApproverPosition'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptApproverPosition'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankBranch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankBranch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankBranch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankBranch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankAccount'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankAccount'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankAccount'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsBankAccount'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsApproverDesg'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsApproverDesg'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsApproverDesg'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'AjaxCollectionsApproverDesg'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'CashCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CashCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'CashCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'CashCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'ChequeCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'ChequeCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'ChequeCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ChequeCollectionSubmissionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowApprove'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowApprove'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowApprove'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowApprove'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowReject'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowReject'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowReject'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'CollectionsWorkflowReject'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'BankRemittanceCreate'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittanceCreate'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'BankRemittanceCreate'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'BankRemittanceCreate'));





