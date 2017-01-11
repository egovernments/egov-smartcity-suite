--action mapping
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Populate Property Department by Ownership','/common/ajaxcommon-propdepartment-byproptype',null,
(select id from eg_module  where name='Property Tax'),1,'Populate Property Department by Ownership',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

--role action mapping
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Property Department by Ownership'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Property Department by Ownership'),
id from eg_role where name in ('ULB Operator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Property Department by Ownership'),
id from eg_role where name in ('Property Verifier');
