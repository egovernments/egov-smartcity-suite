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
package org.egov.works.web.controller.revisionestimate;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.service.UOMService;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.service.RevisionEstimateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/revisionestimate")
public class CreateRevisionEstimateController extends GenericWorkFlowController {

    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private UOMService uomService;
    
    @Autowired
    private RevisionEstimateService revisionEstimateService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showAbstractEstimateForm(@ModelAttribute("revisionEstimate") final RevisionAbstractEstimate revisionEstimate,
            @RequestParam final Long workOrderEstimateId, final Model model) {
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateById(workOrderEstimateId);
        revisionEstimate.setParent(workOrderEstimate.getEstimate());
        
        final List<RevisionAbstractEstimate> revisionAbstractEstimates = revisionEstimateService.findApprovedRevisionEstimatesByParent(workOrderEstimate.getEstimate().getId());
        
        for (RevisionAbstractEstimate re : revisionAbstractEstimates) {
            
        }

        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);
        model.addAttribute("uoms", uomService.findAll());
        model.addAttribute("revisionEstimate", revisionEstimate);
        model.addAttribute("exceptionaluoms", worksUtils.getExceptionalUOMS());
        model.addAttribute("workOrderDate", sdf.format(workOrderEstimate.getWorkOrder().getWorkOrderDate()));
        model.addAttribute("workOrderEstimate", workOrderEstimate);
        model.addAttribute("scheduleCategories", scheduleCategoryService.getAllScheduleCategories());
        revisionEstimate.setParent(workOrderEstimate.getEstimate());;
        model.addAttribute("workOrder", workOrderEstimate.getWorkOrder());
        return "revisionEstimate-form";
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String saveRevisionEstimate(@ModelAttribute final RevisionAbstractEstimate revisionEstimate,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult bindErrors,
            final HttpServletRequest request) {
        
        final RevisionAbstractEstimate savedRevisionEstimate = revisionEstimateService.createRevisionEstimate(revisionEstimate);
        
        return "redirect:/revisionestimate/revisionestimate-success?revisionEstimate=" + savedRevisionEstimate.getId();
    }
    
    @RequestMapping(value = "/revisionestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute RevisionAbstractEstimate revisionEstimate,
            @RequestParam("revisionEstimate") Long id,
            final HttpServletRequest request, final Model model) {

        if (id != null)
            revisionEstimate = revisionEstimateService.getRevisionEstimateById(id);

        model.addAttribute("message", "Revision Estimate Save successfully");

        return new ModelAndView("revisionEstimate-success", "revisionEstimate", revisionEstimate);
    }
}
