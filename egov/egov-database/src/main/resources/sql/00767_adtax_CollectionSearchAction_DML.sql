update eg_action set name='CollectAdvertisementTax',displayname='Collect Advertisement Tax' where displayname='Search Hoarding' and name='SearchHoarding';

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'CollectAdvertisementTax'));