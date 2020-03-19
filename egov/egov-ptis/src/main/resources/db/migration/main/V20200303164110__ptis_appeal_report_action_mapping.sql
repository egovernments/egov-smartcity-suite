INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Appeal Register Report','/report/appeal',null,(select id from eg_module where name='Existing property'),1,'Appeal Register Report',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'appealreportresults','/report/appeal/result',null,(select id from eg_module where name='Existing property'),1,'appealreportresults',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Appeal Register Report'),id from eg_role where name in ('Property Approver','Property Administrator','SYSTEM');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'appealreportresults'),id from eg_role where name in ('Property Approver','Property Administrator','SYSTEM');



