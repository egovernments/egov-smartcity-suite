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

import org.apache.log4j.Logger;
import org.egov.infra.utils.DateUtils;
import org.egov.tl.utils.Constants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class TradeLicense extends License {
    private static final Logger LOGGER = Logger.getLogger(TradeLicense.class);
    private static final long serialVersionUID = 1L;
    private List<MotorMaster> motorMasterList;
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

    @Override
    public String getStateDetails() {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        final StringBuffer details = new StringBuffer();
        if (getLicenseNumber() != null && !getLicenseNumber().isEmpty())
            details.append("TradeLicense Number " +getLicenseNumber() +" and ");
       details.append(String.format(" Application Number %s with application date %s.", applicationNumber ,
                (applicationDate!=null ?formatter.format(applicationDate):(formatter.format(new Date())))));
        return details.toString();
    }
    
    public List<String> populateHotelGradeList() {
        hotelGradeList = new ArrayList<String>();
        for (final String element : HOTELGRADE)
            hotelGradeList.add(element);
        return hotelGradeList;
    }
    
    public Boolean disablePrintCertificate() {
        Boolean disablePrintCert = false;
        if (getTradeName().isNocApplicable() != null && getTradeName().isNocApplicable()) {
            final Calendar instance = Calendar.getInstance();
            final Date newDate = new Date();
            if (getCommencementDate() != null) {
                instance.setTime(getCommencementDate());
                instance.add(Calendar.MONTH, 10);
                if (newDate.before(instance.getTime()))
                    disablePrintCert = true;
            }
        }
        return disablePrintCert;
    }

    public List<MotorMaster> getMotorMasterList() {
        return motorMasterList;
    }

    public List<String> getHotelGradeList() {
        return hotelGradeList;
    }

    public void setMotorMasterList(final List<MotorMaster> motorMasterList) {
        this.motorMasterList = motorMasterList;
    }


    public void setHotelGradeList(final List<String> hotelGradeList) {
        this.hotelGradeList = hotelGradeList;
    }

    @NotAudited
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

    @NotAudited
    public BigDecimal getSandBuckets() {
        return sandBuckets;
    }

    public void setSandBuckets(final BigDecimal sandBuckets) {
        this.sandBuckets = sandBuckets;
    }

    @NotAudited
    public BigDecimal getWaterBuckets() {
        return waterBuckets;
    }

    public void setWaterBuckets(final BigDecimal waterBuckets) {
        this.waterBuckets = waterBuckets;
    }

    @NotAudited
    public BigDecimal getDcpExtinguisher() {
        return dcpExtinguisher;
    }

    public void setDcpExtinguisher(final BigDecimal dcpExtinguisher) {
        this.dcpExtinguisher = dcpExtinguisher;
    }

    @NotAudited
    public String getNocNumber() {
        return nocNumber;
    }

    public void setNocNumber(final String nocNumber) {
        this.nocNumber = nocNumber;
    }

    @NotAudited
    public Boolean getIsCertificateGenerated() {
        return isCertificateGenerated;
    }

    public void setIsCertificateGenerated(final Boolean isCertificateGenerated) {
        this.isCertificateGenerated = isCertificateGenerated;
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
    @NotAudited
    public List<LicenseDocument> getDocuments() {
        return documents;
    }

    @Override
    public void setDocuments(final List<LicenseDocument> documents) {
        this.documents = documents;
    }

}
