DROP VIEW egwtr_mv_dcb_view;

CREATE OR REPLACE VIEW egwtr_mv_dcb_view AS 
 SELECT mv.propertyid,
    mv.address,
    mv.hscno,
    mv.username,
    mv.houseno,
    mv.zoneid,
    mv.wardid,
    mv.block,
    mv.locality,
    mv.street,
    mv.mobileno,
    mv.connectiontype,
    mv.connectionstatus,
    sum(mv.curr_demand) AS curr_demand,
    sum(mv.curr_coll) AS curr_coll,
    sum(mv.curr_balance) AS curr_balance,
    sum(mv.arr_demand) AS arr_demand,
    sum(mv.arr_coll) AS arr_coll,
    sum(mv.arr_balance) AS arr_balance
   FROM ( SELECT mvp.upicno AS propertyid,
            mvp.address,
            con.consumercode AS hscno,
            mvp.ownersname AS username,
            mvp.houseno,
            mvp.zoneid,
            mvp.wardid,
            mvp.blockid AS block,
            mvp.localityid AS locality,
            mvp.streetid AS street,
            mvp.mobileno,
            cd.connectiontype,
            cd.connectionstatus,
            sum(currdd.amount) AS curr_demand,
            sum(currdd.amt_collected) AS curr_coll,
            sum(currdd.amount - currdd.amt_collected) AS curr_balance,
            sum(COALESCE(0, 0)) AS arr_demand,
            sum(COALESCE(0, 0)) AS arr_coll,
            sum(COALESCE(0, 0)) AS arr_balance
           FROM egwtr_connection con
             JOIN egwtr_connectiondetails cd ON con.id = cd.connection
             JOIN egpt_mv_propertyinfo mvp ON con.propertyidentifier::text = mvp.upicno::text
             JOIN eg_demand currdmd ON currdmd.id = cd.demand
             LEFT JOIN eg_demand_details currdd ON currdd.id_demand = currdmd.id
             LEFT JOIN eg_demand_reason dr ON dr.id = currdd.id_demand_reason
             LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
             LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
             LEFT JOIN eg_module m ON m.id = im.id_module
          WHERE cd.connectionstatus::text = 'ACTIVE'::text AND drm.code::text = 'WTAXCHARGES'::text AND drm.isdemand = true AND im.start_date <= now() AND im.end_date >= now() AND m.name::text = 'Water Tax Management'::text AND im.installment_type::text = 'Monthly'::text AND cd.connectiontype::text = 'METERED'::text
          GROUP BY mvp.upicno, mvp.address, con.consumercode, mvp.ownersname, mvp.houseno, mvp.zoneid, mvp.wardid, mvp.blockid, mvp.localityid, mvp.streetid, mvp.mobileno, cd.connectiontype,cd.connectionstatus
        UNION
         SELECT mvp.upicno,
            mvp.address,
            con.consumercode AS hscno,
            mvp.ownersname AS username,
            mvp.houseno,
            mvp.zoneid,
            mvp.wardid,
            mvp.blockid AS block,
            mvp.localityid AS locality,
            mvp.streetid AS street,
            mvp.mobileno,
            cd.connectiontype,
            cd.connectionstatus,
            sum(COALESCE(0, 0)) AS curr_demand,
            sum(COALESCE(0, 0)) AS curr_coll,
            sum(COALESCE(0, 0)) AS curr_balance,
            sum(COALESCE(arrdd.amount, 0::bigint::double precision)) AS arr_demand,
            sum(COALESCE(arrdd.amt_collected, 0::double precision)) AS arr_coll,
            sum(COALESCE(arrdd.amount - arrdd.amt_collected, 0::double precision)) AS arr_balance
           FROM egwtr_connection con
             JOIN egwtr_connectiondetails cd ON con.id = cd.connection
             JOIN egpt_mv_propertyinfo mvp ON con.propertyidentifier::text = mvp.upicno::text
             JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
             LEFT JOIN eg_demand_details arrdd ON arrdd.id_demand = arrdmd.id
             LEFT JOIN eg_demand_reason dr ON dr.id = arrdd.id_demand_reason
             LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
          WHERE cd.connectionstatus::text = 'ACTIVE'::text AND drm.code::text = 'WTAXCHARGES'::text AND NOT (dr.id_installment IN ( SELECT eim.id
                   FROM eg_installment_master eim
                  WHERE eim.start_date <= now() AND eim.end_date >= now() AND (eim.id_module IN ( SELECT em.id
                           FROM eg_module em
                          WHERE em.name::text = ANY (ARRAY['Water Tax Management'::text, 'Property Tax'::text]))))) AND cd.connectiontype::text = 'METERED'::text
          GROUP BY mvp.upicno, mvp.address, con.consumercode, mvp.ownersname, mvp.houseno, mvp.zoneid, mvp.wardid, mvp.blockid, mvp.localityid, mvp.streetid, mvp.mobileno, cd.connectiontype,cd.connectionstatus
        UNION (
                 SELECT mvp.upicno,
                    mvp.address,
                    con.consumercode AS hscno,
                    mvp.ownersname AS username,
                    mvp.houseno,
                    mvp.zoneid,
                    mvp.wardid,
                    mvp.blockid AS block,
                    mvp.localityid AS locality,
                    mvp.streetid AS street,
                    mvp.mobileno,
                    cd.connectiontype,
                    cd.connectionstatus,
                    sum(COALESCE(currdd.amount, 0::bigint::double precision)) AS curr_demand,
                    sum(COALESCE(currdd.amt_collected, 0::double precision)) AS curr_coll,
                    sum(COALESCE(currdd.amount - currdd.amt_collected, 0::double precision)) AS curr_balance,
                    sum(COALESCE(0, 0)) AS arr_demand,
                    sum(COALESCE(0, 0)) AS arr_coll,
                    sum(COALESCE(0, 0)) AS arr_balance
                   FROM egwtr_connection con
                     JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                     JOIN egpt_mv_propertyinfo mvp ON con.propertyidentifier::text = mvp.upicno::text
                     JOIN eg_demand currdmd ON currdmd.id = cd.demand
                     LEFT JOIN eg_demand_details currdd ON currdd.id_demand = currdmd.id
                     LEFT JOIN eg_demand_reason dr ON dr.id = currdd.id_demand_reason
                     LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
                     LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
                     LEFT JOIN eg_module m ON m.id = im.id_module
                  WHERE cd.connectionstatus::text = 'ACTIVE'::text AND drm.code::text = 'WTAXCHARGES'::text AND drm.isdemand = true AND im.start_date <= now() AND im.end_date >= now() AND m.name::text = 'Property Tax'::text AND cd.connectiontype::text = 'NON_METERED'::text
                  GROUP BY mvp.upicno, mvp.address, con.consumercode, mvp.ownersname, mvp.houseno, mvp.zoneid, mvp.wardid, mvp.blockid, mvp.localityid, mvp.streetid, mvp.mobileno, cd.connectiontype,cd.connectionstatus
                UNION
                 SELECT mvp.upicno,
                    mvp.address,
                    con.consumercode AS hscno,
                    mvp.ownersname AS username,
                    mvp.houseno,
                    mvp.zoneid,
                    mvp.wardid,
                    mvp.blockid AS block,
                    mvp.localityid AS locality,
                    mvp.streetid AS street,
                    mvp.mobileno,
                    cd.connectiontype,
                    cd.connectionstatus,
                    sum(COALESCE(0, 0)) AS curr_demand,
                    sum(COALESCE(0, 0)) AS curr_coll,
                    sum(COALESCE(0, 0)) AS curr_balance,
                    sum(COALESCE(arrdd.amount, 0::bigint::double precision)) AS arr_demand,
                    sum(COALESCE(arrdd.amt_collected, 0::double precision)) AS arr_coll,
                    sum(COALESCE(arrdd.amount - arrdd.amt_collected, 0::double precision)) AS arr_balance
                   FROM egwtr_connection con
                     JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                     JOIN egpt_mv_propertyinfo mvp ON con.propertyidentifier::text = mvp.upicno::text
                     JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
                     JOIN eg_demand_details arrdd ON arrdd.id_demand = arrdmd.id
                     LEFT JOIN eg_demand_reason dr ON dr.id = arrdd.id_demand_reason
                     LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
                  WHERE cd.connectionstatus::text = 'ACTIVE'::text AND drm.code::text = 'WTAXCHARGES'::text AND NOT (dr.id_installment IN ( SELECT eim.id
                           FROM eg_installment_master eim
                          WHERE eim.start_date <= now() AND eim.end_date >= now() AND (eim.id_module IN ( SELECT em.id
                                   FROM eg_module em
                                  WHERE em.name::text = 'Property Tax'::text)))) AND cd.connectiontype::text = 'NON_METERED'::text
                  GROUP BY mvp.upicno, mvp.address, con.consumercode, mvp.ownersname, mvp.houseno, mvp.zoneid, mvp.wardid, mvp.blockid, mvp.localityid, mvp.streetid, mvp.mobileno, cd.connectiontype,cd.connectionstatus
        )) mv
  GROUP BY mv.propertyid, mv.address, mv.hscno, mv.username, mv.houseno, mv.zoneid, mv.wardid, mv.block, mv.locality, mv.street, mv.mobileno, mv.connectiontype,mv.connectionstatus;