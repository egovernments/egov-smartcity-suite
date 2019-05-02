-------------------------new role to manually reconcile the transaction---------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'ONLINEPAYMENT APPROVER', 'Reconcile online payments', now(), (select id from eg_user where lower(username) ='system') , (select id from eg_user where lower(username) ='system'), now(), 0);

----------------Search onlinepayment Receeipts -----------------------
DELETE from eg_roleaction where actionid in(select id from eg_action where name='SearchOnlineReceipts' and contextroot='collection') and roleid in (select id from eg_role where name in('ONLINEPAYMENT APPROVER')) ;
DELETE from eg_roleaction where actionid in(select id from eg_action where name='SearchOnlineReceiptsSearch' and contextroot='collection') and roleid in (select id from eg_role where name in('ONLINEPAYMENT APPROVER')) ;
DELETE from eg_roleaction where actionid in(select id from eg_action where name='ReceiptReconcileOnlinePayment' and contextroot='collection') and roleid in (select id from eg_role where name in('ONLINEPAYMENT APPROVER')) ;
DELETE from eg_roleaction where actionid in(select id from eg_action where name='SearchOnlineReceiptReset' and contextroot='collection') and roleid in (select id from eg_role where name in('ONLINEPAYMENT APPROVER')) ;
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ONLINEPAYMENT APPROVER'),(select id from eg_action where name='SearchOnlineReceipts'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ONLINEPAYMENT APPROVER'),(select id from eg_action where name='SearchOnlineReceiptsSearch'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ONLINEPAYMENT APPROVER'),(select id from eg_action where name='ReceiptReconcileOnlinePayment'));
INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ONLINEPAYMENT APPROVER'),(select id from eg_action where name='SearchOnlineReceiptReset')); 