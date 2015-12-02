------------------START------------------
DROP VIEW egwtr_mv_dcb_view;

CREATE OR REPLACE VIEW egwtr_mv_dcb_view AS 
SELECT mvp.upicno as propertyid,
  mvp.address,
  con.consumercode AS hscno,
  mvp.ownersname   AS username,
  mvp.zoneid       AS zoneid,
  mvp.wardid       AS wardid,
  mvp.blockid      AS block,
  mvp.localityid   AS locality,
  mvp.streetid     AS street,
  cd.connectiontype,
  currdd.amount                                          AS curr_demand,
  currdd.amt_collected                                   AS curr_coll,
  currdd.amount::DOUBLE PRECISION - currdd.amt_collected AS curr_balance,
  COALESCE(0, 0)                                         AS arr_demand,
  COALESCE(0, 0)                                         AS arr_coll,
  COALESCE(0, 0)                                         AS arr_balance
FROM egwtr_connection con
JOIN egwtr_connectiondetails cd
ON con.id = cd.connection
JOIN egpt_mv_propertyinfo mvp
ON con.propertyidentifier::text = mvp.upicno::text
JOIN eg_demand currdmd
ON currdmd.id = cd.demand
LEFT JOIN eg_demand_details currdd
ON currdd.id_demand = currdmd.id
LEFT JOIN eg_demand_reason dr
ON dr.id = currdd.id_demand_reason
LEFT JOIN eg_demand_reason_master drm
ON drm.id = dr.id_demand_reason_master
LEFT JOIN eg_installment_master im
ON im.id = dr.id_installment
LEFT JOIN eg_module m
ON m.id                         = im.id_module
WHERE cd.connectionstatus::text = 'ACTIVE'::text
AND drm.code::text              = 'WTAXCHARGES'::text
AND drm.isdemand                = true
AND im.start_date              <= now()
AND im.end_date                >= now()
AND m.name::text                = 'Water Tax Management'::text
AND im.installment_type::text   = 'Monthly'::text
AND cd.connectiontype::text     = 'METERED'::text
UNION
SELECT mvp.upicno,
  mvp.address,
  con.consumercode AS hscno,
  mvp.ownersname   AS username,
  mvp.zoneid       AS zoneid,
  mvp.wardid       AS wardid,
  mvp.blockid      AS block,
  mvp.localityid   AS locality,
  mvp.streetid     AS street,
  cd.connectiontype,
  COALESCE(0, 0)                                                                      AS curr_demand,
  COALESCE(0, 0)                                                                      AS curr_coll,
  COALESCE(0, 0)                                                                      AS curr_balance,
  COALESCE(arrdd.amount, 0::bigint)                                                   AS arr_demand,
  COALESCE(arrdd.amt_collected, 0::DOUBLE PRECISION)                                  AS arr_coll,
  COALESCE(arrdd.amount::DOUBLE PRECISION - arrdd.amt_collected, 0::DOUBLE PRECISION) AS arr_balance
FROM egwtr_connection con
JOIN egwtr_connectiondetails cd
ON con.id = cd.connection
JOIN egpt_mv_propertyinfo mvp
ON con.propertyidentifier::text = mvp.upicno::text
JOIN eg_demand arrdmd
ON arrdmd.id = cd.demand
LEFT JOIN eg_demand_details arrdd
ON arrdd.id_demand = arrdmd.id
LEFT JOIN eg_demand_reason dr
ON dr.id = arrdd.id_demand_reason
LEFT JOIN eg_demand_reason_master drm
ON drm.id                       = dr.id_demand_reason_master
WHERE cd.connectionstatus::text = 'ACTIVE'::text
AND drm.code::text              = 'WTAXCHARGES'::text
AND NOT (dr.id_installment     IN
  (SELECT eim.id
  FROM eg_installment_master eim
  WHERE eim.start_date <= now()
  AND eim.end_date     >= now()
  AND (eim.id_module   IN
    (SELECT em.id
    FROM eg_module em
    WHERE em.name::text = ANY (ARRAY['Water Tax Management'::text, 'Property Tax'::text])
    ))
  ))
AND cd.connectiontype::text = 'METERED'::text
UNION
  (SELECT mvp.upicno,
    mvp.address,
    con.consumercode AS hscno,
    mvp.ownersname   AS username,
    mvp.zoneid       AS zoneid,
    mvp.wardid       AS wardid,
    mvp.blockid      AS block,
    mvp.localityid   AS locality,
    mvp.streetid     AS street,
    cd.connectiontype,
    COALESCE(currdd.amount, 0::bigint)                                                    AS curr_demand,
    COALESCE(currdd.amt_collected, 0::DOUBLE PRECISION)                                   AS curr_coll,
    COALESCE(currdd.amount::DOUBLE PRECISION - currdd.amt_collected, 0::DOUBLE PRECISION) AS curr_balance,
    COALESCE(0, 0)                                                                        AS arr_demand,
    COALESCE(0, 0)                                                                        AS arr_coll,
    COALESCE(0, 0)                                                                        AS arr_balance
  FROM egwtr_connection con
  JOIN egwtr_connectiondetails cd
  ON con.id = cd.connection
  JOIN egpt_mv_propertyinfo mvp
  ON con.propertyidentifier::text = mvp.upicno::text
  JOIN eg_demand currdmd
  ON currdmd.id = cd.demand
  LEFT JOIN eg_demand_details currdd
  ON currdd.id_demand = currdmd.id
  LEFT JOIN eg_demand_reason dr
  ON dr.id = currdd.id_demand_reason
  LEFT JOIN eg_demand_reason_master drm
  ON drm.id = dr.id_demand_reason_master
  LEFT JOIN eg_installment_master im
  ON im.id = dr.id_installment
  LEFT JOIN eg_module m
  ON m.id                         = im.id_module
  WHERE cd.connectionstatus::text = 'ACTIVE'::text
  AND drm.code::text              = 'WTAXCHARGES'::text
  AND drm.isdemand                = true
  AND im.start_date              <= now()
  AND im.end_date                >= now()
  AND m.name::text                = 'Property Tax'::text
  AND cd.connectiontype::text     = 'NON_METERED'::text
  UNION
  SELECT mvp.upicno,
    mvp.address,
    con.consumercode AS hscno,
    mvp.ownersname   AS username,
    mvp.zoneid       AS zoneid,
    mvp.wardid       AS wardid,
    mvp.blockid      AS block,
    mvp.localityid   AS locality,
    mvp.streetid     AS street,
    cd.connectiontype,
    COALESCE(0, 0)                                                                      AS curr_demand,
    COALESCE(0, 0)                                                                      AS curr_coll,
    COALESCE(0, 0)                                                                      AS curr_balance,
    COALESCE(arrdd.amount, 0::bigint)                                                   AS arr_demand,
    COALESCE(arrdd.amt_collected, 0::DOUBLE PRECISION)                                  AS arr_coll,
    COALESCE(arrdd.amount::DOUBLE PRECISION - arrdd.amt_collected, 0::DOUBLE PRECISION) AS arr_balance
  FROM egwtr_connection con
  JOIN egwtr_connectiondetails cd
  ON con.id = cd.connection
  JOIN egpt_mv_propertyinfo mvp
  ON con.propertyidentifier::text = mvp.upicno::text
  JOIN eg_demand arrdmd
  ON arrdmd.id = cd.demand
  JOIN eg_demand_details arrdd
  ON arrdd.id_demand = arrdmd.id
  LEFT JOIN eg_demand_reason dr
  ON dr.id = arrdd.id_demand_reason
  LEFT JOIN eg_demand_reason_master drm
  ON drm.id                       = dr.id_demand_reason_master
  WHERE cd.connectionstatus::text = 'ACTIVE'::text
  AND drm.code::text              = 'WTAXCHARGES'::text
  AND NOT (dr.id_installment     IN
    (SELECT eim.id
    FROM eg_installment_master eim
    WHERE eim.start_date <= now()
    AND eim.end_date     >= now()
    AND (eim.id_module   IN
      (SELECT em.id
      FROM eg_module em
      WHERE em.name::text = 'Property Tax'::text
      ))
    ))
  AND cd.connectiontype::text = 'NON_METERED'::text
  );
-------------------END-------------------
