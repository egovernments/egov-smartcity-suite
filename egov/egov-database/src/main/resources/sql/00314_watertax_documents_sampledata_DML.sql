    
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Registered Sale Seed',  
            'Registered sale deed of the building', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Latest Property Tax Paid Receipt', 
            'Last paid copy of Property Tax paid receipt', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Copy of Building Plan Permission',  
            'Copy of Building approval plan/permission', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'NEWCONNECTION'), 'Details of existing slumps and certificates', 
            'Details of existing slumps and certificates', true, true, now(), 1);
            
--rollback delete from egwtr_document_names;
