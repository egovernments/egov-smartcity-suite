Insert into eg_usercounter_map  values(nextval('seq_eg_usercounter_map'), (select id from eg_user where username ='syed'),(select id from eg_location where name='Zone-1'), 
to_date('01-04-2012','DD-MM-YYYY'), to_date('31-03-2099','DD-MM-YYYY'),1, current_timestamp);

Insert into eg_usercounter_map  values(nextval('seq_eg_usercounter_map'), (select id from eg_user where username ='suresh'),(select id from eg_location where name='Zone-1'), 
to_date('01-04-2012','DD-MM-YYYY'), to_date('31-03-2099','DD-MM-YYYY'),1, current_timestamp);


insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Property Tax Collection'),(select id from eg_role where name = 'ULB Operator'));

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Mutation Fee Payment'),(select id from eg_role where name = 'ULB Operator'));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Mutation Fee Payment Search'),(select id from eg_role where name = 'ULB Operator'));
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Mutation Fee Payment Generate'),(select id from eg_role where name = 'ULB Operator'));