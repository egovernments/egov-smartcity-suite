
DROP VIEW IF EXISTS pgr_router_escalation_view;

CREATE OR REPLACE VIEW pgr_router_escalation_view
                                       AS
  SELECT row_number() over() AS id,
  COALESCE(r.complainttypeid,0) AS ctid,
    COALESCE(ct.name,'NA')             AS ctname,
    COALESCE(cc.id,0)                  AS categoryid,
    COALESCE(cc.name,'NA')                AS categoryname,
    COALESCE(ct.code,'NA')             AS ctcode,
    r.position                         AS routerpos,
    p.name                             AS routerposname,
    COALESCE(r.bndryid,0)              AS bndryid,
    b.name                             AS bndryname,
    COALESCE(
    (SELECT ph.position_to
    FROM egeis_position_hierarchy ph
    WHERE ph.position_from = r.position
    AND ph.object_sub_type =ct.code
    AND ph.object_type_id  =
      (SELECT id FROM eg_object_type WHERE type='Complaint'
      )
    ),0) AS esclvl1,
    COALESCE(
    (SELECT esclvl1pos.name
    FROM eg_position esclvl1pos
    WHERE esclvl1pos.id=
      (SELECT ph.position_to
      FROM egeis_position_hierarchy ph
      WHERE ph.position_from = r.position
      AND ph.object_sub_type =ct.code
      AND ph.object_type_id  =
        (SELECT id FROM eg_object_type WHERE type='Complaint'
        )
      )
    ),'NA') AS esclvl1posname,
    COALESCE(
    (SELECT ph.position_to
    FROM egeis_position_hierarchy ph
    WHERE ph.object_sub_type=ct.code
    AND ph.object_type_id   =
      (SELECT id FROM eg_object_type WHERE type='Complaint'
      )
    AND ph.position_from=
      (SELECT ph.position_to
      FROM egeis_position_hierarchy ph
      WHERE ph.position_from = r.position
      AND ph.object_sub_type =ct.code
      AND ph.object_type_id  =
        (SELECT id FROM eg_object_type WHERE type='Complaint'
        )
      )
    ),0) AS esclvl2,
    COALESCE(
    (SELECT esclvl2pos.name
    FROM eg_position esclvl2pos
    WHERE esclvl2pos.id=
      (SELECT ph.position_to
      FROM egeis_position_hierarchy ph
      WHERE ph.object_sub_type=ct.code
      AND ph.object_type_id   =
        (SELECT id FROM eg_object_type WHERE type='Complaint'
        )
      AND ph.position_from=
        (SELECT ph.position_to
        FROM egeis_position_hierarchy ph
        WHERE ph.position_from = r.position
        AND ph.object_sub_type =ct.code
        AND ph.object_type_id  =
          (SELECT id FROM eg_object_type WHERE type='Complaint'
          )
        )
      )
    ),'NA')AS esclvl2posname,
    COALESCE(
    (SELECT ph.position_to
    FROM egeis_position_hierarchy ph
    WHERE ph.object_sub_type=ct.code
    AND ph.object_type_id   =
      (SELECT id FROM eg_object_type WHERE type='Complaint'
      )
    AND ph.position_from=
      (SELECT ph.position_to
      FROM egeis_position_hierarchy ph
      WHERE ph.object_sub_type=ct.code
      AND ph.object_type_id   =
        (SELECT id FROM eg_object_type WHERE type='Complaint'
        )
      AND ph.position_from=
        (SELECT ph.position_to
        FROM egeis_position_hierarchy ph
        WHERE ph.position_from = r.position
        AND ph.object_sub_type =ct.code
        AND ph.object_type_id  =
          (SELECT id FROM eg_object_type WHERE type='Complaint'
          )
        )
      )
    ),0) AS esclvl3,
    COALESCE(
    (SELECT escalvl3pos.name
    FROM eg_position escalvl3pos
    WHERE escalvl3pos.id=
      (SELECT ph.position_to
      FROM egeis_position_hierarchy ph
      WHERE ph.object_sub_type=ct.code
      AND ph.object_type_id   =
        (SELECT id FROM eg_object_type WHERE type='Complaint'
        )
      AND ph.position_from=
        (SELECT ph.position_to
        FROM egeis_position_hierarchy ph
        WHERE ph.object_sub_type=ct.code
        AND ph.object_type_id   =
          (SELECT id FROM eg_object_type WHERE type='Complaint'
          )
        AND ph.position_from=
          (SELECT ph.position_to
          FROM egeis_position_hierarchy ph
          WHERE ph.position_from = r.position
          AND ph.object_sub_type =ct.code
          AND ph.object_type_id  =
            (SELECT id FROM eg_object_type WHERE type='Complaint'
            )
          )
        )
      )
    ),'NA') AS esclvl3posname
  FROM egpgr_router r
  LEFT JOIN egpgr_complainttype ct
  ON r.complainttypeid=ct.id
  LEFT JOIN egpgr_complainttype_category cc
  ON ct.category = cc.id
  LEFT JOIN eg_boundary b
  ON r.bndryid =b.id
  LEFT JOIN eg_position p
  ON r.position = p.id
  ORDER BY complainttypeid;

  --roleaction mapping
  
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'routerescaltionreport','/routerescalation/reportgeneration',null,(select id from EG_MODULE where name = 'Router Escalation Report'),3,'Router Escalation Report',false,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='routerescaltionreport'));

Insert into eg_roleaction values((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='routerescaltionreport'));

Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),(select id from eg_action where name='routerescaltionreport'));

Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),(select id from eg_action where name='routerescaltionreport'));

Insert into eg_roleaction values((select id from eg_role where name='TL VIEW ACCESS'),(select id from eg_action where name='routerescaltionreport'));
