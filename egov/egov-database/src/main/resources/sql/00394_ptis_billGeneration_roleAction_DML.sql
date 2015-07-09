 Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'PropTax generateBill','/bills/billGeneration-generateBill.action',
(select id from eg_module where name='Property Tax'),0,null,'f','ptis');

Insert into eg_roleaction values((select id from eg_role where name='CSC Operator'),
(select id from eg_Action where name='PropTax generateBill' and contextroot='ptis'));

Insert into eg_roleaction values((select id from eg_role where name='ULB Operator'),
(select id from eg_Action where name='PropTax generateBill' and contextroot='ptis'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_Action where name='PropTax generateBill' and contextroot='ptis'));


