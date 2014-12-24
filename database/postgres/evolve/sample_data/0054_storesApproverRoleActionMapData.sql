#UP



INSERT INTO EG_ROLEACTION_MAP(ROLEID,ACTIONID)
(SELECT (SELECT id_role FROM EG_USERROLE WHERE id_user IN (
	(SELECT id_user  FROM EG_USER WHERE user_name LIKE'manager'))) AS userid,id FROM EG_ACTION
WHERE id IN (SELECT id FROM EG_ACTION WHERE module_id IN (
SELECT id_module FROM EG_MODULE WHERE module_name IN ( 'storesMasters', 'Item', 'Supplier','storesReports')) ));

#DOWN
