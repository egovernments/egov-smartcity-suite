
update eg_action set url='/search/searchForm' where url='/search/searchForm/' and contextroot='lcms';

insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'searchlegalcaseresult','/search/legalsearchResult',(select id from eg_module 
 where name='LCMS Transactions'),1,'searchlegalcaseresult',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));
 
insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='searchlegalcaseresult'));