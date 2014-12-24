/**
 * Object of this class is used for report data filling
 * 
 * @author nayeem
 */

package org.egov.ptis.nmc.model;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARREARS_DMD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CURRENT_DMD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTGOVT_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.waterRates;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infstr.utils.NumberToWord;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

public class PropertyBillInfo {

	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private BasicProperty basicProperty;
	private String billNo;
	private int isCentralGovtProp = 0;
	private String arrearsPeriod;
	private String currentPeriod;
	
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
	}

	public String getWardNo() {
		return getBasicProperty().getPropertyID().getWard().getBoundaryNum().toString();

	}

	public String getHouseNo() {
		return getBasicProperty().getAddress().getHouseNo();
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

	public BigDecimal getArrEdCess() {

		BigDecimal edCessRes = reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		BigDecimal edCessNonRes = reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);

		if (edCessRes.equals(BigDecimal.ZERO)) {
			return edCessNonRes.setScale(2);
		} else {
			return edCessRes.setScale(2);
		}
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
		return BigDecimal.ZERO.setScale(2);
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
		return BigDecimal.ZERO.setScale(2);
	}

	public BigDecimal getGrandTotal() {
		BigDecimal sumOfArrCurr = BigDecimal.ZERO;
		for (String typeOfDue : reasonwiseDues.keySet()) {
			for (String dmdReason : reasonwiseDues.get(typeOfDue).keySet()) {
				sumOfArrCurr = sumOfArrCurr.add(reasonwiseDues.get(typeOfDue).get(dmdReason));
			}
		}
		return sumOfArrCurr.setScale(2);
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
			DateFormat sf= new SimpleDateFormat("dd/MM/yyyy");
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
		DateFormat sf= new SimpleDateFormat("dd/MM/yyyy");
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
}
