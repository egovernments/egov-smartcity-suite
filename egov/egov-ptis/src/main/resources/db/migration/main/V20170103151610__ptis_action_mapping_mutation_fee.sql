-------Add Mutation Fee

update EG_ACTION set url='/mutationfee/create' where name='Add Mutation Fee';

----Modify Mutation Fee

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Modify Mutation Fee','/mutationfee/modify',null,
(select id from eg_module where name='PTIS-Masters'),1,'Modify Mutation Fee',true,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Modify Mutation Fee'),
id from eg_role where name in ('Super User','Property Approver');

----View Mutation Fee
insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Mutation Fee','/mutationfee/view',null,
(select id from eg_module where name='PTIS-Masters'),1,'View Mutation Fee',true,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Mutation Fee'),
id from eg_role where name in ('Super User','Property Approver');


