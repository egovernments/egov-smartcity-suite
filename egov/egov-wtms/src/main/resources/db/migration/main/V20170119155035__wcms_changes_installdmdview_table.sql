
DROP TABLE if exists egwtr_mv_inst_dem_coll;

DROP sequence if exists seq_egwtr_mv_inst_dem_coll;

create sequence seq_egwtr_mv_inst_dem_coll;

CREATE TABLE  egwtr_mv_inst_dem_coll AS 
 SELECT dmndcoldtls.id as id,
    dmndcoldtls.connectiondetailsid,
    dmndcoldtls.id_installment,
    max(dmndcoldtls.createddate) AS createddate,
    max(dmndcoldtls.watercharge) AS watercharge,
    max(dmndcoldtls.waterchargecoll) AS waterchargecoll
   FROM ( SELECT nextval('seq_egwtr_mv_inst_dem_coll') AS id,
   currdem.connectiondetailsid,
            dr.id_installment,
            det.create_date AS createddate,
                CASE drm.code
                    WHEN 'WTAXCHARGES'::text THEN det.amount
                    ELSE 0
                END AS watercharge,
                CASE drm.code
                    WHEN 'WTAXCHARGES'::text THEN det.amt_collected
                    ELSE 0
                END AS waterchargecoll
           FROM egwtr_mv_dcb_view currdem,
            eg_demand_details det,
            eg_demand_reason dr,
            eg_demand_reason_master drm
          WHERE det.id_demand = currdem.demand AND det.id_demand_reason = dr.id AND dr.id_demand_reason_master = drm.id) dmndcoldtls
  GROUP BY dmndcoldtls.connectiondetailsid, dmndcoldtls.id_installment,dmndcoldtls.id;

  create index idx_egwtrCollview_conndetId on egwtr_mv_inst_dem_coll(connectiondetailsid);