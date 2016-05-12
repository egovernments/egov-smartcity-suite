INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Search Bulk Router Generation','/bulkRouter/search',null,(select id from EG_MODULE where name = 'Router'),1,'Bulk Router Generation',true,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'Search Bulk Router Generation';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Save Bulk Router','/bulkRouter/save',null,(select id from EG_MODULE where name = 'Router'),1,'Save Bulk Router',false,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'Save Bulk Router';

