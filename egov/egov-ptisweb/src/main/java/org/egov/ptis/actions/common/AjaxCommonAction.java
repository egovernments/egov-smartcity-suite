/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.actions.common;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_CONSTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.repository.UserRepository;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.commons.Designation;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.CategoryDao;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyDepartment;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.model.MutationFeeDetails;
import org.egov.ptis.domain.repository.PropertyDepartmentRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Namespace("/common")
@ResultPath("/WEB-INF/jsp/common/")
@Results({ @Result(name = "ward", location = "ajaxCommon-ward.jsp"),
        @Result(name = "street", location = "ajaxCommon-street.jsp"),
        @Result(name = "area", location = "ajaxCommon-area.jsp"),
        @Result(name = "category", location = "ajaxCommon-category.jsp"),
        @Result(name = "structural", location = "ajaxCommon-structural.jsp"),
        @Result(name = "designationList", location = "ajaxCommon-designationList.jsp"),
        @Result(name = "userList", location = "ajaxCommon-userList.jsp"),
        @Result(name = "propCategory", location = "ajaxCommon-propCategory.jsp"),
        @Result(name = "checkExistingCategory", location = "ajaxCommon-checkExistingCategory.jsp"),
        @Result(name = "usage", location = "ajaxCommon-usage.jsp"),
        @Result(name = "calculateMutationFee", location = "ajaxCommon-calculateMutationFee.jsp"),
        @Result(name = "propDepartment", location = "ajaxcommon-propdepartment.jsp"),
        @Result(name = "defaultcitizen", location = "ajaxcommon-citizenfordoctype.jsp")})
public class AjaxCommonAction extends BaseFormAction implements ServletResponseAware {

    private static final String AJAX_RESULT = "AJAX_RESULT";
    private static final String CATEGORY = "category";
    private static final String FAILURE = "failure";
    private static final String USAGE = "usage";
    private static final String PROP_TYPE_CATEGORY = "propCategory";
    private static final String RESULT_STRUCTURAL = "structural";
    private static final String RESULT_PART_NUMBER = "partNumber";
    private static final String WARD = "ward";
    private static final String AREA = "area";
    private static final String RESULT_CHECK_EXISTING_CATEGORY = "checkExistingCategory";
    private static final String RESULT_MUTATION_FEE = "calculateMutationFee";
    private static final String DEFAULT_CITIZEN_NAME = "The Holder Of The Premises";
    
    private Long zoneId;
    private Long wardId;
    private Long areaId;
    private Long locality;
    private Long departmentId;
    private Long designationId;
    private Integer propTypeId;
    private String usageFactor;
    private String structFactor;
    private Float revisedRate;
    private List<Boundary> wardList;
    private List<Boundary> areaList;
    private List<Boundary> streetList;
    private List<PropertyUsage> propUsageList;
    private List<Designation> designationMasterList = new ArrayList<Designation>();
    private List<User> userList = new ArrayList<User>();
    private List<Category> categoryList;
    private List<StructureClassification> structuralClassifications;
    private String returnStream = "";
    private Map<String, String> propTypeCategoryMap = new TreeMap<String, String>();
    private Date completionOccupationDate;
    private Logger LOGGER = Logger.getLogger(getClass());
    private List<String> partNumbers;
    private HttpServletResponse response;
    private List<Assignment> assignmentList;
    private String currentStatusCode;
    private String mobileNumber;
    private String categoryExists = "no";
    private String assessmentNo;
    private Long usageId;
    private Long structureClassId;
    private Date categoryFromDate;
    private String validationMessage = "";
    private String propTypeCategory;
    private BigDecimal partyValue;
    private BigDecimal departmentValue;
    private BigDecimal mutationFee = BigDecimal.ZERO;
    private List<PropertyDepartment> propertyDepartmentList;
    private List<DocumentType> assessmentDocumentList;
    private String assessmentDocumentType = "";
    private User defaultCitizen;
    
    @Autowired
    private CategoryDao categoryDAO;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private CrossHierarchyService crossHierarchyService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;
    @Autowired
    private transient PropertyDepartmentRepository propertyDepartmentRepository;
    @Autowired
    private transient PropertyService propService;
    @Autowired
    private transient UserRepository userRepository;

    @Override
    public Object getModel() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-wardByZone")
    public String wardByZone() {
        LOGGER.debug("Entered into wardByZone, zoneId: " + zoneId);
        wardList = new ArrayList<Boundary>();
        wardList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
        LOGGER.debug("Exiting from wardByZone, No of wards in zone: " + zoneId + "are "
                + ((wardList != null) ? wardList : ZERO));
        return WARD;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-areaByWard")
    public String areaByWard() {
        LOGGER.debug("Entered into areaByWard, wardId: " + wardId);
        areaList = new ArrayList<Boundary>();
        areaList = boundaryService.getActiveChildBoundariesByBoundaryId(getWardId());
        LOGGER.debug("Exiting from areaByWard, No of areas in ward: " + wardId + " are "
                + ((areaList != null) ? areaList : ZERO));
        return AREA;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-streetByWard")
    public String streetByWard() {
        LOGGER.debug("Entered into streetByWard, wardId: " + wardId);
        streetList = new ArrayList<Boundary>();
        streetList = getPersistenceService().findAllBy(
                "select CH.child from CrossHierarchy CH where CH.parent.id = ? ", getWardId());
        LOGGER.debug("Exiting from streetByWard, No of streets in ward: " + wardId + " are "
                + ((streetList != null) ? streetList : ZERO));
        return "street";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-populateDesignationsByDept")
    public String populateDesignationsByDept() {
        LOGGER.debug("Entered into populateUsersByDesignation : departmentId : " + departmentId);
        if (departmentId != null) {
            Designation designation = assignmentService.getPrimaryAssignmentForUser(
                    securityUtils.getCurrentUser().getId()).getDesignation();
            if (designation.getName().equals(ASSISTANT_DESGN)) {
                designationMasterList.add(designationService.getDesignationByName(REVENUE_OFFICER_DESGN));
            } else if (designation.getName().equals(REVENUE_OFFICER_DESGN)) {
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
            }
        }

        LOGGER.debug("Exiting from populateUsersByDesignation : No of Designation : "
                + ((designationMasterList != null) ? designationMasterList.size() : ZERO));

        return "designationList";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-populateDesignationsByDeptForRevisionPetition")
    public String populateDesignationsByDeptForRevisionPetition() {
        LOGGER.debug("Entered into populateUsersByDesignation : departmentId : " + departmentId + currentStatusCode);
        if (departmentId != null) {
            // designationMasterList =
            // designationService.getAllDesignationByDepartment(departmentId,new
            // Date());
            Designation designation = assignmentService.getPrimaryAssignmentForUser(
                    securityUtils.getCurrentUser().getId()).getDesignation();
            if (currentStatusCode == null || "".equals(currentStatusCode)) {
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
            } else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_CREATED)) {
                designationMasterList.add(designationService.getDesignationByName(ASSISTANT_DESGN));
            } else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_HEARING_FIXED)) {
                designationMasterList.add(designationService.getDesignationByName(REVENUE_INSPECTOR_DESGN));
            } else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED)) {//
                designationMasterList.add(designationService.getDesignationByName(REVENUE_OFFICER_DESGN));
            } else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_INSPECTION_COMPLETED)) {
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
            } else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_INSPECTION_VERIFY)) {
                designationMasterList.add(designationService.getDesignationByName(ASSISTANT_DESGN));
            } else if (designation.getName().equals(ASSISTANT_DESGN)) {
                designationMasterList.add(designationService.getDesignationByName(REVENUE_OFFICER_DESGN));
            } else if (designation.getName().equals(REVENUE_OFFICER_DESGN)) {
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
            }
        }

        LOGGER.debug("Exiting from populateUsersByDesignation : No of Designation : "
                + ((designationMasterList != null) ? designationMasterList.size() : ZERO));

        return "designationList";
    }

    @Action(value = "/ajaxCommon-populateUsersByDeptAndDesignation")
    public String populateUsersByDeptAndDesignation() {
        LOGGER.debug("Entered into populateUsersByDesignation : designationId : " + designationId);
        if (designationId != null && departmentId != null) {
            assignmentList = assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(departmentId,
                    designationId, new Date());
        }
        LOGGER.debug("Exiting from populateUsersByDesignation : No of users : "
                + ((userList != null) ? userList : ZERO));
        return "userList";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-categoryByRateUsageAndStructClass")
    public String categoryByRateUsageAndStructClass() {

        LOGGER.debug("Entered into categoryByRateUsageAndStructClass method, Usage Factor: " + usageFactor
                + ", Structure Classification: " + structFactor);

        PropertyUsage propUsage = (PropertyUsage) getPersistenceService().find(
                "from PropertyUsage pu where pu.usageName=?", usageFactor);
        StructureClassification structureClass = (StructureClassification) getPersistenceService().find(
                "from StructureClassification sc where sc.typeName=?", structFactor);

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

    @SuppressWarnings("unchecked")
    @Actions({ 
    	@Action(value = "/ajaxCommon-propTypeCategoryByPropType"), 
    	@Action(value = "/public/ajaxCommon-propTypeCategoryByPropType")})
    public String propTypeCategoryByPropType() {
        LOGGER.debug("Entered into propTypeCategoryByPropType, propTypeId: " + propTypeId);
        PropertyTypeMaster propType = (PropertyTypeMaster) getPersistenceService().find(
                "from PropertyTypeMaster ptm where ptm.id=?", propTypeId.longValue());
        if (propType != null) {
            if (propType.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
                propTypeCategoryMap.putAll(VAC_LAND_PROPERTY_TYPE_CATEGORY);
            } else {
                propTypeCategoryMap.putAll(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
            }
            setPropTypeCategoryMap(propTypeCategoryMap);
        } else {
            LOGGER.debug("propTypeCategoryByPropType: NULL -> propType is null");
        }
        LOGGER.debug("Exiting from propTypeCategoryByPropType, No of Categories: "
                + ((propTypeCategoryMap != null) ? propTypeCategoryMap.size() : ZERO));
        return PROP_TYPE_CATEGORY;
    }

    @Action(value = "/ajaxCommon-locationFactorsByWard")
    public String locationFactorsByWard() {
        LOGGER.debug("Entered into locationFactorsByWard, wardId: " + wardId);

        categoryList = new ArrayList<Category>();
        categoryList.addAll(getPersistenceService().findAllBy(
                "select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
                        + "and bc.category.propUsage = null and bc.category.structureClass = null", wardId));

        LOGGER.debug("locationFactorsByWard: categories - " + categoryList);
        LOGGER.debug("Exiting from locationFactorsByWard");

        return CATEGORY;
    }

    @Action(value = "/ajaxCommon-populateStructuralClassifications")
    public String populateStructuralClassifications() {
        LOGGER.debug("Entered into getStructureClassifications, Date: " + completionOccupationDate);
        structuralClassifications = new ArrayList<StructureClassification>();
        try {
            if (completionOccupationDate.after(new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY)
                    .parse(DATE_CONSTANT))) {
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
            throw new ApplicationRuntimeException("Error while parsing Floor Completion / occupation", pe);
        }
        Collections.sort(structuralClassifications, new Comparator() {
            @Override
            public int compare(Object object1, Object object2) {
                return ((StructureClassification) object1).getTypeName().compareTo(
                        ((StructureClassification) object2).getTypeName());
            }
        });
        LOGGER.info("getStructureClassifications - Structural Factors : " + structuralClassifications);
        LOGGER.debug("Exiting from getStructureClassifications");
        return RESULT_STRUCTURAL;
    }

    @Actions({ 
    	@Action(value = "/ajaxCommon-getUserByMobileNo"), 
    	@Action(value = "/public/ajaxCommon-getUserByMobileNo")})
    public void getUserByMobileNo() throws IOException {
        if (StringUtils.isNotBlank(mobileNumber)) {
            final User user = (User) getPersistenceService().find("From User where mobileNumber = ?", mobileNumber);
            final JSONObject jsonObject = new JSONObject();
            if (null != user) {
                jsonObject.put("exists", Boolean.TRUE);
                jsonObject.put("name", user.getName());
                jsonObject.put("mobileNumber", user.getMobileNumber());
                jsonObject.put("salutaion", user.getSalutation());
                jsonObject.put("gender", user.getGender());
                jsonObject.put("email", user.getEmailId());
                jsonObject.put("guardian", user.getGuardian());
                jsonObject.put("guardianRelarion", user.getGuardianRelation());
            }
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            IOUtils.write(jsonObject.toString(), response.getWriter());
        }
    }

    @Action(value = "/ajaxCommon-checkIfCategoryExists")
    public String checkIfCategoryExists() {
        LOGGER.debug("Entered into checkIfCategoryExists ");
        Category existingCategory = (Category) getPersistenceService().find(
                "select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
                        + "and bc.category.propUsage.id = ? and bc.category.structureClass.id = ? and bc.category.fromDate = ? and bc.category.isActive = true ",
                zoneId, usageId, structureClassId, categoryFromDate);
        if (existingCategory != null) {
            categoryExists = "yes";
            validationMessage = getText("unit.rate.exists.for.combination", new String[] { existingCategory.getCategoryAmount()
                    .toString() });
        }
        return RESULT_CHECK_EXISTING_CATEGORY;
    }

    @Actions({ 
    	@Action(value = "/ajaxCommon-usageByPropType"), 
    	@Action(value = "/public/ajaxCommon-usageByPropType")})
    public String usageByPropType() {
        LOGGER.debug("Entered into usageByPropType, propTypeId: " + propTypeId);
        if (propTypeCategory.equals(CATEGORY_MIXED))
            propUsageList = getPersistenceService().findAllBy("From PropertyUsage where isActive = true order by usageName ");
        else if (propTypeCategory.equals(CATEGORY_RESIDENTIAL))
            propUsageList = getPersistenceService()
                    .findAllBy("From PropertyUsage where isResidential = true and isActive = true  order by usageName ");
        else if (propTypeCategory.equals(CATEGORY_NON_RESIDENTIAL))
            propUsageList = getPersistenceService().findAllBy(
                    "From PropertyUsage where isResidential = false and isActive = true  order by usageName ");
        LOGGER.debug("Exiting from usageByPropType, No of Usages: " + ((propUsageList != null) ? propUsageList : ZERO));
        return USAGE;
    }
    
    @Action(value = "/ajaxCommon-checkIfPropertyExists")
    public void checkIfPropertyExists() throws IOException {
        if (StringUtils.isNotBlank(assessmentNo)) {
            final BasicProperty basicProperty = (BasicProperty) getPersistenceService().find("from BasicPropertyImpl bp where bp.oldMuncipalNum=? and bp.active='Y'",
                    assessmentNo);
            final JSONObject jsonObject = new JSONObject();
            if (null != basicProperty) {
                jsonObject.put("exists", Boolean.TRUE);
            }
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            IOUtils.write(jsonObject.toString(), response.getWriter());
        }
    }
    
    /**
     * API to calculate Mutation Fee dynamically
     * @return
     */
    @Action(value = "/ajaxCommon-calculateMutationFee")
    public String calculateMutationFee(){
    	// Maximum among partyValue and departmentValue will be considered as the documentValue
    	BigDecimal documentValue = (partyValue.compareTo(departmentValue) > 0 ? partyValue : departmentValue);
    	
    	if(documentValue.compareTo(BigDecimal.ZERO) > 0){
    		BigDecimal excessDocValue = BigDecimal.ZERO;
    		BigDecimal multiplicationFactor = BigDecimal.ZERO;
                MutationFeeDetails mutationFeeDetails = (MutationFeeDetails) getPersistenceService().find(
                    "from MutationFeeDetails where lowLimit <= ? and (highLimit is null OR highLimit >= ?) and toDate > now()",
                    documentValue, documentValue);
            if(mutationFeeDetails != null){
    			if(mutationFeeDetails.getFlatAmount() != null && mutationFeeDetails.getFlatAmount().compareTo(BigDecimal.ZERO) > 0){
    				if(mutationFeeDetails.getIsRecursive().toString().equalsIgnoreCase("N")){
    					mutationFee = mutationFeeDetails.getFlatAmount();
    				}else{
    					excessDocValue = documentValue.subtract(mutationFeeDetails.getLowLimit()).add(BigDecimal.ONE);
    					multiplicationFactor = excessDocValue.divide(mutationFeeDetails.getRecursiveFactor(), BigDecimal.ROUND_CEILING);
    					mutationFee = mutationFeeDetails.getFlatAmount().add(multiplicationFactor.multiply(mutationFeeDetails.getRecursiveAmount()));
    				}
    			}
    			if(mutationFeeDetails.getPercentage() != null && mutationFeeDetails.getPercentage().compareTo(BigDecimal.ZERO) > 0){
    				if(mutationFeeDetails.getIsRecursive().toString().equalsIgnoreCase("N")){
    					mutationFee = (documentValue.multiply(mutationFeeDetails.getPercentage())).divide(PropertyTaxConstants.BIGDECIMAL_100);
    				}
    			}
    			mutationFee = mutationFee.setScale(0, BigDecimal.ROUND_HALF_UP);
    		}
    	}
    	return RESULT_MUTATION_FEE;
    }
    
    @Actions({ @Action(value = "/ajaxcommon-propdepartment-byproptype"),
            @Action(value = "/public/ajaxcommon-propdepartment-byproptype") })
    public String getPropDepartmentByPropType() {
        if (propTypeId != null) {
            PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService()
                    .find("from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT))
                propertyDepartmentList = propertyDepartmentRepository.getAllStateDepartments();
            else if (propTypeMstr.getCode().startsWith("CENTRAL_GOVT"))
                propertyDepartmentList = propertyDepartmentRepository.getAllCentralDepartments();
        }
        setPropertyDepartmentList(propertyDepartmentList);
        return "propDepartment";
    }
    
    @Actions({ @Action(value = "/ajaxcommon-defaultcitizen-fordoctype"),
            @Action(value = "/public/ajaxcommon-defaultcitizen-fordoctype") })
    public String getDefaultCitizenForDoctype() {

        if (assessmentDocumentType != null) {
            final User user = userRepository.findByUsername(DEFAULT_CITIZEN_NAME);
            defaultCitizen.setName(user.getName());
            defaultCitizen.setMobileNumber(user.getMobileNumber());
            defaultCitizen.setGender(user.getGender());
            defaultCitizen.setGuardian(user.getGuardian());
            defaultCitizen.setGuardianRelation(user.getGuardianRelation());
        }
        return "defaultcitizen";
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public List<Designation> getDesignationMasterList() {
        return designationMasterList;
    }

    public void setDesignationMasterList(List<Designation> designationMasterList) {
        this.designationMasterList = designationMasterList;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
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

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }

    public DesignationService getDesignationService() {
        return designationService;
    }

    public void setDesignationService(DesignationService designationService) {
        this.designationService = designationService;
    }

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    public void setAssignmentService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public void setSecurityUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public String getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(String currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public List<Boundary> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Boundary> areaList) {
        this.areaList = areaList;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCategoryExists() {
        return categoryExists;
    }

    public void setCategoryExists(String categoryExists) {
        this.categoryExists = categoryExists;
    }

    public Long getUsageId() {
        return usageId;
    }

    public void setUsageId(Long usageId) {
        this.usageId = usageId;
    }

    public Long getStructureClassId() {
        return structureClassId;
    }

    public void setStructureClassId(Long structureClassId) {
        this.structureClassId = structureClassId;
    }

    public Date getCategoryFromDate() {
        return categoryFromDate;
    }

    public void setCategoryFromDate(Date categoryFromDate) {
        this.categoryFromDate = categoryFromDate;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getPropTypeCategory() {
        return propTypeCategory;
    }

    public void setPropTypeCategory(String propTypeCategory) {
        this.propTypeCategory = propTypeCategory;
    }

    public void setCategoryDAO(CategoryDao categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void setBoundaryService(BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCrossHierarchyService(CrossHierarchyService crossHierarchyService) {
        this.crossHierarchyService = crossHierarchyService;
    }

    public void setBoundaryTypeService(BoundaryTypeService boundaryTypeService) {
        this.boundaryTypeService = boundaryTypeService;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public BigDecimal getPartyValue() {
        return partyValue;
    }

    public void setPartyValue(BigDecimal partyValue) {
        this.partyValue = partyValue;
    }

    public BigDecimal getDepartmentValue() {
        return departmentValue;
    }

    public void setDepartmentValue(BigDecimal departmentValue) {
        this.departmentValue = departmentValue;
    }

    public BigDecimal getMutationFee() {
        return mutationFee;
    }

    public void setMutationFee(BigDecimal mutationFee) {
        this.mutationFee = mutationFee;
    }

    public List<PropertyDepartment> getPropertyDepartmentList() {
        return propertyDepartmentList;
    }

    public void setPropertyDepartmentList(List<PropertyDepartment> propertyDepartmentList) {
        this.propertyDepartmentList = propertyDepartmentList;
    }

    public List<DocumentType> getAssessmentDocumentList() {
        return assessmentDocumentList;
    }

    public void setAssessmentDocumentList(List<DocumentType> assessmentDocumentList) {
        this.assessmentDocumentList = assessmentDocumentList;
    }

    public String getAssessmentDocumentType() {
        return assessmentDocumentType;
    }

    public void setAssessmentDocumentType(String assessmentDocumentType) {
        this.assessmentDocumentType = assessmentDocumentType;
    }

    public User getDefaultCitizen() {
        return defaultCitizen;
    }

    public void setDefaultCitizen(User defaultCitizen) {
        this.defaultCitizen = defaultCitizen;
    }

}