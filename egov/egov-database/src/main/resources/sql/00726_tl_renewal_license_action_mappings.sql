Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'NewTradeLicense-renewal','/newtradelicense/newTradeLicense-renewal.action',null,(select id from EG_MODULE where name = 'Trade License'),null,null,'f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'NewTradeLicense-renewal';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLCreator'), id from eg_action where name = 'NewTradeLicense-renewal';

CREATE SEQUENCE  egtl_application_number;