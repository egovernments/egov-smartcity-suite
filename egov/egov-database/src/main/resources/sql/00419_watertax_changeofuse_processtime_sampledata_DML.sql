INSERT INTO egwtr_application_process_time(id, applicationtype, category, processingtime, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_PROCESS_TIME'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 
    (select id from egwtr_category where code = 'BPL'), 15, true, now(), 1);
INSERT INTO egwtr_application_process_time(id, applicationtype, category, processingtime, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_PROCESS_TIME'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 
    (select id from egwtr_category where code = 'COMMERCIAL'), 45, true, now(), 1);
INSERT INTO egwtr_application_process_time(id, applicationtype, category, processingtime, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_PROCESS_TIME'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 
    (select id from egwtr_category where code = 'DOMESTIC'), 30, true, now(), 1);
INSERT INTO egwtr_application_process_time(id, applicationtype, category, processingtime, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_PROCESS_TIME'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 
    (select id from egwtr_category where code = 'GENERAL'), 30, true, now(), 1);
INSERT INTO egwtr_application_process_time(id, applicationtype, category, processingtime, active, createddate, createdby)
    VALUES (nextval('SEQ_EGWTR_APPLICATION_PROCESS_TIME'), (select id from egwtr_application_type where code = 'CHANGEOFUSE'), 
    (select id from egwtr_category where code = 'OYT'), 30, true, now(), 1);
    
--rollback delete from egwtr_application_process_time where applicationtype=(select id from egwtr_application_type where code = 'CHANGEOFUSE');
