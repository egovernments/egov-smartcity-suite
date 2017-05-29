CREATE TABLE EGP_NOTIFICATIONS
(
  id bigint NOT NULL,
  subject character varying(100) NOT NULL,
  message character varying(500) NOT NULL,
  readstatus boolean DEFAULT false,
  priorityflag character varying(1) not null,
  userid bigint not null,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  version bigint NOT NULL,
  CONSTRAINT pk_EGP_NOTIFICATIONS PRIMARY KEY (id),
  CONSTRAINT fk_portalNotification_user FOREIGN KEY (userid)
      REFERENCES eg_user (id),
  CONSTRAINT fk_portalnotification_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_portalnotification_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egp_notifications;
