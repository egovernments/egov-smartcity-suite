-----------------Role action mappings to Search Overhead----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewContractorGrade','/masters/contractorGrade-viewContractorGrade.action','mode=view',(select id from EG_MODULE where name = 'WorksContractorGradeMaster'),3,'View Contractor Grade','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ViewContractorGrade' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ViewContractorGrade' and contextroot = 'egworks'));

update eg_action set displayname='Modify Contractor Grade' where name='WorksContractorGradeViewEdit' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ViewContractorGrade' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='ViewContractorGrade' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'ViewContractorGrade' and contextroot = 'egworks';

--rollback update eg_action set displayname='View/Edit Contractor Grade' where name='WorksContractorGradeViewEdit' and contextroot = 'egworks';