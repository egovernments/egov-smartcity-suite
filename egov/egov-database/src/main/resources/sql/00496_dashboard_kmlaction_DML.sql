
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'GisFileDownload','/downloadfile/gis',
(select id from eg_module where name='EGI-SETUP'),1,'GisFileDownload',false,'egi',(select id from eg_module where name='Administration' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'GisFileDownload'));

