#UP
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='TenderResponse.laststatus'),
to_date('21/05/2010','dd/MM/yyyy'),'Agreement Order signed');
insert into eg_appconfig_values(id,key_id, effective_from,value)
values(seq_eg_appconfig_values.nextval,(select id from  eg_appconfig where key_name='TenderResponse.setstatus'),
to_date('21/05/2010','dd/MM/yyyy'),'Acceptance Letter Issued,AcceptanceLetterAcknowledged,Agreement Order signed');
#DOWN
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='TenderResponse.setstatus');
DELETE FROM EG_APPCONFIG_VALUES where KEY_ID in (select id from EG_APPCONFIG where KEY_NAME='TenderResponse.laststatus');

