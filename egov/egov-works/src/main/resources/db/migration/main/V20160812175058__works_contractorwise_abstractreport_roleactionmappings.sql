---Actions and roleactions for contractor wise abstract report
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'ContractorWiseAbstractSearchForm','/reports/contractorwiseabstractreport/searchform',null,(select id from eg_module where name='WorksReports'),5,
'Contractor wise abstract report','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxContractorWiseAbstract','/reports/ajax-contractorwiseabstractreport',null,(select id from eg_module where name='WorksReports'),1,
'Ajax Contractor wise abstract report search','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'ContractorWiseAbstractPdfExcel','/reports/contractorwiseabstract/pdf',null,(select id from eg_module where name='WorksReports'),1,
'Contractor wise abstract report pdf excel','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxContractorsForContractorWiseAbstract','/reports/ajax-searchcontractors',null,(select id from eg_module where name='WorksReports'),1,
'Ajax Contractor wise abstract report for contractors','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ContractorWiseAbstractSearchForm'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxContractorWiseAbstract'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ContractorWiseAbstractPdfExcel'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxContractorsForContractorWiseAbstract'));

insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='ContractorWiseAbstractSearchForm'));
insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='AjaxContractorWiseAbstract'));
insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='ContractorWiseAbstractPdfExcel'));
insert into eg_roleaction values((select id from eg_role where name='Works Creator'),(select id from eg_action where name='AjaxContractorsForContractorWiseAbstract'));

insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='ContractorWiseAbstractSearchForm'));
insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='AjaxContractorWiseAbstract'));
insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='ContractorWiseAbstractPdfExcel'));
insert into eg_roleaction values((select id from eg_role where name='Works Approver'),(select id from eg_action where name='AjaxContractorsForContractorWiseAbstract'));

insert into eg_roleaction values((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='ContractorWiseAbstractSearchForm'));
insert into eg_roleaction values((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='AjaxContractorWiseAbstract'));
insert into eg_roleaction values((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='ContractorWiseAbstractPdfExcel'));
insert into eg_roleaction values((select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='AjaxContractorsForContractorWiseAbstract'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name in('ContractorWiseAbstractSearchForm','AjaxContractorWiseAbstract','ContractorWiseAbstractPdfExcel','AjaxContractorsForContractorWiseAbstract')) and roleid in(select id from eg_role where name in('ERP Report Viewer','Works Approver','Works Creator','Super User'));
--rollback delete from eg_action where name in('ContractorWiseAbstractSearchForm','AjaxContractorWiseAbstract','ContractorWiseAbstractPdfExcel','AjaxContractorsForContractorWiseAbstract');