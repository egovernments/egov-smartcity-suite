INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Contractor wise abstract report','Contractor wise abstract report',(select id from EG_MODULE where name = 'Works Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorWiseAbstractSearchForm') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorWiseAbstract') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ContractorWiseAbstractPdfExcel') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'AjaxContractorsForContractorWiseAbstract') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));


INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Creator') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Works Approver') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ERP Report Viewer') ,(select id FROM eg_feature WHERE name = 'Contractor wise abstract report'));