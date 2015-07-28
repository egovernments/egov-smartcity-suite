INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'CreateReceipt' and CONTEXTROOT='collection'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action  WHERE NAME = 'SaveReceipt' and CONTEXTROOT='collection'));
