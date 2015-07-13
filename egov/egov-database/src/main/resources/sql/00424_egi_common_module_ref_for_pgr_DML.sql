INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'PGR-COMMON',false,'pgr',(SELECT id FROM eg_module WHERE contextroot='pgr' AND parentmodule IS NULL),
'PGR-COMMON',null);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='PGR-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='pgr' AND parentmodule IS NULL);
