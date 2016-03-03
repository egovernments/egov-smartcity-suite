-- INSERT deposit code search action into EG_ACTION ---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksScheduleCategoryEdit','/masters/scheduleCategory-edit.action',null,(select id from EG_MODULE where name = 'WorksScheduleCategoryMaster'),1,'WorksScheduleCategoryMaster','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksScheduleCategoryEdit' and contextroot = 'egworks'));  

-- INSERT deposit code search action into EG_ACTION ---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'Search Deposit Code','/masters/depositCode-search.action',null,(select id from EG_MODULE where name = 'WorksDepositCodeMaster'),2,'Search Deposit Code','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Search Deposit Code' and contextroot = 'egworks'));

-- INSERT deposit code newform action into EG_ACTION ---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Generate Deposit Code','/masters/depositCode-newform.action',null,(select id from EG_MODULE where name = 'WorksDepositCodeMaster'),1,'Generate Deposit Code','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Generate Deposit Code' and contextroot = 'egworks'));

---INSERT DepositCode into ACCOUNTDETAILTYPE
Insert into ACCOUNTDETAILTYPE (ID,NAME,DESCRIPTION,TABLENAME,COLUMNNAME,ATTRIBUTENAME,NBROFLEVELS,ISACTIVE,CREATED,LASTMODIFIED,MODIFIEDBY,FULL_QUALIFIED_NAME) values
(NEXTVAL('seq_accountdetailtype'),'DEPOSITCODE','DEPOSITCODE','egw_depositcode','id','depositcode_id',1,1,
now(),null,null,'org.egov.works.models.masters.DepositCode');

ALTER TABLE egw_depositcode ALTER COLUMN isactive DROP DEFAULT;
ALTER TABLE egw_depositcode ALTER COLUMN isactive TYPE boolean USING CASE isactive WHEN '1' THEN true ELSE '0' END;
ALTER TABLE egw_depositcode ALTER COLUMN isactive SET DEFAULT true;


--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksScheduleCategoryEdit' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksScheduleCategoryEdit' and contextroot = 'egworks';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='Search Deposit Code' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'Search Deposit Code' and contextroot = 'egworks';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='Generate Deposit Code' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'Generate Deposit Code' and contextroot = 'egworks';

--rollback delete from ACCOUNTDETAILTYPE where NAME='DEPOSITCODE' and ATTRIBUTENAME='DEPOSITCODE'
--ALTER TABLE egw_depositcode MODIFY COLUMN isactive smallint;