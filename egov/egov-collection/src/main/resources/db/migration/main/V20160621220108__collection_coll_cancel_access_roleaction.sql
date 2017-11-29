INSERT into eg_role values(nextval('seq_eg_role'),'Coll_Cancel Access','This role has access to  cancel screen ',current_date,1,1,current_date,0); 

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Cancel Access'),(select id from eg_action where name='SearchReceipts'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Cancel Access'),(select id from eg_action where name='SearchReceiptReset'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Cancel Access'),(select id from eg_action where name='SearchReceiptSearch'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Cancel Access'),(select id from eg_action where name='CancelReceipt'));

INSERT into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Coll_Cancel Access'),(select id from eg_action where name='SaveOnCancelReceipt'));
