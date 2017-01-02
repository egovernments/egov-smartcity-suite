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

package org.egov.pgr.web.controller.masters.escalation;

import org.apache.commons.io.IOUtils;
import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/bulkEscalation")
public class BulkEscalationController {

    private final EscalationService escalationService;
    private final ComplaintTypeService complaintTypeService;
    private final ObjectTypeService objectTypeService;
    private final PositionHierarchyService positionHierarchyService;

    @Autowired
    public BulkEscalationController(final EscalationService escalationService, final ComplaintTypeService complaintTypeService,
            final ObjectTypeService objectTypeService, final PositionHierarchyService positionHierarchyService) {
        this.escalationService = escalationService;
        this.complaintTypeService = complaintTypeService;
        this.objectTypeService = objectTypeService;
        this.positionHierarchyService = positionHierarchyService;

    }

    @ModelAttribute("complainttypes")
    public List<ComplaintType> complaintTypes() {
        return complaintTypeService.findActiveComplaintTypes();
    }

    @ModelAttribute
    public BulkEscalationGenerator bulkEscalationGenerator() {
        return new BulkEscalationGenerator();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String newform() {
        return "bulkEscalation-new";
    }

    @RequestMapping(value = "/search-result", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void search(final Model model, final HttpServletRequest request,
            @ModelAttribute final BulkEscalationGenerator bulkEscalationGenerator, final HttpServletResponse response)
            throws IOException {
        final List<EscalationHelper> escalationHelperList = new ArrayList<>();
        final List<PositionHierarchy> escalationRecords = escalationService
                .getEscalationObjByComplaintTypeFromPosition(bulkEscalationGenerator.getComplaintTypes(),
                        bulkEscalationGenerator.getFromPosition());
        for (final PositionHierarchy posHir : escalationRecords) {
            final EscalationHelper escalationHelper = new EscalationHelper();
            if (posHir.getObjectSubType() != null)
                escalationHelper.setComplaintType(complaintTypeService.findByCode(posHir.getObjectSubType()));

            escalationHelper.setFromPosition(posHir.getFromPosition());
            escalationHelper.setToPosition(posHir.getToPosition());
            escalationHelperList.add(escalationHelper);
        }
        final String escalationJSONData = new StringBuilder("{ \"data\":").append(toJSON(escalationHelperList, EscalationHelper.class, EscalationHelperAdaptor.class)).append("}")
                .toString();
        IOUtils.write(escalationJSONData, response.getWriter());

    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute final BulkEscalationGenerator bulkEscalationGenerator, final BindingResult errors,
            final RedirectAttributes redirectAttrs, final Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "bulkescalation.unble.to.save");
            return "bulkEscalation-new";
        } else {
            for (final ComplaintType complaintType : bulkEscalationGenerator.getComplaintTypes()) {
                final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
                final PositionHierarchy positionHierarchy = new PositionHierarchy();
                positionHierarchy.setObjectType(objectType);
                positionHierarchy.setObjectSubType(complaintType.getCode());
                positionHierarchy.setFromPosition(bulkEscalationGenerator.getFromPosition());
                positionHierarchy.setToPosition(bulkEscalationGenerator.getToPosition());
                final PositionHierarchy existingPosHierarchy = escalationService.getExistingEscalation(positionHierarchy);
                if (existingPosHierarchy != null) {
                    existingPosHierarchy.setToPosition(bulkEscalationGenerator.getToPosition());
                    positionHierarchyService.updatePositionHierarchy(existingPosHierarchy);
                } else
                    positionHierarchyService.createPositionHierarchy(positionHierarchy);
            }
            redirectAttrs.addFlashAttribute("message", "msg.bulkescalation.success");
            return "redirect:/bulkEscalation/search";
        }
    }
}
