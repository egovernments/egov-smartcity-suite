INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'generateMarriageCertificate'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'show-mrregistrationunitzone'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'generateReIssueCertificate'));