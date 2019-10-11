CREATE TABLE EGPT_DEMANDVOUCHER
(
  id bigint NOT NULL primary key,
  property bigint NOT NULL,
  voucherheader bigint NOT NULL,
  version numeric default 1,
  createdby numeric default 1,
  createddate timestamp without time zone default now(),
  lastmodifiedby numeric default 1,
  lastmodifieddate timestamp without time zone default now()
);

ALTER TABLE egpt_demandvoucher ADD constraint fk_pt_voucher foreign key (property) references egpt_property (id);
CREATE INDEX idx_demandvoucher_property ON egpt_demandvoucher(property);
CREATE INDEX idx_demandvoucher_voucherheader ON egpt_demandvoucher(voucherheader);

COMMENT ON COLUMN egpt_demandvoucher.property IS 'primary key of egpt_property';
COMMENT ON COLUMN egpt_demandvoucher.voucherheader IS 'primary key of voucherheader';
COMMENT ON TABLE egpt_demandvoucher IS 'This table stores link between property and voucherheader';

CREATE SEQUENCE SEQ_EGPT_DEMANDVOUCHER;

--rollback queries
--drop table egpt_demandvoucher;
--drop sequence seq_egpt_demandvoucher;