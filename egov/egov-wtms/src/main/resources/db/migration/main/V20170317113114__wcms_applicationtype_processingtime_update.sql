update egwtr_application_process_time set processingtime = 15 where applicationtype=(select id from egwtr_application_type where code = 'NEWCONNECTION');
update egwtr_application_process_time set processingtime = 15 where applicationtype=(select id from egwtr_application_type where code = 'ADDNLCONNECTION');
update egwtr_application_process_time set processingtime = 15 where applicationtype=(select id from egwtr_application_type where code = 'CHANGEOFUSE');
update egwtr_application_process_time set processingtime = 15 where applicationtype=(select id from egwtr_application_type where code = 'RECONNECTION');
update egwtr_application_process_time set processingtime = 7 where applicationtype=(select id from egwtr_application_type where code = 'CLOSINGCONNECTION');

