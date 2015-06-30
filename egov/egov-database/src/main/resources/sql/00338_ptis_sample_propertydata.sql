INSERT INTO egpt_demandcalculations (id, id_demand, propertytax, rate_of_tax, current_interest, arrear_interest, modified_date, created_date, created_by, modified_by, taxinfo, alv)
values (nextval('seq_egpt_demandcalculations'), (select id from eg_demand where base_demand=505), null, null, null, null, current_timestamp, current_timestamp, 1, 1, null, null);

INSERT INTO egpt_floordemandcalc (id, id_floordet, id_dmdcalc, createtimestamp, lastupdatedtimestamp)
values (nextval('seq_egpt_floordemandcalc'), (select fDet.id from egpt_basic_property bp, egpt_property prop, egpt_property_detail propDet, egpt_floor_detail fDet
 where bp.propertyid  = '1085000001' and bp.id = prop.id_basic_property and prop.status = 'A' and prop.id = propDet.id_property and propDet.id = fDet.id_property_detail),
 (select id from egpt_demandcalculations where id_demand  = (select id from eg_demand where base_demand=505)), current_timestamp, current_timestamp);