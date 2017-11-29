--ageing report
update eg_action set url='/report/ageing/boundarywise' , name='Boundarywise Ageing Report'
where name='Ageing Report By Boundary wise';

update eg_action set url='/report/ageing/departmentwise',name='Departmentwise Ageing Report'
where name='Ageing Report By Department wise';

update eg_action set url='/report/ageing' ,name='AgeingReport Search' where name='Ageing report search result';

--complainttypewise report
update eg_action set url='/report/grievancetypewise' , name='GrievanceTypewise Report Search'
where name='Complaint Type Wise Report';

update eg_action set url='/report/grievancetypewise/grand-total' , name='GrievanceTypewise Report Grandtotal'
where name='ComplaintTypewise report Grand Total';

update eg_action set url='/report/grievancetypewise/download' , name='Download GrievanceTypewise Report'
where name='ComplaintTypewise report Download';

delete from eg_feature_action  where action  in (select id from eg_action where name='Complaint Type Wise Report search result');

delete from eg_roleaction where actionid  in(select id from eg_action where name='Complaint Type Wise Report search result');

delete from eg_action where name='Complaint Type Wise Report search result';

--functionarywise report
update eg_action set url='/report/functionarywise' ,name='Functionarywise Report Search' where name='Functionary Wise Report Search';

update eg_action set url='/report/functionarywise/grand-total' ,name='Functionarywise Report Grandtotal'
where name='Functionarywise report Grand Total';

update eg_action set url='/report/functionarywise/download' , name='Download Functionarywise Report'
where name='Functionarywise report Download';

delete from eg_feature_action  where action  in (select id from eg_action where name='Functionary Wise Report Result');

delete from eg_roleaction where actionid  in(select id from eg_action where name='Functionary Wise Report Result');

delete from eg_action where name='Functionary Wise Report Result';

--routerescalation report
update eg_action set url='/report/escalationrouter/download' ,name='Download EscalationRouter Report'
where name='routerescaltionreport';

update eg_action set url='/report/escalationrouter' ,name='EscalationRouter Report Search' , displayname ='Escalation Router Report'
where name='routerescalationsearch';

delete from eg_feature_action  where action  in (select id from eg_action where name='routerescalationresult');

delete from eg_roleaction where actionid  in(select id from eg_action where name='routerescalationresult');

delete from eg_action where name='routerescalationresult';


--drillDown report
update eg_action set url='/report/drilldown/boundarywise' ,name='Boundarywise Drilldown Report'
where name='Drill Down Report By Boundary wise';

update eg_action set url='/report/drilldown/departmentwise' ,name='Departmentwise Drilldown Report'
where name='Drill Down Report By Department wise';

update eg_action set url='/report/drilldown' ,name='Drilldown Report Search' where name='Drill Down Report search result';

