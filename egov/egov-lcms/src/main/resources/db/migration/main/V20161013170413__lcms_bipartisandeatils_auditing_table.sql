---alter table in Bipartisan Details----
ALTER TABLE eglc_bipartisandetails ADD COLUMN createddate timestamp without time zone ;
ALTER TABLE eglc_bipartisandetails ADD COLUMN  lastmodifieddate timestamp without time zone ;
ALTER TABLE eglc_bipartisandetails ADD COLUMN createdby bigint ;
ALTER TABLE eglc_bipartisandetails ADD COLUMN lastmodifiedby bigint ;


----Auditing table for legalCase---

CREATE TABLE eglc_legalcase_aud
(
  id integer NOT NULL,
  rev integer NOT NULL,
  casenumber character varying(50) , 
  casedate date, 
  casetitle character varying(1024) , 
  appealnum  character varying(50), 
  court bigint , 
  casetype bigint , 
  remarks character varying(1024), 
  casereceivingdate date, 
  isfiledbycorporation boolean ,
  lcnumber character varying(50) , 
  respondentgovtdept bigint, 
  prayer character varying(1024), 
  issenioradvrequired boolean , 
  petitiontype bigint , 
  assigntoidboundary numeric,  
  opppartyadvocate character varying(128), 
  representedby character varying(256), 
  lcnumbertype character varying(256) ,  
  casefirstappearancedate date, 
  previousdate date,  
  petitionerappearancedate date, 
  stampnumber character varying(50), 
  officerincharge character varying(50),
  noticedate date,
  nextdate date,
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
);
ALTER TABLE ONLY eglc_legalcase_aud ADD CONSTRAINT pk_eglc_legalcase_aud PRIMARY KEY (id, rev);


------------Auditing table for BipartisanDetails---

CREATE TABLE  eglc_bipartisandetails_aud
(
  id bigint NOT NULL ,
  rev integer NOT NULL,
  name character varying(128)  ,
  address character varying(256),
  contactnumber character varying(20) ,
  legalcase bigint ,
  isrespondent boolean ,
  serialnumber numeric ,
  isrespondentgovernment boolean,
  respondentgovtdept bigint,
  lastmodifieddate timestamp without time zone  ,
  lastmodifiedby bigint ,
  revtype numeric
);
ALTER TABLE ONLY eglc_bipartisandetails_aud ADD CONSTRAINT pk_eglc_bipartisandetails_aud PRIMARY KEY (id, rev);
