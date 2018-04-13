-- Table: eg_event

-- DROP TABLE eg_event;

CREATE TABLE eg_event
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(200) NOT NULL,
  start_date timestamp without time zone NOT NULL,
  start_time character varying(20) NOT NULL,
  end_date timestamp without time zone NOT NULL,
  end_time character varying(20) NOT NULL,
  eventhost character varying(100) NOT NULL,
  eventlocation character varying(100) NOT NULL,
  address character varying(200) NOT NULL,
  wallpaper character varying(50),
  ispaid boolean NOT NULL,
  fees double precision,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  CONSTRAINT eg_event_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE eg_event
  OWNER TO postgres;


-- Sequence: seq_eg_event

-- DROP SEQUENCE seq_eg_event;
  CREATE SEQUENCE seq_eg_event
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  ALTER TABLE seq_eg_event
  OWNER TO postgres;


INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname) VALUES (nextval('SEQ_EG_MODULE'), 'Event Notification', true, 'eventnotification', NULL, 'Event Notification');

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname) VALUES (nextval('SEQ_EG_MODULE'), 'Event', true, 'eventnotification', (select id from eg_module where name='Event Notification' ), 'Event');

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Create Event','/event/new/',(select id from eg_module where name='Event' ),1,
'Create Event',true,'eventnotification',(select id from eg_module where name='Event Notification' and parentmodule is null));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'View Event','/event/view/',(select id from eg_module where name='Event' ),1,
'View Event',true,'eventnotification',(select id from eg_module where name='Event Notification' and parentmodule is null));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Modify Event','/event/update/',(select id from eg_module where name='Event' ),1,
'Modify Event',true,'eventnotification',(select id from eg_module where name='Event Notification' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Create Event'));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='View Event'));
Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Modify Event'));