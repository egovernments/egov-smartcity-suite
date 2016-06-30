ALTER TABLE egcl_remittance ADD COLUMN bankaccount bigint;

ALTER TABLE egcl_remittance ADD CONSTRAINT fk_rmtnc_bankaccount FOREIGN KEY (bankaccount) REFERENCES bankaccount (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

COMMENT ON COLUMN egcl_remittance.bankaccount IS 'Bank Account where Amount is Remitted';

  
