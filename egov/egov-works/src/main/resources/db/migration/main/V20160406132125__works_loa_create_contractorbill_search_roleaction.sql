----Search/View LOA Link Role Action
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SearchLOAToCreateContractorBillForLOA','/searchletterofacceptance/searchformloa-contractorbill',null,(select id from EG_MODULE where name = 'WorksContractorBill'),1,'Search/View LOA','true','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SearchLOAToCreateContractorBillForLOA' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'SearchLOAToCreateContractorBillForLOA' and contextroot = 'egworks'));

-----------------Role action mappings to search LOA Result----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchLetterOfAcceptanceToCreateContractorBill','/letterofacceptance/ajaxsearch-loaforcontractorbill',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),0,'AjaxSearchLetterOfAcceptanceToCreateContractorBill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchLetterOfAcceptanceToCreateContractorBill' and contextroot = 'egworks'));
insert into eg_roleaction(roleid, actionid) values ((select id from eg_role where name = 'Works Creator'), (select id from eg_action where name = 'AjaxSearchLetterOfAcceptanceToCreateContractorBill' and contextroot = 'egworks'));

-----------------Role action mappings to search estimate numbers for LOA----------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxSearchEstimateNumbersForContractorBill','/letterofacceptance/ajaxestimatenumbers-contractorbill',null,(select id from eg_module where name='WorksLetterOfAcceptance'),1,'AjaxSearchEstimateNumbersForContractorBill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxSearchEstimateNumbersForContractorBill'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchEstimateNumbersForContractorBill' and contextroot = 'egworks'));

-----------------Role action mappings to search letter of acceptance numbers----------------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxSearchLOANumberForContractorBill','/letterofacceptance/ajaxloanumber-contractorbill',null,(select id from eg_module where name='WorksLetterOfAcceptance'),1,'Ajax Search LOA NUMBER For Contractor Bill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxSearchLOANumberForContractorBill'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchLOANumberForContractorBill' and contextroot = 'egworks'));

-----------------Role action mappings to search contractor for LOA numbers----------------------
insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'AjaxSearchContractorNameforContractorBill','/letterofacceptance/ajaxsearchcontractors-loaforcontractorbill',null,(select id from eg_module where name='WorksLetterOfAcceptance'),1,'AjaxSearchLOANumberForContractorBill','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='AjaxSearchContractorNameforContractorBill'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchContractorNameforContractorBill' and contextroot = 'egworks'));

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='SearchLOAToCreateContractorBillForLOA' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='SearchLOAToCreateContractorBillForLOA' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLetterOfAcceptanceToCreateContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLetterOfAcceptanceToCreateContractorBill' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateNumbersForContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchEstimateNumbersForContractorBill' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLOANumberForContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchLOANumberForContractorBill' and contextroot = 'egworks');

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchContractorNameforContractorBill' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxSearchContractorNameforContractorBill' and contextroot = 'egworks');

--rollback delete from eg_action where name in ('SearchLOAToCreateContractorBillForLOA','AjaxSearchLetterOfAcceptanceToCreateContractorBill','AjaxSearchEstimateNumbersForContractorBill','AjaxSearchLOANumberForContractorBill','AjaxSearchContractorNameforContractorBill') and contextroot = 'egworks';