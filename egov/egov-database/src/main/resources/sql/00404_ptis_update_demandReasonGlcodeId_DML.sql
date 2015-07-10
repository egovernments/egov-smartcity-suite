-- updates to set arrears glcodeid

update eg_demand_reason set glcodeid = (select id from chartofaccounts where glcode = '4311004')
where id_installment <> (select id from eg_installment_master where id_module = (select id from eg_module where name='Property Tax') and now() between start_date and end_date);

-- updates to set current glcodeid
update eg_demand_reason set glcodeid = (select id from chartofaccounts where glcode = '1100101')
where id_demand_reason_master = (select id from eg_demand_reason_master where code = 'GEN_TAX' and module = (select id from eg_module where name='Property Tax'))
and id_installment = (select id from eg_installment_master where id_module = (select id from eg_module where name='Property Tax') and now() between start_date and end_date);

update eg_demand_reason set glcodeid = (select id from chartofaccounts where glcode = '3503001')
where id_demand_reason_master = (select id from eg_demand_reason_master where code = 'LIB_CESS' and module=(select id from eg_module where name='Property Tax'))
and id_installment = (select id from eg_installment_master where id_module = (select id from eg_module where name='Property Tax') and now() between start_date and end_date);

update eg_demand_reason set glcodeid = (select id from chartofaccounts where glcode = '3503002')
where id_demand_reason_master = (select id from eg_demand_reason_master where code = 'EDU_CESS' and module=(select id from eg_module where name='Property Tax'))
and id_installment = (select id from eg_installment_master where id_module = (select id from eg_module where name='Property Tax') and now() between start_date and end_date);

update eg_demand_reason set glcodeid = (select id from chartofaccounts where glcode = '1402001')
where id_demand_reason_master = (select id from eg_demand_reason_master where code = 'UNAUTH_PENALTY' and module=(select id from eg_module where name='Property Tax'))
and id_installment = (select id from eg_installment_master where id_module = (select id from eg_module where name='Property Tax') and now() between start_date and end_date);
