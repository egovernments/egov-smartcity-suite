--various assessment document types 

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Patta Certificate',false,null,'CREATE_ASMT_DOC',(select id from egpt_application_type where code='CREATE'));

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'MRO Proceedings',false,null,'CREATE_ASMT_DOC',(select id from egpt_application_type where code='CREATE'));

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Will Deed',false,null,'CREATE_ASMT_DOC',(select id from egpt_application_type where code='CREATE'));

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Decree Document',false,null,'CREATE_ASMT_DOC',(select id from egpt_application_type where code='CREATE'));

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Registered Document',false,null,'CREATE_ASMT_DOC',(select id from egpt_application_type where code='CREATE'));

insert into egpt_document_type values(nextval('seq_egpt_document_type'),'Photo of Property With Holder',false,null,'CREATE_ASMT_DOC',(select id from egpt_application_type where code='CREATE'));
