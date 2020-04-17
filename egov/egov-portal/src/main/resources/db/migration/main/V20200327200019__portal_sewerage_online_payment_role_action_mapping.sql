Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'), (select id from eg_action where name = 'SewerageCitizenOnlinePayment'));
