CREATE TABLE egwtr_legacy_receipts
(
  id bigint NOT NULL,  -- Primary Key
  booknumber character varying(64), -- Book no
  receiptnumber character varying(50) NOT NULL,  -- receipt no
  receiptdate timestamp without time zone NOT NULL, -- receipt date
  amount double precision NOT NULL, -- receipt amount
  fromdate timestamp without time zone,  -- receipt from date
  todate timestamp without time zone, -- receipt to date
  connectiondetails bigint NOT NULL, -- FK to egwtr_connectiondetails
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone NOT NULL,
  lastmodifiedby bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_legacy_receipts_pkey PRIMARY KEY (id),
  CONSTRAINT fk_legacy_receipts_connectiondetails_fkey FOREIGN KEY (connectiondetails) REFERENCES egwtr_connectiondetails (id)
);


CREATE SEQUENCE seq_egwtr_legacy_receipts;

CREATE INDEX idx_legacyreceipts_conndetails ON egwtr_connectiondetails (id);


COMMENT ON TABLE egwtr_legacy_receipts IS 'Water Charges Migrated Receipts';
COMMENT ON COLUMN egwtr_legacy_receipts.id IS 'Primary Key';
COMMENT ON COLUMN egwtr_legacy_receipts.booknumber IS 'Book No';
COMMENT ON COLUMN egwtr_legacy_receipts.receiptnumber IS 'Receipt No';
COMMENT ON COLUMN egwtr_legacy_receipts.receiptdate IS 'Receipt Date';
COMMENT ON COLUMN egwtr_legacy_receipts.amount IS 'Receipt Amount';
COMMENT ON COLUMN egwtr_legacy_receipts.fromdate IS 'Receipt from date';
COMMENT ON COLUMN egwtr_legacy_receipts.todate IS 'Receipt to date';
COMMENT ON COLUMN egwtr_legacy_receipts.connectiondetails IS 'Waterconnectiondetails id - foriegn key';