insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'DownloadEstimatePhotograph','/estimatephotograph/downloadphotgraphs',null,(select id from EG_MODULE where name = 'WorksRevisionEstimate'),0,'DownloadEstimatePhotograph',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'DownloadEstimatePhotograph' and contextroot = 'egworks'));
--Feature mapping
INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'DownloadEstimatePhotograph') ,(select id FROM eg_feature WHERE name = 'Upload Estimate Photograph'));

--rollback delete from eg_roleaction where actionid in(select id from eg_action where name = 'DownloadEstimatePhotograph' and contextroot='egworks') and roleid in(select id from eg_role where name in('Works Creator'));
--rollback delete from eg_action where name = 'DownloadEstimatePhotograph' and contextroot='egworks';
