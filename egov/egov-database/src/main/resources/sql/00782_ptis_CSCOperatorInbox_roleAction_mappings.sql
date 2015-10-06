
INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where url='/inbox'), (Select id from eg_role where name='CSC Operator'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where url='/inbox/draft'), (Select id from eg_role where name='CSC Operator'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where url='/inbox/history'), (Select id from eg_role where name='CSC Operator'));

