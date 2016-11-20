INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'DefaultersReportPT','/report/defaultersReportPT',null,(select id from eg_module where name='PTIS-Reports'),null,'Defaulters Report(PT)',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'DefaultersReportPT'),id from eg_role where name in ('ULB Operator','Super User','VIEW_ACCESS_ROLE','ERP Report Viewer','Property Verifier','Property Approver');


INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'DefaultersReportVLT','/report/defaultersReportVLT',null,(select id from eg_module where name='PTIS-Reports'),null,'Defaulters Report(VLT)',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'DefaultersReportVLT'),id from eg_role where name in ('ULB Operator','Super User','VIEW_ACCESS_ROLE','ERP Report Viewer','Property Verifier','Property Approver');


INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'DefaultersReportResult','/report/defaultersReport/result',null,(select id from eg_module where name='PTIS-Reports'),null,'Defaulters Report Result',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'DefaultersReportResult'),id from eg_role where name in ('ULB Operator','Super User','VIEW_ACCESS_ROLE','ERP Report Viewer','Property Verifier','Property Approver');


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DefaultersReportPT') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DefaultersReportVLT') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DefaultersReportResult') ,(select id FROM eg_feature WHERE name = 'Property Tax Reports'));


----------- Delete existing actions -------------- 
delete from eg_feature_action where action in ((select id FROM eg_action  WHERE name = 'defaultersReportResult' and url = '/reports/defaultersReport-getDefaultersList.action'));
delete from eg_feature_action where action in ((select id FROM eg_action  WHERE name = 'defaultersReport' and url = '/reports/defaultersReport-search.action'));

delete from EG_ROLEACTION where actionid in (select id from eg_action where name = 'defaultersReport' and url = '/reports/defaultersReport-search.action');
delete from EG_ROLEACTION where actionid in (select id from eg_action where name = 'defaultersReportResult' and url = '/reports/defaultersReport-getDefaultersList.action');

delete from EG_ACTION where name = 'defaultersReport' and url = '/reports/defaultersReport-search.action';
delete from EG_ACTION where name = 'defaultersReportResult' and url = '/reports/defaultersReport-getDefaultersList.action';


