alter table egpt_basic_property drop column source;

alter table egpt_basic_property rename column ismigrated to source;

--rollback alter table egpt_basic_property rename column source to  ismigrated;
--rollback alter table egpt_basic_property add  column source varchar(30);;