Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-14','DD-MM-YY'),to_date('01-04-14','DD-MM-YY'),to_date('31-03-15','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/14-15','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-15','DD-MM-YY'),to_date('01-04-15','DD-MM-YY'),to_date('31-03-16','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/15-16','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-16','DD-MM-YY'),to_date('01-04-16','DD-MM-YY'),to_date('31-03-17','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/17-17','Yearly');


--demand reason master
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'License Fee', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Trade License'), 'License_Fee', 3, current_timestamp, current_timestamp,'t');
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Motor Fee', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Trade License'), 'Motor_Fee', 4, current_timestamp, current_timestamp,'t');
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Workforce Fee', (select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Trade License'), 'Workforce_Fee', 4, current_timestamp, current_timestamp,'t');


