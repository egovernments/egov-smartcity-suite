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

package org.egov.tl.web.controller.transactions.newlicense;

import org.egov.tl.entity.License;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.LicenseConfigurationService;
import org.egov.tl.web.validator.newlicense.UpdateLicenseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;
import static org.egov.infra.reporting.util.ReportUtil.reportAsResponseEntity;
import static org.egov.tl.utils.Constants.MESSAGE;

@Controller
@RequestMapping("/license/update")
public class UpdateLicenseController extends UpdateLicenseProcessflowController {

    private static final String REDIRECT_TO_VIEW = "redirect:/license/success/";
    private static final String LICENSECREATE = "license-create";

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    @Autowired
    private UpdateLicenseValidator updateLicenseValidator;

    @GetMapping("{licenseId}")
    public String update(TradeLicense tradeLicense, Model model, RedirectAttributes redirectAttributes) {
        if (updateLicenseValidator.applicationInProgress(tradeLicense, redirectAttributes)) {
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        } else {
            model.addAttribute("digiSignEnabled", licenseConfigurationService.digitalSignEnabled());
            return LICENSECREATE;
        }
    }

    @PostMapping("{licenseId}")
    public String save(@Valid @ModelAttribute TradeLicense tradeLicense, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        updateLicenseValidator.validate(tradeLicense, bindingResult);
        if (bindingResult.hasErrors()) {
            return LICENSECREATE;
        } else if (updateLicenseValidator.applicationInProgress(tradeLicense, redirectAttributes)) {
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        } else {
            licenseNewApplicationService.saveApplication(tradeLicense);
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        }
    }

    @PostMapping("forward/{licenseId}")
    public String forwardApplication(@Valid @ModelAttribute TradeLicense tradeLicense, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        updateLicenseValidator.validate(tradeLicense, bindingResult);
        if (bindingResult.hasErrors()) {
            return LICENSECREATE;
        } else if (updateLicenseValidator.applicationInProgress(tradeLicense, redirectAttributes)) {
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        } else {
            licenseNewApplicationService.forwardApplication(tradeLicense);
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.application.forward");
            redirectAttributes.addFlashAttribute("approverName", tradeLicense.getWorkflowContainer().getApproverName());
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        }
    }

    @PostMapping("approve/{licenseId}")
    public String approveApplication(@Valid @ModelAttribute TradeLicense tradeLicense, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        updateLicenseValidator.validate(tradeLicense, bindingResult);
        if (bindingResult.hasErrors()) {
            return LICENSECREATE;
        } else if (updateLicenseValidator.applicationInProgress(tradeLicense, redirectAttributes)) {
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        } else {
            licenseNewApplicationService.approveApplication(tradeLicense);
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.license.approve");
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        }
    }

    @PostMapping("reject/{licenseId}")
    public String rejectApplication(@Valid @ModelAttribute TradeLicense tradeLicense, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        updateLicenseValidator.validate(tradeLicense, bindingResult);
        if (bindingResult.hasErrors()) {
            return LICENSECREATE;
        } else if (updateLicenseValidator.applicationInProgress(tradeLicense, redirectAttributes)) {
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        } else {
            licenseNewApplicationService.rejectApplication(tradeLicense);
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.application.rejected");
            redirectAttributes.addFlashAttribute("initiatorPosition",
                    tradeLicense.getCurrentState().getOwnerPosition().getDeptDesig().getDesignation().getName());
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        }
    }

    @PostMapping("cancel/{licenseId}")
    public String cancelApplication(@Valid @ModelAttribute TradeLicense tradeLicense, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        updateLicenseValidator.validate(tradeLicense, bindingResult);
        if (bindingResult.hasErrors()) {
            return LICENSECREATE;
        } else if (updateLicenseValidator.applicationInProgress(tradeLicense, redirectAttributes)) {
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        } else {
            licenseNewApplicationService.cancelApplication(tradeLicense);
            redirectAttributes.addFlashAttribute(MESSAGE, "msg.license.cancel");
            return REDIRECT_TO_VIEW + tradeLicense.getId();
        }
    }

    @PostMapping("sign/{licenseId}")
    public String digitalSignApplication(@Valid @ModelAttribute TradeLicense tradeLicense, BindingResult bindingResult,
                                         Model model) {
        updateLicenseValidator.validate(tradeLicense, bindingResult);
        if (bindingResult.hasErrors())
            return LICENSECREATE;
        License license = licenseNewApplicationService.generateCertificateForDigiSign(tradeLicense);
        model.addAttribute("fileStoreIds", license.getCertificateFileId());
        model.addAttribute("applicationNo", license.getApplicationNumber());
        model.addAttribute("ulbCode", getCityCode());
        return "license-bulk-digisign-forward";
    }

    @PostMapping("generateCertificate/{licenseId}")
    public ResponseEntity<InputStreamResource> generateCertificate(TradeLicense tradeLicense) {
        return reportAsResponseEntity(tradeLicenseService.generateLicenseCertificate(tradeLicense, true));
    }

    @PostMapping("preview/{licenseId}")
    public ResponseEntity<InputStreamResource> previewCertificate(TradeLicense tradeLicense) {
        return reportAsResponseEntity(tradeLicenseService.generateLicenseCertificate(tradeLicense, false));
    }
}