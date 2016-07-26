
insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,MODULE) values (nextval('seq_eg_appconfig'),'DataEntryCutOffDate',
'Data entry cut off date',0,(select id from eg_module where name='EGF'));


Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'contingentBillApprove','/bill/contingentBill-approveOnCreate.action',null,
(select id from eg_module where name='Bill Registers'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='contingentBillApprove'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Bill Creator'),(select id from eg_action where name='contingentBillApprove'));
