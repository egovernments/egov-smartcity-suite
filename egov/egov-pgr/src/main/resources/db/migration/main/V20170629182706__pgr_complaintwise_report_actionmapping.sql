
--complainttypewise report action mapping
Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'ComplaintTypewise report Grand Total','/report/grand-total',null,
(select id from eg_module where name like 'Complaint Type Wise Report')
,null,'ComplaintTypewise report Grand Total','false','pgr',0,1,now(),1,now(),(select id from eg_module where name='PGR'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (NEXTVAL('SEQ_EG_ACTION'),
'ComplaintTypewise report Download','/report/download',null,
(select id from eg_module where name='Complaint Type Wise Report'),null,
'ComplaintTypewise report Download','false','pgr',0,1,now(),1,now(),(select id from eg_module where name='PGR'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='ComplaintTypewise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='ComplaintTypewise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='PGR VIEW ACCESS'),
(select id from eg_action where name='ComplaintTypewise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='PGR VIEW ACCESS'),
(select id from eg_action where name='ComplaintTypewise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='ComplaintTypewise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='ComplaintTypewise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Routing Officer'),
(select id from eg_action where name='ComplaintTypewise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Routing Officer'),
(select id from eg_action where name='ComplaintTypewise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Redressal Officer'),
(select id from eg_action where name='ComplaintTypewise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Redressal Officer'),
(select id from eg_action where name='ComplaintTypewise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Officer'),
(select id from eg_action where name='ComplaintTypewise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grievance Officer'),
(select id from eg_action where name='ComplaintTypewise report Download'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='ComplaintTypewise report Grand Total'),
(select id FROM EG_FEATURE WHERE name  ='Grievance Typewise Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='ComplaintTypewise report Download'),
(select id FROM EG_FEATURE WHERE name  ='Grievance Typewise Report'));
