-------------MASTERS-------------
------------START-----------------
CREATE TABLE eglc_advocate_master
(
  id bigint NOT NULL,
  salutation  character varying(10) NOT NULL,
  name character varying (250) NOT NULL,
  address character varying(256),
  contactphone character varying(256),
  specilization character varying(256),
  mobilenumber character varying(10),
  email  character varying(50),
  monthlyrenumeration numeric(15,2),
  isretaineradvocate boolean  NOT NULL,
  firmname  character varying(50),
  pannumber character varying(20),
  paymentmode character varying(20),
  bankaccount character varying(50),
  bankname bigint,
  ifsccode character varying(11),
  tinumber character varying(11),
  fee double precision,
  bankbranch bigint,
  isactive  boolean  NOT NULL,
  issenioradvocate boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint  NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_advocate_master PRIMARY KEY (id),
  CONSTRAINT unq_advocate_name UNIQUE (name),
  CONSTRAINT unq_advocate_mobilenumber UNIQUE (mobilenumber),
  CONSTRAINT unq_advocate_pannumber UNIQUE (pannumber),
  CONSTRAINT fk_advocate_bankname FOREIGN KEY (bankname) REFERENCES bank (id),
  CONSTRAINT fk_advocate_bankbranch FOREIGN KEY (bankbranch) REFERENCES bankbranch (id)

);
CREATE SEQUENCE seq_eglc_advocate_master;
CREATE INDEX idx_advocate_bankname ON eglc_advocate_master(bankname);
CREATE INDEX idx_advocate_bankbranch ON eglc_advocate_master(bankbranch);

COMMENT ON TABLE eglc_advocate_master IS 'Advocate Master table';
COMMENT ON COLUMN eglc_advocate_master.id IS 'Primary Key';
COMMENT ON COLUMN eglc_advocate_master.name IS 'Name of Advocate';
COMMENT ON COLUMN eglc_advocate_master.address IS 'Address of Advocate';
COMMENT ON COLUMN eglc_advocate_master.contactphone IS 'Phone Number';
COMMENT ON COLUMN eglc_advocate_master.specilization IS 'Advocate Speciality';
COMMENT ON COLUMN eglc_advocate_master.mobilenumber IS 'Mobile Number';
COMMENT ON COLUMN eglc_advocate_master.email IS 'Email';
COMMENT ON COLUMN eglc_advocate_master.monthlyrenumeration IS 'Monthly Fees';
COMMENT ON COLUMN eglc_advocate_master.isretaineradvocate IS 'Is Retainer Advocate?';
COMMENT ON COLUMN eglc_advocate_master.firmname IS 'FIR Name';
COMMENT ON COLUMN eglc_advocate_master.pannumber IS 'PAN Number';
COMMENT ON COLUMN eglc_advocate_master.isactive IS 'Is Active?';
COMMENT ON COLUMN eglc_advocate_master.issenioradvocate IS 'Is Senior Advocate?';
COMMENT ON COLUMN eglc_advocate_master.salutation IS 'Mr./Mrs./Shri ';
COMMENT ON COLUMN eglc_advocate_master.paymentmode IS 'Payment Mode';
COMMENT ON COLUMN eglc_advocate_master.bankname IS 'Name of Bank holding the account. Foreign Key Bank';
COMMENT ON COLUMN eglc_advocate_master.bankaccount IS 'Bank Account Number';
COMMENT ON COLUMN eglc_advocate_master.ifsccode IS 'IFSC Code of Bank for Bank to Bank Payment.';
COMMENT ON COLUMN eglc_advocate_master.tinumber IS 'TIN Number of the Legal Firm(if any)';
COMMENT ON COLUMN eglc_advocate_master.fee IS 'Fee';
COMMENT ON COLUMN eglc_advocate_master.bankbranch IS 'Foreign Key BANKBRANCH';
COMMENT ON COLUMN eglc_advocate_master.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_advocate_master.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_advocate_master.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_advocate_master.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_advocate_master.version IS 'Version';
------------------------END--------------------------------------
-----------------------START-------------------------------------
CREATE TABLE eglc_casetype_master
(
  id bigint NOT NULL,
  code character varying(50)  NOT NULL,
  casetype character varying(100) NOT NULL,
  notes   character varying(255),
  ordernumber numeric,
  active boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_casetype_master PRIMARY KEY (id),
  CONSTRAINT unq_casetype_case_type UNIQUE (casetype),
  CONSTRAINT unq_casetype_code UNIQUE (code)
);
CREATE SEQUENCE seq_eglc_casetype_master;

COMMENT ON TABLE eglc_casetype_master IS 'casetype master table';
COMMENT ON COLUMN eglc_casetype_master.id IS 'Primary Key';
COMMENT ON COLUMN eglc_casetype_master.casetype IS 'Case Type';
COMMENT ON COLUMN eglc_casetype_master.notes IS 'Notes';
COMMENT ON COLUMN eglc_casetype_master.ordernumber IS 'Order Number for Sorting';
COMMENT ON COLUMN eglc_casetype_master.code IS 'Code for Case Type';
COMMENT ON COLUMN eglc_casetype_master.active IS 'ISActive?';
COMMENT ON COLUMN eglc_casetype_master.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_casetype_master.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_casetype_master.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_casetype_master.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_casetype_master.version IS 'Version';
------------------------END--------------------------------------
-----------------------START-------------------------------------
CREATE TABLE eglc_case_stage
(  
   id bigint NOT NULL,
   stage character varying(100) NOT NULL,
   active boolean NOT NULL,
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_case_stage PRIMARY KEY (id)
);
CREATE SEQUENCE seq_eglc_case_stage;

COMMENT ON TABLE eglc_case_stage IS 'case stage master table';
COMMENT ON COLUMN eglc_case_stage.id IS 'Primary Key';
COMMENT ON COLUMN eglc_case_stage.stage IS 'Legal Case Stage';
COMMENT ON COLUMN eglc_case_stage.active IS 'ISActive?';
COMMENT ON COLUMN eglc_case_stage.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_case_stage.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_case_stage.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_case_stage.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_case_stage.version IS 'Version';
------------------------END---------------------------------------
-----------------------START-------------------------------------
CREATE TABLE eglc_courttype_master
(
  id bigint  NOT NULL,
  code character varying(12) NOT NULL,
  courttype character varying(256) NOT NULL,
  notes   character varying(256),
  ordernumber  numeric,
  active boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_courttype_master PRIMARY KEY (id),
  CONSTRAINT unq_courttype_courttype UNIQUE (courttype),
  CONSTRAINT unq_courttype_code UNIQUE (code)
);
CREATE SEQUENCE seq_eglc_courttype_master;

COMMENT ON TABLE eglc_courttype_master IS 'case stage master table';
COMMENT ON COLUMN eglc_courttype_master.id IS 'Primary Key';
COMMENT ON COLUMN eglc_courttype_master.courttype IS 'Court type';
COMMENT ON COLUMN eglc_courttype_master.notes IS 'Notes';
COMMENT ON COLUMN eglc_courttype_master.ordernumber IS 'Order Number for Sorting';
COMMENT ON COLUMN eglc_courttype_master.code IS 'Code of Court Type';
COMMENT ON COLUMN eglc_courttype_master.active IS 'ISActive?';
COMMENT ON COLUMN eglc_courttype_master.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_courttype_master.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_courttype_master.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_courttype_master.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_courttype_master.version IS 'Version';
------------------------END--------------------------------------
-----------------------START-------------------------------------
CREATE TABLE eglc_court_master
(
  id  bigint NOT NULL,
  name  character varying(256) NOT NULL,
  address character varying(256),
  courttype bigint NOT NULL,
  ordernumber  numeric,
  active boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_court_master PRIMARY KEY (id),
  CONSTRAINT unq_court_name UNIQUE (name),
  CONSTRAINT fk_court_courttype FOREIGN KEY (courttype) REFERENCES eglc_courttype_master (id)
);
CREATE SEQUENCE SEQ_eglc_court_master;
CREATE INDEX idx_court_courttype ON eglc_court_master(courttype);

COMMENT ON TABLE eglc_court_master IS 'court master table';
COMMENT ON COLUMN eglc_court_master.id IS 'Primary Key';
COMMENT ON COLUMN eglc_court_master.name IS 'Name of the Court';
COMMENT ON COLUMN eglc_court_master.address IS 'Address of the Court';
COMMENT ON COLUMN eglc_court_master.courttype IS 'Foreign Key of EGLC_COURTTYPE_MASTER';
COMMENT ON COLUMN eglc_court_master.ordernumber IS 'Order Number for Sorting';
COMMENT ON COLUMN eglc_court_master.active IS 'ISActive?';
COMMENT ON COLUMN eglc_court_master.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_court_master.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_court_master.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_court_master.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_court_master.version IS 'Version';
------------------------END---------------------------------------
-----------------------START-------------------------------------
CREATE TABLE eglc_governmentdepartment
(
  id bigint NOT NULL,
  code character varying(50),
  name character varying(256),
  description character varying(256),
  ordernumber numeric,
  active boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_governmentdepartment PRIMARY KEY (id),
  CONSTRAINT unq_governmentdepartment_name UNIQUE (name)
);
CREATE SEQUENCE seq_eglc_governmentdepartment;

COMMENT ON TABLE eglc_governmentdepartment IS 'government department master table';
COMMENT ON COLUMN eglc_governmentdepartment.id IS 'Primary Key';
COMMENT ON COLUMN eglc_governmentdepartment.code IS 'Code of Government Department';
COMMENT ON COLUMN eglc_governmentdepartment.name IS 'Name of Government Department';
COMMENT ON COLUMN eglc_governmentdepartment.description IS 'Description of Government Department';
COMMENT ON COLUMN eglc_governmentdepartment.ordernumber IS 'Order Number for Sorting';
COMMENT ON COLUMN eglc_governmentdepartment.active IS 'ISActive?';
COMMENT ON COLUMN eglc_governmentdepartment.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_governmentdepartment.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_governmentdepartment.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_governmentdepartment.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_governmentdepartment.version IS 'Version';
------------------------END---------------------------------------
-----------------------START-------------------------------------
CREATE TABLE eglc_interimtype_master
(
  id bigint  NOT NULL,
  code character varying(12) NOT NULL,
  interimordertype character varying(100) NOT NULL,
  description character varying(256),
  ordernumber numeric,
  active boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_interimtype_master PRIMARY KEY (id),
  CONSTRAINT unq_interimtype_code UNIQUE (code),
  CONSTRAINT unq_interimtype_interimordertype UNIQUE (interimordertype)
 );
CREATE SEQUENCE seq_eglc_interimtype_master;

COMMENT ON TABLE eglc_interimtype_master IS 'Interimtype master table';
COMMENT ON COLUMN eglc_interimtype_master.id IS 'Primary Key';
COMMENT ON COLUMN eglc_interimtype_master.interimordertype IS 'Interim Order Type';
COMMENT ON COLUMN eglc_interimtype_master.description IS 'Description of Interim Order Type';
COMMENT ON COLUMN eglc_interimtype_master.code IS 'Code of Interim Order Type';
COMMENT ON COLUMN eglc_interimtype_master.ordernumber IS 'Order Number for Sorting';
COMMENT ON COLUMN eglc_interimtype_master.active IS 'ISActive?';
COMMENT ON COLUMN eglc_interimtype_master.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_interimtype_master.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_interimtype_master.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_interimtype_master.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_interimtype_master.version IS 'Version';
------------------------END---------------------------------------
-----------------------START-------------------------------------
CREATE TABLE eglc_judgmenttype_master
(
  id bigint NOT NULL,
  code character varying(50) NOT NULL,
  judgmenttype  character varying(50) NOT NULL,
  description   character varying(256),
  ordernumber numeric  ,
  active boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_judgmenttype_master PRIMARY KEY (id),
  CONSTRAINT unq_judgmenttype_code UNIQUE (code),
  CONSTRAINT unq_judgmenttype_judgmenttype UNIQUE (judgmenttype)
);
CREATE SEQUENCE seq_eglc_judgmenttype_master;

COMMENT ON TABLE eglc_judgmenttype_master IS 'Judgmenttype master table';
COMMENT ON COLUMN eglc_judgmenttype_master.id IS 'Primary Key';
COMMENT ON COLUMN eglc_judgmenttype_master.judgmenttype IS 'Judgment Type Name';
COMMENT ON COLUMN eglc_judgmenttype_master.description IS 'Judgment Type Description';
COMMENT ON COLUMN eglc_judgmenttype_master.code IS 'Judgment Type Code';
COMMENT ON COLUMN eglc_judgmenttype_master.ordernumber IS 'Order Number for Sorting';
COMMENT ON COLUMN eglc_judgmenttype_master.active IS 'ISActive?';
COMMENT ON COLUMN eglc_judgmenttype_master.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_judgmenttype_master.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_judgmenttype_master.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_judgmenttype_master.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_judgmenttype_master.version IS 'Version';
------------------------END---------------------------------------
-----------------------START-------------------------------------
CREATE TABLE  eglc_petitiontype_master
(
  id bigint  NOT NULL,
  code character varying(50)  NOT NULL,
  petitiontype character varying(100) NOT NULL,
  courttype bigint,
  ordernumber numeric,
  active boolean NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_petitiontype_master PRIMARY KEY (id),
  CONSTRAINT unq_petitiontype_code UNIQUE (code),
  CONSTRAINT fk_petitiontype_courttype FOREIGN KEY (courttype) REFERENCES eglc_courttype_master (id)
);
CREATE SEQUENCE seq_eglc_petitiontype_master;
CREATE INDEX idx_petitiontype_courttype ON eglc_petitiontype_master(courttype);

COMMENT ON TABLE eglc_petitiontype_master IS 'Judgmenttype master table';
COMMENT ON COLUMN eglc_petitiontype_master.id IS 'Primary Key';
COMMENT ON COLUMN eglc_petitiontype_master.code IS 'Code of Petition Type';
COMMENT ON COLUMN eglc_petitiontype_master.petitiontype IS 'Name of Petition Type';
COMMENT ON COLUMN eglc_petitiontype_master.courttype IS 'Foreign Key of EGLC_COURTTYPE_MASTER';
COMMENT ON COLUMN eglc_petitiontype_master.ordernumber IS 'Order Number for Sorting';
COMMENT ON COLUMN eglc_petitiontype_master.active IS 'ISActive?';
COMMENT ON COLUMN eglc_petitiontype_master.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_petitiontype_master.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_petitiontype_master.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_petitiontype_master.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_petitiontype_master.version IS 'Version';
------------------------END---------------------------------------
----------------------TRANSACTIONS---------------------------------
-----------------------START---------------------------------------
CREATE TABLE eglc_legalcase
(
  id bigint NOT NULL, 
  casenumber character varying(50) NOT NULL, 
  casedate date, 
  casetitle character varying(1024) NOT NULL, 
  appealnum  character varying(50), 
  court bigint NOT NULL, 
  casetype bigint NOT NULL, 
  remarks character varying(1024), 
  status bigint NOT NULL, 
  casereceivingdate date, 
  isfiledbycorporation boolean NOT NULL,
  lcnumber character varying(50) NOT NULL, 
  isrespondentgovernment boolean NOT NULL, 
  respondentgovtdept bigint, 
  prayer character varying(1024) NOT NULL, 
  issenioradvrequired boolean NOT NULL, 
  petitiontype bigint NOT NULL, 
  assigntoidboundary numeric, 
  functionary bigint, 
  opppartyadvocate character varying(128), 
  representedby character varying(256), 
  lcnumbertype character varying(256) NOT NULL,  
  casefirstappearancedate date, 
  previousdate date, 
  nextdate date, 
  petitionerappearancedate date, 
  stampnumber character varying(50), 
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_legalcase PRIMARY KEY (id),
  CONSTRAINT unq_legalcase_casenumber UNIQUE (casenumber),
  CONSTRAINT unq_legalcase_lcnumber UNIQUE (lcnumber),
  CONSTRAINT fk_legalcase_status FOREIGN KEY (status) REFERENCES egw_status (id), 
  CONSTRAINT fk_legalcase_casetype FOREIGN KEY (casetype) REFERENCES eglc_casetype_master (id), 
  CONSTRAINT fk_legalcase_court FOREIGN KEY (court) REFERENCES eglc_court_master (id), 
  CONSTRAINT fk_legalcase_respondentgovtdept FOREIGN KEY (respondentgovtdept) REFERENCES eglc_governmentdepartment (id), 
  CONSTRAINT fk_legalcase_petition FOREIGN KEY (petitiontype) REFERENCES eglc_petitiontype_master (id), 
  CONSTRAINT fk_legalcase_functionary FOREIGN KEY (functionary) REFERENCES functionary (id) 
);
CREATE SEQUENCE seq_eglc_legalcase;
CREATE INDEX idx_legalcase_status ON eglc_legalcase (status);
CREATE INDEX idx_legalcase_casetype ON eglc_legalcase (casetype); 
CREATE INDEX idx_legalcase_court ON eglc_legalcase (court);
CREATE INDEX idx_legalcase_respondentgovtdept ON eglc_legalcase (respondentgovtdept);
CREATE INDEX idx_legalcase_petition ON eglc_legalcase (petitiontype);
CREATE INDEX idx_legalcase_functionary ON eglc_legalcase (functionary);

COMMENT ON TABLE eglc_legalcase IS 'LegalCase table';
COMMENT ON COLUMN EGLC_LEGALCASE.ID IS 'Primary Key';
COMMENT ON COLUMN eglc_legalcase.casenumber IS 'Case Number assigned by the Court';
COMMENT ON COLUMN eglc_legalcase.casedate IS 'Case Filing Date';
COMMENT ON COLUMN eglc_legalcase.casetitle IS 'Title of Case';
COMMENT ON COLUMN eglc_legalcase.appealnum IS 'Appeal Number';
COMMENT ON COLUMN eglc_legalcase.court IS 'Foreingn Key of EGLC_COURT_MASTER';
COMMENT ON COLUMN eglc_legalcase.casetype IS 'Foreign Key of EGLC_CASETYPE_MASTER';
COMMENT ON COLUMN eglc_legalcase.remarks IS 'Remarks';
COMMENT ON COLUMN eglc_legalcase.status IS 'Foreign Key of EGW_STATUS';
COMMENT ON COLUMN eglc_legalcase.casereceivingdate IS 'Case Receiving Date';
COMMENT ON COLUMN eglc_legalcase.isfiledbycorporation IS 'Is Case Filed By Corporation?';
COMMENT ON COLUMN eglc_legalcase.lcnumber IS 'LC Number';
COMMENT ON COLUMN eglc_legalcase.isrespondentgovernment IS 'Is Government Respondent';
COMMENT ON COLUMN eglc_legalcase.respondentgovtdept IS 'Foreign Key of EGLC_GOVERNMENTDEPARTMENT';
COMMENT ON COLUMN eglc_legalcase.prayer IS 'Prayer';
COMMENT ON COLUMN eglc_legalcase.issenioradvrequired IS 'Is Senior Advocate Required';
COMMENT ON COLUMN eglc_legalcase.petitiontype IS 'Foreign Key of EGLC_PETITIONTYPE_MASTER';
COMMENT ON COLUMN eglc_legalcase.functionary IS 'Foreign Key of FUNCTIONARY';
COMMENT ON COLUMN eglc_legalcase.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_legalcase.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_legalcase.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcase.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcase.opppartyadvocate IS 'Opposite Party Advocate';
COMMENT ON COLUMN eglc_legalcase.representedby IS 'Represented By';
COMMENT ON COLUMN eglc_legalcase.lcnumbertype IS 'LC Number Type';
COMMENT ON COLUMN eglc_legalcase.casefirstappearancedate IS 'Case First Appearance Date';
COMMENT ON COLUMN eglc_legalcase.previousdate IS 'Previous Date';
COMMENT ON COLUMN eglc_legalcase.nextdate IS 'Case Next Date';
COMMENT ON COLUMN eglc_legalcase.petitionerappearancedate IS 'Petitioner Appearance Date';
COMMENT ON COLUMN eglc_legalcase.stampnumber IS 'Stamp Number';
COMMENT ON COLUMN eglc_legalcase.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_judgment
(
  id bigint  NOT NULL,
  orderdate date NOT NULL,
  senttodepton   date,
  implementbydate  date,
  costawarded   numeric(20,2),
  compensationawarded  numeric(20,2),
  judgmentdetails  character varying(256),
  advisorfee  double precision,
  arbitratorfee  double precision,
  enquirydetails  character varying(256),
  enquirydate    date,
  setasidepetitiondate   date,
  setasidepetitiondetails  character varying(256),
  legalcase bigint NOT NULL,
  judgmenttype bigint  NOT NULL,
  saphearingdate date,
  issapaccepted boolean NOT NULL,
  parent bigint,
  ismemorequired boolean NOT NULL,
  certifiedmemofwddate date,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_judgment PRIMARY KEY (id),
  CONSTRAINT fk_judgment_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id),
  CONSTRAINT fk_judgment_judgmenttype FOREIGN KEY (judgmenttype) REFERENCES eglc_judgmenttype_master(id),
  CONSTRAINT fk_judgment_parent FOREIGN KEY (parent) REFERENCES eglc_judgment (id)
);
CREATE SEQUENCE seq_eglc_judgment;
CREATE INDEX idx_judgment_legalcase ON eglc_judgment (legalcase);
CREATE INDEX idx_judgment_judgmenttype ON eglc_judgment (judgmenttype);
CREATE INDEX idx_judgment_parent ON eglc_judgment (parent);

COMMENT ON TABLE eglc_judgment IS 'Judgment table';
COMMENT ON COLUMN eglc_judgment.id IS 'Primary Key';
COMMENT ON COLUMN eglc_judgment.orderdate IS 'Judgment Order Date';
COMMENT ON COLUMN eglc_judgment.senttodepton IS 'Order Sent to Department on date';
COMMENT ON COLUMN eglc_judgment.implementbydate IS 'Judgement Implement By Date';
COMMENT ON COLUMN eglc_judgment.costawarded IS 'Cost Awarded';
COMMENT ON COLUMN eglc_judgment.compensationawarded IS 'Compensation Awarded';
COMMENT ON COLUMN eglc_judgment.judgmentdetails IS 'Judgement Details';
COMMENT ON COLUMN eglc_judgment.advisorfee IS 'Advisor Fee';
COMMENT ON COLUMN eglc_judgment.arbitratorfee IS 'Arbitrator Fee';
COMMENT ON COLUMN eglc_judgment.enquirydetails IS 'Enquiry Details';
COMMENT ON COLUMN eglc_judgment.enquirydate IS 'Enquiry Date';
COMMENT ON COLUMN eglc_judgment.setasidepetitiondate IS 'Set Aside Pentition Filed on Date';
COMMENT ON COLUMN eglc_judgment.setasidepetitiondetails IS 'Set Aside Petition details';
COMMENT ON COLUMN eglc_judgment.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_judgment.judgmenttype IS 'Foreign Key of EGLC_JUDGMENTTYPE_MASTER';
COMMENT ON COLUMN eglc_judgment.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_judgment.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_judgment.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_judgment.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_judgment.saphearingdate IS 'Date of SA Petition Hearing';
COMMENT ON COLUMN eglc_judgment.issapaccepted IS 'Is Set Aside Petition Accepted?';
COMMENT ON COLUMN eglc_judgment.parent IS 'Foreign Key of EGLC_JUDGMENT';
COMMENT ON COLUMN eglc_judgment.ismemorequired IS 'Is Certificate copy of memo required?';
COMMENT ON COLUMN eglc_judgment.certifiedmemofwddate IS 'Date of Forwarding Certificate copy of memo';
COMMENT ON COLUMN eglc_judgment.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_judgmentimpl
(
  id bigint  NOT NULL,
  iscompiled  boolean NOT NULL,
  dateofcompliance  date,
  compliancereport  character varying(256),
  judgment bigint not null,
  reason character varying(256),
  implementationdetails character varying(256),
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_judgmentimpl PRIMARY KEY (id),
  CONSTRAINT fk_judgmentimpl_judgment FOREIGN KEY (judgment) REFERENCES  eglc_judgment (id)
); 
CREATE SEQUENCE seq_eglc_judgmentimpl;
CREATE INDEX idx_judgmentimpl_judgment ON eglc_judgmentimpl (judgment);

COMMENT ON TABLE eglc_judgmentimpl IS 'JudgmentImpl table';
COMMENT ON COLUMN eglc_judgmentimpl.id IS 'Primary Key';
COMMENT ON COLUMN eglc_judgmentimpl.iscompiled IS 'Is Completed?';
COMMENT ON COLUMN eglc_judgmentimpl.dateofcompliance IS 'Date of Compliance';
COMMENT ON COLUMN eglc_judgmentimpl.compliancereport IS 'Compliance Report';
COMMENT ON COLUMN eglc_judgmentimpl.judgment IS 'Foreign Key of EGLC_JUDGMENT';
COMMENT ON COLUMN eglc_judgmentimpl.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_judgmentimpl.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_judgmentimpl.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_judgmentimpl.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_judgmentimpl.reason IS 'Reason';
COMMENT ON COLUMN eglc_judgmentimpl.implementationdetails IS 'Implementation Details';
COMMENT ON COLUMN eglc_judgmentimpl.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_appeal
(
  id bigint NOT NULL,
  srnumber  character varying(50) NOT NULL,
  appealfiledon  date,
  appealfiledby character varying(100),
  judgmentimpl  bigint NOT NULL,
  status  bigint  NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_appeal PRIMARY KEY (id),
  CONSTRAINT fk_appeal_judgmentimpl FOREIGN KEY (judgmentimpl) REFERENCES eglc_judgmentimpl (id),
  CONSTRAINT fk_appeal_status FOREIGN KEY (status) REFERENCES egw_status (id)
);
CREATE SEQUENCE seq_eglc_appeal;
CREATE INDEX idx_appeal_judgmentimpl ON eglc_appeal(judgmentimpl);
CREATE INDEX idx_appeal_status ON eglc_appeal(status);

COMMENT ON TABLE eglc_appeal IS 'Appeal table';
COMMENT ON COLUMN eglc_appeal.id IS 'Primary Key';
COMMENT ON COLUMN eglc_appeal.srnumber IS 'Serial Number';
COMMENT ON COLUMN eglc_appeal.appealfiledon IS 'Appead Filed On';
COMMENT ON COLUMN eglc_appeal.appealfiledby IS 'Appeal File By';
COMMENT ON COLUMN eglc_appeal.status IS 'Foreign Key of EGW_STATUS';
COMMENT ON COLUMN eglc_appeal.judgmentimpl IS 'Foreign Key of EGLC_JUDGMENTIMPL';
COMMENT ON COLUMN eglc_appeal.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_appeal.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_appeal.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_appeal.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_appeal.version IS 'Version';
 ---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE  eglc_bipartisandetails
(
  id bigint NOT NULL,
  name character varying(128)  NOT NULL,
  address character varying(256),
  contactnumber character varying(20),
  legalcase bigint  NOT NULL,
  isrespondent boolean NOT NULL,
  serialnumber numeric NOT NULL,
  isrespondentgovernment boolean NOT NULL,
  respondentgovtdept bigint,
  CONSTRAINT pk_bipartisandetails PRIMARY KEY (id),
  CONSTRAINT fk_bipartisandetails_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id),
  CONSTRAINT fk_bipartisandetails_respondentgovtdept FOREIGN KEY (respondentgovtdept) REFERENCES eglc_governmentdepartment (id) 
);
CREATE SEQUENCE seq_eglc_bipartisandetails;
CREATE INDEX idx_bipartisandetails_legalcase ON eglc_bipartisandetails(legalcase);
CREATE INDEX idx_bipartisandetails_respondentgovtdept ON eglc_bipartisandetails(respondentgovtdept);

COMMENT ON TABLE eglc_bipartisandetails IS 'Bipartisan Details table';
COMMENT ON COLUMN eglc_bipartisandetails.id IS 'Primary Key';
COMMENT ON COLUMN eglc_bipartisandetails.name IS 'Name of the Party';
COMMENT ON COLUMN eglc_bipartisandetails.address IS 'Name of the Party';
COMMENT ON COLUMN eglc_bipartisandetails.contactnumber IS 'Contact Number of the Party';
COMMENT ON COLUMN eglc_bipartisandetails.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_bipartisandetails.isrespondent IS 'Is Respondent or Petitioner';
COMMENT ON COLUMN eglc_bipartisandetails.serialnumber IS 'Serial Number';
COMMENT ON COLUMN eglc_bipartisandetails.isrespondentgovernment IS 'Is Government Respondent';
COMMENT ON COLUMN eglc_bipartisandetails.respondentgovtdept IS 'Foreign Key of EGLC_GOVERNMENTDEPARTMENT';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_legalcase_batchcase
(
  id bigint NOT NULL,
  legalcase bigint NOT NULL,
  casedate date NOT NULL,
  casenumber character varying(50) NOT NULL,
  petitionername character varying(1024) NOT NULL,
  CONSTRAINT PK_legalcase_batchcase PRIMARY KEY (id),
  CONSTRAINT fk_legalcasebatchcase_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id)
);
CREATE SEQUENCE seq_eglc_legalcase_batchcase;
CREATE INDEX idx_legalcasebatchcase_legalcase ON eglc_legalcase_batchcase(legalcase);

COMMENT ON TABLE eglc_legalcase_batchcase IS 'Legalcase Batchcase table';
COMMENT ON COLUMN eglc_legalcase_batchcase.id IS 'Primary Key';
COMMENT ON COLUMN eglc_legalcase_batchcase.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_legalcase_batchcase.casedate IS 'Case Date';
COMMENT ON COLUMN eglc_legalcase_batchcase.casenumber IS 'Case Number';
COMMENT ON COLUMN eglc_legalcase_batchcase.petitionername IS 'Petition Name';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE  eglc_contempt
(
  id bigint  NOT NULL,
  canumber character varying(50) NOT NULL,
  receivingdate  date,
  iscommapprrequired boolean NOT NULL,
  commappdate  date,
  judgmentimpl bigint NOT NULL,
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_contempt PRIMARY KEY (id),
  CONSTRAINT fk_contempt_judgmentimpl FOREIGN KEY (judgmentimpl) REFERENCES eglc_judgmentimpl (id)
);
CREATE SEQUENCE seq_eglc_contempt;
CREATE INDEX idx_contempt_judgmentimpl ON eglc_contempt (judgmentimpl);

COMMENT ON TABLE eglc_contempt IS 'Contempt table';
COMMENT ON COLUMN eglc_contempt.id IS 'Primary Key';
COMMENT ON COLUMN eglc_contempt.canumber IS 'Case Number';
COMMENT ON COLUMN eglc_contempt.receivingdate IS 'Recieving Date';
COMMENT ON COLUMN eglc_contempt.iscommapprrequired IS 'Is Contempt Appearance Required?';
COMMENT ON COLUMN eglc_contempt.commappdate IS 'Contempt Appearance Date';
COMMENT ON COLUMN eglc_contempt.judgmentimpl IS 'Foreign Key of EGLC_JUDGMENTIMPL';
COMMENT ON COLUMN eglc_contempt.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_contempt.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_contempt.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_contempt.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_contempt.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_hearings
(
   id bigint  NOT NULL,
   hearingdate date   NOT NULL,
   legalcase bigint  NOT NULL,
   isstandingcounselpresent  boolean NOT NULL,
   additionallawyers character varying(256),
   isseniorstandingcounselpresent boolean NOT NULL,
   hearingoutcome character varying(256),
   purposeofhearing character varying(256),
   status bigint,
   referencenumber character varying(50),
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_hearings PRIMARY KEY (id),
   CONSTRAINT fk_hearings_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id),
   CONSTRAINT fk_hearings_status FOREIGN KEY (status) REFERENCES egw_status (id)
);
CREATE SEQUENCE seq_eglc_hearings;
CREATE INDEX idx_hearings_legalcase ON eglc_hearings (legalcase);
CREATE INDEX idx_hearings_status ON eglc_hearings (status);

COMMENT ON TABLE eglc_hearings IS 'Hearings table';
COMMENT ON COLUMN eglc_hearings.id IS 'Primary Key';
COMMENT ON COLUMN eglc_hearings.hearingdate IS 'Hearing Date';
COMMENT ON COLUMN eglc_hearings.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_hearings.isstandingcounselpresent IS 'Is Standing Counsel Present? Yes/No';
COMMENT ON COLUMN eglc_hearings.additionallawyers IS 'Additional Lawyers';
COMMENT ON COLUMN eglc_hearings.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_hearings.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_hearings.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_hearings.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_hearings.hearingoutcome IS 'Hearing Outcome';
COMMENT ON COLUMN eglc_hearings.isseniorstandingcounselpresent IS 'Is Senior Standing Counsel Present at Hearing? Yes/No';
COMMENT ON COLUMN eglc_hearings.purposeofhearing IS 'Purpose of Hearing';
COMMENT ON COLUMN eglc_hearings.status IS 'Foreign Kye of EGW_STATUS';
COMMENT ON COLUMN eglc_hearings.version IS 'Version';
-----------------------------END------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_lcinterimorder
(
   id bigint NOT NULL, 
   iodate date NOT NULL, 
   mpnumber character varying(50),  
   notes character varying(1024),  
   intordertypeid bigint, 
   legalcase bigint, 
   sendtostandingcounsel date, 
   petitionfiledon date, 
   reportfilingdue date, 
   senttodepartment date, 
   revdfromhod date, 
   reportsendtostandingcounsel date, 
   reportfilingdate date,  
   status bigint, 
   referencenumber character varying(50), 
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_lcinterimorder PRIMARY KEY (id),
   CONSTRAINT fk_lcinterimorder_intordertype FOREIGN KEY (intordertypeid) REFERENCES eglc_interimtype_master (id),
   CONSTRAINT fk_lcinterimorder_legalcase FOREIGN KEY (legalcase) REFERENCES  eglc_legalcase (id),
   CONSTRAINT fk_lcinterimorder_status FOREIGN KEY (status) REFERENCES egw_status (id)
);
CREATE SEQUENCE seq_eglc_lcinterimorder;
CREATE INDEX idx_lcinterimorder_intordertype ON eglc_lcinterimorder (intordertypeid);
CREATE INDEX idx_lcinterimorder_legalcase ON eglc_lcinterimorder (legalcase); 
CREATE INDEX idx_lcinterimorder_status ON eglc_lcinterimorder (status); 

COMMENT ON TABLE eglc_lcinterimorder IS 'Legalcase Interiom Order table';
COMMENT ON COLUMN eglc_lcinterimorder.id IS 'Primary Key';
COMMENT ON COLUMN eglc_lcinterimorder.iodate IS 'Interim Order Date';
COMMENT ON COLUMN eglc_lcinterimorder.mpnumber IS 'MP Number';
COMMENT ON COLUMN eglc_lcinterimorder.notes IS 'Notes';
COMMENT ON COLUMN eglc_lcinterimorder.intordertypeid IS 'Foreign Key of EGLC_INTERIMTYPE_MASTER';
COMMENT ON COLUMN eglc_lcinterimorder.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_lcinterimorder.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_lcinterimorder.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_lcinterimorder.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_lcinterimorder.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_lcinterimorder.sendtostandingcounsel IS 'Send to Standing Counsel';
COMMENT ON COLUMN eglc_lcinterimorder.petitionfiledon IS 'Date of Petition Filed';
COMMENT ON COLUMN eglc_lcinterimorder.reportfilingdue IS 'Report Filing Due Date';
COMMENT ON COLUMN eglc_lcinterimorder.senttodepartment IS 'Sent To Department';
COMMENT ON COLUMN eglc_lcinterimorder.revdfromhod IS 'Report From HOD';
COMMENT ON COLUMN eglc_lcinterimorder.reportsendtostandingcounsel IS 'Date of Report Send to Standing Counsel';
COMMENT ON COLUMN eglc_lcinterimorder.reportfilingdate IS 'Report Filing Date';
COMMENT ON COLUMN eglc_lcinterimorder.status IS 'Foreign Key of EGW_STATUS';
COMMENT ON COLUMN eglc_lcinterimorder.referencenumber IS 'Reference Number';
COMMENT ON COLUMN eglc_lcinterimorder.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_legalcase_advocate
(
   id bigint NOT NULL, 
   advocatemaster bigint NOT NULL, 
   assignedtodate date, 
   vakalatdate date, 
   legalcase bigint NOT NULL, 
   isactive boolean NOT NULL, 
   ordernumber character varying(50), 
   orderdate date, 
   senioradvocate bigint, 
   seniorassignedtodate date, 
   ordernumberjunior character varying(50), 
   orderdatejunior date, 
   juniorstage bigint, 
   reassignmentreasonjunior character varying(1024), 
   seniorstage bigint, 
   reassignmentreasonsenior character varying(1024), 
   changeadvocate boolean NOT NULL, 
   changesenioradvocate boolean NOT NULL, 
   CONSTRAINT pk_legalcase_advocate PRIMARY KEY (id),
   CONSTRAINT fk_legalcase_advocate_advocatemaster FOREIGN KEY (advocatemaster) REFERENCES eglc_advocate_master (id), 
   CONSTRAINT fk_legalcase_advocate_senioradvocate FOREIGN KEY (senioradvocate) REFERENCES eglc_advocate_master (id), 
   CONSTRAINT fk_legalcase_advocate_seniorstage FOREIGN KEY (seniorstage) REFERENCES eglc_case_stage (id), 
   CONSTRAINT fk_legalcase_advocate_juniorstage FOREIGN KEY (juniorstage) REFERENCES eglc_case_stage (id), 
   CONSTRAINT fk_legalcase_advocate_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
); 
CREATE SEQUENCE seq_eglc_legalcase_advocate;
CREATE INDEX idx_legalcase_advocate_advocatemaster ON eglc_legalcase_advocate (advocatemaster);
CREATE INDEX idx_legalcase_advocate_senioradvocate ON eglc_legalcase_advocate (senioradvocate); 
CREATE INDEX idx_legalcase_advocate_seniorstage ON eglc_legalcase_advocate (seniorstage);
CREATE INDEX idx_legalcase_advocate_juniorstage ON eglc_legalcase_advocate (juniorstage);
CREATE INDEX idx_legalcase_advocate_legalcase ON eglc_legalcase_advocate (legalcase);

COMMENT ON TABLE eglc_legalcase_advocate IS 'Legalcase Advocate table';
COMMENT ON COLUMN eglc_legalcase_advocate.id IS 'Primary Key';
COMMENT ON COLUMN eglc_legalcase_advocate.advocatemaster IS 'Foreign Key of EGLC_ADVOCATE_MASTER';
COMMENT ON COLUMN eglc_legalcase_advocate.assignedtodate IS 'Assigned to Date';
COMMENT ON COLUMN eglc_legalcase_advocate.vakalatdatE IS 'Vakalat Date';
COMMENT ON COLUMN eglc_legalcase_advocate.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_legalcase_advocate.isactive IS 'Is Active?';
COMMENT ON COLUMN eglc_legalcase_advocate.ordernumber IS 'Order Number';
COMMENT ON COLUMN eglc_legalcase_advocate.orderdate IS 'Order Date';
COMMENT ON COLUMN eglc_legalcase_advocate.senioradvocate IS 'Foreign Key of EGLC_ADVOCATE_MASTER';
COMMENT ON COLUMN eglc_legalcase_advocate.seniorassignedtodate IS 'Is Senior Advocate?';
COMMENT ON COLUMN eglc_legalcase_advocate.ordernumberjunior IS 'Junior Order Number';
COMMENT ON COLUMN eglc_legalcase_advocate.orderdatejunior IS 'Junior Order Date';
COMMENT ON COLUMN eglc_legalcase_advocate.juniorstage IS 'Foreign Key of EGLC_CASE_STAGE';
COMMENT ON COLUMN eglc_legalcase_advocate.reassignmentreasonjunior IS 'Junior Re Assignment Reason';
COMMENT ON COLUMN eglc_legalcase_advocate.seniorstage IS 'Foreign Key of EGLC_CASE_STAGE';
COMMENT ON COLUMN eglc_legalcase_advocate.reassignmentreasonsenior IS 'Senior Re assignment Reason';
COMMENT ON COLUMN eglc_legalcase_advocate.changeadvocate IS 'Change Advocate';
COMMENT ON COLUMN eglc_legalcase_advocate.changesenioradvocate IS 'Change Senior Advocate';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_legalcase_dept
(
  id bigint NOT NULL,
  legalcase bigint NOT NULL, 
  department bigint NOT NULL, 
  dateofreceiptofpwr date, 
  position bigint NOT NULL, 
  isprimarydepartment boolean NOT NULL, 
  assignon date, 
  CONSTRAINT pk_legalcase_dept PRIMARY KEY (id),
  CONSTRAINT fk_legalcase_dept_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id), 
  CONSTRAINT fk_legalcase_dept_department FOREIGN KEY (department) REFERENCES eg_department (id), 
  CONSTRAINT fk_legalcase_dept_position FOREIGN KEY (position) REFERENCES eg_position (id) 
);
CREATE SEQUENCE seq_eglc_legalcase_dept;
CREATE INDEX idx_legalcase_dept_legalcase ON eglc_legalcase_dept (legalcase);
CREATE INDEX idx_legalcase_dept_department ON eglc_legalcase_dept (department);
CREATE INDEX idx_legalcase_dept_position ON eglc_legalcase_dept (position);

COMMENT ON TABLE eglc_legalcase_dept IS 'Legalcase Department table';
COMMENT ON COLUMN eglc_legalcase_dept.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_legalcase_dept.department IS 'Foreign Key of EG_DEPARTMENT';
COMMENT ON COLUMN eglc_legalcase_dept.id IS 'Primary Key';
COMMENT ON COLUMN eglc_legalcase_dept.dateofreceiptofpwr IS 'Date of Receipt of PWR';
COMMENT ON COLUMN eglc_legalcase_dept.position IS 'Foreign Key of EG_POSITION';
COMMENT ON COLUMN eglc_legalcase_dept.isprimarydepartment IS 'Is Primary Department';
COMMENT ON COLUMN eglc_legalcase_dept.assignon IS 'Assigned on Date';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_legalcasedisposal
(
  id bigint NOT NULL, 
  disposaldate date, 
  disposaldetails character varying(1024), 
  consignmenttorecordroomdate date, 
  legalcase bigint NOT NULL, 
  createddate timestamp without time zone  NOT NULL,
  lastmodifieddate timestamp without time zone  NOT NULL,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_legalcasedisposal PRIMARY KEY (id),
  CONSTRAINT fk_legalcasedisposal_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
);
CREATE SEQUENCE seq_eglc_legalcasedisposal;
CREATE INDEX idx_legalcasedisposal_legalcase ON eglc_legalcasedisposal (legalcase);

COMMENT ON TABLE eglc_legalcasedisposal IS 'Legalcase Disposal table';
COMMENT ON COLUMN eglc_legalcasedisposal.id IS 'Primary Key';
COMMENT ON COLUMN eglc_legalcasedisposal.disposaldate IS 'Disposal Date';
COMMENT ON COLUMN eglc_legalcasedisposal.disposaldetails IS 'Foreign Key of?? EGLC_LEGALCASEDISPOSAL';
COMMENT ON COLUMN eglc_legalcasedisposal.consignmenttorecordroomdate IS 'Consignment to record room date';
COMMENT ON COLUMN eglc_legalcasedisposal.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_legalcasedisposal.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_legalcasedisposal.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_legalcasedisposal.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcasedisposal.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcasedisposal.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_legalcase_miscdetails
(	
    id bigint NOT NULL,
    miscdate date NOT NULL, 
    referencenumber character varying(50), 
    remarks character varying(1024), 
    legalcase bigint NOT NULL, 
    status bigint NOT NULL, 
    createddate timestamp without time zone  NOT NULL,
    lastmodifieddate timestamp without time zone  NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    version numeric DEFAULT 0,
    CONSTRAINT pk_legalcase_miscdetails PRIMARY KEY (id),
    CONSTRAINT fk_legalcase_miscdetails_status FOREIGN KEY (status) REFERENCES egw_status (id), 
    CONSTRAINT fk_legalcase_miscdetails_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
);
CREATE SEQUENCE seq_eglc_legalcase_miscdetails;
CREATE INDEX idx_legalcase_miscdetails_legalcase ON eglc_legalcase_miscdetails (legalcase);
CREATE INDEX idx_legalcase_miscdetails_status ON eglc_legalcase_miscdetails (status);

COMMENT ON TABLE eglc_legalcase_miscdetails IS 'Legalcase Misc Details table';
COMMENT ON COLUMN eglc_legalcase_miscdetails.id IS 'Primary Key';
COMMENT ON COLUMN eglc_legalcase_miscdetails.miscdate IS 'Miscellaneous Date';
COMMENT ON COLUMN eglc_legalcase_miscdetails.referencenumber IS 'Reference Number';
COMMENT ON COLUMN eglc_legalcase_miscdetails.remarks IS 'Remarks';
COMMENT ON COLUMN eglc_legalcase_miscdetails.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_legalcase_miscdetails.status IS 'Foreign Key of EGW_STATUS';
COMMENT ON COLUMN eglc_legalcase_miscdetails.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_legalcase_miscdetails.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_legalcase_miscdetails.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcase_miscdetails.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcase_miscdetails.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_paperbook 
(	
   id bigint NOT NULL, 
   legalcase bigint, 
   lastdatetodepositamount date, 
   depositedamount double precision, 
   concernedofficername character varying(128), 
   remarks character varying(512), 
   ispaperbookrequired boolean NOT NULL, 
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_paperbook PRIMARY KEY (id),
   CONSTRAINT fk_paperbook_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
  );
CREATE SEQUENCE seq_eglc_paperbook;
CREATE INDEX idx_paperbook_legalcase ON eglc_paperbook (legalcase);

COMMENT ON TABLE eglc_paperbook IS 'Paperbook table';
COMMENT ON COLUMN eglc_paperbook.id IS 'Primary Key';
COMMENT ON COLUMN eglc_paperbook.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_paperbook.lastdatetodepositamount IS 'Last Date to Deposit Amount';
COMMENT ON COLUMN eglc_paperbook.depositedamount IS 'Deposited Amount';
COMMENT ON COLUMN eglc_paperbook.concernedofficername IS 'Concerned Officer Name';
COMMENT ON COLUMN eglc_paperbook.remarks IS 'Remarks';
COMMENT ON COLUMN eglc_paperbook.ispaperbookrequired IS 'Is Paper Book Required';
COMMENT ON COLUMN eglc_paperbook.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_paperbook.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_paperbook.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_paperbook.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_paperbook.version IS 'Version';
 ---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_processregister 
(	
    id bigint NOT NULL,
    legalcase bigint, 
    detailedaddress character varying(256), 
    processdate date, 
    nextdateofprocess date, 
    receivedon date, 
    processhandedoverto character varying(128), 
    dateofhandingover date, 
    processfilingattorney character varying(128), 
    remarks character varying(512), 
    isprocessregrequired boolean NOT NULL, 
    createddate timestamp without time zone  NOT NULL,
    lastmodifieddate timestamp without time zone  NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    version numeric DEFAULT 0,
    CONSTRAINT pk_processregister PRIMARY KEY (id),
    CONSTRAINT fk_proregister_legalcaseid FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
   ) ;
CREATE SEQUENCE seq_eglc_processregister;
CREATE INDEX idx_processregister_legalcase ON eglc_processregister (legalcase);

COMMENT ON TABLE eglc_processregister IS 'Process Register table';
COMMENT ON COLUMN eglc_processregister.id IS 'Primary Key';
COMMENT ON COLUMN eglc_processregister.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_processregister.detailedaddress IS 'Detailed Address';
COMMENT ON COLUMN eglc_processregister.processdate IS 'Process Date';
COMMENT ON COLUMN eglc_processregister.nextdateofprocess IS 'Next Date of Process';
COMMENT ON COLUMN eglc_processregister.receivedon IS 'Received Date';
COMMENT ON COLUMN eglc_processregister.processhandedoverto IS 'Process Handed over to';
COMMENT ON COLUMN eglc_processregister.dateofhandingover IS 'Date of Handing over';
COMMENT ON COLUMN eglc_processregister.processfilingattorney IS 'Process Filing Attorney';
COMMENT ON COLUMN eglc_processregister.remarks IS 'Remarks';
COMMENT ON COLUMN eglc_processregister.isprocessregrequired IS 'Is Process Required';
COMMENT ON COLUMN eglc_processregister.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_processregister.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_processregister.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_processregister.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_processregister.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_pwr 
(	
   id  bigint NOT NULL, 
   cafilingdate date, 
   legalcase bigint NOT NULL, 
   caduedate date, 
   pwrduedate date, 
   CONSTRAINT pk_eglc_pwr PRIMARY KEY (id),
   CONSTRAINT fk_pwr_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
);
CREATE SEQUENCE seq_eglc_pwr;
CREATE INDEX idx_pwr_legalcase ON eglc_pwr (legalcase);

COMMENT ON TABLE eglc_pwr IS 'PWR table';
COMMENT ON COLUMN eglc_pwr.id IS 'Primary Key';
COMMENT ON COLUMN eglc_pwr.cafilingdate IS 'CA Filing Date';
COMMENT ON COLUMN eglc_pwr.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_pwr.caduedate IS 'CA Due Date';
COMMENT ON COLUMN eglc_pwr.pwrduedate IS 'PWR Due Date';
 ---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_reminder 
(	
   id bigint NOT NULL, 
   remarks character varying(256), 
   reminderdate date NOT NULL, 
   legalcasedept bigint NOT NULL, 
   createddate timestamp without time zone,
   lastmodifieddate timestamp without time zone,
   version numeric DEFAULT 0,
   CONSTRAINT pk_eglc_reminder PRIMARY KEY (id),
   CONSTRAINT fk_reminder_legalcasedept FOREIGN KEY (legalcasedept) REFERENCES eglc_legalcase_dept (id) 
);
CREATE SEQUENCE seq_eglc_reminder;
CREATE INDEX idx_reminder_legalcasedept ON eglc_reminder (legalcasedept);

COMMENT ON TABLE eglc_reminder IS 'Reminder table';
COMMENT ON COLUMN eglc_reminder.id IS 'Primary Key';
COMMENT ON COLUMN eglc_reminder.remarks IS 'Remarks';
COMMENT ON COLUMN eglc_reminder.reminderdate IS 'Reminder Date';
COMMENT ON COLUMN eglc_reminder.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_reminder.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_reminder.legalcasedept IS 'Foreign Key Of EGLC_LEGALCASE_DEPT';
COMMENT ON COLUMN eglc_reminder.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_vacatestay_petition 
(	
   id bigint NOT NULL, 
   lcinterimorder bigint NOT NULL, 
   receivedfromstandingcounsel date, 
   sendtostandingcounsel date, 
   petitionfiledon date NOT NULL, 
   remarks character varying(1024), 
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_vacatestay_petition PRIMARY KEY (id),
   CONSTRAINT fk_vacatestaypetition_lcinterimorder FOREIGN KEY (lcinterimorder) REFERENCES eglc_lcinterimorder (id) 
);
CREATE SEQUENCE seq_eglc_vacatestay_petition;
CREATE INDEX idx_vacatestaypetition_lcinterimorder ON eglc_vacatestay_petition (lcinterimorder);

COMMENT ON TABLE eglc_vacatestay_petition IS 'Vacatestay Petitionr table';
COMMENT ON COLUMN eglc_vacatestay_petition.id IS 'Primary Key';
COMMENT ON COLUMN eglc_vacatestay_petition.lcinterimorder IS 'Foreign Key of EGLC_LCINTERIMORDER';
COMMENT ON COLUMN eglc_vacatestay_petition.receivedfromstandingcounsel IS 'Date of Petition Recieved Form Standing Counsel';
COMMENT ON COLUMN eglc_vacatestay_petition.sendtostandingcounsel IS 'Date of Petition Send to Staing Counsel';
COMMENT ON COLUMN eglc_vacatestay_petition.petitionfiledon IS 'Petition File on ';
COMMENT ON COLUMN eglc_vacatestay_petition.remarks IS 'Remarks';
COMMENT ON COLUMN eglc_vacatestay_petition.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_vacatestay_petition.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_vacatestay_petition.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_vacatestay_petition.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_vacatestay_petition.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_vakalat
(	
   id bigint NOT NULL, 
   duedate date, 
   compldate date, 
   assignedto character varying(50), 
   legalcase bigint, 
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_vakalat PRIMARY KEY (id),
   CONSTRAINT fk_vakalat_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id) 
);
CREATE SEQUENCE seq_eglc_vakalat;
CREATE INDEX idx_vakalat_legalcase ON eglc_vakalat (legalcase);

COMMENT ON TABLE eglc_vakalat IS 'Vakalat table';
COMMENT ON COLUMN eglc_vakalat.id IS 'Primary Key';
COMMENT ON COLUMN eglc_vakalat.duedate IS 'Vakalat Due Date';
COMMENT ON COLUMN eglc_vakalat.compldate IS 'Vakalat Completion Date';
COMMENT ON COLUMN eglc_vakalat.assignedto IS 'Vakalat Assigned to';
COMMENT ON COLUMN eglc_vakalat.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_vakalat.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_vakalat.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_vakalat.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_vakalat.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_vakalat.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_legalcasefee
(	
   id bigint NOT NULL, 
   installmentnumber numeric NOT NULL, 
   additionalfees double precision, 
   reason character varying(256), 
   seniodadvfee double precision, 
   arbitratorfee double precision, 
   legalcase bigint NOT NULL, 
   lcadvocate bigint, 
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_legalcasefee PRIMARY KEY (id),
   CONSTRAINT fk_legalcasefee_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id), 
   CONSTRAINT fk_legalcasefee_lcadvocate FOREIGN KEY (lcadvocate) REFERENCES eglc_legalcase_advocate (id)
);
CREATE SEQUENCE seq_eglc_legalcasefee;
CREATE INDEX idx_legalcasefee_legalcase ON eglc_legalcasefee (legalcase);
CREATE INDEX idx_legalcasefee_lcadvocate ON eglc_legalcasefee (lcadvocate);

COMMENT ON TABLE eglc_legalcasefee IS 'Legalcase Fee table';
COMMENT ON COLUMN eglc_legalcasefee.id IS 'Primary Key';
COMMENT ON COLUMN eglc_legalcasefee.installmentnumber IS 'Installment Date';
COMMENT ON COLUMN eglc_legalcasefee.additionalfees IS 'Additional Fees';
COMMENT ON COLUMN eglc_legalcasefee.reason IS 'Reason';
COMMENT ON COLUMN eglc_legalcasefee.seniodadvfee IS 'Senior Advocate Fees';
COMMENT ON COLUMN eglc_legalcasefee.arbitratorfee IS 'Arbitrator Fee';
COMMENT ON COLUMN eglc_legalcasefee.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_legalcasefee.lcadvocate IS 'Foreign Key of EGLC_LEGALCASE_ADVOCATE';
COMMENT ON COLUMN eglc_legalcasefee.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_legalcasefee.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_legalcasefee.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcasefee.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_legalcasefee.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_employeehearing
(	
   employee bigint, 
   hearing bigint, 
   CONSTRAINT fk_employeehearing_employee FOREIGN KEY (employee) REFERENCES egeis_employee (id),
   CONSTRAINT fk_employeehearing_hearing FOREIGN KEY (hearing) REFERENCES eglc_hearings (id)
 );
CREATE SEQUENCE seq_eglc_employeehearing;
CREATE INDEX idx_employeehearing_employee ON eglc_employeehearing (employee);
CREATE INDEX idx_employeehearing_hearing ON eglc_employeehearing (hearing);

COMMENT ON TABLE eglc_employeehearing IS 'Employee Hearing table';
COMMENT ON COLUMN eglc_employeehearing.employee IS 'Foreign Key of EG_EMPLOYEE';
COMMENT ON COLUMN eglc_employeehearing.hearing IS 'Foreign Key of EGLC_HEARINGS';
 ---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_advocate_bill 
(	
    id  bigint NOT NULL, 
    billnumber character varying(64) NOT NULL, 
    billdate date NOT NULL, 
    advocate bigint NOT NULL,
    finbillid numeric, 
    totalinvoicedamount double precision default 0 NOT NULL, 
    totalpassedamount double precision default 0 NOT NULL, 
    state bigint,  
    createddate timestamp without time zone  NOT NULL,
    lastmodifieddate timestamp without time zone  NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    version numeric DEFAULT 0,
    CONSTRAINT pk_advocate_bill PRIMARY KEY (id),
    CONSTRAINT fk_advocatebill_advocate FOREIGN KEY (advocate) REFERENCES eglc_advocate_master (id), 
    CONSTRAINT fk_advocatebill_state FOREIGN KEY (state) REFERENCES eg_wf_states (id) 
);
CREATE SEQUENCE seq_eglc_advocate_bill;
CREATE INDEX idx_advocatebill_advocate ON eglc_advocate_bill (advocate);
CREATE INDEX idx_advocatebill_state ON eglc_advocate_bill (state);

COMMENT ON TABLE eglc_advocate_bill IS 'Advocate Bill table';
COMMENT ON COLUMN eglc_advocate_bill.id IS 'Primary Key';
COMMENT ON COLUMN eglc_advocate_bill.billnumber IS 'Bill Number';
COMMENT ON COLUMN eglc_advocate_bill.billdate IS 'Bill Date';
COMMENT ON COLUMN eglc_advocate_bill.advocate IS 'Foreign Key of EGLC_ADVOCATE_MASTER';
COMMENT ON COLUMN eglc_advocate_bill.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_advocate_bill.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_advocate_bill.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_advocate_bill.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_advocate_bill.finbillid IS 'Foreign Key of EG_BILLREGISTER';
COMMENT ON COLUMN eglc_advocate_bill.totalinvoicedamount IS 'Invoice Amount';
COMMENT ON COLUMN eglc_advocate_bill.totalpassedamount IS 'Passed Amount';
COMMENT ON COLUMN eglc_advocate_bill.state IS 'Foreign Key of EG_WF_STATES';
COMMENT ON COLUMN eglc_advocate_bill.version IS 'Version';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_bill 
(	
   id bigint NOT NULL, 
   serialnumber numeric NOT NULL, 
   advocatebill bigint NOT NULL, 
   stage bigint NOT NULL, 
   accounthead bigint NOT NULL, 
   invoiceamount double precision NOT NULL, 
   passedamount double precision NOT NULL, 
   remarks character varying(256), 
   legalcase bigint NOT NULL, 
   previousamountpaidforstage double precision, 
   seniorpresent character varying(32), 
   CONSTRAINT pk_eglc_bill PRIMARY KEY (id),
   CONSTRAINT fk_bill_accounthead FOREIGN KEY (accounthead) REFERENCES chartofaccounts (id), 
   CONSTRAINT fk_bill_advocatebill FOREIGN KEY (advocatebill) REFERENCES eglc_advocate_bill (id), 
   CONSTRAINT fk_bill_stage FOREIGN KEY (stage) REFERENCES eglc_case_stage (id), 
   CONSTRAINT fk_bill_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id)
);
CREATE SEQUENCE seq_eglc_bill;
CREATE INDEX idx_bill_accounthead ON eglc_bill (accounthead);
CREATE INDEX idx_bill_advocatebill ON eglc_bill (advocatebill);
CREATE INDEX idx_bill_stage ON eglc_bill (stage);
CREATE INDEX idx_bill_legalcase ON eglc_bill (legalcase);

COMMENT ON TABLE eglc_bill IS ' Bill table';
COMMENT ON COLUMN eglc_bill.id IS 'Primary Key';
COMMENT ON COLUMN eglc_bill.serialnumber IS 'Bill Serial Number';
COMMENT ON COLUMN eglc_bill.advocatebill IS 'Foreign Key of EGLC_ADVOCATE_BILL';
COMMENT ON COLUMN eglc_bill.stage IS 'Foreign Key of EGLC_CASE_STAGE';
COMMENT ON COLUMN eglc_bill.accounthead IS 'Foreign Key of CHARTOFACCOUNTS';
COMMENT ON COLUMN eglc_bill.invoiceamount IS 'Invoiced Amount';
COMMENT ON COLUMN eglc_bill.passedamount IS 'Passed Amount';
COMMENT ON COLUMN eglc_bill.remarks IS 'Remarks';
COMMENT ON COLUMN eglc_bill.legalcase IS 'Foreign Key of EGLC_LEGALCASE';
COMMENT ON COLUMN eglc_bill.previousamountpaidforstage IS 'Previous amount paid for Case stage';
COMMENT ON COLUMN eglc_bill.seniorpresent IS 'Is Senior Advocate Present?';
 ---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_advcourttype_mapping 
(	
   advocate bigint, 
   courttype bigint, 
   CONSTRAINT fk_advcourttype_mapping_advocate FOREIGN KEY (advocate) REFERENCES eglc_advocate_master (id), 
   CONSTRAINT fk_advcourttype_mapping_courttype FOREIGN KEY (courttype) REFERENCES eglc_courttype_master (id) 
);
CREATE SEQUENCE seq_eglc_advcourttype_mapping;
CREATE INDEX idx_advcourttype_mapping_advocate ON eglc_advcourttype_mapping (advocate);
CREATE INDEX idx_advcourttype_mapping_courttype ON eglc_advcourttype_mapping (courttype);

COMMENT ON TABLE eglc_advcourttype_mapping IS 'Advocate and Courttype mapping tables';
COMMENT ON COLUMN eglc_advcourttype_mapping.advocate IS 'Foreign Key of eglc_advocate_master';
COMMENT ON COLUMN eglc_advcourttype_mapping.courttype IS 'Foreign Key of eglc_courttype_master';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_advocatedept_mapping 
(	
   department bigint NOT NULL, 
   advocate bigint NOT NULL, 
   CONSTRAINT fk_advocatedept_mapping_department FOREIGN KEY (department) REFERENCES eg_department (id), 
   CONSTRAINT fk_advocatedept_mapping_advocate FOREIGN KEY (advocate) REFERENCES eglc_advocate_master (id) 
);
CREATE SEQUENCE seq_eglc_advocatedept_mapping;
CREATE INDEX idx_advocatedeptmapping_department ON eglc_advocatedept_mapping (department);
CREATE INDEX idx_advocatedeptmapping_advocate ON eglc_advocatedept_mapping (advocate);

COMMENT ON TABLE eglc_advocatedept_mapping IS 'Advocate and Department mapping tables';
COMMENT ON COLUMN eglc_advocatedept_mapping.department IS 'Foreign Key of EG_DEPARTMENT';
COMMENT ON COLUMN eglc_advocatedept_mapping.advocate IS 'Foreign Key of EGLC_ADVOCATE_MASTER';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_retainership_fee 
(	
    id bigint  NOT NULL, 
    month numeric, 
    year numeric,
    CONSTRAINT pk_retainership_fee PRIMARY KEY (id)
);
CREATE SEQUENCE seq_eglc_retainership_fee;

COMMENT ON TABLE eglc_retainership_fee IS 'Retainership Fee tables';
COMMENT ON COLUMN eglc_retainership_fee.id IS 'Primary Key';
COMMENT ON COLUMN eglc_retainership_fee.month IS 'Month';
COMMENT ON COLUMN EGLC_RETAINERSHIP_FEE.YEAR IS 'Year';
 ---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
 CREATE TABLE  eglc_legalcase_document
(
  legalcase bigint NOT NULL,
  filestoreid bigint NOT NULL,
  CONSTRAINT fk_legalcasedocument_legalcase FOREIGN KEY (legalcase) REFERENCES eglc_legalcase (id), 
  CONSTRAINT fk_legalcasedocument_filestoreid FOREIGN KEY (filestoreid) REFERENCES eg_filestoremap (id) 
 );
CREATE SEQUENCE seq_eglc_legalcase_document;
CREATE INDEX idx_legalcasedocument_legalcase ON eglc_legalcase_document (legalcase);
CREATE INDEX idx_legalcasedocument_filestoreid ON eglc_legalcase_document (filestoreid);

COMMENT ON TABLE eglc_legalcase_document IS 'Legalcase Document tables';
COMMENT ON COLUMN eglc_legalcase_document.legalcase IS 'Foreign Key of  eglc_legalcase';
COMMENT ON COLUMN eglc_legalcase_document.filestoreid IS 'Foreign Key of  eg_filestoremap';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_judgment_document
(
   judgment bigint NOT NULL,
   filestoreid bigint NOT NULL,
   CONSTRAINT fk_judgmentdocument_judgment FOREIGN KEY (judgment) REFERENCES eglc_judgment (id), 
   CONSTRAINT fk_judgmentdocument_filestoreid FOREIGN KEY (filestoreid) REFERENCES eg_filestoremap (id) 
);
CREATE SEQUENCE seq_eglc_judgment_document;
CREATE INDEX idx_judgmentdocument_judgment ON eglc_judgment_document (judgment);
CREATE INDEX idx_judgmentdocument_filestoreid ON eglc_judgment_document (filestoreid);

COMMENT ON TABLE eglc_judgment_document IS 'Judgment Document tables';
COMMENT ON COLUMN eglc_judgment_document.judgment IS 'Foreign Key of eglc_judgment';
COMMENT ON COLUMN eglc_judgment_document.filestoreid IS 'Foreign Key of eg_filestoremap';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_lcio_document
(
   lcinterimorder bigint NOT NULL,
   filestoreid bigint NOT NULL,
   CONSTRAINT fk_lciodocument_lcinterimorder FOREIGN KEY (lcinterimorder) REFERENCES eglc_lcinterimorder (id), 
   CONSTRAINT fk_lciodocument_filestoreid FOREIGN KEY (filestoreid) REFERENCES eg_filestoremap (id) 
);
CREATE SEQUENCE seq_eglc_lcio_document;
CREATE INDEX idx_lciodocument_lcinterimorder ON eglc_lcio_document (lcinterimorder);
CREATE INDEX idx_lciodocument_filestoreid ON eglc_lcio_document (filestoreid);

COMMENT ON TABLE eglc_lcio_document IS 'lcio Document tables';
COMMENT ON COLUMN eglc_lcio_document.lcinterimorder IS 'Foreign Key of eglc_lcinterimorder';
COMMENT ON COLUMN eglc_lcio_document.filestoreid IS 'Foreign Key of eg_filestoremap';
---------------------------END-------------------------------------------------------
---------------------------START-----------------------------------------------------
CREATE TABLE eglc_pwr_document
(
   pwr bigint NOT NULL,
   filestoreid bigint NOT NULL,
   pwrdocumenttype character varying(20) NOT NULL,
   CONSTRAINT fk_pwrdocument_pwr FOREIGN KEY (pwr) REFERENCES eglc_pwr (id), 
   CONSTRAINT fk_lciodocument_filestoreid FOREIGN KEY (filestoreid) REFERENCES eg_filestoremap (id) 
);
CREATE SEQUENCE seq_eglc_pwr_document;
CREATE INDEX idx_pwrdocument_pwr ON eglc_pwr_document (pwr);
CREATE INDEX idx_pwrdocument_filestoreid ON eglc_pwr_document (filestoreid);

COMMENT ON TABLE eglc_pwr_document IS 'pwr Document tables';
COMMENT ON COLUMN eglc_pwr_document.pwr IS 'Foreign Key of eglc_pwr ';
COMMENT ON COLUMN eglc_pwr_document.filestoreid IS 'Foreign Key of eg_filestoremap';
COMMENT ON COLUMN eglc_pwr_document. pwrdocumenttype IS 'Upload_PWR,UPLOAD_CA ';

---------------------------END-------------------------------------------------------
