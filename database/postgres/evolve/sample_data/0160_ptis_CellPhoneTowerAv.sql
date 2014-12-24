#UP

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'CELL_PHONE_TOWER_AV','Cell Phone Tower Annual Value','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'CELL_PHONE_TOWER_AV'),to_date('01/04/2009','dd/MM/yyyy'),'120970');

#DOWN
delete from EG_APPCONFIG_VALUES where key_id = (select id from EG_APPCONFIG where KEY_NAME = 'CELL_PHONE_TOWER_AV');
delete from EG_APPCONFIG where KEY_NAME = 'CELL_PHONE_TOWER_AV' and module = 'Property Tax';
