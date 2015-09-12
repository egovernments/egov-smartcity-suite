
------------------START------------------
CREATE TABLE eg_authorization_rule (
    id bigint NOT NULL,
    actionid bigint,
    object_type character varying(256),
    scriptid bigint
);
ALTER TABLE ONLY eg_authorization_rule ADD CONSTRAINT eg_authorization_rule_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_authorization_rule ADD CONSTRAINT fk_auth_actionid FOREIGN KEY (actionid) REFERENCES eg_action(id);
ALTER TABLE ONLY eg_authorization_rule ADD CONSTRAINT fk_scriptid_auth FOREIGN KEY (scriptid) REFERENCES eg_script(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_checklists (
    id bigint NOT NULL,
    appconfig_values_id bigint NOT NULL,
    checklistvalue character varying(5) NOT NULL,
    object_id bigint NOT NULL,
    lastmodifieddate timestamp without time zone
);
CREATE SEQUENCE seq_eg_checklists
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_checklists ADD CONSTRAINT eg_checklists_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_checklists ADD CONSTRAINT fk_eg_checklist_appconfig FOREIGN KEY (appconfig_values_id) REFERENCES eg_appconfig_values(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_location (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(100),
    locationid bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    isactive smallint,
    islocation smallint
);
CREATE SEQUENCE seq_eg_location
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_location ADD CONSTRAINT eg_location_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_location_ipmap (
    id bigint NOT NULL,
    locationid bigint NOT NULL,
    ipaddress character varying(150) NOT NULL
);
CREATE SEQUENCE seq_eg_location_ipmap
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_location_ipmap ADD CONSTRAINT eg_location_ipmap_ipaddress_key UNIQUE (ipaddress);
ALTER TABLE ONLY eg_location_ipmap ADD CONSTRAINT eg_location_ipmap_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_location_ipmap ADD CONSTRAINT fk_location_id FOREIGN KEY (locationid) REFERENCES eg_location(id); 
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_usercounter_map (
    id bigint NOT NULL,
    userid bigint NOT NULL,
    counterid bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);
CREATE SEQUENCE seq_eg_usercounter_map
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_usercounter_map ADD CONSTRAINT eg_usercounter_map_pkey PRIMARY KEY (id);
CREATE INDEX indx_eucm_counterid ON eg_usercounter_map USING btree (counterid);
CREATE INDEX indx_eucm_userid ON eg_usercounter_map USING btree (userid);
ALTER TABLE ONLY eg_usercounter_map ADD CONSTRAINT fk_mapcounterid FOREIGN KEY (counterid) REFERENCES eg_location(id); 
ALTER TABLE ONLY eg_usercounter_map ADD CONSTRAINT fk_mapuserid FOREIGN KEY (userid) REFERENCES eg_user(id);
-------------------END-------------------

------------------START-------------------
CREATE TABLE accountdetailkey (
    id bigint NOT NULL,
    groupid bigint NOT NULL,
    glcodeid bigint,
    detailtypeid bigint NOT NULL,
    detailname character varying(50) NOT NULL,
    detailkey bigint NOT NULL
);
CREATE SEQUENCE seq_accountdetailkey
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY accountdetailkey ADD CONSTRAINT accountdetailkey_pkey PRIMARY KEY (id);
CREATE INDEX indx_acdk_acdtid ON accountdetailkey USING btree (detailtypeid);
-------------------END-------------------

------------------START------------------
CREATE TABLE accountdetailtype (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(50) NOT NULL,
    tablename character varying(50),
    columnname character varying(50),
    attributename character varying(50) NOT NULL,
    nbroflevels bigint NOT NULL,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone,
    modifiedby bigint,
    full_qualified_name character varying(250)
);
CREATE SEQUENCE seq_accountdetailtype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY accountdetailtype ADD CONSTRAINT accountdetailtype_attributename_key UNIQUE (attributename);
ALTER TABLE ONLY accountdetailtype ADD CONSTRAINT accountdetailtype_name_key UNIQUE (name);
ALTER TABLE ONLY accountdetailtype ADD CONSTRAINT accountdetailtype_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE accountentitymaster (
    id bigint NOT NULL,
    name character varying(350),
    code character varying(25),
    detailtypeid bigint,
    narration character varying(250),
    isactive smallint,
    lastmodified timestamp without time zone,
    modifiedby bigint,
    created timestamp without time zone
);
CREATE SEQUENCE seq_accountentitymaster
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY accountentitymaster ADD CONSTRAINT accountentitymaster_code_key UNIQUE (code);
ALTER TABLE ONLY accountentitymaster ADD CONSTRAINT accountentitymaster_name_key UNIQUE (name);
ALTER TABLE ONLY accountentitymaster ADD CONSTRAINT accountentitymaster_pkey PRIMARY KEY (id);
CREATE INDEX indx_aem_acdtid ON accountentitymaster USING btree (detailtypeid);
-------------------END-------------------

------------------START------------------
CREATE TABLE accountgroup (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    nbroflevels bigint NOT NULL
);
CREATE SEQUENCE seq_accountgroup
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY accountgroup ADD CONSTRAINT accountgroup_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE bank (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    narration character varying(250),
    isactive smallint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    created timestamp without time zone NOT NULL,
    modifiedby bigint NOT NULL,
    type character varying(50)
);

CREATE SEQUENCE seq_bank
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY bank ADD CONSTRAINT bank_code_key UNIQUE (code);
ALTER TABLE ONLY bank ADD CONSTRAINT bank_name_key UNIQUE (name);
ALTER TABLE ONLY bank ADD CONSTRAINT bank_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE bankaccount (
    id bigint NOT NULL,
    branchid bigint NOT NULL,
    accountnumber character varying(20) NOT NULL,
    accounttype character varying(150) NOT NULL,
    narration character varying(250),
    isactive smallint NOT NULL,
    glcodeid bigint,
    currentbalance double precision NOT NULL,
    fundid bigint,
    payto character varying(100),
    type character varying(50),
    createdby bigint,
    lastmodifiedby bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    version bigint
);
CREATE SEQUENCE seq_bankaccount
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY bankaccount ADD CONSTRAINT bankaccount_branchid_accountnumber_key UNIQUE (branchid, accountnumber);
ALTER TABLE ONLY bankaccount ADD CONSTRAINT bankaccount_pkey PRIMARY KEY (id);
CREATE INDEX indx_bankaccount_coaid ON bankaccount USING btree (glcodeid);
CREATE INDEX indx_bankaccount_fundid ON bankaccount USING btree (fundid);
-------------------END-------------------

------------------START------------------
CREATE TABLE bankbranch (
    id bigint NOT NULL,
    branchcode character varying(50) NOT NULL,
    branchname character varying(50) NOT NULL,
    branchaddress1 character varying(50) NOT NULL,
    branchaddress2 character varying(50),
    branchcity character varying(50),
    branchstate character varying(50),
    branchpin character varying(50),
    branchphone character varying(15),
    branchfax character varying(15),
    bankid bigint,
    contactperson character varying(50),
    isactive smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    modifiedby bigint NOT NULL,
    narration character varying(250),
    micr character varying(50)
);
CREATE SEQUENCE seq_bankbranch
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY bankbranch ADD CONSTRAINT bankbranch_bankid_branchname_key UNIQUE (bankid, branchname);
ALTER TABLE ONLY bankbranch ADD CONSTRAINT bankbranch_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX bank_branch_code ON bankbranch USING btree (bankid, branchcode);
-------------------END-------------------

------------------START------------------
CREATE TABLE bankentries (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    refno character varying(20) NOT NULL,
    type character(1) NOT NULL,
    txndate timestamp without time zone NOT NULL,
    txnamount bigint NOT NULL,
    glcodeid bigint,
    voucherheaderid bigint,
    remarks character varying(100),
    isreversed smallint,
    instrumentheaderid bigint
);
CREATE SEQUENCE seq_bankentries
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY bankentries ADD CONSTRAINT bankentries_pkey PRIMARY KEY (id);
CREATE INDEX indx_be_bankaccountid ON bankentries USING btree (bankaccountid);
CREATE INDEX indx_be_coaid ON bankentries USING btree (glcodeid);
CREATE INDEX indx_be_vhid ON bankentries USING btree (voucherheaderid);
-------------------END-------------------

------------------START------------------
CREATE TABLE bankentries_mis (
    id bigint NOT NULL,
    bankentries_id bigint,
    function_id bigint
);
CREATE SEQUENCE seq_bankentries_mis
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY bankentries_mis ADD CONSTRAINT bankentries_mis_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE bankreconciliation (
    id bigint NOT NULL,
    bankaccountid bigint,
    amount double precision,
    transactiontype character varying(2) NOT NULL,
    instrumentheaderid bigint
);
CREATE SEQUENCE seq_bankreconciliation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY bankreconciliation ADD CONSTRAINT bankreconciliation_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE calendaryear (
    id bigint NOT NULL,
    calendaryear character varying(50),
    startingdate timestamp without time zone,
    endingdate timestamp without time zone,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone,
    modifiedby smallint
);
CREATE SEQUENCE seq_calendaryear
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY calendaryear ADD CONSTRAINT calendaryear_calendaryear_key UNIQUE (calendaryear);
ALTER TABLE ONLY calendaryear ADD CONSTRAINT calendaryear_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE chartofaccountdetail (
    id bigint NOT NULL,
    glcodeid bigint NOT NULL,
    detailtypeid bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone
);
CREATE SEQUENCE seq_chartofaccountdetail
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY chartofaccountdetail ADD CONSTRAINT chartofaccountdetail_glcodeid_detailtypeid_key UNIQUE (glcodeid, detailtypeid);
ALTER TABLE ONLY chartofaccountdetail ADD CONSTRAINT chartofaccountdetail_pkey PRIMARY KEY (id);
CREATE INDEX indx_coad_acdtid ON chartofaccountdetail USING btree (detailtypeid);
CREATE INDEX indx_coad_coaid ON chartofaccountdetail USING btree (glcodeid);
-------------------END-------------------

------------------START------------------
CREATE TABLE chartofaccounts (
    id bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    name character varying(150) NOT NULL,
    description character varying(250),
    isactiveforposting boolean NOT NULL,
    parentid bigint,
    lastmodified timestamp without time zone,
    modifiedby smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    purposeid bigint,
    operation character(1),
    type character(1) NOT NULL,
    class smallint,
    classification smallint,
    functionreqd boolean,
    budgetcheckreq boolean,
    scheduleid bigint,
    receiptscheduleid bigint,
    receiptoperation character(1),
    paymentscheduleid bigint,
    paymentoperation character(1),
    majorcode character varying(255),
    createdby bigint,
    fiescheduleid bigint,
    fieoperation character varying(1)
);
CREATE SEQUENCE seq_chartofaccounts
    START WITH 1222
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT chartofaccounts_glcode_key UNIQUE (glcode);
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT chartofaccounts_pkey PRIMARY KEY (id);
CREATE INDEX coa_type ON chartofaccounts USING btree (type);
CREATE INDEX indx_coa_payscheduleid ON chartofaccounts USING btree (paymentscheduleid);
CREATE INDEX indx_coa_purposeid ON chartofaccounts USING btree (purposeid);
CREATE INDEX indx_coa_receiptscheduleid ON chartofaccounts USING btree (receiptscheduleid);
CREATE INDEX indx_coa_scheduleid ON chartofaccounts USING btree (scheduleid);
-------------------END-------------------

------------------START------------------
CREATE TABLE cheque_dept_mapping (
    id bigint NOT NULL,
    allotedto bigint NOT NULL,
    accountchequeid bigint NOT NULL
);
CREATE SEQUENCE seq_cheque_dept_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY cheque_dept_mapping ADD CONSTRAINT cheque_dept_mapping_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE closedperiods (
    id bigint NOT NULL,
    startingdate timestamp without time zone NOT NULL,
    endingdate timestamp without time zone NOT NULL,
    isclosed bigint NOT NULL
);
-------------------END-------------------

------------------START------------------
CREATE TABLE companydetail (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    address1 character varying(50) NOT NULL,
    address2 character varying(50),
    city character varying(50) NOT NULL,
    pin character varying(10) NOT NULL,
    state character varying(25),
    phone character varying(25) NOT NULL,
    contactperson character varying(50),
    mobile character varying(25),
    fax character varying(25),
    email character varying(25),
    isactive smallint NOT NULL,
    modifiedby bigint,
    created timestamp without time zone NOT NULL,
    lastmodified timestamp without time zone,
    narration character varying(250),
    dbname character varying(50)
);
CREATE SEQUENCE seq_companydetail
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY companydetail ADD CONSTRAINT companydetail_pkey PRIMARY KEY (id);
ALTER TABLE ONLY companydetail ADD CONSTRAINT companydetail_name_key UNIQUE (name);
-------------------END-------------------

------------------START------------------
CREATE TABLE contrajournalvoucher (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    frombankaccountid bigint,
    tobankaccountid bigint,
    instrumentheaderid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_contrajournalvoucher
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY contrajournalvoucher ADD CONSTRAINT contrajournalvoucher_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_bill (
    id bigint NOT NULL,
    id_demand bigint,
    citizen_name character varying(1024) NOT NULL,
    citizen_address character varying(1024) NOT NULL,
    bill_no character varying(20) NOT NULL,
    id_bill_type bigint NOT NULL,
    issue_date timestamp without time zone NOT NULL,
    last_date timestamp without time zone,
    module_id bigint NOT NULL,
    user_id bigint NOT NULL,
    create_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL,
    is_cancelled character(1) DEFAULT 'N'::bpchar NOT NULL,
    fundcode character varying(32),
    functionary_code double precision,
    fundsource_code character varying(32),
    department_code character varying(32),
    coll_modes_not_allowed character varying(512),
    boundary_num bigint,
    boundary_type character varying(512),
    total_amount double precision,
    total_collected_amount double precision,
    service_code character varying(50),
    part_payment_allowed character(1),
    override_accountheads_allowed character(1),
    description character varying(250),
    min_amt_payable double precision,
    consumer_id character varying(64),
    dspl_message character varying(256),
    callback_for_apportion character(1) DEFAULT 0 NOT NULL
);
CREATE SEQUENCE seq_eg_bill
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_bill ADD CONSTRAINT pk_eg_bill PRIMARY KEY (id);

COMMENT ON TABLE eg_bill IS 'Bills for the demand framework';
COMMENT ON COLUMN eg_bill.id IS 'Primary Key';
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
CREATE TABLE eg_bill_details (
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
CREATE SEQUENCE seq_eg_bill_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_bill_details ADD CONSTRAINT pk_eg_bill_details PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_bill_subtype (
    id bigint NOT NULL,
    name character varying(120) NOT NULL,
    expenditure_type character varying(50) NOT NULL
);
CREATE SEQUENCE seq_eg_bill_subtype
    START WITH 26
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_bill_subtype ADD CONSTRAINT eg_bill_subtype_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_bill_type (
    id bigint NOT NULL,
    name character varying(32) NOT NULL,
    code character varying(10) NOT NULL,
    create_date date NOT NULL,
    modified_date date NOT NULL
);
CREATE SEQUENCE seq_eg_bill_type
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_bill_type ADD CONSTRAINT pk_eg_bill_type PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
---TODO BILL DEATILS 2times ?
CREATE TABLE eg_billdetails (
    id bigint NOT NULL,
    billid bigint NOT NULL,
    functionid bigint,
    glcodeid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    narration character varying(250)
);
CREATE SEQUENCE seq_eg_billdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_billdetails ADD CONSTRAINT eg_billdetails_pkey PRIMARY KEY (id);
CREATE INDEX indx_ebd_billid ON eg_billdetails USING btree (billid);
CREATE INDEX indx_ebd_functionid ON eg_billdetails USING btree (functionid);
CREATE INDEX indx_ebd_glcodeid ON eg_billdetails USING btree (glcodeid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_billpayeedetails (
    id bigint NOT NULL,
    billdetailid bigint NOT NULL,
    accountdetailtypeid bigint NOT NULL,
    accountdetailkeyid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    tdsid bigint,
    narration character varying(250),
    pc_department bigint
);
CREATE SEQUENCE seq_eg_billpayeedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_billpayeedetails ADD CONSTRAINT eg_billpayeedetails_pkey PRIMARY KEY (id);
CREATE INDEX index_egbill_payd_accdetkey ON eg_billpayeedetails USING btree (accountdetailkeyid);
CREATE INDEX indx_ebpd_adtid ON eg_billpayeedetails USING btree (accountdetailtypeid);
CREATE INDEX indx_ebpd_bdid ON eg_billpayeedetails USING btree (billdetailid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_billreceipt (
    id bigint NOT NULL,
    billid bigint NOT NULL,
    receipt_number character varying(50),
    receipt_date timestamp without time zone,
    receipt_amount double precision NOT NULL,
    collection_status character varying(20),
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    createdby bigint,
    lastmodifiedby bigint,
    is_cancelled character(1) DEFAULT 'N'::bpchar NOT NULL
);
CREATE SEQUENCE seq_eg_billreceipt
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_billreceipt ADD CONSTRAINT pk_eg_billreceipt PRIMARY KEY (id);

COMMENT ON TABLE eg_billreceipt IS 'Maps receipt information to a bill';
COMMENT ON COLUMN eg_billreceipt.id IS 'Primary Key';
COMMENT ON COLUMN eg_billreceipt.billid IS 'FK to eg_bill';
COMMENT ON COLUMN eg_billreceipt.receipt_number IS 'receipt NUMBER';
COMMENT ON COLUMN eg_billreceipt.receipt_date IS 'receipt date';
COMMENT ON COLUMN eg_billreceipt.receipt_amount IS 'receipt amount';
COMMENT ON COLUMN eg_billreceipt.collection_status IS 'status of collection (approved, pending, etc)';
COMMENT ON COLUMN eg_billreceipt.is_cancelled IS 'receipt status';
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_billregister (
    id bigint NOT NULL,
    billnumber character varying(50) NOT NULL,
    billdate timestamp without time zone NOT NULL,
    billamount double precision NOT NULL,
    fieldid bigint,
    worksdetailid character varying(50),
    billstatus character varying(50) NOT NULL,
    narration character varying(1024),
    passedamount double precision,
    billtype character varying(50),
    expendituretype character varying(20) NOT NULL,
    advanceadjusted double precision,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    statusid bigint,
    workorderdate timestamp without time zone,
    zone character varying(20),
    division character varying(50),
    workordernumber character varying(50),
    billpasseddate timestamp without time zone,
    isactive boolean,
    billapprovalstatus character varying(50),
    po character varying(50),
    state_id bigint,
    version bigint
);
CREATE SEQUENCE seq_eg_billregister
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_billregister ADD CONSTRAINT eg_billregister_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_billregister ADD CONSTRAINT eg_billregister_billnumber_key UNIQUE (billnumber);
CREATE INDEX indx_billreg_expendituretype ON eg_billregister USING btree (expendituretype);
CREATE INDEX indx_billreg_statusid ON eg_billregister USING btree (statusid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_billregistermis (
    id bigint,
    billid bigint NOT NULL,
    fundid bigint,
    segmentid bigint,
    subsegmentid bigint,
    fieldid bigint,
    subfieldid bigint,
    functionaryid bigint,
    sanctionedby character varying(30),
    sanctiondate timestamp without time zone,
    sanctiondetail character varying(200),
    narration character varying(300),
    lastupdatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    disbursementtype character varying(30),
    escalation bigint,
    advancepayments bigint,
    securedadvances bigint,
    deductamountwitheld bigint,
    departmentid bigint,
    month bigint,
    financialyearid bigint,
    fundsourceid bigint,
    rebate real,
    billtype character varying(50),
    payto character varying(250),
    paybydate timestamp without time zone,
    mbrefno character varying(200),
    schemeid bigint,
    subschemeid bigint,
    voucherheaderid bigint,
    sourcepath character varying(150),
    partybillnumber character varying(50),
    partybilldate timestamp without time zone,
    inwardserialnumber character varying(50),
    billsubtype bigint,
    budgetary_appnumber character varying(30),
    budgetcheckreq boolean,
    functionid bigint
);
CREATE SEQUENCE seq_eg_billregistermis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE INDEX indx_ebrm_billid ON eg_billregistermis USING btree (billid);
CREATE INDEX indx_ebrm_fieldid ON eg_billregistermis USING btree (fieldid);
CREATE INDEX indx_ebrm_funationaryid ON eg_billregistermis USING btree (functionaryid);
CREATE INDEX indx_ebrm_fundid ON eg_billregistermis USING btree (fundid);
CREATE INDEX indx_ebrm_segmentid ON eg_billregistermis USING btree (segmentid);
CREATE INDEX indx_ebrm_subfieldid ON eg_billregistermis USING btree (subfieldid);
CREATE INDEX indx_ebrm_subsegid ON eg_billregistermis USING btree (subsegmentid);
CREATE INDEX indx_billmis_departmentid ON eg_billregistermis USING btree (departmentid);
CREATE INDEX indx_billmis_voucherheaderid ON eg_billregistermis USING btree (voucherheaderid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_designation (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(1024),
    chartofaccounts bigint,
    version bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_eg_designation
    START WITH 69
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_designation ADD CONSTRAINT eg_designation_designation_name_key UNIQUE (name);
ALTER TABLE ONLY eg_designation ADD CONSTRAINT eg_designation_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_drawingofficer (
    id bigint NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(150),
    bank bigint,
    bankbranch bigint,
    accountnumber character varying(20),
    tan character varying(10),
    "position" bigint,
    version bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_eg_drawingofficer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_drawingofficer ADD CONSTRAINT eg_drawingofficer_code_key UNIQUE (code);
ALTER TABLE ONLY eg_drawingofficer ADD CONSTRAINT eg_drawingofficer_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_employee (
    id bigint NOT NULL,
    date_of_birth timestamp without time zone,
    blood_group bigint,
    mother_tonuge character varying(256),
    religion_id bigint,
    community_id bigint,
    gender character(1),
    is_handicapped character(1),
    is_med_report_available character(1),
    date_of_first_appointment timestamp without time zone,
    identification_marks1 character varying(1024),
    languages_known_id bigint,
    mode_of_recruiment_id bigint,
    recruitment_type_id bigint,
    employment_status bigint DEFAULT 1,
    category_id bigint,
    qulified_id bigint,
    salary_bank bigint,
    pay_fixed_in_id bigint,
    grade_id bigint,
    present_designation integer,
    scale_of_pay character varying(1024),
    basic_pay bigint,
    spl_pay bigint,
    pp_sgpp_pay bigint,
    annual_increment_id bigint,
    gpf_ac_number character varying(1024),
    retirement_age smallint,
    present_department integer,
    if_on_duty_arrangment_duty_dep character varying(256),
    location character varying(256),
    cost_center character varying(256),
    id_dept bigint,
    id_user bigint,
    isactive smallint,
    empfather_firstname character varying(256),
    empfather_lastname character varying(256),
    empfather_middlename character varying(256),
    emp_firstname character varying(256) NOT NULL,
    emp_lastname character varying(256),
    emp_middlename character varying(256),
    identification_marks2 character varying(1024),
    pan_number character varying(256),
    name character varying(256),
    maturity_date timestamp without time zone,
    bank character varying(256),
    createdtime timestamp without time zone,
    created_by bigint,
    status bigint,
    death_date timestamp without time zone,
    lastmodified_date timestamp without time zone,
    deputation_date timestamp without time zone,
    govt_order_no character varying(256),
    retirement_date timestamp without time zone,
    payment_type character varying(32),
    posting_type_id bigint,
    code character varying(32) NOT NULL,
    modified_by bigint,
    is_avail_quarters smallint DEFAULT 2,
    permanent_address character varying(1024),
    correspondence_address character varying(1024),
    version numeric DEFAULT 0
);
ALTER TABLE ONLY eg_employee ADD CONSTRAINT eg_employee_code_key UNIQUE (code);
ALTER TABLE ONLY eg_employee ADD CONSTRAINT eg_employee_pkey PRIMARY KEY (id);
CREATE INDEX index_emp_blood_group ON eg_employee USING btree (blood_group);
CREATE INDEX index_emp_category_id ON eg_employee USING btree (category_id);
CREATE INDEX index_emp_community_id ON eg_employee USING btree (community_id);
CREATE INDEX index_emp_date_of_birth ON eg_employee USING btree (date_of_birth);
CREATE INDEX index_emp_emp_firstname ON eg_employee USING btree (emp_firstname);
CREATE INDEX index_emp_gender ON eg_employee USING btree (gender);
CREATE INDEX index_emp_grade_id ON eg_employee USING btree (grade_id);
CREATE INDEX index_emp_id_dept ON eg_employee USING btree (id_dept);
CREATE INDEX index_emp_id_user ON eg_employee USING btree (id_user);
CREATE INDEX index_emp_isactive ON eg_employee USING btree (isactive);
CREATE INDEX index_emp_languages_known_id ON eg_employee USING btree (languages_known_id);
CREATE INDEX index_emp_mode_of_rect_id ON eg_employee USING btree (mode_of_recruiment_id);
CREATE INDEX index_emp_mother_tonuge ON eg_employee USING btree (mother_tonuge);
CREATE INDEX index_emp_qulified_id ON eg_employee USING btree (qulified_id);
CREATE INDEX index_emp_recruitment_type_id ON eg_employee USING btree (recruitment_type_id);
CREATE INDEX index_emp_religion_id ON eg_employee USING btree (religion_id);
CREATE INDEX index_emp_status_id ON eg_employee USING btree (employment_status);
-------------------END-------------------

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
CREATE TABLE eg_modules (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(250)
);
CREATE SEQUENCE seq_eg_modules
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_modules ADD CONSTRAINT eg_modules_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_object_history (
    id bigint NOT NULL,
    object_type_id bigint,
    modifed_by bigint,
    object_id bigint,
    remarks character varying(4000),
    modifieddate timestamp without time zone
);
CREATE SEQUENCE seq_object_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_object_history ADD CONSTRAINT eg_object_history_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_object_type (
    id bigint NOT NULL,
    type character varying(20) NOT NULL,
    description character varying(50),
    lastmodifieddate timestamp without time zone NOT NULL
);
CREATE SEQUENCE seq_object_type
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_object_type ADD CONSTRAINT eg_object_type_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_object_type ADD CONSTRAINT eg_object_type_type_key UNIQUE (type);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_partytype (
    id bigint NOT NULL,
    code character varying(20) NOT NULL,
    parentid bigint,
    description character varying(100) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone
);
CREATE SEQUENCE seq_eg_partytype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_partytype ADD CONSTRAINT eg_partytype_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_position (
    name character varying(256) NOT NULL,
    id bigint NOT NULL,
    deptdesig bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    ispostoutsourced boolean,
    version bigint
);
CREATE SEQUENCE seq_eg_position
    START WITH 7
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_position ADD CONSTRAINT eg_position_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_reason_category (
    id bigint NOT NULL,
    name character varying(64) NOT NULL,
    code character varying(64) NOT NULL,
    "order" bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL
);
CREATE SEQUENCE seq_eg_reason_category
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_reason_category ADD CONSTRAINT pk_eg_reason_category PRIMARY KEY (id);
COMMENT ON TABLE eg_reason_category IS 'Master table for Demand Reason Categories';
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_uom (
    id bigint NOT NULL,
    uomcategoryid bigint NOT NULL,
    uom character varying(30) NOT NULL,
    narration character varying(250),
    conv_factor bigint NOT NULL,
    baseuom smallint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_eg_uom
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_uom ADD CONSTRAINT eg_uom_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_uom ADD CONSTRAINT eg_uom_uom_key UNIQUE (uom);
CREATE INDEX indx_euom_categoryid ON eg_uom USING btree (uomcategoryid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_uomcategory (
    id bigint NOT NULL,
    category character varying(30) NOT NULL,
    narration character varying(250),
    lastmodified timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_eg_uomcategory
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_uomcategory ADD CONSTRAINT eg_uomcategory_category_key UNIQUE (category);
ALTER TABLE ONLY eg_uomcategory ADD CONSTRAINT eg_uomcategory_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE financialyear (
    id bigint NOT NULL,
    financialyear character varying(50),
    startingdate timestamp without time zone,
    endingdate timestamp without time zone,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    modifiedby bigint,
    isactiveforposting smallint NOT NULL,
    isclosed smallint,
    transferclosingbalance smallint
);
CREATE SEQUENCE seq_financialyear
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY financialyear ADD CONSTRAINT financialyear_financialyear_key UNIQUE (financialyear);
ALTER TABLE ONLY financialyear ADD CONSTRAINT financialyear_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE financial_institution (
    id bigint NOT NULL,
    name character varying(250) NOT NULL
);
CREATE SEQUENCE seq_financial_institution
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY financial_institution ADD CONSTRAINT financial_institution_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE fiscalperiod (
    id bigint NOT NULL,
    type bigint,
    name character varying(50) NOT NULL,
    startingdate timestamp without time zone NOT NULL,
    endingdate timestamp without time zone NOT NULL,
    parentid bigint,
    isactiveforposting bigint,
    isactive smallint NOT NULL,
    modifiedby bigint,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    created timestamp without time zone NOT NULL,
    financialyearid bigint,
    isclosed smallint,
    moduleid bigint
);
CREATE SEQUENCE seq_fiscalperiod
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY fiscalperiod ADD CONSTRAINT fiscalperiod_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX fsp_name ON fiscalperiod USING btree (name);
CREATE INDEX indx_fp_finyearid ON fiscalperiod USING btree (financialyearid);
-------------------END-------------------

------------------START------------------
CREATE TABLE function (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    type character varying(50),
    llevel bigint NOT NULL,
    parentid bigint,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    modifiedby bigint,
    isnotleaf smallint NOT NULL,
    parentcode character varying(50),
    createdby bigint
);
CREATE SEQUENCE seq_function
    START WITH 252
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY function ADD CONSTRAINT function_code_key UNIQUE (code);
ALTER TABLE ONLY function ADD CONSTRAINT function_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE functionary (
    id bigint NOT NULL,
    code bigint NOT NULL,
    name character varying(256) NOT NULL,
    createtimestamp timestamp without time zone,
    updatetimestamp timestamp without time zone,
    isactive smallint,
    module_name character varying(60)
);
CREATE SEQUENCE seq_functionary
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY functionary ADD CONSTRAINT functionary_code_key UNIQUE (code);
ALTER TABLE ONLY functionary ADD CONSTRAINT functionary_name_key UNIQUE (name);
ALTER TABLE ONLY functionary ADD CONSTRAINT functionary_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE fund (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50),
    llevel bigint NOT NULL,
    parentid bigint,
    isactive smallint NOT NULL,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    created timestamp without time zone NOT NULL,
    modifiedby bigint,
    isnotleaf smallint,
    identifier character(1),
    purpose_id smallint,
    payglcodeid bigint,
    recvglcodeid bigint,
    createdby bigint,
    openingdebitbalance double precision,
    openingcreditbalance double precision,
    transactiondebitamount double precision,
    transactioncreditamount double precision
);
ALTER TABLE ONLY fund ADD CONSTRAINT fund_code_key UNIQUE (code);
ALTER TABLE ONLY fund ADD CONSTRAINT fund_pkey PRIMARY KEY (id);
CREATE INDEX indx_fund_purposeid ON fund USING btree (purpose_id);
-------------------END-------------------

------------------START------------------
CREATE TABLE fundsource (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    type character varying(50),
    parentid bigint,
    isactive boolean NOT NULL,
    created timestamp without time zone NOT NULL,
    financialinstid bigint,
    funding_type character varying(50),
    loan_percentage real,
    source_amount double precision,
    rate_of_interest real,
    loan_period real,
    moratorium_period real,
    repayment_frequency character varying(15),
    no_of_installment bigint,
    bankaccountid bigint,
    govt_order character varying(250),
    govt_date timestamp without time zone,
    dp_code_number character varying(250),
    dp_code_resg character varying(250),
    fin_inst_letter_num character varying(250),
    fin_inst_letter_date timestamp without time zone,
    fin_inst_schm_num character varying(250),
    fin_inst_schm_date timestamp without time zone,
    subschemeid bigint,
    llevel bigint,
    isnotleaf smallint,
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_fundsource
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY fundsource ADD CONSTRAINT fundsource_code_key UNIQUE (code);
ALTER TABLE ONLY fundsource ADD CONSTRAINT fundsource_name_key UNIQUE (name);
ALTER TABLE ONLY fundsource ADD CONSTRAINT fundsource_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE generalledger (
    id bigint NOT NULL,
    voucherlineid bigint NOT NULL,
    effectivedate timestamp without time zone NOT NULL,
    glcodeid bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    description character varying(250),
    voucherheaderid bigint,
    functionid bigint,
    remittancedate timestamp without time zone
);
CREATE SEQUENCE seq_generalledger
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY generalledger ADD CONSTRAINT generalledger_pkey PRIMARY KEY (id);
CREATE INDEX indx_gl_cdt ON generalledger USING btree (creditamount);
CREATE INDEX indx_gl_dbt ON generalledger USING btree (debitamount);
CREATE INDEX indx_gl_functionid ON generalledger USING btree (functionid);
CREATE INDEX indx_gl_glcode ON generalledger USING btree (glcode);
CREATE INDEX indx_gl_glcodeid ON generalledger USING btree (glcodeid);
CREATE INDEX indx_glid_vhid ON generalledger USING btree (voucherheaderid);
-------------------END-------------------

------------------START------------------
CREATE TABLE generalledgerdetail (
    id bigint NOT NULL,
    generalledgerid bigint NOT NULL,
    detailkeyid bigint NOT NULL,
    detailtypeid bigint NOT NULL,
    amount double precision
);
CREATE SEQUENCE seq_generalledgerdetail
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY generalledgerdetail ADD CONSTRAINT generalledgerdetail_pkey PRIMARY KEY (id);
CREATE INDEX indx_gld_acdtypeid ON generalledgerdetail USING btree (detailtypeid);
CREATE INDEX indx_gld_glid ON generalledgerdetail USING btree (generalledgerid);
-------------------END-------------------

------------------START------------------
CREATE TABLE miscbilldetail (
    id bigint NOT NULL,
    billnumber character varying(50),
    billdate timestamp without time zone,
    amount double precision NOT NULL,
    passedamount double precision NOT NULL,
    paidto character varying(250) NOT NULL,
    paidbyid bigint,
    billvhid bigint,
    payvhid bigint,
    paidamount double precision
);
CREATE SEQUENCE seq_miscbilldetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY miscbilldetail ADD CONSTRAINT miscbilldetail_pkey PRIMARY KEY (id);
CREATE INDEX indx_mb_paidamount ON miscbilldetail USING btree (paidamount);
CREATE INDEX indx_mb_paidto ON miscbilldetail USING btree (paidto);
CREATE INDEX indx_mbd_pvhid ON miscbilldetail USING btree (payvhid);
CREATE INDEX indx_mbd_vhid ON miscbilldetail USING btree (billvhid);
-------------------END-------------------

------------------START------------------
CREATE TABLE paymentheader (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    type character varying(50) NOT NULL,
    bankaccountnumberid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint,
    paymentamount double precision,
    concurrencedate timestamp without time zone,
    drawingofficer_id bigint
);
CREATE SEQUENCE seq_paymentheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY paymentheader ADD CONSTRAINT paymentheader_pkey PRIMARY KEY (id);
CREATE INDEX indx_ph_accountid ON paymentheader USING btree (bankaccountnumberid);
CREATE INDEX indx_ph_vhid ON paymentheader USING btree (voucherheaderid);
-------------------END-------------------

------------------START------------------
CREATE TABLE scheme (
    id bigint NOT NULL,
    code character varying(20),
    name character varying(50),
    validfrom timestamp without time zone,
    validto timestamp without time zone,
    isactive boolean DEFAULT false,
    description character varying(255),
    fundid bigint,
    sectorid bigint,
    aaes bigint,
    fieldid bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_scheme
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY scheme ADD CONSTRAINT scheme_code_fundid_key UNIQUE (code, fundid);
ALTER TABLE ONLY scheme ADD CONSTRAINT scheme_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE sub_scheme (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    validfrom timestamp without time zone NOT NULL,
    validto timestamp without time zone,
    isactive character varying(1) NOT NULL,
    schemeid bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    department bigint,
    initial_estimate_amount bigint,
    council_loan_proposal_number character varying(256),
    council_loan_proposal_date timestamp without time zone,
    council_admin_sanction_number character varying(256),
    council_admin_sanction_date timestamp without time zone,
    govt_loan_proposal_number character varying(256),
    govt_loan_proposal_date timestamp without time zone,
    govt_admin_sanction_number character varying(256),
    govt_admin_sanction_date timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_sub_scheme
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY sub_scheme ADD CONSTRAINT sub_scheme_code_schemeid_key UNIQUE (code, schemeid);
ALTER TABLE ONLY sub_scheme ADD CONSTRAINT sub_scheme_pkey PRIMARY KEY (id);
CREATE INDEX indx_schemeid ON sub_scheme USING btree (schemeid);
-------------------END-------------------

------------------START------------------
CREATE TABLE tds (
    id bigint NOT NULL,
    type character varying(20),
    ispaid smallint,
    glcodeid bigint,
    isactive smallint,
    lastmodified timestamp without time zone,
    created timestamp without time zone,
    modifiedby bigint,
    rate double precision,
    effectivefrom timestamp without time zone,
    createdby bigint NOT NULL,
    remitted character varying(100),
    bsrcode character varying(20),
    description character varying(200),
    partytypeid bigint,
    bankid bigint,
    caplimit double precision,
    isearning character varying(1),
    recoveryname character varying(50),
    calculationtype character varying(50),
    section character varying(50),
    recovery_mode character(1) DEFAULT 'M'::bpchar NOT NULL,
    remittance_mode character(1) DEFAULT 'M'::bpchar,
    ifsccode character varying(16),
    accountnumber character varying(32),
    CONSTRAINT tds_ma CHECK ((recovery_mode = ANY (ARRAY['M'::bpchar, 'A'::bpchar])))
);
CREATE SEQUENCE seq_tds
    START WITH 7
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY tds ADD CONSTRAINT tds_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tds ADD CONSTRAINT tds_type_key UNIQUE (type);
-------------------END-------------------

------------------START------------------
CREATE TABLE transactionsummary (
    id bigint NOT NULL,
    glcodeid bigint NOT NULL,
    openingdebitbalance double precision NOT NULL,
    openingcreditbalance double precision NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    accountdetailtypeid bigint,
    accountdetailkey bigint,
    financialyearid bigint NOT NULL,
    fundid bigint,
    fundsourceid bigint,
    narration character varying(300),
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    departmentid bigint,
    functionaryid bigint,
    functionid smallint,
    divisionid bigint
);
CREATE SEQUENCE seq_transactionsummary
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT transactionsummary_pkey PRIMARY KEY (id);
CREATE INDEX indx_ts_acdtypeid ON transactionsummary USING btree (accountdetailtypeid);
CREATE INDEX indx_ts_coaid ON transactionsummary USING btree (glcodeid);
CREATE INDEX indx_ts_finyear ON transactionsummary USING btree (financialyearid);
CREATE INDEX indx_ts_fsourseid ON transactionsummary USING btree (fundsourceid);
CREATE INDEX indx_ts_fundid ON transactionsummary USING btree (fundid);
-------------------END-------------------

------------------START------------------
CREATE TABLE voucherheader (
    id bigint NOT NULL,
    cgn character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    type character varying(100) NOT NULL,
    description character varying(1024),
    effectivedate timestamp without time zone NOT NULL,
    vouchernumber character varying(30),
    voucherdate timestamp without time zone NOT NULL,
    fundid bigint,
    fiscalperiodid bigint NOT NULL,
    status smallint,
    originalvcid bigint,
    isconfirmed smallint DEFAULT 0,
    createdby bigint,
    refcgno character varying(10),
    cgvn character varying(50) NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    moduleid bigint,
    state_id bigint,
    createddate timestamp without time zone,
    version bigint
);
CREATE SEQUENCE seq_voucherheader
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY voucherheader ADD CONSTRAINT voucherheader_pkey PRIMARY KEY (id);
ALTER TABLE ONLY voucherheader ADD CONSTRAINT voucherheader_cgvn_fiscalperiodid_key UNIQUE (cgvn, fiscalperiodid);
CREATE UNIQUE INDEX cgn_c ON voucherheader USING btree (cgn);
CREATE INDEX indx_vh_fundid ON voucherheader USING btree (fundid);
-------------------END-------------------

------------------START------------------
CREATE TABLE vouchermis (
    id bigint NOT NULL,
    billnumber bigint,
    divisionid bigint,
    departmentid bigint,
    voucherheaderid bigint,
    fundsourceid bigint,
    schemeid bigint,
    subschemeid bigint,
    functionaryid bigint,
    sourcepath character varying(250),
    budgetary_appnumber character varying(30),
    budgetcheckreq boolean,
    functionid bigint
);
CREATE SEQUENCE seq_vouchermis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE INDEX indx_vmis_schemeid ON vouchermis USING btree (schemeid);
CREATE INDEX indx_vmis_subschemeid ON vouchermis USING btree (subschemeid);
CREATE INDEX indx_vmis_vhid ON vouchermis USING btree (voucherheaderid);

---------------------------------NOT STRUCTURED---------------------------------

CREATE TABLE eg_advancereqpayeedetails (
    id bigint NOT NULL,
    advancerequisitiondetailid bigint NOT NULL,
    accountdetailtypeid bigint NOT NULL,
    accountdetailkeyid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    tdsid bigint,
    narration character varying(250)
);
ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT eg_advancereqpayeedetails_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_advancerequisition ADD CONSTRAINT eg_advancerequisition_advancerequisitionnumber_key UNIQUE (advancerequisitionnumber);
CREATE INDEX indx_arpd_adtid ON eg_advancereqpayeedetails USING btree (accountdetailtypeid);
CREATE INDEX indx_arpd_ardid ON eg_advancereqpayeedetails USING btree (advancerequisitiondetailid);
CREATE INDEX indx_arpd_tdsid ON eg_advancereqpayeedetails USING btree (tdsid);

CREATE TABLE eg_advancerequisition (
    id bigint NOT NULL,
    advancerequisitionnumber character varying(100) NOT NULL,
    advancerequisitiondate timestamp without time zone NOT NULL,
    advancerequisitionamount double precision NOT NULL,
    narration character varying(512),
    arftype character varying(50) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone NOT NULL,
    statusid bigint NOT NULL,
    state_id bigint
);

ALTER TABLE ONLY eg_advancerequisition ADD CONSTRAINT eg_advancerequisition_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_advancerequisition ADD CONSTRAINT eg_advancerequisition_advancerequisitionnumber_key UNIQUE (advancerequisitionnumber);

CREATE TABLE eg_advancerequisitiondetails (
    id bigint NOT NULL,
    advancerequisitionid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    creditamount double precision,
    debitamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    narration character varying(256),
    functionid bigint
);
ALTER TABLE ONLY eg_advancerequisitiondetails ADD CONSTRAINT eg_advancerequisitiondetails_pkey PRIMARY KEY (id);
CREATE INDEX indx_advreqdetail_advreqid ON eg_advancerequisitiondetails USING btree (advancerequisitionid);
CREATE INDEX indx_advreqdetail_functionid ON eg_advancerequisitiondetails USING btree (functionid);
CREATE INDEX indx_advreqdetail_glcodeid ON eg_advancerequisitiondetails USING btree (glcodeid);

CREATE TABLE eg_advancerequisitionmis (
    id bigint NOT NULL,
    advancerequisitionid bigint NOT NULL,
    fundid bigint,
    fieldid bigint,
    subfieldid bigint,
    functionaryid bigint,
    lastupdatedtime timestamp without time zone NOT NULL,
    departmentid bigint,
    fundsourceid bigint,
    payto character varying(250),
    paybydate timestamp without time zone,
    schemeid bigint,
    subschemeid bigint,
    voucherheaderid bigint,
    sourcepath character varying(256),
    partybillnumber character varying(50),
    partybilldate timestamp without time zone,
    referencenumber character varying(50),
    id_function bigint
);
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT eg_advancerequisitionmis_pkey PRIMARY KEY (id);
CREATE INDEX indx_advreqmis_advreqid ON eg_advancerequisitionmis USING btree (advancerequisitionid);
CREATE INDEX indx_advreqmis_deptid ON eg_advancerequisitionmis USING btree (departmentid);
CREATE INDEX indx_advreqmis_fieldid ON eg_advancerequisitionmis USING btree (fieldid);
CREATE INDEX indx_advreqmis_functionaryid ON eg_advancerequisitionmis USING btree (functionaryid);
CREATE INDEX indx_advreqmis_fundid ON eg_advancerequisitionmis USING btree (fundid);
CREATE INDEX indx_advreqmis_fundsourceid ON eg_advancerequisitionmis USING btree (fundsourceid);
CREATE INDEX indx_advreqmis_schemeid ON eg_advancerequisitionmis USING btree (schemeid);
CREATE INDEX indx_advreqmis_subfieldid ON eg_advancerequisitionmis USING btree (subfieldid);
CREATE INDEX indx_advreqmis_subschemeid ON eg_advancerequisitionmis USING btree (subschemeid);
CREATE INDEX indx_advreqmis_vhid ON eg_advancerequisitionmis USING btree (voucherheaderid);

CREATE TABLE eg_appl_domain (
    id bigint NOT NULL,
    name character varying(128) NOT NULL,
    description character varying(50)
);
ALTER TABLE ONLY eg_appl_domain ADD CONSTRAINT eg_appl_domain_pkey PRIMARY KEY (id);

CREATE TABLE eg_chairperson (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    fromdate date NOT NULL,
    todate date,
    active boolean NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    version numeric DEFAULT 0 NOT NULL
);
CREATE SEQUENCE seq_eg_chairperson
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_chairperson ADD CONSTRAINT pk_chairperson PRIMARY KEY (id);

CREATE TABLE eg_deduction_details (
    id bigint NOT NULL,
    tdsid bigint NOT NULL,
    partytypeid bigint,
    doctypeid bigint,
    docsubtypeid bigint,
    datefrom timestamp without time zone,
    dateto timestamp without time zone,
    lowlimit double precision NOT NULL,
    highlimit double precision,
    incometax real,
    surcharge real,
    education real,
    lastmodifieddate timestamp without time zone,
    amount double precision,
    cumulativehighlimit double precision,
    cumulativelowlimit double precision
);
ALTER TABLE ONLY eg_deduction_details ADD CONSTRAINT eg_deduction_details_pkey PRIMARY KEY (id);

CREATE SEQUENCE seq_eg_deduction_details
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE TABLE eg_dept_do_mapping (
    id bigint NOT NULL,
    department_id bigint NOT NULL,
    drawingofficer_id bigint NOT NULL
);
ALTER TABLE ONLY eg_dept_do_mapping ADD CONSTRAINT eg_dept_do_mapping_pkey PRIMARY KEY (id);

CREATE TABLE eg_dept_functionmap (
    id bigint NOT NULL,
    departmentid bigint,
    functionid bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
ALTER TABLE ONLY eg_dept_functionmap ADD CONSTRAINT eg_dept_functionmap_pkey PRIMARY KEY (id);

CREATE SEQUENCE seq_departmentfunctionmap
    START WITH 339
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE eg_digital_signed_docs (
    id bigint NOT NULL,
    id_module bigint,
    objecttype character varying(64) NOT NULL,
    objectid bigint NOT NULL,
    objectno character varying(100),
    document bytea NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL
);
CREATE SEQUENCE seq_eg_digital_signed_docs
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_digital_signed_docs ADD CONSTRAINT eg_digital_signed_docs_pkey PRIMARY KEY (id);
CREATE INDEX digitalsign_objectid_idx ON eg_digital_signed_docs USING btree (objectid);

CREATE TABLE eg_entity (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone
);
CREATE SEQUENCE seq_eg_entity
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_entity ADD CONSTRAINT eg_entity_name_key UNIQUE (name);
ALTER TABLE ONLY eg_entity ADD CONSTRAINT eg_entity_pkey PRIMARY KEY (id);

CREATE TABLE eg_remittance (
    id bigint NOT NULL,
    tdsid bigint NOT NULL,
    fundid bigint NOT NULL,
    fyid bigint NOT NULL,
    month bigint NOT NULL,
    paymentvhid bigint NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    asondate timestamp without time zone
);
CREATE SEQUENCE seq_eg_remittance
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT eg_remittance_pkey PRIMARY KEY (id);

CREATE TABLE eg_remittance_detail (
    id bigint NOT NULL,
    remittanceid bigint NOT NULL,
    remittancegldtlid bigint,
    lastmodifieddate timestamp without time zone,
    remittedamt double precision,
    generalledgerid bigint
);
CREATE SEQUENCE seq_eg_remittance_detail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_remittance_detail ADD CONSTRAINT eg_remittance_detail_pkey PRIMARY KEY (id);
CREATE INDEX idx_remit_detail_gldtl ON eg_remittance_detail USING btree (remittancegldtlid);
CREATE INDEX idx_remit_detail_remit ON eg_remittance_detail USING btree (remittanceid);

CREATE TABLE eg_remittance_gldtl (
    id bigint NOT NULL,
    gldtlid bigint NOT NULL,
    gldtlamt double precision,
    lastmodifieddate timestamp without time zone,
    remittedamt double precision,
    tdsid bigint
);
CREATE SEQUENCE seq_eg_remittance_gldtl
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_remittance_gldtl ADD CONSTRAINT eg_remittance_gldtl_pkey PRIMARY KEY (id);

CREATE TABLE eg_surrendered_cheques (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    chequenumber character varying(20) NOT NULL,
    chequedate timestamp without time zone NOT NULL,
    vhid bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);
CREATE SEQUENCE seq_eg_surrendered_cheques
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_surrendered_cheques ADD CONSTRAINT eg_surrendered_cheques_pkey PRIMARY KEY (id);
CREATE INDEX indx_esc_accountid ON eg_surrendered_cheques USING btree (bankaccountid);
CREATE INDEX indx_esc_vhid ON eg_surrendered_cheques USING btree (vhid);

CREATE TABLE eg_tasks (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone
);
CREATE SEQUENCE seq_eg_tasks
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_tasks ADD CONSTRAINT eg_tasks_name_key UNIQUE (name);
ALTER TABLE ONLY eg_tasks ADD CONSTRAINT eg_tasks_pkey PRIMARY KEY (id);

CREATE TABLE eg_userdetails (
    id_userdet bigint NOT NULL,
    id_user bigint NOT NULL,
    id_bankbranch bigint,
    extrafield1 character varying(32),
    extrafield2 character varying(32),
    extrafield3 character varying(32),
    dob timestamp without time zone,
    locale character varying(16),
    id_emp character varying(16)
);
ALTER TABLE ONLY eg_userdetails ADD CONSTRAINT eg_userdetails_id_emp_key UNIQUE (id_emp);
ALTER TABLE ONLY eg_userdetails ADD CONSTRAINT eg_userdetails_id_user_key UNIQUE (id_user);
ALTER TABLE ONLY eg_userdetails ADD CONSTRAINT eg_userdetails_pkey PRIMARY KEY (id_userdet);

CREATE TABLE eg_view (
    complaintnumber character varying(32),
    userid bigint,
    dateofview timestamp without time zone
);
ALTER TABLE ONLY eg_view ADD CONSTRAINT eg_view_complaintnumber_key UNIQUE (complaintnumber);

CREATE TABLE schedulemapping (
    id bigint NOT NULL,
    reporttype character varying(10) NOT NULL,
    schedule character varying(10) NOT NULL,
    schedulename character varying(250) NOT NULL,
    repsubtype character varying(10),
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    isremission smallint
);
CREATE SEQUENCE seq_schedulemapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY schedulemapping ADD CONSTRAINT schedulemapping_pkey PRIMARY KEY (id);
ALTER TABLE ONLY schedulemapping ADD CONSTRAINT schedulemapping_schedule_reporttype_key UNIQUE (schedule, reporttype);

CREATE SEQUENCE seq_ass_prd
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



CREATE SEQUENCE seq_eg_advancereqdetails
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_eg_advancereqpayeedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_eg_advancerequisition
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE seq_eg_advancerequisitionmis
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;



CREATE SEQUENCE seq_eg_dept_do_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eg_disbursement_mode
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE SEQUENCE seq_eg_emp_assignment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_eg_emp_assignment_prd
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_service_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_voucherdetail
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2011_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2012_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2013_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2014_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2015_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE SEQUENCE sq_receiptheader_2016_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE SEQUENCE sq_receiptheader_2017_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2018_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2019_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2020_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2021_22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2022_23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2023_24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2024_25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE sq_receiptheader_2025_26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



ALTER TABLE ONLY cheque_dept_mapping ADD CONSTRAINT chequedept_dept_fk FOREIGN KEY (allotedto) REFERENCES eg_department(id);
 
ALTER TABLE ONLY eg_installment_master ADD CONSTRAINT pk_egpt_installment_master PRIMARY KEY (id);
ALTER TABLE ONLY eg_installment_master ADD CONSTRAINT fk_instmstr_module FOREIGN KEY (id_module) REFERENCES eg_module(id); 

ALTER TABLE ONLY function ADD CONSTRAINT fk_function FOREIGN KEY (parentid) REFERENCES function(id);
 
ALTER TABLE ONLY fund ADD CONSTRAINT fk_fund1 FOREIGN KEY (parentid) REFERENCES fund(id);

ALTER TABLE ONLY fundsource ADD CONSTRAINT fin_source_bankaccount_fk FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY fundsource ADD CONSTRAINT fin_source_sub_scheme_fk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id); 
ALTER TABLE ONLY fundsource ADD CONSTRAINT fk_fs FOREIGN KEY (parentid) REFERENCES fundsource(id);
ALTER TABLE ONLY fundsource ADD CONSTRAINT fundsource_fin_inst_fk FOREIGN KEY (financialinstid) REFERENCES financial_institution(id);

ALTER TABLE ONLY eg_advancerequisition ADD CONSTRAINT fk_advancereq_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id); 

ALTER TABLE ONLY eg_advancerequisitiondetails ADD CONSTRAINT fk_advreqdetail_brg FOREIGN KEY (advancerequisitionid) REFERENCES eg_advancerequisition(id); 
ALTER TABLE ONLY eg_advancerequisitiondetails ADD CONSTRAINT fk_advreqdetail_fun FOREIGN KEY (functionid) REFERENCES function(id); 
ALTER TABLE ONLY eg_advancerequisitiondetails ADD CONSTRAINT fk_advreqdetail_gl FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);
 

ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armis_dpt FOREIGN KEY (departmentid) REFERENCES eg_department(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armis_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armisar_ar FOREIGN KEY (advancerequisitionid) REFERENCES eg_advancerequisition(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armischeme_scheme FOREIGN KEY (schemeid) REFERENCES scheme(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armisfield_bdry FOREIGN KEY (fieldid) REFERENCES eg_boundary(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armisfund_fd FOREIGN KEY (fundid) REFERENCES fund(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armisfunry_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armissubfield_bdry FOREIGN KEY (subfieldid) REFERENCES eg_boundary(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armisubsm_subscheme FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id); 
ALTER TABLE ONLY eg_advancerequisitionmis ADD CONSTRAINT fk_armisvh_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id); 

ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT fk_arpd_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id); 
ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT fk_arpd_ard FOREIGN KEY (advancerequisitiondetailid) REFERENCES eg_advancerequisitiondetails(id); 
ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT fk_arpd_tds FOREIGN KEY (tdsid) REFERENCES tds(id); 
 
ALTER TABLE ONLY contrajournalvoucher ADD CONSTRAINT fk_ba_cjv FOREIGN KEY (frombankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY contrajournalvoucher ADD CONSTRAINT fk_ba_cjv1 FOREIGN KEY (tobankaccountid) REFERENCES bankaccount(id); 

ALTER TABLE ONLY bankreconciliation ADD CONSTRAINT fk_bacc_brs FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY bankaccount ADD CONSTRAINT fk_bb_ba FOREIGN KEY (branchid) REFERENCES bankbranch(id); 

ALTER TABLE ONLY eg_billdetails ADD CONSTRAINT fk_bd_brg FOREIGN KEY (billid) REFERENCES eg_billregister(id); 
ALTER TABLE ONLY eg_billdetails ADD CONSTRAINT fk_bd_fun FOREIGN KEY (functionid) REFERENCES function(id); 
ALTER TABLE ONLY eg_billdetails ADD CONSTRAINT fk_bd_gl FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY eg_bill_details ADD CONSTRAINT fk_eg_bill_det_idbill FOREIGN KEY (id_bill) REFERENCES eg_bill(id);
ALTER TABLE ONLY eg_bill_details ADD CONSTRAINT eg_installment_id FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id);

ALTER TABLE ONLY eg_billpayeedetails ADD CONSTRAINT fk_bdp_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id); 
ALTER TABLE ONLY eg_billpayeedetails ADD CONSTRAINT sys_c009660 FOREIGN KEY (tdsid) REFERENCES tds(id);

ALTER TABLE ONLY bankbranch ADD CONSTRAINT fk_bk_bb FOREIGN KEY (bankid) REFERENCES bank(id); 

ALTER TABLE ONLY eg_billregister ADD CONSTRAINT fk_br_fd FOREIGN KEY (fieldid) REFERENCES eg_boundary(id); 
ALTER TABLE ONLY eg_billregister ADD CONSTRAINT sys_c0010469 FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);

ALTER TABLE ONLY eg_billregistermis ADD CONSTRAINT fk_brm_br FOREIGN KEY (billid) REFERENCES eg_billregister(id); 
ALTER TABLE ONLY eg_billregistermis ADD CONSTRAINT fk_brm_bst FOREIGN KEY (billsubtype) REFERENCES eg_bill_subtype(id); 
ALTER TABLE ONLY eg_billregistermis ADD CONSTRAINT fk_brm_dpt FOREIGN KEY (departmentid) REFERENCES eg_department(id); 
ALTER TABLE ONLY eg_billregistermis ADD CONSTRAINT fk_brm_fd FOREIGN KEY (fundid) REFERENCES fund(id); 
ALTER TABLE ONLY eg_billregistermis ADD CONSTRAINT fk_brm_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id); 
ALTER TABLE ONLY eg_billregistermis ADD CONSTRAINT fk_brm_fy FOREIGN KEY (financialyearid) REFERENCES financialyear(id); 
ALTER TABLE ONLY eg_billregistermis ADD CONSTRAINT fk_brm_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id); 

ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_mod_id FOREIGN KEY (module_id) REFERENCES eg_module(id); 
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
 
ALTER TABLE ONLY eg_citizen ADD CONSTRAINT fk_citizen_user FOREIGN KEY (id) REFERENCES eg_user(id) MATCH FULL; 
 
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_coa_gl FOREIGN KEY (glcode) REFERENCES chartofaccounts(glcode);
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_fun_gl FOREIGN KEY (functionid) REFERENCES function(id);
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_voucherheader FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);

ALTER TABLE ONLY generalledgerdetail ADD CONSTRAINT fk_dt_gld FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY generalledgerdetail ADD CONSTRAINT fk_gl_gld FOREIGN KEY (generalledgerid) REFERENCES generalledger(id);

ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_coa_coa FOREIGN KEY (parentid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_cos_sch FOREIGN KEY (scheduleid) REFERENCES schedulemapping(id); 
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_cos_sch1 FOREIGN KEY (receiptscheduleid) REFERENCES schedulemapping(id); 
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_cos_sch2 FOREIGN KEY (paymentscheduleid) REFERENCES schedulemapping(id);
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fiescheduleid_shedule_map_fk FOREIGN KEY (fiescheduleid) REFERENCES schedulemapping(id);

ALTER TABLE ONLY accountentitymaster ADD CONSTRAINT fk_userid_pk FOREIGN KEY (modifiedby) REFERENCES eg_user(id); 

ALTER TABLE ONLY accountdetailkey ADD CONSTRAINT fk_coa_dk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY accountdetailkey ADD CONSTRAINT fk_dt_dk FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY tds ADD CONSTRAINT fk_coa_tds FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY tds ADD CONSTRAINT fk_tds_bk FOREIGN KEY (bankid) REFERENCES bank(id); 
ALTER TABLE ONLY tds ADD CONSTRAINT fk_tds_fy FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);

ALTER TABLE ONLY chartofaccountdetail ADD CONSTRAINT fk_coadt FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY chartofaccountdetail ADD CONSTRAINT fk_dt_coa FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);
 
ALTER TABLE ONLY eg_deduction_details ADD CONSTRAINT fk_dedd_fy FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);
 
ALTER TABLE ONLY eg_dept_do_mapping ADD CONSTRAINT fk_deptdo_deptid FOREIGN KEY (department_id) REFERENCES eg_department(id); 
ALTER TABLE ONLY eg_dept_do_mapping ADD CONSTRAINT fk_deptdo_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id); 

ALTER TABLE ONLY eg_designation ADD CONSTRAINT fk_designation_chartofacc_id FOREIGN KEY (chartofaccounts) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY eg_designation ADD CONSTRAINT fk_desinatoin_glcodeid FOREIGN KEY (chartofaccounts) REFERENCES chartofaccounts(id); 

ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_dettype FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_fs_txn FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_fund_ts FOREIGN KEY (fundid) REFERENCES fund(id);
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_fy_ts FOREIGN KEY (financialyearid) REFERENCES financialyear(id);
 
ALTER TABLE ONLY eg_digital_signed_docs ADD CONSTRAINT fk_digitalsign FOREIGN KEY (createdby) REFERENCES eg_user(id); 

ALTER TABLE ONLY accountentitymaster ADD CONSTRAINT fk_dt_aem FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY eg_drawingofficer ADD CONSTRAINT fk_eg_drawingofficer_position FOREIGN KEY ("position") REFERENCES eg_position(id);
 
ALTER TABLE ONLY eg_bill ADD CONSTRAINT fk_eg_module FOREIGN KEY (module_id) REFERENCES eg_module(id); 
ALTER TABLE ONLY eg_bill ADD CONSTRAINT fk_eg_user FOREIGN KEY (user_id) REFERENCES eg_user(id);

ALTER TABLE ONLY fiscalperiod ADD CONSTRAINT fk_fp_md FOREIGN KEY (moduleid) REFERENCES eg_module(id);
ALTER TABLE ONLY fiscalperiod ADD CONSTRAINT fk_fy_fp FOREIGN KEY (financialyearid) REFERENCES financialyear(id);
 
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_fp_vh FOREIGN KEY (fiscalperiodid) REFERENCES fiscalperiod(id); 
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_fund_vh FOREIGN KEY (fundid) REFERENCES fund(id); 
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_voucher_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id); 
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_voucherheader_vh FOREIGN KEY (originalvcid) REFERENCES voucherheader(id);

ALTER TABLE ONLY miscbilldetail ADD CONSTRAINT fk_mbd_pbi FOREIGN KEY (paidbyid) REFERENCES eg_user(id); 
ALTER TABLE ONLY miscbilldetail ADD CONSTRAINT fk_mbd_pvh FOREIGN KEY (payvhid) REFERENCES voucherheader(id); 
ALTER TABLE ONLY miscbilldetail ADD CONSTRAINT fk_mbd_vh FOREIGN KEY (billvhid) REFERENCES voucherheader(id);

ALTER TABLE ONLY eg_object_history ADD CONSTRAINT fk_modified_by FOREIGN KEY (modifed_by) REFERENCES eg_user(id); 
ALTER TABLE ONLY eg_object_history ADD CONSTRAINT fk_object_type_id FOREIGN KEY (object_type_id) REFERENCES eg_object_type(id); 

ALTER TABLE ONLY paymentheader ADD CONSTRAINT fk_ph_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id);
ALTER TABLE ONLY paymentheader ADD CONSTRAINT fk_ba_ph FOREIGN KEY (bankaccountnumberid) REFERENCES bankaccount(id);
ALTER TABLE ONLY paymentheader ADD CONSTRAINT fk_vh_ph FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);  

  
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_fd FOREIGN KEY (fundid) REFERENCES fund(id); 
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_fy FOREIGN KEY (fyid) REFERENCES financialyear(id); 
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_tds FOREIGN KEY (tdsid) REFERENCES tds(id); 
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_vh FOREIGN KEY (paymentvhid) REFERENCES voucherheader(id); 

ALTER TABLE ONLY eg_remittance_gldtl ADD CONSTRAINT fk_rmtgl_gld FOREIGN KEY (gldtlid) REFERENCES generalledgerdetail(id);
ALTER TABLE ONLY eg_remittance_gldtl ADD CONSTRAINT sys_c009869 FOREIGN KEY (tdsid) REFERENCES tds(id);

ALTER TABLE ONLY schedulemapping ADD CONSTRAINT fk_scd_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id); 
ALTER TABLE ONLY schedulemapping ADD CONSTRAINT fk_sch_cuser FOREIGN KEY (createdby) REFERENCES eg_user(id);
 
ALTER TABLE ONLY sub_scheme ADD CONSTRAINT fk_sub_scheme_department FOREIGN KEY (department) REFERENCES eg_department(id); 
ALTER TABLE ONLY sub_scheme ADD CONSTRAINT sub_scheme_r01 FOREIGN KEY (schemeid) REFERENCES scheme(id);

ALTER TABLE ONLY eg_surrendered_cheques ADD CONSTRAINT fk_surc_ba FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY eg_surrendered_cheques ADD CONSTRAINT fk_surc_vh FOREIGN KEY (vhid) REFERENCES voucherheader(id);
 
ALTER TABLE ONLY eg_view ADD CONSTRAINT fk_user_view FOREIGN KEY (userid) REFERENCES eg_user(id); 

ALTER TABLE ONLY contrajournalvoucher ADD CONSTRAINT fk_vh_cjv FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id); 


ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id); 
ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_schemeid FOREIGN KEY (schemeid) REFERENCES scheme(id); 
ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_subschemeidpk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);
ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_vhidpk FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id); 

ALTER TABLE ONLY bankentries ADD CONSTRAINT fkbaid FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY bankentries ADD CONSTRAINT fkcoaid FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY bankentries ADD CONSTRAINT fkvhid FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);
 
ALTER TABLE ONLY bankentries_mis ADD CONSTRAINT sys_c0011773 FOREIGN KEY (bankentries_id) REFERENCES bankentries(id); 
ALTER TABLE ONLY bankentries_mis ADD CONSTRAINT sys_c0011774 FOREIGN KEY (function_id) REFERENCES function(id);

---------------------------------NOT STRUCTURED ENDS---------------------------------
