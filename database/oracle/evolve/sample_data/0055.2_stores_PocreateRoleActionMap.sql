
insert into EG_ROLEACTION_MAP(ROLEID,ACTIONID)
       (select (select id_role from EG_USERROLE where id_user in (
       	(select id_user  from eg_user where user_name like'manager'))) as userid,id from eg_action
      where id in ( select id from eg_action where name in ('Create Purchase Order - Non Indent')));

insert into EG_ROLEACTION_MAP(ROLEID,ACTIONID)
       (select (select id_role from EG_USERROLE where id_user in (
       	(select id_user  from eg_user where user_name like'egovernments'))) as userid,id from eg_action
      where id in ( select id from eg_action where name in ('Create Purchase Order - Non Indent')));