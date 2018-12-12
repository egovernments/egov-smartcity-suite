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
package org.egov.ptis.actions.create;

import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.document.DocumentTypeDetails;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.*;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;
import org.egov.ptis.domain.entity.property.view.SurveyBean;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.repository.PropertyDepartmentRepository;
import org.egov.ptis.domain.repository.master.floortype.FloorTypeRepository;
import org.egov.ptis.domain.repository.master.rooftype.RoofTypeRepository;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.repository.master.walltype.WallTypeRepository;
import org.egov.ptis.domain.repository.master.woodtype.WoodTypeRepository;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.PropertySurveyService;
import org.egov.ptis.domain.service.reassign.ReassignService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

/**
 * @author parvati
 */
@ParentPackage("egov")
@Namespace("/create")
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = "new", location = "create/createProperty-new.jsp"),
        @Result(name = "dataEntry", location = "create/createProperty-dataEntry.jsp"),
        @Result(name = "ack", location = "create/createProperty-ack.jsp"),
        @Result(name = "dataEntry-ack", location = "create/createProperty-dataEntryAck.jsp"),
        @Result(name = "view", location = "create/createProperty-view.jsp"),
        @Result(name = "error", location = "common/meeseva-errorPage.jsp"),
        @Result(name = CreatePropertyAction.PRINT_ACK, location = "create/createProperty-printAck.jsp"),
        @Result(name = CreatePropertyAction.MEESEVA_RESULT_ACK, location = "common/meesevaAck.jsp"),
        @Result(name = CreatePropertyAction.EDIT_DATA_ENTRY, location = "create/createProperty-editDataEntry.jsp"),
        @Result(name = TARGET_WORKFLOW_ERROR, location = "workflow/workflow-error.jsp")})
public class CreatePropertyAction extends PropertyTaxBaseAction {

    private static final String REJECT_ERROR_INITIATOR_INACTIVE = "reject.error.initiator.inactive";
    private static final long serialVersionUID = -2329719786287615451L;
    private static final String RESULT_ACK = "ack";
    private static final String RESULT_NEW = "new";
    private static final String RESULT_ERROR = "error";
    private static final String RESULT_VIEW = "view";
    private static final String MSG_REJECT_SUCCESS = " Property Rejected Successfully ";
    private static final String CREATE = "create";
    private static final String RESULT_DATAENTRY = "dataEntry";
    public static final String PRINT_ACK = "printAck";
    protected static final String EDIT_DATA_ENTRY = "editDataEntry";
    private static final String MEESEVA_SERVICE_CODE_NEWPROPERTY = "PT01";
    private static final String MEESEVA_SERVICE_CODE_SUBDIVISION = "PT04";
    private static final String PROPTYPEMASTER_QUERY = "from PropertyTypeMaster ptm where ptm.id = ?";
    private static final String MEESEVA_SERVICE_CODE = "meesevaServicecode";
    private static final String MUTATION_MASTER_QUERY = "from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?";
    private static final String UNIT_RATE_ERROR = "unitrate.error";
    private static final String EXEMPTED_REASON_LIST = "taxExemptedList";
    private static final String NOTEXISTS_POSITION = "notexists.position";
    private transient Logger logger = Logger.getLogger(getClass());
    private PropertyImpl property = new PropertyImpl();
    @Autowired
    transient PropertyPersistenceService basicPropertyService;
    @Autowired
    transient APTaxCalculator taxCalculator;
    
    private Long zoneId;
    private Long wardId;
    private Long blockId;
    private Long streetId;
    private Long locality;
    private Long floorTypeId;
    private Long roofTypeId;
    private Long wallTypeId;
    private Long woodTypeId;
    private Long ownershipType;
    private Long electionWardId;

    private String wardName;
    private String zoneName;
    private String blockName;
    private String houseNumber;
    private String addressStr;
    private String pinCode;
    private String areaOfPlot;
    private String dateOfCompletion;
    private String applicationNo;
    private String corrAddress1;
    private String corrAddress2;
    private String corrPinCode;
    private String upicNo;
    private String taxExemptionId;
    private String parentIndex;
    private String amenities;
    private String[] floorNoStr = new String[100];
    private String propTypeId;
    private String propUsageId;
    private String propOccId;
    private String propertyCategory;
    private String docNumber;
    private String nonResPlotArea;
    private String applicationNoMessage;
    private String assessmentNoMessage;
    private String propertyInitiatedBy;
    private String mode = CREATE;
    private String northBoundary;
    private String southBoundary;
    private String eastBoundary;
    private String westBoundary;
    private String sitalArea;

    private Long mutationId;

    private transient Map<String, String> propTypeCategoryMap;
    private transient SortedMap<Integer, String> floorNoMap;
    private transient Map<String, String> deviationPercentageMap;
    private transient List<DocumentType> documentTypes = new ArrayList<>();

    private String reportId;
    private boolean approved;
    private boolean floorDetailsEntered;

    private transient BasicProperty basicProp;
    @Autowired
    private transient PropertyService propService;
    private transient PropertyTypeMaster propTypeMstr;
    @Autowired
    private transient PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private transient PropertyImpl newProperty = new PropertyImpl();
    private transient Address ownerAddress = new CorrespondenceAddress();
    Date propCompletionDate = null;
    @Autowired
    private transient BoundaryService boundaryService;
    @Autowired
    private transient SecurityUtils securityUtils;
    @Autowired
    private transient ReportViewerUtil reportViewerUtil;
    @Autowired
    private transient PropertySurveyService propertySurveyService;
    @Autowired
    private transient NoticeService noticeService;
    

    private Boolean loggedUserIsMeesevaUser = Boolean.FALSE;
    private boolean citizenPortalUser;
    private Boolean isDataEntryOperator = Boolean.FALSE;
    private String indexNumber;
    private String modifyRsn;
    private Boolean showTaxCalcBtn = Boolean.FALSE;
    private Long propertyDepartmentId;
    private transient List<PropertyDepartment> propertyDepartmentList = new ArrayList<>();
    private transient List<VacantLandPlotArea> vacantLandPlotAreaList = new ArrayList<>();
    private transient List<LayoutApprovalAuthority> layoutApprovalAuthorityList = new ArrayList<>();
    private Long vacantLandPlotAreaId;
    private Long layoutApprovalAuthorityId;
    private boolean allowEditDocument = Boolean.FALSE;
    private transient List<DocumentType> assessmentDocumentTypes = new ArrayList<>();
    private transient List<String> assessmentDocumentNames;
    private transient DocumentTypeDetails documentTypeDetails = new DocumentTypeDetails();
    private boolean eligibleInitiator = Boolean.TRUE;
    private boolean dataEntry = Boolean.FALSE;
    private String applicationSource;

    @Autowired
    private transient PropertyDepartmentRepository propertyDepartmentRepository;

    @Autowired
    private transient VacantLandPlotAreaRepository vacantLandPlotAreaRepository;

    @Autowired
    private transient LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepository;
    
    @Autowired
    private transient ReassignService reassignmentservice;
    
    @Autowired
    private transient WoodTypeRepository woodTypeRepository;
    
    @Autowired
    private transient WallTypeRepository wallTypeRepository;
    
    @Autowired
    private transient RoofTypeRepository roofTypeRepository;

    @Autowired
    private transient FloorTypeRepository floorTypeRepository;
    
    @PersistenceContext
    private transient EntityManager entityManager;

    public CreatePropertyAction() {
        super();
        property.setPropertyDetail(new BuiltUpProperty());
        property.setBasicProperty(new BasicPropertyImpl());
        this.addRelatedEntity("property", PropertyImpl.class);
        this.addRelatedEntity("property.propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
        this.addRelatedEntity("property.propertyDetail.floorDetails.unitType", PropertyTypeMaster.class);
        this.addRelatedEntity("property.propertyDetail.floorDetails.propertyUsage", PropertyUsage.class);
        this.addRelatedEntity("property.propertyDetail.floorDetails.propertyOccupation", PropertyOccupation.class);
        this.addRelatedEntity("property.propertyDetail.floorDetails.structureClassification",
                StructureClassification.class);
        this.addRelatedEntity("property.basicProperty.propertyOwnerInfo.owner", Citizen.class);
        this.addRelatedEntity("propertyDetail.apartment", Apartment.class);
        addRelatedEntity("property.propertyDetail.floorType", FloorType.class);
        addRelatedEntity("property.propertyDetail.roofType", RoofType.class);
        addRelatedEntity("property.propertyDetail.wallType", WallType.class);
        addRelatedEntity("property.propertyDetail.woodType", WoodType.class);
        addRelatedEntity("property.taxExemptedReason", TaxExemptionReason.class);
        addRelatedEntity("property.propertyDetail.propertyDepartment", PropertyDepartment.class);
    }

    @Override
    public StateAware getModel() {
        return property;
    }

    @SkipValidation
    @Action(value = "/createProperty-newForm")
    public String newForm() {

        if (loggedUserIsMeesevaUser) {
            final HttpServletRequest request = ServletActionContext.getRequest();
            if (request.getParameter("applicationNo") == null || request.getParameter(MEESEVA_SERVICE_CODE) == null) {
                addActionMessage(getText("MEESEVA.005"));
                return RESULT_ERROR;
            } else {

                if (request.getParameter(MEESEVA_SERVICE_CODE).equalsIgnoreCase(MEESEVA_SERVICE_CODE_NEWPROPERTY))
                    getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_CODE);
                else if (request.getParameter(MEESEVA_SERVICE_CODE).equalsIgnoreCase(MEESEVA_SERVICE_CODE_SUBDIVISION))
                    getMutationListByCode(PROP_CREATE_RSN_BIFUR);
                property.setMeesevaApplicationNumber(request.getParameter("applicationNo"));
                property.setMeesevaServiceCode(request.getParameter(MEESEVA_SERVICE_CODE));
            }
        }
        showCalculateTaxButton();
        return RESULT_NEW;
    }

    private void showCalculateTaxButton() {
        if (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN)
                || StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT)
                || StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT)
                || SOURCE_ONLINE.equalsIgnoreCase(applicationSource))
            showTaxCalcBtn = Boolean.TRUE;
    }

    @SuppressWarnings("unchecked")
    private void getMutationListByCode(final String code) {
        final List<PropertyMutationMaster> mutationList = getPersistenceService()
                .findAllBy("from PropertyMutationMaster pmm where pmm.type=? and pmm.code=?", PROP_CREATE_RSN, code);
        addDropdownData("MutationList", mutationList);
    }

    @Action(value = "/createProperty-create")
    public String create() {
        if (loggedUserIsMeesevaUser && property.getMeesevaApplicationNumber() != null) {
            property.setApplicationNo(property.getMeesevaApplicationNumber());
            property.setSource(PropertyTaxConstants.SOURCE_MEESEVA);
        }
        if (SOURCE_ONLINE.equalsIgnoreCase(applicationSource) && ApplicationThreadLocals.getUserId() == null)
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        if (multipleSubmitCondition(property, approverPositionId)) {
            return multipleSubmitRedirect();
        }
        if (property.getPropertyDetail().isAppurtenantLandChecked()) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                    Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
                property.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
            else
                property.getPropertyDetail().setPropertyType(PropertyTaxConstants.BUILT_UP_PROPERTY);
            return createAppurTenantProperties(STATUS_WORKFLOW);
        }

        final BasicProperty basicProperty = createBasicProp(STATUS_WORKFLOW);
        try {
            addDemandAndCompleteDate(STATUS_WORKFLOW, basicProperty, basicProperty.getPropertyMutationMaster());
        } catch (final TaxCalculatorExeption e) {
            basicProperty.setPropertyOwnerInfoProxy(basicProperty.getPropertyOwnerInfo());
            addActionError(getText(UNIT_RATE_ERROR));
            logger.error("create : There are no Unit rates defined for chosen combinations", e);
            return RESULT_NEW;
        }
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        // this should be appending to messgae
        transitionWorkFlow(property);
        basicPropertyService.applyAuditing(property.getState());
        if (loggedUserIsMeesevaUser && property.getMeesevaApplicationNumber() != null)
            basicProperty.setSource(PropertyTaxConstants.SOURCEOFDATA_MEESEWA);
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        propService.processAndStoreDocument(property.getAssessmentDocuments());
        if (!loggedUserIsMeesevaUser)
            basicPropertyService.persist(basicProperty);
        else {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("ADMISSIONFEE", "0");
            meesevaParams.put("APPLICATIONNUMBER", property.getMeesevaApplicationNumber());
            basicPropertyService.createBasicProperty(basicProperty, meesevaParams);
        }
        if (citizenPortalUser)
            propService.pushPortalMessage(property, APPLICATION_TYPE_NEW_ASSESSENT);
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        setBasicProp(basicProperty);
        propService.saveDocumentTypeDetails(basicProperty, getDocumentTypeDetails());
        setAckMessage("Property Data Saved Successfully in the System and forwarded to : ");
        setApplicationNoMessage(" with application number : ");
        if (!loggedUserIsMeesevaUser)
            return RESULT_ACK;
        else
            return MEESEVA_RESULT_ACK;

    }

    private String createAppurTenantProperties(final Character status) {
        final BasicProperty nonVacantBasicProperty = createBasicProp(STATUS_DEMAND_INACTIVE);
        updatePropertyStatusValuesRemarks(nonVacantBasicProperty);
        PropertyImpl nonVacantProperty = null;
        try {
            nonVacantProperty = createNonVacantProperty(status, nonVacantBasicProperty);
            final BasicProperty vacantBasicProperty = createBasicProp(STATUS_DEMAND_INACTIVE);
            updatePropertyStatusValuesRefProperty(nonVacantBasicProperty, vacantBasicProperty);
            createVacantProperty(status, nonVacantProperty, vacantBasicProperty);
        } catch (final TaxCalculatorExeption e) {
            nonVacantBasicProperty.setPropertyOwnerInfoProxy(nonVacantBasicProperty.getPropertyOwnerInfo());
            addActionError(getText(UNIT_RATE_ERROR));
            logger.error("create : There are no Unit rates defined for chosen combinations", e);
            return RESULT_NEW;
        }

        setBasicProp(nonVacantBasicProperty);
        if (citizenPortalUser) {
            nonVacantProperty.setSource(Source.CITIZENPORTAL.toString());
            propService.pushPortalMessage(nonVacantProperty, APPLICATION_TYPE_NEW_ASSESSENT);
        }

        setAckMessage("Property Created Successfully in the System and Forwarded to : ");
        setApplicationNoMessage(" with application number : ");
        setAssessmentNoMessage(" for Digital Signature with assessment number : ");
        property = nonVacantProperty;
        if (!loggedUserIsMeesevaUser)
            return RESULT_ACK;
        else
            return MEESEVA_RESULT_ACK;
    }

    private PropertyImpl createNonVacantProperty(final Character status, final BasicProperty nonVacantBasicProperty)
            throws TaxCalculatorExeption {
        final PropertyImpl nonVacantProperty = createAppurTenantProperty(status, nonVacantBasicProperty, Boolean.TRUE);
        if (!property.getDocuments().isEmpty()){
            nonVacantProperty.setDocuments(property.getDocuments());
            propService.processAndStoreDocument(nonVacantProperty.getDocuments());
        }

        persistAndMessage(nonVacantBasicProperty, nonVacantProperty);
        return nonVacantProperty;
    }

    private void createVacantProperty(final Character status, final PropertyImpl nonVacantProperty,
            final BasicProperty vacantBasicProperty) throws TaxCalculatorExeption {
        final PropertyImpl vacantProperty = createAppurTenantProperty(status, vacantBasicProperty, Boolean.FALSE);
        vacantProperty.setPropertyDetail(changePropertyDetail(vacantProperty));
        vacantProperty.getPropertyDetail().setCategoryType(getCategoryByNonVacantPropertyType(nonVacantProperty));
        vacantProperty.getPropertyDetail().setAppurtenantLandChecked(null);
        vacantBasicProperty.getAddress().setHouseNoBldgApt(null);
        persistAndMessage(vacantBasicProperty, vacantProperty);
    }

    private void updatePropertyStatusValuesRefProperty(final BasicProperty nonVacantBasicProperty,
            final BasicProperty vacantBasicProperty) {
        final PropertyStatusValues propStatusVal = updatePropertyStatusValuesRemarks(vacantBasicProperty);
        propStatusVal.setReferenceBasicProperty(nonVacantBasicProperty);
    }

    private PropertyStatusValues updatePropertyStatusValuesRemarks(final BasicProperty basicProperty) {
        final PropertyStatusValues propStatusVal = basicProperty.getPropertyStatusValuesSet().iterator().next();
        propStatusVal.setRemarks(APPURTENANT_PROPERTY);
        return propStatusVal;
    }

    private String getCategoryByNonVacantPropertyType(final PropertyImpl nonVacantProperty) {
        final String propertyType = nonVacantProperty.getPropertyDetail().getPropertyTypeMaster().getCode();
        return OWNERSHIP_TYPE_PRIVATE.equals(propertyType) || OWNERSHIP_TYPE_VAC_LAND.equals(propertyType)
                ? CATEGORY_VACANT_LAND
                : OWNERSHIP_TYPE_STATE_GOVT.equals(propertyType) ? CATEGORY_STATE_GOVT : CATEGORY_CENTRAL_GOVT;
    }

    private void persistAndMessage(final BasicProperty basicProperty, final PropertyImpl property) {
        if (!loggedUserIsMeesevaUser)
            basicPropertyService.persist(basicProperty);
        else {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("ADMISSIONFEE", "0");
            meesevaParams.put("APPLICATIONNUMBER", property.getMeesevaApplicationNumber());
            basicPropertyService.createBasicProperty(basicProperty, meesevaParams);
        }
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        final DocumentTypeDetails docTypeDetails = cloneDocumentTypeDetails();
        propService.saveDocumentTypeDetails(basicProperty, docTypeDetails);
    }

    private DocumentTypeDetails cloneDocumentTypeDetails() {
        final DocumentTypeDetails docTypeDetails = new DocumentTypeDetails();
        docTypeDetails.setCourtName(documentTypeDetails.getCourtName());
        docTypeDetails.setDocumentNo(documentTypeDetails.getDocumentNo());
        docTypeDetails.setDocumentDate(documentTypeDetails.getDocumentDate());
        docTypeDetails.setDocumentName(documentTypeDetails.getDocumentName());
        docTypeDetails.setProceedingNo(documentTypeDetails.getProceedingNo());
        docTypeDetails.setProceedingDate(documentTypeDetails.getProceedingDate());
        docTypeDetails.setSigned(documentTypeDetails.isSigned());
        return docTypeDetails;
    }

    private PropertyImpl createAppurTenantProperty(final Character status, final BasicProperty basicProperty,
            final Boolean nonVacant) throws TaxCalculatorExeption {
        PropertyImpl clonedProp = (PropertyImpl) property.createPropertyclone();
        if (nonVacant) {
            final List<Floor> flrDtlsSet = cloneFloorDetails();
            clonedProp.getPropertyDetail().setFloorDetailsProxy(flrDtlsSet);
        }
        clonedProp.setBasicProperty(basicProperty);
        clonedProp.setPropertyModifyReason(PROP_CREATE_RSN);

        taxExemptionId = taxExemptionId == null || taxExemptionId.isEmpty() ? "-1" : taxExemptionId;
        clonedProp = propService.createProperty(clonedProp, getAreaOfPlot(),
                basicProperty.getPropertyMutationMaster().getCode(), propTypeId, propUsageId, propOccId, status,
                getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(), getWallTypeId(),
                getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(), getVacantLandPlotAreaId(),
                getLayoutApprovalAuthorityId(), nonVacant);
        clonedProp.setStatus(status);
        if (!clonedProp.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService
                    .getLowestDtOfCompFloorWise(clonedProp.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = clonedProp.getPropertyDetail().getDateOfCompletion();
        basicProperty.setPropOccupationDate(propCompletionDate);
        if (propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            clonedProp.setPropertyDetail(changePropertyDetail(clonedProp));
        basicProperty.addProperty(clonedProp);
        if (basicProperty.getSource() == PropertyTaxConstants.SOURCEOFDATA_APPLICATION) {
            propService.createDemand(clonedProp, propCompletionDate);
        }
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        transitionWorkFlow(clonedProp);
        basicPropertyService.applyAuditing(clonedProp.getState());
        if (loggedUserIsMeesevaUser && clonedProp.getMeesevaApplicationNumber() != null)
            basicProperty.setSource(PropertyTaxConstants.SOURCEOFDATA_MEESEWA);
        propService.updateIndexes(clonedProp, APPLICATION_TYPE_NEW_ASSESSENT);
        if (!property.getAssessmentDocuments().isEmpty()) {
            clonedProp.setAssessmentDocuments(property.getAssessmentDocuments());
            propService.processAndStoreDocument(clonedProp.getAssessmentDocuments());
        }
        return clonedProp;
    }

    private List<Floor> cloneFloorDetails() {
        Floor floor;
        final List<Floor> flrDtlsSet = new ArrayList<>();
        for (final Floor flr : property.getPropertyDetail().getFloorDetailsProxy()) {
            floor = new Floor(flr.getConstructionTypeSet(), flr.getStructureClassification(), flr.getPropertyUsage(),
                    flr.getPropertyOccupation(), flr.getFloorNo(), flr.getDepreciationMaster(), flr.getBuiltUpArea(),
                    flr.getFloorArea(), flr.getWaterMeter(), flr.getElectricMeter(), null, null, flr.getRentPerMonth(),
                    flr.getManualAlv(), flr.getUnitType(), flr.getUnitTypeCategory(), flr.getWaterRate(), flr.getAlv(),
                    flr.getOccupancyDate(), flr.getOccupantName(), flr.getUnstructuredLand(), flr.getFloorDmdCalc(),
                    flr.getFirmName(), flr.getBuildingPermissionNo(), flr.getBuildingPermissionDate(),
                    flr.getBuildingPlanPlinthArea(), flr.getFloorUid(), flr.getConstructionDate());
            flrDtlsSet.add(floor);
        }
        return flrDtlsSet;
    }

    private void populateFormData() {
        final PropertyDetail propertyDetail = property.getPropertyDetail();
        if (property.getTaxExemptedReason() != null)
            taxExemptionId = property.getTaxExemptedReason().getId().toString();
        if (propertyDetail != null) {
            if (!propertyDetail.getFloorDetails().isEmpty())
                setFloorDetails(property);
            setPropTypeId(propertyDetail.getPropertyTypeMaster().getId().toString());
            if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId)) {
                propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                        Long.valueOf(propTypeId));
                if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                    setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
                else
                    setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
            }

            if (!propertyDetail.getPropertyType().equals(VACANT_PROPERTY)) {
                propertyDetail.setCategoryType(propertyDetail.getCategoryType());
                if (property.getPropertyDetail().getFloorType() != null)
                    floorTypeId = property.getPropertyDetail().getFloorType().getId();
                if (property.getPropertyDetail().getRoofType() != null)
                    roofTypeId = property.getPropertyDetail().getRoofType().getId();
                if (property.getPropertyDetail().getWallType() != null)
                    wallTypeId = property.getPropertyDetail().getWallType().getId();
                if (property.getPropertyDetail().getWoodType() != null)
                    woodTypeId = property.getPropertyDetail().getWoodType().getId();
                if (property.getPropertyDetail().getSitalArea() != null)
                    setAreaOfPlot(property.getPropertyDetail().getSitalArea().getArea().toString());
            }

            if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId)) {
                propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                        Long.valueOf(propTypeId));
                setPropertyDepartmentId(property.getPropertyDetail().getPropertyDepartment() != null
                        ? property.getPropertyDetail().getPropertyDepartment().getId() : null);
                if (propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT))
                    setPropertyDepartmentList(propertyDepartmentRepository.getAllStateDepartments());
                else if (propTypeMstr.getCode().startsWith("CENTRAL_GOVT"))
                    setPropertyDepartmentList(propertyDepartmentRepository.getAllCentralDepartments());
                else if (propTypeMstr.getCode().equals(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE))
                    setPropertyDepartmentList(propertyDepartmentRepository.getAllPrivateDepartments());
            }
            if (property.getPropertyDetail().getVacantLandPlotArea() != null)
                vacantLandPlotAreaId = property.getPropertyDetail().getVacantLandPlotArea().getId();
            if (property.getPropertyDetail().getLayoutApprovalAuthority() != null)
                layoutApprovalAuthorityId = property.getPropertyDetail().getLayoutApprovalAuthority().getId();
        }

        if (basicProp != null) {
            basicProp.setPropertyOwnerInfoProxy(basicProp.getPropertyOwnerInfo());
            setMutationId(basicProp.getPropertyMutationMaster().getId());
            final PropertyStatusValues statusValues = (PropertyStatusValues) getPersistenceService()
                    .find("From PropertyStatusValues where basicProperty.id = ?", basicProp.getId());
            if (null != statusValues && null != statusValues.getReferenceBasicProperty())
                setParentIndex(statusValues.getReferenceBasicProperty().getUpicNo());
            if (null != basicProp.getAddress()) {
                setHouseNumber(basicProp.getAddress().getHouseNoBldgApt());
                setPinCode(basicProp.getAddress().getPinCode());
            }

            for (final PropertyOwnerInfo ownerInfo : basicProp.getPropertyOwnerInfo())
                for (final Address userAddress : ownerInfo.getOwner().getAddress())
                    if (null != userAddress) {
                        final String corrAddress = (userAddress.getHouseNoBldgApt() != null
                                ? userAddress.getHouseNoBldgApt() + ","
                                : "") + userAddress.getAreaLocalitySector();
                        setCorrAddress1(corrAddress);
                        setCorrAddress2(userAddress.getStreetRoadLine());
                        setCorrPinCode(userAddress.getPinCode());
                    }
            if (null != basicProp.getPropertyID()) {
                final PropertyID propBoundary = basicProp.getPropertyID();
                setNorthBoundary(propBoundary.getNorthBoundary());
                setSouthBoundary(propBoundary.getSouthBoundary());
                setEastBoundary(propBoundary.getEastBoundary());
                setWestBoundary(propBoundary.getWestBoundary());
                if (null != propBoundary.getLocality().getId())
                    setLocality(boundaryService.getBoundaryById(propBoundary.getLocality().getId()).getId());
                if (null != propBoundary.getElectionBoundary() && null != propBoundary.getElectionBoundary().getId())
                    setElectionWardId(
                            boundaryService.getBoundaryById(propBoundary.getElectionBoundary().getId()).getId());
                if (null != propBoundary.getZone().getId()) {
                    final Boundary zone = propBoundary.getZone();
                    setZoneId(boundaryService.getBoundaryById(zone.getId()).getId());
                    setZoneName(zone.getName());
                }
                if (null != propBoundary.getWard().getId()) {
                    final Boundary ward = propBoundary.getWard();
                    setWardId(boundaryService.getBoundaryById(ward.getId()).getId());
                    setWardName(ward.getName());
                }
                if (null != propBoundary.getArea().getId()) {
                    final Boundary area = propBoundary.getArea();
                    setBlockId(boundaryService.getBoundaryById(area.getId()).getId());
                    setBlockName(area.getName());
                }
            }
            try {
                final Query query = entityManager.createNamedQuery("DOCUMENT_TYPE_DETAILS_BY_ID");
                query.setParameter("basicProperty", basicProp.getId());
                setDocumentTypeDetails((DocumentTypeDetails) query.getSingleResult());
            } catch (final Exception e) {
                logger.error("No Document type details present for Basicproperty " + e);
            }
        }
    }

    @SkipValidation
    @Action(value = "/createProperty-view")
    public String view() {
        isReassignEnabled = reassignmentservice.isReassignEnabled();
        stateAwareId = property.getId();
        transactionType = APPLICATION_TYPE_NEW_ASSESSENT;
        final String currState = property.getState().getValue();
        populateFormData();
        if(SOURCE_SURVEY.equalsIgnoreCase(property.getSource()))
            enableActionsForGIS(property, documentTypes);
        if (currState.endsWith(WF_STATE_REJECTED)
                || property.getState().getNextAction() != null && property.getState().getNextAction()
                        .equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING)
                || currState.endsWith(WFLOW_ACTION_NEW)) {
            showCalculateTaxButton();
            mode = EDIT;
            setAllowEditDocument(Boolean.TRUE);
            return RESULT_NEW;
        } else {
            mode = VIEW;
            for (final PropertyOwnerInfo ownerInfo : basicProp.getPropertyOwnerInfo())
                for (final Address userAddress : ownerInfo.getOwner().getAddress())
                    if (null != userAddress)
                        setCorrAddress1(userAddress.toString());
            setDocNumber(property.getDocNumber());
            return RESULT_VIEW;
        }
    }

    @SkipValidation
    @Action(value = "/createProperty-editDataEntryForm")
    public String editDataEntryForm() {
        upicNo = indexNumber;
        mode = EDIT;
        setDataEntry(Boolean.TRUE);
        populateFormData();
        return EDIT_DATA_ENTRY;
    }

    @SkipValidation
    @Action(value = "/createProperty-forward")
    public String forward() {
        String loggedInUserDesignation = "";
        if (property.getState() != null) {
            final List<Assignment> loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    property.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser().getId(),
                    new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty()
                    ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }
        final Assignment wfInitiator = getWorkflowInitiator(loggedInUserDesignation);
        if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction) && wfInitiator == null) {
            if (propertyTaxCommonUtils.isRoOrCommissioner(loggedInUserDesignation))
                addActionError(getText(REJECT_ERROR_INITIATOR_INACTIVE, Arrays.asList(REVENUE_INSPECTOR_DESGN)));
            else 
                addActionError(getText(REJECT_ERROR_INITIATOR_INACTIVE,
                        Arrays.asList(JUNIOR_ASSISTANT + "/" + SENIOR_ASSISTANT)));
            return mode.equalsIgnoreCase(EDIT) ? RESULT_NEW : RESULT_VIEW;
        }
        else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)
                && propService.getWorkflowInitiator(property) == null) {
            addActionError(getText(REJECT_ERROR_INITIATOR_INACTIVE,
                    Arrays.asList(JUNIOR_ASSISTANT + "/" + SENIOR_ASSISTANT)));
            return mode.equalsIgnoreCase(EDIT) ? RESULT_NEW : RESULT_VIEW;
        }
        if (multipleSubmitCondition(property, approverPositionId)) {
            return multipleSubmitRedirect();
        }
        if (mode.equalsIgnoreCase(EDIT) && !WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            validate();
            if (hasErrors() && (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN)
                    || StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT)
                    || StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT))) {
                showTaxCalcBtn = Boolean.TRUE;
                setAllowEditDocument(Boolean.TRUE);
                return RESULT_NEW;
            } else if (hasErrors())
                return RESULT_NEW;
            updatePropertyDetails();
            try {
                propService.createDemand(property, basicProp.getPropOccupationDate());
            } catch (final TaxCalculatorExeption e) {
                addActionError(getText(UNIT_RATE_ERROR));
                logger.error("forward : There are no Unit rates defined for chosen combinations", e);
                return RESULT_NEW;
            }
        } else {
            validateApproverDetails();
            if (hasErrors())
                return RESULT_VIEW;
        }
        if (!WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
            transitionWorkFlow(property);

        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
            return approve();
        else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction)) {
            transitionWorkFlow(property);
            return reject();
        }
        basicProp.setUnderWorkflow(true);
        basicPropertyService.applyAuditing(property.getState());
        if (SOURCE_SURVEY.equalsIgnoreCase(property.getSource())) {
            SurveyBean surveyBean = new SurveyBean();
            surveyBean.setProperty(property);
            if (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN)
                    || StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT)
                    || StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT)) {
                BigDecimal totalTax = propService.getSurveyTax(property, new Date());
                surveyBean.setApplicationTax(totalTax);
                GisDetails gisDetails = property.getGisDetails();
                if(gisDetails != null){
                    gisDetails.setPropertyZone(basicProp.getPropertyID().getZone());
                    gisDetails.setApplicationTax(totalTax);
                    GisAuditDetails auditDetails = new GisAuditDetails(gisDetails);
                    gisDetails.addAuditDetails(auditDetails);
                }
            }
            if(property.getCurrentState().getValue().toUpperCase().endsWith(WF_STATE_REVENUE_OFFICER_APPROVED.toUpperCase())){
                BigDecimal surveyVariance = propertyTaxUtil.getTaxDifferenceForGIS(property);
                property.setSurveyVariance(surveyVariance);
                if(surveyVariance.compareTo(BigDecimal.TEN)>0 && !property.isThirdPartyVerified()){
                    noticeService.generateComparisonNotice(property);
                    property.setSentToThirdParty(true);
                    surveyBean.setProperty(property);
                }
            }
            propertySurveyService.updateSurveyIndex(APPLICATION_TYPE_NEW_ASSESSENT, surveyBean);
        }
            basicProp.addProperty(property);
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(property.getSource()))
            propService.updatePortal(property, APPLICATION_TYPE_NEW_ASSESSENT);
        basicPropertyService.persist(basicProp);
        setDocNumber(getDocNumber());
        setApplicationNoMessage(" with application number : ");
        return RESULT_ACK;
    }

    /**
     * @param loggedInUserDesignation
     * @return
     */
    private Assignment getWorkflowInitiator(final String loggedInUserDesignation) {
        Assignment wfInitiator;
        if (propertyTaxCommonUtils.isRoOrCommissioner(loggedInUserDesignation))
            wfInitiator = propService.getUserOnRejection(property);
        else
            wfInitiator = propService.getWorkflowInitiator(property);
        return wfInitiator;
    }

    public void updatePropertyDetails() {
        updatePropertyId(basicProp);
        final Character status = STATUS_WORKFLOW;
        updatePropAddress(basicProp);
        basicPropertyService.createOwners(property, basicProp, ownerAddress);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
                .find(MUTATION_MASTER_QUERY, PROP_CREATE_RSN, mutationId);
        basicProp.setPropertyMutationMaster(propertyMutationMaster);
        taxExemptionId = taxExemptionId == null || taxExemptionId.isEmpty() ? "-1" : taxExemptionId;
        property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
                propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(),
                getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId(), Boolean.FALSE);
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
        basicProp.setPropOccupationDate(propCompletionDate);

        if (property != null && !property.getDocuments().isEmpty())
            propService.processAndStoreDocument(property.getDocuments());

        if (propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            property.setPropertyDetail(
                    propService.changePropertyDetail(property, property.getPropertyDetail(), 0).getPropertyDetail());
        if (property.getPropertyDetail().getLayoutApprovalAuthority() != null
                && "No Approval".equals(property.getPropertyDetail().getLayoutApprovalAuthority().getName())) {
            property.getPropertyDetail().setLayoutPermitNo(null);
            property.getPropertyDetail().setLayoutPermitDate(null);
        }
        if (getDocumentTypeDetails().getDocumentName().equals(PropertyTaxConstants.DOCUMENT_NAME_NOTARY_DOCUMENT))
            property.getPropertyDetail().setStructure(true);
        else
            property.getPropertyDetail().setStructure(false);
        if (property.getAssessmentDocuments() != null) {
            propService.clearOldDocumentAttachments(property.getAssessmentDocuments(), getDocumentTypeDetails());
            propService.processAndStoreDocument(property.getAssessmentDocuments());
        }
        property.setBasicProperty(basicProp);
        propService.updateReferenceBasicProperty(basicProp, getParentIndex());
        if (basicProp != null)
            propService.updateDocumentTypeDetails(basicProp, getDocumentTypeDetails());
    }

    private void updatePropertyId(final BasicProperty basicProperty) {
        final PropertyID propertyId = basicProperty.getPropertyID();
        propertyId.setZone(boundaryService.getBoundaryById(getZoneId()));
        propertyId.setWard(boundaryService.getBoundaryById(getWardId()));
        propertyId.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        propertyId.setModifiedDate(new Date());
        propertyId.setArea(boundaryService.getBoundaryById(getBlockId()));
        propertyId.setLocality(boundaryService.getBoundaryById(getLocality()));
        propertyId.setEastBoundary(getEastBoundary());
        propertyId.setWestBoundary(getWestBoundary());
        propertyId.setNorthBoundary(getNorthBoundary());
        propertyId.setSouthBoundary(getSouthBoundary());
    }

    @SkipValidation
    @Action(value = "/createProperty-approve")
    public String approve() {
        // For exempted property on approve setting status as 'A'
        if (property.getIsExemptedFromTax())
            property.setStatus(STATUS_ISACTIVE);
        else
            property.setStatus(STATUS_DEMAND_INACTIVE);
        final String assessmentNo = propertyTaxNumberGenerator.generateAssessmentNumber();
        basicProp.setUpicNo(assessmentNo);
        basicProp.setAssessmentdate(new Date());
        final PropertyStatus propStatus = (PropertyStatus) getPersistenceService()
                .find("from PropertyStatus where statusCode=?", PROPERTY_STATUS_APPROVED);
        basicProp.setStatus(propStatus);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
                .find(MUTATION_MASTER_QUERY, PROP_CREATE_RSN, basicProp.getPropertyMutationMaster().getId());
        if (!propertyMutationMaster.getCode().equals(PROP_CREATE_RSN_BIFUR)
                && basicProp.getPropertyStatusValuesSet().isEmpty()
                && WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
            basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, PROP_CREATE_RSN, null, null,
                    null, null, getParentIndex()));
        approved = true;
        setWardId(basicProp.getPropertyID().getWard().getId());
        basicPropertyService.applyAuditing(property.getState());
        String appConfigValue = getDemandVoucherAppConfigValue();
        if("Y".equalsIgnoreCase(appConfigValue)){
            Map<String, Map<String, Object>> voucherData = propService.prepareDemandVoucherData(property, null, true);
            financialUtil.createVoucher(basicProp.getUpicNo(), voucherData, APPLICATION_TYPE_NEW_ASSESSENT);
        }
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        if (SOURCE_SURVEY.equalsIgnoreCase(property.getSource())) {
            SurveyBean surveyBean = new SurveyBean();
            surveyBean.setProperty(property);
            propertySurveyService.updateSurveyIndex(APPLICATION_TYPE_NEW_ASSESSENT, surveyBean);
        }
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(property.getSource()))
            propService.updatePortal(property, APPLICATION_TYPE_NEW_ASSESSENT);
        basicPropertyService.update(basicProp);

        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        approverName = "";
        final Assignment userAssignment = assignmentService
                .getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId());
        if (null != userAssignment)
            propertyInitiatedBy = userAssignment.getEmployee().getName().concat("~")
                    .concat(userAssignment.getPosition().getName());
        setAckMessage("Property Created Successfully in the System and Forwarded to : ");
        setAssessmentNoMessage(" for Digital Signature with assessment number : ");
        return RESULT_ACK;
    }

    @SkipValidation
    @Action(value = "/createProperty-reject")
    public String reject() {
        basicPropertyService.applyAuditing(property.getState());
        if (StringUtils.isNotBlank(sitalArea)) {
            final Area area = new Area();
            area.setArea(new Float(sitalArea));
            property.getPropertyDetail().setSitalArea(area);
        }
        if (property.getStatus().equals(PropertyTaxConstants.STATUS_CANCELLED))
            basicProp.setUnderWorkflow(false);
        else
            basicProp.setUnderWorkflow(true);
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        if (SOURCE_SURVEY.equalsIgnoreCase(property.getSource())) {
            SurveyBean surveyBean = new SurveyBean();
            if(isThirdPartyCheckbox() 
            		&& PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING.equalsIgnoreCase(property.getState().getNextAction())){
            	property.setThirdPartyVerified(true);
            }
            surveyBean.setProperty(property);
            propertySurveyService.updateSurveyIndex( APPLICATION_TYPE_NEW_ASSESSENT, surveyBean);
        }
        basicPropertyService.persist(basicProp);
        if (Source.CITIZENPORTAL.toString().equalsIgnoreCase(property.getSource()))
            propService.updatePortal(property, APPLICATION_TYPE_NEW_ASSESSENT);
        approverName = "";
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        Assignment assignment;
        if (property.getBasicProperty().getSource().equals(PropertyTaxConstants.SOURCEOFDATA_ONLINE)
                || property.getBasicProperty().getSource().equals(PropertyTaxConstants.SOURCEOFDATA_SURVEY))
            propertyInitiatedBy = propertyTaxUtil
                    .getApproverUserName(property.getStateHistory().get(0).getOwnerPosition().getId());
        else
            setPropertyInitiatedBy(getInitiator());

        if ("Closed".equals(property.getState().getValue())) {
            assignment = assignmentService.getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId());
            propertyInitiatedBy = assignment.getEmployee().getName().concat("~")
                    .concat(assignment.getPosition().getName());
            setPropertyInitiatedBy(getInitiator());
            setAckMessage(MSG_REJECT_SUCCESS + " By ");
        } else
            setAckMessage(MSG_REJECT_SUCCESS + " and forwarded to : ");
        setApplicationNoMessage(" with application No :");
        return RESULT_ACK;
    }

    private void setFloorDetails(final Property property) {
        final List<Floor> floorList = property.getPropertyDetail().getFloorDetails();
        property.getPropertyDetail().setFloorDetailsProxy(floorList);
        int i = 0;
        for (final Floor flr : floorList) {
            floorNoStr[i] = FLOOR_MAP.get(flr.getFloorNo());
            i++;
        }
    }

    public List<Floor> getFloorDetails() {
        return new ArrayList<>(property.getPropertyDetail().getFloorDetails());
    }

    @Override
    @SuppressWarnings("unchecked")
    @SkipValidation
    public void prepare() {
        setUserInfo();
        setUserDesignations();
        propertyByEmployee = propService.isEmployee(securityUtils.getCurrentUser())
                && !ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName());
        loggedUserIsMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());
        citizenPortalUser = propService.isCitizenPortalUser(securityUtils.getCurrentUser());
        isDataEntryOperator = propService.isDataEntryOperator(securityUtils.getCurrentUser());
        if (isNotBlank(getModelId())) {
            property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            if (StringUtils.isNotBlank(modifyRsn))
                property = (PropertyImpl) persistenceService.merge(property);
            basicProp = property.getBasicProperty();
        }
        if (null != property && null != property.getId() && null != property.getState()) {
            if (!(property.getStatus().toString().equalsIgnoreCase("C") && citizenPortalUser))
                preparePropertyTaxDetails(property);
            historyMap = propService.populateHistory(property);
        }
        if (property.getState() != null) {
        endorsementRequired = propertyTaxCommonUtils.getEndorsementGenerate(securityUtils.getCurrentUser().getId(), property.getCurrentState());
        ownersName = property.getBasicProperty().getFullOwnerName();
        applicationNumber =  property.getApplicationNo();
        endorsementNotices = propertyTaxCommonUtils.getEndorsementNotices(applicationNumber);
    }
        documentTypes = propService.getDocumentTypesForTransactionType(TransactionType.CREATE);
        assessmentDocumentTypes = propService.getDocumentTypesForTransactionType(TransactionType.CREATE_ASMT_DOC);
        final List<FloorType> floorTypeList = floorTypeRepository.findByActiveTrueOrderByName();
        final List<RoofType> roofTypeList = roofTypeRepository.findByActiveTrueOrderByName();
        final List<WallType> wallTypeList = wallTypeRepository.findByActiveTrueOrderByName();
        final List<WoodType> woodTypeList = woodTypeRepository.findByActiveTrueOrderByName();
        final List<PropertyTypeMaster> propTypeList = getPersistenceService()
                .findAllBy("from PropertyTypeMaster where type != 'EWSHS' order by orderNo");
        final List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
        List<PropertyMutationMaster> mutationList;
        if (StringUtils.isBlank(applicationSource))
            mutationList = getPersistenceService().findAllBy("from PropertyMutationMaster pmm where pmm.type=?",
                    PROP_CREATE_RSN);
        else
            mutationList = getPersistenceService().findAllBy(
                    "from PropertyMutationMaster pmm where pmm.type=? and pmm.code=?", PROP_CREATE_RSN,
                    PROP_CREATE_RSN_NEWPROPERTY_CODE);
        if (null != property && property.getMeesevaServiceCode() != null) {
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVA_SERVICE_CODE_NEWPROPERTY))
                getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_CODE);
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVA_SERVICE_CODE_SUBDIVISION))
                getMutationListByCode(PROP_CREATE_RSN_BIFUR);
        }
        final List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
        final List<String> structureList = getPersistenceService()
                .findAllBy("from StructureClassification where isActive = true order by typeName ");
        final List<String> apartmentsList = getPersistenceService().findAllBy("from Apartment order by name");
        final List<String> taxExemptionReasonList = getPersistenceService()
                .findAllBy("from TaxExemptionReason where isActive = true order by name");

        final List<Boundary> localityList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE);
        final List<Boundary> zones = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> electionWardList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD, ADMIN_HIERARCHY_TYPE);
        final List<Boundary> enumerationBlockList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ELECTIONWARD_BNDRY_TYPE,
                        ELECTION_HIERARCHY_TYPE);

        addDropdownData("zones", zones);
        addDropdownData("wards", Collections.emptyList());
        addDropdownData("streets", Collections.emptyList());
        addDropdownData("blocks", Collections.emptyList());
        setDeviationPercentageMap(DEVIATION_PERCENTAGE);
        addDropdownData("PropTypeMaster", propTypeList);
        addDropdownData("floorType", floorTypeList);
        addDropdownData("roofType", roofTypeList);
        addDropdownData("wallType", wallTypeList);
        addDropdownData("woodType", woodTypeList);
        addDropdownData("apartments", apartmentsList);

        addDropdownData("OccupancyList", propOccList);
        addDropdownData("StructureList", structureList);
        addDropdownData("AgeFactorList", ageFacList);
        addDropdownData("MutationList", mutationList);
        addDropdownData("LocationFactorList", Collections.emptyList());
        setFloorNoMap(FLOOR_MAP);
        addDropdownData("localityList", localityList);
        addDropdownData("electionWardList", electionWardList);
        addDropdownData("enumerationBlockList", enumerationBlockList);
        addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId)) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                    Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
            else
                setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
        } else
            setPropTypeCategoryMap(Collections.emptyMap());
       if(property!=null)
           populateUsages(isNotBlank(propertyCategory) ? propertyCategory : property.getPropertyDetail().getCategoryType());
       else
           populateUsages(propertyCategory);
       // tax exempted properties
        addDropdownData(EXEMPTED_REASON_LIST, CommonServices.getTaxExemptedList());

        // Loading Property Department based on ownership of property
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId)) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                    Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT))
                setPropertyDepartmentList(propertyDepartmentRepository.getAllStateDepartments());
            else if (propTypeMstr.getCode().startsWith("CENTRAL_GOVT"))
                setPropertyDepartmentList(propertyDepartmentRepository.getAllCentralDepartments());
            else if (propTypeMstr.getCode().equals(PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE))
                setPropertyDepartmentList(propertyDepartmentRepository.getAllPrivateDepartments());
        }
        setVacantLandPlotAreaList(vacantLandPlotAreaRepository.findAll());
        setLayoutApprovalAuthorityList(layoutApprovalAuthorityRepository.findAll());
        addDropdownData("vacantLandPlotAreaList", vacantLandPlotAreaList);
        addDropdownData("layoutApprovalAuthorityList", layoutApprovalAuthorityList);
        setAssessmentDocumentNames(PropertyTaxConstants.ASSESSMENT_DOCUMENT_NAMES);
        addDropdownData("assessmentDocumentNameList", assessmentDocumentNames);
        super.prepare();
    }

    private BasicProperty createBasicProp(final Character status) {
        final BasicProperty basicProperty = new BasicPropertyImpl();
        final PropertyStatus propStatus = (PropertyStatus) getPersistenceService()
                .find("from PropertyStatus where statusCode=?", PROPERTY_STATUS_WORKFLOW);
        basicProperty.setRegdDocDate(property.getBasicProperty().getRegdDocDate());
        basicProperty.setRegdDocNo(property.getBasicProperty().getRegdDocNo());
        basicProperty.setActive(Boolean.TRUE);
        basicProperty.setAddress(createPropAddress());
        basicProperty.setPropertyID(createPropertyID(basicProperty));
        basicProperty.setStatus(propStatus);
        basicProperty.setUnderWorkflow(true);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
                .find(MUTATION_MASTER_QUERY, PROP_CREATE_RSN, mutationId);
        basicProperty.setPropertyMutationMaster(propertyMutationMaster);
        if (propertyMutationMaster.getCode().equals(PROP_CREATE_RSN_BIFUR)
                || property.getPropertyDetail().isAppurtenantLandChecked())
            basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, PROP_CREATE_RSN, null,
                    null, null, null, getParentIndex()));

        basicProperty.setBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
        if (!property.getPropertyDetail().isAppurtenantLandChecked()) {
            basicPropertyService.createOwners(property, basicProperty, ownerAddress);
            property.setBasicProperty(basicProperty);
        } else
            basicPropertyService.createOwnersForAppurTenant(property, basicProperty, ownerAddress);
        property.setPropertyModifyReason(PROP_CREATE_RSN);
        return basicProperty;
    }

    private void addDemandAndCompleteDate(final Character status, final BasicProperty basicProperty,
            final PropertyMutationMaster propertyMutationMaster) throws TaxCalculatorExeption {
        taxExemptionId = taxExemptionId == null || taxExemptionId.isEmpty() ? "-1" : taxExemptionId;
        property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
                propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(),
                getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId(), Boolean.FALSE);
        property.setStatus(status);
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
        basicProperty.setPropOccupationDate(propCompletionDate);
        if (propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            property.setPropertyDetail(changePropertyDetail(property));
        basicProperty.addProperty(property);
        try {
            if (basicProperty.getSource() == PropertyTaxConstants.SOURCEOFDATA_APPLICATION) {
                if (property != null && !property.getDocuments().isEmpty())
                    propService.processAndStoreDocument(property.getDocuments());

                propService.createDemand(property, propCompletionDate);
            }
        } catch (final TaxCalculatorExeption e) {
            throw new TaxCalculatorExeption();
        }
    }

    /**
     * Changes the property details from {@link BuiltUpProperty} to {@link VacantProperty}
     *
     * @return vacant property details
     * @see VacantProperty
     */

    private VacantProperty changePropertyDetail(final PropertyImpl property) {
        final PropertyDetail propertyDetail = property.getPropertyDetail();
        if (getDocumentTypeDetails().getDocumentName() != null && getDocumentTypeDetails().getDocumentName()
                .equals(PropertyTaxConstants.DOCUMENT_NAME_NOTARY_DOCUMENT))
            propertyDetail.setStructure(true);
        final VacantProperty vacantProperty = new VacantProperty(propertyDetail.getSitalArea(),
                propertyDetail.getTotalBuiltupArea(), propertyDetail.getCommBuiltUpArea(),
                propertyDetail.getPlinthArea(), propertyDetail.getCommVacantLand(), propertyDetail.getNonResPlotArea(),
                false, propertyDetail.getSurveyNumber(), propertyDetail.getFieldVerified(),
                propertyDetail.getFieldVerificationDate(), propertyDetail.getFloorDetails(),
                propertyDetail.getPropertyDetailsID(), propertyDetail.getWaterMeterNum(),
                propertyDetail.getElecMeterNum(), 0, propertyDetail.getFieldIrregular(),
                propertyDetail.getDateOfCompletion(), propertyDetail.getProperty(), propertyDetail.getUpdatedTime(),
                propertyDetail.getPropertyUsage(), null, propertyDetail.getPropertyTypeMaster(),
                propertyDetail.getPropertyType(), propertyDetail.getInstallment(),
                propertyDetail.getPropertyOccupation(), propertyDetail.getPropertyMutationMaster(),
                propertyDetail.getComZone(), propertyDetail.getCornerPlot(),
                propertyDetail.getExtentSite() != null ? propertyDetail.getExtentSite() : 0.0,
                propertyDetail.getExtentAppartenauntLand() != null ? propertyDetail.getExtentAppartenauntLand() : 0.0,
                propertyDetail.getFloorType(), propertyDetail.getRoofType(), propertyDetail.getWallType(),
                propertyDetail.getWoodType(), propertyDetail.isLift(), propertyDetail.isToilets(),
                propertyDetail.isWaterTap(), propertyDetail.isStructure(), propertyDetail.isElectricity(),
                propertyDetail.isAttachedBathRoom(), propertyDetail.isWaterHarvesting(), propertyDetail.isCable(),
                propertyDetail.getSiteOwner(), propertyDetail.getPattaNumber(), propertyDetail.getCurrentCapitalValue(),
                propertyDetail.getMarketValue(), propertyDetail.getCategoryType(),
                                propertyDetail.getOccupancyCertificationNo(), propertyDetail.getOccupancyCertificationDate(),
                                propertyDetail.isAppurtenantLandChecked(),
                propertyDetail.isCorrAddressDiff(), propertyDetail.getPropertyDepartment(),
                propertyDetail.getVacantLandPlotArea(), propertyDetail.getLayoutApprovalAuthority(),
                propertyDetail.getLayoutPermitNo(), propertyDetail.getLayoutPermitDate());

        vacantProperty.setManualAlv(propertyDetail.getManualAlv());
        vacantProperty.setOccupierName(propertyDetail.getOccupierName());
        return vacantProperty;
    }

    private void updatePropAddress(final BasicProperty basicProperty) {
        final Address propAddr = basicProperty.getAddress();
        propAddr.setHouseNoBldgApt(getHouseNumber());
        propAddr.setAreaLocalitySector(boundaryService.getBoundaryById(getLocality()).getName());
        final String cityName = ApplicationThreadLocals.getCityName();
        propAddr.setStreetRoadLine(boundaryService.getBoundaryById(getWardId()).getName());
        propAddr.setCityTownVillage(cityName);

        if (getPinCode() != null && !getPinCode().isEmpty())
            propAddr.setPinCode(getPinCode());
        for (final PropertyOwnerInfo owner : basicProperty.getPropertyOwnerInfo())
            for (final Address address : owner.getOwner().getAddress())
                if (null != address)
                    ownerAddress = address;
        if (!(property.getPropertyDetail().isCorrAddressDiff() != null
                && property.getPropertyDetail().isCorrAddressDiff())) {
            ownerAddress.setAreaLocalitySector(propAddr.getAreaLocalitySector());
            ownerAddress.setHouseNoBldgApt(propAddr.getHouseNoBldgApt());
            ownerAddress.setStreetRoadLine(propAddr.getStreetRoadLine());
            ownerAddress.setCityTownVillage(cityName);
            ownerAddress.setPinCode(propAddr.getPinCode());
        } else {
            final String[] corrAddr = getCorrAddress1().split(",");
            if (corrAddr.length == 1)
                ownerAddress.setAreaLocalitySector(getCorrAddress1());
            else
                ownerAddress.setAreaLocalitySector(corrAddr[1]);
            ownerAddress.setStreetRoadLine(getCorrAddress2());
            ownerAddress.setCityTownVillage(cityName);
            ownerAddress.setPinCode(getCorrPinCode());
            ownerAddress.setHouseNoBldgApt(null);
        }
    }

    private PropertyAddress createPropAddress() {
        final Address propAddr = new PropertyAddress();
        propAddr.setHouseNoBldgApt(getHouseNumber());
        propAddr.setAreaLocalitySector(boundaryService.getBoundaryById(getLocality()).getName());
        final String cityName = ApplicationThreadLocals.getCityName();
        propAddr.setStreetRoadLine(boundaryService.getBoundaryById(getWardId()).getName());
        propAddr.setCityTownVillage(cityName);

        if (getPinCode() != null && !getPinCode().isEmpty())
            propAddr.setPinCode(getPinCode());
        if (!(property.getPropertyDetail().isCorrAddressDiff() != null
                && property.getPropertyDetail().isCorrAddressDiff())) {
            ownerAddress = new CorrespondenceAddress();
            ownerAddress.setAreaLocalitySector(propAddr.getAreaLocalitySector());
            ownerAddress.setHouseNoBldgApt(propAddr.getHouseNoBldgApt());
            ownerAddress.setStreetRoadLine(propAddr.getStreetRoadLine());
            ownerAddress.setCityTownVillage(cityName);
            ownerAddress.setPinCode(propAddr.getPinCode());
        } else {
            ownerAddress = new CorrespondenceAddress();
            ownerAddress.setAreaLocalitySector(getCorrAddress1());
            ownerAddress.setStreetRoadLine(getCorrAddress2());
            ownerAddress.setCityTownVillage(cityName);
            ownerAddress.setPinCode(getCorrPinCode());
        }
        return (PropertyAddress) propAddr;
    }

    private PropertyID createPropertyID(final BasicProperty basicProperty) {
        final PropertyID propertyId = new PropertyID();
        propertyId.setZone(boundaryService.getBoundaryById(getZoneId()));
        propertyId.setWard(boundaryService.getBoundaryById(getWardId()));
        propertyId.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        propertyId.setCreatedDate(new Date());
        propertyId.setModifiedDate(new Date());
        propertyId.setModifiedDate(new Date());
        propertyId.setArea(boundaryService.getBoundaryById(getBlockId()));
        propertyId.setLocality(boundaryService.getBoundaryById(getLocality()));
        if (getStreetId() != null && getStreetId() != -1)
            propertyId.setStreet(boundaryService.getBoundaryById(getStreetId()));
        propertyId.setEastBoundary(getEastBoundary());
        propertyId.setWestBoundary(getWestBoundary());
        propertyId.setNorthBoundary(getNorthBoundary());
        propertyId.setSouthBoundary(getSouthBoundary());
        propertyId.setBasicProperty(basicProperty);
        return propertyId;
    }

    @Override
    public void validate() {

        if (null != property && property.getMeesevaServiceCode() != null
                && !"".equals(property.getMeesevaServiceCode())) {
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVA_SERVICE_CODE_NEWPROPERTY))
                getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_CODE);
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVA_SERVICE_CODE_SUBDIVISION))
                getMutationListByCode(PROP_CREATE_RSN_BIFUR);
        }
        if (locality == null || locality == -1)
            addActionError(getText("mandatory.localityId"));

        if (null != propTypeId && !"-1".equals(propTypeId))
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                    Long.valueOf(propTypeId));
        if (zoneId == null || zoneId == -1)
            addActionError(getText("mandatory.zone"));
        if (wardId == null || wardId == -1)
            addActionError(getText("mandatory.ward"));
        if (blockId == null || blockId == -1)
            addActionError(getText("mandatory.block"));
        else if (null != propTypeMstr && !propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            if (!StringUtils.isBlank(houseNumber)) {
                if (propService.isDuplicateDoorNumber(houseNumber, basicProp))
                    addActionError(getText("houseNo.duplicate"));
            }

            else if (property != null && property.getState() != null && property.getState().getNextAction() != null
                    && property.getState().getNextAction()
                            .equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING))
                addActionError(getText("mandatory.doorNo"));

        if (electionWardId == null || electionWardId == -1)
            addActionError(getText("mandatory.election.ward"));
        validateOwnerDetails(property);
        validateProperty(property, areaOfPlot, eastBoundary, westBoundary, southBoundary,
                northBoundary, propTypeId,
                null, null, vacantLandPlotAreaId,
                layoutApprovalAuthorityId, getDocumentTypeDetails());

        if (isBlank(pinCode))
            addActionError(getText("mandatory.pincode"));
        if (property.getPropertyDetail().isCorrAddressDiff() != null
                && property.getPropertyDetail().isCorrAddressDiff()) {
            if (isBlank(corrAddress1))
                addActionError(getText("mandatory.corr.addr1"));
            if (isBlank(corrPinCode) && corrPinCode.length() < 6)
                addActionError(getText("mandatory.corr.pincode.size"));
        }

        if (null != mutationId && mutationId != -1) {
            final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
                    .find("from PropertyMutationMaster pmm where pmm.id=?", mutationId);
            if (propertyMutationMaster.getCode().equals(PROP_CREATE_RSN_BIFUR))
                if (StringUtils.isNotBlank(parentIndex)) {
                    final BasicProperty basicProperty = basicPropertyService
                            .find("From BasicPropertyImpl where upicNo = ? ", parentIndex);
                    checkIfParentIsUnderWorkflow(basicProperty);
                    if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
                        final Area area = new Area();
                        area.setArea(new Float(areaOfPlot));
                        property.getPropertyDetail().setSitalArea(area);
                        if (basicProperty != null && basicProperty.getActiveProperty() != null) {
                            property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
                            final String errorKey = propService.validationForBifurcation(property, basicProperty,
                                    propertyMutationMaster.getCode());
                            if (!isBlank(errorKey))
                                addActionError(getText(errorKey));
                        } else
                            addActionError(getText("error.parent"));
                    }
                } else
                    addActionError(getText("error.parent.index"));
        } else
            addActionError(getText("mandatory.createRsn"));

        if (loggedUserIsMeesevaUser || citizenPortalUser || !propertyByEmployee) {
            final PropertyID propertyid = new PropertyID();
            propertyid.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
            property.getBasicProperty().setPropertyID(propertyid);

            if (null != getElectionWardId() && getElectionWardId() != -1 && null != property.getBasicProperty()) {
                final Assignment assignment = propService.isCscOperator(securityUtils.getCurrentUser())
                        ? propService.getAssignmentByDeptDesigElecWard(property.getBasicProperty()) : null;
                if (assignment == null && propService.getUserPositionByZone(property.getBasicProperty(), false) == null)
                    addActionError(getText(NOTEXISTS_POSITION));
            }

        } else if (property.getId() == null && !isDataEntryOperator) {
            final Assignment initiator = propertyTaxCommonUtils
                    .getWorkflowInitiatorAssignment(securityUtils.getCurrentUser().getId());
            if (initiator == null)
                addActionError(getText(NOTEXISTS_POSITION));
        }
        validateApproverDetails();
        super.validate();
        if (isBlank(getModelId()))
            showCalculateTaxButton();
        if (propTypeMstr != null
                && propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT)
                && propertyDepartmentId == null && property.getId() != null)
            addActionError(getText("mandatory.property.department"));
        if (upicNo == null || "".equals(upicNo))
            validateDocumentDetails(getDocumentTypeDetails());
    }

	private void checkIfParentIsUnderWorkflow(final BasicProperty basicProperty) {
		if(basicProperty.isUnderWorkflow())
			addActionError(getText("error.parent.under.wf"));
	}

    @SkipValidation
    @Action(value = "/createProperty-printAck")
    public String printAck() {
        if (ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName())
                && ApplicationThreadLocals.getUserId() == null)
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        reportId = reportViewerUtil
                .addReportToTempCache(basicPropertyService.propertyAcknowledgement(property));
        return PRINT_ACK;
    }

    @SkipValidation
    @Action(value = "/createProperty-dataEntry")
    public String dataEntry() {
        setDataEntry(Boolean.TRUE);
        return RESULT_DATAENTRY;
    }

    @SkipValidation
    @Action(value = "/createProperty-updateDataEntry")
    public String updateDataEntry() {
        upicNo = indexNumber;
        validate();
        if (hasErrors())
            return EDIT_DATA_ENTRY;
        basicProp.setRegdDocDate(property.getBasicProperty().getRegdDocDate());
        basicProp.setRegdDocNo(property.getBasicProperty().getRegdDocNo());
        basicProp.setActive(Boolean.TRUE);
        basicProp.setSource(PropertyTaxConstants.SOURCEOFDATA_DATAENTRY);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
                .find(MUTATION_MASTER_QUERY, PROP_CREATE_RSN, mutationId);
        basicProp.setPropertyMutationMaster(propertyMutationMaster);
        basicProp.setBoundary(boundaryService.getBoundaryById(getElectionWardId()));

        updatePropAddress(basicProp);
        updatePropertyId(basicProp);

        basicPropertyService.updateOwners(property, basicProp, ownerAddress);
        property.setBasicProperty(basicProp);
        propService.updatePropertyDetail(property, floorTypeId, roofTypeId, wallTypeId, woodTypeId, areaOfPlot,
                propertyCategory, nonResPlotArea, propUsageId, propOccId, propTypeId);

        if (StringUtils.isNotBlank(taxExemptionId) && !taxExemptionId.equals("-1")) {
            final TaxExemptionReason taxExemptionReason = (TaxExemptionReason) persistenceService
                    .find("From TaxExemptionReason where id = ?", Long.valueOf(taxExemptionId));
            property.setTaxExemptedReason(taxExemptionReason);
            property.setIsExemptedFromTax(Boolean.TRUE);
        }
        if (StringUtils.isBlank(taxExemptionId)) {
            property.setTaxExemptedReason(null);
            property.setIsExemptedFromTax(Boolean.FALSE);
        }

        propService.updateFloorDetails(property, getFloorDetails());
        basicPropertyService.update(basicProp);
        setAckMessage("Property data entry modified successfully for Assessment No ");
        return RESULT_ACK;
    }

    @ValidationErrorPage("dataEntry")
    @Action(value = "/createProperty-createDataEntry")
    public String save() {
        if (upicNo == null || "".equals(upicNo)) {
            addActionError(getText("mandatory.indexNumber"));
            return RESULT_DATAENTRY;
        }

        propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                Long.valueOf(propTypeId));

        if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND) && StringUtils.isBlank(houseNumber)) {
            addActionError(getText("mandatory.doorNo"));
            return RESULT_DATAENTRY;
        }
        final BasicProperty basicProperty = createBasicProp(PropertyTaxConstants.STATUS_ISACTIVE);
        basicProperty.setUnderWorkflow(false);
        basicProperty.setSource(PropertyTaxConstants.SOURCEOFDATA_DATAENTRY);
        basicProperty.setOldMuncipalNum(upicNo);
        basicProperty.setUpicNo(propertyTaxNumberGenerator.generateAssessmentNumber());

        try {
            addDemandAndCompleteDate(PropertyTaxConstants.STATUS_ISACTIVE, basicProperty,
                    basicProperty.getPropertyMutationMaster());
        } catch (final TaxCalculatorExeption e) {
            basicProperty.setPropertyOwnerInfoProxy(basicProperty.getPropertyOwnerInfo());
            addActionError(getText(UNIT_RATE_ERROR));
            logger.error("save : There are no Unit rates defined for chosen combinations", e);
            return RESULT_NEW;
        }
        basicProperty.setAssessmentdate(propCompletionDate);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        basicPropertyService.persist(basicProperty);
        setBasicProp(basicProperty);
        setAckMessage("Property data entry saved in the system successfully and created with Assessment No "
                + basicProperty.getUpicNo());
        return "dataEntry-ack";
    }

    @SkipValidation
    @Action(value = "/createProperty-calculateTax")
    public void calculateTax() {
        validate();
        if (hasErrors())
            try {
                ServletActionContext.getResponse().getWriter().write(getText("enter.mandatory.fields"));
            } catch (final IOException e) {
                logger.error("calculateTax() : User has not entered all the mandatory fields", e);
            }
        else {
            HashMap<Installment, TaxCalculationInfo> instTaxMap = new HashMap<>();
            final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService()
                    .find(MUTATION_MASTER_QUERY, PROP_CREATE_RSN, mutationId);
            BasicProperty basicProperty = new BasicPropertyImpl();
            if (basicProp == null) {
                basicProperty = createBasicProp(STATUS_DEMAND_INACTIVE);
                basicProperty.setPropertyMutationMaster(propertyMutationMaster);
            } else {
                updatePropertyId(basicProp);
                basicProp.setPropertyMutationMaster(propertyMutationMaster);
            }
            taxExemptionId = taxExemptionId == null || taxExemptionId.isEmpty() ? "-1" : taxExemptionId;
            property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(),
                    propTypeId, propUsageId, propOccId, STATUS_DEMAND_INACTIVE, getDocNumber(), getNonResPlotArea(),
                    getFloorTypeId(), getRoofTypeId(), getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId),
                    getPropertyDepartmentId(), getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId(),
                    Boolean.FALSE);
            if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                propCompletionDate = propService
                        .getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetails());
            else
                propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
            if (basicProp == null) {
                basicProperty.setPropOccupationDate(propCompletionDate);
                property.setBasicProperty(basicProperty);
            } else {
                basicProp.setPropOccupationDate(propCompletionDate);
                property.setBasicProperty(basicProp);
            }
            if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
                property.setPropertyDetail(propService.changePropertyDetail(property, property.getPropertyDetail(), 0)
                        .getPropertyDetail());
            try {
                instTaxMap = taxCalculator.calculatePropertyTax(property, propCompletionDate);
            } catch (final TaxCalculatorExeption e) {
                logger.error("calculateTax() : There are no Unit rates defined for chosen combinations", e);
            }
            final String resultString = propertyTaxCommonUtils.getCurrentHalfyearTax(instTaxMap, propTypeMstr);
            final String jsonsString = new GsonBuilder().create().toJson(resultString);
            ServletActionContext.getResponse().setContentType("application/json");
            try {
                ServletActionContext.getResponse().getWriter().write(jsonsString);
            } catch (final IOException e) {
                logger.error("calculateTax() : Error while writing response", e);
            }
        }
    }

    public void validateInitiatorDesgn() {
        final User currentUser = securityUtils.getCurrentUser();
        if (propService.isEmployee(currentUser) && !propertyTaxCommonUtils.isEligibleInitiator(currentUser.getId())) {
            setEligibleInitiator(false);
            addActionError(getText("initiator.noteligible"));
        }
    }

    @Override
    public PropertyImpl getProperty() {
        return property;
    }

    @Override
    public void setProperty(final PropertyImpl property) {
        this.property = property;
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

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(final Long blockId) {
        this.blockId = blockId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(final String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(final String addressStr) {
        this.addressStr = addressStr;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(final String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAreaOfPlot() {
        return areaOfPlot;
    }

    public void setAreaOfPlot(final String areaOfPlot) {
        this.areaOfPlot = areaOfPlot;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(final String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public SortedMap<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(final SortedMap<Integer, String> floorNoMap) {
        this.floorNoMap = floorNoMap;
    }

    public String getCorrAddress1() {
        return corrAddress1;
    }

    public void setCorrAddress1(final String corrAddress1) {
        this.corrAddress1 = corrAddress1;
    }

    public String getCorrAddress2() {
        return corrAddress2;
    }

    public void setCorrAddress2(final String corrAddress2) {
        this.corrAddress2 = corrAddress2;
    }

    public String getCorrPinCode() {
        return corrPinCode;
    }

    public void setCorrPinCode(final String corrPinCode) {
        this.corrPinCode = corrPinCode;
    }

    public BasicProperty getBasicProp() {
        return basicProp;
    }

    public void setBasicProp(final BasicProperty basicProp) {
        this.basicProp = basicProp;
    }

    public PropertyService getPropService() {
        return propService;
    }

    public void setPropService(final PropertyService propService) {
        this.propService = propService;
    }

    public Long getMutationId() {
        return mutationId;
    }

    public void setMutationId(final Long mutationId) {
        this.mutationId = mutationId;
    }

    public String getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(final String parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(final String amenities) {
        this.amenities = amenities;
    }

    public String[] getFloorNoStr() {
        return floorNoStr;
    }

    public void setFloorNoStr(final String[] floorNoStr) {
        this.floorNoStr = floorNoStr;
    }

    public String getPropTypeId() {
        return propTypeId;
    }

    public void setPropTypeId(final String propTypeId) {
        this.propTypeId = propTypeId;
    }

    public String getPropUsageId() {
        return propUsageId;
    }

    public void setPropUsageId(final String propUsageId) {
        this.propUsageId = propUsageId;
    }

    public String getPropOccId() {
        return propOccId;
    }

    public void setPropOccId(final String propOccId) {
        this.propOccId = propOccId;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(final String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public Map<String, String> getPropTypeCategoryMap() {
        return propTypeCategoryMap;
    }

    public void setPropTypeCategoryMap(final Map<String, String> propTypeCategoryMap) {
        this.propTypeCategoryMap = propTypeCategoryMap;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(final String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public PropertyTypeMaster getPropTypeMstr() {
        return propTypeMstr;
    }

    public void setPropTypeMstr(final PropertyTypeMaster propTypeMstr) {
        this.propTypeMstr = propTypeMstr;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public String getNonResPlotArea() {
        return nonResPlotArea;
    }

    public void setNonResPlotArea(final String nonResPlotArea) {
        this.nonResPlotArea = nonResPlotArea;
    }

    public void setPropertyTaxNumberGenerator(final PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
        this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
    }

    public PropertyImpl getNewProperty() {
        return newProperty;
    }

    public void setNewProperty(final PropertyImpl newProperty) {
        this.newProperty = newProperty;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(final Long locality) {
        this.locality = locality;
    }

    public Long getFloorTypeId() {
        return floorTypeId;
    }

    public void setFloorTypeId(final Long floorTypeId) {
        this.floorTypeId = floorTypeId;
    }

    public Long getRoofTypeId() {
        return roofTypeId;
    }

    public void setRoofTypeId(final Long roofTypeId) {
        this.roofTypeId = roofTypeId;
    }

    public Long getWallTypeId() {
        return wallTypeId;
    }

    public void setWallTypeId(final Long wallTypeId) {
        this.wallTypeId = wallTypeId;
    }

    public Long getWoodTypeId() {
        return woodTypeId;
    }

    public void setWoodTypeId(final Long woodTypeId) {
        this.woodTypeId = woodTypeId;
    }

    public Long getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(final Long ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(final String blockName) {
        this.blockName = blockName;
    }

    @Override
    public AssignmentService getAssignmentService() {
        return assignmentService;
    }

    @Override
    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Long getElectionWardId() {
        return electionWardId;
    }

    public void setElectionWardId(final Long electionWardId) {
        this.electionWardId = electionWardId;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public void setBoundaryService(final BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    public void setSecurityUtils(final SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public void setbasicPropertyService(final PropertyPersistenceService basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    public void setPropCompletionDate(final Date propCompletionDate) {
        this.propCompletionDate = propCompletionDate;
    }

    public String getReportId() {
        return reportId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(final boolean approved) {
        this.approved = approved;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(final String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(final String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(final String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(final String westBoundary) {
        this.westBoundary = westBoundary;
    }

    public Map<String, String> getDeviationPercentageMap() {
        return deviationPercentageMap;
    }

    public void setDeviationPercentageMap(final Map<String, String> deviationPercentageMap) {
        this.deviationPercentageMap = deviationPercentageMap;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(final List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Address getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(final Address ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getApplicationNoMessage() {
        return applicationNoMessage;
    }

    public void setApplicationNoMessage(final String applicationNoMessage) {
        this.applicationNoMessage = applicationNoMessage;
    }

    public String getAssessmentNoMessage() {
        return assessmentNoMessage;
    }

    public void setAssessmentNoMessage(final String assessmentNoMessage) {
        this.assessmentNoMessage = assessmentNoMessage;
    }

    public String getPropertyInitiatedBy() {
        return propertyInitiatedBy;
    }

    public void setPropertyInitiatedBy(final String propertyInitiatedBy) {
        this.propertyInitiatedBy = propertyInitiatedBy;
    }

    @Override
    public String getAdditionalRule() {
        return NEW_ASSESSMENT;
    }

    public String getUpicNo() {
        return upicNo;
    }

    public void setUpicNo(final String upicNo) {
        this.upicNo = upicNo;
    }

    public String getTaxExemptionId() {
        return taxExemptionId;
    }

    public void setTaxExemptionId(final String taxExemptionId) {
        this.taxExemptionId = taxExemptionId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(final Long streetId) {
        this.streetId = streetId;
    }

    public Map<String, BigDecimal> getPropertyTaxDetailsMap() {
        return propertyTaxDetailsMap;
    }

    public void setPropertyTaxDetailsMap(final Map<String, BigDecimal> propertyTaxDetailsMap) {
        this.propertyTaxDetailsMap = propertyTaxDetailsMap;
    }

    @Override
    public String getIndexNumber() {
        return indexNumber;
    }

    @Override
    public void setIndexNumber(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getModifyRsn() {
        return modifyRsn;
    }

    public void setModifyRsn(final String modifyRsn) {
        this.modifyRsn = modifyRsn;
    }

    @Override
    public String getPendingActions() {
        return getProperty() != null ? getProperty().getCurrentState().getNextAction() : null;
    }

    @Override
    public String getCurrentDesignation() {
        return getProperty().getId() != null && !(getProperty().getCurrentState().getValue().endsWith(STATUS_REJECTED)
                || getProperty().getCurrentState().getValue().endsWith(WFLOW_ACTION_NEW))
                        ? propService.getDesignationForPositionAndUser(
                                getProperty().getCurrentState().getOwnerPosition().getId(),
                                securityUtils.getCurrentUser().getId())
                        : null;
    }

    public Boolean getShowTaxCalcBtn() {
        return showTaxCalcBtn;
    }

    public void setShowTaxCalcBtn(final Boolean showTaxCalcBtn) {
        this.showTaxCalcBtn = showTaxCalcBtn;
    }

    public Long getPropertyDepartmentId() {
        return propertyDepartmentId;
    }

    public void setPropertyDepartmentId(final Long propertyDepartmentId) {
        this.propertyDepartmentId = propertyDepartmentId;
    }

    public List<PropertyDepartment> getPropertyDepartmentList() {
        return propertyDepartmentList;
    }

    public void setPropertyDepartmentList(final List<PropertyDepartment> propertyDepartmentList) {
        this.propertyDepartmentList = propertyDepartmentList;
    }

    public List<VacantLandPlotArea> getVacantLandPlotAreaList() {
        return vacantLandPlotAreaList;
    }

    public void setVacantLandPlotAreaList(final List<VacantLandPlotArea> vacantLandPlotAreaList) {
        this.vacantLandPlotAreaList = vacantLandPlotAreaList;
    }

    public List<LayoutApprovalAuthority> getLayoutApprovalAuthorityList() {
        return layoutApprovalAuthorityList;
    }

    public void setLayoutApprovalAuthorityList(final List<LayoutApprovalAuthority> layoutApprovalAuthorityList) {
        this.layoutApprovalAuthorityList = layoutApprovalAuthorityList;
    }

    public Long getVacantLandPlotAreaId() {
        return vacantLandPlotAreaId;
    }

    public void setVacantLandPlotAreaId(final Long vacantLandPlotAreaId) {
        this.vacantLandPlotAreaId = vacantLandPlotAreaId;
    }

    public Long getLayoutApprovalAuthorityId() {
        return layoutApprovalAuthorityId;
    }

    public void setLayoutApprovalAuthorityId(final Long layoutApprovalAuthorityId) {
        this.layoutApprovalAuthorityId = layoutApprovalAuthorityId;
    }

    public boolean isAllowEditDocument() {
        return allowEditDocument;
    }

    public void setAllowEditDocument(final boolean allowEditDocument) {
        this.allowEditDocument = allowEditDocument;
    }

    public List<DocumentType> getAssessmentDocumentTypes() {
        return assessmentDocumentTypes;
    }

    public void setAssessmentDocumentTypes(final List<DocumentType> assessmentDocumentTypes) {
        this.assessmentDocumentTypes = assessmentDocumentTypes;
    }

    public List<String> getAssessmentDocumentNames() {
        return assessmentDocumentNames;
    }

    public void setAssessmentDocumentNames(final List<String> assessmentDocumentNames) {
        this.assessmentDocumentNames = assessmentDocumentNames;
    }

    public DocumentTypeDetails getDocumentTypeDetails() {
        return documentTypeDetails;
    }

    public void setDocumentTypeDetails(final DocumentTypeDetails documentTypeDetails) {
        this.documentTypeDetails = documentTypeDetails;
    }

    public boolean isFloorDetailsEntered() {
        return floorDetailsEntered;
    }

    public void setFloorDetailsEntered(final boolean floorDetailsEntered) {
        this.floorDetailsEntered = floorDetailsEntered;
    }

    public boolean isEligibleInitiator() {
        return eligibleInitiator;
    }

    public void setEligibleInitiator(final boolean eligibleInitiator) {
        this.eligibleInitiator = eligibleInitiator;
    }

    public boolean isDataEntry() {
        return dataEntry;
    }

    public void setDataEntry(final boolean dataEntry) {
        this.dataEntry = dataEntry;
    }

    public String getApplicationSource() {
        return applicationSource;
    }

    public void setApplicationSource(final String applicationSource) {
        this.applicationSource = applicationSource;
    }
    
    public boolean isCitizenPortalUser() {
        return citizenPortalUser;
    }

    public void setCitizenPortalUser(boolean citizenPortalUser) {
        this.citizenPortalUser = citizenPortalUser;
    }

    public String getSitalArea() {
        return sitalArea;
    }

    public void setSitalArea(String sitalArea) {
        this.sitalArea = sitalArea;
    }
}