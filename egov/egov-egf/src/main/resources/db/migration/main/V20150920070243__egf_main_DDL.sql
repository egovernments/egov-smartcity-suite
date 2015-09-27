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
ALTER TABLE ONLY bankentries ADD CONSTRAINT fkbaid FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY bankentries ADD CONSTRAINT fkcoaid FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY bankentries ADD CONSTRAINT fkvhid FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);
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
ALTER TABLE ONLY bankentries_mis ADD CONSTRAINT sys_c0011773 FOREIGN KEY (bankentries_id) REFERENCES bankentries(id); 
ALTER TABLE ONLY bankentries_mis ADD CONSTRAINT sys_c0011774 FOREIGN KEY (function_id) REFERENCES function(id);
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
ALTER TABLE ONLY contrajournalvoucher ADD CONSTRAINT fk_ba_cjv FOREIGN KEY (frombankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY contrajournalvoucher ADD CONSTRAINT fk_ba_cjv1 FOREIGN KEY (tobankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY contrajournalvoucher ADD CONSTRAINT fk_vh_cjv FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id); 
CREATE INDEX indx_cjv_faccountid ON contrajournalvoucher USING btree (frombankaccountid);
CREATE INDEX indx_cjv_toaccountid ON contrajournalvoucher USING btree (tobankaccountid);
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
ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT fk_br_fd FOREIGN KEY (fieldid) REFERENCES eg_boundary(id);
ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT FK_BR_STATE FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
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
ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_br FOREIGN KEY (billid) REFERENCES eg_billregister(id);
ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_bst FOREIGN KEY (billsubtype) REFERENCES eg_bill_subtype(id);
ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_dpt FOREIGN KEY (departmentid) REFERENCES eg_department(id);
ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_fd FOREIGN KEY (fundid) REFERENCES fund(id);
ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);
ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_fy FOREIGN KEY (financialyearid) REFERENCES financialyear(id);
ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);
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
ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_brg FOREIGN KEY (billid) REFERENCES eg_billregister(id);
ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_fun FOREIGN KEY (functionid) REFERENCES function(id);
ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_gl FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);
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
ALTER TABLE ONLY tds ADD CONSTRAINT fk_coa_tds FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY tds ADD CONSTRAINT fk_tds_bk FOREIGN KEY (bankid) REFERENCES bank(id); 
ALTER TABLE ONLY tds ADD CONSTRAINT fk_tds_fy FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);


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
ALTER TABLE ONLY eg_billpayeedetails
    ADD CONSTRAINT fk_bdp_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY eg_billpayeedetails
    ADD CONSTRAINT sys_c009660 FOREIGN KEY (tdsid) REFERENCES tds(id);
-------------------END-------------------


---------------------START----------------------


ALTER TABLE ONLY bankbranch ADD CONSTRAINT fk_bk_bb FOREIGN KEY (bankid) REFERENCES bank(id); 

ALTER TABLE ONLY eg_billregister ADD CONSTRAINT sys_c0010469 FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);

---------------------------------------END-----------------------------------------------------



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
ALTER TABLE ONLY paymentheader ADD CONSTRAINT fk_ph_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id);
ALTER TABLE ONLY paymentheader ADD CONSTRAINT fk_ba_ph FOREIGN KEY (bankaccountnumberid) REFERENCES bankaccount(id);
ALTER TABLE ONLY paymentheader ADD CONSTRAINT fk_vh_ph FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);  

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
ALTER TABLE ONLY miscbilldetail ADD CONSTRAINT fk_mbd_pbi FOREIGN KEY (paidbyid) REFERENCES eg_user(id); 
ALTER TABLE ONLY miscbilldetail ADD CONSTRAINT fk_mbd_pvh FOREIGN KEY (payvhid) REFERENCES voucherheader(id); 
ALTER TABLE ONLY miscbilldetail ADD CONSTRAINT fk_mbd_vh FOREIGN KEY (billvhid) REFERENCES voucherheader(id);
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
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_dettype FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_fs_txn FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_fund_ts FOREIGN KEY (fundid) REFERENCES fund(id);
ALTER TABLE ONLY transactionsummary ADD CONSTRAINT fk_fy_ts FOREIGN KEY (financialyearid) REFERENCES financialyear(id);

-------------------------------END-------------------

-----------------------------------START-----------------------------------------
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

---------------------------------END-------------------------------
---------------------------------START---------------------------------------
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

-----------------------------------END--------------------------------------

------------------------START------------------------------------------------
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


 
-----------------------------------END------------------------------------


--------------------START----------------------------------------------------------
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
ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT fk_arpd_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id); 
ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT fk_arpd_ard FOREIGN KEY (advancerequisitiondetailid) REFERENCES eg_advancerequisitiondetails(id); 
ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT fk_arpd_tds FOREIGN KEY (tdsid) REFERENCES tds(id); 
ALTER TABLE ONLY eg_advancereqpayeedetails ADD CONSTRAINT eg_advancereqpayeedetails_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_advancerequisition ADD CONSTRAINT eg_advancerequisition_advancerequisitionnumber_key UNIQUE (advancerequisitionnumber);
CREATE INDEX indx_arpd_adtid ON eg_advancereqpayeedetails USING btree (accountdetailtypeid);
CREATE INDEX indx_arpd_ardid ON eg_advancereqpayeedetails USING btree (advancerequisitiondetailid);
CREATE INDEX indx_arpd_tdsid ON eg_advancereqpayeedetails USING btree (tdsid);




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
----------------------------------END---------------------------

-------------------START----------------------------
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
ALTER TABLE ONLY eg_deduction_details ADD CONSTRAINT fk_dedd_fy FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);

CREATE SEQUENCE seq_eg_deduction_details
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
-----------------------------END------------------------

---------------------------------_START--------------
CREATE TABLE eg_dept_do_mapping (
    id bigint NOT NULL,
    department_id bigint NOT NULL,
    drawingofficer_id bigint NOT NULL
);
ALTER TABLE ONLY eg_dept_do_mapping ADD CONSTRAINT eg_dept_do_mapping_pkey PRIMARY KEY (id);

CREATE SEQUENCE seq_eg_dept_do_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;
ALTER TABLE ONLY eg_dept_do_mapping ADD CONSTRAINT fk_deptdo_deptid FOREIGN KEY (department_id) REFERENCES eg_department(id); 
ALTER TABLE ONLY eg_dept_do_mapping ADD CONSTRAINT fk_deptdo_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id); 


-------------------END ---------------------------

---------------------START---------------------
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

---TODO check if needed---
CREATE SEQUENCE seq_departmentfunctionmap
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
----------------------------END--------------------------------    
---------------------START------------------------    
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
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_fd FOREIGN KEY (fundid) REFERENCES fund(id); 
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_fy FOREIGN KEY (fyid) REFERENCES financialyear(id); 
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_tds FOREIGN KEY (tdsid) REFERENCES tds(id); 
ALTER TABLE ONLY eg_remittance ADD CONSTRAINT fk_rmt_vh FOREIGN KEY (paymentvhid) REFERENCES voucherheader(id); 

------------END --------------------

---------------------START--------------------------
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
-----------------------------END---------------------

-------------------------START--------------------
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
ALTER TABLE ONLY eg_remittance_gldtl ADD CONSTRAINT fk_rmtgl_gld FOREIGN KEY (gldtlid) REFERENCES generalledgerdetail(id);
ALTER TABLE ONLY eg_remittance_gldtl ADD CONSTRAINT FK_RGLDTL_TDS FOREIGN KEY (tdsid) REFERENCES tds(id);
---------------------END --------------------------------


CREATE TABLE egf_cbill (
    id bigint
);
---------------------END --------------------------------
CREATE TABLE egf_instrumenttype (
    id bigint NOT NULL,
    type character varying(50),
    isactive character varying(1),
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);
ALTER TABLE ONLY egf_instrumenttype
    ADD CONSTRAINT egf_instrumenttype_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_instrumenttype
    ADD CONSTRAINT egf_instrumenttype_type_key UNIQUE (type);
    
CREATE TABLE egf_instrumentaccountcodes (
    id bigint NOT NULL,
    typeid bigint,
    glcodeid bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);
CREATE SEQUENCE seq_egf_instrumentaccountcodes
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT egf_instrumentaccountcodes_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT fk_egf_instracccodes_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);
ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT fk_egf_instracccodes_instrtype FOREIGN KEY (typeid) REFERENCES egf_instrumenttype(id);

    


    
CREATE TABLE egf_ecstype (
    id bigint NOT NULL,
    type character varying(30) NOT NULL,
    isactive bigint NOT NULL
);
ALTER TABLE ONLY egf_ecstype
    ADD CONSTRAINT egf_ecstype_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_ecstype
    ADD CONSTRAINT egf_ecstype_type_key UNIQUE (type);
    
CREATE SEQUENCE seq_egf_ecstype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;
    CREATE TABLE egf_instrumentvoucher (
    id bigint,
    instrumentheaderid bigint,
    voucherheaderid bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);

CREATE INDEX indx_iv_vh ON egf_instrumentvoucher USING btree (voucherheaderid);

CREATE SEQUENCE seq_egf_instrumentvoucher
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
CREATE TABLE egf_instrumentheader (
    id bigint NOT NULL,
    instrumentnumber character varying(20),
    instrumentdate timestamp without time zone,
    instrumentamount double precision NOT NULL,
    id_status bigint NOT NULL,
    bankaccountid bigint,
    payto character varying(250),
    ispaycheque character(1),
    instrumenttype bigint,
    bankid bigint,
    detailkeyid bigint,
    detailtypeid bigint,
    transactionnumber character varying(50),
    transactiondate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    payee character varying(50),
    bankbranchname character varying(50),
    surrendarreason character varying(100),
    serialno character varying(16),
    ecstype bigint
);
ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT egf_instrumentheader_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT adt_im_pk FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT baid_im_pk FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);
ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT bankid_im_pk FOREIGN KEY (bankid) REFERENCES bank(id);
ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT fk_inh_ecs FOREIGN KEY (ecstype) REFERENCES egf_ecstype(id);
ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT statusid_im_pk FOREIGN KEY (id_status) REFERENCES egw_status(id);
CREATE INDEX indx_ih_in ON egf_instrumentheader USING btree (instrumentnumber);
CREATE INDEX indx_ih_payto ON egf_instrumentheader USING btree (payto);
CREATE INDEX indx_ih_status ON egf_instrumentheader USING btree (id_status);
CREATE INDEX indx_transaction_date ON egf_instrumentheader USING btree (transactiondate);
CREATE INDEX indx_iv_ih ON egf_instrumentvoucher USING btree (instrumentheaderid);
    
    
CREATE TABLE egf_instrumentotherdetails (
    id bigint,
    instrumentheaderid bigint,
    payinslipid bigint,
    instrumentstatusdate timestamp without time zone,
    reconciledamount double precision,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    reconciledon timestamp without time zone,
    dishonorbankrefno character varying(20)
);



CREATE SEQUENCE seq_egf_instrumentheader
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE seq_egf_instrumentotherdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE seq_egf_instrumenttype
    START WITH 11
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

----------------START-------------------------------
CREATE TABLE egf_fundingagency (
    id bigint NOT NULL,
    code character varying(16) NOT NULL,
    address character varying(256),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    remarks character varying(200),
    name character varying(100),
    isactive smallint
);
CREATE SEQUENCE seq_egf_fundingagency
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;
ALTER TABLE ONLY egf_fundingagency
    ADD CONSTRAINT egf_fundingagency_pkey PRIMARY KEY (id);    
ALTER TABLE ONLY egf_fundingagency
    ADD CONSTRAINT egf_fundingagency_code_key UNIQUE (code);
    
---------------------END --------------------------------
----------------START-------------------------------
CREATE TABLE egf_loangrantheader (
    id bigint NOT NULL,
    subschemeid bigint,
    councilresno character varying(48),
    govtorderno character varying(48),
    amendmentno character varying(48),
    projectcost double precision,
    sanctionedcost double precision,
    revisedcost double precision,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    amendmentdate timestamp without time zone,
    councilresdate timestamp without time zone,
    govtorderdate timestamp without time zone
);
ALTER TABLE ONLY egf_loangrantheader
    ADD CONSTRAINT egf_loangrantheader_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_loangrantheader
    ADD CONSTRAINT fk_egf_lgheader_subscheme FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);

    
---------------------END --------------------------------

----------------START-------------------------------
CREATE TABLE egf_loangrantdetail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    agencyid bigint NOT NULL,
    loanamount double precision,
    grantamount double precision,
    percentage real,
    agencyschemeno character varying(48),
    councilresno character varying(48),
    loansanctionno character varying(48),
    agreementdate timestamp without time zone,
    commorderno character varying(48),
    docid bigint,
    type character varying(16) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);
ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT egf_loangrantdetail_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT fk_egf_lgdetail_agency FOREIGN KEY (agencyid) REFERENCES egf_fundingagency(id);

ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT fk_egf_lgdetail_lgheader FOREIGN KEY (headerid) REFERENCES egf_loangrantheader(id);
---------------------END --------------------------------
----------------START-------------------------------
CREATE TABLE egf_loangrantreceiptdetail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    agencyid bigint,
    amount double precision,
    description character varying(1024),
    voucherheaderid bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    instrumentheaderid bigint,
    bankaccountid bigint
);
ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT egf_loangrantreceiptdetail_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_agency FOREIGN KEY (agencyid) REFERENCES egf_fundingagency(id);

ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_bankacc FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_instrument FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);

ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_lgheader FOREIGN KEY (headerid) REFERENCES egf_loangrantheader(id);

ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);


CREATE SEQUENCE seq_egf_loangrantdetail
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;


CREATE SEQUENCE seq_egf_loangrantheader
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;

CREATE SEQUENCE seq_egf_loangrantreceiptdetail
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;
---------------------END --------------------------------
----------------START-------------------------------
CREATE TABLE egf_scheme_bankaccount (
    id bigint NOT NULL,
    schemeid bigint,
    subschemeid bigint,
    bankaccountid bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);
CREATE SEQUENCE seq_egf_scheme_bankaccount
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;
ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT egf_scheme_bankaccount_pkey PRIMARY KEY (id);  
ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_bankacc FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_sch FOREIGN KEY (schemeid) REFERENCES scheme(id);

ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_subsch FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);

    
---------------------END --------------------------------
----------------START-------------------------------
CREATE TABLE egf_subscheme_project (
    id bigint NOT NULL,
    subschemeid bigint NOT NULL,
    projectcodeid bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);
CREATE SEQUENCE seq_egf_subscheme_project
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;
ALTER TABLE ONLY egf_subscheme_project
    ADD CONSTRAINT egf_subscheme_project_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_subscheme_project
    ADD CONSTRAINT fk_egf_subscheme_prj_subsch FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);
    
---------------------END --------------------------------
----------------START-------------------------------
CREATE TABLE egf_fixeddeposit (
    id bigint NOT NULL,
    fileno character varying(48),
    amount double precision,
    depositdate timestamp without time zone NOT NULL,
    bankbranchid bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    interestrate double precision NOT NULL,
    period character varying(64),
    serialnumber character varying(32) NOT NULL,
    outflowgjvid bigint,
    gjvamount double precision,
    maturityamount double precision,
    maturitydate timestamp without time zone,
    withdrawaldate timestamp without time zone,
    inflowgjvid bigint,
    challanreceiptvhid bigint,
    instrumentheaderid bigint,
    receiptamount double precision,
    remarks character varying(512),
    parentid bigint,
    extend smallint DEFAULT 0,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);
CREATE SEQUENCE seq_egf_fixeddeposit
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT egf_fixeddeposit_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_bkaccountid FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_bkbranchid FOREIGN KEY (bankbranchid) REFERENCES bankbranch(id);
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_challanvh FOREIGN KEY (inflowgjvid) REFERENCES voucherheader(id);
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_fdvh FOREIGN KEY (outflowgjvid) REFERENCES voucherheader(id);
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_id FOREIGN KEY (parentid) REFERENCES egf_fixeddeposit(id);
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_instrumentid FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);
ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_withdrawalvh FOREIGN KEY (challanreceiptvhid) REFERENCES voucherheader(id);
    
---------------------END --------------------------------
----------------START-------------------------------
CREATE TABLE egf_grant (
    id bigint NOT NULL,
    deptid bigint,
    financialyearid bigint NOT NULL,
    period character varying(10) NOT NULL,
    proceedingsno character varying(48) NOT NULL,
    proceedingsdate timestamp without time zone NOT NULL,
    accrualvoucherid bigint NOT NULL,
    accrualamount double precision,
    grantvoucherid bigint,
    receiptvoucherid bigint,
    grantamount double precision,
    instrumentheaderid bigint,
    remarks character varying(512),
    granttype character varying(48),
    commtaxofficer character varying(48),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);
CREATE SEQUENCE seq_egf_grant
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT egf_grant_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_accrualvh FOREIGN KEY (accrualvoucherid) REFERENCES voucherheader(id);
ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_deptid FOREIGN KEY (deptid) REFERENCES eg_department(id);
ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_finyearid FOREIGN KEY (financialyearid) REFERENCES financialyear(id);
ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_grantvh FOREIGN KEY (grantvoucherid) REFERENCES voucherheader(id);
ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_instrumentheader FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);
ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_receiptvh FOREIGN KEY (receiptvoucherid) REFERENCES voucherheader(id);    
---------------------END --------------------------------
----------------START-------------------------------
CREATE TABLE egf_budget (
    id bigint NOT NULL,
    name character varying(50),
    description character varying(250),
    financialyearid bigint NOT NULL,
    state_id bigint,
    parent bigint,
    isactivebudget bigint,
    updatedtimestamp timestamp without time zone NOT NULL,
    isprimarybudget bigint NOT NULL,
    createdby bigint,
    lastmodifiedby bigint,
    isbere character varying(20),
    as_on_date timestamp without time zone,
    materializedpath character varying(10),
    reference_budget bigint,
    document_number bigint
);
ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT egf_budget_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT egf_budget_name_key UNIQUE (name);
ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT fk_egf_budget_budget FOREIGN KEY (parent) REFERENCES egf_budget(id);
ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT fk_egf_budget_eg_finyear1 FOREIGN KEY (financialyearid) REFERENCES financialyear(id);
ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT fk_egf_budget_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT fk_reference_budget FOREIGN KEY (reference_budget) REFERENCES egf_budget(id);
CREATE INDEX budget_fyear ON egf_budget USING btree (financialyearid);
    
CREATE SEQUENCE seq_egf_budget
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
    
CREATE TABLE egf_budgetgroup (
    id bigint NOT NULL,
    majorcode bigint,
    maxcode bigint,
    mincode bigint,
    name character varying(250),
    description character varying(250),
    budgetingtype character varying(250),
    accounttype character varying(250),
    isactive smallint,
    updatedtimestamp timestamp without time zone NOT NULL
);
ALTER TABLE egf_budgetgroup ALTER COLUMN isactive TYPE boolean USING CASE WHEN isactive = 0 THEN FALSE WHEN isactive = 1 THEN TRUE ELSE NULL END;

ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT egf_budgetgroup_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_majorcode FOREIGN KEY (majorcode) REFERENCES chartofaccounts(id);
ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_maxcode FOREIGN KEY (maxcode) REFERENCES chartofaccounts(id);
ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_mincode FOREIGN KEY (mincode) REFERENCES chartofaccounts(id);
CREATE INDEX budgetgroup_mincode ON egf_budgetgroup USING btree (mincode);
CREATE INDEX indx_bg_majorcode ON egf_budgetgroup USING btree (majorcode);
CREATE INDEX indx_bg_maxcode ON egf_budgetgroup USING btree (maxcode);

CREATE TABLE egf_budgetdetail (
    id bigint NOT NULL,
    using_department bigint,
    executing_department bigint,
    function bigint,
    budget bigint NOT NULL,
    budgetgroup bigint NOT NULL,
    originalamount double precision,
    approvedamount double precision,
    anticipatory_amount double precision,
    budgetavailable double precision,
    scheme bigint,
    subscheme bigint,
    functionary bigint,
    boundary bigint,
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint,
    state_id bigint,
    fund bigint,
    materializedpath character varying(10),
    document_number bigint,
    uniqueno character varying(32),
    planningpercent real
);

ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT egf_budgetdetail_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT egf_budgetdetail_budget_budgetgroup_scheme_subscheme_functi_key UNIQUE (budget, budgetgroup, scheme, subscheme, functionary, function, executing_department, fund);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_exe_dept FOREIGN KEY (executing_department) REFERENCES eg_department(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_functionary FOREIGN KEY (functionary) REFERENCES functionary(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_using_dept FOREIGN KEY (using_department) REFERENCES eg_department(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_budget FOREIGN KEY (budget) REFERENCES egf_budget(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_fund FOREIGN KEY (fund) REFERENCES fund(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_funtion FOREIGN KEY (function) REFERENCES function(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_group FOREIGN KEY (budgetgroup) REFERENCES egf_budgetgroup(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_scheme FOREIGN KEY (scheme) REFERENCES scheme(id);
ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_subscheme FOREIGN KEY (subscheme) REFERENCES sub_scheme(id);
CREATE INDEX budgetdetail_budget ON egf_budgetdetail USING btree (budget);
CREATE INDEX budgetdetail_budgetgroup ON egf_budgetdetail USING btree (budgetgroup);
CREATE INDEX budgetdetail_dept ON egf_budgetdetail USING btree (executing_department);
CREATE INDEX budgetdetail_function ON egf_budgetdetail USING btree (function);

CREATE TABLE egf_reappropriation_misc (
    id bigint NOT NULL,
    sequence_number character varying(1024),
    reappropriation_date timestamp without time zone,
    remarks character varying(1024),
    state_id bigint,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone
);

ALTER TABLE ONLY egf_reappropriation_misc
    ADD CONSTRAINT egf_reappropriation_misc_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_reappropriation_misc
    ADD CONSTRAINT egf_reappropriation_misc_sequence_number_key UNIQUE (sequence_number);
CREATE TABLE egf_budget_reappropriation (
    id bigint NOT NULL,
    budgetdetail bigint NOT NULL,
    anticipatory_amount bigint,
    addition_amount bigint,
    deduction_amount bigint,
    state_id bigint,
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint,
    reappropriation_misc bigint,
    original_addition_amount bigint,
    original_deduction_amount bigint,
    status bigint
);
ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT egf_budget_reappropriation_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_egf_budgetdetail FOREIGN KEY (budgetdetail) REFERENCES egf_budgetdetail(id);
ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_egf_reappropriation_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_reapp_status FOREIGN KEY (status) REFERENCES egw_status(id);
ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_reappropriation_misc FOREIGN KEY (reappropriation_misc) REFERENCES egf_reappropriation_misc(id);
    


CREATE TABLE egf_budget_usage (
    id bigint NOT NULL,
    financialyearid bigint NOT NULL,
    moduleid bigint,
    referencenumber character varying(50) NOT NULL,
    createdby bigint,
    consumedamt double precision,
    releasedamt double precision,
    updatedtime timestamp without time zone NOT NULL,
    budgetdetailid bigint NOT NULL,
    appropriationnumber character varying(32)
);

ALTER TABLE ONLY egf_budget_usage
    ADD CONSTRAINT egf_budget_usage_pkey PRIMARY KEY (id);  
ALTER TABLE ONLY egf_budget_usage
    ADD CONSTRAINT fk_fp_bu FOREIGN KEY (financialyearid) REFERENCES financialyear(id);
    
CREATE SEQUENCE seq_egf_budgetdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 99999999999999
    CACHE 1;
CREATE SEQUENCE seq_egf_budgetgroup
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 99999999999999
    CACHE 1;
    
CREATE SEQUENCE seq_egf_reappropriation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;    
CREATE SEQUENCE seq_egf_reappropriation_misc
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE seq_egf_budget_usage
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;



  
    --------------------------

CREATE TABLE egf_account_cheques (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    fromchequenumber character varying(50) NOT NULL,
    tochequenumber character varying(50) NOT NULL,
    receiveddate timestamp without time zone NOT NULL,
    isexhausted bigint,
    nextchqno character varying(50),
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    serialno character varying(16)
);
CREATE SEQUENCE seq_egf_account_cheques
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egf_account_cheques
    ADD CONSTRAINT egf_account_cheques_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_account_cheques
    ADD CONSTRAINT fk_ba_chq FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);    
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
ALTER TABLE ONLY cheque_dept_mapping ADD CONSTRAINT chequedept_dept_fk FOREIGN KEY (allotedto) REFERENCES eg_department(id);
ALTER TABLE ONLY cheque_dept_mapping
    ADD CONSTRAINT chequedept_cheque_fk FOREIGN KEY (accountchequeid) REFERENCES egf_account_cheques(id);

-------------------END-------------------
CREATE TABLE egf_dishonorcheque (
    id bigint NOT NULL,
    instrumentheaderid bigint NOT NULL,
    originalvhid bigint NOT NULL,
    statusid bigint NOT NULL,
    bankcharges bigint,
    transactiondate timestamp without time zone NOT NULL,
    bankchargeglcodeid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    bankreferencenumber character varying(20),
    reversalvhid bigint,
    bankchargesvhid bigint,
    stateid bigint NOT NULL,
    bankreason character varying(50),
    instrumentdishonorreason character varying(50)
);
CREATE SEQUENCE seq_egf_dishonorchq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishonorcheque_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishcq_bcvh_fk FOREIGN KEY (bankchargesvhid) REFERENCES voucherheader(id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_cruse_fk FOREIGN KEY (createdby) REFERENCES eg_user(id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_insthead_fk FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_mbuse_fk FOREIGN KEY (modifiedby) REFERENCES eg_user(id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_ovh_fk FOREIGN KEY (originalvhid) REFERENCES voucherheader(id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_rvh_fk FOREIGN KEY (reversalvhid) REFERENCES voucherheader(id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_st_fk FOREIGN KEY (statusid) REFERENCES egw_status(id);
ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_state_fk FOREIGN KEY (stateid) REFERENCES eg_wf_states(id);

    
CREATE TABLE egf_dishonorcheque_detail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    debitamt bigint,
    creditamt bigint,
    detailkey bigint,
    detailtype bigint,
    functionid bigint
);
    
CREATE SEQUENCE seq_egf_dishonorchqdet
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishonorcheque_detail_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqde_head_fk FOREIGN KEY (headerid) REFERENCES egf_dishonorcheque(id);
ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_acdet_fk FOREIGN KEY (detailtype) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_acdetky_fk FOREIGN KEY (detailkey) REFERENCES accountdetailkey(id);
ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_glcode_fk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);
    
    
    
CREATE TABLE ecstype (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    debitamt bigint,
    creditamt bigint,
    detailkey bigint,
    detailtype bigint,
    functionid bigint
);


CREATE TABLE egf_recovery_bankdetails (
    id bigint NOT NULL,
    tds_id bigint NOT NULL,
    fund_id bigint NOT NULL,
    bank_id bigint NOT NULL,
    branch_id bigint NOT NULL,
    bankaccount_id bigint NOT NULL
);
CREATE SEQUENCE seq_egf_recovery_bankdetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;
ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT egf_recovery_bankdetails_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT fk_recbank_acc FOREIGN KEY (bankaccount_id) REFERENCES bankaccount(id);
ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT fk_recbank_bank FOREIGN KEY (bank_id) REFERENCES bank(id);
ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT fk_recbank_branch FOREIGN KEY (branch_id) REFERENCES bankbranch(id);
ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT fk_recbank_fund FOREIGN KEY (fund_id) REFERENCES fund(id);
ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT fk_recbank_tds FOREIGN KEY (tds_id) REFERENCES tds(id);
-----------------------------------------------END-------------------------
----------------START-------------------
    
    CREATE TABLE egf_accountcode_purpose (
    id bigint NOT NULL,
    name character varying(250),
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint
);
----------------------END-------------------    
