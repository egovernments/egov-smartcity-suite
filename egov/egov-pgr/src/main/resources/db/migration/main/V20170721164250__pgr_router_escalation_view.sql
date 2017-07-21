drop view pgr_router_escalation_view;
CREATE OR REPLACE VIEW pgr_router_escalation_view AS 
 SELECT row_number() OVER () AS id,
    COALESCE(r.complainttypeid, 0::numeric) AS ctid,
    COALESCE(ct.name, 'NA'::character varying) AS ctname,
    COALESCE(cc.id, 0::numeric) AS categoryid,
    COALESCE(cc.name, 'NA'::character varying) AS categoryname,
    COALESCE(ct.code, 'NA'::character varying) AS ctcode,
    r."position" AS routerpos,
    p.name AS routerposname,
    COALESCE(r.bndryid, 0::bigint) AS bndryid,
    b.name AS bndryname,
    COALESCE(( SELECT ph.position_to
           FROM egeis_position_hierarchy ph
          WHERE ph.position_from = r."position" AND ph.object_sub_type::text = ct.code::text AND ph.object_type_id = (( SELECT eg_object_type.id
                   FROM eg_object_type
                  WHERE eg_object_type.type::text = 'Complaint'::text))), 0::bigint) AS esclvl1,
    COALESCE(( SELECT esclvl1pos.name
           FROM eg_position esclvl1pos
          WHERE esclvl1pos.id = (( SELECT ph.position_to
                   FROM egeis_position_hierarchy ph
                  WHERE ph.position_from = r."position" AND ph.object_sub_type::text = ct.code::text AND ph.object_type_id = (( SELECT eg_object_type.id
                           FROM eg_object_type
                          WHERE eg_object_type.type::text = 'Complaint'::text))))), 'NA'::character varying) AS esclvl1posname,
    COALESCE(( SELECT ph.position_to
           FROM egeis_position_hierarchy ph
          WHERE ph.object_sub_type::text = ct.code::text AND ph.object_type_id = (( SELECT eg_object_type.id
                   FROM eg_object_type
                  WHERE eg_object_type.type::text = 'Complaint'::text)) AND ph.position_from = (( SELECT ph_1.position_to
                   FROM egeis_position_hierarchy ph_1
                  WHERE ph_1.position_from = r."position" AND ph_1.object_sub_type::text = ct.code::text AND ph_1.object_type_id = (( SELECT eg_object_type.id
                           FROM eg_object_type
                          WHERE eg_object_type.type::text = 'Complaint'::text))))), 0::bigint) AS esclvl2,
    COALESCE(( SELECT esclvl2pos.name
           FROM eg_position esclvl2pos
          WHERE esclvl2pos.id = (( SELECT ph.position_to
                   FROM egeis_position_hierarchy ph
                  WHERE ph.object_sub_type::text = ct.code::text AND ph.object_type_id = (( SELECT eg_object_type.id
                           FROM eg_object_type
                          WHERE eg_object_type.type::text = 'Complaint'::text)) AND ph.position_from = (( SELECT ph_1.position_to
                           FROM egeis_position_hierarchy ph_1
                          WHERE ph_1.position_from = r."position" AND ph_1.object_sub_type::text = ct.code::text AND ph_1.object_type_id = (( SELECT eg_object_type.id
                                   FROM eg_object_type
                                  WHERE eg_object_type.type::text = 'Complaint'::text))))))), 'NA'::character varying) AS esclvl2posname,
    COALESCE(( SELECT ph.position_to
           FROM egeis_position_hierarchy ph
          WHERE ph.object_sub_type::text = ct.code::text AND ph.object_type_id = (( SELECT eg_object_type.id
                   FROM eg_object_type
                  WHERE eg_object_type.type::text = 'Complaint'::text)) AND ph.position_from = (( SELECT ph_1.position_to
                   FROM egeis_position_hierarchy ph_1
                  WHERE ph_1.object_sub_type::text = ct.code::text AND ph_1.object_type_id = (( SELECT eg_object_type.id
                           FROM eg_object_type
                          WHERE eg_object_type.type::text = 'Complaint'::text)) AND ph_1.position_from = (( SELECT ph_2.position_to
                           FROM egeis_position_hierarchy ph_2
                          WHERE ph_2.position_from = r."position" AND ph_2.object_sub_type::text = ct.code::text AND ph_2.object_type_id = (( SELECT eg_object_type.id
                                   FROM eg_object_type
                                  WHERE eg_object_type.type::text = 'Complaint'::text))))))), 0::bigint) AS esclvl3,
    COALESCE(( SELECT escalvl3pos.name
           FROM eg_position escalvl3pos
          WHERE escalvl3pos.id = (( SELECT ph.position_to
                   FROM egeis_position_hierarchy ph
                  WHERE ph.object_sub_type::text = ct.code::text AND ph.object_type_id = (( SELECT eg_object_type.id
                           FROM eg_object_type
                          WHERE eg_object_type.type::text = 'Complaint'::text)) AND ph.position_from = (( SELECT ph_1.position_to
                           FROM egeis_position_hierarchy ph_1
                          WHERE ph_1.object_sub_type::text = ct.code::text AND ph_1.object_type_id = (( SELECT eg_object_type.id
                                   FROM eg_object_type
                                  WHERE eg_object_type.type::text = 'Complaint'::text)) AND ph_1.position_from = (( SELECT ph_2.position_to
                                   FROM egeis_position_hierarchy ph_2
                                  WHERE ph_2.position_from = r."position" AND ph_2.object_sub_type::text = ct.code::text AND ph_2.object_type_id = (( SELECT eg_object_type.id
   FROM eg_object_type
  WHERE eg_object_type.type::text = 'Complaint'::text))))))))), 'NA'::character varying) AS esclvl3posname,
  ct.isactive as active
   FROM egpgr_router r
     LEFT JOIN egpgr_complainttype ct ON r.complainttypeid = ct.id
     LEFT JOIN egpgr_complainttype_category cc ON ct.category = cc.id
     LEFT JOIN eg_boundary b ON r.bndryid = b.id
     LEFT JOIN eg_position p ON r."position" = p.id
  ORDER BY r.complainttypeid;
