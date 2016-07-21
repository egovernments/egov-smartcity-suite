-----------------Role action mappings to search estimate numbers for Create LOA ----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Estimate Numbers For Create LOA','/abstractestimate/ajaxestimatenumbers-tocreateloa',null,(select id from eg_module where name='WorksAbstractEstimate'),1,'Ajax Search Estimate Numbers For Create LOA','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Estimate Numbers For Create LOA'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search Estimate Numbers For Create LOA' and contextroot = 'egworks'));

-----------------Role action mappings to search letter of acceptance numbers for Create LOA----------------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Admin Sanction Number To Create LOA','/abstractestimate/ajaxadminsanctionnumbers-tocreateloa',null,(select id from eg_module where name='WorksAbstractEstimate'),1,'Ajax Search Admin Sanction Number To Create LOA','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Admin Sanction Number To Create LOA'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search Admin Sanction Number To Create LOA' and contextroot = 'egworks'));

-----------------Role action mappings to search letter of acceptance numbers for Create LOA----------------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Work Identification Numbers To Create LOA','/abstractestimate/ajaxworkidentificationnumbers-tocreateloa',null,(select id from eg_module where name='WorksAbstractEstimate'),1,'Ajax Search Work Identification Numbers To Create LOA','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Work Identification Numbers To Create LOA'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search Work Identification Numbers To Create LOA' and contextroot = 'egworks'));

-----------------Role action mappings to search contractors for to modify LOA ----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Ajax Search Contractors For To Modify LOA','/letterofacceptance/ajaxcontractors-modifyloa',null,(select id from eg_module where name='WorksLetterOfAcceptance'),1,'Ajax Search Contractors For To Modify LOA','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax Search Contractors For To Modify LOA'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Ajax Search Contractors For To Modify LOA' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Contractors For To Modify LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Contractors For To Modify LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search Contractors For To Modify LOA' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Estimate Numbers For Create LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Estimate Numbers For Create LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search Estimate Numbers For Create LOA' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Admin Sanction Number To Create LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Admin Sanction Number To Create LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search Admin Sanction Number To Create LOA' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Work Identification Numbers To Create LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='Ajax Search Work Identification Numbers To Create LOA' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'Ajax Search Work Identification Numbers To Create LOA' and contextroot = 'egworks';
