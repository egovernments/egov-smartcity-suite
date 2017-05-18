ALTER TABLE egp_inbox ADD CONSTRAINT egp_inbox_unique_module_applicationnumber UNIQUE (moduleid,applicationnumber);

--rollback alter table egp_inbox drop CONSTRAINT egp_inbox_unique_module_applicationnumber;