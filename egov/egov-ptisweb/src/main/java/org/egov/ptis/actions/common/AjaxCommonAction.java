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
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

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
        @Result(name = "defaultcitizen", location = "ajaxcommon-citizenfordoctype.jsp") })
public class AjaxCommonAction extends BaseFormAction implements ServletResponseAware {

    /**
     *
     */
    private static final long serialVersionUID = -2147988330701242191L;
    private static final String CATEGORY = "category";
    private static final String FAILURE = "failure";
    private static final String USAGE = "usage";
    private static final String PROP_TYPE_CATEGORY = "propCategory";
    private static final String RESULT_STRUCTURAL = "structural";
    private static final String WARD = "ward";
    private static final String AREA = "area";
    private static final String RESULT_CHECK_EXISTING_CATEGORY = "checkExistingCategory";
    private static final String RESULT_MUTATION_FEE = "calculateMutationFee";
    private static final String DEFAULT_CITIZEN_NAME = "The Holder Of The Premises";
    private static final String RECURSIVEFACTOR_N = "N";

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
    private List<Designation> designationMasterList = new ArrayList<>();
    private final List<User> userList = new ArrayList<>();
    private List<Category> categoryList;
    private List<StructureClassification> structuralClassifications;
    private String returnStream = "";
    private Map<String, String> propTypeCategoryMap = new TreeMap<>();
    private Date completionOccupationDate;
    private final Logger logger = Logger.getLogger(getClass());
    private List<String> partNumbers;
    private transient HttpServletResponse response;
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
    private BigDecimal mutationFee = ZERO;
    private List<PropertyDepartment> propertyDepartmentList;
    private List<DocumentType> assessmentDocumentList;
    private String assessmentDocumentType = "";
    private User defaultCitizen;
    private BigDecimal extentOfSite;
    private BigDecimal plingthArea;

    @Autowired
    private transient CategoryDao categoryDAO;
    @Autowired
    private transient BoundaryService boundaryService;
    @Autowired
    private transient DesignationService designationService;
    @Autowired
    private transient AssignmentService assignmentService;
    @Autowired
    private transient SecurityUtils securityUtils;
    @Autowired
    private transient PropertyDepartmentRepository propertyDepartmentRepository;
    @Autowired
    private transient UserRepository userRepository;

    @Override
    public Object getModel() {
        return null;
    }

    @Action(value = "/ajaxCommon-wardByZone")
    public String wardByZone() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into wardByZone, zoneId: " + zoneId);
        wardList = new ArrayList<>();
        wardList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
        if (logger.isDebugEnabled())
            logger.debug("Exiting from wardByZone, No of wards in zone: " + zoneId + "are "
                    + (wardList != null ? wardList : ZERO));
        return WARD;
    }

    @Action(value = "/ajaxCommon-areaByWard")
    public String areaByWard() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into areaByWard, wardId: " + wardId);
        areaList = new ArrayList<>();
        areaList = boundaryService.getActiveChildBoundariesByBoundaryId(getWardId());
        if (logger.isDebugEnabled())
            logger.debug("Exiting from areaByWard, No of areas in ward: " + wardId + " are "
                    + (areaList != null ? areaList : ZERO));
        return AREA;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-streetByWard")
    public String streetByWard() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into streetByWard, wardId: " + wardId);
        streetList = new ArrayList<>();
        streetList = getPersistenceService().findAllBy("select CH.child from CrossHierarchy CH where CH.parent.id = ? ",
                getWardId());
        if (logger.isDebugEnabled())
            logger.debug("Exiting from streetByWard, No of streets in ward: " + wardId + " are "
                    + (streetList != null ? streetList : ZERO));
        return "street";
    }

    @Action(value = "/ajaxCommon-populateDesignationsByDept")
    public String populateDesignationsByDept() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into populateUsersByDesignation : departmentId : " + departmentId);
        if (departmentId != null) {
            final Designation designation = assignmentService
                    .getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId()).getDesignation();
            if (designation.getName().equals(ASSISTANT_DESGN))
                designationMasterList.add(designationService.getDesignationByName(REVENUE_OFFICER_DESGN));
            else if (designation.getName().equals(REVENUE_OFFICER_DESGN))
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
        }

        if (logger.isDebugEnabled())
            logger.debug("Exiting from populateUsersByDesignation : No of Designation : "
                    + (designationMasterList != null ? designationMasterList.size() : ZERO));

        return "designationList";
    }

    @Action(value = "/ajaxCommon-populateDesignationsByDeptForRevisionPetition")
    public String populateDesignationsByDeptForRevisionPetition() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into populateUsersByDesignation : departmentId : " + departmentId + currentStatusCode);
        if (departmentId != null) {
            final Designation designation = assignmentService
                    .getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId()).getDesignation();
            if (currentStatusCode == null || "".equals(currentStatusCode))
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
            else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_CREATED))
                designationMasterList.add(designationService.getDesignationByName(ASSISTANT_DESGN));
            else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_HEARING_FIXED))
                designationMasterList.add(designationService.getDesignationByName(REVENUE_INSPECTOR_DESGN));
            else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_HEARING_COMPLETED))
                designationMasterList.add(designationService.getDesignationByName(REVENUE_OFFICER_DESGN));
            else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_INSPECTION_COMPLETED))
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
            else if (currentStatusCode != null && !"".equals(currentStatusCode)
                    && currentStatusCode.equals(PropertyTaxConstants.OBJECTION_INSPECTION_VERIFY))
                designationMasterList.add(designationService.getDesignationByName(ASSISTANT_DESGN));
            else if (designation.getName().equals(ASSISTANT_DESGN))
                designationMasterList.add(designationService.getDesignationByName(REVENUE_OFFICER_DESGN));
            else if (designation.getName().equals(REVENUE_OFFICER_DESGN))
                designationMasterList.add(designationService.getDesignationByName(COMMISSIONER_DESGN));
        }

        if (logger.isDebugEnabled())
            logger.debug("Exiting from populateUsersByDesignation : No of Designation : "
                    + (designationMasterList != null ? designationMasterList.size() : ZERO));

        return "designationList";
    }

    @Action(value = "/ajaxCommon-populateUsersByDeptAndDesignation")
    public String populateUsersByDeptAndDesignation() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into populateUsersByDesignation : designationId : " + designationId);
        if (designationId != null && departmentId != null)
            assignmentList = assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(departmentId,
                    designationId, new Date());
        if (logger.isDebugEnabled())
            logger.debug(
                    "Exiting from populateUsersByDesignation : No of users : " + (userList != null ? userList : ZERO));
        return "userList";
    }

    @Action(value = "/ajaxCommon-categoryByRateUsageAndStructClass")
    public String categoryByRateUsageAndStructClass() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into categoryByRateUsageAndStructClass method, Usage Factor: " + usageFactor
                    + ", Structure Classification: " + structFactor);

        final PropertyUsage propUsage = (PropertyUsage) getPersistenceService()
                .find("from PropertyUsage pu where pu.usageName=?", usageFactor);
        final StructureClassification structureClass = (StructureClassification) getPersistenceService()
                .find("from StructureClassification sc where sc.typeName=?", structFactor);

        if (propUsage != null && structureClass != null && revisedRate != null) {
            Criterion usgId;
            Criterion classId;
            Criterion catAmt;
            final Conjunction conjunction = Restrictions.conjunction();
            usgId = Restrictions.eq("propUsage", propUsage);
            classId = Restrictions.eq("structureClass", structureClass);
            catAmt = Restrictions.eq("categoryAmount", revisedRate);
            conjunction.add(usgId);
            conjunction.add(classId);
            conjunction.add(catAmt);

            final Criterion criterion = conjunction;
            categoryList = categoryDAO.getCategoryByRateUsageAndStructClass(criterion);

        }

        addDropdownData("categoryList", categoryList);
        if (logger.isDebugEnabled())
            logger.debug("Exiting from categoryByRateUsageAndStructClass method");
        if (categoryList == null) {
            if (logger.isDebugEnabled())
                logger.debug(
                        "categoryByRateUsageAndStructClass: categoryList is NULL \n Exiting from categoryByRateUsageAndStructClass");
            return FAILURE;
        } else {
            if (logger.isDebugEnabled())
                logger.debug("categoryByRateUsageAndStructClass: categoryList:" + categoryList
                        + "\nExiting from categoryByRateUsageAndStructClass");
            return CATEGORY;
        }
    }

    @Actions({ @Action(value = "/ajaxCommon-propTypeCategoryByPropType"),
            @Action(value = "/public/ajaxCommon-propTypeCategoryByPropType") })
    public String propTypeCategoryByPropType() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into propTypeCategoryByPropType, propTypeId: " + propTypeId);
        final PropertyTypeMaster propType = (PropertyTypeMaster) getPersistenceService()
                .find("from PropertyTypeMaster ptm where ptm.id=?", propTypeId.longValue());
        if (propType != null) {
            if (propType.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                propTypeCategoryMap.putAll(VAC_LAND_PROPERTY_TYPE_CATEGORY);
            else
                propTypeCategoryMap.putAll(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
            setPropTypeCategoryMap(propTypeCategoryMap);
        } else if (logger.isDebugEnabled())
            logger.debug("propTypeCategoryByPropType: NULL -> propType is null");
        if (logger.isDebugEnabled())
            logger.debug("Exiting from propTypeCategoryByPropType, No of Categories: "
                    + (propTypeCategoryMap != null ? propTypeCategoryMap.size() : ZERO));
        return PROP_TYPE_CATEGORY;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-locationFactorsByWard")
    public String locationFactorsByWard() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into locationFactorsByWard, wardId: " + wardId);

        categoryList = new ArrayList<>();
        categoryList.addAll(
                getPersistenceService().findAllBy("select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
                        + "and bc.category.propUsage = null and bc.category.structureClass = null", wardId));

        if (logger.isDebugEnabled()) {
            logger.debug("locationFactorsByWard: categories - " + categoryList);
            logger.debug("Exiting from locationFactorsByWard");
        }

        return CATEGORY;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/ajaxCommon-populateStructuralClassifications")
    public String populateStructuralClassifications() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into getStructureClassifications, Date: " + completionOccupationDate);
        structuralClassifications = new ArrayList<>();
        try {
            if (completionOccupationDate
                    .after(new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY).parse(DATE_CONSTANT))) {
                if (logger.isDebugEnabled())
                    logger.debug("Base Rate - Structural Factors");
                structuralClassifications
                        .addAll(getPersistenceService().findAllBy("from StructureClassification where code like 'R%'"));
            } else {
                if (logger.isDebugEnabled())
                    logger.debug("Rent Chart - Structural Factors");
                structuralClassifications
                        .addAll(getPersistenceService().findAllBy("from StructureClassification where code like 'R%'"));
            }
        } catch (final ParseException pe) {
            logger.error("Error while parsing Floor Completion / occupation", pe);
            throw new ApplicationRuntimeException("Error while parsing Floor Completion / occupation", pe);
        }
        Collections.sort(structuralClassifications, (object1, object2) -> object1.getTypeName()
                .compareTo(object2.getTypeName()));
        if (logger.isInfoEnabled())
            logger.info("getStructureClassifications - Structural Factors : " + structuralClassifications);
        if (logger.isDebugEnabled())
            logger.debug("Exiting from getStructureClassifications");
        return RESULT_STRUCTURAL;
    }

    @Actions({ @Action(value = "/ajaxCommon-getUserByMobileNo"),
            @Action(value = "/public/ajaxCommon-getUserByMobileNo") })
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
        if (logger.isDebugEnabled())
            logger.debug("Entered into checkIfCategoryExists ");
        final Category existingCategory = (Category) getPersistenceService().find(
                "select bc.category from BoundaryCategory bc where bc.bndry.id = ? "
                        + "and bc.category.propUsage.id = ? and bc.category.structureClass.id = ? and bc.category.fromDate = ? and bc.category.isActive = true ",
                zoneId, usageId, structureClassId, categoryFromDate);
        if (existingCategory != null) {
            categoryExists = "yes";
            validationMessage = getText("unit.rate.exists.for.combination",
                    new String[] { existingCategory.getCategoryAmount().toString() });
        }
        return RESULT_CHECK_EXISTING_CATEGORY;
    }

    @SuppressWarnings("unchecked")
    @Actions({ @Action(value = "/ajaxCommon-usageByPropType"), @Action(value = "/public/ajaxCommon-usageByPropType") })
    public String usageByPropType() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into usageByPropType, propTypeId: " + propTypeId);
        if (propTypeCategory.equals(CATEGORY_MIXED))
            propUsageList = getPersistenceService()
                    .findAllBy("From PropertyUsage where isActive = true order by usageName ");
        else if (propTypeCategory.equals(CATEGORY_RESIDENTIAL))
            propUsageList = getPersistenceService().findAllBy(
                    "From PropertyUsage where isResidential = true and isActive = true  order by usageName ");
        else if (propTypeCategory.equals(CATEGORY_NON_RESIDENTIAL))
            propUsageList = getPersistenceService().findAllBy(
                    "From PropertyUsage where isResidential = false and isActive = true  order by usageName ");
        if (logger.isDebugEnabled())
            logger.debug("Exiting from usageByPropType, No of Usages: " + (propUsageList != null ? propUsageList : ZERO));
        return USAGE;
    }

    @Action(value = "/ajaxCommon-checkIfPropertyExists")
    public void checkIfPropertyExists() throws IOException {
        if (StringUtils.isNotBlank(assessmentNo)) {
            final BasicProperty basicProperty = (BasicProperty) getPersistenceService()
                    .find("from BasicPropertyImpl bp where bp.oldMuncipalNum=? and bp.active='Y'", assessmentNo);
            final JSONObject jsonObject = new JSONObject();
            if (null != basicProperty)
                jsonObject.put("exists", Boolean.TRUE);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            IOUtils.write(jsonObject.toString(), response.getWriter());
        }
    }

    /**
     * API to calculate Mutation Fee dynamically
     *
     * @return
     */
    @Action(value = "/ajaxCommon-calculateMutationFee")
    public String calculateMutationFee() {
        // Maximum among partyValue and departmentValue will be considered as
        // the documentValue
        final BigDecimal documentValue = partyValue.compareTo(departmentValue) > 0 ? partyValue : departmentValue;

        if (documentValue.compareTo(ZERO) > 0) {
            BigDecimal excessDocValue;
            BigDecimal multiplicationFactor;
            final MutationFeeDetails mutationFeeDetails = (MutationFeeDetails) getPersistenceService().find(
                    "from MutationFeeDetails where lowLimit <= ? and (highLimit is null OR highLimit >= ?) and toDate > now()",
                    documentValue, documentValue);
            if (mutationFeeDetails != null) {
                if (mutationFeeDetails.getFlatAmount() != null
                        && mutationFeeDetails.getFlatAmount().compareTo(ZERO) > 0)
                    if (mutationFeeDetails.getIsRecursive().toString().equalsIgnoreCase(RECURSIVEFACTOR_N))
                        mutationFee = mutationFeeDetails.getFlatAmount();
                    else {
                        excessDocValue = documentValue.subtract(mutationFeeDetails.getLowLimit()).add(BigDecimal.ONE);
                        multiplicationFactor = excessDocValue.divide(mutationFeeDetails.getRecursiveFactor(),
                                BigDecimal.ROUND_CEILING);
                        mutationFee = mutationFeeDetails.getFlatAmount()
                                .add(multiplicationFactor.multiply(mutationFeeDetails.getRecursiveAmount()));
                    }
                if (mutationFeeDetails.getPercentage() != null
                        && mutationFeeDetails.getPercentage().compareTo(ZERO) > 0)
                    if (mutationFeeDetails.getIsRecursive().toString().equalsIgnoreCase(RECURSIVEFACTOR_N))
                        mutationFee = documentValue.multiply(mutationFeeDetails.getPercentage())
                                .divide(PropertyTaxConstants.BIGDECIMAL_100);
                mutationFee = mutationFee.setScale(0, BigDecimal.ROUND_HALF_UP);
            }
        }
        return RESULT_MUTATION_FEE;
    }

    @Actions({ @Action(value = "/ajaxcommon-propdepartment-byproptype"),
            @Action(value = "/public/ajaxcommon-propdepartment-byproptype") })
    public String getPropDepartmentByPropType() {
        if (propTypeId != null) {
            final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService()
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

        if (assessmentDocumentType != null)
            defaultCitizen = userRepository.findByUsername(DEFAULT_CITIZEN_NAME);
        return "defaultcitizen";
    }

    @Actions({
            @Action(value = "/ajaxCommon-isAppurTenant"),
            @Action(value = "/public/ajaxCommon-isAppurTenant") })
    public void isAppurTenant() throws IOException {
        final JSONObject jsonObject = new JSONObject();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (extentOfSite != null && plingthArea != null) {
            final BigDecimal triplePlingthArea = plingthArea.multiply(BigDecimal.valueOf(3));
            final BigDecimal permissableArea = triplePlingthArea.compareTo(BigDecimal.valueOf(1000)) < 0 ? triplePlingthArea
                    : BigDecimal.valueOf(1000);
            final BigDecimal landArea = extentOfSite.subtract(permissableArea);
            jsonObject.put("isAppurTenantLand", landArea.compareTo(ZERO) > 0);
            jsonObject.put("extentAppartenauntLand", permissableArea);
            jsonObject.put("vacantLandArea", landArea);
        } else
            jsonObject.put("isAppurTenantLand", Boolean.FALSE);
        IOUtils.write(jsonObject.toString(), response.getWriter());
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(final Long areaId) {
        this.areaId = areaId;
    }

    public String getUsageFactor() {
        return usageFactor;
    }

    public void setUsageFactor(final String usageFactor) {
        this.usageFactor = usageFactor;
    }

    public String getStructFactor() {
        return structFactor;
    }

    public void setStructFactor(final String structFactor) {
        this.structFactor = structFactor;
    }

    public Float getRevisedRate() {
        return revisedRate;
    }

    public void setRevisedRate(final Float revisedRate) {
        this.revisedRate = revisedRate;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(final List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Boundary> getWardList() {
        return wardList;
    }

    public void setWardList(final List<Boundary> wardList) {
        this.wardList = wardList;
    }

    public List<Boundary> getStreetList() {
        return streetList;
    }

    public void setStreetList(final List<Boundary> streetList) {
        this.streetList = streetList;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public List<Designation> getDesignationMasterList() {
        return designationMasterList;
    }

    public void setDesignationMasterList(final List<Designation> designationMasterList) {
        this.designationMasterList = designationMasterList;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(final Long designationId) {
        this.designationId = designationId;
    }

    public List<User> getUserList() {
        return userList;
    }

    public String getReturnStream() {
        return returnStream;
    }

    public void setReturnStream(final String returnStream) {
        this.returnStream = returnStream;
    }

    public Integer getPropTypeId() {
        return propTypeId;
    }

    public void setPropTypeId(final Integer propTypeId) {
        this.propTypeId = propTypeId;
    }

    public List<PropertyUsage> getPropUsageList() {
        return propUsageList;
    }

    public void setPropUsageList(final List<PropertyUsage> propUsageList) {
        this.propUsageList = propUsageList;
    }

    public Map<String, String> getPropTypeCategoryMap() {
        return propTypeCategoryMap;
    }

    public void setPropTypeCategoryMap(final Map<String, String> propTypeCategoryMap) {
        this.propTypeCategoryMap = propTypeCategoryMap;
    }

    public Date getCompletionOccupationDate() {
        return completionOccupationDate;
    }

    public void setCompletionOccupationDate(final Date completionOccupationDate) {
        this.completionOccupationDate = completionOccupationDate;
    }

    public List<StructureClassification> getStructuralClassifications() {
        return structuralClassifications;
    }

    public void setStructuralClassifications(final List<StructureClassification> structuralClassifications) {
        this.structuralClassifications = structuralClassifications;
    }

    public List<String> getPartNumbers() {
        return partNumbers;
    }

    public void setPartNumbers(final List<String> partNumbers) {
        this.partNumbers = partNumbers;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(final Long locality) {
        this.locality = locality;
    }

    @Override
    public void setServletResponse(final HttpServletResponse httpServletResponse) {
        response = httpServletResponse;
    }

    public DesignationService getDesignationService() {
        return designationService;
    }

    public void setDesignationService(final DesignationService designationService) {
        this.designationService = designationService;
    }

    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(final List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public void setSecurityUtils(final SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public String getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(final String currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public List<Boundary> getAreaList() {
        return areaList;
    }

    public void setAreaList(final List<Boundary> areaList) {
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

    public void setCategoryExists(final String categoryExists) {
        this.categoryExists = categoryExists;
    }

    public Long getUsageId() {
        return usageId;
    }

    public void setUsageId(final Long usageId) {
        this.usageId = usageId;
    }

    public Long getStructureClassId() {
        return structureClassId;
    }

    public void setStructureClassId(final Long structureClassId) {
        this.structureClassId = structureClassId;
    }

    public Date getCategoryFromDate() {
        return categoryFromDate;
    }

    public void setCategoryFromDate(final Date categoryFromDate) {
        this.categoryFromDate = categoryFromDate;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(final String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public String getPropTypeCategory() {
        return propTypeCategory;
    }

    public void setPropTypeCategory(final String propTypeCategory) {
        this.propTypeCategory = propTypeCategory;
    }

    public void setCategoryDAO(final CategoryDao categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void setBoundaryService(final BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    public String getAssessmentNo() {
        return assessmentNo;
    }

    public void setAssessmentNo(final String assessmentNo) {
        this.assessmentNo = assessmentNo;
    }

    public BigDecimal getPartyValue() {
        return partyValue;
    }

    public void setPartyValue(final BigDecimal partyValue) {
        this.partyValue = partyValue;
    }

    public BigDecimal getDepartmentValue() {
        return departmentValue;
    }

    public void setDepartmentValue(final BigDecimal departmentValue) {
        this.departmentValue = departmentValue;
    }

    public BigDecimal getMutationFee() {
        return mutationFee;
    }

    public void setMutationFee(final BigDecimal mutationFee) {
        this.mutationFee = mutationFee;
    }

    public List<PropertyDepartment> getPropertyDepartmentList() {
        return propertyDepartmentList;
    }

    public void setPropertyDepartmentList(final List<PropertyDepartment> propertyDepartmentList) {
        this.propertyDepartmentList = propertyDepartmentList;
    }

    public List<DocumentType> getAssessmentDocumentList() {
        return assessmentDocumentList;
    }

    public void setAssessmentDocumentList(final List<DocumentType> assessmentDocumentList) {
        this.assessmentDocumentList = assessmentDocumentList;
    }

    public String getAssessmentDocumentType() {
        return assessmentDocumentType;
    }

    public void setAssessmentDocumentType(final String assessmentDocumentType) {
        this.assessmentDocumentType = assessmentDocumentType;
    }

    public User getDefaultCitizen() {
        return defaultCitizen;
    }

    public void setDefaultCitizen(final User defaultCitizen) {
        this.defaultCitizen = defaultCitizen;
    }

    public BigDecimal getExtentOfSite() {
        return extentOfSite;
    }

    public void setExtentOfSite(final BigDecimal extentOfSite) {
        this.extentOfSite = extentOfSite;
    }

    public BigDecimal getPlingthArea() {
        return plingthArea;
    }

    public void setPlingthArea(final BigDecimal plingthArea) {
        this.plingthArea = plingthArea;
    }

}