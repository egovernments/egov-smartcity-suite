ALTER TABLE egtl_feematrix ADD COLUMN financialyear bigint;
ALTER TABLE egtl_feematrix ADD CONSTRAINT fk_egtl_feematrix_finyear FOREIGN KEY(financialyear) REFERENCES financialyear(id);
