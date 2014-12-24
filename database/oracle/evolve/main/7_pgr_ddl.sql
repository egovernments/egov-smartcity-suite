#UP

CREATE SEQUENCE  SEQ_EGGR_CATEGORY  ;

CREATE SEQUENCE  SEQ_EGGR_PRIORITY    ;

CREATE SEQUENCE  SEQ_EGGR_COMPLAINTRMODES    ;

alter table EGGR_COMPLAINTDETAILS add  IMAGE_FILENAME varchar2(50);

alter table EGGR_COMPLAINTDETAILS add LATITUDE NUMBER(20,18);

alter table EGGR_COMPLAINTDETAILS add LONGITUDE NUMBER(20,18);

alter table EGGR_COMPLAINTDETAILS add  DEVICEID VARCHAR2(100);

alter table eggr_complaintdetails add geoLocInfo1 varchar2(1024);
alter table eggr_complaintdetails add geoLocInfo2 varchar2(1024);
alter table eggr_complaintdetails add geoLocInfo3 varchar2(1024);
alter table eggr_complaintdetails add geoLocInfo4 varchar2(1024);
alter table eggr_complaintdetails add geoLocInfoUrl1 varchar2(1024);
alter table eggr_complaintdetails add geoLocInfoUrl2 varchar2(1024);

CREATE SEQUENCE  SEQ_EGGR_REDRESSALDETAILS   ;

alter table eggr_complaintdetails drop column image_filename;

alter table eggr_complaintdetails add  image_document_number varchar2(50);



alter table eggr_complaintdetails add  ID_STATE NUMBER ;

alter table eggr_complaintdetails add CONSTRAINT FK_COMPLAINTDETAILS_STATE  FOREIGN KEY (ID_STATE) REFERENCES EG_WF_STATES(ID);

alter table eggr_complaintdetails add CREATED_BY NUMBER;
alter table eggr_complaintdetails add MODIFIED_BY NUMBER;
alter table eggr_complaintdetails add CREATED_DATE DATE;
alter table eggr_complaintdetails add MODIFIED_DATE DATE;



alter table eg_router add  postitionid number(32,0);
alter table eg_router add CONSTRAINT FK_ROUTER_POSITION  FOREIGN KEY (postitionid) REFERENCES eg_position(ID);



alter table eggr_redressaldetails add postitionid number(32,0);
alter table eggr_redressaldetails add CONSTRAINT FK_REDRESSAL_POSITION  FOREIGN KEY (postitionid) REFERENCES eg_position(ID);

alter table eg_router rename column postitionid to positionid;

CREATE SEQUENCE  SEQ_EGGR_COMPLAINTSTATUS   ;
DROP SEQUENCE seq_eggr_complaintstatus;
CREATE SEQUENCE seq_eggr_complaintstatus  ;
#DOWN




drop sequence SEQ_EGGR_COMPLAINTRMODES ;

drop sequence SEQ_EGGR_PRIORITY;

drop sequence SEQ_EGGR_CATEGORY;


alter table EGGR_COMPLAINTDETAILS drop column IMAGE_FILENAME;

alter table EGGR_COMPLAINTDETAILS drop column LATITUDE;

alter table EGGR_COMPLAINTDETAILS drop column LONGITUDE;

alter table EGGR_COMPLAINTDETAILS drop column DEVICEID;

alter table EGGR_COMPLAINTDETAILS drop column geoLocInfo1;

alter table EGGR_COMPLAINTDETAILS drop column geoLocInfo2;

alter table EGGR_COMPLAINTDETAILS drop column geoLocInfo3;

alter table EGGR_COMPLAINTDETAILS drop column geoLocInfo4;

alter table EGGR_COMPLAINTDETAILS drop column geoLocInfoUrl1;

alter table EGGR_COMPLAINTDETAILS drop column geoLocInfoUrl2;

drop sequence SEQ_EGGR_REDRESSALDETAILS;


alter table eggr_complaintdetails drop column image_document_number;

alter table eggr_complaintdetails add column image_filename varchar2(50);

alter table eggr_complaintdetails drop column CREATED_BY , MODIFIED_BY ,CREATED_DATE ,MODIFIED_DATE;

alter table eggr_complaintdetails drop CONSTRAINT FK_COMPLAINTDETAILS_STATE;

alter table eggr_complaintdetails drop column ID_STATE;


alter table  eggr_redressaldetails drop CONSTRAINT FK_REDRESSAL_POSITION;
alter table  eggr_redressaldetails drop column postitionid;

alter table  eg_router drop CONSTRAINT FK_ROUTER_POSITION;
alter table  eg_router drop column postitionid;
alter table eg_router rename column positionid to postitionid;
drop sequence SEQ_EGGR_REDRESSALDETAILS;
DROP SEQUENCE seq_eggr_complaintstatus;
