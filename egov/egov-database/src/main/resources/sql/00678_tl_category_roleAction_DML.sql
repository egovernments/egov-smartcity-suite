--module names
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Trade License Masters','t','tl',(select id from eg_module where name = 'Trade License'),'Masters', null);

insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Trade License Category','t','tl',(select id from eg_module where name = 'Trade License Masters'),'Category', null);

--action urls
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create License Category','/masters/licenseCategory-newform.action',null,(select id from EG_MODULE where name = 'Trade License Category'),1,'Create License Category','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Save License Category','/masters/licenseCategory-save.action',null,(select id from EG_MODULE where name = 'Trade License Category'),1,'Save License Category','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Modify License Category','/masters/licenseCategory-edit.action','userMode=edit',(select id from EG_MODULE where name = 'Trade License Category'),2,'Modify License Category','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'saveEditedCategory','/masters/licenseCategory-newform.action','id=',(select id from EG_MODULE where name = 'Trade License Category'),2,'saveEditedCategory','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'View License Category','/masters/licenseCategory-edit.action','userMode=view',(select id from EG_MODULE where name = 'Trade License Category'),3,'View License Category','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));


--roleaciton mappings for super user
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Create License Category' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Save License Category' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Modify License Category' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='View License Category' and contextroot = 'tl';


