insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'WorksSearchWorkProgressRegister' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'AjaxWinCodesToSearchWorkProgressRegister' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'AjaxSearchWorkProgressRegister' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'WorkProgressRegisterPdf' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByDepartmentWise' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'WorksGetFinancialYear' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'EstimateAbstractReportByDepartmentWisePdf' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='WorksGetSubScheme' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'WorksSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'AjaxSearchEstimateAbstractReportByTypeOfWorkWise' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'), (select id from eg_action where name = 'EstimateAbstractReportByTypeOfWorkWisePdf' and contextroot = 'egworks'));

insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'ERP Report Viewer'),(select id from eg_action where name ='WorksGetSubTypeOfWork' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('WorksSearchWorkProgressRegister','AjaxWinCodesToSearchWorkProgressRegister','AjaxSearchWorkProgressRegister','WorkProgressRegisterPdf','WorksSearchEstimateAbstractReportByDepartmentWise','AjaxSearchEstimateAbstractReportByDepartmentWise','WorksGetFinancialYear','EstimateAbstractReportByDepartmentWisePdf','WorksGetSubScheme','WorksSearchEstimateAbstractReportByTypeOfWorkWise','AjaxSearchEstimateAbstractReportByTypeOfWorkWise','EstimateAbstractReportByTypeOfWorkWisePdf') and contextroot = 'egworks') and roleid in(select id from eg_role where name = 'ERP Report Viewer','WorksGetSubTypeOfWork');