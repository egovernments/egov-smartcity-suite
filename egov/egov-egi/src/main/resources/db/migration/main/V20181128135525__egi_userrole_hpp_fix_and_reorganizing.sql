INSERT INTO eg_action
(ID,NAME,URL,QUERYPARAMS,QUERYPARAMREGEX,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,
 VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
values (NEXTVAL('SEQ_EG_ACTION'),'EMPLOYEE BY USERNAME','/user/employee-username-like/',null,'userName=(\w+( \w+)*)',
        (SELECT id FROM EG_MODULE WHERE name = 'User Module'),0,'EMPLOYEE BY USERNAME',FALSE, 
        'egi',0,(select id FROM eg_user WHERE username='system'),now(),
        (SELECT id FROM eg_user WHERE username='system'),now(),
        (SELECT id FROM eg_module WHERE name = 'Administration' AND parentmodule IS NULL));

INSERT INTO eg_roleaction(roleid,actionid) SELECT ra.roleid, act.id FROM 
(SELECT roleid FROM EG_ROLEACTION WHERE actionid=(SELECT id FROM eg_action WHERE name='ajaxuserlist')) ra,
 (SELECT id FROM EG_ACTION WHERE name='EMPLOYEE BY USERNAME') act;

INSERT INTO eg_feature_action(feature,action) SELECT ft.id,act.id FROM 
(SELECT id FROM eg_feature WHERE name='User Role Assignment') ft, 
(SELECT id FROM eg_action WHERE name='EMPLOYEE BY USERNAME') act;

DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='ajaxuserlist');

DELETE FROM eg_roleaction WHERE actionid=(SELECT id FROM eg_action WHERE name='ajaxuserlist');

DELETE FROM eg_action WHERE name='ajaxuserlist' AND contextroot='egi';

UPDATE eg_action SET name='EMPLOYEE ROLE BY USERNAME', url='/userrole/employee',
queryParamRegex='userName=(\w+( \w+)*)', displayname='Employee Role By Username' WHERE name='AjaxLoadRoleByUser';

DELETE FROM eg_feature_action WHERE action=(SELECT id FROM eg_action WHERE name='UpdateuserRole');

DELETE FROM eg_roleaction WHERE actionid=(SELECT id FROM eg_action WHERE name='UpdateuserRole');

DELETE FROM eg_action WHERE name='UpdateuserRole';

UPDATE eg_action SET name='UPDATE EMPLOYEE USER ROLE', displayname='Update Employee User Role' WHERE name='UpdateuserRoleForm';

UPDATE eg_action SET name='SEARCH EMPLOYEE USER ROLE' WHERE name='ViewuserRoleForm';

UPDATE eg_action SET name='VIEW EMPLOYEE USER ROLE', displayname='View Employee User Role' WHERE name='viewuserRole';