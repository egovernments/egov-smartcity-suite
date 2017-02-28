delete from EG_ROLEACTION where actionid=(SELECT id FROM eg_action WHERE name = 'Validate Serial No is Unique') and roleid=(SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver');

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'Validate Serial No is Unique'));
