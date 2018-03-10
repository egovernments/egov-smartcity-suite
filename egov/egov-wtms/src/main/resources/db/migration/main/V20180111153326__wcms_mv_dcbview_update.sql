
drop table if exists egwtr_mv_dcb_view;
CREATE  TABLE egwtr_mv_dcb_view AS
SELECT propertyid,
  address,
  hscno,
  oldhscno,
  username,
  houseno,
  zoneid,
  wardid,
  block,
  locality,
  street,
  mobileno,
  aadharno,
  pt_firsthalf_demand,
  pt_secondhalf_demand,
  pt_firsthalf_collection,
  pt_secondhalf_collection,
  propertytype,
  applicationtype,
  usagetype,
  categorytype,
  pipesize,
  watersource,
  connectiondetailsid,
  connectiontype,
  connectionstatus,
  demand,
  numberofperson,
  numberofrooms,
  sumpcapacity,
  executiondate,
  donationcharges,
  legacy,
  approvalnumber,
  advance_coll,
  due_period,
  workorderdate,
  workordernumber,
  SUM( curr_demand) AS curr_demand,
  SUM(curr_coll) AS curr_coll,
  SUM(curr_balance) AS curr_balance,
  SUM(arr_demand) AS arr_demand,
  SUM(arr_coll) AS arr_coll,
  SUM(arr_balance) AS arr_balance
FROM
  (SELECT distinct mvp.upicno   AS propertyid,
    mvp.address        AS address,
    con.consumercode   AS hscno,
    con.oldConsumerNumber   AS oldhscno,
    mvp.ownersname     AS username,
    mvp.houseno        AS houseno,
    mvp.zoneid         AS zoneid,
    mvp.wardid         AS wardid,
    mvp.blockid        AS block,
    mvp.localityid     AS locality,
    mvp.mobileno       AS mobileno,
    mvp.streetid       AS street,
    mvp.aadharno AS aadharno,
    mvp.aggregate_current_firsthalf_demand AS pt_firsthalf_demand,
    mvp.aggregate_current_secondhalf_demand AS pt_secondhalf_demand,
    mvp.current_firsthalf_collection AS pt_firsthalf_collection,
    mvp.current_secondhalf_collection AS pt_secondhalf_collection,
    prt.name AS propertytype,
    apt.name AS applicationtype,
    ut.name AS usagetype,
    cat.name AS categorytype,
    ps.code AS pipesize,
    wrsc.watersourcetype AS watersource,
    cd.id  AS connectiondetailsid,
    cd.connectiontype  AS connectiontype,
    cd.connectionstatus AS connectionstatus,
    dmc.demand AS demand,
    cd.numberofperson AS numberofperson,
    cd.numberofrooms AS numberofrooms,
    cd.sumpcapacity AS sumpcapacity,
    cd.executiondate AS executiondate,
    cd.donationcharges AS donationcharges,
    cd.legacy AS legacy,
    cd.approvalnumber AS approvalnumber,
    cd.workorderdate AS workorderdate,
    cd.workordernumber AS workordernumber,
    0 as advance_coll,
    null as due_period,
    SUM(currdd.amount)  AS curr_demand,
    SUM(currdd.amt_collected) AS curr_coll,
    SUM(currdd.amount::DOUBLE PRECISION - currdd.amt_collected) AS curr_balance,
    SUM(COALESCE(0, 0))::DOUBLE PRECISION AS arr_demand,
    SUM(COALESCE(0, 0))::DOUBLE PRECISION AS arr_coll,
    SUM(COALESCE(0, 0))::DOUBLE PRECISION AS arr_balance
  FROM egwtr_connection con
  JOIN egwtr_connectiondetails cd
  ON con.id = cd.connection
  JOIN egwtr_property_type prt
  ON cd.propertytype = prt.id
  JOIN egwtr_application_type apt
  ON cd.applicationtype = apt.id
  JOIN egwtr_usage_type  ut
  ON cd.usagetype = ut.id
  JOIN egwtr_category  cat
  ON cd.category = cat.id
  JOIN egwtr_pipesize   ps
  ON cd.pipesize = ps.id
  JOIN egwtr_water_source wrsc
  ON cd.watersource = wrsc.id
  JOIN egpt_mv_propertyinfo mvp
  ON con.propertyidentifier = mvp.upicno
  JOIN egwtr_demand_connection dmc
  ON dmc.connectiondetails = cd.id
  JOIN eg_demand currdmd
  ON currdmd.id = dmc.demand
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
  WHERE cd.connectionstatus in ('ACTIVE','INACTIVE','CLOSED','HOLDING','DISCONNECTED')
  AND drm.code             = 'METERCHARGES'
  AND drm.isdemand                = true
  AND m.name               = 'Water Tax Management'
  AND im.installment_type   = 'Monthly'
  AND cd.connectiontype     = 'METERED'
  GROUP BY mvp.upicno,
    mvp.address,
    con.consumercode ,
    con.oldConsumerNumber ,
    mvp.ownersname ,
    mvp.houseno,
    mvp.zoneid,
    mvp.wardid,
    mvp.blockid,
    mvp.localityid,
    mvp.mobileno,
    mvp.streetid ,
    mvp.aadharno,
    mvp.aggregate_current_firsthalf_demand,
    mvp.aggregate_current_secondhalf_demand,
    mvp.current_firsthalf_collection,
    mvp.current_secondhalf_collection,
    prt.name ,
    apt.name ,
    ut.name ,
    cat.name,
    ps.code ,
    wrsc.watersourcetype,
    cd.id,
    cd.connectiontype,
    cd.connectionstatus,
    dmc.demand,
    cd.numberofperson ,
    cd.numberofrooms ,
    cd.sumpcapacity ,
    cd.executiondate,
    cd.donationcharges,
    cd.legacy,
    cd.approvalnumber,
    cd.workorderdate,
    cd.workordernumber
  UNION
    (SELECT mvp.upicno,
      mvp.address,
      con.consumercode AS hscno,
      con.oldConsumerNumber   AS oldhscno,
      mvp.ownersname   AS username,
      mvp.houseno      AS houseno,
      mvp.zoneid       AS zoneid,
      mvp.wardid       AS wardid,
      mvp.blockid      AS block,
      mvp.localityid   AS locality,
      mvp.mobileno     AS mobileno,
      mvp.streetid     AS street,
      mvp.aadharno AS aadharno,
      mvp.aggregate_current_firsthalf_demand AS pt_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand AS pt_secondhalf_demand,
      mvp.current_firsthalf_collection AS pt_firsthalf_collection,
      mvp.current_secondhalf_collection AS pt_secondhalf_collection,
      prt.name AS propertytype,
      apt.name AS applicationtype,
      ut.name AS usagetype,
      cat.name AS categorytype,
      ps.code AS pipesize,
      wrsc.watersourcetype AS watersource,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      dmc.demand,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate,
      cd.donationcharges,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber,
      0 as advance_coll,
      null as due_period,
      SUM(COALESCE(currdd.amount, 0::bigint))                                                    AS curr_demand,
      SUM(COALESCE(currdd.amt_collected, 0::DOUBLE PRECISION))                                   AS curr_coll,
      SUM(COALESCE(currdd.amount::DOUBLE PRECISION - currdd.amt_collected, 0::DOUBLE PRECISION)) AS curr_balance,
      SUM(COALESCE(0, 0))::DOUBLE PRECISION AS arr_demand,
      SUM(COALESCE(0, 0))::DOUBLE PRECISION AS arr_coll,
      SUM(COALESCE(0, 0))::DOUBLE PRECISION AS arr_balance
    FROM egwtr_connection con
    JOIN egwtr_connectiondetails cd
    ON con.id = cd.connection
    JOIN egwtr_property_type prt
    ON cd.propertytype = prt.id
    JOIN egwtr_application_type apt
    ON cd.applicationtype = apt.id
    JOIN egwtr_usage_type  ut
    ON cd.usagetype = ut.id
    JOIN egwtr_category  cat
    ON cd.category = cat.id
    JOIN egwtr_pipesize   ps
    ON cd.pipesize = ps.id
    JOIN egwtr_water_source wrsc
    ON cd.watersource = wrsc.id
    JOIN egpt_mv_propertyinfo mvp
    ON con.propertyidentifier = mvp.upicno
    JOIN egwtr_demand_connection dmc
    ON dmc.connectiondetails = cd.id
    JOIN eg_demand currdmd
    ON currdmd.id = dmc.demand
    LEFT JOIN eg_demand_details currdd
    ON currdd.id_demand = currdmd.id
    LEFT JOIN eg_demand_reason dr
    ON dr.id = currdd.id_demand_reason
    LEFT JOIN eg_demand_reason_master drm
    ON drm.id = dr.id_demand_reason_master
    LEFT JOIN eg_installment_master im
    ON im.id = dr.id_installment
    LEFT JOIN eg_module m
    ON m.id  = im.id_module
    WHERE cd.connectionstatus in ('ACTIVE','INACTIVE','CLOSED','HOLDING','DISCONNECTED')
    AND drm.code  = 'WTAXCHARGES'
    AND drm.isdemand  = true
    and currdmd.is_history ='N'
    and exists (select inst.* from eg_installment_master inst, financialyear finyear where dr.id_installment=inst.id and inst.id_module=(SELECT id FROM eg_module WHERE name = 'Property Tax') and cast(now() as date) between finyear.startingdate and finyear.endingdate and cast(inst.start_date as date)>=finyear.startingdate and cast(inst.end_date as date)<=finyear.endingdate)
    AND cd.connectiontype = 'NON_METERED'
    GROUP BY mvp.upicno,
      mvp.address,
      con.consumercode ,
      con.oldConsumerNumber ,
      mvp.ownersname ,
      mvp.houseno,
      mvp.zoneid,
      mvp.wardid,
      mvp.blockid,
      mvp.localityid,
      mvp.mobileno,
      mvp.streetid ,
      mvp.aadharno,
      mvp.aggregate_current_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand,
      mvp.current_firsthalf_collection,
      mvp.current_secondhalf_collection,
      prt.name,
      apt.name,
      ut.name ,
      cat.name ,
      ps.code ,
      wrsc.watersourcetype ,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      dmc.demand,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate ,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber
  UNION
    (SELECT mvp.upicno,
      mvp.address,
      con.consumercode AS hscno,
      con.oldConsumerNumber   AS oldhscno,
      mvp.ownersname   AS username,
      mvp.houseno      AS houseno,
      mvp.zoneid       AS zoneid,
      mvp.wardid       AS wardid,
      mvp.blockid      AS block,
      mvp.localityid   AS locality,
      mvp.mobileno     AS mobileno,
      mvp.streetid     AS street,
      mvp.aadharno AS aadharno,
      mvp.aggregate_current_firsthalf_demand AS pt_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand AS pt_secondhalf_demand,
      mvp.current_firsthalf_collection AS pt_firsthalf_collection,
      mvp.current_secondhalf_collection AS pt_secondhalf_collection,
      prt.name AS propertytype,
      apt.name AS applicationtype,
      ut.name AS usagetype,
      cat.name AS categorytype,
      ps.code AS pipesize,
      wrsc.watersourcetype AS watersource,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      dmc.demand as demand,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber,
      0 as advance_coll,
      null as due_period,
      0 AS curr_demand,
      0 AS curr_coll,
      0 AS curr_balance,
      0 AS arr_demand,
      0 AS arr_coll,
      0 AS arr_balance
    FROM egwtr_connection con
    JOIN egwtr_connectiondetails cd
    ON con.id = cd.connection
    JOIN egwtr_property_type prt
    ON cd.propertytype = prt.id
    JOIN egwtr_application_type apt
    ON cd.applicationtype = apt.id
    JOIN egwtr_usage_type  ut
    ON cd.usagetype = ut.id
    JOIN egwtr_category  cat
    ON cd.category = cat.id
    JOIN egwtr_pipesize   ps
    ON cd.pipesize = ps.id
    JOIN egwtr_water_source wrsc
    ON cd.watersource = wrsc.id
    JOIN egpt_mv_propertyinfo mvp
    ON con.propertyidentifier = mvp.upicno
    JOIN egwtr_demand_connection dmc
    ON dmc.connectiondetails = cd.id
    JOIN eg_demand currdmd
    ON currdmd.id = dmc.demand
    LEFT JOIN eg_demand_details currdd
    ON currdd.id_demand = currdmd.id
    LEFT JOIN eg_demand_reason dr
    ON dr.id = currdd.id_demand_reason
    LEFT JOIN eg_demand_reason_master drm
    ON drm.id = dr.id_demand_reason_master
    LEFT JOIN eg_installment_master im
    ON im.id = dr.id_installment
    LEFT JOIN eg_module m
    ON m.id  = im.id_module
    WHERE cd.connectionstatus ='INPROGRESS'
    AND drm.code != 'WTAXCHARGES'
    AND drm.isdemand  = true
    and currdmd.is_history ='N'
    AND cd.connectiontype = 'NON_METERED' and cd.legacy =false and cd.statusid=(select id from egw_status where moduletype ='WATERTAXAPPLICATION' and code ='APPROVED')
   GROUP BY mvp.upicno,
      mvp.address,
      con.consumercode ,
      con.oldConsumerNumber ,
      mvp.ownersname ,
      mvp.houseno,
      mvp.zoneid,
      mvp.wardid,
      mvp.blockid,
      mvp.localityid,
      mvp.mobileno,
      mvp.streetid ,
      mvp.aadharno,
      mvp.aggregate_current_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand,
      mvp.current_firsthalf_collection,
      mvp.current_secondhalf_collection,
      prt.name,
      apt.name,
      ut.name ,
      cat.name,
      ps.code,
      wrsc.watersourcetype ,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      dmc.demand,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate ,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber
  UNION
    ( SELECT mvp.upicno,
      mvp.address,
      con.consumercode AS hscno,
      con.oldConsumerNumber   AS oldhscno,
      mvp.ownersname   AS username,
      mvp.houseno      AS houseno,
      mvp.zoneid       AS zoneid,
      mvp.wardid       AS wardid,
      mvp.blockid      AS block,
      mvp.localityid   AS locality,
      mvp.mobileno     AS mobileno,
      mvp.streetid     AS street,
      mvp.aadharno AS aadharno,
      mvp.aggregate_current_firsthalf_demand AS pt_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand AS pt_secondhalf_demand,
      mvp.current_firsthalf_collection AS pt_firsthalf_collection,
      mvp.current_secondhalf_collection AS pt_secondhalf_collection,
      prt.name AS propertytype,
      apt.name AS applicationtype,
      ut.name AS usagetype,
      cat.name AS categorytype,
      ps.code AS pipesize,
      wrsc.watersourcetype AS watersource,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      0 as demand,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber,
      0 as advance_coll,
     null as due_period,
      0 AS curr_demand,
      0 AS curr_coll,
      0 AS curr_balance,
      0 AS arr_demand,
      0 AS arr_coll,
      0 AS arr_balance
    FROM egwtr_connection con
    JOIN egwtr_connectiondetails cd
    ON con.id = cd.connection
    JOIN egwtr_property_type prt
    ON cd.propertytype = prt.id
    JOIN egwtr_application_type apt
    ON cd.applicationtype = apt.id
    JOIN egwtr_usage_type  ut
    ON cd.usagetype = ut.id
    JOIN egwtr_category  cat
    ON cd.category = cat.id
    JOIN egwtr_pipesize   ps
    ON cd.pipesize = ps.id
    JOIN egwtr_water_source wrsc
    ON cd.watersource = wrsc.id
    JOIN egpt_mv_propertyinfo mvp
    ON con.propertyidentifier = mvp.upicno
    WHERE cd.connectionstatus in ('ACTIVE','INACTIVE','CLOSED','HOLDING','DISCONNECTED')
    AND cd.connectiontype = 'METERED'
    AND not exists (select egwtr_demand_connection.* from egwtr_demand_connection where egwtr_demand_connection.connectiondetails=cd.id)
    GROUP BY mvp.upicno,
      mvp.address,
      con.consumercode ,
      con.oldConsumerNumber ,
      mvp.ownersname ,
      mvp.houseno,
      mvp.zoneid,
      mvp.wardid,
      mvp.blockid,
      mvp.localityid,
      mvp.mobileno,
      mvp.streetid ,
      mvp.aadharno,
      mvp.aggregate_current_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand,
      mvp.current_firsthalf_collection,
      mvp.current_secondhalf_collection,
      prt.name,
      apt.name,
      ut.name ,
      cat.name,
      ps.code,
      wrsc.watersourcetype ,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate ,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber
    UNION
    SELECT mvp.upicno,
      mvp.address,
      con.consumercode AS hscno,
      con.oldConsumerNumber   AS oldhscno,
      mvp.ownersname   AS username,
      mvp.houseno      AS houseno,
      mvp.zoneid       AS zoneid,
      mvp.wardid       AS wardid,
      mvp.blockid      AS block,
      mvp.localityid   AS locality,
      mvp.mobileno     AS mobileno,
      mvp.streetid     AS street,
      mvp.aadharno AS aadharno,
      mvp.aggregate_current_firsthalf_demand AS pt_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand AS pt_secondhalf_demand,
      mvp.current_firsthalf_collection AS pt_firsthalf_collection,
      mvp.current_secondhalf_collection AS pt_secondhalf_collection,
      prt.name AS propertytype,
      apt.name AS applicationtype,
      ut.name AS usagetype,
      cat.name AS categorytype,
      ps.code AS pipesize,
      wrsc.watersourcetype AS watersource,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      NULL as demand,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber,
      0 as advance_coll,
     null as due_period,
      0 AS curr_demand,
      0 AS curr_coll,
      0 AS curr_balance,
      0 AS arr_demand,
      0 AS arr_coll,
      0 AS arr_balance
    FROM egwtr_connection con
    JOIN egwtr_connectiondetails cd
    ON con.id = cd.connection
    JOIN egwtr_property_type prt
    ON cd.propertytype = prt.id
    JOIN egwtr_application_type apt
    ON cd.applicationtype = apt.id
    JOIN egwtr_usage_type  ut
    ON cd.usagetype = ut.id
    JOIN egwtr_category  cat
    ON cd.category = cat.id
    JOIN egwtr_pipesize   ps
    ON cd.pipesize = ps.id
    JOIN egwtr_water_source wrsc
    ON cd.watersource = wrsc.id
    JOIN egpt_mv_propertyinfo mvp
    ON con.propertyidentifier = mvp.upicno
    WHERE cd.connectionstatus in ('ACTIVE','INACTIVE','CLOSED','HOLDING','DISCONNECTED')
    AND cd.connectiontype = 'NON_METERED'
    AND not exists (select egwtr_demand_connection.* from egwtr_demand_connection where egwtr_demand_connection.connectiondetails=cd.id)
    GROUP BY mvp.upicno,
      mvp.address,
      con.consumercode ,
      con.oldConsumerNumber ,
      mvp.ownersname ,
      mvp.houseno,
      mvp.zoneid,
      mvp.wardid,
      mvp.blockid,
      mvp.localityid,
      mvp.mobileno,
      mvp.streetid ,
      mvp.aadharno,
      mvp.aggregate_current_firsthalf_demand,
      mvp.aggregate_current_secondhalf_demand,
      mvp.current_firsthalf_collection,
      mvp.current_secondhalf_collection,
      prt.name,
      apt.name,
      ut.name ,
      cat.name,
      ps.code,
      wrsc.watersourcetype ,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      cd.numberofperson ,
      cd.numberofrooms ,
      cd.sumpcapacity ,
      cd.executiondate ,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber
    )
  ) 
 )
) mv
GROUP BY propertyid,
  address,
  hscno,
  oldhscno,
  username,
  houseno,
  zoneid,
  wardid,
  block,
  locality,
  mobileno,
  street,
  aadharno,
  pt_firsthalf_demand,
  pt_secondhalf_demand,
  pt_firsthalf_collection,
  pt_secondhalf_collection,
  propertytype,
  applicationtype,
  usagetype,
  categorytype,
  pipesize,
  watersource,
  connectiondetailsid,
  connectiontype,
  connectionstatus,
  demand,
  numberofperson,
  numberofrooms,
  sumpcapacity,
  executiondate,
  donationcharges,
  legacy,
  approvalnumber,
  workorderdate,
  workordernumber,
  advance_coll,
  due_period;
  
create index idx_egwtrview_hscno on egwtr_mv_dcb_view(hscno);
create index idx_egwtrview_conndetailid on egwtr_mv_dcb_view(connectiondetailsid);
