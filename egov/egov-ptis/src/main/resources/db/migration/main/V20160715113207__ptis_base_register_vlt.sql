Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Base Register Report VLT','/report/baseRegisterVlt', null,(select id from EG_MODULE where name = 'PTIS-Reports'),null, 'Base Register(VLT)','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Base Register Report VLT result','report/baseRegisterVlt/result', null, (select id from EG_MODULE where name = 'PTIS-Reports'),null,'Base Register Report VLT result',false,'ptis',0,1,now(),1,now(), (select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Base Register Report VLT'),id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Base Register Report VLT result'), id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');
 
insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where displayname = 'Base Register Report VLT result' and contextroot='ptis'),(select id from eg_role where name in ('ERP Report Viewer')));


