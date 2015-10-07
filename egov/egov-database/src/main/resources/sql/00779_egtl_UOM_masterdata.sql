insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'HP','HP',false,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'Person','Person',false,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'KiloGram','KiloGram',true,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'Ton','Ton',true,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'Ton','Ton',true,1,1,now(),now(),0);

insert into egtl_mstr_unitofmeasure (id,code,name,active,createdby,lastmodifiedby,createddate,lastmodifieddate,version)
values(nextval('seq_egtl_mstr_unitofmeasure'),'Meter','Meter',true,1,1,now(),now(),0);



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'Create-License FeeMatrix','/feematrix/create?fee=License Fee',
(select id from eg_module where name='Trade License Masters'),1,'Create-License FeeMatrix',true,'tl',
(select id from eg_module where name='Trade License' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Create-License FeeMatrix'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'Create-Motor FeeMatrix','/feematrix/create?fee=Motor Fee',
(select id from eg_module where name='Trade License Masters'),1,'Create-Motor FeeMatrix',true,'tl',
(select id from eg_module where name='Trade License' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Create-Motor FeeMatrix'));



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
values(nextval('SEQ_EG_ACTION'),'Create-Workforce FeeMatrix','/feematrix/create?fee=Motor Fee',
(select id from eg_module where name='Trade License Masters'),1,'Create-Workforce FeeMatrix',true,'tl',
(select id from eg_module where name='Trade License' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Create-Workforce FeeMatrix'));






