delete from eg_roleaction  where actionid in (
select id from eg_action  where name in ('DailyAbstractRecoveryReport', 'PropTax Defaulters Report', 'PropTax Defaulters Report Result', 'PropTax BakayaFerist Report', 'PropTax BakayaFerist Report Result',
'Jamabandi Report', 'Jamabandi Report Main', 'DailyCollectionReportSearch', 'DailyCollectionReportResult', 'HeadWiseDMDCOLL Report', 'Big Building Recovery Report', 'Big Building Recovery Report Main',
'EgsAndEduCessCollectionReport', 'EgsAndEduCess CollectionReport Result', 'PropertyTaxAuditReport', 'Zone Wise Demand', 'GeneratedBills', 'Generated Bills By Part No',
'DCBReport', 'DCBReport_Result'));

delete from eg_action  where name in ('DailyAbstractRecoveryReport', 'PropTax Defaulters Report', 'PropTax Defaulters Report Result', 'PropTax BakayaFerist Report', 'PropTax BakayaFerist Report Result',
'Jamabandi Report', 'Jamabandi Report Main', 'DailyCollectionReportSearch', 'DailyCollectionReportResult', 'HeadWiseDMDCOLL Report', 'Big Building Recovery Report', 'Big Building Recovery Report Main',
'EgsAndEduCessCollectionReport', 'EgsAndEduCess CollectionReport Result', 'PropertyTaxAuditReport', 'Zone Wise Demand', 'GeneratedBills', 'Generated Bills By Part No',
'DCBReport', 'DCBReport_Result');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Approver') from eg_action where name in ('Zone Wise Collection Report',
'Ward Wise Collection Report',
'Block Wise Collection Report',
'Locality Wise Collection Report',
'Search Notice');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Verifier') from eg_action where name in ('Zone Wise Collection Report',
'Ward Wise Collection Report',
'Block Wise Collection Report',
'Locality Wise Collection Report',
'Search Notice');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Approver') from eg_action where name in ('Search Property',
'Search Property By Index',
'Search Property By Bndry',
'Search Property By Area',
'Search Property By Location',
'Search Property By Demand',
'Search Property By Assessment');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Verifier') from eg_action where name in ('Search Property',
'Search Property By Index',
'Search Property By Bndry',
'Search Property By Area',
'Search Property By Location',
'Search Property By Demand',
'Search Property By Assessment');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Approver') from eg_action where name in ('View Property');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Verifier') from eg_action where name in ('View Property');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Approver') from eg_action where name in ('Populate Wards');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Verifier') from eg_action where name in ('Populate Wards');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Approver') from eg_action where name in ('View DCB Property Display');

insert into eg_roleaction(actionid, roleid) 
select id, (select id from eg_role where name = 'Property Verifier') from eg_action where name in ('View DCB Property Display');

delete from eg_roleaction  where roleid = (select id from eg_role where name = 'Property Verifier') and actionid = (select id from eg_action where name in ('Create Property'));
