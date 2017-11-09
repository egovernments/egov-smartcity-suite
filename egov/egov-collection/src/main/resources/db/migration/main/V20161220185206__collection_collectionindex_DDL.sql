DROP INDEX IF EXISTS idx_collidx_billingservice;
DROP INDEX IF EXISTS idx_collidx_consumertype;
create index idx_collidx_billingservice on eg_collectionindex(billingservice);
create index idx_collidx_consumertype on eg_collectionindex(consumertype);
