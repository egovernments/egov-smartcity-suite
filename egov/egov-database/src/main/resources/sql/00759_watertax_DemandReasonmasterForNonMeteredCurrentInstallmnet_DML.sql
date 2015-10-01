insert into eg_demand_reason(id,id_demand_reason_master,id_installment,
percentage_basis,id_base_reason,create_date,modified_date,glcodeid) 
values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where
 code='WTAXCHARGES'),(select id from eg_installment_master where id_module in 
 (select id from eg_module where name ='Property Tax') 
 and INSTALLMENT_YEAR=to_date('01-10-15','DD-MM-YY') ),null,null,now(),now(),
 (select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));