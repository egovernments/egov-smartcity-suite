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

package org.egov.tl.web.actions.viewtradelicense;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.actions.BaseLicenseAction;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.tl.utils.Constants.MESSAGE;
import static org.egov.tl.utils.Constants.REPORT_PAGE;

@ParentPackage("egov")
@Results({@Result(name = REPORT_PAGE, location = "viewTradeLicense-report.jsp"),
        @Result(name = MESSAGE, location = "viewTradeLicense-message.jsp"),
        @Result(name = "digisigncertificate", type = "redirect", location = "/tradelicense/download/digisign-certificate",
                params = {"file", "${digiSignedFile}", "applnum", "${applNum}"})
})
public class ViewTradeLicenseAction extends BaseLicenseAction {
    private static final long serialVersionUID = 1L;

    private Long licenseId;
    private String digiSignedFile;
    private String applNum;

    @Override
    public TradeLicense getModel() {
        return tradeLicense;
    }

    @Override
    public String getReportId() {
        return reportId;
    }

    @Override
    @Action(value = "/viewtradelicense/viewTradeLicense-showForApproval")
    public String showForApproval() {
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        return super.showForApproval();
    }

    @Action(value = "/viewtradelicense/viewTradeLicense-view")
    public String view() {
        if (license() != null && license().getId() != null)
            tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        else if (applicationNo != null && !applicationNo.isEmpty())
            tradeLicense = tradeLicenseService.getLicenseByApplicationNumber(applicationNo);
        licenseHistory = tradeLicenseService.populateHistory(tradeLicense);
        return Constants.VIEW;
    }

    @ValidationErrorPage(REPORT_PAGE)
    @Action(value = "/viewtradelicense/viewTradeLicense-generateCertificate")
    public String generateCertificate() {
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        if (isNotBlank(tradeLicense.getCertificateFileId())) {
            setDigiSignedFile(license().getCertificateFileId());
            setApplNum(license().getApplicationNumber());
            return "digisigncertificate";
        } else {
            reportId = reportViewerUtil.addReportToTempCache(tradeLicenseService.generateLicenseCertificate(license(), false));
            return REPORT_PAGE;
        }
    }

    @ValidationErrorPage(REPORT_PAGE)
    @Action(value = "/viewtradelicense/generate-provisional-certificate")
    public String generateProvisionalCertificate() {
        tradeLicense = tradeLicenseService.getLicenseById(license().getId());
        reportId = reportViewerUtil.addReportToTempCache(tradeLicenseService.generateLicenseCertificate(license(), true));
        return REPORT_PAGE;
    }

    public Long getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }

    public String getDigiSignedFile() {
        return digiSignedFile;
    }

    public void setDigiSignedFile(String digiSignedFile) {
        this.digiSignedFile = digiSignedFile;
    }

    public String getApplNum() {
        return applNum;
    }

    public void setApplNum(String applNum) {
        this.applNum = applNum;
    }

}