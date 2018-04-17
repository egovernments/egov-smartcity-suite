package org.egov.ptis.web.controller.transactions.survey;

import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPES;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

import java.util.List;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.ptis.domain.entity.property.survey.SearchSurveyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/surveyproperties") 
public class SurveyPropertyController {
    
    @Autowired
    private BoundaryService boundaryService;
    
    @ModelAttribute("applicationTypes")
    public List<String> getApplicationTypes() {
        return APPLICATION_TYPES;
    }
    
    @ModelAttribute("localitylist")
    public List<Boundary> localities() {
            return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
    }
    
    @ModelAttribute("wardlist")
    public List<Boundary> wards() {
            return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, ADMIN_HIERARCHY_TYPE);
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(final Model model) {
        model.addAttribute("surveyApplication", new SearchSurveyRequest());
        return "surveyApplication-form";
    }

}
