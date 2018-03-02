DROP VIEW egtl_dcb_aggr_view;
CREATE OR REPLACE VIEW egtl_dcb_aggr_view AS 
 SELECT 
    row_number() OVER () AS id,
    lic.license_number AS licensenumber,
    lic.address AS licaddress,
    lic.id AS licenseid,
    licadd.applicant_name AS username,
    lic.id_parent_bndry AS wardid,
    lic.id_adm_bndry AS locality,
    currdd.amount AS currentdemand,
    currdd.amt_collected AS currentcollection,
    currdd.amount - currdd.amt_collected AS currentbalance,
    cr.reason_amount AS collectedamount,
    im.start_date AS installment,
    lic.is_active AS active,
    drm.reasonmaster as demandreason,
    fy.financialyear as financialyear
   FROM egtl_license lic
     JOIN egtl_licensee licadd ON licadd.id_license = lic.id
     JOIN egtl_mstr_status licst ON licst.id = lic.id_status
     LEFT JOIN eg_demand_details currdd ON currdd.id_demand = lic.id_demand
     LEFT JOIN eg_demand_reason dr ON dr.id = currdd.id_demand_reason
     LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
     LEFT JOIN egdm_collected_receipts cr ON cr.id_demand_detail = currdd.id
     LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
     LEFT JOIN eg_module m ON m.id = im.id_module
     LEFT JOIN financialyear fy on fy.startingdate = im.start_date
  WHERE drm.isdemand = true AND m.name::text = 'Trade License'::text
  ORDER BY lic.id;