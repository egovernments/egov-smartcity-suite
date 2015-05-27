update eg_user set username='anonymous' where username='9999999999';

insert into eg_user (id,locale,username,password,pwdexpirydate,active,name,type,version) 
values(nextval('seq_eg_user'),'en_IN','999999999','$2a$10$uheIOutTnD33x7CDqac1zOL8DMiuz7mWplToPgcf7oxAI9OzRKxmK',
'31-Dec-2020','TRUE','999999999','CITIZEN',0);
