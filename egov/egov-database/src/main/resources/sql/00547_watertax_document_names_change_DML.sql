delete from egwtr_application_documents;
delete from egwtr_document_names where  documentname = 'BPL Document';
UPDATE eg_appconfig_values SET VALUE = 'Ration Card' WHERE KEY_ID = (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DOCUMENTREQUIREDFORBPL');