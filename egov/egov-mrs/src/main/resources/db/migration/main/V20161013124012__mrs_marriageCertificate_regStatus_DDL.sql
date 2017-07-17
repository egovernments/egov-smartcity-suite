---------------- egmrs_certificate -------------------------

CREATE TABLE egmrs_certificate
(
  id bigint NOT NULL, -- Primary Key
  registration bigint, -- FK to egmrs_registration
  certificatetype character varying(32),
  certificateno character varying(64), 
  certificatedate timestamp without time zone,
  filestore bigint,
  reissue bigint,  -- FK to egmrs_reissue
  certificateissued boolean DEFAULT false,
  createdby bigint NOT NULL,
  createddate timestamp without time zone, 
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  version bigint,
  CONSTRAINT pk_egmrs_certificate PRIMARY KEY (id),
  CONSTRAINT fk_certificate_registration FOREIGN KEY (registration)
      REFERENCES egmrs_registration (id) ,
  CONSTRAINT fk_certificate_reissue FOREIGN KEY (reissue)
      REFERENCES egmrs_reissue (id),
  CONSTRAINT uniq_certificateno_type UNIQUE (certificateno,certificatetype)
);

CREATE SEQUENCE SEQ_EGMRS_CERTIFICATE;

CREATE SEQUENCE SEQ_EGMRS_CERTIFICATE_NUMBER;

-------------------- egmrs_registration ------------------------
 
ALTER TABLE egmrs_registration ALTER COLUMN status type bigint using cast(status as bigint);

ALTER TABLE ONLY egmrs_registration
    ADD CONSTRAINT fk_registration_status FOREIGN KEY (status) REFERENCES egw_status(id);

ALTER TABLE egmrs_registration ADD COLUMN isactive boolean DEFAULT false;

ALTER TABLE egmrs_registration drop COLUMN certificateissued;
