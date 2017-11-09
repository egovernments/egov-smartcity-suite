Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,
createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),
'ajax edit watertax donation amount','/ajax-isdonationamount-editable',null,(select id from eg_module
 where name='WaterTaxTransactions'),6,'ajaxeditdonationamount','false','wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='ajax edit watertax donation amount'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Tax Approver'),
(select id from eg_action where name='ajax edit watertax donation amount'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),
(select id from eg_action where name='ajax edit watertax donation amount'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),
(select id from eg_action where name='ajax edit watertax donation amount'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax edit watertax donation amount') ,(select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax edit watertax donation amount') ,(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajax edit watertax donation amount') ,(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('ajax edit watertax donation amount') and contextroot = 'wtms') and roleid in((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Water Tax Approver'),(select id from eg_role where name = 'ULB Operator'),(select id from eg_role where name = 'Property Administrator'));
--rollback delete from eg_feature_action where action = (select id FROM eg_action  WHERE name = 'ajax edit watertax donation amount') and feature in ((select id FROM eg_feature WHERE name = 'Create WaterTax NewConnection'),(select id FROM eg_feature WHERE name = 'Create WaterTax AdditionalConnection'),(select id FROM eg_feature WHERE name = 'Update WaterTax Connection'));
--rollback delete from eg_action where name in ('ajax edit watertax donation amount') and contextroot = 'wtms';