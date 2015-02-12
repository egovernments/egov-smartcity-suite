package org.egov.pgr.web.controller.complaint;

import java.util.Collections;
import java.util.List;

import org.egov.exceptions.EGOVRuntimeException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/complaint-update/{id}")
public class ComplaintUpdationController {

	private ComplaintService complaintService;
	private ComplaintTypeService complaintTypeService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ComplaintStatusMappingService complaintStatusMappingService;



	@Autowired
	public ComplaintUpdationController(ComplaintService complaintService,ComplaintTypeService complaintTypeService) {
		this.complaintService = complaintService;
		this.complaintTypeService=complaintTypeService;
	}


	@ModelAttribute
	public Complaint getComplaint(@PathVariable Long id)
	{
		Complaint complaint= complaintService.get(id);
		return complaint;
	}

	@ModelAttribute("complaintType")
	public List<ComplaintType> complaintTypes() {
		return complaintTypeService.findAll();
	}

	@ModelAttribute("status")
	public List<ComplaintStatus> getStatus() {

		return complaintStatusMappingService.getStatusByRoleAndCurrentStatus(null, null);
	}




	@RequestMapping(method = RequestMethod.GET)
	public String show(Model model,@PathVariable Long id){
		try{
			Complaint complaint =complaintService.get(id); 
			//set the defaults
			model.addAttribute("zone", Collections.EMPTY_LIST);
			model.addAttribute("ward", Collections.EMPTY_LIST);

			if (complaint.getComplaintType().isLocationRequired()) 
			{
				model.addAttribute("zone", commonService.getZones());
				if(complaint.getLocation()!=null)
				{
					model.addAttribute("zone", commonService.getZones()); 
					model.addAttribute("ward", commonService.getWards(complaint.getLocation().getParent()));
				}
			}
		}catch(Exception e)
		{
			throw new EGOVRuntimeException("Missing mandatory fields in the data");
		}

		return "complaint-update";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String update(@ModelAttribute Complaint complaint, BindingResult errors, RedirectAttributes redirectAttrs,Model model)
	{
		if (!errors.hasErrors()) {
			complaintService.update(complaint);
			redirectAttrs.addFlashAttribute("message", "Successfully created Complaint Type !");
		}
		show(model,complaint.getId());
		return "redirect:/complaint-update/"+complaint.getId();
	}




}
