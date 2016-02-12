

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ExpenseBillXls','/bill/expenseBillPrint-exportXls.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ExpenseBillXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ExpenseBillXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ExpenseBillXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Approver'),(select id from eg_action where name='ExpenseBillXls'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ExpenseBillXls'));



Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'ExpenseBillPdf','/bill/expenseBillPrint-exportPdf.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='ExpenseBillPdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Creator'),(select id from eg_action where name='ExpenseBillPdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Payment Approver'),(select id from eg_action where name='ExpenseBillPdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Approver'),(select id from eg_action where name='ExpenseBillPdf'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Creator'),(select id from eg_action where name='ExpenseBillPdf'));

