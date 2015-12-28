delete from eg_roleaction where roleid in (select id from eg_role where name='ULB Operator') and actionid in (select id from eg_action where contextroot ='pgr');
