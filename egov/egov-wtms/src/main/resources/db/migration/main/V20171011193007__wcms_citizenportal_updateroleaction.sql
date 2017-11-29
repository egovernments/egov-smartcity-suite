insert into eg_roleaction (actionid,roleid) values((select id from eg_action where name ='CommonWaterTaxSearchScreen'),(select id from eg_role where name='CITIZEN'));
