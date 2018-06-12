
DROP VIEW if exists pgr_router_escalation_view;

CREATE OR REPLACE VIEW pgr_router_escalation_view AS 
 SELECT row_number() OVER () AS id,
    COALESCE(r.complainttypeid, 0::numeric) AS ctid,
    COALESCE(ct.name, 'NA'::character varying) AS ctname,
    COALESCE(cc.id, 0::numeric) AS categoryid,
    COALESCE(cc.name, 'NA'::character varying) AS categoryname,
    r."position" AS routerpos,
    p.name AS routerposname,
    COALESCE(r.bndryid, 0::bigint) AS bndryid,
    b.name AS bndryname,
    COALESCE(( SELECT eh.toposition
           FROM egpgr_escalationhierarchy eh
          WHERE eh.fromposition = r."position" AND eh.grievancetype = ct.id), 0::bigint) AS esclvl1,
    COALESCE(( SELECT esclvl1pos.name
           FROM eg_position esclvl1pos
          WHERE esclvl1pos.id = (( SELECT eh.toposition
                   FROM egpgr_escalationhierarchy eh
                  WHERE eh.fromposition = r."position" AND eh.grievancetype = ct.id))), 'NA'::character varying) AS esclvl1posname,
    COALESCE(( SELECT eh.toposition
           FROM egpgr_escalationhierarchy eh
          WHERE eh.grievancetype = ct.id AND eh.fromposition = (( SELECT eh_1.toposition
                   FROM egpgr_escalationhierarchy eh_1
                  WHERE eh_1.fromposition = r."position" AND eh_1.grievancetype = ct.id ))), 0::bigint) AS esclvl2,
    COALESCE(( SELECT esclvl2pos.name
           FROM eg_position esclvl2pos
          WHERE esclvl2pos.id = (( SELECT eh.toposition
                   FROM egpgr_escalationhierarchy eh
                  WHERE eh.grievancetype = ct.id AND eh.fromposition = (( SELECT eh_1.toposition
                           FROM egpgr_escalationhierarchy eh_1
                          WHERE eh_1.fromposition = r."position" AND eh_1.grievancetype = ct.id ))))), 'NA'::character varying) AS esclvl2posname,
    COALESCE(( SELECT eh.toposition
           FROM egpgr_escalationhierarchy eh
          WHERE eh.grievancetype = ct.id AND eh.fromposition = (( SELECT eh_1.toposition
                   FROM egpgr_escalationhierarchy eh_1
                  WHERE eh_1.grievancetype = ct.id AND eh_1.fromposition = (( SELECT eh_2.toposition
                           FROM egpgr_escalationhierarchy eh_2
                          WHERE eh_2.fromposition = r."position" AND eh_2.grievancetype = ct.id ))))), 0::bigint) AS esclvl3,
    COALESCE(( SELECT escalvl3pos.name
           FROM eg_position escalvl3pos
          WHERE escalvl3pos.id = (( SELECT eh.toposition
                   FROM egpgr_escalationhierarchy eh
                  WHERE eh.grievancetype = ct.id AND eh.fromposition = (( SELECT eh_1.toposition
                           FROM egpgr_escalationhierarchy eh_1
                          WHERE eh_1.grievancetype = ct.id AND eh_1.fromposition = (( SELECT eh_2.toposition
                                   FROM egpgr_escalationhierarchy eh_2
                                  WHERE eh_2.fromposition = r."position" AND eh_2.grievancetype = ct.id))))))), 'NA'::character varying) AS esclvl3posname,
    ct.isactive AS active
   FROM egpgr_router r
     LEFT JOIN egpgr_complainttype ct ON r.complainttypeid = ct.id
     LEFT JOIN egpgr_complainttype_category cc ON ct.category = cc.id
     LEFT JOIN eg_boundary b ON r.bndryid = b.id
     LEFT JOIN eg_position p ON r."position" = p.id
  ORDER BY r.complainttypeid;
