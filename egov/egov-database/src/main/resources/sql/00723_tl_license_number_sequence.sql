create SEQUENCE egtl_cnc_license_number;

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'New Trade License Before Renew'), (select id from eg_role where name = 'TLCreator'));