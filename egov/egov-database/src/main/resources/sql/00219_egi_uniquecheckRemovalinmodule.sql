alter table eg_module drop constraint uk_eg_module_code;

--rollback alter table eg_module add CONSTRAINT uk_eg_module_code UNIQUE (code);