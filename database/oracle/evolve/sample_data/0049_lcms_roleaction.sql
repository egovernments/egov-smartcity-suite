#UP

insert into EG_ROLEACTION_MAP values((select ID_ROLE from EG_ROLES where ROLE_NAME ='LCO'),(SELECT a.id  FROM eg_action a WHERE a.url IN ('/reports/timeSeriesReport!edit.action')));          

#DOWN
