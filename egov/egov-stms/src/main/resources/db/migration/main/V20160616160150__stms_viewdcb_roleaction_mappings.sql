
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES
 (nextval('seq_eg_role'), 'Sewerage Tax Report Viewer', 'Sewerage Tax Report Viewer', now(), 1, 1, now(), 0);

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),
'viewSewerageConnectionDCBReport','/reports/sewerageRateReportView',null,(select id from eg_module where name='SewerageTransactions')
,2,'view DCB Report',false,'stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'viewSewerageConnectionDCBReport'),
 id from eg_role where name in ('Super User');

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'viewSewerageConnectionDCBReport'),
 id from eg_role where name in ('Sewerage Tax Report Viewer');
 
 INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'viewSewerageConnectionDCBReport'),
 id from eg_role where name in ('Sewerage Tax Administrator');
 
INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'viewSewerageConnectionDCBReport'),
 id from eg_role where name in ('Sewerage Tax Creator');



