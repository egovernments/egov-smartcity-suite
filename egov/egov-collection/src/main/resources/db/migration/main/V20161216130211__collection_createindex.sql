DROP INDEX IF EXISTS idx_collhd_consumercode;
DROP INDEX IF EXISTS idx_collindex_consumercode;
CREATE INDEX idx_collhd_consumercode on egcl_collectionheader(consumercode);
create index idx_collindex_consumercode on eg_collectionindex(consumercode);
