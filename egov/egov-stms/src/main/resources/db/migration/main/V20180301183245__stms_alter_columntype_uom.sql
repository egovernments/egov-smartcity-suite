ALTER TABLE ONLY egswtax_estimation_details  DROP  CONSTRAINT fk_estimationDetail_uomid;

Alter table egswtax_estimation_details ALTER COLUMN unitofmeasurement type character varying(50) using cast(unitofmeasurement as character varying(50));