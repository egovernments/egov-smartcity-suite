--Installment / Demand related master data
Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,
DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042017,to_date('01-04-17','DD-MM-YY'),
to_date('01-04-17','DD-MM-YY'),to_date('31-03-18','DD-MM-YY'),
(select id from eg_module where name = 'Trade License' and parentmodule is null),
current_timestamp,'TL/17-18','Yearly');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License')), (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2017 00:00:00','dd/MM/yyyy HH24:MI:SS')), null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License'));


Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Penalty' and module=(select id from eg_module where name='Trade License')), (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2017 00:00:00','dd/MM/yyyy HH24:MI:SS')), null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Penalties and Fines-Penalty for Un-authorised Construction'));

