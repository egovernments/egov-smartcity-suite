DROP VIEW MV1;
DROP VIEW MV2;
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
        FROM egpt_property_owner_info  
       WHERE basicproperty = m_propertyid;  
   user_rec egpt_property_owner_info%ROWTYPE;   
BEGIN  
     m_ownernames := '';  
   OPEN c_user;  
   LOOP  
      FETCH c_user INTO user_rec;  
      EXIT WHEN NOT FOUND;  
      m_iduser := user_rec.owner;        
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

------------ view egpt_mv_current_arrear_dem_coll ------------------
CREATE OR REPLACE VIEW egpt_mv_current_arrear_dem_coll AS
SELECT currnt.id_basic_property            AS basicpropertyid,
    coalesce (currnt.amt_demand, 0)      AS aggregate_current_demand,
    coalesce (currnt.amt_collected, 0) AS current_collection,
    coalesce (arrear.amt_demand, 0)       AS aggregate_arrear_demand,
    coalesce (arrear.amt_collected, 0)  AS arrearcollection
FROM egpt_mv_current_dem_coll currnt,
    egpt_mv_arrear_dem_coll arrear
WHERE currnt.id_basic_property = arrear.id_basic_property;

------------ view egpt_mv_propertyInfo ------------------
 CREATE OR REPLACE VIEW egpt_mv_propertyInfo AS
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
    dmdColl.aggregate_current_demand    AS aggregate_current_demand,
    dmdColl.aggregate_arrear_demand     AS aggregate_arrear_demand,
    dmdColl.current_collection          AS current_collection,
    dmdColl.arrearcollection            AS arrearcollection,
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
    egpt_mv_current_arrear_dem_coll dmdColl,  
    egpt_mv_curr_dmdcalc dmdcalc 
  WHERE bp.addressid         = addr.addressid
  AND prop.id_basic_property = bp.id
  AND propdet.id_property      = prop.id_property
  AND propid.id                = bp.id_propertyid
  AND dmdColl.basicpropertyid   = bp.id
  AND dmdcalc.id_basic_property = bp.id;


