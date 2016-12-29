------------------------- eg_wf_matrix -------------------
update eg_wf_matrix set validactions='Forward,Cancel Registration' where objecttype='MarriageRegistration' and  validactions='Forward,Print Rejection Certificate,Cancel Registration' and additionalrule='MARRIAGE REGISTRATION';

------------------------- eg_wf_types -------------------
update eg_wf_types set typefqn='org.egov.mrs.domain.entity.MarriageRegistration' where typefqn='org.egov.mrs.domain.entity.Registration' and module = (SELECT id FROM eg_module WHERE name='Marriage Registration');

INSERT INTO eg_wf_types (id,module,type,link,createdby,createddate,lastmodifiedby,lastmodifieddate,enabled,grouped,typefqn,displayname,version) VALUES (nextval('seq_eg_wf_types'),(SELECT id FROM eg_module WHERE name='Marriage Registration'),'ReIssue','/mrs/reissue/update/:ID',1,now(),1,now(), 'Y', 'N','org.egov.mrs.domain.entity.ReIssue', 'ReIssue',0);


------------------------- eg_action / EG_ROLEACTION -------------------
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
VALUES (nextval('seq_eg_action'), 'editReIssue', '/reissue/update', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 8, 'Edit Marriage Registration Re-Issue', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'editReIssue'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'editReIssue'));
