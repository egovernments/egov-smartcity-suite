#UP


/***** master data for Chennai customization  *****/

UPDATE CHARTOFACCOUNTS SET purposeid=16 WHERE id 
IN( SELECT id FROM CHARTOFACCOUNTS WHERE glcode LIKE'412%' AND TYPE='A' AND classification=4); 

UPDATE CHARTOFACCOUNTS SET purposeid=15 WHERE id 
IN( SELECT id FROM CHARTOFACCOUNTS WHERE glcode LIKE'411%' AND TYPE='A' AND classification=4); 

UPDATE CHARTOFACCOUNTS SET purposeid=17 WHERE id 
IN( SELECT id FROM CHARTOFACCOUNTS WHERE glcode LIKE'272%' AND TYPE='E' AND classification=4); 

UPDATE CHARTOFACCOUNTS SET purposeid=18 WHERE id 
IN( SELECT id FROM CHARTOFACCOUNTS WHERE glcode LIKE'312%' AND TYPE='L' AND classification=4); 

UPDATE CHARTOFACCOUNTS SET purposeid=19 WHERE id 
IN( SELECT id FROM CHARTOFACCOUNTS WHERE glcode LIKE '410%' AND classification=4); 



UPDATE CHARTOFACCOUNTS SET purposeid=20 WHERE classification=4 AND glcode='2728000';


/********************  UOM Sample Data  *******************/


Insert into EG_UOMCATEGORY
   (ID, CATEGORY, NARRATION, LASTMODIFIED, CREATEDDATE, CREATEDBY, LASTMODIFIEDBY)
 Values
   (SEQ_EG_UOMCATEGORY.nextval, 'AREA', 'area', SYSDATE, SYSDATE, (select id_user from EG_USER where user_name='egovernments'), (select id_user from EG_USER where user_name='egovernments'));

Insert into EG_UOMCATEGORY
   (ID, CATEGORY, NARRATION, LASTMODIFIED, CREATEDDATE, CREATEDBY, LASTMODIFIEDBY)
 Values
   (SEQ_EG_UOMCATEGORY.nextval, 'Length', 'Length', TO_TIMESTAMP('11/8/2008 7:25:11.000000 AM','fmMMfm/fmDDfm/YYYY fmHH12fm:MI:SS.FF AM'), TO_DATE('11/08/2008 07:25:11', 'MM/DD/YYYY HH24:MI:SS'), (select id_user from EG_USER where user_name='egovernments'), (select id_user from EG_USER where user_name='egovernments'));


Insert into EG_UOM
   (ID, UOMCATEGORYID, UOM, NARRATION, CONV_FACTOR, BASEUOM, LASTMODIFIED, CREATEDDATE, CREATEDBY, LASTMODIFIEDBY)
 Values
   (SEQ_EG_UOM.nextval, (SELECT  id FROM   EG_UOMCATEGORY WHERE category='AREA' ) , 'SqFt', 'SqFt', 1, 1, SYSDATE,SYSDATE, (select id_user from EG_USER where user_name='egovernments'), (select id_user from EG_USER where user_name='egovernments'));

Insert into EG_UOM
   (ID, UOMCATEGORYID, UOM, NARRATION, CONV_FACTOR, BASEUOM, LASTMODIFIED, CREATEDDATE, CREATEDBY, LASTMODIFIEDBY)
 Values
   (SEQ_EG_UOM.nextval, (SELECT  id FROM   EG_UOMCATEGORY WHERE category='Length' ) , 'MTR', 'MTR', 1, 1, SYSDATE,SYSDATE, (select id_user from EG_USER where user_name='egovernments'), (select id_user from EG_USER where user_name='egovernments'));



/********************  Asset Sample Data  *******************/

  INSERT INTO EG_ASSETCATEGORY
     (ID, CODE, NAME, MAXLIFE, ASSETCODE, ACCDEPCODE, DEPEXPCODE, REVCODE, 
     DEPMETHORD, ASSETTYPE_ID, uom_id, created_by, modified_by, 
     created_date, modified_date)
   VALUES
     (SEQ_EG_ASSETCATEGORY.NEXTVAL, 'BUILD', 'BUILDING', 50, 921, 963,722, 
     765, 1, (SELECT  id  FROM  EG_ASSET_TYPE WHERE name LIKE 'ImmovableAsset'),
     (SELECT ID FROM EG_UOM WHERE UOM='SqFt'),1,1,SYSDATE,SYSDATE);

INSERT INTO EG_ASSETCATEGORY
     (ID, CODE, NAME, MAXLIFE, ASSETCODE, ACCDEPCODE, DEPEXPCODE, REVCODE, 
     DEPMETHORD, ASSETTYPE_ID, uom_id, created_by, modified_by, 
     created_date, modified_date)
   VALUES
     (SEQ_EG_ASSETCATEGORY.NEXTVAL, 'ROADS', 'Roads', 33, 930, 972, 723, 765,
     1, (SELECT  id  FROM  EG_ASSET_TYPE WHERE name LIKE 'ImmovableAsset'),
     (SELECT ID FROM EG_UOM WHERE UOM='MTR'),1,1,SYSDATE,SYSDATE);


 INSERT INTO EG_ASSET
    (ID, CODE, STATUSID, DESCRIPTION,  CATEGORYID, MODEOFACQUISITION, 
    NAME,created_by, modified_by, created_date, modified_date)
  VALUES
    (SEQ_EG_ASSET.NEXTVAL, 'CommBuild', (SELECT id FROM EGW_STATUS WHERE moduletype='ASSET' 
    AND description IN('Capitalized')), 'Commercial Building', (SELECT id FROM EG_ASSETCATEGORY 
    WHERE code='BUILD') , '1','Commercial Building',1,1,SYSDATE,SYSDATE);

  INSERT INTO EG_ASSET
     (ID, CODE, STATUSID, DESCRIPTION, CATEGORYID, MODEOFACQUISITION, 
     NAME,created_by, modified_by, created_date, modified_date)
   VALUES
     (SEQ_EG_ASSET.NEXTVAL, 'OFFICEBUILD', (SELECT id FROM EGW_STATUS WHERE moduletype='ASSET' 
     AND description IN('Capitalized')), 'Office Building',  (SELECT id FROM EG_ASSETCATEGORY 
     WHERE code='BUILD'), '1', 'Office Building',1,1,SYSDATE,SYSDATE);

#DOWN
