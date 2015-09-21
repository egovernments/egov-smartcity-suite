
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'searchTrade-search','/search/searchTrade-search.action',(select id from eg_module where name='Search Trade'),1,'searchTrade-search',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='searchTrade-search'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'editTradeLicense-edit','/newtradelicense/editTradeLicense-edit.action',(select id from eg_module where name='Trade License Transactions'),1,'editTradeLicense-edit',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='editTradeLicense-edit'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'newTradeLicense-create','/newtradelicense/newTradeLicense-create.action',(select id from eg_module where name='Trade License Transactions'),1,'newTradeLicense-create',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='newTradeLicense-create'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'transferTradeLicense-create','/transfer/transferTradeLicense-create.action',(select id from eg_module where name='Trade License Transactions'),1,'transferTradeLicense-create',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='transferTradeLicense-create'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'transferTradeLicense-edit','/transfer/transferTradeLicense-edit.action',(select id from eg_module where name='Trade License Transactions'),1,'transferTradeLicense-edit',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='transferTradeLicense-edit'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'viewTradeLicense-view','/viewtradelicense/viewTradeLicense-view.action',(select id from eg_module where name='Trade License Transactions'),1,'viewTradeLicense-view',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='viewTradeLicense-view'));



Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'objection-create','/objection/objection-create.action',(select id from eg_module where name='Trade License Transactions'),1,'objection-create',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='objection-create'));


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'ajax-populateDivisions','/domain/commonTradeLicenseAjax-populateDivisions.action',(select id from eg_module where name='Trade License Transactions'),1,'ajax-populateDivisions',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='ajax-populateDivisions'));

