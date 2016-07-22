--------------------------------------------------------- START -------------------------------------------------------------------
DELETE FROM eg_roleaction where actionid IN (select id from eg_action where parentmodule = (select id from eg_module where name = 'Asset Category') and url like '%.action');
DELETE FROM eg_action where parentmodule = (select id from eg_module where name = 'Asset Category' );
delete from eg_module where name = 'Asset Category';

DELETE FROM eg_roleaction 
where actionid IN (select id from eg_action where parentmodule = (select id from eg_module where name = 'Asset Master'));
DELETE FROM eg_action where parentmodule = (select id from eg_module where name = 'Asset Master');
delete from eg_module where name = 'Asset Master';
--------------------------------------------------------- END ---------------------------------------------------------------------
