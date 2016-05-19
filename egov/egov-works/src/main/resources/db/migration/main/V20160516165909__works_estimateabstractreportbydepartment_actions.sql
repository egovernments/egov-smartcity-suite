-----------------Role action mappings to Estimate Abstract Report By Department form----------------

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchEstimateAbstractReportByDepartmentWise','/reports/estimateabstractreport/departmentwise-searchform',null,(select id from EG_MODULE where name = 'WorksReports'),1,'Estimate Abstract Report By Department','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));


insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchEstimateAbstractReportByDepartmentWise','/reports/ajax-estimateabstractreportbydepartmentwise',null,(select id from EG_MODULE where name = 'WorksReports'),1,'Search Estimate Abstract Report By Department','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksGetFinancialYear','/lineestimate/getfinancilyearbyid',null,(select id from EG_MODULE where name = 'WorksLineEstimate'),1,'WorksGetFinancialYear','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksGetFinancialYear' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksGetFinancialYear' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksGetFinancialYear' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'WorksGetFinancialYear' and contextroot = 'egworks'));


insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'EstimateAbstractReportByDepartmentWisePdf','/reports/estimateabstractreportbydepartmentwise/pdf',null,(select id from EG_MODULE where name = 'WorksReports'),1,'Estimate Abstract Report By Department WisePdf','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks'));





--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks';


