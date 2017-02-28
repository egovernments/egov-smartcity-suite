update eg_action set url ='/registration/modify-approved' where name='Update Marriage Registration Record';

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'calculateMarriageFee'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Admin'), (SELECT id FROM eg_action WHERE name = 'show-mrregistrationunitzone'));