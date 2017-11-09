INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CreateSewerageMonthlyRates','/masters/seweragerates',null,(select id from EG_MODULE where name = 'sewerageMasters'),1,'Create Monthly rates Master',true,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetSewerageMonthlyRate','/masters/getseweragerates',null,(select id from EG_MODULE where name = 'sewerageMasters'),1,'Get Sewerage Monthly Rates',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='CreateSewerageMonthlyRates' and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Property Administrator'), (select id from eg_action where name = 'CreateSewerageMonthlyRates'and contextroot = 'stms'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetSewerageMonthlyRate' and contextroot = 'stms'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Property Administrator'),(select id from eg_action where name ='GetSewerageMonthlyRate' and contextroot = 'stms'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ajaxExistingSewerageValidate','/masters/ajaxexistingseweragevalidate',null,(select id from EG_MODULE where name = 'sewerageMasters'),1,'Create Sewerage Monthly Rates',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='ajaxExistingSewerageValidate' and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Property Administrator'), (select id from eg_action where name = 'ajaxExistingSewerageValidate'and contextroot = 'stms'));

-------------------END-------------------

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('ajaxExistingSewerageValidate', 'GetSewerageMonthlyRate','CreateSewerageMonthlyRates') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Property Administrator'));
--rollback delete from eg_action where name in ('ajaxExistingSewerageValidate','GetSewerageMonthlyRate','CreateSewerageMonthlyRates') and contextroot = 'stms';
