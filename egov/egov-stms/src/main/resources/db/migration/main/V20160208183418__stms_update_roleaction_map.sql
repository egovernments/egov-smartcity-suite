-----------------Update Sewerage Connection Role action Mapping--------------------
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'UpdateSewerageApplicationDetails','/transactions/update/',null,(select id from eg_module where name='SewerageTransactions'),null,'Update Sewerage Application Details','false','stms',0,1,to_timestamp('2015-08-15 11:04:04.11703','null'),1,to_timestamp('2015-08-15 11:04:04.11703','null'),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='UpdateSewerageApplicationDetails' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name ='UpdateSewerageApplicationDetails' and contextroot = 'stms'));

-----------------Download File Role action Mapping--------------------
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'DownloadFile','/transactions/downloadFile',null,(select id from eg_module where name='SewerageTransactions'),null,'Download Sewerage Application Files','false','stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='DownloadFile' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name ='DownloadFile' and contextroot = 'stms'));


--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'ULB Operator') and actionid = (select id from eg_action where name ='DownloadFile' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='DownloadFile' and contextroot = 'stms');
--rollback delete from EG_ACTION where name = 'DownloadFile' and contextroot = 'stms';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'ULB Operator') and actionid = (select id from eg_action where name ='UpdateSewerageApplicationDetails' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='UpdateSewerageApplicationDetails' and contextroot = 'stms');
--rollback delete from EG_ACTION where name = 'UpdateSewerageApplicationDetails' and contextroot = 'stms';