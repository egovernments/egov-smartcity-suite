------------------START------------------
CREATE TABLE egw_contractor
(
  id bigint NOT NULL,
  code character varying(50) NOT NULL,
  name character varying(100) NOT NULL,
  correspondence_address character varying(250),
  payment_address character varying(250),
  contact_person character varying(100),
  email character varying(100),
  narration character varying(1024),
  pan_number character varying(14),
  tin_number character varying(14),
  bank_id bigint,
  ifsc_code character varying(15),
  bank_account character varying(22),
  pwd_approval_code character varying(50),
  createdby bigint NOT NULL,
  modifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,
  edit_enabled character(1) DEFAULT '1'::bpchar,
  CONSTRAINT pk_contractor PRIMARY KEY (id),
  CONSTRAINT unq_contractor UNIQUE (code),
  CONSTRAINT fk_contractor_bank FOREIGN KEY (bank_id)
      REFERENCES bank (id),
  CONSTRAINT fk_contractor_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_contractor_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_contractor_bank ON egw_contractor USING btree (bank_id);

CREATE SEQUENCE SEQ_egw_contractor;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_contractor_detail
(
  id bigint NOT NULL,
  contractor_id bigint NOT NULL,
  department_id bigint,
  registration_number character varying(50),
  contractor_grade_id bigint,
  status_id bigint,
  startdate timestamp without time zone,
  enddate timestamp without time zone,
  createdby bigint NOT NULL,
  modifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,
  my_contractor_index bigint NOT NULL,
  CONSTRAINT pk_contractor_detail PRIMARY KEY (id),
  CONSTRAINT fk_contdtl_contractor FOREIGN KEY (contractor_id)
      REFERENCES egw_contractor (id),
  CONSTRAINT fk_contdtl_dept FOREIGN KEY (department_id)
      REFERENCES eg_department (id),
  CONSTRAINT fk_contdtl_grade FOREIGN KEY (contractor_grade_id)
      REFERENCES egw_contractor_grade (id),
  CONSTRAINT fk_contdtl_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_contdtl_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_contdtl_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_contdtl_grade ON egw_contractor_detail USING btree (contractor_grade_id);
CREATE INDEX idx_contdtl_contractor ON egw_contractor_detail USING btree (contractor_id);
CREATE INDEX idx_contdtl_department ON egw_contractor_detail USING btree (department_id);
CREATE INDEX idx_contdtl_status ON egw_contractor_detail USING btree (status_id);

CREATE SEQUENCE SEQ_egw_contractor_detail;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_schedulecategory
(
  id bigint NOT NULL,
  code character varying(255) NOT NULL,
  description character varying(4000) NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,  
  modified_by bigint,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_schedulecategory PRIMARY KEY (id),
  CONSTRAINT unq_schedulecategory_code UNIQUE (code),
  CONSTRAINT fk_schedulecategory_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_schedulecategory_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE SEQUENCE SEQ_egw_schedulecategory;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_scheduleofrate
(
  id bigint NOT NULL,
  code character varying(255) NOT NULL,  
  description character varying(4000),  
  sor_category_id bigint NOT NULL,
  uom_id bigint NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_scheduleofrate PRIMARY KEY (id),
  CONSTRAINT unq_scheduleofrate_code UNIQUE (code),
  CONSTRAINT fk_scheduleofrate_category FOREIGN KEY (sor_category_id)
      REFERENCES egw_schedulecategory (id),
  CONSTRAINT fk_scheduleofrate_uom FOREIGN KEY (uom_id)
      REFERENCES eg_uom (id),
  CONSTRAINT fk_scheduleofrate_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_scheduleofrate_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_sor_category ON egw_scheduleofrate USING btree (sor_category_id);
CREATE INDEX idx_sor_uom ON egw_scheduleofrate USING btree (uom_id);

CREATE SEQUENCE SEQ_egw_scheduleofrate;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_sor_rate
(
  id bigint NOT NULL,
  scheduleofrate_id bigint NOT NULL,
  value double precision NOT NULL,
  startdate timestamp without time zone,
  enddate timestamp without time zone,  
  my_sor_index bigint NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_sor_rate PRIMARY KEY (id),
  CONSTRAINT fk_scheduleofrate_sorrate FOREIGN KEY (scheduleofrate_id)
      REFERENCES egw_scheduleofrate (id),
  CONSTRAINT fk_sorrate_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_sorrate_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_sorrate_scheduleofrate ON egw_sor_rate USING btree (scheduleofrate_id);

CREATE SEQUENCE SEQ_egw_sor_rate;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_marketrate
(
  id bigint NOT NULL, 
  scheduleofrate_id bigint NOT NULL,
  value double precision NOT NULL,
  startdate timestamp without time zone,
  enddate timestamp without time zone, 
  market_sor_index bigint NOT NULL,
  created_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone NOT NULL,
  CONSTRAINT pk_egw_marketrate PRIMARY KEY (id),
  CONSTRAINT fk_marketrate_sor FOREIGN KEY (scheduleofrate_id)
      REFERENCES egw_scheduleofrate (id),
  CONSTRAINT fk_marketrate_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_marketrate_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_marketrate_sor ON egw_marketrate USING btree (scheduleofrate_id);

CREATE SEQUENCE SEQ_egw_marketrate;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_overhead
(
  id bigint NOT NULL, 
  name character varying(255) NOT NULL, 
  expenditure_type character varying(255) NOT NULL,
  accountcode_id bigint,
  description character varying(1024) NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone, 
  CONSTRAINT pk_overhead PRIMARY KEY (id),
  CONSTRAINT unq_overhead_name UNIQUE (name),
  CONSTRAINT fk_overhead_chartofaccounts FOREIGN KEY (accountcode_id)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_overhead_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_overhead_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_overhead_accountcode ON egw_overhead USING btree (accountcode_id);

CREATE SEQUENCE SEQ_egw_overhead;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_overhead_rate
(
  id bigint NOT NULL,
  overhead_id bigint NOT NULL,
  lumpsum_amount double precision,
  percentage double precision,
  startdate timestamp without time zone NOT NULL,
  enddate timestamp without time zone,
  my_ohr_index bigint NOT NULL,
  CONSTRAINT pk_overhead_rate PRIMARY KEY (id),
  CONSTRAINT fk_overhead_rate FOREIGN KEY (overhead_id)
      REFERENCES egw_overhead (id)
);

CREATE INDEX idx_overhead_rate ON egw_overhead_rate USING btree (overhead_id);

CREATE SEQUENCE SEQ_egw_overhead_rate;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_natureofwork
(
  id bigint NOT NULL,
  name character varying(128) NOT NULL,
  expenditure_type character varying(255) NOT NULL,
  code character varying(128) NOT NULL,
  CONSTRAINT pk_natureofwork PRIMARY KEY (id)
);

CREATE SEQUENCE SEQ_egw_natureofwork;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_depositcode
(
  id bigint NOT NULL,
  code character varying(256) NOT NULL,
  deposit_workname character varying(256) NOT NULL,
  description character varying(1024),
  natureofwork_id bigint,  
  fund_id bigint,
  functionary_id bigint,
  function_id bigint,
  scheme_id bigint,
  subscheme_id bigint,
  typeofwork_id bigint,
  subtypeofwork_id bigint,
  department_id bigint,
  ward_id bigint,
  zone_id bigint,
  financialyear_id bigint NOT NULL,
  fundsource_id bigint,
  isactive smallint,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone, 
  CONSTRAINT pk_depositcode PRIMARY KEY (id),
  CONSTRAINT unq_depositcode UNIQUE (code),
  CONSTRAINT fk_depcode_department FOREIGN KEY (department_id)
      REFERENCES eg_department (id),
  CONSTRAINT fk_depcode_financialyear FOREIGN KEY (financialyear_id)
      REFERENCES financialyear (id),
  CONSTRAINT fk_depcode_function FOREIGN KEY (function_id)
      REFERENCES function (id),
  CONSTRAINT fk_depcode_functionary FOREIGN KEY (functionary_id)
      REFERENCES functionary (id),
  CONSTRAINT fk_depcode_fund FOREIGN KEY (fund_id)
      REFERENCES fund (id),
  CONSTRAINT fk_depcode_fundsource FOREIGN KEY (fundsource_id)
      REFERENCES fundsource (id),
  CONSTRAINT fk_depcode_scheme FOREIGN KEY (scheme_id)
      REFERENCES scheme (id),
  CONSTRAINT fk_depcode_subscheme FOREIGN KEY (subscheme_id)
      REFERENCES sub_scheme (id), 
  CONSTRAINT fk_depcode_typeofwork FOREIGN KEY (typeofwork_id)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_depcode_subtypeofwork FOREIGN KEY (subtypeofwork_id)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_depcode_ward FOREIGN KEY (ward_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_depcode_workstype FOREIGN KEY (natureofwork_id)
      REFERENCES egw_natureofwork (id),
  CONSTRAINT fk_depcode_zone FOREIGN KEY (zone_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_depcode_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_depcode_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_depcode_department ON egw_depositcode USING btree (department_id);
CREATE INDEX idx_depcode_financialyear ON egw_depositcode USING btree (financialyear_id);
CREATE INDEX idx_depcode_function ON egw_depositcode USING btree (function_id);
CREATE INDEX idx_depcode_functionary ON egw_depositcode USING btree (functionary_id);
CREATE INDEX idx_depcode_fund ON egw_depositcode USING btree (fund_id);
CREATE INDEX idx_depcode_fundsource ON egw_depositcode USING btree (fundsource_id);
CREATE INDEX idx_depcode_scheme ON egw_depositcode USING btree (scheme_id);
CREATE INDEX idx_depcode_subscheme ON egw_depositcode USING btree (subscheme_id);
CREATE INDEX idx_depcode_typeofwork ON egw_depositcode USING btree (typeofwork_id);
CREATE INDEX idx_depcode_subtypeofwork ON egw_depositcode USING btree (subtypeofwork_id);
CREATE INDEX idx_depcode_ward ON egw_depositcode USING btree (ward_id);
CREATE INDEX idx_depcode_natureofwork ON egw_depositcode USING btree (natureofwork_id);
CREATE INDEX idx_depcode_zone ON egw_depositcode USING btree (zone_id);

CREATE SEQUENCE SEQ_EGW_depositcode;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_nonsor
(
  id bigint NOT NULL,
  description character varying(4000) NOT NULL,
  uom_id bigint NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_nonsor PRIMARY KEY (id),
  CONSTRAINT fk_nonsor_uom FOREIGN KEY (uom_id)
      REFERENCES eg_uom (id),
  CONSTRAINT fk_nonsor_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_nonsor_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_nonsor_uom ON egw_nonsor USING btree (uom_id);

CREATE SEQUENCE SEQ_EGW_nonsor;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_template
(
  id bigint NOT NULL,
  code character varying(100) NOT NULL,
  name character varying(256) NOT NULL,
  description character varying(1024),
  status bigint NOT NULL,
  worktype_id bigint NOT NULL,
  worksubtype_id bigint,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_estimate_template PRIMARY KEY (id),
  CONSTRAINT unq_estimate_template_code UNIQUE (code),
  CONSTRAINT fk_esttemplate_worktype FOREIGN KEY (worktype_id)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_esttemplate_worksubtype FOREIGN KEY (worksubtype_id)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_esttemplate_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_esttemplate_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_est_template_worktype ON egw_estimate_template USING btree (worktype_id);
CREATE INDEX idx_est_template_worksubtype ON egw_estimate_template USING btree (worksubtype_id);

CREATE SEQUENCE SEQ_EGW_estimate_template;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_est_template_activity
(
  id bigint NOT NULL,
  estimate_template_id bigint NOT NULL,
  scheduleofrate_id bigint,
  value double precision NOT NULL,
  nonsor_id bigint,
  uom_id bigint,
  template_activity_index bigint NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_est_template_activity PRIMARY KEY (id),
  CONSTRAINT fk_est_templ_act_nonsor FOREIGN KEY (nonsor_id)
      REFERENCES egw_nonsor (id),
  CONSTRAINT fk_est_templ_act_sor FOREIGN KEY (scheduleofrate_id)
      REFERENCES egw_scheduleofrate (id),
  CONSTRAINT fk_est_templ_act_templheader FOREIGN KEY (estimate_template_id)
      REFERENCES egw_estimate_template (id),
  CONSTRAINT fk_est_templ_act_uom FOREIGN KEY (uom_id)
      REFERENCES eg_uom (id),
  CONSTRAINT fk_est_templ_act_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_est_templ_act_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_est_temp_act_est_temp ON egw_est_template_activity USING btree (estimate_template_id);
CREATE INDEX idx_est_temp_act_nonsor ON egw_est_template_activity USING btree (nonsor_id);
CREATE INDEX idx_est_temp_act_sor ON egw_est_template_activity USING btree (scheduleofrate_id);
CREATE INDEX idx_est_temp_act_uom ON egw_est_template_activity USING btree (uom_id);

CREATE SEQUENCE SEQ_EGW_est_template_activity;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_milestone_template
(
  id bigint NOT NULL,
  code character varying(100) NOT NULL,
  name character varying(256) NOT NULL,
  description character varying(1024),
  status bigint NOT NULL,
  worktype_id bigint NOT NULL,
  worksubtype_id bigint,
  state_id bigint,
  status_id bigint,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_milestone_template PRIMARY KEY (id),
  CONSTRAINT unq_milestone_template_code UNIQUE (code),
  CONSTRAINT fk_milestonetempl_tw FOREIGN KEY (worktype_id)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_milestonetempl_worksubtype FOREIGN KEY (worksubtype_id)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_milestonetempl_stateid FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_milestonetempl_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),
  CONSTRAINT fk_milestonetempl_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_milestonetempl_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_milestonetempl_state ON egw_milestone_template USING btree (state_id);
CREATE INDEX idx_milestonetempl_status ON egw_milestone_template USING btree (status_id);
CREATE INDEX idx_milestonetempl_worksubtype ON egw_milestone_template USING btree (worksubtype_id);
CREATE INDEX idx_egw_mls_template_worktype ON egw_milestone_template USING btree (worktype_id);

CREATE SEQUENCE SEQ_egw_milestone_template;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_milestone_templ_activity
(
  id bigint NOT NULL,
  milestone_template_id bigint NOT NULL,
  stage_order_no bigint NOT NULL,
  description character varying(1024) NOT NULL,  
  percentage bigint NOT NULL,
  milestonetempl_activity_index bigint NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  modifiedby bigint,
  modifieddate timestamp without time zone,
  CONSTRAINT pk_milestone_templ_activity PRIMARY KEY (id),
  CONSTRAINT fk_milestonetempl_act_mth FOREIGN KEY (milestone_template_id)
      REFERENCES egw_milestone_template (id),
  CONSTRAINT fk_milestonetempl_act_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_milestonetempl_act_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_milestonetempl_template ON egw_milestone_templ_activity USING btree (milestone_template_id);

CREATE SEQUENCE SEQ_EGW_MILESTONE_TEMPL_ACTIVITY;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_projectcode
(
  id bigint NOT NULL,
  code character varying(256) NOT NULL,
  description character varying(1024),
  isactive smallint,
  name character varying(1024),
  status_id bigint,
  completion_date timestamp without time zone,
  project_value double precision DEFAULT 0,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_projectcode PRIMARY KEY (id),
  CONSTRAINT unq_projectcode UNIQUE (code),
  CONSTRAINT fk_projectcode_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_projectcode_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_projectcode_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_projectcode_status ON egw_projectcode USING btree (status_id);

CREATE SEQUENCE SEQ_egw_projectcode;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_abstractestimate
(
  id bigint NOT NULL,
  estimate_number character varying(256) NOT NULL,
  estimate_date timestamp without time zone NOT NULL, 
  nameofwork character varying(1024) NOT NULL,  
  description character varying(1024) NOT NULL, 
  jurisdiction bigint NOT NULL,
  natureofwork bigint NOT NULL,  
  location character varying(250),
  LONGITUDE bigint,
  LATITUDE bigint,
  work_type bigint,
  work_subtype bigint,
  user_department bigint NOT NULL,
  executing_department bigint NOT NULL,  
  work_value double precision,
  estimate_value double precision,
  preparedby bigint NOT NULL, 
  fundsource_id bigint NOT NULL, 
  document_number bigint,
  status_id bigint,
  state_id bigint,
  budgetapprno character varying(256),
  budget_apprn_date timestamp without time zone,
  budgetavailable double precision, 
  budgetrejectionno character varying(256),
  despositcode_id bigint,
  projectcode_id bigint,
  parentid bigint,
  approved_date timestamp without time zone,
  is_copied_est character varying(1) DEFAULT 'N'::character varying,   
  createdby bigint NOT NULL,
  modifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,
  CONSTRAINT pk_abstractestimate PRIMARY KEY (id),
  CONSTRAINT unq_estimate_number UNIQUE (estimate_number),
  CONSTRAINT fk_estimate_boundary FOREIGN KEY (jurisdiction)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_estimate_natureofwork FOREIGN KEY (natureofwork)
      REFERENCES egw_natureofwork (id),
  CONSTRAINT fk_estimate_worktype FOREIGN KEY (work_type)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_estimate_worksubtype FOREIGN KEY (work_subtype)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_estimate_userdept FOREIGN KEY (user_department)
      REFERENCES eg_department (id),
  CONSTRAINT fk_estimate_execdept FOREIGN KEY (executing_department)
      REFERENCES eg_department,
  CONSTRAINT fk_estimate_preparedby FOREIGN KEY (preparedby)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_estimate_fundsource FOREIGN KEY (fundsource_id)
      REFERENCES fundsource (id),
  CONSTRAINT fk_estimate_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_estimate_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),
  CONSTRAINT fk_estimate_despositcode FOREIGN KEY (despositcode_id)
      REFERENCES egw_depositcode (id),
  CONSTRAINT fk_estimate_projcode FOREIGN KEY (projectcode_id)
      REFERENCES egw_projectcode (id),
  CONSTRAINT fk_estimate_re FOREIGN KEY (parentid)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_estimate_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_estimate_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id) 
);


CREATE INDEX idx_est_jurisdiction ON egw_abstractestimate USING btree (jurisdiction);
CREATE INDEX idx_est_natureofwork ON egw_abstractestimate USING btree (natureofwork);
CREATE INDEX idx_est_work_type ON egw_abstractestimate USING btree (work_type);
CREATE INDEX idx_est_work_subtype ON egw_abstractestimate USING btree (work_subtype);
CREATE INDEX idx_est_user_department ON egw_abstractestimate USING btree (user_department);
CREATE INDEX idx_est_exec_department ON egw_abstractestimate USING btree (executing_department);
CREATE INDEX idx_est_preparedby ON egw_abstractestimate USING btree (preparedby);
CREATE INDEX idx_est_fundsource_id ON egw_abstractestimate USING btree (fundsource_id);
CREATE INDEX idx_est_status_id ON egw_abstractestimate USING btree (status_id);
CREATE INDEX idx_est_state_id ON egw_abstractestimate USING btree (state_id);
CREATE INDEX idx_est_despositcode_id ON egw_abstractestimate USING btree (despositcode_id);
CREATE INDEX idx_est_projectcode_id ON egw_abstractestimate USING btree (projectcode_id);
CREATE INDEX idx_est_parentid ON egw_abstractestimate USING btree (parentid);


CREATE SEQUENCE SEQ_EGW_ABSTRACTESTIMATE;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_activity
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  scheduleofrate_id bigint,
  nonsor_id bigint,
  uom_id bigint,
  unit_rate double precision NOT NULL,
  quantity double precision NOT NULL,
  sor_rate double precision,
  servicetaxperc double precision,
  revision_type character varying(50),
  parentid bigint,
  estimate_activity_index bigint NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone, 
  CONSTRAINT pk_estimate_activity PRIMARY KEY (id),  
  CONSTRAINT fk_est_activity_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_est_activity_sor FOREIGN KEY (scheduleofrate_id)
      REFERENCES egw_scheduleofrate (id),
  CONSTRAINT fk_est_activity_nonsor FOREIGN KEY (nonsor_id)
      REFERENCES egw_nonsor (id),
  CONSTRAINT fk_est_activity_uom FOREIGN KEY (uom_id)
      REFERENCES eg_uom (id),
  CONSTRAINT fk_est_activity_parent FOREIGN KEY (parentid)
      REFERENCES egw_estimate_activity (id),
  CONSTRAINT check_estimate_quantity CHECK (quantity >= 0::double precision),
  CONSTRAINT check_estimate_servicetaxperc CHECK (servicetaxperc >= 0::double precision),
  CONSTRAINT fk_est_activity_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_est_activity_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_est_activity_estimate ON egw_estimate_activity USING btree (abstractestimate_id);
CREATE INDEX idx_est_activity_nonsor ON egw_estimate_activity USING btree (nonsor_id);
CREATE INDEX idx_est_activity_sor ON egw_estimate_activity USING btree (scheduleofrate_id);
CREATE INDEX idx_est_activity_uom ON egw_estimate_activity USING btree (uom_id);
CREATE INDEX idx_est_activity_parent ON egw_estimate_activity USING btree (parentid);

CREATE SEQUENCE seq_egw_estimate_activity;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_multiyear_estimate
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  financialyear_id bigint NOT NULL,
  percentage double precision NOT NULL,
  multiyear_estimate_index bigint NOT NULL,
  created_by bigint,
  created_date timestamp without time zone,
  modified_by bigint,
  modified_date timestamp without time zone, 
  CONSTRAINT pk_multiyearestimate PRIMARY KEY (id),
  CONSTRAINT fk_multiyear_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_multiyear_est_finyear FOREIGN KEY (financialyear_id)
      REFERENCES financialyear (id),
  CONSTRAINT check_multiyear_percentage CHECK (percentage <= 100::double precision),
  CONSTRAINT fk_multiyear_est_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_multiyear_est_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_multiyear_estimate ON egw_multiyear_estimate USING btree (abstractestimate_id);
CREATE INDEX idx_multiyear_est_finyear ON egw_multiyear_estimate USING btree (financialyear_id);

CREATE SEQUENCE seq_egw_multiyear_estimate;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_overheads
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,  
  overhead_id bigint NOT NULL,
  amount double precision,
  estimate_overhead_index bigint NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_estimate_overheads PRIMARY KEY (id),
  CONSTRAINT fk_est_oh_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_est_oh_overhead FOREIGN KEY (overhead_id)
      REFERENCES egw_overhead (id),
  CONSTRAINT fk_est_oh_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_est_oh_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_est_oh_estimate ON egw_estimate_overheads USING btree (abstractestimate_id);
CREATE INDEX idx_est_oh_overhead ON egw_estimate_overheads USING btree (overhead_id);

CREATE SEQUENCE seq_egw_estimate_overheads;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_assets
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  asset_id bigint NOT NULL,
  estimate_asset_index bigint,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_estimate_assets PRIMARY KEY (id),
  CONSTRAINT fk_estimateassets_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_estimateassets_asset FOREIGN KEY (asset_id)
      REFERENCES egasset_asset (id),
  CONSTRAINT fk_estimateassets_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_estimateassets_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_estimateassets_estimate ON egw_estimate_assets USING btree (abstractestimate_id);
CREATE INDEX idx_estimateassets_asset ON egw_estimate_assets USING btree (asset_id);

CREATE SEQUENCE seq_egw_estimate_assets;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_financialdetail
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  fund_id bigint NOT NULL,
  budgetgroup_id bigint,
  coa_id bigint,
  function_id bigint,
  functionary_id bigint,  
  scheme_id bigint,
  subscheme_id bigint,
  estimate_findtl_index bigint NOT NULL,  
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_est_financialdetail PRIMARY KEY (id),
  CONSTRAINT fk_findetail_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_est_findetail_fund FOREIGN KEY (fund_id)
      REFERENCES fund (id),
  CONSTRAINT fk_est_findetail_budgetgroup FOREIGN KEY (budgetgroup_id)
      REFERENCES egf_budgetgroup (id),
  CONSTRAINT fk_est_findetail_coa FOREIGN KEY (coa_id)
      REFERENCES chartofaccounts (id),  
  CONSTRAINT fk_est_findetail_function FOREIGN KEY (function_id)
      REFERENCES function (id),
  CONSTRAINT fk_est_findetail_functionary FOREIGN KEY (functionary_id)
      REFERENCES functionary (id),
  CONSTRAINT fk_est_findetail_scheme FOREIGN KEY (scheme_id)
      REFERENCES scheme (id),
  CONSTRAINT fk_est_findetail_subscheme FOREIGN KEY (subscheme_id)
      REFERENCES sub_scheme (id),
  CONSTRAINT fk_est_findetail_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_est_findetail_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX index_est_findetail_estimate ON egw_estimate_financialdetail USING btree (abstractestimate_id);
CREATE INDEX index_est_findetail_fund ON egw_estimate_financialdetail USING btree (fund_id);
CREATE INDEX index_est_findetail_budgrp ON egw_estimate_financialdetail USING btree (budgetgroup_id);
CREATE INDEX index_est_findetail_coa ON egw_estimate_financialdetail USING btree (coa_id);
CREATE INDEX index_est_findetail_function ON egw_estimate_financialdetail USING btree (function_id);
CREATE INDEX index_est_findetail_functionary ON egw_estimate_financialdetail USING btree (functionary_id);
CREATE INDEX index_est_findetail_scheme ON egw_estimate_financialdetail USING btree (scheme_id);
CREATE INDEX index_est_findetail_sub_scheme ON egw_estimate_financialdetail USING btree (subscheme_id);

CREATE SEQUENCE seq_egw_estimate_financialdetail;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_financingsource
(
  id bigint NOT NULL,
  financialdetail_id bigint NOT NULL, 
  fundsource_id bigint NOT NULL,
  percentage double precision NOT NULL, 
  estimate_finsource_index bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_est_financingsource PRIMARY KEY (id),
  CONSTRAINT fk_est_finsource_findtl FOREIGN KEY (financialdetail_id)
      REFERENCES egw_estimate_financialdetail (id),
  CONSTRAINT fk_est_finsource_fundsource FOREIGN KEY (fundsource_id)
      REFERENCES fundsource (id)
);

CREATE INDEX idx_est_est_findtl ON egw_estimate_financingsource USING btree (financialdetail_id);
CREATE INDEX idx_est_finsource_fundsource ON egw_estimate_financingsource USING btree (fundsource_id);

CREATE SEQUENCE seq_egw_estimate_financingsource;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_depositworks_usage
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  depositcode_id bigint NOT NULL,
  coa_id bigint NOT NULL,
  financialyear_id bigint NOT NULL,
  total_deposit_amount double precision,
  consumed_amt double precision,
  released_amt double precision,
  appropriation_number character varying(256) NOT NULL,
  appropriation_date timestamp without time zone NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_depositworks_usage PRIMARY KEY (id),
  CONSTRAINT fk_depworks_usage_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_depworks_usage_coa FOREIGN KEY (coa_id)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_depworks_usage_depcode FOREIGN KEY (depositcode_id)
      REFERENCES egw_depositcode (id),
  CONSTRAINT fk_depworks_usage_finyear FOREIGN KEY (financialyear_id)
      REFERENCES financialyear (id),
  CONSTRAINT fk_depworks_usage_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_depworks_usage_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_depositworks_usage_estimate ON egw_depositworks_usage USING btree (abstractestimate_id);
CREATE INDEX idx_depositworks_usage_depcode ON egw_depositworks_usage USING btree (depositcode_id);
CREATE INDEX idx_depositworks_usage_coa ON egw_depositworks_usage USING btree (coa_id); 
CREATE INDEX idx_depositworks_usage_finyear ON egw_depositworks_usage USING btree (financialyear_id);

CREATE SEQUENCE seq_egw_depositworks_usage;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_appropriation
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  budgetusage_id bigint,
  balanceavailable bigint NOT NULL,
  depositworksusage_id bigint,
  CONSTRAINT pk_estimate_appropriation PRIMARY KEY (id),
  CONSTRAINT fk_est_appropriation_bud FOREIGN KEY (budgetusage_id)
      REFERENCES egf_budget_usage (id),
  CONSTRAINT fk_est_appropriation_depusage FOREIGN KEY (depositworksusage_id)
      REFERENCES egw_depositworks_usage (id),
  CONSTRAINT fk_est_appropriation_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id)
);

CREATE INDEX idx_est_appropriation_estimate ON egw_estimate_appropriation USING btree (abstractestimate_id);
CREATE INDEX idx_est_appropriation_budgetusage ON egw_estimate_appropriation USING btree (budgetusage_id);
CREATE INDEX idx_est_appropriation_depusage ON egw_estimate_appropriation USING btree (depositworksusage_id);

CREATE SEQUENCE seq_egw_estimate_appropriation;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_estimate_photographs
(
  id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  image bytea NOT NULL,
  latitude bigint NOT NULL,
  longitude bigint NOT NULL,
  description character varying(1024),
  captured_date timestamp without time zone NOT NULL,
  est_photo_index bigint,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_est_photographs PRIMARY KEY (id),
  CONSTRAINT fk_estimate_photographs FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_estimate_photographs_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_estimate_photographs_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_est_photographs_estimate ON egw_estimate_photographs USING btree (abstractestimate_id);

CREATE SEQUENCE seq_egw_estimate_photographs;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_offline_status
(
  id bigint NOT NULL,
  object_type character varying(128) NOT NULL,
  object_id bigint NOT NULL,  
  status_date timestamp without time zone NOT NULL,
  status_id bigint NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_egw_offline_status PRIMARY KEY (id),
  CONSTRAINT fk_offlinestatus_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_offlinestatus_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_offlinestatus_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_offlinestatus_status ON egw_offline_status USING btree (status_id);

CREATE SEQUENCE seq_egw_offline_status;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_workspackage
(
  id bigint NOT NULL,
  wp_number character varying(100) NOT NULL,
  wp_date timestamp without time zone NOT NULL,
  name character varying(1024) NOT NULL,  
  description character varying(1024),  
  department_id bigint NOT NULL,
  tender_file_number character varying(100) NOT NULL,
  document_number bigint,
  status_id bigint,
  state_id bigint,
  approved_date timestamp without time zone,
  latest_offline_status bigint,
  preparedby bigint NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone, 
  CONSTRAINT pk_workspackage PRIMARY KEY (id),
  CONSTRAINT unq_workspackage UNIQUE (wp_number),
  CONSTRAINT fk_workspackage_dept FOREIGN KEY (department_id)
      REFERENCES eg_department (id),
  CONSTRAINT fk_workspackage_employee FOREIGN KEY (preparedby)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_workspackage_state_id FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),
  CONSTRAINT fk_workspackage_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_workspackage_offlinestatus FOREIGN KEY (latest_offline_status)
      REFERENCES egw_offline_status (id),
  CONSTRAINT fk_workspackage_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_workspackage_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_workspackage_department ON egw_workspackage USING btree (department_id);
CREATE INDEX idx_workspackage_preparedby ON egw_workspackage USING btree (preparedby);
CREATE INDEX idx_workspackage_status ON egw_workspackage USING btree (status_id);
CREATE INDEX idx_workspackage_state_id ON egw_workspackage USING btree (state_id);
CREATE INDEX idx_workspackage_offlinestatus ON egw_workspackage USING btree (latest_offline_status);

CREATE SEQUENCE seq_egw_workspackage;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_workspackage_details
(
  id bigint NOT NULL,
  workspackage_id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  wp_index bigint NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone, 
  CONSTRAINT pk_workspackage_details PRIMARY KEY (id),
  CONSTRAINT fk_wpdetails_wp FOREIGN KEY (workspackage_id)
      REFERENCES egw_workspackage (id),
  CONSTRAINT fk_wpdetails_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_wpdetails_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_wpdetails_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_wpdetails_wp ON egw_workspackage_details USING btree (workspackage_id);
CREATE INDEX idx_wpdetails_estimate ON egw_workspackage_details USING btree (abstractestimate_id);

CREATE SEQUENCE seq_egw_workspackage_details;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_retender
(
  id bigint NOT NULL,
  workspackage_id bigint NOT NULL,
  reason character varying(1024) NOT NULL,
  retender_date timestamp without time zone NOT NULL,
  iteration_no bigint NOT NULL,
  retender_index bigint,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_retender PRIMARY KEY (id),
  CONSTRAINT fk_retender_wp FOREIGN KEY (workspackage_id)
      REFERENCES egw_workspackage (id),
  CONSTRAINT fk_retender_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_retender_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_retender_wp ON egw_retender USING btree (workspackage_id);

CREATE SEQUENCE seq_egw_retender;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_retender_history
(
  id bigint NOT NULL,
  workspackage_id bigint NOT NULL,
  retender_id bigint NOT NULL,
  status_date timestamp without time zone NOT NULL,
  status_id bigint NOT NULL,
  retender_history_index bigint NOT NULL,
  retender_hist_dtls_index bigint,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_retender_history PRIMARY KEY (id),
  CONSTRAINT fk_retender_hist_retender FOREIGN KEY (retender_id)
      REFERENCES egw_retender (id),
  CONSTRAINT fk_retender_hist_wp FOREIGN KEY (workspackage_id)
      REFERENCES egw_workspackage (id),
  CONSTRAINT fk_retender_hist_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
 CONSTRAINT fk_retender_hist_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_retender_hist_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_retender_hist_retender ON egw_retender_history USING btree (retender_id);
CREATE INDEX idx_retender_hist_wp ON egw_retender_history USING btree (workspackage_id);
CREATE INDEX idx_retender_hist_status ON egw_retender_history USING btree (status_id);

CREATE SEQUENCE seq_egw_retender_history;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_tender_header
(
  id bigint NOT NULL,
  tender_no character varying(100) NOT NULL,
  tender_date timestamp without time zone NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_tender_header PRIMARY KEY (id),
  CONSTRAINT fk_tender_header_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_tender_header_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE SEQUENCE seq_egw_tender_header;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_tender_estimate
(
  id bigint NOT NULL,
  tender_header_id bigint NOT NULL,
  workspackage_id bigint,
  abstractestimate_id bigint,
  tender_type character varying(100) NOT NULL,
  tender_estimate_index bigint,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_tender_estimate PRIMARY KEY (id),
  CONSTRAINT fk_tenderest_tenderheader FOREIGN KEY (tender_header_id)
      REFERENCES egw_tender_header (id),
  CONSTRAINT fk_tenderest_wp FOREIGN KEY (workspackage_id)
      REFERENCES egw_workspackage (id),
  CONSTRAINT fk_tenderest_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_tenderest_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_tenderest_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_tenderest_wp ON egw_tender_estimate USING btree (workspackage_id);
CREATE INDEX idx_tenderest_estimate ON egw_tender_estimate USING btree (abstractestimate_id);
CREATE INDEX idx_tenderest_tenderheader ON egw_tender_estimate USING btree (tender_header_id);

CREATE SEQUENCE seq_egw_tender_estimate;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_tender_response
(
  id bigint NOT NULL,
  tender_estimate_id bigint NOT NULL,
  negotiation_number character varying(256) NOT NULL,
  negotiation_date timestamp without time zone NOT NULL,
  perc_quoted_rate double precision,
  perc_negotiated_rate double precision,
  tender_negotiated_value bigint DEFAULT 0,
  status_id bigint,
  state_id bigint,
  narration character varying(500),
  prepared_by bigint NOT NULL,
  document_number bigint,
  approved_date timestamp without time zone,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_tender_response PRIMARY KEY (id),
  CONSTRAINT fk_tenderresponse_tenderest FOREIGN KEY (tender_estimate_id)
      REFERENCES egw_tender_estimate (id),
  CONSTRAINT fk_tenderresponse_employee FOREIGN KEY (prepared_by)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_tenderresponse_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_tenderresponse_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),  
  CONSTRAINT fk_tenderresponse_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_tenderresponse_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_tenderresponse_tenderest ON egw_tender_response USING btree (tender_estimate_id);
CREATE INDEX idx_tenderresponse_preparedby ON egw_tender_response USING btree (prepared_by);
CREATE INDEX idx_tenderresponse_status ON egw_tender_response USING btree (status_id);
CREATE INDEX idx_tenderresponse_state ON egw_tender_response USING btree (state_id);

CREATE SEQUENCE seq_egw_tender_response;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_tender_response_activity
(
  id bigint NOT NULL,
  tender_response_id bigint NOT NULL,
  estimate_activity_id bigint NOT NULL,
  negotiated_rate double precision,
  negotiated_quantity double precision,
  tend_resp_act_index bigint NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_tender_response_activity PRIMARY KEY (id),
  CONSTRAINT fk_tra_tr FOREIGN KEY (tender_response_id)
      REFERENCES egw_tender_response (id),
  CONSTRAINT fk_tra_estactivity FOREIGN KEY (estimate_activity_id)
      REFERENCES egw_estimate_activity (id),
  CONSTRAINT fk_tra_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_tra_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_tra_tender_response_id ON egw_tender_response_activity USING btree (tender_response_id);
CREATE INDEX idx_tra_activity_id ON egw_tender_response_activity USING btree (estimate_activity_id);

CREATE SEQUENCE seq_egw_tender_response_activity;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_tender_response_quotes
(
  id bigint NOT NULL,
  tender_response_activity_id bigint NOT NULL,
  contractor_id bigint NOT NULL,
  quoted_rate double precision NOT NULL,
  quoted_quantity double precision,
  tra_quotes_index bigint,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_tender_response_quotes PRIMARY KEY (id),
  CONSTRAINT fk_trq_contractor FOREIGN KEY (contractor_id)
      REFERENCES egw_contractor (id),
  CONSTRAINT fk_trq_tra FOREIGN KEY (tender_response_activity_id)
      REFERENCES egw_tender_response_activity (id),
  CONSTRAINT fk_trq_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_trq_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_trq_contractor_id ON egw_tender_response_quotes USING btree (contractor_id);
CREATE INDEX idx_trq_tra ON egw_tender_response_quotes USING btree (tender_response_activity_id);

CREATE SEQUENCE seq_egw_tender_response_quotes;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_tender_resp_contractors
(
  id bigint NOT NULL,
  tender_response_id bigint NOT NULL,
  contractor_id bigint NOT NULL,
  tend_resp_cont_index bigint NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_egw_tender_resp_contractors PRIMARY KEY (id),
  CONSTRAINT fk_trc_contractor FOREIGN KEY (contractor_id)
      REFERENCES egw_contractor (id),
  CONSTRAINT fk_trc_tender_response FOREIGN KEY (tender_response_id)
      REFERENCES egw_tender_response (id),
  CONSTRAINT fk_trc_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_trc_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_trc_contractor_id ON egw_tender_resp_contractors USING btree (contractor_id);
CREATE INDEX idx_trc_tender_response ON egw_tender_resp_contractors USING btree (tender_response_id);

CREATE SEQUENCE seq_egw_tender_resp_contractors;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_workorder
(
  id bigint NOT NULL,
  workorder_number character varying(256) NOT NULL,
  workorder_date timestamp without time zone NOT NULL,
  contractor_id bigint NOT NULL,  
  emd_amount_deposited double precision,
  security_deposit double precision,
  labour_welfare_fund double precision,
  preparedby bigint NOT NULL,
  engineer_incharge1 bigint NOT NULL,
  engineer_incharge2 bigint,
  tender_number character varying(64),
  negotiation_number character varying(64),
  wp_number character varying(64),
  work_order_details character varying(1024),
  agreement_details character varying(1024),
  payment_terms character varying(1024),
  contract_period bigint,
  defect_liability_period bigint,  
  document_number bigint,
  status_id bigint,
  state_id bigint,
  workorder_amount bigint NOT NULL, 
  parentid bigint,
  approved_date timestamp without time zone,
  createdby bigint NOT NULL,
  modifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,
  CONSTRAINT pk_workorder PRIMARY KEY (id),
  CONSTRAINT fk_wo_contractor FOREIGN KEY (contractor_id)
      REFERENCES egw_contractor (id),
  CONSTRAINT fk_wo_preparedby FOREIGN KEY (preparedby)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_wo_eng_incharge1 FOREIGN KEY (engineer_incharge1)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_wo_eng_incharge2 FOREIGN KEY (engineer_incharge2)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_wo_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_wo_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),
  CONSTRAINT fk_wo_parentid FOREIGN KEY (parentid)
      REFERENCES egw_workorder (id),
  CONSTRAINT fk_wo_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_wo_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_wo_contractor ON egw_workorder USING btree (contractor_id);
CREATE INDEX idx_wo_preparedby ON egw_workorder USING btree (preparedby);
CREATE INDEX idx_wo_engineer_incharge1 ON egw_workorder USING btree (engineer_incharge1);
CREATE INDEX idx_wo_engineer_incharge2 ON egw_workorder USING btree (engineer_incharge2);
CREATE INDEX idx_wo_parent ON egw_workorder USING btree (parentid);
CREATE INDEX idx_wo_status ON egw_workorder USING btree (status_id);
CREATE INDEX idx_wo_state ON egw_workorder USING btree (state_id);

CREATE SEQUENCE seq_egw_workorder;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_workorder_estimate
(
  id bigint NOT NULL,
  workorder_id bigint NOT NULL,
  abstractestimate_id bigint NOT NULL,
  work_completion_date timestamp without time zone,
  estimate_wo_amount bigint NOT NULL,
  workorder_estimate_index bigint NOT NULL,
  createdby bigint NOT NULL,
  modifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,  
  CONSTRAINT pk_workorder_estimate PRIMARY KEY (id),
  CONSTRAINT fk_workorderest_workorder FOREIGN KEY (workorder_id)
      REFERENCES egw_workorder (id),
  CONSTRAINT fk_workorderest_estimate FOREIGN KEY (abstractestimate_id)
      REFERENCES egw_abstractestimate (id),
  CONSTRAINT fk_workorderest_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_workorderest_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_workorderest_estimate ON egw_workorder_estimate USING btree (abstractestimate_id);
CREATE INDEX idx_workorderest_workorder ON egw_workorder_estimate USING btree (workorder_id);

CREATE SEQUENCE seq_egw_workorder_estimate;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_workorder_activity
(
  id bigint NOT NULL,
  workorder_estimate_id bigint NOT NULL,
  estimate_activity_id bigint NOT NULL,
  approved_quantity bigint NOT NULL,
  approved_rate double precision NOT NULL,
  approved_amount double precision NOT NULL,
  remarks character varying(1024),
  workorder_estimate_index bigint NOT NULL,
  createdby bigint NOT NULL,
  modifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,  
  CONSTRAINT pk_wo_activity PRIMARY KEY (id),
  CONSTRAINT fk_wo_activity_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id),
  CONSTRAINT fk_wo_activity_estimateactivity FOREIGN KEY (estimate_activity_id)
      REFERENCES egw_estimate_activity (id),
  CONSTRAINT fk_wo_activity_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_wo_activity_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_wo_activity_estimateactivity ON egw_workorder_activity USING btree (estimate_activity_id);
CREATE INDEX idx_wo_activity_woe ON egw_workorder_activity USING btree (workorder_estimate_id);

CREATE SEQUENCE seq_egw_workorder_activity;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_workorder_assets
(
  id bigint NOT NULL,
  workorder_estimate_id bigint NOT NULL,
  asset_id bigint NOT NULL,
  wo_est_asset_index bigint NOT NULL,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_wo_assets PRIMARY KEY (id),
  CONSTRAINT fk_wo_assets_asset FOREIGN KEY (asset_id)
      REFERENCES egasset_asset (id),
  CONSTRAINT fk_wo_assets_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id),
  CONSTRAINT fk_wo_assets_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_wo_assets_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_wo_assets_asset ON egw_workorder_assets USING btree (asset_id);
CREATE INDEX idx_wo_assets_woe ON egw_workorder_assets USING btree (workorder_estimate_id);

CREATE SEQUENCE seq_egw_workorder_assets;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_mb_header
(
  id bigint NOT NULL,
  workorder_id bigint NOT NULL,
  workorder_estimate_id bigint NOT NULL,
  mb_refno character varying(100) NOT NULL,
  mb_date timestamp without time zone NOT NULL,
  from_page_no bigint NOT NULL,
  to_page_no bigint,
  prepared_by bigint NOT NULL,
  abstract character varying(1024) NOT NULL,
  contractor_comments character varying(1024),  
  status_id bigint,
  state_id bigint,
  billregister_id bigint,
  document_number bigint,   
  is_legacy_mb smallint,
  mb_amount bigint,
  approved_date timestamp without time zone,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_mb_header PRIMARY KEY (id),
  CONSTRAINT fk_mbh_wo FOREIGN KEY (workorder_id)
      REFERENCES egw_workorder (id),
  CONSTRAINT fk_mbh_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id),
  CONSTRAINT fk_mbh_billregister FOREIGN KEY (billregister_id)
      REFERENCES eg_billregister (id),
  CONSTRAINT fk_mbh_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_mbh_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),
  CONSTRAINT fk_mbh_preparedby FOREIGN KEY (prepared_by)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_mbh_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_mbh_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_mbh_workorder ON egw_mb_header USING btree (workorder_id);
CREATE INDEX idx_mbh_woe ON egw_mb_header USING btree (workorder_estimate_id);
CREATE INDEX idx_mbh_billregister ON egw_mb_header USING btree (billregister_id);
CREATE INDEX idx_mbh_status ON egw_mb_header USING btree (status_id);
CREATE INDEX idx_mbh_state ON egw_mb_header USING btree (state_id);
CREATE INDEX idx_mbh_preparedby ON egw_mb_header USING btree (prepared_by);

CREATE SEQUENCE seq_egw_mb_header;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_mb_details
(
  id bigint NOT NULL,
  mbheader_id bigint NOT NULL,
  wo_activity_id bigint NOT NULL,
  quantity bigint NOT NULL,
  rate bigint,
  amount bigint,
  remarks character varying(1024),
  order_number character varying(100),
  order_date timestamp without time zone,
  mbd_mbh_index bigint NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_mb_details PRIMARY KEY (id),
  CONSTRAINT fk_mbd_mbh FOREIGN KEY (mbheader_id)
      REFERENCES egw_mb_header (id), 
  CONSTRAINT fk_mbd_woa FOREIGN KEY (wo_activity_id)
      REFERENCES egw_workorder_activity (id),
  CONSTRAINT fk_mbd_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_mbd_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_mbd_woa ON egw_mb_details USING btree (wo_activity_id);
CREATE INDEX idx_mbd_mbheader ON egw_mb_details USING btree (mbheader_id);

CREATE SEQUENCE seq_egw_mb_details;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_contractorbill
(
  id bigint NOT NULL,
  part_billnumber bigint,
  document_number bigint,
  approved_date timestamp without time zone
);
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_contractorbill_assets
(
  id bigint NOT NULL,
  asset_id bigint NOT NULL,
  billregister_id bigint NOT NULL,
  coa_id bigint NOT NULL, 
  workorder_estimate_id bigint NOT NULL,
  amount bigint NOT NULL,
  narration character varying(1024),  
  bill_asset_index bigint NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_contractorbill_assets PRIMARY KEY (id),
  CONSTRAINT fk_billassets_asset FOREIGN KEY (asset_id)
      REFERENCES egasset_asset (id),
  CONSTRAINT fk_billassets_billregister FOREIGN KEY (billregister_id)
      REFERENCES eg_billregister (id),
  CONSTRAINT fk_billassets_coa FOREIGN KEY (coa_id)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_billassets_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id),
  CONSTRAINT fk_billassets_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_billassets_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_egw_astforbill_asset ON egw_contractorbill_assets USING btree (asset_id);
CREATE INDEX idx_egw_astforbill_billregister ON egw_contractorbill_assets USING btree (billregister_id);
CREATE INDEX idx_egw_astforbill_coa ON egw_contractorbill_assets USING btree (coa_id);
CREATE INDEX idx_egw_assetforbill_wo_est ON egw_contractorbill_assets USING btree (workorder_estimate_id);

CREATE SEQUENCE seq_egw_contractorbill_assets;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_standard_deductions_bill
(
  id bigint NOT NULL,
  billregister_id bigint NOT NULL,
  workorder_id bigint NOT NULL,
  workorder_estimate_id bigint,
  coa_id bigint NOT NULL,
  deduction_type character varying(256) NOT NULL,
  amount bigint NOT NULL,
  narration character varying(1024),
  stddeductionsbill_index bigint NOT NULL,  
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_stddeductions_bill PRIMARY KEY (id),
  CONSTRAINT fk_stddeductbill_billreg FOREIGN KEY (billregister_id)
      REFERENCES eg_billregister (id),
  CONSTRAINT fk_stddeductbill_wo FOREIGN KEY (workorder_id)
      REFERENCES egw_workorder (id),
  CONSTRAINT fk_stddeductbill_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id),
  CONSTRAINT fk_stddeductbill_coa FOREIGN KEY (coa_id)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_stddeductbill_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_stddeductbill_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_stddeductbill_billreg ON egw_standard_deductions_bill USING btree (billregister_id);
CREATE INDEX idx_stddeductbill_wo ON egw_standard_deductions_bill USING btree (workorder_id);
CREATE INDEX idx_stddeductbill_woe ON egw_standard_deductions_bill USING btree (workorder_estimate_id);
CREATE INDEX idx_stddeductbill_coa ON egw_standard_deductions_bill USING btree (coa_id);

CREATE SEQUENCE seq_egw_standard_deductions_bill;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_statutory_deductions_bill
(
  id bigint NOT NULL,
  subpartytype_id bigint,
  typeofwork_id bigint,
  billregister_id bigint NOT NULL,
  billpayeedetails_id bigint,
  statutorydeductionbill_index bigint,
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_statutorydeductionbill PRIMARY KEY (id),
  CONSTRAINT fk_statdeductbill_billpayee FOREIGN KEY (billpayeedetails_id)
      REFERENCES eg_billpayeedetails (id),
  CONSTRAINT fk_statdeductbill_billregister FOREIGN KEY (billregister_id)
      REFERENCES eg_billregister (id),
  CONSTRAINT fk_statdeductbill_subpartytype FOREIGN KEY (subpartytype_id)
      REFERENCES eg_partytype (id),
  CONSTRAINT fk_statdeductbill_typeofwork FOREIGN KEY (typeofwork_id)
      REFERENCES egw_typeofwork (id),
  CONSTRAINT fk_statdeductbill_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_statdeductbill_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_statdeductbill_billpayee ON egw_statutory_deductions_bill USING btree (billpayeedetails_id);
CREATE INDEX idx_statdeductbill_billreg ON egw_statutory_deductions_bill USING btree (billregister_id);
CREATE INDEX idx_statdeductbill_subpartytype ON egw_statutory_deductions_bill USING btree (subpartytype_id);
CREATE INDEX idx_statdeductbill_typeofwork ON egw_statutory_deductions_bill USING btree (typeofwork_id);

CREATE SEQUENCE seq_egw_statutory_deductions_bill;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_cancelled_bill
(
  id bigint NOT NULL,
  mbheader_id bigint NOT NULL,
  billregister_id bigint NOT NULL,
  CONSTRAINT pk_cancelled_bill PRIMARY KEY (id),
  CONSTRAINT fk_cb_billreg FOREIGN KEY (billregister_id)
      REFERENCES eg_billregister (id),
  CONSTRAINT fk_cb_mbheader FOREIGN KEY (mbheader_id)
      REFERENCES egw_mb_header (id)
);

CREATE INDEX idx_cb_billregister ON egw_cancelled_bill USING btree (billregister_id);
CREATE INDEX idx_cb_mbheader ON egw_cancelled_bill USING btree (mbheader_id);

CREATE SEQUENCE seq_egw_cancelled_bill;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_revision_estimate
(
  id bigint
);
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_revision_workorder
(
  id bigint
);
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_milestone
(
  id bigint NOT NULL,
  workorder_estimate_id bigint NOT NULL,
  document_number bigint,
  state_id bigint,
  status_id bigint,
  approved_date timestamp without time zone,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,  
  CONSTRAINT pk_milestone PRIMARY KEY (id),
  CONSTRAINT fk_milestone_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id),
  CONSTRAINT fk_milestone_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_milestone_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),
  CONSTRAINT fk_milestone_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_milestone_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_milestone_woe ON egw_milestone USING btree (workorder_estimate_id);
CREATE INDEX idx_milestone_status ON egw_milestone USING btree (status_id);
CREATE INDEX idx_milestone_state ON egw_milestone USING btree (state_id);

CREATE SEQUENCE seq_egw_milestone;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_milestone_activity
(
  id bigint NOT NULL,
  stage_order_no bigint NOT NULL,
  milestone_id bigint NOT NULL,
  description character varying(1024) NOT NULL,
  percentage bigint NOT NULL,
  milestone_activity_index bigint NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  modifiedby bigint,
  modifieddate timestamp without time zone,
  CONSTRAINT pk_milestone_activity PRIMARY KEY (id),
  CONSTRAINT fk_milestone_act_milestone FOREIGN KEY (milestone_id)
      REFERENCES egw_milestone (id),
  CONSTRAINT fk_milestone_act_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_milestone_act_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_milestone_act_milestone ON egw_milestone_activity USING btree (milestone_id);

CREATE SEQUENCE seq_egw_milestone_activity;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_track_milestone
(
  id bigint NOT NULL,
  milestone_id bigint NOT NULL,
  total_percentage bigint,
  state_id bigint,
  status_id bigint,
  is_project_completed smallint,
  approved_date timestamp without time zone,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint,
  modified_date timestamp without time zone,
  CONSTRAINT pk_track_milestone PRIMARY KEY (id),
  CONSTRAINT fk_trackmilestone_milestone FOREIGN KEY (milestone_id)
      REFERENCES egw_milestone (id),
  CONSTRAINT fk_trackmilestone_status FOREIGN KEY (status_id)
      REFERENCES egw_status (id),
  CONSTRAINT fk_trackmilestone_state FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id),
  CONSTRAINT fk_trackmilestone_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_trackmilestone_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_trackmilestone_milestone ON egw_track_milestone USING btree (milestone_id);
CREATE INDEX idx_trackmilestone_status ON egw_track_milestone USING btree (status_id);
CREATE INDEX idx_trackmilestone_state ON egw_track_milestone USING btree (state_id);

CREATE SEQUENCE seq_egw_track_milestone;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_track_milestone_activity
(
  id bigint NOT NULL,
  trackmilestone_id bigint,
  milestone_activity_id bigint,
  status_id character varying(50),
  completed_percentage bigint,
  remarks character varying(1024),
  completetion_date timestamp without time zone,
  trackmilestone_activity_index bigint NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  modifiedby bigint,
  modifieddate timestamp without time zone,
  CONSTRAINT pk_track_milestone_activity PRIMARY KEY (id),
  CONSTRAINT fk_tma_trackmilestone FOREIGN KEY (trackmilestone_id)
      REFERENCES egw_track_milestone (id),
  CONSTRAINT fk_tma_milestoneact FOREIGN KEY (milestone_activity_id)
      REFERENCES egw_milestone_activity (id),
  CONSTRAINT fk_tma_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_tma_modifiedby FOREIGN KEY (modifiedby)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_tma_milestoneact ON egw_track_milestone_activity USING btree (milestone_activity_id);
CREATE INDEX idx_tma_trackmilestone ON egw_track_milestone_activity USING btree (trackmilestone_id);

CREATE SEQUENCE seq_egw_track_milestone_activity;
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_contractor_advance
(
  id bigint NOT NULL,
  workorder_estimate_id bigint NOT NULL,
  drawing_officer bigint NOT NULL,
  CONSTRAINT fk_contadv_drawingofficer FOREIGN KEY (drawing_officer)
      REFERENCES eg_drawingofficer (id),
  CONSTRAINT fk_contadv_woe FOREIGN KEY (workorder_estimate_id)
      REFERENCES egw_workorder_estimate (id)
);

CREATE INDEX idx_contadv_drawingofficer ON egw_contractor_advance USING btree (drawing_officer);
CREATE INDEX idx_contadv_woe ON egw_contractor_advance USING btree (workorder_estimate_id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egw_depositcoa_budgethead
(
  id bigint NOT NULL,
  deposit_coa character varying(50) NOT NULL,
  workdone_budget_head character varying(50) NOT NULL
);
-------------------END-------------------

------------------START------------------
ALTER TABLE EGW_ABSTRACTESTIMATE DROP COLUMN PREPAREDBY;

ALTER TABLE EGW_ABSTRACTESTIMATE ALTER COLUMN longitude TYPE double precision;
ALTER TABLE EGW_ABSTRACTESTIMATE ALTER COLUMN latitude TYPE double precision;
-------------------END-------------------

--rollback ALTER TABLE EGW_ABSTRACTESTIMATE ALTER COLUMN longitude TYPE bigint;
--rollback ALTER TABLE EGW_ABSTRACTESTIMATE ALTER COLUMN latitude TYPE bigint;

--rollback ALTER TABLE EGW_ABSTRACTESTIMATE ADD COLUMN preparedby bigint NOT NULL, 
--rollback ALTER TABLE EGW_ABSTRACTESTIMATE ADD CONSTRAINT fk_estimate_preparedby FOREIGN KEY (preparedby) REFERENCES egeis_employee (id),
--rollback CREATE INDEX idx_est_preparedby ON egw_abstractestimate USING btree (preparedby);

--rollback DROP TABLE egw_depositcoa_budgethead;

--rollback DROP TABLE egw_contractor_advance;

--rollback DROP SEQUENCE seq_egw_track_milestone_activity;
--rollback DROP TABLE egw_track_milestone_activity;

--rollback DROP SEQUENCE seq_egw_track_milestone;
--rollback DROP TABLE egw_track_milestone;

--rollback DROP SEQUENCE seq_egw_milestone_activity;
--rollback DROP TABLE egw_milestone_activity;

--rollback DROP SEQUENCE seq_egw_milestone;
--rollback DROP TABLE egw_milestone;

--rollback DROP TABLE egw_revision_workorder;
--rollback DROP TABLE egw_revision_estimate;

--rollback DROP SEQUENCE seq_egw_cancelled_bill;
--rollback DROP TABLE egw_cancelled_bill;

--rollback DROP SEQUENCE seq_egw_statutory_deductions_bill;
--rollback DROP TABLE egw_statutory_deductions_bill;

--rollback DROP SEQUENCE seq_egw_standard_deductions_bill;
--rollback DROP TABLE egw_standard_deductions_bill;

--rollback DROP SEQUENCE seq_egw_contractorbill_assets;
--rollback DROP TABLE egw_contractorbill_assets;

--rollback DROP TABLE egw_contractorbill

--rollback DROP SEQUENCE seq_egw_mb_details;
--rollback DROP TABLE egw_mb_details;

--rollback DROP SEQUENCE seq_egw_mb_header;
--rollback DROP TABLE egw_mb_header;

--rollback DROP SEQUENCE seq_egw_workorder_assets;
--rollback DROP TABLE egw_workorder_assets;

--rollback DROP SEQUENCE seq_egw_workorder_activity;
--rollback DROP TABLE egw_workorder_activity;

--rollback DROP SEQUENCE seq_egw_workorder_estimate;
--rollback DROP TABLE egw_workorder_estimate;

--rollback DROP SEQUENCE seq_egw_workorder;
--rollback DROP TABLE egw_workorder;

--rollback DROP SEQUENCE seq_egw_tender_resp_contractors;
--rollback DROP TABLE egw_tender_resp_contractors;

--rollback DROP SEQUENCE seq_egw_tender_response_quotes;
--rollback DROP TABLE egw_tender_response_quotes;

--rollback DROP SEQUENCE seq_egw_tender_response_activity;
--rollback DROP TABLE egw_tender_response_activity;

--rollback DROP SEQUENCE seq_egw_tender_response;
--rollback DROP TABLE egw_tender_response;

--rollback DROP SEQUENCE seq_egw_tender_estimate;
--rollback DROP TABLE egw_tender_estimate;

--rollback DROP SEQUENCE seq_egw_tender_header;
--rollback DROP TABLE egw_tender_header;

--rollback DROP SEQUENCE seq_egw_retender_history;
--rollback DROP TABLE egw_retender_history;

--rollback DROP SEQUENCE seq_egw_retender;
--rollback DROP TABLE egw_retender;

--rollback DROP SEQUENCE seq_egw_workspackage_details;
--rollback DROP TABLE egw_workspackage_details;

--rollback DROP SEQUENCE seq_egw_workspackage;
--rollback DROP TABLE egw_workspackage;

--rollback DROP SEQUENCE seq_egw_offline_status;
--rollback DROP TABLE egw_offline_status;

--rollback DROP SEQUENCE seq_egw_estimate_photographs;
--rollback DROP TABLE egw_estimate_photographs;

--rollback DROP SEQUENCE seq_egw_estimate_appropriation;
--rollback DROP TABLE egw_estimate_appropriation;

--rollback DROP SEQUENCE seq_egw_depositworks_usage;
--rollback DROP TABLE egw_depositworks_usage;

--rollback DROP SEQUENCE seq_egw_estimate_financingsource;
--rollback DROP TABLE egw_estimate_financingsource;

--rollback DROP SEQUENCE seq_egw_estimate_financialdetail;
--rollback DROP TABLE egw_estimate_financialdetail;

--rollback DROP SEQUENCE seq_egw_estimate_assets;
--rollback DROP TABLE egw_estimate_assets;

--rollback DROP SEQUENCE seq_egw_estimate_overheads;
--rollback DROP TABLE egw_estimate_overheads;

--rollback DROP SEQUENCE seq_egw_multiyear_estimate;
--rollback DROP TABLE egw_multiyear_estimate;

--rollback DROP SEQUENCE seq_egw_estimate_activity;
--rollback DROP TABLE egw_estimate_activity;

--rollback DROP SEQUENCE SEQ_EGW_ABSTRACTESTIMATE;
--rollback DROP TABLE EGW_ABSTRACTESTIMATE;

--rollback DROP SEQUENCE SEQ_egw_projectcode;
--rollback DROP TABLE egw_projectcode;

--rollback DROP SEQUENCE SEQ_EGW_MILESTONE_TEMPL_ACTIVITY;
--rollback DROP TABLE EGW_MILESTONE_TEMPL_ACTIVITY;

--rollback DROP SEQUENCE SEQ_egw_milestone_template;
--rollback DROP TABLE egw_milestone_template;

--rollback DROP SEQUENCE SEQ_egw_est_template_activity;
--rollback DROP TABLE egw_est_template_activity;

--rollback DROP SEQUENCE SEQ_egw_estimate_template;
--rollback DROP TABLE egw_estimate_template;

--rollback DROP SEQUENCE SEQ_EGW_NONSOR;
--rollback DROP TABLE EGW_NONSOR;

--rollback DROP SEQUENCE SEQ_egw_depositcode;
--rollback DROP TABLE egw_depositcode;

--rollback DROP SEQUENCE SEQ_egw_natureofwork;
--rollback DROP TABLE egw_natureofwork;

--rollback DROP SEQUENCE SEQ_egw_overhead_rate;
--rollback DROP TABLE egw_overhead_rate;

--rollback DROP SEQUENCE SEQ_egw_overhead;
--rollback DROP TABLE egw_overhead;

--rollback DROP SEQUENCE SEQ_egw_market_rate;
--rollback DROP TABLE egw_market_rate;

--rollback DROP SEQUENCE SEQ_egw_sor_rate;
--rollback DROP TABLE egw_sor_rate;

--rollback DROP SEQUENCE SEQ_egw_scheduleofrate;
--rollback DROP TABLE egw_scheduleofrate;

--rollback DROP SEQUENCE SEQ_egw_schedulecategory;
--rollback DROP TABLE egw_schedulecategory;

--rollback DROP SEQUENCE SEQ_egw_contractor_detail;
--rollback DROP TABLE egw_contractor_detail;

--rollback DROP SEQUENCE SEQ_egw_contractor;
--rollback DROP TABLE egw_contractor;





