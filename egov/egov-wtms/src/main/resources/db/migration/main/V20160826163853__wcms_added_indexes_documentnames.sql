DROP INDEX IF EXISTS idx_documentnames_name;
create index "idx_documentnames_name" on egwtr_document_names(documentname);
alter table egwtr_documents drop constraint if exists fk_documents_appdocument;
delete from egwtr_documents where applicationdocumentsid not in (select distinct id from egwtr_application_documents);
alter table egwtr_documents add constraint fk_documents_appdocument foreign key (applicationdocumentsid) references egwtr_application_documents(id);