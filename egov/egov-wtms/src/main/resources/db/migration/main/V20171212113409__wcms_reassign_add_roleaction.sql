insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name='ReassignWaterChargesApplication' and contextroot='wtms'),(select id from eg_role where name='Property Administrator'));

