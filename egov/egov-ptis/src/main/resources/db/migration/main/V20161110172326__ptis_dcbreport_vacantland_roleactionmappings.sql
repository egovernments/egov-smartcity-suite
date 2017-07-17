INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'VacantLandDCBReport','/report/dcbReportVLT',null,(select id from eg_module where name='PTIS-Reports'),3,'DCB Report(VLT)',true,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'VacantLandDCBReport'),id from eg_role where name in ('ULB Operator','Super User','VIEW_ACCESS_ROLE','ERP Report Viewer','Property Verifier','Property Approver');

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'VacantLandDCBReportResult','/report/dcbReportVLT/result',null,(select id from eg_module where name='PTIS-Reports'),1,'DCB Report(VLT) Result',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'VacantLandDCBReportResult'),id from eg_role where name in ('ULB Operator','Super User','VIEW_ACCESS_ROLE','ERP Report Viewer','Property Verifier','Property Approver');

update eg_action set displayname = 'DCB Report(PT)' where name='DCBReport' and contextroot='ptis';