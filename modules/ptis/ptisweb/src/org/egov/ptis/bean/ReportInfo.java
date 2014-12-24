package org.egov.ptis.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author subhash
 *
 */
public class ReportInfo {

	private String currInstallment;
	private String zoneNo;
	private String wardNo;
	private String partNo;
	private List<AssesseeInfo> assesseeInfoList;
	private List<DemandCollInfo> demandCollInfoList;
	private List<UnitWiseInfo> unitWiseInfoList;
	private String dateString;
	private Map<String,BigDecimal> demandCollMap;
	private Integer totalNoProps;
	
	
	public String getCurrInstallment() {
		return currInstallment;
	}
	public void setCurrInstallment(String currInstallment) {
		this.currInstallment = currInstallment;
	}
	public String getZoneNo() {
		return zoneNo;
	}
	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}
	public String getWardNo() {
		return wardNo;
	}
	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public List<AssesseeInfo> getAssesseeInfoList() {
		return assesseeInfoList;
	}
	public void setAssesseeInfoList(List<AssesseeInfo> assesseeInfoList) {
		this.assesseeInfoList = assesseeInfoList;
	}
	public List<DemandCollInfo> getDemandCollInfoList() {
		return demandCollInfoList;
	}
	public void setDemandCollInfoList(List<DemandCollInfo> demandCollInfoList) {
		this.demandCollInfoList = demandCollInfoList;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public List<UnitWiseInfo> getUnitWiseInfoList() {
		return unitWiseInfoList;
	}
	public void setUnitWiseInfoList(List<UnitWiseInfo> unitWiseInfoList) {
		this.unitWiseInfoList = unitWiseInfoList;
	}
	public Map<String, BigDecimal> getDemandCollMap() {
		return demandCollMap;
	}
	public void setDemandCollMap(Map<String, BigDecimal> demandCollMap) {
		this.demandCollMap = demandCollMap;
	}
	public Integer getTotalNoProps() {
		return totalNoProps;
	}
	public void setTotalNoProps(Integer totalNoProps) {
		this.totalNoProps = totalNoProps;
	}
	
	
	
	public BigDecimal getArrSewerageTax() {
		return demandCollMap.get("arrSewerageTax");
	}
	public BigDecimal getArrWaterTax() {
		return demandCollMap.get("arrWaterTax");
	}
	public BigDecimal getArrGenTax() {
		return demandCollMap.get("arrGenTax");
	}
	public BigDecimal getArrFireTax() {
		return demandCollMap.get("arrFireTax");
	}
	public BigDecimal getArrLightTax() {
		return demandCollMap.get("arrLightTax");
	}
	public BigDecimal getArrEduTax() {
		return demandCollMap.get("arrEduTax");
	}
	public BigDecimal getArrEgsTax() {
		return demandCollMap.get("arrEgsTax");
	}
	public BigDecimal getArrBigBldgTax() {
		return demandCollMap.get("arrBigBldgTax");
	}
	public BigDecimal getArrSewerageTaxColl() {
		return demandCollMap.get("arrSewerageTaxColl");
	}
	public BigDecimal getArrWaterTaxColl() {
		return demandCollMap.get("arrWaterTaxColl");
	}
	public BigDecimal getArrGenTaxColl() {
		return demandCollMap.get("arrGenTaxColl");
	}
	public BigDecimal getArrFireTaxColl() {
		return demandCollMap.get("arrFireTaxColl");
	}
	public BigDecimal getArrLightTaxColl() {
		return demandCollMap.get("arrLightTaxColl");
	}
	public BigDecimal getArrEduTaxColl() {
		return demandCollMap.get("arrEduTaxColl");
	}
	public BigDecimal getArrEgsTaxColl() {
		return demandCollMap.get("arrEgsTaxColl");
	}
	public BigDecimal getArrBigBldgTaxColl() {
		return demandCollMap.get("arrBigBldgTaxColl");
	}
	public BigDecimal getCurrSewerageTax() {
		return demandCollMap.get("currSewerageTax");
	}
	public BigDecimal getCurrWaterTax() {
		return demandCollMap.get("currWaterTax");
	}
	public BigDecimal getCurrGenTax() {
		return demandCollMap.get("currGenTax");
	}
	public BigDecimal getCurrFireTax() {
		return demandCollMap.get("currFireTax");
	}
	public BigDecimal getCurrLightTax() {
		return demandCollMap.get("currLightTax");
	}
	public BigDecimal getCurrEduTax() {
		return demandCollMap.get("currEduTax");
	}
	public BigDecimal getCurrEgsTax() {
		return demandCollMap.get("currEgsTax");
	}
	public BigDecimal getCurrBigBldgTax() {
		return demandCollMap.get("currBigBldgTax");
	}
	public BigDecimal getCurrSewerageTaxColl() {
		return demandCollMap.get("currSewerageTaxColl");
	}
	public BigDecimal getCurrWaterTaxColl() {
		return demandCollMap.get("currWaterTaxColl");
	}
	public BigDecimal getCurrGenTaxColl() {
		return demandCollMap.get("currGenTaxColl");
	}
	public BigDecimal getCurrFireTaxColl() {
		return demandCollMap.get("currFireTaxColl");
	}
	public BigDecimal getCurrLightTaxColl() {
		return demandCollMap.get("currLightTaxColl");
	}
	public BigDecimal getCurrEduTaxColl() {
		return demandCollMap.get("currEduTaxColl");
	}
	public BigDecimal getCurrEgsTaxColl() {
		return demandCollMap.get("currEgsTaxColl");
	}
	public BigDecimal getCurrBigBldgTaxColl() {
		return demandCollMap.get("currBigBldgTaxColl");
	}
	public BigDecimal getArrChqBuncPenalty() {
		return demandCollMap.get("arrChqBuncPenalty");
	}
	public BigDecimal getArrPenaltyFine() {
		return demandCollMap.get("arrPenaltyFine");
	}
	public BigDecimal getArrChqBuncPnltyColl() {
		return demandCollMap.get("arrChqBuncPnltyColl");
	}
	public BigDecimal getArrPenaltyFineColl() {
		return demandCollMap.get("arrPenaltyFineColl");
	}
	public BigDecimal getCurrChqBuncPenalty() {
		return demandCollMap.get("currChqBuncPenalty");
	}
	public BigDecimal getCurrPenaltyFine() {
		return demandCollMap.get("currPenaltyFine");
	}
	public BigDecimal getCurrChqBncPntyColl() {
		return demandCollMap.get("currChqBncPntyColl");
	}
	public BigDecimal getCurrPnltyFineColl() {
		return demandCollMap.get("currPnltyFineColl");
	}
	public BigDecimal getArrDmdTotal() {
		return demandCollMap.get("arrDmdTotal");
	}
	public BigDecimal getCurrDmdTotal() {
		return demandCollMap.get("currDmdTotal");
	}
	public BigDecimal getArrCollTotal() {
		return demandCollMap.get("arrCollTotal");
	}
	public BigDecimal getCurrCollTotal() {
		return demandCollMap.get("currCollTotal");
	}
	public BigDecimal getArrDmdGrandTotal() {
		return demandCollMap.get("arrDmdGrandTotal");
	}
	public BigDecimal getCurrDmdGrandTotal() {
		return demandCollMap.get("currDmdGrandTotal");
	}
	public BigDecimal getArrCollGrandTotal() {
		return demandCollMap.get("arrCollGrandTotal");
	}
	public BigDecimal getCurrCollGrandTotal() {
		return demandCollMap.get("currCollGrandTotal");
	}
	
	
	public BigDecimal getArrAdvt() {
		return (BigDecimal.ZERO).setScale(2);
	}
	public BigDecimal getCurrAdvt() {
		return (BigDecimal.ZERO).setScale(2);
	}
	public BigDecimal getArrAdvtColl() {
		return (BigDecimal.ZERO).setScale(2);
	}
	public BigDecimal getCurrAdvtColl() {
		return (BigDecimal.ZERO).setScale(2);
	}
	public BigDecimal getArrMutation() {
		return (BigDecimal.ZERO).setScale(2);
	}
	public BigDecimal getCurrMutation() {
		return (BigDecimal.ZERO).setScale(2);
	}
	public BigDecimal getArrMutColl() {
		return (BigDecimal.ZERO).setScale(2);
	}
	public BigDecimal getCurrMutColl() {
		return (BigDecimal.ZERO).setScale(2);
	}
	
}
