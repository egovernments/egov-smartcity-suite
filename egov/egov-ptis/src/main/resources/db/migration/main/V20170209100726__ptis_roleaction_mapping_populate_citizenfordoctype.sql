
--action and roleaction mapping to populate default citizen by document type

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Populate Default User For Doctype','/common/ajaxcommon-defaultcitizen-fordoctype.action',null,
(select id from eg_module  where name='Property Tax'),1,'Populate Default User For Doctype',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Default User For Doctype'),
id from eg_role where name in ('Super User');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Default User For Doctype'),
id from eg_role where name in ('ULB Operator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Populate Default User For Doctype'),
id from eg_role where name in ('Property Verifier');
