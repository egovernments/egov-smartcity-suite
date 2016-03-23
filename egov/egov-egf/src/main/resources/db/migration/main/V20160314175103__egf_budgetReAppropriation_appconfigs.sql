Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budgetDetail.grid.component','Budget Grid Components',(select id from eg_module where name='EGF'));

Insert into eg_appconfig (ID,KEY_NAME,DESCRIPTION,MODULE) values (nextval('seq_eg_appconfig'),'budgetDetail.header.component','Budget Header Components',(select id from eg_module where name='EGF'));

Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetDetail.grid.component'),current_date,'function,fund');

Insert into eg_appconfig_values (ID,KEY_ID,EFFECTIVE_FROM,VALUE) values (nextval('seq_eg_appconfig_values'),(select id from eg_appconfig where KEY_NAME ='budgetDetail.header.component'),current_date,'executingDepartment');

UPDATE EG_APPCONFIG_VALUES set value =  'Approved' WHERE KEY_ID IN (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME = 'budget_final_approval_status');
