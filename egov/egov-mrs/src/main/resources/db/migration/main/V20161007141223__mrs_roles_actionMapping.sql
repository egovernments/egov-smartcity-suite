---------------Create new Role--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Marriage Registration Admin', 'Marriage Registration Administrator', now(), 1, 1, now(), 0);

INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Marriage Registration Creator', 'Marriage Registration Creator', now(), 1, 1, now(), 0);

INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Marriage Registration Approver', 'Marriage Registration Approver', now(), 1, 1, now(), 0);

INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'Marriage Registration RprtViewr', 'Marriage Registration Report Viewer', now(), 1, 1, now(), 0);

---------------Map the Action urls to the role--------------------

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application)
VALUES (nextval('seq_eg_action'), 'generateMarriageCertificate', '/certificate/registration', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 7, 'Generate Marriage Certificate', false, 'mrs', 0, 1, now(), 1, now(),
(select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'generateMarriageCertificate'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'generateMarriageCertificate'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'CreateRegistration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'RegistrationDataEntry'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (SELECT id FROM eg_action WHERE name = 'RegistrationDataEntry'));

