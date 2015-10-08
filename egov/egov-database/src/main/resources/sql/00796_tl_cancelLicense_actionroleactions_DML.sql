--Action and Roleaction Mappings for Cancel License
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Cancel License','/search/searchLicense-commonForm.action','mode=Cancel License',(select id from EG_MODULE where name = 'Trade License Transactions'),3,'Cancel License','t','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Cancel License Search','/search/searchLicense-commonSearch.action',null,(select id from EG_MODULE where name = 'Trade License Transactions'),4,'Cancel License Search','f','tl',0,1,now(),1,now(),(select id from eg_module  where name = 'Trade License'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover'), id from eg_action where name='Cancel License' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover'), id from eg_action where name='Cancel License Search' and contextroot = 'tl';

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'TLApprover'), id from eg_action where name='Cancel Trade License New Form' and contextroot = 'tl';

