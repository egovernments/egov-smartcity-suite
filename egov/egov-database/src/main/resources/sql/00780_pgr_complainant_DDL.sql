alter table egpgr_complainant  add column address character varying(256);
--rollback alter table egpgr_complainant drop COLUMN address;
