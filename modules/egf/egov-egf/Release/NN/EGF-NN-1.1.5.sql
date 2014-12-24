show user;
-- remove Administration link from menutree
delete from eg_roleaction_map where actionid in (79); 

-- Infrastructure.2.0.8.sql starts
ALTER TABLE RECEIPTHEADER ADD MANUALRECEIPTNUMBER VARCHAR(10);
-- Infrastructure.2.0.8.sql ends

-- Infrastructure.2.0.10.sql starts
alter table bankaccount add(payto varchar(100));

ALTER TABLE EG_BILLREGISTERMIS ADD (PAYTO  VARCHAR2(250));

ALTER TABLE CHEQUEDETAIL ADD (STATUS NUMBER);
ALTER TABLE chequedetail RENAME COLUMN status TO chqstatus;

alter table tds add(caplimit numeric(15,2));

-- Infrastructure.2.0.10.sql ends


-- Infrastructure.2.1.sql starts
-- specify the user name of DB by removing the <instance name> tag
-- grant create sequence ,alter any sequence,select any sequence to <instancename>;

CREATE OR REPLACE  PROCEDURE recreate_sequence (seqname IN VARCHAR2, assoc_table IN VARCHAR2, assoc_col IN VARCHAR2) as
   max_val INTEGER;
   seq_existing VARCHAR2(75);
   bExists INTEGER;
BEGIN

   EXECUTE IMMEDIATE 'SELECT NVL(MAX(' ||assoc_col ||'),0) FROM ' || assoc_table INTO max_val;
   begin
    bExists := 1;
 	select sequence_name INTO seq_existing from user_sequences where sequence_name like seqname;
 	exception
 	WHEN NO_DATA_FOUND then
 		bExists:= 0;
 	end;
   
   IF (bExists) != 0  THEN
   	  EXECUTE IMMEDIATE 'DROP SEQUENCE ' || seqname;
   END IF;
    max_val:= max_val + 1;
    dbms_output.put_line('maxval ->'|| max_val);
	
   EXECUTE IMMEDIATE 'CREATE SEQUENCE ' || seqname || ' START WITH ' || max_val || ' MINVALUE ' || max_val || ' INCREMENT BY 1 NOCYCLE NOCACHE NOORDER' ;
   dbms_output.put_line('SEQUENCE created ->'|| seqname);
   commit;
 END;
/

ALTER TABLE EG_ACTION 
ADD (URL VARCHAR(70),
     QUERYPARAMS VARCHAR(70),
     URLORDERID NUMBER);

ALTER TABLE EG_MODULE
ADD (ISENABLED NUMBER(1),
     MODULE_NAMELOCAL VARCHAR2(128),
     BASEURL VARCHAR2(256),
     PARENTID NUMBER);
     
/****** Change on 07/02/2008 **************/
ALTER TABLE EG_HEIRARCHY_TYPE
  ADD TYPE_CODE VARCHAR2(50);

-- insert values into type_code before making it unique not null.  
UPDATE EG_HEIRARCHY_TYPE
SET TYPE_CODE = substr(TYPE_NAME, 1, 4) || ID_HEIRARCHY_TYPE
WHERE TYPE_CODE IS NULL;

ALTER TABLE EG_HEIRARCHY_TYPE MODIFY (TYPE_CODE UNIQUE NOT NULL);


/****** User related table changes start *************/
ALTER TABLE EG_USER 
ADD (FROMDATE       DATE,
  TODATE         DATE);

UPDATE EG_USER SET FROMDATE='01-Jan-1990'
WHERE FROMDATE IS NULL;

UPDATE EG_USER SET TODATE='01-Jan-2999'
WHERE TODATE IS NULL;

ALTER TABLE EG_USERROLE 
ADD (ID INTEGER,
  FROMDATE    DATE,
  TODATE      DATE,
  IS_HISTORY  CHAR(1 BYTE)  DEFAULT 'N' );
  
UPDATE EG_USERROLE
SET ID=rownum,
FROMDATE='01-Jan-1990';

ALTER TABLE EG_USERROLE MODIFY (ID NOT NULL);
  
  ALTER TABLE EG_USERROLE DROP CONSTRAINT PK_USERROLE;
  
  ALTER TABLE EG_USERROLE ADD (
      CONSTRAINT PK_USER_ROLE PRIMARY KEY (ID)
        USING INDEX 
        TABLESPACE USERS
        PCTFREE    10
        INITRANS   2
        MAXTRANS   255
        STORAGE    (
                    INITIAL          64K
                    MINEXTENTS       1
                    MAXEXTENTS       2147483645
                    PCTINCREASE      0
                   ));
    
    
    ALTER TABLE EG_USERROLE ADD (
      CONSTRAINT FK_USER_USERROLE FOREIGN KEY (ID_USER) 
        REFERENCES EG_USER (ID_USER));
    
    ALTER TABLE EG_USERROLE ADD (
      CONSTRAINT FK_ROLE_USERROLE FOREIGN KEY (ID_ROLE) 
        REFERENCES EG_ROLES (ID_ROLE));
    
commit;	
	
call recreate_sequence('SEQ_EG_USERROLE' , 'EG_USERROLE' , 'ID' );

/****** User related table changes end *************/

/****** Change on 14/02/2008 **************/

/****** City website table changes start *************/

ALTER TABLE EG_CITY_WEBSITE
ADD(ID   NUMBER,
  ISACTIVE  INTEGER DEFAULT 1);
  
  UPDATE EG_CITY_WEBSITE 
  SET ID = ROWNUM
  WHERE ID IS NULL;
  
ALTER TABLE EG_CITY_WEBSITE MODIFY (ID NOT NULL);
  
  
 ALTER TABLE EG_CITY_WEBSITE ADD (
    CONSTRAINT PK_CITYWEBSITE PRIMARY KEY (ID)
     );

  
 call recreate_sequence ('SEQ_EG_CITY', 'EG_CITY_WEBSITE' , 'ID');
  

/****** Change on 18/02/2008 **************/
  
  ALTER TABLE EG_ROLES
  DROP COLUMN REPORTSTO;
  
   ALTER TABLE EG_ROLES DROP CONSTRAINT FK_DEPT_ROLES; 
    
  ALTER TABLE EG_ROLES DROP CONSTRAINT UQ_NAME_DEPT; 
  
  ALTER TABLE EG_ROLES
  DROP COLUMN ID_DEPT;
  


/******* Change on 11/03/2008 - for position and hierarchy *******/
CREATE TABLE EG_OBJECT_TYPE
(
  ID                NUMBER,
  TYPE              VARCHAR2(20 BYTE)           NOT NULL,
  DESCRIPTION       VARCHAR2(50 BYTE),
  LASTMODIFIEDDATE  DATE                        NOT NULL
);



ALTER TABLE EG_OBJECT_TYPE ADD (
  PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));


CREATE TABLE EG_POSITION
(
  POSITION_NAME     VARCHAR2(256 BYTE)          NOT NULL,
  ID                NUMBER(32),
  SANCTIONED_POSTS  NUMBER(32),
  OUTSOURCED_POSTS  NUMBER(32),
  DESIG_ID          NUMBER(32),
  EFFECTIVE_DATE    DATE
);

ALTER TABLE EG_POSITION ADD (
  CONSTRAINT PK_EG_POSITION PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));


CREATE TABLE EG_POSITION_HIR
(
  ID              NUMBER(32),
  POSITION_FROM   NUMBER(32),
  POSITION_TO     NUMBER(32),
  OBJECT_TYPE_ID  NUMBER(32)
);

ALTER TABLE EG_POSITION_HIR ADD (
  CONSTRAINT PK_EG_POSITION_HIR PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));


ALTER TABLE EG_POSITION_HIR ADD (
  CONSTRAINT OBJ_TYPE FOREIGN KEY (OBJECT_TYPE_ID) 
    REFERENCES EG_OBJECT_TYPE (ID));

ALTER TABLE EG_POSITION_HIR ADD (
  CONSTRAINT POS_FROM FOREIGN KEY (POSITION_FROM) 
    REFERENCES EG_POSITION (ID));

ALTER TABLE EG_POSITION_HIR ADD (
  CONSTRAINT POS_TO FOREIGN KEY (POSITION_TO) 
    REFERENCES EG_POSITION (ID));


/********* jurisdiction values*************/

ALTER TABLE EG_USER_JURVALUES DROP CONSTRAINT PK_USER_JURVALUES;

ALTER TABLE EG_USER_JURVALUES
  ADD FROMDATE DATE
  ADD TODATE DATE
  ADD ID NUMBER(32);
  
 UPDATE EG_USER_JURVALUES 
 SET ID= ROWNUM, FROMDATE = TO_Date('01/04/2000', 'DD/MM/YYYY');
  
ALTER TABLE EG_USER_JURVALUES
      MODIFY (ID_USER_JURLEVEL   NOT NULL ,
      ID_BNDRY   NOT NULL ,
      FROMDATE   NOT NULL ,
	  ID   NOT NULL);
 
 ALTER TABLE EG_USER_JURVALUES ADD (IS_HISTORY CHAR(1) DEFAULT 'N' NOT NULL);
  
   
CREATE UNIQUE INDEX PK_USER_JUR_LEVEL ON EG_USER_JURVALUES
  (ID);
 
call recreate_sequence ('SEQ_EG_USER_JURVALUES' , 'EG_USER_JURVALUES', 'ID' );
  
  
ALTER TABLE EG_USER_JURVALUES ADD (
  CONSTRAINT PK_USER_JUR_VALUES PRIMARY KEY (ID));
  
  
/***** Boundary changes**********/    

ALTER TABLE EG_BOUNDARY ADD 
 	  (FROMDATE DATE,
       TODATE DATE);
	  
UPDATE EG_BOUNDARY
SET FROMDATE=TO_Date('01/04/1990', 'DD/MM/YYYY')
WHERE FROMDATE IS NULL;

ALTER TABLE EG_BOUNDARY MODIFY
	  (FROMDATE NOT NULL);  
  
ALTER TABLE EG_BOUNDARY ADD 
	  (IS_HISTORY CHAR(1) DEFAULT 'N' NOT NULL);
  
  
ALTER TABLE EG_BOUNDARY
  ADD BNDRYID NUMBER;

ALTER TABLE EG_BOUNDARY_TYPE
	  DROP CONSTRAINT BNDRY_HIERARCHY_UNIQUE;
  
ALTER TABLE EG_BOUNDARY_TYPE
  ADD BNDRYNAME_LOCAL VARCHAR2(64);
  
CREATE UNIQUE INDEX BNDRYHIRAR_HIRARTYPE_UNIQUE ON EG_BOUNDARY_TYPE
  (ID_HEIRARCHY_TYPE, HIERARCHY)
  LOGGING
  TABLESPACE USERS
  PCTFREE    10
  INITRANS   2
  MAXTRANS   255
  STORAGE    (
              INITIAL          64K
              MINEXTENTS       1
              MAXEXTENTS       2147483645
              PCTINCREASE      0
              BUFFER_POOL      DEFAULT
             )
NOPARALLEL;


ALTER TABLE EG_BOUNDARY_TYPE ADD (
  CONSTRAINT BNDRYHIRAR_HIRARTYPE_UNIQUE UNIQUE (ID_HEIRARCHY_TYPE, HIERARCHY)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));
 
 call recreate_sequence('SEQ_EG_BNDRY_TYPE', 'EG_BOUNDARY_TYPE', 'ID_BNDRY_TYPE');


/*********** changeon 14-mar-2008 to **********/

alter table eg_action add  (MODULE_ID NUMBER, ORDER_NUMBER NUMBER,
DISPLAY_NAME     VARCHAR2(80 BYTE),
 IS_ENABLED       NUMBER(1),
 ACTION_HELP_URL  VARCHAR2(255 BYTE)); 
 
ALTER TABLE eg_module ADD
	  (MODULE_DESC VARCHAR2(256 BYTE),
  	   ORDER_NUM NUMBER);

call recreate_sequence('SEQ_EG_ACTION' , 'EG_ACTION' , 'ID');
call recreate_sequence('SEQ_MODULEMASTER' , 'EG_MODULE' , 'ID_MODULE');


CREATE OR REPLACE VIEW V_EG_ROLE_ACTION_MODULE_MAP
(MODULE_ID, MODULE_NAME, PARENT_ID, ACTION_ID, ACTION_NAME, 
 ACTION_URL, ORDER_NUMBER, TYPEFLAG, IS_ENABLED)
AS 
select m.ID_MODULE as module_id,  m.module_name as module_name, 
m.PARENTID as parent_id,  null, null, null, 
m.ORDER_NUM as order_number, 'M', m.ISENABLED as is_enabled
from eg_module m
union
select null,  null, 
a.MODULE_ID as parent_id,  a.ID as action_id, a.DISPLAY_NAME as action_name, a.URL || decode(a.QUERYPARAMS, null, '', '?' || a.QUERYPARAMS) as action_url, 
a.ORDER_NUMBER as order_number, 'A', a.IS_ENABLED as is_enabled
from eg_action a
/

/************* change on 17 mar 2008 ********************/

ALTER TABLE eg_numbers ADD(MONTH number);


/********** created eg_object_history table***************/

call recreate_sequence('SEQ_OBJECT_TYPE' , 'EG_OBJECT_TYPE' , 'ID');
  

CREATE TABLE EG_OBJECT_HISTORY
(
  ID NUMBER NOT NULL,
  OBJECT_TYPE_ID NUMBER,
  MODIFED_BY NUMBER,
  OBJECT_ID NUMBER,
  REMARKS VARCHAR2(56),
  MODIFIEDDATE DATE,
  CONSTRAINT PK_EG_OBJECT_HISTORY PRIMARY KEY (ID )
);


ALTER TABLE EG_OBJECT_HISTORY ADD 
CONSTRAINT FK_OBJECT_TYPE_ID
 FOREIGN KEY (OBJECT_TYPE_ID)
 REFERENCES EG_OBJECT_TYPE (ID) ;


ALTER TABLE EG_OBJECT_HISTORY ADD 
CONSTRAINT FK_MODIFIED_BY
 FOREIGN KEY (MODIFED_BY)
 REFERENCES EG_USER (ID_USER);

call recreate_sequence('SEQ_OBJECT_HISTORY' , 'EG_OBJECT_HISTORY' , 'ID' );

ALTER TABLE EG_BOUNDARY_TYPE DROP CONSTRAINT BNDRY_NAME_UNIQUE;



alter table eg_billregistermis add(mbrefno varchar(200));

ALTER TABLE EG_BOUNDARY
  ADD BNDRY_NAME_LOCAL VARCHAR2(256 BYTE);


  -- mani for recovery master on 02-06-2008
alter table tds add(ISEARNING varchar2(1));

 /****** change on 18/04/2008 relted to cheque reversal for infrastructure***************/

ALTER TABLE EGCL_TRANSACTION_HEADER DROP CONSTRAINT VOUCHER_MODULE_UNIQUE;

ALTER TABLE EGCL_TRANSACTION_HEADER DROP CONSTRAINT UNQ_VOUCHER;

ALTER TABLE EGCL_TRANSACTION_HEADER ADD (REVERSAL_TRANS_ID  INTEGER);

CREATE UNIQUE INDEX UNQ_VOUNUM_MODULE_REVERSALID ON EGCL_TRANSACTION_HEADER
(ID_MODULE, REVERSAL_TRANS_ID, VOUCHER_NUM)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

ALTER TABLE EGCL_TRANSACTION_HEADER ADD CONSTRAINT UNQ_VOUNUM_MODULE_REVERSALID UNIQUE (ID_MODULE, REVERSAL_TRANS_ID, VOUCHER_NUM)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               );
               
               
               
ALTER TABLE EGCL_TRANSACTION_HEADER ADD CONSTRAINT FK_REVERSAL_TRANS_ID FOREIGN KEY (REVERSAL_TRANS_ID) 
    REFERENCES EGCL_TRANSACTION_HEADER (ID_TRANS);   


   

/********* change on 24/05/2008*********************/

/********* START For EMPLOYEE LIGHT only - do not use if using PAYROLL OR PIMS tables *********************
 NOT EXECUTED IN 1.1.5.IT WILL BE EXECUTED IN FUTURE RELEASES
ALTER TABLE EG_DEPARTMENT DROP PRIMARY KEY CASCADE;
DROP TABLE EG_DEPARTMENT CASCADE CONSTRAINTS;

CREATE TABLE EG_DEPARTMENT
(
  ID_DEPT            INTEGER                    NOT NULL,
  DEPT_NAME          VARCHAR2(64 BYTE)          NOT NULL,
  DEPT_DETAILS       VARCHAR2(128 BYTE)             NULL,
  UPDATETIME         DATE                       NOT NULL,
  DEPT_CODE          VARCHAR2(520 BYTE)             NULL,
  DEPT_ADDR          VARCHAR2(250 BYTE)             NULL,
  ISBILLINGLOCATION  INTEGER                        NULL,
  PARENTID           INTEGER                        NULL,
  ISLEAF             INTEGER                        NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


DROP TABLE EG_DEPARTMENT_ADDRESS CASCADE CONSTRAINTS;

CREATE TABLE EG_DEPARTMENT_ADDRESS
(
  DEPARTMENTID  NUMBER                          NOT NULL,
  ADDRESSID     NUMBER                          NOT NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


ALTER TABLE EG_EMPLOYEE DROP PRIMARY KEY CASCADE;
DROP TABLE EG_EMPLOYEE CASCADE CONSTRAINTS;

CREATE TABLE EG_EMPLOYEE
(
  ID                              NUMBER(32)    NOT NULL,
  DATE_OF_BIRTH                   DATE              NULL,
  BLOOD_GROUP                     NUMBER(10)        NULL,
  MOTHER_TONUGE                   VARCHAR2(256 BYTE)     NULL,
  RELIGION_ID                     NUMBER(32)        NULL,
  COMMUNITY_ID                    NUMBER(32)        NULL,
  GENDER                          CHAR(1 BYTE)      NULL,
  IS_HANDICAPPED                  CHAR(1 BYTE)      NULL,
  IS_MED_REPORT_AVAILABLE         CHAR(1 BYTE)      NULL,
  DATE_OF_FIRST_APPOINTMENT       DATE              NULL,
  IDENTIFICATION_MARKS1           VARCHAR2(1024 BYTE)     NULL,
  LANGUAGES_KNOWN_ID              NUMBER(32)        NULL,
  MODE_OF_RECRUIMENT_ID           NUMBER(32)        NULL,
  RECRUITMENT_TYPE_ID             NUMBER(32)        NULL,
  STATUS_ID                       NUMBER(10)        NULL,
  CATEGORY_ID                     NUMBER(32)        NULL,
  QULIFIED_ID                     NUMBER(32)        NULL,
  SALARY_BANK                     NUMBER(32)        NULL,
  BANK                            VARCHAR2(256 BYTE)     NULL,
  SB_ACCOUNT_NUMBER               VARCHAR2(1024 BYTE)     NULL,
  PAY_FIXED_IN_ID                 NUMBER(32)        NULL,
  GRADE_ID                        NUMBER(32)        NULL,
  PRESENT_DESIGNATION             NUMBER(5)         NULL,
  SCALE_OF_PAY                    VARCHAR2(1024 BYTE)     NULL,
  BASIC_PAY                       NUMBER(32)        NULL,
  SPL_PAY                         NUMBER(32)        NULL,
  PP_SGPP_PAY                     NUMBER(32)        NULL,
  ANNUAL_INCREMENT_ID             NUMBER(32)        NULL,
  GPF_AC_NUMBER                   VARCHAR2(1024 BYTE)     NULL,
  RETIREMENT_AGE                  NUMBER(3)         NULL,
  PRESENT_DEPARTMENT              NUMBER(5)         NULL,
  IF_ON_DUTY_ARRANGMENT_DUTY_DEP  VARCHAR2(256 BYTE)     NULL,
  LOCATION                        VARCHAR2(256 BYTE)     NULL,
  COST_CENTER                     VARCHAR2(256 BYTE)     NULL,
  ID_DEPT                         NUMBER(32)        NULL,
  ID_USER                         NUMBER(32)        NULL,
  ISACTIVE                        NUMBER(1)         NULL,
  EMPFATHER_FIRSTNAME             VARCHAR2(256 BYTE)     NULL,
  EMPFATHER_LASTNAME              VARCHAR2(256 BYTE)     NULL,
  EMPFATHER_MIDDLENAME            VARCHAR2(256 BYTE)     NULL,
  EMP_FIRSTNAME                   VARCHAR2(256 BYTE) NOT NULL,
  EMP_LASTNAME                    VARCHAR2(256 BYTE)     NULL,
  EMP_MIDDLENAME                  VARCHAR2(256 BYTE)     NULL,
  IDENTIFICATION_MARKS2           VARCHAR2(1024 BYTE)     NULL,
  PAN_NUMBER                      VARCHAR2(256 BYTE)     NULL,
  NAME                            VARCHAR2(256 BYTE)     NULL,
  MATURITY_DATE                   DATE              NULL,
  CODE                            NUMBER(32)        NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


ALTER TABLE EG_EMP_ASSIGNMENT_PRD DROP PRIMARY KEY CASCADE;
DROP TABLE EG_EMP_ASSIGNMENT_PRD CASCADE CONSTRAINTS;

CREATE TABLE EG_EMP_ASSIGNMENT_PRD
(
  ID           NUMBER(32)                       NOT NULL,
  FROM_DATE    DATE                                 NULL,
  TO_DATE      DATE                                 NULL,
  ID_EMPLOYEE  NUMBER(32)                           NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


CREATE UNIQUE INDEX PK_DEPT ON EG_DEPARTMENT
(ID_DEPT)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX UQ_DEPT_NAME ON EG_DEPARTMENT
(DEPT_NAME)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX UNIQCODE ON EG_DEPARTMENT
(DEPT_CODE)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX PK_EG_EMPLOYEE ON EG_EMPLOYEE
(ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


CREATE UNIQUE INDEX PK_EG_EMP_ASSIGNMENT_PRD ON EG_EMP_ASSIGNMENT_PRD
(ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


ALTER TABLE EG_EMP_ASSIGNMENT DROP PRIMARY KEY CASCADE;
DROP TABLE EG_EMP_ASSIGNMENT CASCADE CONSTRAINTS;

CREATE TABLE EG_EMP_ASSIGNMENT
(
  ID                 NUMBER(32)                 NOT NULL,
  ID_FUND            NUMBER(32)                     NULL,
  ID_FUNCTION        NUMBER(32)                     NULL,
  DESIGNATIONID      NUMBER(32)                     NULL,
  ID_FUNCTIONARY     NUMBER(32)                     NULL,
  PCT_ALLOCATION     VARCHAR2(256 BYTE)             NULL,
  REPORTS_TO         NUMBER(32)                     NULL,
  ID_EMP_ASSIGN_PRD  NUMBER(32)                     NULL,
  FIELD              NUMBER(12)                     NULL,
  MAIN_DEPT          NUMBER(10)                     NULL,
  POSITION_ID        NUMBER(32)                     NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


CREATE UNIQUE INDEX PK_EG_EMP_ASSIGNMENT ON EG_EMP_ASSIGNMENT
(ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


ALTER TABLE EG_EMPLOYEE_DEPT DROP PRIMARY KEY CASCADE;
DROP TABLE EG_EMPLOYEE_DEPT CASCADE CONSTRAINTS;

CREATE TABLE EG_EMPLOYEE_DEPT
(
  DEPTID         NUMBER(4)                          NULL,
  ID             NUMBER(10)                         NULL,
  ASSIGNMENT_ID  NUMBER(10)                         NULL,
  HOD            NUMBER(10)                         NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


CREATE UNIQUE INDEX PK_EG_EMPLOYEE_DEPT ON EG_EMPLOYEE_DEPT
(ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


ALTER TABLE EG_DEPARTMENT ADD (
  CONSTRAINT PK_DEPT PRIMARY KEY (ID_DEPT)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ),
  CONSTRAINT UQ_DEPT_NAME UNIQUE (DEPT_NAME)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ),
  CONSTRAINT UNIQCODE UNIQUE (DEPT_CODE)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));

ALTER TABLE EG_EMPLOYEE ADD (
  CONSTRAINT PK_EG_EMPLOYEE PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));

ALTER TABLE EG_EMP_ASSIGNMENT_PRD ADD (
  CONSTRAINT PK_EG_EMP_ASSIGNMENT_PRD PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));

ALTER TABLE EG_EMP_ASSIGNMENT ADD (
  CONSTRAINT PK_EG_EMP_ASSIGNMENT PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));

ALTER TABLE EG_EMPLOYEE_DEPT ADD (
  CONSTRAINT PK_EG_EMPLOYEE_DEPT PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));

ALTER TABLE EG_DEPARTMENT_ADDRESS ADD (
  FOREIGN KEY (ADDRESSID) 
    REFERENCES EG_ADDRESS (ADDRESSID),
  FOREIGN KEY (DEPARTMENTID) 
    REFERENCES EG_DEPARTMENT (ID_DEPT));

ALTER TABLE EG_EMPLOYEE ADD (
  CONSTRAINT PRESENT_DEPT_FK FOREIGN KEY (PRESENT_DEPARTMENT) 
    REFERENCES EG_DEPARTMENT (ID_DEPT),
    CONSTRAINT ID_USER_FK1 FOREIGN KEY (ID_USER) 
    REFERENCES EG_USER (ID_USER),
  CONSTRAINT ID_DEPT_FK FOREIGN KEY (ID_DEPT) 
    REFERENCES EG_DEPARTMENT (ID_DEPT));

ALTER TABLE EG_EMP_ASSIGNMENT_PRD ADD (
  CONSTRAINT ID_EMPLOYEE_FK FOREIGN KEY (ID_EMPLOYEE) 
    REFERENCES EG_EMPLOYEE (ID));


DROP TABLE EG_POSITION CASCADE CONSTRAINTS;


CREATE TABLE EG_POSITION
(
  POSITION_NAME     VARCHAR2(256 BYTE)          NOT NULL,
  ID                NUMBER(32),
  SANCTIONED_POSTS  NUMBER(32),
  OUTSOURCED_POSTS  NUMBER(32),
  DESIG_ID          NUMBER(32),
  EFFECTIVE_DATE    DATE
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;

CREATE UNIQUE INDEX PK_EG_POSITION ON EG_POSITION
(ID)
LOGGING
TABLESPACE USERS
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

ALTER TABLE EG_POSITION ADD (
  CONSTRAINT PK_EG_POSITION PRIMARY KEY (ID)
    USING INDEX 
    TABLESPACE USERS
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       2147483645
                PCTINCREASE      0
               ));

ALTER TABLE EG_EMP_ASSIGNMENT ADD (
  CONSTRAINT REPORTS_FK FOREIGN KEY (REPORTS_TO) 
    REFERENCES EG_EMPLOYEE (ID),
  CONSTRAINT PRD_FK FOREIGN KEY (ID_EMP_ASSIGN_PRD) 
    REFERENCES EG_EMP_ASSIGNMENT_PRD (ID),
  CONSTRAINT ID_FUND_FK FOREIGN KEY (ID_FUND) 
    REFERENCES FUND (ID),
  CONSTRAINT FUNCTION_FK FOREIGN KEY (ID_FUNCTION) 
    REFERENCES FUNCTION (ID),
  CONSTRAINT FARY_FK FOREIGN KEY (ID_FUNCTIONARY) 
    REFERENCES FUNCTIONARY (ID),
  CONSTRAINT DES_FK FOREIGN KEY (DESIGNATIONID) 
    REFERENCES EG_DESIGNATION (DESIGNATIONID),
  CONSTRAINT POS_ID FOREIGN KEY (POSITION_ID) 
    REFERENCES EG_POSITION (ID),
  CONSTRAINT MAIN_DE FOREIGN KEY (MAIN_DEPT) 
    REFERENCES EG_DEPARTMENT (ID_DEPT));

ALTER TABLE EG_EMPLOYEE_DEPT ADD (
  CONSTRAINT DEPT_IDS FOREIGN KEY (DEPTID) 
    REFERENCES EG_DEPARTMENT (ID_DEPT),
  CONSTRAINT ASS_ID FOREIGN KEY (ASSIGNMENT_ID) 
    REFERENCES EG_EMP_ASSIGNMENT (ID),
  CONSTRAINT HOD_ID FOREIGN KEY (HOD) 
    REFERENCES EG_DEPARTMENT (ID_DEPT));


INSERT INTO ACCOUNTDETAILTYPE ( ID, NAME, DESCRIPTION, TABLENAME, COLUMNNAME, ATTRIBUTENAME,
NBROFLEVELS, ISACTIVE, CREATED, LASTMODIFIED, MODIFIEDBY ) VALUES ( 
4, 'Employee', 'Employee', 'eg_employee', 'id', 'eg_employee_id', 1, 1,  TO_Date( '03/23/2007 12:08:06 AM', 'MM/DD/YYYY HH:MI:SS AM')
,  TO_Date( '03/23/2007 12:08:06 AM', 'MM/DD/YYYY HH:MI:SS AM'), 1);



CREATE SEQUENCE SEQ_ASS_DEPT
  START WITH 19
  MAXVALUE 999999999999999999999999999
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;

CREATE SEQUENCE SEQ_ASS
  START WITH 12122
  MAXVALUE 999999999999999999999999999
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;


CREATE SEQUENCE SEQ_ASS_PRD
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;


CREATE OR REPLACE VIEW EG_EIS_EMPLOYEEINFO
(ASS_ID, PRD_ID, ID, CODE, NAME, 
 DESIGNATIONID, FROM_DATE, TO_DATE, REPORTS_TO, DATE_OF_FA, 
 ISACTIVE, DEPT_ID, FUNCTIONARY_ID, POS_ID, USER_ID)
AS 
SELECT EEA.ID,EAP.ID,EE.ID,EE.CODE,EE.EMP_FIRSTNAME||' '||EE.EMP_MIDDLENAME||' '||EE.EMP_LASTNAME, 
   EEA.DESIGNATIONID,EAP.FROM_DATE,EAP.TO_DATE,EEA.REPORTS_TO,EE.DATE_OF_FIRST_APPOINTMENT,EE.ISACTIVE,
   EEA.MAIN_DEPT,EEA.ID_FUNCTIONARY,EEA.POSITION_ID,ee.ID_USER 
   FROM EG_EMP_ASSIGNMENT_PRD EAP, EG_EMP_ASSIGNMENT EEA, EG_EMPLOYEE EE 
   WHERE EE.ID = EAP.ID_EMPLOYEE 
  AND EAP.ID=EEA.ID_EMP_ASSIGN_PRD;
  NOORDER;
  
  ********* END For EMPLOYEE LIGHT only - do not use if using PAYROLL OR PIMS tables *********************/

-- Infrastructure.2.1.sql ends


-- for advance register reports
/* postponed to next release
insert into eg_action(id,name,entityid,taskid,updatedtime) values(284,'Register of Advance',null,null, '21-May-2008');
insert into menutree values(377,'Register of Advance',140,284,2306);
insert into eg_roleaction_map values(5,284);
*/
-- other bill details
ALTER TABLE OTHERBILLDETAIL ADD (PAYVHID  NUMBER);

ALTER TABLE OTHERBILLDETAIL ADD CONSTRAINT FK_OBD_VH1 FOREIGN KEY (PAYVHID) 
    REFERENCES VOUCHERHEADER (ID);

-- make 483200 and 483100 as control code for Advance register
INSERT INTO ChartOfAccountDetail (id, glCodeId, detailTypeId, defaultKeyId, defaultValue, isControlCode)
VALUES(seq_ChartOfAccountDetail.nextval, (select id from chartofaccounts where glcode='483100'), 1, null, null, 1);

INSERT INTO ChartOfAccountDetail (id, glCodeId, detailTypeId, defaultKeyId, defaultValue, isControlCode)
VALUES(seq_ChartOfAccountDetail.nextval, (select id from chartofaccounts where glcode='483200'), 1, null, null, 1);


-- since employee related changes are not executed and name column in eg_employee table is in needed for Advance report adding manually here
alter table eg_employee add    (NAME   VARCHAR2(256 BYTE)     NULL);
commit;
exit;