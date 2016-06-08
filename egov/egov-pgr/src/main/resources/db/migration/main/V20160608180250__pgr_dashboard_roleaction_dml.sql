INSERT INTO EG_ROLEACTION (roleid,actionid) values ((select id from eg_role where name='Employee Admin'), (select id from eg_action where name='erpdashboard' and contextroot='dashboard'));

