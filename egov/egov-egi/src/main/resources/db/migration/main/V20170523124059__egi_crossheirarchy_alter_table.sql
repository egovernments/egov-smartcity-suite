alter table eg_crosshierarchy add column lastmodifieddate timestamp without time zone default now();
alter table eg_crosshierarchy add column createddate timestamp without time zone default now();



--rollback alter table eg_crosshierarchy drop column lastmodifieddate;
--rollback alter table eg_crosshierarchy drop column createddate;

