-----------------Role action mappings to Estimate Abstract Report By Department form----------------

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchEstimateAbstractReportByTypeOfWorkWise','/reports/estimateabstractreport/typeofworkwise-searchform',null,(select id from EG_MODULE where name = 'WorksReports'),1,'Estimate Abstract Report By Type Of Work','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));


insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchEstimateAbstractReportByTypeOfWorkWise','/reports/ajax-estimateabstractreportbytypeofworkwise',null,(select id from EG_MODULE where name = 'WorksReports'),1,'Search Estimate Abstract Report By Type Of Work','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));


insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'EstimateAbstractReportByTypeOfWorkWisePdf','/reports/estimateabstractreportbytypeofworkwise/pdf',null,(select id from EG_MODULE where name = 'WorksReports'),1,'Estimate Abstract Report By Type Of Work WisePdf','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Approver'), (select id from eg_action where name = 'EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks'));





--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Approver') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks';


