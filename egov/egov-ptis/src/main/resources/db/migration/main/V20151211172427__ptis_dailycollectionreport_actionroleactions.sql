Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Daily collection report','/report/dailyCollection', null,(select id from EG_MODULE where name = 'PTIS-Reports'),null,'Daily collection report',true,'ptis',0,1,now(),1,now(),
(select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Daily collection report'), id from eg_role where name in ('Property Verifier');

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Daily collection report result','/report/dailyCollection/result', null,(select id from EG_MODULE where name = 'PTIS-Reports'),null,'Daily collection report result',false,'ptis',0,1,now(),1,now(),
(select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Daily collection report result'), id from eg_role where name in('Property Verifier');