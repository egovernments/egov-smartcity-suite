update eg_action set enabled=true where name='OpeningBalance-Search';

insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Financial Report Viewer'), (select id from eg_action where name = 'OpeningBalance-Search'));