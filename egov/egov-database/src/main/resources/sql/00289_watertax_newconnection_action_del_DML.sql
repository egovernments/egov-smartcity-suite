delete from EG_ROLEACTION where actionid in (select id FROM eg_action  WHERE name='WaterTaxNewConnectionSuccess' and contextroot='wtms');

delete from eg_action where name='WaterTaxNewConnectionSuccess' and contextroot='wtms';
