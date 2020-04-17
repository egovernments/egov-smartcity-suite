Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'portalDeLink','/citizen/delinkconnection/',null,(select id from EG_MODULE where name = 'Portal Services'),null,'Portal Link',false,'portal',0,1,now(),
(select id from eg_user where username = 'egovernments'),now(),(select id from eg_module  where name = 'Citizen Portal'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='portalDeLink' and contextroot = 'portal'));
