Insert into EG_ACTION ("id","name","url","queryparams","parentmodule","ordernumber","displayname","enabled","contextroot","version","createdby","createddate","lastmodifiedby","lastmodifieddate","application") values (nextval('seq_eg_action'),'OnlineReceiptView','/citizen/onlineReceipt-view.action',null,(select ID from eg_module where NAME ='Receipt Services'),1,'OnlineReceiptView','0','collection',0,1,current_timestamp,1,current_timestamp,(select id from eg_module where name='Collection' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'OnlineReceiptView' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'OnlineReceiptView' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Remitter') ,(select id FROM eg_action  WHERE name = 'OnlineReceiptView' and contextroot='collection'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'OnlineReceiptView' and contextroot='collection'));


