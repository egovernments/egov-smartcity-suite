------------------START------------------
CREATE TABLE egcl_remittance
(
  id bigint NOT NULL,
  version bigint NOT NULL DEFAULT 1,
  referencenumber character varying(50) not null,
  referencedate timestamp without time zone NOT NULL,
  voucherheader bigint,
  fund bigint,
  function bigint,
  remarks character varying(250),
  reasonfordelay character varying(250),
  status bigint NOT NULL,
  state bigint,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egcl_remittance PRIMARY KEY (id),
  CONSTRAINT fk_remit_state FOREIGN KEY (state) REFERENCES eg_wf_states (id),
  CONSTRAINT fk_remit_status FOREIGN KEY (status) REFERENCES egw_status (id),
  CONSTRAINT fk_remit_function FOREIGN KEY (function) REFERENCES function (id),
  CONSTRAINT fk_remit_fund FOREIGN KEY (fund) REFERENCES fund (id),
  CONSTRAINT unq_remit_referencenumber UNIQUE (referencenumber)
);
CREATE INDEX idx_remit_voucher on egcl_remittance (voucherheader);
CREATE INDEX idx_remit_function on egcl_remittance (function);
CREATE INDEX idx_remit_status on egcl_remittance (status);
CREATE INDEX idx_remit_state on egcl_remittance (state);
CREATE SEQUENCE seq_egcl_remittance;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_remittancedetails
(
  id bigint NOT NULL,
  remittance bigint NOT NULL,
  chartofaccount bigint NOT NULL,
  creditamount double precision,
  debitamount double precision,
  CONSTRAINT pk_egcl_remittancedtl PRIMARY KEY (id),
  CONSTRAINT fk_remitdtl_remittance FOREIGN KEY (remittance) REFERENCES egcl_remittance (id),
  CONSTRAINT fk_remitdtl_coa FOREIGN KEY (chartofaccount) REFERENCES chartofaccounts (id)
);
CREATE INDEX idx_remitdtl_remit on egcl_remittancedetails (remittance);
CREATE INDEX idx_remitdtl_coa on egcl_remittancedetails (chartofaccount);
CREATE SEQUENCE seq_egcl_remittancedetails;
-------------------END-------------------
------------------START------------------
CREATE TABLE egcl_collectionremittance
(
  collectionheader bigint NOT NULL,
  remittance bigint NOT NULL,
  CONSTRAINT fk_collhead FOREIGN KEY (collectionheader) REFERENCES egcl_collectionheader (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_remit FOREIGN KEY (remittance) REFERENCES egcl_remittance (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
-------------------END-------------------
