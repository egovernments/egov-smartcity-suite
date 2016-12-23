CREATE OR REPLACE VIEW pgr_router_escalation_view
                                       AS
  SELECT COALESCE(r.complainttypeid,0) AS ctid,
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


--Roleaction Mapping

INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Router Escalation Report', true, 'pgr', (select id from eg_module where name='Pgr Reports'), 'Router Escalation Report', 5);

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'routerescalationsearch', '/routerescalation/search-form', NULL, (SELECT id FROM eg_module WHERE name = 'Router Escalation Report'), 5, 'Router Escalation Report', TRUE, 'pgr', 0, 1, now(), 1,now(), (SELECT id FROM eg_module WHERE name = 'PGR' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Super User'), (SELECT id FROM eg_action WHERE  name = 'routerescalationsearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Grievance Officer'), (SELECT id FROM eg_action WHERE  name = 'routerescalationsearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Redressal Officer'), (SELECT id FROM eg_action WHERE  name = 'routerescalationsearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Grivance Administrator'), (SELECT id FROM eg_action WHERE  name = 'routerescalationsearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Grievance Routing Officer'), (SELECT id FROM eg_action WHERE  name = 'routerescalationsearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'PGR VIEW ACCESS'), (SELECT id FROM eg_action WHERE  name = 'routerescalationsearch'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'routerescalationresult', '/routerescalation/search-resultList', NULL, (SELECT id FROM eg_module WHERE name = 'Router Escalation Report'), 5, 'routerescalationresult', FALSE, 'pgr', 0, 1, now(), 1,now(), (SELECT id FROM eg_module WHERE name = 'PGR' AND parentmodule IS NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Super User'), (SELECT id FROM eg_action WHERE  name = 'routerescalationresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Grievance Officer'), (SELECT id FROM eg_action WHERE  name = 'routerescalationresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Redressal Officer'), (SELECT id FROM eg_action WHERE  name = 'routerescalationresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Grivance Administrator'), (SELECT id FROM eg_action WHERE  name = 'routerescalationresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'Grievance Routing Officer'), (SELECT id FROM eg_action WHERE  name = 'routerescalationresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name LIKE 'PGR VIEW ACCESS'), (SELECT id FROM eg_action WHERE  name = 'routerescalationresult'));
