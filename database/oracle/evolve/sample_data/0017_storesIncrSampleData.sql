#UP



 Insert into EG_POSITION_HIR
   (ID, POSITION_FROM, POSITION_TO, OBJECT_TYPE_ID)
 Values
   (SEQ_POSITION_HIR.nextval, (SELECT e.POS_ID  FROM eg_eis_employeeinfo e WHERE e.CODE=100), (select id from eg_position where position_name like 'Sr Engg'), (select id from eg_object_type where type='StockOpeningBalance'));


INSERT INTO EG_NUMBER_GENERIC (   ID, OBJECTTYPE, VALUE,    UPDATEDTIMESTAMP) 
VALUES (SEQ_EG_NUMBER_GENERIC.NEXTVAL ,'PO-AAAA-2009-10' ,0 ,sysdate );
#DOWN
