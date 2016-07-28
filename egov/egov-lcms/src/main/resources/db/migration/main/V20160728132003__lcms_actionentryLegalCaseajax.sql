Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'legalcasepopulatePetitionList','/legalcase/ajax-petitionTypeByCourtType',(select id from eg_module 
 where name='LCMS Transactions'),1,'legalcasepopulatePetitionList',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='legalcasepopulatePetitionList'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'legalcasepopulateCourtNameList','/legalcase/ajax-courtNameByCourtType',(select id from eg_module 
 where name='LCMS Transactions'),1,'legalcasepopulateCourtNameList',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='legalcasepopulateCourtNameList'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application)
 values(nextval('SEQ_EG_ACTION'),'AddStandingCouncil','/standingCouncil/create/',(select id from eg_module 
 where name='LCMS Transactions'),1,'AddStandingCouncil',false,'lcms',(select id from eg_module where name='LCMS' 
 and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='AddStandingCouncil'));

