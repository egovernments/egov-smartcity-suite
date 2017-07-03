
delete from eg_roleaction where actionid = (select id from eg_action where name='watertaxestimationnotice') and roleid=(select id from eg_role where name='Water Tax Approver');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),(select id from eg_action where name='watertaxestimationnotice'));

delete from eg_roleaction where actionid = (select id from eg_action where name='workorderreportview') and roleid=(select id from eg_role where name='Water Tax Approver');

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),(select id from eg_action where name='workorderreportview'));







