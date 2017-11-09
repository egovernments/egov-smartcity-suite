update egp_portalservice set name='Renewal of License' where code='Renewal of License';
update egp_portalservice set name='Closure of License' where code='Closure of License';
update egp_portalservice set name='License Fee Payment' where code='License Fee Payment';

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='View Trade License Generate Certificate'));
INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='Generate Provisional Certificate'));

