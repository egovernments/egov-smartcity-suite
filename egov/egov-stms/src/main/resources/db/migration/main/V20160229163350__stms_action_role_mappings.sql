---------------Action urls for View application--------------------
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'viewSewerageConnection','/application/view/',null,(select id from eg_module where name='SewerageTransactions'),null,'View Sewerage Connection','false','stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values((select id from eg_role where name = 'Citizen'), (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Collection Operator'),(select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms'));


--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Sewerage Tax Approver') and actionid = (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Collection Operator') and actionid = (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Citizen') and actionid = (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'CSC Operator') and actionid = (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'ULB Operator') and actionid = (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='viewSewerageConnection' and contextroot = 'stms');

--rollback delete from EG_ACTION where name = 'viewSewerageConnection' and contextroot = 'stms';