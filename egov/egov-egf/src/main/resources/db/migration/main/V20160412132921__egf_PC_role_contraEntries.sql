


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),
(select id from eg_action where name='Modify Contra Entries-Search'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),
(select id from eg_action where name='Bank to Bank Transfer'));
