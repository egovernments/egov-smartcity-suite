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

package org.egov.pgr.web.controller.masters.escalationTime;

import org.egov.eis.service.DesignationService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.service.ComplaintEscalationService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pims.commons.Designation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/escalationTime")
public class SearchEscalationTimeController {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private ComplaintEscalationService escalationService;

    @Autowired
    private DesignationService designationService;

    @ModelAttribute
    public EscalationForm escalationForm() {
        return new EscalationForm();
    }

    @PostMapping("search-view")
    public String searchEscalationTimeForm(@ModelAttribute EscalationForm escalationForm, Model model) {
        if (escalationForm.getComplaintType() != null) {
            List<Escalation> escalationList = escalationService
                    .findAllBycomplaintTypeId(escalationForm.getComplaintType()
                            .getId());

            if (escalationList.size() > 0) {
                escalationForm.setEscalationList((escalationList));
                model.addAttribute("mode", "dataFound");
            } else {
                escalationForm.getEscalationList().clear();
                escalationForm.addEscalationList(new Escalation());
                model.addAttribute("mode", "noDataFound");
            }
        }
        return "escalationTime-searchView";
    }

    @GetMapping("search-view")
    public String searchForm(@ModelAttribute EscalationForm escalationForm, Model model) {
        if (escalationForm.getComplaintType() != null) {
            List<Escalation> escalationList = escalationService
                    .findAllBycomplaintTypeId(escalationForm.getComplaintType()
                            .getId());

            if (escalationList.size() > 0) {
                escalationForm.setEscalationList((escalationList));

            } else {
                escalationForm.addEscalationList(new Escalation());
                model.addAttribute("mode", "noDataFound");
            }
        } else {
            escalationForm.addEscalationList(new Escalation());
        }
        return "escalationTime-searchView";
    }

    @GetMapping("save-escalationTime")
    public String saveEscalationTimeForm(@ModelAttribute EscalationForm escalationForm) {
        return "escalationTime-searchView";
    }

    @PostMapping("save-escalationTime")
    public String saveEscalationTime(@ModelAttribute EscalationForm escalationForm, RedirectAttributes redirectAttrs) {

        ComplaintType compType = null;
        if (escalationForm.getComplaintType() != null
                && escalationForm.getComplaintType().getId() != null) {
            compType = complaintTypeService.findBy(escalationForm
                    .getComplaintType().getId());

            List<Escalation> escalationList = escalationService
                    .findAllBycomplaintTypeId(escalationForm.getComplaintType()
                            .getId());

            if (escalationList != null && escalationList.size() > 0)
                escalationService.deleteAllInBatch(escalationList);
        }
        if (compType != null && escalationForm.getEscalationList() != null
                && escalationForm.getEscalationList().size() > 0) {
            for (Escalation escalation : escalationForm.getEscalationList()) {
                if (escalation.getDesignation().getId() != null) {
                    Designation desig = designationService
                            .getDesignationById(escalation.getDesignation()
                                    .getId());
                    escalation.setComplaintType(compType);
                    escalation.setDesignation(desig);
                    escalation.setNoOfHrs(escalation.getNoOfHrs());
                    escalationService.create(escalation);
                }
            }

        }
        redirectAttrs.addFlashAttribute("message", "msg.escalate.time.success");
        return "escalationTime-searchView";
    }

}
