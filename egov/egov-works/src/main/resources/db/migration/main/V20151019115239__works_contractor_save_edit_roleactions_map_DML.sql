-- INSERT contractor save action into EG_ACTION ---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSaveContractor','/masters/contractor-save.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),1,'WorksSaveContractor','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSaveContractor' and contextroot = 'egworks'));

-- ALTER data type of COULMN edit_enabled in egw_contractor --
ALTER TABLE egw_contractor ALTER COLUMN edit_enabled DROP DEFAULT;
ALTER TABLE egw_contractor ALTER COLUMN edit_enabled TYPE boolean
  USING CASE edit_enabled
    WHEN '1' THEN true
    ELSE '0' END;
ALTER TABLE egw_contractor ALTER COLUMN edit_enabled SET DEFAULT false;

-- INSERT contractor details into ACCOUNTDETAILTYPE---
Insert into ACCOUNTDETAILTYPE (ID,NAME,DESCRIPTION,TABLENAME,COLUMNNAME,ATTRIBUTENAME,NBROFLEVELS,ISACTIVE,CREATED,LASTMODIFIED,MODIFIEDBY,FULL_QUALIFIED_NAME) values (4,'contractor','contractor','egw_contractor','id','contractor_id',1,1,to_date('14-10-09','DD-MM-RR'),null,null,'org.egov.works.models.masters.Contractor');

-- INSERT contractor edit action into EG_ACTION---
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksEditContractor','/masters/contractor-edit.action',null,(select id from EG_MODULE where name = 'WorksContractorMaster'),1,'WorksEditContractor','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksEditContractor' and contextroot = 'egworks'));


