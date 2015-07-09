alter table egpt_property_docs  add column supportdoc bigint not null;
alter table egpt_property_docs drop doc_number;