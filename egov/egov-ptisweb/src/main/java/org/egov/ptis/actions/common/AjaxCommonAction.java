/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.common;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.AREA_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_CONSTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import static org.egov.web.actions.BaseFormAction.NEW;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Functionary;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.service.EisUtilService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.CategoryDao;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.web.actions.BaseFormAction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("serial")
@ParentPackage("egov")
/*
 * @Results({ @Result(name = "AJAX_RESULT", type = "Stream", location =
 * "returnStream", params = { "contentType", "text/plain" }) })
 */
@Transactional(readOnly = true)
@Namespace("/common")
@ResultPath("/WEB-INF/jsp/common/")
@Results({
    @Result(name="ward",location="ajaxCommon-ward.jsp") ,
    @Result(name="street",location="ajaxCommon-street.jsp")  
  })
public class AjaxCommonAction extends BaseFormAction {

	private static final String AJAX_RESULT = "AJAX_RESULT";
	private static final String CATEGORY = "category";
	private static final String FAILURE = "failure";
	private static final String USAGE = "usage";
	private static final String PROP_TYPE_CATEGORY = "propCategory";
	private static final String RESULT_STRUCTURAL = "structural";
	private static final String RESULT_PART_NUMBER = "partNumber";
	private static final String WARD = "ward";

	private Long zoneId;
	private Long wardId;
	private Long areaId;
	private Integer departmentId;
	private Integer designationId;
	private Integer propTypeId;
	private String usageFactor;
	private String structFactor;
	private Float revisedRate;
	private List<Boundary> wardList;
	private List<Boundary> areaList;
	private List<Boundary> streetList;
	private List<PropertyUsage> propUsageList;
	private List<DesignationMaster> designationMasterList = new ArrayList<DesignationMaster>();
	private List<User> userList = new ArrayList<User>();
	private List<Category> categoryList;
	private List<StructureClassification> structuralClassifications;
	private String returnStream = "";
	private Map<String, String> propTypeCategoryMap = new TreeMap<String, String>();
	private Date completionOccupationDate;
	private Logger LOGGER = Logger.getLogger(getClass());
	private List<String> partNumbers;
	@Autowired
	private CategoryDao categoryDAO;

	@Override
	public Object getModel() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "/ajaxCommon-wardByZone")
	public String wardByZone() {
		LOGGER.debug("Entered into wardByZone, zoneId: " + zoneId);
		wardList = new ArrayList<Boundary>();
		wardList = getPersistenceService()
				.findAllBy("from Boundary BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.id ",
						"Ward", getZoneId());
		LOGGER.debug("Exiting from wardByZone, No of wards in zone: " + zoneId + "are "
				+ ((wardList != null) ? wardList : ZERO));
		return WARD;
	}
	
	@SuppressWarnings("unchecked")
	public String areaByWard() {
		LOGGER.debug("Entered into areaByWard, wardId: " + wardId);
		areaList = new ArrayList<Boundary>();
		areaList = getPersistenceService()
				.findAllBy(
						"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
						AREA_BNDRY_TYPE, getWardId());
		LOGGER.debug("Exiting from areaByWard, No of areas in ward: " + wardId + " are "
				+ ((areaList != null) ? areaList : ZERO));
		return "area";
	}

	@SuppressWarnings("unchecked")
	@Action(value = "/ajaxCommon-streetByWard")
	public String streetByWard() {
		LOGGER.debug("Entered into streetByWard, wardId: " + wardId);
		streetList = new ArrayList<Boundary>();
		streetList = getPersistenceService().findAllBy("select CH.child from CrossHeirarchyImpl CH where CH.parent.id = ? ",getWardId());
		LOGGER.debug("Exiting from streetByWard, No of streets in ward: " + wardId + " are "
				+ ((streetList != null) ? streetList : ZERO));
		return "street";
	}

	@SuppressWarnings("unchecked")
	public String populateDesignationsByDept() {
		LOGGER.debug("Entered into populateUsersByDesignation : departmentId : " + departmentId);
		if (departmentId != null) {
			// FIX ME
			// designationMasterList =
			// eisManager.getAllDesignationByDept(departmentId);
			designationMasterList = null;
		}

		LOGGER.debug("Exiting from populateUsersByDesignation : No of Designation : "
				+ ((designationMasterList != null) ? designationMasterList.size() : ZERO));

		return "designationList";
	}

	public String populateUsersByDesignation() {
		LOGGER.debug("Entered into populateUsersByDesignation : designationId : " + designationId);
		if (designationId != null) {
			Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Functionary.class);
			criteria.add(Restrictions.eq("name", "Assessment"));
			Functionary functionary = (Functionary) criteria.list().get(0);
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("designationId", designationId.toString());
			paramMap.put("functionaryId", functionary.getId().toString());
			EisUtilService eisService = new EisUtilService();
			eisService.setPersistenceService(persistenceService);
			List<EmployeeView> empInfoList = eisService.getEmployeeInfoList(paramMap);
			for (EmployeeView employeeView : empInfoList) {
				userList.add(employeeView.getUserMaster());
			}
		}
		LOGGER.debug("Exiting from populateUsersByDesignation : No of users : "
				+ ((userList != null) ? userList : ZERO));
		return "userList";
	}

	public String categoryByRateUsageAndStructClass() {

		LOGGER.debug("Entered into categoryByRateUsageAndStructClass method, Usage Factor: "
				+ usageFactor + ", Structure Classification: " + structFactor);

		PropertyUsage propUsage = (PropertyUsage) getPersistenceService().find(
				"from PropertyUsage pu where pu.usageName=?", usageFactor);
		StructureClassification structureClass = (StructureClassification) getPersistenceService()
				.find("from StructureClassification sc where sc.typeName=?", structFactor);

		if (propUsage != null && structureClass != null && revisedRate != null) {
			Criterion usgId = null;
			Criterion classId = null;
			Criterion catAmt = null;
			Conjunction conjunction = Restrictions.conjunction();
			usgId = Restrictions.eq("propUsage", propUsage);
			classId = Restrictions.eq("structureClass", structureClass);
			catAmt = Restrictions.eq("categoryAmount", revisedRate);
			conjunction.add(usgId);
			conjunction.add(classId);
			conjunction.add(catAmt);

			Criterion criterion = conjunction;
			categoryList = categoryDAO.getCategoryByRateUsageAndStructClass(criterion);

		}

		addDropdownData("categoryList", categoryList);
		LOGGER.debug("Exiting from categoryByRateUsageAndStructClass method");
		if (categoryList == null) {
			LOGGER.debug("categoryByRateUsageAndStructClass: categoryList is NULL \n Exiting from categoryByRateUsageAndStructClass");
			return FAILURE;
		} else {
			LOGGER.debug("categoryByRateUsageAndStructClass: categoryList:" + categoryList
					+ "\nExiting from categoryByRateUsageAndStructClass");
			return CATEGORY;
		}
	}

	public String usageByPropType() {
		LOGGER.debug("Entered into usageByPropType, propTypeId: " + propTypeId);
		setPropUsageList(CommonServices.usagesForPropType(propTypeId));
		LOGGER.debug("Exiting from usageByPropType, No of Usages: "
				+ ((propUsageList != null) ? propUsageList : ZERO));
		return USAGE;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "/ajaxCommon-propTypeCategoryByPropType", results = { @Result(name = PROP_TYPE_CATEGORY, location = "/ajaxCommon-propCategory.jsp") })
	public String propTypeCategoryByPropType() {
		LOGGER.debug("Entered into propTypeCategoryByPropType, propTypeId: " + propTypeId);
		PropertyTypeMaster propType = (PropertyTypeMaster) getPersistenceService().find(
				"from PropertyTypeMaster ptm where ptm.id=?", propTypeId.longValue());
		if (propType != null) {
			if (propType.getCode().equalsIgnoreCase(PROPTYPE_RESD)) {
				propTypeCategoryMap.putAll(PropertyTaxConstants.RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_NON_RESD)) {
				propTypeCategoryMap
						.putAll(PropertyTaxConstants.NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY);
			} else if (propType.getCode().equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
				propTypeCategoryMap.putAll(PropertyTaxConstants.OPEN_PLOT_PROPERTY_TYPE_CATEGORY);

			}
			setPropTypeCategoryMap(propTypeCategoryMap);
		} else {
			LOGGER.debug("propTypeCategoryByPropType: NULL -> propType is null");
		}
		LOGGER.debug("Exiting from propTypeCategoryByPropType, No of Categories: "
				+ ((propTypeCategoryMap != null) ? propTypeCategoryMap.size() : ZERO));
		return PROP_TYPE_CATEGORY;
	}

	public String locationFactorsByWard() {
		LOGGER.debug("Entered into locationFactorsByWard, wardId: " + wardId);

		categoryList = new ArrayList<Category>();
		categoryList.addAll(getPersistenceService().findAllBy(
				"select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
						+ "and bc.category.propUsage = null and bc.category.structureClass = null",
				wardId));

		LOGGER.debug("locationFactorsByWard: categories - " + categoryList);
		LOGGER.debug("Exiting from locationFactorsByWard");

		return CATEGORY;
	}

	public String populateStructuralClassifications() {
		LOGGER.debug("Entered into getStructureClassifications, Date: " + completionOccupationDate);
		structuralClassifications = new ArrayList<StructureClassification>();
		try {
			if (completionOccupationDate.after(new SimpleDateFormat(
					PropertyTaxConstants.DATE_FORMAT_DDMMYYY).parse(DATE_CONSTANT))) {
				LOGGER.debug("Base Rate - Structural Factors");
				structuralClassifications.addAll(getPersistenceService().findAllBy(
						"from StructureClassification where code like 'R%'"));
			} else {
				LOGGER.debug("Rent Chart - Structural Factors");
				structuralClassifications.addAll(getPersistenceService().findAllBy(
						"from StructureClassification where code like 'R%'"));
			}
		} catch (ParseException pe) {
			LOGGER.error("Error while parsing Floor Completion / occupation", pe);
			throw new EGOVRuntimeException("Error while parsing Floor Completion / occupation", pe);
		}
		Collections.sort(structuralClassifications, new Comparator() {
			@Override
			public int compare(Object object1, Object object2) {
				return ((StructureClassification) object1).getTypeName().compareTo(
						((StructureClassification) object2).getTypeName());
			}
		});
		LOGGER.info("getStructureClassifications - Structural Factors : "
				+ structuralClassifications);
		LOGGER.debug("Exiting from getStructureClassifications");
		return RESULT_STRUCTURAL;
	}

	public String partNumbersByWard() {
		LOGGER.debug("Entered into areaByWard, wardId: " + wardId);
		partNumbers = new ArrayList<String>();
		partNumbers = getPersistenceService().findAllBy(
				"SELECT distinct pmv.partNo FROM PropertyMaterlizeView pmv "
						+ "WHERE pmv.ward.id = ? order by pmv.partNo asc", getWardId());

		LOGGER.debug("Exiting from partNumbersByWard");
		return RESULT_PART_NUMBER;
	}

	
	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
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

	public Float getRevisedRate() {
		return revisedRate;
	}

	public void setRevisedRate(Float revisedRate) {
		this.revisedRate = revisedRate;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<Boundary> getWardList() {
		return wardList;
	}

	public void setWardList(List<Boundary> wardList) {
		this.wardList = wardList;
	}

	public List<Boundary> getStreetList() {
		return streetList;
	}

	public void setStreetList(List<Boundary> streetList) {
		this.streetList = streetList;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List<DesignationMaster> getDesignationMasterList() {
		return designationMasterList;
	}

	public void setDesignationMasterList(List<DesignationMaster> designationMasterList) {
		this.designationMasterList = designationMasterList;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public List<User> getUserList() {
		return userList;
	}

	public String getReturnStream() {
		return returnStream;
	}

	public void setReturnStream(String returnStream) {
		this.returnStream = returnStream;
	}

	public Integer getPropTypeId() {
		return propTypeId;
	}

	public void setPropTypeId(Integer propTypeId) {
		this.propTypeId = propTypeId;
	}

	public List<PropertyUsage> getPropUsageList() {
		return propUsageList;
	}

	public void setPropUsageList(List<PropertyUsage> propUsageList) {
		this.propUsageList = propUsageList;
	}

	public Map<String, String> getPropTypeCategoryMap() {
		return propTypeCategoryMap;
	}

	public void setPropTypeCategoryMap(Map<String, String> propTypeCategoryMap) {
		this.propTypeCategoryMap = propTypeCategoryMap;
	}

	public Date getCompletionOccupationDate() {
		return completionOccupationDate;
	}

	public void setCompletionOccupationDate(Date completionOccupationDate) {
		this.completionOccupationDate = completionOccupationDate;
	}

	public List<StructureClassification> getStructuralClassifications() {
		return structuralClassifications;
	}

	public void setStructuralClassifications(List<StructureClassification> structuralClassifications) {
		this.structuralClassifications = structuralClassifications;
	}

	public List<String> getPartNumbers() {
		return partNumbers;
	}

	public void setPartNumbers(List<String> partNumbers) {
		this.partNumbers = partNumbers;
	}

}
