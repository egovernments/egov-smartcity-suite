ALTER TABLE ONLY eg_action
    ADD CONSTRAINT eg_action_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egw_status
    ADD CONSTRAINT egw_status_pkey PRIMARY KEY (id);


ALTER TABLE ONLY eg_department
    ADD CONSTRAINT eg_department_dept_code_key UNIQUE (dept_code);

ALTER TABLE ONLY eg_department
    ADD CONSTRAINT eg_department_dept_name_key UNIQUE (dept_name);

ALTER TABLE ONLY eg_department
    ADD CONSTRAINT eg_department_pkey PRIMARY KEY (id_dept);

ALTER TABLE ONLY calendaryear
    ADD CONSTRAINT calendaryear_calendaryear_key UNIQUE (calendaryear);

ALTER TABLE ONLY calendaryear
    ADD CONSTRAINT calendaryear_pkey PRIMARY KEY (id);

ALTER TABLE ONLY companydetail
    ADD CONSTRAINT companydetail_name_key UNIQUE (name);

ALTER TABLE ONLY companydetail
    ADD CONSTRAINT companydetail_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_action
    ADD CONSTRAINT eg_action_url_queryparams_context_root_key UNIQUE (url, queryparams, context_root);

ALTER TABLE ONLY eg_address_type_master
    ADD CONSTRAINT eg_address_type_master_pkey PRIMARY KEY (id_address_type);

ALTER TABLE ONLY eg_address
    ADD CONSTRAINT eg_address_pkey PRIMARY KEY (addressid);

ALTER TABLE ONLY eg_address
    ADD CONSTRAINT fk_addtype_id FOREIGN KEY (id_addresstypemaster) REFERENCES eg_address_type_master(id_address_type);

ALTER TABLE ONLY eg_appconfig
    ADD CONSTRAINT eg_appconfig_key_name_module_key UNIQUE (key_name, module);

ALTER TABLE ONLY eg_appconfig
    ADD CONSTRAINT eg_appconfig_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_appconfig_values
    ADD CONSTRAINT eg_appconfig_values_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_appl_domain
    ADD CONSTRAINT eg_appl_domain_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_appconfig_values
    ADD CONSTRAINT fk_appdata_key FOREIGN KEY (key_id) REFERENCES eg_appconfig(id);

ALTER TABLE ONLY eg_audit_event
    ADD CONSTRAINT eg_audit_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_authorization_rule
    ADD CONSTRAINT eg_authorization_rule_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_authorization_rule
    ADD CONSTRAINT fk_auth_actionid FOREIGN KEY (actionid) REFERENCES eg_action(id);

CREATE INDEX indx_eb_bndrytypeid ON eg_boundary USING btree (id_bndry_type);

ALTER TABLE ONLY eg_heirarchy_type
    ADD CONSTRAINT eg_heirarchy_type_pkey PRIMARY KEY (id_heirarchy_type);

ALTER TABLE ONLY eg_boundary_type
    ADD CONSTRAINT eg_boundary_type_pkey PRIMARY KEY (id_bndry_type);

ALTER TABLE ONLY eg_boundary
    ADD CONSTRAINT eg_boundary_pkey PRIMARY KEY (id_bndry);


ALTER TABLE ONLY eg_boundary_type
    ADD CONSTRAINT bndry_type_parent FOREIGN KEY (parent) REFERENCES eg_boundary_type(id_bndry_type);

ALTER TABLE ONLY eg_boundary_type
    ADD CONSTRAINT bndry_type_heirarchy_fk FOREIGN KEY (id_heirarchy_type) REFERENCES eg_heirarchy_type(id_heirarchy_type);


ALTER TABLE ONLY eg_boundary
    ADD CONSTRAINT bndry_type_fk FOREIGN KEY (id_bndry_type) REFERENCES eg_boundary_type(id_bndry_type);

ALTER TABLE ONLY eg_user
    ADD CONSTRAINT eg_user_pkey PRIMARY KEY (id_user);

ALTER TABLE ONLY eg_user_sign
    ADD CONSTRAINT eg_user_sign_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_user_jurvalues
    ADD CONSTRAINT fk_bndry_jurvalues FOREIGN KEY (id_bndry) REFERENCES eg_boundary(id_bndry);

ALTER TABLE ONLY eg_user_jurlevel
    ADD CONSTRAINT fk_bndrytype_jurlevel FOREIGN KEY (id_bndry_type) REFERENCES eg_boundary_type(id_bndry_type);

ALTER TABLE ONLY eg_checklists
    ADD CONSTRAINT fk_eg_checklist_appconfig FOREIGN KEY (appconfig_values_id) REFERENCES eg_appconfig_values(id);

ALTER TABLE ONLY eg_checklists
    ADD CONSTRAINT eg_checklists_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_citizen
    ADD CONSTRAINT eg_citizen_pkey PRIMARY KEY (citizenid);

ALTER TABLE ONLY eg_city_website
    ADD CONSTRAINT eg_city_website_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_city_website
    ADD CONSTRAINT fk_bdr_cw FOREIGN KEY (bndryid) REFERENCES eg_boundary(id_bndry);

ALTER TABLE ONLY eg_crossheirarchy_linkage
    ADD CONSTRAINT eg_crossheirarchy_linkage_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_digital_signed_docs
    ADD CONSTRAINT eg_digital_signed_docs_pkey PRIMARY KEY (id);

CREATE INDEX digitalsign_objectid_idx ON eg_digital_signed_docs USING btree (objectid);

ALTER TABLE ONLY eg_digital_signed_docs
    ADD CONSTRAINT fk_digitalsign FOREIGN KEY (createdby) REFERENCES eg_user(id_user);


ALTER TABLE ONLY eg_heirarchy_type
    ADD CONSTRAINT eg_heirarchy_type_type_code_key UNIQUE (type_code);

ALTER TABLE ONLY eg_heirarchy_type
    ADD CONSTRAINT eg_heirarchy_type_type_name_key UNIQUE (type_name);

ALTER TABLE ONLY eg_location
    ADD CONSTRAINT eg_location_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_location_ipmap
    ADD CONSTRAINT eg_location_ipmap_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_location_ipmap
    ADD CONSTRAINT fk_location_id FOREIGN KEY (locationid) REFERENCES eg_location(id);

ALTER TABLE ONLY eg_location_ipmap
    ADD CONSTRAINT eg_location_ipmap_ipaddress_key UNIQUE (ipaddress);

ALTER TABLE ONLY eg_login_log
    ADD CONSTRAINT eg_login_log_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_module
    ADD CONSTRAINT eg_module_module_name_key UNIQUE (module_name);

ALTER TABLE ONLY eg_module
    ADD CONSTRAINT eg_module_pkey PRIMARY KEY (id_module);

ALTER TABLE ONLY eg_modules
    ADD CONSTRAINT eg_modules_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_object_type
    ADD CONSTRAINT eg_object_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_object_type
    ADD CONSTRAINT eg_object_type_type_key UNIQUE (type);

ALTER TABLE ONLY eg_partytype
    ADD CONSTRAINT eg_partytype_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_position_hir
    ADD CONSTRAINT eg_position_hir_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_position_hir
    ADD CONSTRAINT eg_position_hir_position_from_position_to_object_type_id_key UNIQUE (position_from, position_to, object_type_id);

ALTER TABLE ONLY eg_position
    ADD CONSTRAINT eg_position_pkey PRIMARY KEY (id);

CREATE INDEX indx_eram_actionid ON eg_roleaction_map USING btree (actionid);

CREATE INDEX indx_eram_roleid ON eg_roleaction_map USING btree (roleid);

ALTER TABLE ONLY eg_roles
    ADD CONSTRAINT eg_roles_pkey PRIMARY KEY (id_role);

ALTER TABLE ONLY eg_roles
    ADD CONSTRAINT eg_roles_role_name_key UNIQUE (role_name);

ALTER TABLE ONLY eg_roleaction_map
    ADD CONSTRAINT fk_action_id FOREIGN KEY (actionid) REFERENCES eg_action(id);

ALTER TABLE ONLY eg_roleaction_map
    ADD CONSTRAINT fk_role_id FOREIGN KEY (roleid) REFERENCES eg_roles(id_role);

ALTER TABLE ONLY eg_script
    ADD CONSTRAINT eg_script_name_start_date_end_date_key UNIQUE (name, start_date, end_date);

ALTER TABLE ONLY eg_script
    ADD CONSTRAINT eg_script_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_authorization_rule
    ADD CONSTRAINT fk_scriptid_auth FOREIGN KEY (scriptid) REFERENCES eg_script(id);

ALTER TABLE ONLY eg_terminal
    ADD CONSTRAINT eg_terminal_ip_address_key UNIQUE (ip_address);

ALTER TABLE ONLY eg_terminal
    ADD CONSTRAINT eg_terminal_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_terminal
    ADD CONSTRAINT eg_terminal_terminal_name_key UNIQUE (terminal_name);

ALTER TABLE ONLY eg_user_jurlevel
    ADD CONSTRAINT eg_user_jurlevel_id_user_id_bndry_type_key UNIQUE (id_user, id_bndry_type);

ALTER TABLE ONLY eg_user_jurlevel
    ADD CONSTRAINT eg_user_jurlevel_pkey PRIMARY KEY (id_user_jurlevel);

ALTER TABLE ONLY eg_user_jurvalues
    ADD CONSTRAINT eg_user_jurvalues_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_user
    ADD CONSTRAINT eg_user_user_name_key UNIQUE (user_name);

ALTER TABLE ONLY eg_usercounter_map
    ADD CONSTRAINT eg_usercounter_map_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_userdetails
    ADD CONSTRAINT eg_userdetails_id_emp_key UNIQUE (id_emp);

ALTER TABLE ONLY eg_userdetails
    ADD CONSTRAINT eg_userdetails_id_user_key UNIQUE (id_user);

ALTER TABLE ONLY eg_userdetails
    ADD CONSTRAINT eg_userdetails_pkey PRIMARY KEY (id_userdet);

ALTER TABLE ONLY eg_userrole
    ADD CONSTRAINT eg_userrole_pkey PRIMARY KEY (id);

CREATE INDEX indx_eucm_counterid ON eg_usercounter_map USING btree (counterid);

CREATE INDEX indx_eucm_userid ON eg_usercounter_map USING btree (userid);

CREATE INDEX indx_eujl_bndrytypeid ON eg_user_jurlevel USING btree (id_bndry_type);

CREATE INDEX indx_eujl_userid ON eg_user_jurlevel USING btree (id_user);

CREATE INDEX indx_eujv_bndryid ON eg_user_jurvalues USING btree (id_bndry);

CREATE INDEX indx_eujv_jurlevelid ON eg_user_jurvalues USING btree (id_user_jurlevel);

ALTER TABLE ONLY eg_user_jurlevel
    ADD CONSTRAINT fk_user_userjurlevel FOREIGN KEY (id_user) REFERENCES eg_user(id_user);


ALTER TABLE ONLY eg_usercounter_map
    ADD CONSTRAINT fk_mapcounterid FOREIGN KEY (counterid) REFERENCES eg_location(id);

ALTER TABLE ONLY eg_usercounter_map
    ADD CONSTRAINT fk_mapuserid FOREIGN KEY (userid) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_userdetails
    ADD CONSTRAINT fk_user_userdetails FOREIGN KEY (id_user) REFERENCES eg_user(id_user);

 
ALTER TABLE ONLY eg_userrole
    ADD CONSTRAINT fk_role_userrole FOREIGN KEY (id_role) REFERENCES eg_roles(id_role);

ALTER TABLE ONLY eg_userrole
    ADD CONSTRAINT fk_user_userrole FOREIGN KEY (id_user) REFERENCES eg_user(id_user);

ALTER TABLE ONLY eg_wf_actions
    ADD CONSTRAINT eg_wf_actions_name_type_key UNIQUE (name, type);

ALTER TABLE ONLY eg_wf_actions
    ADD CONSTRAINT eg_wf_actions_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_wf_amountrule
    ADD CONSTRAINT eg_wf_amountrule_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_wf_matrix
    ADD CONSTRAINT eg_wf_matrix_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_wf_states
    ADD CONSTRAINT eg_wf_states_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_wf_types
    ADD CONSTRAINT eg_wf_types_pkey PRIMARY KEY (id_type);

ALTER TABLE ONLY eg_wf_states
    ADD CONSTRAINT sys_c0010317 FOREIGN KEY (previous) REFERENCES eg_wf_states(id);

ALTER TABLE ONLY eg_wf_states
    ADD CONSTRAINT sys_c0010318 FOREIGN KEY (next) REFERENCES eg_wf_states(id);

ALTER TABLE ONLY eg_wf_types
    ADD CONSTRAINT eg_wf_types_wf_type_key UNIQUE (wf_type);

ALTER TABLE ONLY eg_wf_types
    ADD CONSTRAINT sys_c0010396 FOREIGN KEY (module_id) REFERENCES eg_module(id_module);


