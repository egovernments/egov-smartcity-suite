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
package org.egov.ptis.actions.admin;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BoundaryCategoryDao;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.AREA_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

@ParentPackage("egov")
@Namespace("/admin")
@ResultPath("/WEB-INF/jsp/")
public class ChangeStreetRateAction extends BaseFormAction {
	private Integer zoneId;
	private Integer wardId;
	private Integer areaId;
	private List<Map<String, String>> readOnlyFields = new ArrayList<Map<String, String>>();
	private String usageFactor;
	private String structFactor;
	private Float currentRate;
	private String currLocFactor;
	private Float revisedRate;
	private String revisedLocFactor;
	private String searchValue;
	private String saveAction;
	private Boundary boundary;

	@Autowired
	private BoundaryCategoryDao boundaryCategoryDAO;

	@Autowired
	private BoundaryService boundaryService;
	
	private static final String SEARCH = "search";
	private static final String RESULTS = "results";
	private static final String ACK = "ack";

	private final Logger LOGGER = Logger.getLogger(getClass());

	public ChangeStreetRateAction() {
	}

	@Override
	public Object getModel() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	@Override
	public void prepare() {
		LOGGER.debug("Entered into the prepare method");
		List<Boundary> zoneList =boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE, REVENUE_HIERARCHY_TYPE);
		List<Boundary> wardList =boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, REVENUE_HIERARCHY_TYPE);
		LOGGER.debug("prepare : wards: " + ((wardList != null) ? wardList : ZERO));
		addDropdownData("Zone", zoneList);
		addDropdownData("wardList", wardList);
		prepareStreetDropDownData(wardId != null);
		addDropdownData("categoryList", Collections.EMPTY_LIST);

		LOGGER.debug("Exit from prepare method");
	}

	@SuppressWarnings("unchecked")
	private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
		LOGGER.debug("Entered into the prepareWardDropDownData");
		if (zoneExists && wardExists) {
			List<Boundary> wardNewList = new ArrayList<Boundary>();
			wardNewList = getPersistenceService()
					.findAllBy(
							"from Boundary BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
							PropertyTaxConstants.WARD, getZoneId().longValue());
			LOGGER.debug("prepareWardDropDownData : No of wards in zone: " + getZoneId() + " are: "
					+ ((wardNewList != null) ? wardNewList.size() : ZERO));
			addDropdownData("wardList", wardNewList);
		} else {
			addDropdownData("wardList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData");
	}

	@SuppressWarnings("unchecked")
	private void prepareAreaDropDownData(boolean wardExists, boolean areaExists) {
		LOGGER.debug("Entered into the prepareAreaDropDownData");
		if (wardExists && areaExists) {
			List<Boundary> areaNewList = new ArrayList<Boundary>();
			areaNewList = getPersistenceService()
					.findAllBy(
							"from Boundary BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
							AREA_BNDRY_TYPE, getWardId().longValue());
			
			LOGGER.debug("prepareAreaDropDownData : No of areas in ward: " + getWardId() + " are: "
					+ ((areaNewList != null) ? areaNewList.size() : ZERO));
			addDropdownData("areaList", areaNewList);
		} else {
			addDropdownData("areaList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareAreaDropDownData");
	}
	
	@SuppressWarnings("unchecked")
	private void prepareStreetDropDownData(boolean wardExists) {
		LOGGER.debug("Entered into the prepareAreaDropDownData");
		if (wardExists ) {
			List<Boundary> streetList = new ArrayList<Boundary>();
			streetList = getPersistenceService()
					.findAllBy("select CH.child from CrossHierarchy CH where CH.parent.id = ? ",getWardId().longValue());
			
			LOGGER.debug("prepareStreetDropDownData : No of areas in ward: " + getWardId() + " are: "
					+ ((streetList != null) ? streetList.size() : ZERO));
			addDropdownData("streetList", streetList);
		} else {
			addDropdownData("streetList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareStreetDropDownData");
	}

	@Action(value="/changeStreetRate-searchForm",results = { @Result(name = SEARCH, location="admin/changeStreetRate-search.jsp") })
	public String searchForm() {
		LOGGER.debug("Entered into searchForm");
		LOGGER.debug("Exit from searchForm");
		return SEARCH;
	}

	@ValidationErrorPage(value = "search")
	@Action(value="/changeStreetRate-search" ,results = { @Result(name = RESULTS, location="admin/changeStreetRate-results.jsp") })
	public String search() {
		LOGGER.debug("Enered into search");
		LOGGER.debug("Exit from search");
		return searchForCategories();

	}

	@SkipValidation
	@ValidationErrorPage(value = "search")
	@Action(value="/changeStreetRate-showSearchResults" ,results = { @Result(name = RESULTS, location="admin/changeStreetRate-results.jsp") })
	public String showSearchResults() {
		return searchForCategories();
	}

	private String searchForCategories() {

		LOGGER.debug("searchForCategories, areaId:" + areaId);

		//boundary = (Boundary) getPersistenceService().find("from BoundaryImpl b where b.id=?", areaId);
	
		
	 if(areaId!=null){
		boundary =boundaryService.getBoundaryById(areaId.longValue());
	  }
	
	try {
			List<Category> list = boundaryCategoryDAO.getCategoriesByBoundry(boundary);
			Map<String, String> fields = null;
			for (Category cat : list) {
				if (cat != null) {
					fields = new HashMap<String, String>();
					if (cat.getPropUsage() != null) {
						fields.put("usageFactor", cat.getPropUsage().getUsageName());
					}
					if (cat.getStructureClass() != null) {
						fields.put("structFactor", cat.getStructureClass().getTypeName());
					}
					fields.put("currentRate", cat.getCategoryAmount().toString());
					fields.put("currLocFactor", cat.getCategoryName());
				}
				readOnlyFields.add(fields);
			}
			if (boundary != null) {
				LOGGER.debug(readOnlyFields.size() + " Categories for " + boundary.getName());
			} else {
				LOGGER.debug("boundary is NULL");
			}

			setSearchValue(boundary.getName());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ApplicationRuntimeException("Exception : " + e);
		}
		LOGGER.debug("Exit from search method");

		return RESULTS;

	}

	@Action(value="/changeStreetRate-editPage" ,results = { @Result(name = EDIT, location="admin/changeStreetRate-edit.jsp") })
	public String editPage() {
		LOGGER.debug("ChangeStreetRateAction-editPage");
		LOGGER.debug("Exit from editPage");
		return EDIT;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = "edit")
	@Action(value="/changeStreetRate-saveData",results = { @Result(name = ACK, location="admin/changeStreetRate-ack.jsp") })
	public String saveData() {

		LOGGER.debug("saveData : areaId:" + areaId + ", Current Location Factor:" + currLocFactor + ", Current Rate :"
				+ currentRate + "Revised Location Factor: " + revisedLocFactor + "Revised Rate : " + revisedRate);
	
		if (areaId != null)
			boundary = boundaryService.getBoundaryById(areaId.longValue());
		
		Category catOld = (Category) getPersistenceService().find(
				"from Category c where c.categoryName=? and c.categoryAmount=?", currLocFactor, currentRate);
		LOGGER.debug("saveData : Category for CurrentLocationFactor: " + currLocFactor + "&" + "CurrentRate : "
				+ catOld);
		Category catLocFactor = (Category) getPersistenceService().find("from Category c where c.id=?",
				Long.parseLong(revisedLocFactor));
		LOGGER.debug("saveData : Category for RevisedLocationFactor: " + revisedLocFactor + ": " + catLocFactor);
		Category catRevised = null;
		if (catLocFactor != null) {
			catRevised = (Category) getPersistenceService().find(
					"from Category c where c.categoryName=? and c.categoryAmount=?", catLocFactor.getCategoryName(),
					revisedRate);
			LOGGER.debug("saveData : Revised Category: " + catRevised);
		}
		BoundaryCategory bc = null;
		if (catOld != null && boundary != null) {
			bc = (BoundaryCategory) getPersistenceService().find(
					"from BoundaryCategory bc where bc.bndry=? and bc.category=?", boundary, catOld);
			LOGGER.debug("saveData : BoundaryCategory for Category: " + catOld + " is " + bc);
		}

		if (bc != null) {
			bc.setCategory(catRevised);
            boundaryCategoryDAO.update(bc);
		}
		LOGGER.debug("saveData : BoundaryCategory after changing Category: " + bc);
		LOGGER.debug("Exit from saveData");
		return ACK;

	}

	@Override
	public void validate() {

		LOGGER.debug("Entered into validate \n zoneId: " + zoneId + "wardId: " + wardId + " areaId: " + areaId
				+ "revisedRate:" + revisedRate);
		if (zoneId != null && zoneId == -1) {
			addActionError(getText("mandatory.zone"));
		}

		if (wardId != null && wardId == -1) {
			addActionError(getText("mandatory.ward"));
		}

		if (areaId != null && areaId == -1) {
			addActionError(getText("mandatory.area"));
		}

		if (saveAction != null && StringUtils.equals(saveAction, "saveData")) {

			if (revisedRate == null || revisedRate == 0.0) {
				addActionError(getText("mandatory.revisedRate"));
			} else {
				Pattern p = Pattern.compile("[^0-9.]");
				Matcher m = p.matcher(revisedRate.toString());
				if (m.find()) {
					addActionError(getText("mandatory.validRevisedRate"));
				}
			}

			if ((revisedRate != null && revisedRate > 0)
					&& (revisedLocFactor == null || StringUtils.isEmpty(revisedLocFactor) || StringUtils.equals(
							revisedLocFactor, "-1"))) {
				addActionError(getText("mandatory.revisedLocFactor"));
			}
		}

		LOGGER.debug("Exit from validate");
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getUsageFactor() {
		return usageFactor;
	}

	public void setUsageFactor(String usageFactor) {
		this.usageFactor = usageFactor;
	}

	public String getStructFactor() {
		return structFactor;
	}

	public void setStructFactor(String structFactor) {
		this.structFactor = structFactor;
	}

	public Float getCurrentRate() {
		return currentRate;
	}

	public void setCurrentRate(Float currentRate) {
		this.currentRate = currentRate;
	}

	public String getCurrLocFactor() {
		return currLocFactor;
	}

	public void setCurrLocFactor(String currLocFactor) {
		this.currLocFactor = currLocFactor;
	}

	public Float getRevisedRate() {
		return revisedRate;
	}

	public void setRevisedRate(Float revisedRate) {
		this.revisedRate = revisedRate;
	}

	public String getRevisedLocFactor() {
		return revisedLocFactor;
	}

	public void setRevisedLocFactor(String revisedLocFactor) {
		this.revisedLocFactor = revisedLocFactor;
	}

	public List<Map<String, String>> getReadOnlyFields() {
		return readOnlyFields;
	}

	public void setReadOnlyFields(List<Map<String, String>> readOnlyFields) {
		this.readOnlyFields = readOnlyFields;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public String getSaveAction() {
		return saveAction;
	}

	public void setSaveAction(String saveAction) {
		this.saveAction = saveAction;
	}

}
