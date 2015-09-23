--module names
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Trade License UOM','t','tl',(select id from eg_module where name = 'Trade License Masters'),'Unit Of Measurement', null);

--action urls
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Create Unit Of Measurement','/masters/unitOfMeasurement-newform.action',null,(select id from EG_MODULE where name = 'Trade License UOM'),1,'Create Unit Of Measurement','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Save UOM','/masters/unitOfMeasurement-save.action',null,(select id from EG_MODULE where name = 'Trade License UOM'),1,'Save UOM','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License UOM'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Search Unit Of Measurement','/masters/unitOfMeasurement-searchform.action','userMode=search',(select id from EG_MODULE where name = 'Trade License UOM'),1,'Search Unit Of Measurement','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'editUnitOfMeasurement','/masters/unitOfMeasurement-newform.action','id=',(select id from EG_MODULE where name = 'Trade License UOM'),1,'editUnitOfMeasurement','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'tradeLicenseAjaxMaster','/masters/ajaxMaster-validateActions.action',null,(select id from EG_MODULE where name = 'Trade License'),1,'tradeLicenseAjaxMaster','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));


--roleaciton mappings for super user
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Create Unit Of Measurement' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Save UOM' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Search Unit Of Measurement' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='editUnitOfMeasurement' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='tradeLicenseAjaxMaster' and contextroot = 'tl';
