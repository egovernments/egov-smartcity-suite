update eg_module set name='Council Management Transaction' where parentmodule= (select id from eg_module where name='Council Management') and displayname='Transactions';

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Preamble', true, 'council', (select id from eg_module where name='Council Management Transaction'), 'Council Preamble', 1);

INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council Agenda', true, 'council', (select id from eg_module where name='Council Management Transaction'), 'Council Agenda', 2);

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-CouncilPreamble','/councilpreamble/new',(select id from eg_module where name='Council Preamble' and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Create Preamble',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-CouncilPreamble'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-CouncilPreamble','/councilpreamble/create',(select id from eg_module where name='Council Preamble' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Create-CouncilPreamble',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-CouncilPreamble'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-CouncilPreamble','/councilpreamble/result',(select id from eg_module where name='Council Preamble' and 
parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Result-CouncilPreamble',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-CouncilPreamble'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Download-documnets','/councilpreamble/downloadfile',(select id from eg_module where name='Council Preamble' and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Download-Photo',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Download-documnets'));




INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update Preamble','/councilpreamble/update',(select id from eg_module where name='Council Preamble' 
and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Update Preamble',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update Preamble'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-CouncilPreamble','/councilpreamble/search/edit',(select id from eg_module where name='Council Preamble' 
and parentmodule=(select id from eg_module where name='Council Management Transaction')),3,'Update Preamble',true,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CouncilPreamble'));



INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CouncilPreamble','/councilpreamble/search/view',(select id from eg_module where name='Council Preamble' 
and parentmodule=(select id from eg_module where name='Council Management Transaction')),2,'View Preamble',true,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CouncilPreamble'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-CouncilPreamble','/councilpreamble/ajaxsearch/view',(select id from eg_module where name='Council Preamble' and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'Search and View Result-CouncilPreamble',false,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

 
INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CouncilPreamble'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'View-CouncilPreamble','/councilpreamble/view',(select id from eg_module where name='Council Preamble'
 and parentmodule=(select id from eg_module where name='Council Management Transaction')),1,'View-CouncilPreamble',false,'council',
 (select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CouncilPreamble'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) 
values(nextval('SEQ_EG_ACTION'),'Edit-CouncilPreamble','/councilpreamble/edit',
(select id from eg_module where name='Council Preamble' and parentmodule=
(select id from eg_module where name='Council Management Transaction'))
,1,'Edit-CouncilPreamble',false,'council',
(select id from eg_module where name='Council Management' and parentmodule is null));


INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CouncilPreamble'));



update eg_action set parentmodule=(select id from eg_module where name ='Council Agenda') ,name = 'New Agenda' where url='/agenda/new' and parentmodule =(select id from eg_module where name = 'Council Management Transaction');

update eg_action set parentmodule=(select id from eg_module where name ='Council Agenda') where url ='/agenda/ajaxsearch' and parentmodule =(select id from eg_module where name = 'Council Management Transaction');

update eg_action set parentmodule=(select id from eg_module where name ='Council Agenda') where url ='agenda/create' and parentmodule =(select id from eg_module where name = 'Council Management Transaction');


INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Council CommitteeType', true, 'council', (select id from eg_module where name='Council Management Master'), 'Committee Type', 6);



INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'New-Committteetype','/committeetype/new',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Create CommitteeType',true,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='New-Committteetype'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-CouncilCommittee','/committeetype/create',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Create-CouncilCommittee',false,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-CouncilCommittee'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-CouncilCommittee','/committeetype/view',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'View-CouncilCommittee',false,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-CouncilCommittee'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Result-CouncilCommittee','/committeetype/result',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Result-CouncilCommittee',false,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Result-CouncilCommittee'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View-CouncilCommittee','/committeetype/search/view',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'View Committee Type',true,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View-CouncilCommittee'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit-CouncilCommittee','/committeetype/search/edit',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Update Committee Type',true,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit-CouncilCommittee'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-CouncilCommitteetype','/committeetype/update',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Update-CouncilCommitteetype',false,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-CouncilCommitteetype'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-CouncilCommitteetype','/committeetype/edit',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Edit-CouncilCommitteetype',false,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-CouncilCommitteetype'));

INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and View Result-CouncilCommitteetype','/committeetype/ajaxsearch/view',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Search and View Result-CouncilCommitteetype',false,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and View Result-CouncilCommitteetype'));


INSERT into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search and Edit Result-CouncilCommitteetype','/committeetype/ajaxsearch/edit',
(select id from eg_module where name='Council CommitteeType' 
and parentmodule=(select id from eg_module where name='Council Management Master')),1,'Search and Edit Result-CouncilCommitteetype',false,'council',(select id from eg_module where name='Council Management Master'));

INSERT into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search and Edit Result-CouncilCommitteetype'));




