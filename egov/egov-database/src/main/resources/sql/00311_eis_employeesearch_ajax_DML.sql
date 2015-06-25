INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'EmpSearchAjax', '/employee/ajax/employees', 
null,(SELECT id FROM eg_module WHERE name='Employee'),  'EmpSearchAjax', 'false', 'eis');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='EmpSearchAjax' and contextroot='eis'),
(SELECT id FROM eg_role where name='Super User'));
