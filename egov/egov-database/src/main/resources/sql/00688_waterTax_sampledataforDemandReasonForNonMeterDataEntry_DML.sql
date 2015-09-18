
 insert into eg_demand_reason(id,id_demand_reason_master,id_installment,
percentage_basis,id_base_reason,create_date,modified_date,glcodeid) 
values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where
 reasonmaster='Water tax charges'),(select id from eg_installment_master where id_module in 
 (select id from eg_module where name ='Property Tax') 
 and INSTALLMENT_YEAR=to_date('01-10-14','DD-MM-YY') ),null,null,now(),now(),
 (select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,
percentage_basis,id_base_reason,create_date,modified_date,glcodeid) 
values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where
 reasonmaster='Water tax charges'),(select id from eg_installment_master where id_module in 
 (select id from eg_module where name ='Property Tax') 
 and INSTALLMENT_YEAR=to_date('01-04-14','DD-MM-YY') ),null,null,now(),now(),
 (select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));
 
 insert into eg_demand_reason(id,id_demand_reason_master,id_installment,
percentage_basis,id_base_reason,create_date,modified_date,glcodeid) 
values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where
 reasonmaster='Water tax charges'),(select id from eg_installment_master where id_module in 
 (select id from eg_module where name ='Property Tax') 
 and INSTALLMENT_YEAR=to_date('01-10-13','DD-MM-YY') ),null,null,now(),now(),
 (select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));
 
 
  insert into eg_demand_reason(id,id_demand_reason_master,id_installment,
percentage_basis,id_base_reason,create_date,modified_date,glcodeid) 
values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where
 reasonmaster='Water tax charges'),(select id from eg_installment_master where id_module in 
 (select id from eg_module where name ='Property Tax') 
 and INSTALLMENT_YEAR=to_date('01-04-13','DD-MM-YY') ),null,null,now(),now(),
 (select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));