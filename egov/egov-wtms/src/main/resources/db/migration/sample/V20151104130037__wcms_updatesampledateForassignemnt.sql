insert into eg_userrole (roleid,userid)values((select id from eg_role where name='ULB Operator')
,(select id from eg_user where username='shahid'));

update egeis_assignment set employee =(select id from egeis_employee where code='E029') where id in(select id from egeis_assignment where department in(select id from eg_department where name='Accounts') and designation in(select id from eg_designation where name='Senior Assistant')
and position in(select id from eg_position where name='R-Operator-1') and employee in(select id from eg_user where username='suresh'));
