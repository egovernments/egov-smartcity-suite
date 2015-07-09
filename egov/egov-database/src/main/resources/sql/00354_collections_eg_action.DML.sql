
Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'ListReceiptWorkFlowAction','/receipts/collectionsWorkflow-listWorkflow.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'ListReceiptWorkFlowAction','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'SubmitReceiptWorkFlowAction','/receipts/collectionsWorkflow-save.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'SubmitReceiptWorkFlowAction','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'GenerateChequeReport','/receipts/collectionsWorkflow-submissionReportCheque.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'GenerateChequeReport','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'ApproveReceiptCollection','/receipts/collectionsWorkflow-approve.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'ApproveReceiptCollection','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'RejectReceiptCollection','/receipts/collectionsWorkflow-saveOrupdate.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'RejectReceiptCollection','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'ListDetailAction','/receipts/collectionsWorkflow!getListDetails.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'ListDetailAction','0','collection',0,1,current_timestamp,1,current_timestamp);


Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'SubmitReceiptCollection','/receipts/collectionsWorkflow-submitCollections.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'SubmitReceiptCollection','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'SearchReceipts','/receipts/searchReceipt.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'SearchReceipts','1','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'ajaxValidateReceiptRemit','/receipts/searchReceipt!validateReceiptRemit.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'ajaxValidateReceiptRemit','0','collection',0,1,current_timestamp,1,current_timestamp);



