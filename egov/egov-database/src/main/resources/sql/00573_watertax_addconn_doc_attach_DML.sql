DELETE FROM egwtr_document_names WHERE applicationtype IN(SELECT id FROM egwtr_application_type WHERE code = 'ADDNLCONNECTION');

INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'ADDNLCONNECTION'), 'Other1',  
            'Other1', false, true, now(), 1);

INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'ADDNLCONNECTION'), 'Other2',  
            'Other2', false, true, now(), 1);  
