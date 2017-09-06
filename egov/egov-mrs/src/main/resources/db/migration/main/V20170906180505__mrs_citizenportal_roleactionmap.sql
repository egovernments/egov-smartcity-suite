--------------------------------------------------------- Marriage Registration------------------------------------------------------------

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='CreateRegistration' and contextroot = 'mrs'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='show-mrregistrationunitzone' and contextroot = 'mrs'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='calculateMarriageFee' and contextroot = 'mrs'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='View Marriage Registration' and contextroot = 'mrs'));


----------------------------------------------------------ReIssue-----------------------------------------------------------------------------

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='Re Issue Marriage Certifiate' and contextroot = 'mrs'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='Search register status MR records' and contextroot = 'mrs'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='CreateReIssue' and contextroot = 'mrs'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='Show Reissue Application Details' and contextroot = 'mrs'));