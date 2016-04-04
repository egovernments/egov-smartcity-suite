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
     JOIN egtl_license_demand licd ON licd.id_license=lic.id
     JOIN eg_demand currdmd ON currdmd.id = licd.id_demand
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
WHERE  licst.code::text = 'ACT'::text
AND drm.isdemand                = true
AND im.start_date              <= now()
AND im.end_date                >= now()
AND m.name::text                = 'Trade License'::text
AND im.installment_type::text   = 'Yearly'::text;


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled
,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'TLDCBReportList','/tlreports/dCBReportList',null,(select id from eg_module 
where name='Trade License Reports'),null,'DCB Report List','false','tl',0,1,to_timestamp('2015-08-19 17:14:12.454451','null')
,1,to_timestamp('2015-08-19 17:14:12.454451','null'),(select id from eg_module where name='Trade License'));


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot
,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
 values (nextval('SEQ_EG_ACTION'),'TradeLicenseDCBReportLocalityWise','/tlreports/dCBReport/licenseNumberWise',
 null,(select id from eg_module where name='Trade License Reports'),4,
 'DCB report By Trade','true','tl',0,1,to_timestamp('2015-08-24 11:11:30.249443','null')
 ,1,to_timestamp('2015-08-24 11:11:30.249443','null'),(select id from eg_module where name='Trade License'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='TLDCBReportList' and contextroot='tl' ));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),
(select id from eg_action where name='TradeLicenseDCBReportLocalityWise' and contextroot='tl' ));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='TLCreator'),
(select id from eg_action where name='TLDCBReportList' and contextroot='tl' ));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='TLCreator'),
(select id from eg_action where name='TradeLicenseDCBReportLocalityWise' and contextroot='tl' ));

