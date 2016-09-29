Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'EstimateBudgetDetailsByFund','/voucher/common-ajaxLoadEstimateBudgetDetailsByFundId.action',null,
(select id from eg_module where name='Budget Reports'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='EstimateBudgetDetailsByFund'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='EstimateBudgetDetailsByFund'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='EstimateBudgetDetailsByFund'));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'EstimateBudgetDetailsByDept','/voucher/common-ajaxLoadEstimateBudgetDetailsByDepartmentId.action',null,
(select id from eg_module where name='Budget Reports'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='EstimateBudgetDetailsByDept'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='EstimateBudgetDetailsByDept'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='EstimateBudgetDetailsByDept'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'EstimateBudgetDetailsByFunc','/voucher/common-ajaxLoadEstimateBudgetDetailsByFuncId.action',null,
(select id from eg_module where name='Budget Reports'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='EstimateBudgetDetailsByFunc'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='ERP Report Viewer'),(select id from eg_action where name='EstimateBudgetDetailsByFunc'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Report Viewer'),(select id from eg_action where name='EstimateBudgetDetailsByFunc'));



