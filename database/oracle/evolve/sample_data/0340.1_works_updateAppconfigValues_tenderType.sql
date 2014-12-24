#UP

UPDATE eg_appconfig_values set value='PERCENTAGE,RATE' where key_id=(select id from eg_appconfig where key_name='TENDER_TYPE');

#DOWN

UPDATE eg_appconfig_values set value='Percentage-Tender,Item-Rate' where key_id=(select id from eg_appconfig where key_name='TENDER_TYPE');
