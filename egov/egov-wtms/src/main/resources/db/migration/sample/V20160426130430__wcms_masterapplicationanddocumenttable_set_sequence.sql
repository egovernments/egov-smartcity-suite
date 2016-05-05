SELECT setval('SEQ_EGWTR_DOCUMENT_NAMES', COALESCE((SELECT MAX(id)+1 FROM egwtr_document_names), 1), false);

SELECT setval('SEQ_EGWTR_APPLICATION_PROCESS_TIME', COALESCE((SELECT MAX(id)+1 FROM egwtr_application_process_time), 1), false);