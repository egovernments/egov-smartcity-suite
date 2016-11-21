insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,CREATEDBY,CREATEDDATE,MODULE) values (nextval('seq_eg_appconfig'),'BILL_EDIT_DESIGNATIONS',
'Edit bill based on designation',0,1,current_date,(select id from eg_module where name='EGF'));

insert into EG_APPCONFIG_VALUES (ID,KEY_ID,EFFECTIVE_FROM,VALUE,VERSION) values (nextval('seq_eg_appconfig_values'),
(select id from EG_APPCONFIG where KEY_NAME ='BILL_EDIT_DESIGNATIONS'),current_date,'Examiner of Accounts',0);
insert into EG_APPCONFIG_VALUES (ID,KEY_ID,EFFECTIVE_FROM,VALUE,VERSION) values (nextval('seq_eg_appconfig_values'),
(select id from EG_APPCONFIG where KEY_NAME ='BILL_EDIT_DESIGNATIONS'),current_date,'Assistant Examiner of accounts',0);


insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='GetSchemesByFundId' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='GetSubSchemesBySchemeId' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='AjaxFunctionNames' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='GetFundSourcesBySubSchemeId' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='GetEntitesByAccountDetailType' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='GetAccountCodesForAccountDetailType' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='GetNetPayableCodesByAccountDetailType' and contextroot = 'EGF'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='GetCheckListByBillSubType' and contextroot = 'EGF'));

--rollback delete from EG_APPCONFIG_VALUES where KEY_ID =(select id from EG_APPCONFIG where KEY_NAME ='BILL_EDIT_DESIGNATIONS');
--rollback delete from EG_APPCONFIG where KEY_NAME ='BILL_EDIT_DESIGNATIONS';
--rollback delete from eg_roleaction where roleid=(select id from eg_role where name = 'Bill Approver') and actionid in(select id from eg_action where name in('GetSchemesByFundId','GetSubSchemesBySchemeId','AjaxFunctionNames','GetFundSourcesBySubSchemeId','GetEntitesByAccountDetailType','GetAccountCodesForAccountDetailType','GetNetPayableCodesByAccountDetailType','GetCheckListByBillSubType'));

