DROP TABLE eg_service_subledgerinfo;
DROP SEQUENCE seq_eg_service_subledgerinfo;
DROP TABLE eg_service_accountdetails;
DROP SEQUENCE seq_eg_service_accountdetails;
DROP TABLE eg_service_dept_mapping;
DROP TABLE eg_servicedetails;
DROP SEQUENCE seq_eg_servicedetails;
DROP TABLE eg_servicecategory;
DROP SEQUENCE seq_eg_servicecategory;
--DROP TABLE eg_bankaccountservicemapping;
--DROP SEQUENCE seq_eg_bankaccountservicemapping;
--DROP TABLE egcl_bank_remittance;
--DROP SEQUENCE seq_egcl_bank_remittance;
--DROP TABLE egcl_onlinepayments;
--DROP SEQUENCE seq_egcl_onlinepayments;
--DROP TABLE egcl_collectionvoucher;
--DROP SEQUENCE seq_egcl_collectionvoucher;
--DROP TABLE egcl_collectioninstrument;
--DROP TABLE egcl_challanheader;
--DROP SEQUENCE seq_egcl_challanheader;
--DROP TABLE egcl_accountpayeedetails;
--DROP SEQUENCE seq_egcl_accountpayeedetails;
--DROP TABLE egcl_collectiondetails;
--DROP SEQUENCE seq_egcl_collectiondetails;
--DROP TABLE egcl_collectionmis;
--DROP SEQUENCE seq_egcl_collectionmis;
--DROP TABLE egcl_collectionheader;
--DROP SEQUENCE seq_egcl_collectionheader;

CREATE TABLE egcl_servicecategory
(
  id bigint NOT NULL,
  name character varying(256) NOT NULL,
  code character varying(50) NOT NULL,
  isactive boolean,
  version bigint NOT NULL DEFAULT 1,
  CREATEDBY bigint NOT NULL,
  CREATEDDATE timestamp without time zone,
  LASTMODIFIEDBY bigint NOT NULL,
  LASTMODIFIEDDATE timestamp without time zone
);
CREATE SEQUENCE seq_egcl_servicecategory;

CREATE TABLE egcl_servicedetails
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  serviceurl character varying(256),
  isenabled boolean,
  callbackurl character varying(256),
  servicetype character(1),
  code character varying(12) NOT NULL,
  fund bigint,
  fundsource bigint,
  functionary bigint,
  vouchercreation boolean,
  scheme bigint,
  subscheme bigint,
  servicecategory bigint,
  isvoucherapproved boolean,
  vouchercutoffdate timestamp without time zone
);
CREATE SEQUENCE seq_egcl_servicedetails;


CREATE TABLE egcl_service_dept_mapping
(
  department bigint NOT NULL,
  servicedetails bigint NOT NULL
);


CREATE TABLE egcl_service_accountdetails
(
  id bigint NOT NULL,
  servicedetails bigint NOT NULL,
  chartofaccount bigint NOT NULL,
  amount double precision,
  functionid bigint
);
CREATE SEQUENCE seq_egcl_service_accountdetails;

CREATE TABLE egcl_service_subledgerinfo
(
  id bigint NOT NULL,
  accountdetailtype bigint NOT NULL,
  accountdetailkey bigint,
  amount double precision,
  serviceaccountdetail bigint NOT NULL
);
CREATE SEQUENCE seq_egcl_service_subledgerinfo;

CREATE TABLE egcl_collectionheader
(
  id bigint NOT NULL,
  referencenumber character varying(50),
  referencedate timestamp without time zone,
  receipttype character(1) NOT NULL,
  receiptnumber character varying(50),
  receiptdate timestamp without time zone not null,
  referencedesc character varying(250),
  manualreceiptnumber character varying(50),
  manualreceiptdate timestamp without time zone,
  ismodifiable boolean,
  servicedetails bigint NOT NULL,
  collectiontype character(1),
  STATE_ID bigint,
  location bigint,
  isreconciled boolean,
  status bigint NOT NULL,
  reasonforcancellation character varying(250),
  paidby character varying(1024), 
  reference_ch_id bigint, 
  overrideaccountheads boolean, 
  partpaymentallowed boolean, 
  displaymsg character varying(256),
  minimumamount double precision,
  totalamount double precision,
  collmodesnotallwd character varying(256),
  consumercode character varying(256),
  callbackforapportioning boolean,
  payeename character varying(256),
  payeeaddress character varying(1024),
  version bigint NOT NULL DEFAULT 1,
  CREATEDBY bigint NOT NULL,
  CREATEDDATE timestamp without time zone,
  LASTMODIFIEDBY bigint NOT NULL,
  LASTMODIFIEDDATE timestamp without time zone
);
CREATE SEQUENCE seq_egcl_collectionheader;


CREATE TABLE egcl_collectionmis
(
  id bigint NOT NULL,
  fund bigint NOT NULL,
  fundsource bigint,
  boundary bigint,
  department bigint NOT NULL,
  scheme bigint,
  subscheme bigint,
  collectionheader bigint NOT NULL,
  functionary bigint,
  depositedinbank bigint
);
CREATE SEQUENCE seq_egcl_collectionmis;

CREATE TABLE egcl_collectiondetails
(
  id bigint NOT NULL,
  chartofaccount bigint NOT NULL,
  dramount double precision,
  cramount double precision,
  ordernumber bigint,
  collectionheader bigint,
  functionid bigint,
  actualcramounttobepaid double precision,
  description character varying(500),
  financialyear bigint,
  isactualdemand boolean
);
CREATE SEQUENCE seq_egcl_collectiondetails;

CREATE TABLE egcl_accountpayeedetails
(
  id bigint NOT NULL,
  collectiondetails bigint NOT NULL,
  accountdetailstype bigint NOT NULL,
  accountdetailskey bigint NOT NULL,
  amount bigint
);
CREATE SEQUENCE seq_egcl_accountpayeedetails;


CREATE TABLE egcl_challanheader
(
  id bigint NOT NULL,
  challannumber character varying(25) NOT NULL,
  collectionheader bigint NOT NULL,
  validupto timestamp without time zone NOT NULL,
  status bigint,
  STATE_ID bigint,
  challandate timestamp without time zone NOT NULL,
  servicedetails bigint,
  reasonforcancellation character varying(256),
  voucherheader bigint,
  oldchallannumber character varying(25),
  version bigint NOT NULL DEFAULT 1,
  CREATEDBY bigint NOT NULL,
  LASTMODIFIEDBY bigint NOT NULL,
  CREATEDDATE timestamp without time zone,
  LASTMODIFIEDDATE timestamp without time zone
);
CREATE SEQUENCE seq_egcl_challanheader;


CREATE TABLE egcl_collectioninstrument
(
  collectionheader bigint NOT NULL,
  instrumentheader bigint NOT NULL
);

CREATE TABLE egcl_collectionvoucher
(
  id bigint NOT NULL,
  collectionheader bigint NOT NULL,
  voucherheader bigint
);
CREATE SEQUENCE seq_egcl_collectionvoucher;


CREATE TABLE egcl_onlinepayments
(
  id bigint NOT NULL,
  collectionheader bigint NOT NULL,
  servicedetails bigint NOT NULL,
  transactionnumber character varying(50),
  transactionamount double precision,
  transactiondate timestamp without time zone,
  status bigint,
  authorisation_statuscode character varying(50),
  remarks character varying(256),
  version bigint NOT NULL DEFAULT 1,
  CREATEDBY bigint NOT NULL,
  LASTMODIFIEDBY bigint NOT NULL,
  CREATEDDATE timestamp without time zone,
  LASTMODIFIEDDATE timestamp without time zone
);
CREATE SEQUENCE seq_egcl_onlinepayments;


CREATE TABLE egcl_bank_remittance
(
  id bigint NOT NULL,
  depositedinbank bigint NOT NULL,
  servicedetails bigint NOT NULL,
  bankaccounttoremit bigint NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  CREATEDBY bigint NOT NULL,
  LASTMODIFIEDBY bigint NOT NULL,
  CREATEDDATE timestamp without time zone,
  LASTMODIFIEDDATE timestamp without time zone
);
CREATE SEQUENCE seq_egcl_bank_remittance;


CREATE TABLE egcl_bankaccountservicemapping
(
  id bigint NOT NULL,
  servicedetails bigint NOT NULL,
  bankaccount bigint NOT NULL,
  department bigint NOT NULL,
  fromdate timestamp without time zone,
  todate timestamp without time zone,
  ecstype bigint,
  version bigint NOT NULL DEFAULT 1,
  CREATEDBY bigint NOT NULL,
  LASTMODIFIEDBY bigint NOT NULL,
  CREATEDDATE timestamp without time zone,
  LASTMODIFIEDDATE timestamp without time zone
);
CREATE SEQUENCE seq_egcl_bankaccountservicemapping;

--DROP TABLE egcl_bankaccountservicemapping;
--DROP SEQUENCE seq_egcl_bankaccountservicemapping;
--DROP TABLE egcl_bank_remittance;
--DROP SEQUENCE seq_egcl_bank_remittance;
--DROP TABLE egcl_onlinepayments;
--DROP SEQUENCE seq_egcl_onlinepayments;
--DROP TABLE egcl_collectionvoucher;
--DROP SEQUENCE seq_egcl_collectionvoucher;
--DROP TABLE egcl_collectioninstrument;
--DROP TABLE egcl_challanheader;
--DROP SEQUENCE seq_egcl_challanheader;
--DROP TABLE egcl_accountpayeedetails;
--DROP SEQUENCE seq_egcl_accountpayeedetails;
--DROP TABLE egcl_collectiondetails;
--DROP SEQUENCE seq_egcl_collectiondetails;
--DROP TABLE egcl_collectionmis;
--DROP SEQUENCE seq_egcl_collectionmis;
--DROP TABLE egcl_collectionheader;
--DROP SEQUENCE seq_egcl_collectionheader;
--DROP TABLE egcl_service_subledgerinfo;
--DROP SEQUENCE seq_egcl_service_subledgerinfo;
--DROP TABLE egcl_service_accountdetails;
--DROP SEQUENCE seq_egcl_service_accountdetails;
--DROP TABLE egcl_service_dept_mapping;
--DROP TABLE egcl_servicedetails;
--DROP SEQUENCE seq_egcl_servicedetails;
--DROP TABLE egcl_servicecategory;
--DROP SEQUENCE seq_egcl_servicecategory;
