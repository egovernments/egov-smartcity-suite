#UP
-- brs scripts

UPDATE eg_module SET isenabled=1 WHERE module_name='BRS';
UPDATE CHARTOFACCOUNTS SET purposeid=30 WHERE glcode='246800';
update eg_action set is_enabled=1 where name='Dishonored Cheques Report';
COMMIT;


#DOWN