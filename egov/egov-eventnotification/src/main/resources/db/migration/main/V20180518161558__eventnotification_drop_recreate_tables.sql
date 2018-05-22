DROP TABLE eg_event;
DROP SEQUENCE seq_eg_event;

CREATE SEQUENCE seq_egevntnotification_event;

CREATE TABLE egevntnotification_event
(
  id bigint PRIMARY KEY NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(200) NOT NULL,
  start_time character varying(20) NOT NULL,
  end_time character varying(20) NOT NULL,
  eventhost character varying(100) NOT NULL,
  eventlocation character varying(100) NOT NULL,
  address character varying(200) NOT NULL,
  ispaid boolean NOT NULL,
  cost double precision,
  event_type character varying(50) NOT NULL,
  filestore bigint,
  start_date bigint NOT NULL,
  end_date bigint NOT NULL,
  message character varying(200),
  url character varying(200),
  status character varying(20),
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint DEFAULT 0
);

DROP TABLE eg_userevent;
DROP SEQUENCE seq_eg_userevent;

CREATE SEQUENCE seq_egevntnotification_userevent;
CREATE TABLE egevntnotification_userevent
(
  id bigint PRIMARY KEY NOT NULL,
  userid bigint NOT NULL,
  eventid bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint
);

DROP TABLE eg_userfcmdevice;
DROP SEQUENCE seq_eg_user_fcm_device;

CREATE SEQUENCE seq_egevntnotification_userfcmdevice;
CREATE TABLE egevntnotification_userfcmdevice
(
  id bigint NOT NULL,
  userid bigint NOT NULL,
  devicetoken character varying NOT NULL,
  deviceid character varying,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint,
  CONSTRAINT pk_eg_userfcmdevice_pkey PRIMARY KEY (id),
  CONSTRAINT unq_eg_user_devicetoken UNIQUE (userid, devicetoken)
);

DROP TABLE egevntnotification_schedule;
DROP SEQUENCE seq_egevntnotification_schedule;

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
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint DEFAULT 0,
  CONSTRAINT egevntnotification_schedule_pkey PRIMARY KEY (id)
);

DROP TABLE egevntnotification_schedule_log;
DROP SEQUENCE seq_egevntnotification_schedule_log;

CREATE SEQUENCE seq_egevntnotification_schedule_log;
CREATE TABLE egevntnotification_schedule_log
(
  id bigint NOT NULL,
  filestore bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint DEFAULT 0,
  CONSTRAINT egevntnotification_schedule_log_pkey PRIMARY KEY (id)
);

DROP TABLE egevntnotification_drafts;
DROP SEQUENCE seq_egeventnotification_drafts;

CREATE SEQUENCE seq_egeventnotification_drafts; 
CREATE TABLE egevntnotification_drafts
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  type character varying(100) NOT NULL,
  module_id bigint,
  category_id bigint,
  notification_message text,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint DEFAULT 0,
  CONSTRAINT egevntnotification_drafts_pkey PRIMARY KEY (id),
  CONSTRAINT fk_category_drafts_ref FOREIGN KEY (category_id)
      REFERENCES egevntnotification_category (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_module_drafts_ref FOREIGN KEY (module_id)
      REFERENCES egevntnotification_module (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
