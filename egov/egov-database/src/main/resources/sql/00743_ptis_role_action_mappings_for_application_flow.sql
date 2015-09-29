Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'AlterAssessment-Form','/search/searchProperty-commonForm.action', 'applicationType=Alter_Assessment',(select id from EG_MODULE where name = 'Existing property'),null,
'Addition/Alteration of Assessment','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'BifurcateAssessment-Form','/search/searchProperty-commonForm.action', 'applicationType=Bifuracate_Assessment',(select id from EG_MODULE where name = 'Existing property'),null,
'Bifurcation of Assessment','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'TransferOwnership-Form','/search/searchProperty-commonForm.action', 'applicationType=Transfer_of_Ownership',(select id from EG_MODULE where name = 'Existing property'),null,
'Transfer Ownership','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'RevisionPetition-Form','/search/searchProperty-commonForm.action', 'applicationType=Revision Petition',(select id from EG_MODULE where name = 'Existing property'),null,
'Create Revision Petition','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'CollectTax-Form','/search/searchProperty-commonForm.action', 'applicationType=Collect_Tax',(select id from EG_MODULE where name = 'Existing property'),null,
'Collect Tax','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'GenerateDemandBill-Form','/search/searchProperty-commonForm.action', 'applicationType=Generate_demand_bill',(select id from EG_MODULE where name = 'Existing property'),null,
'Generate Demand Bill','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'Assessment-commonSearch','/search/searchProperty-commonSearch.action', null,(select id from EG_MODULE where name = 'Existing property'),null,
null,'f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator'), id from eg_action where name = 'AlterAssessment-Form';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator'), id from eg_action where name = 'Assessment-commonSearch';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator'), id from eg_action where name = 'BifurcateAssessment-Form';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator'), id from eg_action where name = 'TransferOwnership-Form';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator'), id from eg_action where name = 'RevisionPetition-Form';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator'), id from eg_action where name = 'GenerateDemandBill-Form';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'CSC Operator'), id from eg_action where name = 'CollectTax-Form';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'CSC Operator'), id from eg_action where name = 'Assessment-commonSearch';