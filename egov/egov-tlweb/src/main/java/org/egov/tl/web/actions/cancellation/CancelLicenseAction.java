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
package org.egov.tl.web.actions.cancellation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseStatusValues;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.UnitOfMeasurement;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.BaseLicenseService;
import org.egov.tl.service.TradeService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Results({ @Result(name = CancelLicenseAction.NEW, location = "cancelLicense-new.jsp"),
    @Result(name = Constants.CANCEL_Result_MSG_PAGE, location = "cancelLicense-cancelResultMsg.jsp"),
    @Result(name = CancelLicenseAction.SUCCESS, type = "redirectAction", location = "CancelLicense.action") })
public class CancelLicenseAction extends BaseFormAction {
    private static final Logger LOGGER = Logger.getLogger(CancelLicenseAction.class);

    protected LicenseUtils licenseUtils;
    private Integer reasonForCancellation;
    private String refernceno;
    private Date commdateApp;
    private String cancelInforemarks;
    private Map reasonMap;
    private String zoneName;
    private String wardName;
    @Autowired
    private SecurityUtils securityUtils;
    protected WorkflowBean workflowBean = new WorkflowBean();
    @Autowired
    private BaseLicenseService baseLicenseService;

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    /**
     * @param ts the ts to set
     */
    public void setTs(final TradeService ts) {
        this.ts = ts;
    }

    /**
     * @return the licenseId
     */
    public Integer getLicenseId() {
        return licenseId;
    }

    /**
     * @param licenseId the licenseId to set
     */
    public void setLicenseId(final Integer licenseId) {
        this.licenseId = licenseId;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return CancelLicenseAction.serialVersionUID;
    }

    private static final long serialVersionUID = 1L;
    private TradeService ts;
    private Integer licenseId;
    private License license = new TradeLicense();

    /**
     * @return the license
     */
    public License getLicense() {
        return license;
    }

    /**
     * @param license the license to set
     */
    public void setLicense(final License license) {
        this.license = license;
    }

    @SkipValidation
    @Action(value = "/cancellation/cancelLicense-newForm")
    public String newForm() {
        license = ts.getTps().findById(licenseId.longValue(), false);
        return Constants.NEW;
    }

    public CancelLicenseAction() {
        this.addRelatedEntity("boundary", Boundary.class);
        this.addRelatedEntity("licensee", Licensee.class);
        this.addRelatedEntity("tradeName", LicenseSubCategory.class);
        this.addRelatedEntity("licensee.boundary", Boundary.class);
        this.addRelatedEntity("buildingType", NatureOfBusiness.class);
        this.addRelatedEntity("category", LicenseCategory.class);
        this.addRelatedEntity("uom", UnitOfMeasurement.class);

    }

    @Override
    public void prepare() {
        super.prepare();
        final List<Boundary> areaList = new ArrayList<Boundary>();
        addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE, areaList);
        // Fetch Ward Dropdown List
        final List<Boundary> divisionList = new ArrayList<Boundary>();
        addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, divisionList);
        // Fetch Zone Dropdown List
        addDropdownData(Constants.DROPDOWN_ZONE_LIST, licenseUtils.getAllZone());
        addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, licenseUtils.getAllTradeNames("TradeLicense"));
        license = ts.getTps().findById(licenseId.longValue(), false);
        // To load zone and ward for a locality
        if (license != null) {
            final Boundary localityBndry = baseLicenseService.blockByLocality(license.getBoundary().getId());
            if (localityBndry != null) {
                final Boundary wardBoundary = localityBndry.getParent();
                final Boundary zoneBoundary = wardBoundary.getParent();
                wardName = wardBoundary.getName();
                zoneName = zoneBoundary.getName();
            }
        }
    }

    /**
     * @description cancels the license by setting status to CANCELLED and Inactive. Makes an entry in EGTL_STATUS_VALUES table
     * @return to cancellation view page
     */
    @ValidationErrorPage(Constants.NEW)
    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "reasonForCancellation", message = "", key = Constants.REQUIRED),
            @RequiredFieldValidator(fieldName = "refernceno", message = "", key = Constants.REQUIRED) })
    @Action(value = "/cancellation/cancelLicense-confirmCancellation")
    public String confirmCancellation() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Cancel Trade License Elements are:<<<<<<<<<<>>>>>>>>>>>>>:" + toString());
        license.setActive(false);
        license.setStatus(licenseUtils.getLicenseStatusbyCode("CAN"));
        final LicenseStatusValues licenseStatusValues = licenseUtils.getCurrentStatus(license);
        if (licenseStatusValues != null)
            licenseStatusValues.setActive(false);
        final LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
        newLicenseStatusValues.setLicense(license);
        newLicenseStatusValues.setLicenseStatus(licenseUtils.getLicenseStatusbyCode("CAN"));
        newLicenseStatusValues.setReason(Integer.valueOf(reasonForCancellation));

        newLicenseStatusValues.setReferenceNo(refernceno);
        newLicenseStatusValues.setReferenceDate(commdateApp);
        newLicenseStatusValues.setCreatedDate(new Date());
        newLicenseStatusValues.setModifiedDate(new Date());
        newLicenseStatusValues.setCreatedBy(securityUtils.getCurrentUser());
        newLicenseStatusValues.setRemarks(cancelInforemarks);
        newLicenseStatusValues.setActive(true);
        newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);

        license.addLicenseStatusValuesSet(newLicenseStatusValues);
        ts.getTps().update((TradeLicense) license);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Cancel Trade License Name of Establishment:<<<<<<<<<<>>>>>>>>>>>>>:" + license.getNameOfEstablishment());
        return Constants.CANCEL_Result_MSG_PAGE;
    }

    /**
     * @return the reasonForCancellation
     */
    public Integer getReasonForCancellation() {
        return reasonForCancellation;
    }

    /**
     * @param reasonForCancellation the reasonForCancellation to set
     */
    public void setReasonForCancellation(final Integer reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
    }

    /**
     * @return the refernceno
     */
    public String getRefernceno() {
        return refernceno;
    }

    /**
     * @param refernceno the refernceno to set
     */
    public void setRefernceno(final String refernceno) {
        this.refernceno = refernceno;
    }

    /**
     * @return the commdateApp
     */
    public Date getCommdateApp() {
        return commdateApp;
    }

    /**
     * @param commdateApp the commdateApp to set
     */
    public void setCommdateApp(final Date commdateApp) {
        this.commdateApp = commdateApp;
    }

    /**
     * @return the cancelInforemarks
     */
    public String getCancelInforemarks() {
        return cancelInforemarks;
    }

    /**
     * @param cancelInforemarks the cancelInforemarks to set
     */
    public void setCancelInforemarks(final String cancelInforemarks) {
        this.cancelInforemarks = cancelInforemarks;
    }

    @Override
    public Object getModel() {
        return license;
    }

    public void setModel(final License license) {
        this.license = license;
    }

    /**
     * @return the reasonMap
     */
    public Map getReasonMap() {

        reasonMap = licenseUtils.getCancellationReasonMap();
        return reasonMap;
    }

    /**
     * @param reasonMap the reasonMap to set
     */
    public void setReasonMap(final Map reasonMap) {
        this.reasonMap = licenseUtils.getCancellationReasonMap();
    }

    @Override
    public String toString()
    {
        final StringBuilder str = new StringBuilder();
        str.append("CancelLicenseAction={");
        str.append("  licenseUtils=").append(licenseUtils == null ? "null" : licenseUtils.toString());
        str.append("  reasonForCancellation=").append(reasonForCancellation == null ? "null" : reasonForCancellation.toString());
        str.append("  refernceno=").append(refernceno == null ? "null" : refernceno.toString());
        str.append("  commdateApp=").append(commdateApp == null ? "null" : commdateApp.toString());
        str.append("  cancelInforemarks=").append(cancelInforemarks == null ? "null" : cancelInforemarks.toString());
        str.append("  reasonMap=").append(reasonMap == null ? "null" : reasonMap.toString());
        str.append("  licenseId=").append(licenseId == null ? "null" : licenseId.toString());
        str.append("  license=").append(license == null ? "null" : license.toString());
        str.append("  ts=").append(ts == null ? "null" : ts.toString());
        return str.toString();
    }

    /*
     * public void setAuditEventService(AuditEventService auditEventService) { this.auditEventService = auditEventService; }
     */

    /*
     * protected void doAuditing(String action, String details) { License license = (License) this.getModel(); final AuditEvent
     * auditEvent = new AuditEvent(AuditModule.HPL, AuditEntity.HPL_LIC, action, this.license.getLicenseNumber(), details);
     * auditEvent.setPkId(license.getId());
     * auditEvent.setDetails2(this.workflowBean.getActionName()==null?"":this.workflowBean.getActionName());
     * this.auditEventService.createAuditEvent(auditEvent, this.license.getClass()); }
     */

    @SkipValidation
    public String auditReport() {
        return "auditReport";
    }

    public String getCancellationDetails() {
        return new StringBuffer("[Reason for Cancellation : ").
                append(getReasonForCancellation()).append(", Reference number : ").append(getRefernceno()).
                append(", Reference date : ").append(getCommdateApp()).append(" ]").toString();

    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

}
