CREATE TABLE egwtr_application_type
(
	id bigint NOT NULL,
	code character varying(25) NOT NULL,
	name character varying(50) NOT NULL,
	description character varying(255),
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
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
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
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
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
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
	application_type_id bigint NOT NULL,
	category_id bigint NOT NULL,
	processing_time numeric NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_application_process_time PRIMARY KEY (id),
	CONSTRAINT unq_application_type_category UNIQUE (application_type_id, category_id),
	CONSTRAINT fk_applicationprocesstime_applicationtypeid FOREIGN KEY (application_type_id)
      		REFERENCES egwtr_application_type (id),
	CONSTRAINT fk_applicationprocesstime_categoryid FOREIGN KEY (category_id)
      		REFERENCES egwtr_category (id),
	CONSTRAINT fk_application_process_time_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_application_process_time_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_application_process_time;
CREATE INDEX idx_applicationprocesstime_applicationtypeid ON egwtr_application_process_time USING btree (application_type_id);
CREATE INDEX idx_applicationprocesstime_categoryid ON egwtr_application_process_time USING btree (category_id);

CREATE TABLE egwtr_pipe_size
(
	id bigint NOT NULL,
	code character varying(25),
	size_inches double precision,
	size_mm double precision,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_pipe_size PRIMARY KEY (id),
	CONSTRAINT unq_pipe_size_code UNIQUE (code),
	CONSTRAINT fk_pipe_size_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_pipe_size_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_pipe_size;


CREATE TABLE egwtr_donation_header
(
	id bigint NOT NULL,
	category_id bigint NOT NULL,
	usage_type_id bigint NOT NULL,
	min_pipe_size_id bigint NOT NULL,
	max_pipe_size_id bigint NOT NULL,
	min_sump_capacity numeric NOT NULL,
	max_sump_capacity numeric NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_donation_header PRIMARY KEY (id),
	CONSTRAINT fk_donationhdr_categoryid FOREIGN KEY (category_id)
      		REFERENCES egwtr_category (id),
	CONSTRAINT fk_donationhdr_usagetypeid FOREIGN KEY (usage_type_id)
      		REFERENCES egwtr_usage_type (id),
	CONSTRAINT fk_donationhdr_min_pipe_size_id FOREIGN KEY (min_pipe_size_id)
      		REFERENCES egwtr_pipe_size (id),
	CONSTRAINT fk_donationhdr_max_pipe_size_id FOREIGN KEY (max_pipe_size_id)
      		REFERENCES egwtr_pipe_size (id),
	CONSTRAINT fk_donationhdr_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_donationhdr_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE INDEX idx_donationhdr_categoryid ON egwtr_donation_header USING btree (category_id);
CREATE INDEX idx_donationhdr_usagetypeid ON egwtr_donation_header USING btree (usage_type_id);
CREATE INDEX idx_donationhdr_minpipesize ON egwtr_donation_header USING btree (min_pipe_size_id);
CREATE INDEX idx_donationhdr_maxpipesize ON egwtr_donation_header USING btree (max_pipe_size_id);

CREATE SEQUENCE seq_egwtr_donation_header;


CREATE TABLE egwtr_donation_details
(
	id bigint NOT NULL,
	donation_header_id bigint NOT NULL,
	from_date date NOT NULL,
	to_date date,
	amount double precision NOT NULL,
	CONSTRAINT pk_donation_details PRIMARY KEY (id),
	CONSTRAINT fk_donation_header_details FOREIGN KEY (donation_header_id)
      		REFERENCES egwtr_donation_header (id)
);

CREATE SEQUENCE seq_egwtr_donation_details;

CREATE INDEX idx_donation_header_id ON egwtr_donation_details USING btree (donation_header_id);


CREATE TABLE egwtr_document_names
(
	id bigint NOT NULL,
	application_type_id bigint NOT NULL,
	document_name character varying(50) NOT NULL,
	description character varying(255),
	isrequired boolean NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_document_names PRIMARY KEY (id),
	CONSTRAINT fk_documentnames_applicationtypeid FOREIGN KEY (application_type_id)
      		REFERENCES egwtr_application_type (id),
	CONSTRAINT fk_document_names_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_document_names_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_document_names;
CREATE INDEX idx_documentnames_applicationtypeid ON egwtr_document_names USING btree (application_type_id);

CREATE TABLE egwtr_meter_cost
(
	id bigint NOT NULL,
	pipe_size_id bigint NOT NULL,
	meter_make character varying(50) NOT NULL,
	cost double precision NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_meter_cost PRIMARY KEY (id),
	CONSTRAINT fk_meter_cost_pipe_size_id FOREIGN KEY (pipe_size_id)
      		REFERENCES egwtr_pipe_size (id),
	CONSTRAINT fk_meter_cost_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_meter_cost_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE INDEX idx_metercost_pipesizeid ON egwtr_meter_cost USING btree (pipe_size_id);
CREATE SEQUENCE seq_egwtr_meter_cost;


CREATE TABLE egwtr_penalty_header
(
	id bigint NOT NULL,
	penalty_type character varying(50) NOT NULL,
	description character varying(255),
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_penalty_header PRIMARY KEY (id),
	CONSTRAINT unq_penalty_type UNIQUE (penalty_type),
	CONSTRAINT fk_penalty_header_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_penalty_header_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_penalty_header;


CREATE TABLE egwtr_penalty_details
(
	id bigint NOT NULL,
	penalty_header_id bigint NOT NULL,
	from_date date NOT NULL,
	to_date date,
	percentage double precision NOT NULL,
	CONSTRAINT pk_penalty_details PRIMARY KEY (id),
	CONSTRAINT fk_penalty_header_details FOREIGN KEY (penalty_header_id)
      		REFERENCES egwtr_penalty_header (id)
);

CREATE SEQUENCE seq_egwtr_penalty_details;

CREATE INDEX idx_penaltydtls_header_id ON egwtr_penalty_details USING btree (penalty_header_id);


CREATE TABLE egwtr_property_type
(
	id bigint NOT NULL,
	code character varying(25) NOT NULL,
	name character varying(50) NOT NULL,
	connection_eligibility char(1) NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_property_type PRIMARY KEY (id),
	CONSTRAINT unq_property_code UNIQUE (code),
	CONSTRAINT unq_property_name UNIQUE (name),
	CONSTRAINT fk_property_type_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_property_type_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_property_type;

CREATE TABLE egwtr_water_source
(
	id bigint NOT NULL,
	code character varying(25) NOT NULL,
	type character varying(50) NOT NULL,
	description character varying(255) NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_water_source PRIMARY KEY (id),
	CONSTRAINT unq_water_source_code UNIQUE (code),
	CONSTRAINT unq_water_source_type UNIQUE (type),
	CONSTRAINT fk_water_source_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_water_source_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_water_source;



CREATE TABLE egwtr_securitydeposit_header
(
	id bigint NOT NULL,
	usage_type_id bigint NOT NULL,
	no_of_months numeric NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_securitydeposit_header PRIMARY KEY (id),
	CONSTRAINT unq_usage_type_id UNIQUE (usage_type_id),
	CONSTRAINT fk_securitydeposit_header_usagetypeid FOREIGN KEY (usage_type_id)
      		REFERENCES egwtr_usage_type (id),
	CONSTRAINT fk_securitydeposit_header_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_securitydeposit_header_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE INDEX idx_securitydeposit_header_usagetypeid ON egwtr_securitydeposit_header USING btree (usage_type_id);
CREATE SEQUENCE seq_egwtr_securitydeposit_header;


CREATE TABLE egwtr_securitydeposit_details
(
	id bigint NOT NULL,
	securitydeposit_header_id bigint NOT NULL,
	from_date date NOT NULL,
	to_date date,
	amount double precision NOT NULL,
	CONSTRAINT pk_securitydeposit_details PRIMARY KEY (id),
	CONSTRAINT fk_securitydeposit_header_details FOREIGN KEY (securitydeposit_header_id)
      		REFERENCES egwtr_securitydeposit_header (id)
);

CREATE SEQUENCE seq_egwtr_securitydeposit_details;
CREATE INDEX idx_securitydeposit_header_id ON egwtr_securitydeposit_details USING btree (securitydeposit_header_id);



CREATE TABLE egwtr_water_rates_header
(
	id bigint NOT NULL,
	connection_type character varying(50) NOT NULL,
	usage_type_id bigint NOT NULL,
	water_source_id bigint NOT NULL,
	pipe_size_id bigint NOT NULL,
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_water_rates_header PRIMARY KEY (id),
	CONSTRAINT fk_water_rates_source_id FOREIGN KEY (pipe_size_id)
      		REFERENCES egwtr_pipe_size (id),	
	CONSTRAINT fk_water_rates_usagetypeid FOREIGN KEY (usage_type_id)
      		REFERENCES egwtr_usage_type (id),
	CONSTRAINT fk_water_rates_pipe_size_id FOREIGN KEY (water_source_id)
      		REFERENCES egwtr_water_source (id),
	CONSTRAINT fk_water_rates_header_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_water_rates_header_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_water_rates_header;
CREATE INDEX idx_water_rates_usagetypeid ON egwtr_water_rates_header USING btree (usage_type_id);
CREATE INDEX idx_water_rates_pipesizeid ON egwtr_water_rates_header USING btree (pipe_size_id);
CREATE INDEX idx_water_rates_watersourceid ON egwtr_water_rates_header USING btree (water_source_id);


CREATE TABLE egwtr_water_rates_details
(
	id bigint NOT NULL,
	water_rates_header_id bigint NOT NULL,
	starting_units numeric,
 	ending_units numeric,
	quantity numeric,
	unit_rate double precision,
	min_rate double precision,
	rate_per_month double precision,
	from_date date NOT NULL,
	to_date date,
	CONSTRAINT pk_water_rates_details PRIMARY KEY (id),
	CONSTRAINT fk_water_rates_header_details FOREIGN KEY (water_rates_header_id)
      		REFERENCES egwtr_water_rates_header (id)
);

CREATE SEQUENCE seq_egwtr_water_rates_details;
CREATE INDEX idx_water_rates_header_id ON egwtr_water_rates_details USING btree (water_rates_header_id);



CREATE TABLE egwtr_connectioncharges_header
(
	id bigint NOT NULL,
	type character varying(50) NOT NULL,
	description character varying(255),
	isactive boolean NOT NULL,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_conctncharges_header PRIMARY KEY (id),
	CONSTRAINT unq_type UNIQUE (type),
	CONSTRAINT fk_conctncharges_header_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_conctncharges_header_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_connectioncharges_header;


CREATE TABLE egwtr_connectioncharges_details
(
	id bigint NOT NULL,
	connectioncharges_header_id bigint NOT NULL,
	from_date date NOT NULL,
	to_date date,
	amount double precision NOT NULL,
	CONSTRAINT pk_conctncharges_details PRIMARY KEY (id),
	CONSTRAINT fk_conctncharges_header_details FOREIGN KEY (connectioncharges_header_id)
      		REFERENCES egwtr_connectioncharges_header (id)
);

CREATE SEQUENCE seq_egwtr_connectioncharges_details;

CREATE INDEX idx_conctndetails_chargesheader_id ON egwtr_connectioncharges_details USING btree (connectioncharges_header_id);


CREATE TABLE egwtr_demandnotice_penalty_period
(
	id bigint NOT NULL,
	demandnotice_issue character varying(15) NOT NULL,
	penalty_days numeric,
	min_con_holding_months numeric,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_demandnotice_penalty_period PRIMARY KEY (id),
	CONSTRAINT fk_demandnotice_penalty_period_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_demandnotice_penalty_period_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egwtr_demandnotice_penalty_period;


--rollback DROP SEQUENCE seq_egwtr_demandnotice_penalty_period;
--rollback DROP SEQUENCE seq_egwtr_connectioncharges_details;
--rollback DROP SEQUENCE seq_egwtr_connectioncharges_header;
--rollback DROP SEQUENCE seq_egwtr_water_rates_details;
--rollback DROP SEQUENCE seq_egwtr_water_rates_header;
--rollback DROP SEQUENCE seq_egwtr_securitydeposit_details;
--rollback DROP SEQUENCE seq_egwtr_securitydeposit_header;
--rollback DROP SEQUENCE seq_egwtr_water_source;
--rollback DROP SEQUENCE seq_egwtr_property_type;
--rollback DROP SEQUENCE seq_egwtr_penalty_details;
--rollback DROP SEQUENCE seq_egwtr_penalty_header;
--rollback DROP SEQUENCE seq_egwtr_meter_cost;
--rollback DROP SEQUENCE seq_egwtr_document_names;
--rollback DROP SEQUENCE seq_egwtr_donation_details;
--rollback DROP SEQUENCE seq_egwtr_donation_header;
--rollback DROP SEQUENCE seq_egwtr_pipe_size;
--rollback DROP SEQUENCE seq_egwtr_application_process_time;
--rollback DROP SEQUENCE seq_egwtr_category;
--rollback DROP SEQUENCE seq_egwtr_usage_type;
--rollback DROP SEQUENCE seq_egwtr_application_type;


--rollback DROP TABLE egwtr_demandnotice_penalty_period;
--rollback DROP TABLE egwtr_connectioncharges_details; 
--rollback DROP TABLE egwtr_connectioncharges_header;
--rollback DROP TABLE egwtr_water_rates_details;
--rollback DROP TABLE egwtr_water_rates_header;
--rollback DROP TABLE egwtr_securitydeposit_details;
--rollback DROP TABLE egwtr_securitydeposit_header;
--rollback DROP TABLE egwtr_water_source;
--rollback DROP TABLE egwtr_property_type;
--rollback DROP TABLE egwtr_penalty_details;
--rollback DROP TABLE egwtr_penalty_header;
--rollback DROP TABLE egwtr_meter_cost;
--rollback DROP TABLE egwtr_document_names;
--rollback DROP TABLE egwtr_donation_details;
--rollback DROP TABLE egwtr_donation_header;
--rollback DROP TABLE egwtr_pipe_size;
--rollback DROP TABLE egwtr_application_process_time;
--rollback DROP TABLE egwtr_category;
--rollback DROP TABLE egwtr_usage_type;
--rollback DROP TABLE egwtr_application_type;


