CREATE TABLE egswtax_batchdemandgenerate
(
  id bigint NOT NULL,
  active boolean NOT NULL DEFAULT true,
  totalrecords bigint NOT NULL DEFAULT 0,
  successfullrecords bigint NOT NULL DEFAULT 0,
  failurerecords bigint NOT NULL DEFAULT 0,
  jobname character varying(120) NOT NULL,
  createddate timestamp without time zone NOT NULL DEFAULT ('now'::text)::date,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version bigint DEFAULT 0,
  installment bigint NOT NULL,
  CONSTRAINT fk_swtax_batchdmdgen_installment FOREIGN KEY (installment)
      REFERENCES eg_installment_master (id)
);

CREATE SEQUENCE SEQ_SWTAX_BATCHDEMANDGENERATE;

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'Number of Records used in next year demand generation','Number of Records used in next year demand generation',0, (select id from eg_module where name='Sewerage Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Number of Records used in next year demand generation' AND  MODULE =(select id from eg_module where name='Sewerage Tax Management')),current_date,  '2000',0);



CREATE TABLE egswtax_demandgenerationlog
(
  id bigint NOT NULL,
  installmentyear character varying(32),
  executionstatus character varying(32),
  demandgenerationstatus character varying(32),
  createddate timestamp without time zone,
  createdby bigint,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  version bigint,
  CONSTRAINT pk_egswtax_demandgeneration PRIMARY KEY (id)
);

create sequence seq_egswtax_demandgenerationlog;

CREATE TABLE egswtax_demandgenerationlogdetail
(
  id bigint NOT NULL,
  demandgenerationlog bigint,
  applicationdetails bigint,
  status character varying(32),
  detail character varying(1000),
  version bigint,
  CONSTRAINT pk_egswtax_demandgenerationdetail PRIMARY KEY (id),
  CONSTRAINT fk_egswtax_demandgeneration FOREIGN KEY (demandgenerationlog)
      REFERENCES egswtax_demandgenerationlog (id),
  CONSTRAINT fk_egswtax_applicationdetails FOREIGN KEY (applicationdetails)
      REFERENCES egswtax_applicationdetails (id)
);

create sequence seq_egswtax_demandgenerationlogdetail;

