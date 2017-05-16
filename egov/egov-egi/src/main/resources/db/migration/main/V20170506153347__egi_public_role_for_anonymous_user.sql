INSERT INTO EG_ROLE(id,name,description,createddate,createdby,lastmodifiedby,lastmodifieddate,version) VALUES (nextval('seq_eg_role'),'PUBLIC','Role for accessing public url',now(),1,1,now(),0);

INSERT INTO EG_USERROLE(roleid,userid) VALUES((select id from eg_role where name='PUBLIC'),(select id from eg_user where username='anonymous'));
