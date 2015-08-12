
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'OnlineTransactionReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'OnlineTransactionReportResult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'SearchOnlineReceipts'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'SearchOnlineReceiptsSearch'));


