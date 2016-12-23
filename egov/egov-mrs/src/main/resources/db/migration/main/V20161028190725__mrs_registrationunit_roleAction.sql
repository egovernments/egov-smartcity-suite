INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'show-mrregistrationunitzone', '/registration/getmrregistrationunitzone', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), 2, 'show-mrregistrationunitzone', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'), (SELECT id FROM eg_action WHERE name = 'show-mrregistrationunitzone'));
INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Creator'), (SELECT id FROM eg_action WHERE name = 'show-mrregistrationunitzone'));


alter table egmrs_reissue add column RegistrationUnit bigint ;

alter table egmrs_reissue add column zone bigint;

alter table egmrs_reissue add CONSTRAINT fk_reissue_registrationunit FOREIGN KEY (RegistrationUnit) REFERENCES egmrs_registrationunit (id);

alter table egmrs_reissue add CONSTRAINT fk_reissue_zone FOREIGN KEY (zone) REFERENCES eg_boundary (id);