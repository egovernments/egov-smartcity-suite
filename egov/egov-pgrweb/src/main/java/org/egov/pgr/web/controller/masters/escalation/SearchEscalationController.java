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

package org.egov.pgr.web.controller.masters.escalation;

import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.EscalationHierarchy;
import org.egov.pgr.entity.contract.EscalationRequest;
import org.egov.pgr.service.ComplaintEscalationService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationHierarchyService;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/complaint/escalation/search")
public class SearchEscalationController {

    private static final String ESCALATIONSEARCHVIEW = "escalation-search";
    private static final String MESSAGE = "message";

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EscalationHierarchyService escalationHierarchyService;

    @Autowired
    private ComplaintEscalationService complaintEscalationService;

    @ModelAttribute
    public EscalationRequest escalationRequest() {
        return new EscalationRequest();
    }

    @ModelAttribute("complaintTypes")
    public List<ComplaintType> complaintTypes() {
        return complaintTypeService.findActiveComplaintTypes();
    }

    @ModelAttribute("positionMasterList")
    public List<Position> positionMasterList() {
        return positionMasterService.getAllPositions();
    }

    @GetMapping
    public String searchEscalationForm() {
        return ESCALATIONSEARCHVIEW;
    }

    @PostMapping
    public String searchForm(EscalationRequest escalationRequest, Model model) {

        if (escalationRequest.getPosition() != null) {
            List<EscalationHierarchy> positionHeirarchyList = escalationHierarchyService
                    .getHeirarchyByFromPosition(escalationRequest.getPosition().getId());
            if (!positionHeirarchyList.isEmpty()) {
                escalationRequest.setEscalationHierarchyList(positionHeirarchyList);
                model.addAttribute("mode", "dataFound");
            } else {
                positionHeirarchyList = new ArrayList<>();
                EscalationHierarchy escalationHierarchy = new EscalationHierarchy();
                escalationHierarchy.setFromPosition(positionMasterService.getPositionById(escalationRequest.getPosition().getId()));
                positionHeirarchyList.add(escalationHierarchy);
                escalationRequest.setEscalationHierarchyList(positionHeirarchyList);
                model.addAttribute("mode", "noDataFound");
            }
        }
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        return ESCALATIONSEARCHVIEW;
    }

    @PostMapping("update/{id}")
    public String saveEscalationForm(@PathVariable Long id, EscalationRequest escalationRequest,
                                     Model model, RedirectAttributes redirectAttrs) {
        if (id == null) {
            model.addAttribute("warning", "escalation.pos.required");
            return ESCALATIONSEARCHVIEW;
        } else {
            complaintEscalationService.updateEscalation(id, escalationRequest);
            redirectAttrs.addFlashAttribute("positionName", escalationRequest.getPosition().getName());
            redirectAttrs.addFlashAttribute(MESSAGE, "msg.escaltion.success");
            return "redirect:/complaint/escalation/search";
        }
    }
}
