-----------------Action name change for Cancel Milestone----------------
update eg_action set DISPLAYNAME = 'Cancel Milestone/Track Milestone' where name = 'SearchMilestoneToCancelForm' and contextroot = 'egworks';

-----------------Changing order numbers for Cancel actions--------------
update eg_action set ordernumber = 0 where name = 'SearchLineEstimateToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 1 where name = 'SearchLOAToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 2 where name = 'SearchMilestoneToCancelForm' and contextroot = 'egworks';
update eg_action set ordernumber = 3 where name = 'SearchContractorBillToCancelForm' and contextroot = 'egworks';

--rollback update eg_action set DISPLAYNAME = 'Cancel Milestone' where name = 'SearchMilestoneToCancelForm' and contextroot = 'egworks';
--rollback update eg_action set ordernumber = 0 where name = 'SearchLineEstimateToCancelForm';
--rollback update eg_action set ordernumber = 0 where name = 'SearchLOAToCancelForm';
--rollback update eg_action set ordernumber = 0 where name = 'SearchMilestoneToCancelForm';
--rollback update eg_action set ordernumber = 0 where name = 'SearchContractorBillToCancelForm';