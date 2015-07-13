    
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 'Registered Sale Seed',  
            'Registered sale deed of the building', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 'Latest Property Tax Paid Receipt', 
            'Last paid copy of Property Tax paid receipt', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 'Ration Card',  
            'Ration Card', true, true, now(), 1);
INSERT INTO egwtr_document_names(id, applicationtype, documentname, description, required, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_DOCUMENT_NAMES'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 'Aadhar card Xerox', 
            'Aadhar card Xerox', true, true, now(), 1);
            
--rollback delete from egwtr_document_names where applicationtype = (select id from egwtr_application_type where code = 'CHANGEOFUSE');
