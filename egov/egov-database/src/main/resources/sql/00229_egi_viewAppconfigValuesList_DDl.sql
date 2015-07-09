Insert into EG_ACTION (id,name,url,
queryparams,parentmodule,ordernumber,displayname,enabled,contextroot) values
(nextval('seq_eg_action'),
'AppconfigValuesListForView','/appConfig/viewList/',
null,(SELECT id FROM EG_MODULE WHERE name = 'Configuration'),
0,'AppconfigValuesListForView','false','egi');

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) 
LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'AppconfigValuesListForView'));
