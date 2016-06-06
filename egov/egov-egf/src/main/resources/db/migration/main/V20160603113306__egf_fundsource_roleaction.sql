

INSERT INTO eg_roleaction (roleid, actionid)  select (select id from eg_role where name='Financial Administrator'), id from eg_action where name in('Create Fundsource','Create-Fundsource','Update-Fundsource','View-Fundsource','Edit-Fundsource','Result-Fundsource',
'View Fundsource','Edit Fundsource','Search and View Result-Fundsource','Search and Edit Result-Fundsource');

INSERT INTO eg_roleaction (roleid, actionid)  select (select id from eg_role where name='Financial Administrator'), id from eg_action where name in('TransferClosingBalance','TransferClosingBalanceTransfer');

INSERT INTO eg_roleaction (roleid, actionid)  values ((select id from eg_role where name='Bill Creator'),(select id from eg_action where name='Cancel Bill Action'));

