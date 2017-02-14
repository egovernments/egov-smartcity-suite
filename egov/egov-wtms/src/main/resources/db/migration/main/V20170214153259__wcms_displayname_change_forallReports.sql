update eg_action set displayname = 'DCB Report Zone Wise(WC)' where name ='WaterTaxDCBReportZoneWise' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'DCB Report Ward Wise(WC)' where name ='WaterTaxDCBReportWardWise' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'DCB Report Block Wise(WC)' where name ='WaterTaxDCBReportBlockWise' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'DCB Report Locality Wise(WC)' where name ='WaterTaxDCBReportLocalityWise' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Search Bill(WC)' where name ='GenerateBillReport' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Generate Bill(WC)' where name ='GenerateBillForConsumerCode' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Daily Collection Report(WC)' where name ='DailyWTCollectionReport' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Arrear Register Report(WC)' where name ='waterchargearrearReport' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Defaulters Report(WC)' where name ='DefaultersReport' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Base Register Report(WC)' where name ='BaseRegister Report' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Number of connections(WC)' where name ='WaterTaxConnectionReportWardWise' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');

update eg_action set displayname = 'Data Entry Records Details Report(WC)' where name ='DataEntryConnectionReport' and contextroot ='wtms' and application =(select id from eg_module where name='Water Tax Management');
