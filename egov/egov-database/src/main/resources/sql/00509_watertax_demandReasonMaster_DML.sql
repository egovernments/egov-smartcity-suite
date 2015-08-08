update eg_demand_reason_master set isdemand=true where category=(select id from eg_reason_category where name='TAX');
update eg_demand_reason_master set isdemand=true where category=(select id from eg_reason_category where name='FINES');
--estimation charges demand reason
INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date,isdemand)
    VALUES (nextval('seq_eg_demand_reason_master'), 'WT_ESTIMATE_CHARGES', (select ID from eg_reason_category 
    where code='TAX'),'N',(select id from eg_module where name = 'Water Tax Management'),'WTAXFIELDINSPEC', 1, now(), now(),false);

INSERT INTO eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,
create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master 
where reasonmaster='WT_ESTIMATE_CHARGES'),(select id from eg_installment_master where id_module in
 (select id from eg_module where name ='Water Tax Management')),null,null,now(),now(),
 (select ID from CHARTOFACCOUNTS where GLCODE = '1407011'));


--updating isdemand for watertax demand reasons
update eg_demand_reason_master set isdemand=false where reasonmaster='WT_CONNECTION_CHARGES';
update eg_demand_reason_master set isdemand=false where reasonmaster='WT_SECURITY_DEPOSIT';
update eg_demand_reason_master set isdemand=false where reasonmaster='WT_DONATION_CHARGES';



