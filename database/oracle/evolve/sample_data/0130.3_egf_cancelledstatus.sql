#UP

INSERT INTO EG_APPCONFIG_VALUES ( ID, KEY_ID, EFFECTIVE_FROM, VALUE ) VALUES (
seq_eg_appconfig_values.nextval, (select id from EG_APPCONFIG where key_name='cancelledstatus'),  TO_Date( '12/17/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), '4');

#DOWN

delete from eg_APPCONFIG_values where key_id in(select id from eg_appconfig where key_name='cancelledstatus');
