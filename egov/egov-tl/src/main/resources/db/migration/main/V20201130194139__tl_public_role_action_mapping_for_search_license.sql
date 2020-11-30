INSERT INTO eg_roleaction (actionid,roleid) values ((select id from eg_action where url = '/search/license'),(select id from eg_role where name = 'PUBLIC'));
