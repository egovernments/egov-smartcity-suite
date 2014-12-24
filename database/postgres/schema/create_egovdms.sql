

CREATE SEQUENCE egovdms.egcr_docmgmt_names_id_seq;
ALTER TABLE egovdms.egcr_docmgmt_names_id_seq OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;

CREATE SEQUENCE egovdms.egcr_ver_names_id_seq;
ALTER TABLE egovdms.egcr_ver_names_id_seq OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;

CREATE TABLE egovdms.egcr_docmgmt_binval
(
  binval_id character varying(64) NOT NULL,
  binval_data bytea NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_docmgmt_binval OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;

CREATE UNIQUE INDEX egcr_docmgmt_binval_idx
  ON egovdms.egcr_docmgmt_binval
  USING btree
  (binval_id);


CREATE TABLE egovdms.egcr_docmgmt_bundle
(
  node_id_hi bigint NOT NULL,
  node_id_lo bigint NOT NULL,
  bundle_data bytea NOT NULL,
  CONSTRAINT egcr_docmgmt_bundle_pkey PRIMARY KEY (node_id_hi, node_id_lo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_docmgmt_bundle OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;


CREATE TABLE egovdms.egcr_docmgmt_names
(
  id integer NOT NULL DEFAULT nextval('egovdms.egcr_docmgmt_names_id_seq'::regclass),
  "name" character varying(255) NOT NULL,
  CONSTRAINT egcr_docmgmt_names_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_docmgmt_names OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;


CREATE TABLE egovdms.egcr_docmgmt_refs
(
  node_id_hi bigint NOT NULL,
  node_id_lo bigint NOT NULL,
  refs_data bytea NOT NULL,
  CONSTRAINT egcr_docmgmt_refs_pkey PRIMARY KEY (node_id_hi, node_id_lo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_docmgmt_refs OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;


CREATE TABLE egovdms.egcr_ver_binval
(
  binval_id character varying(64) NOT NULL,
  binval_data bytea NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_ver_binval OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;


CREATE UNIQUE INDEX egcr_ver_binval_idx
  ON egovdms.egcr_ver_binval
  USING btree
  (binval_id);


CREATE TABLE egovdms.egcr_ver_bundle
(
  node_id_hi bigint NOT NULL,
  node_id_lo bigint NOT NULL,
  bundle_data bytea NOT NULL,
  CONSTRAINT egcr_ver_bundle_pkey PRIMARY KEY (node_id_hi, node_id_lo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_ver_bundle OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;


CREATE TABLE egovdms.egcr_ver_names
(
  id integer NOT NULL DEFAULT nextval('egovdms.egcr_ver_names_id_seq'::regclass),
  "name" character varying(255) NOT NULL,
  CONSTRAINT egcr_ver_names_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_ver_names OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;


CREATE TABLE egovdms.egcr_ver_refs
(
  node_id_hi bigint NOT NULL,
  node_id_lo bigint NOT NULL,
  refs_data bytea NOT NULL,
  CONSTRAINT egcr_ver_refs_pkey PRIMARY KEY (node_id_hi, node_id_lo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE egovdms.egcr_ver_refs OWNER TO erp_dms_owner@ERP_DMS_SUFFIX@;

--Synonym are created in create_synonym.sql
--CREATE SYNONYM egoverp.egcr_docmgmt_names_id_seq FOR egovdms.egcr_docmgmt_names_id_seq;
--CREATE SYNONYM egoverp.egcr_ver_names_id_seq FOR egovdms.egcr_ver_names_id_seq;
--CREATE SYNONYM egoverp.egcr_docmgmt_binval FOR egovdms.egcr_docmgmt_binval;
--CREATE SYNONYM egoverp.egcr_docmgmt_bundle FOR egovdms.egcr_docmgmt_bundle;
--CREATE SYNONYM egoverp.egcr_docmgmt_names FOR egovdms.egcr_docmgmt_names;
--CREATE SYNONYM egoverp.egcr_docmgmt_refs FOR egovdms.egcr_docmgmt_refs;
--CREATE SYNONYM egoverp.egcr_ver_binval FOR egovdms.egcr_ver_binval;
--CREATE SYNONYM egoverp.egcr_ver_bundle FOR egovdms.egcr_ver_bundle;
--CREATE SYNONYM egoverp.egcr_ver_names FOR egovdms.egcr_ver_names;
--CREATE SYNONYM egoverp.egcr_ver_refs FOR egovdms.egcr_ver_refs;

