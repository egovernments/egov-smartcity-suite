alter table egpt_basic_property drop column applicationno;
alter table egpt_property add applicationno character varying(25);

--rollback alter table egpt_property drop column applicationno;
--rollback alter table egpt_basic_property add applicationno character varying(25);