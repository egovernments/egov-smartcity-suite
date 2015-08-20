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
package org.egov.ptis.actions.modify;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_ALTER_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_BIFURCATE_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.BILL_COLLECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEVIATION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_AMALGAMATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_BIFURCATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_MODIFY_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_AMALG;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_COURT_RULE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_UPDATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_WORKFLOW_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_CLERK_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

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
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.ValidationError;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.actions.workflow.WorkflowAction;
import org.egov.ptis.client.util.FinancialUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.client.workflow.WorkflowDetails;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@ResultPath(value = "/WEB-INF/jsp")
@Results({ @Result(name = ModifyPropertyAction.RESULT_ACK, location = "modify/modifyProperty-ack.jsp"),
    @Result(name = ModifyPropertyAction.EDIT, location = "modify/modifyProperty-new.jsp"),
    @Result(name = ModifyPropertyAction.NEW, location = "modify/modifyProperty-new.jsp"),
    @Result(name = ModifyPropertyAction.VIEW, location = "modify/modifyProperty-view.jsp"),
    @Result(name = TARGET_WORKFLOW_ERROR, location = "workflow/workflow-error.jsp"),
    @Result(name = ModifyPropertyAction.BALANCE, location = "modify/modifyProperty-balance.jsp"),
    @Result(name = ModifyPropertyAction.PRINTACK, location = "modify/modifyProperty-printAck.jsp") })
@Namespace("/modify")
public class ModifyPropertyAction extends WorkflowAction {
    private static final String BIFURCATION = "Bifurcation";
    private final Logger LOGGER = Logger.getLogger(getClass());
    protected static final String BALANCE = "balance";
    private static final long serialVersionUID = 1L;
    protected static final String RESULT_ACK = "ack";
    private static final String RESULT_ERROR = "error";
    protected static final String VIEW = "view";
    private static final String MODIFY_ACK_TEMPLATE = "modifyProperty_ack";
    public static final String PRINTACK = "printAck";
    private PersistenceService<Property, Long> propertyImplService;
    private PersistenceService<Floor, Long> floorService;
    private BasicProperty basicProp;
    private PropertyImpl oldProperty = new PropertyImpl();
    private PropertyImpl propertyModel = new PropertyImpl();
    private String areaOfPlot;
    private Map<String, String> waterMeterMap;
    private boolean generalTax;
    private boolean sewerageTax;
    private boolean lightingTax;
    private boolean fireServTax;
    private boolean bigResdBldgTax;
    private boolean educationCess;
    private boolean empGuaCess;
    private TreeMap<Integer, String> floorNoMap;
    private String dateOfCompletion;
    private String modifyRsn;
    private Map<String, String> modifyReasonMap;
    private String ownerName;
    private String propAddress;
    private String corrsAddress;
    private String[] amalgPropIds;
    private PropertyService propService;
    private String courtOrdNum;
    private String orderDate;
    private String judgmtDetails;
    private String isAuthProp;
    private String amalgStatus;
    private BasicProperty amalgPropBasicProp;
    private String oldpropId;
    private String oldOwnerName;
    private String oldPropAddress;
    private String ackMessage;
    private Map<String, String> amenitiesMap;
    private String propTypeId;
    private String propTypeCategoryId;
    private String propUsageId;
    private String propOccId;
    private String amenities;
    private String[] floorNoStr = new String[100];
    List<ValidationError> errors = new ArrayList<ValidationError>();
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    int i = 0;
    private PropertyImpl propWF;// would be current property workflow obj
    private Map<String, String> propTypeCategoryMap;
    FinancialUtil financialUtil = new FinancialUtil();
    private String docNumber;
    private Category propertyCategory;
    private boolean isfloorDetailsRequired;
    private boolean updateData;
    private PropertyAddress propertyAddr;
    private String parcelId;
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private String errorMessage;
    private String partNo;
    private List<PropertyOwnerInfo> propertyOwners = new ArrayList<PropertyOwnerInfo>();
    private String modificationType;
    private boolean isTenantFloorPresent;
    private String mode;
    private Integer buildingPermissionNo;
    private Date buildingPermissionDate;
    private Long floorTypeId;
    private Long roofTypeId;
    private Long wallTypeId;
    private Long woodTypeId;
    private List<DocumentType> documentTypes = new ArrayList<>();
    private ReportService reportService;
    private Integer reportId = -1;
    private PropertyTypeMaster propTypeMstr;
    private Map<String, String> deviationPercentageMap;
    private String certificationNumber;
    private String northBoundary;
    private String southBoundary;
    private String eastBoundary;
    private String westBoundary;
    private BigDecimal currentPropertyTax;
    private BigDecimal currentPropertyTaxDue;
    private BigDecimal currentWaterTaxDue;
    private BigDecimal arrearPropertyTaxDue;
    private String taxDueErrorMsg;
    private Long taxExemptedReason;
    
    @Autowired
    private PropertyPersistenceService basicPropertyService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    private PropertyStatusValuesDAO propertyStatusValuesDAO;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private AssignmentService assignmentService;

    public ModifyPropertyAction() {
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

    @SkipValidation
    @Action(value = "/modifyProperty-modifyForm")
    public String modifyForm() {
        LOGGER.debug("Entered into modifyForm, \nIndexNumber: " + indexNumber + ", BasicProperty: " + basicProp
                + ", OldProperty: " + oldProperty + ", PropertyModel: " + propertyModel);
        String target = "";
        target = populateFormData(Boolean.FALSE);
        LOGGER.debug("modifyForm: IsAuthProp: " + getIsAuthProp() + ", AreaOfPlot: " + getAreaOfPlot()
                + ", PropTypeId: " + getPropTypeId() + ", PropTypeCategoryId: " + getPropTypeCategoryId()
                + ", PropUsageId: " + getPropUsageId() + ", PropOccId: " + getPropOccId());
        LOGGER.debug("Exiting from modifyForm");
        return target;
    }

    private String populateFormData(final Boolean fromInbox) {
        LOGGER.debug("Entered into populateFormData");
        String target = "";
        PropertyImpl propertyImpl = null;
        if (basicProp.isUnderWorkflow() && !fromInbox) {
            setWfErrorMsg(getText("wf.pending.msg", "Property Alter/Addition"));
            target = TARGET_WORKFLOW_ERROR;
        } else {
            if (PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(modifyRsn) && !fromInbox) {
                final Map<String, BigDecimal> propertyTaxDetails = propService.getCurrentPropertyTaxDetails(basicProp
                        .getActiveProperty());
                currentPropertyTax = propertyTaxDetails.get(CURR_DMD_STR);
                currentPropertyTaxDue = propertyTaxDetails.get(CURR_DMD_STR).subtract(
                        propertyTaxDetails.get(CURR_COLL_STR));
                arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                        propertyTaxDetails.get(ARR_COLL_STR));
                currentWaterTaxDue = propertyService.getWaterTaxDues(basicProp.getUpicNo());
                if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                    setTaxDueErrorMsg(getText("taxdues.error.msg", new String[] { BIFURCATION }));
                    return BALANCE;
                }
            }

            setOldProperty((PropertyImpl) getBasicProp().getProperty());
            if (propWF == null && (propertyModel == null || propertyModel.getId() == null))
                propertyImpl = (PropertyImpl) oldProperty.createPropertyclone();
            else
                propertyImpl = propWF != null ? propWF : propertyModel;
            setProperty(propertyImpl);
            setOwnerName(basicProp.getFullOwnerName());
            setPropAddress(basicProp.getAddress().toString());
            propertyAddr = basicProp.getAddress();
            corrsAddress = PropertyTaxUtil.getOwnerAddress(basicProp.getPropertyOwnerInfo());
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
            final PropertyTypeMaster propertyType = propertyModel.getPropertyDetail().getPropertyTypeMaster();
            propTypeId = propertyType.getId().toString();
            if (propertyModel.getPropertyDetail().getPropertyUsage() != null)
                propUsageId = propertyModel.getPropertyDetail().getPropertyUsage().getId().toString();
            if (propertyModel.getPropertyDetail().getPropertyOccupation() != null)
                propOccId = propertyModel.getPropertyDetail().getPropertyOccupation().getId().toString();
            setDocNumber(propertyModel.getDocNumber());
            if (propertyModel.getPropertyDetail().getFloorDetails().size() > 0)
                setFloorDetails(propertyModel);
            target = NEW;
        }

        LOGGER.debug("populateFormData - target : " + target + "\n Exiting from populateFormData");
        return target;
    }

    @SkipValidation
    @Action(value = "/modifyProperty-view")
    public String view() {
        LOGGER.debug("Entered into view, BasicProperty: " + basicProp + ", ModelId: " + getModelId());
        if (getModelId() != null) {
            propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
            LOGGER.debug("view: PropertyModel by model id: " + propertyModel);
        }
        final String currWfState = propertyModel.getState().getValue();
        populateFormData(Boolean.TRUE);
        corrsAddress = PropertyTaxUtil.getOwnerAddress(propertyModel.getBasicProperty().getPropertyOwnerInfo());
        amalgPropIds = new String[10];
        if (propertyModel.getPropertyDetail().getFloorDetails().size() > 0)
            setFloorDetails(propertyModel);
        if (!currWfState.endsWith(WF_STATE_COMMISSIONER_APPROVED)) {
            int i = 0;
            for (final PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet()) {
                if (propstatval.getIsActive().equals("W")) {
                    setPropStatValForView(propstatval);
                    LOGGER.debug("view: PropertyStatusValues for new modify screen: " + propstatval);
                }
                // setting the amalgamated properties
                LOGGER.debug("view: Amalgamated property ids:");
                if (PROP_CREATE_RSN.equals(propstatval.getPropertyStatus().getStatusCode())
                        && propstatval.getIsActive().equals("Y"))
                    if (propstatval.getReferenceBasicProperty() != null) {
                        amalgPropIds[i] = propstatval.getReferenceBasicProperty().getUpicNo();
                        LOGGER.debug(amalgPropIds[i] + ", ");
                        i++;
                    }
            }
        }

        if (currWfState.endsWith(WF_STATE_COMMISSIONER_APPROVED)) {
            setIsApprPageReq(Boolean.FALSE);
            if (basicProp.getUpicNo() != null && !basicProp.getUpicNo().isEmpty())
                setIndexNumber(basicProp.getUpicNo());

            int i = 0;
            for (final PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet()) {
                if (propstatval.getIsActive().equals("Y")) {
                    setPropStatValForView(propstatval);
                    LOGGER.debug("PropertyStatusValues for view modify screen: " + propstatval);
                }
                // setting the amalgamated properties
                LOGGER.debug("view: Amalgamated property ids:");
                if (PROP_CREATE_RSN.equals(propstatval.getPropertyStatus().getStatusCode())
                        && propstatval.getIsActive().equals("Y"))
                    if (propstatval.getReferenceBasicProperty() != null) {
                        amalgPropIds[i] = propstatval.getReferenceBasicProperty().getUpicNo();
                        LOGGER.debug(amalgPropIds[i] + ", ");
                        i++;
                    }
            }
        }

        propertyAddr = basicProp.getAddress();
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        setDocNumber(propertyModel.getDocNumber());
        LOGGER.debug("view: ModifyReason: " + getModifyRsn());
        LOGGER.debug("Exiting from view");
        return VIEW;
    }

    @SkipValidation
    @Action(value = "/modifyProperty-forward")
    public String forwardModify() {
        LOGGER.debug("forwardModify: Modify property started " + propertyModel);
        validate();
        final long startTimeMillis = System.currentTimeMillis();
        if (getModelId() != null && !getModelId().trim().isEmpty()) {
            propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));

            LOGGER.debug("forwardModify: Workflow property: " + propWF);
            basicProp = propWF.getBasicProperty();
            setBasicProp(basicProp);
        } else
            populateBasicProp();
        oldProperty = (PropertyImpl) basicProp.getProperty();
        if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
            final Area area = new Area();
            area.setArea(new Float(areaOfPlot));
            propertyModel.getPropertyDetail().setSitalArea(area);
        }
        final String errorKey = propService.validationForBifurcation(propertyModel, basicProp, modifyRsn);
        if (!isBlank(errorKey))
            addActionError(getText(errorKey));

        final PropertyTypeMaster oldPropTypeMstr = oldProperty.getPropertyDetail().getPropertyTypeMaster();
        final PropertyTypeMaster newPropTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                "from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
        if (null != newPropTypeMstr && !newPropTypeMstr.getType().equals(oldPropTypeMstr.getType()))
            if (newPropTypeMstr.getType().equals(OWNERSHIP_TYPE_VAC_LAND_STR))
                addActionError(getText("error.nonVacantToVacant"));
        if (hasErrors())
            if (REVENUE_CLERK_DESGN.equalsIgnoreCase(userDesgn) || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(userDesgn))
                return NEW;
            else if (BILL_COLLECTOR_DESGN.equalsIgnoreCase(userDesgn) || COMMISSIONER_DESGN.equalsIgnoreCase(userDesgn)
                    || REVENUE_OFFICER_DESGN.equalsIgnoreCase(userDesgn))
                return VIEW;
        modifyBasicProp(getDocNumber());
        transitionWorkFlow(propertyModel);
        basicProp.setUnderWorkflow(Boolean.TRUE);
        basicPropertyService.applyAuditing(propertyModel.getState());
        basicPropertyService.update(basicProp);
        propService.updateIndexes(propertyModel,
                PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) ? APPLICATION_TYPE_ALTER_ASSESSENT
                        : APPLICATION_TYPE_BIFURCATE_ASSESSENT);
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        prepareAckMsg();
        buildEmailandSms(propertyModel, APPLICATION_TYPE_ALTER_ASSESSENT);
        addActionMessage(getText("property.forward.success", new String[] { propertyModel.getBasicProperty()
                .getUpicNo() }));
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        LOGGER.info("forwardModify: Modify property forwarded successfully; Time taken(ms) = " + elapsedTimeMillis);
        LOGGER.debug("forwardModify: Modify property forward ended");
        return RESULT_ACK;
    }

    @SkipValidation
    @Action(value = "/modifyProperty-forwardView")
    public String forwardView() {
        LOGGER.debug("Entered into forwardView");
        validateApproverDetails();
        if (hasErrors())
            if (REVENUE_CLERK_DESGN.equalsIgnoreCase(userDesgn) || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(userDesgn))
                return NEW;
            else if (BILL_COLLECTOR_DESGN.equalsIgnoreCase(userDesgn) || COMMISSIONER_DESGN.equalsIgnoreCase(userDesgn)
                    || REVENUE_OFFICER_DESGN.equalsIgnoreCase(userDesgn))
                return VIEW;
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        LOGGER.debug("forwardView: Workflow property: " + propertyModel);
        transitionWorkFlow(propertyModel);
        basicPropertyService.update(basicProp);
        propService.updateIndexes(propertyModel,
                PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) ? APPLICATION_TYPE_ALTER_ASSESSENT
                        : APPLICATION_TYPE_BIFURCATE_ASSESSENT);
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        prepareAckMsg();
        buildEmailandSms(propertyModel, APPLICATION_TYPE_ALTER_ASSESSENT);
        addActionMessage(getText("property.forward.success", new String[] { propertyModel.getBasicProperty()
                .getUpicNo() }));
        LOGGER.debug("Exiting from forwardView");
        return RESULT_ACK;
    }

    @SkipValidation
    @Action(value = "modifyProperty-approve")
    public String approve() {
        LOGGER.debug("Enter method approve");
        amalgPropIds = new String[10];
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        LOGGER.debug("approve: Workflow property: " + propertyModel);
        basicProp = propertyModel.getBasicProperty();
        oldProperty = (PropertyImpl) basicProp.getProperty();

        transitionWorkFlow(propertyModel);
        // FIX ME -- Uncomment this while implementing amalgamation of
        // assessment
        /*
         * int i = 0; for (PropertyStatusValues propstatval :
         * basicProp.getPropertyStatusValuesSet()) { if
         * (propstatval.getIsActive().equals("Y")) { if
         * (PROP_CREATE_RSN.equals(propstatval
         * .getPropertyStatus().getStatusCode())) { if
         * (propstatval.getReferenceBasicProperty() != null) { amalgPropIds[i] =
         * propstatval.getReferenceBasicProperty().getUpicNo(); i++; } } } }
         * setAmalgPropInactive();
         */
        if (!PROPERTY_MODIFY_REASON_OBJ.equals(modifyRsn))
            propService.setWFPropStatValActive(basicProp);

        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) || PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)
                || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
            // createVoucher(); // Creates voucher
        }

        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());

        /**
         * The old property will be made history and the workflow property will
         * be made active only when all the changes are completed in case of
         * modify reason is 'ADD_OR_ALTER' or 'BIFURCATE'
         */
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
            propertyModel.setStatus(STATUS_ISACTIVE);
            oldProperty.setStatus(STATUS_ISHISTORY);
            propertyTaxUtil.makeTheEgBillAsHistory(basicProp);
        }
        // upload docs
        processAndStoreDocumentsWithReason(basicProp, getReason(modifyRsn));
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)
                || PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn))
            updateAddress();

        basicPropertyService.update(basicProp);
        propService.updateIndexes(propertyModel,
                PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) ? APPLICATION_TYPE_ALTER_ASSESSENT
                        : APPLICATION_TYPE_BIFURCATE_ASSESSENT);
        setBasicProp(basicProp);
        setAckMessage(getText("property.approve.succes", new String[] { propertyModel.getBasicProperty().getUpicNo() }));
        buildEmailandSms(propertyModel, APPLICATION_TYPE_ALTER_ASSESSENT);
        addActionMessage(getText("property.approve.success", new String[] { propertyModel.getBasicProperty()
                .getUpicNo() }));
        LOGGER.debug("Exiting approve");
        return RESULT_ACK;
    }

    @SkipValidation
    @Action(value = "/modifyProperty-reject")
    public String reject() {
        LOGGER.debug("reject: Property rejection started");
        if (isBlank(approverComments)) {
            addActionError(getText("property.workflow.remarks"));
            if (REVENUE_CLERK_DESGN.equalsIgnoreCase(userDesgn) || REVENUE_INSPECTOR_DESGN.equalsIgnoreCase(userDesgn))
                return NEW;
            else if (BILL_COLLECTOR_DESGN.equalsIgnoreCase(userDesgn) || COMMISSIONER_DESGN.equalsIgnoreCase(userDesgn)
                    || REVENUE_OFFICER_DESGN.equalsIgnoreCase(userDesgn))
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
        if (REVENUE_CLERK_DESGN.equalsIgnoreCase(userDesgn))
            propertyModel.setStatus(STATUS_ISHISTORY);
        LOGGER.debug("reject: BasicProperty: " + basicProperty);
        transitionWorkFlow(propertyModel);
        propertyImplService.update(propertyModel);
        propService.updateIndexes(propertyModel,
                PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) ? APPLICATION_TYPE_ALTER_ASSESSENT
                        : APPLICATION_TYPE_BIFURCATE_ASSESSENT);
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        String username = "";
        if (propService.isEmployee(propertyModel.getCreatedBy()))
            username = propertyModel.getCreatedBy().getUsername();
        else
            username = assignmentService
                    .getPrimaryAssignmentForPositon(propertyModel.getStateHistory().get(0).getOwnerPosition().getId())
                    .getEmployee().getUsername();
        setAckMessage("Property Rejected Successfully and forwarded to initiator : " + username
                + " with Index Number : ");
        buildEmailandSms(propertyModel, APPLICATION_TYPE_ALTER_ASSESSENT);
        LOGGER.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
        LOGGER.debug("reject: Property rejection ended");
        return RESULT_ACK;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        LOGGER.debug("Entered into preapre, ModelId: " + getModelId());
        super.prepare();
        setUserInfo();
        propertyByEmployee = propService.isEmployee(securityUtils.getCurrentUser());
        if (getModelId() != null && !getModelId().isEmpty()) {
            setBasicProp((BasicProperty) getPersistenceService().find(
                    "select prop.basicProperty from PropertyImpl prop where prop.id=?", Long.valueOf(getModelId())));
            LOGGER.debug("prepare: BasicProperty: " + basicProp);
            propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            if (propWF != null)
                setProperty(propWF);
        } else if (indexNumber != null && !indexNumber.trim().isEmpty())
            setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
                    indexNumber));
        documentTypes = propService.getPropertyModificationDocumentTypes();
        final List<FloorType> floorTypes = getPersistenceService().findAllBy("from FloorType order by name");
        final List<RoofType> roofTypes = getPersistenceService().findAllBy("from RoofType order by name");
        final List<WallType> wallTypes = getPersistenceService().findAllBy("from WallType order by name");
        final List<WoodType> woodTypes = getPersistenceService().findAllBy("from WoodType order by name");
        final List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
                "from PropertyTypeMaster order by orderNo");
        final List<String> StructureList = getPersistenceService().findAllBy("from StructureClassification");
        final List<PropertyUsage> usageList = getPersistenceService()
                .findAllBy("from PropertyUsage order by usageName");
        final List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
        final List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
        final List<String> apartmentsList = getPersistenceService().findAllBy("from Apartment order by name");
        final List<String> taxExemptionReasonList = getPersistenceService().findAllBy(
                "from TaxExeptionReason order by name");
        setFloorNoMap(CommonServices.floorMap());
        addDropdownData("floorType", floorTypes);
        addDropdownData("roofType", roofTypes);
        addDropdownData("wallType", wallTypes);
        addDropdownData("woodType", woodTypes);
        addDropdownData("PropTypeMaster", propTypeList);
        addDropdownData("OccupancyList", propOccList);
        addDropdownData("UsageList", usageList);
        addDropdownData("StructureList", StructureList);
        addDropdownData("AgeFactorList", ageFacList);
        addDropdownData("apartments", apartmentsList);
        addDropdownData("taxExemptionReasonList", taxExemptionReasonList);
        populatePropertyTypeCategory();
        setDeviationPercentageMap(DEVIATION_PERCENTAGE);
        if (getBasicProp() != null)
            setPropAddress(getBasicProp().getAddress().toString());
        if (propWF != null) {
            setOwnerName(propWF.getBasicProperty().getFullOwnerName());
            final List<PropertyOwnerInfo> ownerSet = propWF.getBasicProperty().getPropertyOwnerInfo();
            if (ownerSet != null && !ownerSet.isEmpty())
                for (final PropertyOwnerInfo owner : ownerSet)
                    for (final Address address : owner.getOwner().getAddress()) {
                        corrsAddress = address.toString();
                        break;
                    }
            for (final PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet())
                if (propstatval.getIsActive().equals("W"))
                    setPropStatValForView(propstatval);
        }
        LOGGER.debug("Exiting from preapre, ModelId: " + getModelId());
    }

    private void populatePropertyTypeCategory() {
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1"))
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    "from PropertyTypeMaster ptm where ptm.id = ?", Long.valueOf(propTypeId));
        else if (propertyModel != null && propertyModel.getPropertyDetail() != null
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

    private void modifyBasicProp(final String docNumber) {
        LOGGER.debug("Entered into modifyBasicProp, BasicProperty: " + basicProp);
        LOGGER.debug("modifyBasicProp: PropTypeId: " + propTypeId + ", PropUsageId: " + propUsageId + ", PropOccId: "
                + propOccId + ", statusModifyRsn: " + modifyRsn + ", NoOfAmalgmatedProps: "
                + (amalgPropIds != null ? amalgPropIds.length : "NULL"));

        Date propCompletionDate = null;
        String mutationCode = null;
        final Character status = STATUS_WORKFLOW;
        final PropertyTypeMaster proptypeMstr = propertyTypeMasterDAO.findById(Integer.valueOf(propTypeId), false);
        if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                    .getFloorDetailsProxy());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
        // AMALG & BIFUR
        if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
            basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, getModifyRsn(),
                    propCompletionDate, null, null, null, null));
            if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn))
                propService.createAmalgPropStatVal(amalgPropIds, basicProp);
            mutationCode = getModifyRsn();
        } else if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn)) { // MODIFY
            basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp,
                    PROPERTY_MODIFY_REASON_ADD_OR_ALTER, propCompletionDate, null, null, null, null));
            mutationCode = getModifyRsn();
        } else if (PROPERTY_MODIFY_REASON_COURT_RULE.equals(getModifyRsn())) { // COURT_RULE
            basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp,
                    PROPERTY_MODIFY_REASON_ADD_OR_ALTER, propCompletionDate, getCourtOrdNum(),
                    propService.getPropOccupatedDate(getOrderDate()), getJudgmtDetails(), null));
            mutationCode = getModifyRsn();
        }
        basicProp.setPropOccupationDate(propCompletionDate);

        setProperty(propService.createProperty(propertyModel, getAreaOfPlot(), mutationCode, propTypeId, propUsageId,
                propOccId, status, propertyModel.getDocNumber(), null, floorTypeId, roofTypeId, wallTypeId, woodTypeId, taxExemptedReason));
        updatePropertyID(basicProp);
        propertyModel.setPropertyModifyReason(modifyRsn);
        propertyModel.setBasicProperty(basicProp);
        propertyModel.setEffectiveDate(propCompletionDate);
        final Long oldPropTypeId = oldProperty.getPropertyDetail().getPropertyTypeMaster().getId();
        final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                "from PropertyTypeMaster ptm where ptm.code = ?", OWNERSHIP_TYPE_VAC_LAND);
        /*
         * if modifying from OPEN_PLOT to OTHERS or from OTHERS to OPEN_PLOT
         * property type
         */
        if ((oldPropTypeId == propTypeMstr.getId() && Long.parseLong(propTypeId) != propTypeMstr.getId() || oldPropTypeId != propTypeMstr
                .getId() && Long.parseLong(propTypeId) == propTypeMstr.getId())
                && !propertyModel.getStatus().equals('W'))
            if (propTypeMstr != null
            && org.apache.commons.lang.StringUtils.equals(propTypeMstr.getId().toString(), propTypeId))
                changePropertyDetail(propertyModel, new VacantProperty(), 0);
            else
                changePropertyDetail(propertyModel, new BuiltUpProperty(), propertyModel.getPropertyDetail()
                        .getNo_of_floors());

        final Property modProperty = propService.modifyDemand(propertyModel, oldProperty);

        if (modProperty != null && !modProperty.getDocuments().isEmpty())
            propService.processAndStoreDocument(modProperty.getDocuments());

        if (modProperty == null)
            basicProp.addProperty(propertyModel);
        else
            basicProp.addProperty(modProperty);

        LOGGER.debug("Exiting modifyBasicProp");
    }

    private void updatePropertyID(final BasicProperty basicProperty) {
        final PropertyID propertyId = basicProperty.getPropertyID();
        if (propertyId != null) {
            propertyId.setEastBoundary(getEastBoundary());
            propertyId.setWestBoundary(getWestBoundary());
            propertyId.setNorthBoundary(getNorthBoundary());
            propertyId.setSouthBoundary(getSouthBoundary());
        }
    }

    /**
     * Changes the property details to {@link BuiltUpProperty} or
     * {@link VacantProperty}
     *
     * @param modProperty
     *            the property which is getting modified
     * @param propDetail
     *            the {@link PropertyDetail} type, either
     *            {@link BuiltUpProperty} or {@link VacantProperty}
     * @param numOfFloors
     *            the no. of floors which is dependent on {@link PropertyDetail}
     * @see {@link PropertyDetail}, {@link BuiltUpProperty},
     *      {@link VacantProperty}
     */

    private void changePropertyDetail(final Property modProperty, final PropertyDetail propDetail,
            final Integer numOfFloors) {

        LOGGER.debug("Entered into changePropertyDetail, Property is Vacant Land");

        final PropertyDetail propertyDetail = modProperty.getPropertyDetail();

        propDetail.setSitalArea(propertyDetail.getSitalArea());
        propDetail.setTotalBuiltupArea(propertyDetail.getTotalBuiltupArea());
        propDetail.setCommBuiltUpArea(propertyDetail.getCommBuiltUpArea());
        propDetail.setPlinthArea(propertyDetail.getPlinthArea());
        propDetail.setCommVacantLand(propertyDetail.getCommVacantLand());
        propDetail.setSurveyNumber(propertyDetail.getSurveyNumber());
        propDetail.setFieldVerified(propertyDetail.getFieldVerified());
        propDetail.setFieldVerificationDate(propertyDetail.getFieldVerificationDate());
        propDetail.setFloorDetails(propertyDetail.getFloorDetails());
        propDetail.setPropertyDetailsID(propertyDetail.getPropertyDetailsID());
        propDetail.setWater_Meter_Num(propertyDetail.getWater_Meter_Num());
        propDetail.setElec_Meter_Num(propertyDetail.getElec_Meter_Num());
        propDetail.setNo_of_floors(numOfFloors);
        propDetail.setFieldIrregular(propertyDetail.getFieldIrregular());
        propDetail.setDateOfCompletion(propertyDetail.getDateOfCompletion());
        propDetail.setProperty(propertyDetail.getProperty());
        propDetail.setUpdatedTime(propertyDetail.getUpdatedTime());
        propDetail.setPropertyTypeMaster(propertyDetail.getPropertyTypeMaster());
        propDetail.setPropertyType(propertyDetail.getPropertyType());
        propDetail.setInstallment(propertyDetail.getInstallment());
        propDetail.setPropertyOccupation(propertyDetail.getPropertyOccupation());
        propDetail.setPropertyMutationMaster(propertyDetail.getPropertyMutationMaster());
        propDetail.setComZone(propertyDetail.getComZone());
        propDetail.setCornerPlot(propertyDetail.getCornerPlot());
        propDetail.setCable(propertyDetail.isCable());
        propDetail.setAttachedBathRoom(propertyDetail.isAttachedBathRoom());
        propDetail.setElectricity(propertyDetail.isElectricity());
        propDetail.setWaterTap(propertyDetail.isWaterTap());
        propDetail.setWaterHarvesting(propertyDetail.isWaterHarvesting());
        propDetail.setLift(propertyDetail.isLift());
        propDetail.setToilets(propertyDetail.isToilets());
        propDetail.setFloorType(propertyDetail.getFloorType());
        propDetail.setRoofType(propertyDetail.getRoofType());
        propDetail.setWallType(propertyDetail.getWallType());
        propDetail.setWoodType(propertyDetail.getWoodType());
        propDetail.setExtentSite(propertyDetail.getExtentSite());
        propDetail.setStructure(propertyDetail.isStructure());
        propDetail.setExtentAppartenauntLand(propertyDetail.getExtentAppartenauntLand());
        if (numOfFloors == 0)
            propDetail.setPropertyUsage(propertyDetail.getPropertyUsage());
        else
            propDetail.setPropertyUsage(null);
        propDetail.setManualAlv(propertyDetail.getManualAlv());
        propDetail.setOccupierName(propertyDetail.getOccupierName());

        modProperty.setPropertyDetail(propDetail);

        LOGGER.debug("Exiting from changePropertyDetail");
    }

    private void populateBasicProp() {
        LOGGER.debug("Entered into populateBasicProp");
        if (basicProp == null)
            if (indexNumber != null && !indexNumber.trim().isEmpty())
                setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
                        indexNumber));
            else if (getModelId() != null && !getModelId().equals(""))
                setBasicProp(((PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                        Long.valueOf(getModelId()))).getBasicProperty());
        LOGGER.debug("Exiting from populateBasicProp");
    }

    // FIX ME -- Uncomment while implementing amalgamation of assessment

    /*
     * @SkipValidation public String getStatus() {
     * LOGGER.debug("Entered into getStatus"); checkAmalgStatus();
     * LOGGER.debug("Exiting from getStatus"); return "showStatus"; }
     */

    /*
     * private void checkAmalgStatus() {
     * LOGGER.debug("Entered into checkAmalgStatus, OldPropId: " + oldpropId);
     * List<String> code = new ArrayList<String>(); BigDecimal currDemand =
     * BigDecimal.ZERO; BigDecimal currDemandDue = BigDecimal.ZERO; BigDecimal
     * currCollection = BigDecimal.ZERO; BigDecimal arrDemand = BigDecimal.ZERO;
     * BigDecimal arrCollection = BigDecimal.ZERO; BigDecimal arrearsDue =
     * BigDecimal.ZERO; PropertyStatusValues propStatVal = null;
     * code.add(PROPERTY_STATUS_MARK_DEACTIVE); amalgPropBasicProp =
     * basicPropertyDAO.getBasicPropertyByPropertyID(oldpropId);
     * LOGGER.debug("Amalgmated BasicProperty: " + amalgPropBasicProp);
     * Map<String, String> wfMap = null; String wfStatus = null; if
     * (amalgPropBasicProp != null) { propStatVal = propertyStatusValuesDAO
     * .getLatestPropertyStatusValuesByPropertyIdAndCode(oldpropId, code); wfMap
     * = amalgPropBasicProp.getPropertyWfStatus(); wfStatus =
     * wfMap.get(WFSTATUS); if (!wfStatus.equalsIgnoreCase("TRUE")) {
     * PropertyImpl oldProp = (PropertyImpl) amalgPropBasicProp.getProperty();
     * setOldOwnerName
     * (ptisCacheMgr.buildOwnerFullName(oldProp.getPropertyOwnerInfo()));
     * setOldPropAddress(
     * ptisCacheMgr.buildAddressByImplemetation(amalgPropBasicProp
     * .getAddress())); Map<String, BigDecimal> DmdCollMap =
     * ptDemandDAO.getDemandCollMap(oldProp); currDemand =
     * DmdCollMap.get("CURR_DMD"); arrDemand = DmdCollMap.get("ARR_DMD");
     * currCollection = DmdCollMap.get("CURR_COLL"); arrCollection =
     * DmdCollMap.get("ARR_COLL"); currDemandDue =
     * currDemand.subtract(currCollection); arrearsDue =
     * arrDemand.subtract(arrCollection); } } if (amalgPropBasicProp == null) {
     * setAmalgStatus("Property does not Exist"); } else if (propStatVal !=
     * null) { setAmalgStatus("Property is Marked for Deactivation"); } else if
     * (!amalgPropBasicProp.isActive()) {
     * setAmalgStatus("Property is Deactivated"); } else if
     * (wfStatus.equalsIgnoreCase("TRUE")) {
     * setAmalgStatus("This Property Under Work flow in " + wfMap.get(WFOWNER) +
     * "'s inbox. Please finish pending work flow before doing any transactions on it."
     * ); } else if (currDemandDue.compareTo(BigDecimal.ZERO) == 1 ||
     * arrearsDue.compareTo(BigDecimal.ZERO) == 1) {
     * setAmalgStatus("Property has Pending Balance"); } else {
     * setAmalgStatus("Property is Ready for Amalgamation"); }
     * LOGGER.debug("AmalgStatus: " + getAmalgStatus() +
     * "\nExiting from checkAmalgStatus"); }
     */

    private void setFloorDetails(final Property property) {
        LOGGER.debug("Entered into setFloorDetails, Property: " + property);

        final List<Floor> floors = property.getPropertyDetail().getFloorDetails();
        property.getPropertyDetail().setFloorDetailsProxy(floors);
        int i = 0;
        for (final Floor flr : floors) {
            floorNoStr[i] = propertyTaxUtil.getFloorStr(flr.getFloorNo());
            i++;
        }

        LOGGER.debug("Exiting from setFloorDetails: ");
    }

    public List<Floor> getFloorDetails() {
        return new ArrayList<Floor>(propertyModel.getPropertyDetail().getFloorDetails());
    }

    @Override
    public void validate() {
        LOGGER.debug("Entered into validate, ModifyRsn: " + modifyRsn);
        validateProperty(propertyModel, areaOfPlot, dateOfCompletion, taxExemptedReason, propTypeId,
                propUsageId, propOccId, floorTypeId, roofTypeId, wallTypeId, woodTypeId);

        validateApproverDetails();

        LOGGER.debug("Exiting from validate, BasicProperty: " + getBasicProp());
    }

    private void setPropStatValForView(final PropertyStatusValues propstatval) {
        LOGGER.debug("Entered into setPropStatValForView " + propstatval);
        final PropertyImpl propertyImpl = propWF != null ? propWF : propertyModel;
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(propstatval.getPropertyStatus().getStatusCode()))
            // setting the court rule details
            if (PROPERTY_MODIFY_REASON_COURT_RULE.equals(propertyImpl.getPropertyDetail().getPropertyMutationMaster()
                    .getCode())) {
                setCourtOrdNum(propstatval.getReferenceNo());
                setOrderDate(sdf.format(propstatval.getReferenceDate()));
                setJudgmtDetails(propstatval.getRemarks());
            }
        if (propertyImpl.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            setDateOfCompletion(propstatval.getExtraField1());

        LOGGER.debug("Entered into setPropStatValForView");
    }

    private void prepareAckMsg() {
        LOGGER.debug("Entered into prepareAckMsg, ModifyRsn: " + modifyRsn);
        final User approverUser = eisCommonService.getUserForPosition(approverPositionId, new Date());

        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn))
            setAckMessage(getText("property.modify.forward.success", new String[] { approverUser.getUsername() }));
        else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn))
            setAckMessage(getText("property.bifur.forward.success", new String[] { approverUser.getUsername() }));
        else if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn))
            setAckMessage(getText("property.amalg.forward.success", new String[] { approverUser.getUsername() }));

        LOGGER.debug("AckMessage: " + getAckMessage() + "\nExiting from prepareAckMsg");
    }

    private String getReason(final String modifyReason) {
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn))
            return DOCS_MODIFY_PROPERTY;
        else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn))
            return DOCS_BIFURCATE_PROPERTY;
        else if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn))
            return DOCS_AMALGAMATE_PROPERTY;
        return org.apache.commons.lang.StringUtils.EMPTY;
    }

    public PropertyImpl updatePropertyForMigratedProp(final PropertyImpl property, final String areaOfPlot,
            final String mutationCode, final String propTypeId, final String propUsageId, final String propOccId,
            final String docnumber, final String nonResPlotArea, final boolean isfloorDetailsRequired) {
        LOGGER.debug("Entered into modifyPropertyForMigratedProp");
        LOGGER.debug("modifyPropertyForMigratedProp: Property: " + property + ", areaOfPlot: " + areaOfPlot
                + ", mutationCode: " + mutationCode + ",propTypeId: " + propTypeId + ", propUsageId: " + propUsageId
                + ", propOccId: " + propOccId);

        if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
            final Area area = new Area();
            area.setArea(new Float(areaOfPlot));
            property.getPropertyDetail().setSitalArea(area);
        }

        if (nonResPlotArea != null && !nonResPlotArea.isEmpty()) {
            final Area area = new Area();
            area.setArea(new Float(nonResPlotArea));
            property.getPropertyDetail().setNonResPlotArea(area);
        }

        property.getPropertyDetail().setFieldVerified('Y');
        property.getPropertyDetail().setProperty(property);
        final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) persistenceService.find(
                "from PropertyTypeMaster PTM where PTM.id = ?", Long.valueOf(propTypeId));
        final String propTypeCode = propTypeMstr.getCode();

        final boolean isNofloors = OWNERSHIP_TYPE_VAC_LAND.equals(propTypeCode);

        if (propUsageId != null && isNofloors) {
            final PropertyUsage usage = (PropertyUsage) persistenceService.find(
                    "from PropertyUsage pu where pu.id = ?", Long.valueOf(propUsageId));
            property.getPropertyDetail().setPropertyUsage(usage);
        } else
            property.getPropertyDetail().setPropertyUsage(null);

        if (propOccId != null && isNofloors) {
            final PropertyOccupation occupancy = (PropertyOccupation) persistenceService.find(
                    "from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
            property.getPropertyDetail().setPropertyOccupation(occupancy);
        } else
            property.getPropertyDetail().setPropertyOccupation(null);

        if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            property.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
        else
            property.getPropertyDetail().setPropertyType(BUILT_UP_PROPERTY);

        property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        propertyModel.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        propertyModel.getPropertyDetail().setPropertyMutationMaster(
                property.getPropertyDetail().getPropertyMutationMaster());
        property.getPropertyDetail().setUpdatedTime(new Date());

        propService.createFloors(propertyModel, mutationCode, propUsageId, propOccId);

        for (final Floor floor : property.getPropertyDetail().getFloorDetails())
            for (final Floor newFloorInfo : propertyModel.getPropertyDetail().getFloorDetails())
                if (floor.getId().equals(newFloorInfo.getId())) {
                    floor.setUnitType(newFloorInfo.getUnitType());
                    floor.setUnitTypeCategory(newFloorInfo.getUnitTypeCategory());
                    floor.setFloorNo(newFloorInfo.getFloorNo());
                    floor.setBuiltUpArea(newFloorInfo.getBuiltUpArea());
                    floor.setPropertyUsage(newFloorInfo.getPropertyUsage());
                    floor.setPropertyOccupation(newFloorInfo.getPropertyOccupation());
                    floor.setWaterRate(newFloorInfo.getWaterRate());
                    floor.setStructureClassification(newFloorInfo.getStructureClassification());
                    floor.setDepreciationMaster(newFloorInfo.getDepreciationMaster());
                    floor.setRentPerMonth(newFloorInfo.getRentPerMonth());
                    floor.setManualAlv(newFloorInfo.getManualAlv());
                    break;
                }
        property.getPropertyDetail().setNo_of_floors(property.getPropertyDetail().getFloorDetails().size());
        property.setDocNumber(docnumber);
        LOGGER.debug("Exiting from createProperty");
        return property;
    }

    private void updateBasicPropForMigratedProp(final String docNumber, PropertyImpl existingProp) {
        LOGGER.debug("Entered into modifyBasicPropForMigratedProp, BasicProperty: " + basicProp);
        LOGGER.debug("modifyBasicPropForMigratedProp: PropTypeId: " + propTypeId + ", PropUsageId: " + propUsageId
                + ", PropOccId: " + propOccId + ", statusModifyRsn: " + modifyRsn);

        Date propCompletionDate = null;
        final PropertyTypeMaster proptypeMstr = propertyTypeMasterDAO.findById(Integer.valueOf(propTypeId), false);
        if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                    .getFloorDetails());
        else
            propCompletionDate = propService.getPropOccupatedDate(getDateOfCompletion());

        basicProp.setExtraField1(isAuthProp);
        basicProp.setPropOccupationDate(propCompletionDate);
        existingProp = updatePropertyForMigratedProp(existingProp, getAreaOfPlot(), PROP_CREATE_RSN, propTypeId,
                propUsageId, propOccId, propertyModel.getDocNumber(), null, isfloorDetailsRequired);
        existingProp.setBasicProperty(basicProp);
        existingProp.setEffectiveDate(propCompletionDate);
        existingProp.getPropertyDetail().setManualAlv(propertyModel.getPropertyDetail().getManualAlv());
        existingProp.getPropertyDetail().setOccupierName(propertyModel.getPropertyDetail().getOccupierName());

        existingProp.setDocNumber(docNumber);
        updateAddress();
        basicProp.setGisReferenceNo(parcelId);
        basicProp.getPropertyID().setNorthBoundary(northBoundary);
        basicProp.getPropertyID().setSouthBoundary(southBoundary);
        basicProp.getPropertyID().setEastBoundary(eastBoundary);
        basicProp.getPropertyID().setWestBoundary(westBoundary);

        propertyImplService.merge(existingProp);
        basicPropertyService.update(basicProp);

        LOGGER.debug("Exiting modifyBasicPropForMigratedProp");
    }

    @ValidationErrorPage(value = "new")
    public String updateData() {
        LOGGER.debug("updateData: Property modification started for Migrated Property, PropertyId: " + propertyModel);
        final long startTimeMillis = System.currentTimeMillis();

        if (ASSISTANT_ROLE.equals(userRole)) {

            final PropertyImpl nonHistoryProperty = (PropertyImpl) basicProp.getProperty();
            processAndStoreDocumentsWithReason(basicProp, getReason(modifyRsn));
            updateBasicPropForMigratedProp(getDocNumber(), nonHistoryProperty);
            setAckMessage("Migrated Property updated Successfully in System with Index Number: ");

            final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
            LOGGER.info("updateData: Property modified successfully in system with Index Number: "
                    + basicProp.getUpicNo() + "; Time taken(ms) = " + elapsedTimeMillis);
        }

        return RESULT_ACK;
    }

    @SkipValidation
    public String modifyOrDataUpdateForm() {
        LOGGER.debug("Entered into modifyOrDataUpdateForm");
        String resultPage = "";
        if (PROPERTY_MODIFY_REASON_DATA_UPDATE.equals(modifyRsn) && basicProp.getIsMigrated().equals('N')) {
            setErrorMessage(" This is not a migrated property ");
            resultPage = RESULT_ERROR;
        } else
            resultPage = populateFormData(Boolean.FALSE);

        LOGGER.debug("Exiting from modifyOrDataUpdateForm");
        return resultPage;
    }

    private void updateAddress() {
        LOGGER.debug("Entered into updateAddress");

        final PropertyAddress addr = basicProp.getAddress();
        if (propertyAddr != null) {
            addr.setHouseNoBldgApt(propertyAddr.getHouseNoBldgApt());
            addr.setLandmark(propertyAddr.getLandmark());
            addr.setPinCode(propertyAddr.getPinCode());
        }
        LOGGER.debug("Exiting from updateAddress");
    }

    @SkipValidation
    @Action(value = "/modifyProperty-printAck")
    public String printAck() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String imagePath = url.concat(PropertyTaxConstants.IMAGES_BASE_PATH).concat(ReportUtil.fetchLogo());
        final PropertyAckNoticeInfo ackBean = new PropertyAckNoticeInfo();
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        ackBean.setOwnerName(basicProp.getFullOwnerName());
        ackBean.setOwnerAddress(basicProp.getAddress().toString());
        ackBean.setApplicationDate(new SimpleDateFormat("dd/MM/yyyy").format(basicProp.getCreatedDate()));
        ackBean.setApplicationNo(propertyModel.getApplicationNo());
        ackBean.setApprovedDate(new SimpleDateFormat("dd/MM/yyyy").format(propWF.getState().getCreatedDate()));
        final Date noticeDueDate = DateUtils.add(propWF.getState().getCreatedDate(), Calendar.DAY_OF_MONTH, 15);
        ackBean.setNoticeDueDate(noticeDueDate);
        reportParams.put("logoPath", imagePath);
        reportParams.put("loggedInUsername", propertyTaxUtil.getLoggedInUser(getSession()).getName());
        final ReportRequest reportInput = new ReportRequest(MODIFY_ACK_TEMPLATE, ackBean, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
        return PRINTACK;
    }

    public BasicProperty getBasicProp() {
        return basicProp;
    }

    public void setBasicProp(final BasicProperty basicProp) {
        this.basicProp = basicProp;
    }

    public Long getTaxExemptedReason() {
		return taxExemptedReason;
	}

	public void setTaxExemptedReason(Long taxExemptedReason) {
		this.taxExemptedReason = taxExemptedReason;
	}

	public String getModifyRsn() {
        return modifyRsn;
    }

    public void setModifyRsn(final String modifyRsn) {
        this.modifyRsn = modifyRsn;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPropAddress() {
        return propAddress;
    }

    public void setPropAddress(final String propAddress) {
        this.propAddress = propAddress;
    }

    public String getAreaOfPlot() {
        return areaOfPlot;
    }

    public void setAreaOfPlot(final String areaOfPlot) {
        this.areaOfPlot = areaOfPlot;
    }

    public Map<String, String> getWaterMeterMap() {
        return waterMeterMap;
    }

    public void setWaterMeterMap(final Map<String, String> waterMeterMap) {
        this.waterMeterMap = waterMeterMap;
    }

    public boolean isGeneralTax() {
        return generalTax;
    }

    public void setGeneralTax(final boolean generalTax) {
        this.generalTax = generalTax;
    }

    public boolean isSewerageTax() {
        return sewerageTax;
    }

    public void setSewerageTax(final boolean sewerageTax) {
        this.sewerageTax = sewerageTax;
    }

    public boolean isLightingTax() {
        return lightingTax;
    }

    public void setLightingTax(final boolean lightingTax) {
        this.lightingTax = lightingTax;
    }

    public boolean isFireServTax() {
        return fireServTax;
    }

    public void setFireServTax(final boolean fireServTax) {
        this.fireServTax = fireServTax;
    }

    public boolean isBigResdBldgTax() {
        return bigResdBldgTax;
    }

    public void setBigResdBldgTax(final boolean bigResdBldgTax) {
        this.bigResdBldgTax = bigResdBldgTax;
    }

    public boolean isEducationCess() {
        return educationCess;
    }

    public void setEducationCess(final boolean educationCess) {
        this.educationCess = educationCess;
    }

    public boolean isEmpGuaCess() {
        return empGuaCess;
    }

    public void setEmpGuaCess(final boolean empGuaCess) {
        this.empGuaCess = empGuaCess;
    }

    public TreeMap<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(final TreeMap<Integer, String> floorNoMap) {
        this.floorNoMap = floorNoMap;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(final String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public Map<String, String> getModifyReasonMap() {
        return modifyReasonMap;
    }

    public void setModifyReasonMap(final Map<String, String> modifyReasonMap) {
        this.modifyReasonMap = modifyReasonMap;
    }

    public String[] getAmalgPropIds() {
        return amalgPropIds;
    }

    public void setAmalgPropIds(final String[] amalgPropIds) {
        this.amalgPropIds = amalgPropIds;
    }

    public PropertyService getPropService() {
        return propService;
    }

    public void setPropService(final PropertyService propService) {
        this.propService = propService;
    }

    public String getCourtOrdNum() {
        return courtOrdNum;
    }

    public void setCourtOrdNum(final String courtOrdNum) {
        this.courtOrdNum = courtOrdNum;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final String orderDate) {
        this.orderDate = orderDate;
    }

    public String getJudgmtDetails() {
        return judgmtDetails;
    }

    public void setJudgmtDetails(final String judgmtDetails) {
        this.judgmtDetails = judgmtDetails;
    }

    public PropertyImpl getOldProperty() {
        return oldProperty;
    }

    public void setOldProperty(final PropertyImpl oldProperty) {
        this.oldProperty = oldProperty;
    }

    @Override
    public PropertyImpl getProperty() {
        return propertyModel;
    }

    @Override
    public void setProperty(final PropertyImpl property) {
        propertyModel = property;
    }

    public String getIsAuthProp() {
        return isAuthProp;
    }

    public void setIsAuthProp(final String isAuthProp) {
        this.isAuthProp = isAuthProp;
    }

    public void setPropertyImplService(final PersistenceService<Property, Long> propertyImplService) {
        this.propertyImplService = propertyImplService;
    }

    public String getAmalgStatus() {
        return amalgStatus;
    }

    public void setAmalgStatus(final String amalgStatus) {
        this.amalgStatus = amalgStatus;
    }

    public BasicProperty getAmalgPropBasicProp() {
        return amalgPropBasicProp;
    }

    public void setAmalgPropBasicProp(final BasicProperty amalgPropBasicProp) {
        this.amalgPropBasicProp = amalgPropBasicProp;
    }

    public String getOldpropId() {
        return oldpropId;
    }

    public void setOldpropId(final String oldpropId) {
        this.oldpropId = oldpropId;
    }

    public String getOldOwnerName() {
        return oldOwnerName;
    }

    public void setOldOwnerName(final String oldOwnerName) {
        this.oldOwnerName = oldOwnerName;
    }

    public String getOldPropAddress() {
        return oldPropAddress;
    }

    public void setOldPropAddress(final String oldPropAddress) {
        this.oldPropAddress = oldPropAddress;
    }

    public Map<String, String> getAmenitiesMap() {
        return amenitiesMap;
    }

    public void setAmenitiesMap(final Map<String, String> amenitiesMap) {
        this.amenitiesMap = amenitiesMap;
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

    public String getCorrsAddress() {
        return corrsAddress;
    }

    public void setCorrsAddress(final String corrsAddress) {
        this.corrsAddress = corrsAddress;
    }

    public String[] getFloorNoStr() {
        return floorNoStr;
    }

    public void setFloorNoStr(final String[] floorNoStr) {
        this.floorNoStr = floorNoStr;
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

    public String getPropTypeCategoryId() {
        return propTypeCategoryId;
    }

    public void setPropTypeCategoryId(final String propTypeCategoryId) {
        this.propTypeCategoryId = propTypeCategoryId;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(final String amenities) {
        this.amenities = amenities;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(final String docNumber) {
        this.docNumber = docNumber;
    }

    public Category getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(final Category propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public boolean isIsfloorDetailsRequired() {
        return isfloorDetailsRequired;
    }

    public void setIsfloorDetailsRequired(final boolean isfloorDetailsRequired) {
        this.isfloorDetailsRequired = isfloorDetailsRequired;
    }

    public boolean isUpdateData() {
        return updateData;
    }

    public void setUpdateData(final boolean updateData) {
        this.updateData = updateData;
    }

    public PersistenceService<Floor, Long> getFloorService() {
        return floorService;
    }

    public void setFloorService(final PersistenceService<Floor, Long> floorService) {
        this.floorService = floorService;
    }

    public PropertyAddress getPropertyAddr() {
        return propertyAddr;
    }

    public void setPropertyAddr(final PropertyAddress propertyAddr) {
        this.propertyAddr = propertyAddr;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(final String parcelId) {
        this.parcelId = parcelId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
        return propertyTaxNumberGenerator;
    }

    public void setPropertyTaxNumberGenerator(final PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
        this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
    }

    public List<PropertyOwnerInfo> getPropertyOwners() {
        return propertyOwners;
    }

    public void setPropertyOwners(final List<PropertyOwnerInfo> propertyOwners) {
        this.propertyOwners = propertyOwners;
    }

    public WorkflowDetails getWorkflowAction() {
        return workflowAction;
    }

    public void setWorkflowAction(final WorkflowDetails workflowAction) {
        this.workflowAction = workflowAction;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(final String partNo) {
        this.partNo = partNo;
    }

    public String getModificationType() {
        return modificationType;
    }

    public void setModificationType(final String modificationType) {
        this.modificationType = modificationType;
    }

    public boolean getIsTenantFloorPresent() {
        return isTenantFloorPresent;
    }

    public void setIsTenantFloorPresent(final boolean isTenantFloorPresent) {
        this.isTenantFloorPresent = isTenantFloorPresent;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public Integer getBuildingPermissionNo() {
        return buildingPermissionNo;
    }

    public void setBuildingPermissionNo(final Integer buildingPermissionNo) {
        this.buildingPermissionNo = buildingPermissionNo;
    }

    public Date getBuildingPermissionDate() {
        return buildingPermissionDate;
    }

    public void setBuildingPermissionDate(final Date buildingPermissionDate) {
        this.buildingPermissionDate = buildingPermissionDate;
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

    public void setBasicPropertyDAO(final BasicPropertyDAO basicPropertyDAO) {
        this.basicPropertyDAO = basicPropertyDAO;
    }

    public void setPropertyTypeMasterDAO(final PropertyTypeMasterDAO propertyTypeMasterDAO) {
        this.propertyTypeMasterDAO = propertyTypeMasterDAO;
    }

    public void setPropertyStatusValuesDAO(final PropertyStatusValuesDAO propertyStatusValuesDAO) {
        this.propertyStatusValuesDAO = propertyStatusValuesDAO;
    }

    public void setPtDemandDAO(final PtDemandDao ptDemandDAO) {
        this.ptDemandDAO = ptDemandDAO;
    }

    public void setbasicPropertyService(final PropertyPersistenceService basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    public void setSecurityUtils(final SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public void setAssignmentService(final AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    public List<DocumentType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(final List<DocumentType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(final Integer reportId) {
        this.reportId = reportId;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public PropertyTypeMaster getPropTypeMstr() {
        return propTypeMstr;
    }

    public void setPropTypeMstr(final PropertyTypeMaster propTypeMstr) {
        this.propTypeMstr = propTypeMstr;
    }

    public Map<String, String> getDeviationPercentageMap() {
        return deviationPercentageMap;
    }

    public void setDeviationPercentageMap(final Map<String, String> deviationPercentageMap) {
        this.deviationPercentageMap = deviationPercentageMap;
    }

    public String getCertificationNumber() {
        return certificationNumber;
    }

    public void setCertificationNumber(final String certificationNumber) {
        this.certificationNumber = certificationNumber;
    }

    public String getNorthBoundary() {
        return northBoundary;
    }

    public void setNorthBoundary(final String northBoundary) {
        this.northBoundary = northBoundary;
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

    public String getSouthBoundary() {
        return southBoundary;
    }

    public void setSouthBoundary(final String southBoundary) {
        this.southBoundary = southBoundary;
    }

    @Override
    public String getAdditionalRule() {
        return PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) ? ADDTIONAL_RULE_ALTER_ASSESSMENT
                : ADDTIONAL_RULE_BIFURCATE_ASSESSMENT;
    }

    public BigDecimal getCurrentPropertyTax() {
        return currentPropertyTax;
    }

    public void setCurrentPropertyTax(final BigDecimal currentPropertyTax) {
        this.currentPropertyTax = currentPropertyTax;
    }

    public BigDecimal getCurrentPropertyTaxDue() {
        return currentPropertyTaxDue;
    }

    public void setCurrentPropertyTaxDue(final BigDecimal currentPropertyTaxDue) {
        this.currentPropertyTaxDue = currentPropertyTaxDue;
    }

    public BigDecimal getCurrentWaterTaxDue() {
        return currentWaterTaxDue;
    }

    public void setCurrentWaterTaxDue(final BigDecimal currentWaterTaxDue) {
        this.currentWaterTaxDue = currentWaterTaxDue;
    }

    public BigDecimal getArrearPropertyTaxDue() {
        return arrearPropertyTaxDue;
    }

    public void setArrearPropertyTaxDue(final BigDecimal arrearPropertyTaxDue) {
        this.arrearPropertyTaxDue = arrearPropertyTaxDue;
    }

    public String getTaxDueErrorMsg() {
        return taxDueErrorMsg;
    }

    public void setTaxDueErrorMsg(final String taxDueErrorMsg) {
        this.taxDueErrorMsg = taxDueErrorMsg;
    }

}
