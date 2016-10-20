CREATE TABLE egmrs_registrationunit
(
  id bigint NOT NULL,
  name varchar(100) NOT NULL,
  code character varying(20) NOT NULL,
  Address character varying(256) NOT NULL,
  IsMainRegistrationUnit boolean NOT NULL default false,
  zone bigint NOT NULL,
  Isactive boolean NOT NULL default true,
  version bigint NOT NULL DEFAULT 0,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_registrationunit PRIMARY KEY (id),
  CONSTRAINT fk_regunit_zone FOREIGN KEY (zone) REFERENCES eg_boundary (id) 
);


create sequence SEQ_EGMRS_REGISTRATIONUNIT;