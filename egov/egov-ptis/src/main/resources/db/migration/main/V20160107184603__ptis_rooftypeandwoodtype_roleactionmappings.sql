------------RoofType RoleActionMappings---------------

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Create Roof Type','/rooftype/create',null,
(select id from eg_module where name='PTIS-Masters'),1,'Roof Type',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Roof Type'),
id from eg_role where name in ('Super User');


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search Roof Type','/rooftype/search',null,
(select id from eg_module where name='PTIS-Masters'),1,'Roof Type',true,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Roof Type'),
id from eg_role where name in ('Super User');

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Roof Type','/rooftype/view',null,
(select id from eg_module where name='PTIS-Masters'),1,'Roof Type',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Roof Type'),
id from eg_role where name in ('Super User');

------------WoodType RoleActionMappings---------------

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Create Wood Type','/woodtype/create',null,
(select id from eg_module where name='PTIS-Masters'),1,'Wood Type',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Wood Type'),
id from eg_role where name in ('Super User');


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search Wood Type','/woodtype/search',null,
(select id from eg_module where name='PTIS-Masters'),1,'Wood Type',true,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Wood Type'),
id from eg_role where name in ('Super User');

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Wood Type','/woodtype/view',null,
(select id from eg_module where name='PTIS-Masters'),1,'Wood Type',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Wood Type'),
id from eg_role where name in ('Super User');


