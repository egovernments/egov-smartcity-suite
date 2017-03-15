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
import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_ALTER_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDTIONAL_RULE_BIFURCATE_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ALTERATION_OF_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.AMALGAMATION_OF_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.ANONYMOUS_USER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_CLIENT_SPECIFIC_DMD_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.BIFURCATION_OF_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.BILL_COLLECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.BUILT_UP_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_NON_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.CATEGORY_RESIDENTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CSC_OPERATOR_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_BAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.DEVIATION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_AMALGAMATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_BIFURCATE_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DOCS_MODIFY_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.GRP_OF_ASSESSMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.JUNIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_AMALG;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_COURT_RULE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_UPDATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.PROP_CREATE_RSN;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_WORKFLOW_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_INSPECTOR_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ROLE_ULB_OPERATOR;
import static org.egov.ptis.constants.PropertyTaxConstants.SENIOR_ASSISTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_DATAENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCE_ONLINE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.TARGET_WORKFLOW_ERROR;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_ASSISTANT_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Namespaces;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.eis.entity.Assignment;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.FinancialUtil;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BuiltUpProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.PtApplicationType;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.VacantProperty;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.repository.master.vacantland.LayoutApprovalAuthorityRepository;
import org.egov.ptis.domain.repository.master.vacantland.VacantLandPlotAreaRepository;
import org.egov.ptis.domain.service.property.PropertyPersistenceService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.gson.GsonBuilder;

/**
 * This class is used to do Addition/Alteration/Bifurcation of an Assessment
 *
 * @author subhash
 */

@ParentPackage("egov")
@ResultPath(value = "/WEB-INF/jsp")
@Results({ @Result(name = ModifyPropertyAction.RESULT_ACK, location = "modify/modifyProperty-ack.jsp"),
        @Result(name = ModifyPropertyAction.EDIT, location = "modify/modifyProperty-new.jsp"),
        @Result(name = ModifyPropertyAction.NEW, location = "modify/modifyProperty-new.jsp"),
        @Result(name = ModifyPropertyAction.CITIZEN_NEW, location = "citizen/modify/modifyProperty-new.jsp"),
        @Result(name = ModifyPropertyAction.VIEW, location = "modify/modifyProperty-view.jsp"),
        @Result(name = TARGET_WORKFLOW_ERROR, location = "workflow/workflow-error.jsp"),
        @Result(name = ModifyPropertyAction.BALANCE, location = "modify/modifyProperty-balance.jsp"),
        @Result(name = ModifyPropertyAction.PRINT_ACK, location = "modify/modifyProperty-printAck.jsp"),
        @Result(name = ModifyPropertyAction.COMMON_FORM, location = "search/searchProperty-commonForm.jsp"),
        @Result(name = ModifyPropertyAction.MEESEVA_ERROR, location = "common/meeseva-errorPage.jsp"),
        @Result(name = ModifyPropertyAction.MEESEVA_RESULT_ACK, location = "common/meesevaAck.jsp") })
@Namespaces(value={@Namespace("/modify"),@Namespace("/citizen/modify")})
public class ModifyPropertyAction extends PropertyTaxBaseAction {
    private static final String FROM_PROPERTY_TYPE_MASTER_WHERE_ID = "from PropertyTypeMaster ptm where ptm.id = ?";
    private final Logger logger = Logger.getLogger(getClass());
    protected static final String COMMON_FORM = "commonForm";
    protected static final String BALANCE = "balance";
    protected static final String RESULT_ACK = "ack";
    protected static final String VIEW = "view";
    protected static final String CITIZEN_NEW = "citizenNewForm";

    private static final String PROPERTY_MODIFY_REJECT_SUCCESS = "property.modify.reject.success";
    private static final String PROPERTY_MODIFY_FINAL_REJECT_SUCCESS = "property.modify.final.reject.success";
    private static final String PROPERTY_MODIFY_APPROVE_SUCCESS = "property.modify.approve.success";
    private static final String PROPERTY_FORWARD_SUCCESS = "property.forward.success";
    private static final String TAXDUES_ERROR_MSG = "taxdues.error.msg";
    private static final String WF_PENDING_MSG = "wf.pending.msg";
    private static final String PROPERTY_MODIFY_REJECT_FAILURE = "property.modify.reject.failure";
    private static final String PROPERTY_ALTER_ADDITION = "Property Alter/Addition";
    private static final String PROPERTY_BIFURCATION = "Property Bifurcation";
    private static final String PROPERTY_GENERAL_REVISION_PETITION = "Property General Revision Petition";
    private static final long serialVersionUID = 1L;
    private static final String BIFURCATION = "Bifurcation";
    private static final String RESULT_ERROR = "error";
    private static final String MODIFY_ACK_TEMPLATE = "mainModifyPropertyAck";
    private static final String GRP_ACK_TEMPLATE = "mainGRPPropertyAck";
    public static final String PRINT_ACK = "printAck";
    public static final String MEESEVA_RESULT_ACK = "meesevaAck";
    public static final String MEESEVA_ERROR = "meesevaError";

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
    private SortedMap<Integer, String> floorNoMap;
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
    private String propertyCategory;
    private String propUsageId;
    private String propOccId;
    private String amenities;
    private String[] floorNoStr = new String[100];
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT_DDMMYYY);
    private PropertyImpl propWF;// would be current property workflow obj
    private Map<String, String> propTypeCategoryMap;
    FinancialUtil financialUtil = new FinancialUtil();
    private String docNumber;
    private boolean isfloorDetailsRequired;
    private boolean updateData;
    private PropertyAddress propertyAddr;
    private String parcelId;
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private String errorMessage;
    private String partNo;
    private List<PropertyOwnerInfo> propertyOwners = new ArrayList<>();
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
    private String reportId;
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
    private Boolean wfInitiatorRejected;
    private String houseNo;
    private String oldPropertyTypeCode;
    private Boolean isMeesevaUser = Boolean.FALSE;
    private String meesevaApplicationNumber;
    private Boolean showTaxCalcBtn = Boolean.FALSE;
    private Long propertyDepartmentId;
    private Long vacantLandPlotAreaId;
    private Long layoutApprovalAuthorityId;
    private List<VacantLandPlotArea> vacantLandPlotAreaList = new ArrayList<>();
    private List<LayoutApprovalAuthority> layoutApprovalAuthorityList = new ArrayList<>();
    private boolean allowEditDocument = Boolean.FALSE;
    private Boolean showAckBtn = Boolean.FALSE;
    private String applicationSource;

    @Autowired
    transient PropertyPersistenceService basicPropertyService;
    @Autowired
    transient PropertyService propertyService;
    @Autowired
    transient PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    transient SecurityUtils securityUtils;
    @Autowired
    transient ReportViewerUtil reportViewerUtil;
    @Autowired
    transient APTaxCalculator taxCalculator;

    @Autowired
    transient VacantLandPlotAreaRepository vacantLandPlotAreaRepository;

    @Autowired
    transient LayoutApprovalAuthorityRepository layoutApprovalAuthorityRepository;

    @Autowired
    @Qualifier("ptaxApplicationTypeService")
    private PersistenceService<PtApplicationType, Long> ptaxApplicationTypeService;

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

    /**
     * Returns modify property form
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-modifyForm")
    public String modifyForm() {
        final User currentUser = securityUtils.getCurrentUser();
        if (logger.isDebugEnabled())
            logger.debug("Entered into modifyForm, \nIndexNumber: " + indexNumber + ", BasicProperty: " + basicProp
                    + ", OldProperty: " + oldProperty + ", PropertyModel: " + propertyModel);
        if (propertyModel.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
            addActionError(getText("error.msg.demandInactive"));
            return COMMON_FORM;
        }
        String target;
        target = populateFormData(Boolean.FALSE);
        isMeesevaUser = propService.isMeesevaUser(currentUser);
        if (isMeesevaUser)
            if (getMeesevaApplicationNumber() == null) {
                addActionMessage(getText("MEESEVA.005"));
                return MEESEVA_ERROR;
            } else
                propertyModel.setMeesevaApplicationNumber(getMeesevaApplicationNumber());
        if (logger.isDebugEnabled()) {
            logger.debug("modifyForm: IsAuthProp: " + getIsAuthProp() + ", AreaOfPlot: " + getAreaOfPlot()
                    + ", PropTypeId: " + getPropTypeId() + ", PropertyCategory: " + getPropertyCategory()
                    + ", PropUsageId: " + getPropUsageId() + ", PropOccId: " + getPropOccId());
            logger.debug("Exiting from modifyForm");
        }
        showTaxCalculateButton();
        if (StringUtils.isBlank(applicationSource)
                && (propService.isEmployee(currentUser) && !propertyTaxCommonUtils.isEligibleInitiator(currentUser.getId()))) {
            addActionError(getText("initiator.noteligible"));
            return COMMON_FORM;
        }
        return target;
    }

    /**
     * Returns modify data entry form
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-modifyDataEntry")
    public String modifyDataEntry() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into modifyForm, \nIndexNumber: " + indexNumber + ", BasicProperty: " + basicProp
                    + ", OldProperty: " + oldProperty + ", PropertyModel: " + propertyModel);
        String target;
        target = populateFormData(Boolean.FALSE);
        if (logger.isDebugEnabled()) {
            logger.debug("modifyForm: IsAuthProp: " + getIsAuthProp() + ", AreaOfPlot: " + getAreaOfPlot()
                    + ", PropTypeId: " + getPropTypeId() + ", PropTypeCategoryId: " + getPropertyCategory()
                    + ", PropUsageId: " + getPropUsageId() + ", PropOccId: " + getPropOccId());
            logger.debug("Exiting from modifyForm");
        }
        return target;
    }

    @SkipValidation
    @Action(value = "/modifyProperty-saveDataEntry")
    public String saveDataEntry() {
        if (logger.isDebugEnabled())
            logger.debug("updateData: Property modification started for Migrated Property, PropertyId: " + propertyModel);
        final long startTimeMillis = System.currentTimeMillis();
        if (logger.isDebugEnabled())
            logger.debug("Entered into modifyForm, \nIndexNumber: " + indexNumber + ", BasicProperty: " + basicProp
                    + ", OldProperty: " + oldProperty + ", PropertyModel: " + propertyModel);
        Date propCompletionDate;
        final PropertyTypeMaster proptypeMstr = propertyTypeMasterDAO.findById(Integer.valueOf(propTypeId), false);
        if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                    .getFloorDetailsProxy());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
        final PropertyImpl property = (PropertyImpl) basicProp.getProperty();
        propService.createProperty(property, getAreaOfPlot(), modifyRsn, propTypeId, propUsageId, propOccId, 'A',
                propertyModel.getDocNumber(), null, floorTypeId, roofTypeId, wallTypeId, woodTypeId, taxExemptedReason,
                propertyDepartmentId, vacantLandPlotAreaId, layoutApprovalAuthorityId, Boolean.FALSE);
        propertyModel.setPropertyModifyReason(modifyRsn);
        propertyModel.setBasicProperty(basicProp);
        propertyModel.setEffectiveDate(propCompletionDate);
        if (logger.isDebugEnabled()) {
            logger.debug("modifyForm: AreaOfPlot: " + getAreaOfPlot() + ", PropTypeId: " + getPropTypeId()
                    + ", PropUsageId: " + getPropUsageId() + ", PropOccId: " + getPropOccId());
            logger.debug("Exiting from modifyForm");
        }
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (logger.isInfoEnabled())
            logger.info("updateData: Property modified successfully in system with Index Number: " + basicProp.getUpicNo()
                    + "; Time taken(ms) = " + elapsedTimeMillis);

        return RESULT_ACK;
    }

    /**
     * Populates form data to be displayed
     *
     * @param fromInbox
     * @return
     */
    private String populateFormData(final Boolean fromInbox) {
        if (logger.isDebugEnabled())
            logger.debug("Entered into populateFormData");
        String target;
        PropertyImpl propertyImpl;
        if (basicProp.isUnderWorkflow() && !fromInbox) {
            final List<String> msgParams = new ArrayList<>();
            if (PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(modifyRsn))
                msgParams.add(PROPERTY_BIFURCATION);
            else if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equalsIgnoreCase(modifyRsn))
                msgParams.add(PROPERTY_ALTER_ADDITION);
            else
                msgParams.add(PROPERTY_GENERAL_REVISION_PETITION);
            setWfErrorMsg(getText(WF_PENDING_MSG, msgParams));
            target = TARGET_WORKFLOW_ERROR;
        } else {
            if (PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(modifyRsn) && !fromInbox) {
                final Map<String, BigDecimal> propertyTaxDetails = propService.getCurrentPropertyTaxDetails(basicProp
                        .getActiveProperty());
                final Map<String, BigDecimal> currentTaxAndDue = propertyService.getCurrentTaxAndBalance(propertyTaxDetails,
                        new Date());
                currentPropertyTax = currentTaxAndDue.get(CURR_DMD_STR);
                currentPropertyTaxDue = currentTaxAndDue.get(CURR_BAL_STR);
                arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                        propertyTaxDetails.get(ARR_COLL_STR));
                currentWaterTaxDue = propertyService.getWaterTaxDues(basicProp.getUpicNo());
                if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {
                    setTaxDueErrorMsg(getText(TAXDUES_ERROR_MSG, new String[] { BIFURCATION }));
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
            if (propertyAddr.getHouseNoBldgApt() != null)
                setHouseNo(propertyAddr.getHouseNoBldgApt().toString());
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
            setOldPropertyTypeCode(oldProperty.getPropertyDetail().getPropertyTypeMaster().getCode());
            if (propertyModel.getPropertyDetail().getPropertyUsage() != null)
                propUsageId = propertyModel.getPropertyDetail().getPropertyUsage().getId().toString();
            if (propertyModel.getPropertyDetail().getPropertyOccupation() != null)
                propOccId = propertyModel.getPropertyDetail().getPropertyOccupation().getId().toString();
            setDocNumber(propertyModel.getDocNumber());
            if (!propertyModel.getPropertyDetail().getFloorDetails().isEmpty())
                setFloorDetails(propertyModel);
            if (propertyModel.getPropertyDetail().getVacantLandPlotArea() != null)
                vacantLandPlotAreaId = propertyModel.getPropertyDetail().getVacantLandPlotArea().getId();
            if (propertyModel.getPropertyDetail().getLayoutApprovalAuthority() != null)
                layoutApprovalAuthorityId = propertyModel.getPropertyDetail().getLayoutApprovalAuthority().getId();
            if(StringUtils.isNotBlank(applicationSource))
                target = CITIZEN_NEW;
            else
                target = NEW;
        }
        if (logger.isDebugEnabled())
            logger.debug("populateFormData - target : " + target + "\n Exiting from populateFormData");
        return target;
    }

    /**
     * Returns modify property view screen when modify property inbox item is opened
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-view")
    public String view() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into view, BasicProperty: " + basicProp + ", ModelId: " + getModelId());
        if (getModelId() != null) {
            propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
            if (logger.isDebugEnabled())
                logger.debug("view: PropertyModel by model id: " + propertyModel);
        }
        showTaxCalculateButton();
        final String currWfState = propertyModel.getState().getValue();
        populateFormData(Boolean.TRUE);
        isEligibleForDocEdit();
        corrsAddress = PropertyTaxUtil.getOwnerAddress(propertyModel.getBasicProperty().getPropertyOwnerInfo());
        amalgPropIds = new String[10];
        if (!propertyModel.getPropertyDetail().getFloorDetails().isEmpty())
            setFloorDetails(propertyModel);
        if (!currWfState.endsWith(WF_STATE_COMMISSIONER_APPROVED)) {
            int i = 0;
            for (final PropertyStatusValues propstatval : basicProp.getPropertyStatusValuesSet()) {
                if ("W".equals(propstatval.getIsActive())) {
                    setPropStatValForView(propstatval);
                    if (logger.isDebugEnabled())
                        logger.debug("view: PropertyStatusValues for new modify screen: " + propstatval);
                }
                // setting the amalgamated properties
                if (logger.isDebugEnabled())
                    logger.debug("view: Amalgamated property ids:");
                if (PROP_CREATE_RSN.equals(propstatval.getPropertyStatus().getStatusCode())
                        && "Y".equals(propstatval.getIsActive()))
                    if (propstatval.getReferenceBasicProperty() != null) {
                        amalgPropIds[i] = propstatval.getReferenceBasicProperty().getUpicNo();
                        if (logger.isDebugEnabled())
                            logger.debug(amalgPropIds[i] + ", ");
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
                if ("Y".equals(propstatval.getIsActive())) {
                    setPropStatValForView(propstatval);
                    if (logger.isDebugEnabled())
                        logger.debug("PropertyStatusValues for view modify screen: " + propstatval);
                }
                // setting the amalgamated properties
                if (logger.isDebugEnabled())
                    logger.debug("view: Amalgamated property ids:");
                if (PROP_CREATE_RSN.equals(propstatval.getPropertyStatus().getStatusCode())
                        && "Y".equals(propstatval.getIsActive()))
                    if (propstatval.getReferenceBasicProperty() != null) {
                        amalgPropIds[i] = propstatval.getReferenceBasicProperty().getUpicNo();
                        if (logger.isDebugEnabled())
                            logger.debug(amalgPropIds[i] + ", ");
                        i++;
                    }
            }
        }

        propertyAddr = basicProp.getAddress();
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        setDocNumber(propertyModel.getDocNumber());
        if (logger.isDebugEnabled()) {
            logger.debug("view: ModifyReason: " + getModifyRsn());
            logger.debug("Exiting from view");
        }
        return VIEW;
    }

    private void isEligibleForDocEdit() {
        final String nextAction = propertyModel.getState().getNextAction();
        if (WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING.equals(nextAction)
                || WF_STATE_ASSISTANT_APPROVAL_PENDING.equals(nextAction))
            setAllowEditDocument(Boolean.TRUE);
    }

    /**
     * Modifies and Forwards the assessment to next user when form is submitted in editable mode
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-forward")
    public String forwardModify() {
        if (logger.isDebugEnabled())
            logger.debug("forwardModify: Modify property started " + propertyModel);
        setOldPropertyTypeCode(basicProp.getProperty().getPropertyDetail().getPropertyTypeMaster().getCode());
        validate();
        if (hasErrors() && (StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN) ||
                StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT) ||
                StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT))
                && PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn)) {
            showTaxCalcBtn = Boolean.TRUE;
            allowEditDocument = Boolean.TRUE;
        }
        checkToDisplayAckButton();
        final long startTimeMillis = System.currentTimeMillis();
        isMeesevaUser = propService.isMeesevaUser(securityUtils.getCurrentUser());

        if (isMeesevaUser && getMeesevaApplicationNumber() != null) {
            propertyModel.setApplicationNo(propertyModel.getMeesevaApplicationNumber());
            propertyModel.setSource(PropertyTaxConstants.SOURCE_MEESEVA);
        }
        if (getModelId() != null && !getModelId().trim().isEmpty()) {
            propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));

            if (logger.isDebugEnabled())
                logger.debug("forwardModify: Workflow property: " + propWF);
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
        if (houseNo != null && !houseNo.isEmpty())
            basicProp.getAddress().setHouseNoBldgApt(houseNo);
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId))
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    FROM_PROPERTY_TYPE_MASTER_WHERE_ID, Long.valueOf(propTypeId));
        propertyModel.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        String errorKey = null;
        if (!hasErrors())
            errorKey = propService.validationForBifurcation(propertyModel, basicProp, modifyRsn);
        if (!isBlank(errorKey))
            addActionError(getText(errorKey));

        final PropertyTypeMaster oldPropTypeMstr = oldProperty.getPropertyDetail().getPropertyTypeMaster();
        if (null != propTypeMstr && !propTypeMstr.getType().equals(oldPropTypeMstr.getType()))
            if (propTypeMstr.getType().equals(OWNERSHIP_TYPE_VAC_LAND_STR))
                addActionError(getText("error.nonVacantToVacant"));
        if (hasErrors())
            if (ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()) || isAssistantOrRIApprovalPending()
                    || !propertyByEmployee)
                return NEW;
            else if (isCommissionerRoOrBillCollector())
                return VIEW;
        try {
            modifyBasicProp(getDocNumber());
        } catch (final TaxCalculatorExeption e) {
            addActionError(getText("unitrate.error"));
            logger.error("forwardModify : There are no Unit rates defined for chosen combinations", e);
            return NEW;
        }
        transitionWorkFlow(propertyModel);
        basicProp.setUnderWorkflow(Boolean.TRUE);
        basicPropertyService.applyAuditing(propertyModel.getState());
        propService.updateIndexes(propertyModel, getApplicationType());
        // added to set createdDate for DemandCalculation object
        if (basicProp.getWFProperty() != null && basicProp.getWFProperty().getPtDemandSet() != null
                && !basicProp.getWFProperty().getPtDemandSet().isEmpty())
            for (final Ptdemand ptDemand : basicProp.getWFProperty().getPtDemandSet())
                basicPropertyService.applyAuditing(ptDemand.getDmdCalculations());
        if (!isMeesevaUser)
            basicPropertyService.update(basicProp);
        else {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("ADMISSIONFEE", "0");
            meesevaParams.put("APPLICATIONNUMBER", propertyModel.getMeesevaApplicationNumber());
            basicProp.setSource(PropertyTaxConstants.SOURCEOFDATA_MEESEWA);
            basicProp.getWFProperty().setApplicationNo(propertyModel.getMeesevaApplicationNumber());
            basicPropertyService.updateBasicProperty(basicProp, meesevaParams);
        }
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        prepareAckMsg();
        buildEmailandSms(propertyModel, getApplicationType());
        addActionMessage(getText(PROPERTY_FORWARD_SUCCESS,
                new String[] { propertyModel.getBasicProperty().getUpicNo() }));
        final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
        if (logger.isInfoEnabled())
            logger.info("forwardModify: Modify property forwarded successfully; Time taken(ms) = " + elapsedTimeMillis);
        if (logger.isDebugEnabled())
            logger.debug("forwardModify: Modify property forward ended");
        return isMeesevaUser ? MEESEVA_RESULT_ACK : RESULT_ACK;
    }

    private void checkToDisplayAckButton() {
        final Boolean rejected = wfInitiatorRejected == null ? Boolean.FALSE : wfInitiatorRejected;
        if(ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()))
            showAckBtn = Boolean.TRUE;
        if(!showAckBtn){
            for (final Role role : securityUtils.getCurrentUser().getRoles())
                if (ROLE_ULB_OPERATOR.equalsIgnoreCase(role.getName()) && !rejected && getModel().getState() == null
                || CSC_OPERATOR_ROLE.equalsIgnoreCase(role.getName()))
                    showAckBtn = Boolean.TRUE;
        }
    }

    @Override
    public String getApplicationType() {
        return PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) ? APPLICATION_TYPE_ALTER_ASSESSENT
                : PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn) ? APPLICATION_TYPE_BIFURCATE_ASSESSENT
                        : APPLICATION_TYPE_GRP;
    }

    /**
     * Modifies and Forwards the assessment to next user when form is submitted in read only mode
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-forwardView")
    public String forwardView() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into forwardView");
        validateApproverDetails();
        if (hasErrors())
            if (isAssistantOrRIApprovalPending())
                return NEW;
            else if (isCommissionerRoOrBillCollector())
                return VIEW;
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        if (logger.isDebugEnabled())
            logger.debug("forwardView: Workflow property: " + propertyModel);
        transitionWorkFlow(propertyModel);
        propService.updateIndexes(propertyModel, getApplicationType());
        basicPropertyService.update(basicProp);
        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        prepareAckMsg();
        buildEmailandSms(propertyModel, APPLICATION_TYPE_ALTER_ASSESSENT);
        addActionMessage(getText(PROPERTY_FORWARD_SUCCESS,
                new String[] { propertyModel.getBasicProperty().getUpicNo() }));
        if (logger.isDebugEnabled())
            logger.debug("Exiting from forwardView");
        return RESULT_ACK;
    }

    /**
     * Approves the assessment when form is approved by the final approver
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-approve")
    public String approve() {
        if (logger.isDebugEnabled())
            logger.debug("Enter method approve");
        amalgPropIds = new String[10];
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        if (logger.isDebugEnabled())
            logger.debug("approve: Workflow property: " + propertyModel);
        basicProp = propertyModel.getBasicProperty();
        oldProperty = (PropertyImpl) basicProp.getProperty();
        transitionWorkFlow(propertyModel);
        createPropertyStatusValues();

        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) || PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)
                || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)) {
            // createVoucher(); // Creates voucher
        }

        setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());

        /**
         * The old property will be made history and the workflow property will be made active only when all the changes are
         * completed in case of modify reason is 'ADD_OR_ALTER' or 'BIFURCATE'
         */
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)
                || PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn)) {
            propertyModel.setStatus(STATUS_ISACTIVE);
            oldProperty.setStatus(STATUS_ISHISTORY);
            final String clientSpecificDmdBill = propertyTaxCommonUtils.getAppConfigValue(APPCONFIG_CLIENT_SPECIFIC_DMD_BILL,
                    PTMODULENAME);
            if ("Y".equalsIgnoreCase(clientSpecificDmdBill))
                propertyTaxCommonUtils.makeExistingDemandBillInactive(basicProp.getUpicNo());
            else
                propertyTaxUtil.makeTheEgBillAsHistory(basicProp);

        }
        processAndStoreDocumentsWithReason(basicProp, getReason(modifyRsn));
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)
                || PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn)
                || PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn))
            updateAddress();
        propService.updateIndexes(propertyModel, getApplicationType());
        basicPropertyService.update(basicProp);
        setBasicProp(basicProp);
        setAckMessage(getText(PROPERTY_MODIFY_APPROVE_SUCCESS, new String[] { getModifyReasonString(),
                propertyModel.getBasicProperty().getUpicNo() }));
        buildEmailandSms(propertyModel, getApplicationType());
        addActionMessage(getText(PROPERTY_MODIFY_APPROVE_SUCCESS, new String[] { getModifyReasonString(),
                propertyModel.getBasicProperty().getUpicNo() }));
        if (logger.isDebugEnabled())
            logger.debug("Exiting approve");
        return RESULT_ACK;
    }

    private void createPropertyStatusValues() {
        Date propCompletionDate;
        final PropertyTypeMaster proptypeMstr = propertyTypeMasterDAO
                .findById(Integer.valueOf(propertyModel.getPropertyDetail().getPropertyTypeMaster().getId().toString()), false);
        if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                    .getFloorDetails());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();

        if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn) || PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn)
                || PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn)) {
            basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp, getModifyRsn(),
                    propCompletionDate, null, null, null, null));
            if (PROPERTY_MODIFY_REASON_AMALG.equals(modifyRsn))
                propService.createAmalgPropStatVal(amalgPropIds, basicProp);
        } else if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn))
            basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp,
                    PROPERTY_MODIFY_REASON_ADD_OR_ALTER, propCompletionDate, null, null, null, null));
        else if (PROPERTY_MODIFY_REASON_COURT_RULE.equals(getModifyRsn()))
            basicProp.addPropertyStatusValues(propService.createPropStatVal(basicProp,
                    PROPERTY_MODIFY_REASON_ADD_OR_ALTER, propCompletionDate, getCourtOrdNum(),
                    propService.getPropOccupatedDate(getOrderDate()), getJudgmtDetails(), null));
    }

    /**
     * Rejects the assessment
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-reject")
    public String reject() {
        if (logger.isDebugEnabled())
            logger.debug("reject: Property rejection started");
        if (isBlank(approverComments)) {
            addActionError(getText("property.workflow.remarks"));
            if (isAssistantOrRIApprovalPending())
                return NEW;
            else if (isCommissionerRoOrBillCollector())
                return VIEW;
        }
        propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                Long.valueOf(getModelId()));
        if (propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propertyModel.getPropertyDetail().getFloorDetails().clear();

        if (logger.isDebugEnabled())
            logger.debug("reject: Property: " + propertyModel);
        final BasicProperty basicProperty = propertyModel.getBasicProperty();
        setBasicProp(basicProperty);
        if (logger.isDebugEnabled())
            logger.debug("reject: BasicProperty: " + basicProperty);

        final User user = securityUtils.getCurrentUser();
        String loggedInUserDesignation = "";
        List<Assignment> loggedInUserAssign;
        if (propertyModel.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    propertyModel.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }
        Assignment wfInitiator;
        if (isRoOrCommissioner(loggedInUserDesignation))
            wfInitiator = propService.getUserOnRejection(propertyModel);
        else
            wfInitiator = propService.getWorkflowInitiator(propertyModel);
        if (wfInitiator != null) {
            transitionWorkFlow(propertyModel);
            propService.updateIndexes(propertyModel, getApplicationType());
            propertyImplService.update(propertyModel);
            setModifyRsn(propertyModel.getPropertyDetail().getPropertyMutationMaster().getCode());
        }
        final String username = getInitiator();

        getAckMsg(username, wfInitiator);
        buildEmailandSms(propertyModel, getApplicationType());
        if (logger.isDebugEnabled()) {
            logger.debug("reject: BasicProperty: " + getBasicProp() + "AckMessage: " + getAckMessage());
            logger.debug("reject: Property rejection ended");
        }
        return RESULT_ACK;
    }

    private boolean isCommissionerRoOrBillCollector() {
        return StringUtils.containsIgnoreCase(userDesignationList, BILL_COLLECTOR_DESGN)
                || StringUtils.containsIgnoreCase(userDesignationList, COMMISSIONER_DESGN)
                || StringUtils.containsIgnoreCase(userDesignationList, REVENUE_OFFICER_DESGN);
    }

    private boolean isRoOrCommissioner(final String loggedInUserDesignation) {
        boolean isany;
        if (!REVENUE_OFFICER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            isany = isCommissioner(loggedInUserDesignation);
        else
            isany = true;
        return isany;
    }

    private boolean isCommissioner(final String loggedInUserDesignation) {
        boolean isanyone;
        if (!ASSISTANT_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || !ADDITIONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = isDeputyOrAbove(loggedInUserDesignation);
        else
            isanyone = true;
        return isanyone;
    }

    private boolean isDeputyOrAbove(final String loggedInUserDesignation) {
        boolean isanyone = false;
        if (DEPUTY_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || ZONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = true;
        return isanyone;
    }

    private void getAckMsg(final String username, final Assignment wfInitiator) {
        if (wfInitiator != null) {
            if (wfInitiator.getEmployee().getUsername().equals(securityUtils.getCurrentUser().getUsername())) {
                wfInitiatorRejected = Boolean.TRUE;
                setAckMessage(getText(PROPERTY_MODIFY_FINAL_REJECT_SUCCESS, new String[] { getModifyReasonString() }));
            } else
                setAckMessage(getText(PROPERTY_MODIFY_REJECT_SUCCESS, new String[] { getModifyReasonString(), username }));
        } else
            setAckMessage(getText(PROPERTY_MODIFY_REJECT_FAILURE));
    }

    private boolean isAssistantOrRIApprovalPending() {
        return StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT)
                || StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT)
                || getModel().getState().getNextAction() != null && getModel().getState().getNextAction()
                        .equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into preapre, ModelId: " + getModelId());
        super.prepare();
        setUserInfo();
        setUserDesignations();
        propertyByEmployee = propService.isEmployee(securityUtils.getCurrentUser());
        if (getModelId() != null && !getModelId().isEmpty()) {
            setBasicProp((BasicProperty) getPersistenceService().find(
                    "select prop.basicProperty from PropertyImpl prop where prop.id=?", Long.valueOf(getModelId())));
            if (logger.isDebugEnabled())
                logger.debug("prepare: BasicProperty: " + basicProp);
            propWF = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID,
                    Long.valueOf(getModelId()));
            if (propWF != null) {
                setProperty(propWF);
                preparePropertyTaxDetails(propWF);
                historyMap = propService.populateHistory(propWF);
            } else {
                preparePropertyTaxDetails(basicProp.getActiveProperty());
                historyMap = propService.populateHistory(basicProp.getActiveProperty());
            }
        } else if (indexNumber != null && !indexNumber.trim().isEmpty()) {
            setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
                    indexNumber));
            preparePropertyTaxDetails(basicProp.getActiveProperty());
        }

        documentTypes = propService.getDocumentTypesForTransactionType(TransactionType.MODIFY);
        final List<FloorType> floorTypes = getPersistenceService().findAllBy("from FloorType order by name");
        final List<RoofType> roofTypes = getPersistenceService().findAllBy("from RoofType order by name");
        final List<WallType> wallTypes = getPersistenceService().findAllBy("from WallType order by name");
        final List<WoodType> woodTypes = getPersistenceService().findAllBy("from WoodType order by name");
        final List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy(
                "from PropertyTypeMaster where type != 'EWSHS' order by orderNo");
        final List<String> structureList = getPersistenceService().findAllBy("from StructureClassification");
        List<PropertyUsage> usageList = getPersistenceService().findAllBy("from PropertyUsage order by usageName");
        final List<PropertyOccupation> propOccList = getPersistenceService().findAllBy("from PropertyOccupation");
        final List<String> ageFacList = getPersistenceService().findAllBy("from DepreciationMaster");
        final List<String> apartmentsList = getPersistenceService().findAllBy("from Apartment order by name");
        final List<String> taxExemptionReasonList = getPersistenceService().findAllBy(
                "from TaxExemptionReason where isActive = true order by name");
        setFloorNoMap(FLOOR_MAP);
        addDropdownData("floorType", floorTypes);
        addDropdownData("roofType", roofTypes);
        addDropdownData("wallType", wallTypes);
        addDropdownData("woodType", woodTypes);
        addDropdownData("PropTypeMaster", propTypeList);
        addDropdownData("OccupancyList", propOccList);
        addDropdownData("StructureList", structureList);
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
                if ("W".equals(propstatval.getIsActive()))
                    setPropStatValForView(propstatval);
        }

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
        setVacantLandPlotAreaList(vacantLandPlotAreaRepository.findAll());
        setLayoutApprovalAuthorityList(layoutApprovalAuthorityRepository.findAll());
        addDropdownData("vacantLandPlotAreaList", vacantLandPlotAreaList);
        addDropdownData("layoutApprovalAuthorityList", layoutApprovalAuthorityList);
        if (logger.isDebugEnabled())
            logger.debug("Exiting from preapre, ModelId: " + getModelId());
    }

    /**
     * Populates Property type categories based on the property type
     */
    private void populatePropertyTypeCategory() {
        if (propTypeId != null && !propTypeId.trim().isEmpty() && !"-1".equals(propTypeId))
            propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                    FROM_PROPERTY_TYPE_MASTER_WHERE_ID, Long.valueOf(propTypeId));
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

    /**
     * Modifies basic property information
     *
     * @param docNumber
     * @throws TaxCalculatorExeption
     */
    private void modifyBasicProp(final String docNumber) throws TaxCalculatorExeption {
        if (logger.isDebugEnabled()) {
            logger.debug("Entered into modifyBasicProp, BasicProperty: " + basicProp);
            logger.debug("modifyBasicProp: PropTypeId: " + propTypeId + ", PropUsageId: " + propUsageId + ", PropOccId: "
                    + propOccId + ", statusModifyRsn: " + modifyRsn + ", NoOfAmalgmatedProps: "
                    + (amalgPropIds != null ? amalgPropIds.length : "NULL"));
        }
        Date propCompletionDate;
        if(SOURCE_ONLINE.equalsIgnoreCase(applicationSource) && ApplicationThreadLocals.getUserId() == null) 
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        final Character status = STATUS_WORKFLOW;
        final PropertyTypeMaster proptypeMstr = propertyTypeMasterDAO.findById(Integer.valueOf(propTypeId), false);
        if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                    .getFloorDetailsProxy());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
        final PropertyMutationMaster propMutMstr = (PropertyMutationMaster) propService.getPropPerServ().find(
                "from PropertyMutationMaster PM where upper(PM.code) = ?", modifyRsn);
        basicProp.setPropertyMutationMaster(propMutMstr);
        basicProp.setPropOccupationDate(propCompletionDate);

        setProperty(propService.createProperty(propertyModel, getAreaOfPlot(), modifyRsn, propTypeId, propUsageId,
                propOccId, status, propertyModel.getDocNumber(), null, floorTypeId, roofTypeId, wallTypeId, woodTypeId,
                taxExemptedReason, propertyDepartmentId, vacantLandPlotAreaId, layoutApprovalAuthorityId, Boolean.FALSE));
        updatePropertyID(basicProp);
        propertyModel.setPropertyModifyReason(modifyRsn);
        propertyModel.setBasicProperty(basicProp);
        propertyModel.setEffectiveDate(propCompletionDate);
        final Long oldPropTypeId = oldProperty.getPropertyDetail().getPropertyTypeMaster().getId();
        final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                "from PropertyTypeMaster ptm where ptm.code = ?", OWNERSHIP_TYPE_VAC_LAND);
        /*
         * if modifying from OPEN_PLOT to OTHERS or from OTHERS to OPEN_PLOT property type
         */
        if ((oldPropTypeId == propTypeMstr.getId() && Long.parseLong(propTypeId) != propTypeMstr.getId()
                || oldPropTypeId != propTypeMstr
                        .getId() && Long.parseLong(propTypeId) == propTypeMstr.getId())
                && !propertyModel.getStatus().equals('W'))
            if (propTypeMstr != null
                    && org.apache.commons.lang.StringUtils.equals(propTypeMstr.getId().toString(), propTypeId))
                changePropertyDetail(propertyModel, new VacantProperty(), 0);
            else
                changePropertyDetail(propertyModel, new BuiltUpProperty(), propertyModel.getPropertyDetail()
                        .getNoofFloors());
        if (propertyModel.getPropertyDetail().getLayoutApprovalAuthority() != null
                && "No Approval".equals(propertyModel.getPropertyDetail().getLayoutApprovalAuthority().getName())) {
            propertyModel.getPropertyDetail().setLayoutPermitNo(null);
            propertyModel.getPropertyDetail().setLayoutPermitDate(null);
        }

        Property modProperty = null;
        try {
            modProperty = propService.modifyDemand(propertyModel, oldProperty);
            if (PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn))
                propService.calculateGrpPenalty(modProperty, propCompletionDate);
        } catch (final TaxCalculatorExeption e) {
            throw new TaxCalculatorExeption();
        }

        if (modProperty != null && !modProperty.getDocuments().isEmpty())
            propService.processAndStoreDocument(modProperty.getDocuments());

        if (modProperty == null)
            basicProp.addProperty(propertyModel);
        else
            basicProp.addProperty(modProperty);

        if (logger.isDebugEnabled())
            logger.debug("Exiting modifyBasicProp");
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

    /**
     * Changes the property details to {@link BuiltUpProperty} or {@link VacantProperty}
     *
     * @param modProperty the property which is getting modified
     * @param propDetail the {@link PropertyDetail} type, either {@link BuiltUpProperty} or {@link VacantProperty}
     * @param numOfFloors the no. of floors which is dependent on {@link PropertyDetail}
     * @see {@link PropertyDetail}, {@link BuiltUpProperty}, {@link VacantProperty}
     */

    private void changePropertyDetail(final Property modProperty, final PropertyDetail propDetail,
            final Integer numOfFloors) {
        if (logger.isDebugEnabled())
            logger.debug("Entered into changePropertyDetail, Property is Vacant Land");

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
        propDetail.setNoofFloors(numOfFloors);
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
        // propDetail.setStructure(propertyDetail.isStructure());
        propDetail.setExtentAppartenauntLand(propertyDetail.getExtentAppartenauntLand());
        if (numOfFloors == 0)
            propDetail.setPropertyUsage(propertyDetail.getPropertyUsage());
        else
            propDetail.setPropertyUsage(null);
        propDetail.setManualAlv(propertyDetail.getManualAlv());
        propDetail.setOccupierName(propertyDetail.getOccupierName());
        propDetail.setLayoutPermitNo(propertyDetail.getLayoutPermitNo());
        propDetail.setLayoutPermitDate(propertyDetail.getLayoutPermitDate());

        modProperty.setPropertyDetail(propDetail);
        if (logger.isDebugEnabled())
            logger.debug("Exiting from changePropertyDetail");
    }

    /**
     * Populates Basic Property based on either the index number or model id
     */
    private void populateBasicProp() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into populateBasicProp");
        if (basicProp == null)
            if (indexNumber != null && !indexNumber.trim().isEmpty())
                setBasicProp((BasicProperty) getPersistenceService().findByNamedQuery(QUERY_BASICPROPERTY_BY_UPICNO,
                        indexNumber));
            else if (getModelId() != null && !getModelId().equals(""))
                setBasicProp(((PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
                        Long.valueOf(getModelId()))).getBasicProperty());
        if (logger.isDebugEnabled())
            logger.debug("Exiting from populateBasicProp");
    }

    /**
     * Populates the floor details proxy by getting floor details from the property detail
     *
     * @param property
     */
    private void setFloorDetails(final Property property) {
        if (logger.isDebugEnabled())
            logger.debug("Entered into setFloorDetails, Property: " + property);

        final List<Floor> floors = property.getPropertyDetail().getFloorDetails();
        property.getPropertyDetail().setFloorDetailsProxy(floors);
        int i = 0;
        for (final Floor flr : floors) {
            floorNoStr[i] = FLOOR_MAP.get(flr.getFloorNo());
            i++;
        }

        if (logger.isDebugEnabled())
            logger.debug("Exiting from setFloorDetails: ");
    }

    /**
     * Returns Floor details list
     *
     * @return
     */
    public List<Floor> getFloorDetails() {
        return new ArrayList<>(propertyModel.getPropertyDetail().getFloorDetails());
    }

    /**
     * Validates form data
     */
    @Override
    public void validate() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into validate, ModifyRsn: " + modifyRsn);
        propertyModel.setBasicProperty(basicProp);
        Date propCompletionDate = null;
        if (!propTypeMstr.getType().equals(OWNERSHIP_TYPE_VAC_LAND_STR) && oldPropertyTypeCode.equals(OWNERSHIP_TYPE_VAC_LAND)
                && modifyRsn.equals(PROPERTY_MODIFY_REASON_ADD_OR_ALTER))
            if (houseNo == null || houseNo.isEmpty())
                addActionError(getText("mandatory.doorNo"));
        if (basicProp.getSource() == SOURCEOFDATA_MIGRATION || basicProp.getSource() == SOURCEOFDATA_DATAENTRY) {
            setOldProperty((PropertyImpl) getBasicProp().getProperty());
            propCompletionDate = propertyTaxUtil.getLowestInstallmentForProperty(oldProperty);
        }
        validateProperty(propertyModel, areaOfPlot, dateOfCompletion, eastBoundary, westBoundary, southBoundary,
                northBoundary, propTypeId,
                null != basicProp.getPropertyID() ? String.valueOf(basicProp.getPropertyID().getZone().getId()) : "",
                propOccId, floorTypeId, roofTypeId, wallTypeId, woodTypeId, modifyRsn, propCompletionDate, vacantLandPlotAreaId,
                layoutApprovalAuthorityId, null);
        validateApproverDetails();
        if (!propertyByEmployee && null != basicProp) {
            final Assignment assignment = propService.isCscOperator(securityUtils.getCurrentUser())
                    ? propService.getAssignmentByDeptDesigElecWard(basicProp)
                    : null;
            if (assignment == null && propService.getUserPositionByZone(basicProp, false) == null)
                addActionError(getText("notexists.position"));
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting from validate, BasicProperty: " + getBasicProp());
    }

    /**
     * Populates property status values to display in a view page
     *
     * @param propstatval
     */
    private void setPropStatValForView(final PropertyStatusValues propstatval) {
        if (logger.isDebugEnabled())
            logger.debug("Entered into setPropStatValForView " + propstatval);
        final PropertyImpl propertyImpl = propWF != null ? propWF : propertyModel;
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(propstatval.getPropertyStatus().getStatusCode()))
            // setting the court rule details
            if (PROPERTY_MODIFY_REASON_COURT_RULE.equals(propertyImpl.getPropertyDetail().getPropertyMutationMaster()
                    .getCode())) {
            setCourtOrdNum(propstatval.getReferenceNo());
            setOrderDate(dateFormatter.format(propstatval.getReferenceDate()));
            setJudgmtDetails(propstatval.getRemarks());
            }
        if (propertyImpl.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            setDateOfCompletion(propstatval.getExtraField1());

        if (logger.isDebugEnabled())
            logger.debug("Entered into setPropStatValForView");
    }

    /**
     * Prepares acknowledgement message
     */
    private void prepareAckMsg() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into prepareAckMsg, ModifyRsn: " + modifyRsn);
        final String userName = propertyTaxUtil.getApproverUserName(approverPositionId);
        final String action = getModifyReasonString();
        setAckMessage(getText("property.modify.forward.success",
                new String[] { action, userName,
                        propertyModel.getApplicationNo() }));

        if (logger.isDebugEnabled())
            logger.debug("AckMessage: " + getAckMessage() + "\nExiting from prepareAckMsg");
    }

    /**
     * Return reason for modification
     *
     * @return
     */
    private String getModifyReasonString() {
        final String action = PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn) ? ALTERATION_OF_ASSESSMENT
                : PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn) ? BIFURCATION_OF_ASSESSMENT
                        : PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION.equals(modifyRsn) ? GRP_OF_ASSESSMENT
                                : AMALGAMATION_OF_ASSESSMENT;
        return action;
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
        if (logger.isDebugEnabled()) {
            logger.debug("Entered into modifyPropertyForMigratedProp");
            logger.debug("modifyPropertyForMigratedProp: Property: " + property + ", areaOfPlot: " + areaOfPlot
                    + ", mutationCode: " + mutationCode + ",propTypeId: " + propTypeId + ", propUsageId: " + propUsageId
                    + ", propOccId: " + propOccId);
        }
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
        property.getPropertyDetail().setNoofFloors(property.getPropertyDetail().getFloorDetails().size());
        property.setDocNumber(docnumber);
        if (logger.isDebugEnabled())
            logger.debug("Exiting from createProperty");
        return property;
    }

    private void updateBasicPropForMigratedProp(final String docNumber, PropertyImpl existingProp) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entered into modifyBasicPropForMigratedProp, BasicProperty: " + basicProp);
            logger.debug("modifyBasicPropForMigratedProp: PropTypeId: " + propTypeId + ", PropUsageId: " + propUsageId
                    + ", PropOccId: " + propOccId + ", statusModifyRsn: " + modifyRsn);
        }
        Date propCompletionDate;
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
        if (logger.isDebugEnabled())
            logger.debug("Exiting modifyBasicPropForMigratedProp");
    }

    @ValidationErrorPage(value = "new")
    public String updateData() {
        if (logger.isDebugEnabled())
            logger.debug("updateData: Property modification started for Migrated Property, PropertyId: " + propertyModel);
        final long startTimeMillis = System.currentTimeMillis();

        if (ASSISTANT_ROLE.equals(userRole)) {

            final PropertyImpl nonHistoryProperty = (PropertyImpl) basicProp.getProperty();
            processAndStoreDocumentsWithReason(basicProp, getReason(modifyRsn));
            updateBasicPropForMigratedProp(getDocNumber(), nonHistoryProperty);
            setAckMessage("Migrated Property updated Successfully in System with Index Number: ");

            final long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
            if (logger.isInfoEnabled())
                logger.info("updateData: Property modified successfully in system with Index Number: "
                        + basicProp.getUpicNo() + "; Time taken(ms) = " + elapsedTimeMillis);
        }

        return RESULT_ACK;
    }

    @SkipValidation
    public String modifyOrDataUpdateForm() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into modifyOrDataUpdateForm");
        String resultPage = "";
        if (PROPERTY_MODIFY_REASON_DATA_UPDATE.equals(modifyRsn)
                && basicProp.getSource().equals(PropertyTaxConstants.SOURCEOFDATA_APPLICATION)) {
            setErrorMessage(" This is not a migrated property ");
            resultPage = RESULT_ERROR;
        } else
            resultPage = populateFormData(Boolean.FALSE);

        if (logger.isDebugEnabled())
            logger.debug("Exiting from modifyOrDataUpdateForm");
        return resultPage;
    }

    private void updateAddress() {
        if (logger.isDebugEnabled())
            logger.debug("Entered into updateAddress");

        final PropertyAddress addr = basicProp.getAddress();
        if (propertyAddr != null) {
            addr.setHouseNoBldgApt(propertyAddr.getHouseNoBldgApt());
            addr.setLandmark(propertyAddr.getLandmark());
            addr.setPinCode(propertyAddr.getPinCode());
        }
        if (logger.isDebugEnabled())
            logger.debug("Exiting from updateAddress");
    }

    /**
     * Prints acknowledgement page
     *
     * @return
     */
    @SkipValidation
    @Action(value = "/modifyProperty-printAck")
    public String printAck() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String imagePath = url.concat(PropertyTaxConstants.IMAGE_CONTEXT_PATH).concat(
                (String) request.getSession().getAttribute("citylogo"));
        final String cityName = request.getSession().getAttribute("citymunicipalityname").toString();
        if(ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()) && ApplicationThreadLocals.getUserId() == null) {
            ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
            setApplicationSource(propertyModel.getSource().toLowerCase());
        }
        final Map<String, Object> reportParams = new HashMap<String, Object>();
        reportParams.put("ownerName", basicProp.getFullOwnerName());
        reportParams.put("applicationDate", new SimpleDateFormat("dd/MM/yyyy").format(basicProp.getCreatedDate()));
        reportParams.put("ownerAddress", basicProp.getAddress().toString());
        reportParams.put("applicationNo", propertyModel.getApplicationNo());
        reportParams.put("approvedDate", new SimpleDateFormat("dd/MM/yyyy").format(propWF.getState().getCreatedDate()));
        final Date noticeDueDate = DateUtils.add(propWF.getState().getCreatedDate(), Calendar.DAY_OF_MONTH, 15);
        reportParams.put("noticeDueDate", new SimpleDateFormat("dd/MM/yyyy").format(noticeDueDate));
        reportParams.put("creationReason", modifyRsn);
        reportParams.put("logoPath", imagePath);
        reportParams.put("cityName", cityName);
        reportParams.put("loggedInUsername", securityUtils.getCurrentUser().getName());
        ReportRequest reportInput;
        if (modifyRsn.equals(PROPERTY_MODIFY_REASON_GENERAL_REVISION_PETITION)) {
            reportParams.put("noOfDays",
                    ptaxApplicationTypeService.findByNamedQuery(PtApplicationType.BY_CODE, "REVISION_PETETION")
                            .getResolutionTime().toString());
            reportInput = new ReportRequest(GRP_ACK_TEMPLATE, reportParams, reportParams);
        } else
            reportInput = new ReportRequest(MODIFY_ACK_TEMPLATE, reportParams, reportParams);
        reportInput.setReportFormat(FileFormat.PDF);
        final ReportOutput reportOutput = reportService.createReport(reportInput);
        reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        return PRINT_ACK;
    }

    @SkipValidation
    @Action(value = "/modifyProperty-calculateTax")
    public void calculateTax() {
        if (logger.isDebugEnabled())
            logger.debug("entering calculateTax()");
        setOldPropertyTypeCode(basicProp.getProperty().getPropertyDetail().getPropertyTypeMaster().getCode());
        validate();
        if (hasErrors())
            try {
                ServletActionContext.getResponse().getWriter().write(getText("enter.mandatory.fields"));
            } catch (final IOException e) {
                logger.error("calculateTax() : User has not entered all the mandatory fields", e);
            }
        else {
            if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
                final Area area = new Area();
                area.setArea(new Float(areaOfPlot));
                propertyModel.getPropertyDetail().setSitalArea(area);
            }
            if (propTypeId != null && !propTypeId.trim().isEmpty() && !propTypeId.equals("-1"))
                propTypeMstr = (PropertyTypeMaster) getPersistenceService().find(
                        FROM_PROPERTY_TYPE_MASTER_WHERE_ID, Long.valueOf(propTypeId));
            propertyModel.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
            Date propCompletionDate;
            final PropertyTypeMaster proptypeMstr = propertyTypeMasterDAO.findById(Integer.valueOf(propTypeId), false);
            if (!proptypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                propCompletionDate = propService.getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail()
                        .getFloorDetailsProxy());
            else
                propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
            setProperty(propService.createProperty(propertyModel, getAreaOfPlot(), modifyRsn, propTypeId, propUsageId,
                    propOccId, STATUS_WORKFLOW, propertyModel.getDocNumber(), null, floorTypeId, roofTypeId, wallTypeId,
                    woodTypeId, taxExemptedReason, propertyDepartmentId, vacantLandPlotAreaId,
                    layoutApprovalAuthorityId, Boolean.FALSE));
            updatePropertyID(basicProp);
            final Long oldPropTypeId = oldProperty.getPropertyDetail().getPropertyTypeMaster().getId();
            if ((oldPropTypeId == propTypeMstr.getId() && Long.parseLong(propTypeId) != propTypeMstr.getId()
                    || oldPropTypeId != propTypeMstr
                            .getId() && Long.parseLong(propTypeId) == propTypeMstr.getId())
                    && !propertyModel.getStatus().equals('W'))
                if (propTypeMstr != null
                        && org.apache.commons.lang.StringUtils.equals(propTypeMstr.getId().toString(), propTypeId))
                    changePropertyDetail(propertyModel, new VacantProperty(), 0);
                else
                    changePropertyDetail(propertyModel, new BuiltUpProperty(), propertyModel.getPropertyDetail()
                            .getNoofFloors());
            HashMap<Installment, TaxCalculationInfo> instTaxMap = new HashMap<Installment, TaxCalculationInfo>();
            try {
                instTaxMap = taxCalculator.calculatePropertyTax(propertyModel, propCompletionDate);
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
        if (logger.isDebugEnabled())
            logger.debug("exiting calculateTax()");
    }

    public void showTaxCalculateButton() {
        if ((StringUtils.containsIgnoreCase(userDesignationList, REVENUE_INSPECTOR_DESGN) ||
                StringUtils.containsIgnoreCase(userDesignationList, JUNIOR_ASSISTANT) ||
                StringUtils.containsIgnoreCase(userDesignationList, SENIOR_ASSISTANT))
                && PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn))
            showTaxCalcBtn = Boolean.TRUE;
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

    public void setTaxExemptedReason(final Long taxExemptedReason) {
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

    public SortedMap<Integer, String> getFloorNoMap() {
        return floorNoMap;
    }

    public void setFloorNoMap(final SortedMap<Integer, String> floorNoMap) {
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

    public void setPropertyTypeMasterDAO(final PropertyTypeMasterDAO propertyTypeMasterDAO) {
        this.propertyTypeMasterDAO = propertyTypeMasterDAO;
    }

    public void setbasicPropertyService(final PropertyPersistenceService basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    public void setSecurityUtils(final SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
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
        String addittionalRule;
        if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(modifyRsn))
            addittionalRule = ADDTIONAL_RULE_ALTER_ASSESSMENT;
        else if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(modifyRsn))
            addittionalRule = ADDTIONAL_RULE_BIFURCATE_ASSESSMENT;
        else
            addittionalRule = GENERAL_REVISION_PETITION;
        return addittionalRule;
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

    public Boolean getWfInitiatorRejected() {
        return wfInitiatorRejected;
    }

    public void setWfInitiatorRejected(final Boolean wfInitiatorRejected) {
        this.wfInitiatorRejected = wfInitiatorRejected;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(final String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public Map<String, BigDecimal> getPropertyTaxDetailsMap() {
        return propertyTaxDetailsMap;
    }

    public void setPropertyTaxDetailsMap(final Map<String, BigDecimal> propertyTaxDetailsMap) {
        this.propertyTaxDetailsMap = propertyTaxDetailsMap;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(final String houseNo) {
        this.houseNo = houseNo;
    }

    public String getOldPropertyTypeCode() {
        return oldPropertyTypeCode;
    }

    public void setOldPropertyTypeCode(final String oldPropertyTypeCode) {
        this.oldPropertyTypeCode = oldPropertyTypeCode;
    }

    public String getMeesevaApplicationNumber() {
        return meesevaApplicationNumber;
    }

    public void setMeesevaApplicationNumber(final String meesevaApplicationNumber) {
        this.meesevaApplicationNumber = meesevaApplicationNumber;
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

    public boolean isAllowEditDocument() {
        return allowEditDocument;
    }

    public void setAllowEditDocument(final boolean allowEditDocument) {
        this.allowEditDocument = allowEditDocument;
    }

    public Boolean getShowAckBtn() {
        return showAckBtn;
    }

    public void setShowAckBtn(final Boolean showAckBtn) {
        this.showAckBtn = showAckBtn;
    }

    public String getApplicationSource() {
        return applicationSource;
    }

    public void setApplicationSource(String applicationSource) {
        this.applicationSource = applicationSource;
    }
}
