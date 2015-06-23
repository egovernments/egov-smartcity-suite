delete from EG_ROLEACTION where ROLEID in(select id from eg_role where name in ('Super User','Data Entry Operator')) and actionid in (select id FROM eg_action  WHERE name='WaterTaxNewConnectionSuccess' and contextroot='wtms');

delete from eg_action where name='WaterTaxNewConnectionSuccess' and contextroot='wtms';
