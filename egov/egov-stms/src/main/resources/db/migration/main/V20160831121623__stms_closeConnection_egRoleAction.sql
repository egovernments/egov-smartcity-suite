--------------------EG_ACTION-------------------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CloseSewerageConnection','/transactions/closeConnection',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Close Sewerage Connection','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CloseSewerageConnectionSuccess','/transactions/closeConnection-success',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Close Sewerage Connection Success','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CloseSewerageConnectionUpdate','/transactions/closeSewerageConnection-update',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Close Sewerage Connection Update','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'SewerageTaxCloseConnectionNotice', 
'/transactions/closeConnectionNotice', null, (select id from eg_module where name = 'Sewerage Tax Management'), 2, 'Sewerage Tax Close Connection Notice', false, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

--------------------EG_ROLEACTION-------------------
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_action where name ='CloseSewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='CloseSewerageConnection' and contextroot = 'stms'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_action where name ='CloseSewerageConnectionSuccess' and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name = 'CloseSewerageConnectionSuccess' and contextroot = 'stms')); 
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='CloseSewerageConnectionSuccess' and contextroot = 'stms')); 

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='CloseSewerageConnectionUpdate' and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name = 'CloseSewerageConnectionUpdate' and contextroot = 'stms')); 
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_action where name ='CloseSewerageConnectionUpdate' and contextroot = 'stms'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='ULB Operator') , (select id from eg_action where name='SewerageTaxCloseConnectionNotice'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'SewerageTaxCloseConnectionNotice' and contextroot = 'stms')); 


------------------ eg_feature ---------------
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create Close Connection','Create Close Connection',(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Modify Close Connection','Create Close Connection',(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CloseSewerageConnection') ,(select id FROM eg_feature WHERE name = 'Create Close Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CloseSewerageConnectionSuccess') ,(select id FROM eg_feature WHERE name = 'Create Close Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CloseSewerageConnectionUpdate') ,(select id FROM eg_feature WHERE name = 'Modify Close Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'CloseSewerageConnectionSuccess') ,(select id FROM eg_feature WHERE name = 'Modify Close Connection'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'SewerageTaxCloseConnectionNotice') ,(select id FROM eg_feature WHERE name = 'Modify Close Connection'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Create Close Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Create Close Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'ULB Operator') ,(select id FROM eg_feature WHERE name = 'Modify Close Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver') ,(select id FROM eg_feature WHERE name = 'Modify Close Connection'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator') ,(select id FROM eg_feature WHERE name = 'Modify Close Connection'));

