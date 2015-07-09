insert into eg_citizen (id) values ((select id from eg_user where username='8888888888'));

--rollback delete from eg_citizen where id=(select id from eg_user where username='8888888888');
