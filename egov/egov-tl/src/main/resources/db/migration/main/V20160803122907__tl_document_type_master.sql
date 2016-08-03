insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES 
(NEXTVAL('SEQ_EG_MODULE'),'License Document Type','t','tl',(select id from eg_module where 
name = 'Trade License Masters'),'License Document Type', 4);


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),
'Create License Document Type','/documenttype/create',null,(select id from EG_MODULE 
where name = 'License Document Type'),1,'Create Document Type','t','tl',0,1,now(),1,now(),(select id from eg_module  where 
name = 'Trade License'));



Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),
'Search License Document Type','/documenttype/search',null,(select id from EG_MODULE 
where name = 'License Document Type'),2,'Search Document Type','t','tl',0,1,now(),1,now(),(select id from eg_module  where 
name = 'Trade License'));


insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Create License Document Type' and contextroot='tl'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Search License Document Type' and contextroot='tl'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),
'Edit Document Type','/documenttype/edit',null,(select id from EG_MODULE 
where name = 'License Document Type'),2,'Search Document Type','f','tl',0,1,now(),1,now(),(select id from eg_module  where 
name = 'Trade License'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Edit Document Type' and contextroot='tl'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),
'Delete Document Type','/documenttype/delete',null,(select id from EG_MODULE 
where name = 'License Document Type'),2,'Search Document Type','f','tl',0,1,now(),1,now(),(select id from eg_module  where 
name = 'Trade License'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='Delete Document Type' and contextroot='tl'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),
'View Document Type','/documenttype/view',null,(select id from EG_MODULE 
where name = 'License Document Type'),2,'Search Document Type','f','tl',0,1,now(),1,now(),(select id from eg_module  where 
name = 'Trade License'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='View Document Type' and contextroot='tl'));

ALTER TABLE egtl_document_type
DROP CONSTRAINT unq_egtl_document_name;

ALTER TABLE egtl_document_type
ADD CONSTRAINT unq_egtl_document_name UNIQUE (name, applicationtype);

ALTER TABLE egtl_document_type Add COLUMN enabled boolean default true;

update egtl_document_type set applicationtype='NEW' where applicationtype='Create License';

