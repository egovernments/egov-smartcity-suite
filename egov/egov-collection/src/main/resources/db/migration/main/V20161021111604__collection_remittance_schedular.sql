CREATE TABLE egcl_remittance_instrument
(
  id bigint NOT NULL,
  remittance bigint,
  instrumentheader bigint,
  reconciled boolean default false,
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifiedby bigint NOT NULL,
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_remittance_instrument PRIMARY KEY (id),
  CONSTRAINT fk_remit_remittance FOREIGN KEY (remittance) REFERENCES egcl_remittance (id),
  CONSTRAINT fk_remit_instrument FOREIGN KEY (instrumentheader) REFERENCES egf_instrumentheader (id)
);

CREATE INDEX idx_remit_remittance on egcl_remittance_instrument (remittance);
CREATE INDEX idx_remit_instrument on egcl_remittance_instrument (instrumentheader);

CREATE SEQUENCE seq_egcl_remittance_instrument;
