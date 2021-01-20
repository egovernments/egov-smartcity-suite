INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,createdby, createddate, lastmodifiedby, lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'), 'MarriageRegistrationRejectionNotice', '/registration/rejectionnotice', null,(select id from eg_module where name='Marriage Registration'), 3, 'Marriage Rejection Notice', false,'mrs', 0, 1, now(), 1, now(),(select id from eg_module where name='Marriage Registration'));


INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Marriage Registration Approver'), (select id from eg_action where name='MarriageRegistrationRejectionNotice'));
