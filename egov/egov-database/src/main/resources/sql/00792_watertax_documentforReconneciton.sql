
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'RECONNECTION'), 'Other',  
            'Other', false, true, now(), 1);  