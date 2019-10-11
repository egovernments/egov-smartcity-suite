drop view if exists onlinepayment_view;
alter table egcl_collectionheader alter column payeename type character varying(512);