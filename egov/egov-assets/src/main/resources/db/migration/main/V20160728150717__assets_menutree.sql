update eg_module set displayname ='Asset Category' where name='Asset Masters Category';
update eg_action set displayname ='Create Asset Category' where name='New-AssetCategory';
update eg_action set displayname ='Modify Asset Category' where name='Search and Edit-AssetCategory';
update eg_action set displayname ='View Asset Category' where name='Search and View-AssetCategory';
insert into eg_module (id,name,enabled,parentmodule,displayname,ordernumber) values(nextval('seq_eg_module'),'Asset Master',true,(select id from eg_module where name='Asset Masters'),'Asset Master',2);
update eg_action set parentmodule =(select id from eg_module where name='Asset Master') where parentmodule=(select id from eg_module where name='Asset Transactions');
update eg_action set displayname ='Create Asset' where name='New-Asset';
update eg_action set displayname ='Modify Asset' where name='Search and Edit-Asset';