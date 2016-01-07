DROP TABLE EGPT_MV_COLLECTIONREPORT;
DROP TABLE EGPT_MV_COLLECTIONREPORTDETAILS;

---EGPT_MV_COLLECTION---
CREATE TABLE EGPT_MV_COLLECTIONREPORT AS SELECT 
receiptHeaderId,PAYMENT_MODE,RECEIPT_NUMBER,RECEIPT_DATE,TAX_COLL,PROPERTYID,WARDID,
ZONEID,AREAID,LOCALITYID,streetId,IDPROPERTY,PAYEENAME,COLLECTIONTYPE,userId,HOUSENUMBER,paidAt,status from 
      (SELECT  collectionHeader.id AS receiptHeaderId,
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
      property.status in ('I','A')) AS RESULT
      group by RESULT.receiptHeaderId,
      RESULT.PAYMENT_MODE,
      RESULT.RECEIPT_NUMBER,
      RESULT.RECEIPT_DATE,
      RESULT.TAX_COLL,
      RESULT.PROPERTYID,
      RESULT.WARDID,
      RESULT.ZONEID,
      RESULT.AREAID ,
      RESULT.LOCALITYID ,
      RESULT.streetid ,
      RESULT.IDPROPERTY,
      RESULT.PAYEENAME,
      RESULT.COLLECTIONTYPE ,
      RESULT.userId,
      RESULT.HOUSENUMBER ,
      RESULT.paidAt,
      RESULT.status;
      
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