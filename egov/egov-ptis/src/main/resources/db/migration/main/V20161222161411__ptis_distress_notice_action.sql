INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'Distress Notice','/distressnotice/searchform',null,(select id from eg_module  where name='Existing property'),8,'Distress Notice',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'Generate Distress Notice','/distressnotice/generatenotice',null,(select id from eg_module  where name='Existing property'),8,'Generate Distress Notice',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Distress Notice' and contextroot='ptis'), id from eg_role where name in ('Property Approver');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Generate Distress Notice' and contextroot='ptis'), id from eg_role where name in ('Property Approver');
