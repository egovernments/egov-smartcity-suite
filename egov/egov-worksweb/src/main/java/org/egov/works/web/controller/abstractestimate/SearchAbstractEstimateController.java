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
package org.egov.works.web.controller.abstractestimate;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.TypeOfWorkService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.OfflineStatusesForAbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimateForLoaSearchRequest;
import org.egov.works.abstractestimate.entity.SearchAbstractEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/abstractestimate")
public class SearchAbstractEstimateController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Autowired
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @RequestMapping(value = "/searchform", method = RequestMethod.GET)
    public String searchForm(@ModelAttribute final SearchAbstractEstimate searchAbstractEstimate, final Model model)
            throws ApplicationException {
        setDropDownValues(model);
        final List<EgwStatus> egwStatuses = egwStatusHibernateDAO.getStatusByModule(WorksConstants.ABSTRACTESTIMATE);
        final List<EgwStatus> newEgwStatuses = new ArrayList<EgwStatus>();
        for (final EgwStatus egwStatus : egwStatuses)
            if (!egwStatus.getCode().equalsIgnoreCase(OfflineStatusesForAbstractEstimate.L1_TENDER_FINALIZED.toString())
                    && !egwStatus.getCode()
                            .equalsIgnoreCase(OfflineStatusesForAbstractEstimate.COMMERCIAL_EVALUATION_DONE.toString())
                    && !egwStatus.getCode()
                            .equalsIgnoreCase(OfflineStatusesForAbstractEstimate.TECHNICAL_EVALUATION_DONE.toString())
                    && !egwStatus.getCode()
                            .equalsIgnoreCase(OfflineStatusesForAbstractEstimate.TENDER_DOCUMENT_RELEASED.toString())
                    && !egwStatus.getCode()
                            .equalsIgnoreCase(OfflineStatusesForAbstractEstimate.TENDER_OPENED.toString())
                    && !egwStatus.getCode().equalsIgnoreCase(
                            OfflineStatusesForAbstractEstimate.NOTICEINVITINGTENDERRELEASED.toString()))
                newEgwStatuses.add(egwStatus);
        model.addAttribute("abstractEstimateStatus", newEgwStatuses);
        model.addAttribute("searchAbstractEstimate", searchAbstractEstimate);
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());
        return "abstractestimate-search";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String viewAbstractEstimate(@PathVariable final String id, final Model model) throws ApplicationException {
        final AbstractEstimate abstractEstimate = estimateService.getAbstractEstimateById(Long.valueOf(id));
        final boolean lineEstimateRequired = worksApplicationProperties.lineEstimateRequired();

        getEstimateDocuments(abstractEstimate);
        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (WorksConstants.YES.equalsIgnoreCase(value.getValue()))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);
        final List<AppConfigValues> showDeductions = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_DEDUCTION_GRID);
        final AppConfigValues showDeduction = showDeductions.get(0);
        if (WorksConstants.YES.equalsIgnoreCase(showDeduction.getValue()))
            model.addAttribute("isEstimateDeductionGrid", true);
        else
            model.addAttribute("isEstimateDeductionGrid", false);
        model.addAttribute(WorksConstants.MODE, WorksConstants.VIEW);
        model.addAttribute("abstractEstimate", abstractEstimate);
        model.addAttribute("documentDetails", abstractEstimate.getDocumentDetails());
        model.addAttribute("workOrderEstimate",
                workOrderEstimateService.getWorkOrderEstimateByAbstractEstimateId(Long.valueOf(id)));
        if (lineEstimateRequired)
            model.addAttribute("paymentreleased",
                    estimateService.getPaymentsReleasedForLineEstimate(abstractEstimate.getLineEstimateDetails()));
        else
            model.addAttribute("paymentreleased",
                    estimateService.getPaymentsReleasedForAbstractEstimate(abstractEstimate));

        model.addAttribute("adminsanctionbydesignation",
                worksUtils.getUserDesignation(abstractEstimate.getApprovedBy()));
        model.addAttribute("measurementsPresent", measurementSheetService.existsByEstimate(abstractEstimate.getId()));

        String techSanctionBy = StringUtils.EMPTY;
        if (!abstractEstimate.getEstimateTechnicalSanctions().isEmpty())
            techSanctionBy = worksUtils.getUserDesignation(abstractEstimate.getEstimateTechnicalSanctions()
                    .get(abstractEstimate.getEstimateTechnicalSanctions().size() - 1).getTechnicalSanctionBy());
        model.addAttribute("technicalsanctionbydesignation", techSanctionBy);
        model.addAttribute("workflowHistory",
                worksUtils.getHistory(abstractEstimate.getState(), abstractEstimate.getStateHistory()));
        model.addAttribute("lineEstimateRequired", lineEstimateRequired);

        return "abstractestimate-view";
    }

    @RequestMapping(value = "/searchabstractestimateforloa-form", method = RequestMethod.GET)
    public String searchAbstractEstimateForLOA(
            @ModelAttribute final AbstractEstimateForLoaSearchRequest abstractEstimateForLoaSearchRequest,
            final Model model) {
        setDropDownValues(model);
        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        final List<Long> departmentIds = new ArrayList<Long>();
        if (departments != null)
            for (final Department department : departments)
                departmentIds.add(department.getId());
        final List<User> abstractEstimateCreatedByUsers = estimateService
                .getAbstractEstimateCreatedByUsers(departmentIds);
        model.addAttribute("abstractEstimateForLoaSearchRequest", abstractEstimateForLoaSearchRequest);
        model.addAttribute("abstractEstimateCreatedByUsers", abstractEstimateCreatedByUsers);
        model.addAttribute("departments", departments);

        final List<EgwStatus> egwStatuses = egwStatusHibernateDAO.getStatusByModule(WorksConstants.ABSTRACTESTIMATE);
        final List<EgwStatus> newEgwStatuses = new ArrayList<EgwStatus>();
        for (final EgwStatus egwStatus : egwStatuses)
            if (egwStatus.getCode().equalsIgnoreCase(WorksConstants.APPROVED) || egwStatus.getCode()
                    .equalsIgnoreCase(OfflineStatusesForAbstractEstimate.L1_TENDER_FINALIZED.toString()))
                newEgwStatuses.add(egwStatus);
        model.addAttribute("egwStatus", newEgwStatuses);
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());

        return "searchAbstractEstimateForLoa-search";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("createdUsers", estimateService.getAbstractEstimateCreatedByUsers());
        model.addAttribute("abstractEstimateStatus", worksUtils.getStatusByModule(WorksConstants.ABSTRACTESTIMATE));

    }

    private void getEstimateDocuments(final AbstractEstimate abstractEstimate) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(abstractEstimate.getId(),
                WorksConstants.ABSTRACTESTIMATE);
        abstractEstimate.setDocumentDetails(documentDetailsList);
    }

    @RequestMapping(value = "/searchabstractestimateforofflinestatus-form", method = RequestMethod.GET)
    public String searchAbstractEstimateToSetOfflineStatus(
            @ModelAttribute final AbstractEstimateForLoaSearchRequest abstractEstimateForLoaSearchRequest,
            final Model model) {
        setDropDownValues(model);
        final List<EgwStatus> egwStatuses = egwStatusHibernateDAO.getStatusByModule(WorksConstants.ABSTRACTESTIMATE);
        final List<EgwStatus> newEgwStatuses = new ArrayList<EgwStatus>();
        for (final EgwStatus egwStatus : egwStatuses)
            if (!egwStatus.getCode().equalsIgnoreCase(WorksConstants.NEW)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.REJECTED)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.CANCELLED)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.CHECKED_STATUS.toString())
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.CREATED_STATUS)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.RESUBMITTED_STATUS))
                newEgwStatuses.add(egwStatus);
        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        final List<Long> departmentIds = new ArrayList<Long>();
        if (departments != null)
            for (final Department department : departments)
                departmentIds.add(department.getId());
        final List<User> abstractEstimateCreatedByUsers = estimateService
                .getAbstractEstimateCreatedByUsers(departmentIds);
        model.addAttribute("egwStatus", newEgwStatuses);
        model.addAttribute("abstractEstimateForLoaSearchRequest", abstractEstimateForLoaSearchRequest);
        model.addAttribute("abstractEstimateCreatedByUsers", abstractEstimateCreatedByUsers);
        return "searchAbstractEstimateForOfflineStatus-search";
    }

    @RequestMapping(value = "/searchestimateform", method = RequestMethod.GET)
    public String showSearchEstimateForm(@RequestParam("typeOfWork") final Long typeOfWorkId, final Model model) {
        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        final List<Long> departmentIds = new ArrayList<Long>();
        if (departments != null)
            for (final Department department : departments)
                departmentIds.add(department.getId());
        final List<User> abstractEstimateCreatedByUsers = estimateService
                .getAbstractEstimateCreatedByUsers(departmentIds);
        model.addAttribute("typeOfWorkId", typeOfWorkId);
        model.addAttribute("typeOfWork",
                typeOfWorkService.getTypeOfWorkByPartyType(WorksConstants.PARTY_TYPE_CONTRACTOR));
        model.addAttribute("abstractEstimateCreatedByUsers", abstractEstimateCreatedByUsers);
        return "estimate-searchform";
    }
}