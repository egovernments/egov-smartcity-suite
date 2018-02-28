
Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),2017,to_date('01-04-17','DD-MM-YY'),to_date('01-04-17','DD-MM-YY'),to_date('31-03-18','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'2017-18','Yearly');

