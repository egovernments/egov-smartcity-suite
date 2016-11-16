ALTER TABLE egcncl_meeting  ADD COLUMN filestore bigint;


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'DownloadFinalResolutionPdf','/councilmeeting/downloadfile/',(select id from eg_module where name='Council Meeting' and parentmodule=(select id from eg_module where name='Council Management Transaction')),8,'DownloadFinalResolutionPdf',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='DownloadFinalResolutionPdf'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Management Admin'),(select id from eg_action where name='DownloadFinalResolutionPdf'));

INSERT into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='DownloadFinalResolutionPdf'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadFinalResolutionPdf') ,(select id FROM eg_feature WHERE name = 'View Meeting'));