CREATE TABLE KNN_PAYROLL
(
  EMPNAME         VARCHAR2(256 BYTE)                NULL,
  FATHERNAME      VARCHAR2(256 BYTE)                NULL,
  DESIGNATION     VARCHAR2(100 BYTE)                NULL,
  PERMADD         VARCHAR2(256 BYTE)                NULL,
  LOCALADD        VARCHAR2(256 BYTE)                NULL,
  CADRE           VARCHAR2(100 BYTE)                NULL,
  SEX             VARCHAR2(2 BYTE)                  NULL,
  CASTE           VARCHAR2(50 BYTE)                 NULL,
  CLASS           VARCHAR2(20 BYTE)                  NULL,
  APPTTYPE        VARCHAR2(100 BYTE)                NULL,
  CURR_DEPT       VARCHAR2(50 BYTE)                 NULL,  
  FROMDATE        DATE                              NULL,
  TODATE          DATE                              NULL,
  BIRTHDATE       DATE                              NULL,
  APPTDATE        DATE                              NULL,
  INCRMONTH       VARCHAR2(20 BYTE)                 NULL,
  SCALE           VARCHAR2(20 BYTE)                 NULL,
  BASIC           NUMBER                            NULL,
  DA              NUMBER                            NULL,
  HRA             NUMBER                            NULL,
  CA              NUMBER                            NULL,
  OTHERALL        NUMBER                            NULL,
  VA              NUMBER                            NULL,
  TOTAL           NUMBER                            NULL,
  PF              NUMBER                            NULL,
  PFADVANCE       NUMBER                            NULL,
  GENINSURANCE    NUMBER                            NULL,
  BLDGDED         NUMBER                            NULL,
  VEHDEC          NUMBER                            NULL,
  IT              NUMBER                            NULL,
  BANKLOAN        VARCHAR2(50 BYTE)                 NULL,
  INSTAMT         NUMBER                            NULL,
  TOTALDED        NUMBER                            NULL,
  NETPAYMENT      NUMBER                            NULL,
  PAN             VARCHAR2(20 BYTE)                 NULL,
  BANKACC         VARCHAR2(50 BYTE)                 NULL,
  BANK            VARCHAR2(100 BYTE)                NULL,
  IDENTIFICATION  VARCHAR2(256 BYTE)                NULL,
  NOMINEES        VARCHAR2(256 BYTE)                NULL,
  EMPID           NUMBER                            NULL,
  REPORTSTO       VARCHAR2(100 BYTE)                NULL,
  INCRDATE        DATE                              NULL,
  BANK_BRANCH 	  varchar2(100 BYTE)	 	    NULL,	
  CURR_SECTION    VARCHAR2(256 BYTE)                NULL
 
 
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;

ALTER TABLE knn_payroll ADD (EMPSLNO     NUMBER  NULL);

update knn_payroll set empslno=empid where empslno is null;

ALTER TABLE KNN_PAYROLL MODIFY (EMPSLNO NOT NULL, EMPID NOT NULL, CURR_DEPT NOT NULL, CURR_SECTION NOT NULL);

ALTER TABLE KNN_PAYROLL ADD (
   CONSTRAINT UNQ_KNN_EMP_REC UNIQUE (EMPSLNO,CURR_DEPT,CURR_SECTION));


CREATE TABLE TMP_SALARYCODES
(
  ID NUMBER NULL,
  HEAD      VARCHAR2(100 BYTE) NULL,
  CATEGORY    VARCHAR2(100 BYTE)                  NULL,
  DESCRIPTION  VARCHAR2(100 BYTE)                  NULL,
  TYPE    VARCHAR2(100 BYTE)                  NULL,
  IS_TAXABLE    VARCHAR2(2 BYTE)                  NULL,
  CAL_TYPE  VARCHAR2(100 BYTE)                  NULL,
  GLCODE  VARCHAR2(100 BYTE)                  NULL,
  IS_RECOVERY VARCHAR2(2 BYTE) NULL
)
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;

DROP SEQUENCE SEQ_KNN_EMP_TMP;
CREATE SEQUENCE SEQ_KNN_EMP_TMP INCREMENT BY 1 START WITH 250 NOCYCLE NOCACHE NOORDER;

ALTER TABLE KNN_PAYROLL MODIFY (SCALE VARCHAR2(100 BYTE));

CREATE TABLE TMP_DEPT_SEC_MAPPING
(
  OLD_DEPTNAME         VARCHAR2(256 BYTE)                NULL,
  OLD_SECTIONNAME         VARCHAR2(256 BYTE)                NULL,
  DEPTNAME      VARCHAR2(256 BYTE)                NULL,
  SECTIONNAME      VARCHAR2(256 BYTE)                NULL
);
