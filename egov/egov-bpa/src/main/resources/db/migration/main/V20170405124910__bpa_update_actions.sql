
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'admissionfeeonchangeofServiceTYpe','/ajax/getAdmissionFees',null,(select id from eg_module where name='BPA Transanctions'),1,'admissionfeeonchangeofServiceTYpe','false','bpa',0,1,now(),1,now(),
(select id from eg_module where name='BPA'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'),
(select id from eg_action where name='admissionfeeonchangeofServiceTYpe'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='admissionfeeonchangeofServiceTYpe'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'updateBpaApplication','/application/update/',null,(select id from eg_module where name='BPA Transanctions'),1,'Update Bpa Application','false','bpa',0,1,now(),1,now(),
(select id from eg_module where name='BPA'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'),
(select id from eg_action where name='updateBpaApplication'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='updateBpaApplication'));