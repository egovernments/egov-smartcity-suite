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
package org.egov.works.web.controller.milestone;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.milestone.entity.SearchRequestMilestone;
import org.egov.works.milestone.entity.enums.MilestoneActivityStatus;
import org.egov.works.models.masters.MilestoneTemplate;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/milestone")
public class SearchMilestoneController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;

    @RequestMapping(value = "/search-form", method = RequestMethod.GET)
    public String showSearchMilestoneForm(
            @ModelAttribute final SearchRequestMilestone searchRequestMilestone,
            final Model model) throws ApplicationException {
        setDropDownValues(model);
        final List<Department> departments = lineEstimateService.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestMilestone.setDepartment(departments.get(0).getId());
        model.addAttribute("currentStatus", MilestoneActivityStatus.values());
        model.addAttribute("searchRequestMilestone", searchRequestMilestone);
        return "searchmilestone-form";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getTypeOfWorkForPartyTypeContractor());
    }

    @RequestMapping(value = "/searchmilestonetemplate", method = RequestMethod.GET)
    public String showSearchMilestoneTemplate(
            @ModelAttribute final MilestoneTemplate milestoneTemplate,
            final Model model) throws ApplicationException {
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getTypeOfWorkForPartyTypeContractor());
        return "milestoneTemplate-search";
    }

    @RequestMapping(value = "/searchtoview-form", method = RequestMethod.GET)
    public String searchMilestoneForm(
            @ModelAttribute final SearchRequestMilestone searchRequestMilestone,
            final Model model) throws ApplicationException {
        setDropDownValues(model);
        final List<Department> departments = lineEstimateService.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestMilestone.setDepartment(departments.get(0).getId());
        model.addAttribute("searchRequestMilestone", searchRequestMilestone);
        model.addAttribute("egwStatus", egwStatusDAO.getStatusByModule(WorksConstants.MILESTONE_MODULE_KEY));
        return "viewMilestone-form";
    }

    @RequestMapping(value = "/searchtracked-form", method = RequestMethod.GET)
    public String searchTrackedMilestoneForm(
            @ModelAttribute final SearchRequestMilestone searchRequestMilestone,
            final Model model) throws ApplicationException {
        setDropDownValues(model);
        final List<Department> departments = lineEstimateService.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestMilestone.setDepartment(departments.get(0).getId());
        model.addAttribute("searchRequestMilestone", searchRequestMilestone);
        model.addAttribute("egwStatus", egwStatusDAO.getStatusByModule(WorksConstants.MILESTONE_MODULE_KEY));
        return "searchTrackMilestone-form";
    }

}
