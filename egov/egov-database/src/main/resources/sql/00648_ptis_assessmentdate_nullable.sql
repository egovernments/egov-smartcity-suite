alter table egpt_basic_property alter column assessmentdate drop not null;

--rollback alter table egpt_basic_property alter column assessmentdate set not null;
