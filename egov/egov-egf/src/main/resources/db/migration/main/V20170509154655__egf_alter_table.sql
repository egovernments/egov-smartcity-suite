alter table egf_accountcode_purpose add column lastmodifieddate timestamp without time zone default now();
alter table accountdetailkey add column lastmodifieddate timestamp without time zone default now();

--rollback alter table egf_accountcode_purpose drop column lastmodifieddate;
--rollback alter table accountdetailkey drop column lastmodifieddate;
