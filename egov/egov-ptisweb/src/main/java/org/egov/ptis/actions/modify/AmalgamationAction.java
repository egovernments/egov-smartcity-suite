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
package org.egov.ptis.actions.modify;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.AMALGAMATION;
import static org.egov.ptis.constants.PropertyTaxConstants.AMALGAMATION_OF_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_CLIENT_SPECIFIC_DMD_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_AMALGAMATION;
import static org.egov.ptis.constants.PropertyTaxConstants.BILL_COLLECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.GUARDIAN_RELATION;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.NOT_AVAILABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_WORKFLOW_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_DATAENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Area;
import org.egov.eis.entity.Assignment;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.portal.entity.Citizen;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.Amalgamation;
import org.egov.ptis.domain.entity.property.AmalgamationOwner;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.master.service.ApartmentService;
import org.egov.ptis.master.service.FloorTypeService;
import org.egov.ptis.master.service.PropertyOccupationService;
import org.egov.ptis.master.service.PropertyUsageService;
import org.egov.ptis.master.service.RoofTypeService;
import org.egov.ptis.master.service.StructureClassificationService;
import org.egov.ptis.master.service.WallTypeService;
import org.egov.ptis.master.service.WoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@ResultPath(value = "/WEB-INF/jsp")
@Namespace("/amalgamation")
@Results({ @Result(name = AmalgamationAction.NEW, location = "amalgamation/amalgamation-new.jsp"),
        @Result(name = AmalgamationAction.RESULT_ACK, location = "amalgamation/amalgamation-ack.jsp"),
        @Result(name = AmalgamationAction.VIEW, location = "amalgamation/amalgamation-view.jsp") })
public class AmalgamationAction extends PropertyTaxBaseAction {

    private static final long serialVersionUID = 1L;
    private final transient Logger LOGGER = Logger.getLogger(getClass());
    protected static final String COMMON_FORM = "commonForm";
    protected static final String RESULT_ACK = "ack";
    private static final String PROPERTY_MODIFY_REJECT_SUCCESS = "property.modify.reject.success";
    private static final String PROPERTY_MODIFY_FINAL_REJECT_SUCCESS = "property.modify.final.reject.success";
    private static final String PROPERTY_MODIFY_APPROVE_SUCCESS = "property.modify.approve.success";
    private static final String PROPERTY_FORWARD_SUCCESS = "property.forward.success";

    private BasicProperty basicProp;
    private PropertyImpl oldProperty = new PropertyImpl();
    private PropertyImpl propertyModel = new PropertyImpl();
    private PropertyImpl propWF;// would be current property workflow obj
    private String mode;
    private String areaOfPlot;
    private String propAddress;
    private String doorNo;
    private String pinCode;
    private Long floorTypeId;
    private Long roofTypeId;
    private Long wallTypeId;
    private Long woodTypeId;
    private String northBoundary;
    private String southBoundary;
    private String eastBoundary;
    private String westBoundary;
    private String propertyCategory;
    private String docNumber;
    private String oldPropertyTypeCode;
    private String modifyRsn;
    private PropertyTypeMaster propTypeMstr;
    private Boolean wfInitiatorRejected;
    private String[] floorNoStr = new String[100];
    private List<DocumentType> documentTypes = new ArrayList<>();
    private Map<Integer, String> floorNoMap;
    private Map<String, String> guardianRelationMap;
    private transient PropertyService propService;
    private Map<String, String> propTypeCategoryMap;
    private boolean allowEditDocument = Boolean.FALSE;

    @Autowired
    private transient PropertyPersistenceService basicPropertyService;
    @Autowired
    private transient SecurityUtils securityUtils;
    @Autowired
    private transient PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    private transient UserService userService;
    @Autowired
    private transient BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private transient PropertyMutationMasterDAO propertyMutationMasterDAO;
    private transient PersistenceService<Property, Long> propertyImplService;
    @Autowired
    private transient FloorTypeService floorTypeService;
    @Autowired
    private transient RoofTypeService roofTypeService;
    @Autowired
    private transient WallTypeService wallTypeService;
    @Autowired
    private transient WoodTypeService woodTypeService;
    @Autowired
    private transient ApartmentService apartmentService;
    @Autowired
    private transient PropertyUsageService propertyUsageService;
    @Autowired
    private transient StructureClassificationService structureClassificationService;
    @Autowired
    private transient PropertyOccupationService propertyOccupationService;

    public AmalgamationAction() {
        super();
        propertyModel.setPropertyDetail(new BuiltUpProperty());
        this.addRelatedEntity("propertyDetail.propertyTypeMaster", PropertyTypeMaster.class);
        this.addRelatedEntity("propertyDetail.apartment", Apartment.class);
    }

    @SkipValidation
    @Override
    public StateAware getModel() {
        return propertyModel;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        LOGGER.debug("Entered into preapre, ModelId: " + getModelId());
        super.prepare();
        setUserInfo();
        setUserDesignations();
        propertyByEmployee = propService.isEmployee(securityUtils.getCurrentUser());
        if (getModelId() != null && !getModelId().isEmpty()) {
            setBasicProp(basicPropertyDAO.getBasicPropertyByProperty(Long.valueOf(getModelId())));
            LOGGER.debug("prepare: BasicProperty: " + basicProp);
            propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            if (propWF != null) {
                setProperty(propWF);
                historyMap = propService.populateHistory(propWF);
            } else {
                historyMap = propService.populateHistory(basicProp.getActiveProperty());
            }
        } else if (indexNumber != null && !indexNumber.trim().isEmpty()) {
            setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
                    indexNumber));
            preparePropertyTaxDetails(basicProp.getActiveProperty());
        }

        documentTypes = propService.getDocumentTypesForTransactionType(TransactionType.OBJECTION);
        List<PropertyUsage> usageList = propertyUsageService.getAllActivePropertyUsages();

        setFloorNoMap(FLOOR_MAP);
        setGuardianRelationMap(GUARDIAN_RELATION);
        addDropdownData("floorType", floorTypeService.getAllFloors());
        addDropdownData("roofType", roofTypeService.getAllRoofTypes());
        addDropdownData("wallType", wallTypeService.getAllWalls());
        addDropdownData("woodType", woodTypeService.getAllWoodTypes());
        addDropdownData("PropTypeMaster", propertyTypeMasterDAO.findAllExcludeEWSHS());
        addDropdownData("OccupancyList", propertyOccupationService.getAllPropertyOccupations());
        addDropdownData("StructureList", structureClassificationService.getAllActiveStructureTypes());
        addDropdownData("apartments", apartmentService.getAllApartments());
        populatePropertyTypeCategory();

        // Loading property usages based on property category
        if (StringUtils.isNoneBlank(propertyCategory))
            if (propertyCategory.equals(CATEGORY_MIXED))
                usageList = propertyUsageService.getAllActivePropertyUsages();
            else if (propertyCategory.equals(CATEGORY_RESIDENTIAL))
                usageList = propertyUsageService.getResidentialPropertyUsages();
            else if (propertyCategory.equals(CATEGORY_NON_RESIDENTIAL))
                usageList = propertyUsageService.getNonResidentialPropertyUsages();

        addDropdownData("UsageList", usageList);
        LOGGER.debug("Exiting from preapre, ModelId: " + getModelId());
    }

    @SkipValidation
    @Action(value = "/amalgamation-newForm")
    public String newForm() {
        mode = EDIT;
        populateFormData();
        return NEW;
    }

    private void populateFormData() {
        PropertyImpl propertyImpl;
        if (basicProp != null) {
            populateAddress();
            setOldProperty((PropertyImpl) getBasicProp().getProperty());
            if (propWF == null && (propertyModel == null || propertyModel.getId() == null))
                propertyImpl = (PropertyImpl) oldProperty.createPropertyclone();
            else
                propertyImpl = propWF != null ? propWF : propertyModel;
            setProperty(propertyImpl);
            setPropAddress(basicProp.getAddress().toString());
            populatePropertyDetails();
            propertyModel.setBasicProperty(basicProp);
            setOldPropertyTypeCode(oldProperty.getPropertyDetail().getPropertyTypeMaster().getCode());
            setDocNumber(propertyModel.getDocNumber());
            if (!propertyModel.getPropertyDetail().getFloorDetails().isEmpty())
                setFloorDetails(propertyModel);

            setAmalgamationPropsAndOwners();
        }
    }

    private void populateAddress() {
        if (basicProp.getPropertyOwnerInfo() != null
                && !basicProp.getPropertyOwnerInfo().isEmpty()) {
            for (final PropertyOwnerInfo propOwner : basicProp.getPropertyOwnerInfo()) {
                final List<Address> addrSet = propOwner.getOwner().getAddress();
                for (final Address address : addrSet) {
                    propAddress = address.toString();
                    doorNo = address.getHouseNoBldgApt() == null ? NOT_AVAILABLE : address.getHouseNoBldgApt();
                    pinCode = address.getPinCode();
                    break;
                }
            }

        }
    }

    private void populatePropertyDetails() {
        if (propertyModel.getPropertyDetail().getFloorType() != null)
            floorTypeId = propertyModel.getPropertyDetail().getFloorType().getId();
        if (propertyModel.getPropertyDetail().getRoofType() != null)
            roofTypeId = propertyModel.getPropertyDetail().getRoofType().getId();
        if (propertyModel.getPropertyDetail().getWallType() != null)
            wallTypeId = propertyModel.getPropertyDetail().getWallType().getId();
        if (propertyModel.getPropertyDetail().getWoodType() != null)
            woodTypeId = propertyModel.getPropertyDetail().getWoodType().getId();
        if (propertyModel.getPropertyDetail().getSitalArea() != null)
            setAreaOfPlot(propertyModel.getPropertyDetail().getSitalArea().getArea().toString());
        if (basicProp.getPropertyID() != null) {
            final PropertyID propertyID = basicProp.getPropertyID();
            northBoundary = propertyID.getNorthBoundary();
            southBoundary = propertyID.getSouthBoundary();
            eastBoundary = propertyID.getEastBoundary();
            westBoundary = propertyID.getWestBoundary();
        }
    }

    private void setAmalgamationPropsAndOwners() {
        populateAmalgamations();
        populateAmalgamationOwners();
    }

    private void populateAmalgamations() {
        if (propertyModel.getId() != null && !propertyModel.getBasicProperty().getAmalgamations().isEmpty()) {
            BasicProperty amalBasicProp;
            for (Amalgamation amal : propertyModel.getBasicProperty().getAmalgamations()) {
                if (propertyModel.getStatus() == null ||
                        (propertyModel.getStatus() != null
                                && propertyModel.getStatus().equals(STATUS_WORKFLOW)))
                    amalBasicProp = basicPropertyDAO.getBasicPropertyByPropertyID(amal.getAmalgamatedProperty().getUpicNo());
                else
                    amalBasicProp = basicPropertyDAO
                            .getInActiveBasicPropertyByPropertyID(amal.getAmalgamatedProperty().getUpicNo());
                for (final PropertyOwnerInfo propOwner : amalBasicProp.getPropertyOwnerInfo()) {
                    final List<Address> addrSet = propOwner.getOwner().getAddress();
                    for (final Address address : addrSet) {
                        amal.setAssessmentNo(amal.getAmalgamatedProperty().getUpicNo());
                        amal.setOwnerName(propOwner.getOwner().getName());
                        amal.setMobileNo(propOwner.getOwner().getMobileNumber());
                        amal.setPropertyAddress(address.toString());
                        break;
                    }
                }
                basicProp.getAmalgamationsProxy().add(amal);
            }

        }
    }

    private void populateAmalgamationOwners() {
        AmalgamationOwner amlgOwner;
        if (propertyModel.getAmalgamationOwners().isEmpty()) {
            for (PropertyOwnerInfo ownerInfo : basicProp.getPropertyOwnerInfo()) {
                amlgOwner = new AmalgamationOwner();
                amlgOwner.setOwner(ownerInfo.getOwner());
                amlgOwner.setProperty(propertyModel);
                propertyModel.getAmalgamationOwnersProxy().add(amlgOwner);
            }
        } else {
            for (AmalgamationOwner ownerInfo : propertyModel.getAmalgamationOwners()) {
                amlgOwner = new AmalgamationOwner();
                amlgOwner.setOwner(ownerInfo.getOwner());
                amlgOwner.setProperty(propertyModel);
                propertyModel.getAmalgamationOwnersProxy().add(amlgOwner);
            }
        }
    }

    private void setFloorDetails(final Property property) {
        LOGGER.debug("Entered into setFloorDetails, Property: " + property);
        final List<Floor> floors = property.getPropertyDetail().getFloorDetails();
        property.getPropertyDetail().setFloorDetailsProxy(floors);
        int i = 0;
        for (final Floor flr : floors) {
            if (getModelId() == null)
                flr.setOccupancyDate(new Date());
            floorNoStr[i] = FLOOR_MAP.get(flr.getFloorNo());
            i++;
        }
        LOGGER.debug("Exiting from setFloorDetails: ");
    }

    /**
     * Modifies and Forwards the assessment to next user when form is submitted in editable mode
     * @return
     */
    @SkipValidation
    @Action(value = "/amalgamation-forwardModify")
    public String forwardModify() {
        LOGGER.debug("forwardModify: Modify property started " + propertyModel);
        setOldPropertyTypeCode(basicProp.getProperty().getPropertyDetail().getPropertyTypeMaster().getCode());
        validate();
        long startTimeMillis = System.currentTimeMillis();
        if (getModelId() != null && !getModelId().trim().isEmpty()) {
            propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));

            LOGGER.debug("forwardModify: Workflow property: " + propWF);
            basicProp = propWF.getBasicProperty();
            setBasicProp(basicProp);
        } else
            populateBasicProp();
        oldProperty = (PropertyImpl) basicProp.getProperty();
        setSitalArea();
        validateVacantLandConversion();
        if (hasErrors())
            if (checkDesignationsForEdit()) {
                allowEditDocument = Boolean.TRUE;
                return NEW;
            } else if (checkDesignationsForView())
                return VIEW;
        try {
            modifyBasicProp();
        } catch (final TaxCalculatorExeption e) {
            addActionError(getText("unitrate.error"));
            LOGGER.error("forwardModify : There are no Unit rates defined for chosen combinations", e);
            return NEW;
        }
        transitionWorkFlow(propertyModel);
        basicProp.setUnderWorkflow(Boolean.TRUE);
        setAmalgamationsForPersist();
        applyAuditingAndUpdateIndex();
        prepareAckMsg();
        addActionMessage(getText(PROPERTY_FORWARD_SUCCESS,
                new String[] { propertyModel.getBasicProperty().getUpicNo() }));
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("forwardModify: Amalgamation forwarded successfully; Time taken(ms) = " + elapsedTimeMillis);
        LOGGER.debug("forwardModify: Amalgamation forward ended");
        return RESULT_ACK;
    }

    private void validateVacantLandConversion() {
        PropertyTypeMaster oldPropTypeMstr = oldProperty.getPropertyDetail().getPropertyTypeMaster();
        if (propTypeMstr != null && !propTypeMstr.getType().equals(oldPropTypeMstr.getType())
                && propTypeMstr.getType().equals(OWNERSHIP_TYPE_VAC_LAND_STR))
            addActionError(getText("error.nonVacantToVacant"));
    }

    private void applyAuditingAndUpdateIndex() {
        basicPropertyService.applyAuditing(propertyModel.getState());
        propService.updateIndexes(propertyModel, getApplicationType());
        if (basicProp.getWFProperty() != null && basicProp.getWFProperty().getPtDemandSet() != null
                && !basicProp.getWFProperty().getPtDemandSet().isEmpty())
            for (final Ptdemand ptDemand : basicProp.getWFProperty().getPtDemandSet())
                basicPropertyService.applyAuditing(ptDemand.getDmdCalculations());
        basicPropertyService.update(basicProp);
    }

    private boolean checkDesignationsForView() {
        return StringUtils.containsIgnoreCase(userDesignationList, BILL_COLLECTOR_DESGN)
                || StringUtils.containsIgnoreCase(userDesignationList, COMMISSIONER_DESGN)
                || StringUtils.containsIgnoreCase(userDesignationList, REVENUE_OFFICER_DESGN);
    }

    private boolean checkDesignationsForEdit() {
        return StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT)
                || StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT)
                || getModel().getState().getNextAction() != null && getModel().getState().getNextAction()
                        .equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING);
    }

    private void setSitalArea() {
        if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
            final Area area = new Area();
            area.setArea(new Float(areaOfPlot));
            propertyModel.getPropertyDetail().setSitalArea(area);
        }
        propertyModel.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
    }

    private void setAmalgamationsForPersist() {
        Amalgamation amalgamatedProp;
        BasicProperty amalBasicProp;
        basicProp.getAmalgamations().clear();
        for (Amalgamation amlg : basicProp.getAmalgamationsProxy()) {
            amalgamatedProp = new Amalgamation();
            amalBasicProp = basicPropertyDAO.getBasicPropertyByPropertyID(amlg.getAssessmentNo());
            amalgamatedProp.setParentProperty(basicProp);
            amalgamatedProp.setAmalgamatedProperty(amalBasicProp);
            amalBasicProp.setUnderWorkflow(true);
            basicPropertyService.applyAuditing(amalgamatedProp);
            basicProp.getAmalgamations().add(amalgamatedProp);
        }
    }

    /**
     * Returns modify property view screen when modify property inbox item is opened
     * @return
     */
    @SkipValidation
    @Action(value = "/amalgamation-view")
    public String view() {
        LOGGER.debug("Entered into view, BasicProperty: " + basicProp + ", ModelId: " + getModelId());
        if (getModelId() != null) {
            propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
            LOGGER.debug("view: PropertyModel by model id: " + propertyModel);
        }
        if (checkDesignationsForEdit()) {
            mode = EDIT;
            allowEditDocument = Boolean.TRUE;
        } else if (checkDesignationsForView()) {
            mode = VIEW;
        }
        final String currWfState = propertyModel.getState().getValue();
        populateFormData();

        if (currWfState.endsWith(WF_STATE_COMMISSIONER_APPROVED)) {
            setIsApprPageReq(Boolean.FALSE);
            if (basicProp.getUpicNo() != null && !basicProp.getUpicNo().isEmpty())
                setIndexNumber(basicProp.getUpicNo());
        }

        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        setDocNumber(propertyModel.getDocNumber());
        LOGGER.debug("view: ModifyReason: " + getModifyRsn());
        LOGGER.debug("Exiting from view");
        return VIEW;
    }

    /**
     * Modifies and Forwards the assessment to next user when form is submitted in read only mode
     * @return
     */
    @SkipValidation
    @Action(value = "/amalgamation-forwardView")
    public String forwardView() {
        LOGGER.debug("Entered into forwardView");
        validateApproverDetails();
        if (hasErrors())
            if (checkDesignationsForEdit())
                return NEW;
            else if (checkDesignationsForView())
                return VIEW;
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        LOGGER.debug("forwardView: Workflow property: " + propertyModel);
        transitionWorkFlow(propertyModel);
        propService.updateIndexes(propertyModel, getApplicationType());
        basicPropertyService.update(basicProp);
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        prepareAckMsg();
        addActionMessage(getText(PROPERTY_FORWARD_SUCCESS,
                new String[] { propertyModel.getBasicProperty().getUpicNo() }));
        LOGGER.debug("Exiting from forwardView");
        return RESULT_ACK;
    }

    /**
     * Populates Basic Property based on either the index number or model id
     */
    private void populateBasicProp() {
        if (basicProp == null)
            if (StringUtils.isNotBlank(indexNumber))
                setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
                        indexNumber));
            else if (StringUtils.isNotBlank(getModelId()))
                setBasicProp(((PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                        Long.valueOf(getModelId()))).getBasicProperty());
    }

    /**
     * Validates form data
     */
    @Override
    public void validate() {
        LOGGER.debug("Entered into validate, ModifyRsn: " + modifyRsn);
        propertyModel.setBasicProperty(basicProp);
        Date propCompletionDate = null;

        if (basicProp.getSource() == SOURCEOFDATA_MIGRATION || basicProp.getSource() == SOURCEOFDATA_DATAENTRY) {
            setOldProperty((PropertyImpl) getBasicProp().getProperty());
            propCompletionDate = propertyTaxUtil.getLowestInstallmentForProperty(oldProperty);
        }
        if (isBlank(propertyModel.getPropertyDetail().getCategoryType())
                || "-1".equals(propertyModel.getPropertyDetail().getCategoryType()))
            addActionError(getText("mandatory.propTypeCategory"));

        if (basicProp.getAmalgamationsProxy().isEmpty())
            addActionError(getText("error.amalgamatedprops.required"));

        validateOwners();
        PropertyDetail propertyDetail = propertyModel.getPropertyDetail();
        Date regDocDate = propertyModel.getBasicProperty().getRegdDocDate();
        if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            if (propertyDetail != null)
                validateVacantProperty(propertyDetail, eastBoundary, westBoundary, southBoundary,
                        northBoundary, modifyRsn, propCompletionDate, null, null);
        } else {
            validateBuiltUpProperty(propertyDetail, floorTypeId, roofTypeId, areaOfPlot, regDocDate, modifyRsn);
            validateFloor(propTypeMstr, propertyModel.getPropertyDetail().getFloorDetailsProxy(), propertyModel, areaOfPlot,
                    regDocDate, modifyRsn, propCompletionDate);
        }
        validateApproverDetails();
        LOGGER.debug("Exiting from validate, BasicProperty: " + getBasicProp());
    }

    private void validateOwners() {
        for (final AmalgamationOwner owner : propertyModel.getAmalgamationOwnersProxy())
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

        validateDuplicateMobileNo();
    }

    private void validateDuplicateMobileNo() {
        final int count = propertyModel.getAmalgamationOwnersProxy().size();
        for (int i = 0; i < count; i++) {
            final AmalgamationOwner owner = propertyModel.getAmalgamationOwnersProxy().get(i);
            if (owner != null)
                for (int j = i + 1; j <= count - 1; j++) {
                    final AmalgamationOwner owner1 = propertyModel.getAmalgamationOwnersProxy().get(j);
                    if (owner1 != null)
                        if (owner.getOwner().getMobileNumber().equalsIgnoreCase(owner1.getOwner().getMobileNumber())
                                && owner.getOwner().getName().equalsIgnoreCase(owner1.getOwner().getName()))
                            addActionError(getText("error.owner.duplicateMobileNo", "", owner.getOwner()
                                    .getMobileNumber().concat(",").concat(owner.getOwner().getName())));
                }
        }
    }

    /**
     * Modifies basic property information
     * @param docNumber
     * @throws TaxCalculatorExeption
     */
    private void modifyBasicProp() throws TaxCalculatorExeption {
        Date propCompletionDate;
        Property modProperty = null;
        final Character status = STATUS_WORKFLOW;
        final PropertyTypeMaster proptypeMstr = propertyModel.getPropertyDetail().getPropertyTypeMaster();
        propCompletionDate = getCompletionDate(proptypeMstr);
        PropertyMutationMaster propMutMstr = propertyMutationMasterDAO.getPropertyMutationMasterByCode(modifyRsn);
        basicProp.setPropertyMutationMaster(propMutMstr);
        basicProp.setPropOccupationDate(propCompletionDate);
        createAmalgamationOwners(propertyModel, basicProp, basicProp.getAddress());
        setProperty(propService.createProperty(propertyModel, getAreaOfPlot(), modifyRsn, proptypeMstr.getId().toString(), null,
                null, status, propertyModel.getDocNumber(), null, floorTypeId, roofTypeId, wallTypeId, woodTypeId,
                null, null, null, null));
        updatePropertyID(basicProp);
        propertyModel.setPropertyModifyReason(modifyRsn);
        propertyModel.setBasicProperty(basicProp);
        propertyModel.setEffectiveDate(propCompletionDate);
        changePropertyDetail(proptypeMstr);

        try {
            modProperty = propService.modifyDemand(propertyModel, oldProperty);
        } catch (final TaxCalculatorExeption e) {
            LOGGER.error("modifyBasicProp : Exception occured while modifying demand ", e);
            throw new TaxCalculatorExeption();
        }

        if (modProperty != null && !modProperty.getDocuments().isEmpty())
            propService.processAndStoreDocument(modProperty.getDocuments());

        if (modProperty == null)
            basicProp.addProperty(propertyModel);
        else
            basicProp.addProperty(modProperty);

        LOGGER.debug("Exiting modifyBasicProp");
    }

    private void changePropertyDetail(final PropertyTypeMaster proptypeMstr) {
        final Long oldPropTypeId = oldProperty.getPropertyDetail().getPropertyTypeMaster().getId();
        final PropertyTypeMaster vltPropTypeMstr = propertyTypeMasterDAO.getPropertyTypeMasterByCode(OWNERSHIP_TYPE_VAC_LAND);
        /*
         * if modifying from OPEN_PLOT to OTHERS or from OTHERS to OPEN_PLOT property type
         */
        if ((oldPropTypeId == vltPropTypeMstr.getId() && proptypeMstr.getId() != vltPropTypeMstr.getId()
                || oldPropTypeId != vltPropTypeMstr
                        .getId() && proptypeMstr.getId() == vltPropTypeMstr.getId())
                && !propertyModel.getStatus().equals('W'))
            if (vltPropTypeMstr != null
                    && org.apache.commons.lang.StringUtils.equals(vltPropTypeMstr.getId().toString(),
                            proptypeMstr.getId().toString()))
                propService.changePropertyDetail(propertyModel, new VacantProperty(), 0);
            else
                propService.changePropertyDetail(propertyModel, new BuiltUpProperty(), propertyModel.getPropertyDetail()
                        .getNoofFloors());
    }

    private Date getCompletionDate(final PropertyTypeMaster proptypeMstr) {
        Date propCompletionDate;
        if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                    .getFloorDetailsProxy());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
        return propCompletionDate;
    }

    /**
     * Updates property boundary information
     *
     * @param basicProperty
     */
    private void updatePropertyID(final BasicProperty basicProperty) {
        final PropertyID propertyId = basicProperty.getPropertyID();
        if (propertyId != null) {
            propertyId.setEastBoundary(getEastBoundary());
            propertyId.setWestBoundary(getWestBoundary());
            propertyId.setNorthBoundary(getNorthBoundary());
            propertyId.setSouthBoundary(getSouthBoundary());
        }
    }

    public void createAmalgamationOwners(final Property property, final BasicProperty basicProperty, final Address ownerAddress) {
        LOGGER.debug("createOwners for property: " + property + ", basicProperty: " + basicProperty
                + ", ownerAddress: " + ownerAddress);
        User user;
        property.getAmalgamationOwners().clear();
        for (AmalgamationOwner ownerInfo : property.getAmalgamationOwnersProxy()) {
            if (ownerInfo != null) {
                if (StringUtils.isNotBlank(ownerInfo.getOwner().getAadhaarNumber()))
                    user = userService.getUserByAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                else
                    user = userService.getUserByNameAndMobileNumberForGender(ownerInfo
                            .getOwner().getName(), ownerInfo.getOwner().getMobileNumber(), ownerInfo.getOwner()
                                    .getGender());

                if (user == null) {
                    final Citizen newOwner = new Citizen();
                    newOwner.setAadhaarNumber(ownerInfo.getOwner().getAadhaarNumber());
                    newOwner.setMobileNumber(ownerInfo.getOwner().getMobileNumber());
                    newOwner.setEmailId(ownerInfo.getOwner().getEmailId());
                    newOwner.setGender(ownerInfo.getOwner().getGender());
                    newOwner.setGuardian(ownerInfo.getOwner().getGuardian());
                    newOwner.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
                    newOwner.setName(ownerInfo.getOwner().getName());
                    newOwner.setSalutation(ownerInfo.getOwner().getSalutation());
                    newOwner.setPassword("NOT SET");
                    newOwner.setUsername(propertyTaxUtil.generateUserName(ownerInfo.getOwner().getName()));
                    userService.createUser(newOwner);
                    ownerInfo.setProperty(property);
                    ownerInfo.setOwner(newOwner);
                    LOGGER.debug("createOwners: OwnerAddress: " + ownerAddress);
                    ownerInfo.getOwner().addAddress(basicProperty.getAddress());
                } else {
                    // If existing user, then do not add correspondence address
                    user.setEmailId(ownerInfo.getOwner().getEmailId());
                    user.setGuardian(ownerInfo.getOwner().getGuardian());
                    user.setGuardianRelation(ownerInfo.getOwner().getGuardianRelation());
                    ownerInfo.setOwner(user);
                    ownerInfo.setProperty(property);
                }
            }
            property.addAmalgamationOwners(ownerInfo);
        }
    }

    /**
     * Populates Property type categories based on the property type
     */
    private void populatePropertyTypeCategory() {
        if (propertyModel != null && propertyModel.getPropertyDetail() != null
                && propertyModel.getPropertyDetail().getPropertyTypeMaster() != null
                && !propertyModel.getPropertyDetail().getPropertyTypeMaster().getId().equals(-1))
            propTypeMstr = propertyModel.getPropertyDetail().getPropertyTypeMaster();
        else if (basicProp != null)
            propTypeMstr = basicProp.getProperty().getPropertyDetail().getPropertyTypeMaster();

        if (propTypeMstr != null) {
            if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                setPropTypeCategoryMap(VAC_LAND_PROPERTY_TYPE_CATEGORY);
            else
                setPropTypeCategoryMap(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
        } else
            setPropTypeCategoryMap(Collections.emptyMap());
    }

    /**
     * Approves the assessment when form is approved by the final approver
     * @return
     */
    @SkipValidation
    @Action(value = "/amalgamation-approve")
    public String approve() {
        LOGGER.debug("Enter method approve");
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        LOGGER.debug("approve: Workflow property: " + propertyModel);
        basicProp = propertyModel.getBasicProperty();
        oldProperty = (PropertyImpl) basicProp.getProperty();
        transitionWorkFlow(propertyModel);
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        createPropertyStatusValues();
        propertyModel.setStatus(STATUS_ISACTIVE);
        oldProperty.setStatus(STATUS_ISHISTORY);
        for (PropertyStatusValues statusValues : basicProp.getPropertyStatusValuesSet())
            basicPropertyService.applyAuditing(statusValues);
        String clientSpecificDmdBill = propertyTaxCommonUtils.getAppConfigValue(APPCONFIG_CLIENT_SPECIFIC_DMD_BILL,
                PTMODULENAME);
        if ("Y".equalsIgnoreCase(clientSpecificDmdBill))
            propertyTaxCommonUtils.makeExistingDemandBillInactive(basicProp.getUpicNo());
        else
            propertyTaxUtil.makeTheEgBillAsHistory(basicProp);

        propService.updateIndexes(propertyModel, getApplicationType());

        basicPropertyService.update(basicProp);
        setBasicProp(basicProp);
        setAckMessage(getText(PROPERTY_MODIFY_APPROVE_SUCCESS, new String[] { AMALGAMATION_OF_ASSESSMENT,
                propertyModel.getBasicProperty().getUpicNo() }));
        addActionMessage(getText(PROPERTY_MODIFY_APPROVE_SUCCESS, new String[] { AMALGAMATION_OF_ASSESSMENT,
                propertyModel.getBasicProperty().getUpicNo() }));
        LOGGER.debug("Exiting approve");
        return RESULT_ACK;
    }

    private void createPropertyStatusValues() {
        Date propCompletionDate;
        PropertyTypeMaster proptypeMstr = propertyModel.getPropertyDetail().getPropertyTypeMaster();
        if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                    .getFloorDetails());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();

        String[] amalgPropIds = new String[10];
        int i = 0;
        for (Amalgamation amalProp : basicProp.getAmalgamations()) {
            amalgPropIds[i] = amalProp.getAmalgamatedProperty().getUpicNo();
            i++;
        }
        basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, getModifyRsn(),
                propCompletionDate, null, null, null, null));
        propService.createAmalgPropStatVal(amalgPropIds, basicProp);
    }

    /**
     * Rejects the assessment
     * @return
     */
    @SkipValidation
    @Action(value = "/amalgamation-reject")
    public String reject() {
        LOGGER.debug("reject: Property rejection started");
        if (isBlank(approverComments)) {
            addActionError(getText("property.workflow.remarks"));
            if (checkDesignationsForEdit())
                return NEW;
            else if (checkDesignationsForView())
                return VIEW;
        }
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        if (propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propertyModel.getPropertyDetail().getFloorDetails().clear();

        LOGGER.debug("reject: Property: " + propertyModel);
        final BasicProperty basicProperty = propertyModel.getBasicProperty();
        setBasicProp(basicProperty);
        LOGGER.debug("reject: BasicProperty: " + basicProperty);
        transitionWorkFlow(propertyModel);
        if (propertyModel.getStatus().equals(PropertyTaxConstants.STATUS_CANCELLED))
            for (Amalgamation amalProp : basicProp.getAmalgamations())
                amalProp.getAmalgamatedProperty().setUnderWorkflow(false);

        propService.updateIndexes(propertyModel, getApplicationType());
        propertyImplService.update(propertyModel);
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        final String username = getInitiator();
        final Assignment wfInitiator = propService.getWorkflowInitiator(propertyModel);
        if (wfInitiator.getEmployee().getUsername().equals(securityUtils.getCurrentUser().getUsername())) {
            wfInitiatorRejected = Boolean.TRUE;
            setAckMessage(getText(PROPERTY_MODIFY_FINAL_REJECT_SUCCESS, new String[] { AMALGAMATION_OF_ASSESSMENT }));
        } else
            setAckMessage(getText(PROPERTY_MODIFY_REJECT_SUCCESS, new String[] { AMALGAMATION_OF_ASSESSMENT, username }));

        LOGGER.debug("reject: Property rejection ended");
        return RESULT_ACK;
    }

    /**
     * Prepares acknowledgement message
     */
    private void prepareAckMsg() {
        final String userName = propertyTaxUtil.getApproverUserName(approverPositionId);
        final String action = AMALGAMATION_OF_ASSESSMENT;
        setAckMessage(getText("property.modify.forward.success",
                new String[] { action, userName,
                        propertyModel.getApplicationNo() }));
    }

    @Override
    public PropertyImpl getProperty() {
        return propertyModel;
    }

    @Override
    public void setProperty(final PropertyImpl property) {
        propertyModel = property;
    }

    @Override
    public String getApplicationType() {
        return APPLICATION_TYPE_AMALGAMATION;
    }

    @Override
    public String getAdditionalRule() {
        return AMALGAMATION;
    }

    @Override
    public String getPendingActions() {
        if (propWF != null)
            return propWF.getCurrentState().getNextAction();
        else if (propertyModel.getId() != null && !propertyModel.getCurrentState().getValue().endsWith(STATUS_REJECTED))
            return propertyModel.getCurrentState().getNextAction();
        else
            return pendingActions;
    }

    @Override
    public String getCurrentDesignation() {
        if (propWF != null && !(propWF.getCurrentState().getValue().endsWith(STATUS_REJECTED) ||
                propWF.getCurrentState().getValue().endsWith(WFLOW_ACTION_NEW)))
            return propService.getDesignationForPositionAndUser(propWF.getCurrentState().getOwnerPosition().getId(),
                    securityUtils.getCurrentUser().getId());
        else if (propertyModel.getId() != null && !(propertyModel.getCurrentState().getValue().endsWith(STATUS_REJECTED) ||
                propertyModel.getCurrentState().getValue().endsWith(WFLOW_ACTION_NEW)))
            return propService.getDesignationForPositionAndUser(propertyModel.getCurrentState().getOwnerPosition().getId(),
                    securityUtils.getCurrentUser().getId());
        else
            return currentDesignation;
    }

    public PropertyService getPropService() {
        return propService;
    }

    public void setPropService(final PropertyService propService) {
        this.propService = propService;
    }

    public void setSecurityUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    public BasicProperty getBasicProp() {
        return basicProp;
    }

    public void setBasicProp(final BasicProperty basicProp) {
        this.basicProp = basicProp;
    }

    public Map<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(final Map<Integer, String> floorNoMap) {
        this.floorNoMap = floorNoMap;
    }

    public String getAreaOfPlot() {
        return areaOfPlot;
    }

    public void setAreaOfPlot(String areaOfPlot) {
        this.areaOfPlot = areaOfPlot;
    }

    public String getPropAddress() {
        return propAddress;
    }

    public void setPropAddress(String propAddress) {
        this.propAddress = propAddress;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public PropertyImpl getOldProperty() {
        return oldProperty;
    }

    public void setOldProperty(PropertyImpl oldProperty) {
        this.oldProperty = oldProperty;
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

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public void setPropertyImplService(final PersistenceService<Property, Long> propertyImplService) {
        this.propertyImplService = propertyImplService;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String[] getFloorNoStr() {
        return floorNoStr;
    }

    public void setFloorNoStr(String[] floorNoStr) {
        this.floorNoStr = floorNoStr;
    }

    public String getOldPropertyTypeCode() {
        return oldPropertyTypeCode;
    }

    public void setOldPropertyTypeCode(String oldPropertyTypeCode) {
        this.oldPropertyTypeCode = oldPropertyTypeCode;
    }

    public PropertyTypeMaster getPropTypeMstr() {
        return propTypeMstr;
    }

    public void setPropTypeMstr(PropertyTypeMaster propTypeMstr) {
        this.propTypeMstr = propTypeMstr;
    }

    public String getModifyRsn() {
        return modifyRsn;
    }

    public void setModifyRsn(String modifyRsn) {
        this.modifyRsn = modifyRsn;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(final List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public Boolean getWfInitiatorRejected() {
        return wfInitiatorRejected;
    }

    public void setWfInitiatorRejected(final Boolean wfInitiatorRejected) {
        this.wfInitiatorRejected = wfInitiatorRejected;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<String, String> getGuardianRelationMap() {
        return guardianRelationMap;
    }

    public void setGuardianRelationMap(Map<String, String> guardianRelationMap) {
        this.guardianRelationMap = guardianRelationMap;
    }

    public Map<String, String> getPropTypeCategoryMap() {
        return propTypeCategoryMap;
    }

    public void setPropTypeCategoryMap(Map<String, String> propTypeCategoryMap) {
        this.propTypeCategoryMap = propTypeCategoryMap;
    }

    public boolean isAllowEditDocument() {
        return allowEditDocument;
    }

    public void setAllowEditDocument(boolean allowEditDocument) {
        this.allowEditDocument = allowEditDocument;
    }

}
