INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ArrearDemandRegisterVLT','/report/arrdmdrgstr-vlt/form',null,
(select id from eg_module  where name='Property Tax'),1,'Arrear Demand Register(VLT)',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ArrearDemandRegisterPT','/report/arrdmdrgstr-pt/form',null,
(select id from eg_module  where name='Property Tax'),1,'Arrear Demand Register(PT)',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ArrearDemandRegisterResult','/report/arrdmdrgstr/result',null,
(select id from eg_module  where name='Property Tax'),1,'ArrearDemandRegisterResult',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterVLT'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterVLT'),
id from eg_role where name in ('ULB Operator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterVLT'),
id from eg_role where name in ('Property Verifier');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterVLT'),
id from eg_role where name in ('Property Approver');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterPT'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterPT'),
id from eg_role where name in ('ULB Operator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterPT'),
id from eg_role where name in ('Property Verifier');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterPT'),
id from eg_role where name in ('Property Approver');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterResult'), id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterResult'), id from eg_role where name in ('ULB Operator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterResult'), id from eg_role where name in ('Property Verifier');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ArrearDemandRegisterResult'), id from eg_role where name in ('Property Approver');
