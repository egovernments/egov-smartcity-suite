ALTER TABLE ONLY eg_servicecategory
    ADD CONSTRAINT eg_servicecategory_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_servicedetails
    ADD CONSTRAINT eg_servicedetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_service_subledgerinfo
    ADD CONSTRAINT eg_service_subledgerinfo_pkey PRIMARY KEY (id);


ALTER TABLE ONLY eg_service_accountdetails
    ADD CONSTRAINT eg_service_accountdetails_pkey PRIMARY KEY (id);

ALTER TABLE ONLY eg_service_accountdetails
    ADD CONSTRAINT eg_srvcacc_srvdtils_fk FOREIGN KEY (id_servicedetails) REFERENCES eg_servicedetails(id);

ALTER TABLE ONLY eg_service_accountdetails
    ADD CONSTRAINT eg_srvcaccdtls_coa_fk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);

ALTER TABLE ONLY eg_service_accountdetails
    ADD CONSTRAINT serviceaccdtls_function_fk FOREIGN KEY (functionid) REFERENCES function(id);

ALTER TABLE ONLY eg_service_dept_mapping
    ADD CONSTRAINT eg_ser_dept_map_srvcdtls_fk FOREIGN KEY (id_servicedetails) REFERENCES eg_servicedetails(id);

ALTER TABLE ONLY eg_service_dept_mapping
    ADD CONSTRAINT eg_service_deptmapping_dept_fk FOREIGN KEY (id_department) REFERENCES eg_department(id_dept);


ALTER TABLE ONLY eg_service_subledgerinfo
    ADD CONSTRAINT eg_subdtls_accdtltyp_fk FOREIGN KEY (id_accountdetailtype) REFERENCES accountdetailtype(id);

ALTER TABLE ONLY eg_service_subledgerinfo
    ADD CONSTRAINT eg_subledgerdetails_srvcacc_fk FOREIGN KEY (id_serviceaccountdetail) REFERENCES eg_service_accountdetails(id);


ALTER TABLE ONLY eg_servicedetails
    ADD CONSTRAINT servicedtls_scheme_fk FOREIGN KEY (schemeid) REFERENCES scheme(id);

ALTER TABLE ONLY eg_servicedetails
    ADD CONSTRAINT servicedtls_servicecat_fk FOREIGN KEY (id_service_category) REFERENCES eg_servicecategory(id);

ALTER TABLE ONLY eg_servicedetails
    ADD CONSTRAINT servicedtls_subscheme_fk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);

