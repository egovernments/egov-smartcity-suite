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
package org.egov.pgr.web.controller.complaint;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.entity.enums.UserType;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.CommonService;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusMappingService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/complaint-update")
public class ComplaintUpdationController {

	private final ComplaintService complaintService;
	private final ComplaintTypeService complaintTypeService;
	private final CommonService commonService;
	private final ComplaintStatusMappingService complaintStatusMappingService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SecurityUtils securityUtils;

	@Autowired
	public ComplaintUpdationController(final ComplaintService complaintService,
			final ComplaintTypeService complaintTypeService, final CommonService commonService,
			final ComplaintStatusMappingService complaintStatusMappingService, final SmartValidator validator) {
		this.complaintService = complaintService;
		this.complaintTypeService = complaintTypeService;
		this.commonService = commonService;
		this.complaintStatusMappingService = complaintStatusMappingService;

	}

	// Dont use this which will query multiple times
	// Not an issue since hibernate will not load once again but it is confusing
	// developers
	@ModelAttribute
	public Complaint getComplaint(@RequestParam final Long id) {
		final Complaint complaint = complaintService.getComplaintById(id);
		return complaint;
	}

	@ModelAttribute("complaintType")
	public List<ComplaintType> complaintTypes() {
		return complaintTypeService.findAll();
	}

	@ModelAttribute("status")
	public List<ComplaintStatus> getStatus(@RequestParam final Long id) {
		final Set<Role> rolesList = securityUtils.getCurrentUser().getRoles();
		return complaintStatusMappingService.getStatusByRoleAndCurrentStatus(rolesList, getComplaint(id).getStatus());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String edit(final Model model, @RequestParam final Long id) {
		final User currentUser = securityUtils.getCurrentUser();
		if (currentUser.getType().equals(UserType.CITIZEN)) {
			complaintService.getComplaintById(id);
			return "complaint-citizen-edit";
		}

		final Complaint complaint = complaintService.getComplaintById(id);
		final List<Hashtable<String, Object>> historyTable = complaintService.getHistory(complaint);
		model.addAttribute("complaintHistory", historyTable);

		prepareWorkflow(model);
		// set the defaults
		// model.addAttribute("zone", Collections.EMPTY_LIST);
		model.addAttribute("ward", Collections.EMPTY_LIST);
		model.addAttribute("zone", commonService.getZones());
		if (complaint.getComplaintType().isLocationRequired())
			// model.addAttribute("zone", commonService.getZones());
			if (complaint.getLocation() != null)
				model.addAttribute("ward", commonService.getWards(complaint.getLocation().getParent().getId()));

		return "complaint-edit";
	}

	private void prepareWorkflow(final Model model) {
		model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());

	}

	@RequestMapping(method = RequestMethod.POST)
	public String update(@ModelAttribute Complaint complaint, final BindingResult errors,
			final RedirectAttributes redirectAttrs, final Model model, final HttpServletRequest request) {
		// change this validator to custom as no need to do complete validation
		// Since the usage of this is screen is very heavy need to consider all
		// performance fixes
		// validator.validate(complaint, errors);
		validateUpdate(complaint, errors, request);
		Long approvalPosition = 0l;
		if (null != request.getParameter("approvalPosition") && !request.getParameter("approvalPosition").isEmpty())
			approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
		String approvalComent = "";
		if (null != request.getParameter("approvalComent"))
			approvalComent = request.getParameter("approvalComent");

		if (!errors.hasErrors()) {
			complaint = complaintService.update(complaint, approvalPosition, approvalComent);

			redirectAttrs.addFlashAttribute("message", "Successfully Updated Complaint !");
		} else {
			prepareWorkflow(model);
			model.addAttribute("zone", commonService.getZones());
			model.addAttribute("ward", Collections.EMPTY_LIST);

			if (complaint.getComplaintType() != null && complaint.getComplaintType().isLocationRequired())
				if (complaint.getLocation() != null)
					model.addAttribute("ward", commonService.getWards(complaint.getLocation().getParent().getId()));

			return "complaint-edit";

		}
		return "redirect:/complaint-update?id=" + complaint.getId();
	}

	private void validateUpdate(final Complaint complaint, final BindingResult errors, final HttpServletRequest request) {
		if (null == complaint.getStatus()) {
			final ObjectError error = new ObjectError("status", "Complaint Status is required");
			errors.addError(error);
		}

		if (null == complaint.getComplaintType()) {
			final ObjectError error = new ObjectError("complaintType", "ComplaintType is required");
			errors.addError(error);
		}

		// comenting as only status or complaint type change does not need
		// coments
		/*
		 * if (null == request.getParameter("approvalComent") ||
		 * request.getParameter("approvalComent").isEmpty()) { ObjectError error
		 * = new ObjectError("approvalComent",
		 * "Complaint coments Cannot be null"); errors.addError(error); }
		 */

	}
}
