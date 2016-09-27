
------------------START------------------
CREATE TABLE egmrs_act
(
  id bigint NOT NULL,
  name character varying(30) NOT NULL,
  description character varying(100),
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_act PRIMARY KEY (id)
);

COMMENT ON TABLE egmrs_act IS 'Master table for Marriage Law/Act';

COMMENT ON COLUMN egmrs_act.id IS 'Primary Key';
COMMENT ON COLUMN egmrs_act.name IS 'Name of the Marriage Law/Act';
COMMENT ON COLUMN egmrs_act.description IS 'More on Law/Act';
COMMENT ON COLUMN egmrs_act.version IS 'JPA for version value';
COMMENT ON COLUMN egmrs_act.createdby IS 'Audit field to capture created by';
COMMENT ON COLUMN egmrs_act.createddate IS 'Audit field to caputre created date';
COMMENT ON COLUMN egmrs_act.lastmodifiedby IS 'Audit field to caputre last modified by';
COMMENT ON COLUMN egmrs_act.lastmodifieddate IS 'Audit field to caputre last modified date';

CREATE SEQUENCE seq_egmrs_act;
-------------------END-------------------

------------------START------------------
CREATE TABLE egmrs_fee
(
  id bigint NOT NULL,
  criteria character varying(50) NOT NULL,
  fees double precision,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  fromdays  bigint,
  todays bigint,
  CONSTRAINT pk_egmrs_fee PRIMARY KEY (id)
);

COMMENT ON TABLE egmrs_fee IS 'Master table for Marriage Registration/Re-Issue fee';

COMMENT ON COLUMN egmrs_fee.id IS 'Primary Key';
COMMENT ON COLUMN egmrs_fee.criteria IS 'Condition for the fee';
COMMENT ON COLUMN egmrs_fee.fees IS 'Fees';
COMMENT ON COLUMN egmrs_fee.version IS 'JPA for version value';
COMMENT ON COLUMN egmrs_fee.createdby IS 'Audit field to capture created by';
COMMENT ON COLUMN egmrs_fee.createddate IS 'Audit field to caputre created date';
COMMENT ON COLUMN egmrs_fee.lastmodifiedby IS 'Audit field to caputre last modified by';
COMMENT ON COLUMN egmrs_fee.lastmodifieddate IS 'Audit field to caputre last modified date';

CREATE SEQUENCE seq_egmrs_fee;
-------------------END-------------------

------------------START------------------
CREATE TABLE egmrs_religion
(
  id bigint NOT NULL,
  name character varying(30) NOT NULL,
  description character varying(100),
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_religion PRIMARY KEY (id)
);

COMMENT ON TABLE egmrs_religion IS 'Master table for Religion';

COMMENT ON COLUMN egmrs_religion.id IS 'Primary Key';
COMMENT ON COLUMN egmrs_religion.name IS 'Religion';
COMMENT ON COLUMN egmrs_religion.description IS 'Description for Religion';
COMMENT ON COLUMN egmrs_religion.version IS 'JPA for version value';
COMMENT ON COLUMN egmrs_religion.createdby IS 'Audit field to capture created by';
COMMENT ON COLUMN egmrs_religion.createddate IS 'Audit field to caputre created date';
COMMENT ON COLUMN egmrs_religion.lastmodifiedby IS 'Audit field to caputre last modified by';
COMMENT ON COLUMN egmrs_religion.lastmodifieddate IS 'Audit field to caputre last modified date';


CREATE SEQUENCE seq_egmrs_religion;
-------------------END-------------------

------------------START------------------ remove his
CREATE TABLE egmrs_identityproof
(
  id bigint NOT NULL,
  photograph boolean,
  deaceaseddeathcertificate boolean,
  divorcecertificate boolean,
  schoolleavingcertificate boolean,
  birthcertificate boolean,
  passport boolean,
  rationcard boolean,
  msebbill boolean,
  telephonebill boolean,
  version bigint NOT NULL DEFAULT 1,
  CONSTRAINT pk_egmrs_identityproof PRIMARY KEY (id)
);


CREATE SEQUENCE seq_egmrs_identityproof;
-------------------END-------------------


------------------START------------------
CREATE TABLE egmrs_applicant
(
  id bigint NOT NULL,
  firstname character varying(30) NOT NULL,
  middlename character varying(20),
  lastname character varying(20),
  othername character varying(20),
  religion bigint,
  religionpractice character varying(10),
  ageinyears smallint ,
  ageinmonths smallint ,
  relationstatus character varying(15) ,
  residenceaddress character varying(256) NOT NULL,
  officeaddress character varying(256) NOT NULL,
  occupation character varying(60),
  mobileno character varying(15),
  email character varying(128),
  aadhaarno character varying(20),
  photo bytea ,
  signature bytea,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_applicant PRIMARY KEY (id),
  CONSTRAINT fk_applicantreligion FOREIGN KEY (religion) REFERENCES egmrs_religion (id) 
);

COMMENT ON TABLE egmrs_applicant IS 'Master table for Religion';

COMMENT ON COLUMN egmrs_applicant.id IS 'Primary Key';
COMMENT ON COLUMN egmrs_applicant.firstname IS 'First name of the applicant';
COMMENT ON COLUMN egmrs_applicant.middlename IS 'Middle name of the applicant';
COMMENT ON COLUMN egmrs_applicant.lastname IS 'Last name of the applicant';
COMMENT ON COLUMN egmrs_applicant.othername IS 'Other name of the applicant';
COMMENT ON COLUMN egmrs_applicant.religion IS 'Religion practices by the applicant';
COMMENT ON COLUMN egmrs_applicant.religionpractice IS 'Since from when applicant is practicing the religion';
COMMENT ON COLUMN egmrs_applicant.ageinyears IS 'Number of years of the age';
COMMENT ON COLUMN egmrs_applicant.ageinmonths IS 'Number of months of the age';
COMMENT ON COLUMN egmrs_applicant.relationstatus IS 'Present relation status';
COMMENT ON COLUMN egmrs_applicant.residenceaddress IS 'Residence address';
COMMENT ON COLUMN egmrs_applicant.officeaddress IS 'Office address';
COMMENT ON COLUMN egmrs_applicant.occupation IS 'Occupation';
COMMENT ON COLUMN egmrs_applicant.mobileno IS 'Mobile no';
COMMENT ON COLUMN egmrs_applicant.email IS 'e-mail address';
COMMENT ON COLUMN egmrs_applicant.aadhaarno IS 'Aadhaar no';
COMMENT ON COLUMN egmrs_applicant.photo IS 'Photo';
COMMENT ON COLUMN egmrs_applicant.signature IS 'Signature';
COMMENT ON COLUMN egmrs_applicant.version IS 'JPA for version value';
COMMENT ON COLUMN egmrs_applicant.lastname IS 'Audit field to capture created by';
COMMENT ON COLUMN egmrs_applicant.othername IS 'Audit field to caputre created date';
COMMENT ON COLUMN egmrs_applicant.lastmodifiedby IS 'Audit field to caputre last modified by';
COMMENT ON COLUMN egmrs_applicant.lastmodifieddate IS 'Audit field to caputre last modified date';

CREATE SEQUENCE seq_egmrs_applicant;

CREATE INDEX idx_app_religion ON egmrs_applicant  USING btree (religion);
-------------------END-------------------

------------------START------------------
CREATE TABLE egmrs_priest
(
  id bigint NOT NULL,
  firstname character varying(30) ,
  middlename character varying(20),
  lastname character varying(20),
  religion bigint,
  age smallint ,
  residenceaddress character varying(256),
  officeaddress character varying(256) ,
  mobileno character varying(15),
  email character varying(128),
  aadhaarno character varying(20),
  version bigint NOT NULL DEFAULT 1,
  createdby bigint,
  createddate timestamp without time zone,
  lastmodifiedby bigint ,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_priest PRIMARY KEY (id),
  CONSTRAINT fk_priest_religion FOREIGN KEY (religion) REFERENCES egmrs_religion (id) 
);

COMMENT ON TABLE egmrs_priest IS 'Table to capture Priest information';

COMMENT ON COLUMN egmrs_priest.id IS 'Primary Key';
COMMENT ON COLUMN egmrs_priest.firstname IS 'First name of the priest';
COMMENT ON COLUMN egmrs_priest.middlename IS 'Middle name of the priest';
COMMENT ON COLUMN egmrs_priest.lastname IS 'Last name of the priest';

COMMENT ON COLUMN egmrs_priest.religion IS 'Religion practices by the priest';

COMMENT ON COLUMN egmrs_priest.residenceaddress IS 'Residence address';
COMMENT ON COLUMN egmrs_priest.officeaddress IS 'Office address';
COMMENT ON COLUMN egmrs_priest.mobileno IS 'Mobile no';
COMMENT ON COLUMN egmrs_priest.email IS 'e-mail address';
COMMENT ON COLUMN egmrs_priest.aadhaarno IS 'Aadhaar no';
COMMENT ON COLUMN egmrs_priest.version IS 'JPA for version value';
 

COMMENT ON COLUMN egmrs_priest.lastmodifiedby IS 'Audit field to caputre last modified by';
COMMENT ON COLUMN egmrs_priest.lastmodifieddate IS 'Audit field to caputre last modified date';

CREATE SEQUENCE seq_egmrs_priest;

CREATE INDEX idx_priest_religion ON egmrs_priest  USING btree (religion);
-------------------END-------------------

------------------START------------------
CREATE TABLE egmrs_registration
(
  id bigint NOT NULL,
  applicationno character varying(25) NOT NULL,
  applicationdate timestamp without time zone NOT NULL,
  registrationno character varying(25),
  dateofmarriage timestamp without time zone NOT NULL,
  marriageact bigint NOT NULL,
  placeofmarriage character varying(30) NOT NULL,
  husband bigint NOT NULL,
  wife bigint NOT NULL,
  priest bigint ,
  couplefromsameplace boolean,
  memorandumofmarriage boolean,
  courtfeestamp boolean,
  affidavit boolean,
  marriagecard boolean,
  feecriteria character varying(50) NOT NULL,
  feepaid double precision NOT NULL,
  "document" bigint ,
  state_id bigint NOT NULL,
  remarks character varying(256), -- size
  rejectionreason character varying(256), --size
  status character varying(30) NOT NULL, --size
  "zone" bigint NOT NULL, -- fk to eg_boundary
  demand bigint NOT NULL, -- fk to eg_demand
  certificateissued boolean DEFAULT false,
  "version" bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_registration PRIMARY KEY (id),
  CONSTRAINT fk_egmrs_reg_husband FOREIGN KEY (husband)  REFERENCES egmrs_applicant (id),
  CONSTRAINT fk_egmrs_reg_wife FOREIGN KEY (wife)     REFERENCES egmrs_applicant (id),
  CONSTRAINT fk_marriageact FOREIGN KEY (marriageact) REFERENCES egmrs_act (id) ,  
  CONSTRAINT fk_reg_priest FOREIGN KEY (priest) REFERENCES egmrs_priest (id) ,
  CONSTRAINT fk_reg_state FOREIGN KEY (state_id) REFERENCES eg_wf_states (id) ,
  CONSTRAINT fk_reg_zone FOREIGN KEY (zone) REFERENCES eg_boundary (id) ,
  CONSTRAINT fk_reg_demand FOREIGN KEY (demand) REFERENCES eg_demand (id) 
);

CREATE SEQUENCE seq_egmrs_registration;
-------------------END-------------------


------------------START------------------
CREATE TABLE egmrs_witness
(
  id bigint NOT NULL,
  firstname character varying(30) NOT NULL,
  middlename character varying(20),
  lastname character varying(20),
  occupation character varying(60),
  relationshipwithapplicant character varying(30),
  age smallint NOT NULL,
  residenceaddress character varying(256) NOT NULL,
  officeaddress character varying(256) NOT NULL,
  mobileno character varying(15),
  email character varying(128),
  aadhaarno character varying(20),
  photo bytea,
  signature bytea,
  registration bigint NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_witness PRIMARY KEY (id),
  CONSTRAINT fk_witness_reg FOREIGN KEY (registration)
      REFERENCES egmrs_registration (id) 
);


CREATE SEQUENCE seq_egmrs_witness;
CREATE INDEX idx_witness_reg ON egmrs_witness  USING btree (registration);
-------------------END-------------------


CREATE TABLE egmrs_proofdocs(
  document bigint not null,
  filestore bigint not null
);

CREATE TABLE egmrs_document
(
  id bigint NOT NULL,
  name character varying(200) NOT NULL,
  code character varying(100) NOT NULL,
  individual boolean DEFAULT false,
  type character varying(15) NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_document PRIMARY KEY (id)
);

create sequence seq_egmrs_document;


---------------------------------------------------
CREATE TABLE egmrs_applicantdocument
(
  id bigint NOT NULL,
  applicant bigint NOT NULL,
  document bigint NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  CONSTRAINT pk_egmrs_appldoc PRIMARY KEY (id),
  CONSTRAINT fk_appldoc_applicant FOREIGN KEY (applicant)   REFERENCES egmrs_applicant (id)     ,
  CONSTRAINT fk_appldoc_document FOREIGN KEY (document)     REFERENCES egmrs_document (id)       
);
CREATE SEQUENCE SEQ_EGMRS_APPLICANTDOCUMENT;
---------------------------------------------------

---------------------------------------------------

CREATE TABLE egmrs_registrationdocument
(
  id bigint NOT NULL,
  registration bigint NOT NULL,
  document bigint NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  CONSTRAINT pk_egmrs_regdoc PRIMARY KEY (id),
  CONSTRAINT fk_regdoc_document FOREIGN KEY (document)      REFERENCES egmrs_document (id)       ,
  CONSTRAINT fk_regdoc_reg FOREIGN KEY (registration)      REFERENCES egmrs_registration (id)      
);

CREATE SEQUENCE SEQ_EGMRS_REGISTRATIONDOCUMENT;
---------------------------------------------------
CREATE SEQUENCE sq_egmrs_registrationno;

CREATE SEQUENCE SEQ_EGMRS_REISSUE;

CREATE TABLE egmrs_reissue
(
  id bigint NOT NULL,
  applicationno character varying(25) NOT NULL,
  applicationdate timestamp NOT NULL,
  reissuedate timestamp,
  applicant bigint NOT NULL,
  registration bigint NOT NULL,
  feecriteria character varying(50) NOT NULL,
  feepaid double precision NOT NULL,  
  state_id bigint NOT NULL,
  remarks character varying(256), -- size
  rejectionreason character varying(256), --size
  status character varying(30) NOT NULL, --size  
  demand bigint NOT NULL, -- fk to eg_demand
  certificateissued boolean DEFAULT false,
  "version" bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egmrs_reissue PRIMARY KEY (id),  
  CONSTRAINT fk_reissue_applicant FOREIGN KEY (applicant) REFERENCES egmrs_applicant (id) ,
  CONSTRAINT fk_reissue_reg FOREIGN KEY (registration) REFERENCES egmrs_registration (id) ,
  CONSTRAINT fk_reissue_state FOREIGN KEY (state_id) REFERENCES eg_wf_states (id) ,
  CONSTRAINT fk_reissue_demand FOREIGN KEY (demand) REFERENCES eg_demand (id) 
);

ALTER TABLE egmrs_applicant add COLUMN proofsAttached bigint;
ALTER TABLE egmrs_applicant add  CONSTRAINT fk_application_identyProf FOREIGN KEY (proofsAttached) REFERENCES egmrs_identityproof (id) ;


