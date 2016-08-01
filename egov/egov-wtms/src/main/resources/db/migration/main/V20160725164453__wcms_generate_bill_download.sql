insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'generateBill-download','/report/generateBillForHSCNo/downloadDemandBill', null,(select id from eg_module where name='WaterTaxReports'),1,
null,'f','wtms',0,1,now(),1,now(),(select id from eg_module  where name = 'Water Tax Management'));


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='generateBill-download' and CONTEXTROOT='wtms'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Administrator'),(select id from eg_action where name='generateBill-download' and CONTEXTROOT='wtms'));