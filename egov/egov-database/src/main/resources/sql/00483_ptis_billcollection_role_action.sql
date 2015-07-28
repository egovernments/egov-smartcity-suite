Delete from EG_ROLEACTION where actionid in (select id FROM eg_action  WHERE NAME = 'SaveReceipt' and CONTEXTROOT='collection') and roleid in (select id from eg_role where UPPER(name) = 'CSC OPERATOR');

Delete from EG_ROLEACTION where actionid in (select id FROM eg_action  WHERE NAME = 'CreateReceipt' and CONTEXTROOT='collection') and roleid in (select id from eg_role where UPPER(name) = 'CSC OPERATOR');

Insert into eg_userrole values((select id from eg_role  where name  = 'CSC Operator'),(select id from eg_user where username ='satyam'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Search Property' 
and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Search Property By Assessment' 
and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'View Property' 
and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'Generate Collection Bill' 
and CONTEXTROOT='ptis'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'CreateReceipt' and CONTEXTROOT='collection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'SaveReceipt' and CONTEXTROOT='collection'));
