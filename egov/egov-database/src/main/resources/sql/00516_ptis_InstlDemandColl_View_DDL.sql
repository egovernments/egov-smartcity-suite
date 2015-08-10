----------------------view : egpt_mv_inst_dem_coll ---------------------------------------------

CREATE VIEW egpt_mv_inst_dem_coll
  AS
  SELECT id_basic_property, id_installment, max(CreatedDate) CreatedDate, 
  COALESCE(max(GeneralTax), null, 0, max(GeneralTax)) GeneralTax, 
  COALESCE(max(LibCessTax), null, 0, max(LibCessTax)) LibCessTax, 
  COALESCE(max(EduCessTax), null, 0, max(EduCessTax)) EduCessTax, 
  COALESCE(max(UnauthPenaltyTax) , null, 0, max(UnauthPenaltyTax)) UnauthPenaltyTax, 
  COALESCE(max(PenaltyFinesTax), null, 0, max(PenaltyFinesTax)) PenaltyFinesTax,  
  COALESCE(max(SewTax), null, 0, max(SewTax)) SewTax, 
  COALESCE(max(VacantLandTax), null, 0, max(VacantLandTax)) VacantLandTax, 
  COALESCE(max(PubSerChrgTax), null, 0, max(PubSerChrgTax)) PubSerChrgTax,
  COALESCE(max(GeneralTaxColl), null, 0, max(GeneralTaxColl)) GeneralTaxColl, 
  COALESCE(max(LibCessTaxColl), null, 0, max(LibCessTaxColl)) LibCessTaxColl, 
  COALESCE(max(EduCessTaxColl), null, 0, max(EduCessTaxColl)) EduCessTaxColl, 
  COALESCE(max(UnauthPenaltyTaxColl) , null, 0, max(UnauthPenaltyTaxColl)) UnauthPenaltyTaxColl, 
  COALESCE(max(PenaltyFinesTaxColl), null, 0, max(PenaltyFinesTaxColl)) PenaltyFinesTaxColl,  
  COALESCE(max(SewTaxColl), null, 0, max(SewTaxColl)) SewTaxColl, 
  COALESCE(max(VacantLandTaxColl), null, 0, max(VacantLandTaxColl)) VacantLandTaxColl, 
  COALESCE(max(PubSerChrgTaxColl), null, 0, max(PubSerChrgTaxColl)) PubSerChrgTaxColl
  FROM (
  SELECT currDem.id_basic_property id_basic_property,
    dr.id_installment id_installment,
   det.create_date CreatedDate,
    (case drm.code when 'GEN_TAX' then det.amount else null end) GeneralTax,
    (case drm.code when 'LIB_CESS' then det.amount else null end) LibCessTax,
    (case drm.code when 'EDU_CESS' then det.amount else null end) EduCessTax,
    (case drm.code when 'UNAUTH_PENALTY' then det.amount else null end) UnauthPenaltyTax,
    (case drm.code when 'PENALTY_FINES' then det.amount else null end) PenaltyFinesTax,
    (case drm.code when 'SEW_TAX' then det.amount else null end) SewTax,
    (case drm.code when 'VAC_LAND_TAX' then det.amount else null end) VacantLandTax,
    (case drm.code when 'PUB_SER_CHRG' then det.amount else null end) PubSerChrgTax,
    (case drm.code when 'GEN_TAX' then det.amt_collected else null end) GeneralTaxColl,
    (case drm.code when 'LIB_CESS' then det.amt_collected else null end) LibCessTaxColl,
    (case drm.code when 'EDU_CESS' then det.amt_collected else null end) EduCessTaxColl,
    (case drm.code when 'UNAUTH_PENALTY' then det.amt_collected else null end) UnauthPenaltyTaxColl,
    (case drm.code when 'PENALTY_FINES' then det.amt_collected else null end) PenaltyFinesTaxColl,
    (case drm.code when 'SEW_TAX' then det.amt_collected else null end) SewTaxColl,
    (case drm.code when 'VAC_LAND_TAX' then det.amt_collected else null end) VacantLandTaxColl,
    (case drm.code when 'PUB_SER_CHRG' then det.amt_collected else null end) PubSerChrgTaxColl
  FROM EGPT_MV_BP_CURR_DEMAND currDem,
    eg_demand_details det,
    eg_demand_reason dr,
    eg_demand_reason_master drm
  WHERE det.id_demand            = currDem.id_demand
  AND det.id_demand_reason       = dr.id
  AND dr.id_demand_reason_master = drm.id) as dmndColDtls
  GROUP BY id_basic_property,
    id_installment;


/*
drop view egpt_mv_inst_dem_coll;
*/
