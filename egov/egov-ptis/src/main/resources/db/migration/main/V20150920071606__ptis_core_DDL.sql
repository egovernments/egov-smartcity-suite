------------------START------------------
create sequence seq_egpt_apartment;
CREATE TABLE egpt_apartment
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  name character varying(25),
  code character varying(10),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint
);

ALTER TABLE ONLY egpt_apartment add CONSTRAINT pk_egpt_appartments PRIMARY KEY (id);

COMMENT ON TABLE egpt_apartment IS 'Master table for apartment details';
COMMENT ON COLUMN egpt_apartment.id IS 'Primary Key';
COMMENT ON COLUMN egpt_apartment.name IS 'Name of the apartment';
COMMENT ON COLUMN egpt_apartment.code IS 'Code for the apartment';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_application_type;
CREATE TABLE egpt_application_type
(
  id bigint NOT NULL,
  code character varying(25) NOT NULL,
  name character varying(50) NOT NULL,
  resolutiontime bigint NOT NULL,
  description character varying(50),
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version bigint
);

ALTER TABLE ONLY egpt_application_type ADD CONSTRAINT pk_egpt_application_type PRIMARY KEY (id);
ALTER TABLE ONLY egpt_application_type ADD CONSTRAINT unq_egpt_application_code UNIQUE (code);
ALTER TABLE ONLY egpt_application_type ADD CONSTRAINT unq_egpt_application_name UNIQUE (name);

COMMENT ON TABLE egpt_application_type IS 'Master for list application types';
COMMENT ON COLUMN egpt_application_type.id IS 'Primary Key';
COMMENT ON COLUMN egpt_application_type.name IS 'Name of the application';
COMMENT ON COLUMN egpt_application_type.code IS 'Code for the application';
COMMENT ON COLUMN egpt_application_type.resolutiontime IS 'Resultation time to resolve the application';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_document_type;
CREATE TABLE egpt_document_type
(
  id numeric NOT NULL,
  name character varying(100),
  mandatory boolean,
  version numeric,
  transactiontype character varying(20),
  id_application_type bigint
);

ALTER TABLE ONLY egpt_document_type ADD CONSTRAINT pk_egpt_document_type PRIMARY KEY (id);
ALTER TABLE ONLY egpt_document_type ADD CONSTRAINT fk_egpt_document_type_applicationtype FOREIGN KEY (id_application_type) REFERENCES egpt_application_type (id);

COMMENT ON TABLE egpt_document_type IS 'Master table for list of docs required with various transactions on property';
COMMENT ON COLUMN egpt_document_type.id IS 'Primary Key';
COMMENT ON COLUMN egpt_document_type.name IS 'Name of the document';
COMMENT ON COLUMN egpt_document_type.mandatory IS 'Flat to tell document is mandatory or not';
COMMENT ON COLUMN egpt_document_type.transactiontype IS 'FK to egpt_application_type';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_exemption_reason;
CREATE TABLE egpt_exemption_reason
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  name character varying(25),
  code character varying(10),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint
);

ALTER TABLE ONLY egpt_exemption_reason ADD CONSTRAINT pk_egpt_exemption_reason PRIMARY KEY (id);
ALTER TABLE ONLY egpt_exemption_reason ADD CONSTRAINT fk_exemption_reason_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id);
ALTER TABLE ONLY egpt_exemption_reason ADD CONSTRAINT fk_exemption_reason_lastmodifiedby FOREIGN KEY (createdby) REFERENCES eg_user (id);

COMMENT ON TABLE egpt_exemption_reason IS 'Master table for property exemption list';
COMMENT ON COLUMN egpt_exemption_reason.id IS 'Primary Key';
COMMENT ON COLUMN egpt_exemption_reason.name IS 'Name of exemption';
COMMENT ON COLUMN egpt_exemption_reason.code IS 'Code of exemption';
COMMENT ON COLUMN egpt_document_type.transactiontype IS 'FK to egpt_application_type';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_property_usage_master;
CREATE TABLE egpt_property_usage_master
(
  id bigint NOT NULL, -- Primary Key
  usg_name character varying(256) NOT NULL, -- name of prpperty usage
  modified_date date NOT NULL,
  usage_factor double precision, -- Usage Factor Value
  usg_name_local character varying(512), -- Not used
  code character varying(16) NOT NULL, -- code for property usage (internal use)
  order_id bigint, -- Order no to display list
  from_date date NOT NULL, -- from date for usage factor validity
  to_date timestamp without time zone, -- To date for usage factor validity
  is_enabled bigint NOT NULL DEFAULT 1, -- is this usage is enabled on system. 0 or 1
  created_by bigint,
  modified_by bigint,
  created_date date,
  ISRESIDENTIAL boolean NOT NULL,
  CONSTRAINT pk_egpt_property_usage_master PRIMARY KEY (id)
);

COMMENT ON TABLE egpt_property_usage_master IS 'Master table for property usages';
COMMENT ON COLUMN egpt_property_usage_master.id IS 'Primary Key';
COMMENT ON COLUMN egpt_property_usage_master.usg_name IS 'name of prpperty usage';
COMMENT ON COLUMN egpt_property_usage_master.usage_factor IS 'Usage Factor Value';
COMMENT ON COLUMN egpt_property_usage_master.usg_name_local IS 'Not used';
COMMENT ON COLUMN egpt_property_usage_master.code IS 'code for property usage (internal use)';
COMMENT ON COLUMN egpt_property_usage_master.order_id IS 'Order no to display list';
COMMENT ON COLUMN egpt_property_usage_master.from_date IS 'from date for usage factor validity';
COMMENT ON COLUMN egpt_property_usage_master.to_date IS 'To date for usage factor validity';
COMMENT ON COLUMN egpt_property_usage_master.is_enabled IS 'is this usage is enabled on system. 0 or 1';

CREATE UNIQUE INDEX idx_unique_key_usagename ON egpt_property_usage_master USING btree (usg_name);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_struc_cl;
CREATE TABLE egpt_struc_cl
(
  id bigint NOT NULL, -- Primary Key
  constr_num bigint NOT NULL, -- Not used
  constr_type character varying(128) NOT NULL, -- Type construction
  constr_descr character varying(512), -- Construction type description
  constr_factor double precision, -- Factor value for construction type
  modified_date date NOT NULL,
  id_installment bigint NOT NULL, -- Not used
  is_history character(1) DEFAULT 'N'::bpchar,
  created_by bigint,
  floor_num bigint, -- Not used
  code character varying(16) NOT NULL,
  order_id bigint, -- Order no to display master
  from_date date NOT NULL, -- From date of effective
  to_date timestamp without time zone, -- To date of effective
  created_date timestamp without time zone,
  modified_by bigint,
  CONSTRAINT pk_egpt_struc_cl PRIMARY KEY (id)
);

COMMENT ON TABLE egpt_struc_cl IS 'Master table for Structual classification';
COMMENT ON COLUMN egpt_struc_cl.id IS 'Primary Key';
COMMENT ON COLUMN egpt_struc_cl.constr_num IS 'Not used';
COMMENT ON COLUMN egpt_struc_cl.constr_type IS 'Type construction';
COMMENT ON COLUMN egpt_struc_cl.constr_descr IS 'Construction type description';
COMMENT ON COLUMN egpt_struc_cl.constr_factor IS 'Factor value for construction type';
COMMENT ON COLUMN egpt_struc_cl.id_installment IS 'Not used';
COMMENT ON COLUMN egpt_struc_cl.floor_num IS 'Not used';
COMMENT ON COLUMN egpt_struc_cl.order_id IS 'Order no to display master';
COMMENT ON COLUMN egpt_struc_cl.from_date IS 'From date of effective';
COMMENT ON COLUMN egpt_struc_cl.to_date IS 'To date of effective';

CREATE UNIQUE INDEX idx_unq_floor_code ON egpt_struc_cl USING btree (code, floor_num);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_mstr_category;
CREATE TABLE egpt_mstr_category
(
  id bigint NOT NULL, -- Primary Key
  category_name character varying(32) NOT NULL, -- For internal use only
  category_amnt double precision NOT NULL, -- Tax amount
  id_installment bigint, -- Not used
  is_history character(1), -- N
  created_by bigint, -- FK to eg_user
  id_usage bigint, -- FK to EGPT_PROPERTY_USAGE_MASTER
  from_date timestamp without time zone, -- Start date of period
  to_date timestamp without time zone, -- End date of period
  modified_by bigint, -- FK to eg_user
  created_date timestamp without time zone,
  modified_date timestamp without time zone,
  id_struct_cl bigint -- FK to EGPT_STRUC_CL
);
ALTER TABLE ONLY egpt_mstr_category    ADD CONSTRAINT pk_egpt_category PRIMARY KEY (id);
ALTER TABLE ONLY egpt_mstr_category  ADD CONSTRAINT fk_category_struct_cl FOREIGN KEY (id_struct_cl) REFERENCES egpt_struc_cl (id);
ALTER TABLE ONLY egpt_mstr_category  ADD CONSTRAINT fk_id_usage FOREIGN KEY (id_usage) REFERENCES egpt_property_usage_master (id);

COMMENT ON TABLE egpt_mstr_category
  IS 'Stores tax rates for usage + structural classification combinations, time-wise.';
COMMENT ON COLUMN egpt_mstr_category.id IS 'Primary Key';
COMMENT ON COLUMN egpt_mstr_category.category_name IS 'For internal use only';
COMMENT ON COLUMN egpt_mstr_category.category_amnt IS 'Tax amount';
COMMENT ON COLUMN egpt_mstr_category.id_installment IS 'Not used';
COMMENT ON COLUMN egpt_mstr_category.is_history IS 'N';
COMMENT ON COLUMN egpt_mstr_category.created_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_mstr_category.id_usage IS 'FK to EGPT_PROPERTY_USAGE_MASTER';
COMMENT ON COLUMN egpt_mstr_category.from_date IS 'Start date of period';
COMMENT ON COLUMN egpt_mstr_category.to_date IS 'End date of period';
COMMENT ON COLUMN egpt_mstr_category.modified_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_mstr_category.id_struct_cl IS 'FK to EGPT_STRUC_CL';

CREATE UNIQUE INDEX idx_unq_egpt_cat_catname ON egpt_mstr_category USING btree (category_name);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_mstr_bndry_category;
CREATE TABLE egpt_mstr_bndry_category
(
  id bigint NOT NULL, -- Primary Key
  id_bndry bigint NOT NULL, -- FK to EG_BOUNDARY
  id_category bigint NOT NULL, -- FK to EGPT_MSTR_CATEGORY
  modified_date date NOT NULL,
  from_date timestamp without time zone, -- Starting date of the period where this value is active
  to_date timestamp without time zone, -- End date of the period where this value is active
  created_by bigint, -- FK to eg_user
  modified_by bigint, -- FK to eg_user
  created_date date
);

ALTER TABLE ONLY egpt_mstr_bndry_category ADD CONSTRAINT pk_egpt_mstr_bndry_category PRIMARY KEY (id);
ALTER TABLE ONLY egpt_mstr_bndry_category ADD CONSTRAINT fk_category_bndry_category FOREIGN KEY (id_category) REFERENCES egpt_mstr_category (id);

COMMENT ON TABLE egpt_mstr_bndry_category IS 'Maps boundaries to usage + structural classification combinations, time-wise.';
COMMENT ON COLUMN egpt_mstr_bndry_category.id IS 'Primary Key';
COMMENT ON COLUMN egpt_mstr_bndry_category.id_bndry IS 'FK to EG_BOUNDARY';
COMMENT ON COLUMN egpt_mstr_bndry_category.id_category IS 'FK to EGPT_MSTR_CATEGORY';
COMMENT ON COLUMN egpt_mstr_bndry_category.from_date IS 'Starting date of the period where this value is active';
COMMENT ON COLUMN egpt_mstr_bndry_category.to_date IS 'End date of the period where this value is active';
COMMENT ON COLUMN egpt_mstr_bndry_category.created_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_mstr_bndry_category.modified_by IS 'FK to eg_user';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_floor_type;
CREATE TABLE egpt_floor_type
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  name character varying(64),
  code character varying(10),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint
);

ALTER TABLE ONLY egpt_floor_type ADD CONSTRAINT pk_egpt_floor_type PRIMARY KEY (id);
ALTER TABLE ONLY egpt_floor_type ADD CONSTRAINT fk_floor_type_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id);
ALTER TABLE ONLY egpt_floor_type ADD CONSTRAINT fk_floor_type_lastmodifiedby FOREIGN KEY (createdby) REFERENCES eg_user (id);

COMMENT ON TABLE egpt_floor_type IS 'Master table for floor type';
COMMENT ON COLUMN egpt_floor_type.id IS 'Primary Key';
COMMENT ON COLUMN egpt_floor_type.name IS 'Name for floor type';
COMMENT ON COLUMN egpt_floor_type.code IS 'Code for floor type';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_wall_type;
CREATE TABLE egpt_wall_type
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  name character varying(64),
  code character varying(10),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  CONSTRAINT pk_egpt_wall_type PRIMARY KEY (id),
  CONSTRAINT fk_wall_type_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_wall_type_lastmodifiedby FOREIGN KEY (createdby)
      REFERENCES eg_user (id)
);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_wood_type;
CREATE TABLE egpt_wood_type
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  name character varying(64),
  code character varying(10),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  CONSTRAINT pk_egpt_wood_type PRIMARY KEY (id),
  CONSTRAINT fk_wood_type_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_wood_type_lastmodifiedby FOREIGN KEY (createdby)
      REFERENCES eg_user (id)
);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_property_type_master;
CREATE TABLE egpt_property_type_master
(
  id bigint NOT NULL, -- Primary Key
  property_type character varying(256) NOT NULL, -- type of property
  modified_date date NOT NULL,
  type_factor bigint NOT NULL, -- Factor value for Property type
  property_type_local character varying(512), -- Not used
  code character varying(32), -- Code for proeprty type (internal use)
  created_by bigint,
  modified_by bigint,
  created_date timestamp without time zone,
  orderno bigint, -- Order no to display list
  CONSTRAINT pk_egpt_propertytype_master PRIMARY KEY (id)
);
COMMENT ON TABLE egpt_property_type_master IS 'Master table for property type';
COMMENT ON COLUMN egpt_property_type_master.id IS 'Primary Key';
COMMENT ON COLUMN egpt_property_type_master.property_type IS 'type of property';
COMMENT ON COLUMN egpt_property_type_master.type_factor IS 'Factor value for Property type';
COMMENT ON COLUMN egpt_property_type_master.property_type_local IS 'Not used';
COMMENT ON COLUMN egpt_property_type_master.code IS 'Code for proeprty type (internal use)';
COMMENT ON COLUMN egpt_property_type_master.orderno IS 'Order no to display list';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_roof_type;
CREATE TABLE egpt_roof_type
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  name character varying(64),
  code character varying(10),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  CONSTRAINT pk_egpt_roof_type PRIMARY KEY (id),
  CONSTRAINT fk_roof_type_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_roof_type_lastmodifiedby FOREIGN KEY (createdby)
      REFERENCES eg_user (id)
);
-------------------END-------------------

------------------START------------------
create sequence SEQ_EGPT_SRC_OF_INFO;
CREATE TABLE egpt_src_of_info
(
  id bigint NOT NULL, -- Primary Key
  source_name character varying(256) NOT NULL, -- source of property name
  lastupdatedtimestamp date NOT NULL,
  source_name_local character varying(768), -- Name locale
  src_sht_name character varying(32), -- Code for Source of Info
  CONSTRAINT pk_egpt_src_of_info PRIMARY KEY (id)
);

COMMENT ON TABLE egpt_src_of_info  IS 'Master table for property source of info';
COMMENT ON COLUMN egpt_src_of_info.id IS 'Primary Key';
COMMENT ON COLUMN egpt_src_of_info.source_name IS 'source of property name';
COMMENT ON COLUMN egpt_src_of_info.source_name_local IS 'Name locale';
COMMENT ON COLUMN egpt_src_of_info.src_sht_name IS 'Code for Source of Info';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_status;
CREATE TABLE egpt_status
(
  id bigint NOT NULL, -- Primary Key
  status_name character varying(256) NOT NULL, -- Property status name
  created_date date NOT NULL,
  is_active character(1), -- is this status acitve. 0 or 1
  code character varying(32), -- property status code (internal use)
  CONSTRAINT egpt_status_pk PRIMARY KEY (id)
);
COMMENT ON TABLE egpt_status IS 'Master table for property status';
COMMENT ON COLUMN egpt_status.id IS 'Primary Key';
COMMENT ON COLUMN egpt_status.status_name IS 'Property status name';
COMMENT ON COLUMN egpt_status.is_active IS 'is this status acitve. 0 or 1';
COMMENT ON COLUMN egpt_status.code IS 'property status code (internal use)';

CREATE UNIQUE INDEX idx_unique_key_status_name ON egpt_status USING btree (status_name);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_occupation_type_master;
CREATE TABLE egpt_occupation_type_master
(
  id bigint NOT NULL, -- Primary Key
  occupation character varying(256) NOT NULL, -- Owner, Tenanted, Vacant
  modified_date date NOT NULL,
  occupany_factor double precision, -- 1, 2, 3 - used in tax calc
  occupation_local character varying(512), -- Not used
  code character varying(16), -- Internal use
  from_date timestamp without time zone, -- From date for occupancy factor validity
  to_date timestamp without time zone, -- To date for occupancy factor validity
  id_usg_mstr bigint, -- Not used
  created_by bigint,
  modified_by bigint,
  created_date date -- From date for occupancy factor validity
);

ALTER TABLE ONLY egpt_occupation_type_master ADD CONSTRAINT pk_egpt_occupation_type_master PRIMARY KEY (id);
ALTER TABLE ONLY egpt_occupation_type_master ADD CONSTRAINT fk_prop_usg_mstr FOREIGN KEY (id_usg_mstr) REFERENCES egpt_property_usage_master (id);

COMMENT ON TABLE egpt_occupation_type_master IS 'Master table for occupation type';
COMMENT ON COLUMN egpt_occupation_type_master.id IS 'Primary Key';
COMMENT ON COLUMN egpt_occupation_type_master.occupation IS 'Owner, Tenanted, Vacant';
COMMENT ON COLUMN egpt_occupation_type_master.occupany_factor IS '1, 2, 3 - used in tax calc';
COMMENT ON COLUMN egpt_occupation_type_master.occupation_local IS 'Not used';
COMMENT ON COLUMN egpt_occupation_type_master.code IS 'Internal use';
COMMENT ON COLUMN egpt_occupation_type_master.from_date IS 'From date for occupancy factor validity';
COMMENT ON COLUMN egpt_occupation_type_master.to_date IS 'To date for occupancy factor validity';
COMMENT ON COLUMN egpt_occupation_type_master.id_usg_mstr IS 'Not used';
COMMENT ON COLUMN egpt_occupation_type_master.created_date IS 'From date for occupancy factor validity';
-------------------END-------------------

------------------START------------------
CREATE TABLE egpt_address
(
  id bigint, -- Primary Key
  subnumber character varying(32), -- Alternate NUMBER
  doornumold character varying(64), -- Old door NUMBER
  emailaddress character varying(64), -- Email address
  mobileno character varying(10), -- Mobile NUMBER
  extrafield1 character varying(128), -- Extra address information
  extrafield2 character varying(128), -- Extra address information
  extrafield3 character varying(128), -- Extra address information
  extrafield4 character varying(128) -- Extra address information
);

COMMENT ON TABLE egpt_address IS 'Link table between PTIS and EG_ADDRESS';
COMMENT ON COLUMN egpt_address.id IS 'Primary Key';
COMMENT ON COLUMN egpt_address.subnumber IS 'Alternate NUMBER';
COMMENT ON COLUMN egpt_address.doornumold IS 'Old door NUMBER';
COMMENT ON COLUMN egpt_address.emailaddress IS 'Email address';
COMMENT ON COLUMN egpt_address.mobileno IS 'Mobile NUMBER';
COMMENT ON COLUMN egpt_address.extrafield1 IS 'Extra address information';
COMMENT ON COLUMN egpt_address.extrafield2 IS 'Extra address information';
COMMENT ON COLUMN egpt_address.extrafield3 IS 'Extra address information';
COMMENT ON COLUMN egpt_address.extrafield4 IS 'Extra address information';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_propertyid;
CREATE TABLE egpt_propertyid
(
  id bigint NOT NULL, -- Primary Key
  zone_num bigint, -- zone boundary (FK to eg_boundary)
  ward_adm_id bigint, -- Ward boundry FK to EG_BOUNDRY
  adm1 bigint, -- FK to EG_BOUNDRY
  adm2 bigint, -- FK to EG_BOUNDRY
  adm3 bigint, -- FK to EG_BOUNDRY
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  front_bndry_street bigint, -- Front bounded boundary
  back_bndry_street bigint, -- Back bounded boundary
  left_bndry_street bigint, -- Left bounded boundary
  right_bndry_street bigint, -- Right bounded boundary
  bndry_street_dmdcalc bigint, -- Boundary for tax calculation in case boundary is diff from actual boundary
  north_bounded character varying(256), -- Property North Bounded by
  south_bounded character varying(256), -- Property South Bounded by
  east_bounded character varying(256), -- Property East Bounded by
  west_bounded character varying(256), -- Property West Bounded by
  created_by bigint,
  modified_by bigint,
  elect_bndry bigint,
  CONSTRAINT pk_egpt_propertyid PRIMARY KEY (id),
  CONSTRAINT fk_eg_boundary_proprtyid FOREIGN KEY (elect_bndry)
      REFERENCES eg_boundary (id)
);
COMMENT ON TABLE egpt_propertyid IS 'Contains Property boundary and bounded by information';
COMMENT ON COLUMN egpt_propertyid.id IS 'Primary Key';
COMMENT ON COLUMN egpt_propertyid.zone_num IS 'zone boundary (FK to eg_boundary)';
COMMENT ON COLUMN egpt_propertyid.ward_adm_id IS 'Ward boundry FK to EG_BOUNDRY';
COMMENT ON COLUMN egpt_propertyid.adm1 IS 'FK to EG_BOUNDRY';
COMMENT ON COLUMN egpt_propertyid.adm2 IS 'FK to EG_BOUNDRY';
COMMENT ON COLUMN egpt_propertyid.adm3 IS 'FK to EG_BOUNDRY';
COMMENT ON COLUMN egpt_propertyid.front_bndry_street IS 'Front bounded boundary';
COMMENT ON COLUMN egpt_propertyid.back_bndry_street IS 'Back bounded boundary';
COMMENT ON COLUMN egpt_propertyid.left_bndry_street IS 'Left bounded boundary';
COMMENT ON COLUMN egpt_propertyid.right_bndry_street IS 'Right bounded boundary';
COMMENT ON COLUMN egpt_propertyid.bndry_street_dmdcalc IS 'Boundary for tax calculation in case boundary is diff from actual boundary';
COMMENT ON COLUMN egpt_propertyid.north_bounded IS 'Property North Bounded by';
COMMENT ON COLUMN egpt_propertyid.south_bounded IS 'Property South Bounded by';
COMMENT ON COLUMN egpt_propertyid.east_bounded IS 'Property East Bounded by';
COMMENT ON COLUMN egpt_propertyid.west_bounded IS 'Property West Bounded by';
-------------------END-------------------
------------------START------------------
CREATE SEQUENCE SEQ_EGPT_BASIC_PROPERTY;
CREATE TABLE egpt_basic_property
(
  id bigint NOT NULL, -- Primary Key
  createdby bigint NOT NULL, -- FK to eg_user
  propertyid character varying(64), -- The property business identifier e.g. 072000008
  isactive boolean NOT NULL DEFAULT true, -- Y or N
  addressid bigint NOT NULL, -- FK to EG_ADDRESS
  createddate timestamp without time zone NOT NULL, -- Created date
  lastmodifieddate timestamp without time zone NOT NULL, -- Modified date
  id_propertyid bigint NOT NULL, -- FK to EGPT_PROPERTYID
  municiapl_no_old character varying(64), -- Not used
  id_adm_bndry bigint, -- The ward boundary ID. FK to eg_boundary.
  extra_field1 character varying(30), -- Not used
  extra_field2 character varying(1024), -- Not used
  extra_field3 character varying(30), -- Not used
  id_prop_create_reason bigint, -- FK to EGPT_MUTATION_MASTER, reason for creation
  prop_occupancy_date timestamp without time zone, -- Date of occupation
  status bigint, -- FK to EGPT_STATUS
  modifiedby bigint, -- FK to eg_user
  gis_ref_no character varying(4000), -- GIS parcel ID
  source character varying DEFAULT 'A'::character varying, -- Y or N
  partno character varying(12), -- Part NUMBER
  all_changes_completed character varying(30),
  is_bill_created character varying DEFAULT false, -- bill creation status for a Tax period and value set to N starting of new Tax period
  bill_crt_error character varying(4000), -- error log while creating bill if any
  is_taxxml_migrated character varying DEFAULT false,
  vl_assessmentno character varying(25),
  regd_doc_no character varying(25) NOT NULL,
  regd_doc_date timestamp without time zone NOT NULL,
  underworkflow boolean DEFAULT false,
  assessmentdate date NOT NULL
);

ALTER TABLE ONLY egpt_basic_property ADD CONSTRAINT pk_egpt_basic_property PRIMARY KEY (id);
ALTER TABLE ONLY egpt_basic_property ADD CONSTRAINT fk_basic_status_id FOREIGN KEY (status) REFERENCES egpt_status (id);
ALTER TABLE ONLY egpt_basic_property ADD CONSTRAINT fk_propertyid_basicprop FOREIGN KEY (id_propertyid) REFERENCES egpt_propertyid (id);
ALTER TABLE ONLY egpt_basic_property add CONSTRAINT fk_basicproperty_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id);
ALTER TABLE ONLY egpt_basic_property add CONSTRAINT fk_basicproperty_modifiedby FOREIGN KEY (modifiedby) REFERENCES eg_user (id);

COMMENT ON TABLE egpt_basic_property IS 'This is the main table of PTIS, which contains the top-level property information';
COMMENT ON COLUMN egpt_basic_property.id IS 'Primary Key';
COMMENT ON COLUMN egpt_basic_property.createdby IS 'FK to eg_user';
COMMENT ON COLUMN egpt_basic_property.propertyid IS 'The property business identifier e.g. Assessmentno';
COMMENT ON COLUMN egpt_basic_property.isactive IS 'Y or N';
COMMENT ON COLUMN egpt_basic_property.addressid IS 'FK to EG_ADDRESS';
COMMENT ON COLUMN egpt_basic_property.createddate IS 'Created date';
COMMENT ON COLUMN egpt_basic_property.lastmodifieddate IS 'Modified date';
COMMENT ON COLUMN egpt_basic_property.id_propertyid IS 'FK to EGPT_PROPERTYID';
COMMENT ON COLUMN egpt_basic_property.municiapl_no_old IS 'Not used';
COMMENT ON COLUMN egpt_basic_property.id_adm_bndry IS 'The ward boundary ID. FK to eg_boundary.';
COMMENT ON COLUMN egpt_basic_property.extra_field1 IS 'Not used';
COMMENT ON COLUMN egpt_basic_property.extra_field2 IS 'Not used';
COMMENT ON COLUMN egpt_basic_property.extra_field3 IS 'Not used';
COMMENT ON COLUMN egpt_basic_property.id_prop_create_reason IS 'FK to EGPT_MUTATION_MASTER, reason for creation';
COMMENT ON COLUMN egpt_basic_property.prop_occupancy_date IS 'Date of occupation';
COMMENT ON COLUMN egpt_basic_property.status IS 'FK to EGPT_STATUS';
COMMENT ON COLUMN egpt_basic_property.modifiedby IS 'FK to eg_user';
COMMENT ON COLUMN egpt_basic_property.gis_ref_no IS 'GIS ref. ID';
COMMENT ON COLUMN egpt_basic_property.source IS 'Source of assessment, Eg:A-application, M-Migration, etc';
COMMENT ON COLUMN egpt_basic_property.partno IS 'Part NUMBER';
COMMENT ON COLUMN egpt_basic_property.is_bill_created IS 'bill creation status for a Tax period and value set to N starting of new Tax period';
COMMENT ON COLUMN egpt_basic_property.bill_crt_error IS 'error log while creating bill if any';
COMMENT ON COLUMN egpt_basic_property.vl_assessmentno IS 'Vacant land assessment no';
COMMENT ON COLUMN egpt_basic_property.regd_doc_no IS 'Registration doc number';
COMMENT ON COLUMN egpt_basic_property.regd_doc_date IS 'Registration doc date';
COMMENT ON COLUMN egpt_basic_property.underworkflow IS 'status to know assessment is under workflow or not';
COMMENT ON COLUMN egpt_basic_property.assessmentdate IS 'Date of final approval for new assessment';

CREATE UNIQUE INDEX IDX_UNQ_PROPERTY_ID ON EGPT_BASIC_PROPERTY USING BTREE (PROPERTYID);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_bulkbillgeneration;
CREATE TABLE egpt_bulkbillgeneration
(
  id bigint NOT NULL,
  zone bigint NOT NULL,
  ward bigint,
  installment bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  modifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint
);

ALTER TABLE ONLY egpt_bulkbillgeneration ADD CONSTRAINT pk_egpt_bulkbillgeneration PRIMARY KEY (id);
ALTER TABLE ONLY egpt_bulkbillgeneration ADD CONSTRAINT fk_bulkbillgnrtn_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id);
ALTER TABLE ONLY egpt_bulkbillgeneration ADD CONSTRAINT fk_bulkbillgnrtn_installment FOREIGN KEY (installment) REFERENCES eg_installment_master (id);
ALTER TABLE ONLY egpt_bulkbillgeneration ADD CONSTRAINT fk_bulkbillgnrtn_lastmodifiedby FOREIGN KEY (createdby) REFERENCES eg_user (id);
ALTER TABLE ONLY egpt_bulkbillgeneration ADD CONSTRAINT fk_bulkbillgnrtn_ward FOREIGN KEY (ward) REFERENCES eg_boundary (id);
ALTER TABLE ONLY egpt_bulkbillgeneration ADD CONSTRAINT fk_bulkbillgnrtn_zone FOREIGN KEY (zone) REFERENCES eg_boundary (id);

COMMENT ON TABLE egpt_bulkbillgeneration IS 'This table holds the boundary to generate bill in bulk';
COMMENT ON COLUMN egpt_bulkbillgeneration.id IS 'Primary Key';
COMMENT ON COLUMN egpt_bulkbillgeneration.zone IS 'FK to eg_boundary, zone for which the bills has to be generated';
COMMENT ON COLUMN egpt_bulkbillgeneration.ward IS 'FK to eg_boundary, ward for which the bills has to be generated';
COMMENT ON COLUMN egpt_bulkbillgeneration.installment IS 'FK to eg_installment_master, installment for which period the bill to be generated';
-------------------END-------------------

------------------START------------------
CREATE TABLE egpt_citizen_address
(
  id_address bigint NOT NULL, -- FK to EG_ADDRESS
  id_citizen bigint NOT NULL -- FK to EG_CITIZEN
);

COMMENT ON TABLE egpt_citizen_address IS 'Intermediate table used to map citizen and addresses';
COMMENT ON COLUMN egpt_citizen_address.id_address IS 'FK to EG_ADDRESS';
COMMENT ON COLUMN egpt_citizen_address.id_citizen IS 'FK to EG_CITIZEN';

CREATE INDEX idx_citizenaddress_addressid ON egpt_citizen_address USING btree (id_address);
CREATE INDEX idx_citizenaddress_citizenid ON egpt_citizen_address USING btree (id_citizen);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_demandcalculations;
CREATE TABLE egpt_demandcalculations
(
  id bigint NOT NULL, -- Primary Key
  id_demand bigint NOT NULL, -- FK to EG_DEMAND
  propertytax double precision, -- Total Property Tax for a installment
  rate_of_tax double precision, -- Not used
  current_interest double precision, -- Not used
  arrear_interest double precision, -- Not used
  modified_date timestamp without time zone NOT NULL, -- Modified date
  created_date timestamp without time zone, -- Created date
  created_by bigint, -- FK to eg_user
  modified_by bigint, -- FK to eg_user
  taxinfo bytea, -- Tax calculation XML
  alv double precision -- Not Used
);

ALTER TABLE ONLY egpt_demandcalculations ADD CONSTRAINT pk_egpt_demandcalculations PRIMARY KEY (id);
ALTER TABLE ONLY egpt_demandcalculations add CONSTRAINT fk_demandcalculations_createdby FOREIGN KEY (created_by) REFERENCES eg_user (id);
ALTER TABLE ONLY egpt_demandcalculations add CONSTRAINT fk_demandcalculations_modifiedby FOREIGN KEY (modified_by ) REFERENCES eg_user (id);

COMMENT ON TABLE egpt_demandcalculations IS 'Tax calculation information';
COMMENT ON COLUMN egpt_demandcalculations.id IS 'Primary Key';
COMMENT ON COLUMN egpt_demandcalculations.id_demand IS 'FK to EG_DEMAND';
COMMENT ON COLUMN egpt_demandcalculations.propertytax IS 'Total Property Tax for a installment';
COMMENT ON COLUMN egpt_demandcalculations.rate_of_tax IS 'Not used';
COMMENT ON COLUMN egpt_demandcalculations.current_interest IS 'Not used';
COMMENT ON COLUMN egpt_demandcalculations.arrear_interest IS 'Not used';
COMMENT ON COLUMN egpt_demandcalculations.modified_date IS 'Modified date';
COMMENT ON COLUMN egpt_demandcalculations.created_date IS 'Created date';
COMMENT ON COLUMN egpt_demandcalculations.created_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_demandcalculations.modified_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_demandcalculations.taxinfo IS 'Tax calculation XML';

CREATE INDEX idx_dmdcalc_iddmd ON egpt_demandcalculations USING btree (id_demand);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_document;
CREATE TABLE egpt_document
(
  id numeric NOT NULL,
  type numeric,
  description character varying(100),
  docdate date,
  enclosed boolean,
  createddate timestamp without time zone,
  createdby bigint,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  version numeric
);

ALTER TABLE ONLY egpt_document ADD CONSTRAINT pkegpt_document PRIMARY KEY (id);
ALTER TABLE ONLY egpt_document add CONSTRAINT fk_document_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id);
ALTER TABLE ONLY egpt_document add CONSTRAINT fk_document_modifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user (id);
-------------------END-------------------
------------------START------------------
create sequence seq_egpt_property;
CREATE TABLE egpt_property
(
  id bigint NOT NULL, -- Primary Key
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  created_by bigint NOT NULL,
  id_source bigint NOT NULL, -- FK to EGPT_SRC_OF_INFO
  id_basic_property bigint NOT NULL, -- FK to EGPT_BASIC_PROPERTY
  is_default_property character varying NOT NULL DEFAULT 'N'::bpchar, -- wheather default property of not, values Y or N
  status character varying DEFAULT 'N'::bpchar, -- State of property. values if property in Workflow -W in History- H in Approved - A
  latest_node bigint, -- Not used
  effective_date date NOT NULL, -- Date of Occupancy
  ischecked character varying DEFAULT 'N'::bpchar, -- Not used
  remarks character varying(1048), -- Not used
  modify_reason character varying(25), -- Not used
  id_installment bigint, -- FK to EG_INSTALLMENT_MASTER
  modified_by bigint,
  state_id bigint, -- FK to EG_WF_STATES
  doc_number character varying(256), -- Uploaded document Number
  area_bndry bigint,
  applicationno character varying(25),
  isexemptedfromtax boolean DEFAULT false,
  tax_exempted_reason bigint
);

ALTER TABLE ONLY egpt_property ADD CONSTRAINT pk_egpt_property PRIMARY KEY (id);
ALTER TABLE ONLY egpt_property ADD CONSTRAINT fk_bas_prop FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property (id);
ALTER TABLE ONLY egpt_property ADD CONSTRAINT fk_exemption_reason FOREIGN KEY (tax_exempted_reason) REFERENCES egpt_exemption_reason (id);
ALTER TABLE ONLY egpt_property ADD CONSTRAINT fk_src_of_info_property FOREIGN KEY (id_source) REFERENCES egpt_src_of_info (id);

COMMENT ON TABLE egpt_property IS 'Contains one row for each property per operation like mutation, transfer';
COMMENT ON COLUMN egpt_property.id IS 'Primary Key';
COMMENT ON COLUMN egpt_property.id_source IS 'FK to EGPT_SRC_OF_INFO';
COMMENT ON COLUMN egpt_property.id_basic_property IS 'FK to EGPT_BASIC_PROPERTY';
COMMENT ON COLUMN egpt_property.is_default_property IS 'wheather default property of not, values Y or N';
COMMENT ON COLUMN egpt_property.status IS 'State of property. values if property in Workflow -W in History- H in Approved - A';
COMMENT ON COLUMN egpt_property.latest_node IS 'Not used';
COMMENT ON COLUMN egpt_property.effective_date IS 'Date of Occupancy';
COMMENT ON COLUMN egpt_property.ischecked IS 'Not used';
COMMENT ON COLUMN egpt_property.remarks IS 'Not used';
COMMENT ON COLUMN egpt_property.modify_reason IS 'Not used';
COMMENT ON COLUMN egpt_property.id_installment IS 'FK to EG_INSTALLMENT_MASTER';
COMMENT ON COLUMN egpt_property.state_id IS 'FK to EG_WF_STATES';
COMMENT ON COLUMN egpt_property.doc_number IS 'Uploaded document Number';

CREATE INDEX idx_prop_inst ON egpt_property USING btree (id_basic_property, id_installment, status);
-------------------END-------------------


------------------START------------------
CREATE TABLE egpt_document_files
(
  document numeric,
  filestore numeric
);
-------------------END-------------------
------------------START------------------
CREATE SEQUENCE SEQ_EGPT_MUTATION_MASTER;
CREATE TABLE egpt_mutation_master
(
  id bigint NOT NULL, -- Primary Key
  mutation_name character varying(256), -- Name of mutation type
  mutation_desc character varying(256), -- Description for mutation type
  type character varying(16), -- CREATE, MODIFY, DEACTIVATE, TRANSFER
  code character varying(16), -- Code for internal use
  order_id bigint -- Dropdown ordering
);

ALTER TABLE ONLY egpt_mutation_master ADD CONSTRAINT pk_egpt_mutation_master PRIMARY KEY (id);

COMMENT ON TABLE egpt_mutation_master IS 'Master table that stores types of mutations';
COMMENT ON COLUMN egpt_mutation_master.id IS 'Primary Key';
COMMENT ON COLUMN egpt_mutation_master.mutation_name IS 'Name of mutation type';
COMMENT ON COLUMN egpt_mutation_master.mutation_desc IS 'Description for mutation type';
COMMENT ON COLUMN egpt_mutation_master.type IS 'CREATE, MODIFY, DEACTIVATE, TRANSFER';
COMMENT ON COLUMN egpt_mutation_master.code IS 'Code for internal use';
COMMENT ON COLUMN egpt_mutation_master.order_id IS 'Dropdown ordering';

CREATE UNIQUE INDEX idx_unq_mutation_code ON egpt_mutation_master USING btree (code);
-------------------END-------------------
------------------START------------------
create sequence seq_egpt_property_detail;
CREATE TABLE egpt_property_detail
(
  id bigint NOT NULL, -- FK to EGPT_MUTATION_MASTER, reason for modification
  id_property bigint NOT NULL, -- FK to EGPT_PROPERTY
  property_length double precision, -- Not used
  property_breadth double precision, -- Not used
  sital_area double precision, -- Area of Plot
  is_irregular character(1) DEFAULT 'N'::bpchar, -- Not used
  plinth_area double precision, -- Not used
  total_builtup_area double precision, -- Not used
  commn_builtup_area double precision, -- Not used
  commn_vcnt_land double precision, -- Not used
  num_floors bigint, -- No.of floors
  id_usg_mstr bigint, -- FK to egpt_property_usage_master
  water_meter_num character varying(32), -- Not used
  elec_meter_num character varying(32), -- Not used
  survey_num character varying(64), -- Not used
  is_field_verified character(1) NOT NULL DEFAULT 'N'::bpchar, -- Not used
  field_verif_date timestamp without time zone, -- Not used
  property_type character varying(64) NOT NULL, -- FK to egpt_property_type_master
  id_propertytypemaster bigint, -- FK to egpt_property_type_master
  id_mutation bigint NOT NULL,
  updt_timestamp date NOT NULL, -- Modified Date
  remarks character varying(32),
  is_comzone character(1), -- Amenities for Govt Properties
  is_cornerplot character(1), -- Not used
  extra_field1 character varying(256), -- General Water meter Rate
  dateofcompletion timestamp without time zone, -- Date of Property Completion/occupancy
  id_occpn_mstr bigint, -- FK to egpt_occupation_type_master
  nonres_plotarea double precision, -- Non-Residential area for Plot Area
  manual_alv character varying(256), -- Manual ALV value for Open Plot
  occupier_name character varying(256), -- Occupier Name for Open Plot
  alv character varying(256),
  lift boolean,
  toilets boolean,
  watertap boolean,
  structure boolean,
  electricity boolean,
  attachedbathroom boolean,
  waterharvesting boolean,
  cable boolean,
  extentsite double precision,
  extent_appurtenant_land double precision,
  siteowner character varying(50),
  floortype bigint,
  rooftype bigint,
  walltype bigint,
  woodtype bigint,
  apartment bigint,
  current_capitalvalue double precision,
  marketvalue double precision,
  pattanumber character varying(32),
  occupancy_certificationno character varying(20),
  category_type character varying(256),
  building_permission_no character varying(30),
  building_permission_date timestamp without time zone,
  deviation_percentage character varying(30),
  buildingplandetails_checked boolean,
  appurtenantland_checked boolean,
  corraddressdiff boolean,
  CONSTRAINT pk_egpt_property_detail PRIMARY KEY (id),
  CONSTRAINT fk_egpt_apartment FOREIGN KEY (apartment)
      REFERENCES egpt_apartment (id),
  CONSTRAINT fk_egpt_prop_usg_prop_det FOREIGN KEY (id_usg_mstr)
      REFERENCES egpt_property_usage_master (id),
  CONSTRAINT fk_floortype_id FOREIGN KEY (floortype)
      REFERENCES egpt_floor_type (id),
  CONSTRAINT fk_id_occpn_mstr FOREIGN KEY (id_occpn_mstr)
      REFERENCES egpt_occupation_type_master (id),
  CONSTRAINT fk_id_propertytypemaster FOREIGN KEY (id_propertytypemaster)
      REFERENCES egpt_property_type_master (id),
  CONSTRAINT fk_mutation_master FOREIGN KEY (id_mutation)
      REFERENCES egpt_mutation_master (id),
  CONSTRAINT fk_property_property_detail FOREIGN KEY (id_property)
      REFERENCES egpt_property (id),
  CONSTRAINT fk_rooftype_id FOREIGN KEY (rooftype)
      REFERENCES egpt_roof_type (id),
  CONSTRAINT fk_walltype_id FOREIGN KEY (walltype)
      REFERENCES egpt_wall_type (id),
  CONSTRAINT fk_woodtype_id FOREIGN KEY (woodtype)
      REFERENCES egpt_wood_type (id)
);

COMMENT ON TABLE egpt_property_detail IS 'Contains Property details like type, usage of property';
COMMENT ON COLUMN egpt_property_detail.id IS 'FK to EGPT_MUTATION_MASTER, reason for modification';
COMMENT ON COLUMN egpt_property_detail.id_property IS 'FK to EGPT_PROPERTY';
COMMENT ON COLUMN egpt_property_detail.property_length IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.property_breadth IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.sital_area IS 'Area of Plot';
COMMENT ON COLUMN egpt_property_detail.is_irregular IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.plinth_area IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.total_builtup_area IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.commn_builtup_area IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.commn_vcnt_land IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.num_floors IS 'No.of floors';
COMMENT ON COLUMN egpt_property_detail.id_usg_mstr IS 'FK to egpt_property_usage_master';
COMMENT ON COLUMN egpt_property_detail.water_meter_num IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.elec_meter_num IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.survey_num IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.is_field_verified IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.field_verif_date IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.property_type IS 'FK to egpt_property_type_master';
COMMENT ON COLUMN egpt_property_detail.id_propertytypemaster IS 'FK to egpt_property_type_master';
COMMENT ON COLUMN egpt_property_detail.updt_timestamp IS 'Modified Date';
COMMENT ON COLUMN egpt_property_detail.is_comzone IS 'Amenities for Govt Properties';
COMMENT ON COLUMN egpt_property_detail.is_cornerplot IS 'Not used';
COMMENT ON COLUMN egpt_property_detail.extra_field1 IS 'General Water meter Rate';
COMMENT ON COLUMN egpt_property_detail.dateofcompletion IS 'Date of Property Completion/occupancy';
COMMENT ON COLUMN egpt_property_detail.id_occpn_mstr IS 'FK to egpt_occupation_type_master';
COMMENT ON COLUMN egpt_property_detail.nonres_plotarea IS 'Non-Residential area for Plot Area';
COMMENT ON COLUMN egpt_property_detail.manual_alv IS 'Manual ALV value for Open Plot';
COMMENT ON COLUMN egpt_property_detail.occupier_name IS 'Occupier Name for Open Plot';

CREATE INDEX idx_propdet_propid ON egpt_property_detail USING btree (id_property);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_floor_detail;
CREATE TABLE egpt_floor_detail
(
  id bigint NOT NULL, -- Primary Key
  id_property_detail bigint NOT NULL, -- FK to EGPT_FLOOR_DETAIL
  floor_no bigint NOT NULL, -- Floor Number - 0, 1, 2 etc.
  created_date timestamp without time zone NOT NULL, -- Created date
  floor_area double precision, -- Not used
  builtup_area double precision, -- Built up area
  id_struc_cl bigint, -- FK to EGPT_STRUC_CL
  id_occpn_mstr bigint, -- FK to EGPT_OCCUPATION_TYPE_MASTER
  id_usg_mstr bigint, -- FK to EGPT_PROPERTY_USAGE_MASTER
  water_meter_num character varying(32), -- Water meter number
  elec_meter_num character varying(32), -- Electirc meter number
  modified_date timestamp without time zone NOT NULL,
  rentper_month character varying(256), -- Rent for the unit
  created_by bigint, -- FK to eg_user
  modified_by bigint, -- FK to eg_user
  id_depreciationmaster bigint, -- FK to EGDCB_DEPRECIATIONMASTER
  manual_alv character varying(256), -- Manually entered ALV
  unit_type bigint, -- Type of Unit
  unit_type_category character varying(256), -- Category for Unit
  water_rate character varying(256), -- Water rate
  alv character varying(256),
  mig_status character varying(1),
  occupancydate timestamp without time zone,
  occupantname character varying(32),
  drainage boolean,
  no_of_seats bigint
);

ALTER TABLE ONLY egpt_floor_detail ADD CONSTRAINT pk_egpt_floor_detail_pk PRIMARY KEY (id);
ALTER TABLE ONLY egpt_floor_detail ADD CONSTRAINT fk_occ_typ_mst_flr_det FOREIGN KEY (id_occpn_mstr) REFERENCES egpt_occupation_type_master (id);
ALTER TABLE ONLY egpt_floor_detail ADD CONSTRAINT fk_prop_flr_det FOREIGN KEY (id_property_detail) REFERENCES egpt_property_detail (id);
ALTER TABLE ONLY egpt_floor_detail ADD CONSTRAINT fk_prp_usg_mst_flr_det FOREIGN KEY (id_usg_mstr) REFERENCES egpt_property_usage_master (id);
ALTER TABLE ONLY egpt_floor_detail ADD CONSTRAINT fk_str_cl_flr_det FOREIGN KEY (id_struc_cl) REFERENCES egpt_struc_cl (id);
ALTER TABLE ONLY egpt_floor_detail ADD CONSTRAINT fk_unit_type FOREIGN KEY (unit_type) REFERENCES egpt_property_type_master (id);

COMMENT ON TABLE egpt_floor_detail IS 'Details of a unit';
COMMENT ON COLUMN egpt_floor_detail.id IS 'Primary Key';
COMMENT ON COLUMN egpt_floor_detail.id_property_detail IS 'FK to EGPT_FLOOR_DETAIL';
COMMENT ON COLUMN egpt_floor_detail.floor_no IS 'Floor Number - 0, 1, 2 etc.';
COMMENT ON COLUMN egpt_floor_detail.created_date IS 'Created date';
COMMENT ON COLUMN egpt_floor_detail.floor_area IS 'Not used';
COMMENT ON COLUMN egpt_floor_detail.builtup_area IS 'Built up area';
COMMENT ON COLUMN egpt_floor_detail.id_struc_cl IS 'FK to EGPT_STRUC_CL';
COMMENT ON COLUMN egpt_floor_detail.id_occpn_mstr IS 'FK to EGPT_OCCUPATION_TYPE_MASTER';
COMMENT ON COLUMN egpt_floor_detail.id_usg_mstr IS 'FK to EGPT_PROPERTY_USAGE_MASTER';
COMMENT ON COLUMN egpt_floor_detail.water_meter_num IS 'Water meter number';
COMMENT ON COLUMN egpt_floor_detail.elec_meter_num IS 'Electirc meter number';
COMMENT ON COLUMN egpt_floor_detail.rentper_month IS 'Rent for the unit';
COMMENT ON COLUMN egpt_floor_detail.created_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_floor_detail.modified_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_floor_detail.id_depreciationmaster IS 'FK to EGDCB_DEPRECIATIONMASTER';
COMMENT ON COLUMN egpt_floor_detail.manual_alv IS 'Manually entered ALV';
COMMENT ON COLUMN egpt_floor_detail.unit_type IS 'Type of Unit';
COMMENT ON COLUMN egpt_floor_detail.unit_type_category IS 'Category for Unit';
COMMENT ON COLUMN egpt_floor_detail.water_rate IS 'Water rate';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_floordemandcalc;
CREATE TABLE egpt_floordemandcalc
(
  id bigint NOT NULL, -- Primary Key
  id_floordet bigint, -- FK to EGPT_FLOORDEMANDCALC
  id_dmdcalc bigint, -- FK to EGPT_DEMANDCALCULATIONS
  categoryamt double precision, -- Guidance Value
  occupancyrebate double precision, -- Rebate against occupancy
  constructionrebate double precision, -- Rebate against Construction type
  depreciation_value double precision, -- Rebate against Depriciation
  usagerebate double precision, -- Rebate against Usage
  createtimestamp timestamp without time zone,
  lastupdatedtimestamp timestamp without time zone,
  tax1 double precision, -- Tax amount for tax head 1 for a floor, ULB t ULB the tax heads are different
  tax2 double precision, -- Tax amount for tax head 2 for a floor
  tax3 double precision, -- Tax amount for tax head 3 for a floor
  tax4 double precision, -- Tax amount for tax head 4 for a floor
  tax5 double precision, -- Tax amount for tax head 5 for a floor
  tax6 double precision, -- Tax amount for tax head 6 for a floor
  tax7 double precision, -- Tax amount for tax head 7 for a floor
  tax8 double precision, -- Tax amount for tax head 8 for a floor
  tax9 double precision, -- Tax amount for tax head 9 for a floor
  tax10 double precision, -- Tax amount for tax head 10 for a floor
  alv double precision, -- Manual ALV value for floor
  mrv double precision
);

ALTER TABLE ONLY egpt_floordemandcalc ADD CONSTRAINT pk_egpt_floordemandcalc PRIMARY KEY (id);
ALTER TABLE ONLY egpt_floordemandcalc ADD CONSTRAINT fk_dmdcalc FOREIGN KEY (id_dmdcalc) REFERENCES egpt_demandcalculations (id);
ALTER TABLE ONLY egpt_floordemandcalc ADD CONSTRAINT fk_floordet FOREIGN KEY (id_floordet) REFERENCES egpt_floor_detail (id);

COMMENT ON TABLE egpt_floordemandcalc IS 'Unitwise tax calculation link table';
COMMENT ON COLUMN egpt_floordemandcalc.id IS 'Primary Key';
COMMENT ON COLUMN egpt_floordemandcalc.id_floordet IS 'FK to EGPT_FLOORDEMANDCALC';
COMMENT ON COLUMN egpt_floordemandcalc.id_dmdcalc IS 'FK to EGPT_DEMANDCALCULATIONS';
COMMENT ON COLUMN egpt_floordemandcalc.categoryamt IS 'Guidance Value';
COMMENT ON COLUMN egpt_floordemandcalc.occupancyrebate IS 'Rebate against occupancy';
COMMENT ON COLUMN egpt_floordemandcalc.constructionrebate IS 'Rebate against Construction type';
COMMENT ON COLUMN egpt_floordemandcalc.depreciation_value IS 'Rebate against Depriciation';
COMMENT ON COLUMN egpt_floordemandcalc.usagerebate IS 'Rebate against Usage';
COMMENT ON COLUMN egpt_floordemandcalc.tax1 IS 'Tax amount for tax head 1 for a floor, ULB to ULB the tax heads are different';
COMMENT ON COLUMN egpt_floordemandcalc.tax2 IS 'Tax amount for tax head 2 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax3 IS 'Tax amount for tax head 3 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax4 IS 'Tax amount for tax head 4 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax5 IS 'Tax amount for tax head 5 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax6 IS 'Tax amount for tax head 6 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax7 IS 'Tax amount for tax head 7 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax8 IS 'Tax amount for tax head 8 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax9 IS 'Tax amount for tax head 9 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.tax10 IS 'Tax amount for tax head 10 for a floor';
COMMENT ON COLUMN egpt_floordemandcalc.alv IS 'ALV value for floor';
COMMENT ON COLUMN egpt_floordemandcalc.mrv IS 'MRV value for floor';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_objection;
CREATE TABLE egpt_objection
(
  id bigint NOT NULL, -- Primary Key
  objection_number character varying(50) NOT NULL, -- Autogenerated
  recieved_on timestamp without time zone, -- Received on date
  recieved_by character varying(256), -- objection received by
  details character varying(1024), -- Objection details
  id_status bigint NOT NULL, -- FK to EGW_STATUS
  id_state bigint, -- FK to EG_WF_STATES
  created_by bigint,
  modified_by bigint,
  created_date timestamp without time zone,
  modified_date timestamp without time zone,
  remarks character varying(1024),
  date_of_outcome timestamp without time zone, -- Objection outcome date
  id_basicproperty bigint NOT NULL, -- FK to EGPT_BASIC_PROPERTY
  document_number_objection character varying(50), -- Objection document no
  document_number_outcome character varying(50), -- outcome document no
  objection_rejected boolean,
  id_property bigint,
  generate_specialnotice boolean DEFAULT false
);

ALTER TABLE ONLY egpt_objection ADD CONSTRAINT pk_egpt_objection PRIMARY KEY (id);
ALTER TABLE ONLY egpt_objection ADD CONSTRAINT fk_objection_basicproperty FOREIGN KEY (id_basicproperty) REFERENCES egpt_basic_property (id);
ALTER TABLE ONLY egpt_objection ADD CONSTRAINT fk_objection_propertyid FOREIGN KEY (id_property) REFERENCES egpt_property (id);

COMMENT ON TABLE egpt_objection IS 'Main objection table';
COMMENT ON COLUMN egpt_objection.id IS 'Primary Key';
COMMENT ON COLUMN egpt_objection.objection_number IS 'Autogenerated';
COMMENT ON COLUMN egpt_objection.recieved_on IS 'Received on date';
COMMENT ON COLUMN egpt_objection.recieved_by IS 'objection received by';
COMMENT ON COLUMN egpt_objection.details IS 'Objection details';
COMMENT ON COLUMN egpt_objection.id_status IS 'FK to EGW_STATUS';
COMMENT ON COLUMN egpt_objection.id_state IS 'FK to EG_WF_STATES';
COMMENT ON COLUMN egpt_objection.date_of_outcome IS 'Objection outcome date';
COMMENT ON COLUMN egpt_objection.id_basicproperty IS 'FK to EGPT_BASIC_PROPERTY';
COMMENT ON COLUMN egpt_objection.document_number_objection IS 'Objection document no';
COMMENT ON COLUMN egpt_objection.document_number_outcome IS 'outcome document no';

CREATE UNIQUE INDEX idx_unq_objectionnumber ON egpt_objection USING btree (objection_number);
-------------------END-------------------


------------------START------------------
create sequence seq_egpt_hearing;
CREATE TABLE egpt_hearing
(
  id bigint NOT NULL, -- Primary Key
  id_objection bigint NOT NULL, -- FK to EGPT_OBJECTION
  planned_hearing_date timestamp without time zone, -- Planned hearing date
  actual_hearing_date timestamp without time zone, -- Actual hearing date
  hearing_details character varying(1024), -- hearing deatils
  created_by bigint, -- FK to eg_user
  modified_by bigint, -- FK to eg_user
  created_date timestamp without time zone,
  modified_date timestamp without time zone,
  objection_idx bigint NOT NULL, -- Hearing List index
  document_number character varying(50), -- hearing document no
  hearingnumber character varying(50), -- hearing no
  inspectionrequired boolean,
  hearingtime character varying(25),
  hearingvenue character varying(128)
);
ALTER TABLE ONLY egpt_hearing ADD CONSTRAINT pk_egpt_hearing PRIMARY KEY (id);
ALTER TABLE ONLY egpt_hearing ADD CONSTRAINT fk_objection_hearing FOREIGN KEY (id_objection) REFERENCES egpt_objection (id);
ALTER TABLE ONLY egpt_hearing ADD CONSTRAINT unq_hearingnumber UNIQUE (hearingnumber);

COMMENT ON TABLE egpt_hearing IS 'Objection hearings';
COMMENT ON COLUMN egpt_hearing.id IS 'Primary Key';
COMMENT ON COLUMN egpt_hearing.id_objection IS 'FK to EGPT_OBJECTION';
COMMENT ON COLUMN egpt_hearing.planned_hearing_date IS 'Planned hearing date';
COMMENT ON COLUMN egpt_hearing.actual_hearing_date IS 'Actual hearing date';
COMMENT ON COLUMN egpt_hearing.hearing_details IS 'hearing deatils';
COMMENT ON COLUMN egpt_hearing.created_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_hearing.modified_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_hearing.objection_idx IS 'Hearing List index';
COMMENT ON COLUMN egpt_hearing.document_number IS 'hearing document no';
COMMENT ON COLUMN egpt_hearing.hearingnumber IS 'hearing no';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_inspection;
CREATE TABLE egpt_inspection
(
  id bigint NOT NULL, -- Primary Key
  id_objection bigint NOT NULL, -- FK to EGPT_OBJECTION
  inspection_remarks character varying(1024), -- Inspection remarks
  created_by bigint, -- FK to eg_user
  modified_by bigint, -- FK to eg_user
  created_date timestamp without time zone,
  modified_date timestamp without time zone,
  objection_idx bigint NOT NULL, -- Inspection list index
  document_number character varying(50) -- Inspection document no
);

ALTER TABLE ONLY egpt_inspection ADD CONSTRAINT pk_egpt_inspection PRIMARY KEY (id);
ALTER TABLE ONLY egpt_inspection ADD CONSTRAINT fk_objection_inspection FOREIGN KEY (id_objection) REFERENCES egpt_objection (id);

COMMENT ON TABLE egpt_inspection IS 'Inspections during Objection process';
COMMENT ON COLUMN egpt_inspection.id IS 'Primary Key';
COMMENT ON COLUMN egpt_inspection.id_objection IS 'FK to EGPT_OBJECTION';
COMMENT ON COLUMN egpt_inspection.inspection_remarks IS 'Inspection remarks';
COMMENT ON COLUMN egpt_inspection.created_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_inspection.modified_by IS 'FK to eg_user';
COMMENT ON COLUMN egpt_inspection.objection_idx IS 'Inspection list index';
COMMENT ON COLUMN egpt_inspection.document_number IS 'Inspection document no';
-------------------END-------------------

------------------START------------------
CREATE TABLE egpt_modification_docs
(
  modification bigint,
  document bigint
);

COMMENT ON TABLE egpt_modification_docs IS 'Docs for property modifications';
-------------------END-------------------

------------------START------------------
CREATE TABLE egpt_mutation_docs
(
  mutation bigint,
  document bigint
);

COMMENT ON TABLE egpt_mutation_docs IS 'Docs for property mutation';
-------------------END-------------------


------------------START------------------
create sequence seq_egpt_property_mutation;
CREATE TABLE egpt_property_mutation
(
  id bigint NOT NULL, -- FK to egpt_mutation_master
  mutationdate timestamp without time zone, -- Order date for Name Transfer
  basicproperty bigint NOT NULL, -- FK to egpt_basic_property
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  applicationno character varying(60), -- Application no
  deedno character varying(64), -- Document Number
  deeddate timestamp without time zone, -- Document Date
  lastmodifiedby bigint,
  docnumber character varying(50), -- Not used
  mutationfee bigint, -- Name Transfer Free
  receiptnumber character varying(60), -- Name Transfer Free receipt no
  marketvalue double precision,
  mutationreason bigint,
  otherfee double precision,
  applicantname character varying(100),
  receiptdate date,
  property bigint,
  saledetail character varying(200),
  state numeric,
  feepayable boolean,
  CONSTRAINT egpt_property_mutation_pk PRIMARY KEY (id),
  CONSTRAINT fk_basicpropformut FOREIGN KEY (basicproperty)
      REFERENCES egpt_basic_property (id)
);

COMMENT ON TABLE egpt_property_mutation IS 'Contains Name Transfer information';
COMMENT ON COLUMN egpt_property_mutation.id IS 'FK to egpt_mutation_master';
COMMENT ON COLUMN egpt_property_mutation.mutationdate IS 'Order date for Name Transfer';
COMMENT ON COLUMN egpt_property_mutation.basicproperty IS 'FK to egpt_basic_property';
COMMENT ON COLUMN egpt_property_mutation.applicationno IS 'Application no';
COMMENT ON COLUMN egpt_property_mutation.deedno IS 'Document Number';
COMMENT ON COLUMN egpt_property_mutation.deeddate IS 'Document Date';
COMMENT ON COLUMN egpt_property_mutation.docnumber IS 'Not used';
COMMENT ON COLUMN egpt_property_mutation.mutationfee IS 'Name Transfer Free';
COMMENT ON COLUMN egpt_property_mutation.receiptnumber IS 'Name Transfer Free receipt no';
-------------------END-------------------


------------------START------------------
CREATE TABLE egpt_mutation_transferee
(
  mutation numeric,
  transferee numeric
);
COMMENT ON TABLE egpt_mutation_transferee IS 'Link table between EGPT_PROPERTY_MUTATION and EG_USER for to whom the property is getting transfered';
COMMENT ON COLUMN egpt_mutation_transferee.mutation IS 'FK to EGPT_PROPERTY_MUTATION';
COMMENT ON COLUMN egpt_mutation_transferee.transferee IS 'FK to EG_USER';
-------------------END-------------------

------------------START------------------
CREATE TABLE egpt_mutation_transferor
(
  mutation bigint NOT NULL, -- FK to EGPT_PROPERTY_MUTATION
  transferor bigint NOT NULL -- FK to EG_CITIZEN
);

ALTER TABLE ONLY egpt_mutation_transferor ADD CONSTRAINT fk_id_mutation FOREIGN KEY (mutation) REFERENCES egpt_property_mutation (id);
ALTER TABLE ONLY egpt_mutation_transferor ADD CONSTRAINT fk_transferor FOREIGN KEY (transferor) REFERENCES eg_user (id);

COMMENT ON TABLE egpt_mutation_transferor IS 'Link table between EGPT_PROPERTY_MUTATION and EG_USER for from whom the property is getting transfered';
COMMENT ON COLUMN egpt_mutation_transferor.mutation IS 'FK to EGPT_PROPERTY_MUTATION';
COMMENT ON COLUMN egpt_mutation_transferor.transferor IS 'FK to EG_USER';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_notice;
CREATE TABLE egpt_notice
(
  id bigint NOT NULL, -- Primary Key
  id_basicproperty bigint, -- FK to EGPT_BASIC_PROPERTY
  id_module bigint, -- FK to EG_MODULE
  noticetype character varying(32), -- Type of notice
  noticeno character varying(64), -- Notice Number, it is also the document ID in the document service
  noticedate timestamp without time zone, -- notice generation date
  id_user bigint, -- FK to EG_USER
  filestoreid bigint
);

ALTER TABLE ONLY egpt_notice ADD CONSTRAINT pk_egpt_notice PRIMARY KEY (id);
ALTER TABLE ONLY egpt_notice ADD CONSTRAINT fk_notice_basicproperty FOREIGN KEY (id_basicproperty) REFERENCES egpt_basic_property (id);
ALTER TABLE ONLY egpt_notice ADD CONSTRAINT unq_noticeno UNIQUE (noticeno);

COMMENT ON TABLE egpt_notice IS 'Contains notice information for properties';
COMMENT ON COLUMN egpt_notice.id IS 'Primary Key';
COMMENT ON COLUMN egpt_notice.id_basicproperty IS 'FK to EGPT_BASIC_PROPERTY';
COMMENT ON COLUMN egpt_notice.id_module IS 'FK to EG_MODULE';
COMMENT ON COLUMN egpt_notice.noticetype IS 'Type of notice';
COMMENT ON COLUMN egpt_notice.noticeno IS 'Notice Number, it is also the document ID in the document service';
COMMENT ON COLUMN egpt_notice.noticedate IS 'notice generation date';
COMMENT ON COLUMN egpt_notice.id_user IS 'FK to EG_USER';
-------------------END-------------------


------------------START------------------
create sequence seq_egpt_property_owner_info;
CREATE TABLE egpt_property_owner_info
(
  id bigint NOT NULL, -- FK to EG_CITIZEN
  basicproperty bigint, -- FK to EGPT_PROPERTY
  id_source bigint, -- Not used
  orderno bigint,
  owner bigint,
  CONSTRAINT fk_prop_owner_propid FOREIGN KEY (basicproperty)
      REFERENCES egpt_basic_property (id),
  CONSTRAINT fk_prop_owner_src FOREIGN KEY (id_source)
      REFERENCES egpt_src_of_info (id)
);

COMMENT ON TABLE egpt_property_owner_info IS 'Mapping table for egpt_basic_propery and eg_user, contains property owner';
COMMENT ON COLUMN egpt_property_owner_info.id IS 'Primary key';
COMMENT ON COLUMN egpt_property_owner_info.basicproperty IS 'FK to EGPT_PROPERTY';
COMMENT ON COLUMN egpt_property_owner_info.id_source IS 'Not used';
COMMENT ON COLUMN egpt_property_owner_info.owner IS 'FK to EG_USER';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_property_receipts;
CREATE TABLE egpt_property_receipts
(
  id bigint NOT NULL, -- Primary Key
  id_basic_property bigint, -- FK to EGPT_BASIC_PROPERTY
  booknumber character varying(64), -- Book no
  rcpt_number character varying(64), -- receipt no
  rcpt_date timestamp without time zone, -- receipt date
  from_date timestamp without time zone, -- receipt from date
  to_date timestamp without time zone, -- receipt to date
  rcpt_amount double precision, -- receipt amount
  CONSTRAINT pk_id_prop_rcpts PRIMARY KEY (id),
  CONSTRAINT fk_id_basic_rcpts FOREIGN KEY (id_basic_property)
      REFERENCES egpt_basic_property (id)
);

COMMENT ON TABLE egpt_property_receipts IS 'Contains receipt information from legacy database';
COMMENT ON COLUMN egpt_property_receipts.id IS 'Primary Key';
COMMENT ON COLUMN egpt_property_receipts.id_basic_property IS 'FK to EGPT_BASIC_PROPERTY';
COMMENT ON COLUMN egpt_property_receipts.booknumber IS 'Book no';
COMMENT ON COLUMN egpt_property_receipts.rcpt_number IS 'receipt no';
COMMENT ON COLUMN egpt_property_receipts.rcpt_date IS 'receipt date';
COMMENT ON COLUMN egpt_property_receipts.from_date IS 'receipt from date';
COMMENT ON COLUMN egpt_property_receipts.to_date IS 'receipt to date';
COMMENT ON COLUMN egpt_property_receipts.rcpt_amount IS 'receipt amount';
-------------------END-------------------

------------------START------------------
create sequence SEQ_PROPERTY_STATUS_VALUES;
CREATE TABLE egpt_property_status_values
(
  id bigint NOT NULL, -- Primary Key
  id_status bigint NOT NULL, -- FK to egw_status
  id_basic_property bigint NOT NULL, -- FK to egpt_basic_property
  ref_date timestamp without time zone, -- Order Date
  ref_num character varying(64), -- Order No
  remarks character varying(1024),
  is_active character(1),
  modified_date timestamp without time zone,
  created_date timestamp without time zone,
  created_by bigint NOT NULL,
  ref_id_basic bigint, -- FK to egpt_basic_property, used only for Bifurcation and Amalgation
  modified_by bigint,
  extra_field1 character varying(64), -- Extra info specific to client
  extra_field2 character varying(64), -- Extra info specific to client
  extra_field3 character varying(64), -- Extra info specific to client
  CONSTRAINT pk_egpt_property_status_values PRIMARY KEY (id),
  CONSTRAINT fk_basicprop FOREIGN KEY (id_basic_property)
      REFERENCES egpt_basic_property (id),
  CONSTRAINT fk_id_ststus FOREIGN KEY (id_status)
      REFERENCES egpt_status (id),
  CONSTRAINT fk_status_ref_basic_idr FOREIGN KEY (ref_id_basic)
      REFERENCES egpt_basic_property (id)
);
COMMENT ON TABLE egpt_property_status_values
  IS 'Contains states of the property, holds top level transactions for property';
COMMENT ON COLUMN egpt_property_status_values.id IS 'Primary Key';
COMMENT ON COLUMN egpt_property_status_values.id_status IS 'FK to egw_status';
COMMENT ON COLUMN egpt_property_status_values.id_basic_property IS 'FK to egpt_basic_property';
COMMENT ON COLUMN egpt_property_status_values.ref_date IS 'Order Date';
COMMENT ON COLUMN egpt_property_status_values.ref_num IS 'Order No';
COMMENT ON COLUMN egpt_property_status_values.ref_id_basic IS 'FK to egpt_basic_property, used only for Bifurcation and Amalgation';
COMMENT ON COLUMN egpt_property_status_values.extra_field1 IS 'Extra info specific to client';
COMMENT ON COLUMN egpt_property_status_values.extra_field2 IS 'Extra info specific to client';
COMMENT ON COLUMN egpt_property_status_values.extra_field3 IS 'Extra info specific to client';
-------------------END-------------------


------------------START------------------
CREATE TABLE egpt_ptdemand
(
  id_demand bigint NOT NULL, -- FK to eg_demand
  id_property bigint NOT NULL, -- FK to egpt_property
  CONSTRAINT fk_egpt_property_id FOREIGN KEY (id_property)
      REFERENCES egpt_property (id)
);
COMMENT ON TABLE egpt_ptdemand IS 'Mappig table b/n egpt_property and eg_demand';
COMMENT ON COLUMN egpt_ptdemand.id_demand IS 'FK to eg_demand';
COMMENT ON COLUMN egpt_ptdemand.id_property IS 'FK to egpt_property';

CREATE UNIQUE INDEX idx_unq_ptdemand ON egpt_ptdemand USING btree (id_demand, id_property);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_rebate_period;
CREATE TABLE egpt_rebate_period
(
  id bigint NOT NULL,
  installment bigint NOT NULL,
  rebatedate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version bigint,
  CONSTRAINT pk_egpt_rebate_id PRIMARY KEY (id),
  CONSTRAINT fk_egpt_rebate_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_egpt_rebate_inst_id FOREIGN KEY (installment)
      REFERENCES eg_installment_master (id),
  CONSTRAINT fk_egpt_rebate_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_recovery;
CREATE TABLE egpt_recovery
(
  id bigint NOT NULL, -- Primary Key
  id_bill bigint NOT NULL, -- FK to eg_bill
  id_basic_property bigint NOT NULL, -- FK to egpt_basic_property
  id_status bigint NOT NULL, -- FK to egw_status
  id_state bigint, -- FK to eg_wf_states
  created_by bigint NOT NULL,
  modified_by bigint NOT NULL,
  created_date date NOT NULL,
  modified_date date NOT NULL,
  CONSTRAINT pk_egpt_recovery PRIMARY KEY (id),
  CONSTRAINT fk_recovery_basicproperty FOREIGN KEY (id_basic_property)
      REFERENCES egpt_basic_property (id)
);
COMMENT ON TABLE egpt_recovery IS 'Main table for Recovery';
COMMENT ON COLUMN egpt_recovery.id IS 'Primary Key';
COMMENT ON COLUMN egpt_recovery.id_bill IS 'FK to eg_bill';
COMMENT ON COLUMN egpt_recovery.id_basic_property IS 'FK to egpt_basic_property';
COMMENT ON COLUMN egpt_recovery.id_status IS 'FK to egw_status';
COMMENT ON COLUMN egpt_recovery.id_state IS 'FK to eg_wf_states';
-------------------END-------------------

------------------START------------------
CREATE TABLE egpt_recovery_cease_notice
(
  id bigint NOT NULL, -- Primary Key
  id_recovery bigint NOT NULL, -- FK to egpt_recovery
  id_notice bigint, -- FK to egpt_notice
  execution_date date NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint NOT NULL,
  created_date date NOT NULL,
  modified_date date NOT NULL,
  remarks character varying(1024),
  CONSTRAINT pk_egpt_seizure_notice PRIMARY KEY (id),
  CONSTRAINT fk_notice_seizure_notice FOREIGN KEY (id_notice)
      REFERENCES egpt_notice (id),
  CONSTRAINT fk_recovery_seizure_notice FOREIGN KEY (id_recovery)
      REFERENCES egpt_recovery (id)
);
COMMENT ON TABLE egpt_recovery_cease_notice IS 'Contains recovery cease details';
COMMENT ON COLUMN egpt_recovery_cease_notice.id IS 'Primary Key';
COMMENT ON COLUMN egpt_recovery_cease_notice.id_recovery IS 'FK to egpt_recovery';
COMMENT ON COLUMN egpt_recovery_cease_notice.id_notice IS 'FK to egpt_notice';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EGPT_RECOVERY_INTMATION;
CREATE TABLE egpt_recovery_intmation
(
  id bigint NOT NULL, -- Primary Key
  id_recovery bigint NOT NULL, -- FK to egpt_recovery
  id_notice bigint, -- FK to egpt_notice
  remarks character varying(1024),
  payment_due_date date NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint NOT NULL,
  created_date date NOT NULL,
  modified_date date NOT NULL,
  CONSTRAINT pk_egpt_notice_int PRIMARY KEY (id),
  CONSTRAINT fk_notice_int_notice FOREIGN KEY (id_notice)
      REFERENCES egpt_notice (id),
  CONSTRAINT fk_notice_int_recovery FOREIGN KEY (id_recovery)
      REFERENCES egpt_recovery (id)
);
COMMENT ON TABLE egpt_recovery_intmation IS 'contains recover intimation details';
COMMENT ON COLUMN egpt_recovery_intmation.id IS 'Primary Key';
COMMENT ON COLUMN egpt_recovery_intmation.id_recovery IS 'FK to egpt_recovery';
COMMENT ON COLUMN egpt_recovery_intmation.id_notice IS 'FK to egpt_notice';
-------------------END-------------------

------------------START------------------
create sequence seq_egpt_recovery_warrant;
CREATE TABLE egpt_recovery_warrant
(
  id bigint NOT NULL, -- Primary Key
  id_recovery bigint NOT NULL, -- FK to egpt_recovery
  id_notice bigint, -- FK to egpt_notice
  remarks character varying(1024),
  created_by bigint NOT NULL,
  modified_by bigint NOT NULL,
  created_date date NOT NULL,
  modified_date date NOT NULL,
  CONSTRAINT pk_egpt_warrant PRIMARY KEY (id),
  CONSTRAINT fk_warrant_notice FOREIGN KEY (id_notice)
      REFERENCES egpt_notice (id),
  CONSTRAINT fk_warrant_recovery FOREIGN KEY (id_recovery)
      REFERENCES egpt_recovery (id)
);
COMMENT ON TABLE egpt_recovery_warrant
  IS 'Contains recovery warrant details';
COMMENT ON COLUMN egpt_recovery_warrant.id IS 'Primary Key';
COMMENT ON COLUMN egpt_recovery_warrant.id_recovery IS 'FK to egpt_recovery';
COMMENT ON COLUMN egpt_recovery_warrant.id_notice IS 'FK to egpt_notice';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EGPT_RECOVERY_WRNT_NOTICE;
CREATE TABLE egpt_recovery_warrant_notice
(
  id bigint NOT NULL, -- Primary Key
  id_recovery bigint NOT NULL, -- FK to egpt_recovery
  id_notice bigint, -- FK to egpt_notice
  remarks character varying(1024),
  warrant_returnby_date date NOT NULL, -- Warrant Return Date
  warrant_cc_list character varying(1024),
  created_by bigint NOT NULL,
  modified_by bigint NOT NULL,
  created_date date NOT NULL,
  modified_date date NOT NULL,
  CONSTRAINT pk_egpt_warrant_notice PRIMARY KEY (id),
  CONSTRAINT fk_notice_warrant_notice FOREIGN KEY (id_notice)
      REFERENCES egpt_notice (id),
  CONSTRAINT fk_recovery_warrant_notice FOREIGN KEY (id_recovery)
      REFERENCES egpt_recovery (id)
);
COMMENT ON TABLE egpt_recovery_warrant_notice IS 'Contains recovery warrant notice details';
COMMENT ON COLUMN egpt_recovery_warrant_notice.id IS 'Primary Key';
COMMENT ON COLUMN egpt_recovery_warrant_notice.id_recovery IS 'FK to egpt_recovery';
COMMENT ON COLUMN egpt_recovery_warrant_notice.id_notice IS 'FK to egpt_notice';
COMMENT ON COLUMN egpt_recovery_warrant_notice.warrant_returnby_date IS 'Warrant Return Date';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EGPT_RECOVERY_WARRANTFEE;
CREATE TABLE egpt_recovery_warrantfee
(
  id bigint NOT NULL,
  id_warrant bigint NOT NULL, -- FK to EGPT_RECOVERY_WARRANT
  id_demand_reason bigint NOT NULL, -- FK to eg_demand_reason
  amount double precision NOT NULL,
  created_by bigint NOT NULL,
  modified_by bigint NOT NULL,
  created_date date NOT NULL,
  modified_date date NOT NULL,
  warrant_idx bigint NOT NULL, -- Warrant Fee List index
  CONSTRAINT pk_egpt_warrantfee PRIMARY KEY (id),
  CONSTRAINT fk_warrantfee_warrant FOREIGN KEY (id_warrant)
      REFERENCES egpt_recovery_warrant (id)
);
COMMENT ON TABLE egpt_recovery_warrantfee IS 'Contains recovery warrant fee details';
COMMENT ON COLUMN egpt_recovery_warrantfee.id_warrant IS 'FK to EGPT_RECOVERY_WARRANT';
COMMENT ON COLUMN egpt_recovery_warrantfee.id_demand_reason IS 'FK to eg_demand_reason';
COMMENT ON COLUMN egpt_recovery_warrantfee.warrant_idx IS 'Warrant Fee List index';
-------------------END-------------------

------------------START------------------
create sequence SEQ_EGPT_UNITCALC_DETAIL;
CREATE TABLE egpt_unitcalc_detail
(
  id bigint NOT NULL,
  id_property bigint NOT NULL,
  unit_number bigint NOT NULL,
  unit_area bigint NOT NULL,
  occupancy_date date NOT NULL,
  guidance_value bigint NOT NULL DEFAULT 0,
  guidance_val_efectivedate date NOT NULL,
  inst_fromdate date NOT NULL,
  unit_occupation character varying(255) NOT NULL,
  monthly_rent bigint NOT NULL DEFAULT 0,
  monthly_rent_tenant bigint NOT NULL DEFAULT 0,
  alv bigint NOT NULL DEFAULT 0,
  resd_alv bigint NOT NULL DEFAULT 0,
  nonresd_alv bigint NOT NULL DEFAULT 0,
  watertax_alv bigint NOT NULL DEFAULT 0,
  bigbldng_alv bigint NOT NULL DEFAULT 0,
  tax_payable bigint NOT NULL DEFAULT 0,
  taxdays bigint NOT NULL DEFAULT 0,
  from_date timestamp without time zone,
  general_tax bigint NOT NULL DEFAULT 0,
  gen_tax_fromdate timestamp without time zone,
  sewerage_tax bigint NOT NULL DEFAULT 0,
  seweragetax_fromdate timestamp without time zone,
  fire_tax bigint NOT NULL DEFAULT 0,
  firetax_fromdate timestamp without time zone,
  light_tax bigint NOT NULL DEFAULT 0,
  lighttax_fromdate timestamp without time zone,
  water_tax bigint NOT NULL DEFAULT 0,
  watertax_fromdate timestamp without time zone,
  educess_resd bigint NOT NULL DEFAULT 0,
  eduresd_fromdate timestamp without time zone,
  educess_nonresd bigint NOT NULL DEFAULT 0,
  edunonresd_fromdate timestamp without time zone,
  egc bigint NOT NULL DEFAULT 0,
  egc_fromdate timestamp without time zone,
  bigbldng_tax bigint NOT NULL DEFAULT 0,
  bigbldngtax_fromdate timestamp without time zone,
  chq_bounce_penalty bigint NOT NULL DEFAULT 0,
  penalty_fines bigint NOT NULL DEFAULT 0,
  created_timestamp timestamp without time zone NOT NULL,
  lastupdated_timestamp timestamp without time zone NOT NULL,
  service_charge bigint NOT NULL DEFAULT 0,
  building_cost bigint NOT NULL DEFAULT 0,
  CONSTRAINT pk_unittaxcalc_id PRIMARY KEY (id),
  CONSTRAINT fk_unittaxcalc_idprop FOREIGN KEY (id_property)
      REFERENCES egpt_property (id)
);
COMMENT ON TABLE egpt_unitcalc_detail  IS 'This table contains a row which gives the consolidated info of ALV and Annual Taxes';

CREATE INDEX idx_pt_ucd_id_prop ON egpt_unitcalc_detail USING btree (id_property);

-------------------END-------------------

------------------START------------------
create sequence SEQ_EGPT_UNITAREACALC_DETAIL;
CREATE TABLE egpt_unitareacalc_detail
(
  id bigint NOT NULL,
  id_unittax bigint NOT NULL,
  taxable_area bigint NOT NULL DEFAULT 0,
  monthly_baserent bigint NOT NULL DEFAULT 0,
  mrv bigint NOT NULL DEFAULT 0,
  unit_occupation character varying(200),
  unit_usage character varying(2),
  base_rent_sqrmtr double precision NOT NULL DEFAULT 0,
  manual_alv bigint NOT NULL DEFAULT 0,
  tenant_rent bigint NOT NULL DEFAULT 0,
  unit_identifier character varying(50),
  floor_number character varying(15),
  CONSTRAINT pk_unitareataxcalc_id PRIMARY KEY (id),
  CONSTRAINT fk_unitareataxcalc_id_unittax FOREIGN KEY (id_unittax)
      REFERENCES egpt_unitcalc_detail (id)
);

CREATE INDEX idx_pt_uacd_id_untax ON egpt_unitareacalc_detail USING btree (id_unittax);
-------------------END-------------------

------------------START------------------
CREATE SEQUENCE SEQ_EGPT_NOTICE_NUMBER;
CREATE SEQUENCE SEQ_EGPT_ASSESSMENT_NUMBER;
-------------------END-------------------

------------------START------------------
-------------- view : EGPT_MV_C1_COLL_RECEIPTS ----------------
CREATE VIEW  EGPT_MV_C1_COLL_RECEIPTS  AS select
  cr.id_demand_detail,
  cr.receipt_number,
  cr.receipt_date,
  cr.reason_amount,
  reas.id_demand_reason_master as reasmid,
  i.start_date as instlsdate,
  i.end_date as instedate
from
  egdm_collected_receipts cr,
  eg_demand_details dd,
  eg_installment_master i,
  eg_demand_reason reas
where
    cr.id_demand_detail = dd.id
and reas.id_installment = i.id
and dd.id_demand_reason = reas.id;

-------------- view : egpt_mv_c2_rcpt_tax_coll ----------------
create view egpt_mv_c2_rcpt_tax_coll as
select 
  receipt_number, 
  sum(reason_amount) as tax_coll
from 
  egpt_mv_c1_coll_receipts
where 
  reasmid = (select id from eg_demand_reason_master where reasonmaster='General Tax' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))         
group by receipt_number;

-------------- view : egpt_mv_c3_rcpt_penalty_coll ----------------
create view egpt_mv_c3_rcpt_penalty_coll as
select 
  receipt_number, 
  sum(reason_amount) as penalty_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='PENALTY_FINES' 
		and module=(select id from eg_module where name='Property Tax'))
and (instlsdate between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))  
group by receipt_number;

-------------- view : egpt_mv_c4_rcpt_librarycess_coll ----------------
create view egpt_mv_c4_rcpt_librarycess_coll as
select 
  receipt_number, 
  sum(reason_amount) as librarycess_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='Library Cess' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))   
group by receipt_number;

-------------- view : egpt_mv_c5_rcpt_arreartax_coll ----------------
create view egpt_mv_c5_rcpt_arreartax_coll as
select 
  receipt_number, 
  sum(reason_amount) as arreartax_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='General Tax' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))      
group by receipt_number;

-------------- view : egpt_mv_c6_rcpt_arrearpenalty_coll ----------------
create view egpt_mv_c6_rcpt_arrearpenalty_coll as
select 
  receipt_number, 
  sum(reason_amount) as arrearpenalty_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='PENALTY_FINES' 
		and module=(select id from eg_module where name='Property Tax'))
and (instlsdate not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))  
group by receipt_number;

-------------- view : egpt_mv_c7_rcpt_arrearlibrarycess_coll ----------------
create view egpt_mv_c7_rcpt_arrearlibrarycess_coll as
select 
  receipt_number, 
  sum(reason_amount) as arrearlibrarycess_coll
from 
  egpt_mv_c1_coll_receipts
where 
 reasmid = (select id from eg_demand_reason_master where reasonmaster='Library Cess' 
		and module=(select id from eg_module where name='Property Tax'))
and  (instlsdate not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)  
	and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE))   
group by receipt_number;

-------------- view : egpt_mv_c8_unique_rcpt_dd ----------------
create view egpt_mv_c8_unique_rcpt_dd as
select
  receipt_number,
  receipt_date,
  min(id_demand_detail) as id_dem_detail
from
  egpt_mv_c1_coll_receipts
group by receipt_number, receipt_date;

-------------- view : egpt_mv_c9_prop_coll_data ----------------
create view egpt_mv_c9_prop_coll_data as
select
  c8.receipt_number,
  c8.receipt_date,
  bp.propertyid,
  prop.id as idproperty,
  pid.zone_num as zoneid, pid.ward_adm_id as wardid, pid.adm1 as areaid, pid.adm2 as localityid, pid.adm3 as streetid, 
  ch.payeename, 
  ch.collectiontype,
  COALESCE(fit.type, 'cash', 'cash', 'bankchallan', 'bankchallan', 'card', 'card', 'online', 'online', 'atm', 'atm','banktobank','banktobank','cheque/dd') as payment_mode,
  u.username
from 
  egpt_mv_c8_unique_rcpt_dd c8,
  eg_demand_details dd,
  egpt_ptdemand ptd,
  egpt_property prop,
  egpt_basic_property bp,
  egpt_propertyid pid,
  egcl_collectionheader ch,
  egcl_collectioninstrument ci,
  egf_instrumentheader fih,
  egf_instrumenttype fit,
  eg_user u
where 
  c8.id_dem_detail = dd.id 
and dd.id_demand = ptd.id_demand
and ptd.id_property = prop.id
and prop.id_basic_property = bp.id
and bp.id_propertyid = pid.id
and (c8.receipt_number = ch.receiptnumber or c8.receipt_number = ch.manualreceiptnumber)
and ch.id = ci.collectionheader
and ci.instrumentheader = fih.id
and cast(fih.instrumenttype as bigint) = fit.id
and ch.createdby = u.id;

-------------- view : egpt_mv_c10_report ----------------
create view egpt_mv_c10_report as
select 
  c9.*, 
  c2.tax_coll, 
  c3.penalty_coll,
  c4.librarycess_coll,
  c5.arreartax_coll,
  c6.arrearpenalty_coll,
  c7.arrearlibrarycess_coll
from
  egpt_mv_c9_prop_coll_data c9 left outer join egpt_mv_c3_rcpt_penalty_coll c3 on c9.receipt_number = c3.receipt_number
  left outer join egpt_mv_c5_rcpt_arreartax_coll c5 on c9.receipt_number = c5.receipt_number
  left outer join egpt_mv_c6_rcpt_arrearpenalty_coll c6 on c9.receipt_number = c6.receipt_number
  left outer join egpt_mv_c7_rcpt_arrearlibrarycess_coll c7 on c9.receipt_number = c7.receipt_number,
  egpt_mv_c2_rcpt_tax_coll c2,
  egpt_mv_c4_rcpt_librarycess_coll c4
where
  c9.receipt_number = c2.receipt_number
  or c9.receipt_number = c4.receipt_number;
  
  
create or replace FUNCTION OWNERNAME(v_basicpropid IN BIGINT)  
RETURNS VARCHAR  as  $$
declare
	v_ownernames VARCHAR(3072);  
	owners eg_user%ROWTYPE;
BEGIN  
	for owners in (select u.* from egpt_property_owner_info po, eg_user u where po.owner=u.id and po.basicproperty = v_basicpropid)
	loop 
		begin
			IF v_ownernames <> '' THEN
			v_ownernames := v_ownernames || ',' || owners.name;
			ELSE
			v_ownernames := owners.name;
			END IF;
		EXCEPTION
		WHEN NO_DATA_FOUND THEN
		NULL;   
		END;
	END LOOP;
	return V_ownernames;   
END; 
$$ LANGUAGE plpgsql;

create or replace function mobilenumber(v_basicpropid IN BIGINT)  
RETURNS VARCHAR  as  $$
declare
	v_mobileno VARCHAR(12);  
	owners eg_user%ROWTYPE;
BEGIN  
	for owners in (select u.* from egpt_property_owner_info po, eg_user u where po.owner=u.id and po.basicproperty = v_basicpropid)
	loop 
		begin
			IF owners.mobilenumber <> null or owners.mobilenumber <> '' THEN
				v_mobileno := owners.mobilenumber;
				EXIT;
			END IF;
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
			NULL;   
		END;
	END LOOP;
	return v_mobileno;   
END; 
$$ LANGUAGE plpgsql;

------------ view egpt_mv_current_property ------------------ 
CREATE OR REPLACE VIEW egpt_mv_current_property AS
SELECT bp.id as id_basic_property,
    prop.id as id_property, prop.id_installment, prop.status, prop.isexemptedfromtax
FROM egpt_basic_property bp,
  egpt_property prop
WHERE prop.id_basic_property = bp.id
AND prop.is_default_property = 'Y'
AND prop.status in ('A', 'I', 'O') --taking Active or Inactive property, Inactive property is assessed or re-assessed it will become active after 15 days.
AND bp.isactive = 'Y';

------------ view egpt_mv_bp_inst_active_demand ------------------ 
CREATE OR REPLACE VIEW egpt_mv_bp_inst_active_demand AS
SELECT bp.id as id_basic_property,
       dem.id_installment,
	   dem.id id_demand
FROM egpt_basic_property bp,
  egpt_mv_current_property prop,
  eg_demand dem,
  egpt_ptdemand ptdem
WHERE prop.id_basic_property = bp.id
AND dem.is_history     = 'N'
AND ptdem.id_demand    = dem.id
AND ptdem.id_property  = prop.id_property;

------------ view egpt_mv_bp_curr_demand ------------------ 
CREATE OR REPLACE VIEW  egpt_mv_bp_curr_demand AS 
SELECT bp.ID AS id_basic_property,
	   admd.id_demand
FROM egpt_mv_bp_inst_active_demand admd, egpt_basic_property bp  
WHERE admd.id_basic_property = bp.id
and admd.id_installment = (SELECT id_installment
	          FROM eg_installment_master
              WHERE start_date <= now()
              AND end_date     >= now()
              AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'));
              
------------ view  egpt_mv_arrear_dem_coll ------------------  
CREATE OR REPLACE VIEW egpt_mv_arrear_dem_coll AS
SELECT dem.id_basic_property, 
    SUM(det.amount) AS amt_demand, 
    SUM(det.amt_collected) AS amt_collected
FROM 
    egpt_mv_bp_curr_demand dem LEFT OUTER JOIN eg_demand_details det ON (
        det.id_demand = dem.id_demand
        AND det.id_demand_reason IN (SELECT id FROM eg_demand_reason demand_reason 
                                    WHERE id_demand_reason_master not in (select id from eg_demand_reason_master where code in ('CHQ_BUNC_PENALTY', 'PENALTY_FINES') and module=(SELECT id FROM eg_module WHERE name='Property Tax'))
									and	 id_installment <> (SELECT id_installment
                                                            FROM eg_installment_master
                                                            WHERE start_date <= now()
                                                            AND end_date     >= now()
                                                            AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'))))
GROUP BY dem.id_basic_property;


------------ view egpt_mv_current_dem_coll ------------------
CREATE OR REPLACE VIEW egpt_mv_current_dem_coll AS
SELECT dem.id_basic_property, 
    SUM(det.amount) AS amt_demand, 
    SUM(det.amt_collected) AS amt_collected
FROM 
    egpt_mv_bp_curr_demand dem LEFT OUTER JOIN eg_demand_details det ON (
        det.id_demand = dem.id_demand
        AND det.id_demand_reason IN (SELECT id FROM eg_demand_reason demand_reason 
                                    WHERE id_demand_reason_master not in (select id from eg_demand_reason_master where code in ('CHQ_BUNC_PENALTY', 'PENALTY_FINES') and module=(SELECT id FROM eg_module WHERE name='Property Tax'))
									and	 id_installment = (SELECT id_installment
                                                            FROM eg_installment_master
                                                            WHERE start_date <= now()
                                                            AND end_date     >= now()
                                                            AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'))))
GROUP BY dem.id_basic_property;

------------ view  egpt_mv_bp_address ------------------
CREATE OR REPLACE VIEW egpt_mv_bp_address AS
SELECT bp.id as id_basic_property,
    addr.id as addressid,
    addr.housenobldgapt,
    CASE WHEN addr.housenobldgapt IS NOT NULL THEN addr.housenobldgapt ELSE '' END ||
        CASE WHEN addr.streetroadline IS NOT NULL THEN ', ' || addr.streetroadline ELSE '' END ||
        CASE WHEN addr.arealocalitysector IS NOT NULL THEN ', ' || addr.arealocalitysector ELSE '' END ||
        CASE WHEN addr.landmark IS NOT NULL THEN ', ' || addr.landmark ELSE '' END ||
        CASE WHEN addr.citytownvillage IS NOT NULL THEN ', ' || addr.citytownvillage ELSE '' END ||
        CASE WHEN addr.pincode IS NOT NULL THEN ' ' || addr.pincode ELSE '' END AS address
FROM 
    egpt_basic_property bp ,eg_address addr
	where bp.isactive='Y'
	and bp.addressid = addr.id;

------------ view egpt_mv_current_prop_det ------------------
CREATE OR REPLACE VIEW egpt_mv_current_prop_det AS
SELECT prop.id_property,
	propdet.id as id_property_detail,
	propdet.id_propertytypemaster,
	propdet.id_usg_mstr,
	propdet.sital_area,
	propdet.total_builtup_area
FROM egpt_mv_current_property prop,
	egpt_property_detail propdet
WHERE
	prop.id_property = propdet.id_property;


------------ view egpt_mv_dem_coll ------------------
CREATE OR REPLACE VIEW egpt_mv_current_arrear_dem_coll AS
SELECT currnt.id_basic_property            AS basicpropertyid,
    coalesce (currnt.amt_demand, 0)      AS aggregate_current_demand,
    coalesce (currnt.amt_collected, 0) AS current_collection,
    coalesce (arrear.amt_demand, 0)       AS aggregate_arrear_demand,
    coalesce (arrear.amt_collected, 0)  AS arrearcollection
FROM egpt_mv_current_dem_coll currnt,
    egpt_mv_arrear_dem_coll arrear
WHERE currnt.id_basic_property = arrear.id_basic_property;


------------ view egpt_mv_curr_dmdcalc ------------------
CREATE OR REPLACE VIEW egpt_mv_curr_dmdcalc AS
select dmdCal.id dmdCalId, 
      dmdcal.alv, 
      currDmd.id_demand dmdId, 
      currdmd.id_basic_property   
from egpt_mv_bp_curr_demand currDmd, 
    egpt_demandcalculations dmdCal 
where 
    currDmd.id_demand = dmdcal.id_demand;


------------ view egpt_mv_propertyInfo ------------------
CREATE OR REPLACE VIEW egpt_mv_propertyInfo AS
SELECT bp.id  							AS basicpropertyid,
    bp.propertyid                		AS upicno, 
    ownername (bp.id) 					AS ownersname,
    addr.housenobldgapt 				AS houseno,
    mobilenumber(bp.id)					AS mobileno,
    addr.address                        AS address,
    propdet.id_propertytypemaster       AS proptymaster,
    propdet.id_usg_mstr                 AS prop_usage_master,
    bp.id_adm_bndry                     AS wardid,
    propid.ZONE_NUM                     AS zoneid,
    propid.adm3                         AS streetid,
    propid.adm1				 			AS blockid,
    propid.adm2				 			AS localityid,
    1                                   AS source_id,
    propdet.sital_area                  AS sital_area,
    propdet.total_builtup_area          AS total_builtup_area,
    bp.status                           AS latest_status,
    dmdColl.aggregate_current_demand    AS aggregate_current_demand,
    dmdColl.aggregate_arrear_demand     AS aggregate_arrear_demand,
    dmdColl.current_collection          AS current_collection,
    dmdColl.arrearcollection            AS arrearcollection,
	bp.gis_ref_no						As gisRefNo,
	prop.isexemptedfromtax				As isexempted,
	bp.source							As source,
    case dmdcalc.alv
      when null then 0  
      else dmdcalc.alv 
     end  as ALV
  FROM egpt_basic_property bp,
    egpt_mv_current_property prop,
    egpt_mv_bp_address addr,
    egpt_mv_current_prop_det propdet,
    egpt_propertyid  propid,
    egpt_mv_current_arrear_dem_coll dmdColl,  
    egpt_mv_curr_dmdcalc dmdcalc 
  WHERE bp.propertyid is not null
  AND bp.addressid         = addr.addressid
  AND prop.id_basic_property = bp.id
  AND propdet.id_property      = prop.id_property
  AND propid.id                = bp.id_propertyid
  AND dmdColl.basicpropertyid   = bp.id
  AND dmdcalc.id_basic_property = bp.id;

------------ view egpt_mv_inst_dem_coll ------------------
CREATE or replace VIEW egpt_mv_inst_dem_coll
  AS
  SELECT id_basic_property, id_installment, max(CreatedDate) CreatedDate, 
  COALESCE(max(GeneralTax), null, 0, max(GeneralTax)) GeneralTax, 
  COALESCE(max(LibCessTax), null, 0, max(LibCessTax)) LibCessTax, 
  COALESCE(max(EduCessTax), null, 0, max(EduCessTax)) EduCessTax, 
  COALESCE(max(UnauthPenaltyTax) , null, 0, max(UnauthPenaltyTax)) UnauthPenaltyTax, 
  COALESCE(max(PenaltyFinesTax), null, 0, max(PenaltyFinesTax)) PenaltyFinesTax,  
  COALESCE(max(SewTax), null, 0, max(SewTax)) SewTax, 
  COALESCE(max(VacantLandTax), null, 0, max(VacantLandTax)) VacantLandTax, 
  COALESCE(max(PubSerChrgTax), null, 0, max(PubSerChrgTax)) PubSerChrgTax,
  COALESCE(max(GeneralTaxColl), null, 0, max(GeneralTaxColl)) GeneralTaxColl, 
  COALESCE(max(LibCessTaxColl), null, 0, max(LibCessTaxColl)) LibCessTaxColl, 
  COALESCE(max(EduCessTaxColl), null, 0, max(EduCessTaxColl)) EduCessTaxColl, 
  COALESCE(max(UnauthPenaltyTaxColl) , null, 0, max(UnauthPenaltyTaxColl)) UnauthPenaltyTaxColl, 
  COALESCE(max(PenaltyFinesTaxColl), null, 0, max(PenaltyFinesTaxColl)) PenaltyFinesTaxColl,  
  COALESCE(max(SewTaxColl), null, 0, max(SewTaxColl)) SewTaxColl, 
  COALESCE(max(VacantLandTaxColl), null, 0, max(VacantLandTaxColl)) VacantLandTaxColl, 
  COALESCE(max(PubSerChrgTaxColl), null, 0, max(PubSerChrgTaxColl)) PubSerChrgTaxColl
  FROM (
  SELECT currDem.id_basic_property id_basic_property,
    dr.id_installment id_installment,
   det.create_date CreatedDate,
    (case drm.code when 'GEN_TAX' then det.amount else null end) GeneralTax,
    (case drm.code when 'LIB_CESS' then det.amount else null end) LibCessTax,
    (case drm.code when 'EDU_CESS' then det.amount else null end) EduCessTax,
    (case drm.code when 'UNAUTH_PENALTY' then det.amount else null end) UnauthPenaltyTax,
    (case drm.code when 'PENALTY_FINES' then det.amount else null end) PenaltyFinesTax,
    (case drm.code when 'SEW_TAX' then det.amount else null end) SewTax,
    (case drm.code when 'VAC_LAND_TAX' then det.amount else null end) VacantLandTax,
    (case drm.code when 'PUB_SER_CHRG' then det.amount else null end) PubSerChrgTax,
    (case drm.code when 'GEN_TAX' then det.amt_collected else null end) GeneralTaxColl,
    (case drm.code when 'LIB_CESS' then det.amt_collected else null end) LibCessTaxColl,
    (case drm.code when 'EDU_CESS' then det.amt_collected else null end) EduCessTaxColl,
    (case drm.code when 'UNAUTH_PENALTY' then det.amt_collected else null end) UnauthPenaltyTaxColl,
    (case drm.code when 'PENALTY_FINES' then det.amt_collected else null end) PenaltyFinesTaxColl,
    (case drm.code when 'SEW_TAX' then det.amt_collected else null end) SewTaxColl,
    (case drm.code when 'VAC_LAND_TAX' then det.amt_collected else null end) VacantLandTaxColl,
    (case drm.code when 'PUB_SER_CHRG' then det.amt_collected else null end) PubSerChrgTaxColl
  FROM EGPT_MV_BP_CURR_DEMAND currDem,
    eg_demand_details det,
    eg_demand_reason dr,
    eg_demand_reason_master drm
  WHERE det.id_demand            = currDem.id_demand
  AND det.id_demand_reason       = dr.id
  AND dr.id_demand_reason_master = drm.id) as dmndColDtls
  GROUP BY id_basic_property,
    id_installment;
    
 -------------------END-------------------
