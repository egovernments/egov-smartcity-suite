package org.egov.pgr.web.controller.masters;

import java.util.List;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = { "/complaint-type/view", "/complaint-type/update" })
public class SearchComplaintTypeController {
    
    private ComplaintTypeService complaintTypeService;
    
    @Autowired
    public SearchComplaintTypeController(ComplaintTypeService complaintTypeService) {
     this.complaintTypeService = complaintTypeService;   
    }
    
    @ModelAttribute
    public ComplaintType hierarchyTypeModel() {
           return new ComplaintType();
    }

    @ModelAttribute(value = "complaintTypes")
    public List<ComplaintType> listComplaintTypes() {
        return complaintTypeService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showComplaintTypes(Model model) {
        return "complainttype-list";
    }
    

}
