------------------START------------------
CREATE TABLE egwtr_application_type
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  name character varying(50) NOT NULL,
  description character varying(255),
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_application_type PRIMARY KEY (id),
  CONSTRAINT unq_application_code UNIQUE (code),
  CONSTRAINT unq_application_name UNIQUE (name)
);
CREATE SEQUENCE seq_egwtr_application_type;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_category
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  name character varying(50) NOT NULL,
  description character varying(255),
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_category PRIMARY KEY (id),
  CONSTRAINT unq_category_code UNIQUE (code),
  CONSTRAINT unq_category_name UNIQUE (name)
);
CREATE SEQUENCE seq_egwtr_category;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_pipesize
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  sizeininch double precision NOT NULL,
  sizeinmilimeter double precision NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_pipesize PRIMARY KEY (id),
  CONSTRAINT unq_pipesize_code UNIQUE (code)
);
CREATE SEQUENCE seq_egwtr_pipesize;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_application_process_time
(
  id bigint NOT NULL,
  applicationtype bigint NOT NULL,
  category bigint NOT NULL,
  processingtime numeric NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_application_process_time PRIMARY KEY (id),
  CONSTRAINT fk_applicationprocesstime_applicationtype FOREIGN KEY (applicationtype) REFERENCES egwtr_application_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_applicationprocesstime_category FOREIGN KEY (category) REFERENCES egwtr_category (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unq_applicationprocesstime_applicationtype_category UNIQUE (applicationtype, category)
);

CREATE INDEX idx_applicationprocesstime_applicationtype ON egwtr_application_process_time USING btree (applicationtype);
CREATE INDEX idx_applicationprocesstime_category ON egwtr_application_process_time USING btree (category);
CREATE SEQUENCE seq_egwtr_application_process_time;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_water_source
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  watersourcetype character varying(50) NOT NULL,
  description character varying(255) NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_water_source PRIMARY KEY (id),
  CONSTRAINT unq_watersource_code UNIQUE (code),
  CONSTRAINT unq_watersource_watersourcetype UNIQUE (watersourcetype)
);
CREATE SEQUENCE seq_egwtr_water_source;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_usage_type
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  name character varying(50) NOT NULL,
  description character varying(255),
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_usage_type PRIMARY KEY (id),
  CONSTRAINT unq_usage_code UNIQUE (code),
  CONSTRAINT unq_usage_name UNIQUE (name)
);
CREATE SEQUENCE seq_egwtr_usage_type;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_securitydeposit
(
  id bigint NOT NULL,
  usagetype bigint NOT NULL,
  noofmonths numeric NOT NULL,
  active boolean NOT NULL,
  fromdate date NOT NULL,
  todate date,
  amount double precision NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_securitydeposit PRIMARY KEY (id),
  CONSTRAINT fk_securitydeposit_usagetype FOREIGN KEY (usagetype) REFERENCES egwtr_usage_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_securitydeposit_usagetype ON egwtr_securitydeposit USING btree (usagetype);
CREATE SEQUENCE seq_egwtr_securitydeposit;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_road_category
(
  id bigint NOT NULL,
  name character varying(50) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_road_category PRIMARY KEY (id),
  CONSTRAINT unq_road_category_name UNIQUE (name)
);
CREATE SEQUENCE seq_egwtr_road_category;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_property_type
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  name character varying(50) NOT NULL,
  connectioneligibility character(1) NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_property_type PRIMARY KEY (id),
  CONSTRAINT unq_propertytype_code UNIQUE (code),
  CONSTRAINT unq_propertytype_name UNIQUE (name)
);
CREATE SEQUENCE seq_egwtr_property_type;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_property_usage
(
  id bigint NOT NULL,
  usagetype bigint NOT NULL,
  propertytype bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_property_usage_pkey PRIMARY KEY (id),
  CONSTRAINT fk_property_usage_propertyid_fkey FOREIGN KEY (propertytype) REFERENCES egwtr_property_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_property_usage_uasgeid_fkey FOREIGN KEY (usagetype) REFERENCES egwtr_usage_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egwtr_property_usage;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_penalty
(
  id bigint NOT NULL,
  penaltytype character varying(50) NOT NULL,
  description character varying(255),
  active boolean NOT NULL,
  fromdate date NOT NULL,
  todate date,
  percentage double precision NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_penalty PRIMARY KEY (id)
);
CREATE SEQUENCE seq_egwtr_penalty;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_donation_header
(
  id bigint NOT NULL,
  category bigint NOT NULL,
  usagetype bigint NOT NULL,
  minpipesize bigint NOT NULL,
  maxpipesize bigint NOT NULL,
  minsumpcapacity numeric NOT NULL,
  maxsumpcapacity numeric NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_donation_header PRIMARY KEY (id),
  CONSTRAINT fk_donationhdr_category FOREIGN KEY (category) REFERENCES egwtr_category (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_donationhdr_maxpipesize FOREIGN KEY (maxpipesize) REFERENCES egwtr_pipesize (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_donationhdr_minpipesize FOREIGN KEY (minpipesize) REFERENCES egwtr_pipesize (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_donationhdr_usagetype FOREIGN KEY (usagetype) REFERENCES egwtr_usage_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_donationhdr_category ON egwtr_donation_header USING btree (category);
CREATE INDEX idx_donationhdr_maxpipesize ON egwtr_donation_header USING btree (maxpipesize);
CREATE INDEX idx_donationhdr_minpipesize ON egwtr_donation_header USING btree (minpipesize);
CREATE INDEX idx_donationhdr_usagetype ON egwtr_donation_header USING btree (usagetype);
CREATE SEQUENCE seq_egwtr_donation_header;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_donation_details
(
  id bigint NOT NULL,
  donationheader bigint NOT NULL,
  fromdate date NOT NULL,
  todate date,
  amount double precision NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_donationdetails PRIMARY KEY (id),
  CONSTRAINT fk_donationdetails_header FOREIGN KEY (donationheader) REFERENCES egwtr_donation_header (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_donationdetails_donationheader ON egwtr_donation_details USING btree (donationheader);
CREATE SEQUENCE seq_egwtr_donation_details;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_metercost
(
  id bigint NOT NULL,
  pipesize bigint NOT NULL,
  metermake character varying(50) NOT NULL,
  amount double precision NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_metercost PRIMARY KEY (id),
  CONSTRAINT fk_metercost_pipesize FOREIGN KEY (pipesize)  REFERENCES egwtr_pipesize (id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_metercost_pipesize ON egwtr_metercost USING btree (pipesize);
CREATE SEQUENCE seq_egwtr_metercost;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_property_category
(
  id bigint NOT NULL,
  categorytype bigint NOT NULL,
  propertytype bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_property_category_pkey PRIMARY KEY (id),
  CONSTRAINT fk_property_category_categoryid_fkey FOREIGN KEY (categorytype)  REFERENCES egwtr_category (id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_property_category_propertyid_fkey FOREIGN KEY (propertytype)  REFERENCES egwtr_property_type (id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egwtr_property_category;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_property_pipe_size
(
  id bigint NOT NULL,
  pipesize bigint NOT NULL,
  propertytype bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_property_pipe_size_pkey PRIMARY KEY (id),
  CONSTRAINT fk_property_pipe_propertyid_fkey FOREIGN KEY (propertytype) REFERENCES egwtr_property_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_property_pipe_size_fkey FOREIGN KEY (pipesize) REFERENCES egwtr_pipesize (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egwtr_property_pipesize;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_water_rates_header
(
  id bigint NOT NULL,
  connectiontype character varying(50) NOT NULL,
  usagetype bigint NOT NULL,
  watersource bigint NOT NULL,
  pipesize bigint NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_waterrates_header PRIMARY KEY (id),
  CONSTRAINT fk_waterrates_pipesize FOREIGN KEY (pipesize) REFERENCES egwtr_pipesize (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_waterrates_usagetype FOREIGN KEY (usagetype) REFERENCES egwtr_usage_type (id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_waterrates_watersource FOREIGN KEY (watersource)  REFERENCES egwtr_water_source (id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_water_rates_pipesize ON egwtr_water_rates_header USING btree (pipesize);
CREATE INDEX idx_water_rates_usagetype ON egwtr_water_rates_header USING btree (usagetype);
CREATE INDEX idx_water_rates_watersource ON egwtr_water_rates_header USING btree (watersource);
CREATE SEQUENCE seq_egwtr_water_rates_header;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_water_rates_details
(
  id bigint NOT NULL,
  waterratesheader bigint NOT NULL,
  startingunits numeric,
  endingunits numeric,
  unitrate double precision,
  minimumrate double precision,
  monthlyrate double precision,
  fromdate date NOT NULL,
  todate date,
  version bigint,
  CONSTRAINT pk_water_rates_details PRIMARY KEY (id),
  CONSTRAINT fk_waterratesdetails_header FOREIGN KEY (waterratesheader) REFERENCES egwtr_water_rates_header (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_waterratesdetails_header ON egwtr_water_rates_details USING btree (waterratesheader);
CREATE SEQUENCE seq_egwtr_water_rates_details;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_document_names
(
  id bigint NOT NULL,
  applicationtype bigint NOT NULL,
  documentname character varying(100) NOT NULL,
  description character varying(255),
  required boolean NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_document_names PRIMARY KEY (id),
  CONSTRAINT fk_documentnames_applicationtype FOREIGN KEY (applicationtype)  REFERENCES egwtr_application_type (id) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_documentnames_applicationtype ON egwtr_document_names USING btree (applicationtype);
CREATE SEQUENCE seq_egwtr_document_names;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_connectioncharges
(
  id bigint NOT NULL,
  type character varying(50) NOT NULL,
  description character varying(255),
  active boolean NOT NULL,
  fromdate date NOT NULL,
  todate date,
  amount double precision NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_connectioncharges PRIMARY KEY (id)
);
CREATE SEQUENCE seq_egwtr_connectioncharges;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_demandnotice_penalty_period
(
  id bigint NOT NULL,
  issueofdemandnotice character varying(15) NOT NULL,
  penaltyperiod numeric,
  min_con_holding_months numeric,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_demandnotice_penalty_period PRIMARY KEY (id)
);
CREATE SEQUENCE seq_egwtr_demandnotice_penalty_period;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_connection
(
  id bigint NOT NULL,
  consumercode character varying(50), -- Nullable, as the consumer code will be generated upon approval of application
  propertyidentifier character varying(50),
  bpaidentifier character varying(50),
  meterserialnumber character varying(50),
  meter bigint,
  parentconnection bigint, -- Applicable only for additional connection application
  initialreading bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_connection PRIMARY KEY (id),
  CONSTRAINT fk_connection_additional FOREIGN KEY (parentconnection) REFERENCES egwtr_connection (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_connection_meter FOREIGN KEY (meter) REFERENCES egwtr_metercost (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN egwtr_connection.consumercode IS 'Nullable, as the consumer code will be generated upon approval of application';
COMMENT ON COLUMN egwtr_connection.parentconnection IS 'Applicable only for additional connection application';
CREATE INDEX idx_connection_bpaid ON egwtr_connection USING btree (bpaidentifier COLLATE pg_catalog."default");
CREATE INDEX idx_connection_consumercode ON egwtr_connection USING btree (consumercode COLLATE pg_catalog."default");
CREATE INDEX idx_connection_meter ON egwtr_connection USING btree (meter);
CREATE INDEX idx_connection_parent ON egwtr_connection USING btree (parentconnection);
CREATE INDEX idx_connection_proptaxid ON egwtr_connection USING btree (propertyidentifier COLLATE pg_catalog."default");
CREATE SEQUENCE seq_egwtr_connection;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_connectiondetails
(
  id bigint NOT NULL,
  applicationtype bigint NOT NULL,
  connection bigint NOT NULL,
  applicationnumber character varying(50), -- Nullable, as this might not be available for migrated data. System generated ULBID+6digit running number
  applicationdate date,
  disposaldate date,
  connectiontype character varying(20) NOT NULL,
  category bigint NOT NULL,
  watersource bigint NOT NULL,
  usagetype bigint NOT NULL,
  propertytype bigint NOT NULL,
  pipesize bigint NOT NULL,
  sumpcapacity numeric,
  numberofperson integer,
  connectionstatus character varying(20) NOT NULL,
  state_id bigint,
  approvalnumber character varying(50),
  approvaldate date,
  demand bigint,
  connectionreason character varying(1024),
  bplcardholdername character varying(16),
  statusid bigint NOT NULL,
  workorderdate date,
  workordernumber character varying(12),
  filestoreid bigint,
  donationcharges double precision,
  executiondate date,
  legacy boolean,
  numberofrooms integer,
  chairperson bigint,
  closeConnectionReason character varying(1024),
  closeConnectionType character(1),
  reConnectionReason character varying(1024),
  closeApprovalDate date,
  reconnectionApprovalDate date,
  ishistory boolean,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_conndtl PRIMARY KEY (id),
  CONSTRAINT fk_conndtl_appltype FOREIGN KEY (applicationtype) REFERENCES egwtr_application_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_category FOREIGN KEY (category) REFERENCES egwtr_category (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_connection FOREIGN KEY (connection) REFERENCES egwtr_connection (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_demand FOREIGN KEY (demand) REFERENCES eg_demand (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_pipesize FOREIGN KEY (pipesize) REFERENCES egwtr_pipesize (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_propertytype FOREIGN KEY (propertytype) REFERENCES egwtr_property_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_source FOREIGN KEY (watersource) REFERENCES egwtr_water_source (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_state FOREIGN KEY (state_id) REFERENCES eg_wf_states (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtl_usage FOREIGN KEY (usagetype) REFERENCES egwtr_usage_type (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conndtls_status FOREIGN KEY (statusid) REFERENCES egw_status (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_connectiondetails_chairperson FOREIGN KEY (chairperson) REFERENCES eg_chairperson (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN egwtr_connectiondetails.applicationnumber IS 'Nullable, as this might not be available for migrated data. System generated ULBID+6digit running number';
CREATE INDEX idx_conndtl_appnumber ON egwtr_connectiondetails USING btree (applicationnumber COLLATE pg_catalog."default");
CREATE INDEX idx_conndtl_apptype ON egwtr_connectiondetails USING btree (applicationtype);
CREATE INDEX idx_conndtl_category ON egwtr_connectiondetails USING btree (category);
CREATE INDEX idx_conndtl_connection ON egwtr_connectiondetails USING btree (connection);
CREATE INDEX idx_conndtl_conntype ON egwtr_connectiondetails USING btree (connectiontype COLLATE pg_catalog."default");
CREATE INDEX idx_conndtl_constatus ON egwtr_connectiondetails USING btree (connectionstatus COLLATE pg_catalog."default");
CREATE INDEX idx_conndtl_pipesize ON egwtr_connectiondetails USING btree (pipesize);
CREATE INDEX idx_conndtl_proptype ON egwtr_connectiondetails USING btree (propertytype);
CREATE INDEX idx_conndtl_usagetype ON egwtr_connectiondetails USING btree (usagetype);
CREATE INDEX idx_conndtl_watersource ON egwtr_connectiondetails USING btree (watersource);
CREATE INDEX idx_connectiondetails_chairperson ON egwtr_connectiondetails USING btree (chairperson);
CREATE SEQUENCE seq_egwtr_connectiondetails;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_documents
(
  filestoreid bigint NOT NULL,
  applicationdocumentsid bigint NOT NULL
);

-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_estimation_details
(
  id bigint NOT NULL,
  connectiondetailsid bigint NOT NULL,
  itemdescription character varying(1024) NOT NULL,
  unitrate double precision,
  unitofmeasurement character varying(50),
  quantity double precision,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_estimationdetails PRIMARY KEY (id),
  CONSTRAINT fk_estimationdetails_connectiondetailsid FOREIGN KEY (connectiondetailsid) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_estimationdetails_connectiondetailsid ON egwtr_estimation_details USING btree (connectiondetailsid);
CREATE SEQUENCE seq_egwtr_estimation_details;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_existing_connection_details
(
  id bigint NOT NULL,
  monthlyfee double precision,
  connectiondetailsid bigint NOT NULL,
  donationcharges double precision,
  arrears double precision,
  metercost double precision,
  metername character varying(20),
  meterno character varying(20),
  previousreading bigint,
  currentreading bigint,
  readingdate date,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_existing_connection_details PRIMARY KEY (id),
  CONSTRAINT fk_existing_connection_details_details FOREIGN KEY (connectiondetailsid) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egwtr_existing_connection_details;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_fieldinspection_details
(
  id bigint NOT NULL,
  connectiondetailsid bigint NOT NULL,
  roadcategory bigint,
  existingpipeline character varying(50),
  pipelinedistance double precision,
  estimationcharges double precision NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_fieldinspectiondetails PRIMARY KEY (id),
  CONSTRAINT fk_fieldinspectiondetails_connectiondetailsid FOREIGN KEY (connectiondetailsid) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fieldinspectiondetails_roadcategory FOREIGN KEY (roadcategory) REFERENCES egwtr_road_category (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_fieldinspectiondetails_connectiondetailsid ON egwtr_fieldinspection_details USING btree (connectiondetailsid);
CREATE INDEX idx_fieldinspectiondetails_roadcategory ON egwtr_fieldinspection_details USING btree (roadcategory);
CREATE SEQUENCE seq_egwtr_fieldinspection_details;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_meter_connection_details
(
  id bigint NOT NULL,
  connectiondetailsid bigint NOT NULL,
  currentreading bigint,
  currentreadingdate date,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_meter_connection_details PRIMARY KEY (id),
  CONSTRAINT fk_meter_connection_details FOREIGN KEY (connectiondetailsid) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_meterentrydetails_connectiondetailsid ON egwtr_meter_connection_details USING btree (connectiondetailsid);
--CREATE INDEX idx_fieldinspectiondetails_roadcategory ON egwtr_fieldinspection_details USING btree (roadcategory);
CREATE SEQUENCE seq_egwtr_meter_connection_details;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_nonmetered_billdetails
(
  id bigint NOT NULL,
  connectiondetailsid bigint NOT NULL,
  billno character varying(20),
  installmentid bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version numeric DEFAULT 0,
  CONSTRAINT pk_nonmetered_billdetails PRIMARY KEY (id),
  CONSTRAINT fk_eg_installment_master_id FOREIGN KEY (installmentid) REFERENCES eg_installment_master (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_nonmetered_bill_connectiondetails FOREIGN KEY (connectiondetailsid) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egwtr_nonmetered_billdetails;
-------------------END-------------------
------------------START------------------
CREATE TABLE egwtr_application_documents
(
  id bigint NOT NULL,
  connectiondetailsid bigint NOT NULL,
  documentnamesid bigint NOT NULL,
  documentnumber character varying(50) NOT NULL,
  documentdate date NOT NULL,
  description character varying(255),
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_application_documents PRIMARY KEY (id),
  CONSTRAINT fk_applicationdocs_connectiondetailsid FOREIGN KEY (connectiondetailsid) REFERENCES egwtr_connectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_applicationdocs_documentnamesid FOREIGN KEY (documentnamesid) REFERENCES egwtr_document_names (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_applicationdocs_connectiondetailsid ON egwtr_application_documents USING btree (connectiondetailsid);
CREATE INDEX idx_applicationdocs_documentnamesid ON egwtr_application_documents USING btree (documentnamesid);
CREATE SEQUENCE seq_egwtr_application_documents;
-------------------END-------------------
------------------START------------------
CREATE VIEW EGWTR_MV_DCB_VIEW AS (
                                    (SELECT bp.propertyid,
                                            bpaddr.address AS address,
                                            con.consumercode AS hscno,
                                            ownername(bp.id) AS username,
                                            propid.zone_num AS zoneid,
                                            propid.WARD_ADM_ID AS wardid,
                                            propid.ADM1 AS block,
                                            propid.ADM2 AS locality,
                                            propid.ADM3 AS street,
                                            cd.connectiontype AS connectiontype,
                                            currdd.amount AS curr_demand,
                                            currdd.amt_collected AS curr_coll,
                                            currdd.amount -currdd.amt_collected AS curr_balance,
                                            coalesce(0,'0') AS arr_demand,
                                            coalesce(0,'0') AS arr_coll,
                                            coalesce(0,'0') AS arr_balance
                                     FROM egwtr_connection con
                                     INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                                     INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
                                     INNER JOIN eg_demand currdmd ON currdmd.id = cd.demand
                                     INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
                                     INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
                                     INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
                                     LEFT JOIN eg_demand_details currdd ON currdd.id_demand =currdmd.id
                                     WHERE cd.connectionstatus = 'ACTIVE'
                                       AND currdd.id_demand_reason IN
                                         (SELECT id
                                          FROM eg_demand_reason
                                          WHERE id_demand_reason_master =
                                              (SELECT id
                                               FROM eg_demand_reason_master
                                               WHERE code='WTAXCHARGES'
                                                 AND isdemand=TRUE)
                                            AND id_installment=
                                              (SELECT id
                                               FROM eg_installment_master
                                               WHERE eg_installment_master.start_date <= now()
                                                 AND eg_installment_master.end_date >= now()
                                                 AND eg_installment_master.id_module = (
                                                                                          (SELECT eg_module.id
                                                                                           FROM eg_module
                                                                                           WHERE eg_module.name::text = 'Water Tax Management'::text))
                                                 AND eg_installment_master.installment_type='Monthly'))
                                       AND cd.connectiontype='METERED')
                                  UNION
                                    (SELECT bp.propertyid,
                                            bpaddr.address AS address,
                                            con.consumercode AS hscno,
                                            ownername(bp.id) AS username,
                                            propid.zone_num AS zoneid,
                                            propid.WARD_ADM_ID AS wardid,
                                            propid.ADM1 AS block,
                                            propid.ADM2 AS locality,
                                            propid.ADM3 AS street,
                                            cd.connectiontype AS connectiontype,
                                            coalesce(0,'0') AS curr_demand,
                                            coalesce(0,'0') AS curr_coll,
                                            coalesce(0,'0') AS curr_balance,
                                            coalesce(arrdd.amount,'0') AS arr_demand,
                                            coalesce(arrdd.amt_collected,'0') AS arr_coll,
                                            coalesce(arrdd.amount -arrdd.amt_collected,'0') AS arr_balance
                                     FROM egwtr_connection con
                                     INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                                     INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
                                     INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
                                     INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
                                     INNER JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
                                     INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
                                     INNER JOIN eg_demand_details arrdd ON arrdd.id_demand =arrdmd.id
                                     WHERE cd.connectionstatus = 'ACTIVE'
                                       AND arrdd.id_demand_reason IN
                                         (SELECT id
                                          FROM eg_demand_reason
                                          WHERE id_demand_reason_master =
                                              (SELECT id
                                               FROM eg_demand_reason_master
                                               WHERE code='WTAXCHARGES')
                                            AND id_installment NOT IN
                                              (SELECT id
                                               FROM eg_installment_master
                                               WHERE eg_installment_master.start_date <= now()
                                                 AND eg_installment_master.end_date >= now()
                                                 AND eg_installment_master.id_module IN
                                                   (SELECT eg_module.id
                                                    FROM eg_module
                                                    WHERE eg_module.name::text IN('Water Tax Management'::text,
                                                                                  'Property Tax'::text))))
                                       AND cd.connectiontype='METERED'))
UNION (
         (SELECT bp.propertyid,
                 bpaddr.address AS address,
                 con.consumercode AS hscno,
                 ownername(bp.id) AS username,
                 propid.zone_num AS zoneid,
                 propid.WARD_ADM_ID AS wardid,
                 propid.ADM1 AS block,
                 propid.ADM2 AS locality,
                 propid.ADM3 AS street,
                 cd.connectiontype AS connectiontype,
                 coalesce(currdd.amount,'0') AS curr_demand,
                 coalesce(currdd.amt_collected,'0') AS curr_coll,
                 coalesce(currdd.amount -currdd.amt_collected,'0') AS curr_balance,
                 coalesce(0,'0') AS arr_demand,
                 coalesce(0,'0') AS arr_coll,
                 coalesce(0,'0') AS arr_balance
          FROM egwtr_connection con
          INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
          INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
          INNER JOIN eg_demand currdmd ON currdmd.id = cd.demand
          INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
          INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
          INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
          LEFT JOIN eg_demand_details currdd ON currdd.id_demand =currdmd.id
          WHERE cd.connectionstatus = 'ACTIVE'
            AND currdd.id_demand_reason IN
              (SELECT id
               FROM eg_demand_reason
               WHERE id_demand_reason_master =
                   (SELECT id
                    FROM eg_demand_reason_master
                    WHERE code='WTAXCHARGES'
                      AND isdemand=TRUE)
                 AND id_installment=
                   (SELECT id
                    FROM eg_installment_master
                    WHERE eg_installment_master.start_date <= now()
                      AND eg_installment_master.end_date >= now()
                      AND eg_installment_master.id_module = (
                                                               (SELECT eg_module.id
                                                                FROM eg_module
                                                                WHERE eg_module.name::text = 'Property Tax'::text))))
            AND cd.connectiontype='NON_METERED')
       UNION
         (SELECT bp.propertyid,
                 bpaddr.address AS address,
                 con.consumercode AS hscno,
                 ownername(bp.id) AS username,
                 propid.zone_num AS zoneid,
                 propid.WARD_ADM_ID AS wardid,
                 propid.ADM1 AS block,
                 propid.ADM2 AS locality,
                 propid.ADM3 AS street,
                 cd.connectiontype AS connectiontype,
                 coalesce(0,'0') AS curr_demand,
                 coalesce(0,'0') AS curr_coll,
                 coalesce(0,'0') AS curr_balance,
                 coalesce(arrdd.amount,'0') AS arr_demand,
                 coalesce(arrdd.amt_collected,'0') AS arr_coll,
                 coalesce(arrdd.amount -arrdd.amt_collected,'0') AS arr_balance
          FROM egwtr_connection con
          INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
          INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
          INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
          INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
          INNER JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
          INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
          INNER JOIN eg_demand_details arrdd ON arrdd.id_demand =arrdmd.id
          WHERE cd.connectionstatus = 'ACTIVE'
            AND arrdd.id_demand_reason IN
              (SELECT id
               FROM eg_demand_reason
               WHERE id_demand_reason_master =
                   (SELECT id
                    FROM eg_demand_reason_master
                    WHERE code='WTAXCHARGES')
                 AND id_installment NOT IN
                   (SELECT id
                    FROM eg_installment_master
                    WHERE eg_installment_master.start_date <= now()
                      AND eg_installment_master.end_date >= now()
                      AND eg_installment_master.id_module IN
                        (SELECT eg_module.id
                         FROM eg_module
                         WHERE eg_module.name::text IN('Property Tax'::text))))
            AND cd.connectiontype='NON_METERED'));
-------------------END-------------------
  

