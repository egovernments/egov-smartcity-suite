---action for edit taxrates
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Edit TaxRates Form','/taxrates/edit',null,(select id from eg_module where name='PTIS-Masters'),1,'Edit TaxRates',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

----role action for edit taxrates
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Edit TaxRates Form' and contextroot='ptis'), id from eg_role where name in ('Property Approver');
