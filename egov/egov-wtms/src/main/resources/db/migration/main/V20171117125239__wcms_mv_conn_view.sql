DROP VIEW IF EXISTS egwtr_mv_conn_view;

CREATE OR REPLACE VIEW egwtr_mv_conn_view AS
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
  propertytype,
  applicationtype,
  connectiondetailsid,
  connectiontype,
  connectionstatus,
  demand,
  executiondate,
  donationcharges,
  legacy,
  approvalnumber,
  workorderdate,
  workordernumber
FROM
  (SELECT mvp.upicno as propertyid,
      mvp.address as address,
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
      prt.name AS propertytype,
      apt.name AS applicationtype,
      cd.id  AS connectiondetailsid,
     cd.connectiontype  AS connectiontype,
    cd.connectionstatus AS connectionstatus,
    dmc.demand AS demand,
    cd.executiondate AS executiondate,
    cd.donationcharges AS donationcharges,
    cd.legacy AS legacy,
    cd.approvalnumber AS approvalnumber,
    cd.workorderdate AS workorderdate,
    cd.workordernumber AS workordernumber
    FROM egwtr_connection con
    JOIN egwtr_connectiondetails cd
    ON con.id = cd.connection
    JOIN egwtr_property_type prt
    ON cd.propertytype = prt.id
    JOIN egwtr_application_type apt
    ON cd.applicationtype = apt.id
    JOIN egpt_mv_propertyinfo mvp
    ON con.propertyidentifier = mvp.upicno
    JOIN egwtr_demand_connection dmc
    ON dmc.connectiondetails = cd.id
    WHERE ((cd.connectionstatus ='ACTIVE'  and cd.statusid=(select id from egw_status where moduletype   ='WATERTAXAPPLICATION' and code ='SANCTIONED')) or (cd.connectionstatus ='INPROGRESS' and  cd.statusid=(select id from egw_status where moduletype ='WATERTAXAPPLICATION' and code ='APPROVED')))
   AND cd.connectiontype = 'NON_METERED' and cd.legacy =false 
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
      prt.name,
      apt.name,
      cd.id,
      cd.connectiontype,
      cd.connectionstatus,
      dmc.demand,
      cd.executiondate ,
      cd.donationcharges ,
      cd.legacy,
      cd.approvalnumber,
      cd.workorderdate,
      cd.workordernumber
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
  propertytype,
  applicationtype,
  connectiondetailsid,
  connectiontype,
  connectionstatus,
  demand,
  executiondate,
  donationcharges,
  legacy,
  approvalnumber,
  workorderdate,
  workordernumber;
                                                                                                                       113,4         47%
