-------------START----------------
CREATE TABLE egeis_employeetype (
    id bigint NOT NULL,
    name character varying(256),
    version bigint,
    lastmodifieddate timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    chartofaccounts bigint
);
CREATE SEQUENCE seq_egeis_employeetype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egeis_employeetype
    ADD CONSTRAINT pk_egeis_employeetype_id PRIMARY KEY (id);
ALTER TABLE ONLY egeis_employeetype
    ADD CONSTRAINT fk_egeis_emptype_coa FOREIGN KEY (chartofaccounts) REFERENCES chartofaccounts(id);
    
-----------------_END--------------
-----------------START--------------
CREATE TABLE egeis_employee (
    id numeric NOT NULL,
    code character varying(256),
    dateofappointment date,
    dateofretirement date,
    employeestatus character varying(16),
    employeetype bigint,
    version numeric DEFAULT 0
);
ALTER TABLE ONLY egeis_employee
    ADD CONSTRAINT pk_egeis_employee_id PRIMARY KEY (id);
--------------------------------_END--------------------

-----------------START--------------
CREATE TABLE egeis_grade_mstr (
    grade_id bigint NOT NULL,
    grade_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    age bigint,
    order_no integer
);
CREATE SEQUENCE egpims_grade_mstr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_grade_value_key UNIQUE (grade_value);
ALTER TABLE ONLY egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_pkey PRIMARY KEY (grade_id);
------------------END------------
------------------------START------------------
CREATE TABLE egeis_jurisdiction (
    id bigint NOT NULL,
    employee bigint NOT NULL,
    boundarytype bigint NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint,
    boundary bigint
);
CREATE SEQUENCE seq_egeis_jurisdiction
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ALTER TABLE ONLY egeis_jurisdiction
    ADD CONSTRAINT pk_egeis_jurisdiction_id PRIMARY KEY (id);   
ALTER TABLE ONLY egeis_jurisdiction
    ADD CONSTRAINT fk_egeis_jurisdiction_boundary FOREIGN KEY (boundary) REFERENCES eg_boundary(id);
 ALTER TABLE ONLY egeis_jurisdiction
    ADD CONSTRAINT fk_egeis_jurisdiction_boundarytype FOREIGN KEY (boundarytype) REFERENCES eg_boundary_type(id);
ALTER TABLE ONLY egeis_jurisdiction
    ADD CONSTRAINT fk_egeis_jurisdiction_employee FOREIGN KEY (employee) REFERENCES egeis_employee(id);
------------_END------------------
--------------START-------------------
CREATE TABLE egeis_leave_status (
    status character varying(256),
    id smallint NOT NULL
);
ALTER TABLE ONLY egeis_leave_status
    ADD CONSTRAINT egeis_leave_status_pkey PRIMARY KEY (id);
    
    
------------_END------------------
--------------START-------------------
CREATE TABLE egeis_local_lang_qul_mstr (
    qulified_id bigint NOT NULL,
    qulified_name character varying(256) NOT NULL,
    end_date timestamp without time zone,
    start_date timestamp without time zone
);

ALTER TABLE ONLY egeis_local_lang_qul_mstr
    ADD CONSTRAINT egeis_local_lang_qul_mstr_pkey PRIMARY KEY (qulified_id);
    
------------_END------------------
--------------START-------------------
CREATE TABLE egeis_mode_of_recruiment_mstr (
    mode_of_recruiment_id bigint NOT NULL,
    mode_of_recruiment_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);
CREATE SEQUENCE egpims_mode_of_recruiment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egeis_mode_of_recruiment_mstr
    ADD CONSTRAINT egeis_mode_of_recruiment_mstr_pkey PRIMARY KEY (mode_of_recruiment_id);
    
 ------------_END------------------
--------------START-------------------   
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
CREATE SEQUENCE egpims_relation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egeis_relation_type
    ADD CONSTRAINT egeis_relation_type_pkey PRIMARY KEY (id);
------------_END------------------
--------------START-------------------
CREATE TABLE egeis_religion_mstr (
    religion_id bigint NOT NULL,
    religion_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);
ALTER TABLE ONLY egeis_religion_mstr
    ADD CONSTRAINT egeis_religion_mstr_pkey PRIMARY KEY (religion_id);
------------_END------------------
-----------------__START-------------------
CREATE TABLE egeis_position_hierarchy (
    id bigint NOT NULL,
    position_from bigint,
    position_to bigint,
    object_type_id bigint,
    object_sub_type character varying(512),
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    version bigint
);
CREATE SEQUENCE seq_egeis_position_hierarchy
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egeis_position_hierarchy
    ADD CONSTRAINT eg_position_hir_pkey PRIMARY KEY (id);
    ALTER TABLE ONLY egeis_position_hierarchy
    ADD CONSTRAINT eg_position_hir_position_from_position_to_object_type_subtype UNIQUE (position_from, position_to, object_type_id, object_sub_type);
    
------------_END------------------

-----------------__START-------------------
CREATE TABLE egeis_assignment (
    id bigint NOT NULL,
    fund bigint,
    function bigint,
    designation bigint,
    functionary bigint,
    department bigint,
    "position" bigint,
    grade bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    fromdate date,
    todate date,
    version bigint,
    employee bigint,
    isprimary boolean
);

CREATE SEQUENCE seq_egeis_assignment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egeis_assignment
    ADD CONSTRAINT eg_emp_assignment_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egeis_assignment
    ADD CONSTRAINT des_fk FOREIGN KEY (designation) REFERENCES eg_designation(id);
CREATE INDEX index_emp_assgn_designationid ON egeis_assignment USING btree (designation);
CREATE INDEX index_emp_assgn_id_function ON egeis_assignment USING btree (function);
CREATE INDEX index_emp_assgn_id_functionary ON egeis_assignment USING btree (functionary);
CREATE INDEX index_emp_assgn_id_fund ON egeis_assignment USING btree (fund);
CREATE INDEX index_emp_assgn_main_dept ON egeis_assignment USING btree (department);
CREATE INDEX index_emp_assgn_position_id ON egeis_assignment USING btree ("position");

ALTER TABLE ONLY egeis_assignment
    ADD CONSTRAINT function_fk FOREIGN KEY (function) REFERENCES function(id);
ALTER TABLE ONLY egeis_assignment
    ADD CONSTRAINT id_fund_fk FOREIGN KEY (fund) REFERENCES fund(id);
ALTER TABLE ONLY egeis_assignment
    ADD CONSTRAINT main_de FOREIGN KEY (department) REFERENCES eg_department(id);
ALTER TABLE ONLY egeis_assignment
    ADD CONSTRAINT pos_id FOREIGN KEY ("position") REFERENCES eg_position(id);

--------------END-----------------
----------------_START-----------------
CREATE TABLE egeis_employee_hod (
    id bigint NOT NULL,
    assignment bigint,
    hod bigint,
    version bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);
CREATE SEQUENCE seq_egeis_employee_hod
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egeis_employee_hod
    ADD CONSTRAINT eg_employee_dept_pkey PRIMARY KEY (id);  
CREATE INDEX index_emp_dept_assignment_id ON egeis_employee_hod USING btree (assignment);
CREATE INDEX index_emp_dept_hod ON egeis_employee_hod USING btree (hod);
ALTER TABLE ONLY egeis_employee_hod
    ADD CONSTRAINT ass_id FOREIGN KEY (assignment) REFERENCES egeis_assignment(id);
ALTER TABLE ONLY egeis_employee_hod
    ADD CONSTRAINT hod_id FOREIGN KEY (hod) REFERENCES eg_department(id);    
--------------END-----------------------
--------------------------START------------------------------
CREATE TABLE egeis_bloodgroup (
    id bigint NOT NULL,
    value character varying(10),
    start_date timestamp without time zone,
    end_date timestamp without time zone
);
CREATE SEQUENCE egeis_blood_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egeis_bloodgroup
    ADD CONSTRAINT egeis_bloodgroup_pkey PRIMARY KEY (id);
--------------------END-------------------------------
----------------START--------------
CREATE TABLE egeis_community_mstr (
    community_id bigint NOT NULL,
    community_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);
CREATE SEQUENCE egpims_community_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egeis_community_mstr
    ADD CONSTRAINT egeis_community_mstr_pkey PRIMARY KEY (community_id);
-----------END-------------    
----------------START--------------
CREATE TABLE egeis_category_mstr (
    category_id bigint NOT NULL,
    category_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    fileid bigint
);
CREATE SEQUENCE egpims_cat_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egeis_category_mstr
    ADD CONSTRAINT egeis_category_mstr_pkey PRIMARY KEY (category_id);


-----------END-------------  
----------------START--------------
CREATE TABLE egeis_elig_cert_type (
    id bigint NOT NULL,
    type character varying(64),
    description character varying(64)
);
CREATE SEQUENCE egeis_elig_cert_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egeis_elig_cert_type
    ADD CONSTRAINT egeis_elig_cert_type_pkey PRIMARY KEY (id);    
-----------------END---------------  

----------------START--------------
CREATE TABLE egeis_post_creation (
    id integer NOT NULL,
    post_name character varying(512) NOT NULL,
    desig_id integer NOT NULL,
    dept_id integer NOT NULL,
    status integer NOT NULL,
    state_id integer,
    qualify_details character varying(1024),
    position_id integer,
    remarks character varying(1024),
    createdby integer NOT NULL,
    createddate date NOT NULL,
    lastmodifieddate date NOT NULL,
    lastmodifiedby integer NOT NULL,
    version bigint
);
CREATE SEQUENCE seq_egeis_post_creation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egeis_post_creation
    ADD CONSTRAINT pk_eis_post_id PRIMARY KEY (id);
ALTER TABLE ONLY egeis_post_creation
    ADD CONSTRAINT fk_eis_post_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
ALTER TABLE ONLY egeis_post_creation
    ADD CONSTRAINT fk_eis_post_status FOREIGN KEY (status) REFERENCES egw_status(id);    
-----------------END---------------  

----------------START--------------
CREATE VIEW view_egeis_employee AS
 SELECT ea.id,
    ee.id AS employee,
    ee.code,
    eu.name,
    eu.username,
    eu.active AS useractive,
    ee.dateofappointment,
    ea.id AS assignment,
    ea.fromdate,
    ea.todate,
    ea.department,
    ea.designation,
    ea."position",
    ea.functionary,
    ea.function,
    ea.fund,
    ea.isprimary
   FROM ((eg_user eu
     JOIN egeis_employee ee ON (((eu.id)::numeric = ee.id)))
     JOIN egeis_assignment ea ON (((ea.employee)::numeric = ee.id)));
-----------------END---------------  

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (384, 'EIS', true, 'eis', NULL, 'Employee Management', 2);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (385, 'EIS Masters', true, 'eis', 384, 'Masters', 1);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (386, 'Employee', true, 'eis', 385, 'Employee', 1);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (387, 'Designation', true, 'eis', 385, 'Designation', 2);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (388, 'Position', true, 'eis', 385, 'Position', 3);
INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (398, 'EIS-COMMON', false, 'eis', 384, 'EIS-COMMON', NULL);

insert into eg_action values(nextval('seq_eg_action'),'EmpDesigAutoComplete','/employee/ajax/designations','',(select id from eg_module where name='Employee'),null,'EmpDesigAutoComplete',false,'eis',0,1,'2015-08-17 15:37:16.114851',1,'2015-08-17 15:37:16.114851',384);
insert into eg_action values(nextval('seq_eg_action'),'EmpPosAutoComplete','/employee/ajax/positions','',(select id from eg_module where name='Employee'),null,'EmpPosAutoComplete',false,'eis',0,1,'2015-08-17 15:37:16.114851',1,'2015-08-17 15:37:16.114851',384);
insert into eg_action values(nextval('seq_eg_action'),'Search Employee','/employee/search','',(select id from eg_module where name='Employee'),1,'Update/View',true,'eis',0,1,'2015-08-17 15:37:16.114851',1,'2015-08-17 15:37:16.114851',384);


insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='EmpDesigAutoComplete'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='EmpPosAutoComplete'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search Employee'));



insert into eg_action values(nextval('seq_eg_action'),'EmpMaster','/employeeMaster/employeeMaster-newForm.action',null,(select id from eg_module where name='EIS-COMMON'),4,'New Emp Create',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'Create Designation','/designation/create',null,(select id from eg_module where name='Designation'),null,'Create',true,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'View Designation','/designation/view',null,(select id from eg_module where name='Designation'),null,'View',true,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'Create Employee','/employee/create',null,(select id from eg_module where name='Employee'),null,'Create',true,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'Search Position','/position/search',null,(select id from eg_module where name='Position'),null,'Search',true,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'Create Position','/position/create',null,(select id from eg_module where name='Position'),null,'Create',true,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'Update Designation','/designation/update',null,(select id from eg_module where name='Designation'),null,'Update',true,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'load designation','/designation/ajax/result',null,(select id from eg_module where name='Designation'),null,'load designation',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'Ajax Call in Search Position','/position/resultList-update',null,(select id from eg_module where name='Position'),null,'Create',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AjaxDesignationsByDepartment','/ajaxWorkFlow-getDesignationsByObjectType',null,(select id from eg_module where name='EIS-COMMON'),1,'Approver Designations by Department','false','eis',0,1,to_timestamp('2015-08-15 11:04:03.817729','null'),1,to_timestamp('2015-08-15 11:04:03.817729','null'),384);
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'AjaxApproverByDesignationAndDepartment','/ajaxWorkFlow-positionsByDepartmentAndDesignation',null,(select id from eg_module where name='EIS-COMMON'),1,'Approver By Designation and Department','false','eis',0,1,to_timestamp('2015-08-15 11:04:03.817729','null'),1,to_timestamp('2015-08-15 11:04:03.817729','null'),384);

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='EmpMaster'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create Designation'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View Designation'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create Employee'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search Position'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create Position'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Designation'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='load designation'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Call in Search Position'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxDesignationsByDepartment'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxApproverByDesignationAndDepartment'));

insert into eg_action values(nextval('seq_eg_action'),'Update Position','/position/position-update',null,388,null,'Create',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'getAllDesigAjax','/common/employeeSearch-getAllDesignations.action',null,398,null,'getAllDesigAjax',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'getAllUnmappedUsers','/common/employeeSearch-getAllUnmappedUsers.action',null,398,null,'getAllUnmappedUsers',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'getPositionsForAssignment','/common/employeeSearch-getPositionsForDeptDesig.action',null,398,null,'getPositionsForAssignment',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'empMasterSave','/common/employeeSearch-save.action',null,398,null,'empMasterSave',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'Update Employee','/employee/update',null,(select id from eg_module where name='Employee'),null,'Update',false,'eis',0,1,'2015-08-17 15:37:16.114851',1,'2015-08-17 15:37:16.114851',384);
insert into eg_action values(nextval('seq_eg_action'),'View Employee','/employee/view',null,(select id from eg_module where name='Employee'),null,'View',false,'eis',0,1,'2015-08-17 15:37:16.114851',1,'2015-08-17 15:37:16.114851',384);

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Position'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='getAllDesigAjax'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='getAllUnmappedUsers'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='getPositionsForAssignment'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='empMasterSave'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Employee'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View Employee'));


insert into eg_action values(nextval('seq_eg_action'),'EmpSearchAjax','/employee/ajax/employees','',(select id from eg_module where name='Employee'),null,'EmpSearchAjax',false,'eis',0,1,'2015-08-17 15:37:17.518114',1,'2015-08-17 15:37:17.518114',384);
insert into eg_action values(nextval('seq_eg_action'),'empMasterModify','/common/employeeSearch-loadEmployee.action','mode=Modify&empId=',398,null,'empMasterModify',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'empMasterView','/common/employeeSearch-loadEmployee.action','mode=View&empId=',398,null,'empMasterView',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'empCodeUniqueCheck','/employeeMaster/employeeMaster-checkEmpCodeForUniqueness.action','',398,null,'empCodeUniqueCheck',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_action values(nextval('seq_eg_action'),'panNoUniqueCheck','/employeeMaster/employeeMaster-checkPanNoForUniqueness.action','',398,null,'panNoUniqueCheck',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);


insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='EmpSearchAjax'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='empMasterModify'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='empMasterView'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='empCodeUniqueCheck'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='panNoUniqueCheck'));

update eg_action set queryparams=null where queryparams='';

insert into eg_action values(nextval('seq_eg_action'),'Position count in Search Position','/position/position-getTotalPositionCount',null,(select id from eg_module where name='Position'),null,'search position',false,'eis',0,1,'2015-08-17 15:37:03.129586',1,'2015-08-17 15:37:03.129586',384);
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Position count in Search Position'));

INSERT INTO EG_ACTION (ID,NAME,createddate,URL,QUERYPARAMS,parentmodule,ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT, APPLICATION) VALUES (nextval('SEQ_EG_ACTION'), 'AjaxDesignationDropdown',current_date , '/workflow/ajaxWorkFlow-getDesignationsByObjectType.action', NULL, (select ID from eg_module where name like 'EIS-COMMON'), 6, 'AjaxDesignationDropdown', false, 'eis',(SELECT id FROM eg_module WHERE name='EIS' AND parentmodule IS NULL));
INSERT INTO EG_ACTION (ID,NAME,createddate,URL,QUERYPARAMS,parentmodule,ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT, APPLICATION) VALUES (nextval('SEQ_EG_ACTION'), 'AjaxApproverDropdown',current_date ,'/workflow/ajaxWorkFlow-getPositionByPassingDesigId.action', NULL,  (select ID from eg_module where name like 'EIS-COMMON'), 6, 'AjaxApproverDropdown', false, 'eis',(SELECT id FROM eg_module WHERE name='EIS' AND parentmodule IS NULL));

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxDesignationDropdown'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxApproverDropdown'));

----------------------------
