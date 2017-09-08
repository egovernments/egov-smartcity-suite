
Insert into eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'REASSIGNMENT','Enable/Disable reassignment option',0,(select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='REASSIGNMENT' and module=(select id from eg_module where name='Water Tax Management')), now(), 'NO', (select id from eg_user where username='egovernments'), (select id from eg_user where username='egovernments'), now(),now(), 0);

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'ReassignWaterChargesApplication', '/application/reassign', null,(select id from eg_module where name='WaterTaxTransactions'), 1, 'Reassign Water Charges application', false,'wtms', 0, 1, now(), 1, now(),(select id from eg_module where name='Water Tax Management'));

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name='ReassignWaterChargesApplication' and contextroot='wtms'),(select id from eg_role where name='ULB Operator'));

insert into eg_roleaction (actionid, roleid) values ((select id from eg_action where name='ReassignWaterChargesApplication' and contextroot='wtms'),(select id from eg_role where name='SYSTEM'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='Reassignment'),
(select id from eg_feature  where name='Create WaterTax NewConnection'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='Reassignment'),
(select id from eg_feature  where name='Create WaterTax AdditionalConnection'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='Reassignment'),
(select id from eg_feature  where name='Create WaterTax ChangeOfUseConnection'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='Reassignment'),
(select id from eg_feature  where name='Create WaterTax ClosureConnection'));

insert into eg_feature_action (action,feature) values ((select id from eg_action where name='Reassignment'),
(select id from eg_feature  where name='Create WaterTax ReConnection'));

