------------------START------------------
CREATE TABLE egasset_asset_category
(
  id bigint NOT NULL,
  code character varying(256) NOT NULL,
  name character varying(256) NOT NULL,
  parentid bigint,
  maxlife bigint,
  asset_accountcode bigint NOT NULL,
  accdep_accountcode bigint,
  revaluation_accountcode bigint NOT NULL,
  depexp_accountcode bigint,
  depreciation_method character varying(50),
  asset_type character varying(50) NOT NULL,
  uom_id bigint NOT NULL,
  category_attribute_template character varying(4000),
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_assetcategory PRIMARY KEY (id),
  CONSTRAINT unq_assetcategory_code UNIQUE (code),
  CONSTRAINT fk_category_parent FOREIGN KEY (parentid)
      REFERENCES egasset_asset_category (id), 
  CONSTRAINT fk_asset_ac FOREIGN KEY (asset_accountcode)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_assetcat_accdep_ac FOREIGN KEY (accdep_accountcode)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_assetcat_reval_ac FOREIGN KEY (revaluation_accountcode)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_assetcat_depexp_ac FOREIGN KEY (depexp_accountcode)
      REFERENCES chartofaccounts (id),
  CONSTRAINT fk_assetcat_uom FOREIGN KEY (uom_id)
      REFERENCES eg_uom (id),
 CONSTRAINT fk_assetcat_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_assetcat_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id)
);

CREATE INDEX idx_assetcat_parentid ON egasset_asset_category USING btree (parentid);
CREATE INDEX idx_assetcat_ac ON egasset_asset_category USING btree (asset_accountcode);
CREATE INDEX idx_assetcat_reval_ac ON egasset_asset_category USING btree (revaluation_accountcode);
CREATE INDEX idx_assetcat_depexp_ac ON egasset_asset_category USING btree (depexp_accountcode);
CREATE INDEX idx_assetcat_accdep_ac ON egasset_asset_category USING btree (accdep_accountcode);
CREATE INDEX idx_assetcat_uomid ON egasset_asset_category USING btree (uom_id);

CREATE SEQUENCE seq_egasset_asset_category;
-------------------END-------------------

------------------START------------------
CREATE TABLE egasset_depreciation_metadata
(
  id bigint NOT NULL,
  assetcategory_id bigint,
  depreciation_rate bigint,
  financialyear_id bigint,  
  depmd_ac_index bigint,
  CONSTRAINT pk_depreciationmetadata PRIMARY KEY (id),
  CONSTRAINT fk_deprmetadata_assetcatid FOREIGN KEY (assetcategory_id)
      REFERENCES egasset_asset_category (id),
  CONSTRAINT fk_deprmetadata_finyearid FOREIGN KEY (financialyear_id)
      REFERENCES financialyear (id) 
);


CREATE INDEX idx_deprmetadata_assetcatid ON egasset_depreciation_metadata USING btree (assetcategory_id);
CREATE INDEX idx_deprmetadata_finyearid ON egasset_depreciation_metadata USING btree (financialyear_id);

CREATE SEQUENCE seq_egasset_depreciation_metadata;
-------------------END-------------------

------------------START------------------
CREATE TABLE egasset_asset
(
  id bigint NOT NULL,
  assetcategory_id bigint NOT NULL,
  code character varying(256) NOT NULL, 
  name character varying(256) NOT NULL,
  description character varying(1024),
  statusid bigint NOT NULL,
  departmentid bigint, 
  modeofacquisition character varying(1024), 
  ward_id bigint,
  area_id bigint,
  location_id bigint,
  street_id bigint,
  asset_details character varying(4000),  
  date_of_creation timestamp without time zone,
  remarks character varying(1024),
  preparedby bigint,
  length bigint,
  width bigint,
  total_area bigint,
  sourcepath character varying(150),
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_asset PRIMARY KEY (id),
  CONSTRAINT unq_asset_code UNIQUE (code),
  CONSTRAINT fk_asset_category FOREIGN KEY (assetcategory_id)
      REFERENCES egasset_asset_category (id),
  CONSTRAINT fk_asset_status FOREIGN KEY (statusid)
      REFERENCES egw_status (id),
  CONSTRAINT fk_asset_department FOREIGN KEY (departmentid)
      REFERENCES eg_department (id), 
  CONSTRAINT fk_boundary_ward FOREIGN KEY (ward_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_asset_area FOREIGN KEY (area_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_asset_location FOREIGN KEY (location_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_boundary_street FOREIGN KEY (street_id)
      REFERENCES eg_boundary (id),
  CONSTRAINT fk_asset_preparedby FOREIGN KEY (preparedby)
      REFERENCES egeis_employee (id),
  CONSTRAINT fk_asset_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_asset_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_asset_categoryid ON egasset_asset USING btree (assetcategory_id);
CREATE INDEX idx_asset_statusid ON egasset_asset USING btree (statusid);
CREATE INDEX idx_asset_departmentid ON egasset_asset USING btree (departmentid);
CREATE INDEX idx_asset_wardid ON egasset_asset USING btree (ward_id);
CREATE INDEX idx_asset_areaid ON egasset_asset USING btree (area_id);
CREATE INDEX idx_asset_locationid ON egasset_asset USING btree (location_id);
CREATE INDEX idx_asset_streetid ON egasset_asset USING btree (street_id);
CREATE INDEX idx_asset_preparedby ON egasset_asset USING btree (preparedby);

CREATE SEQUENCE seq_egasset_asset;
-------------------END-------------------

------------------START------------------
CREATE TABLE egasset_asset_activities
(
  id bigint NOT NULL,
  asset_id bigint NOT NULL,
  additionamount double precision,
  deductionamount double precision,
  activitydate timestamp without time zone NOT NULL,
  identifier character(1) NOT NULL, 
  description character varying(256),
  created_by bigint NOT NULL,
  modified_by bigint,
  created_date timestamp without time zone NOT NULL,
  modified_date timestamp without time zone,
  CONSTRAINT pk_asset_activities PRIMARY KEY (id),
  CONSTRAINT fk_asset_act_asset FOREIGN KEY (asset_id)
      REFERENCES egasset_asset (id),
  CONSTRAINT fk_asset_act_createdby FOREIGN KEY (created_by)
      REFERENCES eg_user (id),
  CONSTRAINT fk_asset_act_modifiedby FOREIGN KEY (modified_by)
      REFERENCES eg_user (id) 
);

CREATE INDEX idx_asset_activity ON egasset_asset_activities USING btree (asset_id);

CREATE SEQUENCE seq_egasset_asset_activities;
-------------------END-------------------

--rollback DROP SEQUENCE seq_egasset_asset_activities;
--rollback DROP TABLE egasset_asset_activities;

--rollback DROP SEQUENCE seq_egasset_asset;
--rollback DROP TABLE egasset_asset;

--rollback DROP SEQUENCE seq_egasset_depreciation_metadata;
--rollback DROP TABLE egasset_depreciation_metadata;

--rollback DROP SEQUENCE seq_egasset_asset_category;
--rollback DROP TABLE egasset_asset_category;

