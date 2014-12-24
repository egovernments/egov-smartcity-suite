#UP
update EG_APPCONFIG_VALUES set  value='executingDepartment' where value='executingDepartment,fund' and KEY_ID=(select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.header.component');

update EG_APPCONFIG_VALUES set value='function,fund' where  VALUE='function' and KEY_ID=(select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.grid.component');

#DOWN
update EG_APPCONFIG_VALUES set  value='executingDepartment,fund' where value='executingDepartment' and KEY_ID=(select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.header.component');

update EG_APPCONFIG_VALUES set value='function' where  VALUE='function,fund' and KEY_ID=(select id from EG_APPCONFIG where KEY_NAME = 'budgetDetail.grid.component');

