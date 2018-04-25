CREATE SEQUENCE seq_eg_event;

CREATE TABLE eg_event
(
  id bigint NOT NULL PRIMARY KEY,
  name character varying(100) NOT NULL,
  description character varying(200) NOT NULL,
  start_time character varying(20) NOT NULL,
  end_time character varying(20) NOT NULL,
  eventhost character varying(100) NOT NULL,
  eventlocation character varying(100) NOT NULL,
  address character varying(200) NOT NULL,
  ispaid boolean NOT NULL,
  cost double precision,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint DEFAULT 0,
  event_type character varying(50) NOT NULL,
  filestore bigint,
  start_date bigint NOT NULL,
  end_date bigint NOT NULL
);


INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname) VALUES (nextval('SEQ_EG_MODULE'), 'Event Notification', true, 'eventnotification', NULL, 'Event Notification');

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Create Event','/event/create/',(select id from eg_module where name='Event Notification' ),1,
'Create Event',false,'eventnotification',(select id from eg_module where name='Event Notification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Event','/event/view/',(select id from eg_module where name='Event Notification' ),1,
'View Event',true,'eventnotification',(select id from eg_module where name='Event Notification'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Modify Event','/event/update/',(select id from eg_module where name='Event Notification' ),1,
'Modify Event',false,'eventnotification',(select id from eg_module where name='Event Notification'));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Create Event'));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Event'));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Modify Event'));
