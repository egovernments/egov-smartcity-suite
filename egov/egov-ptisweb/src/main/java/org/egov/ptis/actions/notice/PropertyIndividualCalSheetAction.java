/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.ptis.actions.notice;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.bean.PropertyCalSheetInfo;
import org.egov.ptis.client.model.AreaTaxCalculationInfo;
import org.egov.ptis.client.model.ConsolidatedUnitTaxCalReport;
import org.egov.ptis.client.model.GovtPropertyInfo;
import org.egov.ptis.client.model.GovtPropertyTaxCalInfo;
import org.egov.ptis.client.model.calculator.APUnitTaxCalculationInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.UnitAreaCalculationDetail;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.egov.infra.utils.MoneyUtils.roundOffTwo;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNotNull;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNotZero;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNull;
import static org.egov.ptis.constants.PropertyTaxConstants.CENTRALGOVT_BUILDING_ALV_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.NOT_AVAILABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_CALSHEET_FOR_GOVT_PROPS;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_DEMAND_CALSHEET;
import static org.egov.ptis.constants.PropertyTaxConstants.STATEGOVT_BUILDING_ALV_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.SqFt;

public class PropertyIndividualCalSheetAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private final BigDecimal TOTAL_MONTHS = new BigDecimal("12");
	private ReportService reportService;
	private String reportId;
	private String indexNum;
	private PropertyTaxUtil propertyTaxUtil;
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat yearformatter = new SimpleDateFormat("yyyy");
	private BasicProperty basicProperty;
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ReportViewerUtil reportViewerUtil;

	@Override
	public Object getModel() {
		return null;
	}

	public String generateCalSheet() {
		try {
			LOGGER.debug("Entered into generateCalSheet method");
			LOGGER.debug("Index Num in generateCalSheet : " + indexNum);
			basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNum);
			Property property = basicProperty.getProperty();
			ReportRequest reportInput = null;
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(OWNERSHIP_TYPE_STATE_GOVT)
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode()
							.equals(OWNERSHIP_TYPE_CENTRAL_GOVT_50)) {
				reportInput = getReportInputDataForGovtProps(property);
			} else {
				reportInput = getReportInputData(property);
			}
			reportId = reportViewerUtil.addReportToTempCache(reportService.createReport(reportInput));
			LOGGER.debug("Exit from generateCalSheet method");
			return "calsheet";
		} catch (Exception e) {
			LOGGER.error("Exception in Generate Cal Sheet: ", e);
			throw new ApplicationRuntimeException("Exception : " + e);
		}
	}

	private ReportRequest getReportInputDataForGovtProps(Property property) {
		ReportRequest reportInput = null;
		List<UnitCalculationDetail> unitCalculationDetails = getUniqueALVUnitCalcDetails(property, true);

		GovtPropertyInfo govtPropInfo = prepareGovtPropInfo(unitCalculationDetails,
				PropertyCalSheetInfo.createCalSheetInfo(property));

		reportInput = new ReportRequest(REPORT_TEMPLATENAME_CALSHEET_FOR_GOVT_PROPS, govtPropInfo, null);
		reportInput.setPrintDialogOnOpenReport(false);
		return reportInput;
	}

	private GovtPropertyInfo prepareGovtPropInfo(List<UnitCalculationDetail> unitCalcDetails,
			PropertyCalSheetInfo calSheetInfo) {
		GovtPropertyInfo govtPropertyInfo = new GovtPropertyInfo();

		for (UnitCalculationDetail unitCalcDetail : unitCalcDetails) {
			GovtPropertyTaxCalInfo govtPropTaxCalInfo = null;
			govtPropertyInfo.setArea(calSheetInfo.getArea());
			govtPropertyInfo.setWard(calSheetInfo.getWard());
			govtPropertyInfo.setHouseNumber(calSheetInfo.getHouseNumber());
			govtPropertyInfo.setIndexNo(calSheetInfo.getIndexNo());
			govtPropertyInfo.setParcelId(calSheetInfo.getParcelId());
			govtPropertyInfo.setPropertyAddress(calSheetInfo.getPropertyAddress());
			govtPropertyInfo.setPropertyOwnerName(calSheetInfo.getPropertyOwnerName());
			govtPropertyInfo.setPropertyType(calSheetInfo.getPropertyType());

			govtPropTaxCalInfo = prepareGovtPropTaxCalTnfo(unitCalcDetail, calSheetInfo);
			govtPropTaxCalInfo.setEffectiveDate(formatter.format(unitCalcDetail.getOccupancyDate()));
			govtPropertyInfo.addGovtPropTaxCalInfo(govtPropTaxCalInfo);
		}

		return govtPropertyInfo;
	}

	private GovtPropertyTaxCalInfo prepareGovtPropTaxCalTnfo(UnitCalculationDetail unitCalcDetail,
			PropertyCalSheetInfo calSheetInfo) {

		GovtPropertyTaxCalInfo govtPropTaxCalInfo = new GovtPropertyTaxCalInfo();
		//govtPropTaxCalInfo.setAmenities(calSheetInfo.getAmenities());
		govtPropTaxCalInfo.setAnnualLettingValue(unitCalcDetail.getAlv());
		govtPropTaxCalInfo.setBuildingCost(unitCalcDetail.getBuildingCost());

		if (isNotNull(unitCalcDetail.getUnitArea()) && isNotZero(unitCalcDetail.getUnitArea())) {
			govtPropTaxCalInfo.setPropertyArea(unitCalcDetail.getUnitArea().toString());
		} else {
			govtPropTaxCalInfo.setPropertyArea(NOT_AVAILABLE);
		}

		if (calSheetInfo.getPropertyType().equalsIgnoreCase(OWNERSHIP_TYPE_STATE_GOVT)) {
			govtPropTaxCalInfo.setAlvPercentage(STATEGOVT_BUILDING_ALV_PERCENTAGE);
			govtPropTaxCalInfo.setAmenities(NOT_AVAILABLE);
		} else {
			govtPropTaxCalInfo.setAlvPercentage(CENTRALGOVT_BUILDING_ALV_PERCENTAGE);
		}

		return govtPropTaxCalInfo;
	}

	private Map<Installment, TaxCalculationInfo> getTaxCalInfoMap(Set<Ptdemand> ptDmdSet) {
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Installment, TaxCalculationInfo>();
		for (Ptdemand ptdmd : ptDmdSet) {
			TaxCalculationInfo taxCalcInfo = propertyTaxUtil.getTaxCalInfo(ptdmd);
			if (taxCalcInfo != null) {
				taxCalInfoMap.put(ptdmd.getEgInstallmentMaster(), taxCalcInfo);
			}
		}
		return taxCalInfoMap;
	}

	private List<UnitCalculationDetail> getUniqueALVUnitCalcDetails(Property property, boolean isCompareAlv) {
		LOGGER.debug("Entered into getUniqueALVUnitCalcDetails, property=" + property);

		String query = "from UnitCalculationDetail ucd join fetch ucd.unitAreaCalculationDetails "
				+ "where ucd.property = ? " + "order by ucd.unitNumber, ucd.installmentFromDate, ucd.fromDate";

		@SuppressWarnings("unchecked")
		List<UnitCalculationDetail> unitCalculationDetails = entityManager.unwrap(Session.class).createQuery(query)
				.setEntity(0, property).list();

		List<UnitCalculationDetail> uniqueALVUnitCalcDetails = new ArrayList<UnitCalculationDetail>();
		UnitCalculationDetail prevUnitCalcDetail = null;

		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {

			if (uniqueALVUnitCalcDetails.isEmpty()) {
				uniqueALVUnitCalcDetails.add(new UnitCalculationDetail(unitCalcDetail));
			} else {
				if (isNotNull(prevUnitCalcDetail)) {
					if (isCompareAlv) {
						if (prevUnitCalcDetail.getAlv().compareTo(unitCalcDetail.getAlv()) != 0) {
							uniqueALVUnitCalcDetails.add(new UnitCalculationDetail(unitCalcDetail));
						}
					} else {
						if (prevUnitCalcDetail.getBuildingCost().compareTo(unitCalcDetail.getBuildingCost()) != 0) {
							uniqueALVUnitCalcDetails.add(new UnitCalculationDetail(unitCalcDetail));
						}
					}
				}
			}

			prevUnitCalcDetail = unitCalcDetail;

		}

		LOGGER.debug("Entered into getUniqueALVUnitCalcDetails, uniqueALVUnitCalcDetails=" + uniqueALVUnitCalcDetails);
		return uniqueALVUnitCalcDetails;
	}

	private ReportRequest getReportInputData(Property property) {
		LOGGER.debug("Entered into getReportInputData method");
		LOGGER.debug("Property : " + property);
		Set<Ptdemand> ptDmdSet = property.getPtDemandSet();
		List<ConsolidatedUnitTaxCalReport> consolidatedUnitTaxCalReportList = new ArrayList<ConsolidatedUnitTaxCalReport>();
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = null; // propertyTaxUtil.getTaxCalInfoMap(ptDmdSet);
		// Map<Installment, TaxCalculationInfo> taxCalInfoList =
		// getTaxCalInfoList(taxCalInfoMap);
		List<UnitCalculationDetail> unitCalculationDetails = getUniqueALVUnitCalcDetails(property, true);
		UnitTaxCalculationInfo unitTaxCalcInfo = null;

		Map<Integer, Set<UnitAreaCalculationDetail>> unitCalcDetails = new TreeMap<Integer, Set<UnitAreaCalculationDetail>>();

		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {
			ConsolidatedUnitTaxCalReport consolidatedUnitTaxCalReport = new ConsolidatedUnitTaxCalReport();

			consolidatedUnitTaxCalReport.setAnnualLettingValue(roundOffTwo(unitCalcDetail.getAlv()));
			consolidatedUnitTaxCalReport
					.setMonthlyRent(unitCalcDetail.getMonthlyRent().compareTo(BigDecimal.ZERO) == 0 ? null
							: unitCalcDetail.getMonthlyRent());

			if (isNotNull(unitCalcDetail.getAlv()) && PropertyTaxUtil.isNotZero(unitCalcDetail.getMonthlyRent())) {
				consolidatedUnitTaxCalReport.setAnnualRentBeforeDeduction(unitCalcDetail.getMonthlyRent().multiply(
						TOTAL_MONTHS));
				BigDecimal dedAmt = consolidatedUnitTaxCalReport.getAnnualRentBeforeDeduction().divide(
						new BigDecimal(10));
				consolidatedUnitTaxCalReport.setDeductionAmount(roundOffTwo(dedAmt));
			}

			// The Assessment date has to be displayed if
			// its the first installment
			if (yearformatter.format(unitCalcDetail.getInstallmentFromDate()).equals(
					yearformatter.format(unitCalcDetail.getOccupancyDate()))) {

				consolidatedUnitTaxCalReport.setInstDate(DateUtils.getDefaultFormattedDate(unitCalcDetail
						.getOccupancyDate()));
			} else {
				consolidatedUnitTaxCalReport.setInstDate(DateUtils.getDefaultFormattedDate(unitCalcDetail
						.getInstallmentFromDate()));
			}

			consolidatedUnitTaxCalReport.setUnitTaxCalInfo(prepareUnitCalculationDetails(unitCalcDetail));

			consolidatedUnitTaxCalReportList.add(consolidatedUnitTaxCalReport);

		}

		PropertyCalSheetInfo propertyCalSheetinfo = PropertyCalSheetInfo.createCalSheetInfo(property);
		propertyCalSheetinfo.setConsolidatedUnitTaxCalReportList(consolidatedUnitTaxCalReportList);

		ReportRequest reportInput = new ReportRequest(REPORT_TEMPLATENAME_DEMAND_CALSHEET, propertyCalSheetinfo, null);
		reportInput.setPrintDialogOnOpenReport(false);
		LOGGER.debug("Exit from getReportInputData method");
		return reportInput;
	}

	private List<UnitTaxCalculationInfo> prepareUnitCalculationDetails(UnitCalculationDetail unitCalcDetail) {
		List<UnitTaxCalculationInfo> unitTaxes = new ArrayList<UnitTaxCalculationInfo>();
		Map<String, Set<UnitAreaCalculationDetail>> unitAreaDetails = new TreeMap<String, Set<UnitAreaCalculationDetail>>();

		String unitId = null;

		for (UnitAreaCalculationDetail unitAreaCalcDetail : unitCalcDetail.getUnitAreaCalculationDetails()) {
			unitId = unitAreaCalcDetail.getUnitIdentifier();

			if (isNull(unitAreaDetails.get(unitId))) {

				Set<UnitAreaCalculationDetail> unitAreas = new TreeSet<UnitAreaCalculationDetail>(
						new UnitAreaCalculationDetailComparator());
				unitAreas.add(unitAreaCalcDetail);

				unitAreaDetails.put(unitId, unitAreas);

			} else {
				unitAreaDetails.get(unitId).add(unitAreaCalcDetail);
			}

		}

		UnitTaxCalculationInfo unitTaxInfo = null;
		AreaTaxCalculationInfo areaTaxInfo = null;
		BigDecimal totalUnitArea = BigDecimal.ZERO;
		BigDecimal totalMonthlyRent = BigDecimal.ZERO;
		String floorNumberString = null;
		String manualALV = null;
		int i = 0;

		for (Map.Entry<String, Set<UnitAreaCalculationDetail>> entry : unitAreaDetails.entrySet()) {

			unitTaxInfo = new APUnitTaxCalculationInfo();
			unitTaxInfo.setFloorNumber(unitCalcDetail.getUnitNumber().toString());

			i = 0;
			totalUnitArea = BigDecimal.ZERO;
			totalMonthlyRent = BigDecimal.ZERO;

			for (UnitAreaCalculationDetail unitArea : entry.getValue()) {

				areaTaxInfo = new AreaTaxCalculationInfo();
				floorNumberString = FLOOR_MAP.get(unitArea.getFloorNumber());

				if (i == 0) {
					unitTaxInfo.setFloorNumber(isNull(floorNumberString) ? unitArea.getFloorNumber()
							: floorNumberString);
					unitTaxInfo.setFloorNumber(isNull(floorNumberString) ? null : floorNumberString);
					unitTaxInfo.setUnitOccupation(unitArea.getUnitOccupation());

					manualALV = unitArea.getManualALV().compareTo(BigDecimal.ZERO) == 0 ? null : roundOffTwo(
							unitArea.getManualALV()).toString();

					unitTaxInfo.setBaseRatePerSqMtPerMonth(roundOffTwo(unitArea.getBaseRentPerSqMtr()));
				}

				totalUnitArea = totalUnitArea.add(unitArea.getTaxableArea());
				totalMonthlyRent = totalMonthlyRent.add(unitArea.getMonthlyRentalValue());

				areaTaxInfo.setTaxableArea(roundOffTwo(unitArea.getTaxableArea()));
				areaTaxInfo.setMonthlyBaseRent(roundOffTwo(unitArea.getMonthlyBaseRent()));
				areaTaxInfo.setCalculatedTax(unitArea.getMonthlyRentalValue());

				i++;
			}

			unitTaxInfo.setFloorArea(roundOffTwo(totalUnitArea));
			unitTaxInfo.setUnitAreaInSqFt(roundOffTwo(totalUnitArea.multiply(SqFt)));
			unitTaxes.add(unitTaxInfo);
		}

		return unitTaxes;
	}

	private class UnitAreaCalculationDetailComparator implements Comparator<UnitAreaCalculationDetail> {

		@Override
		public int compare(UnitAreaCalculationDetail o1, UnitAreaCalculationDetail o2) {

			int result = o1.getTaxableArea().compareTo(o2.getTaxableArea());

			if (result == 0) {
				result = o1.getMonthlyBaseRent().compareTo(o2.getMonthlyBaseRent());
				// reverse logic to reverse the order greater amount to smaller
				// amount
				result = result == -1 ? 1 : result == 1 ? -1 : 0;
			} else {
				result = result == 1 ? -1 : 1;
			}

			return result;
		}

	}

	private Map<Installment, TaxCalculationInfo> getTaxCalInfoList(Map<Installment, TaxCalculationInfo> taxCalInfoMap) {
		Map<Installment, TaxCalculationInfo> taxCalInfoList = new TreeMap<Installment, TaxCalculationInfo>();
		TaxCalculationInfo firstInstTxCalInfo = null;
		TaxCalculationInfo prevTaxCalInfo = null;
		Boolean isPropertyModified = PropertyTaxUtil.isPropertyModified(basicProperty.getProperty());
		int i = 0;

		for (Map.Entry<Installment, TaxCalculationInfo> txCalInfo : taxCalInfoMap.entrySet()) {

			// first installment
			if (i == 0) {
				firstInstTxCalInfo = txCalInfo.getValue();
				Boolean isMultipleBRsEffective = false;
				// set Installment date

				if (!isMultipleBRsEffective) {
					taxCalInfoList.put(txCalInfo.getKey(), firstInstTxCalInfo);
					prevTaxCalInfo = firstInstTxCalInfo;
				} else {
					continue;
				}
			}
			i++;
			if (i == 1)
				continue;

		}
		return taxCalInfoList;
	}

	public String getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(String indexNum) {
		this.indexNum = indexNum;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getReportId() {
		return reportId;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

}
