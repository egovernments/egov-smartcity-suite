-----------------Role action mappings to search LOA Numbers to Cancel MB----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchLoaNumbersToCancelMB','/measurementbook/ajaxloanumbers-mbtocancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),1,'Search LOA Numbers to cancel MB','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchLoaNumbersToCancelMB' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchLoaNumbersToCancelMB' and contextroot = 'egworks'));

-----------------Role action mappings to search Contractors to Cancel MB----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchContractorsToCancelMB','/measurementbook/ajaxcontractors-mbtocancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),1,'Search Contractors to cancel MB','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchContractorsToCancelMB' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchContractorsToCancelMB' and contextroot = 'egworks'));

-----------------Role action mappings to search Work Id Numbers to Cancel MB----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchWorkIdNumbersToCancelMB','/measurementbook/ajaxworkidentificationnumbers-mbtocancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),1,'Search Work Id Numbers to cancel MB','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'SearchWorkIdNumbersToCancelMB' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SearchWorkIdNumbersToCancelMB' and contextroot = 'egworks'));

-----------------Role action mappings to validate lastest MB to Cancel MB----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ValidateIsLatestMB','/measurementbook/ajaxvalidatelatestmb-mbtocancel',null,(select id from EG_MODULE where name = 'WorksAdministrator'),1,'Validate MB if it is latest','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Administrator'), (select id from eg_action where name = 'ValidateIsLatestMB' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ValidateIsLatestMB' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ValidateIsLatestMB' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='ValidateIsLatestMB' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'ValidateIsLatestMB' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchWorkIdNumbersToCancelMB' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchWorkIdNumbersToCancelMB' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchWorkIdNumbersToCancelMB' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchContractorsToCancelMB' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchContractorsToCancelMB' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchContractorsToCancelMB' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchLoaNumbersToCancelMB' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Administrator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchLoaNumbersToCancelMB' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'SearchLoaNumbersToCancelMB' and contextroot = 'egworks';