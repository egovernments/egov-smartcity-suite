drop index if exists idx_collmis_boundaryid;
create index idx_collmis_boundaryid on egcl_collectionmis(boundary);