INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'DishonoredCheque'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'DishonoredChequeAccountNo'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'DishonoredChequeList'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'Voucher View'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'ViewReceipts'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'DishonoredChequeProcess'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'DishonoredChequeSubmit'));
