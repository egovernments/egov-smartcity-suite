CREATE TABLE eg_device (
  id bigint NOT NULL,
  deviceUId character varying(128)  NOT NULL,
  type character varying(32) NOT NULL,
  OSVersion character varying(32),
  createddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint,  
  CONSTRAINT eg_device_pkey PRIMARY KEY (id),
  CONSTRAINT eg_device_device_id_key UNIQUE (deviceUId)
) ;

CREATE TABLE eg_userdevice (
  userId bigint NOT NULL,
  deviceId bigint NOT NULL,
  createdDate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
  CONSTRAINT fk_userdevice FOREIGN KEY (deviceId)
      REFERENCES eg_device (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_user_userdevice FOREIGN KEY (userId)
      REFERENCES eg_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE seq_eg_device
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;



