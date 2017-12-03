/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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

package org.egov.mrs.web.controller.application.registration;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.mrs.application.MarriageConstants.FILESTORE_MODULECODE;
import static org.egov.mrs.application.MarriageConstants.MODULE_NAME;
import static org.egov.mrs.application.MarriageConstants.NOOFDAYSTOPRINT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageRegistrationSearchFilter;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.web.adaptor.MarriageCerftificateJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageReIssueJsonAdaptor;
import org.egov.mrs.web.adaptor.MarriageRegistrationJsonAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles the Registration search
 *
 * @author nayeem
 */
@Controller
@RequestMapping(value = {"/registration"})
public class SearchRegistrationController {

    private static final String DATA = "{ \"data\":";
    private static final String REGISTRATION = "registration";
    private final MarriageRegistrationService marriageRegistrationService;
    private final SecurityUtils securityUtils;
    @Autowired
    protected MarriageRegistrationUnitService marriageRegistrationUnitService;
    @Autowired
    private MarriageCertificateService marriageCertificateService;
    @Autowired
    private FileStoreUtils fileStoreUtils;
    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    public SearchRegistrationController(final MarriageRegistrationService marriageRegistrationService,
                                        final SecurityUtils securityUtils) {
        this.marriageRegistrationService = marriageRegistrationService;
        this.securityUtils = securityUtils;
    }

    public void prepareSearchForm(final Model model) {
        model.addAttribute("marriageRegistrationUnit", marriageRegistrationUnitService.getActiveRegistrationunit());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showSearch(final Model model) {

        final List<Role> operatorRoles = securityUtils.getCurrentUser().getRoles().stream()
                .filter(role -> "Collection Operator".equalsIgnoreCase(role.getName())).collect(Collectors.toList());
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        final boolean isCollectionOperator = operatorRoles == null || operatorRoles.isEmpty() ? false : true;
        model.addAttribute("isCollectionOperator", isCollectionOperator);
        prepareSearchForm(model);
        return "registration-search";
    }

    @RequestMapping(value = "/searchApproved", method = RequestMethod.GET)
    public String showSearchApproved(final Model model) {

        final List<Role> operatorRoles = securityUtils.getCurrentUser().getRoles().stream()
                .filter(role -> "Collection Operator".equalsIgnoreCase(role.getName())).collect(Collectors.toList());
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        final boolean isCollectionOperator = operatorRoles == null || operatorRoles.isEmpty() ? false : true;
        model.addAttribute("isCollectionOperator", isCollectionOperator);
        prepareSearchForm(model);
        return "registration-search-approved";
    }

    @ModelAttribute("marriageRegistrationTypes")
    public List<MarriageCertificateType> mrTypeList() {
        final List<MarriageCertificateType> enumValues = new ArrayList<>();
        for (final MarriageCertificateType mct : Arrays.asList(MarriageCertificateType.values()))
            if (!"REJECTION".equalsIgnoreCase(mct.name()))
                enumValues.add(mct);
        return enumValues;
    }

    @RequestMapping(value = "/collectmrfee", method = RequestMethod.GET)
    public String showSearchApprovedforFee(final Model model) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        model.addAttribute("mode", "collectmrfee");
        prepareSearchForm(model);
        return "registration-search-forfee";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(final Model model, @ModelAttribute final MarriageRegistration registration) {
        final List<MarriageRegistration> searchResultList = marriageRegistrationService.searchMarriageRegistrations(registration);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MarriageRegistration.class, MarriageRegistrationJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/searchApproved", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchRegisterStatusMarriageRecords(final Model model,
            @ModelAttribute final MarriageRegistration registration) {
        final List<MarriageRegistration> searchResultList = marriageRegistrationService.searchRegistrationByStatus(registration,
                MarriageRegistration.RegistrationStatus.REGISTERED.toString());
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MarriageRegistration.class, MarriageRegistrationJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/collectmrfeeajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchMarriageRegistrationsForFeeCollection(final Model model,
            @ModelAttribute final MarriageRegistration registration) {
        final List<MarriageRegistration> searchResultList = marriageRegistrationService
                .searchMarriageRegistrationsForFeeCollection(registration);
        final List<MarriageRegistration> newSearchResultList = new ArrayList<>();
        for (final MarriageRegistration mr : searchResultList)
            if (mr != null && !mr.isFeeCollected())
                newSearchResultList.add(mr);
        return new StringBuilder(DATA)
                .append(toJSON(newSearchResultList, MarriageRegistration.class, MarriageRegistrationJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/collectmrreissuefeeajaxsearch", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchApprovedMarriageReIssueRecordsForFee(final Model model,
            @ModelAttribute final MarriageRegistrationSearchFilter mrSearchFilter) {
        final List<ReIssue> searchResultList = marriageRegistrationService
                .searchApprovedReIssueRecordsForFeeCollection(mrSearchFilter);
        final List<ReIssue> newSearchResultList = new ArrayList<>();
        for (final ReIssue mrr : searchResultList)
            if (mrr != null && !mrr.isFeeCollected())
                newSearchResultList.add(mrr);
        return new StringBuilder(DATA).append(toJSON(newSearchResultList, ReIssue.class, MarriageReIssueJsonAdaptor.class))
                .append("}")
                .toString();
    }

    @RequestMapping(value = "/searchregisteredrecord", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchRegisteredStatusMarriageRecords(final Model model,
            @ModelAttribute final MarriageRegistration registration) {
        final List<MarriageRegistration> searchResultList = marriageRegistrationService.searchRegistrationByStatus(registration,
                MarriageRegistration.RegistrationStatus.REGISTERED.toString());
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MarriageRegistration.class, MarriageRegistrationJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/reissuecertificate", method = RequestMethod.GET)
    public String reissueCertificateSearch(final Model model, final HttpServletRequest request) {
        model.addAttribute(REGISTRATION, new MarriageRegistration());
        prepareSearchForm(model);
        return "registration-search-certificateissue";
    }

    @RequestMapping(value = "/searchcertificates", method = RequestMethod.GET)
    public String showReportForm(final Model model) {

        model.addAttribute("certificate", new MarriageCertificate());
        model.addAttribute("certificateType", MarriageCertificateType.values());
        return "registration-search-certificate";
    }

    @RequestMapping(value = "/searchcertificates", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchApprovedMarriageRecords(final Model model, @ModelAttribute final MarriageCertificate certificate) {
        final List<MarriageCertificate> searchResultList = marriageCertificateService.searchMarriageCertificates(certificate);
        int noOfToDaysToPrint = 0;
        final List<AppConfigValues> appConfigValues = appConfigValuesService
                .getConfigValuesByModuleAndKey(MODULE_NAME, NOOFDAYSTOPRINT);
        if (appConfigValues != null && appConfigValues.get(0).getValue() != null)
            noOfToDaysToPrint = Integer.parseInt(appConfigValues.get(0).getValue());

        for (final MarriageCertificate certficateobj : searchResultList)
            certficateobj.setPrintCertificateResrictionDays(noOfToDaysToPrint);
        return new StringBuilder(DATA)
                .append(toJSON(searchResultList, MarriageCertificate.class, MarriageCerftificateJsonAdaptor.class)).append("}")
                .toString();
    }

    @RequestMapping(value = "/printcertficate/{id}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> download(@PathVariable final long id) {
        final MarriageCertificate certificate = marriageCertificateService.findById(id);
        return fileStoreUtils.fileAsResponseEntity(certificate.getFileStore().getFileStoreId(), FILESTORE_MODULECODE, false);
    }
}