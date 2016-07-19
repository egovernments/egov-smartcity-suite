---------------Updating search actions----------------------
update eg_action set displayname = 'Search LOA' where name = 'WorksSearchLetterOfAcceptanceForm' and contextroot = 'egworks';
update eg_action set displayname = 'Search Contractor Bill' where name = 'WorksSearchContractorBill' and contextroot = 'egworks';
update eg_action set displayname = 'Search Measurement Book' where name = 'SearchMBHeader' and contextroot = 'egworks';