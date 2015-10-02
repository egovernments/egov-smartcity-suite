Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create-FeeMatrix','/feematrix/create',(select id from eg_module where name='Trade License Masters'),1,'Create-FeeMatrix',true,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create-FeeMatrix'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update-FeeMatrix','/feematrix/update',(select id from eg_module where name='Trade License Masters'),1,'Update-FeeMatrix',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Update-FeeMatrix'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'View-FeeMatrix','/feematrix/view',(select id from eg_module where name='Trade License Masters'),1,'View-FeeMatrix',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='View-FeeMatrix'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Edit-FeeMatrix','/feematrix/edit',(select id from eg_module where name='Trade License Masters'),1,'Edit-FeeMatrix',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Edit-FeeMatrix'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Ajax-SubCategoryByParent','/domain/commonAjax-ajaxPopulateSubCategory.action',(select id from eg_module where name='Trade License Masters'),1,'Ajax-SubCategoryByParent',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Ajax-SubCategoryByParent'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Search-FeeMatrix','/feematrix/search',(select id from eg_module where name='Trade License Masters'),1,'Search-FeeMatrix',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search-FeeMatrix'));
