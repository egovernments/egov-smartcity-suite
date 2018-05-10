Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Create Schedule','/schedule/create/',(select id from eg_module where name='Notifications' ),1,
'Create Schedule',false,'eventnotification',(select id from eg_module where name='Notifications'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Schedule','/schedule/view/',(select id from eg_module where name='Notifications' ),1,
'Schedule',true,'eventnotification',(select id from eg_module where name='Notifications'));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Schedule'));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Create Schedule'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Delete Schedule','/schedule/delete/',(select id from eg_module where name='Notifications' ),1,
'Delete Schedule',false,'eventnotification',(select id from eg_module where name='Notifications'));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Delete Schedule'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Update Schedule','/schedule/update/',(select id from eg_module where name='Notifications' ),1,
'Update Schedule',false,'eventnotification',(select id from eg_module where name='Notifications'));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Update Schedule'));

CREATE SEQUENCE seq_egevntnotification_schedule;
CREATE TABLE egevntnotification_schedule
(
  id bigint NOT NULL,
  templatename character varying(100) NOT NULL,
  notification_type character varying(200) NOT NULL,
  status character varying(100) NOT NULL,
  start_date bigint NOT NULL,
  start_time character varying(20) NOT NULL,
  repeat character varying(50) NOT NULL,
  message_template character varying(500) NOT NULL,
  createdby bigint,
  createddate bigint,
  updatedby bigint,
  updateddate bigint,
  CONSTRAINT egevntnotification_schedule_pkey PRIMARY KEY (id)
);

update eg_action set displayname='Event' where name='View Event'
