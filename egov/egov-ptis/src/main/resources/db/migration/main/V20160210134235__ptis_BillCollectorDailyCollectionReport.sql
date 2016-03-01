create sequence SEQ_BILLCOLLECTORWISE_REPORT;
create sequence SEQ_BILLCOLLCTOR_DETAIL;
create sequence SEQ_BILLCOLLCTOR_BLOCKDETAIL;
create sequence SEQ_BCREPORT_COLLECTION;


create table EGPT_BILLCOLLECTORWISE_REPORT (
ID bigint NOT NULL,
DISTRICT character varying(50) NOT NULL,
ULBNAME character varying(50) NOT NULL,
ULBCODE character varying(50) NOT NULL,
schemaname character varying(50) NOT NULL,
Last_calculated_date Date,
isactive boolean NOT NULL DEFAULT true);

create table EGPT_BILLCOLLCTOR_DETAIL(
ID bigint NOT NULL,
BCREPORTID bigint NOT NULL,
CollectorName character varying(125) NOT NULL,
employeeCode character varying(25) ,
mobilenumber character varying(12) ,
TAXINSPECTOR_NAME character varying(125) ,
TAXINSPECTOR_MOBILENUMBER character varying(12) ,
TAXINSPECTOR_EMPLOYEECODE character varying(25) ,
RevenueOfficer_Name character varying(125) ,
RevenueOfficer_mobilenumber character varying(12) ,
RevenueOfficer_employeeCode character varying(25) ,
Commissioner_Name character varying(125) ,
Commissioner_MobileNumber character varying(12) ,
Commissioner_employeecode character varying(25) ,
isactive boolean NOT NULL DEFAULT true
);

create table EGPT_BILLCOLLCTOR_BLOCKDETAIL (
ID bigint NOT NULL,
BILLCOLLECTOR  bigint NOT NULL,
zoneid  bigint NOT NULL,
wardid   bigint NOT NULL,
blockid  bigint NOT NULL,
zonename  character varying(50),
wardname character varying(50),
blockname   character varying(50),
totalAssessments  bigint NOT NULL,
isactive boolean NOT NULL DEFAULT true);



create table EGPT_BCREPORT_COLLECTION (
ID bigint NOT NULL,
DATE date NOT NULL,
BILLCOLLECTOR bigint NOT NULL,
TARGET_ARREARS_DEMAND double precision DEFAULT 0, 
TARGET_CURRENT_DEMAND double precision DEFAULT 0,
TODAY_ARREARS_COLLECTION double precision DEFAULT 0,
TODAY_CURRENTYEAR_COLLECTION double precision DEFAULT 0,
CUMMULATIVE_ARREARS_COLLECTION double precision DEFAULT 0,
CUMMULATIVE_CURRENTYEAR_COLLECTION double precision DEFAULT 0,
LASTYEAR_COLLECTION double precision DEFAULT 0,
LASTYEAR_CUMMULATIVE_COLLECTION double precision DEFAULT 0
);



alter table EGPT_BILLCOLLECTORWISE_REPORT add constraint pk_EGPT_BILLCOLLECTORWISE_REPORT primary key (id);
ALTER TABLE ONLY EGPT_BILLCOLLECTORWISE_REPORT ADD CONSTRAINT unq_billcollr_ulb  UNIQUE (DISTRICT, ULBNAME);

alter table EGPT_BILLCOLLCTOR_DETAIL add constraint pk_EGPT_BILLCOLLCTOR_DETAIL primary key (id);
ALTER TABLE EGPT_BILLCOLLCTOR_DETAIL ADD CONSTRAINT fk_EGPT_Blcltrdtl_bcrreport FOREIGN KEY (BCREPORTID) REFERENCES EGPT_BILLCOLLECTORWISE_REPORT (id);

ALTER TABLE EGPT_BILLCOLLCTOR_BLOCKDETAIL ADD CONSTRAINT pk_EGPT_BILLCOLLCTOR_BLOCKDETAIL PRIMARY KEY (id);
ALTER TABLE EGPT_BILLCOLLCTOR_BLOCKDETAIL ADD CONSTRAINT fk_EGPT_Blcltr_block_billColl FOREIGN KEY (BILLCOLLECTOR) REFERENCES EGPT_BILLCOLLCTOR_DETAIL (id);
ALTER TABLE ONLY EGPT_BILLCOLLCTOR_BLOCKDETAIL ADD CONSTRAINT unq_billcoll_blkDtl  UNIQUE (BILLCOLLECTOR, zoneid,wardid,blockid);

ALTER TABLE EGPT_BCREPORT_COLLECTION ADD CONSTRAINT pk_EGPT_BCREPORT_COLLECTION PRIMARY KEY (id);
ALTER TABLE EGPT_BCREPORT_COLLECTION ADD CONSTRAINT fk_EGPT_BCREPORT_billCollr FOREIGN KEY (BILLCOLLECTOR) REFERENCES EGPT_BILLCOLLCTOR_DETAIL (id);



Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'dailyBCCollection-form','/reports/billcollectorDailyCollectionReport-form', null,(select id from EG_MODULE where name = 'PTIS-Reports'),1,'Billcollector wise daily Collection report','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'dailyBCCollection-result','/reports/billcollectorDailyCollectionReportList', null,(select id from EG_MODULE where name = 'PTIS-Reports'),null,null,'f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


