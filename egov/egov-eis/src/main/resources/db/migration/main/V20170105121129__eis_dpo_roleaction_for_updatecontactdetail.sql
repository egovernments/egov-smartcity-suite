INSERT into eg_role values(nextval('seq_eg_role'),'DPO Modification Features','DPO Modification Features',current_date,1,1,current_date,0);

INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Edit Employee Contact'), id from eg_role where name in ('DPO Modification Features');
