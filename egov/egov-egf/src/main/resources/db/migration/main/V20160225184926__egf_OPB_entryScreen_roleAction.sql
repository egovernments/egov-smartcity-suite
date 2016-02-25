

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='Opening Balance Entry'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='Major Head Ajax'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='Minor Head Ajax'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='Ajax Accounts'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='Ajax Transaction Submit'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='Ajax Delete'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='Ajax get TransactionSummary'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='SearchTransactionSummariesForNonSubledger'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='SearchTransactionSummariesForSubledger'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='SearchTransactionSummariesForNonSubledger'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='ajax-common-detailtype'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='ajax-common-detailcode'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),
(select id from eg_action where name='loadAllAssetCodes'));

