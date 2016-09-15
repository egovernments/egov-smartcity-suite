
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Bank','View Bank',(select id from eg_module  where name = 'EGF'));

--View Bank
INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Bank'), id from eg_action where name  in('BankMasterView','modifyBank','BankBranchMaster','BankAccountMaster' );

INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Bank'),id from eg_role where name in('Super User','Financial Administrator');
