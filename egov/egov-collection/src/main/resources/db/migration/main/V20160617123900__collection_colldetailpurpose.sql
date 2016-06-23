alter table egcl_collectiondetails add column purpose character varying(50);
update egcl_collectiondetails set purpose='OTHERS' where purpose is null;
alter table egcl_collectiondetails alter column purpose set not null;
CREATE INDEX indx_colldet_purpose ON egcl_collectiondetails (purpose);
--alter table egcl_collectiondetails drop column if exists purpose;
