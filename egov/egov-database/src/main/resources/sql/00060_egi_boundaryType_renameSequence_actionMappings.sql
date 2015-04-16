INSERT INTO EG_ACTION(ID, NAME, ENTITYID, TASKID, UPDATEDTIME, URL, QUERYPARAMS, URLORDERID, MODULE_ID, ORDER_NUMBER, DISPLAY_NAME, IS_ENABLED, ACTION_HELP_URL, CONTEXT_ROOT)
Values (nextval('SEQ_EG_ACTION'), 'AddChildBoundaryType', NULL, NULL, now(), '/controller/boundaryType/addChild', NULL, NULL, (SELECT ID_MODULE FROM EG_MODULE WHERE MODULE_DESC = 'Jurisdiction'),
0, 'Add Child Boundary Type', 1, NULL, 'egi');

INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPERUSER') ,(select id FROM eg_action  WHERE name = 'AddChildBoundaryType'));

DROP SEQUENCE seq_eg_boundary_type;

ALTER SEQUENCE seq_eg_bndry_type RENAME TO seq_eg_boundary_type;



-- rollback create sequence seq_eg_boundary_type;

-- ROLLBACK ALTER SEQUENCE seq_eg_boundary_type RENAME TO seq_eg_bndry_type;
