ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT eg_remittance_pkey PRIMARY KEY (id);

ALTER TABLE ONLY generalledger
    ADD CONSTRAINT generalledger_pkey PRIMARY KEY (id);
ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT egw_typeofwork_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tds
    ADD CONSTRAINT tds_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_remittance_detail
    ADD CONSTRAINT eg_remittance_detail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_remittance_gldtl
    ADD CONSTRAINT eg_remittance_gldtl_pkey PRIMARY KEY (id);

ALTER TABLE ONLY generalledgerdetail
    ADD CONSTRAINT generalledgerdetail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT egf_ebconsumer_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT egf_ebdetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_target_area
    ADD CONSTRAINT egf_target_area_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_wardtargetarea_mapping
    ADD CONSTRAINT egf_wardtargetarea_mapping_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_ebschedulerlog
    ADD CONSTRAINT egf_ebschedulerlog_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_ecstype
    ADD CONSTRAINT egf_ecstype_pkey PRIMARY KEY (id);


ALTER TABLE ONLY egf_ebschedulerlogdetails
    ADD CONSTRAINT egf_ebschedulerlogdetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_fundingagency
    ADD CONSTRAINT egf_fundingagency_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT egf_loangrantdetail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_loangrantheader
    ADD CONSTRAINT egf_loangrantheader_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT egf_loangrantreceiptdetail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT egf_scheme_bankaccount_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_subscheme_project
    ADD CONSTRAINT egf_subscheme_project_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT egf_fixeddeposit_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT eg_advancereqpayeedetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT egf_grant_pkey PRIMARY KEY (id);


ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT eg_advancerequisition_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT eg_advancerequisitiondetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT eg_advancerequisitionmis_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT eg_billdetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_billpayeedetails
    ADD CONSTRAINT eg_billpayeedetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT eg_billregister_pkey PRIMARY KEY (id);


ALTER TABLE ONLY eg_bill_subtype
    ADD CONSTRAINT eg_bill_subtype_pkey PRIMARY KEY (id);


ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT miscbilldetail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT egf_budget_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT egf_budgetdetail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT egf_budgetgroup_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT egf_budget_reappropriation_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_reappropriation_misc
    ADD CONSTRAINT egf_reappropriation_misc_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_budget_usage
    ADD CONSTRAINT egf_budget_usage_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_dept_functionmap
    ADD CONSTRAINT eg_dept_functionmap_pkey PRIMARY KEY (id);


ALTER TABLE ONLY egf_account_cheques
    ADD CONSTRAINT egf_account_cheques_pkey PRIMARY KEY (id);

ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT contrajournalvoucher_pkey PRIMARY KEY (id);

ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT transactionsummary_pkey PRIMARY KEY (id);


ALTER TABLE ONLY eg_dept_do_mapping
    ADD CONSTRAINT eg_dept_do_mapping_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishonorcheque_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishonorcheque_pkey PRIMARY KEY (id);


ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT egf_instrumentaccountcodes_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT egf_instrumentheader_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_instrumenttype
    ADD CONSTRAINT egf_instrumenttype_pkey PRIMARY KEY (id);


ALTER TABLE ONLY paymentheader
    ADD CONSTRAINT paymentheader_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT eg_deduction_details_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT egf_recovery_bankdetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_remittance_schd_payment
    ADD CONSTRAINT egf_remittance_schd_payment_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_remittance_scheduler
    ADD CONSTRAINT egf_remittance_scheduler_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT egf_fundflow_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT fk_rmt_fd FOREIGN KEY (fundid) REFERENCES fund(id);

ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT fk_rmt_fy FOREIGN KEY (fyid) REFERENCES financialyear(id);

ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT fk_rmt_tds FOREIGN KEY (tdsid) REFERENCES tds(id);

ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT fk_rmt_usr FOREIGN KEY (createdby) REFERENCES eg_user(id);

ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT fk_rmt_usr1 FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);

ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT fk_rmt_vh FOREIGN KEY (paymentvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY eg_remittance_gldtl
    ADD CONSTRAINT fk_rmtgl_gld FOREIGN KEY (gldtlid) REFERENCES generalledgerdetail(id);

ALTER TABLE ONLY eg_remittance_gldtl
    ADD CONSTRAINT sys_c009869 FOREIGN KEY (tdsid) REFERENCES tds(id);


ALTER TABLE ONLY generalledgerdetail
    ADD CONSTRAINT fk_dt_gld FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY generalledgerdetail
    ADD CONSTRAINT fk_gl_gld FOREIGN KEY (generalledgerid) REFERENCES generalledger(id);


ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT fk_ebconsumer_boundary FOREIGN KEY (wardid) REFERENCES eg_boundary(id);

ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT fk_ebconsumer_created_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id);

ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT fk_ebconsumer_modified_user_id FOREIGN KEY (modifiedby) REFERENCES eg_user(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_bill_id FOREIGN KEY (billid) REFERENCES eg_billregister(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_consumer_no FOREIGN KEY (consumerno) REFERENCES egf_ebconsumer(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_created_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_modified_user_id FOREIGN KEY (modifiedby) REFERENCES eg_user(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_status FOREIGN KEY (status) REFERENCES egw_status(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_fyid FOREIGN KEY (financialyearid) REFERENCES financialyear(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_pos FOREIGN KEY (position_id) REFERENCES eg_position(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_ta FOREIGN KEY (target_area_id) REFERENCES egf_target_area(id);

ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_ward FOREIGN KEY (wardid) REFERENCES eg_boundary(id);


ALTER TABLE ONLY egf_target_area
    ADD CONSTRAINT fk_id_position FOREIGN KEY (positionid) REFERENCES eg_position(id);

ALTER TABLE ONLY egf_target_area
    ADD CONSTRAINT fk_target_area_created_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id);

ALTER TABLE ONLY egf_target_area
    ADD CONSTRAINT fk_target_area_modify_user_id FOREIGN KEY (modifiedby) REFERENCES eg_user(id);

ALTER TABLE ONLY egf_wardtargetarea_mapping
    ADD CONSTRAINT fk_target_area FOREIGN KEY (targetareaid) REFERENCES egf_target_area(id);

ALTER TABLE ONLY egf_wardtargetarea_mapping
    ADD CONSTRAINT fk_ward FOREIGN KEY (boundaryid) REFERENCES eg_boundary(id);

ALTER TABLE ONLY egf_ebschedulerlogdetails
    ADD CONSTRAINT fk_egf_eblog_ebconsumer FOREIGN KEY (consumer) REFERENCES egf_ebconsumer(id);

ALTER TABLE ONLY egf_fundingagency
    ADD CONSTRAINT egf_fundingagency_code_key UNIQUE (code);

ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT fk_egf_lgdetail_agency FOREIGN KEY (agencyid) REFERENCES egf_fundingagency(id);

ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT fk_egf_lgdetail_lgheader FOREIGN KEY (headerid) REFERENCES egf_loangrantheader(id);

ALTER TABLE ONLY egf_loangrantheader
    ADD CONSTRAINT fk_egf_lgheader_subscheme FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);

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

ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_bankacc FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_sch FOREIGN KEY (schemeid) REFERENCES scheme(id);

ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_subsch FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);

ALTER TABLE ONLY egf_subscheme_project
    ADD CONSTRAINT fk_egf_subscheme_prj_subsch FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);

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




ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT fk_arpd_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT fk_arpd_ard FOREIGN KEY (advancerequisitiondetailid) REFERENCES eg_advancerequisitiondetails(id);



ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT fk_arpd_tds FOREIGN KEY (tdsid) REFERENCES tds(id);

ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT eg_advancerequisition_advancerequisitionnumber_key UNIQUE (advancerequisitionnumber);





ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereq_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereqcreator_user FOREIGN KEY (createdby) REFERENCES eg_user(id);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereqmodifier_user FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereqstatus_egwstatus FOREIGN KEY (statusid) REFERENCES egw_status(id);





ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT fk_advreqdetail_brg FOREIGN KEY (advancerequisitionid) REFERENCES eg_advancerequisition(id);



ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT fk_advreqdetail_fun FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT fk_advreqdetail_gl FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armis_dpt FOREIGN KEY (departmentid) REFERENCES eg_department(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armis_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisar_ar FOREIGN KEY (advancerequisitionid) REFERENCES eg_advancerequisition(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armischeme_scheme FOREIGN KEY (schemeid) REFERENCES scheme(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisfield_bdry FOREIGN KEY (fieldid) REFERENCES eg_boundary(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisfund_fd FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisfunry_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armissubfield_bdry FOREIGN KEY (subfieldid) REFERENCES eg_boundary(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisubsm_subscheme FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisvh_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_brg FOREIGN KEY (billid) REFERENCES eg_billregister(id);



ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_fun FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_gl FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_billpayeedetails
    ADD CONSTRAINT fk_bdp_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY eg_billpayeedetails
    ADD CONSTRAINT sys_c009660 FOREIGN KEY (tdsid) REFERENCES tds(id);


ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT eg_billregister_billnumber_key UNIQUE (billnumber);




ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT fk_br_fd FOREIGN KEY (fieldid) REFERENCES eg_boundary(id);


ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT fk_br_usr FOREIGN KEY (createdby) REFERENCES eg_user(id);



ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT fk_br_usr1 FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);


ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT sys_c0010469 FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);


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



ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT fk_mbd_pbi FOREIGN KEY (paidbyid) REFERENCES eg_user(id);



ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT fk_mbd_pvh FOREIGN KEY (payvhid) REFERENCES voucherheader(id);

ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT fk_mbd_vh FOREIGN KEY (billvhid) REFERENCES voucherheader(id);

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



ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_majorcode FOREIGN KEY (majorcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_maxcode FOREIGN KEY (maxcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_mincode FOREIGN KEY (mincode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_egf_budgetdetail FOREIGN KEY (budgetdetail) REFERENCES egf_budgetdetail(id);


ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_egf_reappropriation_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_reapp_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_reappropriation_misc FOREIGN KEY (reappropriation_misc) REFERENCES egf_reappropriation_misc(id);



ALTER TABLE ONLY egf_reappropriation_misc
    ADD CONSTRAINT egf_reappropriation_misc_sequence_number_key UNIQUE (sequence_number);


ALTER TABLE ONLY egf_budget_usage
    ADD CONSTRAINT fk_fp_bu FOREIGN KEY (financialyearid) REFERENCES financialyear(id);


ALTER TABLE ONLY egf_account_cheques
    ADD CONSTRAINT fk_ba_chq FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);


ALTER TABLE ONLY cheque_dept_mapping
    ADD CONSTRAINT chequedept_cheque_fk FOREIGN KEY (accountchequeid) REFERENCES egf_account_cheques(id);



ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT fk_ba_cjv FOREIGN KEY (frombankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT fk_ba_cjv1 FOREIGN KEY (tobankaccountid) REFERENCES bankaccount(id);

ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT fk_cjv_ih FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);


ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT fk_vh_cjv FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_dettype FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_fs_txn FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);

ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_fy_ts FOREIGN KEY (financialyearid) REFERENCES financialyear(id);

ALTER TABLE ONLY eg_dept_do_mapping
    ADD CONSTRAINT fk_deptdo_deptid FOREIGN KEY (department_id) REFERENCES eg_department(id);



ALTER TABLE ONLY eg_dept_do_mapping
    ADD CONSTRAINT fk_deptdo_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_fund_ts FOREIGN KEY (fundid) REFERENCES fund(id);

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


ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqde_head_fk FOREIGN KEY (headerid) REFERENCES egf_dishonorcheque(id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_acdet_fk FOREIGN KEY (detailtype) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_acdetky_fk FOREIGN KEY (detailkey) REFERENCES accountdetailkey(id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_glcode_fk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);


ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT fk_egf_instracccodes_coa FOREIGN KEY (typeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT fk_egf_instracccodes_instrtype FOREIGN KEY (typeid) REFERENCES egf_instrumenttype(id);



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


ALTER TABLE ONLY egf_instrumenttype
    ADD CONSTRAINT egf_instrumenttype_type_key UNIQUE (type);


ALTER TABLE ONLY paymentheader
    ADD CONSTRAINT fk_ba_ph FOREIGN KEY (bankaccountnumberid) REFERENCES bankaccount(id);
ALTER TABLE ONLY paymentheader
    ADD CONSTRAINT fk_ph_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id);

ALTER TABLE ONLY paymentheader
    ADD CONSTRAINT fk_vh_ph FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);


ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT fk_dedd_fy FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);



ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT fk_dedd_typw FOREIGN KEY (doctypeid) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT fk_dedd_typw1 FOREIGN KEY (docsubtypeid) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY tds
    ADD CONSTRAINT tds_type_key UNIQUE (type);

ALTER TABLE ONLY tds
    ADD CONSTRAINT fk_coa_tds FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);
ALTER TABLE ONLY tds
    ADD CONSTRAINT fk_tds_bk FOREIGN KEY (bankid) REFERENCES bank(id);



ALTER TABLE ONLY tds
    ADD CONSTRAINT fk_tds_fy FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);


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

ALTER TABLE ONLY egf_remittance_schd_payment
    ADD CONSTRAINT fk_schpay_sch FOREIGN KEY (schd_id) REFERENCES egf_remittance_scheduler(id);



ALTER TABLE ONLY egf_remittance_schd_payment
    ADD CONSTRAINT fk_schpay_vh FOREIGN KEY (vhid) REFERENCES voucherheader(id);


ALTER TABLE ONLY egf_remittance_scheduler
    ADD CONSTRAINT fk_sch_cretby FOREIGN KEY (createdby) REFERENCES eg_user(id);



ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT fk_fundflow_bankaccount_id FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT fk_fundflow_create_eg_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id);



ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT fk_fundflow_modif_eg_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id);

ALTER TABLE ONLY egf_ecstype
    ADD CONSTRAINT egf_ecstype_type_key UNIQUE (type);
ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT fk_typeofwork_typeofwork FOREIGN KEY (parentid) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT fk_typeofwork_usr FOREIGN KEY (createdby) REFERENCES eg_user(id);



ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT fk_typeofwork_usr1 FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);


ALTER TABLE ONLY generalledger
    ADD CONSTRAINT fk_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);
ALTER TABLE ONLY generalledger
    ADD CONSTRAINT fk_coa_gl FOREIGN KEY (glcode) REFERENCES chartofaccounts(glcode);
ALTER TABLE ONLY generalledger
    ADD CONSTRAINT fk_fun_gl FOREIGN KEY (functionid) REFERENCES function(id);
ALTER TABLE ONLY generalledger
    ADD CONSTRAINT fk_voucherheader FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);


CREATE INDEX idx_remit_detail_gldtl ON eg_remittance_detail USING btree (remittancegldtlid);

CREATE INDEX idx_remit_detail_remit ON eg_remittance_detail USING btree (remittanceid);

CREATE INDEX indx_gld_acdtypeid ON generalledgerdetail USING btree (detailtypeid);

CREATE INDEX indx_gld_glid ON generalledgerdetail USING btree (generalledgerid);

CREATE INDEX idx_egf_ebdetails_fyid ON egf_ebdetails USING btree (financialyearid);

CREATE INDEX idx_egf_ebdetails_pos ON egf_ebdetails USING btree (position_id);

CREATE INDEX idx_egf_ebdetails_ta ON egf_ebdetails USING btree (target_area_id);

CREATE INDEX idx_egf_ebdetails_ward ON egf_ebdetails USING btree (wardid);

CREATE INDEX indx_arpd_adtid ON eg_advancereqpayeedetails USING btree (accountdetailtypeid);

CREATE INDEX indx_arpd_ardid ON eg_advancereqpayeedetails USING btree (advancerequisitiondetailid);

CREATE INDEX indx_arpd_tdsid ON eg_advancereqpayeedetails USING btree (tdsid);

CREATE INDEX indx_advreqdetail_advreqid ON eg_advancerequisitiondetails USING btree (advancerequisitionid);

CREATE INDEX indx_advreqdetail_functionid ON eg_advancerequisitiondetails USING btree (functionid);

CREATE INDEX indx_advreqdetail_glcodeid ON eg_advancerequisitiondetails USING btree (glcodeid);

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


CREATE INDEX indx_ebd_billid ON eg_billdetails USING btree (billid);




CREATE INDEX indx_ebd_functionid ON eg_billdetails USING btree (functionid);



CREATE INDEX indx_ebd_glcodeid ON eg_billdetails USING btree (glcodeid);

CREATE INDEX index_egbill_payd_accdetkey ON eg_billpayeedetails USING btree (accountdetailkeyid);


CREATE INDEX indx_ebpd_adtid ON eg_billpayeedetails USING btree (accountdetailtypeid);

CREATE INDEX indx_ebpd_bdid ON eg_billpayeedetails USING btree (billdetailid);

CREATE INDEX indx_billreg_expendituretype ON eg_billregister USING btree (expendituretype);


CREATE INDEX indx_billreg_statusid ON eg_billregister USING btree (statusid);


CREATE INDEX indx_billmis_departmentid ON eg_billregistermis USING btree (departmentid);



CREATE INDEX indx_billmis_voucherheaderid ON eg_billregistermis USING btree (voucherheaderid);


CREATE INDEX indx_ebrm_billid ON eg_billregistermis USING btree (billid);



CREATE INDEX indx_ebrm_fieldid ON eg_billregistermis USING btree (fieldid);



CREATE INDEX indx_ebrm_funationaryid ON eg_billregistermis USING btree (functionaryid);



CREATE INDEX indx_ebrm_fundid ON eg_billregistermis USING btree (fundid);



CREATE INDEX indx_ebrm_segmentid ON eg_billregistermis USING btree (segmentid);



CREATE INDEX indx_ebrm_subfieldid ON eg_billregistermis USING btree (subfieldid);



CREATE INDEX indx_ebrm_subsegid ON eg_billregistermis USING btree (subsegmentid);


CREATE INDEX indx_mb_paidamount ON miscbilldetail USING btree (paidamount);



CREATE INDEX indx_mb_paidto ON miscbilldetail USING btree (paidto);



CREATE INDEX indx_mbd_pvhid ON miscbilldetail USING btree (payvhid);



CREATE INDEX indx_mbd_vhid ON miscbilldetail USING btree (billvhid);


CREATE INDEX budget_fyear ON egf_budget USING btree (financialyearid);


CREATE INDEX budgetdetail_budget ON egf_budgetdetail USING btree (budget);



CREATE INDEX budgetdetail_budgetgroup ON egf_budgetdetail USING btree (budgetgroup);



CREATE INDEX budgetdetail_dept ON egf_budgetdetail USING btree (executing_department);



CREATE INDEX budgetdetail_function ON egf_budgetdetail USING btree (function);

CREATE INDEX budgetgroup_mincode ON egf_budgetgroup USING btree (mincode);

CREATE INDEX indx_bg_majorcode ON egf_budgetgroup USING btree (majorcode);

CREATE INDEX indx_bg_maxcode ON egf_budgetgroup USING btree (maxcode);


CREATE INDEX indx_cjv_faccountid ON contrajournalvoucher USING btree (frombankaccountid);



CREATE INDEX indx_cjv_toaccountid ON contrajournalvoucher USING btree (tobankaccountid);

CREATE INDEX indx_ts_acdtypeid ON transactionsummary USING btree (accountdetailtypeid);



CREATE INDEX indx_ts_coaid ON transactionsummary USING btree (glcodeid);



CREATE INDEX indx_ts_finyear ON transactionsummary USING btree (financialyearid);



CREATE INDEX indx_ts_fsourseid ON transactionsummary USING btree (fundsourceid);



CREATE INDEX indx_ts_fundid ON transactionsummary USING btree (fundid);


CREATE INDEX indx_ih_in ON egf_instrumentheader USING btree (instrumentnumber);



CREATE INDEX indx_ih_payto ON egf_instrumentheader USING btree (payto);



CREATE INDEX indx_ih_status ON egf_instrumentheader USING btree (id_status);

CREATE INDEX indx_transaction_date ON egf_instrumentheader USING btree (transactiondate);

CREATE INDEX indx_iv_ih ON egf_instrumentvoucher USING btree (instrumentheaderid);



CREATE INDEX indx_iv_vh ON egf_instrumentvoucher USING btree (voucherheaderid);


CREATE INDEX indx_ph_accountid ON paymentheader USING btree (bankaccountnumberid);



CREATE INDEX indx_ph_vhid ON paymentheader USING btree (voucherheaderid);


CREATE INDEX indx_gl_cdt ON generalledger USING btree (creditamount);



CREATE INDEX indx_gl_dbt ON generalledger USING btree (debitamount);



CREATE INDEX indx_gl_functionid ON generalledger USING btree (functionid);



CREATE INDEX indx_gl_glcode ON generalledger USING btree (glcode);



CREATE INDEX indx_gl_glcodeid ON generalledger USING btree (glcodeid);

CREATE INDEX indx_glid_vhid ON generalledger USING btree (voucherheaderid);



CREATE INDEX idx_egw_typeofwork_created ON egw_typeofwork USING btree (createdby);



CREATE INDEX idx_egw_typeofwork_lastmd ON egw_typeofwork USING btree (lastmodifiedby);



CREATE INDEX idx_egw_typeofwork_parent ON egw_typeofwork USING btree (parentid);


