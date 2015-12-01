Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ReceiptReconcileOnlinePayment','/citizen/onlineReceipt-reconcileOnlinePayment.action',null,(select id from eg_module where name='Receipt Services'),1,'ReceiptReconcileOnlinePayment',false,'collection',0,1,to_timestamp('2015-08-15 11:04:27.32357','null'),1,to_timestamp('2015-08-15 11:04:27.32357','null'),(select id from eg_module where name='Collection'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='ReceiptReconcileOnlinePayment'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ReceiptReconcileOnlinePayment'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ReceiptReconcileOnlinePayment'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ReceiptReconcileOnlinePayment'));


