#UP

/************ Sample data on EG_APPCONFIG_VALUES for capturing deceased date. *************/

   Insert into EG_APPCONFIG_VALUES
   (ID, KEY_ID, EFFECTIVE_FROM, VALUE)
 Values
(SEQ_EG_APPCONFIG_VALUES.nextval,(select app.id from eg_appconfig app where app.key_name='STATUS_MODULE_TYPE'  and app.module='EIS_STATUS'), TO_DATE('02/25/2010  00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'Employee');

Insert into EG_APPCONFIG_VALUES
   (ID, KEY_ID, EFFECTIVE_FROM, VALUE)
 Values
(SEQ_EG_APPCONFIG_VALUES.nextval,(select app.id from eg_appconfig app where app.key_name='STATUS_DESCRIPTION'  and app.module='EIS_STATUS'), TO_DATE('02/25/2010 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'Deceased');


#DOWN