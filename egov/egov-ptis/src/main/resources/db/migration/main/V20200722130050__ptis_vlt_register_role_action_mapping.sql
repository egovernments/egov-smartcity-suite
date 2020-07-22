-----------role action mapping-------

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'VLTRegister','/report/taxregister-vlt/form',null,(select id from eg_module where name='Existing property'),1,'Property Tax Register (VLT)',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'VLTRegister' and contextroot='ptis'), id from eg_role where name in ('ERP Report Viewer','Property Verifier','Property Approver','SYSTEM');

