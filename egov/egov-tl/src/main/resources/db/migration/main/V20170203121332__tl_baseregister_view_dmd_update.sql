CREATE OR REPLACE FUNCTION getfeematrixrate(
  apptype BIGINT,
  cat     BIGINT,
  subcat  BIGINT,
  uom     BIGINT,
  appdate TIMESTAMP,
  tradewt BIGINT,
  installmentdate TIMESTAMP)
  RETURNS BIGINT
AS
$BODY$
DECLARE
  v_feematrix_amt NUMERIC;
  countval        NUMERIC;
BEGIN
if(apptype=(select id from egtl_mstr_app_type where name='New')) then 
  countval:=
  (SELECT COUNT(ID)
   FROM EGTL_FEEMATRIX
   WHERE NATUREOFBUSINESS =
         (SELECT id
          FROM egtl_mstr_business_nature
          WHERE name = 'Permanent'
         )
         AND LICENSEAPPTYPE = apptype
         AND LICENSECATEGORY = cat
         AND SUBCATEGORY = SUBCAT
         AND UNITOFMEASUREMENT = UOM
         AND FEETYPE =
             (SELECT ID
              FROM EGTL_MSTR_FEE_TYPE
              WHERE NAME = 'License Fee'
             )
         AND FINANCIALYEAR =
             (SELECT ID
              FROM FINANCIALYEAR
              WHERE appdate BETWEEN STARTINGDATE AND ENDINGDATE
             )
  );
else 
  countval:=
  (SELECT COUNT(ID)
   FROM EGTL_FEEMATRIX
   WHERE NATUREOFBUSINESS =
         (SELECT id
          FROM egtl_mstr_business_nature
          WHERE name = 'Permanent'
         )
         AND LICENSEAPPTYPE = apptype
         AND LICENSECATEGORY = cat
         AND SUBCATEGORY = SUBCAT
         AND UNITOFMEASUREMENT = UOM
         AND FEETYPE =
             (SELECT ID
              FROM EGTL_MSTR_FEE_TYPE
              WHERE NAME = 'License Fee'
             )
         AND FINANCIALYEAR =
             (SELECT ID
              FROM FINANCIALYEAR
              WHERE installmentdate BETWEEN STARTINGDATE AND ENDINGDATE
             )
  );
end if;
IF(countval = 1)  THEN
	if(apptype=(select id from egtl_mstr_app_type where name='New')) then 
		SELECT FD.AMOUNT INTO V_FEEMATRIX_AMT FROM EGTL_FEEMATRIX_DETAIL FD WHERE FD.FEEMATRIX = (SELECT ID FROM EGTL_FEEMATRIX WHERE NATUREOFBUSINESS = (SELECT id FROM egtl_mstr_business_nature WHERE name = 'Permanent') AND LICENSEAPPTYPE = apptype AND LICENSECATEGORY = cat	 AND SUBCATEGORY = SUBCAT AND UNITOFMEASUREMENT = UOM AND FEETYPE =(SELECT ID  FROM EGTL_MSTR_FEE_TYPE WHERE NAME = 'License Fee') AND FINANCIALYEAR = (SELECT ID from FINANCIALYEAR WHERE appdate BETWEEN STARTINGDATE AND ENDINGDATE) AND tradewt BETWEEN uomfrom AND uomto);
	else
		SELECT FD.AMOUNT INTO V_FEEMATRIX_AMT FROM EGTL_FEEMATRIX_DETAIL FD WHERE FD.FEEMATRIX = (SELECT ID FROM EGTL_FEEMATRIX WHERE NATUREOFBUSINESS = (SELECT id FROM egtl_mstr_business_nature WHERE name = 'Permanent') AND LICENSEAPPTYPE = apptype AND LICENSECATEGORY = cat	 AND SUBCATEGORY = SUBCAT AND UNITOFMEASUREMENT = UOM AND FEETYPE =(SELECT ID  FROM EGTL_MSTR_FEE_TYPE WHERE NAME = 'License Fee') AND FINANCIALYEAR = (SELECT ID from FINANCIALYEAR WHERE installmentdate BETWEEN STARTINGDATE AND ENDINGDATE) AND tradewt BETWEEN uomfrom AND uomto);
	end if;
  
ELSE
    V_FEEMATRIX_AMT :=0;
END IF;
  RETURN v_feematrix_amt;
  EXCEPTION
  WHEN OTHERS
    THEN
      RAISE NOTICE 'getfeematrixamt : % %',
      SQLERRM,
      SQLSTATE;
END;
$BODY$ LANGUAGE plpgsql;

DROP VIEW egtl_mv_baseregister_view;

CREATE OR REPLACE VIEW egtl_mv_baseregister_view AS
SELECT
    mv.licensenumber,
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
    mv.TradeAddress,
    mv.commencementdate,
    mv.status,
    mv.statusname,
    mv.dmd_installmentyear,
    SUM(arrearlicensefee)                                         AS arrearlicensefee,
    SUM(arrearPenaltyFee)                                         AS arrearPenaltyFee,
    SUM(curLicenseFee)                                            AS curLicenseFee,
    SUM(curPenaltyFee)                                            AS curPenaltyFee,
    mv.unitofmeasure,
    mv.uom,
    mv.tradewt,
    mv.apptype,
    mv.appdate,
    getfeematrixrate((select id from egtl_mstr_app_type  where name ='New'), cat, subcat, uom, commencementdate, tradewt,(select installment_year from eg_installment_master where id=dmd_installmentyear)) AS rateval
  FROM
    (SELECT
       lic.license_number               AS licensenumber,
       lic.name_of_estab                AS tradetitle,
       lic.id                           AS licenseid,
       licadd.applicant_name            AS owner,
       licadd.mobile_phonenumber        AS mobile,
       cat.id                           AS cat,
       cat.name                         AS categoryname,
       subcat.id                        AS subcat,
       subcat.name                      AS subcategoryname,
       lic.assessmentno                 AS assessmentno,
       COALESCE(lic.id_parent_bndry, 0) AS ward,
       COALESCE(revward.name, 'NA')     AS wardname,
       lic.id_adm_bndry                 AS locality,
       bnd.name                         AS localityname,
       lic.address                      AS TradeAddress,
       lic.commencementdate             AS commencementdate,
       lic.id_status                    AS status,
       licstatus.status_name            AS statusname,
       curdmd.id_installment		AS dmd_installmentyear,
       COALESCE(0, 0)                   AS arrearlicensefee,
       COALESCE(0, 0)                   AS arrearPenaltyFee,
       CASE
       WHEN drm.reasonmaster = 'License Fee'
         THEN (curdd.amount - curdd.amt_collected)
       ELSE 0
       END                              AS curLicenseFee,
       CASE
       WHEN drm.reasonmaster = 'Penalty'
         THEN (curdd.amount - curdd.amt_collected)
       ELSE 0
       END                              AS curPenaltyFee,
       uom.name                         AS unitofmeasure,
       uom.id                           AS uom,
       lic.trade_area_weight            AS tradewt,
       lic.licenseapptype               AS apptype,
       lic.appl_date                    AS appdate
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
       LEFT JOIN eg_boundary revward
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
       LEFT JOIN egtl_subcategory_details SCD
         ON SUBCAT.ID = SCD.SUBCATEGORY_ID
       LEFT JOIN egtl_mstr_unitofmeasure uom
         ON SCD.uom_id = uom.ID
     WHERE drm.isdemand = TRUE
     and im.id=curdmd.id_installment
	AND m.name :: TEXT = 'Trade License' :: TEXT
           AND im.installment_type :: TEXT = 'Yearly' :: TEXT
           AND scd.feetype_id =
               (SELECT id
                FROM egtl_mstr_fee_type
                WHERE name = 'License Fee'
               )
     UNION
     SELECT
       lic.license_number               AS licensenumber,
       lic.name_of_estab                AS tradetitle,
       lic.id                           AS licenseid,
       licadd.applicant_name            AS username,
       licadd.mobile_phonenumber        AS mobile,
       cat.id                           AS category,
       cat.name                         AS categoryname,
       subcat.id                        AS subcategory,
       subcat.name                      AS subcategoryname,
       lic.assessmentno                 AS assessmentno,
       COALESCE(lic.id_parent_bndry, 0) AS ward,
       COALESCE(revward.name, 'NA')     AS wardname,
       lic.id_adm_bndry                 AS locality,
       bnd.name                         AS localityname,
       lic.address                      AS TradeAddress,
       lic.commencementdate             AS commencementdate,
       lic.id_status                    AS status,
       licstatus.status_name            AS statusname,
       arrdmd.id_installment		AS dmd_installmentyear,
       CASE
       WHEN drm.reasonmaster = 'License Fee'
         THEN (arrdd.amount - arrdd.amt_collected)
       ELSE 0
       END                              AS arrearLicenseFee,
       CASE
       WHEN drm.reasonmaster = 'Penalty'
         THEN (arrdd.amount - arrdd.amt_collected)
       ELSE 0
       END                              AS arrearPenaltyFee,
       COALESCE(0, 0)                   AS currlicensefee,
       COALESCE(0, 0)                   AS currPenaltyFee,
       uom.name                         AS unitofmeasure,
       uom.id                           AS uom,
       lic.trade_area_weight            AS tradewt,
       lic.licenseapptype               AS apptype,
       lic.appl_date                    AS appdate
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
       LEFT JOIN eg_boundary revward
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
       LEFT JOIN egtl_subcategory_details SCD
         ON SUBCAT.ID = SCD.SUBCATEGORY_ID
       LEFT JOIN egtl_mstr_unitofmeasure uom
         ON SCD.uom_id = uom.ID
     WHERE drm.isdemand = TRUE
	   and im.id <> arrdmd.id_installment
          AND m.name :: TEXT = 'Trade License' :: TEXT
           AND im.installment_type :: TEXT = 'Yearly' :: TEXT
           AND scd.feetype_id =
               (SELECT id
                FROM egtl_mstr_fee_type
                WHERE name = 'License Fee'
               )
    ) AS mv
  GROUP BY mv.licensenumber,
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
    mv.TradeAddress,
    mv.commencementdate,
    mv.status,
    mv.statusname,
    mv.dmd_installmentyear,
    mv.unitofmeasure,
    mv.tradewt,
    mv.uom,
    mv.appdate,
    mv.apptype;


