INSERT into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Employee Position Search',
'/employeepositions/_search',null,(select id from eg_module where name='Employee'),
1,'Employee Positions',false,'eis',0,1,now(),1,now(),(select id from eg_module where name='EIS'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='Employee Position Search'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Employee Admin'),
(select id from eg_action where name='Employee Position Search'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='EMPLOYEE'),
(select id from eg_action where name='Employee Position Search'));