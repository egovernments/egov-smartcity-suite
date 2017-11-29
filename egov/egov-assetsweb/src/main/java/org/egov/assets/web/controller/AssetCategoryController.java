/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.assets.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.egov.assets.autonumber.AssetCategoryCodeGenerator;
import org.egov.assets.model.AssetCategory;
import org.egov.assets.model.CategoryPropertyType;
import org.egov.assets.service.AssetCategoryService;
import org.egov.assets.service.CategoryPropertyTypeService;
import org.egov.assets.util.AssetConstants;
import org.egov.assets.web.adaptor.AssetCategoryJsonAdaptor;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.service.UOMService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/assetcategory")
public class AssetCategoryController {
	private final static String ASSETCATEGORY_NEW = "assetcategory-new";
	private final static String ASSETCATEGORY_RESULT = "assetcategory-result";
	private final static String ASSETCATEGORY_EDIT = "assetcategory-edit";
	private final static String ASSETCATEGORY_VIEW = "assetcategory-view";
	private final static String ASSETCATEGORY_SEARCH = "assetcategory-search";
	private static final String ASSETCATEGORY_PROPERTIES = "assetcategory_properties";
	@Autowired
	private AssetCategoryService assetCategoryService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private AppConfigValueService appConfigValueService;

	@Autowired
	private ChartOfAccountsHibernateDAO chartOfAccountsService;
	@Autowired
	private UOMService uomService;

	@Autowired
	private AutonumberServiceBeanResolver beanResolver;
	@Autowired
	private CategoryPropertyTypeService categoryPropertyTypeService;

	/*
	 * @Autowiredc private UOMDao uOMService;
	 */

	private void prepareNewForm(Model model) {
		model.addAttribute("assetTypes",
				Arrays.asList(AssetCategory.AssetType.values()));
		model.addAttribute("assetCategorys", assetCategoryService.findAll());
		model.addAttribute("depreciationMethods",
				Arrays.asList(AssetCategory.DepreciationMethod.values()));
		AppConfigValues accountCodePurposeId = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME,
						AssetConstants.ASSETACCCODEPURPOSEID).get(0);
		model.addAttribute("accountCodes", chartOfAccountsService
				.getAccountCodeByPurpose(Integer.valueOf(accountCodePurposeId
						.getValue())));

		AppConfigValues accountDepPurposeId = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME,
						AssetConstants.ACCDEPPURPOSEID).get(0);
		model.addAttribute("accountDeps", chartOfAccountsService
				.getAccountCodeByPurpose(Integer.valueOf(accountDepPurposeId
						.getValue())));

		AppConfigValues accountDepExpPurposeId = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME,
						AssetConstants.DEPEXPACCPURPOSEID).get(0);
		model.addAttribute("accountDepExps", chartOfAccountsService
				.getAccountCodeByPurpose(Integer.valueOf(accountDepExpPurposeId
						.getValue())));

		AppConfigValues accountRevResPurposeId = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME,
						AssetConstants.REVRESACCPURPOSEID).get(0);
		model.addAttribute("accountRevRess", chartOfAccountsService
				.getAccountCodeByPurpose(Integer.valueOf(accountRevResPurposeId
						.getValue())));
		model.addAttribute("uOMs", uomService.findAllOrderByCategory());

		AppConfigValues assetCategoryCreationCode = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME, AssetConstants.ASSET_CATEGORY_CODE_CREATION_MODE).get(0);
		model.addAttribute("codeGenerationMode", assetCategoryCreationCode.getValue());

	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		prepareNewForm(model);
		model.addAttribute("assetCategory", new AssetCategory());
		return ASSETCATEGORY_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(
			@Valid @ModelAttribute AssetCategory assetCategory,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		AppConfigValues assetCategoryCreationCode = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME, AssetConstants.ASSET_CATEGORY_CODE_CREATION_MODE).get(0);
		if(!assetCategoryCreationCode.getValue().equalsIgnoreCase("Auto"))
		{
			if(assetCategory.getCode()==null || assetCategory.getCode().isEmpty())
			{
				errors.addError(new ObjectError("accountCodes", messageSource.getMessage("comment.not.null", null, null)));
			}
		}

		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ASSETCATEGORY_NEW;
		}

		//Fetch the mode in which the assetCategory is being created
		//If it is Auto then populate it with the auto generated sequence number
		if(assetCategoryCreationCode.getValue().equals("Auto"))
		{
			AssetCategoryCodeGenerator assetCategoryCodeGenerator = (AssetCategoryCodeGenerator)beanResolver.getAutoNumberServiceFor(AssetCategoryCodeGenerator.class);
			String assetCategoryNumber = assetCategoryCodeGenerator.getNextNumber(assetCategory);
			assetCategory.setCode(assetCategoryNumber);
		}
		assetCategoryService.create(assetCategory);
		redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
				"msg.assetcategory.success", null, null));
		return "redirect:/assetcategory/result/" + assetCategory.getId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Long id, Model model) {
		AssetCategory assetCategory = assetCategoryService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("assetCategory", assetCategory);
		return ASSETCATEGORY_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute AssetCategory assetCategory,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ASSETCATEGORY_EDIT;
		}
		assetCategoryService.update(assetCategory);
		redirectAttrs.addFlashAttribute("message", messageSource.getMessage(
				"msg.assetcategory.success", null, null));
		return "redirect:/assetcategory/result/" + assetCategory.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Long id, Model model) {
		AssetCategory assetCategory = assetCategoryService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("assetCategory", assetCategory);
		return ASSETCATEGORY_VIEW;
	}

	@RequestMapping(value = "/properties/{id}", method = RequestMethod.GET)
	public String properties(@PathVariable("id") final Long id, Model model) {
		AssetCategory assetCategory = assetCategoryService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("assetCategory", assetCategory);
		return "assetcategory-properties";
	}


	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id, Model model) {
		AssetCategory assetCategory = assetCategoryService.findOne(id);
		model.addAttribute("assetCategory", assetCategory);
		return ASSETCATEGORY_RESULT;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		AssetCategory assetCategory = new AssetCategory();
		prepareNewForm(model);
		model.addAttribute("assetCategory", assetCategory);
		return ASSETCATEGORY_SEARCH;

	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(
			@PathVariable("mode") final String mode, Model model,
			@ModelAttribute final AssetCategory assetCategory) {
		List<AssetCategory> searchResultList = assetCategoryService
				.search(assetCategory);
		String result = new StringBuilder("{ \"data\":")
		.append(toSearchResultJson(searchResultList)).append("}")
		.toString();
		return result;
	}

	@RequestMapping(value = "/getParentAccounts/{parentId}", method = RequestMethod.GET)
	public @ResponseBody String getParentAccounts(@PathVariable("parentId")  Long parentId) {
		AssetCategory assetCategory = assetCategoryService.findOne(parentId);

		//Since some fields might not be mandatory in accountCategory check for null
		String result = "assetAccountCode:" + ((assetCategory.getAssetAccountCode() == null) ? "" :(assetCategory.getAssetAccountCode().getId())) + 
				",accDepAccountCode:" + ((assetCategory.getAccDepAccountCode() == null) ? "" : assetCategory.getAccDepAccountCode().getId()) +
				",revAccountCode:" + ((assetCategory.getRevAccountCode() == null) ? "" :assetCategory.getRevAccountCode().getId()) +
				",depExpAccountCode:" + ((assetCategory.getDepExpAccountCode() == null) ? "" :assetCategory.getDepExpAccountCode().getId()) +
				",uom:" + ((assetCategory.getUom() == null) ? "" :  +assetCategory.getUom().getId());
		return result;
	}

	@RequestMapping(value = "/masterdata", method = RequestMethod.GET)
	public @ResponseBody String fetchMasterData(
			@RequestParam(value = "id") final Long id,
			@RequestParam(value = "value") final String value){
		CategoryPropertyType category = categoryPropertyTypeService.findOne(id);
		List<?> resultList = categoryPropertyTypeService.getMasterData(category, value);

		return toMasterDataJson(resultList,category.getEnumValues());
	}

	@RequestMapping(value = "/categoryproperty/delete", method = RequestMethod.GET)
	public @ResponseBody void deleteCategoryProperty(
			@RequestParam(value="categoryPropertyId")final Long categoryPropertyId){
		categoryPropertyTypeService.delete(categoryPropertyId);

	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(AssetCategory.class,
				new AssetCategoryJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}

	private String toMasterDataJson(List<?> resultList ,String value)
	{
		JsonArray response =new JsonArray();
		Class<?> c;
		try {
			c = Class.forName(value);
			for(Object classObj:resultList)
			{
				JsonObject jsonObject=new JsonObject();
				Method idMethod=c.getMethod("getId");
				Method nameMethod=c.getMethod("getName");
				Long idValue=(Long) idMethod.invoke(classObj);
				String nameValue=(String) nameMethod.invoke(classObj);
				jsonObject.addProperty("id", idValue);
				jsonObject.addProperty("name", nameValue);
				response.add(jsonObject);
			}
		} catch (ClassNotFoundException e1) {
		} catch (NoSuchMethodException | SecurityException e) {


		} catch (IllegalAccessException e) {


		} catch (IllegalArgumentException e) {


		} catch (InvocationTargetException e) {


		}

		return response.toString();
	}
}