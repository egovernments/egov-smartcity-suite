alter table egp_inbox add column resolveddate timestamp without time zone;

--rollback alter table egp_inbox drop column resolveddate;