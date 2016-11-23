----actions for property deactivation---
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Property Deactivation Form','/deactivation/new',null,(select id from eg_module where name='Existing property'),1,'Property Deactivation',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Property Deactivation Search','/deactivation/search',null,(select id from eg_module where name='Existing property'),1,'Property Deactivation Search',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Property Deactivation Update','/deactivation/update',null,(select id from eg_module where name='Existing property'),1,'Property Deactivation Update',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

-----roleaction mapping-------
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Deactivation Form' and contextroot='ptis'), id from eg_role where name in ('Property Approver');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Deactivation Search' and contextroot='ptis'), id from eg_role where name in ('Property Approver');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Deactivation Update' and contextroot='ptis'), id from eg_role where name in ('Property Approver');




