ALTER TABLE egtl_license
  ADD COLUMN classification_type VARCHAR(50),
  ADD COLUMN employers_name VARCHAR(50),
  ADD COLUMN mandal_name VARCHAR(100),
  ADD COLUMN door_number VARCHAR(50),
  ADD COLUMN direct_worker_male bigint,
  ADD COLUMN direct_worker_female bigint,
  ADD COLUMN contract_worker_male bigint,
  ADD COLUMN contract_worker_female bigint,
  ADD COLUMN daily_wages_male bigint,
  ADD COLUMN daily_wages_female bigint,
  ADD COLUMN total_workers bigint;

ALTER TABLE egtl_license_aud
  ADD COLUMN classification_type VARCHAR(50),
  ADD COLUMN employers_name VARCHAR(50),
  ADD COLUMN mandal_name VARCHAR(100),
  ADD COLUMN door_number VARCHAR(50),
  ADD COLUMN direct_worker_male bigint,
  ADD COLUMN direct_worker_female bigint,
  ADD COLUMN contract_worker_male bigint,
  ADD COLUMN contract_worker_female bigint,
  ADD COLUMN daily_wages_male bigint,
  ADD COLUMN daily_wages_female bigint,
  ADD COLUMN total_workers bigint;

CREATE TABLE egtl_mstr_employers (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
	version bigint
);

CREATE SEQUENCE seq_egtl_mstr_employers;

INSERT INTO egtl_mstr_employers (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_employers'),'Proprietor',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_employers (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_employers'),'Managing Partner',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_employers (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_employers'),'Partner',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_employers (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_employers'),'Director',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_employers (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_employers'),'Managing Director',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_employers (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_employers'),'CEO',Current_date,Current_date,1,1,0);

COMMENT ON TABLE egtl_mstr_employers IS 'Master Table for Labour Employers';
COMMENT ON COLUMN egtl_mstr_employers.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_employers.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_employers.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_employers.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN egtl_mstr_employers.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_employers.lastmodifiedby IS 'Last Modified by UserId foreign key to EG_USER';

CREATE TABLE egtl_mstr_classification_type (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
	version bigint
);
CREATE SEQUENCE seq_egtl_mstr_classification_type;

COMMENT ON TABLE egtl_mstr_classification_type IS 'Master Table for Labour Classification Type';
COMMENT ON COLUMN egtl_mstr_classification_type.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_classification_type.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_classification_type.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_classification_type.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN egtl_mstr_classification_type.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_classification_type.lastmodifiedby IS 'Last Modified by UserId foreign key to EG_USER';

INSERT INTO egtl_mstr_classification_type (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_classification_type'),'Proprietor',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_classification_type (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_classification_type'),'Partnership Firm',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_classification_type (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_classification_type'),'Private Limited',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_classification_type (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_classification_type'),'Public Limited',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_classification_type (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_classification_type'),'Public Sector Under Taking',Current_date,Current_date,1,1,0);
INSERT INTO egtl_mstr_classification_type (id,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version) values(nextval('seq_egtl_mstr_classification_type'),'Co-operative society',Current_date,Current_date,1,1,0);

