package org.egov.pgr.web.controller.complaint.citizen;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.web.controller.complaint.GenericComplaintController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/complaint/citizen/")
public class CitizenComplaintRegistrationController extends GenericComplaintController {

    @RequestMapping(value = "show-reg-form", method = GET)
    public String showComplaintRegistrationForm(@ModelAttribute final Complaint complaint) {
        return "complaint/citizen/registration-form";
    }

    @RequestMapping(value = "anonymous/show-reg-form", method = GET)
    public String showAnonymousComplaintRegistrationForm(@ModelAttribute final Complaint complaint) {
        return "complaint/citizen/anonymous-registration-form";
    }
    
    @RequestMapping(value = "register", method = POST)
    public String registerComplaint(@Valid @ModelAttribute final Complaint complaint, final BindingResult resultBinder, @RequestParam("files") final MultipartFile[] files) {
        if (resultBinder.hasErrors())
            return "complaint/citizen/registration-form";
        complaint.setSupportDocs(addToFileStore(files));
        complaintService.createComplaint(complaint);
        return "redirect:show-reg-form";
    }
    
    @RequestMapping(value = "anonymous/register", method = POST)
    public String registerComplaintAnonymous(@Valid @ModelAttribute final Complaint complaint, final BindingResult resultBinder, @RequestParam("files") final MultipartFile[] files) {
        if(StringUtils.isBlank(complaint.getComplainant().getEmail()) && StringUtils.isBlank(complaint.getComplainant().getMobile()))
            resultBinder.rejectValue("complainant.email", "email.or.mobile.ismandatory");
        
        if(StringUtils.isBlank(complaint.getComplainant().getName())) 
            resultBinder.rejectValue("complainant.name", "complainant.name.ismandatory");
        
        if (resultBinder.hasErrors()) 
            return "complaint/citizen/anonymous-registration-form";
         
        complaint.setSupportDocs(addToFileStore(files));
        complaintService.createComplaint(complaint);
        return "redirect:show-reg-form";
    }

}