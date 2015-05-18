
INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'AjaxLoadBoundaryTypes', NULL, NULL, now(), '/controller/boundaryTypes-by-hierarchyType', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'AjaxLoadBoundaryTypes', 0, NULL, 'egi');


--rollback delete from eg_action where name = 'AjaxLoadBoundaryTypes';


INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AjaxLoadBoundaryTypes'));

--rollback delete from eg_roleaction_map where actionid=(select id from eg_action where name = 'AjaxLoadBoundaryTypes');


update eg_action set url = '/controller/addChildBoundaryType/isChildPresent' where name= 'AjaxAddChildBoundaryTypeCheck';

--rollback update eg_action set url = '/addChildBoundaryType/isChildPresent' where name= 'AjaxAddChildBoundaryTypeCheck';


