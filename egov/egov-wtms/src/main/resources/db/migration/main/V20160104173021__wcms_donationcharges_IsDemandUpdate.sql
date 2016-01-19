update eg_demand_reason_master set isdemand=false where code='WTAXDONATION' and module in(select id from eg_module where name='Water Tax Management');
