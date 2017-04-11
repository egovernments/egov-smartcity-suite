update egw_est_template_activity set version = 0 where id in (select id from egw_est_template_activity where version is null);
update egw_estimate_template set version = 0 where id in (select id from egw_estimate_template where version is null);
alter table egw_est_template_activity alter column version set default 0;
alter table egw_estimate_template alter column version set default 0;

--rollback alter table egw_est_template_activity alter column version drop default;
--rollback alter table egw_estimate_template alter column version drop default;
--rollback update egw_est_template_activity set version = null where id in (select id from egw_est_template_activity where version=0);
--rollback update egw_estimate_template set version = null where id in (select id from egw_estimate_template where version=0);