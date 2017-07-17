INSERT INTO eg_action( id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot,
 version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
VALUES ( nextval('seq_eg_action'),'Reassign','/reassign',null,(select id from eg_module where name='Property Tax'),'1','Reassign',false,'ptis',0,'1',now(),'1',now(),0);


Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Verifier'),
(select id from eg_action where name='Reassign'));

