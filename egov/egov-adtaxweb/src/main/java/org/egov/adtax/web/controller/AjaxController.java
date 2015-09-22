package org.egov.adtax.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.egov.adtax.entity.SubCategory;
import org.egov.adtax.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxController {
    
    @Autowired
    private   SubCategoryService subCategoryService;
    
    @RequestMapping(value = "/ajax-subCategories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<SubCategory> getDesignations(@RequestParam final Long category,
                        final HttpServletResponse response) {
        
         List<SubCategory> subCategoryList = new ArrayList<SubCategory>();
        if (category != null && !"".equals(category)) {
            subCategoryList = subCategoryService.getAllActiveSubCategoryByCategoryId(category);
            subCategoryList.forEach(subCategory -> subCategory.toString());
        }
        return subCategoryList;
    }
}
