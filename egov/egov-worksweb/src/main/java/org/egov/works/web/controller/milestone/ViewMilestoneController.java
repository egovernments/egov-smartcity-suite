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

import org.egov.infra.exception.ApplicationException;
import org.egov.works.master.service.MilestoneTemplateActivityService;
import org.egov.works.master.service.MilestoneTemplateService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.service.MilestoneService;
import org.egov.works.milestone.service.TrackMilestoneService;
import org.egov.works.models.masters.MilestoneTemplate;
import org.egov.works.models.masters.MilestoneTemplateActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/milestone")
public class ViewMilestoneController {

    @Autowired
    private MilestoneTemplateService milestoneTemplateService;

    @Autowired
    private MilestoneTemplateActivityService milestoneTemplateActivityService;
    
    @Autowired
    private TrackMilestoneService trackMilestoneService;
    
    @Autowired
    private MilestoneService milestoneService; 

    @RequestMapping(value = "/viewmilestonetemplate/{id}", method = RequestMethod.GET)
    public String viewMilestoneTemplate(@PathVariable final String id, final Model model,
            final HttpServletRequest request)
                    throws ApplicationException {
        final MilestoneTemplate milestoneTemplate = milestoneTemplateService.getMilestoneTemplateById(Long.parseLong(id));
        model.addAttribute("milestoneTemplate", milestoneTemplate);
        model.addAttribute("mode", "view");
        return "milestoneTemplate-view";
    }

    @RequestMapping(value = "/setmilestonetemplateactivities/{id}", method = RequestMethod.GET)
    public @ResponseBody List<MilestoneTemplateActivity> populateMilestoneTemplateActivity(@PathVariable final String id,
            final Model model,
            final HttpServletRequest request)
                    throws ApplicationException {
        final List<MilestoneTemplateActivity> milestoneTemplateActivities = milestoneTemplateActivityService
                .getMilestoneTemplateActivityByMilestoneTemplate(Long.parseLong(id));
        return milestoneTemplateActivities;
    }
    
    @RequestMapping(value = "/viewmilestone/{id}", method = RequestMethod.GET)
    public String viewMilestone(@PathVariable final String id, final Model model,
            final HttpServletRequest request)
                    throws ApplicationException {
        final Milestone milestone = milestoneService.getMilestoneById(Long.parseLong(id));
        model.addAttribute("milestone", milestone);
        return "milestone-view";
    }
    
    @RequestMapping(value = "/viewtrackmilestone/{id}", method = RequestMethod.GET)
    public String viewTrackMilestone(@PathVariable final String id, final Model model,
            final HttpServletRequest request)
                    throws ApplicationException {
        final TrackMilestone trackMilestone = trackMilestoneService.getTrackMilestoneByMilestoneId(Long.parseLong(id));
        model.addAttribute("trackMilestone", trackMilestone);
        return "trackmilestone-view";
    }

}
