Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'digitalSignature-WaterTaxPreviewWorkflow','/digitalSignature/waterTax/previewSignedWorkOrderConnection', null,(select id from EG_MODULE where name = 'WaterTaxTransactions'),1,
null,'f','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/waterTax/previewSignedWorkOrderConnection'), (select id from eg_role where name in ('Property Administrator'));
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where url = '/digitalSignature/waterTax/previewSignedWorkOrderConnection'), (select id from eg_role where name in ('Property Verifier'));