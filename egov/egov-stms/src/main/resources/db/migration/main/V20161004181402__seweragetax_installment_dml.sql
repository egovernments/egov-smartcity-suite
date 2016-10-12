INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type,financial_year) VALUES 
(nextval('SEQ_EG_INSTALLMENT_MASTER'), 042017, '2017-04-01 00:00:00','2017-04-01 00:00:00','2017-09-30 23:59:59',
 (select id from eg_module  where name = 'Sewerage Tax Management'and parentmodule is null), current_timestamp, '2017-2018-1', NULL,'2017-18');

INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type,financial_year) VALUES 
(nextval('SEQ_EG_INSTALLMENT_MASTER'), 102017, '2017-10-01 00:00:00','2017-10-01 00:00:00','2018-03-31 23:59:59',
 (select id from eg_module  where name = 'Sewerage Tax Management'and parentmodule is null), current_timestamp, '2017-2018-2', NULL,'2017-18');

 Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='SEWERAGEADVANCE' and module=(select id from eg_module where name='Sewerage Tax Management')),
 inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst  where inst.id_module=(select id from eg_module where name='Sewerage Tax Management') and inst.financial_year='2017-18'); 
  
 UPDATE eg_installment_master set end_date = '2016-03-31 23:59:59' where installment_num = 102015 and id_module=(select id from eg_module  where name = 'Sewerage Tax Management'and parentmodule is null) and financial_year='2015-16' and start_date='2015-10-01 00:00:00' and end_date='2016-04-10 00:00:00';