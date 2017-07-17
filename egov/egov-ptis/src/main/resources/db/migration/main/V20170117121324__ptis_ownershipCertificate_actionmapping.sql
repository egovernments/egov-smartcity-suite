-----Ownership Certificate Action Mapping

insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'OwnershipCertificate','/ownershipcertificate/searchform',null,
(select id from eg_module where name='PTIS-Reports'),1,'Ownership Certificate',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'GenerateOwnershipCertificate','/ownershipcertificate/generate',null,
(select id from eg_module where name='PTIS-Reports'),1,'Generate Ownership Certificate',false,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));


INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'OwnershipCertificate'),id from eg_role where name in ('Property Approver');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'GenerateOwnershipCertificate'),id from eg_role where name in ('Property Approver');












