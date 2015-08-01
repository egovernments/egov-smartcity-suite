delete from egwtr_document_names where applicationtype in(select id from egwtr_application_type where code = 'NEWCONNECTION');

INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Ration card',  
            'Ration card', false, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Sale deed document', 
            'Sale deed document', false, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Last paid property tax receipt',  
            'Last paid property tax receipt', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Occupancy certificate from the town planning department', 
            'Occupancy certificate from the town planning department', false, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Sump Picture', 
            'Sump Picture', false, true, now(), 1);  
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Photo of Building', 
            'Photo of Building', false, true, now(), 1);  
            
--rollback delete from egwtr_document_names where applicationtype in(select id from egwtr_application_type where code = 'NEWCONNECTION');
--rollback INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby) VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Registered Sale Seed', 'Registered sale deed of the building', true, true, now(), 1);
--rollback INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby) VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Latest Property Tax Paid Receipt', 'Last paid copy of Property Tax paid receipt', true, true, now(), 1);
--rollback INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby) VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Copy of Building Plan Permission', 'Copy of Building approval plan/permission', true, true, now(), 1);
--rollback INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby) VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Details of existing slumps and certificates', 'Details of existing slumps and certificates', true, true, now(), 1);