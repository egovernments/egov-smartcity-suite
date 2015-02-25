package org.egov.pgr.web.controller.complaint;

import java.util.Collections;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.utils.SecurityUtils;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.dao.RoleDAO;
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
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/complaint-update/{id}")
public class ComplaintUpdationController {

	private ComplaintService complaintService;
	private ComplaintTypeService complaintTypeService;
	private CommonService commonService;
	private ComplaintStatusMappingService complaintStatusMappingService;
	private SmartValidator validator;
	private SecurityUtils securityUtils;
	private RoleDAO roleDAO;  

	@Autowired
	public ComplaintUpdationController(ComplaintService complaintService,
			ComplaintTypeService complaintTypeService,
			CommonService commonService,
			ComplaintStatusMappingService complaintStatusMappingService,
			SmartValidator validator, SecurityUtils securityUtils,
			RoleDAO roleDAO) {
		this.complaintService = complaintService;
		this.complaintTypeService = complaintTypeService;
		this.commonService = commonService;
		this.complaintStatusMappingService = complaintStatusMappingService;
		this.validator = validator;
		this.securityUtils = securityUtils;  
		this.roleDAO = roleDAO;
	}

	@ModelAttribute
	public Complaint getComplaint(@PathVariable Long id) {
		Complaint complaint = complaintService.get(id);
		return complaint;
	}

	@ModelAttribute("complaintType")
	public List<ComplaintType> complaintTypes() {
		return complaintTypeService.findAll();
	}

	@ModelAttribute("status")
	public List<ComplaintStatus> getStatus(@PathVariable Long id) {
		
		List<Role> rolesList = roleDAO.getRolesByUser(securityUtils.getCurrentUser().getId());
		
		return complaintStatusMappingService.getStatusByRoleAndCurrentStatus(rolesList, getComplaint(id).getStatus());
	} 

	@RequestMapping(method = RequestMethod.GET)
	public String edit(Model model, @PathVariable Long id) {
		try {
			Complaint complaint = complaintService.get(id);
			// set the defaults
			model.addAttribute("zone", Collections.EMPTY_LIST);
			model.addAttribute("ward", Collections.EMPTY_LIST);

			if (complaint.getComplaintType().isLocationRequired()) {
				model.addAttribute("zone", commonService.getZones());
				if (complaint.getLocation() != null) {
					model.addAttribute("ward",commonService.getWards(complaint.getLocation().getParent().getId()));
				}
			} 
		} catch (Exception e) {
			throw new EGOVRuntimeException("Missing mandatory fields in the data");
		}

		return "complaint-edit";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String update(@ModelAttribute Complaint complaint,
			BindingResult errors, RedirectAttributes redirectAttrs, Model model) {
	//change this validator to custom as no need to do complete validation 
	// Since the usage of this is screen is very heavy need to consider all performance fixes	
		validator.validate(complaint, errors);
		if (!errors.hasErrors()) {
			complaintService.update(complaint);
			redirectAttrs.addFlashAttribute("message",
					"Successfully created Complaint Type !");
		} else 
		{
			model.addAttribute("zone", Collections.EMPTY_LIST);
			model.addAttribute("ward", Collections.EMPTY_LIST);

			if (complaint.getComplaintType() != null && complaint.getComplaintType().isLocationRequired())
			{
				model.addAttribute("zone", commonService.getZones());
				if (complaint.getLocation() != null) {
					model.addAttribute("ward",commonService.getWards(complaint.getLocation().getParent().getId()));
				}

			} 

		}
		return "redirect:/complaint-edit/" + complaint.getId();
	}

}