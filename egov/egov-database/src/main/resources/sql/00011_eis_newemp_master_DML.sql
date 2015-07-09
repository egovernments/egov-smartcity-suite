INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'EmpMaster', NULL,NULL, (select current_date), '/employeeMaster/employeeMaster-newForm.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), 4, 'New Emp Create', 1, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('EmpMaster'));
 
INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'getAllDesigAjax', NULL,NULL, (select current_date), '/common/employeeSearch-getAllDesignations.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'getAllDesigAjax', 0, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('getAllDesigAjax'));
 
INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'getAllUnmappedUsers', NULL,NULL, (select current_date), '/common/employeeSearch-getAllUnmappedUsers.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'getAllUnmappedUsers', 0, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('getAllUnmappedUsers'));
 
INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'getPositionsForAssignment', NULL,NULL, (select current_date), '/common/employeeSearch-getPositionsForDeptDesig.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'getPositionsForAssignment', 0, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('getPositionsForAssignment')); 
 
INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'empMasterSave', NULL,NULL, (select current_date), '/common/employeeSearch-save.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'empMasterSave', 0, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('empMasterSave'));
 
INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'empMasterModify', NULL,NULL, (select current_date), '/common/employeeSearch-loadEmployee.action', 'mode=Modify&empId=', NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'empMasterModify', 0, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('empMasterModify'));
 
INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'empMasterView', NULL,NULL, (select current_date), '/common/employeeSearch-loadEmployee.action', 'mode=View&empId=', NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'empMasterView', 0, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('empMasterView')); 
 
INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'empCodeUniqueCheck', NULL,NULL, (select current_date), '/employeeMaster/employeeMaster-checkEmpCodeForUniqueness.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'empCodeUniqueCheck', 0, NULL, 'eis');

INSERT INTO eg_action (ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
VALUES ((select nextval('seq_eg_action')), 'panNoUniqueCheck', NULL,NULL, (select current_date), '/employeeMaster/employeeMaster-checkPanNoForUniqueness.action', NULL, NULL,
(SELECT id_module FROM EG_MODULE WHERE module_name LIKE 'Employee'), NULL, 'panNoUniqueCheck', 0, NULL, 'eis');

INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('empCodeUniqueCheck'));
 
INSERT INTO eg_roleaction_map(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER') AS roleid,id FROM eg_action
 WHERE name IN('panNoUniqueCheck'));  
 

--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('panNoUniqueCheck')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER');
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('empCodeUniqueCheck')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER');
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('empMasterView')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER'); 
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('empMasterModify')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER'); 
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('empMasterSave')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER'); 
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('getPositionsForAssignment')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER'); 
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('getAllUnmappedUsers')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER');  
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('getAllDesigAjax')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER'); 
 
--rollback DELETE FROM eg_roleaction_map WHERE ACTIONID IN(SELECT id FROM eg_action WHERE name IN('EmpMaster')) AND 
--rollback  ROLEID IN(SELECT id_role FROM eg_roles WHERE UPPER(role_name) LIKE 'SUPER USER');  
 
--rollback DELETE FROM eg_action WHERE name IN('panNoUniqueCheck'); 
--rollback DELETE FROM eg_action WHERE name IN('empCodeUniqueCheck');
--rollback DELETE FROM eg_action WHERE name IN('empMasterView');
--rollback DELETE FROM eg_action WHERE name IN('empMasterModify');
--rollback DELETE FROM eg_action WHERE name IN('empMasterSave');
--rollback DELETE FROM eg_action WHERE name IN('getPositionsForAssignment');
--rollback DELETE FROM eg_action WHERE name IN('getAllUnmappedUsers');
--rollback DELETE FROM eg_action WHERE name IN('getAllDesigAjax');
--rollback DELETE FROM eg_action WHERE name IN('EmpMaster');

