
--------------------------------------------------------
--  DDL for Table EG_REASON_CATEGORY
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_REASON_CATEGORY;

CREATE TABLE EG_REASON_CATEGORY 
(	ID bigint, 
	NAME VARCHAR(64) NOT NULL, 
	CODE VARCHAR(64) NOT NULL, 
	ORDER_ID bigint NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL
);
 
COMMENT ON COLUMN EG_REASON_CATEGORY.ID IS '';
 
COMMENT ON COLUMN EG_REASON_CATEGORY.NAME IS '';
 
COMMENT ON COLUMN EG_REASON_CATEGORY.CODE IS '';
 
COMMENT ON COLUMN EG_REASON_CATEGORY.ORDER_ID IS '';
 
COMMENT ON COLUMN EG_REASON_CATEGORY.MODIFIED_DATE IS '';
 
COMMENT ON TABLE EG_REASON_CATEGORY  IS 'Master table for Demand Reason Categories';

--------------------------------------------------------
--  DDL for Table EG_DEMAND_REASON_MASTER
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_DEMAND_REASON_MASTER;

CREATE TABLE EG_DEMAND_REASON_MASTER 
(	ID bigint, 
	REASON_MASTER VARCHAR(64) NOT NULL, 
	ID_CATEGORY bigint NOT NULL, 
	IS_DEBIT CHAR(1) NOT NULL, 
	MODULE_ID bigint NOT NULL, 
	CODE VARCHAR(16) NOT NULL, 
	ORDER_ID bigint NOT NULL, 
	CREATED_DATE timestamp without time zone NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL
);

COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.ID IS 'Primary Key';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.REASON_MASTER IS 'Name of the demand reason';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.ID_CATEGORY IS 'FK to eg_reason_category';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.IS_DEBIT IS 'is tax reason is debit. 0 or 1';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.MODULE_ID IS 'FK to eg_module';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.CODE IS 'tax reason code(uses internally)';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.ORDER_ID IS 'Order no to display list';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.CREATED_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_REASON_MASTER.MODIFIED_DATE IS '';
 
COMMENT ON TABLE EG_DEMAND_REASON_MASTER  IS 'Master table for Demand Reason Master';

--------------------------------------------------------
--  DDL for Table EG_INSTALLMENT_MASTER
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_INSTALLMENT_MASTER;

CREATE TABLE EG_INSTALLMENT_MASTER
(	ID BIGINT, 
	INSTALLMENT_NUM BIGINT NOT NULL, 
	INSTALLMENT_YEAR timestamp without time zone NOT NULL, 
	START_DATE timestamp without time zone NOT NULL, 
	END_DATE timestamp without time zone NOT NULL, 
	ID_MODULE BIGINT, 
	LASTUPDATEDTIMESTAMP timestamp without time zone, 
	DESCRIPTION VARCHAR(25), 
	INSTALLMENT_TYPE VARCHAR(50)
); 

COMMENT ON TABLE EG_INSTALLMENT_MASTER  IS 'This table contains the period for which the bills are being generated';
	 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.ID IS 'primary key';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.INSTALLMENT_NUM IS 'Installment number';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.INSTALLMENT_YEAR IS 'Installment year';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.START_DATE IS 'installment start date';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.END_DATE IS 'installment end date';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.ID_MODULE IS 'fk to eg_module';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.LASTUPDATEDTIMESTAMP IS 'last updated time when row got updated';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.DESCRIPTION IS 'Descriptiion of installment';
 
COMMENT ON COLUMN EG_INSTALLMENT_MASTER.INSTALLMENT_TYPE IS 'type of installment';
 
 
--------------------------------------------------------
--  DDL for Table EG_DEMAND_REASON
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_DEMAND_REASON;

CREATE TABLE EG_DEMAND_REASON 
(	ID bigint, 
	ID_DEMAND_REASON_MASTER bigint NOT NULL, 
	ID_INSTALLMENT bigint NOT NULL, 
	PERCENTAGE_BASIS real, 
	ID_BASE_REASON bigint, 
	CREATE_DATE timestamp without time zone NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL, 
	GLCODEID bigint
);

COMMENT ON COLUMN EG_DEMAND_REASON.ID IS 'Primary Key';
 
COMMENT ON COLUMN EG_DEMAND_REASON.ID_DEMAND_REASON_MASTER IS 'FK to eg_demand_reason_master';
 
COMMENT ON COLUMN EG_DEMAND_REASON.ID_INSTALLMENT IS 'FK to eg_installment_master';
 
COMMENT ON COLUMN EG_DEMAND_REASON.PERCENTAGE_BASIS IS 'Not used';
 
COMMENT ON COLUMN EG_DEMAND_REASON.ID_BASE_REASON IS 'FK to eg_demand_reason';
 
COMMENT ON COLUMN EG_DEMAND_REASON.CREATE_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_REASON.MODIFIED_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_REASON.GLCODEID IS 'FK to chartofaccounts';
 
COMMENT ON TABLE EG_DEMAND_REASON  IS 'Master table for Demand reasons per installment';
--------------------------------------------------------
--  DDL for Table EG_DEMAND_REASON_DETAILS
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_DEMAND_REASON_DETAILS;

CREATE TABLE EG_DEMAND_REASON_DETAILS 
(	ID bigint, 
	ID_DEMAND_REASON bigint NOT NULL, 
	PERCENTAGE real, 
	FROM_DATE timestamp without time zone NOT NULL, 
	TO_DATE timestamp without time zone NOT NULL, 
	LOW_LIMIT double precision, 
	HIGH_LIMIT double precision, 
	CREATE_DATE timestamp without time zone NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL, 
	FLAT_AMOUNT double precision, 
	IS_FLATAMNT_MAX double precision
);

COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.ID IS 'Primary Key';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.ID_DEMAND_REASON IS 'FK to eg_demand_reason';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.PERCENTAGE IS 'tax perc for each demand reason';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.FROM_DATE IS 'tax perc for each demand reason validity start date';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.TO_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.LOW_LIMIT IS 'low limit amount for tax reason';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.HIGH_LIMIT IS 'High limit amount for tax reason';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.CREATE_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.MODIFIED_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.FLAT_AMOUNT IS 'Flat tax amount to be applicable';
 
COMMENT ON COLUMN EG_DEMAND_REASON_DETAILS.IS_FLATAMNT_MAX IS 'if the tax for reason is flat amount then amount comes here';
 
COMMENT ON TABLE EG_DEMAND_REASON_DETAILS  IS 'Master table for Demand Reason details like %age of tax to be calculated on calculated amount like ALV';

--------------------------------------------------------
--  DDL for Table EG_BILL_TYPE
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_BILL_TYPE;

CREATE TABLE EG_BILL_TYPE 
(	ID bigint, 
	NAME VARCHAR(32) NOT NULL, 
	CODE VARCHAR(10) NOT NULL, 
	CREATE_DATE DATE NOT NULL, 
	MODIFIED_DATE DATE NOT NULL
);

CREATE SEQUENCE SEQ_EG_DEMAND;

CREATE TABLE EG_DEMAND 
(	ID bigint, 
	ID_INSTALLMENT bigint NOT NULL, 
	BASE_DEMAND bigint, 
	IS_HISTORY CHAR(1) DEFAULT 'N' NOT NULL, 
	CREATE_DATE timestamp without time zone NOT NULL NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL NOT NULL, 
	AMT_COLLECTED double precision, 
	STATUS CHAR(1), 
	MIN_AMT_PAYABLE double precision, 
	AMT_REBATE double precision
);

COMMENT ON COLUMN EG_DEMAND.ID IS 'Primary Key';
 
COMMENT ON COLUMN EG_DEMAND.ID_INSTALLMENT IS 'FK to eg_installment_master';
 
COMMENT ON COLUMN EG_DEMAND.BASE_DEMAND IS 'Total demand for a installment';
 
COMMENT ON COLUMN EG_DEMAND.IS_HISTORY IS 'history status of demand';
 
COMMENT ON COLUMN EG_DEMAND.CREATE_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND.MODIFIED_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND.AMT_COLLECTED IS 'Tax amount collected';
 
COMMENT ON COLUMN EG_DEMAND.STATUS IS 'Not used';
 
COMMENT ON COLUMN EG_DEMAND.MIN_AMT_PAYABLE IS 'Minimum Amount payable';
 
COMMENT ON COLUMN EG_DEMAND.AMT_REBATE IS 'Tax rebate given';
 
COMMENT ON TABLE EG_DEMAND  IS 'Main table for Demand Tax';
--------------------------------------------------------
--  DDL for Table EG_DEMAND_DETAILS
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_DEMAND_DETAILS;

CREATE TABLE EG_DEMAND_DETAILS 
(	ID bigint, 
	ID_DEMAND bigint NOT NULL, 
	ID_DEMAND_REASON bigint NOT NULL, 
	ID_STATUS bigint, 
	FILE_REFERENCE_NO VARCHAR(32), 
	REMARKS VARCHAR(512), 
	AMOUNT bigint NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL, 
	CREATE_DATE timestamp without time zone NOT NULL, 
	AMT_COLLECTED double precision DEFAULT 0, 
	AMT_REBATE double precision DEFAULT 0
);

  
COMMENT ON COLUMN EG_DEMAND_DETAILS.ID IS 'Primary Key';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.ID_DEMAND IS 'FK to eg_demand';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.ID_DEMAND_REASON IS 'FK to eg_demand_reason';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.ID_STATUS IS 'Not used';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.FILE_REFERENCE_NO IS 'Not used';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.REMARKS IS 'remarks if any';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.AMOUNT IS 'Tax Amount';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.MODIFIED_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.CREATE_DATE IS '';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.AMT_COLLECTED IS 'Tax Amount collected';
 
COMMENT ON COLUMN EG_DEMAND_DETAILS.AMT_REBATE IS 'tax rebate given';
 
COMMENT ON TABLE EG_DEMAND_DETAILS  IS 'Contains Tax details';

--------------------------------------------------------
--  DDL for Table EG_BILL 
--------------------------------------------------------

CREATE SEQUENCE SEQ_EG_BILL;

CREATE TABLE EG_BILL 
(	ID bigint NOT NULL, 
	ID_DEMAND bigint, 
	CITIZEN_NAME VARCHAR(1024) NOT NULL, 
	CITIZEN_ADDRESS VARCHAR(1024) NOT NULL, 
	BILL_NO VARCHAR(20) NOT NULL, 
	ID_BILL_TYPE bigint NOT NULL, 
	ISSUE_DATE timestamp without time zone NOT NULL, 
	LAST_DATE timestamp without time zone, 
	MODULE_ID bigint NOT NULL, 
	USER_ID bigint NOT NULL, 
	CREATE_DATE timestamp without time zone NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL, 
	IS_HISTORY CHAR(1) DEFAULT 'N' NOT NULL, 
	IS_CANCELLED CHAR(1) DEFAULT 'N' NOT NULL, 
	FUNDCODE VARCHAR(32), 
	FUNCTIONARY_CODE double precision, 
	FUNDSOURCE_CODE VARCHAR(32), 
	DEPARTMENT_CODE VARCHAR(32), 
	COLL_MODES_NOT_ALLOWED VARCHAR(512), 
	BOUNDARY_NUM bigint, 
	BOUNDARY_TYPE VARCHAR(512), 
	TOTAL_AMOUNT double precision, 
	TOTAL_COLLECTED_AMOUNT double precision, 
	SERVICE_CODE VARCHAR(50), 
	PART_PAYMENT_ALLOWED CHAR(1), 
	OVERRIDE_ACCOUNTHEADS_ALLOWED CHAR(1), 
	DESCRIPTION VARCHAR(250), 
	MIN_AMT_PAYABLE double precision, 
	CONSUMER_ID VARCHAR(64), 
	DSPL_MESSAGE VARCHAR(256), 
	CALLBACK_FOR_APPORTION CHAR(1) DEFAULT 0 NOT NULL
);


COMMENT ON COLUMN EG_BILL.ID IS 'Primary Key';
 
COMMENT ON COLUMN EG_BILL.ID_DEMAND IS 'FK to eg_demand';
 
COMMENT ON COLUMN EG_BILL.CITIZEN_NAME IS 'citizen name';
 
COMMENT ON COLUMN EG_BILL.CITIZEN_ADDRESS IS 'Citizen address';
 
COMMENT ON COLUMN EG_BILL.BILL_NO IS 'Bill no';
 
COMMENT ON COLUMN EG_BILL.ID_BILL_TYPE IS 'FK to eg_bill_type';
 
COMMENT ON COLUMN EG_BILL.ISSUE_DATE IS 'Bill issue date';
 
COMMENT ON COLUMN EG_BILL.LAST_DATE IS 'Last date of payment using this bill';
 
COMMENT ON COLUMN EG_BILL.MODULE_ID IS 'FK to eg_module';
 
COMMENT ON COLUMN EG_BILL.USER_ID IS 'FK to eg_user';
 
COMMENT ON COLUMN EG_BILL.CREATE_DATE IS '';
 
COMMENT ON COLUMN EG_BILL.MODIFIED_DATE IS '';
 
COMMENT ON COLUMN EG_BILL.IS_HISTORY IS 'Bill history status';
 
COMMENT ON COLUMN EG_BILL.IS_CANCELLED IS 'Bill cancel status';
 
COMMENT ON COLUMN EG_BILL.FUNDCODE IS 'fund code';
 
COMMENT ON COLUMN EG_BILL.FUNCTIONARY_CODE IS 'functionary code';
 
COMMENT ON COLUMN EG_BILL.FUNDSOURCE_CODE IS 'fund source code';
 
COMMENT ON COLUMN EG_BILL.DEPARTMENT_CODE IS 'Department that bill entity belongs to';
 
COMMENT ON COLUMN EG_BILL.COLL_MODES_NOT_ALLOWED IS 'allowd collection modes for this bill';
 
COMMENT ON COLUMN EG_BILL.BOUNDARY_NUM IS 'boundary of entity, for which bill is generated';
 
COMMENT ON COLUMN EG_BILL.BOUNDARY_TYPE IS 'boundary type of entity, for which bill is generated';
 
COMMENT ON COLUMN EG_BILL.TOTAL_AMOUNT IS 'total bill amount';
 
COMMENT ON COLUMN EG_BILL.TOTAL_COLLECTED_AMOUNT IS 'total amount collected for this bill';
 
COMMENT ON COLUMN EG_BILL.SERVICE_CODE IS 'service code from collection system for each billing system';
 
COMMENT ON COLUMN EG_BILL.PART_PAYMENT_ALLOWED IS 'information to collection system, do system need to allow partial payment';
 
COMMENT ON COLUMN EG_BILL.OVERRIDE_ACCOUNTHEADS_ALLOWED IS 'information to collection system, do collection system allow for override  of account head wise collection';
 
COMMENT ON COLUMN EG_BILL.DESCRIPTION IS 'Description of entity for which bill is created';
 
COMMENT ON COLUMN EG_BILL.MIN_AMT_PAYABLE IS 'minimu amount payable for this bill';
 
COMMENT ON COLUMN EG_BILL.CONSUMER_ID IS 'consumer id, for different billing system diff unique ref no will be thr';
 
COMMENT ON COLUMN EG_BILL.DSPL_MESSAGE IS 'message need to be shown on collection screen';
 
COMMENT ON COLUMN EG_BILL.CALLBACK_FOR_APPORTION IS 'call back required or not while doing collection';
 
COMMENT ON TABLE EG_BILL  IS 'Bills for the demand framework';
 

--------------------------------------------------------
--  DDL for Table EG_BILL_DETAILS
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_BILL_DETAILS;

CREATE TABLE EG_BILL_DETAILS 
(	ID bigint, 
	ID_DEMAND_REASON bigint, 
	CREATE_DATE timestamp without time zone, 
	MODIFIED_DATE timestamp without time zone NOT NULL, 
	ID_BILL bigint NOT NULL, 
	COLLECTED_AMOUNT double precision, 
	ORDER_NO bigint, 
	GLCODE VARCHAR(64), 
	FUNCTION_CODE VARCHAR(32), 
	CR_AMOUNT double precision, 
	DR_AMOUNT double precision, 
	DESCRIPTION VARCHAR(128), 
	ID_INSTALLMENT bigint, 
	ADDITIONAL_FLAG bigint
);

--------------------------------------------------------
--  DDL for Table EG_BILLRECEIPT
--------------------------------------------------------
CREATE SEQUENCE SEQ_EG_BILLRECEIPT;

CREATE TABLE EG_BILLRECEIPT 
(	ID bigint, 
	BILLID bigint NOT NULL, 
	RECEIPT_NUMBER VARCHAR(50), 
	RECEIPT_DATE timestamp without time zone, 
	RECEIPT_AMOUNT double precision NOT NULL, 
	COLLECTION_STATUS VARCHAR(20), 
	CREATED_DATE timestamp without time zone NOT NULL, 
	MODIFIED_DATE timestamp without time zone NOT NULL, 
	CREATEDBY bigint, 
	LASTMODIFIEDBY bigint, 
	IS_CANCELLED CHAR(1) DEFAULT 'N' NOT NULL
);

COMMENT ON COLUMN EG_BILLRECEIPT.ID IS 'Primary Key';
 
COMMENT ON COLUMN EG_BILLRECEIPT.BILLID IS 'FK to eg_bill';
 
COMMENT ON COLUMN EG_BILLRECEIPT.RECEIPT_NUMBER IS 'receipt NUMBER';
 
COMMENT ON COLUMN EG_BILLRECEIPT.RECEIPT_DATE IS 'receipt date';
 
COMMENT ON COLUMN EG_BILLRECEIPT.RECEIPT_AMOUNT IS 'receipt amount';
 
COMMENT ON COLUMN EG_BILLRECEIPT.COLLECTION_STATUS IS 'status of collection (approved, pending, etc)';
 
COMMENT ON COLUMN EG_BILLRECEIPT.CREATED_DATE IS '';
 
COMMENT ON COLUMN EG_BILLRECEIPT.MODIFIED_DATE IS '';
 
COMMENT ON COLUMN EG_BILLRECEIPT.CREATEDBY IS '';
 
COMMENT ON COLUMN EG_BILLRECEIPT.LASTMODIFIEDBY IS '';
 
COMMENT ON COLUMN EG_BILLRECEIPT.IS_CANCELLED IS 'receipt status';
 
COMMENT ON TABLE EG_BILLRECEIPT  IS 'Maps receipt information to a bill';
