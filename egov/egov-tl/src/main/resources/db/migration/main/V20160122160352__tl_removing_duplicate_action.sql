delete from eg_roleaction where actionid in ( select id from eg_action where name ='Enter Trade License');
delete from eg_action where name ='Enter Trade License';