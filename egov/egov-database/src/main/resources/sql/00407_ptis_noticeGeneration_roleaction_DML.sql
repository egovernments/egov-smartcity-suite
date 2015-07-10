INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER,DISPLAYNAME, ENABLED, CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE)
Values (nextval('SEQ_EG_ACTION'), 'generateSpecialNotice','/notice/propertyTaxNotice-generateNotice', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'New Property'),3, 'generateSpecialNotice',false,'ptis',0,1,now(),1,now());

Insert into eg_roleaction values((select id from eg_role where name='ULB Operator'),
(select id from eg_Action where name='generateSpecialNotice' and contextroot='ptis'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_Action where name='generateSpecialNotice' and contextroot='ptis'));

