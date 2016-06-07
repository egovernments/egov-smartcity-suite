update eg_demand_reason set glcodeid = (select id from chartofaccounts where glcode='1402001') where id_demand_reason_master=(select id from eg_demand_reason_master where reasonmaster='Penalty' and module=(select id from eg_module where name='Trade License'));

update eg_demand_reason set glcodeid = (select id from chartofaccounts where glcode='1401101') where id_demand_reason_master=(select id from eg_demand_reason_master where reasonmaster='License Fee' and module=(select id from eg_module where name='Trade License'));
