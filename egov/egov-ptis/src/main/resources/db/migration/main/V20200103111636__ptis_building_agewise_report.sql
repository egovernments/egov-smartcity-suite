INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'BuildingAgeWiseAssessmentReport','/report/agewiseassessmentreport',null,(select id from eg_module where name='PTIS-Reports'),3,'Building Agewise Assessment Report',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'BuildingAgeWiseAssessmentReport'),id from eg_role where name in ('Super User','ERP Report Viewer','Property Verifier','Property Approver','SYSTEM');

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'BuildingAgeWiseAssessmentReportResult','/report/agewiseassessmentreport/result',null,(select id from eg_module where name='PTIS-Reports'),1,'Building Agewise Assessment Report Result',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'BuildingAgeWiseAssessmentReportResult'),id from eg_role where name in ('Super User','ERP Report Viewer','Property Verifier','Property Approver','SYSTEM');

