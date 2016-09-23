------------------START------------------
CREATE TABLE egswtax_connection
(
  id bigint NOT NULL,
  dhsc_number character varying(50), 
  propertyidentifier character varying(50),
  propertytype character varying(50) NOT NULL,
  noofclosets_residential integer,
  noofclosets_nonresidential integer,
  connectionstatus character varying(20) NOT NULL,
  demand bigint,
  executiondate date,
  legacy boolean,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_swtax_connection PRIMARY KEY (id),
  CONSTRAINT fk_swtax_connection_demand FOREIGN KEY (demand)
      REFERENCES eg_demand (id)
);

CREATE INDEX idx_swtax_connection_dhsc_number ON egswtax_connection USING btree (dhsc_number);
CREATE INDEX idx_swtax_connection_propertyidentifier ON egswtax_connection USING btree (propertyidentifier);
CREATE INDEX idx_swtax_connection_demand ON egswtax_connection USING btree (demand);
CREATE INDEX idx_swtax_connectionstatus ON egswtax_connection USING btree (connectionstatus);

CREATE sequence seq_egswtax_connection;
-------------------END-------------------

------------------START------------------
CREATE TABLE egswtax_applicationdetails
(
  id bigint NOT NULL,
  applicationtype bigint NOT NULL,
  connection bigint NOT NULL,
  applicationnumber character varying(50), 
  applicationdate date,
  disposaldate date,
  state_id bigint,
  status bigint NOT NULL,  
  approvalnumber character varying(50),
  approvaldate date,   
  workordernumber character varying(50),
  workorderdate date,
  filestoreid bigint,
  donationcharges double precision,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_swtax_applicationdetail PRIMARY KEY (id),
  CONSTRAINT fk_swtax_appdtl_appltype FOREIGN KEY (applicationtype)
      REFERENCES egswtax_application_type (id),
  CONSTRAINT fk_swtax_appdtl_connection FOREIGN KEY (connection)
      REFERENCES egswtax_connection (id),   
  CONSTRAINT fk_swtax_appdtl_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),    
   CONSTRAINT fk_swtax_appdtl_status FOREIGN KEY (status)
      REFERENCES egw_status (id)
);

CREATE SEQUENCE seq_egswtax_applicationdetails;

CREATE INDEX idx_swtax_appdtl_apptype ON egswtax_applicationdetails USING btree (applicationtype);
CREATE INDEX idx_swtax_appdtl_connection ON egswtax_applicationdetails USING btree (connection);
CREATE INDEX idx_swtax_appdtl_applicationno ON egswtax_applicationdetails USING btree (applicationnumber);
CREATE INDEX idx_swtax_appdtl_state ON egswtax_applicationdetails USING btree (state_id);
CREATE INDEX idx_swtax_appdtl_status ON egswtax_applicationdetails USING btree (status);
-------------------END-------------------

------------------START------------------
CREATE TABLE egswtax_connection_history
(
  id bigint NOT NULL,
  connection bigint NOT NULL,
  propertytype character varying(50) NOT NULL,
  noofclosets_residential integer,
  noofclosets_nonresidential integer,
  demand bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_swtax_connectionhistory PRIMARY KEY (id),
  CONSTRAINT fk_swtax_connectionhistory_demand FOREIGN KEY (demand)
      REFERENCES eg_demand (id),
  CONSTRAINT fk_swtax_connectionhistory_connection FOREIGN KEY (connection)
      REFERENCES egswtax_connection (id)
);

CREATE INDEX idx_swtax_connectionhistory_connection ON egswtax_connection_history USING btree (connection);
CREATE INDEX idx_swtax_connectionhistory_demand ON egswtax_connection_history USING btree (demand);

CREATE sequence seq_egswtax_connection_history;
-------------------END-------------------

------------------START------------------
CREATE TABLE egswtax_estimation_details
(
  id bigint NOT NULL,
  applicationdetails bigint NOT NULL,
  itemdescription character varying(1024) NOT NULL,
  unitrate double precision,
  unitofmeasurement character varying(50),
  quantity double precision,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_swtax_estimationdetails PRIMARY KEY (id),
  CONSTRAINT fk_swtax_estimationdetails_applicationdetails FOREIGN KEY (applicationdetails)
      REFERENCES egswtax_applicationdetails (id)
);

CREATE INDEX idx_swtax_estimationdetails_applicationdetails ON egswtax_estimation_details USING btree (applicationdetails);
CREATE sequence seq_egswtax_estimation_details;
-------------------END-------------------

------------------START------------------
CREATE TABLE egswtax_fieldinspection_details
(
  id bigint NOT NULL,
  applicationdetails bigint NOT NULL,
  noofpipes integer,
  pipesize numeric,
  noofscrews integer,
  estimationcharges double precision NOT NULL,
  filestoreid bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_swtax_fieldinspectiondetails PRIMARY KEY (id),
  CONSTRAINT fk_swtax_fieldinspectiondetails_applicationdetails FOREIGN KEY (applicationdetails)
      REFERENCES egswtax_applicationdetails (id)
);

CREATE index  idx_swtax_fieldinspectiondetails_applicationdetails ON egswtax_fieldinspection_details USING btree (applicationdetails);
CREATE sequence seq_egswtax_fieldinspection_details;
-------------------END-------------------

--rollback DROP SEQUENCE seq_egswtax_fieldinspection_details;
--rollback DROP TABLE egswtax_fieldinspection_details;

--rollback DROP SEQUENCE seq_egswtax_estimation_details;
--rollback DROP TABLE egswtax_estimation_details;

--rollback DROP SEQUENCE seq_egswtax_connection_history;
--rollback DROP TABLE egswtax_connection_history;

--rollback DROP SEQUENCE seq_egswtax_applicationdetails;
--rollback DROP TABLE egswtax_applicationdetails;

--rollback DROP SEQUENCE seq_egswtax_connection;
--rollback DROP TABLE egswtax_connection;