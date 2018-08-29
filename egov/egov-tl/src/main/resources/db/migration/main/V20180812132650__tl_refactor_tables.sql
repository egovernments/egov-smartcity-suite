Update eg_action set displayname='Create License Subcategory', name='Create License Subcategory' where name='Create License SubCategory';
Update eg_action set displayname='Modify License Subcategory',name='Modify License Subcategory' where name='Modify License SubCategory';
Update eg_action set displayname='View License Subcategory',name='View License Subcategory' where name='View License SubCategory';

--Drop view before alter table
DROP VIEW egtl_mv_baseregister_view;

ALTER TABLE EGTL_MSTR_SUB_CATEGORY DROP COLUMN id_license_sub_type;
ALTER TABLE EGTL_MSTR_SUB_CATEGORY DROP COLUMN id_nature;
ALTER TABLE EGTL_MSTR_SUB_CATEGORY DROP COLUMN id_license_type;
DROP TABLE EGTL_MSTR_LICENSE_SUB_TYPE;
DROP SEQUENCE SEQ_EGTL_MSTR_LICENSE_SUB_TYPE;
DROP TABLE EGTL_MSTR_LICENSE_TYPE;
DROP SEQUENCE SEQ_EGTL_MSTR_LICENSE_TYPE;
ALTER TABLE EGTL_MSTR_BUSINESS_NATURE ALTER COLUMN "name" Type varchar(25);
ALTER TABLE EGTL_MSTR_CATEGORY ALTER COLUMN "name" TYPE varchar(150);
ALTER TABLE EGTL_MSTR_SUB_CATEGORY  ALTER COLUMN "name" TYPE varchar(150);
INSERT INTO egtl_configuration values (nextval('seq_egtl_configuration'), 'CHEQUE_BOUNCE_PENALTY', '1000','License fee cheque bounce penalty',(select id from eg_user where username='system'),now(),(select id from eg_user where username='system'),now(),0);
update egtl_document_type set applicationType=(select id from EGTL_MSTR_APP_TYPE where code='NEW') where applicationType='NEW';
update egtl_document_type set applicationType=(select id from EGTL_MSTR_APP_TYPE where code='RENEW') where applicationType='RENEW';
update egtl_document_type set applicationType=(select id from EGTL_MSTR_APP_TYPE where code='CLOSURE') where applicationType='CLOSURE';
ALTER TABLE egtl_document_type ALTER COLUMN applicationType TYPE bigint USING(trim(applicationType)::bigint);
ALTER TABLE egtl_document_type ALTER COLUMN applicationType set not null;
ALTER TABLE egtl_license DROP COLUMN temp_license_number;
ALTER TABLE EGTL_MSTR_APP_TYPE ALTER COLUMN "name" TYPE varchar(50);
DROP TABLE EGTL_TRADE_LICENSE;
DROP TABLE EGTL_TRADE_LICENSE_AUD;
CREATE TABLE egtl_licensee_aud
(
  id integer,
  rev integer,
  revtype numeric,
  applicant_name VARCHAR(256),
  father_spouse_name VARCHAR(256),
  id_license bigint,
  mobile_phonenumber VARCHAR(16),
  uniqueid VARCHAR(16),
  email_id VARCHAR(64),
  address VARCHAR(250)
);

ALTER TABLE egtl_license_aud
  DROP COLUMN license_type,
  DROP COLUMN lastmodifieddate,
  DROP COLUMN lastmodifiedby,
  DROP COLUMN state_id;

ALTER TABLE egtl_license_aud
  ADD COLUMN digisignedcertfilestoreid VARCHAR(40),
  ADD COLUMN ASSESSMENTNO VARCHAR(64),
  ADD COLUMN DATEOFEXPIRY DATE,
  ADD COLUMN ID_ADM_BNDRY bigint,
  ADD COLUMN ID_PARENT_BNDRY bigint,
  ADD COLUMN adminward bigint,
  ADD COLUMN NATUREOFBUSINESS bigint,
  ADD COLUMN ID_STATUS bigint,
  ADD COLUMN licenseAppType bigint,
  ADD COLUMN certificateFileId varchar(40),
  ADD COLUMN approvedBy bigint,
  ADD COLUMN applicationSource varchar(25);

--Recreate view after alter table
CREATE OR REPLACE VIEW egtl_mv_baseregister_view AS
  SELECT mv.licensenumber,
    mv.oldlicensenumber,
    mv.tradetitle,
    mv.licenseid,
    mv.owner,
    mv.mobile,
    mv.cat,
    mv.categoryname,
    mv.subcat,
    mv.subcategoryname,
    mv.assessmentno,
    mv.ward,
    mv.wardname,
    mv.locality,
    mv.localityname,
    mv.adminward,
    mv.adminwardname,
    mv.tradeaddress,
    mv.commencementdate,
    mv.status,
    mv.statusname,
    sum(mv.arrearlicensefee) AS arrearlicensefee,
    sum(mv.arrearpenaltyfee) AS arrearpenaltyfee,
    sum(mv.curlicensefee) AS curlicensefee,
    sum(mv.curpenaltyfee) AS curpenaltyfee,
    mv.unitofmeasure,
    mv.uom,
    mv.tradewt,
    mv.apptype,
    mv.appdate,
    getfeematrixrate(mv.apptype, mv.cat, mv.subcat, mv.tradewt, mv.licenseid) AS rateval
  FROM ( SELECT row_number() OVER () AS id,
                lic.license_number AS licensenumber,
                lic.old_license_number AS oldlicensenumber,
                lic.name_of_estab AS tradetitle,
                lic.id AS licenseid,
                licadd.applicant_name AS owner,
                licadd.mobile_phonenumber AS mobile,
                cat.id AS cat,
                cat.name AS categoryname,
                subcat.id AS subcat,
                subcat.name AS subcategoryname,
           lic.assessmentno,
                COALESCE(lic.id_parent_bndry, 0::bigint) AS ward,
                COALESCE(revward.name, 'NA'::character varying) AS wardname,
                lic.id_adm_bndry AS locality,
                bnd.name AS localityname,
           lic.adminward,
                adminward.name AS adminwardname,
                lic.address AS tradeaddress,
           lic.commencementdate,
                lic.id_status AS status,
                licstatus.status_name AS statusname,
                curdmd.id_installment AS dmd_installmentyear,
                COALESCE(0, 0) AS arrearlicensefee,
                COALESCE(0, 0) AS arrearpenaltyfee,
                CASE
                WHEN drm.reasonmaster::text = 'License Fee'::text THEN curdd.amount - curdd.amt_collected
                ELSE 0::double precision
                END AS curlicensefee,
                CASE
                WHEN drm.reasonmaster::text = 'Penalty'::text THEN curdd.amount - curdd.amt_collected
                ELSE 0::double precision
                END AS curpenaltyfee,
                uom.name AS unitofmeasure,
                uom.id AS uom,
                lic.trade_area_weight AS tradewt,
                lic.licenseapptype AS apptype,
                lic.appl_date AS appdate
         FROM egtl_license lic
           JOIN egtl_licensee licadd ON licadd.id_license = lic.id
           JOIN egtl_mstr_sub_category subcat ON subcat.id = lic.id_sub_category
           JOIN egtl_mstr_category cat ON cat.id = subcat.id_category
           JOIN egtl_mstr_status licstatus ON lic.id_status = licstatus.id
           JOIN eg_demand curdmd ON curdmd.id = lic.id_demand
           JOIN eg_boundary bnd ON lic.id_adm_bndry = bnd.id
           LEFT JOIN eg_boundary revward ON lic.id_parent_bndry = revward.id
           LEFT JOIN eg_demand_details curdd ON curdd.id_demand = curdmd.id
           LEFT JOIN eg_demand_reason dr ON dr.id = curdd.id_demand_reason
           LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
           LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
           LEFT JOIN eg_module m ON m.id = im.id_module
           LEFT JOIN egtl_subcategory_details scd ON subcat.id = scd.subcategory_id
           LEFT JOIN egtl_mstr_unitofmeasure uom ON scd.uom_id = uom.id
           LEFT JOIN eg_boundary adminward ON lic.adminward = adminward.id
         WHERE drm.isdemand = true AND im.start_date = to_date('01/04/'::text || date_part('YEAR'::text, now()), 'DD/MM/YYYY'::text) AND m.name::text = 'Trade License'::text AND im.installment_type::text = 'Yearly'::text AND scd.feetype_id = (( SELECT egtl_mstr_fee_type.id
                                                                                                                                                                                                                                                     FROM egtl_mstr_fee_type
                                                                                                                                                                                                                                                     WHERE egtl_mstr_fee_type.name::text = 'License Fee'::text))
         UNION
         SELECT row_number() OVER () AS id,
                lic.license_number AS licensenumber,
                lic.old_license_number AS oldlicensenumber,
                lic.name_of_estab AS tradetitle,
                lic.id AS licenseid,
                licadd.applicant_name AS username,
                licadd.mobile_phonenumber AS mobile,
                cat.id AS category,
                cat.name AS categoryname,
                subcat.id AS subcategory,
                subcat.name AS subcategoryname,
           lic.assessmentno,
                COALESCE(lic.id_parent_bndry, 0::bigint) AS ward,
                COALESCE(revward.name, 'NA'::character varying) AS wardname,
                lic.id_adm_bndry AS locality,
                bnd.name AS localityname,
           lic.adminward,
                adminward.name AS adminwardname,
                lic.address AS tradeaddress,
           lic.commencementdate,
                lic.id_status AS status,
                licstatus.status_name AS statusname,
                arrdmd.id_installment AS dmd_installmentyear,
                CASE
                WHEN drm.reasonmaster::text = 'License Fee'::text THEN arrdd.amount - arrdd.amt_collected
                ELSE 0::double precision
                END AS arrearlicensefee,
                CASE
                WHEN drm.reasonmaster::text = 'Penalty'::text THEN arrdd.amount - arrdd.amt_collected
                ELSE 0::double precision
                END AS arrearpenaltyfee,
                COALESCE(0, 0) AS currlicensefee,
                COALESCE(0, 0) AS currpenaltyfee,
                uom.name AS unitofmeasure,
                uom.id AS uom,
                lic.trade_area_weight AS tradewt,
                lic.licenseapptype AS apptype,
                lic.appl_date AS appdate
         FROM egtl_license lic
           JOIN egtl_licensee licadd ON licadd.id_license = lic.id
           JOIN egtl_mstr_sub_category subcat ON subcat.id = lic.id_sub_category
           JOIN egtl_mstr_category cat ON cat.id = subcat.id_category
           JOIN egtl_mstr_status licstatus ON lic.id_status = licstatus.id
           JOIN eg_demand arrdmd ON arrdmd.id = lic.id_demand
           JOIN eg_boundary bnd ON lic.id_adm_bndry = bnd.id
           LEFT JOIN eg_boundary revward ON lic.id_parent_bndry = revward.id
           LEFT JOIN eg_demand_details arrdd ON arrdd.id_demand = arrdmd.id
           LEFT JOIN eg_demand_reason dr ON dr.id = arrdd.id_demand_reason
           LEFT JOIN eg_demand_reason_master drm ON drm.id = dr.id_demand_reason_master
           LEFT JOIN eg_installment_master im ON im.id = dr.id_installment
           LEFT JOIN eg_module m ON m.id = im.id_module
           LEFT JOIN egtl_subcategory_details scd ON subcat.id = scd.subcategory_id
           LEFT JOIN egtl_mstr_unitofmeasure uom ON scd.uom_id = uom.id
           LEFT JOIN eg_boundary adminward ON lic.adminward = adminward.id
         WHERE drm.isdemand = true AND im.start_date <> to_date('01/04/'::text || date_part('YEAR'::text, now()), 'DD/MM/YYYY'::text) AND m.name::text = 'Trade License'::text AND im.installment_type::text = 'Yearly'::text AND scd.feetype_id = (( SELECT egtl_mstr_fee_type.id
                                                                                                                                                                                                                                                      FROM egtl_mstr_fee_type
                                                                                                                                                                                                                                                      WHERE egtl_mstr_fee_type.name::text = 'License Fee'::text))) mv
  GROUP BY mv.licensenumber, mv.oldlicensenumber, mv.tradetitle, mv.licenseid, mv.owner, mv.mobile, mv.cat, mv.categoryname, mv.subcat, mv.subcategoryname, mv.assessmentno, mv.ward, mv.wardname, mv.locality, mv.localityname, mv.adminward, mv.adminwardname, mv.tradeaddress, mv.commencementdate, mv.status, mv.statusname, mv.unitofmeasure, mv.tradewt, mv.uom, mv.appdate, mv.apptype;