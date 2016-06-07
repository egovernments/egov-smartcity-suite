insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'FunctionsByFundIdAndDepartmentId','/lineestimate/getfunctionsbyfundidanddepartmentid',null,(select id from EG_MODULE where name = 'WorksLineEstimate'),1,'FunctionsByFundIdAndDepartmentId','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name ='FunctionsByFundIdAndDepartmentId' and contextroot = 'egworks'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='FunctionsByFundIdAndDepartmentId' and contextroot = 'egworks'));

update eg_action set url = '/lineestimate/getbudgethead' where name = 'AjaxLoadBudgetHeadByFunction';



--rollback update eg_action set url = '/lineestimate/getbudgetheadbyfunction' where name = 'AjaxLoadBudgetHeadByFunction';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name ='FunctionsByFundIdAndDepartmentId' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='FunctionsByFundIdAndDepartmentId' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'FunctionsByFundIdAndDepartmentId' and contextroot = 'egworks';


