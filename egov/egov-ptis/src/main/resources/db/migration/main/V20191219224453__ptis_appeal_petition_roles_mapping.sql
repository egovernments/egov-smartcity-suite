-----------role action mapping-------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AppealPetition-Form','/search/searchproperty-appealpetition.action', null,
(select id from EG_MODULE where name = 'Existing property'),null,'Appeal Petition','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Property Tax Appeal Petition New','/revPetition/appealpetition-newform.action',null,(select id from eg_module where name='Existing property'),1,'Property Tax Appeal Petition New',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'AppealPetition-Form' and contextroot='ptis'), id from eg_role where name in ('CSC Operator','CITIZEN','PUBLIC','SYSTEM');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Tax Appeal Petition New' and contextroot='ptis'), id from eg_role where name in ('CSC Operator','CITIZEN','PUBLIC','SYSTEM');
