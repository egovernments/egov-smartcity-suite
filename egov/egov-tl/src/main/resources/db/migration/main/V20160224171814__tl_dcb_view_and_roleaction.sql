--ajax action
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchAjax-PopulateData','/domain/commonTradeLicenseAjax-populateData.action',null,(select id from EG_MODULE where name = 'Trade License'),3,'SearchAjax-PopulateData','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchAjax-PopulateData'));

Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='SearchAjax-PopulateData'));

Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='SearchAjax-PopulateData'));

Insert into eg_roleaction values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='SearchAjax-PopulateData'));

Insert into eg_roleaction values((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='SearchAjax-PopulateData'));

--DCB Report
CREATE OR REPLACE VIEW egtl_mv_dcb_view AS 
SELECT lic.license_number AS licenseNumber,
  lic.address,
  lic.id AS licenseId,
  licAdd.applicant_name   AS username,
  --lic.id_parent_bndry.parent.id       AS zoneid,
 lic.id_parent_bndry       AS wardid,
  lic.id_adm_bndry   AS locality,
  currdd.amount                                          AS curr_demand,
  currdd.amt_collected                                   AS curr_coll,
  currdd.amount::DOUBLE PRECISION - currdd.amt_collected AS curr_balance,
  COALESCE(0, 0)                                         AS arr_demand,
  COALESCE(0, 0)                                         AS arr_coll,
  COALESCE(0, 0)                                         AS arr_balance

FROM egtl_license lic
     JOIN egtl_licensee licAdd  ON licAdd.id_license=lic.id
     JOIN egtl_mstr_status licst ON licst.id = lic.id_status
     JOIN eg_demand currdmd ON currdmd.id = lic.id_demand
     JOIN eg_boundary bnd ON lic.id_adm_bndry = bnd.id
     LEFT JOIN eg_demand_details currdd ON currdd.id_demand = currdmd.id
LEFT JOIN eg_demand_reason dr
ON dr.id = currdd.id_demand_reason
LEFT JOIN eg_demand_reason_master drm
ON drm.id = dr.id_demand_reason_master
LEFT JOIN eg_installment_master im
ON im.id = dr.id_installment
LEFT JOIN eg_module m
ON m.id                         = im.id_module
WHERE  lic.is_active=true
AND drm.isdemand                = true
AND im.start_date              <= now()
AND im.end_date                >= now()
AND m.name::text                = 'Trade License'::text
AND im.installment_type::text   = 'Yearly'::text;
