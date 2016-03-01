Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Current Instalment DCB Report','/report/currentInstDCB', null,
(select id from EG_MODULE where name = 'PTIS-Reports'),null,'Current Instalment DCB Report','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Current Instalment DCB Report'), id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Current Instalment DCB Report result','/report/currentInstDCB/result', null,
(select id from EG_MODULE where name = 'PTIS-Reports'),null, 'Current Instalment DCB Report result','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Current Instalment DCB Report result'), id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');