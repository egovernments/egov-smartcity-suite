-----------------Role action mappings to search estimate numbers for LOA Offline Status----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Estimate Numbers For LOA Offline Status','/letterofacceptance/ajaxestimatenumbers-loaofflinestatus',null,(select id from eg_module where name='WorksOfflineStatus'),1,'Ajax Search Estimate Numbers For LOA Offline Status','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Estimate Numbers For LOA Offline Status'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search Estimate Numbers For LOA Offline Status' and contextroot = 'egworks'));

-----------------Role action mappings to search letter of acceptance numbers for LOA Offline Status----------------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search LOA NUMBER To LOA Offline Status','/letterofacceptance/ajaxloanumber-loaofflinestatus',null,(select id from eg_module where name='WorksOfflineStatus'),1,'Ajax Search LOA NUMBER To LOA Offline Status','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search LOA NUMBER To LOA Offline Status'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search LOA NUMBER To LOA Offline Status' and contextroot = 'egworks'));

-----------------Role action mappings to search letter of acceptance numbers for LOA Offline Status----------------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Contractors To LOA Offline Status','/letterofacceptance/ajaxcontractors-loaofflinestatus',null,(select id from eg_module where name='WorksOfflineStatus'),1,'Ajax Search Contractors To LOA Offline Status','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Contractors To LOA Offline Status'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search Contractors To LOA Offline Status' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Estimate Numbers For LOA Offline Status' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Estimate Numbers For LOA Offline Status' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search Estimate Numbers For LOA Offline Status' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search LOA NUMBER To LOA Offline Status' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search LOA NUMBER To LOA Offline Status' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search LOA NUMBER To LOA Offline Status' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Contractors To LOA Offline Status' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Contractors To LOA Offline Status' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search Contractors To LOA Offline Status' and contextroot = 'egworks';
