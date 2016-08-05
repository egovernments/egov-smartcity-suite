Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot,
 version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('SEQ_EG_ACTION'),'chequeAssignmentPrint',
 '/payment/chequeAssignmentPrint-generateChequeFormat.action',
null,(select id from eg_module where name='Payments'),11,'chequeAssignmentPrint','false','EGF',0,1,now(),1,now(),
(select id from eg_module where name = 'EGF' and parentmodule is null));

INSERT INTO eg_roleaction (roleid, actionid)
SELECT (select id from eg_role where name='Super User'), (select id from eg_action where name='chequeAssignmentPrint')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Super User') and actionid in 
 (select id from eg_action where name='chequeAssignmentPrint'));
