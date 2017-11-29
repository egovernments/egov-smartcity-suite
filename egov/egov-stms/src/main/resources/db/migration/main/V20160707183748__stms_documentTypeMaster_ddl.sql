------ Document type master ----------------------
alter table egswtax_document_type_master  drop constraint unq_egswtax_document_name;
alter table egswtax_document_type_master  add constraint unq_egswtax_document_name_appType unique(description,applicationtype);

insert into egswtax_document_type_master(id,description,isactive,applicationtype,ismandatory,version) values (nextval('seq_egswtax_document_type_master'),'Others','t',(select id from egswtax_application_type where code='CHANGEINCLOSETS' and active='t'),'f',0);

