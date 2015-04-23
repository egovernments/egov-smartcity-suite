package org.egov.ptis.scheduler;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.MiscellaneousTaxDetail;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.quartz.StatefulJob;

public class UpdateTaxJob extends AbstractQuartzJob implements StatefulJob {

	private static final long serialVersionUID = 1L;

	protected PersistenceService persistenceService;
	private PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
	private Logger LOGGER = Logger.getLogger(UpdateTaxJob.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void executeJob() {
		LOGGER.debug("Updating taxes...");

		Long currentTimeMillis = System.currentTimeMillis();

		Installment currentInstallment = PropertyTaxUtil.getCurrentInstallment();

		List indexNumbers = getPersistenceService()
				.getSession()
				.createSQLQuery(
						"SELECT DISTINCT(bp.propertyid) "
								+ "FROM egpt_basic_property bp, egpt_property prop, egpt_ptdemand ptd, "
								+ "eg_demand d, egpt_demandcalculations dc, egpt_property_detail pd, "
								+ "egpt_floordemandcalc fdc, egpt_floor_detail fd "
								+ "WHERE bp.id_basic_property = prop.id_basic_property "
								+ "AND bp.is_active = 'Y' "
								+ "AND prop.status = 'A' "
								+ "AND prop.id_property = ptd.id_property "
								+ "AND ptd.id_demand = d.id "
								+ "AND d.id_installment = "
								+ currentInstallment.getId()
								+ " "
								+ "AND d.id = dc.id_demand "
								+ "AND pd.id_property = prop.id_property "
								+ "AND fd.id_property_detail = pd.id_property_detail "
								+ "AND fd.id_floor_detail = fdc.id_floordet "
								+ "AND (fdc.tax1 + fdc.tax2 + fdc.tax3 + fdc.tax4 + fdc.tax5 + fdc.tax6 + fdc.tax7 + fdc.tax8 + fdc.tax9 + fdc.tax10) = 0 "
								+ "AND fdc.id_dmdcalc = dc.id").setMaxResults(50).list();

		LOGGER.info("Index number " + indexNumbers);

		BasicProperty basicProperty = null;

		Ptdemand ptDemand = null;
		Set floorDemands = null;

		for (Object indexNumber : indexNumbers) {

			basicProperty = (BasicProperty) getPersistenceService().find(
					"from BasicPropertyImpl where active = true and upicNo = ?", indexNumber.toString());
			if (basicProperty != null) {
				ptDemand = PropertyDAOFactory.getDAOFactory().getPtDemandDao()
						.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());

				floorDemands = ptDemand.getDmdCalculations().getFlrwiseDmdCalculations();

				TaxCalculationInfo taxCalInfo = propertyTaxUtil.getTaxCalInfo(ptDemand);

				if (taxCalInfo != null) {
					if (floorDemands.isEmpty()) {

						LOGGER.info("Floor demand calculations does not exists for " + indexNumber.toString());

						for (FloorIF floor : basicProperty.getProperty().getPropertyDetail().getFloorDetails()) {

							FloorwiseDemandCalculations flrDmdCalc = createFloorDmdCalc(ptDemand.getDmdCalculations(),
									floor, taxCalInfo);

							ptDemand.getDmdCalculations().addFlrwiseDmdCalculations(flrDmdCalc);
							getPersistenceService().setType(FloorwiseDemandCalculations.class);
							getPersistenceService().persist(flrDmdCalc);
						}
					} else {

						if (taxCalInfo.getUnitTaxCalculationInfos().get(0) instanceof List) {
							for (List<UnitTaxCalculationInfo> units : taxCalInfo.getUnitTaxCalculationInfos()) {
								for (UnitTaxCalculationInfo unit : units) {
									for (Object flrDmdCalc : floorDemands) {

										FloorIF floor = ((FloorwiseDemandCalculations) flrDmdCalc).getFloor();

										if (propertyTaxUtil.isMatch(unit, floor)) {
											setFloorDmdCalTax(unit, ((FloorwiseDemandCalculations) flrDmdCalc));
											getPersistenceService().setType(FloorwiseDemandCalculations.class);
											getPersistenceService().update(flrDmdCalc);
											break;
										}
									}
								}
							}
						} else {
							for (int i = 0; i < taxCalInfo.getUnitTaxCalculationInfos().size(); i++) {

								UnitTaxCalculationInfo unit = (UnitTaxCalculationInfo) taxCalInfo
										.getUnitTaxCalculationInfos().get(i);

								for (Object flrDmdCalc : floorDemands) {

									FloorIF floor = ((FloorwiseDemandCalculations) flrDmdCalc).getFloor();

									if (propertyTaxUtil.isMatch(unit, floor)) {
										setFloorDmdCalTax(unit, ((FloorwiseDemandCalculations) flrDmdCalc));
										getPersistenceService().setType(FloorwiseDemandCalculations.class);
										getPersistenceService().update(flrDmdCalc);
										break;
									}

								}
							}
						}
					}
				}
			} else {
				LOGGER.info("BasicProperty is null for index number=" + indexNumber);
			}
		}

		LOGGER.info("Index numbers updated in " + ((System.currentTimeMillis() - currentTimeMillis) / 1000) + " sec(s)");

	}


	private FloorwiseDemandCalculations createFloorDmdCalc(PTDemandCalculations ptDmdCal, FloorIF floor,
			TaxCalculationInfo taxCalcInfo) {
		FloorwiseDemandCalculations floorDmdCalc = new FloorwiseDemandCalculations();

		floorDmdCalc.setPTDemandCalculations(ptDmdCal);
		floorDmdCalc.setFloor(floor);

		if (taxCalcInfo.getUnitTaxCalculationInfos().get(0) instanceof List) {
			for (List<UnitTaxCalculationInfo> unitTaxs : taxCalcInfo.getUnitTaxCalculationInfos()) {
				for (UnitTaxCalculationInfo unitTax : unitTaxs) {
					if (propertyTaxUtil.isMatch(unitTax, floor)) {
						floorDmdCalc.setAlv(unitTax.getAnnualRentAfterDeduction());
						setFloorDmdCalTax(unitTax, floorDmdCalc);
					}
				}
			}
		} else {
			for (int i = 0; i < taxCalcInfo.getUnitTaxCalculationInfos().size(); i++) {

				UnitTaxCalculationInfo unit = (UnitTaxCalculationInfo) taxCalcInfo.getUnitTaxCalculationInfos().get(i);

				if (propertyTaxUtil.isMatch(unit, floor)) {
					setFloorDmdCalTax(unit, floorDmdCalc);
				}
			}
		}

		LOGGER.debug("floorDmdCalc: " + floorDmdCalc + "\nExiting from createFloorDmdCalc");
		return floorDmdCalc;
	}

	private void setFloorDmdCalTax(UnitTaxCalculationInfo unitTax, FloorwiseDemandCalculations floorDmdCalc) {
		floorDmdCalc.setAlv(unitTax.getAnnualRentAfterDeduction());
		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				if (NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax1(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax2(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax3(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax4(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax5(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax6(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax7(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax8(taxDetail.getCalculatedTaxValue());
				} else if (NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD.equals(miscTax.getTaxName())) {
					floorDmdCalc.setTax9(taxDetail.getCalculatedTaxValue());
				}
			}
		}
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}
