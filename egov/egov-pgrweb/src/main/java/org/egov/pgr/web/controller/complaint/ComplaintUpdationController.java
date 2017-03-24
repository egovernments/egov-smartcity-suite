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

package org.egov.pgr.web.controller.complaint;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.CitizenFeedback;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusMappingService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;

import static org.egov.pgr.utils.constants.PGRConstants.APPROVAL_COMMENT_ATTRIB;
import static org.egov.pgr.utils.constants.PGRConstants.APPROVAL_POSITION_ATTRIB;
import static org.egov.pgr.utils.constants.PGRConstants.CITIZEN_RATING_ATTRIB;
import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_ATTRIB;
import static org.egov.pgr.utils.constants.PGRConstants.LOCATION_ATTRIB;
import static org.egov.pgr.utils.constants.PGRConstants.MODULE_NAME;

@Controller
@RequestMapping(value = "/complaint/update/{crnNo}")
public class ComplaintUpdationController {

    private static final String COMPLAINT_UPDATE_SUCCESS = "/update-success";
    private static final String COMPLAINT_EDIT = "complaint-edit";
    private static final String COMPLAINT_CITIZEN_EDIT = "complaint-citizen-edit";

    private final ComplaintService complaintService;
    private final ComplaintTypeService complaintTypeService;
    private final ComplaintStatusMappingService complaintStatusMappingService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    public ComplaintUpdationController(final ComplaintService complaintService,
                                       final ComplaintTypeService complaintTypeService,
                                       final ComplaintStatusMappingService complaintStatusMappingService) {
        this.complaintService = complaintService;
        this.complaintTypeService = complaintTypeService;
        this.complaintStatusMappingService = complaintStatusMappingService;

    }

    @ModelAttribute
    public void getComplaint(@PathVariable final String crnNo, final Model model) {
        final Complaint complaint = complaintService.getComplaintByCRN(crnNo);
        model.addAttribute(COMPLAINT_ATTRIB, complaint);
        model.addAttribute("complaintHistory", complaintService.getHistory(complaint));
        model.addAttribute("skippableForward", complaintService.canSendToPreviousAssignee(complaint));
        model.addAttribute("status",
                complaintStatusMappingService.getStatusByRoleAndCurrentStatus(securityUtils.getCurrentUser().getRoles(),
                        complaint.getStatus()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("complaintType", complaintTypeService.findActiveComplaintTypes());
        model.addAttribute("ward", Collections.emptyList());
        if (complaint.getCitizenFeedback() != null)
            model.addAttribute(CITIZEN_RATING_ATTRIB, complaint.getCitizenFeedback().ordinal());
        if (complaint.getLocation() != null && complaint.getChildLocation() != null) {
            model.addAttribute("ward",
                    boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(
                            complaint.getLocation().getBoundaryType().getName(), "Administration"));
            model.addAttribute(LOCATION_ATTRIB,
                    crossHierarchyService.getChildBoundariesNameAndBndryTypeAndHierarchyType("Locality", "Location"));
        } else if (complaint.getLat() > 0D && complaint.getLng() > 0D) {
            model.addAttribute("ward",
                    boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(
                            complaint.getLocation().getBoundaryType().getName(), "Administration"));
            model.addAttribute(LOCATION_ATTRIB,
                    crossHierarchyService.findChildBoundariesByParentBoundary(
                            complaint.getLocation().getBoundaryType().getName(),
                            complaint.getLocation().getBoundaryType().getHierarchyType().getName(),
                            complaint.getLocation().getName()));
        }
        if (null != complaint.getComplaintType()) {
            model.addAttribute("mailSubject", "Grievance regarding " + complaint.getComplaintType().getName());
            model.addAttribute("mailBody", complaintService.getEmailBody(complaint));
        }
        if (complaint.getStatus() != null)
            model.addAttribute("complaintStatus", complaint.getStatus().getName());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String edit(final Model model, @PathVariable final String crnNo) {
        return securityUtils.currentUserType().equals(UserType.CITIZEN) ? COMPLAINT_CITIZEN_EDIT : COMPLAINT_EDIT;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute Complaint complaint, final BindingResult errors,
                         final RedirectAttributes redirectAttrs, final Model model, final HttpServletRequest request,
                         @RequestParam("files") final MultipartFile[] files) {
        // this validation is common for citizen and official. Any more specific validation required for official then write
        // different method
        validateUpdate(complaint, errors, request);

        Long approvalPosition = 0l;
        String approvalComent = "";
        String result;
        if (request.getParameter(APPROVAL_COMMENT_ATTRIB) != null && !request.getParameter(APPROVAL_COMMENT_ATTRIB).trim().isEmpty())
            approvalComent = request.getParameter(APPROVAL_COMMENT_ATTRIB);
        if (request.getParameter(APPROVAL_POSITION_ATTRIB) != null && !request.getParameter(APPROVAL_POSITION_ATTRIB).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION_ATTRIB));
        if (request.getParameter(CITIZEN_RATING_ATTRIB) != null && !request.getParameter(CITIZEN_RATING_ATTRIB).isEmpty())
            complaint.setCitizenFeedback(CitizenFeedback.values()[Integer.valueOf(request.getParameter(CITIZEN_RATING_ATTRIB))]);
        if (!errors.hasErrors()) {
            if (!securityUtils.currentUserType().equals(UserType.CITIZEN) && files != null)
                complaint.getSupportDocs().addAll(fileStoreUtils.addToFileStore(files, MODULE_NAME, false));
            complaintService.update(complaint, approvalPosition, approvalComent, false);
            redirectAttrs.addFlashAttribute(COMPLAINT_ATTRIB, complaint);
            result = "redirect:" + complaint.getCrn() + COMPLAINT_UPDATE_SUCCESS;
        } else
            result = securityUtils.currentUserType().equals(UserType.CITIZEN) ? COMPLAINT_CITIZEN_EDIT : COMPLAINT_EDIT;
        return result;
    }

    @RequestMapping(COMPLAINT_UPDATE_SUCCESS)
    public ModelAndView successView(@ModelAttribute final Complaint complaint) {
        return new ModelAndView("complaint/reg-success", COMPLAINT_ATTRIB, complaint);
    }

    private void validateUpdate(final Complaint complaint, final BindingResult errors,
                                final HttpServletRequest request) {
        if (complaint.getStatus() == null)
            errors.rejectValue("status", "status.requried");

        if (request.getParameter(APPROVAL_COMMENT_ATTRIB) == null || request.getParameter(APPROVAL_COMMENT_ATTRIB).trim().isEmpty())
            errors.addError(new ObjectError(APPROVAL_COMMENT_ATTRIB, messageSource.getMessage("comment.not.null", null, null)));

        if (complaint.getLocation() == null && complaint.getLat() > 0D && complaint.getLng() > 0D)
            errors.rejectValue(LOCATION_ATTRIB, "location.info.not.found");

        if ((complaint.getLocation() == null || complaint.getChildLocation() == null) && complaint.getLat() <= 0D
                && complaint.getLng() <= 0D)
            errors.rejectValue(LOCATION_ATTRIB, "location.info.not.found");
    }
}