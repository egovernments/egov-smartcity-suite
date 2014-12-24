package org.egov.ptis.actions.notice;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_CALSHEET_FOR_GOVT_PROPS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.SqFt;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.utils.DateUtils;
import org.egov.ptis.bean.PropertyInfo;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.ConsolidatedUnitTaxCalReport;
import org.egov.ptis.nmc.model.GovtPropertyInfo;
import org.egov.ptis.nmc.model.GovtPropertyTaxCalInfo;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.BaseFormAction;

public class PropertyIndividualCalSheetAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private ReportService reportService;
	private Integer reportId = -1;
	private String indexNum;
	private PropertyTaxUtil propertyTaxUtil;
	private PropertyInfo propertyInfo = new PropertyInfo(new PropertyImpl());
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat yearformatter = new SimpleDateFormat("yyyy");
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateCalSheet() {
		try {
			LOGGER.debug("Entered into generateCalSheet method");
			LOGGER.debug("Index Num in generateCalSheet : " + indexNum);
			BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
			BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNum);
			Property property = basicProperty.getProperty();
			ReportRequest reportInput = null;
			if (property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(PROPTYPE_STATE_GOVT) 
					|| property.getPropertyDetail().getPropertyTypeMaster().getCode().equals(PROPTYPE_CENTRAL_GOVT)) {
				reportInput = getReportInputDataForGovtProps(property);
			} else {
				reportInput = getReportInputData(property);
			}
			reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportInput), getSession());
			LOGGER.debug("Exit from generateCalSheet method");
			return "calsheet";
		} catch (Exception e) {
			LOGGER.error("Exception in Generate Cal Sheet: ", e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
	}

	private ReportRequest getReportInputDataForGovtProps(Property property) {
		ReportRequest reportInput = null;
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = getTaxCalInfoMap(property.getPtDemandSet());
		GovtPropertyInfo govtPropInfo = prepareGovtPropInfo(taxCalInfoMap);
		reportInput = new ReportRequest(REPORT_TEMPLATENAME_CALSHEET_FOR_GOVT_PROPS, govtPropInfo, null);
		reportInput.setPrintDialogOnOpenReport(false);
		return reportInput;
	}

	private GovtPropertyInfo prepareGovtPropInfo(Map<Installment, TaxCalculationInfo> taxCalInfoMap) {
		GovtPropertyInfo govtPropertyInfo = new GovtPropertyInfo();
		TaxCalculationInfo taxCalInfo = null;
		Boolean isBldgCostChanged = Boolean.FALSE;
		for (Installment inst : taxCalInfoMap.keySet()) {
			GovtPropertyTaxCalInfo govtPropTaxCalInfo = null;
			if (taxCalInfo == null) {
				taxCalInfo = taxCalInfoMap.get(inst);
			} else {
				if (!taxCalInfo.getBuildingCost().equals(taxCalInfoMap.get(inst).getBuildingCost())) {
					isBldgCostChanged = Boolean.TRUE;
					govtPropTaxCalInfo = prepareGovtPropTaxCalTnfo(taxCalInfo);
					govtPropTaxCalInfo.setEffectiveDate(formatter.format(taxCalInfo.getOccupencyDate()));
					govtPropertyInfo.addGovtPropTaxCalInfo(govtPropTaxCalInfo);
					
					taxCalInfo = taxCalInfoMap.get(inst);
					
					govtPropertyInfo.setArea(taxCalInfo.getArea());
					govtPropertyInfo.setWard(taxCalInfo.getWard());
					govtPropertyInfo.setHouseNumber(taxCalInfo.getHouseNumber());
					govtPropertyInfo.setIndexNo(taxCalInfo.getIndexNo());
					govtPropertyInfo.setParcelId(taxCalInfo.getParcelId());
					govtPropertyInfo.setPropertyAddress(taxCalInfo.getPropertyAddress());
					govtPropertyInfo.setPropertyOwnerName(taxCalInfo.getPropertyOwnerName());
					govtPropertyInfo.setPropertyType(taxCalInfo.getPropertyType());
					
					govtPropTaxCalInfo = prepareGovtPropTaxCalTnfo(taxCalInfo);
					govtPropTaxCalInfo.setEffectiveDate(formatter.format(taxCalInfo.getOccupencyDate()));
					govtPropertyInfo.addGovtPropTaxCalInfo(govtPropTaxCalInfo);
				}
			}
		}
		if (!isBldgCostChanged) {
			govtPropertyInfo.setArea(taxCalInfo.getArea());
			govtPropertyInfo.setWard(taxCalInfo.getWard());
			govtPropertyInfo.setHouseNumber(taxCalInfo.getHouseNumber());
			govtPropertyInfo.setIndexNo(taxCalInfo.getIndexNo());
			govtPropertyInfo.setParcelId(taxCalInfo.getParcelId());
			govtPropertyInfo.setPropertyAddress(taxCalInfo.getPropertyAddress());
			govtPropertyInfo.setPropertyOwnerName(taxCalInfo.getPropertyOwnerName());
			govtPropertyInfo.setPropertyType(taxCalInfo.getPropertyType());
			GovtPropertyTaxCalInfo govtPropTaxCalInfo = prepareGovtPropTaxCalTnfo(taxCalInfo);
			govtPropTaxCalInfo.setEffectiveDate(formatter.format(taxCalInfo.getOccupencyDate()));
			govtPropertyInfo.addGovtPropTaxCalInfo(govtPropTaxCalInfo);
		}
		return govtPropertyInfo;
	}

	private GovtPropertyTaxCalInfo prepareGovtPropTaxCalTnfo(TaxCalculationInfo taxCalInfo) {
		
		GovtPropertyTaxCalInfo govtPropTaxCalInfo = new GovtPropertyTaxCalInfo();
		govtPropTaxCalInfo .setAmenities(taxCalInfo.getAmenities());
		govtPropTaxCalInfo.setAnnualLettingValue(taxCalInfo.getTotalAnnualLettingValue());
		govtPropTaxCalInfo.setBuildingCost(taxCalInfo.getBuildingCost());
		if (taxCalInfo.getPropertyArea() != null) {
			govtPropTaxCalInfo.setPropertyArea(taxCalInfo.getPropertyArea().toString());
		} else {
			govtPropTaxCalInfo.setPropertyArea("N/A");
		}
		
		govtPropTaxCalInfo.setAlvPercentage(taxCalInfo.getAlvPercentage());
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
	
	private ReportRequest getReportInputData(Property property) {
		LOGGER.debug("Entered into getReportInputData method");
		LOGGER.debug("Property : " + property);
		Set<Ptdemand> ptDmdSet = property.getPtDemandSet();
		List<ConsolidatedUnitTaxCalReport> consolidatedUnitTaxCalReportList = new ArrayList<ConsolidatedUnitTaxCalReport>();
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = propertyInfo.getTaxCalInfoMap(ptDmdSet);
		Map<Installment, TaxCalculationInfo> taxCalInfoList = getTaxCalInfoList(taxCalInfoMap);
		int i = 0;
		TaxCalculationInfo newTaxCalInfo = null;
		for (Map.Entry<Installment, TaxCalculationInfo> taxCalInfo : taxCalInfoList.entrySet()) {
			for (UnitTaxCalculationInfo consUnitTaxCalInfo :taxCalInfo.getValue().getConsolidatedUnitTaxCalculationInfo()) {
				ConsolidatedUnitTaxCalReport consolidatedUnitTaxCalReport = new ConsolidatedUnitTaxCalReport();
				List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
				for (UnitTaxCalculationInfo unitTaxCalInfo : taxCalInfo.getValue().getUnitTaxCalculationInfo()) {
					if (unitTaxCalInfo.getUnitNumber().equals(consUnitTaxCalInfo.getUnitNumber())) {
						BigDecimal unitAreaScale = unitTaxCalInfo.getUnitArea().setScale(2, BigDecimal.ROUND_HALF_UP);
						BigDecimal unitAreaInSqFtScale = (unitTaxCalInfo.getUnitArea().multiply(SqFt)).setScale(2,
								BigDecimal.ROUND_HALF_UP);
						unitTaxCalInfo.setUnitArea(unitAreaScale);
						unitTaxCalInfo.setUnitAreaInSqFt(unitAreaInSqFtScale);
						
						if (unitTaxCalInfo.getManualAlv() != null && !unitTaxCalInfo.getManualAlv().isEmpty()) {
							BigDecimal manualAlvScale = (new BigDecimal(unitTaxCalInfo.getManualAlv())).setScale(2,
									BigDecimal.ROUND_HALF_UP);
							unitTaxCalInfo.setManualAlv(manualAlvScale.toString());
						}
						units.add(unitTaxCalInfo);
						
						//The Assessment date has to be displayed if its the first installment
						if ((yearformatter.format(taxCalInfo.getKey().getFromDate())).equals(yearformatter
								.format(unitTaxCalInfo.getOccpancyDate()))) {
							
							consolidatedUnitTaxCalReport.setInstDate(DateUtils.getDefaultFormattedDate(unitTaxCalInfo
									.getOccpancyDate()));
						} else {
							consolidatedUnitTaxCalReport.setInstDate(DateUtils.getDefaultFormattedDate(taxCalInfo
									.getKey().getFromDate()));
						}
								
					}
				}
				consolidatedUnitTaxCalReport.setAnnualLettingValue(consUnitTaxCalInfo.getAnnualRentAfterDeduction()
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				consolidatedUnitTaxCalReport.setMonthlyRent(consUnitTaxCalInfo.getMonthlyRent());
				consolidatedUnitTaxCalReport.setAnnualRentBeforeDeduction(consUnitTaxCalInfo.getAnnualRentBeforeDeduction());
				
				if (consUnitTaxCalInfo.getAnnualRentBeforeDeduction() != null) {
					BigDecimal dedAmt = consUnitTaxCalInfo.getAnnualRentBeforeDeduction().divide(new BigDecimal(10));
					consolidatedUnitTaxCalReport.setDeductionAmount(dedAmt.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
				
				consolidatedUnitTaxCalReport.setUnitTaxCalInfo(units);
				consolidatedUnitTaxCalReportList.add(consolidatedUnitTaxCalReport);
				if (i == 0) {
					newTaxCalInfo = taxCalInfo.getValue();
					i++;
				}
			}
		}
		
		newTaxCalInfo.setConsolidatedUnitTaxCalReportList(consolidatedUnitTaxCalReportList);
		ReportRequest reportInput = null;
		reportInput = new ReportRequest(NMCPTISConstants.REPORT_TEMPLATENAME_DEMAND_CALSHEET, newTaxCalInfo, null);
		reportInput.setPrintDialogOnOpenReport(false);
		LOGGER.debug("Exit from getReportInputData method");
		return reportInput;
	}

	private Map<Installment, TaxCalculationInfo> getTaxCalInfoList(Map<Installment, TaxCalculationInfo> taxCalInfoMap) {
		Map<Installment, TaxCalculationInfo> taxCalInfoList = new HashMap<Installment, TaxCalculationInfo>();
		TaxCalculationInfo firstInstTxCalInfo=null;
		TaxCalculationInfo prevTaxCalInfo = null;
		int i = 0;
		
		for (Map.Entry<Installment, TaxCalculationInfo> txCalInfo : taxCalInfoMap.entrySet()) {
			
			//first installment
			if (i==0) {
				firstInstTxCalInfo = txCalInfo.getValue();
				//set Installment date
				for (UnitTaxCalculationInfo unitinfo : firstInstTxCalInfo.getConsolidatedUnitTaxCalculationInfo()) {
					unitinfo.setInstDate(DateUtils.getDefaultFormattedDate(unitinfo.getOccpancyDate()));
				}
				taxCalInfoList.put(txCalInfo.getKey(), firstInstTxCalInfo);
				prevTaxCalInfo = firstInstTxCalInfo;
			}
			i++;
			if (i == 1) continue;
			
			int size = 0;
			for (UnitTaxCalculationInfo unitInfo1 : prevTaxCalInfo.getConsolidatedUnitTaxCalculationInfo()) {
				TaxCalculationInfo taxcal=null;
				List<UnitTaxCalculationInfo> removeListConUnitInfo = new ArrayList<UnitTaxCalculationInfo>();
				
				//Compare alv of each UnitTaxCalculationInfo and remove(adding to separete list) it from the list if the alv has not changed
				for (UnitTaxCalculationInfo unitInfo2 : txCalInfo.getValue().getConsolidatedUnitTaxCalculationInfo()){
					taxcal = txCalInfo.getValue();
					if (unitInfo1.getUnitNumber().equals(unitInfo2.getUnitNumber())) {

						if (unitInfo1.getAnnualRentAfterDeduction()
								.compareTo(unitInfo2.getAnnualRentAfterDeduction()) == 0) {
							
							removeListConUnitInfo.add(unitInfo2);
						} 
					}
				}
				
				if (removeListConUnitInfo.size() > 0) {
					List<UnitTaxCalculationInfo> removeListUnitInfo = new ArrayList<UnitTaxCalculationInfo>();
					//Remove from the ConsolidatedUnitTaxCalculationInfo list
					taxcal.getConsolidatedUnitTaxCalculationInfo().removeAll(removeListConUnitInfo);
					
					//Remove the corresponding unittaxinfo from the UnitTaxCalculationInfo list
					for (UnitTaxCalculationInfo ui1 : removeListConUnitInfo) {
						for (UnitTaxCalculationInfo ui2 : taxcal.getUnitTaxCalculationInfo()) {
							if (ui1.getUnitNumber().equals(ui2.getUnitNumber())) {
								removeListUnitInfo.add(ui2);
							}
						}
					}
					taxcal.getUnitTaxCalculationInfo().removeAll(removeListUnitInfo);
				}
				size++;
				if ((prevTaxCalInfo.getConsolidatedUnitTaxCalculationInfo().size()) == size) {
					if (taxcal != null && taxcal.getConsolidatedUnitTaxCalculationInfo().size() != 0) {
						prevTaxCalInfo = taxcal;
						taxCalInfoList.put(txCalInfo.getKey(), taxcal);
						txCalInfo.getKey();
					}
				}
			}
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

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

}
