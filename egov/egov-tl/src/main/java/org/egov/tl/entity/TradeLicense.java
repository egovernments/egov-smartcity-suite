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
package org.egov.tl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.utils.DateUtils;
import org.egov.tl.utils.Constants;
import org.joda.time.LocalDate;

public class TradeLicense extends License {
    private static final Logger LOGGER = Logger.getLogger(TradeLicense.class);
    private static final long serialVersionUID = 1L;
    private List<MotorDetails> installedMotorList;
    private Boolean motorInstalled;
    private List<MotorMaster> motorMasterList;
    private BigDecimal totalHP;
    private List<String> hotelGradeList;
    private String hotelGrade;
    private List hotelSubCatList;
    public static final String[] HOTELGRADE = { "Grade A", "Grade B", "Grade C" };
    private List licenseZoneList;
    private BigDecimal sandBuckets;
    private BigDecimal waterBuckets;
    private BigDecimal dcpExtinguisher;
    private String nocNumber;
    private Boolean isCertificateGenerated;
    private Long id;
    private List<LicenseDocument> documents = new ArrayList<>();

    public Set<EgDemandDetails> additionalDemandDetails(final Set<EgDemandReasonMaster> egDemandReasonMasters,
            final Installment installment) {
        LOGGER.debug("Adding additinal Demand Details...");
        final Set<EgDemandDetails> addtionalDemandDetails = new LinkedHashSet<EgDemandDetails>();
        BigDecimal baseMotorFee = null;
        BigDecimal actualMotorFee = null;
        BigDecimal amountToaddBaseDemand = BigDecimal.ZERO;
        for (final MotorMaster mm : getMotorMasterList()) {
            if (mm.getMotorHpFrom().compareTo(BigDecimal.ZERO) == 0 && mm.getMotorHpTo().compareTo(BigDecimal.ZERO) == 0)
                baseMotorFee = mm.getUsingFee();
            if (getTotalHP() == null || mm.getMotorHpFrom().compareTo(getTotalHP()) <= 0
                    && mm.getMotorHpTo().compareTo(getTotalHP()) >= 0)
                actualMotorFee = mm.getUsingFee();
        }
        LOGGER.debug("Adding Motor Fee Details...");
        for (final EgDemandReasonMaster dm : egDemandReasonMasters)
            if (dm.getReasonMaster().equalsIgnoreCase("Motor Fee"))
                for (final EgDemandReason reason : dm.getEgDemandReasons())
                    if (reason.getEgInstallmentMaster().getId().equals(installment.getId()))
                        // check for current year installment only
                        if (getTotalHP() == null || getTotalHP().compareTo(BigDecimal.ZERO) == 0) {
                            addtionalDemandDetails.add(EgDemandDetails
                                    .fromReasonAndAmounts(baseMotorFee, reason, BigDecimal.ZERO));
                            amountToaddBaseDemand = baseMotorFee;
                        } else {
                            addtionalDemandDetails.add(EgDemandDetails.fromReasonAndAmounts(actualMotorFee, reason,
                                    BigDecimal.ZERO));
                            amountToaddBaseDemand = actualMotorFee;
                        }
        LOGGER.debug("Adding Motor Fee completed.");
        LOGGER.debug("Addtional Demand Details size." + addtionalDemandDetails.size());
        LOGGER.debug("Adding additinal Demand Details done.");
        for (final LicenseDemand ld : getDemandSet())
            if (ld.getEgInstallmentMaster().getId() == installment.getId()) {
                ld.getEgDemandDetails().addAll(addtionalDemandDetails);
                ld.addBaseDemand(amountToaddBaseDemand);
                break;
            }
        return addtionalDemandDetails;
    }

    @Override
    public String generateApplicationNumber(final String runningNumber) {
        setApplicationNumber("TL-APPL" + runningNumber);
        return getApplicationNumber();
    }

    @Override
    public String generateLicenseNumber(final Serializable runningNumber) {
        this.licenseNumber = String.format("TL/%05d/%s", runningNumber, DateUtils.currentDateToYearFormat());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Generated License Number : "+this.licenseNumber);
        return this.licenseNumber;
    }

    public String generateNocNumber(final String runningNumber) {
        LOGGER.debug("Generating NOC Number...");
        final StringBuilder nocNumber = new StringBuilder(32);
        nocNumber.append("W.O.").append(Constants.BACKSLASH).append("PRO-NOC").append(Constants.BACKSLASH).append(runningNumber)
                .append(Constants.BACKSLASH).append("LC.");
        setNocNumber(nocNumber.toString());
        LOGGER.debug("Generated NOC Number =" + nocNumber.toString());
        LOGGER.debug("Generating NOC Number completed.");
        return nocNumber.toString();
    }

    public List<MotorDetails> getInstalledMotorList() {
        return installedMotorList;
    }

    public Boolean getMotorInstalled() {
        return motorInstalled;
    }

    public List<MotorMaster> getMotorMasterList() {
        return motorMasterList;
    }

    public List<String> getHotelGradeList() {
        return hotelGradeList;
    }

    @Override
    public String getStateDetails() {
        final StringBuffer details = new StringBuffer();
        if (getLicenseNumber() != null && !getLicenseNumber().isEmpty())
            details.append(getLicenseNumber()).append("/");
        details.append(getApplicationNumber());
        return details.toString();
    }

    public BigDecimal getTotalHP() {
        return totalHP;
    }

    public Boolean isMotorInstalled() {
        return motorInstalled;
    }

    public void setInstalledMotorList(final List<MotorDetails> installedMotorList) {
        this.installedMotorList = installedMotorList;
    }

    public void setMotorInstalled(final Boolean motorInstalled) {
        this.motorInstalled = motorInstalled;
    }

    public void setMotorMasterList(final List<MotorMaster> motorMasterList) {
        this.motorMasterList = motorMasterList;
    }

    public void setTotalHP(final BigDecimal totalHP) {
        this.totalHP = totalHP;
    }

    public void setHotelGradeList(final List<String> hotelGradeList) {
        this.hotelGradeList = hotelGradeList;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append("TradeLicense={");
        str.append(super.toString());
        str.append("  motorInstalled=").append(motorInstalled);
        str.append("  totalHP=").append(totalHP == null ? "null" : totalHP.toString());
        str.append("  installedMotorList=").append(installedMotorList == null ? "null" : installedMotorList.toString());
        str.append("}");
        return str.toString();
    }

    @Override
    public void setCreationAndExpiryDate() {
        setDateOfCreation(new Date());
        updateExpiryDate(new Date());
    }

    @Override
    public void setCreationAndExpiryDateForEnterLicense() {
        setDateOfCreation(getDateOfCreation());
        updateExpiryDate(getDateOfCreation());
    }

    @Override
    public void updateExpiryDate(final Date renewalDate) {
        // this.setDateOfCreation(new Date());
        final Calendar instance = Calendar.getInstance();
        if ("PFA".equalsIgnoreCase(getFeeTypeStr())) {
            final Date dateOfExpiry = renewalDate;
            instance.setTime(dateOfExpiry);
            final int month = instance.get(Calendar.MONTH);
            int year = instance.get(Calendar.YEAR);
            year = year + 5;
            if (month == Calendar.JANUARY || month == Calendar.FEBRUARY || month == Calendar.MARCH)
                year = year - 1;
            instance.set(year, 2, 31);
            setDateOfExpiry(instance.getTime());
        } else if ("CNC".equalsIgnoreCase(getFeeTypeStr())) {
            final Date dateOfExpiry = renewalDate;
            instance.setTime(dateOfExpiry);
            final int month = instance.get(Calendar.MONTH);
            int year = instance.get(Calendar.YEAR);
            year = year + 1;
            if (month == Calendar.JANUARY || month == Calendar.FEBRUARY || month == Calendar.MARCH)
                year = year - 1;
            instance.set(year, 2, 31);
            setDateOfExpiry(instance.getTime());
        }
    }

    public List<String> populateHotelGradeList() {
        hotelGradeList = new ArrayList<String>();
        for (final String element : HOTELGRADE)
            hotelGradeList.add(element);
        return hotelGradeList;
    }

    public String getHotelGrade() {
        return hotelGrade;
    }

    public void setHotelGrade(final String hotelGrade) {
        this.hotelGrade = hotelGrade;
    }

    public List getHotelSubCatList() {
        return hotelSubCatList;
    }

    public void setHotelSubCatList(final List hotelSubCatList) {
        this.hotelSubCatList = hotelSubCatList;
    }

    public List getLicenseZoneList() {
        return licenseZoneList;
    }

    public void setLicenseZoneList(final List licenseZoneList) {
        this.licenseZoneList = licenseZoneList;
    }

    public BigDecimal getSandBuckets() {
        return sandBuckets;
    }

    public void setSandBuckets(final BigDecimal sandBuckets) {
        this.sandBuckets = sandBuckets;
    }

    public BigDecimal getWaterBuckets() {
        return waterBuckets;
    }

    public void setWaterBuckets(final BigDecimal waterBuckets) {
        this.waterBuckets = waterBuckets;
    }

    public BigDecimal getDcpExtinguisher() {
        return dcpExtinguisher;
    }

    public void setDcpExtinguisher(final BigDecimal dcpExtinguisher) {
        this.dcpExtinguisher = dcpExtinguisher;
    }

    public String getNocNumber() {
        return nocNumber;
    }

    public void setNocNumber(final String nocNumber) {
        this.nocNumber = nocNumber;
    }

    public Boolean getIsCertificateGenerated() {
        return isCertificateGenerated;
    }

    public void setIsCertificateGenerated(final Boolean isCertificateGenerated) {
        this.isCertificateGenerated = isCertificateGenerated;
    }

    // TODO: Reviewed by Satyam, suggested to rename the variable name, committing after changes
    public Boolean disablePrintCertificate() {
        Boolean disablePrintCert = false;
        if (getTradeName().isNocApplicable() != null && getTradeName().isNocApplicable()) {
            final Calendar instance = Calendar.getInstance();
            final Date newDate = new Date();
            if (getDateOfCreation() != null) {
                instance.setTime(getDateOfCreation());
                instance.add(Calendar.MONTH, 10);
                if (newDate.before(instance.getTime()))
                    disablePrintCert = true;
            }
        }
        return disablePrintCert;
    }

    @Override
    public String getAuditDetails() {
        return new StringBuffer("[Name of the Establishment : ").
                append(getNameOfEstablishment()).append(", Applicant Name : ").append(getLicensee().getApplicantName()).
                append(", Application Date : ").append(DateUtils.toDefaultDateFormat(new LocalDate(getApplicationDate()))).
                append(", Address : ").append(licensee.getAddress())
                .append(", Trade Name : ").append(getTradeName().getName()).append(" ]").toString();

    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<LicenseDocument> getDocuments() {
        return documents;
    }

    @Override
    public void setDocuments(final List<LicenseDocument> documents) {
        this.documents = documents;
    }

}
