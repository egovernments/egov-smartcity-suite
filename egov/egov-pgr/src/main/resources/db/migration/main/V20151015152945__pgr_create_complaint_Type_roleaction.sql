INSERT INTO eg_roleaction (roleid, actionid) VALUES ((select id from eg_role where name='Super User'),(select id from eg_action where url = '/complainttype/create'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ((select id from eg_role where name='Grivance Administrator'),(select id from eg_action where url = '/complainttype/create'));
