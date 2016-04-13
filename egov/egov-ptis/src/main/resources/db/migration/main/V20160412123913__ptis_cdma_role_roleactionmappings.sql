-- Create Role and role-action mappings
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('SEQ_EG_ROLE'), 'VIEW_ACCESS_ROLE', 'user has access to view masters, reports, transactional data, etc', now(), 1, 1, now(), 0);


--EGI home page role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Inbox','InboxDraft','InboxHistory','OfficialsProfileEdit','OfficialSentFeedBack','OfficialChangePassword','AddFavourite','RemoveFavourite','Official Home Page') and contextroot = 'egi' );

--PTIS Reports role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='VIEW_ACCESS_ROLE') as roleid, id from eg_action where parentmodule = 362 and contextroot = 'ptis' );

--PTIS Masters role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search Roof Type','View Roof Type','Search Wood Type','View Wood Type','Search Floor Type','View Floor Type','Search Wall Type','View Wall Type','ViewTaxRates') and contextroot = 'ptis' );

--PTIS search/view property role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search Property','Search Property By Index','Search Property By Mobile No','Search Property By Door No','Search Property By Bndry','Search Property By Area','Populate LocationFactors','Populate Wards','View Property','Search Property By Location','Search Property By Demand','Search Property By Assessment','View DCB Property','View DCB Property Display','View Headwise DCB','View MigData on DCB') and contextroot = 'ptis' );
