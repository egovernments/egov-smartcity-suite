insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Transfer Property View'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Transfer Property Approve'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Transfer Property Forward'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Transfer Property Reject'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Transfer Property Form'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='CSC Operator'), (select id from eg_action where name='Transfer Property Save'));
