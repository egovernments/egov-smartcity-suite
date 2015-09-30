Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'tradeLicenseSubCategoryAjax','/domain/commonTradeLicenseAjax-populateSubCategory.action',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,'tradeLicenseSubCategoryAjax','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLCreator'), id from eg_action where name='tradeLicenseSubCategoryAjax' and contextroot = 'tl';


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'tradeLicenseLocalityAjax','/domain/commonTradeLicenseAjax-blockByLocality',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,'tradeLicenseLocalityAjax','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name='tradeLicenseLocalityAjax' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLCreator'), id from eg_action where name='tradeLicenseLocalityAjax' and contextroot = 'tl';

