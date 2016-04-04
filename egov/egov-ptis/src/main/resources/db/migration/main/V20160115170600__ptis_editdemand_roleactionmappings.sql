insert into eg_roleaction  (actionid,roleid) values((select id from eg_action where name='Edit Demand Update Form' and contextroot='ptis'),
(select id from eg_role where name='ULB Operator'));