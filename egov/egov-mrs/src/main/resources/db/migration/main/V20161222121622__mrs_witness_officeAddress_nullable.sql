alter table egmrs_witness  alter column officeaddress DROP  NOT NULL;
update eg_module set enabled=false where name='Marriage Act';

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'Validate Serial No is Unique'));