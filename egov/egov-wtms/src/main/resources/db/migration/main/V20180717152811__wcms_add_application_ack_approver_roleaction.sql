

delete from eg_roleaction where roleid = (select id from eg_role where name='Water Tax Approver') and 
actionid = (select id from eg_action where name='watertaxAcknowledgement' and contextroot='wtms');

INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='watertaxAcknowledgement' and contextroot='wtms'));

