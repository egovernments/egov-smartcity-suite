
INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Search TL For Bulk Demand Notice','/search/demandnotice',null,(select id from EG_MODULE where name = 'Trade License Transactions'),4,'Generate Bulk Demand Notice','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice'), (select id from eg_role where name = 'TLAdmin'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice'), (select id from eg_role where name = 'Super User'));


INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Search TL For Bulk Demand Notice generation','/search/demandnotice-result',null,(select id from EG_MODULE where name = 'Trade License Transactions'),4,'Search TL For Bulk Demand Notice generation','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice generation'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice generation'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice generation'), (select id from eg_role where name = 'TLAdmin'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'Search TL For Bulk Demand Notice generation'), (select id from eg_role where name = 'Super User'));


INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'generate tl demand notice pdf','/demandnotice/generate',null,(select id from EG_MODULE where name = 'Trade License Transactions'),4,'generate tl demand notice pdf','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'generate tl demand notice pdf'), (select id from eg_role where name = 'TLCreator'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'generate tl demand notice pdf'), (select id from eg_role where name = 'TLApprover'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'generate tl demand notice pdf'), (select id from eg_role where name = 'TLAdmin'));
INSERT INTO eg_roleaction (actionid, roleid) values ((select id from eg_action where name = 'generate tl demand notice pdf'), (select id from eg_role where name = 'Super User'));