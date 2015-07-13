UPDATE eg_module SET name='EGI-COMMON',enabled=false WHERE name='EgiPortal';
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='EGI-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='egi' AND parentmodule IS NULL);

INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'EIS-COMMON',false,'eis',(SELECT id FROM eg_module WHERE contextroot='eis' AND parentmodule IS NULL),
'EIS-COMMON',null);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='EIS-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='eis' AND parentmodule IS NULL);

INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'EGF-COMMON',false,'egf',(SELECT id FROM eg_module WHERE contextroot='egf' AND parentmodule IS NULL),
'EGF-COMMON',null);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='EGF-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='egf' AND parentmodule IS NULL);

INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'PTIS-COMMON',false,'ptis',(SELECT id FROM eg_module WHERE contextroot='ptis' AND parentmodule IS NULL),
'PTIS-COMMON',null);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='PTIS-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='ptis' AND parentmodule IS NULL);

INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'COLLECTION-COMMON',false,'collection',(SELECT id FROM eg_module WHERE contextroot='collection' AND parentmodule IS NULL),'COLLECTION-COMMON',null);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='COLLECTION-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='collection' AND parentmodule IS NULL);

INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'BPA-COMMON',false,'bpa',(SELECT id FROM eg_module WHERE contextroot='bpa' AND parentmodule IS NULL),
'BPA-COMMON',null);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='BPA-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='bpa' AND parentmodule IS NULL);

INSERT INTO eg_module (id,name,enabled,contextroot,parentmodule,displayname,ordernumber)
VALUES(nextval('SEQ_EG_MODULE'),'PORTAL-COMMON',false,'bpa',(SELECT id FROM eg_module WHERE contextroot='portal' AND parentmodule IS NULL),
'PORTAL-COMMON',null);
UPDATE eg_action SET parentmodule=(SELECT id FROM eg_module WHERE name='PORTAL-COMMON') WHERE parentmodule=(SELECT id FROM eg_module WHERE
contextroot='portal' AND parentmodule IS NULL);
