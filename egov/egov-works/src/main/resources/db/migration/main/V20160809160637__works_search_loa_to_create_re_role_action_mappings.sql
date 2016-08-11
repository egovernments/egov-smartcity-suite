---------Revision Estimate Module--------
insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksRevisionEstimate','true',null,(select id from eg_module where name = 'Works Management'),'Revision Estimate', 5); 

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksSearchLOAToCreateRE','/searchletterofacceptance/searchloare-form',null,(select id from EG_MODULE where name = 'WorksRevisionEstimate'),2,'Create Revision Estimate','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksSearchLOAToCreateRE' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='WorksSearchLOAToCreateRE' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchLOAToCreateRE' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='WorksSearchLOAToCreateRE' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksSearchLOAToCreateRE' and contextroot = 'egworks';

--rollback delete from eg_module where name='WorksRevisionEstimate';