Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'SaveOnCancelReceipt','/receipts/receipt-saveOnCancel.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'SaveOnCancelReceipt','0','collection',0,1,current_timestamp,1,current_timestamp);

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate") values (nextval('seq_eg_action'),'OnlineReceipt','/citizen/onlineReceipt-newform.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'OnlineReceipt','0','collection',0,1,current_timestamp,1,current_timestamp);




