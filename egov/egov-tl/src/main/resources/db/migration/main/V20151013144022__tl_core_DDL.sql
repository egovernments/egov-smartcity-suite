------------------START------------------
CREATE TABLE egtl_mstr_app_type (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    version bigint
);

ALTER TABLE ONLY egtl_mstr_app_type
    ADD CONSTRAINT pk_egtl_mstr_app_type PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_egtl_mstr_app_type ON egtl_mstr_app_type USING btree (id);

COMMENT ON TABLE egtl_mstr_app_type IS 'Master Table for Application Type';
COMMENT ON COLUMN egtl_mstr_app_type.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_app_type.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_app_type.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_app_type.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_mstr_app_type.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_app_type.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_department (
    id bigint NOT NULL,
    name character varying(1024) NOT NULL
);

ALTER TABLE ONLY egtl_mstr_department
    ADD CONSTRAINT pk_egtl_mstr_department PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_egtl_mstr_department ON egtl_mstr_department USING btree (id);

COMMENT ON TABLE egtl_mstr_department IS 'Master Table for Department';
COMMENT ON COLUMN egtl_mstr_department.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_department.name IS 'Name';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_fee_type (
    id bigint NOT NULL,
    name character varying(64) NOT NULL,
    code character varying(12),
    feeprocesstype bigint,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint,
    version bigint
);

ALTER TABLE ONLY egtl_mstr_fee_type
    ADD CONSTRAINT pk_egtl_mstr_fee_type PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_egtl_mstr_fee_type ON egtl_mstr_fee_type USING btree (id);

COMMENT ON TABLE egtl_mstr_fee_type IS 'Master Table For Fee Type';
COMMENT ON COLUMN egtl_mstr_fee_type.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_fee_type.name IS 'Name';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_license_type (
    id bigint NOT NULL,
    name character varying(64) NOT NULL,
    id_module bigint NOT NULL
);

ALTER TABLE ONLY egtl_mstr_license_type
    ADD CONSTRAINT pk_egtl_mstr_license_type PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_license_type
    ADD CONSTRAINT fk_license_type_id_module FOREIGN KEY (id_module) REFERENCES eg_module(id);
CREATE UNIQUE INDEX idx_egtl_mstr_license_type ON egtl_mstr_license_type USING btree (id);

COMMENT ON TABLE egtl_mstr_license_type IS 'Master Table for License Type';
COMMENT ON COLUMN egtl_mstr_license_type.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_license_type.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_license_type.id_module IS 'Id Module foreign key to table EG_MODULE';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_license_sub_type (
    id bigint NOT NULL,
    name character varying(64),
    code character varying(32),
    id_license_type bigint
);

ALTER TABLE ONLY egtl_mstr_license_sub_type
    ADD CONSTRAINT pk_egtl_mstr_license_sub_type PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_license_sub_type
    ADD CONSTRAINT fk_subtype_licensetype FOREIGN KEY (id_license_type) REFERENCES egtl_mstr_license_type(id);
CREATE UNIQUE INDEX idx_egtl_mstr_license_sub_type ON egtl_mstr_license_sub_type USING btree (id);

COMMENT ON TABLE egtl_mstr_license_sub_type IS 'Master Table for License Sub Type';
COMMENT ON COLUMN egtl_mstr_license_sub_type.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_license_sub_type.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_license_sub_type.code IS 'Code';
COMMENT ON COLUMN egtl_mstr_license_sub_type.id_license_type IS 'Id License Type foreign key to table EGTL_MSTR_LICENSE_TYPE';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_motor_fee_range (
    id bigint NOT NULL,
    motor_hp_from bigint NOT NULL,
    motor_hp_to bigint NOT NULL,
    using_fee bigint NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    effective_to timestamp without time zone,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

ALTER TABLE ONLY egtl_mstr_motor_fee_range
    ADD CONSTRAINT pk_egtl_mstr_motor_fee_range PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_egtl_mstr_motor_fee_range ON egtl_mstr_motor_fee_range USING btree (id);

COMMENT ON TABLE egtl_mstr_motor_fee_range IS 'Master Table for Motor Fee Type Range';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.motor_hp_from IS 'Motor Horse Power From';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.motor_hp_to IS 'Motor Horse Power To';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.using_fee IS 'Using Fee ';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.effective_from IS 'Effective from Date';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.effective_to IS 'Effective to Date';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_motor_fee_range.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_schedule (
    id bigint NOT NULL,
    schedule_code character varying(16) NOT NULL,
    schedule_name character varying(256),
    order_date timestamp without time zone,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

ALTER TABLE ONLY egtl_mstr_schedule
    ADD CONSTRAINT pk_egtl_mstr_schedule PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_egtl_mstr_schedule ON egtl_mstr_schedule USING btree (id);

COMMENT ON TABLE egtl_mstr_schedule IS 'Master Table for Schedule';
COMMENT ON COLUMN egtl_mstr_schedule.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_schedule.schedule_code IS 'Schedule Code';
COMMENT ON COLUMN egtl_mstr_schedule.schedule_name IS 'Schedule Name';
COMMENT ON COLUMN egtl_mstr_schedule.order_date IS 'Order Date';
COMMENT ON COLUMN egtl_mstr_schedule.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_schedule.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_mstr_schedule.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_schedule.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_business_nature (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

ALTER TABLE ONLY egtl_mstr_business_nature
    ADD CONSTRAINT pk_egtl_mstr_business_nature PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_egtl_mstr_business_nature ON egtl_mstr_business_nature USING btree (id);

COMMENT ON TABLE egtl_mstr_business_nature IS 'Master Table for Business Nature';
COMMENT ON COLUMN egtl_mstr_business_nature.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_business_nature.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_business_nature.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_business_nature.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_mstr_business_nature.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_business_nature.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_category (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    code character varying(256) NOT NULL,
    version bigint DEFAULT 0
);

ALTER TABLE ONLY egtl_mstr_category
    ADD CONSTRAINT pk_egtl_mstr_category PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_category
    ADD CONSTRAINT unq_tlcategory_code UNIQUE (code);
ALTER TABLE ONLY egtl_mstr_category
    ADD CONSTRAINT unq_tlcategory_name UNIQUE (name);
CREATE UNIQUE INDEX idx_egtl_mstr_category ON egtl_mstr_category USING btree (id);

COMMENT ON TABLE egtl_mstr_category IS 'Master Table for Category';
COMMENT ON COLUMN egtl_mstr_category.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_category.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_category.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_category.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_mstr_category.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_category.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_sub_category (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    code character varying(32),
    id_license_type bigint,
    id_nature bigint,
    id_category bigint,
    id_tl_dept bigint,
    id_schedule bigint,
    section_applicable character varying(256),
    pfa_applicable boolean DEFAULT false,
    fee_based_on character varying(40),
    approval_required boolean DEFAULT false,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    id_license_sub_type bigint,
    noc_applicable boolean DEFAULT false,
    version bigint DEFAULT 0
);

ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT pk_egtl_mstr_sub_category PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT unq_egtl_mstr_sub_categoryunique UNIQUE (code);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT unq_tlsubcategory_code UNIQUE (code);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT unq_tlsubcategory_name UNIQUE (name);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT fk_egtl_sub_category_category FOREIGN KEY (id_category) REFERENCES egtl_mstr_category(id);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT fk_egtl_sub_category_nature FOREIGN KEY (id_nature) REFERENCES egtl_mstr_business_nature(id);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT fk_egtl_sub_category_sub_type FOREIGN KEY (id_license_sub_type) REFERENCES egtl_mstr_license_sub_type(id);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT fk_egtl_sub_category_tldept FOREIGN KEY (id_tl_dept) REFERENCES egtl_mstr_department(id);
ALTER TABLE ONLY egtl_mstr_sub_category
    ADD CONSTRAINT fk_egtl_sub_category_type FOREIGN KEY (id_license_type) REFERENCES egtl_mstr_license_type(id);
CREATE UNIQUE INDEX idx_code_egtl_mstr_sub_category ON egtl_mstr_sub_category USING btree (code);
CREATE UNIQUE INDEX idx_id_egtl_mstr_sub_category ON egtl_mstr_sub_category USING btree (id);

COMMENT ON TABLE egtl_mstr_sub_category IS 'Master Table for Sub Category';
COMMENT ON COLUMN egtl_mstr_sub_category.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_sub_category.name IS 'Name';
COMMENT ON COLUMN egtl_mstr_sub_category.code IS 'Code';
COMMENT ON COLUMN egtl_mstr_sub_category.id_license_type IS 'Id License Type foreign key to table EGTL_MSTR_LICENSE_TYPE';
COMMENT ON COLUMN egtl_mstr_sub_category.id_nature IS 'Id Nature Foreign key to table EGTL_MSTR_NATURE';
COMMENT ON COLUMN egtl_mstr_sub_category.id_category IS 'Id Category foreign key to  table EGTL_MSTR_CATEGORY';
COMMENT ON COLUMN egtl_mstr_sub_category.id_tl_dept IS 'Id TL Department foreign key to table EGTL_MSTR_DEPARTMENT';
COMMENT ON COLUMN egtl_mstr_sub_category.id_schedule IS 'Id Schedule foreign key to table EGTL_MSTR_SCHEDULE';
COMMENT ON COLUMN egtl_mstr_sub_category.section_applicable IS 'Section Applicable';
COMMENT ON COLUMN egtl_mstr_sub_category.pfa_applicable IS 'PFA Applicable';
COMMENT ON COLUMN egtl_mstr_sub_category.fee_based_on IS 'Fee Based on';
COMMENT ON COLUMN egtl_mstr_sub_category.approval_required IS 'Approval Required';
COMMENT ON COLUMN egtl_mstr_sub_category.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_mstr_sub_category.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_mstr_sub_category.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_sub_category.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
COMMENT ON COLUMN egtl_mstr_sub_category.id_license_sub_type IS 'Id license Sub Type foreign key to table EGTL_MSTR_LICENSE_SUB_TYPE';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_unitofmeasure (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    version bigint
);

ALTER TABLE ONLY egtl_mstr_unitofmeasure
    ADD CONSTRAINT pk_egtl_mstr_unitofmeasure PRIMARY KEY (id);
ALTER TABLE ONLY egtl_mstr_unitofmeasure
    ADD CONSTRAINT unq_tluom_code UNIQUE (code);
ALTER TABLE ONLY egtl_mstr_unitofmeasure
    ADD CONSTRAINT unq_tluom_name UNIQUE (name);
ALTER TABLE ONLY egtl_mstr_unitofmeasure
    ADD CONSTRAINT fk_egtl_mstr_unitofmeasure_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);
ALTER TABLE ONLY egtl_mstr_unitofmeasure
    ADD CONSTRAINT fk_egtl_mstr_unitofmeasure_modifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_document_type (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    applicationtype character varying(50) NOT NULL,
    mandatory boolean,
    version bigint
);

ALTER TABLE ONLY egtl_document_type
    ADD CONSTRAINT pk_egtl_document_type PRIMARY KEY (id);
ALTER TABLE ONLY egtl_document_type
    ADD CONSTRAINT unq_egtl_document_name UNIQUE (name);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_document (
    id bigint NOT NULL,
    type bigint NOT NULL,
    description character varying(50),
    docdate timestamp without time zone,
    enclosed boolean,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint,
    version bigint
);

ALTER TABLE ONLY egtl_document
    ADD CONSTRAINT pk_egtl_document PRIMARY KEY (id);
ALTER TABLE ONLY egtl_document
    ADD CONSTRAINT fk_document_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);
ALTER TABLE ONLY egtl_document
    ADD CONSTRAINT fk_document_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);
ALTER TABLE ONLY egtl_document
    ADD CONSTRAINT fk_egtl_document_type FOREIGN KEY (type) REFERENCES egtl_document_type(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_document_files (
    document bigint,
    filestore bigint
);

ALTER TABLE ONLY egtl_document_files
    ADD CONSTRAINT fk_doc_files_document FOREIGN KEY (document) REFERENCES egtl_document(id);
ALTER TABLE ONLY egtl_document_files
    ADD CONSTRAINT fk_doc_files_filestore FOREIGN KEY (filestore) REFERENCES eg_filestoremap(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_feematrix (
    id bigint NOT NULL,
    natureofbusiness bigint,
    licenseapptype bigint,
    licensecategory bigint,
    subcategory bigint,
    feetype bigint,
    unitofmeasurement bigint,
    uniqueno character varying(32),
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint,
    version bigint
);

ALTER TABLE ONLY egtl_feematrix
    ADD CONSTRAINT pk_egtl_feematrix PRIMARY KEY (id);
ALTER TABLE ONLY egtl_feematrix
    ADD CONSTRAINT fk_egtl_fm_ft FOREIGN KEY (feetype) REFERENCES egtl_mstr_fee_type(id);
ALTER TABLE ONLY egtl_feematrix
    ADD CONSTRAINT fk_egtl_fm_lat FOREIGN KEY (licenseapptype) REFERENCES egtl_mstr_app_type(id);
ALTER TABLE ONLY egtl_feematrix
    ADD CONSTRAINT fk_egtl_fm_lc FOREIGN KEY (licensecategory) REFERENCES egtl_mstr_category(id);
ALTER TABLE ONLY egtl_feematrix
    ADD CONSTRAINT fk_egtl_fm_lsc FOREIGN KEY (subcategory) REFERENCES egtl_mstr_sub_category(id);
ALTER TABLE ONLY egtl_feematrix
    ADD CONSTRAINT fk_egtl_fm_nb FOREIGN KEY (natureofbusiness) REFERENCES egtl_mstr_business_nature(id);
ALTER TABLE ONLY egtl_feematrix
    ADD CONSTRAINT fk_egtl_fm_uom FOREIGN KEY (unitofmeasurement) REFERENCES egtl_mstr_unitofmeasure(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_feematrix_detail (
    id bigint,
    feematrix bigint,
    uomfrom bigint,
    uomto bigint,
    percentage bigint,
    fromdate date,
    todate date,
    amount double precision,
    version bigint
);

ALTER TABLE ONLY egtl_feematrix_detail
    ADD CONSTRAINT fk_egtl_fmd_fm FOREIGN KEY (feematrix) REFERENCES egtl_feematrix(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_mstr_status (
    id bigint NOT NULL,
    status_name character varying(256) NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    is_active boolean,
    code character varying(32),
    order_id bigint
);

ALTER TABLE ONLY egtl_mstr_status
    ADD CONSTRAINT pk_egtl_mstr_status PRIMARY KEY (id);
CREATE UNIQUE INDEX idx_egtl_mstr_status ON egtl_mstr_status USING btree (id);

COMMENT ON TABLE egtl_mstr_status IS 'Master table for Status';
COMMENT ON COLUMN egtl_mstr_status.id IS 'Primary Key';
COMMENT ON COLUMN egtl_mstr_status.status_name IS 'Status Name';
COMMENT ON COLUMN egtl_mstr_status.lastupdatedtimestamp IS 'last updated Time stamp';
COMMENT ON COLUMN egtl_mstr_status.is_active IS 'Is Active';
COMMENT ON COLUMN egtl_mstr_status.code IS 'Code';
COMMENT ON COLUMN egtl_mstr_status.order_id IS 'Order Id';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_license (
    id bigint NOT NULL,
    appl_num character varying(128) NOT NULL,
    appl_date timestamp without time zone NOT NULL,
    license_number character varying(50),
    temp_license_number character varying(100),
    dateofcreation timestamp without time zone,
    id_status bigint,
    is_active boolean,
    id_sub_category bigint,
    license_type character varying(32),
    name_of_estab character varying(256),
    rent_paid bigint,
    id_adm_bndry bigint NOT NULL,
    remarks character varying(512),
    rentalagreement boolean,
    tin_num character varying(40),
    power_of_attorney character varying(256),
    company_detail character varying(256),
    service_tax_num character varying(56),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    old_license_number character varying(50),
    phonenumber character varying(16),
    noofrooms bigint,
    id_objection bigint,
    dateofexpiry timestamp without time zone,
    feetypetext character varying(24),
    id_licensetransfer bigint,
    checklist character varying(4000),
    doc_image_number character varying(50),
    company_pan_number character varying(16),
    vat_number character varying(16),
    bank_ifsc_code character varying(12),
    min_solvency bigint,
    avg_annual_turnover bigint,
    fee_exemption character varying(5),
    office_emailid character varying(64),
    buildingtype bigint,
    property_no character varying(64),
    address character varying(250) NOT NULL,
    ownership_type character varying(120) NOT NULL,
    id_category bigint NOT NULL,
    workers_capacity bigint NOT NULL,
    trade_area_weight bigint NOT NULL,
    id_uom bigint NOT NULL
);

ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT pk_egtl_license_details PRIMARY KEY (id);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT unq_old_license_number UNIQUE (old_license_number);
CREATE UNIQUE INDEX idx_old_license_number ON egtl_license USING btree (old_license_number);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT egtl_license_buildingtype FOREIGN KEY (buildingtype) REFERENCES egtl_mstr_business_nature(id);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT egtl_license_category FOREIGN KEY (id_category) REFERENCES egtl_mstr_category(id);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT egtl_license_uom FOREIGN KEY (id_uom) REFERENCES egtl_mstr_unitofmeasure(id);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_eglmstr_id_status FOREIGN KEY (id_status) REFERENCES egtl_mstr_status(id);
ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_egtl_license_bndry FOREIGN KEY (id_adm_bndry) REFERENCES eg_boundary(id);
CREATE UNIQUE INDEX idx_egtl_license_details ON egtl_license USING btree (id);

COMMENT ON TABLE egtl_license IS 'Main table for License, which stores all specific information which is shared by other license sub modules';
COMMENT ON COLUMN egtl_license.id IS 'Primary Key Id of the Table';
COMMENT ON COLUMN egtl_license.appl_num IS 'Application Number for License';
COMMENT ON COLUMN egtl_license.appl_date IS 'Application TIMESTAMP without time zone for License';
COMMENT ON COLUMN egtl_license.license_number IS 'License Number';
COMMENT ON COLUMN egtl_license.temp_license_number IS 'Temporary License Number';
COMMENT ON COLUMN egtl_license.dateofcreation IS 'Date of Creation';
COMMENT ON COLUMN egtl_license.id_status IS 'Id Status of the License from EGTL_MSTR_STATUS table like New, Underwork Flow, Active etc.,';
COMMENT ON COLUMN egtl_license.is_active IS 'Tells whether the license is active 1 and inactive 0';
COMMENT ON COLUMN egtl_license.id_sub_category IS 'Id Sub Category from table EGTL_MSTR_SUB_CATEGORY that tells the class of the license';
COMMENT ON COLUMN egtl_license.license_type IS 'Type of License like trade, hawker, pwd etc.,';
COMMENT ON COLUMN egtl_license.name_of_estab IS 'Name of Establisment';
COMMENT ON COLUMN egtl_license.rent_paid IS 'Tells if the Rent is Paid or not';
COMMENT ON COLUMN egtl_license.id_adm_bndry IS 'Id of the Administrative Boundary foreign key to table EG_BOUNDARY';
COMMENT ON COLUMN egtl_license.remarks IS 'Remarks';
COMMENT ON COLUMN egtl_license.rentalagreement IS 'Rental Agreement';
COMMENT ON COLUMN egtl_license.tin_num IS 'TIN Number';
COMMENT ON COLUMN egtl_license.power_of_attorney IS 'Power of Attorney';
COMMENT ON COLUMN egtl_license.company_detail IS 'Company Detail';
COMMENT ON COLUMN egtl_license.service_tax_num IS 'Service Tax Number';
COMMENT ON COLUMN egtl_license.createddate IS 'License Created Date';
COMMENT ON COLUMN egtl_license.lastmodifieddate IS 'License last Modified Date';
COMMENT ON COLUMN egtl_license.createdby IS 'License Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_license.lastmodifiedby IS 'License last Modified by UserId foreign key to EG_USER';
COMMENT ON COLUMN egtl_license.old_license_number IS 'Old License Number entered when legacy data is captured';
COMMENT ON COLUMN egtl_license.phonenumber IS 'Phone Number';
COMMENT ON COLUMN egtl_license.noofrooms IS 'Number of Rooms in the Building';
COMMENT ON COLUMN egtl_license.id_objection IS 'Id License Objection foreign key to table EGTL_OBJECTION';
COMMENT ON COLUMN egtl_license.dateofexpiry IS 'Date of Expiry';
COMMENT ON COLUMN egtl_license.feetypetext IS 'Fee Type Text CNC/PFA';
COMMENT ON COLUMN egtl_license.id_licensetransfer IS 'Id License Transfer foreign key to table EGTL_LICENSETRANSFER';
COMMENT ON COLUMN egtl_license.checklist IS 'Stores information about Check List';
COMMENT ON COLUMN egtl_license.doc_image_number IS 'In Case of WaterWorks License and Veterinary License Image is uploaded, this stores that Image Number';
COMMENT ON COLUMN egtl_license.company_pan_number IS 'Companys PAN Number';
COMMENT ON COLUMN egtl_license.vat_number IS 'VAT Number';
COMMENT ON COLUMN egtl_license.bank_ifsc_code IS 'Bank IFSC Code';
COMMENT ON COLUMN egtl_license.min_solvency IS 'Minimum Solvency';
COMMENT ON COLUMN egtl_license.avg_annual_turnover IS 'Average Annual Turn Over';
COMMENT ON COLUMN egtl_license.fee_exemption IS 'Fee Exemtion';
COMMENT ON COLUMN egtl_license.office_emailid IS 'Office Email Id';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_objection (
    id bigint NOT NULL,
    id_license bigint,
    reason bigint,
    objectionnumber character varying(32),
    name character varying(32),
    address character varying(64),
    objectiondate timestamp without time zone,
    details character varying(512),
    docnumber character varying(32),
    state_id bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

ALTER TABLE ONLY egtl_objection
    ADD CONSTRAINT pk_egtl_objection PRIMARY KEY (id);
ALTER TABLE ONLY egtl_objection
    ADD CONSTRAINT fk_egtl_objection_license FOREIGN KEY (id_license) REFERENCES egtl_license(id);
ALTER TABLE ONLY egtl_objection
    ADD CONSTRAINT fk_egtl_objection_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
CREATE UNIQUE INDEX idx_egtl_objection ON egtl_objection USING btree (id);

COMMENT ON TABLE egtl_objection IS 'ObjectionTable used for maintaining the data when the License undergoes Objection Process';
COMMENT ON COLUMN egtl_objection.id IS 'Primary Key';
COMMENT ON COLUMN egtl_objection.id_license IS 'Id License foreign key to table EGTL_LICENSE';
COMMENT ON COLUMN egtl_objection.reason IS 'Objection Reason';
COMMENT ON COLUMN egtl_objection.objectionnumber IS 'Objection Number';
COMMENT ON COLUMN egtl_objection.name IS 'Name';
COMMENT ON COLUMN egtl_objection.address IS 'Address';
COMMENT ON COLUMN egtl_objection.objectiondate IS 'Objection Date';
COMMENT ON COLUMN egtl_objection.details IS 'Details';
COMMENT ON COLUMN egtl_objection.docnumber IS 'Document Number';
COMMENT ON COLUMN egtl_objection.state_id IS 'Foreign Key State Id for the table EG_WF_STATES';
COMMENT ON COLUMN egtl_objection.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_objection.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_objection.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_objection.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_activity (
    id bigint NOT NULL,
    id_objection bigint,
    details character varying(64),
    activitydate timestamp without time zone,
    expecteddateofresponse timestamp without time zone,
    remarks character varying(512),
    docnumber character varying(32),
    type character varying(16),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

ALTER TABLE ONLY egtl_activity
    ADD CONSTRAINT pk_egtl_activity PRIMARY KEY (id);
ALTER TABLE ONLY egtl_activity
    ADD CONSTRAINT fk_egtl_activity_objection FOREIGN KEY (id_objection) REFERENCES egtl_objection(id);
CREATE UNIQUE INDEX idx_egtl_activity ON egtl_activity USING btree (id);

COMMENT ON TABLE egtl_activity IS 'Activity Table used for maintaining the data when the License undergoes Objection Process';
COMMENT ON COLUMN egtl_activity.id IS 'Primary Key Id of the Table';
COMMENT ON COLUMN egtl_activity.id_objection IS 'Objection Id foreign key to table EG_OBJECTION';
COMMENT ON COLUMN egtl_activity.details IS 'Details entered while objecting license is stored';
COMMENT ON COLUMN egtl_activity.activitydate IS 'Date on which Objection Activity was started';
COMMENT ON COLUMN egtl_activity.expecteddateofresponse IS 'Expected TIMESTAMP without time zone of Objection Response';
COMMENT ON COLUMN egtl_activity.remarks IS 'Remarks';
COMMENT ON COLUMN egtl_activity.docnumber IS 'If any Document is uploaded during Objection, the document Number is stored';
COMMENT ON COLUMN egtl_activity.type IS 'Objection Type like Inspection, Response, PreNotice etc';
COMMENT ON COLUMN egtl_activity.createddate IS 'Objection Created Date';
COMMENT ON COLUMN egtl_activity.lastmodifieddate IS 'Objection last Modified Date';
COMMENT ON COLUMN egtl_activity.createdby IS 'Objection Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_activity.lastmodifiedby IS 'Objection last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_licensetransfer (
    id bigint NOT NULL,
    id_license bigint,
    oldapplicationnumber character varying(32),
    oldapplicantname character varying(32),
    oldapplicationdate timestamp without time zone,
    oldnameofestablishment character varying(256),
    id_address bigint,
    oldphonenumber character varying(16),
    approved boolean,
    licensetype character varying(32),
    state_id bigint,
    id_adm_bndry bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    oldhome_phonenumber character varying(16),
    oldmobile_phonenumber character varying(16),
    olduniqueid character varying(16),
    oldemail_id character varying(64)
);

ALTER TABLE ONLY egtl_licensetransfer
    ADD CONSTRAINT pk_egtl_licensetransfer PRIMARY KEY (id);
ALTER TABLE ONLY egtl_licensetransfer
    ADD CONSTRAINT fk_egtl_licensetrans_address FOREIGN KEY (id_address) REFERENCES eg_address(id);
ALTER TABLE ONLY egtl_licensetransfer
    ADD CONSTRAINT fk_egtl_licensetrans_license FOREIGN KEY (id_license) REFERENCES egtl_license(id);
ALTER TABLE ONLY egtl_licensetransfer
    ADD CONSTRAINT fk_licensetrans_boundary FOREIGN KEY (id_adm_bndry) REFERENCES eg_boundary(id);
ALTER TABLE ONLY egtl_licensetransfer
    ADD CONSTRAINT fk_licensetrans_eg_wf_states FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
CREATE UNIQUE INDEX idx_egtl_licensetransfer ON egtl_licensetransfer USING btree (id);

COMMENT ON TABLE egtl_licensetransfer IS 'It is used to store old details of the license when the license is transferred';
COMMENT ON COLUMN egtl_licensetransfer.id IS 'Primary Key';
COMMENT ON COLUMN egtl_licensetransfer.id_license IS 'Foriegn Key Id License foreign key to table EGTL_LICENSE';
COMMENT ON COLUMN egtl_licensetransfer.oldapplicationnumber IS 'Old Application Number';
COMMENT ON COLUMN egtl_licensetransfer.oldapplicantname IS 'Old Applicant Name';
COMMENT ON COLUMN egtl_licensetransfer.oldapplicationdate IS 'Old Application Date';
COMMENT ON COLUMN egtl_licensetransfer.oldnameofestablishment IS 'Old Establishment Name';
COMMENT ON COLUMN egtl_licensetransfer.id_address IS 'Id Adress Foreign Key to table EG_ADDRESS';
COMMENT ON COLUMN egtl_licensetransfer.oldphonenumber IS 'Old Phone Number';
COMMENT ON COLUMN egtl_licensetransfer.approved IS 'Approved';
COMMENT ON COLUMN egtl_licensetransfer.licensetype IS 'Licence Type';
COMMENT ON COLUMN egtl_licensetransfer.state_id IS 'State Id foreign key to the table EG_WF_STATES used in the process of workflow';
COMMENT ON COLUMN egtl_licensetransfer.id_adm_bndry IS 'Id of the Administrative Boundary foreign key to table EG_BOUNDARY';
COMMENT ON COLUMN egtl_licensetransfer.createddate IS 'License Transfer Created Date';
COMMENT ON COLUMN egtl_licensetransfer.lastmodifieddate IS 'License Transfer last Modified Date';
COMMENT ON COLUMN egtl_licensetransfer.createdby IS 'License Transfer Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_licensetransfer.lastmodifiedby IS 'License Transfer last Modified by UserId foreign key to EG_USER';
COMMENT ON COLUMN egtl_licensetransfer.oldhome_phonenumber IS 'Old Home Phone Number';
COMMENT ON COLUMN egtl_licensetransfer.oldmobile_phonenumber IS 'Old Mobile Number';
COMMENT ON COLUMN egtl_licensetransfer.olduniqueid IS 'Old Unique Id';
COMMENT ON COLUMN egtl_licensetransfer.oldemail_id IS 'Old Email Id';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_license_demand (
    id_demand bigint,
    id_license bigint,
    id_installment bigint,
    renewal_date timestamp without time zone,
    is_laterenewal character(1)
);
COMMENT ON TABLE egtl_license_demand IS 'This table is used as an association table for EGTL_LICENSE and EG_DEMAND when demand is generated';
COMMENT ON COLUMN egtl_license_demand.id_demand IS 'Id Demand foreign key to table EG_DEMAND';
COMMENT ON COLUMN egtl_license_demand.id_license IS 'Id License foreign key to table EGTL_LICENSE';
COMMENT ON COLUMN egtl_license_demand.id_installment IS 'Demand generated for which Installment Id foreign key to EG_INSTALLMENT';
COMMENT ON COLUMN egtl_license_demand.renewal_date IS 'If the License is Renewed, then the Renewal TIMESTAMP without time zone of the same is stored here';
COMMENT ON COLUMN egtl_license_demand.is_laterenewal IS 'If the license is Renewed late, then this data is updated';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_licensee (
    id bigint NOT NULL,
    applicant_name character varying(256) NOT NULL,
    father_spouse_name character varying(256),
    age bigint,
    gender character varying(10),
    qualification character varying(64),
    pan_num character varying(10),
    nationality character varying(64),
    id_license bigint NOT NULL,
    id_adm_bndry bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    phonenumber character varying(16),
    mobile_phonenumber character varying(16),
    uniqueid character varying(16),
    email_id character varying(64),
    address character varying(250) NOT NULL
);

ALTER TABLE ONLY egtl_licensee
    ADD CONSTRAINT pk_egtl_licensee PRIMARY KEY (id);
ALTER TABLE ONLY egtl_licensee
    ADD CONSTRAINT fk_egtl_licensee_bndry FOREIGN KEY (id_adm_bndry) REFERENCES eg_boundary(id);
ALTER TABLE ONLY egtl_licensee
    ADD CONSTRAINT fk_egtl_licensee_license FOREIGN KEY (id_license) REFERENCES egtl_license(id);
CREATE UNIQUE INDEX idx_egtl_licensee ON egtl_licensee USING btree (id);

COMMENT ON TABLE egtl_licensee IS 'Table for Licensee which stores information Regarding the personal details given by the applicant like name, fathers name etc.,';
COMMENT ON COLUMN egtl_licensee.id IS 'Primary Key Id of the Table';
COMMENT ON COLUMN egtl_licensee.applicant_name IS 'Applicant Name';
COMMENT ON COLUMN egtl_licensee.father_spouse_name IS 'Father or Spouse Name';
COMMENT ON COLUMN egtl_licensee.age IS 'Age of the Applicant';
COMMENT ON COLUMN egtl_licensee.gender IS 'Gender of the Applicant';
COMMENT ON COLUMN egtl_licensee.qualification IS 'Qualification of the Applicant';
COMMENT ON COLUMN egtl_licensee.pan_num IS 'PAN Number of the Applicant';
COMMENT ON COLUMN egtl_licensee.nationality IS 'Nationality of the Applicant';
COMMENT ON COLUMN egtl_licensee.id_license IS 'Id License foreign key to EGTL_LICENSE';
COMMENT ON COLUMN egtl_licensee.id_adm_bndry IS 'Id of the Administrative Boundary foreign key to table EG_BOUNDARY';
COMMENT ON COLUMN egtl_licensee.createddate IS 'Licensee Created Date';
COMMENT ON COLUMN egtl_licensee.lastmodifieddate IS 'Licensee last Modified Date';
COMMENT ON COLUMN egtl_licensee.createdby IS 'Licensee Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_licensee.lastmodifiedby IS 'Licensee last Modified by UserId foreign key to EG_USER';
COMMENT ON COLUMN egtl_licensee.phonenumber IS 'Applicants Phone Number';
COMMENT ON COLUMN egtl_licensee.mobile_phonenumber IS 'Applicants Mobile Number';
COMMENT ON COLUMN egtl_licensee.uniqueid IS 'Applicants Unique Id';
COMMENT ON COLUMN egtl_licensee.email_id IS 'Applicants Email Id';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_licenseobjection_docs (
    objection bigint,
    document bigint
);

ALTER TABLE ONLY egtl_licenseobjection_docs
    ADD CONSTRAINT fk_objection_docs_document FOREIGN KEY (document) REFERENCES egtl_document(id);
ALTER TABLE ONLY egtl_licenseobjection_docs
    ADD CONSTRAINT fk_objection_docs_objection FOREIGN KEY (objection) REFERENCES egtl_objection(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_licensetransfer_docs (
    transfer bigint,
    document bigint
);

ALTER TABLE ONLY egtl_licensetransfer_docs
    ADD CONSTRAINT fk_transfer_docs_document FOREIGN KEY (document) REFERENCES egtl_document(id);
ALTER TABLE ONLY egtl_licensetransfer_docs
    ADD CONSTRAINT fk_transfer_docs_transfer FOREIGN KEY (transfer) REFERENCES egtl_licensetransfer(id);
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_major_works (
    id bigint NOT NULL,
    id_license bigint,
    fire_fighter_system character varying(256),
    detection character varying(256),
    passive_protection character varying(256)
);

ALTER TABLE ONLY egtl_major_works
    ADD CONSTRAINT pk_egtl_major_works PRIMARY KEY (id);
ALTER TABLE ONLY egtl_major_works
    ADD CONSTRAINT fk_egtl_major_works FOREIGN KEY (id_license) REFERENCES egtl_license(id);
CREATE UNIQUE INDEX idx_egtl_major_works ON egtl_major_works USING btree (id);

COMMENT ON TABLE egtl_major_works IS 'Stores information of Electrical License sub module Fire Contractor major works details';
COMMENT ON COLUMN egtl_major_works.id IS 'Primary Key Id of the Table';
COMMENT ON COLUMN egtl_major_works.id_license IS 'Id License Foreign Key to table EGTL_LICENSE';
COMMENT ON COLUMN egtl_major_works.fire_fighter_system IS 'Fire Fighther System';
COMMENT ON COLUMN egtl_major_works.detection IS 'Detection';
COMMENT ON COLUMN egtl_major_works.passive_protection IS 'Passive Protection';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_motor_details (
    id bigint NOT NULL,
    id_license bigint NOT NULL,
    hp bigint,
    no_of_machines bigint,
    history boolean DEFAULT false
);

ALTER TABLE ONLY egtl_motor_details
    ADD CONSTRAINT pk_egtl_motor_details PRIMARY KEY (id);
ALTER TABLE ONLY egtl_motor_details
    ADD CONSTRAINT fk_motor_license FOREIGN KEY (id_license) REFERENCES egtl_license(id);
CREATE UNIQUE INDEX idx_egtl_motor_details ON egtl_motor_details USING btree (id);

COMMENT ON TABLE egtl_motor_details IS 'stores information of Trade License Motor Details';
COMMENT ON COLUMN egtl_motor_details.id IS 'Primary Key Id of the Table';
COMMENT ON COLUMN egtl_motor_details.id_license IS 'Id License Foreign Key to table EGTL_LICENSE';
COMMENT ON COLUMN egtl_motor_details.hp IS 'Horse Power';
COMMENT ON COLUMN egtl_motor_details.no_of_machines IS 'Number Of Machines';
COMMENT ON COLUMN egtl_motor_details.history IS 'History';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_notice (
    id bigint NOT NULL,
    id_objection bigint,
    noticenumber character varying(64),
    noticetype character varying(64),
    modulename character varying(64),
    noticedate timestamp without time zone,
    docnumber character varying(32),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);

ALTER TABLE ONLY egtl_notice
    ADD CONSTRAINT pk_egtl_notice PRIMARY KEY (id);
ALTER TABLE ONLY egtl_notice
    ADD CONSTRAINT fk_egtl_notice_objection FOREIGN KEY (id_objection) REFERENCES egtl_objection(id);
CREATE UNIQUE INDEX idx_egtl_notice ON egtl_notice USING btree (id);

COMMENT ON TABLE egtl_notice IS 'Notive Table used for maintaining the data when the Notice is Generated.';
COMMENT ON COLUMN egtl_notice.id IS 'Primary Key';
COMMENT ON COLUMN egtl_notice.id_objection IS 'Id Objection foreign key to table EGTL_OBJECTION';
COMMENT ON COLUMN egtl_notice.noticenumber IS 'Notice Number';
COMMENT ON COLUMN egtl_notice.noticetype IS 'Notice Type';
COMMENT ON COLUMN egtl_notice.modulename IS 'Module Name';
COMMENT ON COLUMN egtl_notice.noticedate IS 'Notice Date';
COMMENT ON COLUMN egtl_notice.docnumber IS 'Document Date';
COMMENT ON COLUMN egtl_notice.createddate IS 'Created Date';
COMMENT ON COLUMN egtl_notice.lastmodifieddate IS 'last Modified Date';
COMMENT ON COLUMN egtl_notice.createdby IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_notice.lastmodifiedby IS 'last Modified by UserId foreign key to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_status_values (
    id bigint NOT NULL,
    id_status bigint NOT NULL,
    id_license bigint NOT NULL,
    id_reason bigint NOT NULL,
    ref_date timestamp without time zone,
    ref_num character varying(64),
    remarks character varying(1024),
    is_active boolean,
    modified_date timestamp without time zone,
    created_date timestamp without time zone,
    extra_field1 character varying(64),
    extra_field2 character varying(64),
    extra_field3 character varying(64),
    created_by bigint NOT NULL,
    modified_by bigint,
    parent_status_val bigint,
    doc_num character varying(100)
);

ALTER TABLE ONLY egtl_status_values
    ADD CONSTRAINT pk_egtl_status_values PRIMARY KEY (id);
ALTER TABLE ONLY egtl_status_values
    ADD CONSTRAINT fk_id_lic_ststus FOREIGN KEY (id_status) REFERENCES egtl_mstr_status(id);
ALTER TABLE ONLY egtl_status_values
    ADD CONSTRAINT fk_lcense FOREIGN KEY (id_license) REFERENCES egtl_license(id);
CREATE UNIQUE INDEX idx_egtl_status_values ON egtl_status_values USING btree (id);

COMMENT ON TABLE egtl_status_values IS 'Status Values table stores info about application when it is cancelled';
COMMENT ON COLUMN egtl_status_values.id IS 'Primary Key';
COMMENT ON COLUMN egtl_status_values.id_status IS 'Id Status foreign key to EGTL_STATUS';
COMMENT ON COLUMN egtl_status_values.id_license IS 'Id License foreign key to EGTL_LICENSE';
COMMENT ON COLUMN egtl_status_values.id_reason IS 'Id reason';
COMMENT ON COLUMN egtl_status_values.ref_date IS 'Reference Date';
COMMENT ON COLUMN egtl_status_values.ref_num IS 'Reference Number';
COMMENT ON COLUMN egtl_status_values.remarks IS 'Remarks';
COMMENT ON COLUMN egtl_status_values.is_active IS 'Is Active';
COMMENT ON COLUMN egtl_status_values.modified_date IS 'Modified Date';
COMMENT ON COLUMN egtl_status_values.created_date IS 'Created Date';
COMMENT ON COLUMN egtl_status_values.extra_field1 IS 'Extra Field 1';
COMMENT ON COLUMN egtl_status_values.extra_field2 IS 'Extra Field 2';
COMMENT ON COLUMN egtl_status_values.extra_field3 IS 'Extra Field 3';
COMMENT ON COLUMN egtl_status_values.created_by IS 'Created User Id foreign key to EG_USER';
COMMENT ON COLUMN egtl_status_values.modified_by IS 'last Modified by UserId foreign key to EG_USER';
COMMENT ON COLUMN egtl_status_values.parent_status_val IS 'Parent Status Values foreign key to the same table';
COMMENT ON COLUMN egtl_status_values.doc_num IS 'Document Number';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_trade_license (
    id bigint NOT NULL,
    total_hp bigint,
    ismotorinstalled boolean DEFAULT false,
    state_id bigint,
    hotel_grade character varying(32),
    sand_buckets bigint,
    water_buckets bigint,
    dcp_extinguisher bigint,
    noc_number character varying(50),
    iscertificategenerated boolean DEFAULT false
);

ALTER TABLE ONLY egtl_trade_license
    ADD CONSTRAINT fk_trade_license_eg_wf_states FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);

COMMENT ON TABLE egtl_trade_license IS 'Child Table of EGTL_LICENSE storing Trade License Specific Data';
COMMENT ON COLUMN egtl_trade_license.id IS 'Primary Key';
COMMENT ON COLUMN egtl_trade_license.total_hp IS 'Total Horse Power';
COMMENT ON COLUMN egtl_trade_license.ismotorinstalled IS 'Is Motor Installed';
COMMENT ON COLUMN egtl_trade_license.state_id IS 'Foreign Key State Id for the table EG_WF_STATES';
COMMENT ON COLUMN egtl_trade_license.hotel_grade IS 'Hotel Grade';
-------------------END-------------------

------------------START------------------
CREATE TABLE egtl_tradelicense_docs (
    license bigint,
    document bigint
);

ALTER TABLE ONLY egtl_tradelicense_docs
    ADD CONSTRAINT fk_license_docs_document FOREIGN KEY (document) REFERENCES egtl_document(id);
ALTER TABLE ONLY egtl_tradelicense_docs
    ADD CONSTRAINT fk_license_docs_license FOREIGN KEY (license) REFERENCES egtl_license(id);
-------------------END-------------------


---Sequences
CREATE SEQUENCE seq_egtl_activity;
CREATE SEQUENCE seq_egtl_document;
CREATE SEQUENCE seq_egtl_document_type;
CREATE SEQUENCE seq_egtl_feematrix;
CREATE SEQUENCE seq_egtl_feematrix_detail;
CREATE SEQUENCE seq_egtl_license;
CREATE SEQUENCE seq_egtl_licensee;
CREATE SEQUENCE seq_egtl_licensetransfer;
CREATE SEQUENCE seq_egtl_major_works;
CREATE SEQUENCE seq_egtl_motor_details;
CREATE SEQUENCE seq_egtl_mstr_app_type;
CREATE SEQUENCE seq_egtl_mstr_business_nature;
CREATE SEQUENCE seq_egtl_mstr_category;
CREATE SEQUENCE seq_egtl_mstr_department;
CREATE SEQUENCE seq_egtl_mstr_fee_type;
CREATE SEQUENCE seq_egtl_mstr_license_sub_type;
CREATE SEQUENCE seq_egtl_mstr_license_type;
CREATE SEQUENCE seq_egtl_mstr_motor_fee_range;
CREATE SEQUENCE seq_egtl_mstr_schedule;
CREATE SEQUENCE seq_egtl_mstr_status;
CREATE SEQUENCE seq_egtl_mstr_sub_category;
CREATE SEQUENCE seq_egtl_mstr_unitofmeasure;
CREATE SEQUENCE seq_egtl_notice;
CREATE SEQUENCE seq_egtl_objection;
CREATE SEQUENCE seq_egtl_status_values;


--ALTER eg_wf_matrix
alter table eg_wf_matrix alter column nextstatus type character varying(100);
