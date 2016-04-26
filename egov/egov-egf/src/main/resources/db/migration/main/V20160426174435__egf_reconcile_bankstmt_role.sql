

INSERT INTO eg_roleaction (roleid, actionid) 
VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='brs pending balance'));




