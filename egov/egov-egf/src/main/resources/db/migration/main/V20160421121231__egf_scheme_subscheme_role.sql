

INSERT INTO eg_roleaction (roleid, actionid) VALUES
 ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Scheme Code unique check'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES
 ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Edit Scheme Code'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES
 ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='View Scheme Code'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES
 ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='exil-SubScheme View'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES
 ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='AjaxMiscReceiptScheme'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES
 ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='AjaxMiscReceiptSubScheme'));


