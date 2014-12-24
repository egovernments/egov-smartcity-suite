#UP

/*************************************** Create **************************/
UPDATE EG_ACTION SET url='/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType=beforeLoad\&mode=create' WHERE name='Bulk Updation Master';


/************ ***************************View ****************************/
INSERT INTO EG_ACTION
  (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 VALUES
  (SEQ_EG_ACTION.NEXTVAL, 'BulkUpdationView', SYSDATE, '/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType=searchBeforeLoad\&mode=view', '', 1, (SELECT ID_MODULE FROM EG_MODULE WHERE module_name LIKE 'Bulk Updation Master'), 2, 'View', 1);

INSERT INTO EG_ROLEACTION_MAP
(ROLEID, ACTIONID)
 VALUES
((SELECT e.id_ROLE FROM EG_ROLES e WHERE e.ROLE_NAME='SUPER USER'), (SELECT ID FROM EG_ACTION WHERE name LIKE 'BulkUpdationView'));

/*****************************************Modify *****************************/

INSERT INTO EG_ACTION
  (ID, NAME, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED)
 VALUES
  (SEQ_EG_ACTION.NEXTVAL, 'BulkUpdationModify', SYSDATE, '/bulkRuleUpdation/BulkUpdateMasterAction.do?submitType=searchBeforeLoad\&mode=modify', '', 1, (SELECT ID_MODULE FROM EG_MODULE WHERE module_name LIKE 'Bulk Updation Master'),3, 'Modify', 1);

INSERT INTO EG_ROLEACTION_MAP
(ROLEID, ACTIONID)
 VALUES
((SELECT e.id_ROLE FROM EG_ROLES e WHERE e.ROLE_NAME='SUPER USER'), (SELECT ID FROM EG_ACTION WHERE name LIKE 'BulkUpdationModify'));


#DOWN