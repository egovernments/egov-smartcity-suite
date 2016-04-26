

INSERT INTO eg_roleaction (roleid, actionid) 
select (select id from eg_role where name='Financial Report Viewer'),id from eg_action where name in('Budget Additional Appropriation',
'Generate budget Approprition Report','Generate budget Approprition Pdf','Generate budget Approprition Xls');
