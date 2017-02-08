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
package org.egov.works.web.controller.mb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/workorder/searchform")
public class SearchWorkOrderForMBHeaderController extends GenericWorkFlowController {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private CityService cityService;

    @RequestMapping(method = RequestMethod.GET)
    public String showSearchWorkOrder(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model) {

        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestLetterOfAcceptance.setDepartmentName(departments.get(0).getId());
        searchRequestLetterOfAcceptance.setEgwStatus(WorksConstants.WO_STATUS_WOCOMMENCED.toUpperCase());
        model.addAttribute("departments", worksUtils.getUserDepartments(securityUtils.getCurrentUser()));
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        final MBHeader mbHeader = new MBHeader();
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAmountRule(mbHeader.getMbAmount() != null
                ? mbHeader.getMbAmount() : BigDecimal.ZERO);
        workflowContainer.setAdditionalRule(
                (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        prepareWorkflow(model, mbHeader, workflowContainer);
        final List<String> validActions = new ArrayList<String>();
        validActions.add(WorksConstants.CREATE_AND_APPROVE);
        validActions.add(WorksConstants.SAVE_ACTION);
        validActions.add(WorksConstants.FORWARD_ACTION.toString());
        validActions.add(WorksConstants.CONTRACTOR_MEASUREMENTS);
        model.addAttribute("stateType", mbHeader.getClass().getSimpleName());
        if (mbHeader.getState() != null && mbHeader.getState().getNextAction() != null) {
            model.addAttribute("nextAction", mbHeader.getState().getNextAction());
            model.addAttribute("pendingActions", mbHeader.getState().getNextAction());
        }
        model.addAttribute("validActionList", validActions);
        model.addAttribute("currentDate", new Date());
        model.addAttribute("mbHeader", mbHeader);
        model.addAttribute("documentDetails", mbHeader.getDocumentDetails());
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        model.addAttribute("amountRule", mbHeader.getMbAmount());
        model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        return "workorder-search";
    }

}
