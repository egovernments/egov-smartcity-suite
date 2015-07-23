update eg_action set parentmodule=(select id from eg_module where name='WaterTaxTransactions') where parentmodule in (select id from eg_module where name='WaterTaxApplication') and contextroot='wtms';
delete from eg_module where name='WaterTaxApplication';
update eg_action set displayname='New Water Tap Connection' where name='WaterTaxCreateNewConnectionNewForm' and contextroot='wtms';


--rollback update eg_action set displayname='New Connection' where name='WaterTaxCreateNewConnectionNewForm' and contextroot='wtms';
--rollback INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'WaterTaxApplication', true, null, (select id from eg_module where name='WaterTaxTransactions'), 'Application', 1);
--rollback update eg_action set parentmodule=(select id from eg_module where name='WaterTaxApplication') where parentmodule in (select id from eg_module where name='WaterTaxTransactions') and contextroot='wtms';
