insert into eg_action values(nextval('seq_eg_action'),'Create Employee Data','/employeeMaster/create',null,
(select id from eg_module where name='Employee'),null,'Employee-Data Entry',true,'eis',0,1,now(),1,now(),(select id from eg_module where name='EIS'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Create Employee Data'));

