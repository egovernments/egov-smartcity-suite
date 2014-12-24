#UP

INSERT INTO EGPT_PROPERTY_TYPE_MASTER
   (ID_PROPERTYTYPEMASTER, PROPERTY_TYPE, MODIFIED_DATE, TYPE_FACTOR, PROPERTY_TYPE_LOCAL,CODE)
 Values
   (SEQ_PROP_TYPE_MASTER.nextval, 'Cinema Hall',sysdate, 1, NULL,'CINEMA_HALL');

insert into EG_APPCONFIG 
  (ID, KEY_NAME, DESCRIPTION, MODULE) 
values 
  (SEQ_EG_APPCONFIG.nextval,'Cinema Hall','Cinema Hall','Property Tax');
  

insert into EG_APPCONFIG_VALUES 
  (ID, KEY_ID, EFFECTIVE_FROM, VALUE) 
values 
(SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'Cinema Hall'),to_date('01/04/2009','dd/MM/yyyy'),'Category A,Category B');



insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'CATEGORY A','Cinema Hall Category A','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'CATEGORY A'),to_date('01/04/2009','dd/MM/yyyy'),'0.72544');

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'CATEGORY B','Cinema Hall Category B','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'CATEGORY B'),to_date('01/04/2009','dd/MM/yyyy'),'0.63000');

#DOWN
