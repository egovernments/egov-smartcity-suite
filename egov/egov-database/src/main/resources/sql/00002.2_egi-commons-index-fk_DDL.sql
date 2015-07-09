


ALTER TABLE ONLY fiscalperiod
    ADD CONSTRAINT fiscalperiod_pkey PRIMARY KEY (id);


ALTER TABLE ONLY schedulemapping
    ADD CONSTRAINT schedulemapping_pkey PRIMARY KEY (id);


ALTER TABLE ONLY egf_accountcode_purpose
    ADD CONSTRAINT egf_accountcode_purpose_name_key UNIQUE (name);



ALTER TABLE ONLY egf_accountcode_purpose
    ADD CONSTRAINT egf_accountcode_purpose_pkey PRIMARY KEY (id);



ALTER TABLE ONLY financialyear
    ADD CONSTRAINT financialyear_financialyear_key UNIQUE (financialyear);



ALTER TABLE ONLY financialyear
    ADD CONSTRAINT financialyear_pkey PRIMARY KEY (id);





ALTER TABLE ONLY scheme
    ADD CONSTRAINT scheme_code_fundid_key UNIQUE (code, fundid);


ALTER TABLE ONLY scheme
    ADD CONSTRAINT scheme_pkey PRIMARY KEY (id);


ALTER TABLE ONLY function
    ADD CONSTRAINT function_pkey PRIMARY KEY (id);

ALTER TABLE ONLY functionary
    ADD CONSTRAINT functionary_pkey PRIMARY KEY (id);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT chartofaccounts_pkey PRIMARY KEY (id);

ALTER TABLE ONLY accountdetailtype
    ADD CONSTRAINT accountdetailtype_pkey PRIMARY KEY (id);

ALTER TABLE ONLY fund
    ADD CONSTRAINT fund_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT eg_designation_pkey PRIMARY KEY (designationid);


ALTER TABLE ONLY eg_drawingofficer
    ADD CONSTRAINT eg_drawingofficer_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT eg_emp_assignment_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_emp_assignment_prd
    ADD CONSTRAINT eg_emp_assignment_prd_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_employee
    ADD CONSTRAINT eg_employee_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_employee_dept
    ADD CONSTRAINT eg_employee_dept_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_uom
    ADD CONSTRAINT eg_uom_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_uomcategory
    ADD CONSTRAINT eg_uomcategory_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egdms_notification
    ADD CONSTRAINT egdms_notification_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egw_satuschange
    ADD CONSTRAINT egw_satuschange_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT egdms_notification_group_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_pkey PRIMARY KEY (grade_id);

ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT sub_scheme_pkey PRIMARY KEY (id);

ALTER TABLE ONLY financial_institution
    ADD CONSTRAINT financial_institution_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egw_contractor_grade
    ADD CONSTRAINT egw_contractor_grade_pkey PRIMARY KEY (id);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT voucherheader_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_record_status
    ADD CONSTRAINT egf_record_status_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egeis_community_mstr
    ADD CONSTRAINT egeis_community_mstr_pkey PRIMARY KEY (community_id);

ALTER TABLE ONLY egeis_elig_cert_type
    ADD CONSTRAINT egeis_elig_cert_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egeis_bloodgroup
    ADD CONSTRAINT egeis_bloodgroup_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egeis_religion_mstr
    ADD CONSTRAINT egeis_religion_mstr_pkey PRIMARY KEY (religion_id);

ALTER TABLE ONLY egeis_mode_of_recruiment_mstr
    ADD CONSTRAINT egeis_mode_of_recruiment_mstr_pkey PRIMARY KEY (mode_of_recruiment_id);

ALTER TABLE ONLY egeis_relation_type
    ADD CONSTRAINT egeis_relation_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egeis_category_mstr
    ADD CONSTRAINT egeis_category_mstr_pkey PRIMARY KEY (category_id);

ALTER TABLE ONLY egeis_leave_status
    ADD CONSTRAINT egeis_leave_status_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egeis_local_lang_qul_mstr
    ADD CONSTRAINT egeis_local_lang_qul_mstr_pkey PRIMARY KEY (qulified_id);

ALTER TABLE ONLY egeis_service_history
    ADD CONSTRAINT egeis_service_history_pkey PRIMARY KEY (id);

ALTER TABLE ONLY accountdetailkey
    ADD CONSTRAINT accountdetailkey_pkey PRIMARY KEY (id);

ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT accountentitymaster_pkey PRIMARY KEY (id);

ALTER TABLE ONLY accountgroup
    ADD CONSTRAINT accountgroup_pkey PRIMARY KEY (id);

ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (id);

ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT bankaccount_pkey PRIMARY KEY (id);

ALTER TABLE ONLY bankbranch
    ADD CONSTRAINT bankbranch_pkey PRIMARY KEY (id);

ALTER TABLE ONLY bankentries
    ADD CONSTRAINT bankentries_pkey PRIMARY KEY (id);

ALTER TABLE ONLY bankentries_mis
    ADD CONSTRAINT bankentries_mis_pkey PRIMARY KEY (id);

ALTER TABLE ONLY bankreconciliation
    ADD CONSTRAINT bankreconciliation_pkey PRIMARY KEY (id);

ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT chartofaccountdetail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cheque_dept_mapping
    ADD CONSTRAINT cheque_dept_mapping_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_object_history
    ADD CONSTRAINT eg_object_history_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_rulegroup
    ADD CONSTRAINT eg_rulegroup_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_rules
    ADD CONSTRAINT eg_rules_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_ruletype
    ADD CONSTRAINT eg_ruletype_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_tasks
    ADD CONSTRAINT eg_tasks_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_ielist
    ADD CONSTRAINT eg_ielist_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_entity
    ADD CONSTRAINT eg_entity_pkey PRIMARY KEY (id);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fundsource_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_surrendered_cheques
    ADD CONSTRAINT eg_surrendered_cheques_pkey PRIMARY KEY (id);

ALTER TABLE ONLY function
    ADD CONSTRAINT function_code_key UNIQUE (code);

 
ALTER TABLE ONLY function
    ADD CONSTRAINT fk_function FOREIGN KEY (parentid) REFERENCES function(id);

ALTER TABLE ONLY functionary
    ADD CONSTRAINT functionary_code_key UNIQUE (code);

ALTER TABLE ONLY functionary
    ADD CONSTRAINT functionary_name_key UNIQUE (name);

ALTER TABLE ONLY fund
    ADD CONSTRAINT fund_code_key UNIQUE (code);

ALTER TABLE ONLY fund
    ADD CONSTRAINT fk_fund1 FOREIGN KEY (parentid) REFERENCES fund(id);

ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT fk_designation_chartofacc_id FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);

ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT fk_grade_id_desig FOREIGN KEY (grade_id) REFERENCES egeis_grade_mstr(grade_id);

ALTER TABLE ONLY eg_drawingofficer
    ADD CONSTRAINT eg_drawingofficer_code_key UNIQUE (code);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT des_fk FOREIGN KEY (designationid) REFERENCES eg_designation(designationid);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT function_fk FOREIGN KEY (id_function) REFERENCES function(id);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT id_fund_fk FOREIGN KEY (id_fund) REFERENCES fund(id);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT main_de FOREIGN KEY (main_dept) REFERENCES eg_department(id_dept);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT pos_id FOREIGN KEY (position_id) REFERENCES eg_position(id);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT prd_fk FOREIGN KEY (id_emp_assign_prd) REFERENCES eg_emp_assignment_prd(id);

ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT reports_fk FOREIGN KEY (reports_to) REFERENCES eg_employee(id);

ALTER TABLE ONLY eg_emp_assignment_prd
    ADD CONSTRAINT id_employee_fk FOREIGN KEY (id_employee) REFERENCES eg_employee(id);

ALTER TABLE ONLY eg_employee
    ADD CONSTRAINT eg_employee_code_key UNIQUE (code);


ALTER TABLE ONLY eg_employee
    ADD CONSTRAINT id_user_fk FOREIGN KEY (id_user) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_employee
    ADD CONSTRAINT status_egw_fk FOREIGN KEY (status) REFERENCES egw_status(id);

ALTER TABLE ONLY eg_employee_dept
    ADD CONSTRAINT ass_id FOREIGN KEY (assignment_id) REFERENCES eg_emp_assignment(id);

ALTER TABLE ONLY eg_employee_dept
    ADD CONSTRAINT dept_ids FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);

ALTER TABLE ONLY eg_employee_dept
    ADD CONSTRAINT hod_id FOREIGN KEY (hod) REFERENCES eg_department(id_dept);

ALTER TABLE ONLY eg_uom
    ADD CONSTRAINT eg_uom_uom_key UNIQUE (uom);

ALTER TABLE ONLY eg_uom
    ADD CONSTRAINT fk_uom_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_uom
    ADD CONSTRAINT fk_uom_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_uom
    ADD CONSTRAINT id_uomcategory FOREIGN KEY (uomcategoryid) REFERENCES eg_uomcategory(id);

ALTER TABLE ONLY eg_uomcategory
    ADD CONSTRAINT eg_uomcategory_category_key UNIQUE (category);

ALTER TABLE ONLY eg_uomcategory
    ADD CONSTRAINT fk_uct_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_uomcategory
    ADD CONSTRAINT fk_uct_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_view
    ADD CONSTRAINT eg_view_complaintnumber_key UNIQUE (complaintnumber);

ALTER TABLE ONLY eg_view
    ADD CONSTRAINT fk_user_view FOREIGN KEY (userid) REFERENCES eg_user(id_user);


ALTER TABLE ONLY egdms_notification
    ADD CONSTRAINT fk_notify_user FOREIGN KEY (position_id) REFERENCES eg_position(id);

ALTER TABLE ONLY egw_satuschange
    ADD CONSTRAINT fk_sc_usr FOREIGN KEY (createdby) REFERENCES eg_user(id_user);


ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT egdms_notification_group_name_key UNIQUE (name);

ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT fk_group_created_by FOREIGN KEY (created_by) REFERENCES eg_user(id_user);

ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT fk_group_modified_by FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);

ALTER TABLE ONLY egdms_notification_user
    ADD CONSTRAINT fk_group_id FOREIGN KEY (group_id) REFERENCES egdms_notification_group(id);

ALTER TABLE ONLY egdms_notification_user
    ADD CONSTRAINT fk_group_user FOREIGN KEY (position_id) REFERENCES eg_position(id);

ALTER TABLE ONLY egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_grade_value_key UNIQUE (grade_value);

ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT sub_scheme_code_schemeid_key UNIQUE (code, schemeid);

ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT fk_sub_scheme_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT fk_sub_scheme_department FOREIGN KEY (department) REFERENCES eg_department(id_dept);

ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT fk_sub_scheme_modified FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT sub_scheme_r01 FOREIGN KEY (schemeid) REFERENCES scheme(id);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT voucherheader_cgvn_fiscalperiodid_key UNIQUE (cgvn, fiscalperiodid);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_fp_vh FOREIGN KEY (fiscalperiodid) REFERENCES fiscalperiod(id);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_fund_vh FOREIGN KEY (fundid) REFERENCES fund(id);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_fundsource_vh FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_vh_functionidpk FOREIGN KEY (functionid) REFERENCES function(id);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_voucher_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);

ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_voucherheader_vh FOREIGN KEY (originalvcid) REFERENCES voucherheader(id);

ALTER TABLE ONLY egf_record_status
    ADD CONSTRAINT fk_vh_rs FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);

ALTER TABLE ONLY egeis_service_history
    ADD CONSTRAINT fk_service_empid FOREIGN KEY (emp_id) REFERENCES eg_employee(id);

ALTER TABLE ONLY accountdetailkey
    ADD CONSTRAINT fk_coa_dk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);

ALTER TABLE ONLY accountdetailkey
    ADD CONSTRAINT fk_dt_dk FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY accountdetailtype
    ADD CONSTRAINT accountdetailtype_attributename_key UNIQUE (attributename);

ALTER TABLE ONLY accountdetailtype
    ADD CONSTRAINT accountdetailtype_name_key UNIQUE (name);

ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT accountentitymaster_code_key UNIQUE (code);

ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT accountentitymaster_name_key UNIQUE (name);

ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT fk_dt_aem FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT fk_userid_pk FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_code_key UNIQUE (code);

ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_name_key UNIQUE (name);

ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT bankaccount_branchid_accountnumber_key UNIQUE (branchid, accountnumber);

ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT fk_bb_ba FOREIGN KEY (branchid) REFERENCES bankbranch(id);

ALTER TABLE ONLY bankbranch
    ADD CONSTRAINT bankbranch_bankid_branchname_key UNIQUE (bankid, branchname);

ALTER TABLE ONLY bankbranch
    ADD CONSTRAINT fk_bk_bb FOREIGN KEY (bankid) REFERENCES bank(id);

ALTER TABLE ONLY bankentries
    ADD CONSTRAINT fkbaid FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY bankentries
    ADD CONSTRAINT fkcoaid FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);

ALTER TABLE ONLY bankentries
    ADD CONSTRAINT fkvhid FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);

ALTER TABLE ONLY bankentries_mis
    ADD CONSTRAINT sys_c0011773 FOREIGN KEY (bankentries_id) REFERENCES bankentries(id);

ALTER TABLE ONLY bankentries_mis
    ADD CONSTRAINT sys_c0011774 FOREIGN KEY (function_id) REFERENCES function(id);

ALTER TABLE ONLY bankreconciliation
    ADD CONSTRAINT fk_bacc_brs FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);


ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT chartofaccountdetail_glcodeid_detailtypeid_key UNIQUE (glcodeid, detailtypeid);

ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT fk_coadt FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);

ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT fk_dt_coa FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT chartofaccounts_glcode_key UNIQUE (glcode);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fiescheduleid_shedule_map_fk FOREIGN KEY (fiescheduleid) REFERENCES schedulemapping(id);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_coa_coa FOREIGN KEY (parentid) REFERENCES chartofaccounts(id);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_cos_sch FOREIGN KEY (scheduleid) REFERENCES schedulemapping(id);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_cos_sch1 FOREIGN KEY (receiptscheduleid) REFERENCES schedulemapping(id);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_cos_sch2 FOREIGN KEY (paymentscheduleid) REFERENCES schedulemapping(id);

ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_pur_coa FOREIGN KEY (purposeid) REFERENCES egf_accountcode_purpose(id);


ALTER TABLE ONLY cheque_dept_mapping
    ADD CONSTRAINT chequedept_dept_fk FOREIGN KEY (allotedto) REFERENCES eg_department(id_dept);

ALTER TABLE ONLY eg_object_history
    ADD CONSTRAINT fk_modified_by FOREIGN KEY (modifed_by) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_object_history
    ADD CONSTRAINT fk_object_type_id FOREIGN KEY (object_type_id) REFERENCES eg_object_type(id);

ALTER TABLE ONLY eg_rulegroup
    ADD CONSTRAINT eg_rulegroup_name_key UNIQUE (name);

ALTER TABLE ONLY eg_rgrule_map
    ADD CONSTRAINT eg_rgrule_map_ruleid_key UNIQUE (ruleid);

ALTER TABLE ONLY eg_rgrule_map
    ADD CONSTRAINT fkrule FOREIGN KEY (ruleid) REFERENCES eg_rules(id);

ALTER TABLE ONLY eg_rgrule_map
    ADD CONSTRAINT fkrulegroup FOREIGN KEY (rulegroupid) REFERENCES eg_rulegroup(id);

ALTER TABLE ONLY eg_rules
    ADD CONSTRAINT eg_rules_name_key UNIQUE (name);

ALTER TABLE ONLY eg_tasks
    ADD CONSTRAINT eg_tasks_name_key UNIQUE (name);

ALTER TABLE ONLY eg_ielist
    ADD CONSTRAINT fkruleid FOREIGN KEY (ruleid) REFERENCES eg_rules(id);

ALTER TABLE ONLY eg_entity
    ADD CONSTRAINT eg_entity_name_key UNIQUE (name);

ALTER TABLE ONLY eg_actionrg_map
    ADD CONSTRAINT eg_actionrg_map_rulegroupid_key UNIQUE (rulegroupid);

ALTER TABLE ONLY eg_actionrg_map
    ADD CONSTRAINT fkaction FOREIGN KEY (actionid) REFERENCES eg_action(id);

ALTER TABLE ONLY eg_actionrg_map
    ADD CONSTRAINT fkrg FOREIGN KEY (rulegroupid) REFERENCES eg_rulegroup(id);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fundsource_code_key UNIQUE (code);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fundsource_name_key UNIQUE (name);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fin_source_bankaccount_fk FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fin_source_sub_scheme_fk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fin_src_crtd_fk FOREIGN KEY (createdby) REFERENCES eg_user(id_user);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fk_fs FOREIGN KEY (parentid) REFERENCES fundsource(id);

ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fundsource_fin_inst_fk FOREIGN KEY (financialinstid) REFERENCES financial_institution(id);

ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT eg_designation_designation_name_key UNIQUE (designation_name);

ALTER TABLE ONLY eg_surrendered_cheques
    ADD CONSTRAINT fk_surc_ba FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY eg_surrendered_cheques
    ADD CONSTRAINT fk_surc_vh FOREIGN KEY (vhid) REFERENCES voucherheader(id);

ALTER TABLE ONLY fiscalperiod
    ADD CONSTRAINT fk_fp_md FOREIGN KEY (moduleid) REFERENCES eg_module(id_module);

ALTER TABLE ONLY fiscalperiod
    ADD CONSTRAINT fk_fy_fp FOREIGN KEY (financialyearid) REFERENCES financialyear(id);




ALTER TABLE ONLY schedulemapping
    ADD CONSTRAINT schedulemapping_schedule_reporttype_key UNIQUE (schedule, reporttype);


ALTER TABLE ONLY schedulemapping
    ADD CONSTRAINT fk_scd_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);


ALTER TABLE ONLY schedulemapping
    ADD CONSTRAINT fk_sch_cuser FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



CREATE INDEX indx_fund_purposeid ON fund USING btree (purpose_id);

CREATE INDEX index_emp_assgn_designationid ON eg_emp_assignment USING btree (designationid);

CREATE INDEX index_emp_assgn_id_function ON eg_emp_assignment USING btree (id_function);

CREATE INDEX index_emp_assgn_id_functionary ON eg_emp_assignment USING btree (id_functionary);

CREATE INDEX index_emp_assgn_id_fund ON eg_emp_assignment USING btree (id_fund);

CREATE INDEX index_emp_assgn_id_prd ON eg_emp_assignment USING btree (id_emp_assign_prd);

CREATE INDEX index_emp_assgn_main_dept ON eg_emp_assignment USING btree (main_dept);

CREATE INDEX index_emp_assgn_position_id ON eg_emp_assignment USING btree (position_id);

CREATE INDEX index_emp_assgn_reports_to ON eg_emp_assignment USING btree (reports_to);

CREATE INDEX index_emp_assgn_id_emp ON eg_emp_assignment_prd USING btree (id_employee);

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

CREATE INDEX index_emp_dept_assignment_id ON eg_employee_dept USING btree (assignment_id);

CREATE INDEX index_emp_dept_deptid ON eg_employee_dept USING btree (deptid);

CREATE INDEX index_emp_dept_hod ON eg_employee_dept USING btree (hod);

CREATE INDEX indx_esc_accountid ON eg_surrendered_cheques USING btree (bankaccountid);

CREATE INDEX indx_esc_vhid ON eg_surrendered_cheques USING btree (vhid);

CREATE INDEX indx_euom_categoryid ON eg_uom USING btree (uomcategoryid);

CREATE INDEX idx_sc_createdby ON egw_satuschange USING btree (createdby);

CREATE INDEX indx_esc_fstatusid ON egw_satuschange USING btree (fromstatus);

CREATE INDEX indx_esc_tostatusid ON egw_satuschange USING btree (tostatus);

CREATE INDEX indx_schemeid ON sub_scheme USING btree (schemeid);

CREATE UNIQUE INDEX cont_grade_unq ON egw_contractor_grade USING btree (grade);

CREATE UNIQUE INDEX cgn_c ON voucherheader USING btree (cgn);

CREATE INDEX indx_vh_fsourcesid ON voucherheader USING btree (fundsourceid);

CREATE INDEX indx_vh_fundid ON voucherheader USING btree (fundid);

CREATE INDEX idx_recordstatus_voucherhdid ON egf_record_status USING btree (voucherheaderid);

CREATE INDEX indx_acdk_acdtid ON accountdetailkey USING btree (detailtypeid);

CREATE INDEX indx_aem_acdtid ON accountentitymaster USING btree (detailtypeid);

CREATE INDEX indx_bankaccount_coaid ON bankaccount USING btree (glcodeid);

CREATE INDEX indx_bankaccount_fundid ON bankaccount USING btree (fundid);

CREATE UNIQUE INDEX bank_branch_code ON bankbranch USING btree (bankid, branchcode);

CREATE INDEX indx_be_bankaccountid ON bankentries USING btree (bankaccountid);

CREATE INDEX indx_be_coaid ON bankentries USING btree (glcodeid);

CREATE INDEX indx_be_vhid ON bankentries USING btree (voucherheaderid);

CREATE INDEX indx_br_bankaccountid ON bankreconciliation USING btree (bankaccountid);

CREATE INDEX indx_coad_acdtid ON chartofaccountdetail USING btree (detailtypeid);

CREATE INDEX indx_coad_coaid ON chartofaccountdetail USING btree (glcodeid);

CREATE INDEX coa_type ON chartofaccounts USING btree (type);

CREATE INDEX indx_coa_payscheduleid ON chartofaccounts USING btree (paymentscheduleid);

CREATE INDEX indx_coa_purposeid ON chartofaccounts USING btree (purposeid);

CREATE INDEX indx_coa_receiptscheduleid ON chartofaccounts USING btree (receiptscheduleid);

CREATE INDEX indx_coa_scheduleid ON chartofaccounts USING btree (scheduleid);

CREATE UNIQUE INDEX fsp_name ON fiscalperiod USING btree (name);

CREATE INDEX indx_fp_finyearid ON fiscalperiod USING btree (financialyearid);
