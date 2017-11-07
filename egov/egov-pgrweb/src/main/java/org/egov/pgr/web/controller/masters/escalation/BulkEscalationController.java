/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller.masters.escalation;

import org.egov.eis.entity.PositionHierarchy;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.contract.BulkEscalationGenerator;
import org.egov.pgr.entity.contract.EscalationHelper;
import org.egov.pgr.entity.contract.EscalationHelperAdaptor;
import org.egov.pgr.service.ComplaintEscalationService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;

@Controller
@RequestMapping("/complaint/bulkescalation")
public class BulkEscalationController {

    @Autowired
    private ComplaintEscalationService escalationService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private ComplaintEscalationService complaintEscalationService;

    @ModelAttribute("complainttypes")
    public List<ComplaintType> complaintTypes() {
        return complaintTypeService.findActiveComplaintTypes();
    }

    @ModelAttribute
    public BulkEscalationGenerator bulkEscalationGenerator() {
        return new BulkEscalationGenerator();
    }

    @GetMapping
    public String bulkEscalationForm() {
        return "bulkescalation";
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchBulkEscalation(BulkEscalationGenerator bulkEscalationGenerator) {
        List<PositionHierarchy> positionHierarchies = escalationService.getEscalationObjByComplaintTypeFromPosition(
                bulkEscalationGenerator.getComplaintTypes(), bulkEscalationGenerator.getFromPosition());
        return new StringBuilder("{ \"data\":").append(toJSON(complaintEscalationService.getEscalationDetailByPositionHierarchy(positionHierarchies),
                EscalationHelper.class, EscalationHelperAdaptor.class)).append("}").toString();
    }

    @PostMapping("update")
    public String updateBulkEscalation(@Valid BulkEscalationGenerator bulkEscalationGenerator,
                                       BindingResult errors, RedirectAttributes redirectAttrs, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "bulkescalation.unble.to.save");
            return "bulkescalation";
        } else {
            complaintEscalationService.updateBulkEscalation(bulkEscalationGenerator);
            redirectAttrs.addFlashAttribute("message", "msg.bulkescalation.success");
            return "redirect:/complaint/bulkescalation";
        }
    }
}
