/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernmentradeLicenseService.org
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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernmentradeLicenseService.org.
 */
package org.egov.tl.web.actions.cancellation;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
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
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseStatusValues;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.Licensee;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.UnitOfMeasurement;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@Results({@Result(name = CancelLicenseAction.NEW, location = "cancelLicense-new.jsp"),
        @Result(name = Constants.CANCEL_Result_MSG_PAGE, location = "cancelLicense-cancelResultMsg.jsp"),
        @Result(name = CancelLicenseAction.SUCCESS, type = "redirectAction", location = "CancelLicense.action")})
public class CancelLicenseAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    protected WorkflowBean workflowBean = new WorkflowBean();
    private Integer reasonForCancellation;
    private String refernceno;
    private Date commdateApp;
    private String cancelInforemarks;
    private Map reasonMap;
    private String zoneName;
    private String wardName;
    private Integer licenseId;
    private TradeLicense license = new TradeLicense();

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private LicenseUtils licenseUtils;
    @Autowired
    private TradeLicenseService tradeLicenseService;

    public CancelLicenseAction() {
        addRelatedEntity("boundary", Boundary.class);
        addRelatedEntity("licensee", Licensee.class);
        addRelatedEntity("tradeName", LicenseSubCategory.class);
        addRelatedEntity("licensee.boundary", Boundary.class);
        addRelatedEntity("buildingType", NatureOfBusiness.class);
        addRelatedEntity("category", LicenseCategory.class);
        addRelatedEntity("uom", UnitOfMeasurement.class);

    }

    public Integer getLicenseId() {
        return this.licenseId;
    }

    public void setLicenseId(Integer licenseId) {
        this.licenseId = licenseId;
    }

    @SkipValidation
    @Action(value = "/cancellation/cancelLicense-newForm")
    public String newForm() {
        this.license = this.tradeLicenseService.getLicenseById(this.licenseId.longValue());
        return Constants.NEW;
    }

    @Override
    public void prepare() {
        super.prepare();
        List<Boundary> areaList = new ArrayList<Boundary>();
        this.addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE, areaList);
        // Fetch Ward Dropdown List
        List<Boundary> divisionList = new ArrayList<Boundary>();
        this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, divisionList);
        // Fetch Zone Dropdown List
        this.addDropdownData(Constants.DROPDOWN_ZONE_LIST, this.licenseUtils.getAllZone());
        this.addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, this.licenseUtils.getAllTradeNames("TradeLicense"));
        this.license = this.tradeLicenseService.getLicenseById(this.licenseId.longValue());
        // To load zone and ward for a locality
        if (this.license != null) {
            Boundary localityBndry = this.tradeLicenseService.blockByLocality(this.license.getBoundary().getId());
            if (localityBndry != null) {
                Boundary wardBoundary = localityBndry.getParent();
                Boundary zoneBoundary = wardBoundary.getParent();
                this.wardName = wardBoundary.getName();
                this.zoneName = zoneBoundary.getName();
            }
        }
    }

    /**
     * @return to cancellation view page
     * @description cancels the license by setting status to CANCELLED and Inactive. Makes an entry in EGTL_STATUS_VALUES table
     */
    @ValidationErrorPage(Constants.NEW)
    @Validations(requiredFields = {
            @RequiredFieldValidator(fieldName = "reasonForCancellation", message = "", key = Constants.REQUIRED),
            @RequiredFieldValidator(fieldName = "refernceno", message = "", key = Constants.REQUIRED)})
    @Action(value = "/cancellation/cancelLicense-confirmCancellation")
    public String confirmCancellation() {
        this.license.setActive(false);
        this.license.setStatus(this.licenseUtils.getLicenseStatusbyCode("CAN"));
        LicenseStatusValues licenseStatusValues = this.licenseUtils.getCurrentStatus(this.license);
        if (licenseStatusValues != null)
            licenseStatusValues.setActive(false);
        LicenseStatusValues newLicenseStatusValues = new LicenseStatusValues();
        newLicenseStatusValues.setLicense(this.license);
        newLicenseStatusValues.setLicenseStatus(this.licenseUtils.getLicenseStatusbyCode("CAN"));
        newLicenseStatusValues.setReason(Integer.valueOf(this.reasonForCancellation));

        newLicenseStatusValues.setReferenceNo(this.refernceno);
        newLicenseStatusValues.setReferenceDate(this.commdateApp);
        newLicenseStatusValues.setCreatedDate(new Date());
        newLicenseStatusValues.setModifiedDate(new Date());
        newLicenseStatusValues.setCreatedBy(this.securityUtils.getCurrentUser());
        newLicenseStatusValues.setRemarks(this.cancelInforemarks);
        newLicenseStatusValues.setActive(true);
        newLicenseStatusValues.setPreviousStatusVal(licenseStatusValues);

        this.license.addLicenseStatusValuesSet(newLicenseStatusValues);
        this.tradeLicenseService.licensePersitenceService().update(this.license);
        return Constants.CANCEL_Result_MSG_PAGE;
    }

    public Integer getReasonForCancellation() {
        return this.reasonForCancellation;
    }

    public void setReasonForCancellation(Integer reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
    }

    public String getRefernceno() {
        return this.refernceno;
    }

    public void setRefernceno(String refernceno) {
        this.refernceno = refernceno;
    }

    public Date getCommdateApp() {
        return this.commdateApp;
    }

    public void setCommdateApp(Date commdateApp) {
        this.commdateApp = commdateApp;
    }

    public String getCancelInforemarks() {
        return this.cancelInforemarks;
    }

    public void setCancelInforemarks(String cancelInforemarks) {
        this.cancelInforemarks = cancelInforemarks;
    }

    @Override
    public Object getModel() {
        return this.license;
    }

    public Map getReasonMap() {

        this.reasonMap = this.licenseUtils.getCancellationReasonMap();
        return this.reasonMap;
    }

    public void setReasonMap(Map reasonMap) {
        this.reasonMap = this.licenseUtils.getCancellationReasonMap();
    }

    public String getCancellationDetails() {
        return new StringBuffer("[Reason for Cancellation : ").
                append(this.getReasonForCancellation()).append(", Reference number : ").append(this.getRefernceno()).
                append(", Reference date : ").append(this.getCommdateApp()).append(" ]").toString();

    }

    public String getZoneName() {
        return this.zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getWardName() {
        return this.wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

}
