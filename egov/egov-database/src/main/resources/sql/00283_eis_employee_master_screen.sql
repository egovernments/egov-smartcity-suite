ALTER TABLE egeis_employee ADD COLUMN dateOfBirth DATE;

ALTER TABLE egeis_employee_hod DROP COLUMN department;

INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'EmpDesigAutoComplete', '/employee/ajax/designations', 
null,(SELECT id FROM eg_module WHERE name='Employee'),  'EmpDesigAutoComplete', 'false', 'eis');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='EmpDesigAutoComplete' and contextroot='eis'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'EmpPosAutoComplete', '/employee/ajax/positions', 
null,(SELECT id FROM eg_module WHERE name='Employee'),  'EmpPosAutoComplete', 'false', 'eis');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='EmpPosAutoComplete' and contextroot='eis'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'Search Employee', '/employee/search', 
null,(SELECT id FROM eg_module WHERE name='Employee'), 1, 'Update/View', 'true', 'eis');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Search Employee' and contextroot='eis'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'Update Employee', '/employee/update', 
null,(SELECT id FROM eg_module WHERE name='Employee'),  'Update', 'false', 'eis');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='Update Employee' and contextroot='eis'),
(SELECT id FROM eg_role where name='Super User'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule,  displayname, 
enabled, contextroot) VALUES (nextval('seq_eg_action'), 'View Employee', '/employee/view', 
null,(SELECT id FROM eg_module WHERE name='Employee'),  'View', 'false', 'eis');

INSERT INTO eg_roleaction (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='View Employee' and contextroot='eis'),
(SELECT id FROM eg_role where name='Super User'));

insert into functionary (id,code,name,createtimestamp,updatetimestamp,isactive)
values(nextval('SEQ_FUNCTIONARY'),1,'Sample',current_date-1,current_date-1,1);

