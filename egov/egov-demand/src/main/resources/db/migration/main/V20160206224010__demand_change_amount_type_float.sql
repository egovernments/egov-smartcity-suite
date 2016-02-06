drop view egwtr_mv_dcb_view;

alter table eg_demand_details alter column amount type double precision using (amount::double precision);
alter table eg_demand alter column base_demand type double precision using (base_demand::double precision);

/*Just recreating this as its not allowed to change type of a column used in view*/
------------------START------------------
CREATE VIEW EGWTR_MV_DCB_VIEW AS (
                                    (SELECT bp.propertyid,
                                            bpaddr.address AS address,
                                            con.consumercode AS hscno,
                                            ownername(bp.id) AS username,
                                            propid.zone_num AS zoneid,
                                            propid.WARD_ADM_ID AS wardid,
                                            propid.ADM1 AS block,
                                            propid.ADM2 AS locality,
                                            propid.ADM3 AS street,
                                            cd.connectiontype AS connectiontype,
                                            currdd.amount AS curr_demand,
                                            currdd.amt_collected AS curr_coll,
                                            currdd.amount -currdd.amt_collected AS curr_balance,
                                            coalesce(0,'0') AS arr_demand,
                                            coalesce(0,'0') AS arr_coll,
                                            coalesce(0,'0') AS arr_balance
                                     FROM egwtr_connection con
                                     INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                                     INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
                                     INNER JOIN eg_demand currdmd ON currdmd.id = cd.demand
                                     INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
                                     INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
                                     INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
                                     LEFT JOIN eg_demand_details currdd ON currdd.id_demand =currdmd.id
                                     WHERE cd.connectionstatus = 'ACTIVE'
                                       AND currdd.id_demand_reason IN
                                         (SELECT id
                                          FROM eg_demand_reason
                                          WHERE id_demand_reason_master =
                                              (SELECT id
                                               FROM eg_demand_reason_master
                                               WHERE code='WTAXCHARGES'
                                                 AND isdemand=TRUE)
                                            AND id_installment=
                                              (SELECT id
                                               FROM eg_installment_master
                                               WHERE eg_installment_master.start_date <= now()
                                                 AND eg_installment_master.end_date >= now()
                                                 AND eg_installment_master.id_module = (
                                                                                          (SELECT eg_module.id
                                                                                           FROM eg_module
                                                                                           WHERE eg_module.name::text = 'Water Tax Management'::text))
                                                 AND eg_installment_master.installment_type='Monthly'))
                                       AND cd.connectiontype='METERED')
                                  UNION
                                    (SELECT bp.propertyid,
                                            bpaddr.address AS address,
                                            con.consumercode AS hscno,
                                            ownername(bp.id) AS username,
                                            propid.zone_num AS zoneid,
                                            propid.WARD_ADM_ID AS wardid,
                                            propid.ADM1 AS block,
                                            propid.ADM2 AS locality,
                                            propid.ADM3 AS street,
                                            cd.connectiontype AS connectiontype,
                                            coalesce(0,'0') AS curr_demand,
                                            coalesce(0,'0') AS curr_coll,
                                            coalesce(0,'0') AS curr_balance,
                                            coalesce(arrdd.amount,'0') AS arr_demand,
                                            coalesce(arrdd.amt_collected,'0') AS arr_coll,
                                            coalesce(arrdd.amount -arrdd.amt_collected,'0') AS arr_balance
                                     FROM egwtr_connection con
                                     INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
                                     INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
                                     INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
                                     INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
                                     INNER JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
                                     INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
                                     INNER JOIN eg_demand_details arrdd ON arrdd.id_demand =arrdmd.id
                                     WHERE cd.connectionstatus = 'ACTIVE'
                                       AND arrdd.id_demand_reason IN
                                         (SELECT id
                                          FROM eg_demand_reason
                                          WHERE id_demand_reason_master =
                                              (SELECT id
                                               FROM eg_demand_reason_master
                                               WHERE code='WTAXCHARGES')
                                            AND id_installment NOT IN
                                              (SELECT id
                                               FROM eg_installment_master
                                               WHERE eg_installment_master.start_date <= now()
                                                 AND eg_installment_master.end_date >= now()
                                                 AND eg_installment_master.id_module IN
                                                   (SELECT eg_module.id
                                                    FROM eg_module
                                                    WHERE eg_module.name::text IN('Water Tax Management'::text,
                                                                                  'Property Tax'::text))))
                                       AND cd.connectiontype='METERED'))
UNION (
         (SELECT bp.propertyid,
                 bpaddr.address AS address,
                 con.consumercode AS hscno,
                 ownername(bp.id) AS username,
                 propid.zone_num AS zoneid,
                 propid.WARD_ADM_ID AS wardid,
                 propid.ADM1 AS block,
                 propid.ADM2 AS locality,
                 propid.ADM3 AS street,
                 cd.connectiontype AS connectiontype,
                 coalesce(currdd.amount,'0') AS curr_demand,
                 coalesce(currdd.amt_collected,'0') AS curr_coll,
                 coalesce(currdd.amount -currdd.amt_collected,'0') AS curr_balance,
                 coalesce(0,'0') AS arr_demand,
                 coalesce(0,'0') AS arr_coll,
                 coalesce(0,'0') AS arr_balance
          FROM egwtr_connection con
          INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
          INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
          INNER JOIN eg_demand currdmd ON currdmd.id = cd.demand
          INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
          INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
          INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
          LEFT JOIN eg_demand_details currdd ON currdd.id_demand =currdmd.id
          WHERE cd.connectionstatus = 'ACTIVE'
            AND currdd.id_demand_reason IN
              (SELECT id
               FROM eg_demand_reason
               WHERE id_demand_reason_master =
                   (SELECT id
                    FROM eg_demand_reason_master
                    WHERE code='WTAXCHARGES'
                      AND isdemand=TRUE)
                 AND id_installment=
                   (SELECT id
                    FROM eg_installment_master
                    WHERE eg_installment_master.start_date <= now()
                      AND eg_installment_master.end_date >= now()
                      AND eg_installment_master.id_module = (
                                                               (SELECT eg_module.id
                                                                FROM eg_module
                                                                WHERE eg_module.name::text = 'Property Tax'::text))))
            AND cd.connectiontype='NON_METERED')
       UNION
         (SELECT bp.propertyid,
                 bpaddr.address AS address,
                 con.consumercode AS hscno,
                 ownername(bp.id) AS username,
                 propid.zone_num AS zoneid,
                 propid.WARD_ADM_ID AS wardid,
                 propid.ADM1 AS block,
                 propid.ADM2 AS locality,
                 propid.ADM3 AS street,
                 cd.connectiontype AS connectiontype,
                 coalesce(0,'0') AS curr_demand,
                 coalesce(0,'0') AS curr_coll,
                 coalesce(0,'0') AS curr_balance,
                 coalesce(arrdd.amount,'0') AS arr_demand,
                 coalesce(arrdd.amt_collected,'0') AS arr_coll,
                 coalesce(arrdd.amount -arrdd.amt_collected,'0') AS arr_balance
          FROM egwtr_connection con
          INNER JOIN egwtr_connectiondetails cd ON con.id = cd.connection
          INNER JOIN egpt_basic_property bp ON con.propertyidentifier = bp.propertyid
          INNER JOIN egpt_property_owner_info propinfo ON propinfo.basicproperty=bp.id
          INNER JOIN egpt_propertyid propid ON bp.id_propertyid=propid.id
          INNER JOIN eg_demand arrdmd ON arrdmd.id = cd.demand
          INNER JOIN egpt_mv_bp_address bpaddr ON bpaddr.id_basic_property =bp.id
          INNER JOIN eg_demand_details arrdd ON arrdd.id_demand =arrdmd.id
          WHERE cd.connectionstatus = 'ACTIVE'
            AND arrdd.id_demand_reason IN
              (SELECT id
               FROM eg_demand_reason
               WHERE id_demand_reason_master =
                   (SELECT id
                    FROM eg_demand_reason_master
                    WHERE code='WTAXCHARGES')
                 AND id_installment NOT IN
                   (SELECT id
                    FROM eg_installment_master
                    WHERE eg_installment_master.start_date <= now()
                      AND eg_installment_master.end_date >= now()
                      AND eg_installment_master.id_module IN
                        (SELECT eg_module.id
                         FROM eg_module
                         WHERE eg_module.name::text IN('Property Tax'::text))))
            AND cd.connectiontype='NON_METERED'));
-------------------END-------------------
