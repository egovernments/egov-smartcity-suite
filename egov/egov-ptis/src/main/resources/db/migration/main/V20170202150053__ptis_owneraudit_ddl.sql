create sequence seq_egpt_owneraudit;
CREATE TABLE egpt_owneraudit
(
  id bigint PRIMARY KEY,
  id_basic_property bigint NOT NULL,
  id_user  bigint,
  doorno character varying(32),
  aadhaarno character varying(32),
  mobileno character varying(32),
  ownername character varying(100),
  gender character varying(32),
  emailid character varying(32),
  guardianrelation character varying(32),
  guardianname    character varying(100),
  createdby bigint,                       
  createddate timestamp without time zone,        
  lastmodifiedby bigint,                       
  lastmodifieddate timestamp without time zone,         
  version bigint               
);
COMMENT ON COLUMN egpt_owneraudit.id_basic_property IS 'id of egpt_basic_property';
COMMENT ON COLUMN egpt_owneraudit.id_user IS 'id of eg_user';
