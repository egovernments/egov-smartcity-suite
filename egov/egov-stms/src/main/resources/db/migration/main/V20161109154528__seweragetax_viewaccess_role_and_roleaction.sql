-- Create Role and role-action mappings
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('SEQ_EG_ROLE'), 'STMS_VIEW_ACCESS_ROLE', 'user has access to view masters, reports, transactional data, etc', now(), 1, 1, now(), 0);

--SEWERAGE Masters view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='STMS_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('DonationMasterView','FromDateByPropertyType','Donation Master Search','DonationMasterSuccess','ViewDonationMaster','SewerageRateView','Sewerage Rates Search','GetSewerageMonthlyRate','viewSewerageRatesMaster') and contextroot = 'stms' );


--Transactions view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='STMS_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('SearchSewerageConnection','viewSewerageConnection','DownloadFile','ViewSewerageCloseConnectionNotice') and contextroot = 'stms' );


--Reports role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='STMS_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('SearchNotice','STNoticeSearchResult','STZipAndDownload','STMergeAndDownload','Sewerage DCB Drill Down Report Wardwise','DCBReportWardwiseList','Sewerage DCB Report View Connections','STNoticeSearchResultCount','viewSewerageConnectionDCBReport','ShowNotice','STDailyCollectionReport') and contextroot = 'stms' );
