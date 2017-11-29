Insert into eg_roleaction values((select id from eg_role where name='TP Standing Counsel'),
(select id from eg_action where name='File Download'));