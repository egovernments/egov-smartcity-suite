INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES
  (nextval('SEQ_EG_ROLE'), 'PGR VIEW ACCESS', 'user has access to view masters, reports, transactional data, etc',
   now(), 1, 1, now(), 0);


INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'ViewComplaintType';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'viewComplaintTypeResult';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'View Router';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'ajaxsearchallcomplaintTypes';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'AjaxRouterBoundariesbyType';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Search viewRouter Result';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'RouterView';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Search Escalation Time';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'AjaxEscTimeDesignation';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Search Escalation Time result';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'View Escalation';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'AjaxEscalationPosition';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Search Escalation result';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'SearchComplaintFormOfficial';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Ageing Report By Boundary wise';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Ageing report search result';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Ageing Report By Department wise';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Drill Down Report By Boundary wise';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Drill Down Report By Department wise';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Drill Down Report search result';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Complaint Type Wise Report';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Complaint Type Wise Report search result';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Functionary Wise Report Search';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'PGR VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Functionary Wise Report Result';

