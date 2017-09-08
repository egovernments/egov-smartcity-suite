insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'bulkBoundaryUpdation-form','/bulkboundaryupdation/form',null,
(select id from eg_module where name='Existing property'),1,'Bulk Boundary Updation',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'bulkBoundaryUpdation-form'),
id from eg_role where name in ('Data Entry Operator');
