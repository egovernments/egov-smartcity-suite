package org.egov.pgr.web.controller.complaint;


import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewComplaintController {

	private ComplaintService complaintService;

	@Autowired
    @Qualifier("localDiskFileStoreService")
    protected FileStoreService fileStoreService;
	
	@Autowired
	public ViewComplaintController(ComplaintService complaintService) {
		this.complaintService = complaintService;
	}

	@RequestMapping(value = "/view-complaint", method = RequestMethod.GET)
	public String viewComplaints(@RequestParam Long complaintId, Model model) {
		Complaint complaint = complaintService.getComplaintById(complaintId);
		model.addAttribute("complaint", complaint);
		return "view-complaint";
	}

}
