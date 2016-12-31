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
package org.egov.ptis.domain.service.property;

import static java.lang.Boolean.FALSE;
import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.MoneyUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.calculator.APTaxCalculationInfo;
import org.egov.ptis.client.service.PenaltyCalculationService;
import org.egov.ptis.client.service.calculator.APTaxCalculator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyStatusValuesDAO;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.Apartment;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.FloorType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyOccupation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertySource;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.PropertyUsage;
import org.egov.ptis.domain.entity.property.RoofType;
import org.egov.ptis.domain.entity.property.StructureClassification;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionApproval;
import org.egov.ptis.domain.entity.property.WallType;
import org.egov.ptis.domain.entity.property.WoodType;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.BoundaryDetails;
import org.egov.ptis.domain.model.OwnerName;
import org.egov.ptis.domain.model.PropertyDetails;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.ArrayUtils;
import org.egov.infra.exception.ApplicationRuntimeException;

/**
 * Service class to perform services related to an Assessment
 *
 * @author subhash
 */
@Transactional(readOnly = true)
public class PropertyService {
    private static final Logger LOGGER = Logger.getLogger(PropertyService.class);
    public static final String APPLICATION_VIEW_URL = "/ptis/view/viewProperty-viewForm.action?applicationNo=%s&applicationType=%s";
    private static final String PROPERTY_WORKFLOW_STARTED = "Property Workflow Started";
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressWarnings("rawtypes")
    private PersistenceService propPerServ;
    private Installment currentInstall;
    private BigDecimal totalAlv = BigDecimal.ZERO;
    protected PersistenceService<BasicProperty, Long> basicPropertyService;
    private final Map<Installment, Set<EgDemandDetails>> demandDetails = new HashMap<Installment, Set<EgDemandDetails>>();
    private Map<Installment, Map<String, BigDecimal>> excessCollAmtMap = new LinkedHashMap<Installment, Map<String, BigDecimal>>();

    @Autowired
    private APTaxCalculator taxCalculator;
    private HashMap<Installment, TaxCalculationInfo> instTaxMap;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    protected EisCommonsService eisCommonsService;
    @Autowired
    private ModuleService moduleDao;
    @SuppressWarnings("rawtypes")
    @Autowired
    private InstallmentHibDao installmentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;
    @Autowired
    @Qualifier("documentTypePersistenceService")
    private PersistenceService<DocumentType, Long> documentTypePersistenceService;
    @Autowired
    @Qualifier("fileStoreService")
    private FileStoreService fileStoreService;
    @Autowired
    private ApplicationIndexService applicationIndexService;
    @Autowired
    private SimpleRestClient simpleRestClient;
    @Autowired
    private PtDemandDao ptDemandDAO;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PropertyStatusValuesDAO propertyStatusValuesDAO;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PenaltyCalculationService penaltyCalculationService;
    @Autowired
    private PTBillServiceImpl ptBillServiceImpl;
    @Autowired
    private EisCommonService eisCommonService;

    /**
     * Creates a new property if property is in transient state else updates persisted property
     *
     * @param property
     * @param areaOfPlot
     * @param mutationCode
     * @param propTypeId
     * @param propUsageId
     * @param propOccId
     * @param status
     * @param docnumber
     * @param nonResPlotArea
     * @param floorTypeId
     * @param roofTypeId
     * @param wallTypeId
     * @param woodTypeId
     * @param taxExemptId
     * @return Created or Updated property
     */
    public PropertyImpl createProperty(final PropertyImpl property, final String areaOfPlot, final String mutationCode,
            final String propTypeId, final String propUsageId, final String propOccId, final Character status,
            final String docnumber, final String nonResPlotArea, final Long floorTypeId, final Long roofTypeId,
            final Long wallTypeId, final Long woodTypeId, final Long taxExemptId) {
        LOGGER.debug("Entered into createProperty");
        LOGGER.debug("createProperty: Property: " + property + ", areaOfPlot: " + areaOfPlot + ", mutationCode: "
                + mutationCode + ",propTypeId: " + propTypeId + ", propUsageId: " + propUsageId + ", propOccId: "
                + propOccId + ", status: " + status);
        currentInstall = (Installment) getPropPerServ().find(
                "from Installment I where I.module.name=? and (I.fromDate <= ? and I.toDate >= ?) ", PTMODULENAME,
                new Date(), new Date());
        final PropertySource propertySource = (PropertySource) getPropPerServ().find(
                "from PropertySource where propSrcCode = ?", PROP_SOURCE);
        if (floorTypeId != null && floorTypeId != -1) {
            final FloorType floorType = (FloorType) getPropPerServ().find("From FloorType where id = ?", floorTypeId);
            property.getPropertyDetail().setFloorType(floorType);
        } else
            property.getPropertyDetail().setFloorType(null);
        if (roofTypeId != null && roofTypeId != -1) {
            final RoofType roofType = (RoofType) getPropPerServ().find("From RoofType where id = ?", roofTypeId);
            property.getPropertyDetail().setRoofType(roofType);
        } else
            property.getPropertyDetail().setRoofType(null);
        if (wallTypeId != null && wallTypeId != -1) {
            final WallType wallType = (WallType) getPropPerServ().find("From WallType where id = ?", wallTypeId);
            property.getPropertyDetail().setWallType(wallType);
        } else
            property.getPropertyDetail().setWallType(null);
        if (woodTypeId != null && woodTypeId != -1) {
            final WoodType woodType = (WoodType) getPropPerServ().find("From WoodType where id = ?", woodTypeId);
            property.getPropertyDetail().setWoodType(woodType);
        } else
            property.getPropertyDetail().setWoodType(null);
        if (taxExemptId != null && taxExemptId != -1) {
            final TaxExemptionReason taxExemptionReason = (TaxExemptionReason) getPropPerServ().find(
                    "From TaxExemptionReason where id = ?", taxExemptId);
            property.setTaxExemptedReason(taxExemptionReason);
            property.setIsExemptedFromTax(Boolean.TRUE);
        }

        if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
            final Area area = new Area();
            area.setArea(new Float(areaOfPlot));
            property.getPropertyDetail().setSitalArea(area);
        }

        if (property.getPropertyDetail().getApartment() != null
                && property.getPropertyDetail().getApartment().getId() != null) {
            final Apartment apartment = (Apartment) getPropPerServ().find("From Apartment where id = ?",
                    property.getPropertyDetail().getApartment().getId());
            property.getPropertyDetail().setApartment(apartment);
        } else
            property.getPropertyDetail().setApartment(null);

        if (nonResPlotArea != null && !nonResPlotArea.isEmpty()) {
            final Area area = new Area();
            area.setArea(new Float(nonResPlotArea));
            property.getPropertyDetail().setNonResPlotArea(area);
        }

        property.getPropertyDetail().setFieldVerified('Y');
        property.getPropertyDetail().setProperty(property);
        final PropertyMutationMaster propMutMstr = (PropertyMutationMaster) getPropPerServ().find(
                "from PropertyMutationMaster PM where upper(PM.code) = ?", mutationCode);
        final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPropPerServ().find(
                "from PropertyTypeMaster PTM where PTM.id = ?", Long.valueOf(propTypeId));
        if (propUsageId != null) {
            final PropertyUsage usage = (PropertyUsage) getPropPerServ().find("from PropertyUsage pu where pu.id = ?",
                    Long.valueOf(propUsageId));
            property.getPropertyDetail().setPropertyUsage(usage);
        } else
            property.getPropertyDetail().setPropertyUsage(null);
        if (propOccId != null) {
            final PropertyOccupation occupancy = (PropertyOccupation) getPropPerServ().find(
                    "from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
            property.getPropertyDetail().setPropertyOccupation(occupancy);
        } else
            property.getPropertyDetail().setPropertyOccupation(null);
        if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
            property.getPropertyDetail().setPropertyType(VACANT_PROPERTY);
        else
            property.getPropertyDetail().setPropertyType(BUILT_UP_PROPERTY);

        property.getPropertyDetail().setPropertyTypeMaster(propTypeMstr);
        property.getPropertyDetail().setPropertyMutationMaster(propMutMstr);
        property.getPropertyDetail().setUpdatedTime(new Date());
        createFloors(property, mutationCode, propUsageId, propOccId);
        property.setStatus(status);
        property.setIsDefaultProperty(PROPERTY_IS_DEFAULT);
        property.setInstallment(currentInstall);
        property.setEffectiveDate(currentInstall.getFromDate());
        property.setPropertySource(propertySource);
        property.setDocNumber(docnumber);
        // TODO move this code out side this api as this dont have to be called
        // every time we create property
        if (property.getApplicationNo() == null)
            property.setApplicationNo(applicationNumberGenerator.generate());
        LOGGER.debug("Exiting from createProperty");
        return property;
    }

    /**
     * Creates floors for a property by getting list of floors from the property details proxy, by removing the existing floors
     * from property detail if any
     *
     * @param property
     * @param mutationCode
     * @param propUsageId
     * @param propOccId
     */
    public void createFloors(final Property property, final String mutationCode, final String propUsageId,
            final String propOccId) {
        LOGGER.debug("Entered into createFloors");
        LOGGER.debug("createFloors: Property: " + property + ", mutationCode: " + mutationCode + ", propUsageId: "
                + propUsageId + ", propOccId: " + propOccId);

        final Area totBltUpArea = new Area();
        Float totBltUpAreaVal = new Float(0);
        if (!property.getPropertyDetail().getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            property.getPropertyDetail().getFloorDetails().clear();
            int floorUid = 1;
            for (final Floor floor : property.getPropertyDetail().getFloorDetailsProxy())
                if (floor != null) {
                    totBltUpAreaVal = totBltUpAreaVal + floor.getBuiltUpArea().getArea();
                    PropertyTypeMaster unitType = null;
                    PropertyUsage usage = null;
                    PropertyOccupation occupancy = null;
                    if (floor.getUnitType() != null)
                        unitType = (PropertyTypeMaster) getPropPerServ().find(
                                "from PropertyTypeMaster utype where utype.id = ?", floor.getUnitType().getId());
                    if (floor.getPropertyUsage() != null)
                        usage = (PropertyUsage) getPropPerServ().find("from PropertyUsage pu where pu.id = ?",
                                floor.getPropertyUsage().getId());
                    if (floor.getPropertyOccupation() != null)
                        occupancy = (PropertyOccupation) getPropPerServ().find(
                                "from PropertyOccupation po where po.id = ?", floor.getPropertyOccupation().getId());

                    StructureClassification structureClass = null;

                    if (floor.getStructureClassification() != null)
                        structureClass = (StructureClassification) getPropPerServ().find(
                                "from StructureClassification sc where sc.id = ?",
                                floor.getStructureClassification().getId());

                    if (floor.getOccupancyDate() != null && floor.getConstructionDate() != null){
                    	floor.setDepreciationMaster(propertyTaxUtil.getDepreciationByDate(floor.getConstructionDate(), floor.getOccupancyDate()));
                    }

                    LOGGER.debug("createFloors: PropertyUsage: " + usage + ", PropertyOccupation: " + occupancy
                            + ", StructureClass: " + structureClass);

                    if (unitType != null
                            && unitType.getCode().equalsIgnoreCase(PropertyTaxConstants.UNITTYPE_OPEN_PLOT))
                        floor.setFloorNo(OPEN_PLOT_UNIT_FLOORNUMBER);

                    floor.setUnitType(unitType);
                    floor.setPropertyUsage(usage);
                    floor.setPropertyOccupation(occupancy);
                    floor.setStructureClassification(structureClass);
                    floor.setPropertyDetail(property.getPropertyDetail());
                    floor.setCreatedDate(new Date());
                    floor.setModifiedDate(new Date());
                    floor.setFloorUid(floorUid++);
                    final User user = userService.getUserById(ApplicationThreadLocals.getUserId());
                    floor.setCreatedBy(user);
                    floor.setModifiedBy(user);
                    property.getPropertyDetail().getFloorDetails().add(floor);
                    // setting total builtup area.
                    totBltUpArea.setArea(totBltUpAreaVal);
                    totBltUpArea.setLength(floor.getBuiltUpArea().getLength());
                    totBltUpArea.setBreadth(floor.getBuiltUpArea().getBreadth());
                    property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);

                }
            property.getPropertyDetail().setNoofFloors(property.getPropertyDetail().getFloorDetailsProxy().size());
        } else {
            property.getPropertyDetail().setNoofFloors(0);
            property.getPropertyDetail().getFloorDetails().clear();
            totBltUpArea.setArea(totBltUpAreaVal);
            property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);
        }

        LOGGER.debug("Exiting from createFloors");
    }

    /**
     * Creates property status values based on the status code
     *
     * @param basicProperty
     * @param statusCode
     * @param propCompletionDate
     * @param courtOrdNum
     * @param orderDate
     * @param judgmtDetails
     * @param parentPropId
     * @return PropertyImpl
     */
    public PropertyStatusValues createPropStatVal(final BasicProperty basicProperty, final String statusCode,
            final Date propCompletionDate, final String courtOrdNum, final Date orderDate, final String judgmtDetails,
            final String parentPropId) {
        LOGGER.debug("Entered into createPropStatVal");
        LOGGER.debug("createPropStatVal: basicProperty: " + basicProperty + ", statusCode: " + statusCode
                + ", propCompletionDate: " + propCompletionDate + ", courtOrdNum: " + courtOrdNum + ", orderDate: "
                + orderDate + ", judgmtDetails: " + judgmtDetails + ", parentPropId: " + parentPropId);
        final PropertyStatusValues propStatVal = new PropertyStatusValues();
        final PropertyStatus propertyStatus = (PropertyStatus) getPropPerServ().find(
                "from PropertyStatus where statusCode=?", statusCode);

        propStatVal.setIsActive("Y");
        final User user = userService.getUserById(ApplicationThreadLocals.getUserId());
        propStatVal.setCreatedDate(new Date());
        propStatVal.setModifiedDate(new Date());
        propStatVal.setCreatedBy(user);
        propStatVal.setModifiedBy(user);
        propStatVal.setPropertyStatus(propertyStatus);
        if (orderDate != null || courtOrdNum != null && !courtOrdNum.equals("") || judgmtDetails != null
                && !judgmtDetails.equals("")) {
            propStatVal.setReferenceDate(orderDate);
            propStatVal.setReferenceNo(courtOrdNum);
            propStatVal.setRemarks(judgmtDetails);
        } else {
            propStatVal.setReferenceDate(new Date());
            propStatVal.setReferenceNo("0001");// There should be rule to create order number, client has to give it
        }
        if (!statusCode.equals(PROP_CREATE_RSN) && propCompletionDate != null) {
            // persist the DateOfCompletion in case of modify property for future reference
            final String propCompDateStr = DateUtils.getFormattedDate(propCompletionDate, DATE_FORMAT_DDMMYYY);
            propStatVal.setExtraField1(propCompDateStr);
        }
        propStatVal.setBasicProperty(basicProperty);
        if (basicProperty.getPropertyMutationMaster() != null
                && basicProperty.getPropertyMutationMaster().getCode().equals(PROP_CREATE_RSN_BIFUR)) {
            final BasicProperty referenceBasicProperty = (BasicProperty) propPerServ.find(
                    "from BasicPropertyImpl bp where bp.upicNo=?", parentPropId);
            propStatVal.setReferenceBasicProperty(referenceBasicProperty);
        }
        LOGGER.debug("createPropStatVal: PropertyStatusValues: " + propStatVal);
        LOGGER.debug("Exiting from createPropStatVal");
        return propStatVal;
    }

    /**
     * Creates installment wise demands for a property
     *
     * @param property
     * @param dateOfCompletion
     * @return Property with installment wise demand set
     * @throws TaxCalculatorExeption
     */
    public Property createDemand(final PropertyImpl property, final Date dateOfCompletion) throws TaxCalculatorExeption {
        LOGGER.debug("Entered into createDemand");
        LOGGER.debug("createDemand: Property: " + property + ", dateOfCompletion: " + dateOfCompletion);

        instTaxMap = taxCalculator.calculatePropertyTax(property, dateOfCompletion);

        Ptdemand ptDemand;
        final Set<Ptdemand> ptDmdSet = new HashSet<Ptdemand>();
        Set<EgDemandDetails> dmdDetailSet;
        List<Installment> instList = new ArrayList<Installment>();
        instList = new ArrayList<Installment>(instTaxMap.keySet());
        LOGGER.debug("createDemand: instList: " + instList);
        currentInstall = propertyTaxCommonUtils.getCurrentInstallment();
        // Clear existing EgDemandDetails and recreate them below 
        property.getPtDemandSet().clear();

        Map<String,Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment installmentFirstHalf = yearwiseInstMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        Installment installmentSecondHalf = yearwiseInstMap.get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);

        APTaxCalculationInfo taxCalcInfo = null;

        /**
         * Only 1 Ptdemand will be created, i.e., for 1st half of current year. 
         * Demand Details will be created from the effective date till 2nd installment of current year 
         *
         * In case of demolition done in 2nd half, APTaxCalculationInfo is generated for current year 2nd installment.
         * In this case, demand needs to be calculated for both installments of next financial year only.
         * Currently, entering future date is not allowed during create/modify etc
         * Demolition in 2nd is the only use case where demand will be calculated for future date
         * The below conditions are added to handle all above use cases
         */
        if(instList.size()==1 && instList.get(0).equals(installmentSecondHalf)){
        	taxCalcInfo = (APTaxCalculationInfo) instTaxMap.get(installmentSecondHalf);
        } else if(dateOfCompletion.after(installmentSecondHalf.getToDate())){
        	//Executed only in case of demolition done in 2nd half
        	taxCalcInfo = (APTaxCalculationInfo) instTaxMap.get(installmentSecondHalf);
        	instList.remove(installmentSecondHalf);
        } else{
        	taxCalcInfo = (APTaxCalculationInfo) instTaxMap.get(installmentFirstHalf);
        }

        dmdDetailSet = createAllDmdDetails(instList, instTaxMap);
        final PTDemandCalculations ptDmdCalc = new PTDemandCalculations();
        ptDemand = new Ptdemand();
        ptDemand.setBaseDemand(taxCalcInfo.getTotalTaxPayable());
        ptDemand.setCreateDate(new Date());
        ptDemand.setEgInstallmentMaster(installmentFirstHalf);

        ptDemand.setEgDemandDetails(dmdDetailSet);
        ptDemand.setIsHistory("N");
        ptDemand.setEgptProperty(property);
        ptDmdSet.add(ptDemand);
        ptDmdCalc.setPtDemand(ptDemand);
        ptDmdCalc.setPropertyTax(taxCalcInfo.getTotalTaxPayable());
        ptDmdCalc.setTaxInfo(taxCalcInfo.getTaxCalculationInfoXML().getBytes());
        propPerServ.applyAuditing(ptDmdCalc);
        ptDemand.setDmdCalculations(ptDmdCalc);

        // In case of Property Type as (Open Plot,State Govt,Central Govt), set the alv to PTDemandCalculations 
        if (property.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            ptDmdCalc.setAlv(taxCalcInfo.getTotalNetARV());
        else if (installmentFirstHalf.equals(currentInstall)) {
            // FloorwiseDemandCalculations should be set only for the current installment for each floor 
            for (final Floor floor : property.getPropertyDetail().getFloorDetails())
                ptDmdCalc.addFlrwiseDmdCalculations(createFloorDmdCalc(ptDmdCalc, floor, taxCalcInfo));
            ptDmdCalc.setAlv(totalAlv);
        }

        property.getPtDemandSet().addAll(ptDmdSet);

        LOGGER.debug("Exiting from createDemand");
        return property;
    }

    /**
     * Called to modify Property demands when the property is modified
     *
     * @param oldProperty
     * @param newProperty
     * @param dateOfCompletion
     * @return newProperty
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Property createDemandForModify(final Property oldProperty, final Property newProperty,
            final Date dateOfCompletion) {
        LOGGER.debug("Entered into createDemandForModify");
        LOGGER.debug("createDemandForModify: oldProperty: " + oldProperty + ", newProperty: " + newProperty
                + ", dateOfCompletion: " + dateOfCompletion);

        List<Installment> instList = new ArrayList<Installment>();
        instList = new ArrayList<Installment>(instTaxMap.keySet());
        LOGGER.debug("createDemandForModify: instList: " + instList);
        Ptdemand ptDemandOld = new Ptdemand();
        Ptdemand ptDemandNew = new Ptdemand();
        Map<String,Installment> yearwiseInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        Installment installmentFirstHalf = yearwiseInstMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        Installment installmentSecondHalf = yearwiseInstMap.get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
        final Map<String, Ptdemand> oldPtdemandMap = getPtdemandsAsInstMap(oldProperty.getPtDemandSet());
        ptDemandOld = oldPtdemandMap.get(installmentFirstHalf.getDescription());
        final PropertyTypeMaster oldPropTypeMaster = oldProperty.getPropertyDetail().getPropertyTypeMaster();
        final PropertyTypeMaster newPropTypeMaster = newProperty.getPropertyDetail().getPropertyTypeMaster();

        if (!oldProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(newProperty.getPropertyDetail().getPropertyTypeMaster().getCode())
                || !oldProperty.getIsExemptedFromTax() ^ !newProperty.getIsExemptedFromTax())
                createAllDmdDetails(oldProperty, newProperty, installmentFirstHalf, instList, instTaxMap);

        final Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());
        ptDemandNew = newPtdemandMap.get(installmentFirstHalf.getDescription());

        final Map<Installment, Set<EgDemandDetails>> newDemandDtlsMap = getEgDemandDetailsSetAsMap(new ArrayList(
                ptDemandNew.getEgDemandDetails()), instList);

        for (final Installment inst : instList) {
            carryForwardCollection(newProperty, inst, newDemandDtlsMap.get(inst), ptDemandOld, oldPropTypeMaster,
                    newPropTypeMaster);

            /*
             * If current year second half is the only installment in the list, then create the arrear demand details from the
             * Ptdemand of current year first installment
             */
            if (instList.size() == 1 && instList.get(0).equals(installmentSecondHalf)) {
                carryForwardPenalty(ptDemandOld, ptDemandNew, installmentFirstHalf);
            } else {
                if (inst.equals(currentInstall)) {
                    carryForwardPenalty(ptDemandOld, ptDemandNew, inst);
                }
            }
        }

        // sort the installment list in ascending order to start the excessCollection adjustment from 1st inst
        LOGGER.info("before adjustExcessCollAmt newDemandDtlsMap.size: " + newDemandDtlsMap.size());
        Collections.sort(instList);

        if (!excessCollAmtMap.isEmpty())
            adjustExcessCollectionAmount(instList, newDemandDtlsMap, ptDemandNew);

        LOGGER.debug("Exiting from createDemandForModify");
        return newProperty;
    }

    /**
     * Carries forward the penalty from the old property to the new property
     * @param ptDemandOld
     * @param ptDemandNew
     * @param inst
     */
	private void carryForwardPenalty(Ptdemand ptDemandOld, Ptdemand ptDemandNew, final Installment inst) {
		List<EgDemandDetails> penaltyDmdDtlsList = null;
		penaltyDmdDtlsList = getEgDemandDetailsListForReason(ptDemandOld.getEgDemandDetails(),
		        DEMANDRSN_CODE_PENALTY_FINES);
		if (penaltyDmdDtlsList != null && penaltyDmdDtlsList.size() > 0)
		    for (final EgDemandDetails penaltyDmdDet : penaltyDmdDtlsList)
		        ptDemandNew.getEgDemandDetails().add((EgDemandDetails) penaltyDmdDet.clone());
		penaltyDmdDtlsList = getEgDemandDetailsListForReason(ptDemandOld.getEgDemandDetails(),
		        DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY);
		if (penaltyDmdDtlsList != null && penaltyDmdDtlsList.size() > 0)
		    for (final EgDemandDetails penaltyDmdDet : penaltyDmdDtlsList)
		        ptDemandNew.getEgDemandDetails().add((EgDemandDetails) penaltyDmdDet.clone());
	}

    /**
     * Modifies property active demand and creates arrears demand and performs the excss colletion adjustment
     *
     * @param propertyModel
     * @param oldProperty
     * @return
     * @throws TaxCalculatorExeption
     */
    public Property modifyDemand(final PropertyImpl propertyModel, final PropertyImpl oldProperty)
            throws TaxCalculatorExeption {
        Date propCompletionDate = null;
        if (!propertyModel.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
            propCompletionDate = getLowestDtOfCompFloorWise(propertyModel.getPropertyDetail().getFloorDetails());
        else
            propCompletionDate = propertyModel.getPropertyDetail().getDateOfCompletion();
        final PropertyImpl newProperty = (PropertyImpl) createDemand(propertyModel, propCompletionDate);
        Property modProperty = null;
        if (oldProperty == null)
            LOGGER.info("modifyBasicProp, Could not get the previous property. DCB for arrears will be incorrect");
        else {
            modProperty = createDemandForModify(oldProperty, newProperty, propCompletionDate);
            modProperty = createArrearsDemand(oldProperty, propCompletionDate, newProperty);
        }

        Map<Installment, Set<EgDemandDetails>> demandDetailsSetByInstallment = null;
        List<Installment> installments = null;

        final Set<EgDemandDetails> oldEgDemandDetailsSet = getOldDemandDetails(oldProperty, newProperty);
        demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(oldEgDemandDetailsSet);
        installments = new ArrayList<Installment>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);
        for (final Installment inst : installments) {
            final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();
            for (final String rsn : DEMAND_RSNS_LIST) {
                final EgDemandDetails newDmndDtls = getEgDemandDetailsForReason(
                        demandDetailsSetByInstallment.get(inst), rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null)
                    // If there is collection then add to map
                    if (newDmndDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0)
                        dmdRsnAmt.put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                                newDmndDtls.getAmtCollected());
            }
            getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }
        final Ptdemand currentDemand = getCurrrentDemand(modProperty);
        demandDetailsSetByInstallment = getEgDemandDetailsSetByInstallment(currentDemand.getEgDemandDetails());
        installments = new ArrayList<Installment>(demandDetailsSetByInstallment.keySet());
        Collections.sort(installments);
        for (final Installment inst : installments) {
            final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();
            for (final String rsn : DEMAND_RSNS_LIST) {
                final EgDemandDetails newDmndDtls = getEgDemandDetailsForReason(
                        demandDetailsSetByInstallment.get(inst), rsn);
                if (newDmndDtls != null && newDmndDtls.getAmtCollected() != null) {
                    final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
                    // If there is extraColl then add to map
                    if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
                        dmdRsnAmt
                                .put(newDmndDtls.getEgDemandReason().getEgDemandReasonMaster().getCode(), extraCollAmt);
                        newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
                        newDmndDtls.setModifiedDate(new Date());
                    }
                }
            }
            getExcessCollAmtMap().put(inst, dmdRsnAmt);
        }

        LOGGER.info("Excess Collection - " + getExcessCollAmtMap());

        adjustExcessCollectionAmount(installments, demandDetailsSetByInstallment, currentDemand);
        return modProperty;
    }

    /**
     * Returns old demand details
     *
     * @param oldProperty
     * @param newProperty
     * @return set of old demand details
     */
    private Set<EgDemandDetails> getOldDemandDetails(final Property oldProperty, final Property newProperty) {

        final Set<EgDemandDetails> oldDemandDetails = new HashSet<EgDemandDetails>();

        for (final EgDemandDetails dd : getCurrrentDemand(oldProperty).getEgDemandDetails())
            if (dd.getEgDemandReason().getEgInstallmentMaster().getFromDate().before(newProperty.getEffectiveDate()))
                oldDemandDetails.add(dd);

        return oldDemandDetails;
    }

    /**
     * Prepares map of installment wise demand details set
     *
     * @param demandDetailsSet
     * @return Map of Installment wise demand details set
     */
    private Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetByInstallment(
            final Set<EgDemandDetails> demandDetailsSet) {
        final Map<Installment, Set<EgDemandDetails>> newEgDemandDetailsSetByInstallment = new HashMap<Installment, Set<EgDemandDetails>>();

        for (final EgDemandDetails dd : demandDetailsSet) {

            if (dd.getAmtCollected() == null)
                dd.setAmtCollected(ZERO);

            if (newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()) == null) {
                final Set<EgDemandDetails> ddSet = new HashSet<EgDemandDetails>();
                ddSet.add(dd);
                newEgDemandDetailsSetByInstallment.put(dd.getEgDemandReason().getEgInstallmentMaster(), ddSet);
            } else
                newEgDemandDetailsSetByInstallment.get(dd.getEgDemandReason().getEgInstallmentMaster()).add(dd);
        }

        return newEgDemandDetailsSetByInstallment;
    }

    /**
     * Returns current installment's demand of a property
     *
     * @param property
     * @return
     */
    private Ptdemand getCurrrentDemand(final Property property) {
        Ptdemand currentDemand = null;

        for (final Ptdemand ptdemand : property.getPtDemandSet())
            if (ptdemand.getEgInstallmentMaster().equals(propertyTaxCommonUtils.getCurrentInstallment())) {
                currentDemand = ptdemand;
                break;
            }
        return currentDemand;
    }

    /**
     * Returns formatted property occupation date
     *
     * @param dateOfCompletion
     * @return
     */
    public Date getPropOccupatedDate(final String dateOfCompletion) {
        LOGGER.debug("Entered into getPropOccupatedDate, dateOfCompletion: " + dateOfCompletion);
        Date occupationDate = null;
        try {
            if (dateOfCompletion != null && !"".equals(dateOfCompletion))
                occupationDate = dateFormatter.parse(dateOfCompletion);
        } catch (final ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.debug("Exiting from getPropOccupatedDate");
        return occupationDate;
    }

    /**
     * Creates installment wise demand details
     *
     * @param installment
     * @param instList
     * @param instTaxMap
     * @return
     */
    private Set<EgDemandDetails> createAllDmdDetails(final List<Installment> instList,
            final HashMap<Installment, TaxCalculationInfo> instTaxMap) {
        LOGGER.debug("Entered into createAllDmdDeatails");

        final Set<EgDemandDetails> dmdDetSet = new HashSet<EgDemandDetails>();
        for (final Installment inst : instList){

                final TaxCalculationInfo taxCalcInfo = instTaxMap.get(inst);
                final Map<String, BigDecimal> taxMap = taxCalculator.getMiscTaxesForProp(taxCalcInfo
                        .getUnitTaxCalculationInfos());

                for (final Map.Entry<String, BigDecimal> tax : taxMap.entrySet()) {
                    final EgDemandReason egDmdRsn = propertyTaxUtil.getDemandReasonByCodeAndInstallment(tax.getKey(),
                            inst);
                    dmdDetSet.add(createDemandDetails(tax.getValue(), egDmdRsn, inst));
                }
         }

        LOGGER.debug("createAllDmdDeatails: dmdDetSet: " + dmdDetSet);
        return dmdDetSet;
    }

    /**
     * Modifies demand details of newly created property
     *
     * @param oldProperty
     * @param newProperty
     * @param installment
     * @param instList
     * @param instTaxMap
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void createAllDmdDetails(final Property oldProperty, final Property newProperty,
            final Installment installment, final List<Installment> instList,
            final HashMap<Installment, TaxCalculationInfo> instTaxMap) {
        LOGGER.debug("Entered into createAllDmdDeatails");
        LOGGER.debug("createAllDmdDeatails: oldProperty: " + oldProperty + ", newProperty: " + newProperty
                + ",installment: " + installment + ", instList: " + instList);
        final Set<EgDemandDetails> adjustedDmdDetailsSet = new HashSet<EgDemandDetails>();

        final Map<String, Ptdemand> oldPtdemandMap = getPtdemandsAsInstMap(oldProperty.getPtDemandSet());
        final Map<String, Ptdemand> newPtdemandMap = getPtdemandsAsInstMap(newProperty.getPtDemandSet());

        Ptdemand ptDemandOld = new Ptdemand();
        Ptdemand ptDemandNew = new Ptdemand();

        Set<EgDemandDetails> newEgDemandDetailsSet = null;
        Set<EgDemandDetails> oldEgDemandDetailsSet = null;

        final List<String> adjstmntReasons = new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 860234856101419601L;

            {
                add(DEMANDRSN_CODE_GENERAL_TAX);
                add(DEMANDRSN_CODE_VACANT_TAX);
                add(DEMANDRSN_CODE_LIBRARY_CESS);
                add(DEMANDRSN_CODE_SEWERAGE_TAX);
                add(DEMANDRSN_CODE_EDUCATIONAL_CESS);
            }
        };

        final List<String> rsnsForNewResProp = new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = -1654413629447625291L;

            {
                add(DEMANDRSN_CODE_GENERAL_TAX);
                add(DEMANDRSN_CODE_VACANT_TAX);
                add(DEMANDRSN_CODE_LIBRARY_CESS);
                add(DEMANDRSN_CODE_SEWERAGE_TAX);
                add(DEMANDRSN_CODE_EDUCATIONAL_CESS);
            }
        };

        new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = -8513477823231046385L;

            {
                add(DEMANDRSN_CODE_GENERAL_TAX);
                add(DEMANDRSN_CODE_VACANT_TAX);
                add(DEMANDRSN_CODE_LIBRARY_CESS);
                add(DEMANDRSN_CODE_SEWERAGE_TAX);
                add(DEMANDRSN_CODE_EDUCATIONAL_CESS);
            }
        };

        ptDemandOld = oldPtdemandMap.get(installment.getDescription());
        ptDemandNew = newPtdemandMap.get(installment.getDescription());

        LOGGER.info("instList==========" + instList);

        final Map<Installment, Set<EgDemandDetails>> oldDemandDtlsMap = getEgDemandDetailsSetAsMap(new ArrayList(
                ptDemandOld.getEgDemandDetails()), instList);
        LOGGER.info("oldDemandDtlsMap : " + oldDemandDtlsMap);

        for (final Installment inst : instList) {
            oldEgDemandDetailsSet = new HashSet<EgDemandDetails>();

            oldEgDemandDetailsSet = oldDemandDtlsMap.get(inst);

            LOGGER.info("inst==========" + inst);
            final Set<EgDemandDetails> demandDtls = demandDetails.get(inst);
            if (demandDtls != null)
                for (final EgDemandDetails dd : demandDtls) {
                    final EgDemandDetails ddClone = (EgDemandDetails) dd.clone();
                    ddClone.setEgDemand(ptDemandNew);
                    adjustedDmdDetailsSet.add(ddClone);
                }
            else {

                EgDemandDetails oldEgdmndDetails = null;
                EgDemandDetails newEgDmndDetails = null;

                newEgDemandDetailsSet = new HashSet<EgDemandDetails>();

                // Getting EgDemandDetails for inst installment

                for (final EgDemandDetails edd : ptDemandNew.getEgDemandDetails())
                    if (edd.getEgDemandReason().getEgInstallmentMaster().equals(inst))
                        newEgDemandDetailsSet.add((EgDemandDetails) edd.clone());

                LOGGER.info("Old Demand Set:" + inst + "=" + oldEgDemandDetailsSet);
                LOGGER.info("New Demand set:" + inst + "=" + newEgDemandDetailsSet);

                if (!oldProperty.getIsExemptedFromTax() && !newProperty.getIsExemptedFromTax())
                    for (int i = 0; i < adjstmntReasons.size(); i++) {
                        final String oldPropRsn = adjstmntReasons.get(i);
                        String newPropRsn = null;

                        /*
                         * Gives EgDemandDetails from newEgDemandDetailsSet for demand reason oldPropRsn, if we dont have
                         * EgDemandDetails then doing collection adjustments
                         */
                        newEgDmndDetails = getEgDemandDetailsForReason(newEgDemandDetailsSet, oldPropRsn);

                        if (newEgDmndDetails == null) {
                            newPropRsn = rsnsForNewResProp.get(i);
                            oldEgdmndDetails = getEgDemandDetailsForReason(oldEgDemandDetailsSet, oldPropRsn);
                            newEgDmndDetails = getEgDemandDetailsForReason(newEgDemandDetailsSet, newPropRsn);

                            if (newEgDmndDetails != null && oldEgdmndDetails != null)
                                newEgDmndDetails.setAmtCollected(newEgDmndDetails.getAmtCollected().add(
                                        oldEgdmndDetails.getAmtCollected()));
                            else
                                continue;
                        }
                    }
                else if (!oldProperty.getIsExemptedFromTax())
                    newEgDemandDetailsSet = adjustmentsForTaxExempted(ptDemandOld.getEgDemandDetails(),
                            newEgDemandDetailsSet, inst);

                // Collection carry forward logic (This logic is moved out
                // of this method, bcoz it has to be invoked in all usecases
                // and not only when there is property type change

                adjustedDmdDetailsSet.addAll(newEgDemandDetailsSet);
                demandDetails.put(inst, newEgDemandDetailsSet);
            }
        }

        // forwards the base collection for current installment Ptdemand
        if (installment.equals(installment)) {
            final Ptdemand ptdOld = oldPtdemandMap.get(installment.getDescription());
            final Ptdemand ptdNew = newPtdemandMap.get(installment.getDescription());
            ptdNew.setAmtCollected(ptdOld.getAmtCollected());
        }

        LOGGER.info("Exit from PropertyService.createAllDmdDeatails, Modify Adjustments for "
                + oldProperty.getBasicProperty().getUpicNo() + " And installment : " + installment + "\n\n"
                + adjustedDmdDetailsSet);
        ptDemandNew.setEgDemandDetails(adjustedDmdDetailsSet);
        LOGGER.debug("Exiting from createAllDmdDeatails");
    }

    /**
     * Carry forwards collection from the old property to the newly created property
     *
     * @param newProperty
     * @param inst
     * @param newEgDemandDetailsSet
     * @param ptDmndOld
     * @param oldPropTypeMaster
     * @param newPropTypeMaster
     * @return
     */
    private Set<EgDemandDetails> carryForwardCollection(final Property newProperty, final Installment inst,
            final Set<EgDemandDetails> newEgDemandDetailsSet, final Ptdemand ptDmndOld,
            final PropertyTypeMaster oldPropTypeMaster, final PropertyTypeMaster newPropTypeMaster) {
        LOGGER.debug("Entered into carryForwardCollection");
        LOGGER.debug("carryForwardCollection: newProperty: " + newProperty + ", inst: " + inst
                + ", newEgDemandDetailsSet: " + newEgDemandDetailsSet + ", ptDmndOld: " + ptDmndOld
                + ", oldPropTypeMaster: " + oldPropTypeMaster + ", newPropTypeMaster: " + newPropTypeMaster);

        final Map<String, BigDecimal> dmdRsnAmt = new LinkedHashMap<String, BigDecimal>();

        final List<String> demandReasonsWithAdvance = new ArrayList<String>(DEMAND_RSNS_LIST);
        demandReasonsWithAdvance.add(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);

        for (final String rsn : demandReasonsWithAdvance) {

            List<EgDemandDetails> oldEgDmndDtlsList = null;
            List<EgDemandDetails> newEgDmndDtlsList = null;

            oldEgDmndDtlsList = getEgDemandDetailsListForReason(ptDmndOld.getEgDemandDetails(), rsn);
            newEgDmndDtlsList = getEgDemandDetailsListForReason(newEgDemandDetailsSet, rsn);

            Map<Installment, EgDemandDetails> oldDemandDtlsMap = null;
            Map<Installment, EgDemandDetails> newDemandDtlsMap = null;
            EgDemandDetails oldDmndDtls = null;
            EgDemandDetails newDmndDtls = null;

            if (oldEgDmndDtlsList != null) {
                oldDemandDtlsMap = getEgDemandDetailsAsMap(oldEgDmndDtlsList);
                oldDmndDtls = oldDemandDtlsMap.get(inst);
            }
            if (newEgDmndDtlsList != null) {
                newDemandDtlsMap = getEgDemandDetailsAsMap(newEgDmndDtlsList);
                newDmndDtls = newDemandDtlsMap.get(inst);
            }

            calculateExcessCollection(dmdRsnAmt, rsn, oldDmndDtls, newDmndDtls);
        }
        excessCollAmtMap.put(inst, dmdRsnAmt);

        demandDetails.put(inst, newEgDemandDetailsSet);
        LOGGER.debug("carryForwardCollection: newEgDemandDetailsSet: " + newEgDemandDetailsSet);
        LOGGER.debug("Exiting from carryForwardCollection");
        return newEgDemandDetailsSet;
    }

    /**
     * Calculates and prepares demand reason wise excess collection amount
     *
     * @param dmdRsnAmt
     * @param rsn
     * @param oldDmndDtls
     * @param newDmndDtls
     */
    public void calculateExcessCollection(final Map<String, BigDecimal> dmdRsnAmt, final String rsn,
            final EgDemandDetails oldDmndDtls, final EgDemandDetails newDmndDtls) {
        /**
         * If old and new demand details are present then set the old collection amount to the new demand details else if old
         * demand details are not present then make the new collection amount as Zero
         */
        if (newDmndDtls != null && oldDmndDtls != null) {
            newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().add(oldDmndDtls.getAmtCollected()));
            newDmndDtls.setAmtRebate(newDmndDtls.getAmtRebate().add(oldDmndDtls.getAmtRebate()));
        } else if (newDmndDtls != null && oldDmndDtls == null) {
            newDmndDtls.setAmtCollected(ZERO);
            newDmndDtls.setAmtRebate(ZERO);
        }

        /**
         * prepares reason wise extra collection amount if any of the demand details has
         */
        if (newDmndDtls != null && !rsn.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE)) {
            // This part of code handles the adjustment of extra collections
            // when there is decrease in tax during property modification.

            final BigDecimal extraCollAmt = newDmndDtls.getAmtCollected().subtract(newDmndDtls.getAmount());
            // If there is extraColl then add to map
            if (extraCollAmt.compareTo(BigDecimal.ZERO) > 0) {
                dmdRsnAmt.put(rsn, extraCollAmt);
                newDmndDtls.setAmtCollected(newDmndDtls.getAmtCollected().subtract(extraCollAmt));
            }
        }

        /**
         * after modify the old demand reason is not there in new property just take the entire collected amount as excess
         * collection when a unit in new property is exempted from tax 16-Oct-2014 with new requirement, refer card #3427
         */
        if (oldDmndDtls != null && newDmndDtls == null)
            if (oldDmndDtls.getAmtCollected().compareTo(BigDecimal.ZERO) > 0)
                dmdRsnAmt.put(rsn, oldDmndDtls.getAmtCollected());
    }

    /**
     * Called locally to get Map of Installment/Ptdemand pair
     *
     * @param ptdemandSet
     * @return
     */
    private Map<String, Ptdemand> getPtdemandsAsInstMap(final Set<Ptdemand> ptdemandSet) {
        LOGGER.debug("Entered into getPtDemandsAsInstMap, PtDemandSet: " + ptdemandSet);
        final Map<String, Ptdemand> ptDemandMap = new TreeMap<String, Ptdemand>();
        for (final Ptdemand ptDmnd : ptdemandSet)
            ptDemandMap.put(ptDmnd.getEgInstallmentMaster().getDescription(), ptDmnd);
        LOGGER.debug("getPtDemandsAsInstMap, ptDemandMap: " + ptDemandMap);
        LOGGER.debug("Exiting from getPtDemandsAsInstMap");
        return ptDemandMap;
    }

    /**
     * Called locally to get Map of Installment/EgDemandDetail pair from list of EgDemandDetails
     *
     * @param demandDetailsList
     * @return demandDetailsMap
     */
    public Map<Installment, EgDemandDetails> getEgDemandDetailsAsMap(final List<EgDemandDetails> demandDetailsList) {
        LOGGER.debug("Entered into getEgDemandDetailsAsMap, demandDetailsList: " + demandDetailsList);
        final Map<Installment, EgDemandDetails> demandDetailsMap = new HashMap<Installment, EgDemandDetails>();
        for (final EgDemandDetails dmndDtls : demandDetailsList)
            demandDetailsMap.put(dmndDtls.getEgDemandReason().getEgInstallmentMaster(), dmndDtls);
        LOGGER.debug("getEgDemandDetailsAsMap: demandDetailsMap: " + demandDetailsMap);
        LOGGER.debug("Exiting from getEgDemandDetailsAsMap");
        return demandDetailsMap;
    }

    /**
     * Called locally to get Installment/Set<EgDemandDetails> pair map
     *
     * @param demandDetailsList
     * @return
     */
    public Map<Installment, Set<EgDemandDetails>> getEgDemandDetailsSetAsMap(
            final List<EgDemandDetails> demandDetailsList, final List<Installment> instList) {
        LOGGER.debug("Entered into getEgDemandDetailsSetAsMap, demandDetailsList: " + demandDetailsList
                + ", instList: " + instList);
        final Map<Installment, Set<EgDemandDetails>> demandDetailsMap = new HashMap<Installment, Set<EgDemandDetails>>();
        Set<EgDemandDetails> ddSet = null;

        for (final Installment inst : instList) {
            ddSet = new HashSet<EgDemandDetails>();
            for (final EgDemandDetails dd : demandDetailsList)
                if (dd.getEgDemandReason().getEgInstallmentMaster().equals(inst))
                    ddSet.add(dd);
            demandDetailsMap.put(inst, ddSet);
        }
        LOGGER.debug("getEgDemandDetailsSetAsMap: demandDetailsMap: " + demandDetailsMap);
        LOGGER.debug("Exiting from getEgDemandDetailsSetAsMap");
        return demandDetailsMap;
    }

    /**
     * Called locally to get EgDemandDetails from the egDemandDetailsSet for demand reason demandReason
     *
     * @param egDemandDetailsSet
     * @param demandReason
     * @return EgDemandDetails
     */
    public EgDemandDetails getEgDemandDetailsForReason(final Set<EgDemandDetails> egDemandDetailsSet,
            final String demandReason) {
        LOGGER.debug("Entered into getEgDemandDetailsForReason, egDemandDetailsSet: " + egDemandDetailsSet
                + ", demandReason: " + demandReason);
        final List<Map<String, EgDemandDetails>> egDemandDetailsList = getEgDemandDetailsAsMap(egDemandDetailsSet);
        EgDemandDetails egDemandDetails = null;
        for (final Map<String, EgDemandDetails> egDmndDtlsMap : egDemandDetailsList) {
            egDemandDetails = egDmndDtlsMap.get(demandReason);
            if (egDemandDetails != null)
                break;
        }
        LOGGER.debug("getEgDemandDetailsForReason: egDemandDetails: " + egDemandDetails);
        LOGGER.debug("Exiting from getEgDemandDetailsForReason");
        return egDemandDetails;
    }

    /**
     * Called locally to get EgDemandDetails from the egDemandDetailsSet for demand reason demandReason
     *
     * @param egDemandDetailsSet
     * @param demandReason
     * @return EgDemandDetails
     */
    private List<EgDemandDetails> getEgDemandDetailsListForReason(final Set<EgDemandDetails> egDemandDetailsSet,
            final String demandReason) {
        LOGGER.debug("Entered into getEgDemandDetailsListForReason: egDemandDetailsSet: " + egDemandDetailsSet
                + ", demandReason: " + demandReason);
        final List<Map<String, EgDemandDetails>> egDemandDetailsList = getEgDemandDetailsAsMap(egDemandDetailsSet);
        final List<EgDemandDetails> demandListForReason = new ArrayList<EgDemandDetails>();
        for (final Map<String, EgDemandDetails> egDmndDtlsMap : egDemandDetailsList)
            if (egDmndDtlsMap.get(demandReason) != null)
                demandListForReason.add(egDmndDtlsMap.get(demandReason));
        LOGGER.debug("getEgDemandDetailsListForReason: demandListForReason: " + demandListForReason);
        LOGGER.debug("Exiting from getEgDemandDetailsListForReason");
        return demandListForReason;
    }

    /**
     * Called locally to get the egDemandDetailsSet as list of maps with demand reason as key and EgDemandDetails as value
     *
     * @param egDemandDetailsSet
     * @param installment
     * @return
     */
    public List<Map<String, EgDemandDetails>> getEgDemandDetailsAsMap(final Set<EgDemandDetails> egDemandDetailsSet) {
        LOGGER.debug("Entered into getEgDemandDetailsAsMap, egDemandDetailsSet: " + egDemandDetailsSet);
        final List<EgDemandDetails> egDemandDetailsList = new ArrayList<EgDemandDetails>(egDemandDetailsSet);
        final List<Map<String, EgDemandDetails>> egDemandDetailsListOfMap = new ArrayList<Map<String, EgDemandDetails>>();

        for (final EgDemandDetails egDmndDtls : egDemandDetailsList) {
            final Map<String, EgDemandDetails> egDemandDetailsMap = new HashMap<String, EgDemandDetails>();
            final EgDemandReasonMaster dmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();
            if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_GENERAL_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_VACANT_TAX))
                egDemandDetailsMap.put(DEMANDRSN_CODE_VACANT_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS))
                egDemandDetailsMap.put(DEMANDRSN_CODE_EDUCATIONAL_CESS, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS))
                egDemandDetailsMap.put(DEMANDRSN_CODE_LIBRARY_CESS, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                egDemandDetailsMap.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES))
                egDemandDetailsMap.put(DEMANDRSN_CODE_PENALTY_FINES, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY))
                egDemandDetailsMap.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE))
                egDemandDetailsMap.put(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE, egDmndDtls);
            egDemandDetailsListOfMap.add(egDemandDetailsMap);
        }
        LOGGER.debug("egDemandDetailsListOfMap: " + egDemandDetailsListOfMap
                + "\n Exiting from getEgDemandDetailsAsMap");
        return egDemandDetailsListOfMap;
    }

    /**
     * Called locally to Adjust EgDemandDetails for Tax Exempted property
     *
     * @param ptDemandOld
     * @param newEgDemandDetails
     * @return newEgDemandDetails
     */
    private Set<EgDemandDetails> adjustmentsForTaxExempted(final Set<EgDemandDetails> oldEgDemandDetails,
            final Set<EgDemandDetails> newEgDemandDetails, final Installment inst) {
        LOGGER.debug("Entered into adjustmentsForTaxExempted, oldEgDemandDetails: " + oldEgDemandDetails
                + ", newEgDemandDetails: " + newEgDemandDetails + ", inst:" + inst);
        BigDecimal totalCollAdjstmntAmnt = BigDecimal.ZERO;

        for (final EgDemandDetails egDmndDtls : oldEgDemandDetails)
            if (egDmndDtls.getEgDemandReason().getEgInstallmentMaster().equals(inst)) {
                final EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();
                if (!egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS)
                        && !egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS)
                        && !egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                    // totalDmndAdjstmntAmnt =
                    // totalDmndAdjstmntAmnt.add(egDmndDtls.getAmount().subtract(
                    // egDmndDtls.getAmtCollected()));
                    totalCollAdjstmntAmnt = totalCollAdjstmntAmnt.add(egDmndDtls.getAmtCollected());
            }

        final List<EgDemandDetails> newEgDmndDetails = new ArrayList<EgDemandDetails>(newEgDemandDetails);

        for (final EgDemandDetails egDmndDtls : newEgDemandDetails) {

            final EgDemandReasonMaster egDmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();

            if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS))
                egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.50")));
            else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS))
                egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));
            else if (egDmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                egDmndDtls.setAmtCollected(totalCollAdjstmntAmnt.multiply(new BigDecimal("0.25")));
        }
        LOGGER.debug("newEgDmndDetails: " + newEgDmndDetails + "\nExiting from adjustmentsForTaxExempted");
        return new HashSet<EgDemandDetails>(newEgDmndDetails);
    }

    /**
     * Creates demand details for the demand reason which being passed
     *
     * @param amount
     * @param dmdRsn
     * @param inst
     * @return Demand details
     */
    private EgDemandDetails createDemandDetails(final BigDecimal amount, final EgDemandReason dmdRsn,
            final Installment inst) {
        LOGGER.debug("Entered into createDemandDetails, amount: " + amount + ", dmdRsn: " + dmdRsn + ", inst: " + inst);
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(amount);
        demandDetail.setAmtCollected(BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(dmdRsn);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        LOGGER.debug("demandDetail: " + demandDetail + "\nExiting from createDemandDetails");
        return demandDetail;
    }

    /**
     * Creates demand details for the demand reason which being passed and sets demand and collection
     *
     * @param amount
     * @param amountCollected
     * @param dmdRsn
     * @param inst
     * @return
     */
    public EgDemandDetails createDemandDetails(final BigDecimal amount, final BigDecimal amountCollected,
            final EgDemandReason dmdRsn, final Installment inst) {
        LOGGER.debug("Entered into createDemandDetails, amount: " + amount + "amountCollected: " + amountCollected
                + ", dmdRsn: " + dmdRsn + ", inst: " + inst);
        final EgDemandDetails demandDetail = new EgDemandDetails();
        demandDetail.setAmount(amount != null ? amount : BigDecimal.ZERO);
        demandDetail.setAmtCollected(amountCollected != null ? amountCollected : BigDecimal.ZERO);
        demandDetail.setAmtRebate(BigDecimal.ZERO);
        demandDetail.setEgDemandReason(dmdRsn);
        demandDetail.setCreateDate(new Date());
        demandDetail.setModifiedDate(new Date());
        LOGGER.debug("demandDetail: " + demandDetail + "\nExiting from createDemandDetails");
        return demandDetail;
    }

    /**
     * Creates Floor wise demand calculations
     *
     * @param ptDmdCal
     * @param floor
     * @param taxCalcInfo
     * @return FloorwiseDemandCalculations
     */
    private FloorwiseDemandCalculations createFloorDmdCalc(final PTDemandCalculations ptDmdCal, final Floor floor,
            final TaxCalculationInfo taxCalcInfo) {
        LOGGER.debug("Entered into createFloorDmdCalc, ptDmdCal: " + ptDmdCal + ", floor: " + floor + ", taxCalcInfo: "
                + taxCalcInfo);
        final FloorwiseDemandCalculations floorDmdCalc = new FloorwiseDemandCalculations();
        floorDmdCalc.setPTDemandCalculations(ptDmdCal);
        floorDmdCalc.setFloor(floor);

        for (final UnitTaxCalculationInfo unitTax : taxCalcInfo.getUnitTaxCalculationInfos())
            if (FLOOR_MAP.get(floor.getFloorNo()).equals(unitTax.getFloorNumber())
                    && floor.getPropertyUsage().getUsageCode().equalsIgnoreCase(unitTax.getUnitUsage())
                    && floor.getPropertyOccupation().getOccupancyCode().equalsIgnoreCase(unitTax.getUnitOccupation())
                    && floor.getStructureClassification().getConstrTypeCode()
                            .equalsIgnoreCase(unitTax.getUnitStructure())
                    && floor.getBuiltUpArea().getArea().equals(Float.valueOf(unitTax.getFloorArea().toString())))
                setFloorDmdCalTax(unitTax, floorDmdCalc);
        totalAlv = totalAlv.add(floorDmdCalc.getAlv());
        LOGGER.debug("floorDmdCalc: " + floorDmdCalc + "\nExiting from createFloorDmdCalc");
        return floorDmdCalc;
    }

    /**
     * Sets floor demand calculation taxes
     *
     * @param unitTax
     * @param floorDmdCalc
     */
    public void setFloorDmdCalTax(final UnitTaxCalculationInfo unitTax, final FloorwiseDemandCalculations floorDmdCalc) {
        floorDmdCalc.setAlv(unitTax.getNetARV());
        floorDmdCalc.setMrv(unitTax.getMrv());
        floorDmdCalc.setCategoryAmt(unitTax.getBaseRate());
        floorDmdCalc.setTotalTaxPayble(unitTax.getTotalTaxPayable());
        for (final MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes())
            if (PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax1(floorDmdCalc.getTax1().add(miscTax.getTotalCalculatedTax()));
            else if (PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax2(floorDmdCalc.getTax2().add(miscTax.getTotalCalculatedTax()));
            else if (PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax3(floorDmdCalc.getTax3().add(miscTax.getTotalCalculatedTax()));
            else if (PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax4(floorDmdCalc.getTax4().add(miscTax.getTotalCalculatedTax()));
            else if (PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_TAX.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax5(floorDmdCalc.getTax5().add(miscTax.getTotalCalculatedTax()));
            else if (PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax6(floorDmdCalc.getTax6().add(miscTax.getTotalCalculatedTax()));
            else if (PropertyTaxConstants.DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES.equals(miscTax.getTaxName()))
                floorDmdCalc.setTax7(floorDmdCalc.getTax7().add(miscTax.getTotalCalculatedTax()));
    }

    /**
     * Returns least date from the floors
     *
     * @param floorList
     * @return
     */
    public Date getLowestDtOfCompFloorWise(final List<Floor> floorList) {
        LOGGER.debug("Entered into getLowestDtOfCompFloorWise, floorList: " + floorList);
        Date completionDate = null;
        for (final Floor floor : floorList) {
            Date floorDate = null;
            if (floor != null) {
                floorDate = floor.getOccupancyDate();
                if (floorDate != null)
                    if (completionDate == null)
                        completionDate = floorDate;
                    else if (completionDate.after(floorDate))
                        completionDate = floorDate;
            }
        }
        LOGGER.debug("completionDate: " + completionDate + "\nExiting from getLowestDtOfCompFloorWise");
        return completionDate;
    }

    /**
     * Creates amalgamation property status values
     *
     * @param amalgPropIds
     * @param parentBasicProperty
     */
    @SuppressWarnings({ "unchecked"})
    public void createAmalgPropStatVal(final String[] amalgPropIds, final BasicProperty parentBasicProperty) {
        LOGGER.debug("Entered into createAmalgPropStatVal, amalgPropIds(length): "
                + (amalgPropIds != null ? amalgPropIds.length : ZERO) + ", parentBasicProperty: " + parentBasicProperty);
        final List<PropertyStatusValues> activePropStatVal = propPerServ.findAllByNamedQuery(
                QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, parentBasicProperty.getUpicNo(), "Y",
                PropertyTaxConstants.PROP_CREATE_RSN);
        LOGGER.debug("createAmalgPropStatVal: activePropStatVal: " + activePropStatVal);
        for (final PropertyStatusValues propstatval : activePropStatVal)
            propstatval.setIsActive("N");

        for (final String amalgId : amalgPropIds)
            if (amalgId != null && !amalgId.equals("")) {
                final BasicProperty amalgBasicProp = (BasicProperty) getPropPerServ().findByNamedQuery(
                        PropertyTaxConstants.QUERY_BASICPROPERTY_BY_UPICNO, amalgId);
                final PropertyStatusValues amalgPropStatVal = new PropertyStatusValues();
                final PropertyStatus propertyStatus = (PropertyStatus) getPropPerServ().find(
                        "from PropertyStatus where statusCode=?", PROPERTY_STATUS_MARK_DEACTIVE);
                amalgPropStatVal.setIsActive("Y");
                amalgPropStatVal.setPropertyStatus(propertyStatus);
                amalgPropStatVal.setReferenceDate(new Date());
                amalgPropStatVal.setReferenceNo("0001");
                amalgPropStatVal.setRemarks("Property Amalgamated");
                amalgBasicProp.addPropertyStatusValues(amalgPropStatVal);
                // At final approval a new PropetyStatusValues has to created
                // with status INACTIVE and set the amalgBasicProp status as
                // INACTIVE and ISACTIVE as 'N'
                amalgPropStatVal.setBasicProperty(amalgBasicProp);

                final PropertyStatusValues propertyStatusValueschild = new PropertyStatusValues();
                final PropertyStatus propertyStatuschild = (PropertyStatus) getPropPerServ().find(
                        "from PropertyStatus where statusCode=?", "CREATE");
                propertyStatusValueschild.setIsActive("Y");
                propertyStatusValueschild.setPropertyStatus(propertyStatuschild);
                propertyStatusValueschild.setReferenceDate(new Date());
                propertyStatusValueschild.setReferenceNo("0001");
                propertyStatusValueschild.setReferenceBasicProperty(amalgBasicProp);
                parentBasicProperty.addPropertyStatusValues(propertyStatusValueschild);
                propertyStatusValueschild.setBasicProperty(parentBasicProperty);
                LOGGER.debug("propertyStatusValueschild: " + propertyStatusValueschild);
            }
        LOGGER.debug("Exiting from createAmalgPropStatVal");
    }

    /**
     * Creates arrears demand for newly created property
     *
     * @param oldproperty
     * @param dateOfCompletion
     * @param property
     * @return
     */
    public Property createArrearsDemand(final Property oldproperty, final Date dateOfCompletion,
            final PropertyImpl property) {
        LOGGER.debug("Entered into createArrearsDemand, oldproperty: " + oldproperty + ", dateOfCompletion: "
                + dateOfCompletion + ", property: " + property);
        Ptdemand currPtDmd = null;
        Ptdemand oldCurrPtDmd = null;
        final Module module = moduleDao.getModuleByName(PTMODULENAME);
        final Installment effectiveInstall = installmentDao
                .getInsatllmentByModuleForGivenDate(module, dateOfCompletion);
        final Installment currInstall = propertyTaxCommonUtils.getCurrentInstallment();
        for (final Ptdemand demand : property.getPtDemandSet())
            if (demand.getIsHistory().equalsIgnoreCase("N"))
                if (demand.getEgInstallmentMaster().equals(currInstall)) {
                    currPtDmd = demand;
                    break;
                }

        for (final Ptdemand ptDmd : oldproperty.getPtDemandSet())
            if (ptDmd.getIsHistory().equalsIgnoreCase("N")) {
                if (ptDmd.getEgInstallmentMaster().equals(currInstall))
                    oldCurrPtDmd = ptDmd;
            }

        addArrDmdDetToCurrentDmd(oldCurrPtDmd, currPtDmd, effectiveInstall,false);

        LOGGER.debug("Exiting from createArrearsDemand");
        return property;
    }

    /**
     * Adds arrears demand details to the current demand
     *
     * @param ptDmd
     * @param currPtDmd
     * @param effectiveInstall
     * @param isDemolition
     */
    public void addArrDmdDetToCurrentDmd(final Ptdemand ptDmd, final Ptdemand currPtDmd,
            final Installment effectiveInstall,boolean isDemolition) {
        LOGGER.debug("Entered into addArrDmdDetToCurrentDmd. ptDmd: " + ptDmd + ", currPtDmd: " + currPtDmd);
        /*
         * For create/modify/GRP/Bifurcation arrear penalty demand details will be added before, other demand details will be added below
         * In case of demolition, arrear penalty also needs to be added along with other demand details
         * This check is done using isDemolition flag
         */
        for (final EgDemandDetails dmdDet : ptDmd.getEgDemandDetails())
            if (dmdDet.getInstallmentStartDate().before(effectiveInstall.getFromDate()))
            	if(!isDemolition){
	            	if(!dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES))
	            		currPtDmd.addEgDemandDetails((EgDemandDetails) dmdDet.clone());
            	} else {
            		currPtDmd.addEgDemandDetails((EgDemandDetails) dmdDet.clone());
            	}
        LOGGER.debug("Exiting from addArrDmdDetToCurrentDmd");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PropertyImpl creteNewPropertyForObjectionWorkflow(final BasicProperty basicProperty2,
            final String objectionNum, final Date objectionDate, final User objWfInitiator, final String docNumber,
            final String modifyRsn) {

        final BasicProperty basicProperty = basicProperty2;

        basicProperty.setAllChangesCompleted(FALSE);

        LOGGER.debug("initiateModifyWfForObjection: basicProperty: " + basicProperty);

        final PropertyImpl oldProperty = (PropertyImpl) basicProperty.getProperty();
        final PropertyImpl newProperty = (PropertyImpl) oldProperty.createPropertyclone();

        LOGGER.debug("initiateModifyWfForObjection: oldProperty: " + oldProperty + ", newProperty: " + newProperty);
        final List floorProxy = new ArrayList();

        for (final Floor floor : newProperty.getPropertyDetail().getFloorDetails())
            if (floor != null) {
                basicPropertyService.applyAuditing(floor);
                floor.setPropertyDetail(newProperty.getPropertyDetail());
                floorProxy.add(floor);
            }
        newProperty.getPropertyDetail().setFloorDetails(floorProxy);
        if (newProperty.getPropertyDetail().getPropertyOccupation() != null)
            newProperty.getPropertyDetail().getPropertyOccupation().getId().toString();
        if (newProperty.getPropertyDetail().getPropertyUsage() != null)
            newProperty.getPropertyDetail().getPropertyUsage().getId().toString();

        newProperty.setStatus(STATUS_WORKFLOW);
        newProperty.setBasicProperty(basicProperty);

        newProperty.getPtDemandSet().clear();
        basicProperty.addProperty(newProperty);

        if (!newProperty.getPropertyDetail().getPropertyTypeMaster().getCode()
                .equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
        }

        LOGGER.debug("Exiting from creteNewPropertyForObjectionWorkflow");
        return newProperty;
    }

    /**
     * Returns property completion date based on the property type
     *
     * @param basicProperty
     * @param newProperty
     * @return
     */
    private Date getPropertyCompletionDate(final BasicProperty basicProperty, final PropertyImpl newProperty) {
        LOGGER.debug("Entered into getPropertyCompletionDate - basicProperty.upicNo=" + basicProperty.getUpicNo());
        Date propCompletionDate = null;
        final String propertyTypeMasterCode = newProperty.getPropertyDetail().getPropertyTypeMaster().getCode();
        if (propertyTypeMasterCode.equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
            for (final PropertyStatusValues propstatval : basicProperty.getPropertyStatusValuesSet())
                if (propstatval.getExtraField1() != null)
                    try {
                        propCompletionDate = dateFormatter.parse(propstatval.getExtraField1());
                    } catch (final ParseException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                else
                    propCompletionDate = basicProperty.getPropOccupationDate();
        } else {
            final List<Floor> floorList = newProperty.getPropertyDetail().getFloorDetails();
            propCompletionDate = getLowestDtOfCompFloorWise(floorList);
            if (propCompletionDate == null)
                propCompletionDate = basicProperty.getPropOccupationDate();
        }

        LOGGER.debug("propCompletionDate=" + propCompletionDate + "\nExiting from getPropertyCompletionDate");
        return propCompletionDate;
    }

    public PersistenceService getPropPerServ() {
        return propPerServ;
    }

    public void setPropPerServ(final PersistenceService propPerServ) {
        this.propPerServ = propPerServ;
    }

    public APTaxCalculator getTaxCalculator() {
        return taxCalculator;
    }

    public void setTaxCalculator(final APTaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    public PropertyTaxUtil getPropertyTaxUtil() {
        return propertyTaxUtil;
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public void setBasicPropertyService(final PersistenceService<BasicProperty, Long> basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    /**
     * setting property status values to Basic Property
     *
     * @param basicProperty
     */
    public void setWFPropStatValActive(final BasicProperty basicProperty) {
        LOGGER.debug("Entered into setWFPropStatValActive, basicProperty: " + basicProperty);
        for (final PropertyStatusValues psv : basicProperty.getPropertyStatusValuesSet()) {
            if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equals(psv.getPropertyStatus().getStatusCode())
                    && psv.getIsActive().equals("W")) {
            	updateWFStatusValues(basicProperty,PROPERTY_MODIFY_REASON_ADD_OR_ALTER);
            }
            if (PROPERTY_MODIFY_REASON_AMALG.equals(psv.getPropertyStatus().getStatusCode())
                    && psv.getIsActive().equals("W")) {
            	updateWFStatusValues(basicProperty,PROPERTY_MODIFY_REASON_AMALG);
            }
            if (PROPERTY_MODIFY_REASON_BIFURCATE.equals(psv.getPropertyStatus().getStatusCode())
                    && psv.getIsActive().equals("W")) {
            	 updateWFStatusValues(basicProperty,PROPERTY_MODIFY_REASON_BIFURCATE);
            }
            if (REVISIONPETITION_STATUS_CODE.equals(psv.getPropertyStatus().getStatusCode())
                    && psv.getIsActive().equals("W")) {
                updateWFStatusValues(basicProperty,REVISIONPETITION_STATUS_CODE);
            }
            if (PROP_CREATE_RSN.equals(psv.getPropertyStatus().getStatusCode()) && psv.getIsActive().equals("W"))
                psv.setIsActive("Y");
        }
        LOGGER.debug("Exitinf from setWFPropStatValActive");
    }

	private void updateWFStatusValues(final BasicProperty basicProperty, String statusCode) {
		final PropertyStatusValues activePropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
		        QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "Y",
		        statusCode);
		if (activePropStatVal != null)
		    activePropStatVal.setIsActive("N");
		final PropertyStatusValues wfPropStatVal = (PropertyStatusValues) propPerServ.findByNamedQuery(
		        QUERY_PROPSTATVALUE_BY_UPICNO_CODE_ISACTIVE, basicProperty.getUpicNo(), "W",
		        statusCode);
		if (wfPropStatVal != null)
		    wfPropStatVal.setIsActive("Y");
	}

    /**
     * Prepares a map of installment and respective reason wise demand for each installment
     *
     * @param property
     * @return Map of installment and respective reason wise demand for each installment
     */
    public Map<Installment, Map<String, BigDecimal>> populateTaxesForVoucherCreation(final Property property) {
        LOGGER.debug("Entered into populateTaxesForVoucherCreation, property: " + property);
        Map<Installment, Map<String, BigDecimal>> amounts = new HashMap<Installment, Map<String, BigDecimal>>();
        if (instTaxMap != null) {
            /*
             * for (Map.Entry<Installment, TaxCalculationInfo> instTaxRec : instTaxMap.entrySet()) { Map<String, BigDecimal>
             * taxMap = taxCalculator.getMiscTaxesForProp(instTaxRec.getValue() .getConsolidatedUnitTaxCalculationInfo());
             * amounts.put(instTaxRec.getKey(), taxMap); }
             */
        } else
            amounts = prepareRsnWiseDemandForOldProp(property);
        LOGGER.debug("amounts: " + amounts + "\nExiting from populateTaxesForVoucherCreation");
        return amounts;
    }

    /**
     * Prepares a map of installment and respective reason wise demand for each installment
     *
     * @param property
     * @return Map of installment and respective reason wise demand for each installment
     */
    public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForOldProp(final Property property) {
        LOGGER.debug("Entered into prepareRsnWiseDemandForOldProp, property: " + property);
        Installment inst = null;
        final Map<Installment, Map<String, BigDecimal>> instWiseDmd = new HashMap<Installment, Map<String, BigDecimal>>();
        for (final Ptdemand ptdemand : property.getPtDemandSet())
            if (ptdemand.getIsHistory().equals("N")) {
                inst = ptdemand.getEgInstallmentMaster();
                final Map<String, BigDecimal> rsnWiseDmd = new HashMap<String, BigDecimal>();
                for (final EgDemandDetails dmdDet : ptdemand.getEgDemandDetails())
                    if (inst.equals(dmdDet.getEgDemandReason().getEgInstallmentMaster()))
                        if (!dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                .equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
                                && !dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                                        .equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY))
                            rsnWiseDmd.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode(),
                                    dmdDet.getAmount());
                instWiseDmd.put(inst, rsnWiseDmd);
            }
        LOGGER.debug("Exiting from prepareRsnWiseDemandForOldProp");
        return instWiseDmd;
    }

    /**
     * Prepares a map of installment and respective reason wise demand for each installment
     *
     * @param property
     * @return
     */
    public Map<Installment, Map<String, BigDecimal>> prepareRsnWiseDemandForPropToBeDeactivated(final Property property) {
        LOGGER.debug("Entered into prepareRsnWiseDemandForPropToBeDeactivated, property: " + property);

        final Map<Installment, Map<String, BigDecimal>> amts = prepareRsnWiseDemandForOldProp(property);
        for (final Installment inst : amts.keySet())
            for (final String dmdRsn : amts.get(inst).keySet())
                amts.get(inst).put(dmdRsn, amts.get(inst).get(dmdRsn).negate());
        LOGGER.debug("amts: " + amts + "\n Exiting from prepareRsnWiseDemandForPropToBeDeactivated");
        return amts;
    }

    /**
     * <p>
     * Adjusts the excess collection amount to Demand Details
     * </p>
     * Ex: if there is excess collection for GEN_TAX then adjustments happens from beginning installment to current installment if
     * still there is excess collecion remaining then it will be adjust to the group to which GEN_TAX belongs.
     *
     * @param installments
     * @param newDemandDetailsByInstallment
     */
    public void adjustExcessCollectionAmount(final List<Installment> installments,
            final Map<Installment, Set<EgDemandDetails>> newDemandDetailsByInstallment, Ptdemand ptDemand) {
        LOGGER.info("Entered into adjustExcessCollectionAmount");
        LOGGER.info("adjustExcessCollectionAmount: installments - " + installments
                + ", newDemandDetailsByInstallment.size - " + newDemandDetailsByInstallment.size());

        final Set<String> demandReasons = new LinkedHashSet<String>(Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
                DEMANDRSN_CODE_GENERAL_TAX,
                DEMANDRSN_CODE_VACANT_TAX, DEMANDRSN_CODE_EDUCATIONAL_CESS, DEMANDRSN_CODE_LIBRARY_CESS,
                DEMANDRSN_CODE_UNAUTHORIZED_PENALTY));

        if (!excessCollAmtMap.isEmpty()) {
            adjustCollection(installments, newDemandDetailsByInstallment, demandReasons, ptDemand);
        }

        LOGGER.info("Excess collection adjustment is successfully completed..");
        LOGGER.debug("Exiting from adjustExcessCollectionAmount");
    }

    /**
     * Adjusts Collection amount to installment wise demand details
     *
     * @param installments
     * @param newDemandDetailsByInstallment
     * @param demandReasons
     * @param ptDemand
     * @return
     */
    private Ptdemand adjustCollection(final List<Installment> installments,
            final Map<Installment, Set<EgDemandDetails>> newDemandDetailsByInstallment,
            final Set<String> demandReasons, Ptdemand ptDemand) {

        LOGGER.info("Entered into adjustCollection");
        EgDemandDetails advanceDemandDetails = null;
        BigDecimal balanceDemand = BigDecimal.ZERO;
        BigDecimal excessCollection = BigDecimal.ZERO;
        for (Map<String, BigDecimal> map : excessCollAmtMap.values()) {
            for (BigDecimal amount : map.values()) {
                excessCollection = excessCollection.add(amount);
            }
        }
        Installment currSecondHalf = propertyTaxUtil.getInstallmentsForCurrYear(new Date()).get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);
        if (excessCollection.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal collection = BigDecimal.ZERO;
            for (EgDemandDetails demandDetials : ptDemand.getEgDemandDetails()) {
                if (advanceDemandDetails == null && DEMANDRSN_CODE_ADVANCE.equals(demandDetials.getEgDemandReason().getEgDemandReasonMaster().getCode())
                        && currSecondHalf.equals(demandDetials.getEgDemandReason().getEgInstallmentMaster())) {
                    advanceDemandDetails = demandDetials;
                }
                if(!demandDetials.getEgDemandReason().getEgDemandReasonMaster().getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)){
	                collection = collection.add(demandDetials.getAmtCollected());
	                demandDetials.setAmtCollected(BigDecimal.ZERO);
                }
            }
            collection = collection.add(excessCollection);
            for (final Installment installment : installments) {
                for (String demandReason : demandReasons) {
                    final EgDemandDetails newDemandDetail = getEgDemandDetailsForReason(
                            newDemandDetailsByInstallment.get(installment), demandReason);

                    if (newDemandDetail != null) {
                        balanceDemand = newDemandDetail.getAmount().subtract(newDemandDetail.getAmtCollected());

                        if (balanceDemand.compareTo(BigDecimal.ZERO) > 0) {
                            if (collection.compareTo(BigDecimal.ZERO) > 0) {
                                if (collection.compareTo(balanceDemand) <= 0) {
                                    newDemandDetail.setAmtCollected(newDemandDetail.getAmtCollected().add(collection));
                                    newDemandDetail.setModifiedDate(new Date());
                                    collection = BigDecimal.ZERO;
                                } else {
                                    newDemandDetail.setAmtCollected(newDemandDetail.getAmtCollected().add(balanceDemand));
                                    newDemandDetail.setModifiedDate(new Date());
                                    collection = collection.subtract(balanceDemand);
                                }
                            }
                        }
                    }
                    if (collection.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }
                }
                if (collection.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }

            if (collection.compareTo(BigDecimal.ZERO) > 0) {
                if (advanceDemandDetails == null) {
                    EgDemandDetails demandDetails = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_ADVANCE,
                            collection,
                            currSecondHalf);
                    ptDemand.getEgDemandDetails().add(demandDetails);
                } else {
                    advanceDemandDetails.getAmtCollected().add(collection);
                }
            }
            LOGGER.info("Exiting from adjustCollection");

        }
        return ptDemand;
    }

    /**
     * Initiates data entry workflow
     *
     * @param basicProperty
     * @param initiater
     */
    public void initiateDataEntryWorkflow(BasicProperty basicProperty, final User initiater) {
        LOGGER.debug("Entered into initiateDataEntryWorkflow");

        final PropertyImpl oldProperty = (PropertyImpl) basicProperty.getProperty();
        final PropertyImpl newProperty = (PropertyImpl) oldProperty.createPropertyclone();

        // Setting the property state to the objection workflow initiator
        final Position owner = eisCommonsService.getPositionByUserId(initiater.getId());
        final String desigName = propertyTaxUtil.getDesignationName(initiater.getId());
        final String value = WFLOW_ACTION_NAME_MODIFY + ":" + desigName + "_" + WF_STATE_APPROVAL_PENDING;

        newProperty.transition(true).start().withSenderName(initiater.getName())
                .withComments(PROPERTY_WORKFLOW_STARTED).withStateValue(value).withOwner(owner)
                .withDateInfo(new Date());

        final PropertyMutationMaster propMutMstr = (PropertyMutationMaster) getPropPerServ().find(
                "from PropertyMutationMaster PM where upper(PM.code) = ?", PROPERTY_MODIFY_REASON_DATA_ENTRY);
        newProperty.getPropertyDetail().setPropertyMutationMaster(propMutMstr);
        newProperty.setStatus(PropertyTaxConstants.STATUS_WORKFLOW);
        basicProperty.addProperty(newProperty);

        basicProperty.addPropertyStatusValues(createPropStatVal(basicProperty, PROPERTY_MODIFY_REASON_ADD_OR_ALTER,
                getPropertyCompletionDate(basicProperty, newProperty), null, null, null, null));

        basicProperty = basicPropertyService.update(basicProperty);
        LOGGER.debug("Exiting from initiateDataEntryWorkflow");

    }

    /**
     * Changes property details
     *
     * @param modProperty
     * @param propDetail
     * @param numOfFloors
     * @return
     */
    public PropertyImpl changePropertyDetail(final PropertyImpl modProperty, final PropertyDetail propDetail,
            final Integer numOfFloors) {

        LOGGER.debug("Entered into changePropertyDetail, Property is Vacant Land");

        final PropertyDetail propertyDetail = modProperty.getPropertyDetail();

        propDetail.setSitalArea(propertyDetail.getSitalArea());
        propDetail.setTotalBuiltupArea(propertyDetail.getTotalBuiltupArea());
        propDetail.setCommBuiltUpArea(propertyDetail.getCommBuiltUpArea());
        propDetail.setPlinthArea(propertyDetail.getPlinthArea());
        propDetail.setCommVacantLand(propertyDetail.getCommVacantLand());
        propDetail.setCurrentCapitalValue(propertyDetail.getCurrentCapitalValue());
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

        if (numOfFloors == 0)
            propDetail.setPropertyUsage(propertyDetail.getPropertyUsage());
        else
            propDetail.setPropertyUsage(null);

        propDetail.setManualAlv(propertyDetail.getManualAlv());
        propDetail.setOccupierName(propertyDetail.getOccupierName());

        modProperty.setPropertyDetail(propDetail);

        LOGGER.debug("Exiting from changePropertyDetail");
        return modProperty;
    }

    public List<DocumentType> getDocumentTypesForTransactionType(final TransactionType transactionType) {
        return documentTypePersistenceService.findAllByNamedQuery(DocumentType.DOCUMENTTYPE_BY_TRANSACTION_TYPE,
                transactionType);
    }

    /**
     * Stores Documents
     *
     * @param documents
     */
    public void processAndStoreDocument(final List<Document> documents) {
        documents.forEach(document -> {
            if (!(document.getUploads().isEmpty() || document.getUploadsContentType().isEmpty())) {
                int fileCount = 0;
                for (final File file : document.getUploads()) {
                    final FileStoreMapper fileStore = fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), FILESTORE_MODULE_NAME);
                    document.getFiles().add(fileStore);
                }
            }
            if (document.getId() == null || document.getType() == null)
                document.setType(documentTypePersistenceService.load(document.getType().getId(), DocumentType.class));
        });
    }

    /**
     * Creates or Updates Application index
     *
     * @param stateAwareObject
     * @param applictionType
     */
    public void updateIndexes(final StateAware stateAwareObject, final String applictionType) {
        final Position position = stateAwareObject.getState().getOwnerPosition();
        User user;
        if (position == null)
            user = stateAwareObject.getState().getCreatedBy();
        else
            user = assignmentService.getAssignmentsForPosition(position.getId(), new Date()).get(0).getEmployee();
		if (!applictionType.isEmpty() && (applictionType.equalsIgnoreCase(APPLICATION_TYPE_NEW_ASSESSENT)
				|| applictionType.equalsIgnoreCase(APPLICATION_TYPE_ALTER_ASSESSENT)
				|| applictionType.equalsIgnoreCase(APPLICATION_TYPE_BIFURCATE_ASSESSENT))) {
            final PropertyImpl property = (PropertyImpl) stateAwareObject;
            ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(property
                    .getApplicationNo());
            User owner = property.getBasicProperty().getPrimaryOwner();
            String source = getApplicationSource(property.getBasicProperty());
            if (applicationIndex==null) {
                applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                        .withApplicationNumber(property.getApplicationNo()).withApplicationDate(property.getCreatedDate()!=null?property.getCreatedDate():new Date())
                        .withApplicationType(applictionType).withApplicantName(owner.getName())
                        .withStatus(property.getState().getValue()).withUrl(format(APPLICATION_VIEW_URL, property.getApplicationNo(), applictionType))
                        .withApplicantAddress(property.getBasicProperty().getAddress().toString()).withOwnername(user.getUsername() + "::" + user.getName())
                        .withChannel(source).withMobileNumber(owner.getMobileNumber())
                        .withAadharNumber(owner.getAadhaarNumber()).withConsumerCode(property.getBasicProperty().getUpicNo())
                        .withClosed(property.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO)
                        .withApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:property.getState().getValue().contains(WF_STATE_REJECTED)||property.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS)
                        .build();

                applicationIndexService.createApplicationIndex(applicationIndex);
            } else {
                applicationIndex.setStatus(property.getState().getValue());
                if (applictionType.equalsIgnoreCase(APPLICATION_TYPE_NEW_ASSESSENT)
                        || applictionType.equalsIgnoreCase(APPLICATION_TYPE_ALTER_ASSESSENT) || applictionType
                        .equalsIgnoreCase(APPLICATION_TYPE_BIFURCATE_ASSESSENT)|| applictionType.equalsIgnoreCase(APPLICATION_TYPE_GRP)) {
                    applicationIndex.setConsumerCode(property.getBasicProperty().getUpicNo());
                    applicationIndex.setApplicantName(owner.getName());
                    applicationIndex.setOwnerName(user.getUsername()+"::"+user.getName());
                    applicationIndex.setMobileNumber(owner.getMobileNumber());
                    applicationIndex.setAadharNumber(owner.getAadhaarNumber());
                    applicationIndex.setClosed(property.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO);
                    applicationIndex.setApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:property.getState().getValue().contains(WF_STATE_REJECTED)||property.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS);
                }
                applicationIndexService.updateApplicationIndex(applicationIndex);
            }

        } else if (!applictionType.isEmpty() && (applictionType.equalsIgnoreCase(APPLICATION_TYPE_REVISION_PETITION) || applictionType.equalsIgnoreCase(APPLICATION_TYPE_GRP))) {
            final RevisionPetition property = (RevisionPetition) stateAwareObject;
            ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(property
                    .getObjectionNumber());
            String source = getApplicationSource(property.getBasicProperty());
            if (applicationIndex == null) {
                User owner = property.getBasicProperty().getPrimaryOwner();
                applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                        .withApplicationNumber(property.getObjectionNumber()).withApplicationDate(property.getCreatedDate()!=null?property.getCreatedDate():new Date())
                        .withApplicationType(applictionType).withApplicantName(owner.getName())
                        .withStatus(property.getState().getValue()).withUrl(format(APPLICATION_VIEW_URL, property.getObjectionNumber(), applictionType))
                        .withApplicantAddress(property.getBasicProperty().getAddress().toString()).withOwnername(user.getUsername() + "::" + user.getName())
                        .withChannel(source).withMobileNumber(owner.getMobileNumber())
                        .withAadharNumber(owner.getAadhaarNumber()).withConsumerCode(property.getBasicProperty().getUpicNo())
                        .withClosed(property.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO)
                        .withApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:property.getState().getValue().contains(WF_STATE_REJECTED)||property.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS)
                        .build();
                applicationIndexService.createApplicationIndex(applicationIndex);
            } else {
                applicationIndex.setStatus(property.getState().getValue());
                applicationIndex.setOwnerName(user.getUsername()+"::"+user.getName());
                applicationIndex.setClosed(property.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO);
                applicationIndex.setApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:property.getState().getValue().contains(WF_STATE_REJECTED)||property.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS);
                applicationIndexService.updateApplicationIndex(applicationIndex);
            }

        } else if (!applictionType.isEmpty() && applictionType.equalsIgnoreCase(APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP)) {
            final PropertyMutation property = (PropertyMutation) stateAwareObject;
            ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(property
                    .getApplicationNo());
            User owner = property.getBasicProperty().getPrimaryOwner();
            String source = getApplicationSource(property.getBasicProperty());
            if (applicationIndex == null) {
                applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                        .withApplicationNumber(property.getApplicationNo()).withApplicationDate(property.getCreatedDate()!=null?property.getCreatedDate():new Date())
                        .withApplicationType(applictionType).withApplicantName(owner.getName())
                        .withStatus(property.getState().getValue()).withUrl(format(APPLICATION_VIEW_URL, property.getApplicationNo(), applictionType))
                        .withApplicantAddress(property.getBasicProperty().getAddress().toString()).withOwnername(user.getUsername() + "::" + user.getName())
                        .withChannel(source).withMobileNumber(owner.getMobileNumber())
                        .withAadharNumber(owner.getAadhaarNumber()).withConsumerCode(property.getBasicProperty().getUpicNo())
                        .withClosed(property.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO)
                        .withApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:property.getState().getValue().contains(WF_STATE_REJECTED)||property.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS)
                        .build();
                applicationIndexService.createApplicationIndex(applicationIndex);
            } else {
                applicationIndex.setStatus(property.getState().getValue());
                applicationIndex.setApplicantName(owner.getName());
                applicationIndex.setOwnerName(user.getUsername()+"::"+user.getName());
                applicationIndex.setMobileNumber(owner.getMobileNumber());
                applicationIndex.setAadharNumber(owner.getAadhaarNumber());
                applicationIndex.setClosed(property.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO);
                applicationIndex.setApproved(property.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:property.getState().getValue().contains(WF_STATE_REJECTED)||property.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS);
                applicationIndexService.updateApplicationIndex(applicationIndex);
            }

        }else if (!applictionType.isEmpty() && applictionType.equalsIgnoreCase(APPLICATION_TYPE_VACANCY_REMISSION)) {
            final VacancyRemission vacancyRemission = (VacancyRemission) stateAwareObject;
            ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(vacancyRemission.getApplicationNumber());
            User owner = vacancyRemission.getBasicProperty().getPrimaryOwner();
            VacancyRemissionApproval vacancyRemissionApproval = vacancyRemission.getVacancyRemissionApproval().get(0);
            String source = getApplicationSource(vacancyRemission.getBasicProperty());
            if (applicationIndex == null) {
                applicationIndex = ApplicationIndex.builder().withModuleName(PTMODULENAME)
                        .withApplicationNumber(vacancyRemission.getApplicationNumber()).withApplicationDate(vacancyRemission.getCreatedDate()!=null?vacancyRemission.getCreatedDate():new Date())
                        .withApplicationType(applictionType).withApplicantName(owner.getName())
                        .withStatus(vacancyRemissionApproval.getState().getValue()).withUrl(format(APPLICATION_VIEW_URL, vacancyRemission.getApplicationNumber(), applictionType))
                        .withApplicantAddress(vacancyRemission.getBasicProperty().getAddress().toString()).withOwnername(user.getUsername() + "::" + user.getName())
                        .withChannel(source).withMobileNumber(owner.getMobileNumber())
                        .withAadharNumber(owner.getAadhaarNumber()).withConsumerCode(vacancyRemission.getBasicProperty().getUpicNo())
                        .withClosed(vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO)
                        .withApproved(vacancyRemissionApproval.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:vacancyRemissionApproval.getState().getValue().contains(WF_STATE_REJECTED)||vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS)
                        .build();
                applicationIndexService.createApplicationIndex(applicationIndex);
            } else {
                applicationIndex.setStatus(vacancyRemissionApproval.getState().getValue());
                applicationIndex.setApplicantName(owner.getName());
                applicationIndex.setOwnerName(user.getUsername()+"::"+user.getName());
                applicationIndex.setMobileNumber(owner.getMobileNumber());
                applicationIndex.setAadharNumber(owner.getAadhaarNumber());
                applicationIndex.setClosed(vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)?ClosureStatus.YES:ClosureStatus.NO);
                applicationIndex.setApproved(vacancyRemissionApproval.getState().getValue().contains(WF_STATE_COMMISSIONER_APPROVED)?ApprovalStatus.APPROVED:vacancyRemissionApproval.getState().getValue().contains(WF_STATE_REJECTED)||vacancyRemissionApproval.getState().getValue().contains(WF_STATE_CLOSED)?ApprovalStatus.REJECTED:ApprovalStatus.INPROGRESS);
                applicationIndexService.updateApplicationIndex(applicationIndex);
            }

        }

    }

    private String getApplicationSource(final BasicProperty property) {
        String source;
        if(SOURCEOFDATA_MEESEWA.equals(property.getSource()))
            source= Source.MEESEVA.toString();
        else if(SOURCEOFDATA_ESEVA.equals(property.getSource()))
            source=Source.ESEVA.toString();
        else if (SOURCEOFDATA_MOBILE.equals(property.getSource()))
            source=Source.MOBILE.toString();
        else
            source=Source.SYSTEM.toString();
        return source;
    }

    /**
     * Returns whether assessment has demand dues or not
     *
     * @param assessmentNo
     * @return
     */
    public Boolean hasDemandDues(final String assessmentNo) {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final BigDecimal currentWaterTaxDue = getWaterTaxDues(assessmentNo);
        final Map<String, BigDecimal> propertyTaxDetails = getCurrentPropertyTaxDetails(basicProperty
                .getActiveProperty());
        final BigDecimal currentPropertyTaxDue = propertyTaxDetails.get(CURR_DMD_STR).subtract(
                propertyTaxDetails.get(CURR_COLL_STR));
        final BigDecimal arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                propertyTaxDetails.get(ARR_COLL_STR));
        return currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0;
    }

    /**
     * Returns Water tax due of an assessment
     *
     * @param assessmentNo
     * @return
     */
    public BigDecimal getWaterTaxDues(final String assessmentNo) {
        final String wtmsRestURL = format(WTMS_TAXDUE_RESTURL,
                WebUtils.extractRequestDomainURL(ServletActionContext.getRequest(), false), assessmentNo);
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get("totalTaxDue")));
    }

    /**
     * Returns Water tax due of an assessment
     *
     * @param assessmentNo
     * @param request
     * @return
     */
    public BigDecimal getWaterTaxDues(final String assessmentNo, final HttpServletRequest request) {
        final String wtmsRestURL = format(PropertyTaxConstants.WTMS_TAXDUE_RESTURL, WebUtils.extractRequestDomainURL(request, false),
                assessmentNo);
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get("totalTaxDue")));
    }

    /**
     * Method to validate bifurcation of property either using create assessment or alter assessment
     *
     * @param propertyModel model object
     * @param basicProperty basic property of the property which is being bifurcated
     * @param reason Reason for creation or Modification
     * @return
     */
    public String validationForBifurcation(final PropertyImpl propertyModel, final BasicProperty basicProperty,
            final String reason) {
        final List<PropertyStatusValues> children = propertyStatusValuesDAO    
                .getPropertyStatusValuesByReferenceBasicProperty(basicProperty);
        final Boolean parentBifurcated = isPropertyBifurcated(basicProperty);
        final Boolean childrenCreated = !children.isEmpty();
        String errorMsg = null;
        /**
         * Reason For Modification is Bifurcation of Assessment
         */
        if (PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(reason)) {
            if (parentBifurcated && !childrenCreated)
                errorMsg = "error.child.not.created";
            // commented as child property extent of site can be greater than parent property
            /*
             * else errorMsg = validateArea(propertyModel, basicProperty.getActiveProperty(), children);
             */
        }
        /**
         * Reason For Modification is Alteration of Assessment
         */
        else if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equalsIgnoreCase(reason)) {
            if (!childrenCreated) {
                if (parentBifurcated)
                    errorMsg = "error.child.not.created";
            } else if (!parentBifurcated)
                errorMsg = "error.parent.not.bifurcated";
        }
        /**
         * Reason For Creation is Bifurcation of Assessment
         */
        else if (PROP_CREATE_RSN_BIFUR.equals(reason))
            if (parentBifurcated)
                getLatestHistoryProperty(basicProperty.getUpicNo());
            else
                basicProperty.getActiveProperty();
        return errorMsg;
    }

    /**
     * Returns the latest history property of a basic property
     *
     * @param upicNo
     * @return
     */
    public PropertyImpl getLatestHistoryProperty(final String upicNo) {
        final PropertyImpl property = (PropertyImpl) propPerServ
                .find("from PropertyImpl prop where prop.basicProperty.upicNo = ? and prop.status = 'H' order by prop.id desc",
                        upicNo);
        return property;
    }

    /**
     * Tells whether the parent is bifurcated or not
     *
     * @param basicProperty
     * @return
     */
    public Boolean isPropertyBifurcated(final BasicProperty basicProperty) {
        Boolean propBifurcated = Boolean.FALSE;
        for (final Property property : basicProperty.getPropertySet())
            if ((PROPERTY_MODIFY_REASON_BIFURCATE.equalsIgnoreCase(property.getPropertyModifyReason()) || PROP_CREATE_RSN_BIFUR
                    .equalsIgnoreCase(property.getPropertyModifyReason()))
                    && !(STATUS_WORKFLOW.equals(property.getStatus()) || STATUS_CANCELLED.equals(property.getStatus()))) {
                propBifurcated = Boolean.TRUE;
                break;
            }
        return propBifurcated;
    }

    /**
     * Converting sqr yards to sqr meters
     *
     * @param vacantLandArea
     * @return
     */
    public BigDecimal convertYardToSquareMeters(final Float vacantLandArea) {
        Float areaInSqMts = null;
        areaInSqMts = new Float(vacantLandArea) * new Float(SQUARE_YARD_TO_SQUARE_METER_VALUE);
        return new BigDecimal(areaInSqMts).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns the proper area value for bifurcation calculation
     *
     * @param area
     * @param property
     * @return
     */
    public BigDecimal getPropertyArea(BigDecimal area, final Property property) {
        final PropertyDetail propertyDetail = property.getPropertyDetail();
        if (propertyDetail.isAppurtenantLandChecked() != null && propertyDetail.isAppurtenantLandChecked())
            area = area.add(BigDecimal.valueOf(propertyDetail.getExtentAppartenauntLand()));
        else
            area = area.add(BigDecimal.valueOf(propertyDetail.getSitalArea().getArea()));
        return area;
    }

    /**
     * Checks whether user is an employee or not
     *
     * @param user
     * @return
     */
    public Boolean isEmployee(final User user) {
        for (final Role role : user.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles())
                if (role != null && role.getName().equals(appconfig.getValue()))
                    return false;
        return true;
    }

    /**
     * Checks whether user is an employee or not
     *
     * @param user
     * @return
     */
    public Boolean isMeesevaUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(MEESEVA_OPERATOR_ROLE))
                return true;
        return false;
    }

    /**
     * Getting User assignment based on designation ,department and zone boundary Reading Designation and Department from
     * appconfig values and Values should be 'Senior Assistant,Junior Assistant' for designation and
     * 'Revenue,Accounts,Administration' for department
     *
     * @param basicProperty
     * @return
     */
    public Assignment getUserPositionByZone(final BasicProperty basicProperty, boolean isForMobile) {
        final String designationStr = getDesignationForThirdPartyUser(isForMobile);
        final String departmentStr = getDepartmentForWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<Assignment>();
        for (final String dept : department) {
            for (final String desg : designation) {
                assignment = assignmentService.findByDepartmentDesignationAndBoundary(departmentService
                        .getDepartmentByName(dept).getId(), designationService.getDesignationByName(desg).getId(),
                        basicProperty.getPropertyID().getElectionBoundary().getId());
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    /**
     * Returns Department for property tax workflow
     *
     * @return
     */
    public String getDepartmentForWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PROPERTYTAX_WORKFLOWDEPARTEMENT);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    /**
     * Returns Designation for third party user
     *
     * @return
     */
    public String getDesignationForThirdPartyUser(boolean isForMobile) {
    	String appConfigKey;
    	if(isForMobile)
    		appConfigKey = PT_WORKFLOWDESIGNATION_MOBILE;
    	else
    		appConfigKey = PROPERTYTAX_WORKFLOWDESIGNATION;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
        		appConfigKey);
        return null != appConfigValue ? appConfigValue.get(0).getValue() : null;
    }

    /**
     * Returns third party user roles
     *
     * @return
     */
    public List<AppConfigValues> getThirdPartyUserRoles() {

        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                PTMODULENAME, PROPERTYTAX_ROLEFORNONEMPLOYEE);

        return !appConfigValueList.isEmpty() ? appConfigValueList : null;

    }

    /**
     * @param fromDemand
     * @param toDemand
     * @return List of property having demand between fromDemand and toDemand
     */
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByDemand(final String fromDemand, final String toDemand) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select distinct pmv from PropertyMaterlizeView pmv where pmv.aggrCurrFirstHalfDmd is not null and pmv.aggrCurrFirstHalfDmd>=:fromDemand ")
                .append("and pmv.aggrCurrFirstHalfDmd<=:toDemand and pmv.isActive = true ");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        query.setBigDecimal("fromDemand", new BigDecimal(fromDemand));
        query.setBigDecimal("toDemand", new BigDecimal(toDemand));

        final List<PropertyMaterlizeView> propertyList = query.list();
        return propertyList;
    }
    /**
     * @param assessmentNum,ownerName,doorNo
     * @return List of property matching the input params
    */
   @SuppressWarnings("unchecked")
   public List<PropertyMaterlizeView> getPropertyByAssessmentAndOwnerDetails(final String assessmentNum, final String oldMuncipalNum, final String ownerName,final String doorNo) {
       final StringBuilder queryStr = new StringBuilder();
       queryStr.append("select distinct pmv from PropertyMaterlizeView pmv ").append(
               " where pmv.isActive = true ");
       if (assessmentNum != null && !assessmentNum.trim().isEmpty())
           queryStr.append(" and pmv.propertyId=:assessmentNum ");
      
       if (oldMuncipalNum != null && !oldMuncipalNum.trim().isEmpty())
           queryStr.append(" and pmv.oldMuncipalNum=:oldMuncipalNum ");
       
       if (ownerName != null && !ownerName.trim().isEmpty())
           queryStr.append(" and upper(trim(pmv.ownerName)) like :OwnerName ");
       if (doorNo != null && !doorNo.trim().isEmpty())
           queryStr.append(" and pmv.houseNo like :HouseNo ");
       final Query query = propPerServ.getSession().createQuery(queryStr.toString());
       if (assessmentNum != null && !assessmentNum.trim().isEmpty())
           query.setString("assessmentNum", assessmentNum);
       
       if (oldMuncipalNum != null && !oldMuncipalNum.trim().isEmpty())
           query.setString("oldMuncipalNum", oldMuncipalNum);
       
       if (doorNo != null && !doorNo.trim().isEmpty())
           query.setString("HouseNo", doorNo + "%");
       if (ownerName != null && !ownerName.trim().isEmpty())
           query.setString("OwnerName", "%" + ownerName.toUpperCase() + "%");

       final List<PropertyMaterlizeView> propertyList = query.list();
       return propertyList;
   }

    /**
     * @param locationId
     * @param houseNo
     * @param ownerName
     * @return List of property matching the input params
     */
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByLocation(final Integer locationId, final String houseNo,
            final String ownerName) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select distinct pmv from PropertyMaterlizeView pmv ").append(
                " where pmv.locality.id=:locationId and pmv.isActive = true ");
        if (houseNo != null && !houseNo.trim().isEmpty())
            queryStr.append("and pmv.houseNo like :HouseNo ");
        if (ownerName != null && !ownerName.trim().isEmpty())
            queryStr.append("and upper(trim(pmv.ownerName)) like :OwnerName");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        query.setLong("locationId", locationId);
        if (houseNo != null && !houseNo.trim().isEmpty())
            query.setString("HouseNo", houseNo + "%");
        if (ownerName != null && !ownerName.trim().isEmpty())
            query.setString("OwnerName", ownerName.toUpperCase() + "%");

        final List<PropertyMaterlizeView> propertyList = query.list();
        return propertyList;
    }

    /**
     * @param zoneId
     * @param wardId
     * @param ownerName
     * @param houseNum
     * @return List of property matching the input params
     */
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByBoundary(final Long zoneId, final Long wardId,
            final String ownerName, final String houseNum) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select distinct pmv from PropertyMaterlizeView pmv, BasicPropertyImpl bp where pmv.basicPropertyID=bp.id ")
                .append("and bp.active='Y' ");
        if (null != zoneId && zoneId != -1)
            queryStr.append(" and pmv.zone.id=:ZoneID");
        if (null != wardId && wardId != -1)
            queryStr.append(" and pmv.ward.id=:WardID");
        if (houseNum != null && !houseNum.trim().isEmpty())
            queryStr.append(" and pmv.houseNo like :HouseNo ");
        if (ownerName != null && !ownerName.trim().isEmpty())
            queryStr.append(" and upper(trim(pmv.ownerName)) like :OwnerName");

        final Query query = propPerServ.getSession().createQuery(queryStr.toString());

        if (null != zoneId && zoneId != -1)
            query.setLong("ZoneID", zoneId);
        if (null != wardId && wardId != -1)
            query.setLong("WardID", wardId);
        if (houseNum != null && !houseNum.trim().isEmpty())
            query.setString("HouseNo", houseNum + "%");
        if (ownerName != null && !ownerName.trim().isEmpty())
            query.setString("OwnerName", ownerName.toUpperCase() + "%");

        final List<PropertyMaterlizeView> propertyList = query.list();
        return propertyList;
    }

    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByDoorNo(final String doorNo) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select distinct pmv from PropertyMaterlizeView pmv where pmv.isActive = true ");
        if (StringUtils.isNotBlank(doorNo))
            queryStr.append("and pmv.houseNo like :doorNo ");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(doorNo))
            query.setString("doorNo", doorNo + "%");
        final List<PropertyMaterlizeView> propertyList = query.list();
        return propertyList;
    }
    
    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByOldMunicipalNo(final String oldMuncipalNum) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select distinct pmv from PropertyMaterlizeView pmv where pmv.isActive = true ");
        if (StringUtils.isNotBlank(oldMuncipalNum))
            queryStr.append("and pmv.oldMuncipalNum=:oldMuncipalNum ");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(oldMuncipalNum))
            query.setString("oldMuncipalNum", oldMuncipalNum );
        final List<PropertyMaterlizeView> propertyList = query.list();
        return propertyList;
    }

    @SuppressWarnings("unchecked")
    public List<PropertyMaterlizeView> getPropertyByMobileNumber(final String MobileNo) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select distinct pmv from PropertyMaterlizeView pmv where pmv.isActive = true ");
        if (StringUtils.isNotBlank(MobileNo))
            queryStr.append("and pmv.mobileNumber =:MobileNo ");
        final Query query = propPerServ.getSession().createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(MobileNo))
            query.setString("MobileNo", MobileNo);
        final List<PropertyMaterlizeView> propertyList = query.list();
        return propertyList;
    }

    public Assignment getWorkflowInitiator(final PropertyImpl property) {
        Assignment wfInitiator;
        if (isEmployee(property.getCreatedBy())){
            if(property.getState() != null  && property.getState().getInitiatorPosition() != null){
                wfInitiator = propertyTaxCommonUtils.getUserAssignmentByPassingPositionAndUser(property
                        .getCreatedBy(),property.getState().getInitiatorPosition());
                if(wfInitiator==null)
                    wfInitiator = assignmentService.getAssignmentsForPosition(property.getState().getInitiatorPosition().getId(), new Date()).get(0);
            }
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(property
                        .getCreatedBy().getId());
        }
        else if (property.getState().getInitiatorPosition() != null){
            wfInitiator = assignmentService.getAssignmentsForPosition(property.getState().getInitiatorPosition().getId(), new Date()).get(0);
            if(wfInitiator==null && !property.getStateHistory().isEmpty())
                wfInitiator = assignmentService.getPrimaryAssignmentForPositon(property.getStateHistory().get(0)
                        .getOwnerPosition().getId());
        }
        else{
            wfInitiator = assignmentService.getPrimaryAssignmentForPositon(property.getState().getOwnerPosition()
                    .getId());
        }
        return wfInitiator;
    }

    public List<Hashtable<String, Object>> populateHistory(final StateAware stateAware) {
        final List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        final Hashtable<String, Object> map = new Hashtable<String, Object>();
        User user = null;
        Position ownerPosition = null;
        if (stateAware.hasState()) {
            State state = stateAware.getCurrentState();
            map.put("date", state.getLastModifiedDate());
            map.put("updatedBy", state.getLastModifiedBy().getUsername() + "::" + state.getLastModifiedBy().getName());
            map.put("status", state.getValue());
            map.put("comments", null != state.getComments() ? state.getComments() : "");
            user = state.getOwnerUser();
            ownerPosition = state.getOwnerPosition();
            if (null != ownerPosition) {
                final User approverUser = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                map.put("user", null != approverUser ? approverUser
                        .getUsername() + "::" + approverUser.getName() : "");
            } else if (null != user)
                map.put("user", user.getUsername() + "::" + user.getName());
            historyTable.add(map);
            List<StateHistory> stateHistory = stateAware.getStateHistory();
            if (null != state.getHistory() && !state.getHistory().isEmpty()) {
                Collections.reverse(stateHistory);
                for (final StateHistory historyState : stateHistory) {
                    final Hashtable<String, Object> HistoryMap = new Hashtable<String, Object>(0);
                    HistoryMap.put("date", historyState.getLastModifiedDate());
                    HistoryMap.put("updatedBy", historyState.getLastModifiedBy().getUsername() + "::"
                            + historyState.getLastModifiedBy().getName());
                    HistoryMap.put("status", historyState.getValue());
                    HistoryMap.put("comments", null != historyState.getComments() ? historyState.getComments() : "");
                    ownerPosition = historyState.getOwnerPosition();
                    user = historyState.getOwnerUser();
                    if (null != ownerPosition) {
                         User approverUser = eisCommonService.getUserForPosition(ownerPosition.getId(), historyState.getLastModifiedDate());
                        HistoryMap.put("user", null != approverUser ? approverUser.getUsername() + "::" + approverUser.getName() : "");
                    } else if (null != user)
                        HistoryMap.put("user", user.getUsername() + "::" + user.getName());
                    historyTable.add(HistoryMap);
                }
            }
        }
        return historyTable;
    }

    public AssessmentDetails loadAssessmentDetails(final BasicProperty basicProperty) {
        final AssessmentDetails assessmentDetail = new AssessmentDetails();
        assessmentDetail.setPropertyID(basicProperty.getUpicNo());
        if (basicProperty.getLatitude() != null && basicProperty.getLongitude() != null) {
            assessmentDetail.setLatitude(basicProperty.getLatitude());
            assessmentDetail.setLongitude(basicProperty.getLongitude());
        }
        assessmentDetail.setFlag(0);
        assessmentDetail.setHouseNo(basicProperty.getAddress().getHouseNoBldgApt());
        assessmentDetail.setPropertyAddress(basicProperty.getAddress().toString());
        final Property property = basicProperty.getProperty();
        final PropertyDetails propertyDetails = new PropertyDetails();
        assessmentDetail.setPropertyDetails(propertyDetails);
        PropertyDetail propertyDetail = null;
        if (property != null) {
            assessmentDetail.setOwnerNames(prepareOwnerInfo(property));
            propertyDetail = property.getPropertyDetail();
            loadPropertyDues(property, assessmentDetail);
        }
        if (null != propertyDetail) {
            assessmentDetail.setBoundaryDetails(prepareBoundaryInfo(basicProperty));
            assessmentDetail.getPropertyDetails().setPropertyType(propertyDetail.getPropertyTypeMaster().getType());
            if (propertyDetail.getPropertyUsage() != null)
                assessmentDetail.getPropertyDetails()
                        .setPropertyUsage(propertyDetail.getPropertyUsage().getUsageName());
            if (null != propertyDetail.getNoofFloors())
                assessmentDetail.getPropertyDetails().setNoOfFloors(propertyDetail.getNoofFloors());
            else
                assessmentDetail.getPropertyDetails().setNoOfFloors(0);
        }
        return assessmentDetail;

    }

    private Set<OwnerName> prepareOwnerInfo(final Property property) {
        final List<PropertyOwnerInfo> propertyOwners = property.getBasicProperty().getPropertyOwnerInfo();
        final Set<OwnerName> ownerNames = new HashSet<OwnerName>(0);
        if (propertyOwners != null && !propertyOwners.isEmpty())
            for (final PropertyOwnerInfo propertyOwner : propertyOwners) {
                final OwnerName ownerName = new OwnerName();
                ownerName.setAadhaarNumber(propertyOwner.getOwner().getAadhaarNumber());
                ownerName.setOwnerName(propertyOwner.getOwner().getName());
                ownerName.setMobileNumber(propertyOwner.getOwner().getMobileNumber());
                ownerName.setEmailId(propertyOwner.getOwner().getEmailId());
                ownerNames.add(ownerName);
            }
        return ownerNames;
    }

    private BoundaryDetails prepareBoundaryInfo(final BasicProperty basicProperty) {
        final BoundaryDetails boundaryDetails = new BoundaryDetails();
        final PropertyID propertyID = basicProperty.getPropertyID();
        if (null != propertyID) {
            if (null != propertyID.getZone()) {
                boundaryDetails.setZoneId(propertyID.getZone().getId());
                boundaryDetails.setZoneNumber(propertyID.getZone().getBoundaryNum());
                boundaryDetails.setZoneName(propertyID.getZone().getName());
                boundaryDetails.setZoneBoundaryType(propertyID.getZone().getBoundaryType().getName());
            }
            if (null != propertyID.getWard()) {
                boundaryDetails.setWardId(propertyID.getWard().getId());
                boundaryDetails.setWardNumber(propertyID.getWard().getBoundaryNum());
                boundaryDetails.setWardName(propertyID.getWard().getName());
                boundaryDetails.setWardBoundaryType(propertyID.getWard().getBoundaryType().getName());
            }
            if (null != propertyID.getElectionBoundary()) {
                boundaryDetails.setAdminWardId(propertyID.getElectionBoundary().getId());
                boundaryDetails.setAdminWardNumber(propertyID.getElectionBoundary().getBoundaryNum());
                boundaryDetails.setAdminWardName(propertyID.getElectionBoundary().getName());
                boundaryDetails.setAdminWardBoundaryType(propertyID.getElectionBoundary().getBoundaryType().getName());
            }
            if (null != propertyID.getArea()) {
                boundaryDetails.setBlockId(propertyID.getArea().getId());
                boundaryDetails.setBlockNumber(propertyID.getArea().getBoundaryNum());
                boundaryDetails.setBlockName(propertyID.getArea().getName());
            }
            if (null != propertyID.getLocality()) {
                boundaryDetails.setLocalityId(propertyID.getLocality().getId());
                boundaryDetails.setLocalityName(propertyID.getLocality().getName());
            }
            if (null != propertyID.getStreet()) {
                boundaryDetails.setStreetId(propertyID.getStreet().getId());
                boundaryDetails.setStreetName(propertyID.getStreet().getName());
            }
        }
        return boundaryDetails;
    }

    private void loadPropertyDues(final Property property, final AssessmentDetails assessmentDetail) {
        final Map<String, BigDecimal> resultmap = ptDemandDAO.getDemandCollMap(property);
        if (null != resultmap && !resultmap.isEmpty()) {
            final BigDecimal currDmd = resultmap.get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR);
            final BigDecimal arrDmd = resultmap.get(PropertyTaxConstants.ARR_DMD_STR);
            final BigDecimal currCollection = resultmap.get(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR);
            final BigDecimal arrColelection = resultmap.get(PropertyTaxConstants.ARR_COLL_STR);

            final BigDecimal taxDue = currDmd.add(arrDmd).subtract(currCollection).subtract(arrColelection);
            assessmentDetail.getPropertyDetails().setTaxDue(taxDue);
            assessmentDetail.getPropertyDetails().setCurrentTax(currDmd);
            assessmentDetail.getPropertyDetails().setArrearTax(arrDmd);
        }
    }

    public Map<String, BigDecimal> getCurrentPropertyTaxDetails(final Property propertyImpl) {
        return ptDemandDAO.getDemandCollMap(propertyImpl);
    }

    /**
     * Returns a map of current tax and balance, based on passed date being in first half or second half of the installments for current year
     * @param propertyTaxDetails
     * @param currDate
     * @return
     */
    public Map<String, BigDecimal> getCurrentTaxAndBalance(Map<String, BigDecimal> propertyTaxDetails, Date currDate){
    	Map<String, BigDecimal> taxValues = new HashMap<String, BigDecimal>();
    	Map<String,Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
    	Installment currInstFirstHalf = currYearInstMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        if(DateUtils.between(new Date(),currInstFirstHalf.getFromDate(),currInstFirstHalf.getToDate())){
        	taxValues.put(PropertyTaxConstants.CURR_DMD_STR, propertyTaxDetails.get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR));
        	taxValues.put(PropertyTaxConstants.CURR_BAL_STR, propertyTaxDetails.get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR)
                    .subtract(propertyTaxDetails.get(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR)));
        }else{
        	taxValues.put(PropertyTaxConstants.CURR_DMD_STR, propertyTaxDetails.get(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR));
        	taxValues.put(PropertyTaxConstants.CURR_BAL_STR, propertyTaxDetails.get(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR)
                    .subtract(propertyTaxDetails.get(PropertyTaxConstants.CURR_SECONDHALF_COLL_STR)));
        }
    	return taxValues;
    }

    /**
     * Returns a map of current tax and balance, based on passed date being in first half or second half of the installments for current year
     * @param propertyTaxDetails
     * @param currDate
     * @return
     */
    public Map<String, BigDecimal> getCurrentTaxDetails(Map<String, Map<String,BigDecimal>> propertyTaxDetails, Date currDate){
    	Map<String, BigDecimal> taxValues = new HashMap<String, BigDecimal>();
    	Map<String,Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(currDate);
    	Installment currInstFirstHalf = currYearInstMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        if(DateUtils.between(new Date(),currInstFirstHalf.getFromDate(),currInstFirstHalf.getToDate())){
        	getTaxDetails(propertyTaxDetails, taxValues,PropertyTaxConstants.CURRENTYEAR_FIRST_HALF,currInstFirstHalf);
        }else{
        	getTaxDetails(propertyTaxDetails, taxValues,PropertyTaxConstants.CURRENTYEAR_SECOND_HALF,null);
        }
    	return taxValues;
    }

    /**
     * Gives the tax details for the installment
     * @param propertyTaxDetails
     * @param taxValues
     * @param installmentHalf
     * @param currInstFirstHalf
     */
	private void getTaxDetails(Map<String, Map<String, BigDecimal>> propertyTaxDetails, Map<String, BigDecimal> taxValues,
			String installmentHalf, Installment currInstFirstHalf) {
		if(currInstFirstHalf != null){
			taxValues.put(PropertyTaxConstants.CURR_DMD_STR, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR));
			taxValues.put(PropertyTaxConstants.CURR_COLL_STR, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR));
			taxValues.put(PropertyTaxConstants.CURR_BAL_STR, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR)
					.subtract((propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR)));
		}else{
			taxValues.put(PropertyTaxConstants.CURR_DMD_STR, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR));
			taxValues.put(PropertyTaxConstants.CURR_COLL_STR, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_SECONDHALF_COLL_STR));
			taxValues.put(PropertyTaxConstants.CURR_BAL_STR, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_SECONDHALF_DMD_STR)
					.subtract((propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.CURR_SECONDHALF_COLL_STR)));
		}
		if(propertyTaxDetails.get(installmentHalf).get(PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX)!= null)
		    taxValues.put(PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX));
		else
		    taxValues.put(PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX));
		taxValues.put(PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS));
		taxValues.put(PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS));
		taxValues.put(PropertyTaxConstants.DEMANDRSN_STR_UNAUTHORIZED_PENALTY, (propertyTaxDetails.get(installmentHalf)).get(PropertyTaxConstants.DEMANDRSN_STR_UNAUTHORIZED_PENALTY));
		taxValues.put(PropertyTaxConstants.ARR_DMD_STR,(propertyTaxDetails.get(PropertyTaxConstants.ARREARS)).get(PropertyTaxConstants.ARR_DMD_STR));
		taxValues.put(PropertyTaxConstants.ARR_COLL_STR,(propertyTaxDetails.get(PropertyTaxConstants.ARREARS)).get(PropertyTaxConstants.ARR_COLL_STR));
		taxValues.put(PropertyTaxConstants.ARR_BAL_STR,((propertyTaxDetails.get(PropertyTaxConstants.ARREARS)).get(PropertyTaxConstants.ARR_DMD_STR))
				.subtract((propertyTaxDetails.get(PropertyTaxConstants.ARREARS)).get(PropertyTaxConstants.ARR_COLL_STR)));
	}

    /**
     * Calculates penalty for General Revision Petition
     *
     * @param modProperty
     * @param propCompletionDate
     */
    public void calculateGrpPenalty(Property modProperty, Date propCompletionDate) {
        currentInstall = propertyTaxCommonUtils.getCurrentInstallment();
        Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        EgDemand currentDemand = null;
        for (EgDemand egDemand : modProperty.getPtDemandSet()) {
            if (egDemand.getEgInstallmentMaster().equals(currentInstall)) {
                currentDemand = egDemand;
                break;
            }
        }

        Map<Installment, BigDecimal> installmentWiseDemand = getInstallmentWiseDemand(currentDemand);
        Map<Installment, EgDemandDetails> installmentWisePenaltyDemandDetail = penaltyCalculationService
                .getInstallmentWisePenaltyDemandDetails(
                        null, currentDemand);
        List<EgDemandDetails> penaltyList = new ArrayList<>();
        DateTime nagarPanchayatPenDate = DateTime.now().withDate(2016, 1, 1);
        final Installment nagarPanchayatPenEndInstallment = installmentDao.getInsatllmentByModuleForGivenDate(
                module, nagarPanchayatPenDate.toDate());
        BigDecimal tax = BigDecimal.ZERO;
        Installment installment = null;
        BigDecimal excessPenalty = BigDecimal.ZERO;
        for (final Map.Entry<Installment, BigDecimal> mapEntry : installmentWiseDemand.entrySet()) {
            installment = mapEntry.getKey();
            if (installment.getToDate().compareTo(propCompletionDate) >= 0) {
                tax = mapEntry.getValue();
                EgDemandDetails existingPenaltyDemandDetail = installmentWisePenaltyDemandDetail.get(installment);
                Date penaltyEffectiveDate = null;
                if (propertyTaxUtil.checkIsNagarPanchayat() && installment.compareTo(nagarPanchayatPenEndInstallment) <= 0) {
                    penaltyEffectiveDate = nagarPanchayatPenDate.toDate();
                } else {
                    penaltyEffectiveDate = getPenaltyEffectiveDate(installment);
                }
                if (penaltyEffectiveDate.before(new Date())) {
                    BigDecimal penaltyAmount = calculatePenalty(null, penaltyEffectiveDate, tax);
                    if (existingPenaltyDemandDetail == null) {
                        EgDemandDetails penaltyDemandDetails = ptBillServiceImpl.insertDemandDetails(
                                DEMANDRSN_CODE_PENALTY_FINES,
                                penaltyAmount,
                                installment);
                        penaltyList.add(penaltyDemandDetails);
                    } else {
                        if (existingPenaltyDemandDetail.getAmtCollected().compareTo(penaltyAmount) > 0) {
                            excessPenalty = existingPenaltyDemandDetail.getAmtCollected().subtract(penaltyAmount);
                            existingPenaltyDemandDetail.setAmtCollected(penaltyAmount);
                        }
                        existingPenaltyDemandDetail.setAmount(penaltyAmount);
                    }
                }
            }
        }
        currentDemand.getEgDemandDetails().addAll(penaltyList);
        List<Installment> installments = new ArrayList<Installment>(installmentWiseDemand.keySet());
        Collections.sort(installments);
        if (excessPenalty.compareTo(BigDecimal.ZERO) > 0) {
            adjustExcessPenalty(currentDemand, installments, excessPenalty);
        }
    }

    /**
     * Adjusts excess penalty collection amount
     *
     * @param currentDemand
     * @param installments
     * @param excessPenalty
     */
    private void adjustExcessPenalty(EgDemand currentDemand, List<Installment> installments, BigDecimal excessPenalty) {
        final Set<String> demandReasons = new LinkedHashSet<String>(Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
                DEMANDRSN_CODE_GENERAL_TAX,
                DEMANDRSN_CODE_VACANT_TAX, DEMANDRSN_CODE_EDUCATIONAL_CESS, DEMANDRSN_CODE_LIBRARY_CESS,
                DEMANDRSN_CODE_UNAUTHORIZED_PENALTY));
        List<EgDemandDetails> demandDetailsList = new ArrayList<EgDemandDetails>(currentDemand.getEgDemandDetails());
        final Map<Installment, Set<EgDemandDetails>> installmentWiseDemandDetails = getEgDemandDetailsSetAsMap(
                demandDetailsList, installments);
        for (Installment installment : installments) {
            Set<EgDemandDetails> demandDetailsSet = installmentWiseDemandDetails.get(installment);
            for (String demandReason : demandReasons) {
                EgDemandDetails demandDetails = getEgDemandDetailsForReason(demandDetailsSet, demandReason);
                if (demandDetails != null) {
                    BigDecimal balance = demandDetails.getAmount().subtract(demandDetails.getAmtCollected());
                    if (balance.compareTo(BigDecimal.ZERO) > 0) {
                        if (excessPenalty.compareTo(BigDecimal.ZERO) > 0) {
                            if (excessPenalty.compareTo(balance) <= 0) {
                                demandDetails.setAmtCollected(demandDetails.getAmtCollected().add(excessPenalty));
                                demandDetails.setModifiedDate(new Date());
                                excessPenalty = BigDecimal.ZERO;
                            } else {
                                demandDetails.setAmtCollected(demandDetails.getAmtCollected().add(balance));
                                demandDetails.setModifiedDate(new Date());
                                excessPenalty = excessPenalty.subtract(balance);
                            }
                        }
                    }
                }
                if (excessPenalty.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            if (excessPenalty.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }
        if (excessPenalty.compareTo(BigDecimal.ZERO) > 0) {
            EgDemandDetails advanceDemandDetails = ptBillServiceImpl.insertDemandDetails(DEMANDRSN_CODE_ADVANCE, excessPenalty,
                    currentInstall);
            currentDemand.getEgDemandDetails().add(advanceDemandDetails);
        }
    }

    /**
     * Returns installment wise demand amount
     *
     * @param currentDemand
     * @return
     */
    private Map<Installment, BigDecimal> getInstallmentWiseDemand(EgDemand currentDemand) {
        final Map<Installment, BigDecimal> installmentWiseDemand = new TreeMap<Installment, BigDecimal>();
        String demandReason = "";
        Installment installment = null;
        final List<String> demandReasonExcludeList = Arrays
                .asList(DEMANDRSN_CODE_PENALTY_FINES, DEMANDRSN_CODE_ADVANCE);
        for (final EgDemandDetails dmdDet : currentDemand.getEgDemandDetails()) {
            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();
            if (!demandReasonExcludeList.contains(demandReason)) {
                installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                if (installmentWiseDemand.get(installment) == null)
                    installmentWiseDemand.put(installment, dmdDet.getAmount());
                else
                    installmentWiseDemand.put(installment,
                            installmentWiseDemand.get(installment).add(dmdDet.getAmount()));

            }
        }
        return installmentWiseDemand;
    }

    /**
     * Calculates penalty on tax amount
     *
     * @param latestCollReceiptDate
     * @param fromDate
     * @param amount
     * @return
     */
    public BigDecimal calculatePenalty(final Date latestCollReceiptDate, final Date fromDate, final BigDecimal amount) {
        BigDecimal penalty = BigDecimal.ZERO;
        final int noOfMonths = PropertyTaxUtil.getMonthsBetweenDates(fromDate, new Date());
        penalty = amount.multiply(PropertyTaxConstants.PENALTY_PERCENTAGE.multiply(new BigDecimal(noOfMonths))).divide(
                BIGDECIMAL_100);
        return MoneyUtils.roundOff(penalty);
    }

    /**
     * Returns penalty effective date for an installment
     *
     * @param installment
     * @return
     */
    private Date getPenaltyEffectiveDate(final Installment installment) {
        return penalyDateWithThreeMonths(installment.getFromDate());
    }

    /**
     * Returns date by adding three months
     *
     * @param date
     * @return
     */
    private Date penalyDateWithThreeMonths(final Date date) {
        final Calendar penalyDate = Calendar.getInstance();
        penalyDate.setTime(date);
        penalyDate.add(Calendar.MONTH, 3);
        penalyDate.set(Calendar.DAY_OF_MONTH, 1);
        return penalyDate.getTime();
    }

    /**
     * Updates the PropertyDetail for a Property
     * @param property
     * @param floorTypeId
     * @param roofTypeId
     * @param wallTypeId
     * @param woodTypeId
     * @param areaOfPlot
     * @param propertyCategory
     * @param nonResPlotArea
     * @param propUsageId
     * @param propOccId
     * @param propTypeId
     */
    public void updatePropertyDetail(Property property, Long floorTypeId, Long roofTypeId, Long wallTypeId, Long woodTypeId,
    		String areaOfPlot, String propertyCategory, String nonResPlotArea, String propUsageId, String propOccId, String propTypeId) {
    	PropertyDetail propertyDetail = property.getPropertyDetail();
		if (floorTypeId != null && floorTypeId != -1) {
            final FloorType floorType = (FloorType) getPropPerServ().find("From FloorType where id = ?", floorTypeId);
            propertyDetail.setFloorType(floorType);
        }
    	if (roofTypeId != null && roofTypeId != -1) {
            final RoofType roofType = (RoofType) getPropPerServ().find("From RoofType where id = ?", roofTypeId);
            propertyDetail.setRoofType(roofType);
        }
    	if (wallTypeId != null && wallTypeId != -1) {
            final WallType wallType = (WallType) getPropPerServ().find("From WallType where id = ?", wallTypeId);
            propertyDetail.setWallType(wallType);
        }
        if (woodTypeId != null && woodTypeId != -1) {
            final WoodType woodType = (WoodType) getPropPerServ().find("From WoodType where id = ?", woodTypeId);
            propertyDetail.setWoodType(woodType);
        }
        if (areaOfPlot != null && !areaOfPlot.isEmpty()) {
            propertyDetail.getSitalArea().setArea(new Float(areaOfPlot));
        }
        propertyDetail.setCategoryType(propertyDetail.getCategoryType());

        if (propertyDetail.getApartment() != null
                && propertyDetail.getApartment().getId() != null) {
            final Apartment apartment = (Apartment) getPropPerServ().find("From Apartment where id = ?",
                    property.getPropertyDetail().getApartment().getId());
            propertyDetail.setApartment(apartment);
        }

        if (nonResPlotArea != null && !nonResPlotArea.isEmpty()) {
        	propertyDetail.getNonResPlotArea().setArea(new Float(nonResPlotArea));
        }

        propertyDetail.setFieldVerified('Y');
        propertyDetail.setProperty(property);
        final PropertyMutationMaster propMutMstr = (PropertyMutationMaster) getPropPerServ().find(
                "from PropertyMutationMaster PM where upper(PM.code) = ?", property.getBasicProperty().getPropertyMutationMaster().getCode());
        final PropertyTypeMaster propTypeMstr = (PropertyTypeMaster) getPropPerServ().find(
                "from PropertyTypeMaster PTM where PTM.id = ?", Long.valueOf(propTypeId));
        if (propUsageId != null) {
            final PropertyUsage usage = (PropertyUsage) getPropPerServ().find("from PropertyUsage pu where pu.id = ?",
                    Long.valueOf(propUsageId));
            propertyDetail.setPropertyUsage(usage);
        }
        if (propOccId != null) {
            final PropertyOccupation occupancy = (PropertyOccupation) getPropPerServ().find(
                    "from PropertyOccupation po where po.id = ?", Long.valueOf(propOccId));
            propertyDetail.setPropertyOccupation(occupancy);
        }
        if (propTypeMstr.getCode().equals(OWNERSHIP_TYPE_VAC_LAND))
        	propertyDetail.setPropertyType(VACANT_PROPERTY);
        else
        	propertyDetail.setPropertyType(BUILT_UP_PROPERTY);

        propertyDetail.setPropertyTypeMaster(propTypeMstr);
        propertyDetail.setPropertyMutationMaster(propMutMstr);
        propertyDetail.setUpdatedTime(new Date());

        if (propertyDetail.getPropertyTypeMaster().getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND)) {
        	propertyDetail.setNoofFloors(0);
        	if(!property.getPropertyDetail().getFloorDetails().isEmpty())
        		property.getPropertyDetail().getFloorDetails().clear();
            property.getPropertyDetail().getTotalBuiltupArea().setArea(new Float(0));
        }
	}

    /**
     * Update the Floor details for a property
     * @param property
     * @param savedFloorDetails
     */
    public void updateFloorDetails(Property property, List<Floor> savedFloorDetails){
    	PropertyTypeMaster unitType = null;
        PropertyUsage usage = null;
        PropertyOccupation occupancy = null;
        StructureClassification structureClass = null;
        final Area totBltUpArea = new Area();
        Float totBltUpAreaVal = new Float(0);
    	for(Floor floorProxy : property.getPropertyDetail().getFloorDetailsProxy()){
    		for(Floor savedFloor : savedFloorDetails){
    			if(floorProxy != null && savedFloor != null){
	        		if(floorProxy.getFloorUid().equals(savedFloor.getFloorUid())){
	        			totBltUpAreaVal = totBltUpAreaVal + floorProxy.getBuiltUpArea().getArea();
	        			//set all fields for each floor, if UID matches
	        			if (floorProxy.getUnitType() != null)
	                        unitType = (PropertyTypeMaster) getPropPerServ().find(
	                                "from PropertyTypeMaster utype where utype.id = ?", floorProxy.getUnitType().getId());
	                    if (floorProxy.getPropertyUsage() != null)
	                        usage = (PropertyUsage) getPropPerServ().find("from PropertyUsage pu where pu.id = ?",
	                        		floorProxy.getPropertyUsage().getId());
	                    if (floorProxy.getPropertyOccupation() != null)
	                        occupancy = (PropertyOccupation) getPropPerServ().find(
	                                "from PropertyOccupation po where po.id = ?", floorProxy.getPropertyOccupation().getId());

	                    if (floorProxy.getStructureClassification() != null)
	                        structureClass = (StructureClassification) getPropPerServ().find(
	                                "from StructureClassification sc where sc.id = ?",
	                                floorProxy.getStructureClassification().getId());
	                    if (floorProxy.getOccupancyDate() != null && floorProxy.getConstructionDate() != null)
	                    	savedFloor.setDepreciationMaster(propertyTaxUtil.getDepreciationByDate(floorProxy.getConstructionDate(),floorProxy.getOccupancyDate()));

	                    if (unitType != null
	                            && unitType.getCode().equalsIgnoreCase(PropertyTaxConstants.UNITTYPE_OPEN_PLOT))
	                    	savedFloor.setFloorNo(OPEN_PLOT_UNIT_FLOORNUMBER);

	                    savedFloor.setUnitType(unitType);
	                    savedFloor.setPropertyUsage(usage);
	                    savedFloor.setPropertyOccupation(occupancy);
	                    savedFloor.setStructureClassification(structureClass);
	                    savedFloor.setPropertyDetail(property.getPropertyDetail());
	                    savedFloor.setModifiedDate(new Date());
	                    final User user = userService.getUserById(ApplicationThreadLocals.getUserId());
	                    savedFloor.setModifiedBy(user);
	                    savedFloor.getBuiltUpArea().setArea(floorProxy.getBuiltUpArea().getArea());
	                    savedFloor.getBuiltUpArea().setLength(floorProxy.getBuiltUpArea().getLength());
	                    savedFloor.getBuiltUpArea().setBreadth(floorProxy.getBuiltUpArea().getLength());
	                    savedFloor.setFirmName(floorProxy.getFirmName());
	                    // setting total builtup area.
	                    totBltUpArea.setArea(totBltUpAreaVal);
	                    totBltUpArea.setLength(floorProxy.getBuiltUpArea().getLength());
	                    totBltUpArea.setBreadth(floorProxy.getBuiltUpArea().getBreadth());
	                    property.getPropertyDetail().setTotalBuiltupArea(totBltUpArea);

	        		}
    			}
    			property.getPropertyDetail().setNoofFloors(property.getPropertyDetail().getFloorDetailsProxy().size());
        	}
    	}
    }

    /**
     * Convert string to date
     * @param dateInString
     * @return
     * @throws ParseException
     */
    public Date convertStringToDate(final String dateInString) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final Date stringToDate = sdf.parse(dateInString);
        return stringToDate;
    }

    /**
     * Fetch the assignments for the designation
     * @param designationName
     * @return
     */
    public List<Assignment> getAssignmentsForDesignation(String designationName){
        List<Assignment> assignmentsList = new ArrayList<Assignment>();
        assignmentsList = assignmentService.findPrimaryAssignmentForDesignationName(designationName);
        return assignmentsList;
    }

    /**
     * Update Reference Basic Property in Property Status values (Bifurcation workflow)
     * @param basicProperty, parentPropId
     */
    public void updateReferenceBasicProperty(final BasicProperty basicProperty, final String parentPropId ){

        PropertyStatusValues propStatVal= (PropertyStatusValues) propPerServ.find("from PropertyStatusValues psv where psv.basicProperty=? order by createdDate desc", basicProperty);
        final BasicProperty referenceBasicProperty = (BasicProperty) propPerServ.find(
                "from BasicPropertyImpl bp where bp.upicNo=?", parentPropId);
        if(referenceBasicProperty != null)
        	propStatVal.setReferenceBasicProperty(referenceBasicProperty);

    }
    /**
     * Returns Water connection details of an assessment
     *
     * @param assessmentNo
     * @param request
     * @return
     */
    public List<Map<String,Object>> getWCDetails(final String assessmentNo, final HttpServletRequest request) {
        final List<Map<String,Object>> waterConnDtls = new ArrayList<>();
        String url= request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        final String wtmsRestURL = String.format(PropertyTaxConstants.WTMS_CONNECT_DTLS_RESTURL,host,assessmentNo);
        final String dtls = simpleRestClient.getRESTResponse(wtmsRestURL);
        JSONArray jsonArr = null;
        ArrayList<String> nameList = new ArrayList<>(); 
        try {
            jsonArr = new JSONArray(dtls);
        } catch (JSONException e1) {
            LOGGER.error("Error in converting string into json array " +e1);
        }
        
        for (int i = 0; i < jsonArr.length(); i++)
        {
            
            try {
                Map<String,Object> newMap = new HashMap<>();
                org.json.JSONObject jsonObj = jsonArr.getJSONObject(i);
                JSONArray names=jsonObj.names();
                if (names != null) { 
                    for (int n=0;n<names.length();n++){ 
                        nameList.add(names.get(n).toString());
                    } 
                 } 
                for(String str : nameList){
                 if(!("propertyID").equals(str)){
                 newMap.put(str,jsonObj.get(str).toString().toLowerCase());
                 }
                }
                waterConnDtls.add(newMap);
            } catch (JSONException e) {
                LOGGER.error("Error in converting json array into json object " +e);
            }
        }
        return waterConnDtls;
    }
    
    /**
     * Method to get total property tax due
     * @param basicProperty
     * @return Total property tax due
     */
    public BigDecimal getTotalPropertyTaxDue(final BasicProperty basicProperty) {
        final Map<String, BigDecimal> propertyTaxDetails = ptDemandDAO.getDemandCollMap(basicProperty.getProperty());
        Map<String, BigDecimal> currentTaxAndDue = getCurrentTaxAndBalance(propertyTaxDetails,
                new Date());
        BigDecimal currentPropertyTaxDue = currentTaxAndDue.get(CURR_BAL_STR);
        BigDecimal arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                propertyTaxDetails.get(ARR_COLL_STR));
        return currentPropertyTaxDue.add(arrearPropertyTaxDue);
    }
    
    public Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays.asList(files).stream().filter(file -> !file.isEmpty()).map(file -> {
                try {
                    return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                            file.getContentType(), PropertyTaxConstants.FILESTORE_MODULE_NAME);
                } catch (final Exception e) {
                    throw new ApplicationRuntimeException("err.input.stream", e);
                }
            }).collect(Collectors.toSet());
        else
            return Collections.emptySet();
    }
    
    public BigDecimal getTotalPropertyTaxDueIncludingPenalty(final BasicProperty basicProperty) {
        final Map<String, BigDecimal> propertyTaxDetails = ptDemandDAO.getDemandIncludingPenaltyCollMap(basicProperty.getProperty());
        BigDecimal crntFirstHalfTaxDue = propertyTaxDetails.get(CURR_FIRSTHALF_DMD_STR).subtract(
                propertyTaxDetails.get(CURR_FIRSTHALF_COLL_STR));
        BigDecimal crntSecondHalfTaxDue = propertyTaxDetails.get(CURR_SECONDHALF_DMD_STR).subtract(
                propertyTaxDetails.get(CURR_SECONDHALF_COLL_STR));
        BigDecimal arrearPropertyTaxDue = propertyTaxDetails.get(ARR_DMD_STR).subtract(
                propertyTaxDetails.get(ARR_COLL_STR));
        BigDecimal totalBalance = crntFirstHalfTaxDue.add(crntSecondHalfTaxDue).add(arrearPropertyTaxDue);
        return totalBalance;
    }
    
    /**
     * Method to get children created for property
     * @param basicProperty
     * @return List<PropertyStatusValues>
     */
    public List<PropertyStatusValues> findChildrenForProperty(final BasicProperty basicProperty) {
        return propertyStatusValuesDAO.getPropertyStatusValuesByReferenceBasicProperty(basicProperty);
    }
    
    public Assignment getUserOnRejection(final StateAware stateAware) {
        List<StateHistory> history = stateAware.getStateHistory();
        Collections.reverse(history);
        Assignment userAssignment = null;
        boolean exists = false;
        for(StateHistory state: history) {   
            List<Assignment> assignments = assignmentService.getAssignmentsForPosition(state.getOwnerPosition().getId());
            for(Assignment assignment:assignments) {
            if(assignment != null && assignment.getDesignation().getName().equals(PropertyTaxConstants.REVENUE_INSPECTOR_DESGN)) {
                userAssignment = assignment;
                exists = true;
            }
           }
            if(exists) break;
        }
        return userAssignment;
    }
    
    public String getDesignationForPositionAndUser(Long positionId,Long userId) {
        List<Assignment> assignment = assignmentService.getAssignmentByPositionAndUserAsOnDate(positionId, userId, new Date());
        return (!assignment.isEmpty()) ? assignment.get(0).getDesignation().getName() : null; 
    }
    
    public Map<Installment, Map<String, BigDecimal>> getExcessCollAmtMap() {
        return excessCollAmtMap;
    }

    public void setExcessCollAmtMap(final Map<Installment, Map<String, BigDecimal>> excessCollAmtMap) {
        this.excessCollAmtMap = excessCollAmtMap;
    }

    public EisCommonsService getEisCommonsService() {
        return eisCommonsService;
    }

    public void setEisCommonsService(final EisCommonsService eisCommonsService) {
        this.eisCommonsService = eisCommonsService;
    }

    public BigDecimal getTotalAlv() {
        return totalAlv;
    }

    public void setTotalAlv(final BigDecimal totalAlv) {
        this.totalAlv = totalAlv;
    }
}