CREATE SEQUENCE seq_egpushbox_userfcmdevice;
CREATE TABLE egpushbox_userfcmdevice
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
  CONSTRAINT pk_eguserfcmdevice_pkey PRIMARY KEY (id),
  CONSTRAINT unq_eguser_devicetoken UNIQUE (userid, devicetoken)
);