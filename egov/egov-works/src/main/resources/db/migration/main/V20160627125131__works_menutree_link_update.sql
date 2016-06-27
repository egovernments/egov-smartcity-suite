update eg_module set ordernumber = 4 where name='WorksLetterOfAcceptance' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_module set ordernumber = 5 where name='WorksOfflineStatus' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_module set ordernumber = 6 where name='WorksMeasurementBook' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_module set ordernumber = 7 where name='WorksMilestone' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_module set ordernumber = 8 where name='WorksContractorBill' and parentmodule = (select id from eg_module where name = 'Works Management');
update eg_module set ordernumber = 12 where name = 'WorksReports' and parentmodule = (select id from eg_module where name = 'Works Management');