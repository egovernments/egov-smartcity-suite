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
package org.egov.works.web.controller.letterofacceptance;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.TypeOfWorkService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptanceForRE;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/searchletterofacceptance")
public class LetterOfAcceptanceSearchController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private TypeOfWorkService typeOfWorkService;
    
    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @RequestMapping(value = "/searchform", method = RequestMethod.GET)
    public String showSearchLineEstimateForm(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model)
            throws ApplicationException {
        setDropDownValues(model);
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        final List<EgwStatus> egwStatuses = egwStatusHibernateDAO.getStatusByModule(WorksConstants.WORKORDER);
        final List<EgwStatus> newEgwStatuses = new ArrayList<EgwStatus>();
        for (final EgwStatus egwStatus : egwStatuses)
            if (!egwStatus.getCode().equalsIgnoreCase(OfflineStatuses.ACCEPTANCE_LETTER_ACKNOWLEDGED.toString())
                    && !egwStatus.getCode().equalsIgnoreCase(OfflineStatuses.ACCEPTANCE_LETTER_ISSUED.toString())
                    && !egwStatus.getCode().equalsIgnoreCase(OfflineStatuses.AGREEMENT_ORDER_SIGNED.toString())
                    && !egwStatus.getCode().equalsIgnoreCase(OfflineStatuses.WORK_ORDER_ACKNOWLEDGED.toString())
                    && !egwStatus.getCode().equalsIgnoreCase(OfflineStatuses.WORK_COMMENCED.toString())
                    && !egwStatus.getCode().equalsIgnoreCase(OfflineStatuses.SITE_HANDED_OVER.toString()))
                newEgwStatuses.add(egwStatus);
        model.addAttribute("egwStatus", newEgwStatuses);
        return "searchletterofacceptance-search";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("egwStatus", egwStatusHibernateDAO.getStatusByModule(WorksConstants.WORKORDER));
        model.addAttribute("typeOfWork",
                typeOfWorkService.getActiveTypeOfWorksByPartyType(WorksConstants.PARTY_TYPE_CONTRACTOR));

    }

    @RequestMapping(value = "/searchloa-milestone", method = RequestMethod.GET)
    public String searchMilestone(@ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance,
            final Model model) throws ApplicationException {
        setDropDownValues(model);
        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestLetterOfAcceptance.setDepartmentName(departments.get(0).getId());
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());
        return "search-searchmilestone";
    }

    @RequestMapping(value = "/searchformloa-contractorbill", method = RequestMethod.GET)
    public String showSearchLOAForContractorBill(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model)
            throws ApplicationException {
        setDropDownValues(model);
        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestLetterOfAcceptance.setDepartmentName(departments.get(0).getId());
        model.addAttribute("departments", departments);
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        return "searchloatocreatecontractorbill-search";
    }

    @RequestMapping(value = "/searchmodifyform", method = RequestMethod.GET)
    public String showSearchLOAModifyForm(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model)
            throws ApplicationException {
        setDropDownValues(model);
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        return "letterofacceptancetomodify-search";
    }

    @RequestMapping(value = "/setloaofflinestatus", method = RequestMethod.GET)
    public String showLOAToSetOfflineStatus(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model)
            throws ApplicationException {
        final List<EgwStatus> egwStatuses = egwStatusHibernateDAO.getStatusByModule(WorksConstants.WORKORDER);
        setDropDownValues(model);
        final List<EgwStatus> newEgwStatuses = new ArrayList<EgwStatus>();
        for (final EgwStatus egwStatus : egwStatuses)
            if (!egwStatus.getCode().equalsIgnoreCase(WorksConstants.CREATED_STATUS)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.REJECTED)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.CANCELLED)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.CHECKED_STATUS)
                    && !egwStatus.getCode().equalsIgnoreCase(WorksConstants.RESUBMITTED_STATUS))
                newEgwStatuses.add(egwStatus);

        model.addAttribute("egwStatus", newEgwStatuses);
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());
        return "setofflinestatus-search";
    }

    @RequestMapping(value = "/searchloare-form", method = RequestMethod.GET)
    public String showSearchLOAForREForm(
            @ModelAttribute final SearchRequestLetterOfAcceptanceForRE searchRequestLetterOfAcceptanceForRE,
            final Model model) throws ApplicationException {
        model.addAttribute("searchRequestLetterOfAcceptanceForRE", searchRequestLetterOfAcceptanceForRE);
        model.addAttribute("workAssignedUsers", letterOfAcceptanceService.getWorkAssignedUsers());
        return "loatocreatere-search";
    }

    @RequestMapping(value = "/searchloacr-form", method = RequestMethod.GET)
    public String searchLOAForContractorRequisition(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model)
            throws ApplicationException {
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        model.addAttribute("workAssignedUsers", letterOfAcceptanceService.getWorkAssignedUsers());
        return "loatocreatecr-search";
    }

}
