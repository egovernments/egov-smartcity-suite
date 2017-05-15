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

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import org.egov.eis.service.DesignationService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.dto.EscalationTimeSearchRequest;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/escalationTime")
public class ViewEscalationTimeController {
    public static final String CONTENTTYPE_JSON = "application/json";

    protected ComplaintTypeService complaintTypeService;
    protected EscalationService escalationService;
    protected DesignationService designationService;

    @Autowired
    public ViewEscalationTimeController(
            final ComplaintTypeService complaintTypeService,
            EscalationService escalationService,
            DesignationService designationService) {
        this.complaintTypeService = complaintTypeService;
        this.escalationService = escalationService;
        this.designationService = designationService;
    }

    @ModelAttribute
    public Escalation escalation() {
        return new Escalation();
    }

    @GetMapping("/search")
    public String searchForm(@ModelAttribute Escalation escalation,
            final Model model) {
        model.addAttribute("mode", "new");
        return "escalationTime-search";
    }

    @PostMapping("/search")
    public String searchEscalationTimeForm(
            @ModelAttribute Escalation escalation, final Model model) {
        model.addAttribute("mode", "new");
        return "escalationTime-search";
    }

    @GetMapping(value = "resultList-update", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(EscalationTimeSearchRequest escalationTimeSearchRequest) {
        return new DataTable<>(escalationService.getEscalationsTime(escalationTimeSearchRequest),
                escalationTimeSearchRequest.draw()).toJson(EscalationTimeAdaptor.class);
    }
}
