
alter table egadtax_hoarding add column legacy boolean DEFAULT false;
alter table egadtax_hoarding add column penaltyCalculationDate  timestamp without time zone;
alter table egadtax_hoarding add column pendingTax bigint DEFAULT 0;

insert INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'CreateLegacyHoarding', '/hoarding/createLegacy', null, 
    (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Create Legacy Hoarding', true, 'adtax', 0, 1, now(), 1, now(),
    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'CreateLegacyHoarding'));
