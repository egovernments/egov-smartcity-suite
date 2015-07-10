INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'ADDNLCONNECTION'), 'Registered Sale Seed',  
            'Registered sale deed of the building', false, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'ADDNLCONNECTION'), 'Latest Property Tax Paid Receipt','Last paid copy of Property Tax paid receipt', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'ADDNLCONNECTION'), 'Copy of Building Plan Permission','Copy of Building approval plan/permission', false, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'ADDNLCONNECTION'), 'Details of existing slumps and certificates','Details of existing slumps and certificates', true, true, now(), 1);
