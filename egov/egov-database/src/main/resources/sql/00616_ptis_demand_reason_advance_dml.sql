INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date, isdemand)
    VALUES (nextval('seq_eg_demand_reason_master'), 'Advance', (select id from eg_reason_category where code='ADVANCE'),'N',
    (select id from eg_module where name = 'Property Tax'),'ADVANCE', 1, now(), now(), true);

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values
 (nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='Advance'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Property Tax') and INSTALLMENT_YEAR=to_date('01-04-15','DD-MM-YY')),null,null,now(),now(), null);
 
--rollback delete from eg_demand_reason  where id_demand_reason_master = (select id from eg_demand_reason_master where reasonmaster='Advance') and id_installment = (select id from eg_installment_master where id_module in (select id from eg_module where name ='Property Tax') and INSTALLMENT_YEAR=to_date('01-04-15','DD-MM-YY'));
--rollback delete from eg_demand_reason_master where reasonmaster = 'Advance' and module = (select id from eg_module where name = 'Property Tax');