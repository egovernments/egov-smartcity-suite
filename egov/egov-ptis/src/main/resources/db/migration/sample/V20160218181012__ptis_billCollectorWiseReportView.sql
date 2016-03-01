
drop view if exists public.billColl_DialyCollection_view;

create or replace view public.billColl_DialyCollection_view
as 
select distinct bcreport.district,bcreport.ulbname as ulbName ,bcreport.ulbcode as ulbCode,
billcoll.collectorname,billcoll.mobilenumber,target_arrears_demand,target_current_demand,today_arrears_collection,today_currentyear_collection, 
cummulative_arrears_collection,cummulative_currentyear_collection,lastyear_collection,lastyear_cummulative_collection ,colln.type  
from EGPT_BILLCOLLECTORWISE_REPORT bcreport, EGPT_BILLCOLLCTOR_DETAIL billcoll left outer join EGPT_BCREPORT_COLLECTION colln on billcoll.id=colln.billcollector 
where bcreport.id=billcoll.bcreportid   order by district,ulbname,collectorname;
