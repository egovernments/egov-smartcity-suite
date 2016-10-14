package org.egov.tl.web.controller.master.subcategory;

import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.egov.tl.service.masters.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/licensesubcategory")
public class ViewSubCategoryController {

	private LicenseSubCategoryService licenseSubCategoryService;
	
	@Autowired
	private LicenseCategoryService licenseCategoryService;
	
	@Autowired
	private UnitOfMeasurementService unitOfMeasurementService;
	
	@Autowired
	private FeeTypeService feeTypeService;

	@Autowired
	public ViewSubCategoryController(LicenseSubCategoryService licenseSubCategoryService) {
		this.licenseSubCategoryService = licenseSubCategoryService;
	}

	@ModelAttribute
	public LicenseSubCategory licenseCategoryModel(@PathVariable String code) {
		return licenseSubCategoryService.findSubCategoryByCode(code);
	}

	@RequestMapping(value = "/view/{code}", method = RequestMethod.GET)
	public String categoryView(Model model) {
		model.addAttribute("licenseCategories", licenseCategoryService.findAll());
		model.addAttribute("licenseFeeTypes", feeTypeService.findAll());
		model.addAttribute("licenseUomTypes", unitOfMeasurementService.findAll());
		return "subcategory-view";
	}
}
