INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='UpdateSewerageApplicationDetails'));
