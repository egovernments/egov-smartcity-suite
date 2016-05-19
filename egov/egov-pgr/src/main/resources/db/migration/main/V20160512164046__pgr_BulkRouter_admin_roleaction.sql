INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Grivance Administrator'), id from eg_action where name = 'Search Bulk Router Generation';

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Grivance Administrator'), id from eg_action where name = 'Save Bulk Router';
