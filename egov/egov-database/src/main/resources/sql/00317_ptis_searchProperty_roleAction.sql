Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'Search Property By Location','/search/searchProperty-srchByLocation.action',
(select id from eg_module where name='Existing property'),1,'Search Property By Location','f','ptis');

Insert into eg_roleaction values((select id from eg_role where name='CSC Operator'),
(select id from eg_Action where name='Search Property By Location' and contextroot='ptis'));

  
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'Search Property By Demand','/search/searchProperty-searchByDemand.action',
(select id from eg_module where name='Existing property'),1,'Search Property By Demand','f','ptis');

Insert into eg_roleaction values((select id from eg_role where name='CSC Operator'),
(select id from eg_Action where name='Search Property By Demand' and contextroot='ptis'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot) 
values(nextval('SEQ_EG_ACTION'),'Search Property By Assessment','/search/searchProperty-srchByAssessment.action',
(select id from eg_module where name='Existing property'),1,'Search Property By Demand','f','ptis');

Insert into eg_roleaction values((select id from eg_role where name='CSC Operator'),
(select id from eg_Action where name='Search Property By Assessment' and contextroot='ptis'));

