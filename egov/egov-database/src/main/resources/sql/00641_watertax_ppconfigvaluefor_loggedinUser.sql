
INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 
'RolesForSearchWAterTaxConnection', 
'Roles To Show Action dropdown In search connection',0, (select id from eg_module where name='Water Tax Management')); 

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='RolesForSearchWAterTaxConnection' AND 
 MODULE =(select id from eg_module where name='Water Tax Management')),current_date,
  'CSC Operator',0);
  INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='RolesForSearchWAterTaxConnection' AND 
 MODULE =(select id from eg_module where name='Water Tax Management')),current_date,
  'ULB Operator',0);
  INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='RolesForSearchWAterTaxConnection' AND 
 MODULE =(select id from eg_module where name='Water Tax Management')),current_date,
  'Water Tax Approver',0);

  INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='RolesForSearchWAterTaxConnection' AND 
 MODULE =(select id from eg_module where name='Water Tax Management')),current_date,
  'Super User',0);
  
  INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='RolesForSearchWAterTaxConnection' AND 
 MODULE =(select id from eg_module where name='Water Tax Management')),current_date,
  'Operator',0);
