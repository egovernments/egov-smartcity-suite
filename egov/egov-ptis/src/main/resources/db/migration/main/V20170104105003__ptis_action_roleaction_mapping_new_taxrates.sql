--updating urls for appconfig taxrates and new action for new taxrates screen

UPDATE eg_action SET url= '/taxrates/appconfig/view' WHERE name= 'ViewTaxRates' AND contextroot= 'ptis';
UPDATE eg_action SET url= '/taxrates/appconfig/edit' WHERE name= 'Edit TaxRates Form' AND contextroot= 'ptis';

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Tax Rates','/taxrates/view',null,
(select id from eg_module  where name='PTIS-Masters'),1,'View Tax Rates(New)',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'EditTaxRates','/taxrates/edit',null,
(select id from eg_module  where name='PTIS-Masters'),1,'Edit Tax Rates(New)',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Tax Rates'),
id from eg_role where name in ('Super User');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View Tax Rates'),
id from eg_role where name in ('Property Approver');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'EditTaxRates'),
id from eg_role where name in ('Super User');
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'EditTaxRates'),
id from eg_role where name in ('Property Approver');
