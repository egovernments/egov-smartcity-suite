INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'PropTax Rev Petition Outcome'), 
(Select id from eg_role where name='Property Approver'));

