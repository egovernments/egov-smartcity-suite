create sequence seq_egtl_validity;
Create table egtl_validity( 
id bigint,
	natureOfBusiness bigint NOT NULL,
	licenseCategory bigint NOT NULL,
	day smallint,
	week smallint,
	month smallint,
	year smallint,
	 createddate timestamp without time zone,
	 createdby bigint,
	 lastmodifieddate timestamp without time zone,
	 lastmodifiedby bigint,
	 version bigint
);

ALTER TABLE ONLY egtl_validity
    ADD CONSTRAINT pk_egtl_validity PRIMARY KEY (id);
ALTER TABLE ONLY egtl_validity
         ADD CONSTRAINT fk_egtl_va_lc FOREIGN KEY (licensecategory) REFERENCES egtl_mstr_category(id);
ALTER TABLE ONLY egtl_validity
    ADD CONSTRAINT fk_egtl_va_nb FOREIGN KEY (natureofbusiness) REFERENCES egtl_mstr_business_nature(id);

