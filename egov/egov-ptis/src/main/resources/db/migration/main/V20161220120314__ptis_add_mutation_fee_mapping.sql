insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Add Mutation Fee','/addmutationfee/create',null,
(select id from eg_module where name='PTIS-Masters'),1,'Add Mutation Fee',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Add Mutation Fee'),
id from eg_role where name in ('Super User','Property Approver');