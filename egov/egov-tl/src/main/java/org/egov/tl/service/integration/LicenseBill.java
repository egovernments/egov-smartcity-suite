/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.tl.service.integration;

import org.egov.demand.interfaces.LatePayPenaltyCalculator;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Module;
import org.egov.tl.entity.TradeLicense;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.infra.utils.StringUtils.defaultIfBlank;

public class LicenseBill extends AbstractBillable implements LatePayPenaltyCalculator {

    public static final String DEFAULT_FUNCTIONARY_CODE = "1";

    private TradeLicense license;
    private String moduleName;
    private String serviceCode;
    private String referenceNumber;
    private String departmentCode;
    private Boolean isCallbackForApportion = Boolean.FALSE;
    private Long userId;
    private Module module;
    private EgBillType billType;
    private String transanctionReferenceNumber;
    private String collModesNotAllowed;

    public TradeLicense getLicense() {
        return license;
    }

    public void setLicense(TradeLicense license) {
        this.license = license;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String getConsumerType() {
        return moduleName;
    }

    @Override
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String getBillPayee() {
        return license.getLicensee().getApplicantName();
    }

    @Override
    public String getEmailId() {
        return EMPTY;
    }

    @Override
    public String getBillAddress() {
        return new StringBuilder().
                append(license.getLicensee().getAddress()).
                append("\nPh : ").
                append(defaultIfBlank(license.getLicensee().getMobilePhoneNumber()))
                .toString();
    }

    @Override
    public EgDemand getCurrentDemand() {
        return license.getCurrentDemand();
    }

    @Override
    public List<EgDemand> getAllDemands() {
        return Arrays.asList(license.getDemand());

    }

    @Override
    public EgBillType getBillType() {
        return billType;

    }

    public void setBillType(EgBillType billType) {
        this.billType = billType;
    }

    @Override
    public Date getBillLastDueDate() {
        return new LocalDate().plusMonths(1).toDate();

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
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
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

    @Override
    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @Override
    public BigDecimal getTotalAmount() {
        return license.getTotalBalance();
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
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
        return collModesNotAllowed;
    }

    public void setCollModesNotAllowed(String collModesNotAllowed) {
        this.collModesNotAllowed = collModesNotAllowed;
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
    public void setPenaltyCalcType(LPPenaltyCalcType penaltyType) {
        // not used
    }

    @Override
    public String getConsumerId() {
        return defaultIfBlank(license.getLicenseNumber(), license.getApplicationNumber());
    }

    @Override
    public BigDecimal calculateLPPenaltyForPeriod(Date fromDate, Date toDate, BigDecimal amount) {
        return null;
    }

    @Override
    public BigDecimal calculatePenalty(Date commencementDate, Date collectionDate, BigDecimal amount) {
        return null;
    }

    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public String getTransanctionReferenceNumber() {
        return transanctionReferenceNumber;
    }

    public void setTransanctionReferenceNumber(String transanctionReferenceNumber) {
        this.transanctionReferenceNumber = transanctionReferenceNumber;
    }
    
    @Override
	public BigDecimal getMinAmountPayable() {
		return getTotalAmount();
	}
}