------pwr-----
CREATE TABLE eglc_pwr_aud 
(	
    id bigint NOT NULL ,
    rev integer NOT NULL,
    cafilingdate date, 
    legalcase bigint , 
    caduedate date, 
    pwrduedate date, 
    pwrapprovaldate date,
    lastmodifieddate timestamp without time zone  ,
    lastmodifiedby bigint ,
    revtype numeric
 );
ALTER TABLE ONLY eglc_pwr_aud  ADD CONSTRAINT pk_eglc_pwr_aud  PRIMARY KEY (id, rev);

ALTER TABLE eglc_pwr ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_pwr ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_pwr ADD COLUMN createdby bigint ;
ALTER TABLE eglc_pwr ADD COLUMN lastmodifiedby bigint ;

------------------------------------------------------------------------------------------
----counter_affidavit--------------
CREATE TABLE eglc_counter_affidavit_aud 
(	
   id bigint NOT NULL ,
   rev integer NOT NULL,
   legalcase bigint , 
   counterAffidavitduedate date, 
   counterAffidavitapprovaldate date,
   lastmodifieddate timestamp without time zone  ,
   lastmodifiedby bigint ,
   revtype numeric
 );

ALTER TABLE ONLY eglc_counter_affidavit_aud  ADD CONSTRAINT pk_eglc_counter_affidavit_aud  PRIMARY KEY (id, rev);

ALTER TABLE eglc_counter_affidavit ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_counter_affidavit ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_counter_affidavit ADD COLUMN createdby bigint ;
ALTER TABLE eglc_counter_affidavit ADD COLUMN lastmodifiedby bigint ;
-------------------------------------------------------------
-----legalcase_dept----
CREATE TABLE eglc_legalcase_dept_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  legalcase bigint  , 
  dateofreceiptofpwr date, 
  isprimarydepartment boolean, 
  assignon date, 
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
 );
ALTER TABLE ONLY eglc_legalcase_dept_aud  ADD CONSTRAINT pk_eglc_legalcase_dept_aud  PRIMARY KEY (id, rev);

ALTER TABLE eglc_legalcase_dept ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_legalcase_dept ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_legalcase_dept ADD COLUMN createdby bigint ;
ALTER TABLE eglc_legalcase_dept ADD COLUMN lastmodifiedby bigint ;

----------------------------------------------------------------------
-----lcInteriomOrder---------------------------

CREATE TABLE eglc_lcinterimorder_aud
(
   id bigint NOT NULL ,
   rev integer NOT NULL,
   iodate date  , 
   mpnumber character varying(50),  
   notes character varying(1024),  
   interimorder bigint, 
   legalcase bigint, 
   sendtostandingcounsel date, 
   petitionfiledon date, 
   reportfilingdue date, 
   senttodepartment date, 
   reportfromhod date, 
   reportsendtostandingcounsel date, 
   reportfilingdate date,   
   referencenumber character varying(50), 
   lastmodifieddate timestamp without time zone  ,
   lastmodifiedby bigint ,
   revtype numeric
);
ALTER TABLE ONLY eglc_lcinterimorder_aud  ADD CONSTRAINT pk_eglc_lcinterimorder_aud  PRIMARY KEY (id, rev);
------------------------------------------------
----------vacatestay_petition --------------
CREATE TABLE eglc_vacatestay_petition_aud
(	
   id bigint NOT NULL ,
   rev integer NOT NULL,
   lcinterimorder bigint NOT NULL, 
   receivedfromstandingcounsel date, 
   sendtostandingcounsel date, 
   petitionfiledon date NOT NULL , 
   remarks character varying(1024), 
   lastmodifieddate timestamp without time zone  ,
   lastmodifiedby bigint ,
   revtype numeric
);
ALTER TABLE ONLY eglc_vacatestay_petition_aud  ADD CONSTRAINT pk_eglc_vacatestay_petition_aud  PRIMARY KEY (id, rev);
-------------------------------------------------------------------
----hearings-----
CREATE TABLE eglc_hearings_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  hearingdate date ,
  legalcase bigint , 
  isstandingcounselpresent boolean , 
  additionallawyers character varying(256),
  isseniorstandingcounselpresent boolean, 
  hearingoutcome character varying(2056), 
  purposeofhearing character varying(1024),
  referencenumber character varying(50),
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
);
ALTER TABLE ONLY eglc_hearings_aud  ADD CONSTRAINT pk_eglc_hearings_aud  PRIMARY KEY (id, rev);
---------------------------------------------
----employee_hearing----
CREATE TABLE eglc_employeehearing_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  employee bigint,
  hearing bigint,
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
);
ALTER TABLE ONLY eglc_employeehearing_aud  ADD CONSTRAINT pk_eglc_employeehearing_aud  PRIMARY KEY (id, rev);

ALTER TABLE eglc_employeehearing ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_employeehearing ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_employeehearing ADD COLUMN createdby bigint ;
ALTER TABLE eglc_employeehearing ADD COLUMN lastmodifiedby bigint ;
----------------------------------------------------
-----judgment------
CREATE TABLE eglc_judgment_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  orderdate date ,
  senttodepton   date,
  implementbydate  date,
  costawarded   double precision,
  compensationawarded double precision,
  judgmentdetails  character varying(256),
  advisorfee  double precision,
  arbitratorfee  double precision,
  enquirydetails  character varying(256),
  enquirydate    date,
  setasidepetitiondate   date,
  setasidepetitiondetails  character varying(256),
  legalcase bigint ,
  judgmenttype bigint,
  saphearingdate date,
  issapaccepted boolean ,
  parent bigint,
  ismemorequired boolean,
  certifiedmemofwddate date,
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
);
ALTER TABLE ONLY eglc_judgment_aud  ADD CONSTRAINT pk_eglc_judgment_aud  PRIMARY KEY (id, rev);
--------------------------------------------------------------
-----judgmentIMpl-----
CREATE TABLE eglc_judgmentimpl_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  iscompiled character varying(20),
  dateofcompliance  date,
  compliancereport  character varying(256),
  judgment bigint ,
  reason character varying(256),
  implementationdetails character varying(256),
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
); 
ALTER TABLE ONLY eglc_judgmentimpl_aud  ADD CONSTRAINT pk_eglc_judgmentimpl_aud  PRIMARY KEY (id, rev);
----------------------------------------------------

----Appeal----
CREATE TABLE eglc_appeal_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  srnumber  character varying(50),
  appealfiledon  date,
  appealfiledby character varying(100),
  judgmentimpl  bigint ,
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
);
ALTER TABLE ONLY eglc_appeal_aud  ADD CONSTRAINT pk_eglc_appeal_aud  PRIMARY KEY (id, rev);

ALTER TABLE eglc_appeal ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_appeal ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_appeal ADD COLUMN createdby bigint ;
ALTER TABLE eglc_appeal ADD COLUMN lastmodifiedby bigint ;
----------------------------------------------------
----contempt------
CREATE TABLE  eglc_contempt_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  canumber character varying(50),
  receivingdate  date,
  iscommapprrequired boolean ,
  commappdate  date ,
  judgmentimpl bigint,
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
);
ALTER TABLE ONLY eglc_contempt_aud  ADD CONSTRAINT pk_eglc_contempt_aud  PRIMARY KEY (id, rev);

ALTER TABLE eglc_contempt ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_contempt ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_contempt ADD COLUMN createdby bigint ;
ALTER TABLE eglc_contempt ADD COLUMN lastmodifiedby bigint ;
---------------------------------------------
-------legalcasedisposal-------
CREATE TABLE eglc_legalcasedisposal_aud
(
   id bigint NOT NULL ,
   rev integer NOT NULL,
   disposaldate date, 
   disposaldetails character varying(1024), 
   consignmenttorecordroomdate date, 
   legalcase bigint, 
   lastmodifieddate timestamp without time zone  ,
   lastmodifiedby bigint ,
   revtype numeric
);
ALTER TABLE ONLY eglc_legalcasedisposal_aud  ADD CONSTRAINT pk_eglc_legalcasedisposal_aud  PRIMARY KEY (id, rev);
--------------------------------------------------
-----legalcase_advocate--------
CREATE TABLE eglc_legalcase_advocate_aud
(
   id bigint NOT NULL ,
   rev integer NOT NULL, 
   advocatemaster bigint , 
   assignedtodate date, 
   vakalatdate date, 
   legalcase bigint , 
   isactive boolean , 
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
   changeadvocate boolean , 
   changesenioradvocate boolean , 
   lastmodifieddate timestamp without time zone  ,
   lastmodifiedby bigint ,
   revtype numeric
); 
ALTER TABLE ONLY eglc_legalcase_advocate_aud  ADD CONSTRAINT pk_eglc_legalcase_advocate_aud  PRIMARY KEY (id, rev);

ALTER TABLE eglc_legalcase_advocate ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_legalcase_advocate ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_legalcase_advocate ADD COLUMN createdby bigint ;
ALTER TABLE eglc_legalcase_advocate ADD COLUMN lastmodifiedby bigint ;

------------------------------------------------------------------