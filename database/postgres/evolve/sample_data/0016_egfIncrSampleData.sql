#UP

-- SET isenabled=0 WHERE parentid=(SELECT id_module FROM eg_module WHERE module_name='EIS') AND module_name NOT IN ('Employee','Designation');
--UPDATE eg_module SET isenabled=0 WHERE module_name='Payroll';
--update eg_appconfig_values val set val.VALUE = 'no' where val.KEY_ID =(select id from eg_appconfig ap where ap.KEY_NAME like 'EMPAUTOGENERATECODE');
--COMMIT;
#DOWN
