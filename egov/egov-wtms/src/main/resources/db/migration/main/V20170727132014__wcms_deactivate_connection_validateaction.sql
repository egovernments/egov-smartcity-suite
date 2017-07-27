Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ValidateOriginalWaterConnectionNumber','/application/originalconnectionnumber-validate', null,(select id from EG_MODULE where name = 'WaterTaxTransactions'),null,'Validate Original Water Connection Number',false,'wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name = 'SYSTEM'),(select id FROM eg_action  WHERE NAME = 'ValidateOriginalWaterConnectionNumber' and CONTEXTROOT='wtms'));

--rollback delete from eg_roleaction where actionid = (select id from eg_action where name='ValidateOriginalWaterConnectionNumber') and roleid = (select id from eg_role where name = 'SYSTEM');

--rollback delete from eg_action where name = 'ValidateOriginalWaterConnectionNumber' and parentmodule=(select id from eg_module where name='WaterTaxTransactions');
