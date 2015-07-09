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

CREATE TABLE functionary (
    id bigint NOT NULL,
    code bigint NOT NULL,
    name character varying(256) NOT NULL,
    createtimestamp timestamp without time zone,
    updatetimestamp timestamp without time zone,
    isactive smallint,
    module_name character varying(60)
);


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




CREATE TABLE fundsource (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    type character varying(50),
    parentid bigint,
    isactive smallint NOT NULL,
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



CREATE TABLE eg_designation (
    designationid bigint NOT NULL,
    deptid bigint,
    designation_name character varying(256) NOT NULL,
    designation_local character varying(256),
    officer_level bigint,
    designation_description character varying(1024),
    sanctioned_posts bigint,
    outsourced_posts bigint,
    basic_from bigint,
    basic_to bigint,
    ann_increment bigint,
    reportsto bigint,
    grade_id bigint,
    glcodeid bigint
);


CREATE TABLE eg_drawingofficer (
    id bigint NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(150),
    id_bank bigint,
    id_branch bigint,
    account_number character varying(20),
    tan character varying(10)
);


CREATE TABLE eg_emp_assignment (
    id bigint NOT NULL,
    id_fund bigint,
    id_function bigint,
    designationid bigint,
    id_functionary bigint,
    pct_allocation character varying(256),
    reports_to bigint,
    id_emp_assign_prd bigint,
    field bigint,
    main_dept bigint,
    position_id bigint,
    govt_order_no character varying(256),
    grade_id bigint,
    is_primary character varying(1) DEFAULT 'Y'::character varying NOT NULL
);


CREATE TABLE eg_emp_assignment_prd (
    id bigint NOT NULL,
    from_date timestamp without time zone,
    to_date timestamp without time zone,
    id_employee bigint
);

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
    is_avail_quarters smallint DEFAULT 2
);

CREATE VIEW eg_eis_employeeinfo AS
 SELECT eea.id AS ass_id,
    eap.id AS prd_id,
    ee.id,
    ee.code,
    (((((ee.emp_firstname)::text || ' '::text) || (ee.emp_middlename)::text) || ' '::text) || (ee.emp_lastname)::text) AS name,
    eea.designationid,
    eap.from_date,
    eap.to_date,
    eea.reports_to,
    ee.date_of_first_appointment AS date_of_fa,
    ee.isactive,
    eea.main_dept AS dept_id,
    eea.id_functionary AS functionary_id,
    eea.position_id AS pos_id,
    ee.id_user AS user_id,
    ee.status,
    ee.employment_status AS employee_type,
    eea.is_primary,
    eea.id AS ass_id_unique,
    eea.id_function AS function_id
   FROM eg_emp_assignment_prd eap,
    eg_emp_assignment eea,
    eg_employee ee
  WHERE ((ee.id = eap.id_employee) AND (eap.id = eea.id_emp_assign_prd));


CREATE TABLE eg_employee_dept (
    deptid smallint,
    id bigint NOT NULL,
    assignment_id bigint,
    hod bigint
);

CREATE TABLE eg_surrendered_cheques (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    chequenumber character varying(20) NOT NULL,
    chequedate timestamp without time zone NOT NULL,
    vhid bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);

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

CREATE TABLE eg_uomcategory (
    id bigint NOT NULL,
    category character varying(30) NOT NULL,
    narration character varying(250),
    lastmodified timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);

CREATE TABLE eg_view (
    complaintnumber character varying(32),
    userid bigint,
    dateofview timestamp without time zone
);


CREATE TABLE egdms_notification (
    id bigint NOT NULL,
    file_id bigint NOT NULL,
    position_id bigint NOT NULL
);


CREATE TABLE egw_satuschange (
    id bigint NOT NULL,
    moduletype character varying(20) NOT NULL,
    moduleid bigint NOT NULL,
    fromstatus bigint,
    tostatus bigint NOT NULL,
    createdby bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    remarks character varying(256)
);



CREATE TABLE egdms_notification_file (
    id bigint NOT NULL,
    sender bigint NOT NULL
);

CREATE TABLE egdms_notification_group (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(250),
    eff_date timestamp without time zone NOT NULL,
    active character(1) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL
);

CREATE TABLE egdms_notification_user (
    position_id bigint NOT NULL,
    group_id bigint NOT NULL
);

CREATE TABLE egeis_grade_mstr (
    grade_id bigint NOT NULL,
    grade_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    age bigint
);


CREATE TABLE scheme (
    id bigint NOT NULL,
    code character varying(20),
    name character varying(50),
    validfrom timestamp without time zone,
    validto timestamp without time zone,
    isactive character varying(1),
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



CREATE TABLE financial_institution (
    id bigint NOT NULL,
    name character varying(250) NOT NULL
);


CREATE TABLE egw_contractor_grade (
    id bigint NOT NULL,
    grade character varying(20) NOT NULL,
    description character varying(100) NOT NULL,
    min_amount double precision NOT NULL,
    max_amount double precision NOT NULL,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    CONSTRAINT sys_c0010480 CHECK ((min_amount >= (0)::double precision)),
    CONSTRAINT sys_c0010481 CHECK ((max_amount >= (0)::double precision))
);



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


CREATE TABLE voucherheader (
    id bigint NOT NULL,
    cgn character varying(50) NOT NULL,
    cgdate timestamp without time zone NOT NULL,
    name character varying(50) NOT NULL,
    type character varying(100) NOT NULL,
    description character varying(1024),
    effectivedate timestamp without time zone NOT NULL,
    vouchernumber character varying(30),
    voucherdate timestamp without time zone NOT NULL,
    departmentid bigint,
    fundid bigint,
    fiscalperiodid bigint NOT NULL,
    status smallint,
    originalvcid bigint,
    fundsourceid bigint,
    isconfirmed smallint DEFAULT 0,
    createdby bigint,
    functionid smallint,
    refcgno character varying(10),
    cgvn character varying(50) NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    moduleid bigint,
    state_id bigint
);

CREATE TABLE egf_record_status (
    id bigint NOT NULL,
    record_type character varying(50) NOT NULL,
    status bigint NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    userid bigint NOT NULL,
    voucherheaderid bigint NOT NULL
);

CREATE TABLE egeis_community_mstr (
    community_id bigint NOT NULL,
    community_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);

CREATE TABLE egeis_elig_cert_type (
    id bigint NOT NULL,
    type character varying(64),
    description character varying(64)
);


CREATE TABLE egeis_bloodgroup (
    id bigint NOT NULL,
    value character varying(10),
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_religion_mstr (
    religion_id bigint NOT NULL,
    religion_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);



CREATE TABLE egeis_mode_of_recruiment_mstr (
    mode_of_recruiment_id bigint NOT NULL,
    mode_of_recruiment_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);

CREATE TABLE egeis_relation_type (
    id bigint NOT NULL,
    relation_type character varying(64),
    full_benefit_eligible smallint,
    gender character varying(1),
    eligible_age bigint,
    elig_status_if_married character varying(1),
    elig_status_if_employed character varying(1),
    narration character varying(256)
);


CREATE TABLE egeis_category_mstr (
    category_id bigint NOT NULL,
    category_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);


CREATE TABLE egeis_leave_status (
    status character varying(256),
    id smallint NOT NULL
);


CREATE TABLE egeis_local_lang_qul_mstr (
    qulified_id bigint NOT NULL,
    qulified_name character varying(256) NOT NULL,
    end_date timestamp without time zone,
    start_date timestamp without time zone
);


CREATE TABLE egeis_service_history (
    id bigint NOT NULL,
    emp_id bigint,
    comment_date timestamp without time zone,
    comments character varying(256),
    reason character varying(256),
    order_no character varying(256),
    doc_no bigint
);

CREATE TABLE accountdetailkey (
    id bigint NOT NULL,
    groupid bigint NOT NULL,
    glcodeid bigint,
    detailtypeid bigint NOT NULL,
    detailname character varying(50) NOT NULL,
    detailkey bigint NOT NULL
);


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


CREATE TABLE accountgroup (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    nbroflevels bigint NOT NULL
);


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

CREATE TABLE bankaccount (
    id bigint NOT NULL,
    branchid bigint NOT NULL,
    accountnumber character varying(20) NOT NULL,
    accounttype character varying(150) NOT NULL,
    narration character varying(250),
    isactive smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    modifiedby bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    glcodeid bigint,
    currentbalance double precision NOT NULL,
    fundid bigint,
    payto character varying(100),
    type character varying(50)
);


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
    micr bigint
);


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


CREATE TABLE bankentries_mis (
    id bigint NOT NULL,
    bankentries_id bigint,
    function_id bigint
);


CREATE TABLE bankreconciliation (
    id bigint NOT NULL,
    bankaccountid bigint,
    amount double precision,
    transactiontype character varying(2) NOT NULL,
    instrumentheaderid bigint
);


CREATE TABLE chartofaccountdetail (
    id bigint NOT NULL,
    glcodeid bigint NOT NULL,
    detailtypeid bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone
);

CREATE TABLE chartofaccounts (
    id bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    name character varying(150) NOT NULL,
    description character varying(250),
    isactiveforposting smallint NOT NULL,
    parentid bigint,
    lastmodified timestamp without time zone,
    modifiedby smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    purposeid bigint,
    operation character(1),
    type character(1) NOT NULL,
    class smallint,
    classification smallint,
    functionreqd bigint,
    budgetcheckreq smallint,
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


CREATE TABLE cheque_dept_mapping (
    id bigint NOT NULL,
    allotedto bigint NOT NULL,
    accountchequeid bigint NOT NULL
);

CREATE TABLE eg_object_history (
    id bigint NOT NULL,
    object_type_id bigint,
    modifed_by bigint,
    object_id bigint,
    remarks character varying(4000),
    modifieddate timestamp without time zone
);
CREATE TABLE eg_rulegroup (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone,
    roleid bigint NOT NULL
);

CREATE TABLE eg_rgrule_map (
    rulegroupid bigint NOT NULL,
    ruleid bigint NOT NULL
);

CREATE TABLE eg_rules (
    id bigint NOT NULL,
    name character varying(50),
    defaultvalue character varying(50),
    minrange bigint,
    maxrange bigint,
    updatedtime timestamp without time zone,
    type character varying(30),
    active bigint,
    included smallint,
    excluded smallint
);

CREATE TABLE eg_ruletype (
    id bigint NOT NULL,
    name character varying(50),
    updatedtime timestamp without time zone
);
CREATE TABLE eg_tasks (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone
);

CREATE TABLE eg_ielist (
    id bigint NOT NULL,
    ruleid bigint NOT NULL,
    value character varying(40) NOT NULL,
    type character(1) NOT NULL,
    updatedtime timestamp without time zone
);


CREATE TABLE eg_entity (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone
);



CREATE TABLE eg_actionrg_map (
    actionid bigint NOT NULL,
    rulegroupid bigint NOT NULL
);

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

CREATE TABLE egf_accountcode_purpose (
    id bigint NOT NULL,
    name character varying(250),
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint
);

CREATE TABLE egeis_deptdesig
(
  id bigint NOT NULL,
  desig_id integer NOT NULL,
  dept_id integer NOT NULL,
  outsourced_posts integer,
  sanctioned_posts integer,
  CONSTRAINT egeis_deptdesig_pkey PRIMARY KEY (id),
  CONSTRAINT egeis_deptdesig_desig_id_dept_id_key UNIQUE (desig_id, dept_id)
);



CREATE VIEW v_egeis_empdetails AS
 SELECT ee.code AS empcode,
    ee.emp_firstname AS employeename,
    desig.designation_name AS designation,
    eap.from_date AS employeeassignfromdate,
    eap.to_date AS employeeassigntodate,
    dept.dept_name AS department,
    func.name AS section
   FROM eg_emp_assignment_prd eap,
    eg_emp_assignment eea,
    eg_employee ee,
    eg_designation desig,
    eg_department dept,
    functionary func
  WHERE ((((((ee.id = eap.id_employee) AND (eap.id = eea.id_emp_assign_prd)) AND (eea.designationid = desig.designationid)) AND (ee.isactive = 1)) AND ((eea.main_dept)::numeric = dept.id_dept)) AND (func.id = eea.id_functionary));




CREATE SEQUENCE functionary_seq;

CREATE SEQUENCE seq_eg_ielist;

CREATE SEQUENCE seq_eg_entity;

CREATE SEQUENCE seq_eg_surrendered_cheques;

CREATE SEQUENCE seq_eg_tasks;

CREATE SEQUENCE seq_sub_scheme;

CREATE SEQUENCE seq_eg_disbursement_mode;

CREATE SEQUENCE seq_functionary;

CREATE SEQUENCE seq_egf_record_status;

CREATE SEQUENCE seq_egdms_generic_file;

CREATE SEQUENCE seq_egdms_intnl_user;

CREATE SEQUENCE seq_egdms_notification;

CREATE SEQUENCE seq_egdms_notification_groupaction;

CREATE SEQUENCE seq_egdms_notification_user;

CREATE SEQUENCE seq_function;

CREATE SEQUENCE seq_fund;

CREATE SEQUENCE seq_fundsource;

CREATE SEQUENCE SEQ_EG_RULEGROUP;
CREATE SEQUENCE SEQ_DESIGNATION;
CREATE SEQUENCE  SEQ_DRAWING_OFFICER;

CREATE SEQUENCE seq_eg_emp_assignment;

CREATE SEQUENCE seq_eg_emp_assignment_prd;

CREATE SEQUENCE SEQ_ASS_DEPT;

CREATE SEQUENCE seq_eg_partytype;


CREATE SEQUENCE seq_eg_rules;

CREATE SEQUENCE seq_eg_ruletype;

CREATE SEQUENCE seq_eg_uom;

CREATE SEQUENCE seq_eg_uomcategory;

CREATE SEQUENCE seq_egw_satuschange;

CREATE SEQUENCE egpims_grade_mstr_seq;

CREATE SEQUENCE seq_financial_institution;

CREATE SEQUENCE egw_contractor_grade_seq;

CREATE SEQUENCE seq_voucherheader;

CREATE SEQUENCE egpims_community_seq;

CREATE SEQUENCE egeis_elig_cert_type_seq;

CREATE SEQUENCE egeis_blood_seq;

CREATE SEQUENCE egpims_religion_seq;

CREATE SEQUENCE egpims_mode_of_recruiment_seq;

CREATE SEQUENCE egpims_relation_seq;

CREATE SEQUENCE egpims_cat_seq;

CREATE SEQUENCE seq_accountdetailkey;

CREATE SEQUENCE seq_accountdetailtype;

CREATE SEQUENCE seq_accountentitymaster;

CREATE SEQUENCE seq_accountgroup;

CREATE SEQUENCE seq_bank;

CREATE SEQUENCE seq_bankaccount;

CREATE SEQUENCE seq_bankbranch;

CREATE SEQUENCE seq_bankentries;

CREATE SEQUENCE seq_bankentries_mis;

CREATE SEQUENCE seq_bankreconciliation;

CREATE SEQUENCE seq_chartofaccountdetail;

CREATE SEQUENCE seq_chartofaccounts;

CREATE SEQUENCE seq_cheque_dept_mapping;

CREATE SEQUENCE seq_fiscalperiod ;
   
CREATE SEQUENCE seq_schedulemapping;

CREATE SEQUENCE seq_egf_accountcode_purpose;
CREATE SEQUENCE egpims_personal_info_seq;

create sequence SEQ_ASS_PRD;
create sequence SEQ_ASS;
create sequence SEQ_EIS_DEPTDESIG;


