

update eg_action set displayname='Create Configuration Value' where name='CreateAppConfig' and contextroot='egi';

update eg_action set displayname='Modify Configuration Value' where name='modifyAppConfig' and contextroot='egi';

update eg_action set displayname='View Configuration Value' where name='viewAppConfig' and contextroot='egi';



INSERT INTO EG_ACTION(ID, NAME, URL, QUERYPARAMS, parentmodule, ORDERNUMBER, DISPLAYNAME, ENABLED, 
 CONTEXTROOT)
Values (nextval('SEQ_EG_ACTION'), 'AppconfigPopulateList', 
 '/appConfig/ajax-appConfigpopulate', NULL,  (SELECT id FROM EG_MODULE WHERE name = 'Configuration'),
0, 'AppconfigPopulateList', 'false', 'egi');

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER')
 ,(select id FROM eg_action  WHERE name = 'AppconfigPopulateList'));