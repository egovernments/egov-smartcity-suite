INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'CSC Operator'),(select id FROM eg_action 
 WHERE NAME = 'editdataEntryDemand' and CONTEXTROOT='wtms'));