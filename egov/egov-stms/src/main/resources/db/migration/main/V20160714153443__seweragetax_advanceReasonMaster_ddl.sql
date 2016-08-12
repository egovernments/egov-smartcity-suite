INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'SEWERAGEADVANCE',
(select id from eg_reason_category where code='ADVANCE'), 'N', (select id from eg_module where name='Sewerage Tax Management'), 'SEWERAGEADVANCE', 4, current_timestamp, current_timestamp,'t');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID)
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='SEWERAGEADVANCE' and module=(select id from eg_module where name='Sewerage Tax Management')), inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where glcode='3504199') from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Sewerage Tax Management'));
