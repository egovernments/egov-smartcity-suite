------------------START------------------
CREATE TABLE egswtax_application_type
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  name character varying(50) NOT NULL,
  description character varying(255),
  processingtime numeric NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_swtax_application_type PRIMARY KEY (id),
  CONSTRAINT unq_swtax_application_code UNIQUE (code),
  CONSTRAINT unq_swtax_application_name UNIQUE (name)
);
CREATE SEQUENCE seq_egswtax_application_type;
-------------------END-------------------

------------------START------------------
CREATE TABLE egswtax_donation_master
(
  id bigint NOT NULL,
  propertytype character varying(50) NOT NULL,
  noofclosets bigint NOT NULL,  
  amount double precision NOT NULL,
  fromdate date NOT NULL,
  todate date,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_donation_master PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egswtax_donation_master;
-------------------END-------------------

------------------START------------------
CREATE TABLE egswtax_sewerage_rates_master
(
  id bigint NOT NULL,
  propertytype character varying(50) NOT NULL,
  monthlyrate double precision NOT NULL,
  fromdate date NOT NULL,
  todate date,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_seweragerates_master PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egswtax_sewerage_rates_master;
-------------------END-------------------

--rollback DROP SEQUENCE seq_egswtax_sewerage_rates_master;
--rollback DROP TABLE egswtax_sewerage_rates_master;

--rollback DROP SEQUENCE seq_egswtax_donation_master;
--rollback DROP TABLE egswtax_donation_master;

--rollback DROP SEQUENCE seq_egswtax_application_type;
--rollback DROP TABLE egswtax_application_type;



