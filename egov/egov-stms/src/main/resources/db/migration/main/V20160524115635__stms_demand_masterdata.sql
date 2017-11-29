
----------- installment ----------------

INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type,financial_year) VALUES 
(nextval('SEQ_EG_INSTALLMENT_MASTER'), 042016, '2016-04-01 00:00:00', '2016-04-01 00:00:00', '2016-09-30 23:59:59',
 (select id from eg_module  where name = 'Sewerage Tax Management'and parentmodule is null), current_timestamp, '2016-2017-1', NULL,'2016-17');

INSERT INTO eg_installment_master (id, installment_num, installment_year, start_date, end_date, id_module, lastupdatedtimestamp, description, installment_type,financial_year) VALUES 
(nextval('SEQ_EG_INSTALLMENT_MASTER'), 102016, '2016-10-01 00:00:00', '2016-10-01 00:00:00', '2017-03-31 23:59:59',
 (select id from eg_module  where name = 'Sewerage Tax Management'and parentmodule is null), current_timestamp, '2016-2017-2', NULL,'2016-17');

---------- demand reason master --------

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Inspection Charge', 
(select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Sewerage Tax Management'), 'INSPECTIONCHARGE', 1, current_timestamp, current_timestamp,'t');

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Estimation Charge', 
(select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Sewerage Tax Management'), 'ESTIMATIONCHARGE', 2, current_timestamp, current_timestamp,'t');

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Sewerage Tax', 
(select id from eg_reason_category where code='TAX'), 'N', (select id from eg_module where name='Sewerage Tax Management'), 'SEWERAGETAX', 3, current_timestamp, current_timestamp,'t');


INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Donation Charge', 
(select id from eg_reason_category where code='Fee'), 'N', (select id from eg_module where name='Sewerage Tax Management'), 'DONATIONCHARGES', 4, current_timestamp, current_timestamp,'t');

------------ fee detail master ------ 

update egswtax_feesdetail_master set code='INSPECTIONCHARGE' where code='INSPECTIONCHARGES';
update egswtax_feesdetail_master set code='ESTIMATIONCHARGE' where code='ESTIMATIONCHARGES';

update egswtax_fees_master set code='INSPECTIONCHARGE' where code='INSPECTIONCHARGES';


------------ Demand Reason ---------

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Inspection Charge' and module=(select id from eg_module where name='Sewerage Tax Management')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Sewerage Tax Management'));

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Estimation Charge' and module=(select id from eg_module where name='Sewerage Tax Management')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Sewerage Tax Management'));

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Sewerage Tax' and module=(select id from eg_module where name='Sewerage Tax Management')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Sewerage Tax Management'));

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Donation Charge' and module=(select id from eg_module where name='Sewerage Tax Management')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Sewerage Tax Management'));



