------------------START------------------
CREATE TABLE eg_installment_master (
    id bigint NOT NULL,
    installment_num bigint NOT NULL,
    installment_year timestamp without time zone NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    id_module bigint,
    lastupdatedtimestamp timestamp without time zone,
    description character varying(25),
    installment_type character varying(50)
);
CREATE SEQUENCE seq_eg_installment_master
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_installment_master ADD CONSTRAINT unq_year_number_mod UNIQUE (id_module, installment_num, installment_year);
ALTER TABLE ONLY eg_installment_master ADD CONSTRAINT pk_egpt_installment_master PRIMARY KEY (id);
ALTER TABLE ONLY eg_installment_master ADD CONSTRAINT fk_instmstr_module FOREIGN KEY (id_module) REFERENCES eg_module(id);

COMMENT ON TABLE eg_installment_master IS 'This table contains the period for which the bills are being generated';
COMMENT ON COLUMN eg_installment_master.id IS 'primary key';
COMMENT ON COLUMN eg_installment_master.installment_num IS 'Installment number';
COMMENT ON COLUMN eg_installment_master.installment_year IS 'Installment year';
COMMENT ON COLUMN eg_installment_master.start_date IS 'installment start date';
COMMENT ON COLUMN eg_installment_master.end_date IS 'installment end date';
COMMENT ON COLUMN eg_installment_master.id_module IS 'fk to eg_module';
COMMENT ON COLUMN eg_installment_master.lastupdatedtimestamp IS 'last updated time when row got updated';
COMMENT ON COLUMN eg_installment_master.description IS 'Descriptiion of installment';
COMMENT ON COLUMN eg_installment_master.installment_type IS 'type of installment';
-------------------END-------------------

------------------START------------------
CREATE SEQUENCE seq_eg_reason_category;
CREATE TABLE eg_reason_category
(
  id bigint NOT NULL,
  name character varying(64) NOT NULL,
  code character varying(64) NOT NULL,
  "order" bigint NOT NULL,
  modified_date timestamp without time zone NOT NULL
);
  
ALTER TABLE eg_reason_category ADD CONSTRAINT pk_eg_reason_category PRIMARY KEY (id);

COMMENT ON TABLE eg_reason_category IS 'Master table for Demand Reason Categories';
COMMENT ON COLUMN eg_reason_category.id IS 'Primary Key';
COMMENT ON COLUMN eg_reason_category.name IS 'Name of the demand category, Eg: Tax, Penalty, Rebate';
COMMENT ON COLUMN eg_reason_category.code IS 'demand category code(uses internally)';
COMMENT ON COLUMN eg_reason_category."order" IS 'Order no to display list';
-------------------END-------------------

------------------START------------------
CREATE SEQUENCE seq_eg_demand_reason_master;
CREATE TABLE eg_demand_reason_master
(
  id bigint NOT NULL, -- Primary Key
  reasonmaster character varying(64) NOT NULL, -- Name of the demand head
  category bigint NOT NULL, -- FK to eg_reason_category
  isdebit character(1) NOT NULL, -- is tax reason is debit. 0 or 1
  module bigint NOT NULL, -- FK to eg_module
  code character varying(16) NOT NULL, -- tax head code(uses internally)
  "order" bigint NOT NULL, -- Order no to display list
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  isdemand boolean NOT NULL
);

ALTER TABLE eg_demand_reason_master ADD CONSTRAINT pk_eg_demand_reason_master PRIMARY KEY (id);
ALTER TABLE eg_demand_reason_master ADD CONSTRAINT fk_reason_category FOREIGN KEY (category) REFERENCES eg_reason_category(id);
ALTER TABLE eg_demand_reason_master ADD CONSTRAINT fk_demand_reason_module FOREIGN KEY (module) REFERENCES eg_module(id);


COMMENT ON TABLE eg_demand_reason_master IS 'Master table for Demand head';
COMMENT ON COLUMN eg_demand_reason_master.id IS 'Primary Key';
COMMENT ON COLUMN eg_demand_reason_master.reasonmaster IS 'Name of the demand head';
COMMENT ON COLUMN eg_demand_reason_master.category IS 'FK to eg_reason_category';
COMMENT ON COLUMN eg_demand_reason_master.isdebit IS 'is tax head is debit. 0 or 1';
COMMENT ON COLUMN eg_demand_reason_master.module IS 'FK to eg_module';
COMMENT ON COLUMN eg_demand_reason_master.code IS 'tax head code(uses internally)';
COMMENT ON COLUMN eg_demand_reason_master."order" IS 'Order no to display list';
-------------------END-------------------

------------------START------------------
CREATE SEQUENCE SEQ_EG_DEMAND_REASON;
CREATE TABLE eg_demand_reason
(
  id bigint NOT NULL, -- Primary Key
  id_demand_reason_master bigint NOT NULL, -- FK to eg_demand_reason_master
  id_installment bigint NOT NULL, -- FK to eg_installment_master
  percentage_basis real, -- Not used
  id_base_reason bigint, -- FK to eg_demand_reason
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  glcodeid bigint -- FK to chartofaccounts
);

ALTER TABLE eg_demand_reason ADD CONSTRAINT pk_egdm_demand_reason PRIMARY KEY (id);
ALTER TABLE eg_demand_reason ADD CONSTRAINT fk_dreason_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts (id);
ALTER TABLE eg_demand_reason ADD CONSTRAINT fk_eg_dem_reason_id_base_dem FOREIGN KEY (id_base_reason) REFERENCES eg_demand_reason (id);
ALTER TABLE eg_demand_reason ADD CONSTRAINT fk_eg_installment_id FOREIGN KEY (id_installment) REFERENCES eg_installment_master (id);
ALTER TABLE eg_demand_reason ADD CONSTRAINT fk_egdemandreasonmaster_id FOREIGN KEY (id_demand_reason_master) REFERENCES eg_demand_reason_master (id);

CREATE INDEX indx_dem_reason_inst ON eg_demand_reason USING btree (id_demand_reason_master, id_installment);
CREATE UNIQUE INDEX indx_eg_demand_reason ON eg_demand_reason USING btree (id);

COMMENT ON TABLE eg_demand_reason IS 'Master table for Demand heads per installment';
COMMENT ON COLUMN eg_demand_reason.id IS 'Primary Key';
COMMENT ON COLUMN eg_demand_reason.id_demand_reason_master IS 'FK to eg_demand_reason_master';
COMMENT ON COLUMN eg_demand_reason.id_installment IS 'FK to eg_installment_master';
COMMENT ON COLUMN eg_demand_reason.percentage_basis IS 'Not used';
COMMENT ON COLUMN eg_demand_reason.id_base_reason IS 'FK to eg_demand_reason';
COMMENT ON COLUMN eg_demand_reason.glcodeid IS 'FK to chartofaccounts';
-------------------END-------------------

------------------START------------------
CREATE SEQUENCE seq_eg_demand_reason_details;
CREATE TABLE eg_demand_reason_details
(
  id bigint NOT NULL, -- Primary Key
  id_demand_reason bigint NOT NULL, -- FK to eg_demand_reason
  percentage real, -- tax perc for each demand reason
  from_date timestamp without time zone NOT NULL, -- tax perc for each demand reason validity start date
  to_date timestamp without time zone NOT NULL,
  low_limit double precision, -- low limit amount for tax reason
  high_limit double precision, -- High limit amount for tax reason
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  flat_amount double precision, -- Flat tax amount to be applicable
  is_flatamnt_max double precision -- if the tax for reason is flat amount then amount comes here
);

ALTER TABLE eg_demand_reason_details ADD CONSTRAINT pk_egdm_demand_reason_details PRIMARY KEY (id);
ALTER TABLE eg_demand_reason_details ADD CONSTRAINT fk_egpt_dem_reason_id FOREIGN KEY (id_demand_reason) REFERENCES eg_demand_reason (id);

COMMENT ON TABLE eg_demand_reason_details IS 'Master table for Demand Reason details like %age of tax to be calculated on calculated amount like ALV';
COMMENT ON COLUMN eg_demand_reason_details.id IS 'Primary Key';
COMMENT ON COLUMN eg_demand_reason_details.id_demand_reason IS 'FK to eg_demand_reason';
COMMENT ON COLUMN eg_demand_reason_details.percentage IS 'tax perc for each demand head';
COMMENT ON COLUMN eg_demand_reason_details.from_date IS 'tax perc for each demand head validity start date';
COMMENT ON COLUMN eg_demand_reason_details.low_limit IS 'low limit amount for tax head';
COMMENT ON COLUMN eg_demand_reason_details.high_limit IS 'High limit amount for tax head';
COMMENT ON COLUMN eg_demand_reason_details.flat_amount IS 'Flat tax amount to be applicable';
COMMENT ON COLUMN eg_demand_reason_details.is_flatamnt_max IS 'if the tax for head is flat amount then amount comes here';
CREATE INDEX idx_dem_reason_det ON eg_demand_reason_details USING btree (id_demand_reason);

-------------------END-------------------

------------------START------------------
CREATE SEQUENCE seq_eg_demand;
CREATE TABLE eg_demand
(
  id bigint NOT NULL, -- Primary Key
  id_installment bigint NOT NULL, -- FK to eg_installment_master
  base_demand bigint, -- Total demand for a installment
  is_history character(1) NOT NULL DEFAULT 'N'::bpchar, -- history status of demand
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  amt_collected double precision, -- Tax amount collected
  status character(1), -- Not used
  min_amt_payable double precision, -- Minimum Amount payable
  amt_rebate double precision -- Tax rebate given
);

ALTER TABLE eg_demand ADD CONSTRAINT pk_egdm_demand PRIMARY KEY (id);

ALTER TABLE eg_demand ADD CONSTRAINT fk_eg_installment_master_id FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id);

COMMENT ON TABLE eg_demand IS 'Main table for revenue entity Demand';
COMMENT ON COLUMN eg_demand.id IS 'Primary Key';
COMMENT ON COLUMN eg_demand.id_installment IS 'FK to eg_installment_master';
COMMENT ON COLUMN eg_demand.base_demand IS 'Total demand for a installment';
COMMENT ON COLUMN eg_demand.is_history IS 'history status of demand';
COMMENT ON COLUMN eg_demand.amt_collected IS 'Tax amount collected';
COMMENT ON COLUMN eg_demand.status IS 'Not used';
COMMENT ON COLUMN eg_demand.min_amt_payable IS 'Minimum Amount payable';
COMMENT ON COLUMN eg_demand.amt_rebate IS 'Tax rebate given';
-------------------END-------------------

------------------START------------------
create sequence seq_eg_demand_details;
CREATE TABLE eg_demand_details
(
  id bigint NOT NULL, -- Primary Key
  id_demand bigint NOT NULL, -- FK to eg_demand
  id_demand_reason bigint NOT NULL, -- FK to eg_demand_reason
  id_status bigint, -- Not used
  file_reference_no character varying(32), -- Not used
  remarks character varying(512), -- remarks if any
  amount bigint NOT NULL, -- Tax Amount
  modified_date timestamp without time zone NOT NULL,
  create_date timestamp without time zone NOT NULL,
  amt_collected double precision DEFAULT 0, -- Tax Amount collected
  amt_rebate double precision DEFAULT 0 -- tax rebate given
);

ALTER TABLE eg_demand_details ADD CONSTRAINT pk_eg_demand_details PRIMARY KEY (id);
ALTER TABLE eg_demand_details ADD CONSTRAINT fk_demdndetail_status FOREIGN KEY (id_status) REFERENCES egw_status (id);
ALTER TABLE eg_demand_details ADD CONSTRAINT fk_egdm_dem_reason_id FOREIGN KEY (id_demand_reason) REFERENCES eg_demand_reason;
ALTER TABLE eg_demand_details ADD CONSTRAINT fk_egdm_demand_id FOREIGN KEY (id_demand) REFERENCES eg_demand (id);

CREATE INDEX indx_demand_det_mvs ON eg_demand_details USING btree (id_demand, id_demand_reason, amount, amt_collected);
CREATE INDEX inqx_dmddet_dmd_dmdrsnid ON eg_demand_details USING btree (id_demand, id_demand_reason);

COMMENT ON TABLE eg_demand_details IS 'Contains Tax details head wise';
COMMENT ON COLUMN eg_demand_details.id IS 'Primary Key';
COMMENT ON COLUMN eg_demand_details.id_demand IS 'FK to eg_demand';
COMMENT ON COLUMN eg_demand_details.id_demand_reason IS 'FK to eg_demand_reason';
COMMENT ON COLUMN eg_demand_details.id_status IS 'Not used';
COMMENT ON COLUMN eg_demand_details.file_reference_no IS 'Not used';
COMMENT ON COLUMN eg_demand_details.remarks IS 'remarks if any';
COMMENT ON COLUMN eg_demand_details.amount IS 'Tax Amount';
COMMENT ON COLUMN eg_demand_details.amt_collected IS 'Tax Amount collected';
COMMENT ON COLUMN eg_demand_details.amt_rebate IS 'tax rebate given';
-------------------END-------------------

------------------START------------------
create sequence seq_egdm_collected_receipts;
CREATE TABLE egdm_collected_receipts
(
  id bigint NOT NULL,
  id_demand_detail bigint NOT NULL,
  receipt_number character varying(50) NOT NULL,
  receipt_date timestamp without time zone NOT NULL,
  receipt_amount double precision NOT NULL,
  reason_amount double precision,
  status character varying(1) NOT NULL,
  updated_time timestamp without time zone NOT NULL
);

ALTER TABLE egdm_collected_receipts ADD CONSTRAINT pk_egdm_collected_receipts PRIMARY KEY (id);
ALTER TABLE egdm_collected_receipts ADD CONSTRAINT fk_eg_dmd_detail_id FOREIGN KEY (id_demand_detail) REFERENCES eg_demand_details (id);

CREATE INDEX egdm_collrcts_dmddetail_index  ON egdm_collected_receipts USING btree (id_demand_detail DESC);

COMMENT ON TABLE egdm_collected_receipts IS 'Contains payment details';
COMMENT ON COLUMN egdm_collected_receipts.id IS 'Primary Key';
COMMENT ON COLUMN egdm_collected_receipts.id_demand_detail IS 'FK to eg_demand_detail';
COMMENT ON COLUMN egdm_collected_receipts.receipt_number IS 'Receipt number for a pyment';
COMMENT ON COLUMN egdm_collected_receipts.receipt_date IS 'Receipt date for a pyment';
COMMENT ON COLUMN egdm_collected_receipts.receipt_amount IS 'Total receipt amount for a pyment';
COMMENT ON COLUMN egdm_collected_receipts.reason_amount IS 'amount paid for reason head';
COMMENT ON COLUMN egdm_collected_receipts.status IS 'Receipt status';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EG_BILL_TYPE;
CREATE TABLE eg_bill_type
(
  id bigint NOT NULL,
  name character varying(32) NOT NULL,
  code character varying(10) NOT NULL,
  create_date date NOT NULL,
  modified_date date NOT NULL
);

ALTER TABLE eg_bill_type ADD CONSTRAINT prk_eg_bill_type PRIMARY KEY (id);

COMMENT ON TABLE eg_bill_type IS 'Bill type for the bill';
COMMENT ON COLUMN eg_bill_type.id IS 'Primary Key';
COMMENT ON COLUMN eg_bill_type.name IS 'Name for bill type';
COMMENT ON COLUMN eg_bill_type.code IS 'Code';
-------------------END-------------------

------------------START------------------
create sequence seq_eg_bill;
CREATE TABLE eg_bill
(
  id bigint NOT NULL, -- Primary Key
  id_demand bigint, -- FK to eg_demand
  citizen_name character varying(1024) NOT NULL, -- citizen name
  citizen_address character varying(1024) NOT NULL, -- Citizen address
  bill_no character varying(20) NOT NULL, -- Bill no
  id_bill_type bigint NOT NULL, -- FK to eg_bill_type
  issue_date timestamp without time zone NOT NULL, -- Bill issue date
  last_date timestamp without time zone, -- Last date of payment using this bill
  module_id bigint NOT NULL, -- FK to eg_module
  user_id bigint NOT NULL, -- FK to eg_user
  create_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  is_history character(1) NOT NULL DEFAULT 'N'::bpchar, -- Bill history status
  is_cancelled character(1) NOT NULL DEFAULT 'N'::bpchar, -- Bill cancel status
  fundcode character varying(32), -- fund code
  functionary_code double precision, -- functionary code
  fundsource_code character varying(32), -- fund source code
  department_code character varying(32), -- Department that bill entity belongs to
  coll_modes_not_allowed character varying(512), -- allowd collection modes for this bill
  boundary_num bigint, -- boundary of entity, for which bill is generated
  boundary_type character varying(512), -- boundary type of entity, for which bill is generated
  total_amount double precision, -- total bill amount
  total_collected_amount double precision, -- total amount collected for this bill
  service_code character varying(50), -- service code from collection system for each billing system
  part_payment_allowed character(1), -- information to collection system, do system need to allow partial payment
  override_accountheads_allowed character(1), -- information to collection system, do collection system allow for override  of account head wise collection
  description character varying(250), -- Description of entity for which bill is created
  min_amt_payable double precision, -- minimu amount payable for this bill
  consumer_id character varying(64), -- consumer id, for different billing system diff unique ref no will be thr
  dspl_message character varying(256), -- message need to be shown on collection screen
  callback_for_apportion character(1) NOT NULL DEFAULT 0 -- call back required or not while doing collection
);

ALTER TABLE eg_bill ADD CONSTRAINT pk_eg_bill PRIMARY KEY (id);
ALTER TABLE eg_bill ADD CONSTRAINT fk_eg_module FOREIGN KEY (module_id) REFERENCES eg_module (id);
ALTER TABLE eg_bill ADD CONSTRAINT fk_eg_user FOREIGN KEY (user_id) REFERENCES eg_user (id);
ALTER TABLE eg_bill ADD CONSTRAINT fk_egbilltype_id FOREIGN KEY (id_bill_type) REFERENCES eg_bill_type (id);
ALTER TABLE eg_bill ADD CONSTRAINT fk_egdemand_id FOREIGN KEY (id_demand) REFERENCES eg_demand (id);

COMMENT ON TABLE eg_bill IS 'Bills for the demand';
COMMENT ON COLUMN eg_bill.id IS 'Primary Key';
COMMENT ON COLUMN eg_bill.id_demand IS 'FK to eg_demand';
COMMENT ON COLUMN eg_bill.citizen_name IS 'citizen name';
COMMENT ON COLUMN eg_bill.citizen_address IS 'Citizen address';
COMMENT ON COLUMN eg_bill.bill_no IS 'Bill no';
COMMENT ON COLUMN eg_bill.id_bill_type IS 'FK to eg_bill_type';
COMMENT ON COLUMN eg_bill.issue_date IS 'Bill issue date';
COMMENT ON COLUMN eg_bill.last_date IS 'Last date of payment using this bill';
COMMENT ON COLUMN eg_bill.module_id IS 'FK to eg_module';
COMMENT ON COLUMN eg_bill.user_id IS 'FK to eg_user';
COMMENT ON COLUMN eg_bill.is_history IS 'Bill history status';
COMMENT ON COLUMN eg_bill.is_cancelled IS 'Bill cancel status';
COMMENT ON COLUMN eg_bill.fundcode IS 'fund code';
COMMENT ON COLUMN eg_bill.functionary_code IS 'functionary code';
COMMENT ON COLUMN eg_bill.fundsource_code IS 'fund source code';
COMMENT ON COLUMN eg_bill.department_code IS 'Department that bill entity belongs to';
COMMENT ON COLUMN eg_bill.coll_modes_not_allowed IS 'allowd collection modes for this bill';
COMMENT ON COLUMN eg_bill.boundary_num IS 'boundary of entity, for which bill is generated';
COMMENT ON COLUMN eg_bill.boundary_type IS 'boundary type of entity, for which bill is generated';
COMMENT ON COLUMN eg_bill.total_amount IS 'total bill amount';
COMMENT ON COLUMN eg_bill.total_collected_amount IS 'total amount collected for this bill';
COMMENT ON COLUMN eg_bill.service_code IS 'service code from collection system for each billing system';
COMMENT ON COLUMN eg_bill.part_payment_allowed IS 'information to collection system, do system need to allow partial payment';
COMMENT ON COLUMN eg_bill.override_accountheads_allowed IS 'information to collection system, do collection system allow for override  of account head wise collection';
COMMENT ON COLUMN eg_bill.description IS 'Description of entity for which bill is created';
COMMENT ON COLUMN eg_bill.min_amt_payable IS 'minimu amount payable for this bill';
COMMENT ON COLUMN eg_bill.consumer_id IS 'consumer id, for different billing system diff unique ref no will be thr';
COMMENT ON COLUMN eg_bill.dspl_message IS 'message need to be shown on collection screen';
COMMENT ON COLUMN eg_bill.callback_for_apportion IS 'call back required or not while doing collection';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EG_BILL_DETAILS;
CREATE TABLE eg_bill_details
(
  id bigint NOT NULL,
  id_demand_reason bigint,
  create_date timestamp without time zone,
  modified_date timestamp without time zone NOT NULL,
  id_bill bigint NOT NULL,
  collected_amount double precision,
  order_no bigint,
  glcode character varying(64),
  function_code character varying(32),
  cr_amount double precision,
  dr_amount double precision,
  description character varying(128),
  id_installment bigint,
  additional_flag bigint
);

ALTER TABLE eg_bill_details ADD CONSTRAINT pk_eg_bill_details PRIMARY KEY (id);
ALTER TABLE eg_bill_details ADD CONSTRAINT eg_installment_id FOREIGN KEY (id_installment) REFERENCES eg_installment_master (id);
ALTER TABLE eg_bill_details ADD CONSTRAINT fk_eg_bill_det_idbill FOREIGN KEY (id_bill) REFERENCES eg_bill (id);

COMMENT ON TABLE eg_bill_details IS 'Bill details for the bill';
COMMENT ON COLUMN eg_bill_details.id IS 'Primary Key';
COMMENT ON COLUMN eg_bill_details.id_demand_reason IS 'FK to eg_demand_reason';
COMMENT ON COLUMN eg_bill_details.id_bill IS 'FK to eg_bill';
COMMENT ON COLUMN eg_bill_details.collected_amount IS 'Amount collected';
COMMENT ON COLUMN eg_bill_details.order_no IS 'order no';
COMMENT ON COLUMN eg_bill_details.glcode IS 'Financials GL Code';
COMMENT ON COLUMN eg_bill_details.function_code IS 'Financials function code';
COMMENT ON COLUMN eg_bill_details.cr_amount IS 'Credit amount';
COMMENT ON COLUMN eg_bill_details.dr_amount IS 'Debit amount';
COMMENT ON COLUMN eg_bill_details.description IS 'Text to understand amount to be paid for which head and period';
COMMENT ON COLUMN eg_bill_details.id_installment IS 'FK to eg_installment_master';
COMMENT ON COLUMN eg_bill_details.additional_flag IS 'Additional Flag';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EG_BILLRECEIPT;
CREATE TABLE eg_billreceipt
(
  id bigint NOT NULL, -- Primary Key
  billid bigint NOT NULL, -- FK to eg_bill
  receipt_number character varying(50), -- receipt NUMBER
  receipt_date timestamp without time zone, -- receipt date
  receipt_amount double precision NOT NULL, -- receipt amount
  collection_status character varying(20), -- status of collection (approved, pending, etc)
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  createdby bigint,
  lastmodifiedby bigint,
  is_cancelled character(1) NOT NULL DEFAULT 'N'::bpchar -- receipt status
);
ALTER TABLE eg_billreceipt ADD CONSTRAINT pk_eg_billreceipt PRIMARY KEY (id);

COMMENT ON TABLE eg_billreceipt
  IS 'Maps receipt information to a bill';
COMMENT ON COLUMN eg_billreceipt.id IS 'Primary Key';
COMMENT ON COLUMN eg_billreceipt.billid IS 'FK to eg_bill';
COMMENT ON COLUMN eg_billreceipt.receipt_number IS 'receipt NUMBER';
COMMENT ON COLUMN eg_billreceipt.receipt_date IS 'receipt date';
COMMENT ON COLUMN eg_billreceipt.receipt_amount IS 'receipt amount';
COMMENT ON COLUMN eg_billreceipt.collection_status IS 'status of collection (approved, pending, etc)';
COMMENT ON COLUMN eg_billreceipt.is_cancelled IS 'receipt status';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EGDM_DEPRECIATIONMASTER;
CREATE TABLE egdm_depreciationmaster
(
  id bigint NOT NULL, -- Primary Key
  year bigint, -- Not used.
  depreciation_pct bigint NOT NULL, -- Depreciation percentage
  module bigint, -- FK to eg_module
  lastmodifieddate date,
  installment bigint NOT NULL, -- FK to eg_installment_master
  is_history character(1) DEFAULT 'N'::bpchar,
  userid bigint, -- Created by user
  depreciation_name character varying(50), -- Depreciation Name
  depreciation_type character varying(25), -- Type of depreciation
  from_date timestamp without time zone, -- From date for depreciation percentage validity
  to_date timestamp without time zone -- To date for depreciation percentage validity
);

ALTER TABLE egdm_depreciationmaster ADD CONSTRAINT pk_egdm_depreciationmaster PRIMARY KEY (id);
ALTER TABLE egdm_depreciationmaster ADD CONSTRAINT fk_depreciation_module FOREIGN KEY (module) REFERENCES eg_module (id);
ALTER TABLE egdm_depreciationmaster ADD CONSTRAINT fk_depremsrtuid_userid FOREIGN KEY (userid) REFERENCES eg_user (id);
ALTER TABLE egdm_depreciationmaster ADD CONSTRAINT fk_depremstr_instlmstrstr FOREIGN KEY (installment) REFERENCES eg_installment_master (id);

COMMENT ON TABLE egdm_depreciationmaster IS 'Depreciation (age factor) configuration for property tax';
COMMENT ON COLUMN egdm_depreciationmaster.id IS 'Primary Key';
COMMENT ON COLUMN egdm_depreciationmaster.year IS 'Not used.';
COMMENT ON COLUMN egdm_depreciationmaster.depreciation_pct IS 'Depreciation percentage';
COMMENT ON COLUMN egdm_depreciationmaster.module IS 'FK to eg_module';
COMMENT ON COLUMN egdm_depreciationmaster.installment IS 'FK to eg_installment_master';
COMMENT ON COLUMN egdm_depreciationmaster.userid IS 'Created by user';
COMMENT ON COLUMN egdm_depreciationmaster.depreciation_name IS 'Depreciation Name';
COMMENT ON COLUMN egdm_depreciationmaster.depreciation_type IS 'Type of depreciation';
COMMENT ON COLUMN egdm_depreciationmaster.from_date IS 'From date for depreciation percentage validity';
COMMENT ON COLUMN egdm_depreciationmaster.to_date IS 'To date for depreciation percentage validity';

