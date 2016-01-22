CREATE TABLE EGW_LINEESTIMATE	
(
  id bigint NOT NULL,
  lineestimatenumber varchar(50) NOT NULL,
  subject varchar(256) NOT NULL,
  fund bigint NOT NULL,
  reference varchar(1024) NOT NULL,
  function bigint NOT NULL,
  description varchar(1024) NOT NULL,
  budgethead bigint NOT NULL,
  scheme bigint,
  subscheme bigint,
  lineestimatedate timestamp without time zone NOT NULL, 
  executingdepartment bigint NOT NULL,
  status bigint NOT NULL,
  state_id bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint DEFAULT 0,
  CONSTRAINT pk_lineestimate PRIMARY KEY (id),
  CONSTRAINT fk_lineestimate_fund FOREIGN KEY (fund) REFERENCES fund (id),
  CONSTRAINT fk_lineestimate_function FOREIGN KEY (function) REFERENCES function (id),
  CONSTRAINT fk_lineestimate_budgethead FOREIGN KEY (budgethead) REFERENCES egf_budgetgroup (id), 		
  CONSTRAINT fk_lineestimate_scheme FOREIGN KEY (scheme) REFERENCES scheme (id),
  CONSTRAINT fk_lineestimate_subscheme FOREIGN KEY (subscheme) REFERENCES sub_scheme (id),
  CONSTRAINT fk_lineestimate_execdept FOREIGN KEY (executingdepartment) REFERENCES eg_department (id),
  CONSTRAINT fk_lineestimate_state FOREIGN KEY (state_id) REFERENCES eg_wf_states (id),
  CONSTRAINT fk_lineestimate_status FOREIGN KEY (status) REFERENCES egw_status (id), 
  CONSTRAINT fk_lineestimate_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
  CONSTRAINT fk_lineestimate_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user (id),
  CONSTRAINT unq_lineestimate_lineestimatenumber UNIQUE (lineestimatenumber)
);

CREATE INDEX idx_lineestimate_fund ON EGW_LINEESTIMATE USING btree (fund);
CREATE INDEX idx_lineestimate_function ON EGW_LINEESTIMATE USING btree (function);
CREATE INDEX idx_lineestimate_budgethead ON EGW_LINEESTIMATE USING btree (budgethead);
CREATE INDEX idx_lineestimate_scheme ON EGW_LINEESTIMATE USING btree (scheme);
CREATE INDEX idx_lineestimate_subscheme ON EGW_LINEESTIMATE USING btree (subscheme);
CREATE INDEX idx_lineestimate_execdept ON EGW_LINEESTIMATE USING btree (executingdepartment);
CREATE INDEX idx_lineestimate_status ON EGW_LINEESTIMATE USING btree (status);

CREATE SEQUENCE SEQ_EGW_LINEESTIMATE;

CREATE TABLE EGW_LINEESTIMATE_DETAILS
(
  id bigint NOT NULL,
  lineestimate bigint NOT NULL,
  nameofwork varchar(1024) NOT NULL,
  estimateamount double precision NOT NULL,
  estimatenumber varchar(50) NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  version bigint DEFAULT 0,
  CONSTRAINT pk_lineestimate_details PRIMARY KEY (id),
  CONSTRAINT fk_lineestimate FOREIGN KEY (lineestimate) REFERENCES egw_lineestimate (id),
  CONSTRAINT fk_lineestimate_details_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
  CONSTRAINT fk_lineestimate_details_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user (id),
  CONSTRAINT unq_lineestimate_details_estimatenumber UNIQUE (estimatenumber)
);

CREATE INDEX idx_lineestimate_details ON EGW_LINEESTIMATE_DETAILS USING btree (lineestimate);

CREATE SEQUENCE SEQ_EGW_LINEESTIMATE_DETAILS;

CREATE TABLE EGW_DOCUMENTS
(
  objectid bigint NOT NULL,
  objecttype character varying(128) NOT NULL,
  filestoreid bigint NOT NULL
);

