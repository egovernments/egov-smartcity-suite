#UP

Insert into EG_APPCONFIG_VALUES
   (ID, KEY_ID, EFFECTIVE_FROM, VALUE)
 Values
(SEQ_EG_APPCONFIG_VALUES.nextval,(select id from eg_appconfig where key_name='PENSION_FUND'), sysdate, 15);


#DOWN

delete from EG_APPCONFIG_VALUES where key_id=(select id from eg_appconfig where key_name='PENSION_FUND');