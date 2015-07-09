insert into eg_userrole (id_role,id_user,id,fromdate,todate,is_history) values ((select id_role from eg_roles where role_name='Super User'),
(select id_user from eg_user where user_name='narasappa'),nextval('seq_eg_userrole'),'01-Apr-2015','31-Mar-2020','N');
