insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'), (select id from eg_action where name = 'cbill-print-crud'));

