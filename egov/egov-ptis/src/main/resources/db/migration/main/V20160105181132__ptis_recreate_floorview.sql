DROP TABLE EGPT_MV_CURRENT_FLOOR_DETAIL;
CREATE TABLE EGPT_MV_CURRENT_FLOOR_DETAIL AS SELECT 
    basicproperty.id AS basicPropertyId,
    currpropdet.id_property AS propertyId,
    usage.usg_name AS natureOfUsage,
    propdet.id_propertytypemaster AS propertyType,
    floordet.floor_no AS floorNo,
    floordet.builtup_area AS plinthArea,
    propdet.sital_area AS builtUpArea,
    strut.constr_type AS classification, 
    floordet.id_occpn_mstr AS occupation,
    floordet.id AS floorid
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