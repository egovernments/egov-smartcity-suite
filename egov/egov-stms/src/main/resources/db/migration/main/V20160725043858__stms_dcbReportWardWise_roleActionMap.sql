
INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Sewerage DCB Drill Down Report Wardwise', 
'/reports/dcb-report-wardwise', null, (select id from eg_module where name = 'SewerageReports'), 2, ' DCB Drill down report Ward wise', true, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Super User') , (select id from eg_action where name='Sewerage DCB Drill Down Report Wardwise'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Creator') , (select id from eg_action where name='Sewerage DCB Drill Down Report Wardwise'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Administrator') , (select id from eg_action where name='Sewerage DCB Drill Down Report Wardwise'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Report Viewer') , (select id from eg_action where name='Sewerage DCB Drill Down Report Wardwise'));

--rollback    delete from eg_roleaction where actionid=(select id from eg_action where name='Sewerage DCB Drill Down Report Wardwise' and contextroot='stms');
--rollback    delete from eg_action where name='Sewerage DCB Drill Down Report Wardwise' and contextroot='stms';

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),
'DCBReportWardwiseList','/reports/dcbReportWardwiseList',null,(select id from eg_module where name='SewerageReports')
,2,'DCB Report Wardwise List',false,'stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'DCBReportWardwiseList'),
 id from eg_role where name in ('Super User');

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'DCBReportWardwiseList'),
 id from eg_role where name in ('Sewerage Tax Report Viewer');
 
INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'DCBReportWardwiseList'),
 id from eg_role where name in ('Sewerage Tax Administrator');
 
INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'DCBReportWardwiseList'),
 id from eg_role where name in ('Sewerage Tax Creator');

--rollback    delete from eg_roleaction where actionid=(select id from eg_action where name='DCBReportWardwiseList' and contextroot='stms');
--rollback    delete from eg_action where name='DCBReportWardwiseList' and contextroot='stms';

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Sewerage DCB Report View Connections', 
'/reports/dcbViewWardConnections', null, (select id from eg_module where name = 'SewerageReports'), 3, 'Sewerage DCB Report View Ward Connections', false, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Super User') , (select id from eg_action where name='Sewerage DCB Report View Connections'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Creator') , (select id from eg_action where name='Sewerage DCB Report View Connections'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Administrator') , (select id from eg_action where name='Sewerage DCB Report View Connections'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Report Viewer') , (select id from eg_action where name='Sewerage DCB Report View Connections'));

--rollback delete from eg_roleaction where actionid=(select id from eg_action where name='Sewerage DCB Report View Connections' and contextroot='stms');
--rollback delete from eg_action where name='Sewerage DCB Report View Connections' and contextroot='stms';
