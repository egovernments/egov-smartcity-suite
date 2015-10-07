Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'NatureOfUsageReport-form','/reports/natureOfUsageReport-form', null,(select id from EG_MODULE where name = 'PTIS-Reports'),1,
'Nature of usage report','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'NatureOfUsageReport-result','/reports/natureOfUsageReportList', null,(select id from EG_MODULE where name = 'PTIS-Reports'),null,
null,'f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'NatureOfUsageReport-form'), id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'NatureOfUsageReport-result'), id from eg_role where name in ('Super User','ULB Operator','Property Administrator','Property Approver','Property Verifier');