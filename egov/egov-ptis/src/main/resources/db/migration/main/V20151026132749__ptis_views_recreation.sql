DROP table egpt_mv_c10_report;
DROP table egpt_mv_c9_prop_coll_data;
DROP table egpt_mv_c8_unique_rcpt_dd;
DROP table egpt_mv_c7_rcpt_arrearlibrarycess_coll;
DROP table egpt_mv_c6_rcpt_arrearpenalty_coll;
DROP table egpt_mv_c5_rcpt_arreartax_coll;
DROP table egpt_mv_c4_rcpt_librarycess_coll;
DROP table egpt_mv_c3_rcpt_penalty_coll;
DROP table egpt_mv_c2_rcpt_tax_coll;
DROP table egpt_mv_c1_coll_receipts;
DROP table egpt_mv_propertyinfo;
DROP table egpt_mv_curr_dmdcalc;
DROP table egpt_mv_current_arrear_dem_coll;
DROP table egpt_mv_current_dem_coll;
DROP table EGPT_MV_CURRENT_FLOOR_DETAIL;
DROP table egpt_mv_current_prop_det;
DROP table egpt_mv_inst_dem_coll;
DROP table egpt_mv_arrear_dem_coll;
DROP table egpt_mv_bp_curr_demand;
DROP table egpt_mv_bp_inst_active_demand;
DROP table egpt_mv_current_property;

CREATE TABLE egpt_mv_c1_coll_receipts AS 
 SELECT cr.id_demand_detail,
    cr.receipt_number,
    cr.receipt_date,
    cr.reason_amount,
    reas.id_demand_reason_master AS reasmid,
    i.start_date AS instlsdate,
    i.end_date AS instedate
   FROM egdm_collected_receipts cr,
    eg_demand_details dd,
    eg_installment_master i,
    eg_demand_reason reas
  WHERE cr.id_demand_detail = dd.id AND reas.id_installment = i.id AND dd.id_demand_reason = reas.id;
  
CREATE TABLE egpt_mv_c2_rcpt_tax_coll AS 
 SELECT egpt_mv_c1_coll_receipts.receipt_number,
    sum(egpt_mv_c1_coll_receipts.reason_amount) AS tax_coll
   FROM egpt_mv_c1_coll_receipts
  WHERE egpt_mv_c1_coll_receipts.reasmid = (( SELECT eg_demand_reason_master.id
           FROM eg_demand_reason_master
          WHERE eg_demand_reason_master.reasonmaster::text = 'General Tax'::text AND eg_demand_reason_master.module = (( SELECT eg_module.id
                   FROM eg_module
                  WHERE eg_module.name::text = 'Property Tax'::text)))) AND egpt_mv_c1_coll_receipts.instlsdate >= (( SELECT financialyear.startingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)) AND egpt_mv_c1_coll_receipts.instlsdate <= (( SELECT financialyear.endingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate))
  GROUP BY egpt_mv_c1_coll_receipts.receipt_number;
  
CREATE TABLE egpt_mv_c3_rcpt_penalty_coll AS 
 SELECT egpt_mv_c1_coll_receipts.receipt_number,
    sum(egpt_mv_c1_coll_receipts.reason_amount) AS penalty_coll
   FROM egpt_mv_c1_coll_receipts
  WHERE egpt_mv_c1_coll_receipts.reasmid = (( SELECT eg_demand_reason_master.id
           FROM eg_demand_reason_master
          WHERE eg_demand_reason_master.reasonmaster::text = 'PENALTY_FINES'::text AND eg_demand_reason_master.module = (( SELECT eg_module.id
                   FROM eg_module
                  WHERE eg_module.name::text = 'Property Tax'::text)))) AND egpt_mv_c1_coll_receipts.instlsdate >= (( SELECT financialyear.startingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)) AND egpt_mv_c1_coll_receipts.instlsdate <= (( SELECT financialyear.endingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate))
  GROUP BY egpt_mv_c1_coll_receipts.receipt_number;
  
CREATE TABLE egpt_mv_c4_rcpt_librarycess_coll AS 
 SELECT egpt_mv_c1_coll_receipts.receipt_number,
    sum(egpt_mv_c1_coll_receipts.reason_amount) AS librarycess_coll
   FROM egpt_mv_c1_coll_receipts
  WHERE egpt_mv_c1_coll_receipts.reasmid = (( SELECT eg_demand_reason_master.id
           FROM eg_demand_reason_master
          WHERE eg_demand_reason_master.reasonmaster::text = 'Library Cess'::text AND eg_demand_reason_master.module = (( SELECT eg_module.id
                   FROM eg_module
                  WHERE eg_module.name::text = 'Property Tax'::text)))) AND egpt_mv_c1_coll_receipts.instlsdate >= (( SELECT financialyear.startingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)) AND egpt_mv_c1_coll_receipts.instlsdate <= (( SELECT financialyear.endingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate))
  GROUP BY egpt_mv_c1_coll_receipts.receipt_number;

 CREATE TABLE egpt_mv_c5_rcpt_arreartax_coll AS 
 SELECT egpt_mv_c1_coll_receipts.receipt_number,
    sum(egpt_mv_c1_coll_receipts.reason_amount) AS arreartax_coll
   FROM egpt_mv_c1_coll_receipts
  WHERE egpt_mv_c1_coll_receipts.reasmid = (( SELECT eg_demand_reason_master.id
           FROM eg_demand_reason_master
          WHERE eg_demand_reason_master.reasonmaster::text = 'General Tax'::text AND eg_demand_reason_master.module = (( SELECT eg_module.id
                   FROM eg_module
                  WHERE eg_module.name::text = 'Property Tax'::text)))) AND (egpt_mv_c1_coll_receipts.instlsdate < (( SELECT financialyear.startingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)) OR egpt_mv_c1_coll_receipts.instlsdate > (( SELECT financialyear.endingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)))
  GROUP BY egpt_mv_c1_coll_receipts.receipt_number;
  
CREATE TABLE egpt_mv_c6_rcpt_arrearpenalty_coll AS 
 SELECT egpt_mv_c1_coll_receipts.receipt_number,
    sum(egpt_mv_c1_coll_receipts.reason_amount) AS arrearpenalty_coll
   FROM egpt_mv_c1_coll_receipts
  WHERE egpt_mv_c1_coll_receipts.reasmid = (( SELECT eg_demand_reason_master.id
           FROM eg_demand_reason_master
          WHERE eg_demand_reason_master.reasonmaster::text = 'PENALTY_FINES'::text AND eg_demand_reason_master.module = (( SELECT eg_module.id
                   FROM eg_module
                  WHERE eg_module.name::text = 'Property Tax'::text)))) AND (egpt_mv_c1_coll_receipts.instlsdate < (( SELECT financialyear.startingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)) OR egpt_mv_c1_coll_receipts.instlsdate > (( SELECT financialyear.endingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)))
  GROUP BY egpt_mv_c1_coll_receipts.receipt_number;

CREATE TABLE egpt_mv_c7_rcpt_arrearlibrarycess_coll AS 
 SELECT egpt_mv_c1_coll_receipts.receipt_number,
    sum(egpt_mv_c1_coll_receipts.reason_amount) AS arrearlibrarycess_coll
   FROM egpt_mv_c1_coll_receipts
  WHERE egpt_mv_c1_coll_receipts.reasmid = (( SELECT eg_demand_reason_master.id
           FROM eg_demand_reason_master
          WHERE eg_demand_reason_master.reasonmaster::text = 'Library Cess'::text AND eg_demand_reason_master.module = (( SELECT eg_module.id
                   FROM eg_module
                  WHERE eg_module.name::text = 'Property Tax'::text)))) AND (egpt_mv_c1_coll_receipts.instlsdate < (( SELECT financialyear.startingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)) OR egpt_mv_c1_coll_receipts.instlsdate > (( SELECT financialyear.endingdate
           FROM financialyear
          WHERE now() >= financialyear.startingdate AND now() <= financialyear.endingdate)))
  GROUP BY egpt_mv_c1_coll_receipts.receipt_number;

CREATE TABLE egpt_mv_c8_unique_rcpt_dd AS 
 SELECT egpt_mv_c1_coll_receipts.receipt_number,
    egpt_mv_c1_coll_receipts.receipt_date,
    min(egpt_mv_c1_coll_receipts.id_demand_detail) AS id_dem_detail
   FROM egpt_mv_c1_coll_receipts
  GROUP BY egpt_mv_c1_coll_receipts.receipt_number, egpt_mv_c1_coll_receipts.receipt_date;
  
CREATE TABLE egpt_mv_c9_prop_coll_data AS 
 SELECT c8.receipt_number,
    c8.receipt_date,
    bp.propertyid,
    prop.id AS idproperty,
    pid.zone_num AS zoneid,
    pid.ward_adm_id AS wardid,
    pid.adm1 AS areaid,
    pid.adm2 AS localityid,
    pid.adm3 AS streetid,
    ch.payeename,
    ch.collectiontype,
    COALESCE(fit.type, 'cash'::character varying, 'cash'::character varying, 'bankchallan'::character varying, 'bankchallan'::character varying, 'card'::character varying, 'card'::character varying, 'online'::character varying, 'online'::character varying, 'atm'::character varying, 'atm'::character varying, 'banktobank'::character varying, 'banktobank'::character varying, 'cheque/dd'::character varying) AS payment_mode,
    u.username
   FROM egpt_mv_c8_unique_rcpt_dd c8,
    eg_demand_details dd,
    egpt_ptdemand ptd,
    egpt_property prop,
    egpt_basic_property bp,
    egpt_propertyid pid,
    egcl_collectionheader ch,
    egcl_collectioninstrument ci,
    egf_instrumentheader fih,
    egf_instrumenttype fit,
    eg_user u
  WHERE c8.id_dem_detail = dd.id AND dd.id_demand = ptd.id_demand AND ptd.id_property = prop.id AND prop.id_basic_property = bp.id AND bp.id_propertyid = pid.id AND (c8.receipt_number::text = ch.receiptnumber::text OR c8.receipt_number::text = ch.manualreceiptnumber::text) AND ch.id = ci.collectionheader AND ci.instrumentheader = fih.id AND fih.instrumenttype = fit.id AND ch.createdby = u.id;
  
CREATE TABLE egpt_mv_c10_report AS 
 SELECT c9.receipt_number,
    c9.receipt_date,
    c9.propertyid,
    c9.idproperty,
    c9.zoneid,
    c9.wardid,
    c9.areaid,
    c9.localityid,
    c9.streetid,
    c9.payeename,
    c9.collectiontype,
    c9.payment_mode,
    c9.username,
    c2.tax_coll,
    c3.penalty_coll,
    c4.librarycess_coll,
    c5.arreartax_coll,
    c6.arrearpenalty_coll,
    c7.arrearlibrarycess_coll
   FROM egpt_mv_c9_prop_coll_data c9
     LEFT JOIN egpt_mv_c3_rcpt_penalty_coll c3 ON c9.receipt_number::text = c3.receipt_number::text
     LEFT JOIN egpt_mv_c5_rcpt_arreartax_coll c5 ON c9.receipt_number::text = c5.receipt_number::text
     LEFT JOIN egpt_mv_c6_rcpt_arrearpenalty_coll c6 ON c9.receipt_number::text = c6.receipt_number::text
     LEFT JOIN egpt_mv_c7_rcpt_arrearlibrarycess_coll c7 ON c9.receipt_number::text = c7.receipt_number::text,
    egpt_mv_c2_rcpt_tax_coll c2,
    egpt_mv_c4_rcpt_librarycess_coll c4
  WHERE c9.receipt_number::text = c2.receipt_number::text OR c9.receipt_number::text = c4.receipt_number::text;

 CREATE TABLE egpt_mv_current_property AS 
 SELECT bp.id AS id_basic_property,
    prop.id AS id_property,
    prop.id_installment,
    prop.status,
    prop.isexemptedfromtax
   FROM egpt_basic_property bp,
    egpt_property prop
  WHERE prop.id_basic_property = bp.id AND prop.is_default_property::text = 'Y'::text AND (prop.status::text = ANY (ARRAY['A'::character varying, 'I'::character varying, 'O'::character varying]::text[])) AND bp.isactive = true;
  
CREATE TABLE egpt_mv_current_prop_det AS 
 SELECT prop.id_property,
    propdet.id AS id_property_detail,
    propdet.id_propertytypemaster,
    propdet.id_usg_mstr,
    propdet.sital_area,
    propdet.total_builtup_area
   FROM egpt_mv_current_property prop,
    egpt_property_detail propdet
  WHERE prop.id_property = propdet.id_property;

CREATE TABLE egpt_mv_bp_inst_active_demand AS 
 SELECT bp.id AS id_basic_property,
    dem.id_installment,
    dem.id AS id_demand
   FROM egpt_basic_property bp,
    egpt_mv_current_property prop,
    eg_demand dem,
    egpt_ptdemand ptdem
  WHERE prop.id_basic_property = bp.id AND prop.id_installment = dem.id_installment AND dem.is_history = 'N'::bpchar AND ptdem.id_demand = dem.id AND ptdem.id_property = prop.id_property;
  
CREATE TABLE egpt_mv_bp_curr_demand AS 
 SELECT bp.id AS id_basic_property,
    admd.id_demand
   FROM egpt_mv_bp_inst_active_demand admd,
    egpt_basic_property bp
  WHERE admd.id_basic_property = bp.id AND admd.id_installment = (( SELECT admd.id_installment
           FROM eg_installment_master
          WHERE eg_installment_master.start_date <= now() AND eg_installment_master.end_date >= now() AND eg_installment_master.id_module = (( SELECT eg_module.id
                   FROM eg_module
                  WHERE eg_module.name::text = 'Property Tax'::text))));

CREATE TABLE egpt_mv_current_dem_coll AS 
 SELECT dem.id_basic_property,
    sum(det.amount) AS amt_demand,
    sum(det.amt_collected) AS amt_collected
   FROM egpt_mv_bp_curr_demand dem
     LEFT JOIN eg_demand_details det ON det.id_demand = dem.id_demand AND (det.id_demand_reason IN ( SELECT demand_reason.id
           FROM eg_demand_reason demand_reason
          WHERE NOT (demand_reason.id_demand_reason_master IN ( SELECT eg_demand_reason_master.id
                   FROM eg_demand_reason_master
                  WHERE (eg_demand_reason_master.code::text = ANY (ARRAY['CHQ_BUNC_PENALTY'::character varying, 'PENALTY_FINES'::character varying]::text[])) AND eg_demand_reason_master.module = (( SELECT eg_module.id
                           FROM eg_module
                          WHERE eg_module.name::text = 'Property Tax'::text)))) AND demand_reason.id_installment = (( SELECT demand_reason.id_installment
                   FROM eg_installment_master
                  WHERE eg_installment_master.start_date <= now() AND eg_installment_master.end_date >= now() AND eg_installment_master.id_module = (( SELECT eg_module.id
                           FROM eg_module
                          WHERE eg_module.name::text = 'Property Tax'::text))))))
  GROUP BY dem.id_basic_property;

CREATE TABLE egpt_mv_arrear_dem_coll AS 
 SELECT dem.id_basic_property,
    sum(det.amount) AS amt_demand,
    sum(det.amt_collected) AS amt_collected
   FROM egpt_mv_bp_curr_demand dem
     LEFT JOIN eg_demand_details det ON det.id_demand = dem.id_demand AND (det.id_demand_reason IN ( SELECT demand_reason.id
           FROM eg_demand_reason demand_reason
          WHERE NOT (demand_reason.id_demand_reason_master IN ( SELECT eg_demand_reason_master.id
                   FROM eg_demand_reason_master
                  WHERE (eg_demand_reason_master.code::text = ANY (ARRAY['CHQ_BUNC_PENALTY'::character varying, 'PENALTY_FINES'::character varying]::text[])) AND eg_demand_reason_master.module = (( SELECT eg_module.id
                           FROM eg_module
                          WHERE eg_module.name::text = 'Property Tax'::text)))) AND demand_reason.id_installment <> (( SELECT demand_reason.id_installment
                   FROM eg_installment_master
                  WHERE eg_installment_master.start_date <= now() AND eg_installment_master.end_date >= now() AND eg_installment_master.id_module = (( SELECT eg_module.id
                           FROM eg_module
                          WHERE eg_module.name::text = 'Property Tax'::text))))))
  GROUP BY dem.id_basic_property;
  
CREATE TABLE egpt_mv_current_arrear_dem_coll AS 
 SELECT currnt.id_basic_property AS basicpropertyid,
    COALESCE(currnt.amt_demand, 0::numeric) AS aggregate_current_demand,
    COALESCE(currnt.amt_collected, 0::double precision) AS current_collection,
    COALESCE(arrear.amt_demand, 0::numeric) AS aggregate_arrear_demand,
    COALESCE(arrear.amt_collected, 0::double precision) AS arrearcollection
   FROM egpt_mv_current_dem_coll currnt,
    egpt_mv_arrear_dem_coll arrear
  WHERE currnt.id_basic_property = arrear.id_basic_property;

CREATE TABLE egpt_mv_curr_dmdcalc AS 
 SELECT dmdcal.id AS dmdcalid,
    dmdcal.alv,
    currdmd.id_demand AS dmdid,
    currdmd.id_basic_property
   FROM egpt_mv_bp_curr_demand currdmd,
    egpt_demandcalculations dmdcal
  WHERE currdmd.id_demand = dmdcal.id_demand;
  
CREATE TABLE egpt_mv_propertyinfo AS 
 SELECT bp.id AS basicpropertyid,
    bp.propertyid AS upicno,
    ownername(bp.id) AS ownersname,
    addr.housenobldgapt AS houseno,
    mobilenumber(bp.id) AS mobileno,
    addr.address,
    propdet.id_propertytypemaster AS proptymaster,
    floor.id_usg_mstr AS prop_usage_master,
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
    egpt_mv_curr_dmdcalc dmdcalc,
    egpt_floor_detail floor
  WHERE bp.propertyid IS NOT NULL AND bp.addressid::numeric = addr.addressid AND prop.id_basic_property = bp.id AND propdet.id_property = prop.id_property 
  AND propid.id = bp.id_propertyid AND dmdcoll.basicpropertyid = bp.id AND dmdcalc.id_basic_property = bp.id
  AND floor.id_property_detail = propdet.id_property_detail;

CREATE TABLE EGPT_MV_CURRENT_FLOOR_DETAIL AS
 SELECT
    basicproperty.id AS basicPropertyId,
    currpropdet.id_property AS propertyId,
    usage.usg_name AS natureOfUsage,
    propdet.id_propertytypemaster AS propertyType,
    floordet.floor_no AS floorNo,
    floordet.builtup_area AS plinthArea,
    propdet.sital_area AS builtUpArea,
    strut.constr_type AS classification, 
    floordet.id_occpn_mstr AS occupation
  FROM egpt_mv_current_prop_det currpropdet,
    egpt_property property,
    egpt_basic_property basicproperty,
    egpt_property_detail propdet,
    egpt_property_usage_master usage,
    egpt_floor_detail floordet,
    egpt_struc_cl strut
  WHERE currpropdet.id_property   = propdet.id_property
    AND basicproperty.id = property.id_basic_property
    AND currpropdet.id_property = property.id
    AND propdet.id = floordet.id_property_detail
    AND floordet.id_struc_cl = strut.id
    AND floordet.id_usg_mstr = usage.id;
    
CREATE TABLE egpt_mv_inst_dem_coll AS 
 SELECT dmndcoldtls.id_basic_property,
    dmndcoldtls.id_installment,
    max(dmndcoldtls.createddate) AS createddate,
    COALESCE(max(dmndcoldtls.generaltax), NULL::bigint, 0::bigint, max(dmndcoldtls.generaltax)) AS generaltax,
    COALESCE(max(dmndcoldtls.libcesstax), NULL::bigint, 0::bigint, max(dmndcoldtls.libcesstax)) AS libcesstax,
    COALESCE(max(dmndcoldtls.educesstax), NULL::bigint, 0::bigint, max(dmndcoldtls.educesstax)) AS educesstax,
    COALESCE(max(dmndcoldtls.unauthpenaltytax), NULL::bigint, 0::bigint, max(dmndcoldtls.unauthpenaltytax)) AS unauthpenaltytax,
    COALESCE(max(dmndcoldtls.penaltyfinestax), NULL::bigint, 0::bigint, max(dmndcoldtls.penaltyfinestax)) AS penaltyfinestax,
    COALESCE(max(dmndcoldtls.sewtax), NULL::bigint, 0::bigint, max(dmndcoldtls.sewtax)) AS sewtax,
    COALESCE(max(dmndcoldtls.vacantlandtax), NULL::bigint, 0::bigint, max(dmndcoldtls.vacantlandtax)) AS vacantlandtax,
    COALESCE(max(dmndcoldtls.pubserchrgtax), NULL::bigint, 0::bigint, max(dmndcoldtls.pubserchrgtax)) AS pubserchrgtax,
    COALESCE(max(dmndcoldtls.generaltaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.generaltaxcoll)) AS generaltaxcoll,
    COALESCE(max(dmndcoldtls.libcesstaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.libcesstaxcoll)) AS libcesstaxcoll,
    COALESCE(max(dmndcoldtls.educesstaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.educesstaxcoll)) AS educesstaxcoll,
    COALESCE(max(dmndcoldtls.unauthpenaltytaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.unauthpenaltytaxcoll)) AS unauthpenaltytaxcoll,
    COALESCE(max(dmndcoldtls.penaltyfinestaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.penaltyfinestaxcoll)) AS penaltyfinestaxcoll,
    COALESCE(max(dmndcoldtls.sewtaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.sewtaxcoll)) AS sewtaxcoll,
    COALESCE(max(dmndcoldtls.vacantlandtaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.vacantlandtaxcoll)) AS vacantlandtaxcoll,
    COALESCE(max(dmndcoldtls.pubserchrgtaxcoll), NULL::double precision, 0::double precision, max(dmndcoldtls.pubserchrgtaxcoll)) AS pubserchrgtaxcoll
   FROM ( SELECT currdem.id_basic_property,
            dr.id_installment,
            det.create_date AS createddate,
                CASE drm.code
                    WHEN 'GEN_TAX'::text THEN det.amount
                    ELSE NULL::bigint
                END AS generaltax,
                CASE drm.code
                    WHEN 'LIB_CESS'::text THEN det.amount
                    ELSE NULL::bigint
                END AS libcesstax,
                CASE drm.code
                    WHEN 'EDU_CESS'::text THEN det.amount
                    ELSE NULL::bigint
                END AS educesstax,
                CASE drm.code
                    WHEN 'UNAUTH_PENALTY'::text THEN det.amount
                    ELSE NULL::bigint
                END AS unauthpenaltytax,
                CASE drm.code
                    WHEN 'PENALTY_FINES'::text THEN det.amount
                    ELSE NULL::bigint
                END AS penaltyfinestax,
                CASE drm.code
                    WHEN 'SEW_TAX'::text THEN det.amount
                    ELSE NULL::bigint
                END AS sewtax,
                CASE drm.code
                    WHEN 'VAC_LAND_TAX'::text THEN det.amount
                    ELSE NULL::bigint
                END AS vacantlandtax,
                CASE drm.code
                    WHEN 'PUB_SER_CHRG'::text THEN det.amount
                    ELSE NULL::bigint
                END AS pubserchrgtax,
                CASE drm.code
                    WHEN 'GEN_TAX'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS generaltaxcoll,
                CASE drm.code
                    WHEN 'LIB_CESS'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS libcesstaxcoll,
                CASE drm.code
                    WHEN 'EDU_CESS'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS educesstaxcoll,
                CASE drm.code
                    WHEN 'UNAUTH_PENALTY'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS unauthpenaltytaxcoll,
                CASE drm.code
                    WHEN 'PENALTY_FINES'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS penaltyfinestaxcoll,
                CASE drm.code
                    WHEN 'SEW_TAX'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS sewtaxcoll,
                CASE drm.code
                    WHEN 'VAC_LAND_TAX'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS vacantlandtaxcoll,
                CASE drm.code
                    WHEN 'PUB_SER_CHRG'::text THEN det.amt_collected
                    ELSE NULL::double precision
                END AS pubserchrgtaxcoll
           FROM egpt_mv_bp_curr_demand currdem,
            eg_demand_details det,
            eg_demand_reason dr,
            eg_demand_reason_master drm
          WHERE det.id_demand = currdem.id_demand AND det.id_demand_reason = dr.id AND dr.id_demand_reason_master = drm.id) dmndcoldtls
  GROUP BY dmndcoldtls.id_basic_property, dmndcoldtls.id_installment;