INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'DataEntryDemandUpdate', '/application/editDemand/', null,
     (select id from eg_module where name='Water Tax Management'), null, 'DataEntryDemandUpdate', false,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'ULB OPERATOR'),(select id FROM eg_action  
WHERE NAME = 'DataEntryDemandUpdatee' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'CSC OPERATOR'),(select id FROM eg_action 
 WHERE NAME = 'DataEntryDemandUpdate' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where UPPER(name) = 'Super User'),(select id FROM eg_action 
 WHERE NAME = 'DataEntryDemandUpdate' and CONTEXTROOT='wtms'));