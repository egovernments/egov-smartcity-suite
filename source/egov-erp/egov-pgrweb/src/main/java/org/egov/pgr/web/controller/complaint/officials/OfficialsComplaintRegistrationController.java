package org.egov.pgr.web.controller.complaint.officials;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.web.controller.complaint.GenericComplaintController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
