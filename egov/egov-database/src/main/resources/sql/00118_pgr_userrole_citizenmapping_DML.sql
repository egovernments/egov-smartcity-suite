delete from eg_userrole where roleid in(select id from eg_role where name ='SuperUser') and userid in
(select id from eg_user where username ='9999999999');