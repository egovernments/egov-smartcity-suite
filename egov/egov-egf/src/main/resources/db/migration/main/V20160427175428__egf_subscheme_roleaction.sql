

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'NameUniqueChecks','/masters/subScheme-nameUniqueCheck.action',null,
(select id from eg_module where name='Masters'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'CodeUniqueChecks','/masters/subScheme-codeUniqueCheck.action',null,
(select id from eg_module where name='Masters'),
null,null,'false','EGF',0,1,current_date,1,current_date,
(select id from eg_module where contextroot='egf' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='NameUniqueChecks'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='NameUniqueChecks'));

INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='CodeUniqueChecks'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='CodeUniqueChecks'));
