package org.egov.tl.web.controller.master.subcategory;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.LicenseCategory;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.UnitOfMeasurement;
import org.egov.tl.entity.enums.RateTypeEnum;
import org.egov.tl.service.FeeTypeService;
import org.egov.tl.service.masters.LicenseCategoryService;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.egov.tl.service.masters.UnitOfMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


	@Controller
	@RequestMapping("/licensesubcategory")
	public class CreateLicenseSubCategoryController {
		
		private final LicenseSubCategoryService licenseSubCategoryService;
		
		@Autowired
		private LicenseCategoryService licenseCategoryService;

		@Autowired
		private UnitOfMeasurementService unitOfMeasurementService;
		
		@Autowired
		private FeeTypeService feeTypeService;


		
		@Autowired
		public CreateLicenseSubCategoryController(final LicenseSubCategoryService licenseSubCategoryService) {
			this.licenseSubCategoryService = licenseSubCategoryService;
		}

		@ModelAttribute
		public LicenseSubCategory licenseSubCategoryModel() {
			return new LicenseSubCategory();
		}
		
		@ModelAttribute(value = "licenseCategories")
		public List<LicenseCategory> getAllCategories() {
			return licenseCategoryService.findAll();
		}
		@ModelAttribute(value = "licenseUomTypes")
		public List<UnitOfMeasurement> getAllUom() {
			return unitOfMeasurementService.findAll();
		}
		@ModelAttribute(value = "licenseFeeTypes")
		public List<FeeType> getAllFeeType() {
			return feeTypeService.findAll();
		}
		
		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public String createSubCategoryForm(@ModelAttribute("licenseSubCategory") LicenseSubCategory licenseSubCategory, Model model) {
			model.addAttribute("rateTypes", RateTypeEnum.values());
			return "subcategory-create";
		}

		@RequestMapping(value = "/create", method = RequestMethod.POST)
		public String createSubCategory(@ModelAttribute @Valid LicenseSubCategory licenseSubCategory, BindingResult errors,
				RedirectAttributes additionalAttr, Model model) {

			if (errors.hasErrors()) {
				return "subcategory-create";
			}
			licenseSubCategoryService.createSubCategory(licenseSubCategory);
			additionalAttr.addFlashAttribute("message", "license.subcategory.save.success");
			
			model.addAttribute("licenseFeeTypes", getAllFeeType());
			model.addAttribute("licenseUomTypes", getAllUom());

			return "redirect:/licensesubcategory/view/" + licenseSubCategory.getCode();
		}
	}

