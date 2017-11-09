--------------------egswtax_fees_master----------------------

CREATE TABLE egswtax_fees_master (
    id bigint NOT NULL,
    description character varying(64) NOT NULL,
    code character varying(12) NOT NULL,
    applicationtype bigint NOT NULL,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint,
    version bigint
);
ALTER TABLE ONLY egswtax_fees_master
    ADD CONSTRAINT pk_egswtax_fees_master PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_fees_master
    ADD CONSTRAINT unq_egswtax_fees_mstr_desc UNIQUE (description);
ALTER TABLE ONLY egswtax_fees_master
    ADD CONSTRAINT unq_egswtax_fees_mstr_code UNIQUE (code);

ALTER TABLE ONLY egswtax_fees_master
    ADD CONSTRAINT fk_fees_mstr_appType FOREIGN KEY (applicationtype) REFERENCES egswtax_application_type(id);


CREATE SEQUENCE seq_egswtax_fees_master;

------------------------egswtax_feesdetail_master----------------------------------

CREATE TABLE egswtax_feesdetail_master (
    id bigint NOT NULL,
    description character varying(64) NOT NULL,
    code character varying(12) NOT NULL,
    fees bigint NOT NULL,
    ismandatory boolean default false,
    isactive boolean default true,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint,
    version bigint
);

ALTER TABLE ONLY egswtax_feesdetail_master
    ADD CONSTRAINT pk_egswtax_feesdetail_master PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_feesdetail_master
    ADD CONSTRAINT fk_egswtax_feesDtl_mstr_fees FOREIGN KEY (fees) REFERENCES egswtax_fees_master(id);
ALTER TABLE ONLY egswtax_feesdetail_master
    ADD CONSTRAINT unq_egswtax_feesdetail_master_desc UNIQUE (description);
ALTER TABLE ONLY egswtax_feesdetail_master
    ADD CONSTRAINT unq_egswtax_feesdetail_master_code UNIQUE (code);

CREATE SEQUENCE seq_egswtax_feesdetail_master;

---------------------------egswtax_connectionfee--------------------------------------

CREATE TABLE egswtax_connectionfee (
    id bigint NOT NULL,
    applicationdetail bigint not null,
    feesdetail bigint NOT NULL,
    amount double precision default 0,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint,
    version bigint
);
ALTER TABLE ONLY egswtax_connectionfee
    ADD CONSTRAINT pk_egswtax_connectionfee PRIMARY KEY (id);

ALTER TABLE ONLY egswtax_connectionfee
    ADD CONSTRAINT fk_egswtax_connectionfee_appDtl FOREIGN KEY (applicationdetail) REFERENCES egswtax_applicationdetails(id);

ALTER TABLE ONLY egswtax_connectionfee
    ADD CONSTRAINT fk_egswtax_connectionfee_feesDtl FOREIGN KEY (feesdetail) REFERENCES egswtax_feesdetail_master(id);

CREATE SEQUENCE seq_egswtax_connectionfee; 


-----------------------------egswtax_document_type_master ----------------------------------------

CREATE TABLE egswtax_document_type_master (
    id bigint NOT NULL,
    description character varying(50) NOT NULL,
    isactive boolean default true,
    applicationtype bigint NOT NULL, 
    ismandatory boolean default false,
    version bigint
);

ALTER TABLE ONLY egswtax_document_type_master
    ADD CONSTRAINT pk_egswtax_document_type_master PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_document_type_master
    ADD CONSTRAINT unq_egswtax_document_name UNIQUE (description);
ALTER TABLE ONLY egswtax_document_type_master
     ADD CONSTRAINT fk_documentType_mstr_appType FOREIGN KEY (applicationtype) REFERENCES egswtax_application_type(id);

CREATE SEQUENCE seq_egswtax_document_type_master;  

---------------------------egswtax_donation_master--------------------------------------

ALTER TABLE egswtax_donation_master DROP COLUMN noofclosets;
ALTER TABLE egswtax_donation_master DROP COLUMN amount;

------------------------egswtax_donationdetail_master----------------------------------

CREATE TABLE egswtax_donationdetail_master (
    id bigint NOT NULL,
    donation bigint not null,
    noofclosets bigint NOT NULL,
    amount double precision NOT NULL,
    version bigint
);

ALTER TABLE ONLY egswtax_donationdetail_master
    ADD CONSTRAINT pk_egswtax_donationdetail_master PRIMARY KEY (id);
ALTER TABLE ONLY egswtax_donationdetail_master
    ADD CONSTRAINT fk_egswtax_dntndtl_donation FOREIGN KEY (donation) REFERENCES egswtax_donation_master(id);

CREATE SEQUENCE seq_egswtax_donationdetail_master; 

