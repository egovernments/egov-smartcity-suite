
Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'SearchReceiptSearch','/receipts/searchReceipt-search.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'SearchReceiptSearch','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'CancelReceipt','/receipts/receipt-cancel.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'CancelReceipt','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'ViewReceipts','/receipts/receipt-viewReceipts.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'ViewReceipts','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'PrintReceipts','/receipts/receipt-printReceipts.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'PrintReceipts','0','collection',0,1,current_timestamp,1,current_timestamp);





