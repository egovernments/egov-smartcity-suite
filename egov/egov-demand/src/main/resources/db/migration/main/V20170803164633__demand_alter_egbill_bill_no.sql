DROP VIEW IF EXISTS egwtr_mv_bill_view;

alter table eg_bill alter column bill_no type varchar(30);

CREATE OR REPLACE VIEW egwtr_mv_bill_view AS
SELECT propertyid,
  address,
  hscno,
  username,
  houseno,
  zoneid,
  wardid,
  block,
  locality,
  street,
  mobileno,
  applicationtype,
  propertytype,
  connectiontype,
  connectionstatus,
  demand,
  demanddocumentnumber
FROM
  (
    (SELECT mvp.upicno   AS propertyid,
      mvp.address        AS address,
      con.consumercode AS hscno,
      mvp.ownersname   AS username,
      mvp.houseno      AS houseno,
      mvp.zoneid       AS zoneid,
      mvp.wardid       AS wardid,
      mvp.blockid      AS block,
      mvp.localityid   AS locality,
      mvp.mobileno     AS mobileno,
      mvp.streetid     AS street,
      apt.name AS applicationtype,
      prt.name AS propertytype,
      cd.connectiontype,
      cd.connectionstatus,
      dmc.demand,
      max(filestore.filestoreid) AS demanddocumentnumber
    FROM egwtr_connection con
    INNER JOIN egwtr_connectiondetails cd
    ON con.id = cd.connection
    INNER JOIN egwtr_application_type apt
    ON cd.applicationtype = apt.id
    INNER JOIN egwtr_property_type prt
    ON cd.propertytype = prt.id
    INNER JOIN egpt_mv_propertyinfo mvp
    ON con.propertyidentifier = mvp.upicno
    INNER JOIN egwtr_demand_connection dmc
    ON dmc.connectiondetails = cd.id
    INNER JOIN eg_demand currdmd
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
  INNER JOIN eg_bill bill
  ON  bill.id_demand =dmc.demand
  INNER JOIN eg_bill_type billtype
  ON billtype.id = bill.id_bill_type
  INNER JOIN egwtr_application_documents appD
  ON cd.id=appD.connectiondetailsid
  INNER JOIN egwtr_documents conndoc
  ON appD.id=conndoc.applicationdocumentsid
  INNER JOIN egwtr_document_names docName
  ON appD.documentnamesid=docName.id
  INNER JOIN eg_filestoremap filestore
  ON filestore.id=conndoc.filestoreid
    WHERE cd.connectionstatus = 'ACTIVE'
    AND drm.code  = 'WTAXCHARGES'
    AND drm.isdemand  = true
    and currdmd.is_history ='N'
    AND cd.connectiontype = 'NON_METERED'
    AND bill.is_cancelled='N'
    AND billtype.code='MANUAL'
    AND docName.documentname='DemandBill'
    AND bill.service_code='WT'
    GROUP BY mvp.upicno,
      mvp.address,
      con.consumercode ,
      mvp.ownersname ,
      mvp.houseno,
      mvp.zoneid,
      mvp.wardid,
      mvp.blockid,
      mvp.localityid,
      mvp.mobileno,
      mvp.streetid ,
      apt.name,
      prt.name,
      cd.connectiontype,
      cd.connectionstatus,
      dmc.demand
    )
  ) mv
GROUP BY propertyid,
  address,
  hscno,
  username,
  houseno,
  zoneid,
  wardid,
  block,
  locality,
  mobileno,
  street,
  applicationtype,
  propertytype,
  connectiontype,
  connectionstatus,
  demand,
  demanddocumentnumber;
