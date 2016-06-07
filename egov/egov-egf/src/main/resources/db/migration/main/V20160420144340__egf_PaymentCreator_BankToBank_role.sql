

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),
(select id from eg_action where name='ContraBTBCreate'));
