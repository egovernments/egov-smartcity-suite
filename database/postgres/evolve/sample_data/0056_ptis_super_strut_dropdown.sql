#UP

  
  --------------EG_APPCONFIG  
  
  insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'MODIFY_START_DATE','List Installments From This Date','Existing property');
  
  --------------EG_APPCONFIG_VALUES 
  
  insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'MODIFY_START_DATE'),to_date('01/04/2009','dd/MM/yyyy'),'01/04/1998');

  update EG_APPCONFIG_values set value ='Temple Land,Waqf board Land,Central Govt. Land,State Govt. Land,Church Land,Private Land,Chennai Corporation,Others' where key_id =(select id from EG_APPCONFIG where module like 'New Property' and key_name like 'Super Structure');

  INSERT INTO EG_ACTION ( ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID,MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL ,CONTEXT_ROOT) VALUES (SEQ_EG_ACTION.nextval, 'transFerOwnerAck', NULL, NULL,  TO_Date( '01/09/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), '/ptis/property/ack.jsp', NULL, NULL, (select id_module from eg_module where module_name='Existing property'), 1, 'transFerOwnerAck ', 0, NULL,'PTIS'); 
  
  INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( (select id_role from eg_roles where role_name='ASSISTANT'), ( select ID from EG_ACTION where name='transFerOwnerAck'  AND CONTEXT_ROOT = 'PTIS' ));
  
  COMMIT;

#DOWN
