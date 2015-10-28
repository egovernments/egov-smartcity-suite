delete from eg_roleaction where actionid in(select id from eg_action where name in('Contra-BTBView'));
delete from eg_action where name in('Contra-BTBView');

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('seq_eg_action'),'Contra-BTBView','/contra/contraBTC-redirect.action',null,399,null,null,'false','EGF',0,1,'2015-07-15 19:14:54.980736',1,'2015-07-15 19:14:54.980736',302);
INSERT INTO eg_roleaction (roleid, actionid) VALUES ( (select id from eg_role where name='Super User'),(select id from eg_action where name='Contra-BTBView'));
