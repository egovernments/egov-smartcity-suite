INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES
  (nextval('SEQ_EG_ROLE'), 'TL VIEW ACCESS', 'user has access to view masters, reports, transactional data, etc', now(),
   1, 1, now(), 0);

INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'View License Category';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'View License SubCategory';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'tradeLicenseSubCategoryAjax';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'View Unit Of Measurement';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'feematrix-view';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Ajax-SubCategoryByParent';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'feematrix-resultview';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Search and View-Validity';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'Search and View Result-Validity';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'SearchTradeLicense';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'searchTrade-search';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'SearchAjax-PopulateData';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'viewTradeLicense-view';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'TradeLicenseDCBReportLocalityWise';
INSERT INTO eg_roleaction (roleid, actionid) SELECT
                                               (SELECT id
                                                FROM eg_role
                                                WHERE name = 'TL VIEW ACCESS'),
                                               id
                                             FROM eg_action
                                             WHERE name = 'TLDCBReportList';

