Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'chequeIssueRegisterReport-bankAdviceExcel','/report/chequeIssueRegisterReport-bankAdviceExcel.action',null,
(select id from eg_module where name='MIS Reports'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction(actionid,roleid) select (select id from eg_action where name='chequeIssueRegisterReport-bankAdviceExcel'), id from eg_role where name in('Super User','Financial Report Viewer','ERP Report Viewer');


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'chequeAssignment-bankAdviceExcel','/payment/chequeAssignment-bankAdviceExcel.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction(actionid,roleid) select (select id from eg_action where name='chequeAssignment-bankAdviceExcel'), id from eg_role where name in('Super User','Payment Creator');

