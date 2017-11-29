INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'CollectionSummaryHeadWiseReport','/reports/collectionSummaryHeadWise-criteria.action',null,(select id from eg_module where name='Collection Reports'),1,'Collection Summary-Head wise Report',true,'collection',0,1,to_timestamp('2015-08-15 11:04:25.670024','null'),1,to_timestamp('2015-08-15 11:04:25.670024','null'),(select id from eg_module where name='Collection'));

INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'CollectionSummaryHeadWiseReportResult','/reports/collectionSummaryHeadWise-report.action',null,(select id from eg_module where name='Collection Reports'),1,'Collection Summary-Head wise Report',false,'collection',0,1,to_timestamp('2015-08-15 11:04:25.670024','null'),1,to_timestamp('2015-08-15 11:04:25.670024','null'),(select id from eg_module where name='Collection'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='CollectionSummaryHeadWiseReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='CollectionSummaryHeadWiseReportResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name= 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CollectionSummaryHeadWiseReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name= 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'CollectionSummaryHeadWiseReportResult'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='CollectionSummaryHeadWiseReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='CollectionSummaryHeadWiseReportResult'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CollectionSummaryHeadWiseReport'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_View Access'),(select id from eg_action where name='CollectionSummaryHeadWiseReportResult'));
