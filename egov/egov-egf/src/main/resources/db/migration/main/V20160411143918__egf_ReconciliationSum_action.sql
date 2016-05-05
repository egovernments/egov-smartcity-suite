

update eg_action set enabled ='f' where name='Reconciliation Summary';


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, 
createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'Reconciliation Summary-New','/brs/bankReconciliation-newForm.action',null,
(select id from eg_module where name='BRS'),
null,'Reconciliation Summary','true','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='Reconciliation Summary-New'));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, 
createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'Reconciliation Summary-summary','/brs/bankReconciliation-brsSummary.action',null,
(select id from eg_module where name='BRS'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='Reconciliation Summary-summary'));
