delete from eg_roleaction where actionid = (select id from eg_action where name='CommonWaterTaxSearchScreen' and contextroot='wtms') and roleid = (select id from eg_role where name='Property Administrator');

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'Property Administrator'),(select id FROM eg_action  WHERE NAME = 'CommonWaterTaxSearchScreen' and CONTEXTROOT='wtms'));

