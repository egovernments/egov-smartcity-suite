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
package org.egov.ptis.actions.citizen.create;

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
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN_BIFUR;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_NOTCREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_DEMAND_INACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.persistence.entity.CorrespondenceAddress;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDepartment;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;
import org.egov.ptis.domain.repository.PropertyDepartmentRepository;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Namespace("/citizen/create")
@Validations
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = CreateAction.NEW, location = "citizen/create/create-new.jsp"),
        @Result(name = CreateAction.RESULT_ACK, location = "citizen/create/create-ack.jsp"),
        @Result(name = CreateAction.PRINTACK, location = "citizen/create/create-printAck.jsp") })
public class CreateAction extends PropertyTaxBaseAction {

    private static final long serialVersionUID = -2329719786287615452L;
    public static final String RESULT_ACK = "ack";
    public static final String PRINTACK = "printAck";
    private static final String PROPTYPEMASTER_QUERY = "from PropertyTypeMaster ptm where ptm.id = ?";
    private final Logger LOGGER = Logger.getLogger(getClass());
    private PropertyImpl property = new PropertyImpl();

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
    protected String ackMessage;
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
    private String northBoundary;
    private String southBoundary;
    private String eastBoundary;
    private String westBoundary;
    private String applicationSource;
    private Long mutationId;
    private Map<String, String> propTypeCategoryMap;
    private TreeMap<Integer, String> floorNoMap;
    private Map<String, String> deviationPercentageMap;
    private Map<String, String> guardianRelationMap;
    private List<DocumentType> documentTypes = new ArrayList<>();
    private String reportId;
    private boolean approved;
    private BasicProperty basicProp;
    private PropertyTypeMaster propTypeMstr;
    private PropertyImpl newProperty = new PropertyImpl();
    private Address ownerAddress = new CorrespondenceAddress();
    Date propCompletionDate = null;
    private Long propertyDepartmentId;
    private Long vacantLandPlotAreaId;
    private Long layoutApprovalAuthorityId;
    private List<PropertyDepartment> propertyDepartmentList = new ArrayList<>();
    private List<VacantLandPlotArea> vacantLandPlotAreaList = new ArrayList<>();
    private List<LayoutApprovalAuthority> layoutApprovalAuthorityList = new ArrayList<>();

    @Autowired
    private PropertyPersistenceService basicPropertyService;
    @Autowired
    private PropertyService propService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private ReportViewerUtil reportViewerUtil;
    @Autowired
    private transient PropertyDepartmentRepository propertyDepartmentRepository;
    @Autowired
    private transient VacantLandPlotAreaRepository vacantLandPlotAreaRepository;

    @Autowired
    private transient LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepository;

    public CreateAction() {
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
    }

    @Override
    public StateAware getModel() {
        return property;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SkipValidation
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare, ModelId: " + getModelId() + ", PropTypeId: " + propTypeId
                    + ", ZoneId: " + zoneId + ", WardId: " + wardId);
        ApplicationThreadLocals.setUserId(2L);
        setUserInfo();
        setUserDesignations();
        propertyByEmployee = false;
        if (isNotBlank(getModelId())) {
            property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));

            basicProp = property.getBasicProperty();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("prepare: Property by ModelId: " + property + "BasicProperty on property: " + basicProp);
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
        addDropdownData("wards", Collections.EMPTY_LIST);
        addDropdownData("blocks", Collections.EMPTY_LIST);
        addDropdownData("streets", Collections.EMPTY_LIST);
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
        addDropdownData("LocationFactorList", Collections.EMPTY_LIST);
        setFloorNoMap(FLOOR_MAP);
        addDropdownData("localityList", localityList);
        addDropdownData("electionWardList", electionWardList);
        addDropdownData("enumerationBlockList", enumerationBlockList);
        addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1")) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
            else
                setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
        } else
            setPropTypeCategoryMap(Collections.EMPTY_MAP);

        // Loading property usages based on property category
        if (StringUtils.isNoneBlank(propertyCategory)) {
            if (propertyCategory.equals(CATEGORY_MIXED))
                usageList = getPersistenceService().findAllBy("From PropertyUsage order by usageName");
            else if (propertyCategory.equals(CATEGORY_RESIDENTIAL))
                usageList = getPersistenceService().findAllBy(
                        "From PropertyUsage where isResidential = true order by usageName");
            else if (propertyCategory.equals(CATEGORY_NON_RESIDENTIAL))
                usageList = getPersistenceService().findAllBy(
                        "From PropertyUsage where isResidential = false order by usageName");
        }

        addDropdownData("UsageList", usageList);
        // tax exempted properties
        addDropdownData("taxExemptedList", CommonServices.getTaxExemptedList());

        // Loading Property Department based on ownership of property

        if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId)) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                    Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT))
                setPropertyDepartmentList(propertyDepartmentRepository.getAllStateDepartments());
            else if (propTypeMstr.getCode().startsWith("CENTRAL_GOVT"))
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

    @Override
    public void validate() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into validate\nZoneId: " + zoneId + ", WardId: " + wardId + ", AreadId: " + blockId
                    + ", HouseNumber: " + houseNumber + ", PinCode: " + pinCode + ", MutationId: " + mutationId);

        if (locality == null || locality == -1)
            addActionError(getText("mandatory.localityId"));

        if (null != propTypeId && !propTypeId.equals("-1")) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    PROPTYPEMASTER_QUERY, Long.valueOf(propTypeId));
        }
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
        if (null == property.getBasicProperty().getRegdDocDate()) {
            addActionError(getText("mandatory.regdocdate"));
        }
        if (StringUtils.isBlank(property.getBasicProperty().getRegdDocNo())) {
            addActionError(getText("mandatory.regdocno"));
        }
        // }

        if (electionWardId == null || electionWardId == -1) {
            addActionError(getText("mandatory.election.ward"));
        }
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

        int count = property.getBasicProperty().getPropertyOwnerInfoProxy().size();
        for (int i = 0; i < count; i++) {
            PropertyOwnerInfo owner = property.getBasicProperty().getPropertyOwnerInfoProxy().get(i);
            if (owner != null) {
                for (int j = i + 1; j <= count - 1; j++) {
                    PropertyOwnerInfo owner1 = property.getBasicProperty().getPropertyOwnerInfoProxy().get(j);
                    if (owner1 != null) {
                        if (owner.getOwner().getMobileNumber().equalsIgnoreCase(owner1.getOwner().getMobileNumber())
                                && owner.getOwner().getName().equalsIgnoreCase(owner1.getOwner().getName())) {
                            addActionError(getText("error.owner.duplicateMobileNo", "", owner.getOwner()
                                    .getMobileNumber().concat(",").concat(owner.getOwner().getName())));
                        }
                    }
                }
            }
        }

        validateProperty(property, areaOfPlot, dateOfCompletion, eastBoundary, westBoundary, southBoundary,
                northBoundary, propTypeId, (null != zoneId && zoneId != -1) ? String.valueOf(zoneId) : "", propOccId,
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
                        if (null != basicProperty && !basicProperty.isUnderWorkflow()) {
                            if (null != basicProperty.getActiveProperty()) {
                                property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
                                final String errorKey = propService.validationForBifurcation(property, basicProperty,
                                        propertyMutationMaster.getCode());
                                if (!isBlank(errorKey))
                                    addActionError(getText(errorKey));
                            } else
                                addActionError(getText("error.parent"));
                        } else
                            addActionError(getText("error.parent.underworkflow"));
                    }
                } else
                    addActionError(getText("error.parent.index"));
        } else
            addActionError(getText("mandatory.createRsn"));

        PropertyID propertyid = new PropertyID();
        propertyid.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        property.getBasicProperty().setPropertyID(propertyid);
        if (null != getElectionWardId() && getElectionWardId() != -1 && null != property.getBasicProperty()
                && null == propService.getUserPositionByZone(property.getBasicProperty(), false)) {
            addActionError(getText("notexists.position"));
        }
        super.validate();
    }

    @SkipValidation
    @Action(value = "/create-newForm")
    public String newForm() {
        if (StringUtils.isBlank(applicationSource) || !applicationSource.equalsIgnoreCase("online")) {
            addActionError(getText("citizen.unAuthorized.user.error"));
        }
        return NEW;
    }

    @Action(value = "/create-create")
    public String create() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create: Property creation started, Property: " + property + ", zoneId: " + zoneId
                    + ", wardId: " + wardId + ", blockId: " + blockId + ", areaOfPlot: " + areaOfPlot
                    + ", dateOfCompletion: " + dateOfCompletion + ", propTypeId: " + propTypeId + ", propUsageId: "
                    + propUsageId + ", propOccId: " + propOccId);
        if (StringUtils.isBlank(applicationSource) || !applicationSource.equalsIgnoreCase("online")) {
            throw new ValidationException(new ValidationError("authenticationError",
                    getText("citizen.unAuthorized.user.error")));
            // addActionError(getText("citizen.unAuthorized.user.error"));
        }

        final long startTimeMillis = System.currentTimeMillis();

        final BasicProperty basicProperty = createBasicProp(STATUS_DEMAND_INACTIVE);
        try {
            addDemandAndCompleteDate(STATUS_DEMAND_INACTIVE, basicProperty, basicProperty.getPropertyMutationMaster());
        } catch (TaxCalculatorExeption e) {
            basicProperty.setPropertyOwnerInfoProxy(basicProperty.getPropertyOwnerInfo());
            addActionError(getText("unitrate.error"));
            LOGGER.error("create : There are no Unit rates defined for chosen combinations", e);
            return NEW;
        }
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("create: BasicProperty after creation: " + basicProperty);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        // this should be appending to messgae
        transitionWorkFlow(property);
        basicPropertyService.applyAuditing(property.getState());
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        basicPropertyService.persist(basicProperty);
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        setBasicProp(basicProperty);
        setAckMessage("Property Data Saved Successfully in the System and forwarded to the official ");
        setApplicationNoMessage(" with application number : ");

        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("create: Property created successfully in system" + "; Time taken(ms) = " + elapsedTimeMillis);
            LOGGER.debug("create: Property creation ended");
        }
        return RESULT_ACK;
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
        basicProperty.setBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
        basicProperty.setSource(PropertyTaxConstants.SOURCEOFDATA_ONLINE);
        basicPropertyService.createOwners(property, basicProperty, ownerAddress);
        property.setBasicProperty(basicProperty);
        property.setPropertyModifyReason(PROP_CREATE_RSN);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BasicProperty: " + basicProperty + "\nExiting from createBasicProp");
        return basicProperty;
    }

    private void addDemandAndCompleteDate(final Character status, final BasicProperty basicProperty,
            final PropertyMutationMaster propertyMutationMaster) throws TaxCalculatorExeption {
        taxExemptionId = (taxExemptionId == null || taxExemptionId.isEmpty()) ? "-1" : taxExemptionId;
        property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
                propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(), vacantLandPlotAreaId,
                layoutApprovalAuthorityId);
        property.setStatus(status);

        LOGGER.debug("createBasicProp: Property after call to PropertyService.createProperty: " + property);
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(property.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = property.getPropertyDetail().getDateOfCompletion();
        basicProperty.setPropOccupationDate(propCompletionDate);
        basicProperty.addProperty(property);
        try {
            if (property != null && !property.getDocuments().isEmpty())
                propService.processAndStoreDocument(property.getDocuments());

            propService.createDemand(property, propCompletionDate);
        } catch (TaxCalculatorExeption e) {
            throw new TaxCalculatorExeption();
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
        String cityName = ApplicationThreadLocals.getCityName();
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

    @SkipValidation
    @Action(value = "/create-printAck")
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

    public PropertyImpl getProperty() {
        return property;
    }

    public void setProperty(PropertyImpl property) {
        this.property = property;
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

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    public Long getLocality() {
        return locality;
    }

    public void setLocality(Long locality) {
        this.locality = locality;
    }

    public Long getFloorTypeId() {
        return floorTypeId;
    }

    public void setFloorTypeId(Long floorTypeId) {
        this.floorTypeId = floorTypeId;
    }

    public Long getRoofTypeId() {
        return roofTypeId;
    }

    public void setRoofTypeId(Long roofTypeId) {
        this.roofTypeId = roofTypeId;
    }

    public Long getWallTypeId() {
        return wallTypeId;
    }

    public void setWallTypeId(Long wallTypeId) {
        this.wallTypeId = wallTypeId;
    }

    public Long getWoodTypeId() {
        return woodTypeId;
    }

    public void setWoodTypeId(Long woodTypeId) {
        this.woodTypeId = woodTypeId;
    }

    public Long getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(Long ownershipType) {
        this.ownershipType = ownershipType;
    }

    public Long getElectionWardId() {
        return electionWardId;
    }

    public void setElectionWardId(Long electionWardId) {
        this.electionWardId = electionWardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAreaOfPlot() {
        return areaOfPlot;
    }

    public void setAreaOfPlot(String areaOfPlot) {
        this.areaOfPlot = areaOfPlot;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getCorrAddress1() {
        return corrAddress1;
    }

    public void setCorrAddress1(String corrAddress1) {
        this.corrAddress1 = corrAddress1;
    }

    public String getCorrAddress2() {
        return corrAddress2;
    }

    public void setCorrAddress2(String corrAddress2) {
        this.corrAddress2 = corrAddress2;
    }

    public String getCorrPinCode() {
        return corrPinCode;
    }

    public void setCorrPinCode(String corrPinCode) {
        this.corrPinCode = corrPinCode;
    }

    public String getUpicNo() {
        return upicNo;
    }

    public void setUpicNo(String upicNo) {
        this.upicNo = upicNo;
    }

    public String getTaxExemptionId() {
        return taxExemptionId;
    }

    public void setTaxExemptionId(String taxExemptionId) {
        this.taxExemptionId = taxExemptionId;
    }

    public String getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(String parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String[] getFloorNoStr() {
        return floorNoStr;
    }

    public void setFloorNoStr(String[] floorNoStr) {
        this.floorNoStr = floorNoStr;
    }

    public String getPropTypeId() {
        return propTypeId;
    }

    public void setPropTypeId(String propTypeId) {
        this.propTypeId = propTypeId;
    }

    public String getPropUsageId() {
        return propUsageId;
    }

    public void setPropUsageId(String propUsageId) {
        this.propUsageId = propUsageId;
    }

    public String getPropOccId() {
        return propOccId;
    }

    public void setPropOccId(String propOccId) {
        this.propOccId = propOccId;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getNonResPlotArea() {
        return nonResPlotArea;
    }

    public void setNonResPlotArea(String nonResPlotArea) {
        this.nonResPlotArea = nonResPlotArea;
    }

    public String getApplicationNoMessage() {
        return applicationNoMessage;
    }

    public void setApplicationNoMessage(String applicationNoMessage) {
        this.applicationNoMessage = applicationNoMessage;
    }

    public String getAssessmentNoMessage() {
        return assessmentNoMessage;
    }

    public void setAssessmentNoMessage(String assessmentNoMessage) {
        this.assessmentNoMessage = assessmentNoMessage;
    }

    public String getPropertyInitiatedBy() {
        return propertyInitiatedBy;
    }

    public void setPropertyInitiatedBy(String propertyInitiatedBy) {
        this.propertyInitiatedBy = propertyInitiatedBy;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(String northBoundary) {
        this.northBoundary = northBoundary;
    }

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(String southBoundary) {
        this.southBoundary = southBoundary;
    }

    public String getEastBoundary() {
        return eastBoundary;
    }

    public void setEastBoundary(String eastBoundary) {
        this.eastBoundary = eastBoundary;
    }

    public String getWestBoundary() {
        return westBoundary;
    }

    public void setWestBoundary(String westBoundary) {
        this.westBoundary = westBoundary;
    }

    public Long getMutationId() {
        return mutationId;
    }

    public void setMutationId(Long mutationId) {
        this.mutationId = mutationId;
    }

    public Map<String, String> getPropTypeCategoryMap() {
        return propTypeCategoryMap;
    }

    public void setPropTypeCategoryMap(Map<String, String> propTypeCategoryMap) {
        this.propTypeCategoryMap = propTypeCategoryMap;
    }

    public TreeMap<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(TreeMap<Integer, String> floorNoMap) {
        this.floorNoMap = floorNoMap;
    }

    public Map<String, String> getDeviationPercentageMap() {
        return deviationPercentageMap;
    }

    public void setDeviationPercentageMap(Map<String, String> deviationPercentageMap) {
        this.deviationPercentageMap = deviationPercentageMap;
    }

    public Map<String, String> getGuardianRelationMap() {
        return guardianRelationMap;
    }

    public void setGuardianRelationMap(Map<String, String> guardianRelationMap) {
        this.guardianRelationMap = guardianRelationMap;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public BasicProperty getBasicProp() {
        return basicProp;
    }

    public void setBasicProp(BasicProperty basicProp) {
        this.basicProp = basicProp;
    }

    public PropertyTypeMaster getPropTypeMstr() {
        return propTypeMstr;
    }

    public void setPropTypeMstr(PropertyTypeMaster propTypeMstr) {
        this.propTypeMstr = propTypeMstr;
    }

    public PropertyImpl getNewProperty() {
        return newProperty;
    }

    public void setNewProperty(PropertyImpl newProperty) {
        this.newProperty = newProperty;
    }

    public Address getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(Address ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public Date getPropCompletionDate() {
        return propCompletionDate;
    }

    public void setPropCompletionDate(Date propCompletionDate) {
        this.propCompletionDate = propCompletionDate;
    }

    public void setPropService(PropertyService propService) {
        this.propService = propService;
    }

    public PropertyService getPropService() {
        return propService;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public String getApplicationSource() {
        return applicationSource;
    }

    public void setApplicationSource(String applicationSource) {
        this.applicationSource = applicationSource;
    }

    public Long getPropertyDepartmentId() {
        return propertyDepartmentId;
    }

    public void setPropertyDepartmentId(Long propertyDepartmentId) {
        this.propertyDepartmentId = propertyDepartmentId;
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

}
