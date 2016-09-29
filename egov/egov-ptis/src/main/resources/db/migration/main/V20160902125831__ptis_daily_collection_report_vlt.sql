Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Daily collection report VLT','/report/dailyCollectionVLT',null,(select id from EG_MODULE where name = 'PTIS-Reports'),null, 'Daily collection report(VLT)','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Daily collection report VLT result','/report/dailyCollectionVLT/result', null,(select id from EG_MODULE where name = 'PTIS-Reports'),null,'Daily collection report VLT result',false,'ptis',0,1,now(),1,now(), (select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Daily collection report VLT'),id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Daily collection report VLT result'), id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');
 
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Daily collection report VLT result' and contextroot='ptis'),(select id from eg_role where name in ('ERP Report Viewer')));
