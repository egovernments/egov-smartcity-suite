create or replace function mobilenumber(v_basicpropid IN BIGINT)  
RETURNS VARCHAR  as  $$
declare
	v_mobileno VARCHAR(12);  
	owners eg_user%ROWTYPE;
BEGIN  
	for owners in (select u.* from egpt_property_owner_info po, eg_user u where po.owner=u.id and po.basicproperty = v_basicpropid)
	loop 
		begin
			IF owners.mobilenumber <> null or owners.mobilenumber <> '' THEN
				v_mobileno := owners.mobilenumber;
				EXIT;
			END IF;
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
			NULL;   
		END;
	END LOOP;
	return v_mobileno;   
END; 
$$ LANGUAGE plpgsql;

drop view egpt_mv_propertyInfo;

CREATE OR REPLACE VIEW egpt_mv_propertyInfo AS
SELECT bp.id  							AS basicpropertyid,
    bp.propertyid                		AS upicno, 
    ownername (bp.id) 					AS ownersname,
    addr.housenobldgapt 				AS houseno,
    mobilenumber(bp.id)					AS mobileno,
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