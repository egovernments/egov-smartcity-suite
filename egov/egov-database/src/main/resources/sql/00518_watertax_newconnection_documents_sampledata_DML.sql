delete from egwtr_document_names where applicationtype in(select id from egwtr_application_type where code = 'NEWCONNECTION');

INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
   VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'BPL Document',  
           'Ration card', false, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
   VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Ration Card', 
            'Ration Card', false, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
   VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Other' , 'Other Documents', false, true, now(), 1);
