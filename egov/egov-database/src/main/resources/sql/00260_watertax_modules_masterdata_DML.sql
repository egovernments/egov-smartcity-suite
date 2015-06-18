
INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Water Tax Management', true, 'wtms', null, 'Water Tax Management', (select max(ordernumber)+1 from eg_module where parentmodule is null));

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'WaterTaxMasters', false, null, (select id from eg_module where name='Water Tax Management'), 'Masters', 1);

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'WaterTaxTransactions', true, null, (select id from eg_module where name='Water Tax Management'), 'Transactions', 2);

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'WaterTaxReports', false, null, (select id from eg_module where name='Water Tax Management'), 'Reports', 3);

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'WaterTaxApplication', true, null, (select id from eg_module where name='WaterTaxTransactions'), 'Application', 1);

--rollback delete from eg_module where name in ('WaterTaxApplication','WaterTaxReports','WaterTaxTransactions','WaterTaxMasters','Water Tax Management');

