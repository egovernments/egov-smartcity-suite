insert into eg_roleaction values((select id from eg_role where name='Works Administrator'),(select id from eg_action where name='ContractCertificatePDF'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Contract Certificate','View Contract Certificate',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractCertificatePDF') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Bill Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Bill Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Voucher Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Voucher Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Payment Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Payment Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Administrator') ,(select id FROM eg_feature WHERE name = 'View Contract Certificate'));