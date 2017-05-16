Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Get Stakeholders List','/ajax/stakeholdersbytype',null,(select id from eg_module where name='BPA Transanctions'),8,'/ajax/stakeholdersbytype','false','bpa',0,1,now(),1,now(),
(select id from eg_module where name='BPA'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='Get Stakeholders List'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'), (select id from eg_action where name='Get Stakeholders List'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='BPA Approver'), (select id from eg_action where name='Get Stakeholders List'));

alter  table egbpa_mstr_stakeholder alter column stakeholdertype type bigint USING stakeholdertype::bigint;