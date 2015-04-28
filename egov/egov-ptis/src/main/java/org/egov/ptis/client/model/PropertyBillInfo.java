/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
/**
 * Object of this class is used for report data filling
 *
 * @author nayeem
 */

package org.egov.ptis.client.model;

import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_STREET_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_WATER_BENEFIT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTGOVT_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_MIXED_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPENPLOT_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.waterRates;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infstr.utils.MoneyUtils;
import org.egov.infstr.utils.NumberToWord;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.joda.time.DateTime;

public class PropertyBillInfo {

	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private BasicProperty basicProperty;
	private String billNo;
	private int isCentralGovtProp = 0;
	private String arrearsPeriod;
	private String currentPeriod;
	private TaxCalculationInfo taxCalcInfo;

	private BigDecimal firstHalfCurrentSewerageTax;
	private BigDecimal firstHalfCurrentWaterTax;
	private BigDecimal firstHalfCurrentGeneralTax;
	private BigDecimal firstHalfCurrentLightTax;
	private BigDecimal firstHalfCurrentFireServiceTax;
	private BigDecimal firstHalfCurrentSewerageBenefitTax;
	private BigDecimal firstHalfCurrentWaterBenefitTax;
	private BigDecimal firstHalfCurrentRoadTax;
	private BigDecimal firstHalfCurrentMunicipalEduCess;
	private BigDecimal firstHalfCurrentEgCess;
	private BigDecimal firstHalfCurrentEduCess;
	private BigDecimal firstHalfCurrentBigBuildingCess;

	private BigDecimal secondHalfCurrentSewerageTax;
	private BigDecimal secondHalfCurrentWaterTax;
	private BigDecimal secondHalfCurrentGeneralTax;
	private BigDecimal secondHalfCurrentLightTax;
	private BigDecimal secondHalfCurrentFireServiceTax;
	private BigDecimal secondHalfCurrentSewerageBenefitTax;
	private BigDecimal secondHalfCurrentWaterBenefitTax;
	private BigDecimal secondHalfCurrentRoadTax;
	private BigDecimal secondHalfCurrentMunicipalEduCess;
	private BigDecimal secondHalfCurrentEgCess;
	private BigDecimal secondHalfCurrentEduCess;
	private BigDecimal secondHalfCurrentBigBuildingCess;

	private String firstSixMonthsPeriod;
	private String secondSixMonthsPeriod;
	
	private Installment currentInstallment;

	public PropertyBillInfo() {
	}

	public PropertyBillInfo(Map<String, Map<String, BigDecimal>> reasonwiseDues, BasicProperty basicProperty,
			String billNo) {
		this.reasonwiseDues = reasonwiseDues;
		this.basicProperty = basicProperty;
		this.billNo = billNo;
		String propType = getBasicProperty().getProperty().getPropertyDetail().getPropertyTypeMaster().getType();
		if (propType != null && PROPTYPE_CENTGOVT_STR.equals(propType)) {
			isCentralGovtProp = 1;
		}

		currentInstallment = PropertyTaxUtil.getCurrentInstallment();

		for (Ptdemand ptDemand : basicProperty.getProperty().getPtDemandSet()) {
			if (ptDemand.getEgInstallmentMaster().equals(currentInstallment)) {
				this.taxCalcInfo = new PropertyTaxUtil().getTaxCalInfo(ptDemand);
				break;
			}
		}
		setCurrentHalfYearTaxes();
	}

	private void setCurrentHalfYearTaxes() {
		BigDecimal[] halfYearTaxes = new BigDecimal[2];
		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_SEWERAGE_TAX));
		firstHalfCurrentSewerageTax = halfYearTaxes[0];
		secondHalfCurrentSewerageTax = halfYearTaxes[1];

		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_FIRE_SERVICE_TAX));
		firstHalfCurrentFireServiceTax = halfYearTaxes[0];
		secondHalfCurrentFireServiceTax = halfYearTaxes[1];

		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_GENERAL_TAX));
		firstHalfCurrentGeneralTax = halfYearTaxes[0];
		secondHalfCurrentGeneralTax = halfYearTaxes[1];

		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_GENERAL_WATER_TAX));
		firstHalfCurrentWaterTax = halfYearTaxes[0];
		secondHalfCurrentWaterTax = halfYearTaxes[1];

		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_LIGHTINGTAX));
		firstHalfCurrentLightTax = halfYearTaxes[0];
		secondHalfCurrentLightTax = halfYearTaxes[1];
		
		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX));
		firstHalfCurrentSewerageBenefitTax = halfYearTaxes[0];
		secondHalfCurrentSewerageBenefitTax = halfYearTaxes[1];
		
		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_WATER_BENEFIT_TAX));
		firstHalfCurrentWaterBenefitTax = halfYearTaxes[0];
		secondHalfCurrentWaterBenefitTax = halfYearTaxes[1];
		
		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_STREET_TAX));
		firstHalfCurrentRoadTax = halfYearTaxes[0];
		secondHalfCurrentRoadTax = halfYearTaxes[1];
		
		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS));
		firstHalfCurrentMunicipalEduCess = halfYearTaxes[0];
		secondHalfCurrentMunicipalEduCess = halfYearTaxes[1];

		BigDecimal edCessRes = reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		BigDecimal edCessNonRes = reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
		halfYearTaxes = getHalfYearTaxes(edCessRes.add(edCessNonRes));
		firstHalfCurrentEduCess = halfYearTaxes[0];
		secondHalfCurrentEduCess = halfYearTaxes[1];

		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX));
		firstHalfCurrentEgCess = halfYearTaxes[0];
		secondHalfCurrentEgCess = halfYearTaxes[1];

		halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX));
		firstHalfCurrentBigBuildingCess = halfYearTaxes[0];
		secondHalfCurrentBigBuildingCess = halfYearTaxes[1];
	}

	private BigDecimal[] getHalfYearTaxes(BigDecimal taxForYear) {
		BigDecimal[] returnValue = new BigDecimal[2];
		BigDecimal rnddTaxForYear = taxForYear.setScale(2, BigDecimal.ROUND_HALF_UP);
		long[] weights = new long[] { 50, 50 };
		if (isEven(rnddTaxForYear, 2)) {
			returnValue[0] = rnddTaxForYear.divide(new BigDecimal(2)).setScale(2);
			returnValue[1] = rnddTaxForYear.divide(new BigDecimal(2)).setScale(2);
		} else {
			returnValue = MoneyUtils.allocate(rnddTaxForYear, weights);
		}
		return returnValue;
	}

	public String getWardNo() {
		return getBasicProperty().getPropertyID().getWard().getBoundaryNum().toString();

	}

	public String getHouseNo() {
		return getBasicProperty().getAddress().getHouseNoBldgApt();
	}

	public String getIndexNo() {
		return getBasicProperty().getUpicNo();
	}

	public String getName() {
		PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
		return (ptisCacheMgr.buildOwnerFullName(basicProperty.getProperty().getPropertyOwnerSet()));
	}

	public BigDecimal getArrSewerageTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_SEWERAGE_TAX).setScale(2);
	}

	public BigDecimal getArrFireserviceTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_FIRE_SERVICE_TAX).setScale(2);
	}

	public BigDecimal getArrGeneralTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_GENERAL_TAX).setScale(2);
	}

	public BigDecimal getArrWaterTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_GENERAL_WATER_TAX).setScale(2);
	}
	
	public BigDecimal getArrLightTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_LIGHTINGTAX).setScale(2);
	}
	
	public BigDecimal getArrSewerageBenefitTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX).setScale(2);
	}
	
	public BigDecimal getArrWaterBenefitTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_WATER_BENEFIT_TAX).setScale(2);
	}
	
	public BigDecimal getArrRoadTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_STREET_TAX).setScale(2);
	}
	
	public BigDecimal getArrMunicipalEduCessTax() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS).setScale(2);
	}

	public BigDecimal getArrEdCess() {
		BigDecimal edCessRes = reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		BigDecimal edCessNonRes = reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);

		return edCessRes.add(edCessNonRes).setScale(2);
	}

	public BigDecimal getArrEgCess() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX).setScale(2);
	}

	public BigDecimal getArrBigRes() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX).setScale(2);
	}

	public BigDecimal getArrBuildCess() {
		return BigDecimal.ZERO.setScale(2);
	}

	public BigDecimal getArrMisc() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY).setScale(2);
	}

	public BigDecimal getCurrSewerageTax() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_SEWERAGE_TAX).setScale(2);
	}

	public BigDecimal getCurrFireserviceTax() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_FIRE_SERVICE_TAX).setScale(2);
	}

	public BigDecimal getCurrGeneralTax() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_GENERAL_TAX).setScale(2);
	}

	public BigDecimal getCurrWaterTax() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_GENERAL_WATER_TAX).setScale(2);
	}

	public BigDecimal getCurrLightTax() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_LIGHTINGTAX).setScale(2);

	}

	public BigDecimal getCurrEdCess() {

		BigDecimal edCessRes = reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		BigDecimal edCessNonRes = reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);

		if (edCessRes.equals(BigDecimal.ZERO)) {
			return edCessNonRes.setScale(2);
		} else {
			return edCessRes.setScale(2);
		}
	}

	public BigDecimal getCurrEgCess() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX).setScale(2);
	}

	public BigDecimal getCurrBigRes() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX).setScale(2);
	}

	public BigDecimal getCurrBuildCess() {
		return BigDecimal.ZERO.setScale(2);
	}

	public BigDecimal getCurrMisc() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY).setScale(2);
	}

	public BigDecimal getGrandTotal() {
		BigDecimal sumOfArrCurr = BigDecimal.ZERO;
		for (String typeOfDue : reasonwiseDues.keySet()) {
			for (String dmdReason : reasonwiseDues.get(typeOfDue).keySet()) {
				sumOfArrCurr = sumOfArrCurr.add(reasonwiseDues.get(typeOfDue).get(dmdReason));
			}
		}
		return sumOfArrCurr.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public String getTotalAmntInWords() {
		return NumberToWord.amountInWords(getGrandTotal().doubleValue());
	}

	public Map<String, Map<String, BigDecimal>> getReasonwiseDues() {
		return reasonwiseDues;
	}

	public void setReasonwiseDues(Map<String, Map<String, BigDecimal>> reasonwiseDues) {
		this.reasonwiseDues = reasonwiseDues;
	}

	public String getBillDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(new Date());
	}

	public String getBillDueDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date billDate;
		try {
			billDate = dateFormat.parse(getBillDate());
		} catch (ParseException e) {
			throw new EGOVRuntimeException("Error in Bill due date", e);
		}
		Calendar billDueDate = Calendar.getInstance();
		billDueDate.setTime(billDate);
		billDueDate.add(Calendar.DATE, 15);
		return dateFormat.format(billDueDate.getTime()).toString();
	}

	public String getWaterMeterStatus() {
		String waterRateCode = basicProperty.getProperty().getPropertyDetail().getExtra_field1();
		return waterRates.get(waterRateCode) == null ? "N/A" : waterRates.get(waterRateCode);
	}

	public String getLastPayDate() {
		EgdmCollectedReceipt lastCollectedReceipt = getLastCollectedReceipt();

		if (lastCollectedReceipt != null) {
			DateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			return sf.format(lastCollectedReceipt.getReceiptDate());
		} else {
			return null;
		}
	}

	public BigDecimal getAmount() {
		EgdmCollectedReceipt lastCollectedReceipt = getLastCollectedReceipt();
		if (lastCollectedReceipt != null) {
			return lastCollectedReceipt.getAmount();
		} else {
			return null;
		}
	}

	public String getAssessmentDate() {
		DateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		return sf.format(basicProperty.getProperty().getCreatedDate());
	}

	public EgdmCollectedReceipt getLastCollectedReceipt() {
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		EgDemand egDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
		Date lastPayDate = null;
		Date newDate = null;
		EgdmCollectedReceipt lastColReceipt = null;
		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {
			for (EgdmCollectedReceipt colRec : dmdDet.getEgdmCollectedReceipts()) {
				if (lastPayDate != null) {
					newDate = colRec.getReceiptDate();
					if (newDate.compareTo(lastPayDate) == 1) {
						lastColReceipt = colRec;
						lastPayDate = newDate;
					}
				} else {
					lastColReceipt = colRec;
					lastPayDate = colRec.getReceiptDate();
				}
			}
		}
		return lastColReceipt;
	}

	/**
	 * returns even no or not, logic here is multiply by 10 until you've reached
	 * the desired precision (number of digits after the .), then use bitwise
	 * comparison
	 * 
	 * @param decimal
	 * @param precision
	 * @return
	 */
	private Boolean isEven(BigDecimal decimal, int precision) {
		BigDecimal tempValue = decimal;
		int i = 0;
		while (++i <= precision)
			tempValue = tempValue.multiply(new BigDecimal(10));
		return (tempValue.intValue() & 1) == 0;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getArea() {
		return getBasicProperty().getPropertyID().getArea().getName();
	}

	public String getPropertyType() {
		return getBasicProperty().getProperty().getPropertyDetail().getPropertyTypeMaster().getType();
	}

	public int getIsCentralGovtProp() {
		return isCentralGovtProp;
	}

	public void setIsCentralGovtProp(int isCentralGovtProp) {
		this.isCentralGovtProp = isCentralGovtProp;
	}

	public String getArrearsPeriod() {
		return arrearsPeriod;
	}

	public void setArrearsPeriod(String arrearsPeriod) {
		this.arrearsPeriod = arrearsPeriod;
	}

	public String getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(String currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public BigDecimal getFirstHalfCurrentSewerageTax() {
		return firstHalfCurrentSewerageTax;
	}

	public BigDecimal getFirstHalfCurrentWaterTax() {
		return firstHalfCurrentWaterTax;
	}

	public BigDecimal getFirstHalfCurrentGeneralTax() {
		return firstHalfCurrentGeneralTax;
	}

	public BigDecimal getFirstHalfCurrentLightTax() {
		return firstHalfCurrentLightTax;
	}

	public BigDecimal getFirstHalfCurrentFireServiceTax() {
		return firstHalfCurrentFireServiceTax;
	}
	
	public BigDecimal getFirstHalfCurrentSewerageBenefitTax() {
		return firstHalfCurrentSewerageBenefitTax;
	}
	
	public BigDecimal getFirstHalfCurrentWaterBenefitTax() {
		return firstHalfCurrentWaterBenefitTax;
	}
	
	public BigDecimal getFirstHalfCurrentRoadTax() {
		return firstHalfCurrentRoadTax;
	}
	
	public BigDecimal getFirstHalfCurrentMunicipalEduCess() {
		return firstHalfCurrentMunicipalEduCess;
	}

	public BigDecimal getFirstHalfCurrentEgCess() {
		return firstHalfCurrentEgCess;
	}

	public BigDecimal getFirstHalfCurrentEduCess() {
		return firstHalfCurrentEduCess;
	}

	public BigDecimal getFirstHalfCurrentBigBuildingCess() {
		return firstHalfCurrentBigBuildingCess;
	}

	public BigDecimal getFirstHalfCurrentMisc() {
		return getCurrMisc().divide(new BigDecimal(2)).setScale(2);
	}

	public BigDecimal getSecondHalfCurrentSewerageTax() {
		return secondHalfCurrentSewerageTax;
	}

	public BigDecimal getSecondHalfCurrentWaterTax() {
		return secondHalfCurrentWaterTax;
	}

	public BigDecimal getSecondHalfCurrentGeneralTax() {
		return secondHalfCurrentGeneralTax;
	}

	public BigDecimal getSecondHalfCurrentLightTax() {
		return secondHalfCurrentLightTax;
	}

	public BigDecimal getSecondHalfCurrentFireServiceTax() {
		return secondHalfCurrentFireServiceTax;
	}
	
	public BigDecimal getSecondHalfCurrentSewerageBenefitTax() {
		return secondHalfCurrentSewerageBenefitTax;
	}
	
	public BigDecimal getSecondHalfCurrentWaterBenefitTax() {
		return secondHalfCurrentWaterBenefitTax;
	}
	
	public BigDecimal getSecondHalfCurrentRoadTax() {
		return secondHalfCurrentRoadTax;
	}
	
	public BigDecimal getSecondHalfCurrentMunicipalEduCess() {
		return secondHalfCurrentMunicipalEduCess;
	}

	public BigDecimal getSecondHalfCurrentEgCess() {
		return secondHalfCurrentEgCess;
	}

	public BigDecimal getSecondHalfCurrentEduCess() {
		return secondHalfCurrentEduCess;
	}

	public BigDecimal getSecondHalfCurrentBigBuildingCess() {
		return secondHalfCurrentBigBuildingCess;
	}

	public BigDecimal getSecondHalfCurrentMisc() {
		return getCurrMisc().divide(new BigDecimal(2)).setScale(2);
	}

	public BigDecimal getResidentialALV() {
		BigDecimal resALVTotal = BigDecimal.ZERO;

		if (this.taxCalcInfo != null) {

			String propType = this.taxCalcInfo.getPropertyType();

			if (propType.equals(PROPTYPE_RESD_STR)) {
				return this.taxCalcInfo.getTotalAnnualLettingValue();
			} else if (propType.equals(PROPTYPE_MIXED_STR)) {
				resALVTotal = sumALVOnUnitUsage(USAGES_FOR_RESD);
			}
		}

		return resALVTotal.setScale(2);
	}

	public BigDecimal getNonResidentialALV() {
		BigDecimal nonResALVTotal = BigDecimal.ZERO;

		if (this.taxCalcInfo != null) {

			String propType = this.taxCalcInfo.getPropertyType();

			if (propType.equals(PropertyTaxConstants.PROPTYPE_NONRESD_STR)) {
				return this.taxCalcInfo.getTotalAnnualLettingValue();
			} else if (propType.equals(PROPTYPE_MIXED_STR)) {
				nonResALVTotal = sumALVOnUnitUsage(USAGES_FOR_NON_RESD);
			}
		}

		return nonResALVTotal.setScale(2);
	}

	/**
	 * Sums the Unit Annual Letting Values based on their usage
	 * 
	 * @param usages
	 * @return ALV
	 */
	private BigDecimal sumALVOnUnitUsage(String usages) {
		BigDecimal totalALV = BigDecimal.ZERO;

		if (this.taxCalcInfo.getUnitTaxCalculationInfos().get(0) instanceof List) {
			for (List<UnitTaxCalculationInfo> unitTaxCalcs : this.taxCalcInfo.getUnitTaxCalculationInfos()) {
				for (UnitTaxCalculationInfo unitTax : unitTaxCalcs) {
					if (usages.contains(unitTax.getUnitUsage())) {
						totalALV = totalALV.add(unitTax.getAnnualRentAfterDeduction());
					}
				}
			}
		} else {
			for (int i = 0; i < this.taxCalcInfo.getUnitTaxCalculationInfos().size(); i++) {
				UnitTaxCalculationInfo unitTax = (UnitTaxCalculationInfo) this.taxCalcInfo.getUnitTaxCalculationInfos()
						.get(i);
				if (usages.contains(unitTax.getUnitUsage())) {
					totalALV = totalALV.add(unitTax.getAnnualRentAfterDeduction());
				}
			}
		}

		return totalALV;
	}

	public BigDecimal getOpenPlotALV() {
		BigDecimal openPlotTotalALV = BigDecimal.ZERO;

		if (this.taxCalcInfo != null) {

			String propType = this.taxCalcInfo.getPropertyType();

			if (propType.equals(PROPTYPE_MIXED_STR)) {
				openPlotTotalALV = sumALVOnUnitUsage(USAGES_FOR_OPENPLOT);
			} else if (propType.equals(PROPTYPE_OPENPLOT_STR)) {
				openPlotTotalALV = this.taxCalcInfo.getTotalAnnualLettingValue();
			}
		}

		return openPlotTotalALV.setScale(2);
	}

	public String getMailingAddress() {
		return new PTISCacheManager().buildAddressByImplemetation(this.basicProperty.getAddress());
	}

	public BigDecimal getArrearsPenalty() {
		return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_PENALTY_FINES);
	}

	public BigDecimal getCurrentPenalty() {
		return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_PENALTY_FINES);
	}

	public BigDecimal getTotalPenalty() {
		return getArrearsPenalty().add(getCurrentPenalty()).setScale(2);
	}

	public String getFirstSixMonthsPeriod() {
		return firstSixMonthsPeriod;
	}

	public void setFirstSixMonthsPeriod(String firstSixMonthsPeriod) {
		this.firstSixMonthsPeriod = firstSixMonthsPeriod;
	}

	public String getSecondSixMonthsPeriod() {
		return secondSixMonthsPeriod;
	}

	public void setSecondSixMonthsPeriod(String secondSixMonthsPeriod) {
		this.secondSixMonthsPeriod = secondSixMonthsPeriod;
	}
	
	public String getFinancialYear() {
		DateTime fromDate = new DateTime(currentInstallment.getFromDate());
		DateTime toDate = new DateTime(currentInstallment.getToDate());
		return fromDate.getYear() + "-" + toDate.getYear();
	}
}
