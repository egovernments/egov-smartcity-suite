-----------role action mapping-------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'PropertyTaxRegisterResult','/report/taxregister/result', null,
(select id from EG_MODULE where name = 'Existing property'),null,'Property Tax Register Result',false,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'PropertyTaxRegister','/report/taxregister-pt/form',null,(select id from eg_module where name='Existing property'),1,'Property Tax Register (PT)',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'PropertyTaxRegisterResult' and contextroot='ptis'), id from eg_role where name in ('ERP Report Viewer','Property Verifier','Property Approver','SYSTEM');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'PropertyTaxRegister' and contextroot='ptis'), id from eg_role where name in ('ERP Report Viewer','Property Verifier','Property Approver','SYSTEM');
