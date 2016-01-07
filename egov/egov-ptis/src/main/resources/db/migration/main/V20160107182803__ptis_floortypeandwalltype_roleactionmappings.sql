--eg_module for Master--
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'PTIS-Masters', true, 'ptis', (select id from eg_module where name='Property Tax'), 'Masters', 6);

--Wall type action and roleactions
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Create Wall Type','/wallType/create',null,(select id from eg_module where name='PTIS-Masters'),1,'Wall Type',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search Wall Type','/wallType/search',null,(select id from eg_module where name='PTIS-Masters'),1,'Wall Type',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Wall Type','/wallType/view',null,(select id from eg_module where name='PTIS-Masters'),1,'Wall Type',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Wall Type'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Wall Type'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Wall Type'),
id from eg_role where name in ('Super User');


--Floortype action and roleactions
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Create Floor Type','/floorType/create',null,(select id from eg_module where name='PTIS-Masters'),1,'Floor Type',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search Floor Type','/floorType/search',null,(select id from eg_module where name='PTIS-Masters'),1,'Floor Type',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Floor Type','/floorType/view',null,(select id from eg_module where name='PTIS-Masters'),1,'Floor Type',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Floor Type'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Floor Type'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Floor Type'),
id from eg_role where name in ('Super User');