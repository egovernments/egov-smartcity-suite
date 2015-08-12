alter table egpt_property rename column id_modify_reason to modify_reason;
alter table egpt_property alter column modify_reason type character varying (25);

--rollback alter table egpt_property rename column modify_reason to id_modify_reason;