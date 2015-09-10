INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Advertisement Tax', true, 'wtms', null, 'Advertisement Tax', (select max(ordernumber)+1 from eg_module where parentmodule is null));

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'AdvertisementTaxMasters', false, null, (select id from eg_module where name='Advertisement Tax'), 'Masters', 1);

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'AdvertisementTaxTransactions', true, null, (select id from eg_module where name='Advertisement Tax'), 'Transactions', 2);

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'AdvertisementTaxReports', false, null, (select id from eg_module where name='Advertisement Tax'), 'Reports', 3);

--rollback delete from eg_module where name in ('AdvertisementTaxReports','AdvertisementTaxTransactions','AdvertisementTaxMasters','Advertisement Tax');

