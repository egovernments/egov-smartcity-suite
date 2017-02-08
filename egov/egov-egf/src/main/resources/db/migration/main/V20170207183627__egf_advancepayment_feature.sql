
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Advance Payment','Create Advance Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Advance Payment','Modify Advance Payment',(select id from eg_module  where name = 'EGF'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View Advance Payment','View Advance Payment',(select id from eg_module  where name = 'EGF'));


INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Create Advance Payment'), id from eg_action where name  in('Advance Requisition for Payment','ajax Advance Requisition for Payment','get party type by arfnumber','Get Advance Bills',
'Advance Payment newform','Accountno by branchid for payment','get Account Balance','Advance Payment create','Advance Payment success');

INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'Modify Advance Payment'), id from eg_action where name  in('Advance Payment success','Advance Payment view');

INSERT INTO eg_feature_action (FEATURE,ACTION )  select (select id FROM eg_feature WHERE name = 'View Advance Payment'), id from eg_action where name  in('Advance Payment view'); 



INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Create Advance Payment'),id from eg_role where name in('Super User','Payment Creator');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'Modify Advance Payment'),id from eg_role where name in('Super User','Payment Approver');
INSERT INTO eg_feature_role ( FEATURE,ROLE) select(select id FROM eg_feature WHERE name = 'View Advance Payment'),id from eg_role where name in('Super User','Payment Creator');




