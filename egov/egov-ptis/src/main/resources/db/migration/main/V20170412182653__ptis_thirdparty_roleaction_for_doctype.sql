insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Default User For Doctype'),
id from eg_role where name in ('CSC Operator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Default User For Doctype'),
id from eg_role where name in ('MeeSeva Operator');
