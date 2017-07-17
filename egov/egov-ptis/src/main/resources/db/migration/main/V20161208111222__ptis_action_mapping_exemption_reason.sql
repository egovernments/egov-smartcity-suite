-----action mapping

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Exemption Reason','/exemption/view',null,
(select id from eg_module where name='PTIS-Masters'),1,'View Exemption Reason',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

------role action mapping

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Exemption Reason'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Exemption Reason'),
id from eg_role where name in ('Property Approver');

