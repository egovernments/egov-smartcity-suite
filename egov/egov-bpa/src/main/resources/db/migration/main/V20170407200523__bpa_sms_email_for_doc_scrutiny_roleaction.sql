--------------schedule appointment for doc scrutiny and sms and email---------------

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'new appointment schedule','/application/scheduleappointment',null,(select id from eg_module where name='BPA Transanctions'),5,'new appointment schedule','false','bpa',0,1,now(),1,now(), (select id from eg_module where name='BPA'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='new appointment schedule'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'), (select id from eg_action where name='new appointment schedule'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BPA Approver'), (select id from eg_action where name='new appointment schedule'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'reschedule appointment','/application/postponeappointment',null,(select id from eg_module where name='BPA Transanctions'),6,'reschedule appointment','false','bpa',0,1,now(),1,now(),(select id from eg_module where name='BPA'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BPA Approver'),(select id from eg_action where name='reschedule appointment'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='reschedule appointment'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'), (select id from eg_action where name='reschedule appointment'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'view scheduled appointment','/application/view-appointment',null,(select id from eg_module where name='BPA Transanctions'),7,'view scheduled appointment','false','bpa',0,1,now(),1,now(),
(select id from eg_module where name='BPA'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='view scheduled appointment'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'), (select id from eg_action where name='view scheduled appointment'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BPA Approver'), (select id from eg_action where name='view scheduled appointment'));
