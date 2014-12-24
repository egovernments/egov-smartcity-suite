#UP

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.header.component'),sysdate,'executingDepartment,fund');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.grid.component'),sysdate,'function');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail_mandatory_fields'),sysdate,'executingDepartment,function,fund');

#DOWN
delete from EG_APPCONFIG_VALUES where key_id = (select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.grid.component');
delete from EG_APPCONFIG_VALUES where key_id = (select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.header.component');
delete from EG_APPCONFIG_VALUES where key_id = (select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail_mandatory_fields');
