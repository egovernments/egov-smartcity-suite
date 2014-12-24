#UP
UPDATE eg_appconfig_values
SET value     ='Part Bill,Final Bill'
WHERE key_id IN
  (SELECT id FROM eg_appconfig WHERE key_name='BILLTYPE'
  );
INSERT INTO EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) VALUES 
(SEQ_EG_APPCONFIG_VALUES.nextval, (select ID from EG_APPCONFIG where KEY_NAME='WORKS_ASSET_STATUS'),
  TO_Date( '04/08/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'Created,CWIP');

#DOWN
UPDATE eg_appconfig_values
SET value     ='Part Bill,Final Bill'
WHERE key_id IN
  (SELECT id FROM eg_appconfig WHERE key_name='BILLTYPE'
  );
 
