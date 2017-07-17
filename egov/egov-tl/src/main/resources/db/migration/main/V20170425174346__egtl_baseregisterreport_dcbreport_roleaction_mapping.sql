
--Roleaction mapping
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'DCBReportgeneration','/tlreports/report',null,444,1,'DCBReportgeneration',FALSE, 'tl',0,1,now(),1,now(),443);

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLCreator'),(SELECT id FROM eg_action WHERE name ='DCBReportgeneration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLApprover'),(SELECT id FROM eg_action WHERE name ='DCBReportgeneration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Super User'),(SELECT id FROM eg_action WHERE name ='DCBReportgeneration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TL VIEW ACCESS'),(select id from eg_action where name='DCBReportgeneration'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLAdmin'),(select id from eg_action where name='DCBReportgeneration'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'DCBReportSum','/tlreports/reporttotal',null,444,1,'BaseRegisterReport',FALSE, 'tl',0,1,now(),1,now(),443);

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLCreator'),(SELECT id FROM eg_action WHERE name ='DCBReportSum'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLApprover'),(SELECT id FROM eg_action WHERE name ='DCBReportSum'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Super User'),(SELECT id FROM eg_action WHERE name ='DCBReportSum'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TL VIEW ACCESS'),(select id from eg_action where name='DCBReportSum'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLAdmin'),(select id from eg_action where name='DCBReportSum'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'BaseReportTotal','/baseregister/reportwisetotal',null,444,1,'BaseReportTotal',FALSE, 'tl',0,1,now(),1,now(),443);

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLCreator'),(SELECT id FROM eg_action WHERE name ='BaseReportTotal'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLApprover'),(SELECT id FROM eg_action WHERE name ='BaseReportTotal'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Super User'),(SELECT id FROM eg_action WHERE name ='BaseReportTotal'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TL VIEW ACCESS'),(select id from eg_action where name='BaseReportTotal'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLAdmin'),(select id from eg_action where name='BaseReportTotal'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'BaseRegisterReport','/baseregister/report',null,444,1,'BaseRegisterReport',FALSE, 'tl',0,1,now(),1,now(),443);

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'TLCreator'), (SELECT id FROM eg_action WHERE name ='BaseRegisterReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLApprover'),(SELECT id FROM eg_action WHERE name ='BaseRegisterReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Super User'),(SELECT id FROM eg_action WHERE name ='BaseRegisterReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TL VIEW ACCESS'),(select id from eg_action where name='BaseRegisterReport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='TLAdmin'),(select id from eg_action where name='BaseRegisterReport'));

--Feature action/role mapping
INSERT INTO EG_FEATURE (id,name,description,module,version) VALUES (nextval('SEQ_EG_ACTION'),'Base Register Report','Base Register Report',(select id from eg_module where name='Trade License'),0);
 
 INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='BaseReportTotal'),(select id FROM EG_FEATURE
 WHERE name  ='Base Register Report'));
 
 INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='BaseRegisterReport'),(select id FROM EG_FEATURE
 WHERE name  ='Base Register Report'));
 
 INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('Super User')),(select id FROM EG_FEATURE
 WHERE name  ='Base Register Report'));
 
 INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLAdmin')),(select id FROM EG_FEATURE
 WHERE name  ='Base Register Report'));
 
 INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLCreator')),(select id FROM EG_FEATURE
 WHERE name  ='YearWise DCB Report'));
 
 INSERT INTO EG_FEATURE_role (role,feature) VALUES ((select id FROM eg_role where name in ('TLApprover')),(select id FROM EG_FEATURE
 WHERE name  ='Base Register Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='DCBReportSum'),(select id FROM EG_FEATURE
WHERE name  ='DCB Report By Trade'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='DCBReportgeneration'),(select id FROM EG_FEATURE
WHERE name  ='DCB Report By Trade'));
