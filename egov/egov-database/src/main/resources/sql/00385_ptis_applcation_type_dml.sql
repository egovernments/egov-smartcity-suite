INSERT INTO egpt_application_type(id, code, name, resolutiontime, description, createddate, createdby) VALUES (nextval('seq_egpt_application_type'), 'CREATE', 'CREATE', 15, 'Create Assessment', now(), 1);
INSERT INTO egpt_application_type(id, code, name, resolutiontime, description, createddate, createdby) VALUES (nextval('seq_egpt_application_type'), 'MODIFY', 'MODIFY', 15, 'Alter Assessment', now(), 1);
INSERT INTO egpt_application_type(id, code, name, resolutiontime, description, createddate, createdby) VALUES (nextval('seq_egpt_application_type'), 'TRANSFER', 'TRANSFER', 15, 'Transfer Assessment', now(), 1);

update egpt_document_type set id_application_type = (select id from egpt_application_type where name = 'CREATE') where transactiontype = 'CREATE';
update egpt_document_type set id_application_type = (select id from egpt_application_type where name = 'MODIFY') where transactiontype = 'MODIFY';
update egpt_document_type set id_application_type = (select id from egpt_application_type where name = 'TRANSFER') where transactiontype = 'TRANSFER';

--rollback update egpt_document_type set id_application_type = null;
--rollback delete from egpt_application_type;