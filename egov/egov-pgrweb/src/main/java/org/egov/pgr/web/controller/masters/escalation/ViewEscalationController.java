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

import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.egov.pgr.web.controller.masters.escalationTime.EscalationForm;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/escalation")
public class ViewEscalationController {
    private static final String ESCALATIONSEARCHVIEW = "escalation-searchView";
    private static final String ESCALATIONFORM = "escalationForm";
    private static final String MESSAGE = "message";

    protected final ComplaintTypeService complaintTypeService;
    private final PositionMasterService positionMasterService;

    private final ObjectTypeService objectTypeService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PositionHierarchyService positionHierarchyService;

    @Autowired
    public ViewEscalationController(final ComplaintTypeService complaintTypeService,
                                    final PositionMasterService positionMasterService, final ObjectTypeService objectTypeService) {
        this.complaintTypeService = complaintTypeService;
        this.positionMasterService = positionMasterService;
        this.objectTypeService = objectTypeService;
    }

    @ModelAttribute
    public EscalationForm escalationForm() {
        return new EscalationForm();
    }

    @ModelAttribute("complaintTypes")
    public List<ComplaintType> complaintTypes() {
        return complaintTypeService.findActiveComplaintTypes();
    }

    @ModelAttribute("positionMasterList")
    public List<Position> positionMasterList() {
        return positionMasterService.getAllPositions();
    }

    @RequestMapping(value = "/search-view", method = GET)
    public String searchEscalationForm(@ModelAttribute final EscalationForm escalationForm, final Model model) {
        if (escalationForm.getPosition() != null) {
            final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);

            final List<PositionHierarchy> positionHeirarchyList = positionHierarchyService
                    .getPositionHeirarchyByFromPositionAndObjectType(escalationForm.getPosition().getId(),
                            objectType.getId());
            if (positionHeirarchyList.size() > 0)
                escalationForm.setPositionHierarchyList(positionHeirarchyList);
            else {
                escalationForm.addPositionHierarchyList(new PositionHierarchy());
                model.addAttribute("mode", "noDataFound");
            }
        } else
            escalationForm.addPositionHierarchyList(new PositionHierarchy());
        return ESCALATIONSEARCHVIEW;
    }

    @RequestMapping(value = "/search-view", method = RequestMethod.POST)
    public String searchForm(@ModelAttribute final EscalationForm escalationForm,
                             final RedirectAttributes redirectAttrs, final Model model) {
        if (escalationForm.getPosition() != null && escalationForm.getPosition().getId() != null) {
            final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);

            List<PositionHierarchy> positionHeirarchyList = positionHierarchyService
                    .getPositionHeirarchyByFromPositionAndObjectType(escalationForm.getPosition().getId(),
                            objectType.getId());
            if (positionHeirarchyList.size() > 0) {
                escalationForm.setPositionHierarchyList(positionHeirarchyList);
                model.addAttribute("mode", "dataFound");

            } else {
                positionHeirarchyList = new ArrayList<>();
                final PositionHierarchy posHierarchy = new PositionHierarchy();
                posHierarchy.setFromPosition(positionMasterService
                        .getPositionById(escalationForm.getPosition().getId()));
                posHierarchy.setObjectType(objectType);
                posHierarchy.setObjectSubType("");
                positionHeirarchyList.add(posHierarchy);
                escalationForm.setPositionHierarchyList(positionHeirarchyList);
                model.addAttribute("mode", "noDataFound");
                model.addAttribute(ESCALATIONFORM, escalationForm);
            }
        } else {
            final String message = "Position is mandatory. Please enter correct position name.";
            redirectAttrs.addFlashAttribute(ESCALATIONFORM, escalationForm);
            model.addAttribute(MESSAGE, message);

        }
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        return ESCALATIONSEARCHVIEW;
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public String saveEscalationForm(@ModelAttribute final EscalationForm escalationForm, final Model model,
                                     final BindingResult errors, final RedirectAttributes redirectAttrs, @PathVariable final Long id) {

        if (id == null) {
            redirectAttrs.addFlashAttribute(ESCALATIONFORM, escalationForm);
            model.addAttribute(MESSAGE, "escalation.pos.required");
            return ESCALATIONSEARCHVIEW;
        }
        final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
        final List<PositionHierarchy> existingPosHierarchy = positionHierarchyService
                .getPositionHeirarchyByFromPositionAndObjectType(id, objectType.getId());

        if (existingPosHierarchy != null && existingPosHierarchy.size() > 0)
            positionHierarchyService.deleteAllInBatch(existingPosHierarchy);
        for (final PositionHierarchy posHierarchy : escalationForm.getPositionHierarchyList())
            if (posHierarchy.getFromPosition() != null && posHierarchy.getFromPosition().getId() != null
                    && posHierarchy.getToPosition() != null && posHierarchy.getToPosition().getId() != null) {

                posHierarchy.setFromPosition(positionMasterService.getPositionById(posHierarchy.getFromPosition()
                        .getId()));
                posHierarchy.setToPosition(positionMasterService.getPositionById(posHierarchy.getToPosition().getId()));
                posHierarchy.setObjectType(objectType);
                posHierarchy.setObjectSubType(posHierarchy.getObjectSubType());
                positionHierarchyService.createPositionHierarchy(posHierarchy);
            } else {
                redirectAttrs.addFlashAttribute(ESCALATIONFORM, escalationForm);
                model.addAttribute(MESSAGE, "escaltion.pos.mandatory");
                return ESCALATIONSEARCHVIEW;
            }

        redirectAttrs.addFlashAttribute(ESCALATIONFORM, escalationForm);
        model.addAttribute(MESSAGE, "msg.escaltion.success");

        return "escalation-success";

    }

}
