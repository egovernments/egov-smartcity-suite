----add column type-----
alter table egpt_objection add column type varchar(20);
update egpt_objection set type='RP';
update eg_wf_states set value=replace(value,'Revision Petition:','RP:') where value like'Revision Petition:%';
update eg_wf_state_history set value=replace(value,'Revision Petition:','RP:') where value like'Revision Petition:%';

------eg_action--------
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Property Tax Gen Rev Petition New','/revPetition/genRevPetition-newForm.action',null,(select id from eg_module where name='Existing property'),1,'Property Tax Gen Rev Petition New',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));


insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Tax Gen Rev Petition New' and contextroot='ptis'), id from eg_role where name='CSC Operator';

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Tax Gen Rev Petition New' and contextroot='ptis'), id from eg_role where name='ULB Operator';

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Tax Gen Rev Petition New' and contextroot='ptis'), id from eg_role where name='Super User';

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Property Tax Gen Rev Petition New' and contextroot='ptis'), id from eg_role where name='MeeSeva Operator';


