drop table egpt_mv_propertyinfo;

CREATE TABLE egpt_mv_propertyinfo AS 
 SELECT bp.id AS basicpropertyid,
    bp.propertyid AS upicno,
    ownername(bp.id) AS ownersname,
    addr.housenobldgapt AS houseno,
    mobilenumber(bp.id) AS mobileno,
    addr.address,
    propdet.id_propertytypemaster AS proptymaster,
    bp.id_adm_bndry AS wardid,
    propid.zone_num AS zoneid,
    propid.adm3 AS streetid,
    propid.adm1 AS blockid,
    propid.adm2 AS localityid,
    1 AS source_id,
    propdet.sital_area,
    propdet.total_builtup_area,
    bp.status AS latest_status,
    dmdcoll.aggregate_current_demand,
    dmdcoll.aggregate_arrear_demand,
    dmdcoll.current_collection,
    dmdcoll.arrearcollection,
    bp.gis_ref_no AS gisrefno,
    prop.isexemptedfromtax AS isexempted,
    bp.source,
        CASE dmdcalc.alv
            WHEN NULL::double precision THEN 0::double precision
            ELSE dmdcalc.alv
        END AS alv
   FROM egpt_basic_property bp,
    egpt_mv_current_property prop,
    egpt_mv_bp_address addr,
    egpt_mv_current_prop_det propdet,
    egpt_propertyid propid,
    egpt_mv_current_arrear_dem_coll dmdcoll,
    egpt_mv_curr_dmdcalc dmdcalc
  WHERE bp.propertyid IS NOT NULL AND bp.addressid::numeric = addr.addressid AND prop.id_basic_property = bp.id AND propdet.id_property = prop.id_property 
  AND propid.id = bp.id_propertyid AND dmdcoll.basicpropertyid = bp.id AND dmdcalc.id_basic_property = bp.id;