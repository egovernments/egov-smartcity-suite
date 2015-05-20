Insert into EG_ROLE (id,name,description,localname,localdescription,createddate,createdby,lastmodifiedby,lastmodifieddate,version) values (nextval('seq_eg_role'),'Asset Administrator','Asset Administrator',null,null,now(),null,null,null,0);

--rollback delete from EG_ROLE where name='Asset Administrator';
