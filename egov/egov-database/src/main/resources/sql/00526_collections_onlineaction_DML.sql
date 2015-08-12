
Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'OnlineTransactionReport','/reports/onlineTransactionReport-criteria.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,'Online Transaction Report','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'OnlineTransactionReportResult','/reports/onlineTransactionReport-report.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,'Online Transaction Report Result','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));


Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'SearchOnlineReceipts','/citizen/searchOnlineReceipt.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'Search Online Receipts','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'SearchOnlineReceiptsSearch','/citizen/searchOnlineReceipt-search.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'SearchOnlineReceiptsSearch','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));


