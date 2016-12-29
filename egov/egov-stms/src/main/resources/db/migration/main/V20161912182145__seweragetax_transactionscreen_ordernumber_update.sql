update eg_action set ordernumber = 2 where displayname = 'Search Connection'  and parentmodule = (select id from eg_module where name='SewerageTransactions');
update eg_action set ordernumber = 3 where displayname = 'Collect Sewerage Charges'  and parentmodule = (select id from eg_module where name='SewerageTransactions');
update eg_action set ordernumber = 4 where displayname = 'Search Notice'  and parentmodule = (select id from eg_module where name='SewerageReports');

