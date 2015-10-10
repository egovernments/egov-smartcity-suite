------------------START------------------
CREATE TABLE eg_filestoremap (
    id bigint NOT NULL,
    filestoreid character varying(36) NOT NULL,
    filename character varying(100) NOT NULL,
    contenttype character varying(100),
    version bigint
);
CREATE SEQUENCE seq_eg_filestoremap
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_filestoremap ADD CONSTRAINT pk_filestoremap PRIMARY KEY (id);
ALTER TABLE ONLY eg_filestoremap ADD CONSTRAINT uk_filestoremap_filestoreid UNIQUE (filestoreid);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_user (
    id bigint NOT NULL,
    title character varying(8),
    salutation character varying(5),
    dob timestamp without time zone,
    locale character varying(16),
    username character varying(64) NOT NULL,
    password character varying(64) NOT NULL,
    pwdexpirydate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    mobilenumber character varying(50),
    altcontactnumber character varying(50),
    emailid character varying(128),
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    active boolean,
    name character varying(100),
    gender smallint,
    pan character varying(10),
    aadhaarnumber character varying(20),
    type character varying(50),
    version numeric DEFAULT 0,
    guardian character varying(100),
    guardianrelation character varying(32)
);
CREATE SEQUENCE seq_eg_user
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_user ADD CONSTRAINT eg_user_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_user ADD CONSTRAINT eg_user_user_name_key UNIQUE (username);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_module (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    enabled boolean,
    contextroot character varying(10),
    parentmodule bigint,
    displayname character varying(50),
    ordernumber bigint
);
CREATE SEQUENCE seq_eg_module
    START WITH 300
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_module ADD CONSTRAINT eg_module_module_name_key UNIQUE (name);
ALTER TABLE ONLY eg_module ADD CONSTRAINT eg_module_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_module ADD CONSTRAINT fk_eg_module_parentmodule FOREIGN KEY (parentmodule) REFERENCES eg_module(id);
-------------------END-------------------

------------------START-------------------
CREATE TABLE eg_action (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    url character varying(150),
    queryparams character varying(150),
    parentmodule bigint NOT NULL,
    ordernumber bigint,
    displayname character varying(80),
    enabled boolean,
    contextroot character varying(32),
    version numeric DEFAULT 0,
    createdby numeric DEFAULT 1,
    createddate timestamp without time zone DEFAULT now(),
    lastmodifiedby numeric DEFAULT 1,
    lastmodifieddate timestamp without time zone DEFAULT now(),
    application bigint NOT NULL
);
CREATE SEQUENCE seq_eg_action
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_action ADD CONSTRAINT eg_action_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_action ADD CONSTRAINT eg_action_url_queryparams_context_root_key UNIQUE (url, queryparams, contextroot);
ALTER TABLE ONLY eg_action ADD CONSTRAINT fk_eg_action_parentmodule FOREIGN KEY (parentmodule) REFERENCES eg_module(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_script (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    type character varying(256) NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    script text,
    startdate timestamp without time zone DEFAULT '1900-01-01 00:00:00'::timestamp without time zone NOT NULL,
    enddate timestamp without time zone DEFAULT '2100-01-01 00:00:00'::timestamp without time zone NOT NULL,
    version bigint,
    CONSTRAINT check_start_end CHECK ((startdate <= enddate))
);
CREATE SEQUENCE seq_eg_script
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_script ADD CONSTRAINT eg_script_name_start_date_end_date_key UNIQUE (name, startdate, enddate);
ALTER TABLE ONLY eg_script ADD CONSTRAINT eg_script_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_address (
    housenobldgapt character varying(32),
    subdistrict character varying(100),
    postoffice character varying(100),
    landmark character varying(256),
    country character varying(50),
    userid bigint,
    type character varying(50),
    streetroadline character varying(256),
    citytownvillage character varying(256),
    arealocalitysector character varying(256),
    district character varying(100),
    state character varying(100),
    pincode character varying(10),
    id numeric NOT NULL,
    version bigint DEFAULT 0
);
CREATE SEQUENCE seq_eg_address
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_address ADD CONSTRAINT eg_address_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_appconfig (
    id bigint NOT NULL,
    key_name character varying(250) NOT NULL,
    description character varying(250) NOT NULL,
    version bigint,
    createdby bigint,
    lastmodifiedby bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    module bigint NOT NULL
);
CREATE SEQUENCE seq_eg_appconfig
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_appconfig ADD CONSTRAINT eg_appconfig_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_appconfig ADD CONSTRAINT uk_keyname_module_unique UNIQUE (key_name, module);
ALTER TABLE ONLY eg_appconfig ADD CONSTRAINT fk_eg_appconfig_moduleid FOREIGN KEY (module) REFERENCES eg_module(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_appconfig_values (
    id bigint NOT NULL,
    key_id bigint NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    value character varying(4000) NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint
);
CREATE SEQUENCE seq_eg_appconfig_values
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_appconfig_values ADD CONSTRAINT eg_appconfig_values_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_appconfig_values ADD CONSTRAINT fk_appdata_key FOREIGN KEY (key_id) REFERENCES eg_appconfig(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_applicationindex (
    id bigint NOT NULL,
    modulename character varying(50) NOT NULL,
    applicationnumber character varying(50) NOT NULL,
    applicationdate date NOT NULL,
    applicationtype character varying(150) NOT NULL,
    applicantname character varying(100) NOT NULL,
    applicantaddress character varying(250),
    disposaldate date,
    ulbname character varying(250) NOT NULL,
    districtname character varying(250),
    status character varying(50) NOT NULL,
    url character varying(250) NOT NULL,
    consumercode character varying(50),
    mobilenumber character varying(15),
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    version numeric NOT NULL
);
CREATE SEQUENCE seq_eg_applicationindex
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_applicationindex ADD CONSTRAINT eg_applicationindex_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_hierarchy_type (
    id bigint NOT NULL,
    name character varying(128) NOT NULL,
    code character varying(50) NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint,
    localname character varying(256)
);
CREATE SEQUENCE seq_eg_hierarchy_type
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_hierarchy_type ADD CONSTRAINT eg_heirarchy_type_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_hierarchy_type ADD CONSTRAINT eg_heirarchy_type_type_code_key UNIQUE (code);
ALTER TABLE ONLY eg_hierarchy_type ADD CONSTRAINT eg_heirarchy_type_type_name_key UNIQUE (name);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_boundary_type (
    id bigint NOT NULL,
    hierarchy bigint NOT NULL,
    parent bigint,
    name character varying(64) NOT NULL,
    hierarchytype bigint NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint,
    localname character varying(64)
);
CREATE SEQUENCE seq_eg_boundary_type
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_boundary_type ADD CONSTRAINT eg_boundary_type_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_boundary_type ADD CONSTRAINT bndry_type_heirarchy_fk FOREIGN KEY (hierarchytype) REFERENCES eg_hierarchy_type(id); 
ALTER TABLE ONLY eg_boundary_type ADD CONSTRAINT bndry_type_parent FOREIGN KEY (parent) REFERENCES eg_boundary_type(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_boundary (
    id bigint NOT NULL,
    boundarynum bigint,
    parent bigint,
    name character varying(512) NOT NULL,
    boundarytype bigint NOT NULL,
    localname character varying(256),
    bndry_name_old character varying(256),
    bndry_name_old_local character varying(256),
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    bndryid bigint,
    longitude double precision,
    latitude double precision,
    materializedpath character varying(32),
    ishistory boolean,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint
);
CREATE SEQUENCE seq_eg_boundary
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_boundary ADD CONSTRAINT eg_boundary_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_boundary ADD CONSTRAINT bndry_type_fk FOREIGN KEY (boundarytype) REFERENCES eg_boundary_type(id); 
CREATE INDEX indx_eb_bndrytypeid ON eg_boundary USING btree (boundarytype);
-------------------END-------------------
------------------START------------------
CREATE TABLE eg_crossheirarchy (
    id bigint NOT NULL,
    parent bigint NOT NULL,
    child bigint NOT NULL,
    version bigint
);
CREATE SEQUENCE seq_eg_crossheirarchy
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_crossheirarchy
    ADD CONSTRAINT eg_crossheirarchy_pkey PRIMARY KEY (id);
-------------------END-------------------
------------------START------------------
CREATE TABLE eg_citypreferences (
    id numeric NOT NULL,
    giskml bigint,
    municipalitylogo bigint,
    createdby numeric,
    createddate timestamp without time zone,
    lastmodifiedby numeric,
    lastmodifieddate timestamp without time zone,
    version numeric,
    municipalityname character varying(50),
    municipalitycontactno character varying(20),
    municipalityaddress character varying(200),
    municipalitycontactemail character varying(50),
    municipalitygislocation character varying(100),
    municipalitycallcenterno character varying(20),
    municipalityfacebooklink character varying(100),
    municipalitytwitterlink character varying(100)
);
CREATE SEQUENCE seq_eg_citypreferences
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_citypreferences ADD CONSTRAINT eg_citypreferences_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_citypreferences ADD CONSTRAINT eg_citypreferences_giskml_fkey FOREIGN KEY (giskml) REFERENCES eg_filestoremap(id); 
ALTER TABLE ONLY eg_citypreferences ADD CONSTRAINT eg_citypreferences_logo_fkey FOREIGN KEY (municipalitylogo) REFERENCES eg_filestoremap(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_city (
    domainurl character varying(128) NOT NULL,
    name character varying(256) NOT NULL,
    localname character varying(256),
    id bigint NOT NULL,
    active boolean,
    version bigint,
    createdby numeric,
    lastmodifiedby numeric,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    code character varying(4),
    recaptchapk character varying(64),
    districtcode character varying(10),
    districtname character varying(50),
    longitude double precision,
    latitude double precision,
    preferences numeric,
    recaptchapub character varying(64)
);
CREATE SEQUENCE seq_eg_city
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_city ADD CONSTRAINT eg_city_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_city ADD CONSTRAINT fk_preference FOREIGN KEY (preferences) REFERENCES eg_citypreferences(id) MATCH FULL;
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_city_aud (
    id integer NOT NULL,
    rev integer NOT NULL,
    name character varying(256),
    localname character varying(256),
    active boolean,
    domainurl character varying(128),
    recaptchapk character varying(64),
    recaptchapub character varying(64),
    code character varying(4),
    districtcode character varying(10),
    districtname character varying(50),
    longitude double precision,
    latitude double precision,
    revtype numeric
);
ALTER TABLE ONLY eg_city_aud ADD CONSTRAINT eg_city_aud_pkey PRIMARY KEY (id, rev);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_correspondence_address (
    id numeric,
    version numeric DEFAULT 0
);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_crosshierarchy (
    id bigint NOT NULL,
    parent bigint NOT NULL,
    child bigint NOT NULL,
    parenttype bigint,
    childtype bigint,
    version bigint default 0
);

CREATE SEQUENCE seq_eg_crosshierarchy
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_crosshierarchy ADD CONSTRAINT eg_crosshierarchy_pkey PRIMARY KEY (id);
alter table eg_crosshierarchy add constraint fk_crossheirarchy_parenttype foreign key (parenttype) references eg_boundary_type (id);
alter table eg_crosshierarchy add constraint fk_crossheirarchy_childtype foreign key (childtype) references eg_boundary_type (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_department (
    id numeric NOT NULL,
    name character varying(64) NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    code character varying(520),
    createdby bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    version bigint
);
CREATE SEQUENCE seq_eg_department
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_department ADD CONSTRAINT eg_department_dept_code_key UNIQUE (code);
ALTER TABLE ONLY eg_department ADD CONSTRAINT eg_department_dept_name_key UNIQUE (name);
ALTER TABLE ONLY eg_department ADD CONSTRAINT eg_department_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_device (
    id bigint NOT NULL,
    deviceuid character varying(128) NOT NULL,
    type character varying(32) NOT NULL,
    osversion character varying(32),
    createddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    version bigint
);
CREATE SEQUENCE seq_eg_device
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_device ADD CONSTRAINT eg_device_device_id_key UNIQUE (deviceuid);
ALTER TABLE ONLY eg_device ADD CONSTRAINT eg_device_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_favourites (
    id bigint NOT NULL,
    userid bigint,
    actionid bigint,
    name character varying(100),
    contextroot character varying(50),
    version bigint
);
CREATE SEQUENCE seq_eg_favourites
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_favourites ADD CONSTRAINT eg_favourites_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_identityrecovery (
    id numeric NOT NULL,
    token character varying(36),
    userid bigint,
    expiry timestamp without time zone,
    version bigint
);
CREATE SEQUENCE seq_eg_identityrecovery
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_identityrecovery ADD CONSTRAINT eg_identityrecovery_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_identityrecovery ADD CONSTRAINT eg_identityrecovery_token_key UNIQUE (token);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_messagetemplate (
    id numeric NOT NULL,
    templatename character varying(100) NOT NULL,
    template character varying NOT NULL,
    locale character varying(10),
    version bigint
);
CREATE SEQUENCE seq_eg_messagetemplate
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_messagetemplate ADD CONSTRAINT eg_messagetemplate_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_messagetemplate ADD CONSTRAINT eg_messagetemplate_templatename_key UNIQUE (templatename);
-------------------END-------------------

------------------START------------------
--Obsolete
CREATE TABLE eg_number_generic (
    id bigint NOT NULL,
    objecttype character varying(50) NOT NULL,
    value bigint NOT NULL,
    updatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);
--obsolete
CREATE SEQUENCE seq_eg_number_generic
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--Obsolete
CREATE TABLE eg_numbers (
    id bigint NOT NULL,
    vouchertype character varying(50) NOT NULL,
    vouchernumber bigint NOT NULL,
    fiscialperiodid bigint NOT NULL,
    month bigint
);

--obsolete
CREATE SEQUENCE seq_eg_numbers
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-------------------END-------------------

------------------START------------------
CREATE TABLE eg_permanent_address (
    id numeric,
    version numeric DEFAULT 0
);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_role (
    id bigint NOT NULL,
    name character varying(32) NOT NULL,
    description character varying(128),
    createddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    version bigint
);
CREATE SEQUENCE seq_eg_role
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_role ADD CONSTRAINT eg_roles_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_role ADD CONSTRAINT eg_roles_role_name_key UNIQUE (name);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_roleaction (
    roleid bigint NOT NULL,
    actionid bigint NOT NULL
);
CREATE INDEX indx_eram_actionid ON eg_roleaction USING btree (actionid);
CREATE INDEX indx_eram_roleid ON eg_roleaction USING btree (roleid);
ALTER TABLE ONLY eg_roleaction ADD CONSTRAINT fk_action_id FOREIGN KEY (actionid) REFERENCES eg_action(id); 
ALTER TABLE ONLY eg_roleaction ADD CONSTRAINT fk_role_id FOREIGN KEY (roleid) REFERENCES eg_role(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_systemaudit (
    id numeric NOT NULL,
    userid bigint,
    ipaddress character varying(20),
    useragentinfo character varying(200),
    logintime timestamp without time zone,
    logouttime timestamp without time zone,
    version numeric
);
CREATE SEQUENCE seq_eg_systemaudit
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_systemaudit ADD CONSTRAINT eg_systemaudit_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_token (
    id bigint NOT NULL,
    tokennumber character varying(128) NOT NULL,
    tokenidentity character varying(100),
    service character varying(100),
    ttlsecs bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    version bigint
);
CREATE SEQUENCE seq_eg_token
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_token ADD CONSTRAINT pk_token PRIMARY KEY (id);
CREATE INDEX idx_token_number ON eg_token USING btree (tokennumber);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_userdevice (
    userid bigint NOT NULL,
    deviceid bigint NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone
);
ALTER TABLE ONLY eg_userdevice ADD CONSTRAINT fk_user_userdevice FOREIGN KEY (userid) REFERENCES eg_user(id);
ALTER TABLE ONLY eg_userdevice ADD CONSTRAINT fk_userdevice FOREIGN KEY (deviceid) REFERENCES eg_device(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_userrole (
    roleid bigint NOT NULL,
    userid bigint NOT NULL
);
ALTER TABLE ONLY eg_userrole ADD CONSTRAINT fk_role_userrole FOREIGN KEY (roleid) REFERENCES eg_role(id);
ALTER TABLE ONLY eg_userrole ADD CONSTRAINT fk_user_userrole FOREIGN KEY (userid) REFERENCES eg_user(id);
-------------------END-------------------

------------------START------------------
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
CREATE SEQUENCE eg_wf_actions_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_wf_actions ADD CONSTRAINT eg_wf_actions_name_type_key UNIQUE (name, type);
ALTER TABLE ONLY eg_wf_actions ADD CONSTRAINT eg_wf_actions_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_amountrule (
    id bigint NOT NULL,
    fromqty bigint,
    toqty bigint,
    ruledesc character varying(30)
);
CREATE SEQUENCE eg_wf_amountrule_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_wf_amountrule ADD CONSTRAINT eg_wf_amountrule_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_matrix (
    id bigint NOT NULL,
    department character varying(30),
    objecttype character varying(30) NOT NULL,
    currentstate character varying(100),
    currentstatus character varying(30),
    pendingactions character varying(512),
    currentdesignation character varying(512),
    additionalrule character varying(50),
    nextstate character varying(100),
    nextaction character varying(100),
    nextdesignation character varying(512),
    nextstatus character varying(30),
    validactions character varying(512) NOT NULL,
    fromqty bigint,
    toqty bigint,
    fromdate date,
    todate date
);
CREATE SEQUENCE eg_wf_matrix_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_wf_matrix ADD CONSTRAINT eg_wf_matrix_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_state_history (
    id bigint NOT NULL,
    state_id bigint NOT NULL,
    value character varying(255) NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    owner_pos bigint,
    owner_user bigint,
    dateinfo timestamp without time zone,
    extradateinfo timestamp without time zone,
    sendername character varying(100),
    comments character varying(1024),
    extrainfo character varying(1024),
    nextaction character varying(255)
);
CREATE SEQUENCE seq_eg_wf_state_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_wf_state_history ADD CONSTRAINT eg_wf_states_history_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_states (
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    dateinfo timestamp without time zone,
    extradateinfo timestamp without time zone,
    comments character varying(1024),
    extrainfo character varying(1024),
    nextaction character varying(255),
    owner_pos bigint,
    owner_user bigint,
    sendername character varying(100),
    status numeric(1,0),
    version bigint
);
CREATE SEQUENCE seq_eg_wf_states
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_wf_states ADD CONSTRAINT eg_wf_states_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE TABLE eg_wf_types (
    id bigint NOT NULL,
    module bigint NOT NULL,
    type character varying(100) NOT NULL,
    link character varying(255) NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    renderyn character varying(1),
    groupyn character varying(1),
    typefqn character varying(255) NOT NULL,
    displayname character varying(100) NOT NULL,
    version bigint
);
CREATE SEQUENCE seq_eg_wf_types
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY eg_wf_types ADD CONSTRAINT eg_wf_types_pkey PRIMARY KEY (id);
ALTER TABLE ONLY eg_wf_types ADD CONSTRAINT eg_wf_types_wf_type_key UNIQUE (type);
ALTER TABLE ONLY eg_wf_types ADD CONSTRAINT sys_c0010396 FOREIGN KEY (module) REFERENCES eg_module(id); 
-------------------END-------------------

------------------START------------------
CREATE TABLE qrtz_calendars (
    sched_name character varying(120) NOT NULL,
    calendar_name character varying(200) NOT NULL,
    calendar bytea NOT NULL
);
ALTER TABLE ONLY qrtz_calendars ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);

CREATE TABLE qrtz_fired_triggers (
    sched_name character varying(120) NOT NULL,
    entry_id character varying(95) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    instance_name character varying(200) NOT NULL,
    fired_time bigint NOT NULL,
    sched_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(200),
    job_group character varying(200),
    is_nonconcurrent boolean,
    requests_recovery boolean
);
ALTER TABLE ONLY qrtz_fired_triggers ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);
CREATE INDEX idx_qrtz_ft_trig_inst_name ON qrtz_fired_triggers USING btree (sched_name, instance_name);
CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON qrtz_fired_triggers USING btree (sched_name, instance_name, requests_recovery);
CREATE INDEX idx_qrtz_ft_j_g ON qrtz_fired_triggers USING btree (sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_ft_jg ON qrtz_fired_triggers USING btree (sched_name, job_group);
CREATE INDEX idx_qrtz_ft_t_g ON qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);
CREATE INDEX idx_qrtz_ft_tg ON qrtz_fired_triggers USING btree (sched_name, trigger_group);

CREATE TABLE qrtz_job_details (
    sched_name character varying(120) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    job_class_name character varying(250) NOT NULL,
    is_durable boolean NOT NULL,
    is_nonconcurrent boolean NOT NULL,
    is_update_data boolean NOT NULL,
    requests_recovery boolean NOT NULL,
    job_data bytea
);
ALTER TABLE ONLY qrtz_job_details ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_j_grp ON qrtz_job_details USING btree (sched_name, job_group);
CREATE INDEX idx_qrtz_j_req_recovery ON qrtz_job_details USING btree (sched_name, requests_recovery);

CREATE TABLE qrtz_locks (
    sched_name character varying(120) NOT NULL,
    lock_name character varying(40) NOT NULL
);
ALTER TABLE ONLY qrtz_locks ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);

CREATE TABLE qrtz_paused_trigger_grps (
    sched_name character varying(120) NOT NULL,
    trigger_group character varying(200) NOT NULL
);
ALTER TABLE ONLY qrtz_paused_trigger_grps ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);

CREATE TABLE qrtz_scheduler_state (
    sched_name character varying(120) NOT NULL,
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);
ALTER TABLE ONLY qrtz_scheduler_state ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);

CREATE TABLE qrtz_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(200),
    misfire_instr smallint,
    job_data bytea
);
ALTER TABLE ONLY qrtz_triggers ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE ONLY qrtz_triggers ADD CONSTRAINT qrtz_triggers_sched_name_fkey FOREIGN KEY (sched_name, job_name, job_group) REFERENCES qrtz_job_details(sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_t_c ON qrtz_triggers USING btree (sched_name, calendar_name);
CREATE INDEX idx_qrtz_t_g ON qrtz_triggers USING btree (sched_name, trigger_group);
CREATE INDEX idx_qrtz_t_j ON qrtz_triggers USING btree (sched_name, job_name, job_group);
CREATE INDEX idx_qrtz_t_jg ON qrtz_triggers USING btree (sched_name, job_group);
CREATE INDEX idx_qrtz_t_n_g_state ON qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_n_state ON qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_next_fire_time ON qrtz_triggers USING btree (sched_name, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_st ON qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);
CREATE INDEX idx_qrtz_t_nft_st_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);
CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);
CREATE INDEX idx_qrtz_t_state ON qrtz_triggers USING btree (sched_name, trigger_state);

CREATE TABLE qrtz_blob_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    blob_data bytea
);
ALTER TABLE ONLY qrtz_blob_triggers ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE ONLY qrtz_blob_triggers ADD CONSTRAINT qrtz_blob_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);

CREATE TABLE qrtz_cron_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    cron_expression character varying(120) NOT NULL,
    time_zone_id character varying(80)
);
ALTER TABLE ONLY qrtz_cron_triggers ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE ONLY qrtz_cron_triggers ADD CONSTRAINT qrtz_cron_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group); 

CREATE TABLE qrtz_simple_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);
ALTER TABLE ONLY qrtz_simple_triggers ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE ONLY qrtz_simple_triggers ADD CONSTRAINT qrtz_simple_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);

CREATE TABLE qrtz_simprop_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    str_prop_1 character varying(512),
    str_prop_2 character varying(512),
    str_prop_3 character varying(512),
    int_prop_1 integer,
    int_prop_2 integer,
    long_prop_1 bigint,
    long_prop_2 bigint,
    dec_prop_1 numeric(13,4),
    dec_prop_2 numeric(13,4),
    bool_prop_1 boolean,
    bool_prop_2 boolean
);
ALTER TABLE ONLY qrtz_simprop_triggers ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE ONLY qrtz_simprop_triggers ADD CONSTRAINT qrtz_simprop_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group); 
-------------------END-------------------

------------------START------------------
CREATE TABLE revinfo (
    id integer NOT NULL,
    "timestamp" bigint,
    userid bigint,
    ipaddress character varying(20)
);
CREATE SEQUENCE revinfo_rev_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE revinfo_rev_seq OWNED BY revinfo.id;
ALTER TABLE ONLY revinfo ALTER COLUMN id SET DEFAULT nextval('revinfo_rev_seq'::regclass);
ALTER TABLE ONLY revinfo ADD CONSTRAINT revinfo_pkey PRIMARY KEY (id);
-------------------END-------------------

------------------START------------------
CREATE VIEW view_eg_menulink AS
 SELECT m.id AS module_id,
    m.displayname AS module_name,
    m.parentmodule AS parent_id,
    NULL::bigint AS action_id,
    NULL::character varying AS action_name,
    NULL::text AS action_url,
    m.ordernumber AS order_number,
    'M'::text AS typeflag,
    m.enabled AS is_enabled,
    NULL::character varying AS context_root
   FROM eg_module m
UNION
 SELECT NULL::bigint AS module_id,
    NULL::character varying AS module_name,
    a.parentmodule AS parent_id,
    a.id AS action_id,
    a.displayname AS action_name,
    ((a.url)::text ||
        CASE
            WHEN (a.queryparams IS NULL) THEN ''::text
            ELSE ('?'::text || (a.queryparams)::text)
        END) AS action_url,
    a.ordernumber AS order_number,
    'A'::text AS typeflag,
    a.enabled AS is_enabled,
    a.contextroot AS context_root
   FROM eg_action a;
