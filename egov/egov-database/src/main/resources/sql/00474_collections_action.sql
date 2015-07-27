Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'OnlineReceiptSaveNew','/citizen/onlineReceipt-saveNew.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'OnlineReceiptSaveNew','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));





