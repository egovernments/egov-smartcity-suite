DROP VIEW egtl_mv_baseregister_view;
CREATE OR REPLACE VIEW egtl_mv_baseregister_view AS 
 SELECT mv.licensenumber,
    mv.tradetitle,
    mv.licenseid,
    mv.owner,
    mv.mobile,
    mv.cat,
    mv.categoryname,
    mv.subcat,
    mv.subcategoryname,
    mv.assessmentno,
    mv.ward,
    mv.wardname,
    mv.locality,
    mv.localityname,
    mv.tradeaddress,
    mv.commencementdate,
    mv.status,
    mv.statusname,
    sum(mv.arrearlicensefee) AS arrearlicensefee,
    sum(mv.arrearpenaltyfee) AS arrearpenaltyfee,
    sum(mv.curlicensefee) AS curlicensefee,
    sum(mv.curpenaltyfee) AS curpenaltyfee,
    mv.unitofmeasure,
    mv.uom,
    mv.tradewt,
    mv.apptype,
    mv.appdate,
    getfeematrixrate(mv.apptype, mv.cat, mv.subcat, mv.tradewt, mv.licenseid) AS rateval
   FROM ( SELECT row_number() OVER () AS id,lic.license_number AS licensenumber,
            lic.name_of_estab AS tradetitle,
            lic.id AS licenseid,
            licadd.applicant_name AS owner,
            licadd.mobile_phonenumber AS mobile,
            cat.id AS cat,
            cat.name AS categoryname,
            subcat.id AS subcat,
            subcat.name AS subcategoryname,
            lic.assessmentno,
            COALESCE(lic.id_parent_bndry, 0::bigint) AS ward,
            COALESCE(revward.name, 'NA'::character varying) AS wardname,
            lic.id_adm_bndry AS locality,
            bnd.name AS localityname,
            lic.address AS tradeaddress,
            lic.commencementdate,
            lic.id_status AS status,
            licstatus.status_name AS statusname,
            curdmd.id_installment AS dmd_installmentyear,
            COALESCE(0, 0) AS arrearlicensefee,
            COALESCE(0, 0) AS arrearpenaltyfee,
                CASE
                    WHEN drm.reasonmaster::text = 'License Fee'::text THEN curdd.amount - curdd.amt_collected
                    ELSE 0::double precision
                END AS curlicensefee,
                CASE
                    WHEN drm.reasonmaster::text = 'Penalty'::text THEN curdd.amount - curdd.amt_collected
                    ELSE 0::double precision
                END AS curpenaltyfee,
            uom.name AS unitofmeasure,
            uom.id AS uom,
            lic.trade_area_weight AS tradewt,
            lic.licenseapptype AS apptype,
            lic.appl_date AS appdate
           FROM egtl_license lic
             JOIN egtl_licensee licadd ON licadd.id_license = lic.id
             JOIN egtl_mstr_sub_category subcat ON subcat.id = lic.id_sub_category
             JOIN egtl_mstr_category cat ON cat.id = subcat.id_category
             JOIN egtl_mstr_status licstatus ON lic.id_status = licstatus.id
             JOIN eg_demand curdmd ON curdmd.id = lic.id_demand
             JOIN eg_boundary bnd ON lic.id_adm_bndry = bnd.id
             LEFT JOIN eg_boundary revward ON lic.id_parent_bndry = revward.id
             LEFT JOIN eg_demand_details curdd ON curdd.id_demand = curdmd.id
             LEFT JOIN eg_demand_reason dr ON dr.id = curdd.id_demand_reason
             LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
             LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
             LEFT JOIN eg_module m ON m.id = im.id_module
             LEFT JOIN egtl_subcategory_details scd ON subcat.id = scd.subcategory_id
             LEFT JOIN egtl_mstr_unitofmeasure uom ON scd.uom_id = uom.id
          WHERE drm.isdemand = true AND im.start_date = to_date('01/04/'::text || date_part('YEAR'::text, now()), 'DD/MM/YYYY'::text) AND m.name::text = 'Trade License'::text AND im.installment_type::text = 'Yearly'::text AND scd.feetype_id = (( SELECT egtl_mstr_fee_type.id
                   FROM egtl_mstr_fee_type
                  WHERE egtl_mstr_fee_type.name::text = 'License Fee'::text))
        UNION
         SELECT row_number() OVER () AS id,lic.license_number AS licensenumber,
            lic.name_of_estab AS tradetitle,
            lic.id AS licenseid,
            licadd.applicant_name AS username,
            licadd.mobile_phonenumber AS mobile,
            cat.id AS category,
            cat.name AS categoryname,
            subcat.id AS subcategory,
            subcat.name AS subcategoryname,
            lic.assessmentno,
            COALESCE(lic.id_parent_bndry, 0::bigint) AS ward,
            COALESCE(revward.name, 'NA'::character varying) AS wardname,
            lic.id_adm_bndry AS locality,
            bnd.name AS localityname,
            lic.address AS tradeaddress,
            lic.commencementdate,
            lic.id_status AS status,
            licstatus.status_name AS statusname,
            arrdmd.id_installment AS dmd_installmentyear,
                CASE
                    WHEN drm.reasonmaster::text = 'License Fee'::text THEN arrdd.amount - arrdd.amt_collected
                    ELSE 0::double precision
                END AS arrearlicensefee,
                CASE
                    WHEN drm.reasonmaster::text = 'Penalty'::text THEN arrdd.amount - arrdd.amt_collected
                    ELSE 0::double precision
                END AS arrearpenaltyfee,
            COALESCE(0, 0) AS currlicensefee,
            COALESCE(0, 0) AS currpenaltyfee,
            uom.name AS unitofmeasure,
            uom.id AS uom,
            lic.trade_area_weight AS tradewt,
            lic.licenseapptype AS apptype,
            lic.appl_date AS appdate
           FROM egtl_license lic
             JOIN egtl_licensee licadd ON licadd.id_license = lic.id
             JOIN egtl_mstr_sub_category subcat ON subcat.id = lic.id_sub_category
             JOIN egtl_mstr_category cat ON cat.id = subcat.id_category
             JOIN egtl_mstr_status licstatus ON lic.id_status = licstatus.id
             JOIN eg_demand arrdmd ON arrdmd.id = lic.id_demand
             JOIN eg_boundary bnd ON lic.id_adm_bndry = bnd.id
             LEFT JOIN eg_boundary revward ON lic.id_parent_bndry = revward.id
             LEFT JOIN eg_demand_details arrdd ON arrdd.id_demand = arrdmd.id
             LEFT JOIN eg_demand_reason dr ON dr.id = arrdd.id_demand_reason
             LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
             LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
             LEFT JOIN eg_module m ON m.id = im.id_module
             LEFT JOIN egtl_subcategory_details scd ON subcat.id = scd.subcategory_id
             LEFT JOIN egtl_mstr_unitofmeasure uom ON scd.uom_id = uom.id
          WHERE drm.isdemand = true AND im.start_date <> to_date('01/04/'::text || date_part('YEAR'::text, now()), 'DD/MM/YYYY'::text) AND m.name::text = 'Trade License'::text AND im.installment_type::text = 'Yearly'::text AND scd.feetype_id = (( SELECT egtl_mstr_fee_type.id
                   FROM egtl_mstr_fee_type
                  WHERE egtl_mstr_fee_type.name::text = 'License Fee'::text))) mv
  GROUP BY mv.licensenumber, mv.tradetitle, mv.licenseid, mv.owner, mv.mobile, mv.cat, mv.categoryname, mv.subcat, mv.subcategoryname, mv.assessmentno, mv.ward, mv.wardname, mv.locality, mv.localityname, mv.tradeaddress, mv.commencementdate, mv.status, mv.statusname, mv.unitofmeasure, mv.tradewt, mv.uom, mv.appdate, mv.apptype;
