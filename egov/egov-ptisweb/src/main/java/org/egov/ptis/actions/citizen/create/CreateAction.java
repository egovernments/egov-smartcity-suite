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
import static org.egov.ptis.constants.PropertyTaxConstants.APPURTENANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_VACANT_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.DEVIATION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTIONWARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.GUARDIAN_RELATION;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.NEW_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN_BIFUR;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_NOTCREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_DEMAND_INACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

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
import org.egov.ptis.domain.entity.document.DocumentTypeDetails;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
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
        @Result(name = CreateAction.PRINT_ACK, location = "citizen/create/create-printAck.jsp") })
public class CreateAction extends PropertyTaxBaseAction {

    private static final String USAGE_LIST = "UsageList";
    private static final String TAX_EXEMPTED_LIST = "taxExemptedList";
    private static final long serialVersionUID = -2329719786287615452L;
    private static final String ONLINE = "online";
    public static final String RESULT_ACK = "ack";
    public static final String PRINT_ACK = "printAck";
    private static final String PROPTYPEMASTER_QUERY = "from PropertyTypeMaster ptm where ptm.id = ?";
    private final Logger logger = Logger.getLogger(getClass());
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
    private SortedMap<Integer, String> floorNoMap;
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
    private List<DocumentType> assessmentDocumentTypes = new ArrayList<>();
    private List<String> assessmentDocumentNames;
    private transient DocumentTypeDetails documentTypeDetails = new DocumentTypeDetails();
    private boolean floorDetailsEntered;

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
        if (logger.isDebugEnabled())
            logger.debug("Entered into prepare, ModelId: " + getModelId() + ", PropTypeId: " + propTypeId
                    + ", ZoneId: " + zoneId + ", WardId: " + wardId);
        ApplicationThreadLocals.setUserId(2L);
        setUserInfo();
        setUserDesignations();
        propertyByEmployee = false;
        if (isNotBlank(getModelId())) {
            property = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));

            basicProp = property.getBasicProperty();
            if (logger.isDebugEnabled())
                logger.debug("prepare: Property by ModelId: " + property + "BasicProperty on property: " + basicProp);
        }

        documentTypes = propService.getDocumentTypesForTransactionType(TransactionType.CREATE);
        assessmentDocumentTypes = propService.getDocumentTypesForTransactionType(TransactionType.CREATE_ASMT_DOC);
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
        final List<String> structureList = getPersistenceService().findAllBy(
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

        addDropdownData(USAGE_LIST, usageList);
        // tax exempted properties
        addDropdownData(TAX_EXEMPTED_LIST, CommonServices.getTaxExemptedList());

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
        setAssessmentDocumentNames(PropertyTaxConstants.ASSESSMENT_DOCUMENT_NAMES);
        addDropdownData("assessmentDocumentNameList", assessmentDocumentNames);
        super.prepare();
        if (logger.isDebugEnabled())
            logger.debug("prepare: PropTypeList: "
                    + (propTypeList != null ? propTypeList : "NULL")
                    + ", PropOccuList: "
                    + (propOccList != null ? propOccList : "NLL")
                    + ", MutationList: "
                    + (mutationList != null ? mutationList : "NULL")
                    + ", AgeFactList: "
                    + (ageFacList != null ? ageFacList : "NULL")
                    + "UsageList: "
                    + (getDropdownData().get(USAGE_LIST) != null ? getDropdownData().get(USAGE_LIST) : "List is NULL")
                    + ", TaxExemptedReasonList: "
                    + (getDropdownData().get(TAX_EXEMPTED_LIST) != null ? getDropdownData().get(TAX_EXEMPTED_LIST)
                            : "List is NULL"));

        logger.debug("Exiting from prepare");
    }

    @Override
    public void validate() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into validate\nZoneId: " + zoneId + ", WardId: " + wardId + ", AreadId: " + blockId
                    + ", HouseNumber: " + houseNumber + ", PinCode: " + pinCode + ", MutationId: " + mutationId);

        if (locality == null || locality == -1)
            addActionError(getText("mandatory.localityId"));

        if (null != propTypeId && !"-1".equals(propTypeId))
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
                floorTypeId, roofTypeId, wallTypeId, woodTypeId, null, null, vacantLandPlotAreaId, layoutApprovalAuthorityId,
                null);

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

        final PropertyID propertyid = new PropertyID();
        propertyid.setElectionBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        property.getBasicProperty().setPropertyID(propertyid);
        if (null != getElectionWardId() && getElectionWardId() != -1 && null != property.getBasicProperty()
                && null == propService.getUserPositionByZone(property.getBasicProperty(), false))
            addActionError(getText("notexists.position"));
        if (!(StringUtils.isBlank(applicationSource) || applicationSource.equalsIgnoreCase(ONLINE)))
            validateDocumentDetails(getDocumentTypeDetails());
        super.validate();
    }

    @SkipValidation
    @Action(value = "/create-newForm")
    public String newForm() {
        if (StringUtils.isBlank(applicationSource) || !applicationSource.equalsIgnoreCase(ONLINE))
            addActionError(getText("citizen.unAuthorized.user.error"));
        return NEW;
    }

    @Action(value = "/create-create")
    public String create() {
        if (logger.isDebugEnabled())
            logger.debug("create: Property creation started, Property: " + property + ", zoneId: " + zoneId
                    + ", wardId: " + wardId + ", blockId: " + blockId + ", areaOfPlot: " + areaOfPlot
                    + ", dateOfCompletion: " + dateOfCompletion + ", propTypeId: " + propTypeId + ", propUsageId: "
                    + propUsageId + ", propOccId: " + propOccId);
        if (StringUtils.isBlank(applicationSource) || !applicationSource.equalsIgnoreCase(ONLINE))
            throw new ValidationException(new ValidationError("authenticationError",
                    getText("citizen.unAuthorized.user.error")));

        final long startTimeMillis = System.currentTimeMillis();

        if (property.getPropertyDetail().isAppurtenantLandChecked()) {
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(PROPTYPEMASTER_QUERY,
                    Long.valueOf(propTypeId));
            if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
                property.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
            else
                property.getPropertyDetail().setPropertyType(PropertyTaxConstants.BUILT_UP_PROPERTY);
            return createAppurTenantProperties(STATUS_DEMAND_INACTIVE);
        }

        final BasicProperty basicProperty = createBasicProp(STATUS_DEMAND_INACTIVE);
        try {
            addDemandAndCompleteDate(STATUS_DEMAND_INACTIVE, basicProperty, basicProperty.getPropertyMutationMaster());
        } catch (final TaxCalculatorExeption e) {
            basicProperty.setPropertyOwnerInfoProxy(basicProperty.getPropertyOwnerInfo());
            addActionError(getText("unitrate.error"));
            logger.error("create : There are no Unit rates defined for chosen combinations", e);
            return NEW;
        }
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        if (logger.isDebugEnabled())
            logger.debug("create: BasicProperty after creation: " + basicProperty);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        // this should be appending to messgae
        transitionWorkFlow(property);
        basicPropertyService.applyAuditing(property.getState());
        propService.updateIndexes(property, APPLICATION_TYPE_NEW_ASSESSENT);
        propService.processAndStoreDocument(property.getAssessmentDocuments());
        basicPropertyService.persist(basicProperty);
        buildEmailandSms(property, APPLICATION_TYPE_NEW_ASSESSENT);
        setBasicProp(basicProperty);
        propService.saveDocumentTypeDetails(basicProperty, getDocumentTypeDetails());
        setAckMessage("Property Data Saved Successfully in the System and forwarded to the official ");
        setApplicationNoMessage(" with application number : ");

        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (logger.isDebugEnabled()) {
            logger.info("create: Property created successfully in system" + "; Time taken(ms) = " + elapsedTimeMillis);
            logger.debug("create: Property creation ended");
        }
        return RESULT_ACK;
    }

    @Override
    public String getAdditionalRule() {
        return NEW_ASSESSMENT;
    }

    private BasicProperty createBasicProp(final Character status) {
        if (logger.isDebugEnabled())
            logger.debug("Entered into createBasicProp, Property: " + property + ", status: " + status + ", wardId: "
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
        if (propertyMutationMaster.getCode().equals(PROP_CREATE_RSN_BIFUR)
                || property.getPropertyDetail().isAppurtenantLandChecked())
            basicProperty.addPropertyStatusValues(propService.createPropStatVal(basicProperty, PROP_CREATE_RSN, null,
                    null, null, null, getParentIndex()));
        basicProperty.setBoundary(boundaryService.getBoundaryById(getElectionWardId()));
        basicProperty.setIsBillCreated(STATUS_BILL_NOTCREATED);
        basicProperty.setSource(PropertyTaxConstants.SOURCEOFDATA_ONLINE);
        if (!property.getPropertyDetail().isAppurtenantLandChecked()) {
            basicPropertyService.createOwners(property, basicProperty, ownerAddress);
            property.setBasicProperty(basicProperty);
        } else
            basicPropertyService.createOwnersForAppurTenant(property, basicProperty, ownerAddress);
        property.setPropertyModifyReason(PROP_CREATE_RSN);
        if (logger.isDebugEnabled())
            logger.debug("BasicProperty: " + basicProperty + "\nExiting from createBasicProp");
        return basicProperty;
    }

    private void addDemandAndCompleteDate(final Character status, final BasicProperty basicProperty,
            final PropertyMutationMaster propertyMutationMaster) throws TaxCalculatorExeption {
        taxExemptionId = taxExemptionId == null || taxExemptionId.isEmpty() ? "-1" : taxExemptionId;
        property = propService.createProperty(property, getAreaOfPlot(), propertyMutationMaster.getCode(), propTypeId,
                propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(), vacantLandPlotAreaId,
                layoutApprovalAuthorityId, Boolean.FALSE);
        property.setStatus(status);

        logger.debug("createBasicProp: Property after call to PropertyService.createProperty: " + property);
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
        } catch (final TaxCalculatorExeption e) {
            throw new TaxCalculatorExeption();
        }
    }

    private PropertyAddress createPropAddress() {

        if (logger.isDebugEnabled())
            logger.debug("Entered into createPropAddress, \nAreaId: " + getBlockId() + ", House Number: "
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
        if (logger.isDebugEnabled())
            logger.debug("PropertyAddress: " + propAddr + "\nExiting from createPropAddress");
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
        logger.debug("PropertyID: " + propertyId + "\nExiting from createPropertyID");
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
        return PRINT_ACK;
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
            addActionError(getText("unitrate.error"));
            logger.error("create : There are no Unit rates defined for chosen combinations", e);
            return NEW;
        }
        setBasicProp(nonVacantBasicProperty);
        setAckMessage("Property Created Successfully in the System and Forwarded to : ");
        setAssessmentNoMessage(" for Digital Signature with assessment number : ");
        return RESULT_ACK;
    }

    private PropertyImpl createNonVacantProperty(final Character status, final BasicProperty nonVacantBasicProperty)
            throws TaxCalculatorExeption {
        final PropertyImpl nonVacantProperty = createAppurTenantProperty(status, nonVacantBasicProperty, Boolean.TRUE);
        persistAndMessage(nonVacantBasicProperty, nonVacantProperty);
        return nonVacantProperty;
    }

    private void createVacantProperty(final Character status, final PropertyImpl nonVacantProperty,
            final BasicProperty vacantBasicProperty)
            throws TaxCalculatorExeption {
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
        final PropertyStatusValues propStatusVal = basicProperty.getPropertyStatusValuesSet()
                .iterator().next();
        propStatusVal.setRemarks(APPURTENANT_PROPERTY);
        return propStatusVal;
    }

    private String getCategoryByNonVacantPropertyType(final PropertyImpl nonVacantProperty) {
        final String propertyType = nonVacantProperty.getPropertyDetail().getPropertyTypeMaster().getCode();
        return OWNERSHIP_TYPE_PRIVATE.equals(propertyType)
                || OWNERSHIP_TYPE_VAC_LAND.equals(propertyType) ? CATEGORY_VACANT_LAND
                        : OWNERSHIP_TYPE_STATE_GOVT.equals(propertyType) ? CATEGORY_STATE_GOVT
                                : CATEGORY_CENTRAL_GOVT;
    }

    private void persistAndMessage(final BasicProperty basicProperty, final PropertyImpl property) {
        basicPropertyService.persist(basicProperty);
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
                basicProperty.getPropertyMutationMaster().getCode(),
                propTypeId,
                propUsageId, propOccId, status, getDocNumber(), getNonResPlotArea(), getFloorTypeId(), getRoofTypeId(),
                getWallTypeId(), getWoodTypeId(), Long.valueOf(taxExemptionId), getPropertyDepartmentId(),
                getVacantLandPlotAreaId(), getLayoutApprovalAuthorityId(), nonVacant);
        clonedProp.setStatus(status);
        if (!clonedProp.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(clonedProp.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = clonedProp.getPropertyDetail().getDateOfCompletion();
        basicProperty.setPropOccupationDate(propCompletionDate);
        if (propTypeMstr != null && propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            clonedProp.setPropertyDetail(changePropertyDetail(clonedProp));
        basicProperty.addProperty(clonedProp);
        if (basicProperty.getSource() == PropertyTaxConstants.SOURCEOFDATA_APPLICATION) {
            if (!clonedProp.getDocuments().isEmpty())
                propService.processAndStoreDocument(clonedProp.getDocuments());

            propService.createDemand(clonedProp, propCompletionDate);
        }
        basicProperty.setUnderWorkflow(Boolean.TRUE);
        basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
        transitionWorkFlow(clonedProp);
        basicPropertyService.applyAuditing(clonedProp.getState());
        propService.updateIndexes(clonedProp, APPLICATION_TYPE_NEW_ASSESSENT);
        propService.processAndStoreDocument(clonedProp.getAssessmentDocuments());
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

    /**
     * Changes the property details from {@link BuiltUpProperty} to {@link VacantProperty}
     *
     * @return vacant property details
     * @see org.egov.ptis.domain.entity.property.VacantProperty
     */

    private VacantProperty changePropertyDetail(final PropertyImpl property) {

        logger.debug("Entered into changePropertyDetail, Property is Vacant land");

        final PropertyDetail propertyDetail = property.getPropertyDetail();
        if (getDocumentTypeDetails().getDocumentName() != null
                && getDocumentTypeDetails().getDocumentName().equals(PropertyTaxConstants.DOCUMENT_NAME_NOTARY_DOCUMENT))
            propertyDetail.setStructure(true);
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
                propertyDetail.getSiteOwner(), propertyDetail.getPattaNumber(), propertyDetail.getCurrentCapitalValue(),
                propertyDetail.getMarketValue(), propertyDetail.getCategoryType(),
                propertyDetail.getOccupancyCertificationNo(), propertyDetail.isAppurtenantLandChecked(),
                propertyDetail.isCorrAddressDiff(), propertyDetail.getPropertyDepartment(),
                propertyDetail.getVacantLandPlotArea(), propertyDetail.getLayoutApprovalAuthority(),
                propertyDetail.getLayoutPermitNo(), propertyDetail.getLayoutPermitDate());

        vacantProperty.setManualAlv(propertyDetail.getManualAlv());
        vacantProperty.setOccupierName(propertyDetail.getOccupierName());

        logger.debug("Exiting from changePropertyDetail");
        return vacantProperty;
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

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(final Long streetId) {
        this.streetId = streetId;
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

    public Long getElectionWardId() {
        return electionWardId;
    }

    public void setElectionWardId(final Long electionWardId) {
        this.electionWardId = electionWardId;
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

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(final String applicationNo) {
        this.applicationNo = applicationNo;
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

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(final String propertyCategory) {
        this.propertyCategory = propertyCategory;
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

    public Long getMutationId() {
        return mutationId;
    }

    public void setMutationId(final Long mutationId) {
        this.mutationId = mutationId;
    }

    public Map<String, String> getPropTypeCategoryMap() {
        return propTypeCategoryMap;
    }

    public void setPropTypeCategoryMap(final Map<String, String> propTypeCategoryMap) {
        this.propTypeCategoryMap = propTypeCategoryMap;
    }

    public SortedMap<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(final SortedMap<Integer, String> floorNoMap) {
        this.floorNoMap = floorNoMap;
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

    public String getReportId() {
        return reportId;
    }

    public void setReportId(final String reportId) {
        this.reportId = reportId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(final boolean approved) {
        this.approved = approved;
    }

    public BasicProperty getBasicProp() {
        return basicProp;
    }

    public void setBasicProp(final BasicProperty basicProp) {
        this.basicProp = basicProp;
    }

    public PropertyTypeMaster getPropTypeMstr() {
        return propTypeMstr;
    }

    public void setPropTypeMstr(final PropertyTypeMaster propTypeMstr) {
        this.propTypeMstr = propTypeMstr;
    }

    public PropertyImpl getNewProperty() {
        return newProperty;
    }

    public void setNewProperty(final PropertyImpl newProperty) {
        this.newProperty = newProperty;
    }

    public Address getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(final Address ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public Date getPropCompletionDate() {
        return propCompletionDate;
    }

    public void setPropCompletionDate(final Date propCompletionDate) {
        this.propCompletionDate = propCompletionDate;
    }

    public void setPropService(final PropertyService propService) {
        this.propService = propService;
    }

    public PropertyService getPropService() {
        return propService;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(final String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public String getApplicationSource() {
        return applicationSource;
    }

    public void setApplicationSource(final String applicationSource) {
        this.applicationSource = applicationSource;
    }

    public Long getPropertyDepartmentId() {
        return propertyDepartmentId;
    }

    public void setPropertyDepartmentId(final Long propertyDepartmentId) {
        this.propertyDepartmentId = propertyDepartmentId;
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

}
