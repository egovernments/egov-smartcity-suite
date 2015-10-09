------------------START------------------
CREATE TABLE egcl_servicecategory
(
  id bigint NOT NULL,
  name character varying(256) NOT NULL,
  code character varying(50) NOT NULL,
  isactive boolean,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egcl_servicecategory PRIMARY KEY (id)
);
CREATE SEQUENCE seq_egcl_servicecategory;
-------------------END-------------------
------------------START------------------
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
  vouchercutoffdate timestamp without time zone,
  created_by bigint NOT NULL,
  created_date timestamp without time zone NOT NULL,
  modified_by bigint NOT NULL,
  modified_date timestamp without time zone NOT NULL,
  ordernumber integer,
  CONSTRAINT pk_egcl_servicedetails PRIMARY KEY (id),
  CONSTRAINT fk_serdtls_fsource FOREIGN KEY (fundsource) REFERENCES fundsource (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_serdtls_functionary FOREIGN KEY (functionary) REFERENCES functionary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_serdtls_fund FOREIGN KEY (fund) REFERENCES fund (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_serdtls_scheme FOREIGN KEY (scheme) REFERENCES scheme (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_serdtls_servicecat FOREIGN KEY (servicecategory) REFERENCES egcl_servicecategory (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_serdtls_subscheme FOREIGN KEY (subscheme) REFERENCES sub_scheme (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egcl_servicedetails;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_service_dept_mapping
(
  department bigint NOT NULL,
  servicedetails bigint NOT NULL,
  CONSTRAINT egcl_ser_dept_map_srvcdtls_fk FOREIGN KEY (servicedetails) REFERENCES egcl_servicedetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT egcl_service_deptmapping_dept_fk FOREIGN KEY (department) REFERENCES eg_department (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_service_accountdetails
(
  id bigint NOT NULL,
  servicedetails bigint NOT NULL,
  chartofaccount bigint NOT NULL,
  amount double precision,
  functionid bigint,
  CONSTRAINT pk_egcl_service_accountdetails PRIMARY KEY (id),
  CONSTRAINT fk_egcl_srvcacc_srvdtils FOREIGN KEY (servicedetails) REFERENCES egcl_servicedetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_egcl_srvcaccdtls_coa FOREIGN KEY (chartofaccount) REFERENCES chartofaccounts (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_serviceaccdtls_function FOREIGN KEY (functionid) REFERENCES function (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egcl_service_accountdetails;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_service_subledgerinfo
(
  id bigint NOT NULL,
  accountdetailtype bigint NOT NULL,
  accountdetailkey bigint,
  amount double precision,
  serviceaccountdetail bigint NOT NULL,
  CONSTRAINT pk_egcl_service_subledgerinfo PRIMARY KEY (id),
  CONSTRAINT fk_egcl_subdtls_accdtltyp FOREIGN KEY (accountdetailtype) REFERENCES accountdetailtype (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_egcl_subledgerdetails_srvcacc FOREIGN KEY (serviceaccountdetail) REFERENCES egcl_service_accountdetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egcl_service_subledgerinfo;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_collectionheader
(
  id bigint NOT NULL,
  referencenumber character varying(50),
  referencedate timestamp without time zone,
  receipttype character(1) NOT NULL,
  receiptnumber character varying(50),
  receiptdate timestamp without time zone NOT NULL,
  referencedesc character varying(250),
  manualreceiptnumber character varying(50),
  manualreceiptdate timestamp without time zone,
  ismodifiable boolean,
  servicedetails bigint NOT NULL,
  collectiontype character(1),
  state_id bigint,
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
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egcl_collectionheader PRIMARY KEY (id),
  CONSTRAINT fk_collhead_chid FOREIGN KEY (reference_ch_id) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collhead_location FOREIGN KEY (location) REFERENCES eg_location (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collhead_service FOREIGN KEY (servicedetails) REFERENCES egcl_servicedetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collhead_state FOREIGN KEY (state_id) REFERENCES eg_wf_states (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collhead_status FOREIGN KEY (status) REFERENCES egw_status (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unq_ch_receiptnumber UNIQUE (receiptnumber)
);
CREATE INDEX idx_collhd_locid ON egcl_collectionheader USING btree (location);
CREATE INDEX idx_collhd_refchid ON egcl_collectionheader USING btree (reference_ch_id);
CREATE INDEX idx_collhd_state ON egcl_collectionheader USING btree (state_id);
CREATE SEQUENCE seq_egcl_collectionheader;
-------------------END-------------------
------------------START------------------
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
  depositedinbank bigint,
  CONSTRAINT pk_egcl_collectionmis PRIMARY KEY (id),
  CONSTRAINT fk_collmis_bank FOREIGN KEY (depositedinbank) REFERENCES bank (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_boundary FOREIGN KEY (boundary) REFERENCES eg_boundary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_department FOREIGN KEY (department) REFERENCES eg_department (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_fund FOREIGN KEY (fund) REFERENCES fund (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_fundsource FOREIGN KEY (fundsource) REFERENCES fundsource (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_funtionary FOREIGN KEY (functionary) REFERENCES functionary (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_scheme FOREIGN KEY (scheme) REFERENCES scheme (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collmis_subscheme FOREIGN KEY (subscheme) REFERENCES sub_scheme (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX indx_collmis_depositedinbank ON egcl_collectionmis USING btree (depositedinbank);
CREATE INDEX indx_collmis_deptid ON egcl_collectionmis USING btree (department);
CREATE SEQUENCE seq_egcl_collectionmis;
-------------------END-------------------
------------------START------------------
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
  isactualdemand boolean,
  CONSTRAINT pk_egcl_collectiondetails PRIMARY KEY (id),
  CONSTRAINT fk_colldtls_coa FOREIGN KEY (chartofaccount) REFERENCES chartofaccounts (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_colldtls_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_colldtls_finyear FOREIGN KEY (financialyear) REFERENCES financialyear (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_colldtls_function FOREIGN KEY (functionid) REFERENCES function (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_colldet_acchead ON egcl_collectiondetails USING btree (chartofaccount);
CREATE SEQUENCE seq_egcl_collectiondetails;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_accountpayeedetails
(
  id bigint NOT NULL,
  collectiondetails bigint NOT NULL,
  accountdetailstype bigint NOT NULL,
  accountdetailskey bigint NOT NULL,
  amount bigint,
  CONSTRAINT pk_egcl_accountpayeedetails PRIMARY KEY (id),
  CONSTRAINT fk_apd_accdtlkey FOREIGN KEY (accountdetailskey) REFERENCES accountdetailkey (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_apd_accdtltype FOREIGN KEY (accountdetailstype) REFERENCES accountdetailtype (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_apd_colldtl FOREIGN KEY (collectiondetails) REFERENCES egcl_collectiondetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egcl_accountpayeedetails;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_challanheader
(
  id bigint NOT NULL,
  challannumber character varying(25) NOT NULL,
  collectionheader bigint NOT NULL,
  validupto timestamp without time zone NOT NULL,
  status bigint,
  state_id bigint,
  challandate timestamp without time zone NOT NULL,
  servicedetails bigint,
  reasonforcancellation character varying(256),
  voucherheader bigint,
  oldchallannumber character varying(25),
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egcl_challanheader PRIMARY KEY (id),
  CONSTRAINT fk_chlnhead_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_chlnhead_service FOREIGN KEY (servicedetails) REFERENCES egcl_servicedetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_chlnhead_state FOREIGN KEY (state_id) REFERENCES eg_wf_states (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_chlnhead_status FOREIGN KEY (status) REFERENCES egw_status (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_chlnhead_voucher FOREIGN KEY (voucherheader) REFERENCES voucherheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unq_challanheader_challannumber UNIQUE (challannumber)
);
CREATE INDEX idx_challan_collheaderid ON egcl_challanheader USING btree (collectionheader);
CREATE INDEX idx_challan_state ON egcl_challanheader USING btree (state_id);
CREATE INDEX idx_challan_status ON egcl_challanheader USING btree (status);
CREATE SEQUENCE seq_egcl_challanheader;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_collectioninstrument
(
  collectionheader bigint NOT NULL,
  instrumentheader bigint NOT NULL,
  CONSTRAINT fk_collinst_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_collectionvoucher
(
  id bigint NOT NULL,
  collectionheader bigint NOT NULL,
  voucherheader bigint,
  CONSTRAINT pk_egcl_collectionvoucher PRIMARY KEY (id),
  CONSTRAINT fk_collvouch_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_collvouch_voucher FOREIGN KEY (voucherheader) REFERENCES voucherheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egcl_collectionvoucher;
-------------------END-------------------
------------------START------------------
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
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egcl_onlinepayments PRIMARY KEY (id),
  CONSTRAINT fk_onpay_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_onpay_service FOREIGN KEY (servicedetails) REFERENCES egcl_servicedetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_onpay_status FOREIGN KEY (status) REFERENCES egw_status (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX idx_online_service ON egcl_onlinepayments USING btree (servicedetails);
CREATE INDEX idx_online_status ON egcl_onlinepayments USING btree (status);
CREATE INDEX idx_op_collheaderid ON egcl_onlinepayments USING btree (collectionheader);
CREATE SEQUENCE seq_egcl_onlinepayments;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_bank_remittance
(
  id bigint NOT NULL,
  depositedinbank bigint NOT NULL,
  servicedetails bigint NOT NULL,
  bankaccounttoremit bigint NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egcl_bank_remittance PRIMARY KEY (id),
  CONSTRAINT fk_bankrmtnc_bank FOREIGN KEY (depositedinbank) REFERENCES bank (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_bankrmtnc_bankaccount FOREIGN KEY (bankaccounttoremit) REFERENCES bankaccount (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_bankrmtnc_service FOREIGN KEY (servicedetails) REFERENCES egcl_servicedetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egcl_bank_remittance;
-------------------END-------------------
------------------START------------------
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
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egcl_bankaccountservicemapping PRIMARY KEY (id),
  CONSTRAINT fk_basm_bankaccount FOREIGN KEY (bankaccount) REFERENCES bankaccount (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_basm_department FOREIGN KEY (department) REFERENCES eg_department (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_basm_service FOREIGN KEY (servicedetails) REFERENCES egcl_servicedetails (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE SEQUENCE seq_egcl_bankaccountservicemapping;
-------------------END-------------------