
CREATE TABLE egeis_grievancetype
(
  id numeric NOT NULL,
  code character varying(50),
  name character varying(100),
  localname character varying(200),
  description character varying(256),
  active boolean,  
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint DEFAULT 0,
  CONSTRAINT pk_eis_grievancetype_id PRIMARY KEY (id),
  CONSTRAINT unq_eis_grievancetype_code UNIQUE (code),
  CONSTRAINT unq_eis_grievancetype_name UNIQUE (name)
);


CREATE SEQUENCE seq_egeis_grievancetype;



CREATE TABLE egeis_grievance
(
  id bigint NOT NULL,
  grievancenumber character varying(50) NOT NULL, 
  grievancetype bigint NOT NULL,
  employee bigint NOT NULL,
  details character varying(500) NOT NULL, 
  grievanceresolution character varying(500),   
  status bigint NOT NULL,
  state_id bigint,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint DEFAULT 0,
  CONSTRAINT pk_eis_grievance PRIMARY KEY (id),
  CONSTRAINT fk_eis_grievance_grievancetype FOREIGN KEY (grievancetype)
      REFERENCES egeis_grievancetype (id),
   CONSTRAINT fk_eis_grievance_employee FOREIGN KEY (employee)
      REFERENCES egeis_employee (id),
  CONSTRAINT unq_eis_grievancenumber UNIQUE (grievancenumber)
);


CREATE SEQUENCE seq_egeis_grievance;

CREATE INDEX fk_eis_grievance_type ON egeis_grievance USING btree (grievancetype);

CREATE INDEX fk_eis_grievance_employee ON egeis_grievance USING btree (employee);


CREATE TABLE egeis_grievancedocs
(
  filestoreid bigint NOT NULL,
  grievanceid bigint NOT NULL
);


