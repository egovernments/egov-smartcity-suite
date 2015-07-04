ALTER TABLE EGPT_PROPERTY_MUTATION ADD COLUMN SALEDETAIL varchar(200);
CREATE TABLE EGPT_MUTATION_DOCS(
MUTATION bigint,
DOCUMENT bigint
);

CREATE SEQUENCE SEQ_EGPT_DOCUMENT;

CREATE TABLE EGPT_DOCUMENT(
 id numeric primary key,
 "type" numeric,
 description varchar(100),
 docdate date,
 enclosed boolean,
 createddate timestamp,
 createdby numeric,
 lastmodifieddate timestamp,
 lastmodifiedby numeric,
 version numeric
);

CREATE SEQUENCE SEQ_EGPT_DOCUMENT_TYPE;
CREATE TABLE EGPT_DOCUMENT_TYPE(
 id numeric primary key,
 name varchar(50),
 moduleName varchar(20),
 subModuleName varchar(50),
 mandatory boolean,
 version numeric
);

ALTER TABLE EG_USER RENAME GARDIAN TO GUARDIAN;
ALTER TABLE EG_USER RENAME GARDIANRELATION TO GUARDIANRELATION;