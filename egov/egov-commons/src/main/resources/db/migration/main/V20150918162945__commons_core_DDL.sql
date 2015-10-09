
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
ALTER TABLE accountentitymaster ALTER COLUMN isactive TYPE boolean USING CASE WHEN isactive = 0 THEN FALSE WHEN isactive = 1 THEN TRUE ELSE NULL END;

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
ALTER TABLE ONLY accountentitymaster ADD CONSTRAINT fk_userid_pk FOREIGN KEY (modifiedby) REFERENCES eg_user(id); 
ALTER TABLE ONLY accountentitymaster ADD CONSTRAINT fk_dt_aem FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);
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
CREATE TABLE bankaccount (
    id bigint NOT NULL,
    branchid bigint NOT NULL,
    accountnumber character varying(20) NOT NULL,
    accounttype character varying(150) NULL,
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
ALTER TABLE bankaccount ALTER COLUMN accounttype DROP NOT NULL;

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
ALTER TABLE ONLY bankaccount ADD CONSTRAINT fk_bb_ba FOREIGN KEY (branchid) REFERENCES bankbranch(id); 
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
ALTER TABLE ONLY bankreconciliation ADD CONSTRAINT fk_bacc_brs FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

-------------------END-------------------

----------------START---------
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

--------------------END------------------
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
    START WITH 1
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
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_coa_coa FOREIGN KEY (parentid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_cos_sch FOREIGN KEY (scheduleid) REFERENCES schedulemapping(id); 
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_cos_sch1 FOREIGN KEY (receiptscheduleid) REFERENCES schedulemapping(id); 
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fk_cos_sch2 FOREIGN KEY (paymentscheduleid) REFERENCES schedulemapping(id);
ALTER TABLE ONLY chartofaccounts ADD CONSTRAINT fiescheduleid_shedule_map_fk FOREIGN KEY (fiescheduleid) REFERENCES schedulemapping(id);

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
ALTER TABLE ONLY accountdetailkey ADD CONSTRAINT fk_coa_dk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY accountdetailkey ADD CONSTRAINT fk_dt_dk FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

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
ALTER TABLE ONLY chartofaccountdetail ADD CONSTRAINT fk_coadt FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY chartofaccountdetail ADD CONSTRAINT fk_dt_coa FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

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
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_designation ADD CONSTRAINT eg_designation_designation_name_key UNIQUE (name);
ALTER TABLE ONLY eg_designation ADD CONSTRAINT eg_designation_pkey PRIMARY KEY (id);

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
----------------START--------------
CREATE TABLE egeis_deptdesig (
    id bigint NOT NULL,
    designation integer NOT NULL,
    department integer NOT NULL,
    outsourcedposts integer,
    sanctionedposts integer,
    version bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_egeis_deptdesig
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT egeis_deptdesig_desig_id_dept_id_key UNIQUE (designation, department);
ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT egeis_deptdesig_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT fk_egeis_deptdesig_departmnt FOREIGN KEY (department) REFERENCES eg_department(id);
ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT fk_egeis_deptdesig_design FOREIGN KEY (designation) REFERENCES eg_designation(id);
 
-----------------END---------------  

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
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_position ADD CONSTRAINT eg_position_pkey PRIMARY KEY (id);
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
ALTER TABLE ONLY eg_drawingofficer ADD CONSTRAINT fk_eg_drawingofficer_position FOREIGN KEY ("position") REFERENCES eg_position(id);
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
ALTER TABLE ONLY fiscalperiod ADD CONSTRAINT fk_fy_fp FOREIGN KEY (financialyearid) REFERENCES financialyear(id);

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
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY function ADD CONSTRAINT function_code_key UNIQUE (code);
ALTER TABLE ONLY function ADD CONSTRAINT function_pkey PRIMARY KEY (id);
ALTER TABLE ONLY function ADD CONSTRAINT fk_function FOREIGN KEY (parentid) REFERENCES function(id);

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
    createdby bigint,
    transactioncreditamount double precision
);

CREATE SEQUENCE seq_fund
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY fund ADD CONSTRAINT fund_code_key UNIQUE (code);
ALTER TABLE ONLY fund ADD CONSTRAINT fund_pkey PRIMARY KEY (id);
CREATE INDEX indx_fund_purposeid ON fund USING btree (purpose_id);
ALTER TABLE ONLY fund ADD CONSTRAINT fk_fund1 FOREIGN KEY (parentid) REFERENCES fund(id);

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
ALTER TABLE ONLY sub_scheme ADD CONSTRAINT fk_sub_scheme_department FOREIGN KEY (department) REFERENCES eg_department(id); 
ALTER TABLE ONLY sub_scheme ADD CONSTRAINT sub_scheme_r01 FOREIGN KEY (schemeid) REFERENCES scheme(id);
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
ALTER TABLE ONLY fundsource ADD CONSTRAINT fin_source_bankaccount_fk FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY fundsource ADD CONSTRAINT fin_source_sub_scheme_fk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id); 
ALTER TABLE ONLY fundsource ADD CONSTRAINT fk_fs FOREIGN KEY (parentid) REFERENCES fundsource(id);
ALTER TABLE ONLY fundsource ADD CONSTRAINT fundsource_fin_inst_fk FOREIGN KEY (financialinstid) REFERENCES financial_institution(id);

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
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_fp_vh FOREIGN KEY (fiscalperiodid) REFERENCES fiscalperiod(id); 
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_fund_vh FOREIGN KEY (fundid) REFERENCES fund(id); 
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_voucher_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id); 
ALTER TABLE ONLY voucherheader ADD CONSTRAINT fk_voucherheader_vh FOREIGN KEY (originalvcid) REFERENCES voucherheader(id);
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
ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id); 
ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_schemeid FOREIGN KEY (schemeid) REFERENCES scheme(id); 
ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_subschemeidpk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);
ALTER TABLE ONLY vouchermis ADD CONSTRAINT fk_vmis_vhidpk FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id); 

--------------------END ---------------------------------

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
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id); 
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_coa_gl FOREIGN KEY (glcode) REFERENCES chartofaccounts(glcode);
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_fun_gl FOREIGN KEY (functionid) REFERENCES function(id);
ALTER TABLE ONLY generalledger ADD CONSTRAINT fk_voucherheader FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);

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
ALTER TABLE ONLY generalledgerdetail ADD CONSTRAINT fk_dt_gld FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);
ALTER TABLE ONLY generalledgerdetail ADD CONSTRAINT fk_gl_gld FOREIGN KEY (generalledgerid) REFERENCES generalledger(id);

-------------------END-------------------

------------------START-----------------
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
ALTER TABLE ONLY eg_surrendered_cheques ADD CONSTRAINT fk_surc_ba FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id); 
ALTER TABLE ONLY eg_surrendered_cheques ADD CONSTRAINT fk_surc_vh FOREIGN KEY (vhid) REFERENCES voucherheader(id);

-----------------------END-----------------------------
-------------------START--------------------

CREATE TABLE egw_status (
    id bigint NOT NULL,
    moduletype character varying(50) NOT NULL,
    description character varying(50),
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    code character varying(30),
    order_id bigint
);

CREATE SEQUENCE seq_egw_status
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egw_status
    ADD CONSTRAINT egw_status_pkey PRIMARY KEY (id);    
----------------END-------------

----------------START------------
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

---TODO remove receiptheader year wise sequences-
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

ALTER TABLE ONLY eg_object_history ADD CONSTRAINT fk_modified_by FOREIGN KEY (modifed_by) REFERENCES eg_user(id); 
ALTER TABLE ONLY eg_object_history ADD CONSTRAINT fk_object_type_id FOREIGN KEY (object_type_id) REFERENCES eg_object_type(id); 

