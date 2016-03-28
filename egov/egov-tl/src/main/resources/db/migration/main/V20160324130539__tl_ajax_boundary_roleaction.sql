insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name ='Load Block By Locality'), (select id from eg_role where name='TLCreator'));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name ='Load Block By Locality'), (select id from eg_role where name='TLApprover'));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name ='Load Block By Locality'), (select id from eg_role where name='TLAdmin'));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name ='Load Block By Locality'), (select id from eg_role where name='Collection Operator'));

