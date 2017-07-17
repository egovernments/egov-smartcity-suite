INSERT INTO EG_ACTION (ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER, DISPLAYNAME, ENABLED, CONTEXTROOT, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, APPLICATION)
VALUES (NEXTVAL('SEQ_EG_ACTION'), 'tldemandnoticereport', '/demandnotice/report', NULL, (SELECT id
                                                                                         FROM EG_MODULE
                                                                                         WHERE name =
                                                                                               'Trade License Reports'),
                                  1, 'tldemandnoticereport', FALSE, 'tl', 0, 1, now(), 1, now(), (SELECT id
                                                                                                  FROM eg_module
                                                                                                  WHERE name =
                                                                                                        'Trade License'
                                                                                                        AND
                                                                                                        parentmodule IS
                                                                                                        NULL));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TLCreator'), (SELECT id
                                                                                     FROM eg_action
                                                                                     WHERE
                                                                                       name = 'tldemandnoticereport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TLApprover'), (SELECT id
                                                                                      FROM eg_action
                                                                                      WHERE
                                                                                        name = 'tldemandnoticereport'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TLAdmin'), (SELECT id
                                                                                   FROM eg_action
                                                                                   WHERE
                                                                                     name = 'tldemandnoticereport'));
