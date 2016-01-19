alter table egtl_license add column startdate date;


--demand reason
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Motor Fee' and module=(select id from eg_module where name='Trade License')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') from eg_installment_master inst where inst.id_module=(select id_module from eg_module where name='Trade License'));


--reason details
Insert into eg_demand_reason_details (ID,ID_DEMAND_REASON,PERCENTAGE,FROM_DATE,TO_DATE,LOW_LIMIT,HIGH_LIMIT,CREATE_DATE,MODIFIED_DATE,FLAT_AMOUNT,IS_FLATAMNT_MAX) values (nextval('seq_eg_demand_reason_details'),(select id from eg_demand_reason where id_demand_reason_master = (select id from EG_DEMAND_REASON_MASTER where REASONMASTER = 'Motor Fee') and id_installment = (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2015 00:00:00','dd/MM/yyyy HH24:MI:SS'))),0.5,to_date('01/04/2004 00:00:00','dd/MM/yyyy HH24:MI:SS'),to_date('01/04/2015 23:59:59','dd/MM/yyyy HH24:MI:SS'),1,9999999999,current_timestamp, current_timestamp,0,0);


--demand reason
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Workforce Fee' and module=(select id from eg_module where name='Trade License')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where name='Licensing Fees-Trade License') from eg_installment_master inst where inst.id_module=(select id_module from eg_module where name='Trade License'));


--reason details
Insert into eg_demand_reason_details (ID,ID_DEMAND_REASON,PERCENTAGE,FROM_DATE,TO_DATE,LOW_LIMIT,HIGH_LIMIT,CREATE_DATE,MODIFIED_DATE,FLAT_AMOUNT,IS_FLATAMNT_MAX) values (nextval('seq_eg_demand_reason_details'),(select id from eg_demand_reason where id_demand_reason_master = (select id from EG_DEMAND_REASON_MASTER where REASONMASTER = 'Workforce Fee') and id_installment = (select id from EG_INSTALLMENT_MASTER where ID_MODULE = (select id from EG_MODULE where name = 'Trade License') and start_date = to_date('01/04/2015 00:00:00','dd/MM/yyyy HH24:MI:SS'))),0.5,to_date('01/04/2004 00:00:00','dd/MM/yyyy HH24:MI:SS'),to_date('01/04/2015 23:59:59','dd/MM/yyyy HH24:MI:SS'),1,9999999999,current_timestamp, current_timestamp,0,0);

