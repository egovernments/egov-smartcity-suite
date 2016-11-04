ALTER TABLE eg_billregister ALTER COLUMN billstatus DROP NOT NULL;

--rollback update eg_billregister set billstatus = 'Approved' where billstatus is null;
--rollback ALTER TABLE eg_billregister ALTER COLUMN billstatus SET NOT NULL;
