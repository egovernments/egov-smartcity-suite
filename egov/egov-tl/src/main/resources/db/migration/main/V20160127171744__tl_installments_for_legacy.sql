
Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),201004,to_date('01-04-10','DD-MM-YY'),to_date('01-04-10','DD-MM-YY'),to_date('31-03-11','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/10-11','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),201104,to_date('01-04-11','DD-MM-YY'),to_date('01-04-11','DD-MM-YY'),to_date('31-03-12','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/11-12','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),201204,to_date('01-04-12','DD-MM-YY'),to_date('01-04-12','DD-MM-YY'),to_date('31-03-13','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/12-13','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),201304,to_date('01-04-13','DD-MM-YY'),to_date('01-04-13','DD-MM-YY'),to_date('31-03-14','DD-MM-YY'),(select id from eg_module where name = 'Trade License' and parentmodule is null),current_timestamp,'TL_I/13-14','Yearly');

update eg_installment_master set INSTALLMENT_NUM=201404 where start_date = to_date('01/04/2014 00:00:00','dd/MM/yyyy HH24:MI:SS') and ID_MODULE=(select id from eg_module where name = 'Trade License' and parentmodule is null);

update eg_installment_master set INSTALLMENT_NUM=201504 where start_date = to_date('01/04/2015 00:00:00','dd/MM/yyyy HH24:MI:SS') and ID_MODULE=(select id from eg_module where name = 'Trade License' and parentmodule is null);

update eg_installment_master set INSTALLMENT_NUM=201604,DESCRIPTION='TL_I/16-17' where start_date = to_date('01/04/2016 00:00:00','dd/MM/yyyy HH24:MI:SS') and ID_MODULE=(select id from eg_module where name = 'Trade License' and parentmodule is null);

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')), (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2013 00:00:00','dd/MM/yyyy HH24:MI:SS')), null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') );

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')), (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2012 00:00:00','dd/MM/yyyy HH24:MI:SS')), null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') );

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')), (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2011 00:00:00','dd/MM/yyyy HH24:MI:SS')), null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') );

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')), (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2010 00:00:00','dd/MM/yyyy HH24:MI:SS')), null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') );