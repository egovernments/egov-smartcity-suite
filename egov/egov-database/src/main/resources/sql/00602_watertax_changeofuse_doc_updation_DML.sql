DELETE FROM egwtr_document_names WHERE documentname not in('Other') AND applicationtype IN(
SELECT id FROM egwtr_application_type WHERE code ='CHANGEOFUSE');
