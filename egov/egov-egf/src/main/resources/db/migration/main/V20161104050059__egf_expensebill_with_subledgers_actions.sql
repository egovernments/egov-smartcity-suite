-----------------Role action mappings to Create Expense Bill ----------------
insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CreateExpenseBillNewForm','/expensebill/newform',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'New Create Expense Bill','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='CreateExpenseBillNewForm' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='CreateExpenseBillNewForm' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CreateExpenseBillCreate','/expensebill/create',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Create Expense Bill','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='CreateExpenseBillCreate' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='CreateExpenseBillCreate' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ExpenseBillSuccess','/expensebill/success',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Create Expense Bill','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='ExpenseBillSuccess' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='ExpenseBillSuccess' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'CreateExpenseBillSave','/expensebill/save',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Create Expense Bill','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='CreateExpenseBillSave' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='CreateExpenseBillSave' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetSchemesByFundId','/common/getschemesbyfundid',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get Schemes By FundId','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetSchemesByFundId' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='GetSchemesByFundId' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetSubSchemesBySchemeId','/common/getsubschemesbyschemeid',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get SubSchemes By SchemeId','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetSubSchemesBySchemeId' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='GetSubSchemesBySchemeId' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetFundSourcesBySubSchemeId','/common/getfundsourcesbysubschemeid',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get FundSources By SubSchemeId','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetFundSourcesBySubSchemeId' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='GetFundSourcesBySubSchemeId' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxFunctionNames','/common/ajaxfunctionnames',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Ajax Function Names','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='AjaxFunctionNames' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='AjaxFunctionNames' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetEntitesByAccountDetailType','/common/getentitesbyaccountdetailtype',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get Entites By Account Detail Type','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetEntitesByAccountDetailType' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='GetEntitesByAccountDetailType' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetAccountCodesForAccountDetailType','/common/getaccountcodesforaccountdetailtype',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get AccountCodes For AccountDetailType','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetAccountCodesForAccountDetailType' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='GetAccountCodesForAccountDetailType' and contextroot = 'EGF'));


insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetNetPayableCodesByAccountDetailType','/common/getnetpayablecodesbyaccountdetailtype',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'GetNetPayable Codes By AccountDetailType','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetNetPayableCodesByAccountDetailType' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='GetNetPayableCodesByAccountDetailType' and contextroot = 'EGF'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GetCheckListByBillSubType','/common/getchecklistbybillsubtype',null,(select id from EG_MODULE where name = 'Bill Registers'),1,'Get CheckList By BillSubType','false','EGF',0,1,now(),1,now(),(select id from eg_module  where name = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='GetCheckListByBillSubType' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Creator'),(select id from eg_action where name ='GetCheckListByBillSubType' and contextroot = 'EGF'));




--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='CreateExpenseBillNewForm' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CreateExpenseBillNewForm' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'CreateExpenseBillNewForm' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='CreateExpenseBillCreate' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CreateExpenseBillCreate' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'CreateExpenseBillCreate' and contextroot = 'EGF';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='ExpenseBillSuccess' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ExpenseBillSuccess' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'ExpenseBillSuccess' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='CreateExpenseBillSave' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='CreateExpenseBillSave' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'CreateExpenseBillSave' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetSchemesByFundId' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetSchemesByFundId' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetSchemesByFundId' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetSubSchemesBySchemeId' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetSubSchemesBySchemeId' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetSubSchemesBySchemeId' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetFundSourcesBySubSchemeId' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetFundSourcesBySubSchemeId' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetFundSourcesBySubSchemeId' and contextroot = 'EGF';



--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxFunctionNames' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='AjaxFunctionNames' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'AjaxFunctionNames' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetEntitesByAccountDetailType' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetEntitesByAccountDetailType' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetEntitesByAccountDetailType' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetAccountCodesForAccountDetailType' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetAccountCodesForAccountDetailType' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetAccountCodesForAccountDetailType' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetNetPayableCodesByAccountDetailType' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetNetPayableCodesByAccountDetailType' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetNetPayableCodesByAccountDetailType' and contextroot = 'EGF';


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Bill Creator') and actionid = (SELECT id FROM eg_action WHERE name ='GetCheckListByBillSubType' and contextroot = 'EGF');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='GetCheckListByBillSubType' and contextroot = 'EGF');
--rollback delete FROM EG_ACTION WHERE name = 'GetCheckListByBillSubType' and contextroot = 'EGF';

