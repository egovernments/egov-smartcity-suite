package org.egov.tl.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;


import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/licensesubcategory")
public class LicenseAjaxController {
	
	
	@Autowired
    private LicenseSubCategoryService licenseSubCategoryService;
    
	@RequestMapping(value = "/getsubcategories-by-category", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<LicenseSubCategory> getSubcategories(@RequestParam final Long categoryId) {
        return licenseSubCategoryService.findAllSubCategoryByCategory(categoryId);
        
    }


}
