
CREATE TABLE egadtax_agency
(
  id bigint NOT NULL,
  code character varying(50) not null, 
  name character varying(250) not null, 
  ssid character varying(50) , 
  emailid character varying(100),
  mobilenumber character varying(10) not null,  
  address character varying(250), 
  STATUS bigint NOT NULL,
  createddate timestamp without time zone NOT NULL default CURRENT_DATE,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  version bigint,
CONSTRAINT pk_adtax_agency PRIMARY KEY (id),
CONSTRAINT unq_adtax_agency_code UNIQUE (code),
CONSTRAINT unq_adtax_agency_name UNIQUE (name),
CONSTRAINT fk_adtax_agency_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
CONSTRAINT fk_adtax_agency_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user (id),
CONSTRAINT FK_adtax_agency_STATUS FOREIGN KEY (STATUS) REFERENCES EGW_STATUS (ID)
);


CREATE INDEX IDX_adtax_agency_STATUS ON  egadtax_agency USING BTREE (STATUS);
CREATE INDEX IDX_adtax_agency_CODE ON  egadtax_agency USING BTREE (CODE);
CREATE INDEX IDX_adtax_agency_NAME ON  egadtax_agency USING BTREE (NAME);


CREATE TABLE egadtax_CATEGORY(
  id bigint NOT NULL,
  code character varying(50) not null, 
  name character varying(250) not null, 
  active boolean NOT NULL  DEFAULT TRUE,
  createddate timestamp without time zone NOT NULL default CURRENT_DATE,
  createdby bigint NOT NULL ,
  version bigint,
CONSTRAINT pk_adtax_CATEGORY PRIMARY KEY (id),
CONSTRAINT unq_adtax_CATEGORY_name UNIQUE (name),
CONSTRAINT fk_adtax_CATEGORY_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
CONSTRAINT unq_adtax_CATEGORY_CODE UNIQUE (CODE)
);


CREATE TABLE egadtax_subCATEGORY(
  id bigint NOT NULL,
  category bigint NOT NULL,
  code character varying(50) not null, 
  description character varying(512) not null, 
  active boolean NOT NULL  DEFAULT TRUE,
  createddate timestamp without time zone NOT NULL default CURRENT_DATE,
  createdby bigint NOT NULL ,
version bigint,
CONSTRAINT pk_adtax_subCATEGORY PRIMARY KEY (id),
CONSTRAINT unq_adtax_subCATEGORY_description UNIQUE (description),
CONSTRAINT unq_adtax_subCATEGORY_CODE UNIQUE (CODE),
CONSTRAINT fk_adtax_subCATEGORY_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id),
CONSTRAINT FK_adtax_subCATEGORY_category FOREIGN KEY (category) REFERENCES egadtax_CATEGORY (ID)
);

CREATE TABLE egadtax_UnitOfMeasure(
  id bigint NOT NULL,
  code character varying(50) not null, 
  description character varying(50) not null, 
  active boolean NOT NULL  DEFAULT TRUE,
 createdby bigint NOT NULL ,
createddate timestamp without time zone NOT NULL default CURRENT_DATE,
version bigint,
CONSTRAINT pk_adtax_UnitOfMeasure PRIMARY KEY (id),
CONSTRAINT fk_adtax_UnitOfMeasure_createdby FOREIGN KEY (createdby) REFERENCES eg_user (id)
);

create table egadtax_propertytype(
id bigint NOT NULL,
description character varying(50) not null, 
active boolean NOT NULL  DEFAULT TRUE,
version bigint,
CONSTRAINT pk_adtax_propertytype PRIMARY KEY (id)
);

create table egadtax_rates_Class(
id bigint NOT NULL,
description character varying(50) not null, 
active boolean NOT NULL  DEFAULT TRUE,
version bigint,
CONSTRAINT pk_adtax_ratesClass PRIMARY KEY (id)
);

create table egadtax_rates(
id bigint NOT NULL,
category bigint NOT NULL,
subcategory bigint NOT NULL,
unitofMeasure bigint not null,
active boolean NOT NULL  DEFAULT TRUE,
validfromdate timestamp without time zone NOT NULL default CURRENT_DATE, -- check
validtodate timestamp without time zone NOT NULL,
version bigint,
CONSTRAINT pk_adtax_rates PRIMARY KEY (id),
CONSTRAINT fk_adtax_rates_category FOREIGN KEY (category) REFERENCES egadtax_CATEGORY (id),
CONSTRAINT fk_adtax_rates_subcategory FOREIGN KEY (subcategory) REFERENCES egadtax_subCATEGORY (id),
CONSTRAINT fk_adtax_rates_uom FOREIGN KEY (unitofMeasure) REFERENCES egadtax_UnitOfMeasure (id), 
CONSTRAINT unq_adtax_rates_cat_subcat_uom UNIQUE (category,subcategory,unitofMeasure) --check
);

CREATE INDEX IDX_adtax_rates_category ON  egadtax_rates USING BTREE (category);
CREATE INDEX IDX_adtax_rates_subcategory ON  egadtax_rates USING BTREE (subcategory);
CREATE INDEX IDX_adtax_rates_unitofMeasure ON  egadtax_rates USING BTREE (unitofMeasure);


create table egadtax_rates_details(
id bigint NOT NULL,
class bigint not null,
unitfrom  bigint not null default 0,
unitto   bigint not null default 1,
rate bigint not null,
version bigint,
CONSTRAINT pk_adtax_ratesdetails PRIMARY KEY (id),
CONSTRAINT fk_adtax_ratesdetail_class FOREIGN KEY (class) REFERENCES egadtax_rates_Class (id), 
CONSTRAINT fk_adtax_ratesdetail_rate FOREIGN KEY (rate) REFERENCES egadtax_rates (id)
);

CREATE INDEX IDX_adtax_ratedtl_unitfrom ON  egadtax_rates_details USING BTREE (unitfrom);
CREATE INDEX IDX_adtax_ratedtl_unitto ON  egadtax_rates_details USING BTREE (unitto);

create table egadtax_revenueinspectors(
id bigint NOT NULL,
name character varying(50) not null, 
active boolean NOT NULL  DEFAULT TRUE,
version bigint,
CONSTRAINT pk_adtax_ri PRIMARY KEY (id)
);


create table egadtax_hoarding(
id bigint NOT NULL,
applicationNumber character varying(25) not null,  
applicationdate  timestamp without time zone not null,
permissionNumber character varying(25) not null, 
hoardingnumber character varying(25) not null, 
hoardingnAME character varying(125) not null, 
type character varying(25) not null, 
agency bigint not null,
Advertiser character varying(125)  not null, 
advertisement_particular character varying(512)  not null, 
propertyType bigint not null,
propertynumber character varying(50) ,
ownerdetail character varying(125) not null,
status bigint not null,
category bigint NOT NULL,
subcategory bigint NOT NULL,
measurement bigint not null,
unitofmeasure bigint not null,
length  bigint,
width bigint,
breadth bigint,
totalheight bigint,
class bigint,
revenueinspector bigint not null,
revenueboundary bigint,
adminboundry bigint not null,
address  character varying(512)  not null, 
advertisementduration character varying(25)  not null, 
taxamount bigint not null,
encroachmentfee bigint,
demandid bigint,
version bigint,
CONSTRAINT pk_adtax_hoarding PRIMARY KEY (id),
CONSTRAINT fk_adtax_hoarding_agency FOREIGN KEY (agency) REFERENCES egadtax_agency (id),
CONSTRAINT fk_adtax_hoarding_propertytype FOREIGN KEY (propertytype) REFERENCES egadtax_propertytype (id),
CONSTRAINT fk_adtax_hoarding_status FOREIGN KEY (status) REFERENCES egw_status (id),
CONSTRAINT fk_adtax_hoarding_category FOREIGN KEY (category) REFERENCES egadtax_CATEGORY (id),
CONSTRAINT fk_adtax_hoarding_subcategory FOREIGN KEY (subcategory) REFERENCES egadtax_subCATEGORY (id),
CONSTRAINT fk_adtax_hoarding_uom FOREIGN KEY (unitofmeasure) REFERENCES egadtax_UnitOfMeasure (id), 
CONSTRAINT fk_adtax_hoarding_class FOREIGN KEY (class) REFERENCES egadtax_rates_Class(id),
CONSTRAINT fk_adtax_hoarding_adminboundry FOREIGN KEY (adminboundry) REFERENCES eg_boundary(id),
CONSTRAINT fk_adtax_hoarding_revenueboundary FOREIGN KEY (revenueboundary) REFERENCES eg_boundary(id),
CONSTRAINT fk_adtax_hoarding_revenueinsptor FOREIGN KEY (revenueinspector) REFERENCES egadtax_revenueinspectors(id),
CONSTRAINT fk_adtax_hoarding_demand FOREIGN KEY (demandid) REFERENCES eg_demand(id)
);

CREATE INDEX IDX_adtax_hoarding_agency ON  egadtax_hoarding USING BTREE (agency);
CREATE INDEX IDX_adtax_hoarding_nAME ON  egadtax_hoarding USING BTREE (hoardingnAME);
CREATE INDEX IDX_adtax_hoarding_category ON  egadtax_hoarding USING BTREE (category);
CREATE INDEX IDX_adtax_hoarding_adminboundry ON  egadtax_hoarding USING BTREE (adminboundry);
CREATE INDEX IDX_adtax_hoarding_demandid ON  egadtax_hoarding USING BTREE (demandid);
CREATE INDEX IDX_adtax_hoarding_appNumber ON  egadtax_hoarding USING BTREE (applicationNumber);
CREATE INDEX IDX_adtax_hoarding_Number ON  egadtax_hoarding USING BTREE (hoardingnumber);


CREATE TABLE egadtax_document_type
(
  id numeric NOT NULL,
  name character varying(100),
  mandatory boolean,
  version numeric,
  transactiontype character varying(20),
  CONSTRAINT egadtax_document_type_pkey PRIMARY KEY (id)
  );
  
CREATE TABLE egadtax_document
(
  id numeric NOT NULL,
  doctype numeric,
  description character varying(100),
  docdate date,
  enclosed boolean,
  createddate timestamp without time zone,
  createdby numeric,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby numeric,
  version numeric,
  CONSTRAINT egadtax_document_pkey PRIMARY KEY (id),
  CONSTRAINT fk_adtax_document_doctype FOREIGN KEY (doctype) REFERENCES egadtax_document_type (id)
);



CREATE TABLE egadtax_hoarding_docs
(
  hoarding bigint,
  document bigint
);

CREATE SEQUENCE SEQ_egadtax_AGENCY;
CREATE SEQUENCE SEQ_egadtax_CATEGORY;
CREATE SEQUENCE SEQ_egadtax_SUBCATEGORY;
CREATE SEQUENCE SEQ_egadtax_UnitOfMeasure;
CREATE SEQUENCE SEQ_egadtax_propertytype;
CREATE SEQUENCE SEQ_egadtax_ratesClass;
CREATE SEQUENCE SEQ_egadtax_rates;
CREATE SEQUENCE SEQ_egadtax_ratesdetails;
CREATE SEQUENCE SEQ_egadtax_revenueinspectors;
CREATE SEQUENCE SEQ_egadtax_hoarding;
create sequence SEQ_egadtax_document;
create sequence SEQ_egadtax_document_TYPE;

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEAGENCY','ACTIVE',now(),'ACTIVE',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'ADVERTISEAGENCY','INACTIVE',now(),'INACTIVE',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'HOARDING','INACTIVE',now(),'INACTIVE',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'HOARDING','ACTIVE',now(),'ACTIVE',1);
 Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'HOARDING','CANCELLED',now(),'CANCELLED',1);


--rollback drop sequence SEQ_egadtax_AGENCY;
--rollback drop sequence SEQ_egadtax_CATEGORY;
--rollback drop sequence SEQ_egadtax_SUBCATEGORY;
--rollback drop sequence SEQ_egadtax_UnitOfMeasure;
--rollback drop sequence SEQ_egadtax_propertytype;
--rollback drop sequence SEQ_egadtax_ratesClass;
--rollback drop sequence SEQ_egadtax_rates;
--rollback DROP SEQUENCE SEQ_egadtax_ratesdetails;
--rollback DROP SEQUENCE SEQ_egadtax_revenueinspectors;
--rollback drop sequence SEQ_egadtax_hoarding;
--rollback drop sequence SEQ_egadtax_document;
--rollback drop sequence SEQ_egadtax_document_TYPE;


--rollback DROP TABLE egadtax_hoarding;
--rollback DROP TABLE egadtax_revenueinspectors;
--rollback DROP TABLE egadtax_rates_details;
--rollback DROP TABLE egadtax_rates;
--rollback DROP TABLE egadtax_rates_Class;
--rollback DROP TABLE egadtax_propertytype;
--rollback DROP TABLE egadtax_UnitOfMeasure;
--rollback DROP TABLE egadtax_subCATEGORY;
--rollback DROP TABLE egadtax_CATEGORY;
--rollback DROP TABLE egadtax_agency;
--rollback drop table egadtax_hoarding_docs;
--rollback drop table egadtax_document;
--rollback drop table egadtax_document_type;


