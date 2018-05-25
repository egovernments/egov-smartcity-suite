DROP TABLE egevntnotification_event;
DROP SEQUENCE seq_egevntnotification_event;

CREATE SEQUENCE seq_egevntnotification_event;

CREATE TABLE egevntnotification_event
(
  id bigint PRIMARY KEY NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(200) NOT NULL,
  eventhost character varying(100) NOT NULL,
  eventlocation character varying(100) NOT NULL,
  address character varying(200) NOT NULL,
  ispaid boolean NOT NULL,
  cost double precision,
  event_type character varying(50) NOT NULL,
  filestore bigint,
  start_date timestamp without time zone NOT NULL,
  end_date timestamp without time zone NOT NULL,
  message character varying(200),
  url character varying(200),
  status character varying(20),
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint DEFAULT 0
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
  start_date timestamp without time zone NOT NULL,
  repeat character varying(50) NOT NULL,
  message_template character varying(500) NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint DEFAULT 0,
  CONSTRAINT egevntnotification_schedule_pkey PRIMARY KEY (id)
);
