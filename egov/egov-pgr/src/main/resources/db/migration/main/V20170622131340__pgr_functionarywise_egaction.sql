
Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate, application) values 
(NEXTVAL('SEQ_EG_ACTION'),'Functionarywise report Grand Total','/functionaryWiseReport/grand-total',null,
(select id from eg_module where name like 'Functionary Wise Report')
,null,'Functionarywise report Grand Total','false','pgr',0,1,now(),1,now(),(select id from eg_module where name='PGR'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (NEXTVAL('SEQ_EG_ACTION'),
'Functionarywise report Download','/functionaryWiseReport/download',null,
(select id from eg_module where name='Functionary Wise Report'),null,
'Functionarywise report Download','false','pgr',0,1,now(),1,now(),(select id from eg_module where name='PGR'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='Functionarywise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='Functionarywise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='PGR VIEW ACCESS'),
(select id from eg_action where name='Functionarywise report Download'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='PGR VIEW ACCESS'),
(select id from eg_action where name='Functionarywise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='Grivance Administrator'),
(select id from eg_action where name='Functionarywise report Grand Total'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
(select id from eg_action where name='Functionarywise report Grand Total'));


INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Functionarywise report Grand Total'),
(select id FROM EG_FEATURE WHERE name  ='Functionarywise Report'));

INSERT INTO EG_FEATURE_ACTION (action,feature) VALUES ((select id FROM eg_action WHERE name ='Functionarywise report Download'),
(select id FROM EG_FEATURE WHERE name  ='Functionarywise Report'));
