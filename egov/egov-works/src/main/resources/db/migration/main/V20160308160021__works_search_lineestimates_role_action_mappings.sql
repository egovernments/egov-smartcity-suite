-----------------Role action mappings to search line estimate----------------------
Insert into eg_action (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(nextval('SEQ_EG_ACTION'),'Search LineEstimate','/lineestimate/ajaxsearch',null,(select id from eg_module where name='WorksLineEstimate'),1,'Search LineEstimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Search LineEstimate'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='Search LineEstimate' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'Search LineEstimate' and contextroot = 'egworks';