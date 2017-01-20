/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.ptis.actions.create;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_NEW_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.DEVIATION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.GUARDIAN_RELATION;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN_BIFUR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN_NEWPROPERTY_BIFURCATION_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN_NEWPROPERTY_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_NOTCREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_DEMAND_INACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

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
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDepartment;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.repository.PropertyDepartmentRepository;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.GsonBuilder;

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
        @Result(name = CreatePropertyAction.PRINTACK, location = "create/createProperty-printAck.jsp"),
        @Result(name = CreatePropertyAction.MEESEVA_RESULT_ACK, location = "common/meesevaAck.jsp"),
        @Result(name = CreatePropertyAction.EDIT_DATA_ENTRY, location = "create/createProperty-editDataEntry.jsp") })
public class CreatePropertyAction extends PropertyTaxBaseAction {

    private static final long serialVersionUID = -2329719786287615451L;
    private static final String RESULT_ACK = "ack";
    private static final String RESULT_NEW = "new";
    private static final String RESULT_ERROR = "error";
    private static final String RESULT_VIEW = "view";
    private static final String MSG_REJECT_SUCCESS = " Property Rejected Successfully ";
    private static final String CREATE = "create";
    private static final String RESULT_DATAENTRY = "dataEntry";
    public static final String PRINTACK = "printAck";
    public static final String MEESEVA_RESULT_ACK = "meesevaAck";
    protected static final String EDIT_DATA_ENTRY = "editDataEntry";
    private final String MEESEVASERVICECODEFORNEWPROPERTY = "PT01";
    private final String MEESEVASERVICECODEFORSUBDIVISION = "PT04";
    private static final String PROPTYPEMASTER_QUERY = "from PropertyTypeMaster ptm where ptm.id = ?";

    private final Logger LOGGER = Logger.getLogger(getClass());
    private PropertyImpl property = new PropertyImpl();
    @Autowired
    private PropertyPersistenceService basicPropertyService;
    @Autowired
    private APTaxCalculator taxCalculator;

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

    private Long mutationId;

    private Map<String, String> propTypeCategoryMap;
    private TreeMap<Integer, String> floorNoMap;
    private Map<String, String> deviationPercentageMap;
    private Map<String, String> guardianRelationMap;
    private List<DocumentType> documentTypes = new ArrayList<>();

    private String reportId;
    private boolean approved;

    private BasicProperty basicProp;
    @Autowired
    private PropertyService propService;
    private PropertyTypeMaster propTypeMstr;
    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private PropertyImpl newProperty = new PropertyImpl();
    private Address ownerAddress = new CorrespondenceAddress();
    Date propCompletionDate = null;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private ReportViewerUtil reportViewerUtil;

    private Boolean loggedUserIsMeesevaUser = Boolean.FALSE;
    private String indexNumber;
    private String modifyRsn;
    private Boolean showTaxCalcBtn = Boolean.FALSE;
    private Long propertyDepartmentId;
    private List<PropertyDepartment> propertyDepartmentList = new ArrayList<>();
    private List<VacantLandPlotArea> vacantLandPlotAreaList = new ArrayList<>();
    private List<LayoutApprovalAuthority> layoutApprovalAuthorityList = new ArrayList<>();
    private Long vacantLandPlotAreaId;
    private Long layoutApprovalAuthorityId;
    
    @Autowired
    transient PropertyDepartmentRepository propertyDepartmentRepository;
    
    @Autowired
    transient VacantLandPlotAreaRepository vacantLandPlotAreaRepository;
    
    @Autowired
    transient LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepository;
    
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

        loggedUserIsMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser) {
            final HttpServletRequest request = ServletActionContext.getRequest();
            if (request.getParameter("applicationNo") == null || request.getParameter("meesevaServicecode") == null) {
                addActionMessage(getText("MEESEVA.005"));
                return RESULT_ERROR;
            } else {

                if (request.getParameter("meesevaServicecode").equalsIgnoreCase(MEESEVASERVICECODEFORNEWPROPERTY))
                    getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_CODE);
                else if (request.getParameter("meesevaServicecode")
                        .equalsIgnoreCase(MEESEVASERVICECODEFORSUBDIVISION))
                    getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_BIFURCATION_CODE);
                property.setMeesevaApplicationNumber(request.getParameter("applicationNo"));
                property.setMeesevaServiceCode(request.getParameter("meesevaServicecode"));
            }
        }
        if (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN) ||
                StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT) ||
                StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT))
            showTaxCalcBtn = Boolean.TRUE;
        return RESULT_NEW;
    }

    @SuppressWarnings("unchecked")
    private void getMutationListByCode(final String code) {
        final List<PropertyMutationMaster> mutationList = getPersistenceService().findAllBy(
                "from PropertyMutationMaster pmm where pmm.type=? and pmm.code=?", PROP_CREATE_RSN, code);
        addDropdownData("MutationList", mutationList);
    }

    @Action(value = "/createProperty-create")
    public String create() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create: Property creation started, Property: " + property + ", zoneId: " + zoneId
                    + ", wardId: " + wardId + ", blockId: " + blockId + ", areaOfPlot: " + areaOfPlot
                    + ", dateOfCompletion: " + dateOfCompletion + ", propTypeId: " + propTypeId + ", propUsageId: "
                    + propUsageId + ", propOccId: " + propOccId);
        final long startTimeMillis = System.currentTimeMillis();
        loggedUserIsMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());

        if (loggedUserIsMeesevaUser && property.getMeesevaApplicationNumber() != null) {
            property.setApplicationNo(property.getMeesevaApplicationNumber());
            property.setSource(PropertyTaxConstants.SOURCEOFDATA_MEESEWA);
        }

        final BasicProperty basicProperty = createBasicProp(STATUS_DEMAND_INACTIVE);
        try {
            addDemandAndCompleteDate(STATUS_DEMAND_INACTIVE, basicProperty, basicProperty.getPropertyMutationMaster());
        } catch (final TaxCalculatorExeption e) {
            basicProperty.setPropertyOwnerInfoProxy(basicProperty.getPropertyOwnerInfo());
            addActionError(getText("unitrate.error"));
            LOGGER.error("create : There are no Unit rates defined for chosen combinations", e);
            return RESULT_NEW;
        }
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create: BasicProperty after creation: " + basicProperty);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        // this should be appending to messgae
        transitionWorkFlow(property);
        basicPropertyService.applyAuditing(property.getState());
        if (loggedUserIsMeesevaUser && property.getMeesevaApplicationNumber() != null)
            basicProperty.setSource(PropertyTaxConstants.SOURCEOFDATA_MEESEWA);
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);

        if (!loggedUserIsMeesevaUser)
            basicPropertyService.persist(basicProperty);
        else {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("ADMISSIONFEE", "0");
            meesevaParams.put("APPLICATIONNUMBER", property.getMeesevaApplicationNumber());
            basicPropertyService.createBasicProperty(basicProperty, meesevaParams);
        }
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        setBasicProp(basicProperty);
        setAckMessage("Property Data Saved Successfully in the System and forwarded to : ");
        setApplicationNoMessage(" with application number : ");
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("create: Property created successfully in system" + "; Time taken(ms) = " + elapsedTimeMillis);
            LOGGER.debug("create: Property creation ended");
        }
        if (!loggedUserIsMeesevaUser)
            return RESULT_ACK;
        else
            return MEESEVA_RESULT_ACK;

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
                propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                        PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));
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
                propTypeMstr = (PropertyTypeMaster) getPersistenceService()
                        .find(PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));
                setPropertyDepartmentId(property.getPropertyDetail().getPropertyDepartment() != null
                        ? property.getPropertyDetail().getPropertyDepartment().getId() : null);
                if (propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT))
                    setPropertyDepartmentList(propertyDepartmentRepository.getAllStateDepartments());
                else if (propTypeMstr.getCode().startsWith("CENTRAL_GOVT"))
                    setPropertyDepartmentList(propertyDepartmentRepository.getAllCentralDepartments());
            }
            if (property.getPropertyDetail().getVacantLandPlotArea() != null)
                vacantLandPlotAreaId = property.getPropertyDetail().getVacantLandPlotArea().getId();
            if (property.getPropertyDetail().getLayoutApprovalAuthority() != null)
                layoutApprovalAuthorityId = property.getPropertyDetail().getLayoutApprovalAuthority().getId();
        }

        if (basicProp != null) {
            basicProp.setPropertyOwnerInfoProxy(basicProp.getPropertyOwnerInfo());
            setMutationId(basicProp.getPropertyMutationMaster().getId());
            final PropertyStatusValues statusValues = (PropertyStatusValues) getPersistenceService().find(
                    "From PropertyStatusValues where basicProperty.id = ?", basicProp.getId());
            if (null != statusValues && null != statusValues.getReferenceBasicProperty())
                setParentIndex(statusValues.getReferenceBasicProperty().getUpicNo());
            if (null != basicProp.getAddress()) {
                setHouseNumber(basicProp.getAddress().getHouseNoBldgApt());
                setPinCode(basicProp.getAddress().getPinCode());
            }

            for (final PropertyOwnerInfo ownerInfo : basicProp.getPropertyOwnerInfo())
                for (final Address ownerAddress : ownerInfo.getOwner().getAddress())
                    if (null != ownerAddress) {
                        final String corrAddress = ownerAddress.getHouseNoBldgApt() + ","
                                + ownerAddress.getAreaLocalitySector();
                        setCorrAddress1(corrAddress);
                        setCorrAddress2(ownerAddress.getStreetRoadLine());
                        setCorrPinCode(ownerAddress.getPinCode());
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
                    setElectionWardId(boundaryService.getBoundaryById(propBoundary.getElectionBoundary().getId())
                            .getId());
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
        }
    }

    @SkipValidation
    @Action(value = "/createProperty-view")
    public String view() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into view, BasicProperty: " + basicProp + ", Property: " + property + ", userDesgn: "
                    + userDesgn);
        final String currState = property.getState().getValue();
        populateFormData();
        if (currState.endsWith(WF_STATE_REJECTED)
                || property.getState().getNextAction() != null && property.getState().getNextAction()
                        .equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING)
                || currState.endsWith(WFLOW_ACTION_NEW)) {
            if (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN) ||
                    StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT) ||
                    StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT))
                showTaxCalcBtn = Boolean.TRUE;
            mode = EDIT;
            return RESULT_NEW;
        } else {
            mode = VIEW;
            for (final PropertyOwnerInfo ownerInfo : basicProp.getPropertyOwnerInfo())
                for (final Address ownerAddress : ownerInfo.getOwner().getAddress())
                    if (null != ownerAddress)
                        setCorrAddress1(ownerAddress.toString());
            setDocNumber(property.getDocNumber());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" Amenities: " + amenities + "NoOfFloors: "
                        + (getFloorDetails() != null ? getFloorDetails().size() : "Floor list is NULL")
                        + " Exiting from view");
            return RESULT_VIEW;
        }
    }

    @SkipValidation
    @Action(value = "/createProperty-editDataEntryForm")
    public String editDataEntryForm() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "Entered into editDataEntryForm, BasicProperty: " + basicProp + ", Property: " + property + ", userDesgn: "
                            + userDesgn);
        upicNo = indexNumber;
        mode = EDIT;
        populateFormData();
        return EDIT_DATA_ENTRY;
    }

    @SkipValidation
    @Action(value = "/createProperty-forward")
    public String forward() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into forward, BasicProperty: " + basicProp + ", Property: " + property
                    + ", userDesgn: " + userDesgn);
        if (mode.equalsIgnoreCase(EDIT)) {
            validate();
            if (hasErrors() && (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN) ||
                    StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT) ||
                    StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT))) {
                showTaxCalcBtn = Boolean.TRUE;
                return RESULT_NEW;
            } else if (hasErrors())
                return RESULT_NEW;
            updatePropertyDetails();
            try {
                propService.createDemand(property, basicProp.getPropOccupationDate());
            } catch (final TaxCalculatorExeption e) {
                addActionError(getText("unitrate.error"));
                LOGGER.error("forward : There are no Unit rates defined for chosen combinations", e);
                return RESULT_NEW;
            }
        } else {
            validateApproverDetails();
            if (hasErrors())
                return RESULT_VIEW;
        }
        transitionWorkFlow(property);

        if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
            return approve();
        else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(workFlowAction))
            return reject();

        basicProp.setUnderWorkflow(true);
        basicPropertyService.applyAuditing(property.getState());
        basicProp.addProperty(property);
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        basicPropertyService.persist(basicProp);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("forward: Property forward started " + property);
        final long startTimeMillis = System.currentTimeMillis();
        setDocNumber(getDocNumber());
        setApplicationNoMessage(" with application number : ");
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("forward: Time taken(ms) = " + elapsedTimeMillis);
            LOGGER.debug("forward: Property forward ended");
        }
        return RESULT_ACK;
    }

    public void updatePropertyDetails() {
        updatePropertyId(basicProp);
        final Character status = STATUS_WORKFLOW;
        updatePropAddress(basicProp);
        basicPropertyService.createOwners(property, basicProp, ownerAddress);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
                "from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?", PROP_CREATE_RSN, mutationId);
        basicProp.setPropertyMutationMaster(propertyMutationMaster);
        taxExemptionId = taxExemptionId == null || taxExemptionId.isEmpty() ? "-1" : taxExemptionId;
        property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
                propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(),
                getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId());
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
        basicProp.setPropOccupationDate(propCompletionDate);

        if (property != null && !property.getDocuments().isEmpty())
            propService.processAndStoreDocument(property.getDocuments());

        if (propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            property.setPropertyDetail(propService.changePropertyDetail(property, property.getPropertyDetail(), 0)
                    .getPropertyDetail());
        if (property.getPropertyDetail().getLayoutApprovalAuthority() != null
                && "No Approval".equals(property.getPropertyDetail().getLayoutApprovalAuthority().getName())) {
            property.getPropertyDetail().setLayoutPermitNo(null);
            property.getPropertyDetail().setLayoutPermitDate(null);
        }
        property.setBasicProperty(basicProp);
        propService.updateReferenceBasicProperty(basicProp, getParentIndex());
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
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("approve: Property approval started for Property: " + property + " BasicProperty: "
                    + basicProp);
        // For exempted property on approve setting status as 'A'
        if (property.getIsExemptedFromTax())
            property.setStatus(STATUS_ISACTIVE);
        else
            property.setStatus(STATUS_DEMAND_INACTIVE);
        final String assessmentNo = propertyTaxNumberGenerator.generateAssessmentNumber();
        basicProp.setUpicNo(assessmentNo);
        basicProp.setAssessmentdate(new Date());
        final PropertyStatus propStatus = (PropertyStatus) getPersistenceService().find(
                "from PropertyStatus where statusCode=?", PROPERTY_STATUS_APPROVED);
        basicProp.setStatus(propStatus);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
                "from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?", PROP_CREATE_RSN,
                basicProp.getPropertyMutationMaster().getId());
        if (!propertyMutationMaster.getCode().equals(PROP_CREATE_RSN_BIFUR))
            if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAction))
                basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, PROP_CREATE_RSN, null, null,
                        null, null, getParentIndex()));
        approved = true;
        setWardId(basicProp.getPropertyID().getWard().getId());
        basicPropertyService.applyAuditing(property.getState());
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        basicPropertyService.update(basicProp);
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        approverName = "";
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(securityUtils.getCurrentUser()
                .getId());
        if (null != userAssignment)
            propertyInitiatedBy = userAssignment.getEmployee().getName().concat("~").concat(
                    userAssignment.getPosition().getName());
        setAckMessage("Property Created Successfully in the System and Forwarded to : ");
        setAssessmentNoMessage(" for Digital Signature with assessment number : ");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("approve: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
            LOGGER.debug("approve: Property approval ended");
        }
        return RESULT_ACK;
    }

    @SkipValidation
    @Action(value = "/createProperty-reject")
    public String reject() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("reject: Property rejection started");
        basicPropertyService.applyAuditing(property.getState());
        if (property.getStatus().equals(PropertyTaxConstants.STATUS_CANCELLED))
            basicProp.setUnderWorkflow(false);
        else
            basicProp.setUnderWorkflow(true);
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        basicPropertyService.persist(basicProp);
        approverName = "";
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        Assignment assignment;
        if (property.getBasicProperty().getSource().equals(PropertyTaxConstants.SOURCEOFDATA_ONLINE) ||
                property.getBasicProperty().getSource().equals(PropertyTaxConstants.SOURCEOFDATA_MOBILE))
            propertyInitiatedBy = propertyTaxUtil.getApproverUserName(property.getStateHistory().get(0)
                    .getOwnerPosition().getId());
        else
            setPropertyInitiatedBy(getInitiator());

        if (property.getState().getValue().equals("Closed")) {
            assignment = assignmentService.getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId());
            propertyInitiatedBy = assignment.getEmployee().getName().concat("~")
                    .concat(assignment.getPosition().getName());
            setPropertyInitiatedBy(getInitiator());
            setAckMessage(MSG_REJECT_SUCCESS + " By ");
        } else
            setAckMessage(MSG_REJECT_SUCCESS + " and forwarded to : ");
        setApplicationNoMessage(" with application No :");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
            LOGGER.debug("reject: Property rejection ended");
        }
        return RESULT_ACK;
    }

    private void setFloorDetails(final Property property) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into setFloorDetails, Property: " + property);
        final List<Floor> floorList = property.getPropertyDetail().getFloorDetails();
        property.getPropertyDetail().setFloorDetailsProxy(floorList);
        int i = 0;
        for (final Floor flr : floorList) {
            floorNoStr[i] = FLOOR_MAP.get(flr.getFloorNo());
            LOGGER.debug("setFloorDetails: floorNoStr[" + i + "]->" + floorNoStr[i]);
            i++;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from setFloorDetails");
    }

    public List<Floor> getFloorDetails() {
        return new ArrayList<>(property.getPropertyDetail().getFloorDetails());
    }

    @Override
    @SuppressWarnings("unchecked")
    @SkipValidation
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare, ModelId: " + getModelId() + ", PropTypeId: " + propTypeId
                    + ", ZoneId: " + zoneId + ", WardId: " + wardId);
        setUserInfo();
        setUserDesignations();
        propertyByEmployee = propService.isEmployee(securityUtils.getCurrentUser());
        if (isNotBlank(getModelId())) {
            property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            if (StringUtils.isNotBlank(modifyRsn))
                property = (PropertyImpl) persistenceService.merge(property);
            basicProp = property.getBasicProperty();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("prepare: Property by ModelId: " + property + "BasicProperty on property: " + basicProp);
        }
        if (null != property && null != property.getId() && null != property.getState()) {
            preparePropertyTaxDetails(property);
            historyMap = propService.populateHistory(property);
        }

        documentTypes = propService.getDocumentTypesForTransactionType(TransactionType.CREATE);
        final List<FloorType> floorTypeList = getPersistenceService().findAllBy("from FloorType order by name");
        final List<RoofType> roofTypeList = getPersistenceService().findAllBy("from RoofType order by name");
        final List<WallType> wallTypeList = getPersistenceService().findAllBy("from WallType order by name");
        final List<WoodType> woodTypeList = getPersistenceService().findAllBy("from WoodType order by name");
        final List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
                "from PropertyTypeMaster where type != 'EWSHS' order by orderNo");
        final List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
        final List<PropertyMutationMaster> mutationList = getPersistenceService().findAllBy(
                "from PropertyMutationMaster pmm where pmm.type=?", PROP_CREATE_RSN);

        if (null != property && property.getMeesevaServiceCode() != null) {
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVASERVICECODEFORNEWPROPERTY))
                getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_CODE);
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVASERVICECODEFORSUBDIVISION))
                getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_BIFURCATION_CODE);
        }
        List<PropertyUsage> usageList = getPersistenceService().findAllBy(
                "from PropertyUsage where isActive = true order by usageName");

        final List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
        final List<String> StructureList = getPersistenceService().findAllBy(
                "from StructureClassification where isActive = true order by typeName ");
        final List<String> apartmentsList = getPersistenceService().findAllBy("from Apartment order by name");
        final List<String> taxExemptionReasonList = getPersistenceService().findAllBy(
                "from TaxExemptionReason where isActive = true order by name");

        final List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        final List<Boundary> zones = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ZONE,
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> electionWardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                WARD, ADMIN_HIERARCHY_TYPE);
        final List<Boundary> enumerationBlockList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(ELECTIONWARD_BNDRY_TYPE,
                        ELECTION_HIERARCHY_TYPE);

        addDropdownData("zones", zones);
        addDropdownData("wards", Collections.emptyList());
        addDropdownData("blocks", Collections.emptyList());
        addDropdownData("streets", Collections.emptyList());
        setDeviationPercentageMap(DEVIATION_PERCENTAGE);
        setGuardianRelationMap(GUARDIAN_RELATION);
        addDropdownData("PropTypeMaster", propTypeList);
        addDropdownData("floorType", floorTypeList);
        addDropdownData("roofType", roofTypeList);
        addDropdownData("wallType", wallTypeList);
        addDropdownData("woodType", woodTypeList);
        addDropdownData("apartments", apartmentsList);

        addDropdownData("OccupancyList", propOccList);
        addDropdownData("StructureList", StructureList);
        addDropdownData("AgeFactorList", ageFacList);
        addDropdownData("MutationList", mutationList);
        addDropdownData("LocationFactorList", Collections.emptyList());
        setFloorNoMap(FLOOR_MAP);
        addDropdownData("localityList", localityList);
        addDropdownData("electionWardList", electionWardList);
        addDropdownData("enumerationBlockList", enumerationBlockList);
        addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId)) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
            else
                setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
        } else
            setPropTypeCategoryMap(Collections.emptyMap());

        // Loading property usages based on property category
        if (StringUtils.isNoneBlank(propertyCategory))
            if (propertyCategory.equals(CATEGORY_MIXED))
                usageList = getPersistenceService().findAllBy("From PropertyUsage order by usageName");
            else if (propertyCategory.equals(CATEGORY_RESIDENTIAL))
                usageList = getPersistenceService().findAllBy(
                        "From PropertyUsage where isResidential = true order by usageName");
            else if (propertyCategory.equals(CATEGORY_NON_RESIDENTIAL))
                usageList = getPersistenceService().findAllBy(
                        "From PropertyUsage where isResidential = false order by usageName");

        addDropdownData("UsageList", usageList);
        // tax exempted properties
        addDropdownData("taxExemptedList", CommonServices.getTaxExemptedList());
        
        // Loading Property Department based on ownership of property
        
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId)) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT))
                setPropertyDepartmentList(propertyDepartmentRepository.getAllStateDepartments());
            else if(propTypeMstr.getCode().startsWith("CENTRAL_GOVT"))
                setPropertyDepartmentList(propertyDepartmentRepository.getAllCentralDepartments());
        }
        setVacantLandPlotAreaList(vacantLandPlotAreaRepository.findAll());
        setLayoutApprovalAuthorityList(layoutApprovalAuthorityRepository.findAll());
        addDropdownData("vacantLandPlotAreaList", vacantLandPlotAreaList);
        addDropdownData("layoutApprovalAuthorityList", layoutApprovalAuthorityList);
        super.prepare();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("prepare: PropTypeList: "
                    + (propTypeList != null ? propTypeList : "NULL")
                    + ", PropOccuList: "
                    + (propOccList != null ? propOccList : "NLL")
                    + ", MutationList: "
                    + (mutationList != null ? mutationList : "NULL")
                    + ", AgeFactList: "
                    + (ageFacList != null ? ageFacList : "NULL")
                    + "UsageList: "
                    + (getDropdownData().get("UsageList") != null ? getDropdownData().get("UsageList") : "List is NULL")
                    + ", TaxExemptedReasonList: "
                    + (getDropdownData().get("taxExemptedList") != null ? getDropdownData().get("taxExemptedList")
                            : "List is NULL"));

        LOGGER.debug("Exiting from prepare");

    }

    private BasicProperty createBasicProp(final Character status) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into createBasicProp, Property: " + property + ", status: " + status + ", wardId: "
                    + wardId);

        final BasicProperty basicProperty = new BasicPropertyImpl();
        final PropertyStatus propStatus = (PropertyStatus) getPersistenceService().find(
                "from PropertyStatus where statusCode=?", PROPERTY_STATUS_WORKFLOW);
        basicProperty.setRegdDocDate(property.getBasicProperty().getRegdDocDate());
        basicProperty.setRegdDocNo(property.getBasicProperty().getRegdDocNo());
        basicProperty.setActive(Boolean.TRUE);
        basicProperty.setAddress(createPropAddress());
        basicProperty.setPropertyID(createPropertyID(basicProperty));
        basicProperty.setStatus(propStatus);
        basicProperty.setUnderWorkflow(true);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
                "from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?", PROP_CREATE_RSN, mutationId);
        basicProperty.setPropertyMutationMaster(propertyMutationMaster);
        if (propertyMutationMaster.getCode().equals(PROP_CREATE_RSN_BIFUR))
            basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, PROP_CREATE_RSN, null, null,
                    null, null, getParentIndex()));
        basicProperty.setBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
        basicPropertyService.createOwners(property, basicProperty, ownerAddress);
        property.setBasicProperty(basicProperty);
        property.setPropertyModifyReason(PROP_CREATE_RSN);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BasicProperty: " + basicProperty + "\nExiting from createBasicProp");
        return basicProperty;
    }

    private void addDemandAndCompleteDate(final Character status, final BasicProperty basicProperty,
            final PropertyMutationMaster propertyMutationMaster) throws TaxCalculatorExeption {
        taxExemptionId = taxExemptionId == null || taxExemptionId.isEmpty() ? "-1" : taxExemptionId;
        property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
                propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(),
                getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId());
        property.setStatus(status);
        LOGGER.debug("createBasicProp: Property after call to PropertyService.createProperty: " + property);
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
        basicProperty.setPropOccupationDate(propCompletionDate);
        if (propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            property.setPropertyDetail(changePropertyDetail());
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
     * @see org.egov.ptis.domain.entity.property.VacantProperty
     */

    private VacantProperty changePropertyDetail() {

        LOGGER.debug("Entered into changePropertyDetail, Property is Vacant land");

        final PropertyDetail propertyDetail = property.getPropertyDetail();
        final VacantProperty vacantProperty = new VacantProperty(propertyDetail.getSitalArea(),
                propertyDetail.getTotalBuiltupArea(), propertyDetail.getCommBuiltUpArea(),
                propertyDetail.getPlinthArea(), propertyDetail.getCommVacantLand(), propertyDetail.getNonResPlotArea(),
                false, propertyDetail.getSurveyNumber(), propertyDetail.getFieldVerified(),
                propertyDetail.getFieldVerificationDate(), propertyDetail.getFloorDetails(),
                propertyDetail.getPropertyDetailsID(), propertyDetail.getWater_Meter_Num(),
                propertyDetail.getElec_Meter_Num(), 0, propertyDetail.getFieldIrregular(),
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
                propertyDetail.getSiteOwner(), propertyDetail.getPattaNumber(),
                propertyDetail.getCurrentCapitalValue(), propertyDetail.getMarketValue(),
                propertyDetail.getCategoryType(), propertyDetail.getOccupancyCertificationNo(),
                propertyDetail.isAppurtenantLandChecked(), propertyDetail.isCorrAddressDiff(),
                propertyDetail.getPropertyDepartment(), propertyDetail.getVacantLandPlotArea(), propertyDetail.getLayoutApprovalAuthority(),
                propertyDetail.getLayoutPermitNo(), propertyDetail.getLayoutPermitDate());

        vacantProperty.setManualAlv(propertyDetail.getManualAlv());
        vacantProperty.setOccupierName(propertyDetail.getOccupierName());

        LOGGER.debug("Exiting from changePropertyDetail");
        return vacantProperty;
    }

    private void updatePropAddress(final BasicProperty basicProperty) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into createPropAddress, \nAreaId: " + getBlockId() + ", House Number: "
                    + getHouseNumber() + ", OldHouseNo: " + ", AddressStr: " + getAddressStr() + ", PinCode: "
                    + getPinCode());

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
        if (!(property.getPropertyDetail().isCorrAddressDiff() != null && property.getPropertyDetail()
                .isCorrAddressDiff())) {
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
            ownerAddress.setHouseNoBldgApt(getHouseNumber());
            ownerAddress.setStreetRoadLine(getCorrAddress2());
            ownerAddress.setCityTownVillage(cityName);
            ownerAddress.setPinCode(getCorrPinCode());
        }
    }

    private PropertyAddress createPropAddress() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into createPropAddress, \nAreaId: " + getBlockId() + ", House Number: "
                    + getHouseNumber() + ", OldHouseNo: " + ", AddressStr: " + getAddressStr() + ", PinCode: "
                    + getPinCode());

        final Address propAddr = new PropertyAddress();
        propAddr.setHouseNoBldgApt(getHouseNumber());
        propAddr.setAreaLocalitySector(boundaryService.getBoundaryById(getLocality()).getName());
        final String cityName = ApplicationThreadLocals.getCityName();
        propAddr.setStreetRoadLine(boundaryService.getBoundaryById(getWardId()).getName());
        propAddr.setCityTownVillage(cityName);

        if (getPinCode() != null && !getPinCode().isEmpty())
            propAddr.setPinCode(getPinCode());
        if (!(property.getPropertyDetail().isCorrAddressDiff() != null && property.getPropertyDetail()
                .isCorrAddressDiff())) {
            ownerAddress = new CorrespondenceAddress();
            ownerAddress.setAreaLocalitySector(propAddr.getAreaLocalitySector());
            ownerAddress.setHouseNoBldgApt(propAddr.getHouseNoBldgApt());
            ownerAddress.setStreetRoadLine(propAddr.getStreetRoadLine());
            ownerAddress.setCityTownVillage(cityName);
            ownerAddress.setPinCode(propAddr.getPinCode());
        } else {
            ownerAddress = new CorrespondenceAddress();
            ownerAddress.setHouseNoBldgApt(getHouseNumber());
            ownerAddress.setAreaLocalitySector(getCorrAddress1());
            ownerAddress.setStreetRoadLine(getCorrAddress2());
            ownerAddress.setCityTownVillage(cityName);
            ownerAddress.setPinCode(getCorrPinCode());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("PropertyAddress: " + propAddr + "\nExiting from createPropAddress");
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
        LOGGER.debug("PropertyID: " + propertyId + "\nExiting from createPropertyID");
        return propertyId;
    }

    @Override
    public void validate() {

        if (null != property && property.getMeesevaServiceCode() != null
                && !property.getMeesevaServiceCode().equals("")) {
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVASERVICECODEFORNEWPROPERTY))
                getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_CODE);
            if (property.getMeesevaServiceCode().equalsIgnoreCase(MEESEVASERVICECODEFORSUBDIVISION))
                getMutationListByCode(PROP_CREATE_RSN_NEWPROPERTY_BIFURCATION_CODE);
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validate\nZoneId: " + zoneId + ", WardId: " + wardId + ", AreadId: " + blockId
                    + ", HouseNumber: " + houseNumber + ", PinCode: " + pinCode + ", MutationId: " + mutationId + "DepartmentId: " + propertyDepartmentId);

        if (locality == null || locality == -1)
            addActionError(getText("mandatory.localityId"));

        if (null != propTypeId && !propTypeId.equals("-1"))
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));
        if (zoneId == null || zoneId == -1)
            addActionError(getText("mandatory.zone"));
        if (wardId == null || wardId == -1)
            addActionError(getText("mandatory.ward"));
        if (blockId == null || blockId == -1)
            addActionError(getText("mandatory.block"));
        else if (null != propTypeMstr && !propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            if (!StringUtils.isBlank(houseNumber))
                validateHouseNumber(wardId, houseNumber, basicProp);
            else if (property != null
                    && property.getState() != null
                    && property.getState().getNextAction() != null
                    && property.getState().getNextAction()
                            .equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING))
                addActionError(getText("mandatory.doorNo"));

        // if (!property.getPropertyDetail().isStructure()) {
        if (null == property.getBasicProperty().getRegdDocDate())
            addActionError(getText("mandatory.regdocdate"));
        if (StringUtils.isBlank(property.getBasicProperty().getRegdDocNo()))
            addActionError(getText("mandatory.regdocno"));

        if (electionWardId == null || electionWardId == -1)
            addActionError(getText("mandatory.election.ward"));
        for (final PropertyOwnerInfo owner : property.getBasicProperty().getPropertyOwnerInfoProxy())
            if (owner != null) {
                if (StringUtils.isBlank(owner.getOwner().getName()))
                    addActionError(getText("mandatory.ownerName"));
                if (null == owner.getOwner().getGender())
                    addActionError(getText("mandatory.gender"));
                if (StringUtils.isBlank(owner.getOwner().getMobileNumber()))
                    addActionError(getText("mandatory.mobilenumber"));
                if (StringUtils.isBlank(owner.getOwner().getGuardianRelation()))
                    addActionError(getText("mandatory.guardianrelation"));
                if (StringUtils.isBlank(owner.getOwner().getGuardian()))
                    addActionError(getText("mandatory.guardian"));
            }

        final int count = property.getBasicProperty().getPropertyOwnerInfoProxy().size();
        for (int i = 0; i < count; i++) {
            final PropertyOwnerInfo owner = property.getBasicProperty().getPropertyOwnerInfoProxy().get(i);
            if (owner != null)
                for (int j = i + 1; j <= count - 1; j++) {
                    final PropertyOwnerInfo owner1 = property.getBasicProperty().getPropertyOwnerInfoProxy().get(j);
                    if (owner1 != null)
                        if (owner.getOwner().getMobileNumber().equalsIgnoreCase(owner1.getOwner().getMobileNumber())
                                && owner.getOwner().getName().equalsIgnoreCase(owner1.getOwner().getName()))
                            addActionError(getText("error.owner.duplicateMobileNo", "", owner.getOwner()
                                    .getMobileNumber().concat(",").concat(owner.getOwner().getName())));
                }
        }

        validateProperty(property, areaOfPlot, dateOfCompletion, eastBoundary, westBoundary, southBoundary,
                northBoundary, propTypeId, null != zoneId && zoneId != -1 ? String.valueOf(zoneId) : "", propOccId,
                floorTypeId, roofTypeId, wallTypeId, woodTypeId, null, null, vacantLandPlotAreaId, layoutApprovalAuthorityId);

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
                    final BasicProperty basicProperty = basicPropertyService.find(
                            "From BasicPropertyImpl where upicNo = ? ", parentIndex);
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

        if (loggedUserIsMeesevaUser || !propertyByEmployee) {
            final PropertyID propertyid = new PropertyID();
            propertyid.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
            property.getBasicProperty().setPropertyID(propertyid);
            if (null != getElectionWardId() && getElectionWardId() != -1 && null != property.getBasicProperty()
                    && null == propService.getUserPositionByZone(property.getBasicProperty(), false))
                addActionError(getText("notexists.position"));
        }
        validateApproverDetails();
        super.validate();
        if (isBlank(getModelId()) && (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN) ||
                StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT) ||
                StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT)))
            showTaxCalcBtn = Boolean.TRUE;
        if(propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT) && (propertyDepartmentId == null) ){
            addActionError(getText("mandatory.property.department"));
        }
    }

    @SkipValidation
    @Action(value = "/createProperty-printAck")
    public String printAck() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String cityLogo = url.concat(PropertyTaxConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        reportId = reportViewerUtil.addReportToTempCache(
                basicPropertyService.propertyAcknowledgement(property, cityLogo, cityName));
        return PRINTACK;
    }

    @SkipValidation
    @Action(value = "/createProperty-dataEntry")
    public String dataEntry() {
        return RESULT_DATAENTRY;
    }

    @SkipValidation
    @Action(value = "/createProperty-updateDataEntry")
    public String updateDataEntry() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "update data entry: Property updation started, Property: " + property + ", UpicNo: " + basicProp.getUpicNo()
                            + ", zoneId: " + zoneId + ", wardId: " + wardId + ", blockId: " + blockId + ", areaOfPlot: "
                            + areaOfPlot
                            + ", dateOfCompletion: " + dateOfCompletion + ", taxExemptedReason: " + ", propTypeId: "
                            + propTypeId + ", propUsageId: " + propUsageId + ", propOccId: " + propOccId);
        validate();
        if (hasErrors()) {
            upicNo = indexNumber;
            return EDIT_DATA_ENTRY;
        }
        basicProp.setRegdDocDate(property.getBasicProperty().getRegdDocDate());
        basicProp.setRegdDocNo(property.getBasicProperty().getRegdDocNo());
        basicProp.setActive(Boolean.TRUE);
        basicProp.setSource(PropertyTaxConstants.SOURCEOFDATA_DATAENTRY);
        final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
                "from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?", PROP_CREATE_RSN, mutationId);
        basicProp.setPropertyMutationMaster(propertyMutationMaster);
        basicProp.setBoundary(boundaryService.getBoundaryById(getElectionWardId()));

        updatePropAddress(basicProp);
        updatePropertyId(basicProp);

        basicPropertyService.updateOwners(property, basicProp, ownerAddress);
        property.setBasicProperty(basicProp);
        propService.updatePropertyDetail(property, floorTypeId, roofTypeId, wallTypeId, woodTypeId, areaOfPlot,
                propertyCategory, nonResPlotArea, propUsageId, propOccId, propTypeId);

        if (StringUtils.isNotBlank(taxExemptionId) && !taxExemptionId.equals("-1")) {
            final TaxExemptionReason taxExemptionReason = (TaxExemptionReason) persistenceService.find(
                    "From TaxExemptionReason where id = ?", Long.valueOf(taxExemptionId));
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
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create: Property creation started, Property: " + property + ", zoneId: " + zoneId
                    + ", wardId: " + wardId + ", blockId: " + blockId + ", areaOfPlot: " + areaOfPlot
                    + ", dateOfCompletion: " + dateOfCompletion + ", taxExemptedReason: " + ", propTypeId: "
                    + propTypeId + ", propUsageId: " + propUsageId + ", propOccId: " + propOccId);
        if (upicNo == null || upicNo.equals("")) {
            addActionError(getText("mandatory.indexNumber"));
            return "dataEntry";
        }

        propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));

        if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND) && StringUtils.isBlank(houseNumber)) {
            addActionError(getText("mandatory.doorNo"));
            return "dataEntry";
        }
        final long startTimeMillis = System.currentTimeMillis();
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
            addActionError(getText("unitrate.error"));
            LOGGER.error("save : There are no Unit rates defined for chosen combinations", e);
            return RESULT_NEW;
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create: BasicProperty after creatation: " + basicProperty);
        basicProperty.setAssessmentdate(propCompletionDate);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        // basicPropertyService.applyAuditing(property);
        basicPropertyService.persist(basicProperty);
        // TODO update index by assesment no
        // propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        setBasicProp(basicProperty);
        setAckMessage("Property data entry saved in the system successfully and created with Assessment No "
                + basicProperty.getUpicNo());
        // setApplicationNoMessage(" with application number : ");
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("create: Property created successfully in system" + "; Time taken(ms) = " + elapsedTimeMillis);
            LOGGER.debug("create: Property creation ended");
        }
        return "dataEntry-ack";
    }

    @SkipValidation
    @Action(value = "/createProperty-calculateTax")
    public void calculateTax() {
        LOGGER.debug("entering calculateTax()");
        validate();
        if (hasErrors())
            try {
                ServletActionContext.getResponse().getWriter().write(getText("enter.mandatory.fields"));
            } catch (final IOException e) {
                LOGGER.error("calculateTax() : User has not entered all the mandatory fields", e);
            }
        else {
            HashMap<Installment, TaxCalculationInfo> instTaxMap = new HashMap<Installment, TaxCalculationInfo>();
            final PropertyMutationMaster propertyMutationMaster = (PropertyMutationMaster) getPersistenceService().find(
                    "from PropertyMutationMaster pmm where pmm.type=? AND pmm.id=?", PROP_CREATE_RSN, mutationId);
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
                    getPropertyDepartmentId(), getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId());
            if (!propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetails());
            else
                propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
            if (basicProp == null) {
                basicProperty.setPropOccupationDate(propCompletionDate);
                property.setBasicProperty(basicProperty);
            } else {
                basicProp.setPropOccupationDate(propCompletionDate);
                property.setBasicProperty(basicProp);
            }
            if (propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
                property.setPropertyDetail(propService.changePropertyDetail(property, property.getPropertyDetail(), 0)
                        .getPropertyDetail());
            try {
                instTaxMap = taxCalculator.calculatePropertyTax(property, propCompletionDate);
            } catch (final TaxCalculatorExeption e) {
                LOGGER.error("calculateTax() : There are no Unit rates defined for chosen combinations", e);
            }
            final String resultString = propertyTaxCommonUtils.getCurrentHalfyearTax(instTaxMap, propTypeMstr);
            final String jsonsString = new GsonBuilder().create().toJson(resultString);
            ServletActionContext.getResponse().setContentType("application/json");
            try {
                ServletActionContext.getResponse().getWriter().write(jsonsString);
            } catch (final IOException e) {
                LOGGER.error("calculateTax() : Error while writing response", e);
            }
        }
        LOGGER.debug("exiting calculateTax()");
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

    public TreeMap<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(final TreeMap<Integer, String> floorNoMap) {
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

    /**
     * This implementation transitions the <code>PropertyImpl</code> to the next workflow state.
     */
    /*
     * @Override protected PropertyImpl property() { return property; }
     */

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

    public Map<String, String> getGuardianRelationMap() {
        return guardianRelationMap;
    }

    public void setGuardianRelationMap(final Map<String, String> guardianRelationMap) {
        this.guardianRelationMap = guardianRelationMap;
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
        return getProperty().getId() != null && !(getProperty().getCurrentState().getValue().endsWith(STATUS_REJECTED) ||
                getProperty().getCurrentState().getValue().endsWith(WFLOW_ACTION_NEW))
                        ? propService.getDesignationForPositionAndUser(getProperty().getCurrentState().getOwnerPosition().getId(),
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

    public void setPropertyDepartmentId(Long propertyDepartmentId) {
        this.propertyDepartmentId = propertyDepartmentId;
    }

    public List<PropertyDepartment> getPropertyDepartmentList() {
        return propertyDepartmentList;
    }

    public void setPropertyDepartmentList(List<PropertyDepartment> propertyDepartmentList) {
        this.propertyDepartmentList = propertyDepartmentList;
    }

    public List<VacantLandPlotArea> getVacantLandPlotAreaList() {
        return vacantLandPlotAreaList;
    }

    public void setVacantLandPlotAreaList(List<VacantLandPlotArea> vacantLandPlotAreaList) {
        this.vacantLandPlotAreaList = vacantLandPlotAreaList;
    }

    public List<LayoutApprovalAuthority> getLayoutApprovalAuthorityList() {
        return layoutApprovalAuthorityList;
    }

    public void setLayoutApprovalAuthorityList(List<LayoutApprovalAuthority> layoutApprovalAuthorityList) {
        this.layoutApprovalAuthorityList = layoutApprovalAuthorityList;
    }

    public Long getVacantLandPlotAreaId() {
        return vacantLandPlotAreaId;
    }

    public void setVacantLandPlotAreaId(Long vacantLandPlotAreaId) {
        this.vacantLandPlotAreaId = vacantLandPlotAreaId;
    }

    public Long getLayoutApprovalAuthorityId() {
        return layoutApprovalAuthorityId;
    }

    public void setLayoutApprovalAuthorityId(Long layoutApprovalAuthorityId) {
        this.layoutApprovalAuthorityId = layoutApprovalAuthorityId;
    }
}