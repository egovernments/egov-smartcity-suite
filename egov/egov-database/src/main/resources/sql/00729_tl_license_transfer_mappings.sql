INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'View Trade License Duplicate Certificate'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'View Trade License Generate Certificate'), (select id from eg_role where name = 'TLCreator'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Transfer Trade License New Form'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'transferTradeLicense-create'), (select id from eg_role where name = 'TLCreator'));