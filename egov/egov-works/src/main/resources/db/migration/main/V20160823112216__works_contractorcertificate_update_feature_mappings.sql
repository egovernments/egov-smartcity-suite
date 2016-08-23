update eg_action set enabled = false where name='ContractCertificatePDF' and contextroot='egworks';

delete from eg_feature_role  where feature = (select id from eg_feature  where name ='View Contract Certificate');
delete from eg_feature_action  where feature = (select id from eg_feature  where name ='View Contract Certificate');
delete from eg_feature  where name = 'View Contract Certificate' and module=(select id from EG_MODULE where name = 'Works Management');

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractCertificatePDF') ,(select id FROM eg_feature WHERE name = 'Search Contractor Bill'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractCertificatePDF') ,(select id FROM eg_feature WHERE name = 'Update Contractor Bill'));