package org.egov.pgr.web.controller.complaint;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/complaint")
public class ComplaintRegistrationController {

	private ComplaintService complaintService;
	private @Autowired ComplaintTypeService complaintTypeService;

	public @Autowired ComplaintRegistrationController(ComplaintService complaintService) {
		this.complaintService = complaintService;
	}
	
	private @ModelAttribute Complaint complaint() {
		return new Complaint();
	}
	
	@RequestMapping( value = "/register", method = GET )
    public String showRegistration() { 
    	return "complaint/registration";
    }
}
	