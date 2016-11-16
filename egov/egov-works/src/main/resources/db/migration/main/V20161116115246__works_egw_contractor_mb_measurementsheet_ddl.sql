-- DROP TABLE egw_contractor_mb_measurementsheet;

CREATE TABLE egw_contractor_mb_measurementsheet
(
  id bigint NOT NULL,
  no numeric,
  length numeric,
  width numeric,
  depthorheight numeric,
  quantity numeric,
  contractormbdetails bigint,
  womsheetid bigint,
  createddate timestamp without time zone,
  createdby bigint,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  version bigint,
  remarks character varying(1024),
  CONSTRAINT pk_egw_contractor_mb_measurementsheet PRIMARY KEY (id),
  CONSTRAINT fk_egw_contractor_mb_measurementsheet_contractormbdetails FOREIGN KEY (contractormbdetails)
      REFERENCES egw_contractor_mb_details (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_egw_contractor_mb_measurementsheet_msheetid FOREIGN KEY (womsheetid)
      REFERENCES egw_wo_measurementsheet (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- DROP SEQUENCE seq_egw_contractor_mb_measurementsheet;

CREATE SEQUENCE seq_egw_contractor_mb_measurementsheet
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 60
  CACHE 1;

