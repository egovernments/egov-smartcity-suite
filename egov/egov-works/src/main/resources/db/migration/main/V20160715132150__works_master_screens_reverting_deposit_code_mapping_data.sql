---------------Reverting Mapping Deposit Code Master actions----------------------
delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksSaveDepositCodeMaster' and contextroot = 'egworks');
delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Search Deposit Code' and contextroot = 'egworks');
delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='Generate Deposit Code' and contextroot = 'egworks');

--rollback Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Generate Deposit Code' and contextroot = 'egworks'));
--rollback Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='Search Deposit Code' and contextroot = 'egworks'));
--rollback Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksSaveDepositCodeMaster' and contextroot = 'egworks'));
