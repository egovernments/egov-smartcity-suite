/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.web.controller.masters.escalation;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import org.egov.commons.ObjectType;
import org.egov.commons.service.ObjectTypeService;
import org.egov.eis.entity.PositionHierarchy;
import org.egov.eis.service.PositionHierarchyService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintRouterService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/escalation")
public class ViewEscalationController {
    public static final String CONTENTTYPE_JSON = "application/json";

    protected final ComplaintTypeService complaintTypeService;
    private final BoundaryTypeService boundaryTypeService;
    private final PositionMasterService positionMasterService;
    private final ComplaintRouterService complaintRouterService;
    private final ObjectTypeService objectTypeService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PositionHierarchyService positionHierarchyService;

    @ModelAttribute
    public EscalationForm escalationForm() {
        return new EscalationForm();
    }

    @ModelAttribute("complaintTypes")
    public List<ComplaintType> complaintTypes() {
        return complaintTypeService.findAll();
    }

    @ModelAttribute("boundaryTypes")
    public List<BoundaryType> boundaryTypes() {
        return boundaryTypeService.getBoundaryTypeByHierarchyTypeName("ADMINISTRATION");
    }

    @ModelAttribute("positionMasterList")
    public List<Position> positionMasterList() {
        return positionMasterService.getAllPositions();
    }

    @Autowired
    public ViewEscalationController(final ComplaintTypeService complaintTypeService,
            final PositionMasterService positionMasterService, final BoundaryTypeService boundaryTypeService,
            final ComplaintRouterService complaintRouterService, final ObjectTypeService objectTypeService) {
        this.complaintTypeService = complaintTypeService;
        this.positionMasterService = positionMasterService;
        this.boundaryTypeService = boundaryTypeService;
        this.complaintRouterService = complaintRouterService;
        this.objectTypeService = objectTypeService;
    }

    @RequestMapping(value = "/view/{id}", method = GET)
    public String viewEscalationForm(@ModelAttribute final EscalationForm escalationForm, final Model model,
            @PathVariable final Long id) {

        if (id != null) {
            final ComplaintRouter complaintRouter = complaintRouterService.getRouterById(id);
            escalationForm.setComplaintRouter(complaintRouter);

            final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);

            if (complaintRouter != null && complaintRouter.getComplaintType() != null) {
                final List<PositionHierarchy> existingPosHierarchy = positionHierarchyService
                        .getPosHirByObjectTypeAndObjectSubType(objectType.getId(), complaintRouter.getComplaintType()
                                .getName());

                if (existingPosHierarchy != null && existingPosHierarchy.size() > 0)
                    escalationForm.setPositionHierarchyList(existingPosHierarchy);
                else {

                    final List<PositionHierarchy> positionHeirarchyList = new ArrayList<PositionHierarchy>();
                    final PositionHierarchy posHierarchy = new PositionHierarchy();
                    posHierarchy.setFromPosition(complaintRouter.getPosition());
                    posHierarchy.setObjectType(objectType);
                    posHierarchy.setObjectSubType(complaintRouter.getComplaintType().getName());
                    positionHeirarchyList.add(posHierarchy);
                    escalationForm.setPositionHierarchyList(positionHeirarchyList);
                }
            }
        }
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());

        return "escalation-view";
    }

    @RequestMapping(value = "/update/{id}", method = POST)
    public String saveEscalationForm(@ModelAttribute final EscalationForm escalationForm, final Model model,
            final BindingResult errors, final RedirectAttributes redirectAttrs, @PathVariable final Long id) {

        final ObjectType objectType = objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT);
        final List<PositionHierarchy> existingPosHierarchy = positionHierarchyService
                .getPosHirByObjectTypeAndObjectSubType(objectType.getId(), escalationForm.getComplaintRouter()
                        .getComplaintType().getName());

        if (existingPosHierarchy != null && existingPosHierarchy.size() > 0)
            positionHierarchyService.deleteAllInBatch(existingPosHierarchy);

        for (final PositionHierarchy posHierarchy : escalationForm.getPositionHierarchyList())
            if (posHierarchy.getFromPosition().getId() != null) {
                posHierarchy.setFromPosition(positionMasterService.getPositionById(posHierarchy.getFromPosition()
                        .getId()));
                posHierarchy.setToPosition(positionMasterService.getPositionById(posHierarchy.getToPosition().getId()));
                posHierarchy
                        .setObjectType(objectTypeService.getObjectTypeByName(PGRConstants.EG_OBJECT_TYPE_COMPLAINT));
                posHierarchy.setObjectSubType(posHierarchy.getObjectSubType());
                positionHierarchyService.createPositionHierarchy(posHierarchy);
            }

        final String message = "Escalation details updated successfully.";
        redirectAttrs.addFlashAttribute("escalationForm", escalationForm);
        model.addAttribute("message", message);

        return "escalation-success";

    }

}
