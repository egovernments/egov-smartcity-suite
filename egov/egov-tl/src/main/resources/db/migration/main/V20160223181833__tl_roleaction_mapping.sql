delete from eg_roleaction where actionid in (select id from eg_action where contextroot ='tl');

--Role  
Insert into EG_ROLE (ID,NAME,DESCRIPTION,CREATEDDATE,CREATEDBY,LASTMODIFIEDBY,LASTMODIFIEDDATE,VERSION) values (nextval('seq_eg_role'),'TLAdmin','TradeLicense Administrator',NOW(),1,1,NOW(),0);

--Masters

INSERT into EG_ROLEACTION(ROLEID,ACTIONID) (SELECT r.id, action.id FROM EG_ROLE r CROSS JOIN eg_action action WHERE r.name = 'TLAdmin' AND action.parentmodule in (select id from eg_module where parentmodule= (select id from eg_module where name = 'Trade License Masters')));

INSERT into EG_ROLEACTION(ROLEID,ACTIONID) (SELECT r.id, action.id FROM EG_ROLE r CROSS JOIN eg_action action WHERE r.name = 'Super User' AND action.parentmodule in (select id from eg_module where parentmodule= (select id from eg_module where name = 'Trade License Masters')));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='tradeLicenseAjaxMaster' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='tradeLicenseAjaxMaster' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-FeeMatrix' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Create-FeeMatrix' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-SubCategoryByParent' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Ajax-SubCategoryByParent' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-FeeTypeBySubCategory' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Ajax-FeeTypeBySubCategory' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-UnitOfMeasurementBySubCategory' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Ajax-UnitOfMeasurementBySubCategory' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Search-FeeMatrix' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Search-FeeMatrix' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Create-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Result-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Search and Edit Result-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Edit-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Update-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Search and View Result-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='View-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='View-Validity' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Delete Fee Matrix' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Delete Fee Matrix' and contextroot='tl'));

--Create new license

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Create New License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Create New License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Create New License' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='tradeLicenseLocalityAjax' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='tradeLicenseLocalityAjax' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='tradeLicenseLocalityAjax' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-loadUomName' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Ajax-loadUomName' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Ajax-loadUomName' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='newTradeLicense-create' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='newTradeLicense-create' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='newTradeLicense-create' and contextroot='tl'));


--tlapprover for inbox item

insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='View Trade License for Approval' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='View Trade License for Approval' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='NewTradeLicense-approve' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='NewTradeLicense-approve' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='tradeLicenseLocalityAjax' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Ajax-loadUomName' and contextroot='tl'));

--search trade license

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchTradeLicense' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='SearchTradeLicense' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='SearchTradeLicense' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='SearchTradeLicense' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='SearchTradeLicense' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='searchTrade-search' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='searchTrade-search' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='searchTrade-search' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='searchTrade-search' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='searchTrade-search' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='viewTradeLicense-view' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='viewTradeLicense-view' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='viewTradeLicense-view' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='viewTradeLicense-view' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='viewTradeLicense-view' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Trade License Bill Collect' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='Trade License Bill Collect' and contextroot='tl'));


--print certificate

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='View Trade License Generate Certificate' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='View Trade License Generate Certificate' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='View Trade License Generate Certificate' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='View Trade License Generate Certificate' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='TradeLicense report viewer' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='TradeLicense report viewer' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='TradeLicense report viewer' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='TradeLicense report viewer' and contextroot='tl'));

--create legacy license

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Enter Trade License Action' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Enter Trade License Action' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Enter Trade License Action' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Save Trade License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Save Trade License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Save Trade License' and contextroot='tl'));

---modify legacy license
insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Modify-Legacy-License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Modify-Legacy-License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Modify-Legacy-License' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Save-Modified-Legacy-License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='Save-Modified-Legacy-License' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='Save-Modified-Legacy-License' and contextroot='tl'));


---Reports

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='TradeLicenseDCBReportLocalityWise' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='TradeLicenseDCBReportLocalityWise' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='TradeLicenseDCBReportLocalityWise' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='TradeLicenseDCBReportLocalityWise' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='TLDCBReportList' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='TLDCBReportList' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='TLDCBReportList' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='TLDCBReportList' and contextroot='tl'));


---renewal

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='New Trade License Before Renew' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='New Trade License Before Renew' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='New Trade License Before Renew' and contextroot='tl'));

insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='NewTradeLicense-renewal' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLCreator'),(select id from eg_action where name='NewTradeLicense-renewal' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLAdmin'),(select id from eg_action where name='NewTradeLicense-renewal' and contextroot='tl'));

--digi signature

insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='digitalSignature-TLTransitionWorkflow' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='digitalSignature-TLDownloadSignDoc' and contextroot='tl'));



--renewal notice
insert into eg_roleaction values ((select id from eg_role where name='Super User'),(select id from eg_action where name='Trade License Renewal Notice' and contextroot='tl'));
insert into eg_roleaction values ((select id from eg_role where name='TLApprover'),(select id from eg_action where name='Trade License Renewal Notice' and contextroot='tl'));


delete from eg_action where contextroot ='tl' and id not in (select actionid from eg_roleaction ) ;

