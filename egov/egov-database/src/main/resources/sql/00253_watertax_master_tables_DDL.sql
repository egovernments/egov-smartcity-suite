DROP SEQUENCE seq_egwtr_demandnotice_penalty_period;
DROP SEQUENCE seq_egwtr_connectioncharges_details;
DROP SEQUENCE seq_egwtr_connectioncharges_header;
DROP SEQUENCE seq_egwtr_water_rates_details;
DROP SEQUENCE seq_egwtr_water_rates_header;
DROP SEQUENCE seq_egwtr_securitydeposit_details;
DROP SEQUENCE seq_egwtr_securitydeposit_header;
DROP SEQUENCE seq_egwtr_water_source;
DROP SEQUENCE seq_egwtr_property_type;
DROP SEQUENCE seq_egwtr_penalty_details;
DROP SEQUENCE seq_egwtr_penalty_header;
DROP SEQUENCE seq_egwtr_meter_cost;
DROP SEQUENCE seq_egwtr_document_names;
DROP SEQUENCE seq_egwtr_donation_details;
DROP SEQUENCE seq_egwtr_donation_header;
DROP SEQUENCE seq_egwtr_pipe_size;
DROP SEQUENCE seq_egwtr_application_process_time;
DROP SEQUENCE seq_egwtr_category;
DROP SEQUENCE seq_egwtr_usage_type;
DROP SEQUENCE seq_egwtr_application_type;


DROP TABLE egwtr_demandnotice_penalty_period;
DROP TABLE egwtr_connectioncharges_details; 
DROP TABLE egwtr_connectioncharges_header;
DROP TABLE egwtr_water_rates_details;
DROP TABLE egwtr_water_rates_header;
DROP TABLE egwtr_securitydeposit_details;
DROP TABLE egwtr_securitydeposit_header;
DROP TABLE egwtr_water_source;
DROP TABLE egwtr_property_type;
DROP TABLE egwtr_penalty_details;
DROP TABLE egwtr_penalty_header;
DROP TABLE egwtr_meter_cost;
DROP TABLE egwtr_document_names;
DROP TABLE egwtr_donation_details;
DROP TABLE egwtr_donation_header;
DROP TABLE egwtr_pipe_size;
DROP TABLE egwtr_application_process_time;
DROP TABLE egwtr_category;
DROP TABLE egwtr_usage_type;
DROP TABLE egwtr_application_type;

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
	CONSTRAINT unq_application_name UNIQUE (name),
	CONSTRAINT fk_application_type_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_application_type_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_application_type;


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
	CONSTRAINT unq_usage_name UNIQUE (name),
	CONSTRAINT fk_usage_type_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_usage_type_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_usage_type;

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
	CONSTRAINT unq_category_name UNIQUE (name),
	CONSTRAINT fk_category_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_category_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_category;


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
	CONSTRAINT unq_applicationprocesstime_applicationtype_category UNIQUE (applicationtype, category),
	CONSTRAINT fk_applicationprocesstime_applicationtype FOREIGN KEY (applicationtype)
      		REFERENCES egwtr_application_type (id),
	CONSTRAINT fk_applicationprocesstime_category FOREIGN KEY (category)
      		REFERENCES egwtr_category (id),
	CONSTRAINT fk_application_process_time_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_application_process_time_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_application_process_time;
CREATE INDEX idx_applicationprocesstime_applicationtype ON egwtr_application_process_time USING btree (applicationtype);
CREATE INDEX idx_applicationprocesstime_category ON egwtr_application_process_time USING btree (category);


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
	CONSTRAINT unq_pipesize_code UNIQUE (code),
	CONSTRAINT fk_pipesize_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_pipesize_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_pipesize;


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
	CONSTRAINT fk_donationhdr_category FOREIGN KEY (category)
      		REFERENCES egwtr_category (id),
	CONSTRAINT fk_donationhdr_usagetype FOREIGN KEY (usagetype)
      		REFERENCES egwtr_usage_type (id),
	CONSTRAINT fk_donationhdr_minpipesize FOREIGN KEY (minpipesize)
      		REFERENCES egwtr_pipesize (id),
	CONSTRAINT fk_donationhdr_maxpipesize FOREIGN KEY (maxpipesize)
      		REFERENCES egwtr_pipesize (id),
	CONSTRAINT fk_donationhdr_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_donationhdr_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE INDEX idx_donationhdr_category ON egwtr_donation_header USING btree (category);
CREATE INDEX idx_donationhdr_usagetype ON egwtr_donation_header USING btree (usagetype);
CREATE INDEX idx_donationhdr_minpipesize ON egwtr_donation_header USING btree (minpipesize);
CREATE INDEX idx_donationhdr_maxpipesize ON egwtr_donation_header USING btree (maxpipesize);

CREATE SEQUENCE seq_egwtr_donation_header;


CREATE TABLE egwtr_donation_details
(
	id bigint NOT NULL,
	donationheader bigint NOT NULL,
	fromdate date NOT NULL,
	todate date,
	amount double precision NOT NULL,
	CONSTRAINT pk_donationdetails PRIMARY KEY (id),
	CONSTRAINT fk_donationdetails_header FOREIGN KEY (donationheader)
      		REFERENCES egwtr_donation_header (id)
);

CREATE SEQUENCE seq_egwtr_donation_details;

CREATE INDEX idx_donationdetails_donationheader ON egwtr_donation_details USING btree (donationheader);


CREATE TABLE egwtr_document_names
(
	id bigint NOT NULL,
	applicationtype bigint NOT NULL,
	documentname character varying(50) NOT NULL,
	description character varying(255),
	required boolean NOT NULL,
	active boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version numeric DEFAULT 0,
	CONSTRAINT pk_document_names PRIMARY KEY (id),
	CONSTRAINT fk_documentnames_applicationtype FOREIGN KEY (applicationtype)
      		REFERENCES egwtr_application_type (id),
	CONSTRAINT fk_document_names_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_document_names_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_document_names;
CREATE INDEX idx_documentnames_applicationtype ON egwtr_document_names USING btree (applicationtype);

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
	CONSTRAINT fk_metercost_pipesize FOREIGN KEY (pipesize)
      		REFERENCES egwtr_pipesize (id),
	CONSTRAINT fk_metercost_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_metercost_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE INDEX idx_metercost_pipesize ON egwtr_metercost USING btree (pipesize);
CREATE SEQUENCE seq_egwtr_metercost;


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
	CONSTRAINT pk_penalty PRIMARY KEY (id),
	CONSTRAINT fk_penalty_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_penalty_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_penalty;


CREATE TABLE egwtr_property_type
(
	id bigint NOT NULL,
	code character varying(25) NOT NULL,
	name character varying(50) NOT NULL,
	connectioneligibility char(1) NOT NULL,
	active boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version numeric DEFAULT 0,
	CONSTRAINT pk_property_type PRIMARY KEY (id),
	CONSTRAINT unq_propertytype_code UNIQUE (code),
	CONSTRAINT unq_propertytype_name UNIQUE (name),
	CONSTRAINT fk_propertytype_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_propertytype_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_property_type;

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
	CONSTRAINT unq_watersource_watersourcetype UNIQUE (watersourcetype),
	CONSTRAINT fk_watersource_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_watersource_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_water_source;



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
	CONSTRAINT fk_securitydeposit_usagetype FOREIGN KEY (usagetype)
      		REFERENCES egwtr_usage_type (id),
	CONSTRAINT fk_securitydeposit_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_securitydeposit_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE INDEX idx_securitydeposit_usagetype ON egwtr_securitydeposit USING btree (usagetype);
CREATE SEQUENCE seq_egwtr_securitydeposit;


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
	CONSTRAINT fk_waterrates_pipesize FOREIGN KEY (pipesize)
      		REFERENCES egwtr_pipesize (id),	
	CONSTRAINT fk_waterrates_usagetype FOREIGN KEY (usagetype)
      		REFERENCES egwtr_usage_type (id),
	CONSTRAINT fk_waterrates_watersource FOREIGN KEY (watersource)
      		REFERENCES egwtr_water_source (id),
	CONSTRAINT fk_waterrates_header_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_waterrates_header_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_water_rates_header;
CREATE INDEX idx_water_rates_usagetype ON egwtr_water_rates_header USING btree (usagetype);
CREATE INDEX idx_water_rates_pipesize ON egwtr_water_rates_header USING btree (pipesize);
CREATE INDEX idx_water_rates_watersource ON egwtr_water_rates_header USING btree (watersource);


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
	CONSTRAINT pk_water_rates_details PRIMARY KEY (id),
	CONSTRAINT fk_waterratesdetails_header FOREIGN KEY (waterratesheader)
      		REFERENCES egwtr_water_rates_header (id)
);

CREATE SEQUENCE seq_egwtr_water_rates_details;
CREATE INDEX idx_waterratesdetails_header ON egwtr_water_rates_details USING btree (waterratesheader);



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
	CONSTRAINT pk_connectioncharges PRIMARY KEY (id),
	CONSTRAINT fk_connectioncharges_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_connectionchargesr_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_connectioncharges;


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
	CONSTRAINT pk_demandnotice_penalty_period PRIMARY KEY (id),
	CONSTRAINT fk_demandnotice_penalty_period_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_demandnotice_penalty_period_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_demandnotice_penalty_period;


--rollback DROP SEQUENCE seq_egwtr_demandnotice_penalty_period;
--rollback DROP SEQUENCE seq_egwtr_connectioncharges;
--rollback DROP SEQUENCE seq_egwtr_water_rates_details;
--rollback DROP SEQUENCE seq_egwtr_water_rates_header;
--rollback DROP SEQUENCE seq_egwtr_securitydeposit;
--rollback DROP SEQUENCE seq_egwtr_water_source;
--rollback DROP SEQUENCE seq_egwtr_property_type;
--rollback DROP SEQUENCE seq_egwtr_penalty;
--rollback DROP SEQUENCE seq_egwtr_metercost;
--rollback DROP SEQUENCE seq_egwtr_document_names;
--rollback DROP SEQUENCE seq_egwtr_donation_details;
--rollback DROP SEQUENCE seq_egwtr_donation_header;
--rollback DROP SEQUENCE seq_egwtr_pipesize;
--rollback DROP SEQUENCE seq_egwtr_application_process_time;
--rollback DROP SEQUENCE seq_egwtr_category;
--rollback DROP SEQUENCE seq_egwtr_usage_type;
--rollback DROP SEQUENCE seq_egwtr_application_type;


--rollback DROP TABLE egwtr_demandnotice_penalty_period;
--rollback DROP TABLE egwtr_connectioncharges;
--rollback DROP TABLE egwtr_water_rates_details;
--rollback DROP TABLE egwtr_water_rates_header;
--rollback DROP TABLE egwtr_securitydeposit;
--rollback DROP TABLE egwtr_water_source;
--rollback DROP TABLE egwtr_property_type;
--rollback DROP TABLE egwtr_penalty;
--rollback DROP TABLE egwtr_metercost;
--rollback DROP TABLE egwtr_document_names;
--rollback DROP TABLE egwtr_donation_details;
--rollback DROP TABLE egwtr_donation_header;
--rollback DROP TABLE egwtr_pipesize;
--rollback DROP TABLE egwtr_application_process_time;
--rollback DROP TABLE egwtr_category;
--rollback DROP TABLE egwtr_usage_type;
--rollback DROP TABLE egwtr_application_type;


