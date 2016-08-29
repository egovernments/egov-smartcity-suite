INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'SearchAgendaToCreateMeeting','/agenda/searchagenda-tocreatemeeting',(select id from eg_module where name='Council Agenda' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'SearchAgendaToCreateMeeting',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchAgendaToCreateMeeting'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),
'SearchMeetingToCreateMOM','/councilmeeting/searchmeeting-tocreatemom',(select id from eg_module where name='Council Meeting' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),4,'SearchMeetingToCreateMOM',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchMeetingToCreateMOM'));

-- Disable edit option as workflow implemented

update eg_action set enabled=false where name='Search and Edit-CouncilPreamble' and contextroot='council';

-- app config to enable sms and email notifications
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SENDSMSFORCOUNCILMEMBER', 'SMS Notification for councilmember is enabled or not',0, (select id from eg_module where name='Council Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDSMSFORCOUNCILMEMBER' AND MODULE =(select id from eg_module where name='Council Management')),current_date, 'YES',0);

INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'SENDEMAILFORCOUNCILMEMBER', 'Email Notification for councilmember is enabled or not',0, (select id from eg_module where name='Council Management'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='SENDEMAILFORCOUNCILMEMBER' AND MODULE =(select id from eg_module where name='Council Management')),current_date, 'YES',0);
