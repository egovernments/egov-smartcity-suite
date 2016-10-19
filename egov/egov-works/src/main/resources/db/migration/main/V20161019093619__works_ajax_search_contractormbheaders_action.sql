insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'AjaxSearchContractorMBHeaders','/mbheader/ajaxcontractormbheaders',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Ajax Search Contractor MB Headers',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));



insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxSearchContractorMBHeaders' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='AjaxSearchContractorMBHeaders' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='AjaxSearchContractorMBHeaders' and contextroot = 'egworks'));



insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values(NEXTVAL('SEQ_EG_ACTION'),'Contractor MB View','/contractorportal/mb/view',
null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'Contractor MB View',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));



insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='Contractor MB View' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='Contractor MB View' and contextroot = 'egworks'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Approver'),(select id from eg_action where name ='Contractor MB View' and contextroot = 'egworks'));