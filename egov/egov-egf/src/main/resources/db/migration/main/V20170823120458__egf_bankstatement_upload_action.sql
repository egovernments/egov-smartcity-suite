insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,
LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'bankStatementUploadSearch','/bankstatement/search',null,
(select id from EG_MODULE where name = 'BRS'),6,'View Bank Statement Uploaded Files',true,'EGF',0,1,now(),1,now(),
(select id from eg_module  where name = 'EGF'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,
LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'bankStatementUploadAjaxSearch','/bankstatement/ajaxsearch',null,
(select id from EG_MODULE where name = 'BRS'),6,null,false,'EGF',0,1,now(),1,now(),
(select id from eg_module  where name = 'EGF'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,
LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'bankStatementDownloadFile','/bankstatement/downloadDoc',null,
(select id from EG_MODULE where name = 'BRS'),6,null,false,'EGF',0,1,now(),1,now(),
(select id from eg_module  where name = 'EGF'));


insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'SYSTEM'),(select id from eg_action where name ='bankStatementUploadSearch' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bank Reconciler'),(select id from eg_action where name ='bankStatementUploadSearch' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'SYSTEM'),(select id from eg_action where name ='bankStatementUploadAjaxSearch' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bank Reconciler'),(select id from eg_action where name ='bankStatementUploadAjaxSearch' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'SYSTEM'),(select id from eg_action where name ='bankStatementDownloadFile' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bank Reconciler'),(select id from eg_action where name ='bankStatementDownloadFile' and contextroot = 'EGF'));

-------------Feature entry--------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Bank Statement File Upload','Bank Statement File Upload',(select id from eg_module  where name = 'BRS'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'bankStatementUploadSearch') ,(select id FROM eg_feature WHERE name = 'Bank Statement File Upload'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'bankStatementUploadAjaxSearch') ,(select id FROM eg_feature WHERE name = 'Bank Statement File Upload'));

INSERT INTO eg_feature_action (ACTION, FEATURE)
VALUES
((select id FROM eg_action  WHERE name = 'bankStatementDownloadFile') ,(select id FROM eg_feature WHERE name = 'Bank Statement File Upload'));

	
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name ='Bank Statement File Upload'));
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Bank Reconciler') ,(select id FROM eg_feature WHERE name ='Bank Statement File Upload'));


