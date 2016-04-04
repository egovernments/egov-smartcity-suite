Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
createdby, createddate, lastmodifiedby, lastmodifieddate, application) values
(nextval('seq_eg_action'),'common-tree-coa','/voucher/common-ajaxloadcoa.action',null,(select id from eg_module where name='EGF-COMMON'),
null,null,'false','EGF',0,1,'2015-07-15',1,'2015-07-15',(select id from eg_module where name ='EGF'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='common-tree-coa'));
