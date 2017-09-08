 
--PGR ROUTER VIEW DOWNLOAD
 Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (NEXTVAL('SEQ_EG_ACTION'),
 'ComplaintRouter Download','/router/download',null,
 (select id from eg_module where name='Pgr Masters'),null,
 'ComplaintRouter Download','false','pgr',0,1,now(),1,now(),(select id from eg_module where name='PGR'));
 
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
 (select id from eg_action where name='ComplaintRouter Download'));
 
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
 (select id from eg_action where name='ComplaintRouter Download'));
 
 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='PGR VIEW ACCESS'),
 (select id from eg_action where name='ComplaintRouter Download'));

 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Routing Officer'),
 (select id from eg_action where name='ComplaintRouter Download'));

 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Redressal Officer'),
 (select id from eg_action where name='ComplaintRouter Download'));
 
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Officer'),
 (select id from eg_action where name='ComplaintRouter Download'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='ComplaintRouter Download'),
 (select id FROM EG_FEATURE WHERE name  ='View Router'));
 