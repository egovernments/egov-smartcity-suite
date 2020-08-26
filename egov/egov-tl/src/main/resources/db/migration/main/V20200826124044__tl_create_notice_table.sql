CREATE TABLE egtl_notice
(
  id bigint NOT NULL, -- Primary Key
  licenseid bigint, -- FK to EGTL_LICENSE
  noticetype character varying(32),
  noticeno character varying(64), 
  noticedate timestamp without time zone,
  filestore bigint,
  applicationnumber character varying(50),
  CONSTRAINT pk_egtl_notice PRIMARY KEY (id),
  CONSTRAINT fk_license_id FOREIGN KEY (licenseid) REFERENCES egtl_license (id),
  CONSTRAINT uniq_noticeno UNIQUE (noticeno)
 );

COMMENT ON TABLE egtl_notice IS 'Contains notice information for license';
COMMENT ON COLUMN egtl_notice.id IS 'Primary Key';
COMMENT ON COLUMN egtl_notice.licenseid IS 'FK to EGTL_LICENSE';
COMMENT ON COLUMN egtl_notice.noticetype IS 'Type of Notice';
COMMENT ON COLUMN egtl_notice.noticeno IS 'Notice Number';
COMMENT ON COLUMN egtl_notice.noticedate IS 'Notice generation date';
COMMENT ON COLUMN egtl_notice.applicationnumber IS 'Application Number';

CREATE SEQUENCE SEQ_EGTL_NOTICE;

create index idx_tlnotice_noticeno on egtl_notice(noticeno);
create index idx_tlnotice_licenseid on egtl_notice(licenseid);
create index idx_tlnotice_applno on egtl_notice(applicationnumber);
