
delete from eg_roleaction  where roleid =(select id from eg_role where name='EMPLOYEE') and
actionid not in(select id from eg_action where contextroot ='egi');