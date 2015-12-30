---EGPT_MV_ARREARPENALTYCOLLECTION---
CREATE TABLE EGPT_MV_ARREARPENALTYCOLLECTION  AS
  SELECT sum(collectionDetails.cramount) AS penaltyAmount,
         collectionDetails.collectionheader AS headerId
         FROM egcl_collectiondetails collectionDetails
         left join chartofaccounts accountHeads on collectionDetails.chartofaccount = accountHeads.id 
         left join eg_installment_master installment on upper(collectionDetails.description) LIKE '%'||upper(installment.description) where
         installment.start_date <> (select start_date from eg_installment_master where 
         start_date <= now() and end_date >= now() and id_module in (select id from eg_module where name='Property Tax'))
         and upper(collectionDetails.description) like upper('PENALTY Fines')||'%' 
         and collectionDetails.cramount > 0 and accountHeads.glcode in ('1402002')
         and installment.id_module in (select id from eg_module where name='Property Tax')
         group by collectionDetails.collectionheader;

         
---EGPT_MV_ARREARCOLLECTION---
CREATE TABLE EGPT_MV_ARREARCOLLECTION  AS
  SELECT sum(collectionDetails.cramount) AS arrearAmount,
         collectionDetails.collectionheader AS headerId
         FROM egcl_collectiondetails collectionDetails
         left join chartofaccounts accountHeads on collectionDetails.chartofaccount = accountHeads.id 
         left join eg_installment_master installment on upper(collectionDetails.description) LIKE '%'||upper(installment.description) 
         where installment.start_date <> (select start_date from eg_installment_master where 
         start_date <= now() and end_date >= now() and id_module in (select id from eg_module where name='Property Tax'))
         and upper(collectionDetails.description) not like upper('Library Cess')||'%' 
         and collectionDetails.cramount > 0 and accountHeads.glcode in ('4311004')
         and installment.id_module in (select id from eg_module where name='Property Tax')
         group by collectionDetails.collectionheader;

         
---EGPT_MV_ARREARLIBCOLLECTION---
CREATE TABLE EGPT_MV_ARREARLIBCOLLECTION  AS
  SELECT sum(collectionDetails.cramount) AS libAmount,
         collectionDetails.collectionheader AS headerId
         FROM egcl_collectiondetails collectionDetails
         left join chartofaccounts accountHeads on collectionDetails.chartofaccount = accountHeads.id 
         left join eg_installment_master installment on upper(collectionDetails.description) LIKE '%'||upper(installment.description) where
         installment.start_date <> (select start_date from eg_installment_master where 
         start_date <= now() and end_date >= now() and id_module in (select id from eg_module where name='Property Tax'))
         and upper(collectionDetails.description) like upper('Library Cess')||'%' 
         and collectionDetails.cramount > 0 and accountHeads.glcode in ('4311004')
         and installment.id_module in (select id from eg_module where name='Property Tax')
         group by collectionDetails.collectionheader;

         
---EGPT_MV_CURRENTPENALTYCOLLECTION---
CREATE TABLE EGPT_MV_CURRENTPENALTYCOLLECTION  AS
  SELECT sum(collectionDetails.cramount) AS penaltyAmount,
         collectionDetails.collectionheader AS headerId
         FROM egcl_collectiondetails collectionDetails
         left join chartofaccounts accountHeads on collectionDetails.chartofaccount = accountHeads.id 
         left join eg_installment_master installment on upper(collectionDetails.description) LIKE '%'||upper(installment.description) where
         installment.start_date = (select start_date from eg_installment_master where 
         start_date <= now() and end_date >= now() and id_module in (select id from eg_module where name='Property Tax'))
         and upper(collectionDetails.description) like upper('PENALTY Fines')||'%' 
         and collectionDetails.cramount > 0 and accountHeads.glcode in ('1402002')
         and installment.id_module in (select id from eg_module where name='Property Tax')
         group by collectionDetails.collectionheader;

         
---EGPT_MV_CURRENTCOLLECTION---
CREATE TABLE EGPT_MV_CURRENTCOLLECTION  AS
  SELECT sum(collectionDetails.cramount) AS currentAmount,
         collectionDetails.collectionheader AS headerId
         FROM egcl_collectiondetails collectionDetails
         left join chartofaccounts accountHeads on collectionDetails.chartofaccount = accountHeads.id 
         left join eg_installment_master installment on upper(collectionDetails.description) LIKE '%'||upper(installment.description) where
         installment.start_date = (select start_date from eg_installment_master where 
         start_date <= now() and end_date >= now() and id_module in (select id from eg_module where name='Property Tax'))
         and upper(collectionDetails.description) not like upper('Library Cess')||'%' 
         and collectionDetails.cramount > 0 and accountHeads.glcode in ('1100101','3503002','1402001','4311004')
         and installment.id_module in (select id from eg_module where name='Property Tax')
         group by collectionDetails.collectionheader;

         
---EGPT_MV_CURRENTLIBCOLLECTION---
CREATE TABLE EGPT_MV_CURRENTLIBCOLLECTION  AS
  SELECT sum(collectionDetails.cramount) AS libAmount,
         collectionDetails.collectionheader AS headerId
         FROM egcl_collectiondetails collectionDetails
         left join chartofaccounts accountHeads on collectionDetails.chartofaccount = accountHeads.id 
         left join eg_installment_master installment on upper(collectionDetails.description) LIKE '%'||upper(installment.description) where
         installment.start_date = (select start_date from eg_installment_master where 
         start_date <= now() and end_date >= now() and id_module in (select id from eg_module where name='Property Tax'))
         and upper(collectionDetails.description) like upper('Library Cess')||'%' 
         and collectionDetails.cramount > 0 and accountHeads.glcode in ('3503001')
         and installment.id_module in (select id from eg_module where name='Property Tax')
         group by collectionDetails.collectionheader;

         
---EGPT_MV_COLLECTION---
CREATE TABLE EGPT_MV_COLLECTIONREPORT AS SELECT
      collectionHeader.id AS receiptHeaderId,
      (CASE WHEN instrumentType.TYPE ='cash' THEN 'cash' WHEN instrumentType.TYPE='bank' THEN 'bank' WHEN  instrumentType.TYPE='card' THEN 'card' ELSE 'cheque/dd' END)  AS PAYMENT_MODE,
      collectionHeader.receiptnumber AS RECEIPT_NUMBER,
      collectionHeader.receiptdate AS RECEIPT_DATE,
      collectionHeader.totalamount AS TAX_COLL,
      propertyInfo.upicno AS PROPERTYID,
      propertyInfo.wardid AS WARDID,
      propertyInfo.zoneid AS ZONEID,
      propertyInfo.blockid AS AREAID,
      propertyInfo.localityid AS LOCALITYID,
      propertyInfo.streetid AS streetId,
      property.id AS IDPROPERTY,
      collectionHeader.paidby AS PAYEENAME,
      collectionHeader.collectiontype AS COLLECTIONTYPE,
      userdetail.id AS userId,
      propertyInfo.houseno AS HOUSENUMBER,
      collectionHeader.source AS paidAt,
      status.id AS status
FROM EGPT_PROPERTY property,
     EGPT_MV_PROPERTYINFO propertyInfo,
     EGCL_COLLECTIONHEADER collectionHeader,
     EGCL_SERVICEDETAILS serviceDetails,
     EG_USER userdetail,
     EGW_STATUS status,
     egcl_collectioninstrument instrument,
     egf_instrumentheader instrumentHeader,
     egf_instrumenttype instrumentType
WHERE property.id_basic_property = propertyInfo.BASICPROPERTYID and 
      propertyInfo.upicno = collectionHeader.consumerCode and 
      status.id = collectionHeader.status and
      serviceDetails.id = collectionHeader.servicedetails and
      collectionHeader.createdby = userdetail.id and 
      instrument.collectionheader = collectionHeader.id and 
      instrumentHeader.id = instrument.instrumentheader and 
      instrumentType.id = instrumentHeader.instrumenttype and 
      serviceDetails.name = 'Property Tax' and
      property.status in ('I','A');


---EGPT_MV_COLLECTIONDETAILS---
CREATE TABLE EGPT_MV_COLLECTIONREPORTDETAILS AS SELECT
      collectionHeader.receiptHeaderId AS receiptHeaderId,
      arrearCollection.arrearAmount  AS arreartax_coll,
      currCollection.currentAmount AS currenttax_coll,
      currPenaltyCollection.penaltyAmount AS PENALTY_COLL,
      arrLibCollection.libAmount AS arrearlibrarycess_coll,
      currLibCollection.libAmount AS librarycess_coll,
      arreaPenaltyCollection.penaltyAmount AS arrearpenalty_coll
FROM EGPT_MV_COLLECTIONREPORT collectionHeader 
     left outer join EGPT_MV_ARREARCOLLECTION arrearCollection on collectionHeader.receiptHeaderId = arrearCollection.headerId
     left outer join EGPT_MV_CURRENTCOLLECTION currCollection on collectionHeader.receiptHeaderId = currCollection.headerId
     left outer join EGPT_MV_ARREARPENALTYCOLLECTION arreaPenaltyCollection on collectionHeader.receiptHeaderId = arreaPenaltyCollection.headerId
     left outer join EGPT_MV_ARREARLIBCOLLECTION arrLibCollection on collectionHeader.receiptHeaderId = arrLibCollection.headerId
     left outer join EGPT_MV_CURRENTLIBCOLLECTION currLibCollection on collectionHeader.receiptHeaderId = currLibCollection.headerId
     left outer join EGPT_MV_CURRENTPENALTYCOLLECTION currPenaltyCollection on collectionHeader.receiptHeaderId = currPenaltyCollection.headerId
     group by collectionHeader.receiptHeaderId,arrearCollection.arrearAmount,currCollection.currentAmount,currPenaltyCollection.penaltyAmount,
              arrLibCollection.libAmount,currLibCollection.libAmount,arreaPenaltyCollection.penaltyAmount;