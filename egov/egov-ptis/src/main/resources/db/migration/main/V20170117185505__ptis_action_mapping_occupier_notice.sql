
insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Occupier Notice','/occupiernotice/search',null,
(select id from eg_module where name='PTIS-Reports'),1,'Occupier Notice',true,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));


insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Genearte Occupier Notice','/occupiernotice/generatenotice',null,
(select id from eg_module where name='PTIS-Reports'),1,'Genearte Occupier Notice',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));


insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Occupier Notice'),
id from eg_role where name in ('Property Approver');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Genearte Occupier Notice'),
id from eg_role where name in ('Property Approver');
