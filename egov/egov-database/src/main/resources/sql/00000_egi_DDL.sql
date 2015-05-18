CREATE TABLE eg_department (
    id_dept numeric NOT NULL,
    dept_name character varying(64) NOT NULL,
    dept_details character varying(128),
    updatetime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    dept_code character varying(520),
    dept_addr character varying(250),
    isbillinglocation bigint,
    parentid bigint,
    isleaf bigint
);

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

CREATE TABLE eg_action (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    entityid bigint,
    taskid bigint,
    updatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    url character varying(150),
    queryparams character varying(150),
    urlorderid bigint,
    module_id bigint,
    order_number bigint,
    display_name character varying(80),
    is_enabled smallint,
    action_help_url character varying(255),
    context_root character varying(32)
);


CREATE TABLE eg_address (
    addressid bigint NOT NULL,
    streetaddress1 character varying(512),
    streetaddress2 character varying(512),
    block character varying(512),
    locality character varying(512),
    citytownvillage character varying(512),
    district character varying(512),
    state character varying(512),
    pincode bigint,
    streetaddress1local character varying(512),
    streeraddress2local character varying(512),
    blocklocal character varying(512),
    localitylocal character varying(512),
    citytownvillagelocal character varying(512),
    districtlocal character varying(512),
    statelocal character varying(512),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_addresstypemaster bigint,
    talukname character varying(256),
    taluklocal character varying(256),
    houseno character varying(32)
);
CREATE TABLE eg_address_type_master (
    id_address_type bigint NOT NULL,
    name_address_type character varying(256) NOT NULL,
    name_address_type_local character varying(256),
    updatedtimestamp timestamp without time zone,
    narration character varying(256)
);
CREATE TABLE eg_appconfig (
    id bigint NOT NULL,
    key_name character varying(250) NOT NULL,
    description character varying(250) NOT NULL,
    module character varying(250) NOT NULL
);

CREATE TABLE eg_appconfig_values (
    id bigint NOT NULL,
    key_id bigint NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    value character varying(4000) NOT NULL
);
CREATE TABLE eg_audit_event (
    id bigint NOT NULL,
    pkid bigint,
    module character varying(100) NOT NULL,
    action character varying(100) NOT NULL,
    entity character varying(100) NOT NULL,
    fqcn character varying(100) NOT NULL,
    bizid character varying(100) NOT NULL,
    username character varying(100) NOT NULL,
    details1 character varying(4000) NOT NULL,
    details2 character varying(4000),
    eventdate timestamp without time zone NOT NULL
);
CREATE TABLE eg_authorization_rule (
    id bigint NOT NULL,
    actionid bigint,
    object_type character varying(256),
    scriptid bigint
);
CREATE TABLE eg_boundary (
    id_bndry bigint NOT NULL,
    bndry_num bigint,
    parent bigint,
    name character varying(512) NOT NULL,
    id_bndry_type bigint NOT NULL,
    updatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    bndry_name_local character varying(256),
    bndry_name_old character varying(256),
    bndry_name_old_local character varying(256),
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL,
    bndryid bigint,
    lng double precision,
    lat double precision,
    materialized_path character varying(32)
);
CREATE TABLE eg_boundary_type (
    id_bndry_type bigint NOT NULL,
    hierarchy bigint NOT NULL,
    parent bigint,
    name character varying(64) NOT NULL,
    updatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    id_heirarchy_type bigint NOT NULL,
    bndryname_local character varying(64)
);
CREATE TABLE eg_checklists (
    id bigint NOT NULL,
    appconfig_values_id bigint NOT NULL,
    checklistvalue character varying(5) NOT NULL,
    object_id bigint NOT NULL,
    lastmodifieddate timestamp without time zone
);
CREATE TABLE eg_citizen (
    citizenid bigint NOT NULL,
    ssn character varying(512),
    pannumber character varying(256),
    passportnumber character varying(256),
    drivinglicencenumber character varying(256),
    rationcardnumber character varying(256),
    voterregistrationnumber character varying(256),
    firstname character varying(512) NOT NULL,
    middlename character varying(512),
    lastname character varying(512),
    birthdate timestamp without time zone,
    homephone character varying(32),
    officephone character varying(32),
    mobilephone character varying(32),
    fax character varying(32),
    emailaddress character varying(64),
    occupation character varying(256),
    jobstatus character varying(128),
    locale character varying(256),
    firstnamelocal character varying(512),
    lastnamelocal character varying(512),
    ownertitle character varying(64),
    ownertitlelocal character varying(64),
    sex character varying(16),
    middlenamelocal character varying(512),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    fathername character varying(256),
    creationtimestamp timestamp without time zone,
    mnicnumber character varying(32),
    updateuserid integer,
    userid integer
);
CREATE TABLE eg_city_website (
    url character varying(128) NOT NULL,
    bndryid integer NOT NULL,
    cityname character varying(256) NOT NULL,
    citynamelocal character varying(256),
    isactive bigint DEFAULT 1,
    id bigint NOT NULL,
    logo character varying(100)
);

CREATE TABLE eg_crossheirarchy_linkage (
    id bigint NOT NULL,
    parent bigint NOT NULL,
    child bigint NOT NULL
);

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
CREATE TABLE eg_favourites (
    id bigint,
    user_id bigint,
    action_id bigint,
    fav_name character varying(100),
    ctx_name character varying(50)
);
CREATE TABLE eg_heirarchy_type (
    id_heirarchy_type bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    type_code character varying(50) NOT NULL
);
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

CREATE TABLE eg_location_ipmap (
    id bigint NOT NULL,
    locationid bigint NOT NULL,
    ipaddress character varying(150) NOT NULL
);
CREATE TABLE eg_login_log (
    id bigint NOT NULL,
    userid bigint NOT NULL,
    logintime timestamp without time zone,
    logouttime timestamp without time zone,
    locationid bigint,
    ipaddress character varying(15)
);
CREATE TABLE eg_module (
    id_module bigint NOT NULL,
    module_name character varying(100) NOT NULL,
    lastupdatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    isenabled smallint,
    module_namelocal character varying(128),
    baseurl character varying(256),
    parentid bigint,
    module_desc character varying(256),
    order_num bigint
);

CREATE TABLE eg_modules (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(250)
);
CREATE TABLE eg_number_generic (
    id bigint NOT NULL,
    objecttype character varying(50) NOT NULL,
    value bigint NOT NULL,
    updatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);
CREATE TABLE eg_numbers (
    id bigint NOT NULL,
    vouchertype character varying(50) NOT NULL,
    vouchernumber bigint NOT NULL,
    fiscialperiodid bigint NOT NULL,
    month bigint
);

CREATE TABLE eg_object_type (
    id bigint NOT NULL,
    type character varying(20) NOT NULL,
    description character varying(50),
    lastmodifieddate timestamp without time zone NOT NULL
);

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

 
CREATE TABLE eg_position (
    position_name character varying(256) NOT NULL,
    id bigint NOT NULL,
    sanctioned_posts bigint,
    outsourced_posts bigint,
    desig_id bigint,
    effective_date timestamp without time zone,
    id_drawing_officer bigint,
    id_deptdesig bigint,
    ispost_outsourced smallint
);

CREATE TABLE eg_position_hir (
    id bigint NOT NULL,
    position_from bigint,
    position_to bigint,
    object_type_id bigint
);

CREATE TABLE eg_roleaction_map (
    roleid bigint,
    actionid bigint
);

CREATE TABLE eg_roles (
    id_role bigint NOT NULL,
    role_name character varying(32) NOT NULL,
    role_desc character varying(128),
    role_name_local character varying(64),
    role_desc_local character varying(128),
    updatetime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    updateuserid bigint
);
CREATE TABLE eg_script (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    script_type character varying(256) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    script text,
    start_date timestamp without time zone DEFAULT '1900-01-01 00:00:00'::timestamp without time zone NOT NULL,
    end_date timestamp without time zone DEFAULT '2100-01-01 00:00:00'::timestamp without time zone NOT NULL,
    CONSTRAINT check_start_end CHECK ((start_date <= end_date))
);

CREATE TABLE eg_terminal (
    id bigint NOT NULL,
    terminal_name character varying(16),
    ip_address character varying(16),
    terminal_desc character varying(64)
);

CREATE TABLE eg_user (
    id_user bigint NOT NULL,
    title character varying(8),
    salutation character varying(5),
    first_name character varying(32) NOT NULL,
    middle_name character varying(32),
    last_name character varying(32),
    dob timestamp without time zone,
    id_department bigint,
    locale character varying(16),
    user_name character varying(64) NOT NULL,
    pwd character varying(64) NOT NULL,
    pwd_reminder character varying(64),
    updatetime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    updateuserid bigint,
    extrafield1 character varying(64),
    extrafield2 character varying(64),
    extrafield3 character varying(64),
    extrafield4 character varying(64),
    is_suspended character(1) DEFAULT 'N'::bpchar NOT NULL,
    id_top_bndry bigint DEFAULT 1,
    reportsto bigint,
    isactive bigint DEFAULT 1 NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    user_sign bigint,
    pwd_updated_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    organizationid bigint,
    mobilenumber character varying(50),
    alternatenumber character varying(50),
    addressid bigint,
    email character varying(128),
    isportaluser smallint DEFAULT 0
);

CREATE TABLE eg_user_jurlevel (
    id_user_jurlevel bigint NOT NULL,
    id_user bigint NOT NULL,
    id_bndry_type bigint NOT NULL,
    updatetime timestamp without time zone NOT NULL
);

CREATE TABLE eg_user_jurvalues (
    id_user_jurlevel bigint NOT NULL,
    id_bndry bigint NOT NULL,
    id bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL
);

CREATE TABLE eg_user_sign (
    id bigint NOT NULL,
    user_signature bytea,
    digi_signature bytea
);

CREATE TABLE eg_usercounter_map (
    id bigint NOT NULL,
    userid bigint NOT NULL,
    counterid bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);

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

CREATE TABLE eg_userrole (
    id_role bigint NOT NULL,
    id_user bigint NOT NULL,
    id bigint NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL
);

CREATE TABLE eg_wf_actions (
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(1024) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone
);

CREATE TABLE eg_wf_amountrule (
    id bigint NOT NULL,
    fromqty bigint,
    toqty bigint,
    ruledesc character varying(30)
);

CREATE TABLE eg_wf_matrix (
    id bigint NOT NULL,
    department character varying(30),
    objecttype character varying(30) NOT NULL,
    currentstate character varying(30),
    currentstatus character varying(30),
    pendingactions character varying(512),
    currentdesignation character varying(512),
    additionalrule character varying(50),
    nextstate character varying(30),
    nextaction character varying(100),
    nextdesignation character varying(512),
    nextstatus character varying(30),
    validactions character varying(512) NOT NULL,
    fromqty bigint,
    toqty bigint
);
CREATE TABLE eg_wf_states (
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    owner bigint,
    date1 timestamp without time zone,
    date2 timestamp without time zone,
    text1 character varying(1024),
    text2 character varying(1024),
    previous bigint,
    next bigint,
    next_action character varying(255)
);

CREATE TABLE eg_wf_types (
    id_type bigint NOT NULL,
    module_id bigint NOT NULL,
    wf_type character varying(100) NOT NULL,
    wf_link character varying(255) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    render_yn character varying(1),
    group_yn character varying(1),
    full_qualified_name character varying(255) NOT NULL,
    display_name character varying(100) NOT NULL
);

CREATE TABLE eg_appl_domain (
    id bigint NOT NULL,
    name character varying(128) NOT NULL,
    description character varying(50)
);


CREATE TABLE egw_status (
    id bigint NOT NULL,
    moduletype character varying(50) NOT NULL,
    description character varying(50),
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    code character varying(30),
    order_id bigint
);


CREATE VIEW v_eg_role_action_module_map AS
 SELECT m.id_module AS module_id,
    m.module_desc AS module_name,
    m.parentid AS parent_id,
    NULL::bigint AS action_id,
    NULL::character varying AS action_name,
    NULL::text AS action_url,
    m.order_num AS order_number,
    'M'::text AS typeflag,
    m.isenabled AS is_enabled,
    NULL::character varying AS context_root
   FROM eg_module m
UNION
 SELECT NULL::bigint AS module_id,
    NULL::character varying AS module_name,
    a.module_id AS parent_id,
    a.id AS action_id,
    a.display_name AS action_name,
    ((a.url)::text ||
        CASE
            WHEN (a.queryparams IS NULL) THEN ''::text
            ELSE ('?'::text || (a.queryparams)::text)
        END) AS action_url,
    a.order_number,
    'A'::text AS typeflag,
    a.is_enabled,
    a.context_root
   FROM eg_action a;

CREATE SEQUENCE eg_audit_event_seq;

CREATE SEQUENCE eg_favourites_seq;

CREATE SEQUENCE eg_script_seq;

CREATE SEQUENCE eg_wf_actions_seq;

CREATE SEQUENCE eg_wf_amountrule_seq;

CREATE SEQUENCE eg_wf_matrix_seq;

CREATE SEQUENCE eg_wf_states_seq;

CREATE SEQUENCE eg_wf_types_seq;

CREATE SEQUENCE seq_eg_taluk;

CREATE SEQUENCE seq_calendaryear;

CREATE SEQUENCE seq_companydetail;

CREATE SEQUENCE seq_eg_action;
CREATE SEQUENCE seq_eg_address;

CREATE SEQUENCE seq_eg_appconfig;

CREATE SEQUENCE seq_eg_appconfig_values;

CREATE SEQUENCE seq_eg_bndry;

CREATE SEQUENCE seq_eg_bndry_type;

CREATE SEQUENCE seq_eg_checklists;

CREATE SEQUENCE seq_eg_crossheirarchy_linkage;

CREATE SEQUENCE seq_eg_digital_signed_docs;

CREATE SEQUENCE seq_eg_heirarchy_type;

CREATE SEQUENCE seq_eg_location;

CREATE SEQUENCE seq_eg_location_ipmap;

CREATE SEQUENCE seq_eg_login_log;

CREATE SEQUENCE seq_eg_number_generic;

CREATE SEQUENCE seq_eg_numbers;

CREATE SEQUENCE seq_object_history;

CREATE SEQUENCE seq_object_type;

CREATE SEQUENCE seq_pos;

CREATE SEQUENCE seq_position_hir;

CREATE SEQUENCE seq_eg_roleaction_map;

CREATE SEQUENCE seq_eg_roles;

CREATE SEQUENCE seq_eg_user;

CREATE SEQUENCE seq_eg_user_jurlevel;

CREATE SEQUENCE seq_eg_user_jurvalues;

CREATE SEQUENCE seq_eg_user_sign;

CREATE SEQUENCE seq_eg_usercounter_map;

CREATE SEQUENCE seq_eg_userrole;

CREATE SEQUENCE seq_eg_department;
  
CREATE SEQUENCE seq_eg_dept;

CREATE SEQUENCE SEQ_MODULEMASTER
START WITH 300
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE seq_eg_modules;

CREATE SEQUENCE seq_egw_status
    START WITH 542
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


