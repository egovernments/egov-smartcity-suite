delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and actionid = (select id from eg_action where name='DonationMasterDetailsScreen');
delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and actionid = (select id from eg_action where name='ChairPersonDetailsScreen');
delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and actionid = (select id from eg_action where name='WaterRatesDetailsMaster');

delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and actionid = (select id from eg_action where name='CategoryMaster');
delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and actionid = (select id from eg_action where name='DocumentNamesMaster');
delete from eg_roleaction where roleid = (select id from eg_role where name='Property Administrator') and actionid = (select id from eg_action where name='ApplicationProcessTimeMaster');