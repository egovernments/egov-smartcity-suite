--module names
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Trade License SubCategory','t','tl',(select id from eg_module where name = 'Trade License Masters'),'Sub Category', null);

--action urls
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create License SubCategory','/masters/licenseSubCategory-newform.action',null,(select id from EG_MODULE where name = 'Trade License SubCategory'),1,'Create License SubCategory','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Save License SubCategory','/masters/licenseSubCategory-save.action',null,(select id from EG_MODULE where name = 'Trade License SubCategory'),1,'Save License SubCategory','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Modify License SubCategory','/masters/licenseSubCategory-edit.action','userMode=edit',(select id from EG_MODULE where name = 'Trade License SubCategory'),2,'Modify License SubCategory','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'saveEditedSubCategory','/masters/licenseSubCategory-newform.action','id=',(select id from EG_MODULE where name = 'Trade License SubCategory'),2,'saveEditedSubCategory','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'View License SubCategory','/masters/licenseSubCategory-edit.action','userMode=view',(select id from EG_MODULE where name = 'Trade License SubCategory'),3,'View License SubCategory','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));



--roleaction mappings for super user
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Create License SubCategory' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Save License SubCategory' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Modify License SubCategory' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='View License SubCategory' and contextroot = 'tl';

