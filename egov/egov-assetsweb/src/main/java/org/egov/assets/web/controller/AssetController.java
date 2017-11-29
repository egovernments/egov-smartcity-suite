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
import org.egov.assets.autonumber.AssetCodeGenerator;
import org.egov.assets.model.Asset;
import org.egov.assets.model.Asset.ModeOfAcquisition;
import org.egov.assets.service.AssetCategoryService;
import org.egov.assets.service.AssetService;
import org.egov.assets.util.AssetConstants;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.egassets.web.adaptor.AssetJsonAdaptor;
import org.egov.eis.service.PersonalInformationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.json.simple.JSONObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/asset")
public class AssetController {
	private final static String ASSET_NEW = "asset-new";
	private final static String ASSET_RESULT = "asset-result";
	private final static String ASSET_EDIT = "asset-edit";
	private final static String ASSET_VIEW = "asset-view";
	private final static String ASSET_SEARCH = "asset-search";
	private final static String LOCALITY="Locality";
	private final static String LOCATION_HIERARCHY_TYPE="LOCATION";
	private final static String ZONE="Zone";
	private final static String BLOCK="Block";
	private final static String REVENUE_HEIRARCHY_TYPE="REVENUE";
	private final static String ELECTION_BOUNDARY_TYPE="Ward";
	private final static String ELECTION_HIERARCHY_TYPE="ADMINISTRATION";
	@Autowired
	private AssetService assetService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private AssetCategoryService assetCategoryService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private BoundaryService boundaryService;
	@Autowired
	private BoundaryTypeService boundaryTypeService;
	@Autowired
	private CrossHierarchyService crossHierarchyService;
	@Autowired
	private AppConfigValueService appConfigValueService;
	@Autowired
	private AutonumberServiceBeanResolver beanResolver;

	@Autowired
	private EgwStatusHibernateDAO egwStatusService;
	@Autowired
	private PersonalInformationService personalInformationService;

	private void prepareNewForm(Model model) {
		model.addAttribute("assetCategorys", assetCategoryService.findAll());
		model.addAttribute("departments", departmentService.getAllDepartments());
		model.addAttribute("locations", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE));
        model.addAttribute("zones",boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE, REVENUE_HEIRARCHY_TYPE));
		model.addAttribute("electionWards", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ELECTION_BOUNDARY_TYPE, ELECTION_HIERARCHY_TYPE));
		model.addAttribute("modeOfAcquisitions",ModeOfAcquisition.values());
		model.addAttribute("egwStatus", egwStatusService.getStatusByModule("ASSET"));
		AppConfigValues assetCodeCreation = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME, AssetConstants.ASSET_CATEGORY_CODE_CREATION_MODE).get(0);
		model.addAttribute("codeGenerationMode", assetCodeCreation.getValue());
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(final Model model) {
		prepareNewForm(model);
		model.addAttribute("asset", new Asset());
		return ASSET_NEW;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute final Asset asset,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {

		AppConfigValues assetCodeCreation = appConfigValueService
				.getConfigValuesByModuleAndKey(AssetConstants.MODULE_NAME, AssetConstants.ASSET_CATEGORY_CODE_CREATION_MODE).get(0);
		if(!assetCodeCreation.getValue().equalsIgnoreCase("Auto"))
		{
			if(asset.getCode()==null || asset.getCode().isEmpty())
			{
				errors.addError(new ObjectError("assetCode", messageSource.getMessage("comment.not.null", null, null)));
			}
		}
		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ASSET_NEW;
		}

		//Fetch the mode in which the assetCategory is being created
		//If it is Auto then populate it with the auto generated sequence number
		if(assetCodeCreation.getValue().equals("Auto"))
		{
			AssetCodeGenerator assetCodeGenerator = beanResolver.getAutoNumberServiceFor(AssetCodeGenerator.class);
			String assetNumber = assetCodeGenerator.getNextNumber(asset);
			asset.setCode(assetNumber);
		}
		asset.setStatus(egwStatusService.findById(asset.getStatus().getId(), false));
		assetService.create(asset);
		redirectAttrs.addFlashAttribute("message",
				messageSource.getMessage("msg.asset.success", null, null));
		return "redirect:/asset/result/" + asset.getId();
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") final Long id, Model model) {
		Asset asset = assetService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("asset", asset);
		model.addAttribute("blockId", ((asset.getLocationDetails().getBlock()!= null)?asset.getLocationDetails().getBlock().getId() :""));
		model.addAttribute("wardId", ((asset.getLocationDetails().getRevenueWard()!= null)?asset.getLocationDetails().getRevenueWard().getId():""));
		model.addAttribute("streetId", ((asset.getLocationDetails().getStreet()!= null)?asset.getLocationDetails().getStreet().getId():""));
		return ASSET_EDIT;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute final Asset asset,
			final BindingResult errors, final Model model,
			final RedirectAttributes redirectAttrs) {

		if (errors.hasErrors()) {
			prepareNewForm(model);
			return ASSET_EDIT;
		}
		assetService.update(asset);
		redirectAttrs.addFlashAttribute("message",
				messageSource.getMessage("msg.asset.success", null, null));
		return "redirect:/asset/result/" + asset.getId();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") final Long id, Model model) {
		Asset asset = assetService.findOne(id);
		prepareNewForm(model);
		model.addAttribute("asset", asset);
		return ASSET_VIEW;
	}

	@RequestMapping(value = "/result/{id}", method = RequestMethod.GET)
	public String result(@PathVariable("id") final Long id, Model model) {
		Asset asset = assetService.findOne(id);
		model.addAttribute("asset", asset);
		return ASSET_RESULT;
	}

	@RequestMapping(value = "/search/{mode}", method = RequestMethod.GET)
	public String search(@PathVariable("mode") final String mode, Model model) {
		Asset asset = new Asset();
		prepareNewForm(model);
		model.addAttribute("asset", asset);
		return ASSET_SEARCH;

	}
	
	
	//This is the new service included and intended for future use to show search screen for Works module.
	@RequestMapping(value = "/resultsearch/{mode}", method = RequestMethod.GET)
	public String searchAsset(@PathVariable("mode") final String mode,
			      @RequestParam("rowId") final int rowId,
			      @RequestParam("assetStatus") final String[] assetStatus, Model model) {
		Asset asset = new Asset();
		prepareNewForm(model);
		model.addAttribute("rowId", rowId);
		model.addAttribute("asset", asset);
		return ASSET_SEARCH;
	}

	@RequestMapping(value = "/ajaxsearch/{mode}", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String ajaxsearch(
			@PathVariable("mode") final String mode, Model model,
			@ModelAttribute final Asset asset) {
//		if(asset.getStatus().getId() != null)
//			asset.setStatus(egwStatusService.findById(asset.getStatus().getId(), false));
		List<Asset> searchResultList = assetService.search(asset);
		String result = new StringBuilder("{ \"data\":")
		.append(toSearchResultJson(searchResultList)).append("}")
		.toString();
		return result;
	}
	
	@RequestMapping(value = "/getBoundariesByLocation/{locationId}", method = RequestMethod.GET)
	public @ResponseBody String getBoundariesByLocation(
			@PathVariable("locationId") final Long locationId,Model model,
			@ModelAttribute final Asset asset){
		
		JSONObject boundaryResultList = fetchBoundariesByLocation(locationId);
		String result = new StringBuilder("{ \"data\":")
		.append(toSearchResultJson(boundaryResultList)).append("}")
		.toString();
		return result;
	}

	public Object toSearchResultJson(final Object object) {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		final Gson gson = gsonBuilder.registerTypeAdapter(Asset.class,
				new AssetJsonAdaptor()).create();
		final String json = gson.toJson(object);
		return json;
	}
	
	private JSONObject fetchBoundariesByLocation(Long locationId){
		BoundaryType blockType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(BLOCK, REVENUE_HEIRARCHY_TYPE);
		final List<Boundary> blocks = crossHierarchyService.getParentBoundaryByChildBoundaryAndParentBoundaryType(locationId, blockType.getId());
		List<Boundary> streets = boundaryService.getChildBoundariesByBoundaryId(locationId);
		final List<JSONObject> wardJsonObjects = new ArrayList<JSONObject>();
		final List<JSONObject> blockJsonObjects = new ArrayList<JSONObject>();
        final List<Long> boundaries = new ArrayList<Long>();
        
        for (final Boundary block : blocks) {
            final Boundary ward = block.getParent();
            final JSONObject jsonObject = new JSONObject();
            final JSONObject wardJsonObject = new JSONObject();
            if (!boundaries.contains(ward.getId())) {
            	wardJsonObject.put("wardId", ward.getId());
            	wardJsonObject.put("wardName", ward.getName());
            	wardJsonObjects.add(wardJsonObject);
            }
            jsonObject.put("blockId", block.getId());
            jsonObject.put("blockName", block.getName());
            jsonObject.put("wards",wardJsonObjects);
            blockJsonObjects.add(jsonObject);
            boundaries.add(ward.getId());
        }
        
        final List<JSONObject> streetJsonObjects = new ArrayList<JSONObject>();
        for (final Boundary street : streets) {
            final JSONObject streetObject = new JSONObject();
            streetObject.put("streetId", street.getId());
            streetObject.put("streetName", street.getName());
            streetJsonObjects.add(streetObject);
        }
        
        final Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
        map.put("blocks", blockJsonObjects);
        map.put("streets", streetJsonObjects);
        final JSONObject boundaryJson = new JSONObject();
        boundaryJson.put("results", map);
        
        return boundaryJson;
	}
}