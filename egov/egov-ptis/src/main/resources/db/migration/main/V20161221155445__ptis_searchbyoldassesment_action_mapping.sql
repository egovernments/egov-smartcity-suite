-----Creating Action

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Search Property By Old Assessment','/search/searchProperty-srchByOldMuncipalNumber.action',null,
(select id from eg_module where name='Existing property'),1,'Search Property By Old Assessment Number',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

------Assigning Action Roles to Users

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Property By Old Assessment'),id from eg_role where name in ('Super User','ULB Operator','VIEW_ACCESS_ROLE','Property Administrator','Property Verifier','Property Approver');
