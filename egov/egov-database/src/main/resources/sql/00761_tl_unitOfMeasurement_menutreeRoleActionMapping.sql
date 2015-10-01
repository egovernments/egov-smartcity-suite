-- Deleting 'Search Unit of Measurement' menutree link
Delete from eg_roleaction where roleid = (select id from eg_role where name = 'Super User') and actionid=(select id from eg_action where name='Search Unit Of Measurement' and contextroot = 'tl');
Delete from eg_action where name='Search Unit Of Measurement' and contextroot = 'tl';

-- Adding Modify / View Menutree links
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Modify Unit Of Measurement','/masters/unitOfMeasurement-edit.action','userMode=edit',(select id from EG_MODULE where name = 'Trade License UOM'),2,'Modify Unit Of Measurement','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'View Unit Of Measurement','/masters/unitOfMeasurement-edit.action','userMode=view',(select id from EG_MODULE where name = 'Trade License UOM'),3,'View Unit Of Measurement','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='Modify Unit Of Measurement' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='View Unit Of Measurement' and contextroot = 'tl';






