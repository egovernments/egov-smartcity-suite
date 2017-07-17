CREATE OR REPLACE VIEW egtl_mv_baseregister_view
AS
  SELECT
    mv.licensenumber,
    mv.tradetitle,
    mv.licenseid,
    mv.owner,
    mv.mobile,
    mv.category,
    mv.categoryname,
    mv.subcategory,
    mv.subcategoryname,
    mv.assessmentno,
    mv.ward,
    mv.wardname,
    mv.locality,
    mv.localityname,
    mv.TradeAddress,
    mv.commencementdate,
    mv.status,
    mv.statusname,
    SUM(arrearlicensefee) AS arrearlicensefee,
    SUM(arrearPenaltyFee) AS arrearPenaltyFee,
    SUM(curLicenseFee)    AS curLicenseFee,
    SUM(curPenaltyFee)    AS curPenaltyFee
  FROM
    (SELECT
       lic.license_number        AS licensenumber,
       lic.name_of_estab         AS tradetitle,
       lic.id                    AS licenseid,
       licadd.applicant_name     AS owner,
       licadd.mobile_phonenumber AS mobile,
       cat.id                    AS category,
       cat.name                  AS categoryname,
       subcat.id                 AS subcategory,
       subcat.name               AS subcategoryname,
       lic.assessmentno          AS assessmentno,
       lic.id_parent_bndry       AS ward,
       revward.name              AS wardname,
       lic.id_adm_bndry          AS locality,
       bnd.name                  AS localityname,
       lic.address               AS TradeAddress,
       lic.commencementdate      AS commencementdate,
       lic.id_status             AS status,
       licstatus.status_name     AS statusname,
       COALESCE(0, 0)            AS arrearlicensefee,
       COALESCE(0, 0)            AS arrearPenaltyFee,
       CASE
       WHEN drm.reasonmaster = 'License Fee'
         THEN curdd.amount
       ELSE 0
       END                       AS curLicenseFee,
       CASE
       WHEN drm.reasonmaster = 'Penalty'
         THEN curdd.amount
       ELSE 0
       END                       AS curPenaltyFee
     FROM egtl_license lic
       JOIN egtl_licensee licadd
         ON licadd.id_license = lic.id
       JOIN egtl_mstr_sub_category subcat
         ON subcat.id = lic.id_sub_category
       JOIN egtl_mstr_category cat
         ON cat.id = subcat.id_category
       JOIN egtl_mstr_status licstatus
         ON lic.id_status = licstatus.id
       JOIN eg_demand curdmd
         ON curdmd.id = lic.id_demand
       JOIN eg_boundary bnd
         ON lic.id_adm_bndry = bnd.id
       JOIN eg_boundary revward
         ON lic.id_parent_bndry = revward.id
       LEFT JOIN eg_demand_details curdd
         ON curdd.id_demand = curdmd.id
       LEFT JOIN eg_demand_reason dr
         ON dr.id = curdd.id_demand_reason
       LEFT JOIN eg_demand_reason_master drm
         ON drm.id = dr.id_demand_reason_master
       LEFT JOIN eg_installment_master im
         ON im.id = dr.id_installment
       LEFT JOIN eg_module m
         ON m.id = im.id_module
     WHERE drm.isdemand = TRUE
           AND extract(YEAR FROM im.installment_year) = extract(YEAR FROM CURRENT_DATE)
           AND m.name :: TEXT = 'Trade License' :: TEXT
           AND im.installment_type :: TEXT = 'Yearly' :: TEXT
     UNION
     SELECT
       lic.license_number        AS licensenumber,
       lic.name_of_estab         AS tradetitle,
       lic.id                    AS licenseid,
       licadd.applicant_name     AS username,
       licadd.mobile_phonenumber AS mobile,
       cat.id                    AS category,
       cat.name                  AS categoryname,
       subcat.id                 AS subcategory,
       subcat.name               AS subcategoryname,
       lic.assessmentno          AS assessmentno,
       lic.id_parent_bndry       AS ward,
       revward.name              AS wardname,
       lic.id_adm_bndry          AS locality,
       bnd.name                  AS localityname,
       lic.address               AS TradeAddress,
       lic.commencementdate      AS commencementdate,
       lic.id_status             AS status,
       licstatus.status_name     AS statusname,
       CASE
       WHEN drm.reasonmaster = 'License Fee'
         THEN arrdd.amount
       ELSE 0
       END                       AS arrearLicenseFee,
       CASE
       WHEN drm.reasonmaster = 'Penalty'
         THEN arrdd.amount
       ELSE 0
       END                       AS arrearPenaltyFee,
       COALESCE(0, 0)            AS currlicensefee,
       COALESCE(0, 0)            AS currPenaltyFee
     FROM egtl_license lic
       JOIN egtl_licensee licadd
         ON licadd.id_license = lic.id
       JOIN egtl_mstr_sub_category subcat
         ON subcat.id = lic.id_sub_category
       JOIN egtl_mstr_category cat
         ON cat.id = subcat.id_category
       JOIN egtl_mstr_status licstatus
         ON lic.id_status = licstatus.id
       JOIN eg_demand arrdmd
         ON arrdmd.id = lic.id_demand
       JOIN eg_boundary bnd
         ON lic.id_adm_bndry = bnd.id
       JOIN eg_boundary revward
         ON lic.id_parent_bndry = revward.id
       LEFT JOIN eg_demand_details arrdd
         ON arrdd.id_demand = arrdmd.id
       LEFT JOIN eg_demand_reason dr
         ON dr.id = arrdd.id_demand_reason
       LEFT JOIN eg_demand_reason_master drm
         ON drm.id = dr.id_demand_reason_master
       LEFT JOIN eg_installment_master im
         ON im.id = dr.id_installment
       LEFT JOIN eg_module m
         ON m.id = im.id_module
     WHERE drm.isdemand = TRUE
           AND extract(YEAR FROM im.installment_year) < extract(YEAR FROM CURRENT_DATE)
           AND m.name :: TEXT = 'Trade License' :: TEXT
           AND im.installment_type :: TEXT = 'Yearly' :: TEXT
    ) AS mv
  GROUP BY mv.licensenumber,
    mv.tradetitle,
    mv.licenseid,
    mv.owner,
    mv.mobile,
    mv.category,
    mv.categoryname,
    mv.subcategory,
    mv.subcategoryname,
    mv.assessmentno,
    mv.ward,
    mv.wardname,
    mv.locality,
    mv.localityname,
    mv.TradeAddress,
    mv.commencementdate,
    mv.status,
    mv.statusname;

---Roleaction Mapping

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
VALUES (nextval('seq_eg_action'), 'baseregistersearch', '/baseregister/search-form', NULL, (SELECT id
                                                                                            FROM eg_module
                                                                                            WHERE name =
                                                                                                  'Trade License Reports'),
                                  5, 'Base Register Report', TRUE, 'tl', 0, 1, '2015-09-16 13:55:12.148791', 1,
        '2015-09-16 13:55:12.148791', (SELECT id
                                       FROM eg_module
                                       WHERE name = 'Trade License' AND parentmodule IS NULL));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'Super User'), (SELECT id
                                                                                      FROM eg_action
                                                                                      WHERE
                                                                                        name = 'baseregistersearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TLCreator'), (SELECT id
                                                                                     FROM eg_action
                                                                                     WHERE
                                                                                       name = 'baseregistersearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TLApprover'), (SELECT id
                                                                                      FROM eg_action
                                                                                      WHERE
                                                                                        name = 'baseregistersearch'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TL VIEW ACCESS'), (SELECT id
                                                                                          FROM eg_action
                                                                                          WHERE name =
                                                                                                'baseregistersearch'));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
VALUES (nextval('seq_eg_action'), 'baseregistersearchresult', '/baseregister/search-resultList', NULL, (SELECT id
                                                                                                        FROM eg_module
                                                                                                        WHERE name =
                                                                                                              'Trade License Reports'),
                                  5, 'baseregistersearchresult', FALSE, 'tl', 0, 1, '2015-09-16 13:55:12.148791', 1,
        '2015-09-16 13:55:12.148791', (SELECT id
                                       FROM eg_module
                                       WHERE name = 'Trade License' AND parentmodule IS NULL));


INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'Super User'), (SELECT id
                                                                                      FROM eg_action
                                                                                      WHERE name =
                                                                                            'baseregistersearchresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TLCreator'), (SELECT id
                                                                                     FROM eg_action
                                                                                     WHERE name =
                                                                                           'baseregistersearchresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TLApprover'), (SELECT id
                                                                                      FROM eg_action
                                                                                      WHERE name =
                                                                                            'baseregistersearchresult'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id
                                                      FROM eg_role
                                                      WHERE name LIKE 'TL VIEW ACCESS'), (SELECT id
                                                                                          FROM eg_action
                                                                                          WHERE name =
                                                                                                'baseregistersearchresult'));

