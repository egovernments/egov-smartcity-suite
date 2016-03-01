Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'digitalSignature-TLTransitionWorkflow','/digitalSignature/tradeLicense/transitionWorkflow',
 null,(select id from EG_MODULE where name = 'Trade License'),1,
null,'false','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/tradeLicense/transitionWorkflow'), (select id from eg_role where name in ('TLApprover'));




Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'digitalSignature-TLDownloadSignDoc','/digitalSignature/tradeLicense/downloadSignedLicenseCertificate',
 null,(select id from EG_MODULE where name = 'Trade License'),1,
null,'false','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/tradeLicense/downloadSignedLicenseCertificate'),
 (select id from eg_role where name in ('TLApprover'));

 
 
INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 
'Create License:Collection Done', NULL, NULL, 'Commissioner', NULL,
 'Create License:generate Certificate', 'Digital Signature Pending', 'Commissioner', 'Collction Done', 
 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix VALUES (nextval('EG_WF_MATRIX_SEQ'), 'ANY', 'TradeLicense', 
'Renewal License:Collection Done', NULL, NULL, 'Commissioner', 'RENEWALTRADE',
 'Renewal License:generate Certificate', 'Renewal-Digital Signature Pending', 'Commissioner', 'Collction Done', 
 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01');


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, MODULE ) VALUES ( nextval('SEQ_EG_APPCONFIG'), 'DIGITALSIGNINCLUDEINWORKFLOW','Digital signature is enabled or not in Trade License',(select id from eg_module where name='Trade License'));



INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
 (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='DIGITALSIGNINCLUDEINWORKFLOW' AND   MODULE =(select id from eg_module where name='Trade License')),current_date,'NO',0);

 
 alter table egtl_license add column filestoreid bigint;
 