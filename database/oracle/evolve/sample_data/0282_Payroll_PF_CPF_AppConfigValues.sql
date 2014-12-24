#UP

/************ Sample data on EG_APPCONFIG_VALUES for PF Setup *************/

Insert into EG_APPCONFIG_VALUES
   (ID, KEY_ID, EFFECTIVE_FROM, VALUE)
 Values
(SEQ_EG_APPCONFIG_VALUES.nextval,(select app.id from eg_appconfig app where app.key_name='RunOnSave'  and app.module='PFModule'), TO_DATE('04/01/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'Y');

/************ Sample data on EG_APPCONFIG_VALUES for CPF Trigger *************/

Insert into EG_APPCONFIG_VALUES
   (ID, KEY_ID, EFFECTIVE_FROM, VALUE)
 Values
(SEQ_EG_APPCONFIG_VALUES.nextval,(select app.id from eg_appconfig app where app.key_name='RunOnSave'  and app.module='CPFModule'), TO_DATE('04/01/2007 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'Y');

#DOWN
