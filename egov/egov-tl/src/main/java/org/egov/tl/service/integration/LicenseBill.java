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

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Installment;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.LatePayPenaltyCalculator;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.tl.entity.License;
import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.service.PenaltyRatesService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isBlank;

@Transactional(readOnly = true)
public class LicenseBill extends AbstractBillable implements LatePayPenaltyCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(LicenseBill.class);

    private License license;
    private String moduleName;
    private String serviceCode;
    private String referenceNumber;
    private Boolean isCallbackForApportion = Boolean.FALSE;
    public static final String DEFAULT_FUNCTIONARY_CODE = "1";
    private String transanctionReferenceNumber;

    @Autowired
    private LicenseUtils licenseUtils;
    @Autowired
    private PenaltyRatesService penaltyRatesService;
    @Autowired
    private EgBillDao egBillDao;

    public License getLicense() {
        return license;
    }

    public void setLicense(final License license) {
        this.license = license;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String getConsumerType() {
        return "";
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
    public String getEmailId() {
        return "";
    }

    @Override
    public String getBillAddress() {
        return license.getLicensee().getAddress() + (StringUtils.isNotBlank(license.getLicensee().getMobilePhoneNumber())
                ? "\nPh : " + license.getLicensee().getMobilePhoneNumber() : "");
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
        return "01";
    }

    @Override
    public String getFundSourceCode() {
        return "01";
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
        return license.getTotalBalance();
    }

    @Override
    public Long getUserId() {
        return ApplicationThreadLocals.getUserId();
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
        //not used
    }

    @Override
    public String getConsumerId() {
        return null;
    }

    @Override
    public BigDecimal calculateLPPenaltyForPeriod(final Date fromDate, final Date toDate, final BigDecimal amount) {
        return null;
    }

    @Override
    public BigDecimal calculatePenalty(final Date commencementDate, final Date collectionDate, final BigDecimal amount) {
        if (commencementDate != null) {
            final int paymentDueDays = Days
                    .daysBetween(new LocalDate(commencementDate.getTime()), new LocalDate(collectionDate.getTime()))
                    .getDays();
            final PenaltyRates penaltyRates = penaltyRatesService.findByDaysAndLicenseAppType(Long.valueOf(paymentDueDays),
                    license.getLicenseAppType());
            if (penaltyRates == null) {
                LOG.warn("License payment due since {} days, There is no penatlity rate definied for License Type {}",
                        paymentDueDays,
                        license.getLicenseAppType().getName());
                return BigDecimal.ZERO;
            }
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

    public Map<Installment, BigDecimal> getCalculatedPenalty(final Date fromDate, final Date collectionDate,
                                                             final EgDemand demand) {
        final Map<Installment, BigDecimal> installmentPenalty = new HashMap<>();
        for (final EgDemandDetails demandDetails : demand.getEgDemandDetails())
            if (!demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode().equals(Constants.PENALTY_DMD_REASON_CODE)
                    && demandDetails.getAmount().subtract(demandDetails.getAmtCollected()).signum() == 1)
                if (fromDate != null)
                    installmentPenalty.put(demandDetails.getEgDemandReason().getEgInstallmentMaster(),
                            calculatePenalty(fromDate, collectionDate, demandDetails.getAmount()));
                else
                    installmentPenalty.put(demandDetails.getEgDemandReason().getEgInstallmentMaster(),
                            calculatePenalty(demandDetails.getEgDemandReason().getEgInstallmentMaster().getFromDate(),
                                    collectionDate, demandDetails.getAmount()));
        return installmentPenalty;
    }
}