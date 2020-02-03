INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Revision Petition Register Report','/report/revisionpetition',null,(select id from eg_module where name='Existing property'),1,'Revision Petition Register Report',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'revsionpetitionreportajax','/report/revisionpetition/results',null,(select id from eg_module where name='Existing property'),1,'revsionpetitionreportajax',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'revsionpetitionreportajax'),id from eg_role where name in ('ERP Report Viewer','SYSTEM');


