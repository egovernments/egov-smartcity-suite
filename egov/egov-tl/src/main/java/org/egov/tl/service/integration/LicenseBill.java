/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.tl.service.integration;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.LatePayPenaltyCalculator;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.tl.entity.License;
import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.service.PenaltyRatesService;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

public class LicenseBill extends AbstractBillable implements LatePayPenaltyCalculator {

    @Autowired
    private LicenseUtils licenseUtils;
    private License license;
    private String moduleName;
    private String serviceCode;
    @Autowired
    private EgBillDao egBillDao;
    private String referenceNumber;
    private Boolean isCallbackForApportion = Boolean.FALSE;
    public static final String DEFAULT_FUNCTIONARY_CODE = "1";
    private String transanctionReferenceNumber;
    @Autowired
    private PenaltyRatesService penaltyRatesService;

    public License getLicense() {
        return license;
    }

    public void setLicense(final License license) {
        this.license = license;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    @Override
    public Module getModule() {
        return licenseUtils.getModule(moduleName);
    }

    @Override
    public String getBillPayee() {
        return license.getLicensee().getApplicantName();
    }

    @Override
    public String getBillAddress() {
        return license.getLicensee().getAddress() + (StringUtils.isNotBlank(license.getLicensee().getPhoneNumber())
                ? "\nPh : " + license.getLicensee().getPhoneNumber() : "");
    }

    @Override
    public EgDemand getCurrentDemand() {
        return license.getCurrentDemand();
    }

    @Override
    public List<EgDemand> getAllDemands() {
        final List<EgDemand> demands = new ArrayList<>();
        demands.add(license.getLicenseDemand());
        return demands;

    }

    @Override
    public EgBillType getBillType() {
        return egBillDao.getBillTypeByCode("AUTO");

    }

    @Override
    public Date getBillLastDueDate() {
        Date dueDate = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(dueDate);
        cal.get(Calendar.MONTH + 1);
        dueDate = cal.getTime();
        return dueDate;

    }

    @Override
    public Long getBoundaryNum() {
        return license.getBoundary().getId();
    }

    @Override
    public String getBoundaryType() {
        return license.getBoundary().getBoundaryType().getName();
    }

    @Override
    public String getDepartmentCode() {
        return licenseUtils.getDepartmentCodeForBillGenerate();
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        return new BigDecimal(DEFAULT_FUNCTIONARY_CODE);
    }

    @Override
    public String getFundCode() {
        return "01";// TODO Change according to TL
    }

    @Override
    public String getFundSourceCode() {
        return "01";// TODO Change according to TL
    }

    @Override
    public Date getIssueDate() {
        return new Date();
    }

    @Override
    public Date getLastDate() {
        return getBillLastDueDate();
    }

    @Override
    public Boolean getOverrideAccountHeadsAllowed() {
        return false;
    }

    @Override
    public Boolean getPartPaymentAllowed() {
        return false;
    }

    public void setServiceCode(final String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @Override
    public String getServiceCode() {
        return serviceCode;
    }

    @Override
    public BigDecimal getTotalAmount() {
        return getCurrentDemand().getBaseDemand();
    }

    @Override
    public Long getUserId() {
        return EgovThreadLocals.getUserId();
    }

    @Override
    public String getDescription() {
        return isBlank(license.getLicenseNumber()) ? "Application No : " + license.getApplicationNumber()
                : "License No : " + license.getLicenseNumber();
    }

    @Override
    public String getDisplayMessage() {
        return moduleName + " Collection";
    }

    @Override
    public String getCollModesNotAllowed() {
        return "";
    }

    public String getPropertyId() {
        return defaultString(license.getLicenseNumber(), license.getApplicationNumber());
    }

    @Override
    public Boolean isCallbackForApportion() {
        return isCallbackForApportion;
    }

    @Override
    public void setCallbackForApportion(final Boolean b) {
        isCallbackForApportion = b;
    }

    @Override
    public BigDecimal getLPPPercentage() {
        return BigDecimal.ZERO;
    }

    @Override
    public LPPenaltyCalcType getLPPenaltyCalcType() {
        return null;
    }

    @Override
    public void setPenaltyCalcType(final LPPenaltyCalcType penaltyType) {

    }

    @Override
    public String getConsumerId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal calculateLPPenaltyForPeriod(final Date fromDate, final Date toDate, final BigDecimal amount) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BigDecimal calculatePenalty(final Date commencementDate, final Date collectionDate, final BigDecimal amount) {
        // FIXME check dateOfCreation/startDate or what ever to be used to calculate penality
        if (commencementDate != null) {
            final int days = Days.daysBetween(new LocalDate(commencementDate.getTime()), new LocalDate(collectionDate.getTime()))
                    .getDays();
            final PenaltyRates penaltyRates = penaltyRatesService.findByDaysAndLicenseAppType(Long.valueOf(days),
                    license.getLicenseAppType());
            return amount.multiply(BigDecimal.valueOf(penaltyRates.getRate() / 100));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String getTransanctionReferenceNumber() {
        return transanctionReferenceNumber;
    }

    public void setTransanctionReferenceNumber(final String transanctionReferenceNumber) {
        this.transanctionReferenceNumber = transanctionReferenceNumber;
    }

    public Map<Installment, BigDecimal> getCalculatedPenalty(final Date commencementDate, final Date collectionDate,
            final BigDecimal amount, final Installment currentInstallment) {
        final Map<Installment, BigDecimal> installmentPenalty = new HashMap<Installment, BigDecimal>();
        installmentPenalty.put(currentInstallment, calculatePenalty(commencementDate, collectionDate, amount));
        return installmentPenalty;
    }
}
