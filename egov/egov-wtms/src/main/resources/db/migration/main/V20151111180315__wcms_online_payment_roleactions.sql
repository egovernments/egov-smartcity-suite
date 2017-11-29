INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Citizen'),(select id FROM eg_action 
 WHERE url = '/search/waterSearch/' and CONTEXTROOT='wtms'));



INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Citizen'),(select id FROM eg_action 
 WHERE url = '/elastic/appSearch/' and CONTEXTROOT='wtms'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Citizen'),(select id FROM eg_action 
 WHERE url = '/application/view/' and CONTEXTROOT='wtms'));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) 
values ((select id from eg_role where name = 'Citizen'),(select id FROM eg_action 
 WHERE url = '/application/generatebill' and CONTEXTROOT='wtms'));