-------------- EG_Action --------------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'LegacySewerageConnection','/transactions/legacyConnection-newform',null,(select id from EG_MODULE where name = 'sewerageMasters'),null,'Data Entry Screen',true,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ajaxCheckUniqueSHSC','/ajaxconnection/check-shscnumber-exists',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Ajax SHSC Number Unique Check','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'saveLegacySewerageConnection','/transactions/legacyConnection-create',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Save Legacy Sewerage Connection','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'legacySewerageConnectionSuccess','/transactions/sewerageLegacyApplication-success',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Legacy Sewerage Connection Success','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ajaxLegacyDemandDetails','/ajaxconnection/getlegacy-demand-details',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'Ajax Legacy Demand Details','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));

-------------- eg_roleaction --------------

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'LegacySewerageConnection'and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='ajaxCheckUniqueSHSC' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='saveLegacySewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='legacySewerageConnectionSuccess' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='ajaxLegacyDemandDetails' and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'LegacySewerageConnection'and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Administrator'),(select id from eg_action where name ='ajaxCheckUniqueSHSC' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Administrator'),(select id from eg_action where name ='saveLegacySewerageConnection' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Administrator'),(select id from eg_action where name ='legacySewerageConnectionSuccess' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Administrator'),(select id from eg_action where name ='ajaxLegacyDemandDetails' and contextroot = 'stms'));

--------- eg_feature ------------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Data Entry Screen','Create Legacy Sewerage Connection',(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'LegacySewerageConnection') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxCheckUniqueSHSC') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'saveLegacySewerageConnection') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'legacySewerageConnectionSuccess') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'ajaxLegacyDemandDetails') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));

INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));
INSERT INTO eg_feature_role (ROLE, FEATURE) VALUES ((select id from eg_role where name = 'Super User') ,(select id FROM eg_feature WHERE name = 'Data Entry Screen'));
