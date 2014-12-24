#UP
-- patch script for delete the employee entries from eg_action

delete from eg_roleaction_map where actionid in (select id from eg_action where module_id in (select id_module from eg_module where module_name like 'Employee'));

delete from eg_action where module_id in (select id_module from eg_module where module_name like 'Employee');

delete from  eg_module where module_name like 'Employee';
commit;


INSERT INTO FILTERSERVICES (id,name) VALUES (17,'Contractor-Improvement Works');
INSERT INTO FILTERSERVICES (id,name) VALUES (18,'Contractor-Deposit Works');
COMMIT;

#DOWN
