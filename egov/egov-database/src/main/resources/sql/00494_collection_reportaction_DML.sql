update eg_action set parentmodule = (select  id from eg_module where name ='Receipt Services') 
where contextroot='collection' and parentmodule = (select  id from eg_module where name ='COLLECTION-COMMON');

update eg_action  set enabled=true where contextroot='collection' and name='CollectionBillingStub'

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'CollectionSummaryReport','/reports/collectionSummary-criteria.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,'Collection Summary','1','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'CollectionSummaryReportResult','/reports/collectionSummary-report.action',null,(select ID from eg_module where NAME ='Collection Reports'),1,'Collection Summary','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));







