/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.service.integration;

import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.LatePayPenaltyCalculator;
import org.egov.demand.model.AbstractBillable;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.tradelicense.domain.entity.License;
import org.egov.tradelicense.domain.entity.LicenseDemand;
import org.egov.tradelicense.utils.LicenseUtils;

public class LicenseBill extends AbstractBillable implements LatePayPenaltyCalculator {

    private LicenseUtils licenseUtils;
    private License license;
    private String moduleName;
    private String serviceCode;
    private EgBillDao billDao;

    public void setBillDao(final EgBillDao billDao) {
        this.billDao = billDao;
    }

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
        return license.getLicensee().getAddress().toString() + "\nPh : "
                + defaultString(license.getLicensee().getPhoneNumber());
    }

    @Override
    public EgDemand getCurrentDemand() {
        final Set<LicenseDemand> demands = license.getDemandSet();
        for (final EgDemand demand : demands)
            if (demand.getIsHistory().equals("N"))
                return demand;
        return null;
    }

    @Override
    public List<EgDemand> getAllDemands() {
        return new ArrayList<EgDemand>(license.getDemandSet());

    }

    @Override
    public EgBillType getBillType() {
        return billDao.getBillTypeByCode("AUTO");

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
        return "CAF";// TODO
    }

    @Override
    public BigDecimal getFunctionaryCode() {
        return BigDecimal.ZERO;
    }

    @Override
    public String getFundCode() {
        return "45061";// TODO Insert
    }

    @Override
    public String getFundSourceCode() {
        return "BOM 637";// TODO
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
        return false;
    }

    @Override
    public void setCallbackForApportion(final Boolean b) {

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
    public BigDecimal calculatePenalty(final Date latestCollectedRcptDate, final Date fromDate, final BigDecimal amount) {
        // TODO Auto-generated method stub
        return null;
    }

}
