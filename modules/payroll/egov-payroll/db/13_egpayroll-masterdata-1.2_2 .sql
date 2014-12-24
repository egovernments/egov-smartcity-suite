#UP
/**********************************  Menu tree for Bulk Update Master Rule **********************/


INSERT INTO EG_MODULE
  (ID_MODULE, MODULE_NAME, LASTUPDATEDTIMESTAMP, ISENABLED, MODULE_NAMELOCAL, PARENTID, MODULE_DESC)
VALUES
 (SEQ_MODULEMASTER.NEXTVAL, 'Bulk Updation Master', SYSDATE, 1, 'Bulk Updation Master', (SELECT ID_MODULE FROM EG_MODULE WHERE module_name LIKE 'Payroll'), 'Bulk Updation Master');

INSERT INTO EG_ACTION
  (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 VALUES
  (SEQ_EG_ACTION.NEXTVAL, 'Bulk Updation Master', SYSDATE, '/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType=beforeLoad', '', 1, (SELECT ID_MODULE FROM EG_MODULE WHERE module_name LIKE 'Bulk Updation Master'), 1, 'Create', 1);

INSERT INTO EG_ROLEACTION_MAP
(ROLEID, ACTIONID)
 VALUES
((SELECT e.id_ROLE FROM EG_ROLES e WHERE e.ROLE_NAME='SUPER USER'), (SELECT ID FROM EG_ACTION WHERE name LIKE 'Bulk Updation Master'));

#DOWN