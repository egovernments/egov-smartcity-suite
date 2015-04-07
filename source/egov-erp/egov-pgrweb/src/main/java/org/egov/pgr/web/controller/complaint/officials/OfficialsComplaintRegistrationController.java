package org.egov.pgr.web.controller.complaint.officials;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.web.controller.complaint.GenericComplaintController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/complaint/officials/")
public class OfficialsComplaintRegistrationController extends GenericComplaintController {

    private @Autowired ReceivingCenterService receivingCenterService;
    
    public @ModelAttribute("receivingCenters") List<ReceivingCenter> receivingCenters() {
        return this.receivingCenterService.findAll();
    }
    
    @RequestMapping(value = "show-reg-form", method = GET)
    public String showComplaintRegistrationForm(@ModelAttribute final Complaint complaint) {
        return "complaint/officials/registration-form";
    }

    @RequestMapping(value = "register", method = POST)
    public String registerComplaint(@Valid @ModelAttribute final Complaint complaint, final BindingResult resultBinder, final RedirectAttributes redirectAttributes,@RequestParam("files") final MultipartFile[] files) {
        if(complaint.getReceivingMode().equals("PAPER") && complaint.getReceivingCenter().isCrnRequired() && complaint.getCRN().isEmpty()) 
            resultBinder.rejectValue("CRN", "crn.mandatory.for.receivingcenter");
        if (resultBinder.hasErrors())
            return "complaint/officials/registration-form";
        complaint.setSupportDocs(addToFileStore(files));
        complaintService.createComplaint(complaint);
        redirectAttributes.addFlashAttribute("complaint", complaint);
        return "redirect:/reg-success";
    }
}
