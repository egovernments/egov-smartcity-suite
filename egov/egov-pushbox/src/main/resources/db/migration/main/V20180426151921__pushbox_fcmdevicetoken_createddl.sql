CREATE TABLE eg_userfcmdevice
(
  id bigint NOT NULL,
  userid bigint NOT NULL,
  devicetoken varchar NOT NULL,
  createddate bigint,
  CONSTRAINT pk_eg_userfcmdevice_pkey PRIMARY KEY (id),
  CONSTRAINT unq_eg_user_devicetoken UNIQUE (userid, devicetoken)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE SEQ_EG_USER_FCM_DEVICE START WITH 1;