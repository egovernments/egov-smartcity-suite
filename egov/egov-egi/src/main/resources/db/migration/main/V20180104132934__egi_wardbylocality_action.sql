INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION)
values (NEXTVAL('SEQ_EG_ACTION'),'getWardsByLocality','/boundary/ward-bylocality',null,(select id from EG_MODULE where name = 'Boundary Module'),0,'Load Wards By Locality',FALSE, 'egi',0,(select id from eg_user where username='system'),now(),(select id from eg_user where username='system'),now(),(SELECT id FROM eg_module WHERE name = 'Administration' AND parentmodule IS NULL));


