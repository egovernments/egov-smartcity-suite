update eg_action set url='/hoarding/getsubcategories-by-category' where name='AjaxSubCategories' and parentmodule=(select id from eg_module where name='ADTAX-COMMON');

update eg_action set url='/hoarding/adtaxCreateLegacy' where name='CreateLegacyHoarding' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/hoarding/hoarding-search-list' where name='searchHoardingResult' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/hoarding/findhoarding-for-update' where name='HoardingSearchUpdate' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/hoarding/adtax-search' where name='searchadvertisement' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/hoarding/getsearch-adtax-result' where name='searchadvertisementResult' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/hoarding/renewl-search-result' where name='renewalsearchresult' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/hoarding/legacyUpdation' where name='HoardingLegacyUpdate' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/deactivate/activerecord-list-search' where name='Search Active Records' and parentmodule=(select id from eg_module where name='AdvertisementTaxTransactions');

update eg_action set url='/reports/dcbreport-search' where name='Agencywise Collection Report' and parentmodule=(select id from eg_module where name='AdvertisementTaxReports');
