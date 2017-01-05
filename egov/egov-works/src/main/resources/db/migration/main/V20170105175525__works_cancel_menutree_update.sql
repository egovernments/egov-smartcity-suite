-----------------Changing order numbers for Cancel actions--------------
update eg_action set ordernumber = 1 where name = 'SearchLineEstimateToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 2 where name = 'SearchEstimateToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 3 where name = 'SearchLOAToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 5 where name = 'SearchEstimateToCancel' and contextroot = 'egworks';
update eg_action set ordernumber = 6 where name = 'SearchMBToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 7 where name = 'SearchMilestoneToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 8 where name = 'SearchContractorBillToCancelForm' and contextroot = 'egworks';