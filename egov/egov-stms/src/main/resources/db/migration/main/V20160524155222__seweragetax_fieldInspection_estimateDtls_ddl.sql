----------------------egswtax_fieldinspection----------------------------------

CREATE TABLE egswtax_fieldinspection
(
  id bigint NOT NULL,
  applicationdetails bigint NOT NULL,
  isactive boolean DEFAULT true,
  inspectiondate date NOT NULL,
  filestoreid bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL
);

ALTER TABLE ONLY egswtax_fieldinspection
    ADD CONSTRAINT pk_fieldinspection_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_fieldinspection
    ADD CONSTRAINT fk_applicationdetails_id_fkey FOREIGN KEY (applicationdetails) REFERENCES egswtax_applicationdetails (id);

CREATE SEQUENCE seq_egswtax_fieldinspection; 

----------------------egswtax_fieldinspection_details----------------------------------

ALTER TABLE egswtax_fieldinspection_details DROP COLUMN isactive;
ALTER TABLE egswtax_fieldinspection_details DROP COLUMN applicationdetails;
ALTER TABLE egswtax_fieldinspection_details DROP COLUMN inspectiondate;
ALTER TABLE egswtax_fieldinspection_details DROP COLUMN  filestoreid;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN fieldinspection bigint NOT NULL;
ALTER TABLE ONLY egswtax_fieldinspection_details
    ADD CONSTRAINT fk_fieldinspection_id_fkey FOREIGN KEY (fieldinspection) REFERENCES egswtax_fieldinspection (id);

-------------------------egswtax_estimation_details----------------------------

ALTER TABLE egswtax_estimation_details ADD COLUMN amount double precision NOT NULL;
Alter table egswtax_estimation_details ALTER COLUMN unitofmeasurement type bigint using cast(unitofmeasurement as bigint);
ALTER TABLE ONLY egswtax_estimation_details
    ADD CONSTRAINT fk_estimationDetail_uomid FOREIGN KEY (unitofmeasurement) REFERENCES eg_uom (id);
