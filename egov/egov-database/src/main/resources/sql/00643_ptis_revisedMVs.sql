update egpt_basic_property set source='A' where source='N';
alter table egpt_basic_property alter column source set default 'A';

------------ function OWNERNAME ------------------
DROP VIEW egpt_mv_propertyInfo;
DROP VIEW egpt_mv_curr_dmdcalc;
DROP VIEW egpt_mv_current_arrear_dem_coll;
DROP VIEW egpt_mv_current_prop_det;
DROP VIEW EGWTR_MV_DCB_VIEW ;
DROP VIEW egpt_mv_bp_address;
DROP VIEW egpt_mv_inst_dem_coll;
DROP VIEW egpt_mv_current_dem_coll;
DROP VIEW egpt_mv_arrear_dem_coll;   
DROP VIEW egpt_mv_bp_curr_demand;
DROP VIEW egpt_mv_bp_inst_active_demand;
DROP VIEW egpt_mv_current_property;
DROP FUNCTION OWNERNAME(BIGINT);

create or replace FUNCTION OWNERNAME(v_basicpropid IN BIGINT)  
RETURNS VARCHAR  as  $$
declare
	v_ownernames VARCHAR(3072);  
	owners eg_user%ROWTYPE;
BEGIN  
	for owners in (select u.* from egpt_property_owner_info po, eg_user u where po.owner=u.id and po.basicproperty = v_basicpropid)
	loop 
		begin
			IF v_ownernames <> '' THEN
			v_ownernames := v_ownernames || ',' || owners.name;
			ELSE
			v_ownernames := owners.name;
			END IF;
		EXCEPTION
		WHEN NO_DATA_FOUND THEN
		NULL;   
		END;
	END LOOP;
	return V_ownernames;   
END; 
$$ LANGUAGE plpgsql;

------------ view egpt_mv_current_property ------------------ 
CREATE OR REPLACE VIEW egpt_mv_current_property AS
SELECT bp.id as id_basic_property,
    prop.id as id_property, prop.id_installment, prop.status, prop.isexemptedfromtax
FROM egpt_basic_property bp,
  egpt_property prop
WHERE prop.id_basic_property = bp.id
AND prop.is_default_property = 'Y'
AND prop.status in ('A', 'I', 'O') --taking Active or Inactive property, Inactive property is assessed or re-assessed it will become active after 15 days.
AND bp.isactive = 'Y';

------------ view egpt_mv_bp_inst_active_demand ------------------ 
CREATE OR REPLACE VIEW egpt_mv_bp_inst_active_demand AS
SELECT bp.id as id_basic_property,
       dem.id_installment,
	   dem.id id_demand
FROM egpt_basic_property bp,
  egpt_mv_current_property prop,
  eg_demand dem,
  egpt_ptdemand ptdem
WHERE prop.id_basic_property = bp.id
AND dem.is_history     = 'N'
AND ptdem.id_demand    = dem.id
AND ptdem.id_property  = prop.id_property;

------------ view egpt_mv_bp_curr_demand ------------------ 
CREATE OR REPLACE VIEW  egpt_mv_bp_curr_demand AS 
SELECT bp.ID AS id_basic_property,
	   admd.id_demand
FROM egpt_mv_bp_inst_active_demand admd, egpt_basic_property bp  
WHERE admd.id_basic_property = bp.id
and admd.id_installment = (SELECT id_installment
	          FROM eg_installment_master
              WHERE start_date <= now()
              AND end_date     >= now()
              AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'));
              
------------ view  egpt_mv_arrear_dem_coll ------------------  
CREATE OR REPLACE VIEW egpt_mv_arrear_dem_coll AS
SELECT dem.id_basic_property, 
    SUM(det.amount) AS amt_demand, 
    SUM(det.amt_collected) AS amt_collected
FROM 
    egpt_mv_bp_curr_demand dem LEFT OUTER JOIN eg_demand_details det ON (
        det.id_demand = dem.id_demand
        AND det.id_demand_reason IN (SELECT id FROM eg_demand_reason demand_reason 
                                    WHERE id_demand_reason_master not in (select id from eg_demand_reason_master where code in ('CHQ_BUNC_PENALTY', 'PENALTY_FINES') and module=(SELECT id FROM eg_module WHERE name='Property Tax'))
									and	 id_installment <> (SELECT id_installment
                                                            FROM eg_installment_master
                                                            WHERE start_date <= now()
                                                            AND end_date     >= now()
                                                            AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'))))
GROUP BY dem.id_basic_property;


------------ view egpt_mv_current_dem_coll ------------------
CREATE OR REPLACE VIEW egpt_mv_current_dem_coll AS
SELECT dem.id_basic_property, 
    SUM(det.amount) AS amt_demand, 
    SUM(det.amt_collected) AS amt_collected
FROM 
    egpt_mv_bp_curr_demand dem LEFT OUTER JOIN eg_demand_details det ON (
        det.id_demand = dem.id_demand
        AND det.id_demand_reason IN (SELECT id FROM eg_demand_reason demand_reason 
                                    WHERE id_demand_reason_master not in (select id from eg_demand_reason_master where code in ('CHQ_BUNC_PENALTY', 'PENALTY_FINES') and module=(SELECT id FROM eg_module WHERE name='Property Tax'))
									and	 id_installment = (SELECT id_installment
                                                            FROM eg_installment_master
                                                            WHERE start_date <= now()
                                                            AND end_date     >= now()
                                                            AND id_module     = (SELECT id FROM eg_module WHERE name='Property Tax'))))
GROUP BY dem.id_basic_property;

------------ view  egpt_mv_bp_address ------------------
CREATE OR REPLACE VIEW egpt_mv_bp_address AS
SELECT bp.id as id_basic_property,
    addr.id as addressid,
    addr.housenobldgapt,
    CASE WHEN addr.housenobldgapt IS NOT NULL THEN addr.housenobldgapt ELSE '' END ||
        CASE WHEN addr.streetroadline IS NOT NULL THEN ', ' || addr.streetroadline ELSE '' END ||
        CASE WHEN addr.arealocalitysector IS NOT NULL THEN ', ' || addr.arealocalitysector ELSE '' END ||
        CASE WHEN addr.landmark IS NOT NULL THEN ', ' || addr.landmark ELSE '' END ||
        CASE WHEN addr.citytownvillage IS NOT NULL THEN ', ' || addr.citytownvillage ELSE '' END ||
        CASE WHEN addr.pincode IS NOT NULL THEN ' ' || addr.pincode ELSE '' END AS address
FROM 
    egpt_basic_property bp ,eg_address addr
	where bp.isactive='Y'
	and bp.addressid = addr.id;

------------ view egpt_mv_current_prop_det ------------------
CREATE OR REPLACE VIEW egpt_mv_current_prop_det AS
SELECT prop.id_property,
	propdet.id as id_property_detail,
	propdet.id_propertytypemaster,
	propdet.id_usg_mstr,
	propdet.sital_area,
	propdet.total_builtup_area
FROM egpt_mv_current_property prop,
	egpt_property_detail propdet
WHERE
	prop.id_property = propdet.id_property;


------------ view egpt_mv_dem_coll ------------------
CREATE OR REPLACE VIEW egpt_mv_current_arrear_dem_coll AS
SELECT currnt.id_basic_property            AS basicpropertyid,
    coalesce (currnt.amt_demand, 0)      AS aggregate_current_demand,
    coalesce (currnt.amt_collected, 0) AS current_collection,
    coalesce (arrear.amt_demand, 0)       AS aggregate_arrear_demand,
    coalesce (arrear.amt_collected, 0)  AS arrearcollection
FROM egpt_mv_current_dem_coll currnt,
    egpt_mv_arrear_dem_coll arrear
WHERE currnt.id_basic_property = arrear.id_basic_property;


------------ view egpt_mv_curr_dmdcalc ------------------
CREATE OR REPLACE VIEW egpt_mv_curr_dmdcalc AS
select dmdCal.id dmdCalId, 
      dmdcal.alv, 
      currDmd.id_demand dmdId, 
      currdmd.id_basic_property   
from egpt_mv_bp_curr_demand currDmd, 
    egpt_demandcalculations dmdCal 
where 
    currDmd.id_demand = dmdcal.id_demand;


------------ view egpt_mv_propertyInfo ------------------
CREATE OR REPLACE VIEW egpt_mv_propertyInfo AS
SELECT bp.id  							AS basicpropertyid,
    bp.propertyid                		AS upicno, 
    ownername (bp.id) 					AS ownersname,
    addr.housenobldgapt 				AS houseno,
    addr.address                        AS address,
    propdet.id_propertytypemaster       AS proptymaster,
    propdet.id_usg_mstr                 AS prop_usage_master,
    bp.id_adm_bndry                     AS wardid,
    propid.ZONE_NUM                     AS zoneid,
    propid.adm3                         AS streetid,
    propid.adm1				 			AS blockid,
    propid.adm2				 			AS localityid,
    1                                   AS source_id,
    propdet.sital_area                  AS sital_area,
    propdet.total_builtup_area          AS total_builtup_area,
    bp.status                           AS latest_status,
    dmdColl.aggregate_current_demand    AS aggregate_current_demand,
    dmdColl.aggregate_arrear_demand     AS aggregate_arrear_demand,
    dmdColl.current_collection          AS current_collection,
    dmdColl.arrearcollection            AS arrearcollection,
	bp.gis_ref_no						As gisRefNo,
	prop.isexemptedfromtax				As isexempted,
	bp.source							As source,
    case dmdcalc.alv
      when null then 0  
      else dmdcalc.alv 
     end  as ALV
  FROM egpt_basic_property bp,
    egpt_mv_current_property prop,
    egpt_mv_bp_address addr,
    egpt_mv_current_prop_det propdet,
    egpt_propertyid  propid,
    egpt_mv_current_arrear_dem_coll dmdColl,  
    egpt_mv_curr_dmdcalc dmdcalc 
  WHERE bp.propertyid is not null
  AND bp.addressid         = addr.addressid
  AND prop.id_basic_property = bp.id
  AND propdet.id_property      = prop.id_property
  AND propid.id                = bp.id_propertyid
  AND dmdColl.basicpropertyid   = bp.id
  AND dmdcalc.id_basic_property = bp.id;

------------ view egpt_mv_inst_dem_coll ------------------
CREATE or replace VIEW egpt_mv_inst_dem_coll
  AS
  SELECT id_basic_property, id_installment, max(CreatedDate) CreatedDate, 
  COALESCE(max(GeneralTax), null, 0, max(GeneralTax)) GeneralTax, 
  COALESCE(max(LibCessTax), null, 0, max(LibCessTax)) LibCessTax, 
  COALESCE(max(EduCessTax), null, 0, max(EduCessTax)) EduCessTax, 
  COALESCE(max(UnauthPenaltyTax) , null, 0, max(UnauthPenaltyTax)) UnauthPenaltyTax, 
  COALESCE(max(PenaltyFinesTax), null, 0, max(PenaltyFinesTax)) PenaltyFinesTax,  
  COALESCE(max(SewTax), null, 0, max(SewTax)) SewTax, 
  COALESCE(max(VacantLandTax), null, 0, max(VacantLandTax)) VacantLandTax, 
  COALESCE(max(PubSerChrgTax), null, 0, max(PubSerChrgTax)) PubSerChrgTax,
  COALESCE(max(GeneralTaxColl), null, 0, max(GeneralTaxColl)) GeneralTaxColl, 
  COALESCE(max(LibCessTaxColl), null, 0, max(LibCessTaxColl)) LibCessTaxColl, 
  COALESCE(max(EduCessTaxColl), null, 0, max(EduCessTaxColl)) EduCessTaxColl, 
  COALESCE(max(UnauthPenaltyTaxColl) , null, 0, max(UnauthPenaltyTaxColl)) UnauthPenaltyTaxColl, 
  COALESCE(max(PenaltyFinesTaxColl), null, 0, max(PenaltyFinesTaxColl)) PenaltyFinesTaxColl,  
  COALESCE(max(SewTaxColl), null, 0, max(SewTaxColl)) SewTaxColl, 
  COALESCE(max(VacantLandTaxColl), null, 0, max(VacantLandTaxColl)) VacantLandTaxColl, 
  COALESCE(max(PubSerChrgTaxColl), null, 0, max(PubSerChrgTaxColl)) PubSerChrgTaxColl
  FROM (
  SELECT currDem.id_basic_property id_basic_property,
    dr.id_installment id_installment,
   det.create_date CreatedDate,
    (case drm.code when 'GEN_TAX' then det.amount else null end) GeneralTax,
    (case drm.code when 'LIB_CESS' then det.amount else null end) LibCessTax,
    (case drm.code when 'EDU_CESS' then det.amount else null end) EduCessTax,
    (case drm.code when 'UNAUTH_PENALTY' then det.amount else null end) UnauthPenaltyTax,
    (case drm.code when 'PENALTY_FINES' then det.amount else null end) PenaltyFinesTax,
    (case drm.code when 'SEW_TAX' then det.amount else null end) SewTax,
    (case drm.code when 'VAC_LAND_TAX' then det.amount else null end) VacantLandTax,
    (case drm.code when 'PUB_SER_CHRG' then det.amount else null end) PubSerChrgTax,
    (case drm.code when 'GEN_TAX' then det.amt_collected else null end) GeneralTaxColl,
    (case drm.code when 'LIB_CESS' then det.amt_collected else null end) LibCessTaxColl,
    (case drm.code when 'EDU_CESS' then det.amt_collected else null end) EduCessTaxColl,
    (case drm.code when 'UNAUTH_PENALTY' then det.amt_collected else null end) UnauthPenaltyTaxColl,
    (case drm.code when 'PENALTY_FINES' then det.amt_collected else null end) PenaltyFinesTaxColl,
    (case drm.code when 'SEW_TAX' then det.amt_collected else null end) SewTaxColl,
    (case drm.code when 'VAC_LAND_TAX' then det.amt_collected else null end) VacantLandTaxColl,
    (case drm.code when 'PUB_SER_CHRG' then det.amt_collected else null end) PubSerChrgTaxColl
  FROM EGPT_MV_BP_CURR_DEMAND currDem,
    eg_demand_details det,
    eg_demand_reason dr,
    eg_demand_reason_master drm
  WHERE det.id_demand            = currDem.id_demand
  AND det.id_demand_reason       = dr.id
  AND dr.id_demand_reason_master = drm.id) as dmndColDtls
  GROUP BY id_basic_property,
    id_installment;

--water tax dcb mv
CREATE VIEW EGWTR_MV_DCB_VIEW AS (
                                    (SELECT bp.propertyid,
                                            bpaddr.address AS address,
                                            con.consumercode AS hscno,
                                            ownername(bp.id) AS username,
                                            propid.zone_num AS zoneid,
                                            propid.WARD_ADM_ID AS wardid,
                                            propid.ADM1 AS block,
                                            propid.ADM2 AS locality,
                                            propid.ADM3 AS street,
                                            cd.connectiontype AS connectiontype,
                                            currdd.amount AS curr_demand,
                                            currdd.amt_collected AS curr_coll,
                                            currdd.amount -currdd.amt_collected AS curr_balance,
                                            coalesce(0,'0') AS arr_demand,
                                            coalesce(0,'0') AS arr_coll,
                                            coalesce(0,'0') AS arr_balance
                                     FROM egwtr_connection con
                                     INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                                     INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
                                     INNER JOIN eg_demand currdmd ON currdmd.id = cd.demand
                                     INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
                                     INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
                                     INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
                                     LEFT JOIN eg_demand_details currdd ON currdd.id_demand =currdmd.id
                                     WHERE cd.connectionstatus = 'ACTIVE'
                                       AND currdd.id_demand_reason IN
                                         (SELECT id
                                          FROM eg_demand_reason
                                          WHERE id_demand_reason_master =
                                              (SELECT id
                                               FROM eg_demand_reason_master
                                               WHERE code='WTAXCHARGES'
                                                 AND isdemand=TRUE)
                                            AND id_installment=
                                              (SELECT id
                                               FROM eg_installment_master
                                               WHERE eg_installment_master.start_date <= now()
                                                 AND eg_installment_master.end_date >= now()
                                                 AND eg_installment_master.id_module = (
                                                                                          (SELECT eg_module.id
                                                                                           FROM eg_module
                                                                                           WHERE eg_module.name::text = 'Water Tax Management'::text))
                                                 AND eg_installment_master.installment_type='Monthly'))
                                       AND cd.connectiontype='METERED')
                                  UNION
                                    (SELECT bp.propertyid,
                                            bpaddr.address AS address,
                                            con.consumercode AS hscno,
                                            ownername(bp.id) AS username,
                                            propid.zone_num AS zoneid,
                                            propid.WARD_ADM_ID AS wardid,
                                            propid.ADM1 AS block,
                                            propid.ADM2 AS locality,
                                            propid.ADM3 AS street,
                                            cd.connectiontype AS connectiontype,
                                            coalesce(0,'0') AS curr_demand,
                                            coalesce(0,'0') AS curr_coll,
                                            coalesce(0,'0') AS curr_balance,
                                            coalesce(arrdd.amount,'0') AS arr_demand,
                                            coalesce(arrdd.amt_collected,'0') AS arr_coll,
                                            coalesce(arrdd.amount -arrdd.amt_collected,'0') AS arr_balance
                                     FROM egwtr_connection con
                                     INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                                     INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
                                     INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
                                     INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
                                     INNER JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
                                     INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
                                     INNER JOIN eg_demand_details arrdd ON arrdd.id_demand =arrdmd.id
                                     WHERE cd.connectionstatus = 'ACTIVE'
                                       AND arrdd.id_demand_reason IN
                                         (SELECT id
                                          FROM eg_demand_reason
                                          WHERE id_demand_reason_master =
                                              (SELECT id
                                               FROM eg_demand_reason_master
                                               WHERE code='WTAXCHARGES')
                                            AND id_installment NOT IN
                                              (SELECT id
                                               FROM eg_installment_master
                                               WHERE eg_installment_master.start_date <= now()
                                                 AND eg_installment_master.end_date >= now()
                                                 AND eg_installment_master.id_module IN
                                                   (SELECT eg_module.id
                                                    FROM eg_module
                                                    WHERE eg_module.name::text IN('Water Tax Management'::text,
                                                                                  'Property Tax'::text))))
                                       AND cd.connectiontype='METERED'))
UNION (
         (SELECT bp.propertyid,
                 bpaddr.address AS address,
                 con.consumercode AS hscno,
                 ownername(bp.id) AS username,
                 propid.zone_num AS zoneid,
                 propid.WARD_ADM_ID AS wardid,
                 propid.ADM1 AS block,
                 propid.ADM2 AS locality,
                 propid.ADM3 AS street,
                 cd.connectiontype AS connectiontype,
                 coalesce(currdd.amount,'0') AS curr_demand,
                 coalesce(currdd.amt_collected,'0') AS curr_coll,
                 coalesce(currdd.amount -currdd.amt_collected,'0') AS curr_balance,
                 coalesce(0,'0') AS arr_demand,
                 coalesce(0,'0') AS arr_coll,
                 coalesce(0,'0') AS arr_balance
          FROM egwtr_connection con
          INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
          INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
          INNER JOIN eg_demand currdmd ON currdmd.id = cd.demand
          INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
          INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
          INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
          LEFT JOIN eg_demand_details currdd ON currdd.id_demand =currdmd.id
          WHERE cd.connectionstatus = 'ACTIVE'
            AND currdd.id_demand_reason IN
              (SELECT id
               FROM eg_demand_reason
               WHERE id_demand_reason_master =
                   (SELECT id
                    FROM eg_demand_reason_master
                    WHERE code='WTAXCHARGES'
                      AND isdemand=TRUE)
                 AND id_installment=
                   (SELECT id
                    FROM eg_installment_master
                    WHERE eg_installment_master.start_date <= now()
                      AND eg_installment_master.end_date >= now()
                      AND eg_installment_master.id_module = (
                                                               (SELECT eg_module.id
                                                                FROM eg_module
                                                                WHERE eg_module.name::text = 'Property Tax'::text))))
            AND cd.connectiontype='NON_METERED')
       UNION
         (SELECT bp.propertyid,
                 bpaddr.address AS address,
                 con.consumercode AS hscno,
                 ownername(bp.id) AS username,
                 propid.zone_num AS zoneid,
                 propid.WARD_ADM_ID AS wardid,
                 propid.ADM1 AS block,
                 propid.ADM2 AS locality,
                 propid.ADM3 AS street,
                 cd.connectiontype AS connectiontype,
                 coalesce(0,'0') AS curr_demand,
                 coalesce(0,'0') AS curr_coll,
                 coalesce(0,'0') AS curr_balance,
                 coalesce(arrdd.amount,'0') AS arr_demand,
                 coalesce(arrdd.amt_collected,'0') AS arr_coll,
                 coalesce(arrdd.amount -arrdd.amt_collected,'0') AS arr_balance
          FROM egwtr_connection con
          INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
          INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
          INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
          INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
          INNER JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
          INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
          INNER JOIN eg_demand_details arrdd ON arrdd.id_demand =arrdmd.id
          WHERE cd.connectionstatus = 'ACTIVE'
            AND arrdd.id_demand_reason IN
              (SELECT id
               FROM eg_demand_reason
               WHERE id_demand_reason_master =
                   (SELECT id
                    FROM eg_demand_reason_master
                    WHERE code='WTAXCHARGES')
                 AND id_installment NOT IN
                   (SELECT id
                    FROM eg_installment_master
                    WHERE eg_installment_master.start_date <= now()
                      AND eg_installment_master.end_date >= now()
                      AND eg_installment_master.id_module IN
                        (SELECT eg_module.id
                         FROM eg_module
                         WHERE eg_module.name::text IN('Property Tax'::text))))
            AND cd.connectiontype='NON_METERED'));

