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
/**
 * Object of this class is used for report data filling
 *
 * @author nayeem
 */

package org.egov.ptis.client.model;

import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.MoneyUtils;
import org.egov.infra.utils.NumberToWordConverter;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.waterRates;

public class PropertyBillInfo {

    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    private Map<String, Map<String, BigDecimal>> reasonwiseDues;
    private BasicProperty basicProperty;
    private String billNo;
    private int isCentralGovtProp = 0;
    private String arrearsPeriod;
    private String currentPeriod;
    private TaxCalculationInfo taxCalcInfo;

    private BigDecimal currentGeneralTax;
    private BigDecimal currentEduCess;
    private BigDecimal currentLibCess;
    private BigDecimal currentUnauthPenalty;

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
        if (propType != null && OWNERSHIP_TYPE_CENTRAL_GOVT_50.equals(propType)) {
            isCentralGovtProp = 1;
        }

        currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();

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

        halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_GENERAL_TAX));
        currentGeneralTax = halfYearTaxes[0];

        halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS));
        currentEduCess = halfYearTaxes[0];

        halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_LIBRARY_CESS));
        currentLibCess = halfYearTaxes[0];

        halfYearTaxes = getHalfYearTaxes(reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY));
        currentUnauthPenalty = halfYearTaxes[0];
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
        return basicProperty.getFullOwnerName();
    }

    public BigDecimal getArrGeneralTax() {
        return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_GENERAL_TAX).setScale(2);
    }

    public BigDecimal getArrEduCess() {
        return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS).setScale(2);
    }

    public BigDecimal getArrLibCess() {
        return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_LIBRARY_CESS).setScale(2);
    }

    public BigDecimal getArrCurrentUnauthPenalty() {
        return reasonwiseDues.get(ARREARS_DMD).get(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY).setScale(2);
    }

    public BigDecimal getCurrGeneralTax() {
        return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_GENERAL_TAX).setScale(2);
    }

    public BigDecimal getCurrEduCess() {
        return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_EDUCATIONAL_CESS).setScale(2);
    }

    public BigDecimal getCurrLibCess() {
        return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_LIBRARY_CESS).setScale(2);

    }

    public BigDecimal getCurrentUnauthPenalty() {
        return reasonwiseDues.get(CURRENT_DMD).get(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY).setScale(2);
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
        return NumberToWordConverter.amountInWordsWithCircumfix(getGrandTotal());
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
            throw new ApplicationRuntimeException("Error in Bill due date", e);
        }
        Calendar billDueDate = Calendar.getInstance();
        billDueDate.setTime(billDate);
        billDueDate.add(Calendar.DATE, 15);
        return dateFormat.format(billDueDate.getTime()).toString();
    }

    public String getWaterMeterStatus() {
        String waterRateCode = basicProperty.getProperty().getPropertyDetail().getWater_Meter_Num();
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
        EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
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

    public BigDecimal getResidentialALV() {
        BigDecimal resALVTotal = BigDecimal.ZERO;

        if (this.taxCalcInfo != null) {

            String propType = this.taxCalcInfo.getPropertyType();

            if (propType.equals(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE)) {
                return this.taxCalcInfo.getTotalNetARV();
            } else {
                resALVTotal = sumALVOnUnitUsage(USAGES_FOR_RESD);
            }
        }

        return resALVTotal.setScale(2);
    }

    public BigDecimal getNonResidentialALV() {
        BigDecimal nonResALVTotal = BigDecimal.ZERO;

        if (this.taxCalcInfo != null) {

            String propType = this.taxCalcInfo.getPropertyType();

            if (propType.equals(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE)) {
                return this.taxCalcInfo.getTotalNetARV();
            } else {
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
            for (UnitTaxCalculationInfo unitTaxInfo : this.taxCalcInfo.getUnitTaxCalculationInfos()) {
                if (usages.contains(unitTaxInfo.getUnitUsage())) {
                    totalALV = totalALV.add(unitTaxInfo.getNetARV());
                }
            }
        } else {
            for (int i = 0; i < this.taxCalcInfo.getUnitTaxCalculationInfos().size(); i++) {
                UnitTaxCalculationInfo unitTax = (UnitTaxCalculationInfo) this.taxCalcInfo.getUnitTaxCalculationInfos()
                        .get(i);
                if (usages.contains(unitTax.getUnitUsage())) {
                    totalALV = totalALV.add(unitTax.getNetARV());
                }
            }
        }

        return totalALV;
    }

    public BigDecimal getOpenPlotALV() {
        BigDecimal openPlotTotalALV = BigDecimal.ZERO;

        if (this.taxCalcInfo != null) {

            String propType = this.taxCalcInfo.getPropertyType();

            if (!propType.equals(OWNERSHIP_TYPE_VAC_LAND)) {
                openPlotTotalALV = sumALVOnUnitUsage(USAGES_FOR_OPENPLOT);
            } else {
                openPlotTotalALV = this.taxCalcInfo.getTotalNetARV();
            }
        }

        return openPlotTotalALV.setScale(2);
    }

    public String getMailingAddress() {
        return this.basicProperty.getAddress().toString();
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
