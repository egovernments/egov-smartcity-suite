
CREATE SEQUENCE SEQ_EGDM_DEPRECIATIONMASTER;
CREATE TABLE EGDM_DEPRECIATIONMASTER  (	
    ID BIGINT NOT NULL, 
	YEAR BIGINT, 
	DEPRECIATION_PCT BIGINT NOT NULL, 
	MODULE BIGINT, 
	LASTMODIFIEDDATE DATE, 
	INSTALLMENT BIGINT NOT NULL, 
	IS_HISTORY CHAR(1) DEFAULT 'N', 
	USERID BIGINT, 
	DEPRECIATION_NAME character varying(50), 
	DEPRECIATION_TYPE character varying(25), 
	FROM_DATE TIMESTAMP without time zone, 
	TO_DATE TIMESTAMP without time zone, 
    CONSTRAINT PK_EGDM_DEPRECIATIONMASTER PRIMARY KEY(ID),
	CONSTRAINT FK_DEPREMSTR_INSTLMSTRSTR FOREIGN KEY (INSTALLMENT) REFERENCES EG_INSTALLMENT_MASTER (ID), 
	CONSTRAINT FK_DEPRECIATION_MODULE FOREIGN KEY (MODULE) REFERENCES EG_MODULE (ID), 
	CONSTRAINT FK_DEPREMSRTUID_USERID FOREIGN KEY (USERID) REFERENCES EG_USER (ID)
   );

   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.ID IS 'Primary Key';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.YEAR IS 'Not used.';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.DEPRECIATION_PCT IS 'Depreciation percentage';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.MODULE IS 'FK to eg_module';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.LASTMODIFIEDDATE IS '';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.INSTALLMENT IS 'FK to eg_installment_master';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.USERID IS 'Created by user';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.DEPRECIATION_NAME IS 'Depreciation Name';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.DEPRECIATION_TYPE IS 'Type of depreciation';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.FROM_DATE IS 'From date for depreciation percentage validity';
 
   COMMENT ON COLUMN EGDM_DEPRECIATIONMASTER.TO_DATE IS 'To date for depreciation percentage validity';
 
   COMMENT ON TABLE EGDM_DEPRECIATIONMASTER  IS 'Depreciation (age factor) configuration for property tax';

CREATE SEQUENCE seq_egpt_floor_type;

CREATE TABLE egpt_floor_type
(
 id BIGINT NOT NULL,
 "version" numeric default 0,
 name character varying(25),
 code character varying(10),
 createddate TIMESTAMP without time zone,
 lastmodifieddate TIMESTAMP without time zone,
 createdby BIGINT,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_floor_type PRIMARY KEY(id),
 CONSTRAINT fk_floor_type_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_floor_type_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id)
);


CREATE SEQUENCE seq_egpt_roof_type;

CREATE TABLE egpt_roof_type
(
 id BIGINT NOT NULL,
 "version" numeric default 0,
 name character varying(25),
 code character varying(10),
 createddate TIMESTAMP without time zone,
 lastmodifieddate TIMESTAMP without time zone,
 createdby BIGINT,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_roof_type PRIMARY KEY(id),
 CONSTRAINT fk_roof_type_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_roof_type_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id)
);

CREATE SEQUENCE seq_egpt_wall_type;
CREATE TABLE egpt_wall_type
(
 id BIGINT NOT NULL,
 "version" numeric default 0,
 name character varying(25),
 code character varying(10),
 createddate TIMESTAMP without time zone,
 lastmodifieddate TIMESTAMP without time zone,
 createdby BIGINT,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_wall_type PRIMARY KEY(id),
 CONSTRAINT fk_wall_type_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_wall_type_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id)
);

CREATE SEQUENCE seq_egpt_wood_type;
CREATE TABLE egpt_wood_type
(
 id BIGINT NOT NULL,
 "version" numeric default 0,
 name character varying(25),
 code character varying(10),
 createddate TIMESTAMP without time zone,
 lastmodifieddate TIMESTAMP without time zone,
 createdby BIGINT,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_wood_type PRIMARY KEY(id),
 CONSTRAINT fk_wood_type_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_wood_type_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id)
);

--EGPT_PROPERTYID
ALTER TABLE egpt_propertyid ADD COLUMN elect_bndry BIGINT;
ALTER TABLE egpt_propertyid ADD CONSTRAINT fk_eg_boundary_proprtyid FOREIGN KEY(elect_bndry) REFERENCES eg_boundary(id);

--EGPT_PROPERTY_DETAIL
ALTER TABLE egpt_property_detail ADD COLUMN lift boolean;
ALTER TABLE egpt_property_detail ADD COLUMN toilets boolean;
ALTER TABLE egpt_property_detail ADD COLUMN watertap boolean;
ALTER TABLE egpt_property_detail ADD COLUMN structure boolean;
ALTER TABLE egpt_property_detail ADD COLUMN drainage boolean;
ALTER TABLE egpt_property_detail ADD COLUMN electricity boolean;
ALTER TABLE egpt_property_detail ADD COLUMN attachedbathroom boolean;
ALTER TABLE egpt_property_detail ADD COLUMN waterharvesting boolean;
ALTER TABLE egpt_property_detail ADD COLUMN cable boolean;
ALTER TABLE egpt_property_detail ADD COLUMN extentsite DOUBLE PRECISION;
ALTER TABLE egpt_property_detail ADD COLUMN extent_appurtenant_land DOUBLE PRECISION;
ALTER TABLE egpt_property_detail ADD COLUMN siteowner character varying(50);
ALTER TABLE egpt_property_detail ADD COLUMN floortype BIGINT;
ALTER TABLE egpt_property_detail ADD COLUMN rooftype BIGINT;
ALTER TABLE egpt_property_detail ADD COLUMN walltype BIGINT;
ALTER TABLE egpt_property_detail ADD COLUMN woodtype BIGINT;

ALTER TABLE egpt_property_detail ADD CONSTRAINT fk_floortype_id FOREIGN KEY(floortype) REFERENCES egpt_floor_type(id);
ALTER TABLE egpt_property_detail ADD CONSTRAINT fk_rooftype_id FOREIGN KEY(rooftype) REFERENCES egpt_roof_type(id);
ALTER TABLE egpt_property_detail ADD CONSTRAINT fk_walltype_id FOREIGN KEY(walltype) REFERENCES egpt_wall_type(id);
ALTER TABLE egpt_property_detail ADD CONSTRAINT fk_woodtype_id FOREIGN KEY(woodtype) REFERENCES egpt_wood_type(id);

--EGPT_BASIC_PROPERTY
ALTER TABLE egpt_basic_property ADD COLUMN applicationno character varying(25);
ALTER TABLE egpt_basic_property ADD COLUMN vl_assessmentNo character varying(25);
ALTER TABLE egpt_basic_property RENAME prop_create_date to prop_occupancy_date;
ALTER TABLE egpt_basic_property ADD COLUMN regd_doc_no BIGINT;
ALTER TABLE egpt_basic_property ADD COLUMN regd_doc_date TIMESTAMP without time zone;
ALTER TABLE egpt_basic_property ADD COLUMN source character varying(30);

--EGPT_PROPERTY_STATUS_VALUES
ALTER TABLE egpt_property_status_values ADD COLUMN building_permission_no BIGINT;
ALTER TABLE egpt_property_status_values ADD COLUMN building_permission_date TIMESTAMP without time zone;
 
--EGPT_FLOOR_DETAIL
ALTER TABLE egpt_floor_detail ADD COLUMN capitalvalue character varying(25); 
ALTER TABLE egpt_floor_detail ADD COLUMN planapproved boolean;

--rollback ALTER TABLE egpt_floor_detail DROP COLUMN capitalvalue;
--rollback ALTER TABLE egpt_floor_detail DROP COLUMN planapproved;
--rollback ALTER TABLE egpt_property_detail DROP CONSTRAINT fk_floortype_id;
--rollback ALTER TABLE egpt_property_detail DROP CONSTRAINT fk_rooftype_id;
--rollback ALTER TABLE egpt_property_detail DROP CONSTRAINT fk_walltype_id;
--rollback ALTER TABLE egpt_property_detail DROP CONSTRAINT fk_woodtype_id;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN lift;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN toilets;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN watertap;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN structure;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN drainage;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN electricity;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN bathroomattached;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN waterharvesting;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN cable;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN extentsite;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN extent_appurtenant_land;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN siteowner;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN floor;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN roof;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN wall;
--rollback ALTER TABLE egpt_property_detail DROP COLUMN wood;
--rollback ALTER TABLE egpt_propertyid DROP COLUMN;
--rollback ALTER TABLE egpt_propertyid DROP CONSTRAINT fk_eg_boundary_proprtyid;
--rollback DROP TABLE egpt_wood_type;
--rollback DROP TABLE egpt_wall_type;
--rollback DROP TABLE egpt_roof_type;
--rollback DROP TABLE egpt_floor_type;
--rollback ALTER TABLE egpt_basic_property DROP COLUMN propapplicationno;
--rollback ALTER TABLE egpt_basic_property DROP COLUMN vacantland_asessment;
--rollback ALTER TABLE egpt_basic_property RENAME prop_occupancy_date to prop_create_date;
--rollback ALTER TABLE egpt_property_status_values DROP COLUMN building_permission_no;
--rollback ALTER TABLE egpt_property_status_values DROP COLUMN building_permission_date;
--rollback ALTER TABLE egpt_basic_property DROP COLUMN regdoc_no;
--rollback ALTER TABLE egpt_basic_property DROP COLUMN regdoc_date;
