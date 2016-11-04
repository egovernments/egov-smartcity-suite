insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksEstimatePhotograph','true',null,(select id from eg_module where name = 'Works Management'),'Physical Progress Tracker', 3);

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'UploadEstimatePhotographs','/lineestimate/searchlineestimateform',null,(select id from EG_MODULE where name = 'WorksEstimatePhotograph'),1,'Upload Estimate Photographs','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='UploadEstimatePhotographs' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='UploadEstimatePhotographs' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Get Estimate Number to Upload Estimate Photograph','/lineestimate/getestimatenumbers-uploadphotographs',null,(select id from EG_MODULE where name = 'WorksEstimatePhotograph'),1,'Get Estimate Number to Upload Estimate Photograph','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Get Estimate Number to Upload Estimate Photograph' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Get Estimate Number to Upload Estimate Photograph' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Get WIN to Upload Estimate Photograph','/lineestimate/getwin-uploadphotographs',null,(select id from EG_MODULE where name = 'WorksEstimatePhotograph'),1,'Get WIN to Upload Estimate Photograph','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Get WIN to Upload Estimate Photograph' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Get WIN to Upload Estimate Photograph' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Ajax Search Line Estimate For Estimate Photographs','/lineestimate/searchlineestimateforestimatephotograph',null,(select id from EG_MODULE where name = 'WorksEstimatePhotograph'),1,'Ajax Search Line Estimate For Estimate Photographs','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Ajax Search Line Estimate For Estimate Photographs' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search Line Estimate For Estimate Photographs' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Upload Estimate Photographs Form','/estimatephotograph/newform',null,(select id from EG_MODULE where name = 'WorksEstimatePhotograph'),1,'Upload Estimate Photographs Form','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Upload Estimate Photographs Form' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Upload Estimate Photographs Form' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Save Estimate Photographs','/estimatephotograph/upload',null,(select id from EG_MODULE where name = 'WorksEstimatePhotograph'),1,'Save Estimate Photographs','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Save Estimate Photographs' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Save Estimate Photographs' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Update Estimate Photographs','/estimatephotograph/update',null,(select id from EG_MODULE where name = 'WorksEstimatePhotograph'),1,'Update Estimate Photographs','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Update Estimate Photographs' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Update Estimate Photographs' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='UploadEstimatePhotographs' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='UploadEstimatePhotographs' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'UploadEstimatePhotographs' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Get Estimate Number to Upload Estimate Photograph' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Get Estimate Number to Upload Estimate Photograph' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Get Estimate Number to Upload Estimate Photograph' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Get WIN to Upload Estimate Photograph' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Get WIN to Upload Estimate Photograph' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Get WIN to Upload Estimate Photograph' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Line Estimate For Estimate Photographs' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Line Estimate For Estimate Photographs' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search Line Estimate For Estimate Photographs' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Upload Estimate Photographs Form' and contextroot = 'egworks');
--rollback  delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Upload Estimate Photographs Form' and contextroot = 'egworks');
--rollback  delete FROM EG_ACTION WHERE name = 'Upload Estimate Photographs Form' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Save Estimate Photographs' and contextroot = 'egworks');
--rollback  delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Save Estimate Photographs' and contextroot = 'egworks');
--rollback  delete FROM EG_ACTION WHERE name = 'Save Estimate Photographs' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Update Estimate Photographs' and contextroot = 'egworks');
--rollback  delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Update Estimate Photographs' and contextroot = 'egworks');
--rollback  delete FROM EG_ACTION WHERE name = 'Update Estimate Photographs' and contextroot = 'egworks';

--rollback delete from eg_module where name='WorksEstimatePhotograph' and parentmodule=(select id from eg_module where name = 'Works Management');