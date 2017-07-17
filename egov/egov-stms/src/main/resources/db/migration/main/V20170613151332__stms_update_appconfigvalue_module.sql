update eg_appconfig set module =(select id from eg_module where name='Sewerage Tax Management') where key_name='SEWERAGEDEPARTMENTFORCSCOPERATORWORKFLOW';
