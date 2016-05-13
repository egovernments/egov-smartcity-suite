
Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, 
version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'CancelBill-search','/voucher/cancelBill-search.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='CancelBill-search'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Creator'),(select id from eg_action where name='CancelBill-search'));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, 
version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'CancelBill-cancelBill','/voucher/cancelBill-cancelBill.action',null,
(select id from eg_module where name='Payments'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='CancelBill-cancelBill'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Creator'),(select id from eg_action where name='CancelBill-cancelBill'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Creator'),(select id from eg_action where name='Cancel Bills'));



