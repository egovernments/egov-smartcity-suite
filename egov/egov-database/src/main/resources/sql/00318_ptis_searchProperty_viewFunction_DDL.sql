DROP FUNCTION OWNERNAME(BIGINT);

------------ function OWNERNAME ------------------
create or replace FUNCTION OWNERNAME(m_propertyid IN BIGINT)  
   RETURNS VARCHAR  as  $$
declare
   m_ownernames   VARCHAR (3072);  
   m_ownername    VARCHAR (3072);  
   m_iduser    BIGINT;  
   c_user  CURSOR
   IS  
      SELECT *  
        FROM egpt_property_owner  
       WHERE id_property = m_propertyid;  
   user_rec egpt_property_owner%ROWTYPE;   
BEGIN  
     m_ownernames := '';  
   OPEN c_user;  
   LOOP  
      FETCH c_user INTO user_rec;  
      EXIT WHEN NOT FOUND;  
      m_iduser := user_rec.ownerid;        
      BEGIN  
         SELECT name  
           INTO m_ownername  
           FROM eg_user  
          WHERE id = m_iduser;            
         IF m_ownernames <> ''  
         THEN  
            m_ownernames := m_ownernames || ',' || m_ownername;  
         ELSE  
            m_ownernames := m_ownername;  
         END IF;         
      EXCEPTION  
         WHEN NO_DATA_FOUND  
         THEN  
         NULL;               
        END;         
   END LOOP;    
   CLOSE c_user;   
   return m_ownernames;   
END; 
$$ LANGUAGE plpgsql;

------------ view egpt_mv_current_property ------------------ 
CREATE OR REPLACE VIEW egpt_mv_current_property AS
SELECT bp.id as id_basic_property,
    prop.id as id_property, prop.id_installment, prop.status
FROM egpt_basic_property bp,
  egpt_property prop
WHERE prop.id_basic_property = bp.id
AND prop.is_default_property = 'Y'
AND prop.status in ('A', 'I', 'H', 'O') --taking Active or Inactive property, Inactive property is assessed or re-assessed it will become active after 21 days.
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
              
------------ view  egpt_mv_arrear_dem_coll  ------------------              
CREATE OR REPLACE VIEW egpt_mv_arrear_dem_coll AS
SELECT dem.id_basic_property, 
    SUM(det.amount) AS amt_demand, 
    SUM(det.amt_collected) AS amt_collected
FROM 
    egpt_mv_bp_curr_demand dem LEFT OUTER JOIN eg_demand_details det ON (
        det.id_demand       = dem.id_demand
        AND det.id_demand_reason IN (SELECT id FROM eg_demand_reason demand_reason 
                                    WHERE id_demand_reason_master not in (select id from eg_demand_reason_master where code in ('CHQ_BUNC_PENALTY', 'PENALTY_FINES') and module=(SELECT id FROM eg_module WHERE name='Property Tax'))
									and	 id_installment <> (SELECT id_installment
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
    propadd.subnumber,
    CASE WHEN addr.housenobldgapt IS NOT NULL THEN addr.housenobldgapt ELSE '' END ||
        CASE WHEN propadd.subnumber IS NOT NULL THEN '/' || propadd.subnumber ELSE '' END ||
        CASE WHEN propadd.doornumold IS NOT NULL THEN '(' || propadd.doornumold || ')' ELSE '' END ||
        CASE WHEN addr.streetroadline IS NOT NULL THEN ', ' || addr.streetroadline ELSE '' END ||
        CASE WHEN addr.arealocalitysector IS NOT NULL THEN ', ' || addr.arealocalitysector ELSE '' END ||
        CASE WHEN addr.citytownvillage IS NOT NULL THEN ', ' || addr.citytownvillage ELSE '' END ||
        CASE WHEN addr.pincode IS NOT NULL THEN ' ' || addr.pincode ELSE '' END AS address
FROM 
    egpt_basic_property bp JOIN (
        eg_address addr LEFT OUTER JOIN egpt_address propadd ON addr.id = propadd.id
    ) ON bp.addressid = addr.id
	where bp.isactive='Y';


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


------------ view egpt_mv_current_prop_det ------------------
CREATE OR REPLACE VIEW egpt_mv_current_prop_det AS
SELECT prop.id_property,
	propdet.id as id_property_detail,
	propdet.id_propertytypemaster,
	propdet.id_usg_mstr,
	propdet.sital_area,
	propdet.total_builtup_area, 
  propdet.extra_field1 
FROM egpt_mv_current_property prop,
	egpt_property_detail propdet
WHERE
	prop.id_property = propdet.id_property;


------------ view mv2 ------------------
CREATE OR REPLACE VIEW mv2 AS
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


------------ view MV1 ------------------
 CREATE OR REPLACE VIEW mv1 AS
SELECT bp.id  AS basicpropertyid,
    bp.propertyid                AS upicno, 
    ownername (prop.id_property) AS ownersname,
    COALESCE (addr.subnumber, addr.housenobldgapt || '/' || addr.subnumber, addr.housenobldgapt ) AS houseno,
    addr.address                         AS address,
    propdet.id_propertytypemaster        AS proptymaster,
    propdet.id_usg_mstr                  AS prop_usage_master,
    bp.id_adm_bndry                      AS wardid,
    propid.ZONE_NUM                      AS zoneid,
    propid.adm3                          AS streetid,
    propid.adm1				 			 AS blockid,
    propid.adm2				 			 AS localityid,
    1                                    AS source_id,
    propdet.sital_area                   AS sital_area,
    propdet.total_builtup_area           AS total_builtup_area,
    bp.status                            AS latest_status,
    mv2.aggregate_current_demand    AS aggregate_current_demand,
    mv2.aggregate_arrear_demand     AS aggregate_arrear_demand,
    mv2.current_collection          AS current_collection,
    mv2.arrearcollection            AS arrearcollection,
    bp.GIS_REF_NO					AS GISREFNO, 
    case dmdcalc.alv
      when null then 0  
      else dmdcalc.alv 
     end  as ALV
  FROM egpt_basic_property bp,
    egpt_mv_current_property prop,
    egpt_mv_bp_address addr,
    egpt_mv_current_prop_det propdet,
    egpt_propertyid  propid,
    mv2,  
    egpt_mv_curr_dmdcalc dmdcalc 
  WHERE bp.addressid         = addr.addressid
  AND prop.id_basic_property = bp.id
  AND propdet.id_property      = prop.id_property
  AND propid.id                = bp.id_propertyid
  AND mv2.basicpropertyid   = bp.id
  AND dmdcalc.id_basic_property = bp.id;

